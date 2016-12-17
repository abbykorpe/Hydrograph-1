package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.TransformEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.OperationHelper
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, Row}
import org.apache.spark.sql.types.StructType
import hydrograph.engine.transformation.userfunctions.base.TransformBase
import hydrograph.engine.spark.core.reusablerow.InputReusableRow
import hydrograph.engine.spark.core.reusablerow.OutputReusableRow

class UpdatedTransformComponent(transformEntity: TransformEntity, componentsParams: BaseComponentParams) extends OperationComponentBase with OperationHelper[TransformBase] with Serializable {

  val op = OperationSchemaCreator[TransformEntity](transformEntity, componentsParams, transformEntity.getOutSocketList().get(0))
  val fm = FieldManupulating(op.getOperationInputFields(), op.getOperationOutputFields(), op.getPassThroughFields(), op.getMapFields(), op.getOperationFields(), null)
  val inputSchema: StructType = componentsParams.getDataFrame.schema
  val outputSchema: StructType = EncoderHelper().getEncoder(fm.getOutputFields(), componentsParams.getSchemaFields())

  override def createComponent(): Map[String, DataFrame] = {

    val transformsList = initializeTransformList(transformEntity.getOperationsList, inputSchema, outputSchema)

    transformsList.foreach { sparkOperation => sparkOperation.baseClassInstance.prepare(sparkOperation.operationEntity.getOperationProperties, sparkOperation.operationEntity.getOperationInputFields, sparkOperation.operationEntity.getOperationOutputFields) }

    val df = componentsParams.getDataFrame.mapPartitions(itr => {
      //Initialize Transform to call prepare Method

      val it = itr.map(row => {
        val outRow = new Array[Any](fm.getOutputFields().size)
        //Map Fields
        RowHelper.setTupleFromRow(outRow, fm.determineMapSourceFieldsPos(), row, fm.determineMapTargetFieldsPos())
        //Passthrough Fields
        RowHelper.setTupleFromRow(outRow, fm.determineInputPassThroughFieldsPos(), row, fm.determineOutputPassThroughFieldsPos())
        transformsList.foreach { tr =>
          //Calling Transform Method
          tr.baseClassInstance.transform(InputReusableRow(row, tr.inputMapper), OutputReusableRow(outRow, tr.outputMapper))

          //Calling Cleanup Method
          if (itr.isEmpty)
            tr.baseClassInstance.cleanup()
        }
        Row.fromSeq(outRow)
      })
      it
    })(RowEncoder(outputSchema))

    val key = transformEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)
  }

}
