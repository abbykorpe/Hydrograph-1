package hydrograph.engine.spark.components.utils

import hydrograph.engine.core.component.entity.base.InputOutputEntityBase
import hydrograph.engine.core.component.entity.elements.SchemaField
import org.apache.spark.sql.types.{ DataType, DataTypes, StructField, StructType }

case class SchemaCreator[T <: InputOutputEntityBase](inputOutputEntityBase: T) {

  def makeSchema(): StructType = {
    StructType(createStructFields())
  }

  def getTypeNameFromDataType(dataType: String): String = {
    Class.forName(dataType).getSimpleName
  }

  def getDataType(schemaField: SchemaField): DataType = {
    getTypeNameFromDataType(schemaField.getFieldDataType) match {
      case "Integer" => DataTypes.IntegerType
      case "String" => DataTypes.StringType
      case "Long" => DataTypes.LongType
      case "Short" => DataTypes.ShortType
      case "Boolean" => DataTypes.BooleanType
      case "Float" => DataTypes.FloatType
      case "Double" => DataTypes.DoubleType
      case "Date" if (schemaField.getFieldFormat.matches(".*[H|m|s|S].*")) => DataTypes.TimestampType
      case "Date" => DataTypes.DateType
      case "BigDecimal" => DataTypes.createDecimalType(schemaField.getFieldPrecision, schemaField.getFieldScale)
    }
  }

  private def createStructFields(): Array[StructField] = {
    val structFields = new Array[StructField](inputOutputEntityBase.getFieldsList.size)

    for (i <- 0 until inputOutputEntityBase.getFieldsList.size()) {
      val schemaField: SchemaField = inputOutputEntityBase.getFieldsList.get(i)
      structFields(i) = StructField(schemaField.getFieldName, getDataType(schemaField), true)
    }
    structFields
  }
}

