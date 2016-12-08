package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.AggregateEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.{AggregateOperation}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.{Column, DataFrame, KeyValueGroupedDataset, Row}

import scala.collection.JavaConversions._

/**
  * Created by gurdits on 10/24/2016.
  */
class SparkAggregateComponent(aggregateEntity: AggregateEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase  with AggregateOperation with Serializable{

  override def createComponent(): Map[String, DataFrame] = {

    val op = OperationSchemaCreator[AggregateEntity](aggregateEntity, componentsParams, aggregateEntity.getOutSocketList().get(0))
    val fm = FieldManupulating(op.getOperationInputFields(), op.getOperationOutputFields(), op.getPassThroughFields(), op.getMapFields(), op.getOperationFields(), aggregateEntity.getKeyFields)
    val outRow = new Array[Any](fm.getOutputFields().size)

    val inputColumn = new Array[Column](fm.getinputFields().size)
    fm.getinputFields().zipWithIndex.foreach(f => {
      inputColumn(f._2) = col(f._1)
    })

    val groupedDF = createGroupDataFrame(componentsParams.getDataFrame().select(inputColumn: _*), fm)
    val aggregateList = initializeAggregate(aggregateEntity.getOperationsList, fm)
    val df = groupedDF.mapGroups((r, i) => {
      val iterable = i.toStream

      //Map Fields
      RowHelper.setTupleFromRow(outRow, fm.determineMapSourceFieldsPos(), iterable.get(0), fm.determineMapTargetFieldsPos())
      //Passthrough Fields
      RowHelper.setTupleFromRow(outRow, fm.determineInputPassThroughFieldsPos(), iterable.get(0), fm.determineOutputPassThroughFieldsPos())


          iterable.foreach(row => {
            aggregateList.foreach(agt => {
              //Calling Aggregate Method
              agt.baseClassInstance.aggregate(RowHelper.convertToReusebleRow(agt.inputFieldPositions,
                row, agt.inputReusableRow))
            })
          })
          //Calling OnCompleteGroup
        aggregateList.foreach(agt => {
          agt.baseClassInstance.onCompleteGroup(agt.outputReusableRow)
          RowHelper.setTupleFromReusableRow(outRow, agt.outputReusableRow, agt.outputFieldPositions)
        })

      Row.fromSeq(outRow)
    })(RowEncoder(EncoderHelper().getEncoder(fm.getOutputFields(), componentsParams.getSchemaFields())))

    val key = aggregateEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)
  }

  def createGroupDataFrame(dataFrame: DataFrame, fieldManupulating: FieldManupulating): KeyValueGroupedDataset[Row, Row] = {
    if (fieldManupulating.getKeyFields().size > 0) {
      dataFrame.groupByKey(r => createKeys(r, fieldManupulating))(RowEncoder(EncoderHelper().getEncoder(fieldManupulating.getKeyFields(), componentsParams
        .getSchemaFields())))
    }
    else {
      val schema1 = StructType(List(StructField("groupField", DataTypes.IntegerType, true)))
      dataFrame.groupByKey(r => Row(1))(RowEncoder(schema1))
    }
  }

  def createKeys(row: Row, fieldManupulating: FieldManupulating): Row = {
    if (fieldManupulating.getKeyFields().size > 0) {
      val keyValues = new Array[Any](fieldManupulating.determineKeyFieldPos().size)
      fieldManupulating.determineKeyFieldPos().zipWithIndex.foreach {
        case (v, i) =>
          keyValues(i) = row.get(v)
      }
      Row.fromSeq(keyValues)
    }
    else {
      Row(1)
    }
  }

}
