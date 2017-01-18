package hydrograph.engine.spark.components.handler

import java.util.ArrayList
import java.util.Properties
import com.amazon.redshift.dataengine.ExpectedResult
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.mutable.ListBuffer
import hydrograph.engine.core.component.entity.elements.KeyField
import hydrograph.engine.core.component.entity.elements.Operation
import hydrograph.engine.expression.api.ValidationAPI
import hydrograph.engine.expression.userfunctions.AggregateForExpression
import hydrograph.engine.expression.userfunctions.CumulateForExpression
import hydrograph.engine.expression.userfunctions.NormalizeForExpression
import hydrograph.engine.expression.userfunctions.TransformForExpression
import hydrograph.engine.expression.utils.ExpressionWrapper
import hydrograph.engine.spark.components.utils.FieldManupulating
import hydrograph.engine.spark.components.utils.ReusableRowHelper
import hydrograph.engine.transformation.userfunctions.base.AggregateTransformBase
import hydrograph.engine.transformation.userfunctions.base.CumulateTransformBase
import hydrograph.engine.transformation.userfunctions.base.NormalizeTransformBase
import hydrograph.engine.transformation.userfunctions.base.ReusableRow
import hydrograph.engine.transformation.userfunctions.base.TransformBase

/**
  * Created by gurdits on 12/1/2016.
  */

object CustomClassLoader{
  def initializeObject[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }
}

case class Operatioin[T](baseClassInstance:T,inputReusableRow:ReusableRow,
                         outputReusableRow:ReusableRow,inputFieldPositions: ListBuffer[Int], outputFieldPositions:
                         ListBuffer[Int],fieldManupulating: FieldManupulating)

trait CumulateOperation{

  def convertToListOfValidation(list: List[Any]): Array[ValidationAPI] = {
    def convert(li:List[Any],converted:ListBuffer[ValidationAPI]):Array[ValidationAPI] = (li,converted) match {
      case (List(),conv) => conv.toArray
      case (x::xs,ys) if x == None => convert(xs,ys++ListBuffer(null))
      case (x::xs,ys) => convert(xs,ys++ListBuffer(x.asInstanceOf[ValidationAPI]))
    }
    convert(list,ListBuffer[ValidationAPI]())
  }

        def initializeCumulate( operationList:java.util.List[Operation], keyFields: Array[KeyField], fieldManupulating: FieldManupulating, expressionObjectList: ListBuffer[Any], initialValueExprs: List[String]):
        List[Operatioin[CumulateTransformBase]] = {

          def cumulate( operationList:List[Operation], fieldManupulating: FieldManupulating, expressionObjectList: List[Any], initialValueExprs: List[String], counter:Int):
          List[Operatioin[CumulateTransformBase]] = (operationList,expressionObjectList,initialValueExprs) match {
            case (List(),_,_) => List()
            case (x :: xs,y :: ys,z :: zs) =>
        val operationInputFieldList = new ArrayList[String]()
        x.getOperationInputFields.foreach(v => operationInputFieldList.add(v))

        val operationOutputFieldList = new ArrayList[String]()
        x.getOperationOutputFields.foreach(v => operationOutputFieldList.add(v))

        val keyFieldList = new ArrayList[String]()
        keyFields.foreach(v => keyFieldList.add(v.getName))

        val props: Properties = x.getOperationProperties
        val blankOutRR = ReusableRowHelper(x, fieldManupulating).convertToOutputReusableRow()
        val blankInRR = ReusableRowHelper(x, fieldManupulating).convertToInputReusableRow()
        val inputFieldPositions = ReusableRowHelper(x, fieldManupulating).determineInputFieldPositions()
        val outputFieldPositions = ReusableRowHelper(x, fieldManupulating).determineOutputFieldPositions()

              val cumulateBase: CumulateTransformBase = (x,y) match {
                case (_,_) if(x.getOperationClass == null) => {
                  var cumulate = new CumulateForExpression
//                  cumulate.setValidationAPI(convertToListOfValidation(y :: ys))
//                  cumulate.setCounter(counter)
//                  cumulate.setInitialValueExpression((z::zs).toArray)
//                  cumulate.callPrepare
                  cumulate
                }
                case _ => {
                  var cumulate = CustomClassLoader.initializeObject[CumulateTransformBase](x.getOperationClass)
                  cumulate.prepare(props, operationInputFieldList, operationOutputFieldList, keyFieldList)
                  cumulate
                }
              }

        Operatioin[CumulateTransformBase](cumulateBase, blankInRR, blankOutRR, inputFieldPositions, outputFieldPositions, fieldManupulating) ::
          cumulate(xs, fieldManupulating,y::ys,z::zs,counter+1)
    }

    if(operationList!=null)
      cumulate(operationList.asScala.toList,fieldManupulating,expressionObjectList.toList,initialValueExprs,0)
    else
      List()
  }

}

trait AggregateOperation{

  def convertToListOfValidation(list: List[Any]): Array[ValidationAPI] = {
    def convert(li:List[Any],converted:ListBuffer[ValidationAPI]):Array[ValidationAPI] = (li,converted) match {
      case (List(),conv) => conv.toArray
      case (x::xs,ys) if x == None => convert(xs,ys++ListBuffer(null))
      case (x::xs,ys) => convert(xs,ys++ListBuffer(x.asInstanceOf[ValidationAPI]))
    }
    convert(list,ListBuffer[ValidationAPI]())
  }

  def initializeAggregate( operationList:java.util.List[Operation], keyFields: Array[KeyField], fieldManupulating: FieldManupulating, expressionObjectList: ListBuffer[Any], initialValueExprs: List[String]):
  List[Operatioin[AggregateTransformBase]] = {

    def aggregate( operationList:List[Operation], fieldManupulating: FieldManupulating, expressionObjectList: List[Any], initialValueExprs: List[String], counter:Int):
    List[Operatioin[AggregateTransformBase]] = (operationList,expressionObjectList,initialValueExprs) match {
      case (List(),_,_) => List()
      case (x :: xs,y :: ys,z :: zs) =>
        val operationInputFieldList = new ArrayList[String]()
        x.getOperationInputFields.foreach(v => operationInputFieldList.add(v))

        val operationOutputFieldList = new ArrayList[String]()
        x.getOperationOutputFields.foreach(v => operationOutputFieldList.add(v))

        val keyFieldList = new ArrayList[String]()
        keyFields.foreach(v => keyFieldList.add(v.getName))

        val props: Properties = x.getOperationProperties
        val blankOutRR = ReusableRowHelper(x, fieldManupulating).convertToOutputReusableRow()
        val blankInRR = ReusableRowHelper(x, fieldManupulating).convertToInputReusableRow()
        val inputFieldPositions = ReusableRowHelper(x, fieldManupulating).determineInputFieldPositions()
        val outputFieldPositions = ReusableRowHelper(x, fieldManupulating).determineOutputFieldPositions()

        val aggregateBase: AggregateTransformBase = (x,y) match {
          case (_,_) if(x.getOperationClass == null) => {
            val expressionWrapper=new ExpressionWrapper(y.asInstanceOf[ValidationAPI],z);
            var aggregate = new AggregateForExpression
              aggregate.setValidationAPI(expressionWrapper)
              aggregate.callPrepare
            aggregate
          }
          case _ => {
            var aggregate = CustomClassLoader.initializeObject[AggregateTransformBase](x.getOperationClass)
            aggregate.prepare(props, operationInputFieldList, operationOutputFieldList, keyFieldList)
            aggregate
          }
        }

        Operatioin[AggregateTransformBase](aggregateBase, blankInRR, blankOutRR, inputFieldPositions, outputFieldPositions, fieldManupulating) ::
          aggregate(xs, fieldManupulating,y::ys,z::zs,counter+1)
    }

    if(operationList!=null)
      aggregate(operationList.asScala.toList,fieldManupulating,expressionObjectList.toList,initialValueExprs,0)
    else
      List()
  }

}

trait NormalizeOperation{

  def initializeNormalize( operationList:java.util.List[Operation], fieldManupulating: FieldManupulating, expressionObjectList: ListBuffer[Any]):
  List[Operatioin[NormalizeTransformBase]] = {

    def normalize(operationList: List[Operation], fieldManupulating: FieldManupulating,expressionObjectList: List[Any]):
    List[Operatioin[NormalizeTransformBase]] = (operationList,expressionObjectList) match {
      case (List(),_) => List()
      case (x :: xs,y :: ys) =>
        val operationInputFieldList = new ArrayList[String]()
        x.getOperationInputFields.foreach(v => operationInputFieldList.add(v))

        val operationOutputFieldList = new ArrayList[String]()
        x.getOperationOutputFields.foreach(v => operationOutputFieldList.add(v))

        val normalizeBase: NormalizeTransformBase = (x,y) match {
          case (_,_) if(y != None && x.getOperationClass == null) => {
            var normalize = new NormalizeForExpression()
            normalize.setValidationAPI(y.asInstanceOf[ExpressionWrapper])
            normalize
          }
          case _ => classLoader[NormalizeTransformBase](x.getOperationClass)
        }

        val props: Properties = x.getOperationProperties
        val blankOutRR = ReusableRowHelper(x, fieldManupulating).convertToOutputReusableRow()
        val blankInRR = ReusableRowHelper(x, fieldManupulating).convertToInputReusableRow()
        val inputFieldPositions = ReusableRowHelper(x, fieldManupulating).determineInputFieldPositions()
        val outputFieldPositions = ReusableRowHelper(x, fieldManupulating).determineOutputFieldPositions()


        normalizeBase.prepare(props)
        Operatioin[NormalizeTransformBase](normalizeBase, blankInRR, blankOutRR, inputFieldPositions, outputFieldPositions, fieldManupulating) ::
          normalize(xs, fieldManupulating, ys)
    }
    if(operationList!=null)
      normalize(operationList.asScala.toList,fieldManupulating,expressionObjectList.toList)
    else
      List()
  }

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }
}

trait TransformOperation{

  def initializeTransform( operationList:java.util.List[Operation], fieldManupulating: FieldManupulating, expressionObjectList: ListBuffer[Any]):
  List[Operatioin[TransformBase]] = {

    def transform(operationList: List[Operation], fieldManupulating: FieldManupulating,expressionObjectList: List[Any]):
    List[Operatioin[TransformBase]] = (operationList,expressionObjectList) match {
      case (List(),_) => List()
      case (x :: xs,y :: ys) =>
        val operationInputFieldList = new ArrayList[String]()
        x.getOperationInputFields.foreach(v => operationInputFieldList.add(v))

        val operationOutputFieldList = new ArrayList[String]()
        x.getOperationOutputFields.foreach(v => operationOutputFieldList.add(v))

        val props: Properties = x.getOperationProperties
        val blankOutRR = ReusableRowHelper(x, fieldManupulating).convertToOutputReusableRow()
        val blankInRR = ReusableRowHelper(x, fieldManupulating).convertToInputReusableRow()
        val inputFieldPositions = ReusableRowHelper(x, fieldManupulating).determineInputFieldPositions()
        val outputFieldPositions = ReusableRowHelper(x, fieldManupulating).determineOutputFieldPositions()

        val transformBase: TransformBase = (x,y) match {
          case (_,_) if(y != None && x.getOperationClass == null) => {
            var transform = new TransformForExpression()
            transform.setValidationAPI(y.asInstanceOf[ValidationAPI])
            transform
          }
          case _ => CustomClassLoader.initializeObject[TransformBase](x.getOperationClass)
        }

        transformBase.prepare(props, operationInputFieldList, operationOutputFieldList)
        Operatioin[TransformBase](transformBase, blankInRR, blankOutRR, inputFieldPositions, outputFieldPositions, fieldManupulating) ::
          transform(xs, fieldManupulating, ys)
    }
    if(operationList!=null)
      transform(operationList.asScala.toList,fieldManupulating,expressionObjectList.toList)
    else
      List()
  }


}

