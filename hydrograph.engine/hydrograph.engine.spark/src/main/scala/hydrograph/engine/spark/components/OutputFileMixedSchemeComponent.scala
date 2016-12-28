package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.OutputFileMixedSchemeEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.core.constants.Constants
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{AnalysisException, Column, SaveMode}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class OutputFileMixedSchemeComponent(outputFileMixedSchemeEntity: OutputFileMixedSchemeEntity, cp:
BaseComponentParams) extends SparkFlow with Serializable {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[OutputFileMixedSchemeComponent])

  def extractLengthsAndDelimiters(getFieldsList: util.List[SchemaField]):String = {
    def extract(schemafields:List[SchemaField],lengthsAndDelimiters:List[String]):List[String] = (schemafields,lengthsAndDelimiters) match {
      case (List(),y) => y
      case (x::xs,y) => extract(xs,(y ++ List(x.getFieldLengthDelimiter)))
    }
    extract(getFieldsList.asScala.toList,List[String]()).mkString(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR)
  }

  def extractLengthsAndDelimitersType(getFieldsList: util.List[SchemaField]):String = {
    def extract(schemafields:List[SchemaField],lengthsAndDelimiters:List[String]):List[String] = (schemafields,lengthsAndDelimiters) match {
      case (List(),y) => y
      case (x::xs,y) => extract(xs,(y ++ List(x.getTypeFieldLengthDelimiter.toString)))
    }
    extract(getFieldsList.asScala.toList,List[String]()).mkString(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR)
  }

  def createSchema(fields:util.List[SchemaField]): Array[Column] ={
    LOG.trace("In method createSchema()")
    val schema=new Array[Column](fields.size())
    fields.zipWithIndex.foreach{ case(f,i)=> schema(i)=col(f.getFieldName)}
    LOG.debug("Schema created for Output File Delimited Component : " + schema.mkString )
    schema
  }

  override def execute() = {
    LOG.trace("In method execute()")
   val dateFormats=getDateFormats()
   try {
     cp.getDataFrame().select(createSchema(outputFileMixedSchemeEntity.getFieldsList): _*).write
       .option("quote", outputFileMixedSchemeEntity.getQuote)
       .option("charset", outputFileMixedSchemeEntity.getCharset)
       .option("safe", outputFileMixedSchemeEntity.getSafe)
       .option("strict", outputFileMixedSchemeEntity.getStrict)
       .option("dateFormats", dateFormats)
       .option("filler", ' ')
       .option("lengthsAndDelimiters", extractLengthsAndDelimiters(outputFileMixedSchemeEntity.getFieldsList))
       .option("lengthsAndDelimitersType", extractLengthsAndDelimitersType(outputFileMixedSchemeEntity.getFieldsList))
       .mode(SaveMode.Overwrite)
       .format("hydrograph.engine.spark.datasource.mixedScheme")
       .save(outputFileMixedSchemeEntity.getPath)
   } catch {
     case e: AnalysisException if (e.getMessage().matches("(.*)cannot resolve(.*)given input columns(.*)"))=>
       LOG.error("Error in Output File Delimited Component "+ outputFileMixedSchemeEntity.getComponentId, e)
       throw new RuntimeException("Error in Output File Delimited Component "
         + outputFileMixedSchemeEntity.getComponentId, e )
     case e:Exception =>
       LOG.error("Error in Output File Delimited Component "+ outputFileMixedSchemeEntity.getComponentId, e)
       throw new RuntimeException("Error in Output File Delimited Component "
         + outputFileMixedSchemeEntity.getComponentId, e)
   }
    LOG.info("Created Output File Delimited Component "+ outputFileMixedSchemeEntity.getComponentId
      + " in Batch "+ outputFileMixedSchemeEntity.getBatch +" with path " + outputFileMixedSchemeEntity.getPath)
    LOG.debug("Component Id: '"+ outputFileMixedSchemeEntity.getComponentId
      +"' in Batch: " + outputFileMixedSchemeEntity.getBatch
      + " having schema: [ " + outputFileMixedSchemeEntity.getFieldsList.asScala.mkString(",")
      + " ] with quote: " + outputFileMixedSchemeEntity.getQuote
      + " strict as " + outputFileMixedSchemeEntity.getStrict + " safe as " + outputFileMixedSchemeEntity.getSafe
      + " at Path: " + outputFileMixedSchemeEntity.getPath)
  }

  def getDateFormats(): String = {
      LOG.trace("In method getDateFormats() which returns \\t separated date formats for Date fields")
      var dateFormats: String = ""
      for (i <- 0 until outputFileMixedSchemeEntity.getFieldsList.size()) {
        dateFormats += outputFileMixedSchemeEntity.getFieldsList.get(i).getFieldFormat + "\t"
      }
      LOG.debug("Date Formats for Date fields : " + dateFormats)
      dateFormats
  }

}
