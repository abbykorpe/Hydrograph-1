package hydrograph.engine.spark.components

import java.util


import hydrograph.engine.core.component.entity.OutputFileDelimitedEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, SaveMode}
import org.slf4j.{LoggerFactory, Logger}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class OutputFileDelimitedComponent(outputFileDelimitedEntity: OutputFileDelimitedEntity,cp:
BaseComponentParams) extends SparkFlow {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[OutputFileDelimitedComponent])

  def createSchema(fields:util.List[SchemaField]): Array[Column] ={
    val schema=new Array[Column](fields.size())
    fields.zipWithIndex.foreach{ case(f,i)=> schema(i)=col(f.getFieldName)}
    schema
  }

  override def execute() = {

   val dateFormats=getDateFormats()
   
    cp.getDataFrame().select(createSchema(outputFileDelimitedEntity.getFieldsList):_*).write
      .option("delimiter", outputFileDelimitedEntity.getDelimiter)
      .option("quote", outputFileDelimitedEntity.getQuote)
      .option("header", outputFileDelimitedEntity.getHasHeader)
      .option("charset", outputFileDelimitedEntity.getCharset)
      .option("strict", outputFileDelimitedEntity.isStrict)
      .option("safe", outputFileDelimitedEntity.getSafe)
      .option("dateFormats", dateFormats)
      .mode(SaveMode.Overwrite)
      .format("hydrograph.engine.spark.delimited.datasource")
      .save(outputFileDelimitedEntity.getPath)
    
    LOG.info("Component Id: '"+ outputFileDelimitedEntity.getComponentId
      +"' in Batch: " + outputFileDelimitedEntity.getBatch
      + " having schema: [ " + outputFileDelimitedEntity.getFieldsList.asScala.mkString(",")
      + " ] with delimiter: " + outputFileDelimitedEntity.getDelimiter + " and quote: " + outputFileDelimitedEntity.getQuote
      + " at Path: " + outputFileDelimitedEntity.getPath)
  }
  
  def getDateFormats(): String =
    {

      var dateFormats: String = ""
      for (i <- 0 until outputFileDelimitedEntity.getFieldsList.size()) {
        dateFormats += outputFileDelimitedEntity.getFieldsList.get(i).getFieldFormat + "\t"
      }

      return dateFormats
    }
}
