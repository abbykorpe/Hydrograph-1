package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.TransformEntity
import hydrograph.engine.core.component.utils.OperationUtils
import hydrograph.engine.expression.userfunctions.TransformForExpression
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.OperationHelper
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.TransformBase
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

class TransformComponent(transformEntity: TransformEntity, componentsParams: BaseComponentParams) extends OperationComponentBase with OperationHelper[TransformBase] with Serializable {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[TransformComponent])
  val outSocketEntity = transformEntity.getOutSocketList().get(0)

  val inputSchema: StructType = componentsParams.getDataFrame.schema
  val outputFields = OperationUtils.getAllFields(transformEntity.getOutSocketList, inputSchema.map(_.name).asJava).asScala
    .toList
  val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())
  val inSocketId: String = transformEntity.getInSocketList.get(0).getInSocketId
  val mapFields = outSocketEntity.getMapFieldsList.asScala.toList
  val passthroughFields: Array[String] = OperationUtils.getPassThrougFields(outSocketEntity.getPassThroughFieldsList,
    inputSchema
      .map
      (_.name).asJava).asScala.toArray[String]

  val mapFieldIndexes = getIndexes(inputSchema, outputSchema, getMapSourceFields(mapFields, inSocketId), getMapTargetFields(mapFields, inSocketId))
  val passthroughIndexes = getIndexes(inputSchema, outputSchema, passthroughFields)


  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")
    val df = componentsParams.getDataFrame.mapPartitions(itr => {
      //Initialize Transform to call prepare Method
      val transformsList = initializeOperationList[TransformForExpression](transformEntity.getOperationsList, inputSchema, outputSchema)

      transformsList.foreach {
        sparkOperation =>
          sparkOperation.baseClassInstance match {
            //For Expression Editor call extra method setValidationAPI
            case t: TransformForExpression => t.setValidationAPI(sparkOperation.validatioinAPI)
            case t: TransformBase => t.prepare(sparkOperation.operationEntity.getOperationProperties, sparkOperation
              .operationEntity.getOperationInputFields, sparkOperation.operationEntity.getOperationOutputFields)
          }
      }

      itr.map(row => {
        val outRow = new Array[Any](outputFields.size)
        //Map Fields
        copyFields(row, outRow, mapFieldIndexes)
        //Passthrough Fields
        copyFields(row, outRow, passthroughIndexes)
        transformsList.foreach { tr =>
          //Calling Transform Method
          tr.baseClassInstance.transform(tr.inputRow.setRow(row), tr.outputRow.setRow(outRow))
          if (itr.isEmpty) {
            LOG.info("Calling cleanup() method of " + tr.baseClassInstance.getClass.toString + " class.")
            tr.baseClassInstance.cleanup()
          }
        }
        Row.fromSeq(outRow)
      })

    })(RowEncoder(outputSchema))

    val key = transformEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)
  }

}
