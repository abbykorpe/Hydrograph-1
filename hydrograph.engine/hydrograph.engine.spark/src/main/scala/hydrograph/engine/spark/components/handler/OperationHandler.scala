package hydrograph.engine.spark.components.handler

import java.util.{Properties, ArrayList}

import hydrograph.engine.core.component.entity.elements.Operation
import hydrograph.engine.expression.api.ValidationAPI
import hydrograph.engine.expression.userfunctions.{AggregateForExpression, TransformForExpression}
import hydrograph.engine.spark.components.utils.{ReusableRowHelper, FieldManupulating}
import hydrograph.engine.transformation.userfunctions.base.{TransformBase, AggregateTransformBase, ReusableRow}
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import hydrograph.engine.core.component.entity.elements.KeyField

/**
  * Created by gurdits on 12/1/2016.
  */

case class Operatioin[T](baseClassInstance:T,inputReusableRow:ReusableRow,
                         outputReusableRow:ReusableRow,inputFieldPositions: ListBuffer[Int], outputFieldPositions:
                         ListBuffer[Int],fieldManupulating: FieldManupulating)


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
          case (_,_) if(y != None && x.getOperationClass == null) => {
            var aggregate = new AggregateForExpression
              aggregate.setValidationAPI(convertToListOfValidation(y :: ys))
              aggregate.setCounter(counter)
              aggregate.setInitialValueExpression((z::zs).toArray)
              aggregate.callPrepare
            aggregate
          }
          case _ => {
            var aggregate = classLoader[AggregateTransformBase](x.getOperationClass)
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
          case _ => classLoader[TransformBase](x.getOperationClass)
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

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }

}

