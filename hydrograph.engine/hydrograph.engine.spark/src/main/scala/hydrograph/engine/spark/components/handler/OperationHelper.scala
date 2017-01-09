package hydrograph.engine.spark.components.handler

import java.util

import hydrograph.engine.core.component.entity.utils.OutSocketUtils
import hydrograph.engine.expression.api.ValidationAPI
import hydrograph.engine.expression.userfunctions.TransformForExpression

import scala.collection.JavaConverters.asScalaBufferConverter
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import hydrograph.engine.core.component.entity.elements.{OutSocket, Operation, MapField, PassThroughField}
import hydrograph.engine.spark.core.reusablerow.RowToReusableMapper
import java.util.ArrayList
import hydrograph.engine.spark.core.reusablerow.InputReusableRow
import hydrograph.engine.transformation.userfunctions.base.ReusableRow
import org.apache.spark.sql.Row
import hydrograph.engine.spark.core.reusablerow.OutputReusableRow
import scala.collection.JavaConverters._
import scala.collection.mutable

import scala.collection.mutable.ListBuffer

case class SparkOperation[T](baseClassInstance: T, operationEntity: Operation, inputRow: InputReusableRow, outputRow:
OutputReusableRow,validatioinAPI:ValidationAPI,initalValue:String)

trait OperationHelper[T] {

  def initializeTransformList(operationList: java.util.List[Operation], inputSchema: StructType, outputSchema: StructType): List[SparkOperation[T]] = {

    def transform(operationList: List[Operation],expList:List[Any],initalValue:List[String]): List[SparkOperation[T]] =
      (operationList,expList,initalValue) match {
      case (List(),_,_) => List()
      case (x :: xs,y::ys,z::zs) =>

        (x,y) match {
          case (_, _) if (y != None && x.getOperationClass == null) => {
            val tf = classLoader[T](classOf[TransformForExpression].getCanonicalName)
            SparkOperation[T](tf, x, InputReusableRow(null, new RowToReusableMapper(inputSchema, x
              .getOperationInputFields)), OutputReusableRow(null, new RowToReusableMapper(outputSchema, x
              .getOperationOutputFields)), y.asInstanceOf[ValidationAPI],z) ::
              transform(xs, ys,zs)
          }
          case _ => {
            val tf = classLoader[T](x.getOperationClass)
            SparkOperation[T](tf, x, InputReusableRow(null, new RowToReusableMapper(inputSchema, x
              .getOperationInputFields)), OutputReusableRow(null, new RowToReusableMapper(outputSchema, x
              .getOperationOutputFields)), null,null) ::
              transform(xs, ys,zs)
          }
        }
    }
    if (operationList != null) {
      val expressionList=operationList.asScala.map(f=> if(f.getExpression!=null && !f.getExpression.equals("")) {
        new ValidationAPI(f.getExpression,"")
      }
      else
        None
      ).toList

      transform(operationList.asScala.toList,expressionList,extractInitialValues(operationList))
    }
    else
      List()
  }

  private def extractInitialValues(getOperationsList: util.List[Operation]): List[String] = {
    def extract(operationList: List[Operation], stringList: List[String]): List[String] = (operationList, stringList) match {
      case (List(), _)    => stringList
      case (x :: xs, str) => extract(xs, str ++ List(x.getAccumulatorInitialValue))
    }
    if (getOperationsList != null)
      extract(getOperationsList.asScala.toList, List[String]())
    else
      List[String]()
  }

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }

  implicit def arrayToList(arr: Array[String]): ArrayList[String] = {
    val lst = new ArrayList[String]()
    arr.foreach(v => lst.add(v))
    lst
  }

  def getMapSourceFields(mapfields: List[MapField], inSocketId: String): Array[String] =
    mapfields.filter { x => x.getInSocketId.equals(inSocketId) }.map { x => x.getSourceName }.toArray[String]

  def getMapTargetFields(mapfields: List[MapField], inSocketId: String): Array[String] =
    mapfields.filter { x => x.getInSocketId.equals(inSocketId) }.map { x => x.getName }.toArray[String]

  def getPassthroughSourceFields(passthroughfields: List[PassThroughField], inSocketId: String): Array[String] =
    passthroughfields.filter { x => x.getInSocketId.equals(inSocketId) }.map { x => x.getName }.toArray[String]

  def getIndexes(firstSchema: StructType, secondSchema: StructType, fields: Array[String]): Array[(Int, Int)] =
    fields.map { field => (firstSchema.fieldIndex(field), secondSchema.fieldIndex(field)) }

  def getIndexes(firstSchema: StructType, secondSchema: StructType, firstFields: Array[String], secondFields: Array[String]): Array[(Int, Int)] =
    firstFields.zip(secondFields).map { pair => (firstSchema.fieldIndex(pair._1), secondSchema.fieldIndex(pair._2)) }

  def copyFields(input: ReusableRow, output: ReusableRow): Unit = {
    for (index <- 0 until input.getFieldNames.size()) {
      output.setField(index, input.getField(index))
    }
  }

  def getPassThroughFields(inputFields: StructType, passThroughFieldList: List[PassThroughField]): List[String] = {
    val set = new mutable.HashSet[String]()
    passThroughFieldList.foreach(f => {
      inputFields.map(_.name).filter(_.matches(f.getName)).foreach(set.+=(_))
    })
    set.toList
  }

  def getAllOutFields(outSocketList: List[OutSocket], inputFields: List[String]): List[String] = {
    val fields = new ListBuffer[String]
    outSocketList.foreach(outSocket => {
      //Adding Passthrough Fields
      OutSocketUtils.getPassThroughFieldsFromOutSocket(outSocket.getPassThroughFieldsList, inputFields.toArray).foreach(f =>
        fields += f)
      //Adding Map Fields
      outSocket.getMapFieldsList.asScala.foreach(f => fields += f.getName)
      //Adding Operation Fields
      outSocket.getOperationFieldList.asScala.foreach(f => fields += f.getName)
    })
    fields.toList
  }

  def copyFields(input: Row, output: Array[Any], indexes: Array[(Int, Int)]): Unit = {
    indexes.foreach(pair => output(pair._2) = input(pair._1))
  }

}

object Testing {

  def main(args: Array[String]) {
    val passThroughField = List(new PassThroughField("id", "in"), new PassThroughField(".*t.*", "in"), new
        PassThroughField(".*t.*", "in"))

    val st = StructType(List(StructField("city", DataTypes.StringType), StructField("eid", DataTypes.StringType)))
    getPassThroughFields(st, passThroughField).foreach(println)


  }

  def getPassThroughFields(inputFields: StructType, passThroughFieldList: List[PassThroughField]): List[String] = {
    val set = new mutable.HashSet[String]()
    if (passThroughFieldList.map(_.getName).contains("*"))
      inputFields.map(_.name).toList
    else {
      passThroughFieldList.foreach(f => {
        inputFields.map(_.name).filter(_.matches(f.getName)).foreach(set.+=(_))
      })
      set.toList
    }
  }
}

