package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.AggregateEntity
import hydrograph.engine.core.component.entity.elements.{KeyField, Operation}
import hydrograph.engine.core.component.utils.OperationUtils
import hydrograph.engine.expression.userfunctions.AggregateForExpression
import hydrograph.engine.expression.utils.ExpressionWrapper
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.{OperationHelper, SparkOperation}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.AggregateTransformBase
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Column, DataFrame, Row}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/**
  * Created by bitwise on 10/24/2016.
  */
class AggregateComponent(aggregateEntity: AggregateEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with OperationHelper[AggregateTransformBase] with Serializable {

  val outSocketEntity = aggregateEntity.getOutSocketList.get(0)
  val inputSchema: StructType = componentsParams.getDataFrame().schema
  val outputFields = OperationUtils.getAllFields(aggregateEntity.getOutSocketList, inputSchema.map(_.name).asJava).asScala
    .toList
  val fieldsForOPeration = OperationUtils.getAllFieldsWithOperationFields(aggregateEntity, outputFields.toList.asJava)
  val operationSchema: StructType = EncoderHelper().getEncoder(fieldsForOPeration.asScala.toList, componentsParams.getSchemaFields())
  val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())
  val inSocketId: String = aggregateEntity.getInSocketList.get(0).getInSocketId
  val mapFields = outSocketEntity.getMapFieldsList.asScala.toList
  val passthroughFields: Array[String] = OperationUtils.getPassThrougFields(outSocketEntity.getPassThroughFieldsList,
    inputSchema
      .map
      (_.name).asJava).asScala.toArray[String]
  val mapFieldIndexes = getIndexes(inputSchema, outputSchema, getMapSourceFields(mapFields, inSocketId), getMapTargetFields(mapFields, inSocketId))
  val passthroughIndexes = getIndexes(inputSchema, outputSchema, passthroughFields)
  val keyFields = if (aggregateEntity.getKeyFields == null) Array[String]()
  else aggregateEntity.getKeyFields.map(_
    .getName)
  val keyFieldsIndexes = getIndexes(inputSchema, keyFields)

  private val LOG: Logger = LoggerFactory.getLogger(classOf[AggregateComponent])

  def extractInitialValues(getOperationsList: util.List[Operation]): List[String] = {
    LOG.trace("In method extractInitialValues()")
    def extract(operationList: List[Operation], stringList: List[String]): List[String] = (operationList, stringList) match {
      case (List(), _) => stringList
      case (x :: xs, str) => extract(xs, str ++ List(x.getAccumulatorInitialValue))
    }
    if (getOperationsList != null)
      extract(getOperationsList.asScala.toList, List[String]())
    else
      List[String]()
  }

  override def createComponent(): Map[String, DataFrame] = {

    LOG.trace("In method createComponent()")
    //    val op = OperationSchemaCreator[AggregateEntity](aggregateEntity, componentsParams, aggregateEntity.getOutSocketList().get(0))
    //    val fm = FieldManupulating(op.getOperationInputFields(), op.getOperationOutputFields(), op.getPassThroughFields(), op.getMapFields(), op.getOperationFields(), aggregateEntity.getKeyFields)
    val outRow = new Array[Any](operationSchema.size)
    var outRowToReturn = ListBuffer[Row]()
    val nullRow = Iterable(Row.fromSeq(new Array[Any](operationSchema.size)))


    val primaryKeys = if (aggregateEntity.getKeyFields == null) Array[KeyField]() else aggregateEntity.getKeyFields
    val secondaryKeys = if (aggregateEntity.getSecondaryKeyFields == null) Array[KeyField]() else aggregateEntity.getSecondaryKeyFields
    LOG.debug("Component Id: '" + aggregateEntity.getComponentId
      + "' Primary Keys: " + primaryKeys
      + " Secondary Keys: " + secondaryKeys)

    val sourceDf = componentsParams.getDataFrame()

    val repartitionedDf = if (primaryKeys.isEmpty) sourceDf.repartition(1) else sourceDf.repartition(primaryKeys.map { field => col(field.getName) }: _*)

    val sortedDf = repartitionedDf.sortWithinPartitions(populateSortKeys(primaryKeys ++ secondaryKeys): _*)

    val intermediateDf = sortedDf.mapPartitions(itr => {

      //Initialize Aggregarte to call prepare Method
      val aggregateList: List[SparkOperation[AggregateTransformBase]] = initializeOperationList[AggregateForExpression](aggregateEntity
        .getOperationsList,
        inputSchema,
        operationSchema)

      aggregateList.foreach(sparkOperation => {
        sparkOperation.baseClassInstance match {
          //For Expression Editor call extra methods
          case a: AggregateForExpression =>
            a.setValidationAPI(new ExpressionWrapper(sparkOperation.validatioinAPI, sparkOperation.initalValue))
            a.callPrepare
          case a: AggregateTransformBase => a.prepare(sparkOperation.operationEntity.getOperationProperties, sparkOperation
            .operationEntity.getOperationInputFields, sparkOperation.operationEntity.getOperationOutputFields, keyFields)
        }
      })

      var prevKeysArray: Array[Any] = null

      itr.flatMap { row => {
        val currKeysArray: Array[Any] = new Array[Any](primaryKeys.size)
        copyFields(row, currKeysArray, keyFieldsIndexes)
        val isPrevKeyDifferent: Boolean = {
          if (prevKeysArray == null)
            true
          else if (!prevKeysArray.zip(currKeysArray).forall(p => p._1 == p._2))
            true
          else false
        }
        val isPrevKeyNull: Boolean = prevKeysArray == null
        val isEndOfIterator: Boolean = itr.isEmpty
        prevKeysArray = currKeysArray

        if (isPrevKeyDifferent) {
          if (!isPrevKeyNull) {
            //Calling OnCompleteGroup
            aggregateList.foreach(agt => {
              agt.baseClassInstance.onCompleteGroup(agt.outputRow.setRow(outRow))
            })
            outRowToReturn += Row.fromSeq(outRow.clone())
          }

          //Map Fields
          copyFields(row, outRow, mapFieldIndexes)
          //Passthrough Fields
          copyFields(row, outRow, passthroughIndexes)
        }

        aggregateList.foreach(agt => {
          try {
            agt.baseClassInstance.aggregate(agt.inputRow.setRow(row))
          }  catch {
            case e: Exception =>
              LOG.error("Error in aggregate() method of " + agt.baseClassInstance.getClass.toString + " class. Error: " + e.getMessage + "\nRow being processed: " + agt.inputRow)
              throw e
          }
        })

        if (isEndOfIterator) {
         aggregateList.foreach(agt => {
            agt.baseClassInstance.onCompleteGroup(agt.outputRow.setRow(outRow))
          })
          outRowToReturn += Row.fromSeq(outRow.clone())
        }

        if ((isPrevKeyDifferent && (!isPrevKeyNull)) || isEndOfIterator) {
          val tempRow = outRowToReturn;
          outRowToReturn = ListBuffer[Row]();
          tempRow
        }
        else
          nullRow
      }
      }
    })(RowEncoder(operationSchema))

    val outputDf = intermediateDf.na.drop("all")

    val key = aggregateEntity.getOutSocketList.get(0).getSocketId
    Map(key -> outputDf)

  }

  def populateSortKeys(keysArray: Array[KeyField]): Array[Column] = {
    LOG.trace("In method populateSortKeys()")
    keysArray.map { field => if (field.getSortOrder.toLowerCase() == "desc") col(field.getName).desc else col(field.getName) }
  }

}
