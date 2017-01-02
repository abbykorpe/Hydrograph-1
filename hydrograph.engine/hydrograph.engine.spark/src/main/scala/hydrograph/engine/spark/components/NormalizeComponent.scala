package hydrograph.engine.spark.components

import java.util
import java.util.Properties

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import hydrograph.engine.core.component.entity.elements.Operation
import hydrograph.engine.core.component.entity.NormalizeEntity
import hydrograph.engine.expression.api.ValidationAPI
import hydrograph.engine.expression.userfunctions.NormalizeForExpression
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.{NormalizeOperation, Operatioin}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import hydrograph.engine.transformation.userfunctions.base.{NormalizeTransformBase, OutputDispatcher, ReusableRow}
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, Row}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by bitwise on 10/18/2016.
  */
class NormalizeComponent(normalizeEntity: NormalizeEntity, componentsParams: BaseComponentParams) extends OperationComponentBase with NormalizeOperation with Serializable {

  private val LOG:Logger = LoggerFactory.getLogger(classOf[OutputFileMixedSchemeComponent])
  def getAllInputFieldsForExpr(getOperationsList: util.List[Operation], list: List[String]): List[String] = {
    LOG.trace("In method getAllInputFieldsForExpr()")
    var list1 = getOperationsList.asScala.toList.flatMap(e => e.getOperationInputFields)
    list1
  }

  def getAllOutputFieldsForExpr(getOperationsList: util.List[Operation], list: List[String]): List[String] = {
    LOG.trace("In method getAllOutputFieldsForExpr()")
    var list1 = getOperationsList.asScala.toList.flatMap(e => e.getOperationOutputFields)
    list1
  }

  def unique[A](ls: List[A]) = {
    LOG.trace("In method unique()")
    def loop(set: Set[A], ls: List[A]): List[A] = ls match {
      case hd :: tail if set contains hd => loop(set, tail)
      case hd :: tail => hd :: loop(set + hd, tail)
      case Nil => Nil
    }

    loop(Set(), ls)
  }

  def convertToList(listBuffer: ListBuffer[String]): util.ArrayList[String] = {
    LOG.trace("In method convertToList()")
    def convert(list: List[String], arrayList: util.ArrayList[String]): util.ArrayList[String] = list match {
      case List() => arrayList
      case x :: xs => {
        arrayList.add(x)
        convert(xs, arrayList)
      }
    }

    convert(listBuffer.toList, new util.ArrayList[String]())
  }

  def getOperationOutputFields(strings: ListBuffer[ListBuffer[String]]): util.ArrayList[util.ArrayList[String]] = {
    LOG.trace("In method getOperationOutputFields()")

    def flattenBufferList(strings: ListBuffer[ListBuffer[String]]): ListBuffer[ListBuffer[String]] = {
      def flattenList(strings: ListBuffer[ListBuffer[String]], finalList: ListBuffer[ListBuffer[String]], count: Int): ListBuffer[ListBuffer[String]] = (finalList, count) match {
        case (f, c) if c == 0 => f
        case (f, count) => flattenList(strings, f += strings.flatten, count - 1)
      }

      flattenList(strings, ListBuffer[ListBuffer[String]](), strings.length)
    }

    def listBufferToArrayList(listBuffer: ListBuffer[java.util.ArrayList[String]]): util.ArrayList[util.ArrayList[String]] = {
      LOG.trace("In method listBufferToArrayList()")

      def convert(list: List[util.ArrayList[String]], arrayList: util.ArrayList[util.ArrayList[String]]): util.ArrayList[util.ArrayList[String]] = list match {
        case List() => arrayList
        case x :: xs => {
          arrayList.add(x)
          convert(xs, arrayList)
        }
      }

      convert(listBuffer.toList, new util.ArrayList[util.ArrayList[String]]())
    }

    val temp = listBufferToArrayList(flattenBufferList(strings).map(e => convertToList(e)))
    temp
  }

  def extractAllInputPositions(inputFields: List[String]): List[Int] = Seq(0 to (inputFields.length - 1)).toList.flatten

  def extractAllOutputPositions(outputFields: List[String]): List[Int] = Seq(0 to (outputFields.length - 1)).toList.flatten

  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")

    val op = OperationSchemaCreator[NormalizeEntity](normalizeEntity, componentsParams, normalizeEntity.getOutSocketList().get(0))

    val fm = FieldManupulating(op.getOperationInputFields(), op.getOperationOutputFields(), op.getPassThroughFields(), op.getMapFields(), op.getOperationFields(), null)

    val outRow = new Array[Any](fm.getOutputFields().size)

    val inputColumn = new Array[Column](fm.getinputFields().size)
    fm.getinputFields().zipWithIndex.foreach(f => {
      inputColumn(f._2) = col(f._1)
    })

    var outputDispatcher: NormalizeOutputCollector = null

    val df = componentsParams.getDataFrame.select(inputColumn: _*).mapPartitions(itr => {
      val normalizeList = initializeNormalize(normalizeEntity.getOperationsList, fm, op.getExpressionObject)
      val it = itr.flatMap(row => {
        //Map Fields
        RowHelper.setTupleFromRow(outRow, fm.determineMapSourceFieldsPos(), row, fm.determineMapTargetFieldsPos())
        //Passthrough Fields
        RowHelper.setTupleFromRow(outRow, fm.determineInputPassThroughFieldsPos(), row, fm.determineOutputPassThroughFieldsPos())

        var nr = normalizeList.get(0)
        var inputReusableRow = RowHelper.convertToReusebleRow(nr.inputFieldPositions, row, nr.inputReusableRow)
        var outputReusableRow = nr.outputReusableRow
        var outputPositions = nr.outputFieldPositions
        outputDispatcher = new NormalizeOutputCollector(outputReusableRow, outRow, fm.determineOutputFieldPositions()(0))

        if (nr.baseClassInstance.isInstanceOf[NormalizeForExpression]) {
          LOG.info("Normalize Operation contains Expressions, so NormalizeForExpression class will be used for processing.")
          var fieldNames: ListBuffer[String] = fm.getinputFields()
          var tuples: Array[Object] = (0 to (fieldNames.length - 1)).toList.map(e => row.get(e).asInstanceOf[Object]).toArray
          var inputFields: List[String] = unique(getAllInputFieldsForExpr(normalizeEntity.getOperationsList, List[String]()))
          var outputFields: List[String] = getAllOutputFieldsForExpr(normalizeEntity.getOperationsList, List[String]())
          var inputPositions: List[Int] = extractAllInputPositions(inputFields)
          outputPositions = extractAllOutputPositions(outputFields).to[ListBuffer]
          inputReusableRow = RowHelper.convertToReusebleRow(inputPositions.to[ListBuffer], row, ReusableRowHelper(normalizeEntity.getOperation, fm).convertToReusableRow(inputFields))
          outputReusableRow = ReusableRowHelper(normalizeEntity.getOperation, fm).convertToReusableRow(outputFields)

          nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setValidationAPI(op.getExpressionObject().get(0).asInstanceOf[ValidationAPI])
          nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setTransformInstancesSize(normalizeList.length)
          val x: java.util.List[String] = op.getExpressionObject().map(e => e.asInstanceOf[ValidationAPI].getExpr)
          LOG.info("List of Expressions: ["+x.toString+"].")
          nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setListOfExpressions(new java.util.ArrayList[String](x))
          LOG.info("outputRecordCount Expression: "+normalizeEntity.getOutputRecordCount+".")
          nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setCountExpression(normalizeEntity.getOutputRecordCount)
          nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setOperationOutputFields(getOperationOutputFields(op.getOperationOutputFields()))
          nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setFieldNames(convertToList(fieldNames).toArray(new Array[String](fieldNames.size)))
          nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setTuples(tuples)

          outputDispatcher = new NormalizeOutputCollector(outputReusableRow, outRow, outputPositions)
        }
        outputDispatcher.initialize
        LOG.info("Calling Normalize() method of " + nr.baseClassInstance.getClass.toString +" class.")
        nr.baseClassInstance.Normalize(inputReusableRow, outputReusableRow, outputDispatcher)
        if (itr.isEmpty)
          LOG.info("Calling cleanup() method of " + nr.baseClassInstance.getClass.toString +" class.")
          nr.baseClassInstance.cleanup()

        outputDispatcher.getOutRows
      })
      it

    })(RowEncoder(EncoderHelper().getEncoder(fm.getOutputFields(), componentsParams.getSchemaFields())))

    val key = normalizeEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)
  }

}

class NormalizeOutputCollector(outputReusableRow: ReusableRow, outRow: Array[Any], outputFieldPositions: ListBuffer[Int]) extends OutputDispatcher {

  private val list = new ListBuffer[Row]()
  private val LOG:Logger = LoggerFactory.getLogger(classOf[OutputFileMixedSchemeComponent])

  override def sendOutput(): Unit = {
    LOG.trace("In method sendOutput()")
    RowHelper.setTupleFromReusableRow(outRow, outputReusableRow, outputFieldPositions)
    val clonedRow = outRow.clone()
    list += Row.fromSeq(clonedRow)
  }

  def initialize: Unit = {
    LOG.trace("In method initialize()")
    list.clear()
  }

  def getOutRows: ListBuffer[Row] = {
    LOG.trace("In method getOutRows()")
    list
  }
}
