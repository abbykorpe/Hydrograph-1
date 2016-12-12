package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.TransformEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.TransformOperation
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, Row}

/**
  * Created by gurdits on 10/18/2016.
  */
class SparkTransformComponent(transformEntity: TransformEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with TransformOperation with Serializable  {

  override def createComponent(): Map[String, DataFrame] = {

    val op = OperationSchemaCreator[TransformEntity](transformEntity, componentsParams, transformEntity.getOutSocketList().get(0))
    val fm = FieldManupulating(op.getOperationInputFields(), op.getOperationOutputFields(), op.getPassThroughFields()
      , op.getMapFields(), op.getOperationFields(), null)

    val inputColumn = new Array[Column](fm.getinputFields().size)
    fm.getinputFields().zipWithIndex.foreach(f => {
      inputColumn(f._2) = col(f._1)
    })

    val df = componentsParams.getDataFrame.select(inputColumn: _*).mapPartitions(itr => {
      //Initialize Transform to call prepare Method
      val transformsList = initializeTransform(transformEntity.getOperationsList,fm,op.getExpressionObject)

      val it = itr.map(row => {
        val outRow = new Array[Any](fm.getOutputFields().size)
        //Map Fields
        RowHelper.setTupleFromRow(outRow, fm.determineMapSourceFieldsPos(), row, fm.determineMapTargetFieldsPos())
        //Passthrough Fields
        RowHelper.setTupleFromRow(outRow, fm.determineInputPassThroughFieldsPos(), row, fm.determineOutputPassThroughFieldsPos())
        transformsList.foreach { tr =>
          //Calling Transform Method
          tr.baseClassInstance.transform(RowHelper.convertToReusebleRow(tr.inputFieldPositions, row, tr.inputReusableRow), tr
            .outputReusableRow)
          RowHelper.setTupleFromReusableRow(outRow, tr.outputReusableRow, tr.outputFieldPositions)
          //Calling Cleanup Method
          if (itr.isEmpty)
            tr.baseClassInstance.cleanup()
        }
        Row.fromSeq(outRow)
      })
      it
    })(RowEncoder(EncoderHelper().getEncoder(fm.getOutputFields(), componentsParams.getSchemaFields())))

    val key = transformEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)
  }

}
