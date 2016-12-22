package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.TransformEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.OperationHelper
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{ Column, DataFrame, Row }
import org.apache.spark.sql.types.StructType
import hydrograph.engine.transformation.userfunctions.base.TransformBase
import hydrograph.engine.spark.core.reusablerow.InputReusableRow
import hydrograph.engine.spark.core.reusablerow.OutputReusableRow
import hydrograph.engine.spark.core.reusablerow.RowToReusableMapper
import scala.collection.JavaConverters._

class ToBeTransformComponent(transformEntity: TransformEntity, componentsParams: BaseComponentParams) extends OperationComponentBase with OperationHelper[TransformBase] with Serializable {

  val outSocketEntity = transformEntity.getOutSocketList().get(0)
  val outputFields: List[String] = outSocketEntity.getAllOutputFields.asScala.toList
  val numOutCols = outputFields.size
  val inputSchema: StructType = componentsParams.getDataFrame.schema
  val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())
  val inSocketId: String = transformEntity.getInSocketList.get(0).getInSocketId
  val mapFields = outSocketEntity.getMapFieldsList.asScala.toList
  val passthroughFields = outSocketEntity.getPassThroughFieldsList.asScala.toList

  val mapFieldIndexes = getIndexes(inputSchema, outputSchema, getMapSourceFields(mapFields, inSocketId), getMapTargetFields(mapFields, inSocketId))
  val passthroughIndexes = getIndexes(inputSchema, outputSchema, getPassthroughSourceFields(passthroughFields, inSocketId))
  val enc = RowEncoder(outputSchema)
  val inDf = componentsParams.getDataFrame

  //-----
  val transformsList = initializeTransformList(transformEntity.getOperationsList, inputSchema, outputSchema)

  //transformsList.foreach { sparkOperation => sparkOperation.baseClassInstance.prepare(sparkOperation.operationEntity.getOperationProperties, sparkOperation.operationEntity.getOperationInputFields, sparkOperation.operationEntity.getOperationOutputFields) }
  val outVals = new Array[Any](numOutCols)
  val rr = transformsList(0).outputRow.setRow(outVals)
  val outRow = Row.fromSeq(outVals)
  rr.setField(0, 5)
  outVals(0) = 23.asInstanceOf[Long]
  outVals(1) = "tempCity"
  outVals(2) = 25528742.asInstanceOf[Long]

  //-----
  override def createComponent(): Map[String, DataFrame] = {

    //inDf.createOrReplaceTempView("tmp")
    //val odf = inDf.sparkSession.sql("select id, city, number, length(name) name_length from tmp")

    val odf = inDf.mapPartitions(itr => itr.map(row => outRow))(enc)

    val key = transformEntity.getOutSocketList.get(0).getSocketId
    Map(key -> odf)
  }

}
