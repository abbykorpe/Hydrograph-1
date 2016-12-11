package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.AggregateEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.{ AggregateOperation }
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{ DataTypes, StructField, StructType }
import org.apache.spark.sql.{ Column, DataFrame, KeyValueGroupedDataset, Row }

import scala.collection.JavaConversions._
import hydrograph.engine.core.component.entity.elements.KeyField
import scala.collection.mutable.ListBuffer

/**
 * Created by gurdits on 10/24/2016.
 */
class SparkAggregateComponent(aggregateEntity: AggregateEntity, componentsParams: BaseComponentParams) extends OperationComponentBase with AggregateOperation with Serializable {

  override def createComponent(): Map[String, DataFrame] = {

    val op = OperationSchemaCreator[AggregateEntity](aggregateEntity, componentsParams, aggregateEntity.getOutSocketList().get(0))
    val fm = FieldManupulating(op.getOperationInputFields(), op.getOperationOutputFields(), op.getPassThroughFields(), op.getMapFields(), op.getOperationFields(), aggregateEntity.getKeyFields)
    val outRow = new Array[Any](fm.getOutputFields().size)

    val inputColumn = new Array[Column](fm.getinputFields().size)
    fm.getinputFields().zipWithIndex.foreach(f => {
      inputColumn(f._2) = col(f._1)
    })

    val primaryKeys = if (aggregateEntity.getKeyFields == null) (Array[KeyField]()) else (aggregateEntity.getKeyFields)
    val secondaryKeys = if (aggregateEntity.getSecondaryKeyFields == null) (Array[KeyField]()) else (aggregateEntity.getSecondaryKeyFields)

    val sourceDf = componentsParams.getDataFrame().select(inputColumn: _*)

    val repartitionedDf = if (primaryKeys.isEmpty) (sourceDf.repartition(1)) else (sourceDf.repartition(primaryKeys.map { field => col(field.getName) }: _*))

    val sortedDf = repartitionedDf.sortWithinPartitions(populateSortKeys(primaryKeys ++ secondaryKeys): _*)

    val outputDf = sortedDf.mapPartitions(itr => {

      //Initialize Aggregarte to call prepare Method
      val aggregateList = initializeAggregate(aggregateEntity.getOperationsList, primaryKeys, fm)
      var prevKeysArray: Array[Any] = null
      val resultRows = ListBuffer[Row]()

      itr.foreach { row =>
        {
          val currKeysArray: Array[Any] = RowHelper.extractKeyFields(row, fm.determineKeyFieldPos())
          val isPrevKeyDifferent: Boolean = {
            if (prevKeysArray == null)
              (true)
            else if (!((prevKeysArray.size == currKeysArray.size) && (prevKeysArray.zip(currKeysArray).forall(p => p._1 == p._2))))
              (true)
            else (false)
          }
          val isPrevKeyNull: Boolean = (prevKeysArray == null)
          val isEndOfIterator: Boolean = itr.isEmpty
          prevKeysArray = currKeysArray

          if (isPrevKeyDifferent) {
            if (!isPrevKeyNull) {
              //Calling OnCompleteGroup
              aggregateList.foreach(agt => {
                agt.baseClassInstance.onCompleteGroup(agt.outputReusableRow)
                RowHelper.setTupleFromReusableRow(outRow, agt.outputReusableRow, agt.outputFieldPositions)
              })
              resultRows += Row.fromSeq(outRow.clone())
            }

            //Map Fields
            RowHelper.setTupleFromRow(outRow, fm.determineMapSourceFieldsPos(), row, fm.determineMapTargetFieldsPos())
            //Passthrough Fields
            RowHelper.setTupleFromRow(outRow, fm.determineInputPassThroughFieldsPos(), row, fm.determineOutputPassThroughFieldsPos())
          }

          aggregateList.foreach(agt => {
            //Calling Aggregate Method
            agt.baseClassInstance.aggregate(RowHelper.convertToReusebleRow(agt.inputFieldPositions, row, agt.inputReusableRow))
          })

          if (isEndOfIterator) {
            //Calling OnCompleteGroup
            aggregateList.foreach(agt => {
              agt.baseClassInstance.onCompleteGroup(agt.outputReusableRow)
              RowHelper.setTupleFromReusableRow(outRow, agt.outputReusableRow, agt.outputFieldPositions)
            })
            resultRows += Row.fromSeq(outRow.clone())
          }
        }
      }

      resultRows.toIterator
    })(RowEncoder(EncoderHelper().getEncoder(fm.getOutputFields(), componentsParams.getSchemaFields())))

    val key = aggregateEntity.getOutSocketList.get(0).getSocketId
    Map(key -> outputDf)

  }

  def populateSortKeys(keysArray: Array[KeyField]): Array[Column] = {
    keysArray.map { field => if (field.getSortOrder == "desc") (col(field.getName).desc) else (col(field.getName)) }
  }

}
