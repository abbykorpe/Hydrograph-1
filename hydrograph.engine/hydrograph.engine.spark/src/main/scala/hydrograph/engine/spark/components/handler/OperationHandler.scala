package hydrograph.engine.spark.components.handler

import java.util.{Properties, ArrayList}

import hydrograph.engine.core.component.entity.elements.Operation
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
  def initializeAggregate( operationList:java.util.List[Operation], keyFields: Array[KeyField], fieldManupulating: FieldManupulating):
  List[Operatioin[AggregateTransformBase]] = {

    def aggregate( operationList:List[Operation], fieldManupulating: FieldManupulating):
    List[Operatioin[AggregateTransformBase]] =operationList match {
      case List() => List()
      case x :: xs =>
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
        val aggregateBase: AggregateTransformBase = classLoader[AggregateTransformBase](x.getOperationClass)

        aggregateBase.prepare(props, operationInputFieldList, operationOutputFieldList, keyFieldList)
        
        Operatioin[AggregateTransformBase](aggregateBase, blankInRR, blankOutRR, inputFieldPositions, outputFieldPositions, fieldManupulating) ::
          aggregate(xs, fieldManupulating)
    }

    if(operationList!=null)
      aggregate(operationList.asScala.toList,fieldManupulating)
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

  def initializeTransform( operationList:java.util.List[Operation], fieldManupulating: FieldManupulating):
  List[Operatioin[TransformBase]] = {

    def transform(operationList: List[Operation], fieldManupulating: FieldManupulating):
    List[Operatioin[TransformBase]] = operationList match {
      case List() => List()
      case x :: xs =>
        val operationInputFieldList = new ArrayList[String]()
        x.getOperationInputFields.foreach(v => operationInputFieldList.add(v))

        val operationOutputFieldList = new ArrayList[String]()
        x.getOperationOutputFields.foreach(v => operationOutputFieldList.add(v))

        val props: Properties = x.getOperationProperties
        val blankOutRR = ReusableRowHelper(x, fieldManupulating).convertToOutputReusableRow()
        val blankInRR = ReusableRowHelper(x, fieldManupulating).convertToInputReusableRow()
        val inputFieldPositions = ReusableRowHelper(x, fieldManupulating).determineInputFieldPositions()
        val outputFieldPositions = ReusableRowHelper(x, fieldManupulating).determineOutputFieldPositions()
        val transformBase: TransformBase = classLoader[TransformBase](x.getOperationClass)

        transformBase.prepare(props, operationInputFieldList, operationOutputFieldList)

        Operatioin[TransformBase](transformBase, blankInRR, blankOutRR, inputFieldPositions, outputFieldPositions, fieldManupulating) ::
          transform(xs, fieldManupulating)
    }
    if(operationList!=null)
      transform(operationList.asScala.toList,fieldManupulating)
    else
      List()
  }

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }

}

