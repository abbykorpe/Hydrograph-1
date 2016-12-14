package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.OutputFileFixedWidthEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, SaveMode}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class OutputFileFixedWidthComponent(outputFileFixedWidthEntity: OutputFileFixedWidthEntity, cp:
BaseComponentParams) extends SparkFlow {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[OutputFileFixedWidthComponent])
  def createSchema(fields:util.List[SchemaField]): Array[Column] ={
    val schema=new Array[Column](fields.size())
    fields.zipWithIndex.foreach{ case(f,i)=> schema(i)=col(f.getFieldName)}
    schema
  }

  override def execute() = {
    val dateFormats=getDateFormats()
    val fieldsLen=new Array[Int](outputFileFixedWidthEntity.getFieldsList.size())

    outputFileFixedWidthEntity.getFieldsList().asScala.zipWithIndex.foreach{ case(s,i)=>
      fieldsLen(i)=s.getFieldLength
    }

    cp.getDataFrame().select(createSchema(outputFileFixedWidthEntity.getFieldsList):_*).write
      .option("charset", outputFileFixedWidthEntity.getCharset)
      .option("length",fieldsLen.mkString(","))
      .option("strict", outputFileFixedWidthEntity.isStrict)
      .option("safe", outputFileFixedWidthEntity.isSafe)
      .option("dateFormats", dateFormats)
      .mode(SaveMode.Overwrite)
      .format("hydrograph.engine.spark.fixedwidth.datasource")
      .save(outputFileFixedWidthEntity.getPath)

    LOG.info("Component Id: '"+ outputFileFixedWidthEntity.getComponentId
      +"' in Batch: " + outputFileFixedWidthEntity.getBatch
      + " having schema: [ " + outputFileFixedWidthEntity.getFieldsList.asScala.mkString(",")
      + " ] at Path: " + outputFileFixedWidthEntity.getPath)
  }

  def getDateFormats(): String = {

    var dateFormats: String = ""
    for (i <- 0 until outputFileFixedWidthEntity.getFieldsList.size()) {
      dateFormats += outputFileFixedWidthEntity.getFieldsList.get(i).getFieldFormat + "\t"
    }

    return dateFormats
  }

}
