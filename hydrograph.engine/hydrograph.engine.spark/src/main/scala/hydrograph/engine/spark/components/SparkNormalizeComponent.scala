package hydrograph.engine.spark.components

import java.util

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import hydrograph.engine.core.component.entity.elements.Operation
import hydrograph.engine.core.component.entity.{ NormalizeEntity}
import hydrograph.engine.expression.api.ValidationAPI
import hydrograph.engine.expression.userfunctions.NormalizeForExpression
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.{NormalizeOperation, Operatioin}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import hydrograph.engine.transformation.userfunctions.base.{ OutputDispatcher, ReusableRow}
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{ Column, DataFrame, Row }

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

  def getOperationOutputFields(strings: ListBuffer[ListBuffer[String]]): util.ArrayList[util.ArrayList[String]] = {

    def flattenBufferList(strings: ListBuffer[ListBuffer[String]]):ListBuffer[ListBuffer[String]] = {

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
    val temp = listBufferToArrayList(strings.map(e => convertToList(e)))
    temp
  }

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

//      val opr = normalizeEntity.getOperationsList()(0)
//      val props: Properties = opr.getOperationProperties
//      val outRR = ReusableRowHelper(opr, fm).convertToOutputReusableRow()
//      outputDispatcher = new NormalizeOutputCollector(outRR, outRow, fm)
//      val normalizeTransformBase: NormalizeTransformBase = classLoader[NormalizeTransformBase](opr.getOperationClass)
//
//      normalizeTransformBase.prepare(props)
//
//      val it = itr.flatMap(row => {
//        outputDispatcher.initialize
//        //Map Fields
//        RowHelper.setTupleFromRow(outRow, fm.determineMapSourceFieldsPos(), row, fm.determineMapTargetFieldsPos())
//        //Passthrough Fields
//        RowHelper.setTupleFromRow(outRow, fm.determineInputPassThroughFieldsPos(), row, fm.determineOutputPassThroughFieldsPos())
//        normalizeTransformBase.Normalize(RowHelper.convertToReusebleRow(ReusableRowHelper(opr, fm).determineInputFieldPositions(), row, ReusableRowHelper(opr, fm).convertToInputReusableRow()), outRR, outputDispatcher)
//        if(itr.isEmpty)
//          normalizeTransformBase.cleanup()
//        outputDispatcher.getOutRows
//      })
//      it

      val normalizeList = initializeNormalize(normalizeEntity.getOperationsList,fm,op.getExpressionObject)

//      if(normalizeList.get(0).isIn)

      val it = itr.map(row => {

        var fieldNames:ListBuffer[String] = fm.getinputFields()
//        var tuples: ListBuffer[Object] =
        var inputFields = unique(getAllInputFieldsForExpr(normalizeEntity.getOperationsList,List[String]()))
        var outputFields = getAllOutputFieldsForExpr(normalizeEntity.getOperationsList,List[String]())
        val inputRR = ReusableRowHelper(normalizeEntity.getOperation,fm).convertToReusableRow(inputFields)
        val outputRR = ReusableRowHelper(normalizeEntity.getOperation,fm).convertToReusableRow(outputFields)

//        var inputPositions = extractInputPositions(fm.getAllInputPositions());

        val outRow = new Array[Any](fm.getOutputFields().size)
        //Map Fields
        RowHelper.setTupleFromRow(outRow, fm.determineMapSourceFieldsPos(), row, fm.determineMapTargetFieldsPos())
        //Passthrough Fields
        RowHelper.setTupleFromRow(outRow, fm.determineInputPassThroughFieldsPos(), row, fm.determineOutputPassThroughFieldsPos())
        normalizeList.foreach { nr =>

          if(nr.isInstanceOf[Operatioin[NormalizeForExpression]]){
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setValidationAPI(op.getExpressionObject().get(0).asInstanceOf[ValidationAPI])
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setTransformInstancesSize(normalizeList.length)
            val x:java.util.List[String] = op.getExpressionObject().map(e => e.asInstanceOf[ValidationAPI].getExpr)
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setListOfExpressions(new java.util.ArrayList[String](x))
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setCountExpression(normalizeEntity.getOutputRecordCount)
            nr.baseClassInstance.asInstanceOf[NormalizeForExpression].setOperationOutputFields(getOperationOutputFields(op.getOperationOutputFields()))
          }

          //Calling Transform Method
          nr.baseClassInstance.Normalize(RowHelper.convertToReusebleRow(nr.inputFieldPositions, row, nr.inputReusableRow), nr
            .outputReusableRow, outputDispatcher)
          RowHelper.setTupleFromReusableRow(outRow, nr.outputReusableRow, nr.outputFieldPositions)
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

class NormalizeOutputCollector(outputReusableRow: ReusableRow, outRow: Array[Any], fieldManupulating: FieldManupulating) extends OutputDispatcher {

  private val list = new ListBuffer[Row]()
  override def sendOutput(): Unit = {

    RowHelper.setTupleFromReusableRow(outRow, outputReusableRow, fieldManupulating
      .determineOutputFieldPositions()(0))

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
