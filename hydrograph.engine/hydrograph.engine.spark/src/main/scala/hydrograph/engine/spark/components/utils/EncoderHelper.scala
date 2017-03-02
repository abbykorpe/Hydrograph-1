/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.engine.spark.components.utils

import hydrograph.engine.core.component.entity.elements.SchemaField
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}

import scala.collection.mutable.ListBuffer

/**
  * The Class EncoderHelper.
  *
  * @author Bitwise
  *
  */
class EncoderHelper extends Serializable {

  def getDummy(): StructType = {
    StructType(List(StructField("count1", DataTypes.IntegerType, true), StructField("new_city", DataTypes.StringType, true), StructField("id", DataTypes.IntegerType, true), StructField("name", DataTypes.StringType, true)))
  }

  def getDataType(schema: SchemaField): DataType = {
    Class.forName(schema.getFieldDataType).getSimpleName match {
      case "Integer" => DataTypes.IntegerType
      case "String" => DataTypes.StringType
      case "Long" => DataTypes.LongType
      case "Short" => DataTypes.ShortType
      case "Boolean" => DataTypes.BooleanType
      case "Float" => DataTypes.FloatType
      case "Double" => DataTypes.DoubleType
      case "Date" if (schema.getFieldFormat.matches(".*[H|m|s|S].*")) => DataTypes.TimestampType
      case "Date" => DataTypes.DateType
      case "BigDecimal" => DataTypes.createDecimalType(checkPrecision(schema.getFieldPrecision),schema.getFieldScale)
    }
  }
 def checkPrecision(precision:Int):Int={
    if(precision== -999) 38 else precision
  }
  
  def getStructFields(schemaFields: Array[SchemaField]): StructType ={
    val structFields = new Array[StructField](schemaFields.size)
    schemaFields.zipWithIndex.foreach(s=>{
      structFields(s._2)= new StructField(s._1.getFieldName,getDataType(s._1))
    })
    StructType(structFields)
  }

  def getStructFieldType(fieldName: String, schemaFields: Array[SchemaField]): DataType = {
    getDataType(schemaFields.filter(s => s.getFieldName.equals(fieldName))(0))
  }

  def getEncoder(outFields: ListBuffer[String], schemaFields: Array[SchemaField]): StructType = {
    val structFields = new Array[StructField](outFields.size)
    outFields.zipWithIndex.foreach(f => {
      structFields(f._2) = new StructField(f._1, getStructFieldType(f._1, schemaFields), true)
    })
    StructType(structFields)
  }

  def getEncoder(outFields: List[String], schemaFields: Array[SchemaField]): StructType = {
    val structFields = new Array[StructField](outFields.size)
    outFields.zipWithIndex.foreach(f => {
      structFields(f._2) = new StructField(f._1, getStructFieldType(f._1, schemaFields), true)
    })
    StructType(structFields)
  }

}
object EncoderHelper {
  def apply(): EncoderHelper = {
    new EncoderHelper()
  }
}
