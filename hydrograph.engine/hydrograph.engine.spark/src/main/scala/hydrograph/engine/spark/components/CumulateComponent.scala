package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.CumulateEntity
import hydrograph.engine.core.component.entity.elements.{KeyField, Operation}
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.CumulateOperation
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{EncoderHelper, FieldManupulating, OperationSchemaCreator, RowHelper}
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, Row, _}
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._

/**
  * Created by vaijnathp on 12/13/2016.
  */
class CumulateComponent(cumulateEntity: CumulateEntity, componentsParams: BaseComponentParams) extends OperationComponentBase with CumulateOperation with Serializable {

  val key = cumulateEntity.getOutSocketList.get(0).getSocketId
  val LOG = LoggerFactory.getLogger(classOf[FilterComponent])

  def extractInitialValues(getOperationsList: List[Operation]): List[String] = {
    def extract(operationList: List[Operation], stringList: List[String]): List[String] = (operationList, stringList) match {
      case (List(), _) => stringList
      case (x :: xs, str) => extract(xs, str ++ List(x.getAccumulatorInitialValue))
    }
    extract(getOperationsList, List[String]())
  }

  override def createComponent(): Map[String, DataFrame] = {

    if (LOG.isTraceEnabled) LOG.trace(cumulateEntity.toString)

    for ( outSocket <- cumulateEntity.getOutSocketList().asScala) {
      LOG.info("Creating cumulate assembly for '"
        + cumulateEntity.getComponentId() + "' for socket: '"
        + outSocket.getSocketId() + "' of type: '"
        + outSocket.getSocketType() + "'")
    }
    val op = OperationSchemaCreator[CumulateEntity](cumulateEntity, componentsParams, cumulateEntity.getOutSocketList().get(0))
    val fm = FieldManupulating(op.getOperationInputFields(), op.getOperationOutputFields(), op.getPassThroughFields(), op.getMapFields(), op.getOperationFields(), cumulateEntity.getKeyFields)
    val outRow = new Array[Any](fm.getOutputFields().size)

    val inputColumn = new Array[Column](fm.getinputFields().size)
    fm.getinputFields().zipWithIndex.foreach(f => {
      inputColumn(f._2) = col(f._1)
    })
    LOG.info("Operation InputFields: {} ", inputColumn.toList.mkString(", ") )
    val primaryKeys = if (cumulateEntity.getKeyFields == null) Array[KeyField]() else cumulateEntity.getKeyFields
    val secondaryKeys = if (cumulateEntity.getSecondaryKeyFields == null) Array[KeyField]() else cumulateEntity.getSecondaryKeyFields

    val sourceDf = componentsParams.getDataFrame().select(inputColumn: _*)

    val repartitionedDf = if (primaryKeys.isEmpty) sourceDf.repartition(1) else sourceDf.repartition(primaryKeys.map { field => col(field.getName) }: _*)

    LOG.info("Data is being Partitioned on keys Fields:{} ", populateSortKeys(primaryKeys ++ secondaryKeys))
    val sortedDf = repartitionedDf.sortWithinPartitions(populateSortKeys(primaryKeys ++ secondaryKeys): _*)


    LOG.info("Cumulate Operation Started....")
    val outputDf = sortedDf.mapPartitions(itr => {

      //Initialize Cumulate to call prepare Method
      val cumulateList = initializeCumulate(cumulateEntity.getOperationsList, primaryKeys, fm)
      var prevKeysArray: Array[Any] = null


      itr.map {
        row => {
          val currKeysArray: Array[Any] = RowHelper.extractKeyFields(row, fm.determineKeyFieldPos())
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
          RowHelper.setTupleFromRow(outRow, fm.determineMapSourceFieldsPos(), row, fm.determineMapTargetFieldsPos())
          //Passthrough Fields
          RowHelper.setTupleFromRow(outRow, fm.determineInputPassThroughFieldsPos(), row, fm.determineOutputPassThroughFieldsPos())

          cumulateList.foreach(cmt => {
            //Calling Cumulate Method
            cmt.baseClassInstance.cumulate(RowHelper.convertToReusebleRow(cmt.inputFieldPositions, row, cmt.inputReusableRow), cmt.outputReusableRow)
            RowHelper.setTupleFromReusableRow(outRow, cmt.outputReusableRow, cmt.outputFieldPositions)
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
    })(RowEncoder(EncoderHelper().getEncoder(fm.getOutputFields(), componentsParams.getSchemaFields())))
    Map(key -> outputDf)
  }

  def populateSortKeys (keysArray: Array[KeyField] ): Array[Column] = {
    keysArray.map {
      field => if (field.getSortOrder.toLowerCase == "desc") (col (field.getName).desc) else (col (field.getName) )
    }
  }
}
