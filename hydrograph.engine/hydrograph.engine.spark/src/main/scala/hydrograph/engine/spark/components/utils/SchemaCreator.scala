package hydrograph.engine.spark.components.utils

import hydrograph.engine.core.component.entity.base.InputOutputEntityBase
import hydrograph.engine.core.component.entity.elements.SchemaField
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}

/**
  * Created by gurdits on 10/15/2016.
  */
case class SchemaCreator[T <: InputOutputEntityBase](inputOutputEntityBase: T) {

  def makeSchema(): StructType = {
    StructType(createStructFields())
  }


  def getTypeNameFromDataType(dataType: String): String = {
    Class.forName(dataType).getSimpleName
  }

  def getDataType(dataType: String):DataType = {
    getTypeNameFromDataType(dataType) match {
      case "Integer" => DataTypes.IntegerType
      case "String" => DataTypes.StringType
      case "Long" => DataTypes.LongType
    }
  }


  private def createStructFields(): Array[StructField] = {
    val sizeOfFieldList: Int = inputOutputEntityBase.getFieldsList.size
    val structFields = new Array[StructField](inputOutputEntityBase.getFieldsList.size)

    var i: Int = 0
    while (i < sizeOfFieldList) {
      val schemaField: SchemaField = inputOutputEntityBase.getFieldsList.get(i)
      structFields(i) = StructField(schemaField.getFieldName, getDataType(schemaField.getFieldDataType), true)
      i+=1
    }
    structFields
  }
}

