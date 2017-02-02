package hydrograph.engine.spark.datasource.avro

import org.apache.spark.sql.types._
import java.math.BigDecimal
import java.nio.ByteBuffer
import java.util.ArrayList
import java.util.Arrays
import java.util.Date
import java.util.HashMap
import java.util.Iterator
import java.util.LinkedList
import java.util.Map
import org.apache.avro.AvroRuntimeException
import org.apache.avro.Schema
import org.apache.avro.Schema.Field
import org.apache.avro.Schema.Type
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericData.Fixed
import org.apache.avro.generic.GenericData.Record
import org.apache.hadoop.hive.serde2.avro.AvroSerDe
import org.apache.hadoop.io.BytesWritable
import org.apache.avro.Schema.Type._
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}
import org.slf4j.{Logger, LoggerFactory}
import scala.collection.JavaConversions._
import org.apache.spark.sql.types.StructType
import org.apache.hadoop.io.ByteWritable
import org.apache.hadoop.hive.metastore.api.Decimal
import scala.beans.BeanProperty
import hydrograph.engine.core.constants.Constants

object CustomSparkToAvro {

  var inputFieldsNumber : Int = 0
  var arrayOfScale : Array[Int] = null
  var arrayOfPrecision : Array[Int] = null
  var indexForPrecision: Int = 0;
  var subIndexForPrecision: Int = 0;
  var indexForScale: Int = 0;
  var subIndexForScale: Int = 0;
  
  def generateAvroSchemaFromFieldsAndTypes(recordName: String,
    schemeTypes: StructType,
    fieldPrecision: Array[Int],
    fieldScale : Array[Int]): Schema = {
    var typeOfField: Array[DataType] = new Array[DataType](schemeTypes.size);

    if (schemeTypes.fields.length == 0) {
      throw new IllegalArgumentException("There must be at least one field")
    }
    var schemeTypesSize = 0
    var i = 0
    while (i < schemeTypes.fields.length) {
      typeOfField.update(i, schemeTypes.apply(i).dataType)
      i += 1
      schemeTypesSize += 1
    }
    if (schemeTypesSize != schemeTypes.fields.length) {
      throw new IllegalArgumentException("You must have a schemaType for every field")
    }
    generateSchema(recordName, schemeTypes.fields, typeOfField, 0, fieldPrecision, fieldScale)
  }

  private def generateSchema(recordName: String,
    schemeFields: Array[StructField],
    dataType: Array[DataType],
    depth: Int,
    fieldPrecision: Array[Int],
    fieldScale: Array[Int]): Schema = {
    val fields = new ArrayList[Field]()
    var typeIndex = 0
    while (typeIndex < schemeFields.length) {
      val fieldName = schemeFields.apply(typeIndex).name
      val schema = createAvroSchema(recordName, schemeFields,dataType(typeIndex), depth + 1, fieldPrecision(typeIndex), 
        fieldScale(typeIndex))
        val nullSchema = Schema.create(Type.NULL)
        val schemas = new LinkedList[Schema]() {
  	      add(nullSchema)
          add(schema)
      }
      fields.add(new Field(fieldName, Schema.createUnion(schemas), "", null))
      typeIndex += 1
    }
    if (depth > 0) {
    recordName + depth     
    }
    val schema = Schema.createRecord(recordName, "auto generated", "", false)
    schema.setFields(fields)
    schema}

  private def createAvroSchema(recordName: String,
    schemeFields: Array[StructField],
    fieldTypes: DataType,
    depth: Int,
    fieldPrecision: Int,
    fieldScale: Int): Schema = {
    
    if (fieldTypes.isInstanceOf[DateType] || fieldTypes.isInstanceOf[TimestampType]) {
      AvroSchemaUtils.getSchemaFor("{" + "\"type\":\"" + AvroSerDe.AVRO_LONG_TYPE_NAME +
        "\"," +
        "\"logicalType\":\"" +
        AvroSerDe.DATE_TYPE_NAME +
        "\"}")
    } else if (fieldTypes.isInstanceOf[DecimalType]) {
      var precision: String = null;
      if (String.valueOf(fieldPrecision).equals("-999"))
        precision = "-999";
      else
        precision = String.valueOf(fieldPrecision);
      var scale: String = String.valueOf(fieldScale);
      return AvroSchemaUtils.getSchemaFor("{" + "\"type\":\"bytes\","
        + "\"logicalType\":\"decimal\"," + "\"precision\":"
        +  precision+ "," + "\"scale\":" + scale + "}");
    } else {
      Schema.create(methodTocheckType(fieldTypes))
    }
  }
  private def methodTocheckType(dataType: DataType): Type = {
    dataType match {
      case StringType => STRING
      case ByteType => BYTES
      case BooleanType => BOOLEAN
      case FloatType => FLOAT
      case LongType => LONG
      case DoubleType => DOUBLE
      case IntegerType => INT
      case ShortType => INT
    }
  }

  def setInputFields(input : Int) ={
    inputFieldsNumber = input
    arrayOfPrecision = new Array[Int](inputFieldsNumber)
    arrayOfScale = new Array[Int](inputFieldsNumber)
  }
   def getInputFields() : Int={
    inputFieldsNumber
  }
     
  def getPrecison(): Array[Int] = {
    arrayOfPrecision
  }
  def getScale(): Array[Int] = {
    arrayOfScale
  }
  def setPrecison(componentPrecision: Int) = {
    while (indexForPrecision == subIndexForPrecision) {
      arrayOfPrecision(indexForPrecision) = componentPrecision
    		  indexForPrecision += 1
    }
    subIndexForPrecision += 1
  }
  def setScale(componentScale: Int) = {
    while (indexForScale == subIndexForScale) {
      arrayOfScale(indexForScale) = componentScale
      indexForScale += 1
    }
    subIndexForScale += 1
  }
}