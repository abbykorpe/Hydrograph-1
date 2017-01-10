package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.OutputFileFixedWidthEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{AnalysisException, Column, SaveMode}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class OutputFileFixedWidthComponent(outputFileFixedWidthEntity: OutputFileFixedWidthEntity, cp:
BaseComponentParams) extends SparkFlow with Serializable {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[OutputFileFixedWidthComponent])
  /*def createSchema(fields:util.List[SchemaField]): Array[Column] ={
    LOG.trace("In method createSchema()")
    val schema=new Array[Column](fields.size())
    fields.zipWithIndex.foreach{ case(f,i)=> schema(i)=col(f.getFieldName)}
    LOG.debug("Schema created for Output File Fixed Width Component : " + schema.mkString )
    schema
  }*/

  override def execute() = {
    LOG.trace("In method execute()")
    val schemaCreator = SchemaCreator(outputFileFixedWidthEntity)
//    val dateFormats=schemaCreator.getDateFormats()
    val fieldsLen=new Array[Int](outputFileFixedWidthEntity.getFieldsList.size())

    outputFileFixedWidthEntity.getFieldsList().asScala.zipWithIndex.foreach{ case(s,i)=>
      fieldsLen(i)=s.getFieldLength
    }

    try {
    cp.getDataFrame().select(schemaCreator.createSchema():_*).write
      .option("charset", outputFileFixedWidthEntity.getCharset)
      .option("length",fieldsLen.mkString(","))
      .option("strict", outputFileFixedWidthEntity.isStrict)
      .option("safe", outputFileFixedWidthEntity.isSafe)
      .option("dateFormats", schemaCreator.getDateFormats)
      .mode( if (outputFileFixedWidthEntity.isOverWrite) SaveMode.Overwrite else SaveMode.ErrorIfExists )
      .format("hydrograph.engine.spark.datasource.fixedwidth")
      .save(outputFileFixedWidthEntity.getPath)
    } catch {
      case e: AnalysisException if (e.getMessage().matches("(.*)cannot resolve(.*)given input columns(.*)"))=>
        LOG.error("Error in Output File Fixed Width Component "+ outputFileFixedWidthEntity.getComponentId, e)
        throw new RuntimeException("Error in Output File Fixed Width Component "
          + outputFileFixedWidthEntity.getComponentId, e )
      case e:Exception =>
        LOG.error("Error in Output File Fixed Width Component "+ outputFileFixedWidthEntity.getComponentId, e)
        throw new RuntimeException("Error in Output File Fixed Width Component "
          + outputFileFixedWidthEntity.getComponentId, e)
    }
    LOG.info("Created Output File Fixed Width Component "+ outputFileFixedWidthEntity.getComponentId
      + " in Batch "+ outputFileFixedWidthEntity.getBatch +" with path " + outputFileFixedWidthEntity.getPath)
    LOG.info("Component Id: '"+ outputFileFixedWidthEntity.getComponentId
      +"' in Batch: " + outputFileFixedWidthEntity.getBatch
      + " having schema: [ " + outputFileFixedWidthEntity.getFieldsList.asScala.mkString(",")
      + " ] at Path: " + outputFileFixedWidthEntity.getPath)
  }

 /* def getDateFormats(): String = {
    LOG.trace("In method getDateFormats() which returns \\t separated date formats for Date fields")
    var dateFormats: String = ""
    for (i <- 0 until outputFileFixedWidthEntity.getFieldsList.size()) {
      dateFormats += outputFileFixedWidthEntity.getFieldsList.get(i).getFieldFormat + "\t"
    }
    LOG.debug("Date Formats for Date fields : " + dateFormats)
    dateFormats
  }
*/
}
