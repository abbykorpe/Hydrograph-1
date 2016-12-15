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

/**
 * Created by gurdits on 10/18/2016.
 */
class SparkNormalizeComponent(normalizeEntity: NormalizeEntity, componentsParams: BaseComponentParams) extends OperationComponentBase with NormalizeOperation with Serializable {

  def getAllInputFieldsForExpr(getOperationsList: util.List[Operation], list:List[String]):List[String] = {
    var list1 = getOperationsList.asScala.toList.flatMap(e => e.getOperationInputFields)
    list1
  }

  def getAllOutputFieldsForExpr(getOperationsList: util.List[Operation], list:List[String]):List[String] = {
    var list1 = getOperationsList.asScala.toList.flatMap(e => e.getOperationOutputFields)
    list1
  }

  def unique[A](ls: List[A]) = {
    def loop(set: Set[A], ls: List[A]): List[A] = ls match {
      case hd :: tail if set contains hd => loop(set, tail)
      case hd :: tail => hd :: loop(set + hd, tail)
      case Nil => Nil
    }

    loop(Set(), ls)
  }

  def convertToList(listBuffer: ListBuffer[String]): util.ArrayList[String] = {
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

    def flattenBufferList(strings: ListBuffer[ListBuffer[String]]):ListBuffer[ListBuffer[String]] = {
      def flattenList(strings:ListBuffer[ListBuffer[String]],finalList:ListBuffer[ListBuffer[String]], count:Int):ListBuffer[ListBuffer[String]]=(finalList,count) match {
        case (f,c) if c== 0 => f
        case (f,count)=> flattenList(strings,f+=strings.flatten,count-1)
      }
      flattenList(strings,ListBuffer[ListBuffer[String]](),strings.length)
    }

    def listBufferToArrayList(listBuffer:ListBuffer[java.util.ArrayList[String]]):util.ArrayList[util.ArrayList[String]] = {
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

  def extractAllInputPositions(inputFields:List[String]):List[Int] = Seq(0 to (inputFields.length-1)).toList.flatten
  def extractAllOutputPositions(outputFields:List[String]):List[Int] = Seq(0 to (outputFields.length-1)).toList.flatten

  override def createComponent(): Map[String, DataFrame] = {

    val op = OperationSchemaCreator[NormalizeEntity](normalizeEntity, componentsParams, normalizeEntity.getOutSocketList().get(0))

    val fm = FieldManupulating(op.getOperationInputFields(), op.getOperationOutputFields(), op.getPassThroughFields(), op.getMapFields(), op.getOperationFields(), null)

    val outRow = new Array[Any](fm.getOutputFields().size)

    val inputColumn = new Array[Column](fm.getinputFields().size)
    fm.getinputFields().zipWithIndex.foreach(f => {
      inputColumn(f._2) = col(f._1)
    })

    var outputDispatcher: NormalizeOutputCollector = null

    val df = componentsParams.getDataFrame.select(inputColumn: _*).mapPartitions(itr => {

      val opr = normalizeEntity.getOperationsList()(0)
      val props: Properties = opr.getOperationProperties
      val outRR = ReusableRowHelper(opr, fm).convertToOutputReusableRow()
      outputDispatcher = new NormalizeOutputCollector(outRR, outRow, fm.determineOutputFieldPositions()(0))

      val normalizeList = initializeNormalize(normalizeEntity.getOperationsList,fm,op.getExpressionObject)
      val it = itr.map(row => {
        //Map Fields
        RowHelper.setTupleFromRow(outRow, fm.determineMapSourceFieldsPos(), row, fm.determineMapTargetFieldsPos())
        //Passthrough Fields
        RowHelper.setTupleFromRow(outRow, fm.determineInputPassThroughFieldsPos(), row, fm.determineOutputPassThroughFieldsPos())
        normalizeList.foreach { nr =>
          var inputReusableRow = RowHelper.convertToReusebleRow(nr.inputFieldPositions, row, nr.inputReusableRow)
          var outputReusableRow = nr.outputReusableRow
          var outputPositions = nr.outputFieldPositions

          if(nr.isInstanceOf[Operatioin[NormalizeForExpression]]){

            var fieldNames:ListBuffer[String] = fm.getinputFields()
            var tuples: Array[Object] = (0 to (fieldNames.length-1)).toList.map(e => row.get(e).asInstanceOf[Object]).toArray
            var inputFields:List[String] = unique(getAllInputFieldsForExpr(normalizeEntity.getOperationsList,List[String]()))
            var outputFields:List[String] = getAllOutputFieldsForExpr(normalizeEntity.getOperationsList,List[String]())
            var inputPositions:List[Int] = extractAllInputPositions(inputFields)
            outputPositions = extractAllOutputPositions(outputFields).to[ListBuffer]
            inputReusableRow = RowHelper.convertToReusebleRow(inputPositions.to[ListBuffer],row,ReusableRowHelper(normalizeEntity.getOperation,fm).convertToReusableRow(inputFields))
            outputReusableRow = ReusableRowHelper(normalizeEntity.getOperation,fm).convertToReusableRow(outputFields)

            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setValidationAPI(op.getExpressionObject().get(0).asInstanceOf[ValidationAPI])
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setTransformInstancesSize(normalizeList.length)
            val x:java.util.List[String] = op.getExpressionObject().map(e => e.asInstanceOf[ValidationAPI].getExpr)
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setListOfExpressions(new java.util.ArrayList[String](x))
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setCountExpression(normalizeEntity.getOutputRecordCount)
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setOperationOutputFields(getOperationOutputFields(op.getOperationOutputFields()))
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setFieldNames(convertToList(fieldNames).toArray(new Array[String](fieldNames.size)))
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setTuples(tuples)

            outputDispatcher = new NormalizeOutputCollector(outRR, outRow, outputPositions)
          }
          outputDispatcher.initialize
          //Calling Transform Method
          nr.baseClassInstance.Normalize(inputReusableRow, outputReusableRow, outputDispatcher)
          RowHelper.setTupleFromReusableRow(outRow, outputReusableRow, outputPositions)
          //Calling Cleanup Method
          if (itr.isEmpty)
            nr.baseClassInstance.cleanup()
        }
        Row.fromSeq(outRow)
      })
      it

    })(RowEncoder(EncoderHelper().getEncoder(fm.getOutputFields(), componentsParams.getSchemaFields())))

    val key = normalizeEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)
  }
//
//  def classLoader[T](className: String): T = {
//    val clazz = Class.forName(className).getDeclaredConstructors();
//    clazz(0).setAccessible(true)
//    clazz(0).newInstance().asInstanceOf[T]
//  }

}

class NormalizeOutputCollector(outputReusableRow: ReusableRow, outRow: Array[Any], outputFieldPositions: ListBuffer[Int]) extends OutputDispatcher {

  private val list = new ListBuffer[Row]()
  override def sendOutput(): Unit = {

    RowHelper.setTupleFromReusableRow(outRow, outputReusableRow, outputFieldPositions)

    val clonedRow = outRow.clone()
    list += Row.fromSeq(clonedRow)
    
  }

    def initialize: Unit = {

    list.clear()
  }
  
  def getOutRows: ListBuffer[Row] = {

    list
  }
}
