package hydrograph.engine.spark.components

import java.util


import hydrograph.engine.core.component.entity.OutputFileDelimitedEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{AnalysisException, Column, SaveMode}
import org.slf4j.{LoggerFactory, Logger}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class OutputFileDelimitedComponent(outputFileDelimitedEntity: OutputFileDelimitedEntity,cp:
BaseComponentParams) extends SparkFlow with Serializable {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[OutputFileDelimitedComponent])

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
     cp.getDataFrame().select(createSchema(outputFileDelimitedEntity.getFieldsList): _*).write
       .option("delimiter", outputFileDelimitedEntity.getDelimiter)
       .option("quote", outputFileDelimitedEntity.getQuote)
       .option("header", outputFileDelimitedEntity.getHasHeader)
       .option("charset", outputFileDelimitedEntity.getCharset)
       .option("strict", outputFileDelimitedEntity.isStrict)
       .option("safe", outputFileDelimitedEntity.getSafe)
       .option("dateFormats", dateFormats)
       .mode(SaveMode.Overwrite)
       .format("hydrograph.engine.spark.datasource.delimited")
       .save(outputFileDelimitedEntity.getPath)
   } catch {
     case e: AnalysisException if (e.getMessage().matches("(.*)cannot resolve(.*)given input columns(.*)"))=>
       LOG.error("Error in Output File Delimited Component "+ outputFileDelimitedEntity.getComponentId, e)
       throw new RuntimeException("Error in Output File Delimited Component "
         + outputFileDelimitedEntity.getComponentId, e )
     case e:Exception =>
       LOG.error("Error in Output File Delimited Component "+ outputFileDelimitedEntity.getComponentId, e)
       throw new RuntimeException("Error in Output File Delimited Component "
         + outputFileDelimitedEntity.getComponentId, e)
   }
    LOG.info("Created Output File Delimited Component "+ outputFileDelimitedEntity.getComponentId
      + " in Batch "+ outputFileDelimitedEntity.getBatch +" with path " + outputFileDelimitedEntity.getPath)
    LOG.debug("Component Id: '"+ outputFileDelimitedEntity.getComponentId
      +"' in Batch: " + outputFileDelimitedEntity.getBatch
      + " having schema: [ " + outputFileDelimitedEntity.getFieldsList.asScala.mkString(",")
      + " ] with delimiter: " + outputFileDelimitedEntity.getDelimiter + " and quote: " + outputFileDelimitedEntity.getQuote
      + " strict as " + outputFileDelimitedEntity.isStrict + " safe as " + outputFileDelimitedEntity.getSafe
      + " at Path: " + outputFileDelimitedEntity.getPath)
  }
  
  def getDateFormats(): String = {
      LOG.trace("In method getDateFormats() which returns \\t separated date formats for Date fields")
      var dateFormats: String = ""
      for (i <- 0 until outputFileDelimitedEntity.getFieldsList.size()) {
        dateFormats += outputFileDelimitedEntity.getFieldsList.get(i).getFieldFormat + "\t"
      }
      LOG.debug("Date Formats for Date fields : " + dateFormats)
      dateFormats
  }

}
