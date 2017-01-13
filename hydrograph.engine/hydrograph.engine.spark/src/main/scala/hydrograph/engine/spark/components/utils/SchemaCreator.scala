package hydrograph.engine.spark.components.utils

import java.util

import hydrograph.engine.core.component.entity.InputFileXMLEntity
import hydrograph.engine.core.component.entity.base.InputOutputEntityBase
import hydrograph.engine.core.component.entity.elements.SchemaField
import org.apache.spark.sql.Column
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

case class SchemaCreator[T <: InputOutputEntityBase](inputOutputEntityBase: T) {

  private val precision:Int=38 
  
  private val LOG:Logger = LoggerFactory.getLogger(classOf[SchemaCreator[T]])

  def getRelativePath(absPath:String):String = absPath.replace(inputOutputEntityBase.asInstanceOf[InputFileXMLEntity].getAbsoluteXPath,"")

  def extractRelativeXPath():List[String] = {
    def extract(schemaFieldList:List[SchemaField],relativeXPath:List[String]):List[String] = (schemaFieldList,relativeXPath) match {
      case (List(),y) => y
      case (x::xs,y) => extract(xs,List[String](getRelativePath(x.getAbsoluteOrRelativeXPath)) ++ y)
    }
    extract(inputOutputEntityBase.getFieldsList.asScala.toList,List[String]())
  }

  private def createStructFieldsForXMLInputOutputComponents(): Array[StructField] = {
    LOG.trace("In method createStructFieldsForXMLInputOutputComponents() which returns Array[StructField] for Input and Output components")
    val structFields = new Array[StructField](inputOutputEntityBase.getFieldsList.size)
    val strict:Boolean = inputOutputEntityBase.asInstanceOf[InputFileXMLEntity].isStrict
    val relativeXPaths = extractRelativeXPath()

    for (i <- 0 until inputOutputEntityBase.getFieldsList.size()) {
      val schemaField: SchemaField = inputOutputEntityBase.getFieldsList.get(i)
      structFields(i) = StructField(schemaField.getFieldName, getDataType(schemaField), strict)
    }
    LOG.debug("Array of StructField created for XML Components from schema is : " + structFields.mkString)
    structFields
  }

  def makeSchema(): StructType = inputOutputEntityBase match {
    case x if(x.isInstanceOf[InputFileXMLEntity]) => StructType(createStructFieldsForXMLInputOutputComponents())
    case x if(!x.isInstanceOf[InputFileXMLEntity]) => StructType(createStructFields())
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
      case "BigDecimal" => DataTypes.createDecimalType(returnScalePrecision(schemaField.getFieldPrecision), returnScalePrecision(schemaField.getFieldScale))
    }
  }

  private def createStructFields(): Array[StructField] = {
    LOG.trace("In method createStructFields() which returns Array[StructField] for Input and Output components")
    val structFields = new Array[StructField](inputOutputEntityBase.getFieldsList.size)

    for (i <- 0 until inputOutputEntityBase.getFieldsList.size()) {
      val schemaField: SchemaField = inputOutputEntityBase.getFieldsList.get(i)
      structFields(i) = StructField(schemaField.getFieldName, getDataType(schemaField), true)
    }
    LOG.debug("Array of StructField created from schema is : " + structFields.mkString)
    structFields
  }
  
  
  def returnScalePrecision(data:Int):Int={
    if(data== -999) precision else data
  }

  def createSchema(): Array[Column] ={
    LOG.trace("In method createSchema()")
    val fields = inputOutputEntityBase.getFieldsList
    val schema=new Array[Column](fields.size())
    fields.zipWithIndex.foreach{ case(f,i)=> schema(i)=col(f.getFieldName)}
    LOG.debug("Schema created : " + schema.mkString )
    schema
  }

  def getDateFormats(): String = {
    LOG.trace("In method getDateFormats() which returns \\t separated date formats for Date fields")
    var dateFormats: String = ""
    for (i <- 0 until inputOutputEntityBase.getFieldsList.size()) {
      dateFormats += inputOutputEntityBase.getFieldsList.get(i).getFieldFormat + "\t"
    }
    LOG.debug("Date Formats for Date fields : " + dateFormats)
    dateFormats
  }

}

