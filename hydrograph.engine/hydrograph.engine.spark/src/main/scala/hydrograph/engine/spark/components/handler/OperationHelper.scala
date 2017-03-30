package hydrograph.engine.spark.components.handler

import java.util.ArrayList

import hydrograph.engine.core.component.entity.elements.{MapField, Operation, PassThroughField}
import hydrograph.engine.core.custom.exceptions.{FieldNotFoundException, UserFunctionClassNotFoundException}
import hydrograph.engine.expression.api.ValidationAPI
import hydrograph.engine.spark.components.utils.{EncoderHelper, SchemaMisMatchException}
import hydrograph.engine.spark.core.reusablerow.{InputReusableRow, OutputReusableRow, RowToReusableMapper}
import hydrograph.engine.transformation.userfunctions.base.ReusableRow
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.reflect.ClassTag

/**
  * The Class SparkOperation.
  *
  * @author Bitwise
  *
  */
case class SparkOperation[T](baseClassInstance: T, operationEntity: Operation, inputRow: InputReusableRow, outputRow:
OutputReusableRow, validatioinAPI: ValidationAPI, initalValue: String, operationOutFields: Array[String], fieldName: Array[String], fieldType: Array[String])

trait OperationHelper[T] {

  def initializeOperationList[U](operationList: java.util.List[Operation], inputSchema: StructType,
                                 outputSchema:
                                 StructType)(implicit ct: ClassTag[U]): List[SparkOperation[T]] = {


    def populateOperation(operationList: List[Operation]): List[SparkOperation[T]] =
      (operationList) match {
        case (List()) => List()
        case (x :: xs) if x.isExpressionPresent => {
          val tf = classLoader[T](ct.runtimeClass.getCanonicalName)
          val fieldName = new Array[String](x.getOperationInputFields.length)
          val fieldType = new Array[String](x.getOperationInputFields.length)

          x.getOperationInputFields.zipWithIndex.foreach(s => {
            fieldName(s._2) = inputSchema(s._1).name;
            fieldType(s._2) = inputSchema(s._1).dataType.typeName
          })

          var so: SparkOperation[T] = null
          try {
            so = SparkOperation[T](tf, x, InputReusableRow(null, new RowToReusableMapper(inputSchema, x
              .getOperationInputFields)), getOutputReusableRow(outputSchema, x), new ValidationAPI(x.getExpression, "")
              , x.getAccumulatorInitialValue, x.getOperationOutputFields, fieldName, fieldType)
          } catch {
            case e: FieldNotFoundException => throw new FieldNotFoundException("\nOperation Id:[\"" + x.getOperationId + "\"]" + e.getMessage)
          }
          so :: populateOperation(xs)
        }

        case (x :: xs) => {
          var so: SparkOperation[T] = null
          try {
            val tf = classLoader[T](x.getOperationClass)
            so = SparkOperation[T](tf, x, InputReusableRow(null, new RowToReusableMapper(inputSchema, x
              .getOperationInputFields)), getOutputReusableRow(outputSchema, x), null, null, null, null, null)
          } catch {
            case e: ClassNotFoundException => throw new UserFunctionClassNotFoundException("\nOperation Id:[\"" + x.getOperationId + "\"]" + e.getMessage)
            case e: FieldNotFoundException => throw new FieldNotFoundException("\nOperation Id:[\"" + x.getOperationId + "\"]" + e.getMessage)
          }
          so :: populateOperation(xs)
        }
      }

    if (operationList != null) {
      populateOperation(operationList.asScala.toList)
    }
    else
      List()

  }

  def getOutputReusableRow[U](outputSchema: StructType, x: Operation): OutputReusableRow = {
    if (x
      .getOperationOutputFields != null) OutputReusableRow(null, new RowToReusableMapper(outputSchema, x
      .getOperationOutputFields))
    else null
  }

  def classLoader[T](className: String): T = {
    try {
      val clazz = Class.forName(className).getDeclaredConstructors
      clazz(0).setAccessible(true)
      clazz(0).newInstance().asInstanceOf[T]
    } catch {
      case e: ClassNotFoundException => throw new UserFunctionClassNotFoundException("\nUser Function:[\"" + className + "\"] not found.")
    }

  }

  def initializeOperationList[U](operationList: java.util.List[Operation], inputSchema: StructType)(implicit ct: ClassTag[U]): List[SparkOperation[T]] = {

    def populateOperation(operationList: List[Operation]): List[SparkOperation[T]] =
      (operationList) match {
        case (List()) => List()
        case (x :: xs) if x.isExpressionPresent => {
          val tf = classLoader[T](ct.runtimeClass.getCanonicalName)
          val fieldName = new Array[String](x.getOperationInputFields.length)
          val fieldType = new Array[String](x.getOperationInputFields.length)

          x.getOperationInputFields.zipWithIndex.foreach(s => {
            fieldName(s._2) = inputSchema(s._1).name;
            fieldType(s._2) = inputSchema(s._1).dataType.typeName
          })

          val (in, out) = getReusableRows(x, inputSchema)
          SparkOperation[T](tf, x, in, out, new ValidationAPI(x.getExpression, ""), x.getAccumulatorInitialValue, x.getOperationOutputFields, fieldName, fieldType) ::
            populateOperation(xs)
        }
        case (x :: xs) => {
          val tf = classLoader[T](x.getOperationClass)
          val (in, out) = getReusableRows(x, inputSchema)
          SparkOperation[T](tf, x, in, out, null, null, null, null, null) ::
            populateOperation(xs)
        }
      }

    if (operationList != null) {
      populateOperation(operationList.asScala.toList)
    } else
      List()
  }

  def getReusableRows(op: Operation, inputSchema: StructType): (InputReusableRow, OutputReusableRow) = {
    val out = if (op.getOperationFields != null) OutputReusableRow(null, new RowToReusableMapper(EncoderHelper().getEncoder(op.getOperationFields), op
      .getOperationOutputFields))
    else OutputReusableRow(null, new RowToReusableMapper(new StructType(), Array[String]()))
    val in = if (op.getOperationInputFields != null) InputReusableRow(null, new RowToReusableMapper(getPartialSchema(inputSchema, op.getOperationInputFields), op
      .getOperationInputFields))
    else InputReusableRow(null, new RowToReusableMapper(new StructType(), Array[String]()))
    (in, out)
  }

  def getPartialSchema(schema: StructType, requiredFields: Array[String]): StructType = {
    var outSchema = new StructType();
    requiredFields.foreach(field => {
      try {
        outSchema = outSchema.add(schema(field))
      } catch {
        case e: IllegalArgumentException => throw new FieldNotFoundException("\nField:[\"" + field + "\"] not found.")
      }
    })
    outSchema
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

  def getIndexes(firstSchema: StructType, fields: Array[String]): Array[(Int, Int)] =
    try {
      fields.zipWithIndex.map { field => (firstSchema.fieldIndex(field._1), field._2) }
    } catch {
      case e: SchemaMisMatchException => throw new SchemaMisMatchException

    }


  def getIndexes(firstSchema: StructType, secondSchema: StructType, firstFields: Array[String], secondFields: Array[String]): Array[(Int, Int)] =
    firstFields.zip(secondFields).map { pair => (firstSchema.fieldIndex(pair._1), secondSchema.fieldIndex(pair._2)) }

  def copyFields(input: ReusableRow, output: ReusableRow): Unit = {
    for (index <- 0 until input.getFieldNames.size()) {
      output.setField(index, input.getField(index))
    }
  }

  def copyFields(input: Row, output: Array[Any], indexes: Array[(Int, Int)]): Unit = {
    indexes.foreach(pair => output(pair._2) = input.get(pair._1))
  }


}

