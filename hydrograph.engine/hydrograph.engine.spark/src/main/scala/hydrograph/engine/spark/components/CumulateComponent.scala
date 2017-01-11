package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.CumulateEntity
import hydrograph.engine.core.component.entity.elements.KeyField
import hydrograph.engine.core.component.utils.OperationUtils
import hydrograph.engine.expression.userfunctions.CumulateForExpression
import hydrograph.engine.expression.utils.ExpressionWrapper
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.{OperationHelper, SparkOperation}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.EncoderHelper
import hydrograph.engine.transformation.userfunctions.base.CumulateTransformBase
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Column, Row, _}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * Created by vaijnathp on 12/13/2016.
  */
class CumulateComponent(cumulateEntity: CumulateEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with  OperationHelper[CumulateTransformBase] with Serializable {

  val key = cumulateEntity.getOutSocketList.get(0).getSocketId
  val LOG = LoggerFactory.getLogger(classOf[FilterComponent])
  val outSocketEntity = cumulateEntity.getOutSocketList.get(0)
  val inputSchema: StructType = componentsParams.getDataFrame().schema
  val outputFields = OperationUtils.getAllFields(cumulateEntity.getOutSocketList, inputSchema.map(_.name).asJava).asScala
    .toList
  val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())
  val inSocketId: String = cumulateEntity.getInSocketList.get(0).getInSocketId
  val mapFields = outSocketEntity.getMapFieldsList.asScala.toList
  val passthroughFields: Array[String] = OperationUtils.getPassThrougFields(outSocketEntity.getPassThroughFieldsList,
    inputSchema
      .map
      (_.name).asJava).asScala.toArray[String]
  val mapFieldIndexes = getIndexes(inputSchema, outputSchema, getMapSourceFields(mapFields, inSocketId), getMapTargetFields(mapFields, inSocketId))
  val passthroughIndexes = getIndexes(inputSchema, outputSchema, passthroughFields)
  val keyFields = cumulateEntity.getKeyFields.map(_.getName)
  val keyFieldsIndexes = getIndexes(inputSchema, keyFields)


  override def createComponent(): Map[String, DataFrame] = {

    if (LOG.isTraceEnabled) LOG.trace(cumulateEntity.toString)

    for ( outSocket <- cumulateEntity.getOutSocketList().asScala) {
      LOG.info("Creating cumulate assembly for '"
        + cumulateEntity.getComponentId() + "' for socket: '"
        + outSocket.getSocketId() + "' of type: '"
        + outSocket.getSocketType() + "'")
    }

    val outRow = new Array[Any](outputFields.size)

    val primaryKeys = if (cumulateEntity.getKeyFields == null) Array[KeyField]() else cumulateEntity.getKeyFields
    val secondaryKeys = if (cumulateEntity.getSecondaryKeyFields == null) Array[KeyField]() else cumulateEntity.getSecondaryKeyFields

    val sourceDf = componentsParams.getDataFrame()

    val repartitionedDf = if (primaryKeys.isEmpty) sourceDf.repartition(1) else sourceDf.repartition(primaryKeys.map { field => col(field.getName) }: _*)

    LOG.info("Data is being Partitioned on keys Fields:{} ", populateSortKeys(primaryKeys ++ secondaryKeys))
    val sortedDf = repartitionedDf.sortWithinPartitions(populateSortKeys(primaryKeys ++ secondaryKeys): _*)


    LOG.info("Cumulate Operation Started....")
    val outputDf = sortedDf.mapPartitions(itr => {

      //Initialize Cumulate to call prepare Method
      val cumulateList: List[SparkOperation[CumulateTransformBase]] = initializeOperationList[CumulateForExpression](cumulateEntity
        .getOperationsList,
        inputSchema,
        outputSchema)

      cumulateList.foreach(sparkOperation => {
        sparkOperation.baseClassInstance match {
          //For Expression Editor call extra methods
          case a: CumulateForExpression =>
            a.setValidationAPI(new ExpressionWrapper(sparkOperation.validatioinAPI, sparkOperation.initalValue))
            a.callPrepare
          case a: CumulateTransformBase => a.prepare(sparkOperation.operationEntity.getOperationProperties, sparkOperation
            .operationEntity.getOperationInputFields, sparkOperation.operationEntity.getOperationOutputFields, keyFields)
        }
      })

      var prevKeysArray: Array[Any] = null


      itr.map {
        row => {
          val currKeysArray: Array[Any] = new Array[Any](cumulateEntity.getKeyFields.size)
          copyFields(row, currKeysArray, keyFieldsIndexes)
          val isPrevKeyDifferent: Boolean = if (prevKeysArray == null)
            true
          else if (!((prevKeysArray.size == currKeysArray.size) && prevKeysArray.zip(currKeysArray).forall(p => p._1 == p._2)))
            true
          else false

          val isPrevKeyNull: Boolean = prevKeysArray == null
          val isEndOfIterator: Boolean = itr.isEmpty
          prevKeysArray = currKeysArray

          if ((isPrevKeyDifferent && !isPrevKeyNull) ) {
            //Calling OnCompleteGroup
            cumulateList.foreach(cmt => {
              cmt.baseClassInstance.onCompleteGroup()
            })
          }
          //Map Fields
          copyFields(row, outRow, mapFieldIndexes)
          //Passthrough Fields
          copyFields(row, outRow, passthroughIndexes)

          cumulateList.foreach(cmt => {
            //Calling Cumulate Method
            cmt.baseClassInstance.cumulate(cmt.inputRow.setRow(row),cmt.outputRow.setRow(outRow))
          })

          if (isEndOfIterator) {
            //Calling OnCompleteGroup
            cumulateList.foreach(cmt => {
              cmt.baseClassInstance.onCompleteGroup()
            })
          }
          Row.fromSeq(outRow)
        }
      }
    })(RowEncoder(outputSchema))
    Map(key -> outputDf)
  }

  def populateSortKeys (keysArray: Array[KeyField] ): Array[Column] = {
    keysArray.map {
      field => if (field.getSortOrder.toLowerCase == "desc") (col (field.getName).desc) else (col (field.getName) )
    }
  }
}
