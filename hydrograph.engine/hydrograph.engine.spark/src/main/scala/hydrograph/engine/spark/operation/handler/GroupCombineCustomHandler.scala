package hydrograph.engine.spark.operation.handler

import hydrograph.engine.spark.core.reusablerow._
import hydrograph.engine.transformation.userfunctions.base.{GroupCombineTransformBase, BufferField, BufferSchema}
import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{StructField, _}

import scala.collection.JavaConversions._

case class GroupCombineCustomHandler(aggregatorTransform: GroupCombineTransformBase, inSchema: StructType, outSchema: StructType, isDeterministic: Boolean) extends UserDefinedAggregateFunction {

  def inputSchema: StructType = inSchema

  def bufferSchema = createBufferSchema(aggregatorTransform)

  def dataType: DataType = outSchema

  def deterministic = isDeterministic

  val bufSchemaVal = bufferSchema

  def initialize(buffer: MutableAggregationBuffer) = {
    var  brr = BufferReusableRow(buffer, new RowToReusableMapper(bufSchemaVal, bufSchemaVal.fieldNames))
    aggregatorTransform.initialize(brr)
  }

  def update(buffer: MutableAggregationBuffer, input: Row) = {
      var brr = BufferReusableRow(buffer, new RowToReusableMapper(bufSchemaVal, bufSchemaVal.fieldNames))
      aggregatorTransform.update(brr, InputReusableRow(input, new RowToReusableMapper(inSchema, inSchema.fieldNames)))
        }

  def merge(buffer1: MutableAggregationBuffer, buffer2: Row) = {
      val brr = BufferReusableRow(buffer1, new RowToReusableMapper(bufSchemaVal, bufSchemaVal.fieldNames))
      val irr = InputReusableRow(buffer2, new RowToReusableMapper(bufSchemaVal, bufSchemaVal.fieldNames))
      aggregatorTransform.merge(brr, irr)
  }

  def evaluate(buffer: Row) = {
    val output = new Array[Any](outSchema.size)
    val orr: OutputReusableRow = OutputReusableRow(output, new RowToReusableMapper(dataType.asInstanceOf[StructType], dataType.asInstanceOf[StructType].fieldNames))
    val irr = InputReusableRow(buffer, new RowToReusableMapper(bufferSchema, bufferSchema.fieldNames))
    aggregatorTransform.evaluate(irr, orr)
    Row.fromSeq(output)
  }

  def createBufferSchema(aggregatorTransformBase: GroupCombineTransformBase): StructType = {
    val bufferSchema: BufferSchema = aggregatorTransformBase.initBufferSchema()
    var bufferFieldMap: Map[String, BufferField] = Map()
        for(bufferField <- bufferSchema.getSchema){
          bufferFieldMap += bufferField._1 -> bufferField._2
        }
      bufferFieldMap

    val array: Array[StructField] = new Array[StructField](bufferFieldMap.size())
    var i: Int = 0
    for (bs <- bufferFieldMap.values) {
      array(i) = new StructField(bs.getFieldName, getSparkDataType(bs.getFieldType, bs.getFieldFormat, bs.getFieldPrecision, bs.getFieldScale))
      i = i + 1
    }
    StructType(array)
  }

  def getSparkDataType(dataType: String, format: String, precision: Int, scale: Int): DataType = dataType match {
    case "Integer" => DataTypes.IntegerType
    case "String" => DataTypes.StringType
    case "Long" => DataTypes.LongType
    case "Short" => DataTypes.ShortType
    case "Boolean" => DataTypes.BooleanType
    case "Float" => DataTypes.FloatType
    case "Double" => DataTypes.DoubleType
    case "Date" if format.matches(".*[H|m|s|S].*") => DataTypes.TimestampType
    case "Date" => DataTypes.DateType
    case "BigDecimal" => DataTypes.createDecimalType(checkPrecision(precision), scale)
  }

  def checkPrecision(precision: Int): Int = if (precision == -999) 38 else precision

}
