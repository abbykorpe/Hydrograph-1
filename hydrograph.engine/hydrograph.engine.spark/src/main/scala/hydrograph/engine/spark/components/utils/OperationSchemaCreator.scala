package hydrograph.engine.spark.components.utils

import hydrograph.engine.core.component.entity.base.OperationEntityBase
import hydrograph.engine.core.component.entity.elements.{Operation, OutSocket}
import hydrograph.engine.core.component.entity.utils.OutSocketUtils
import hydrograph.engine.spark.components.platform.BaseComponentParams

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import java.util.Properties

/**
  * Created by gurdits on 10/18/2016.
  */
 class OperationSchemaCreator[T <: OperationEntityBase](entity: T, baseComponentParams: BaseComponentParams, outSocket: OutSocket) extends Serializable {

  var inputFieldList = ListBuffer[ListBuffer[String]]()
  var outputFields = ListBuffer[ListBuffer[String]]()
  var transformClass = ListBuffer[String]()
  var operationProperties = ListBuffer[Properties]()


  def getMapFields(): ListBuffer[(String, String)] = {
    var mapFields = ListBuffer[(String, String)]()
    for (mapField <- outSocket.getMapFieldsList) {
      mapFields += ((mapField.getSourceName, mapField.getName))
    }
    mapFields
  }


  def getOperationFields(): ListBuffer[String] = {
    var opFields = ListBuffer[String]()
    outSocket.getOperationFieldList.foreach(op =>
      opFields += (op.getName))
    opFields
  }

  def getPassThroughFields(): ListBuffer[String] = {
    val list = new Array[String](baseComponentParams.getDataFrame().schema.fieldNames.length)
    val passthroughFields = new ListBuffer[String]()
    baseComponentParams.getDataFrame().schema.fieldNames.zipWithIndex.foreach(x => list(x._2)=x._1)

    val passthroughSchema = OutSocketUtils.getPassThroughFieldsFromOutSocket(outSocket.getPassThroughFieldsList, list)
    if (passthroughSchema != null && passthroughSchema.length == 0 && !entity.isOperationPresent)
      baseComponentParams.getDataFrame().schema.fieldNames.foreach(x => passthroughFields += x);
    else
      passthroughSchema.foreach(x => passthroughFields += x)
    passthroughFields
  }

  def initializeOperationFieldsForOutSocket: Unit = {
    if (entity.isOperationPresent)
      entity.getOperationsList.filter(x => isOperationIDExistsInOperationFields(x.getOperationId, outSocket))foreach { opr =>
        inputFieldList += extractInputFields(opr)
        outputFields += extractOutputFields(opr)
        transformClass += opr.getOperationClass
        operationProperties += extractProperties(opr)
      }
  }


  def getOperationInputFields(): ListBuffer[ListBuffer[String]] = {
    inputFieldList
  }

  def getOperationOutputFields(): ListBuffer[ListBuffer[String]] = {
    outputFields
  }

  def getOperationClass(): ListBuffer[String] = {
    transformClass
  }

  def extractOutputFields(operation: Operation): ListBuffer[String] = {
    var outputFields = ListBuffer[String]()
    if (operation.getOperationOutputFields != null)
      operation.getOperationOutputFields.foreach(x=> outputFields += x)
    outputFields
  }

  def extractInputFields(operation: Operation): ListBuffer[String] = {
    var inputFields = ListBuffer[String]()
    if (operation.getOperationInputFields != null)
      operation.getOperationInputFields.foreach(x => inputFields += x)
    inputFields
  }
  
    def extractProperties(operation: Operation): Properties = {
    var props = new Properties()
    if (operation.getOperationProperties != null)
      operation.getOperationProperties.foreach(x => props += x)
    props
  }

  def isOperationIDExistsInOperationFields(operationId: String, outSocket: OutSocket): Boolean = {
    for (eachOperationField <- outSocket.getOperationFieldList()) {
      if (eachOperationField.getOperationId().equals(operationId)) {
        return true
      }
    }
    false
  }
}
object OperationSchemaCreator{
  def  apply[T <: OperationEntityBase](entity: T, baseComponentParams: BaseComponentParams, outSocket: OutSocket): OperationSchemaCreator[T] ={
   val op= new OperationSchemaCreator(entity,baseComponentParams,outSocket)
    op.initializeOperationFieldsForOutSocket
    op
  }
}

