package hydrograph.engine.spark.components.handler

import scala.collection.JavaConverters.asScalaBufferConverter
import org.apache.spark.sql.types.StructType
import hydrograph.engine.core.component.entity.elements.Operation
import hydrograph.engine.spark.core.reusablerow.RowToReusableMapper
import java.util.ArrayList
import hydrograph.engine.core.component.entity.elements.MapField
import hydrograph.engine.core.component.entity.elements.PassThroughField
import hydrograph.engine.spark.core.reusablerow.InputReusableRow
import hydrograph.engine.transformation.userfunctions.base.ReusableRow
import org.apache.spark.sql.Row
import hydrograph.engine.spark.core.reusablerow.OutputReusableRow

case class SparkOperation[T](baseClassInstance: T, operationEntity: Operation, inputRow: InputReusableRow, outputRow: OutputReusableRow)

trait OperationHelper[T] {

  def initializeTransformList(operationList: java.util.List[Operation], inputSchema: StructType, outputSchema: StructType): List[SparkOperation[T]] = {

    def transform(operationList: List[Operation]): List[SparkOperation[T]] = (operationList) match {
      case (List()) => List()
      case (x :: xs) =>

        val transformBase: T = classLoader[T](x.getOperationClass)

        SparkOperation[T](transformBase, x, InputReusableRow(null, new RowToReusableMapper(inputSchema, x.getOperationInputFields)), OutputReusableRow(null, new RowToReusableMapper(outputSchema, x.getOperationOutputFields))) ::
          transform(xs)
    }
    if (operationList != null)
      transform(operationList.asScala.toList)
    else
      List()
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

  def copyFields(input: Row, output: Array[Any], indexes: Array[(Int, Int)]): Unit = {
    indexes.foreach(pair => output(pair._2) = input(pair._1))
  }

}

