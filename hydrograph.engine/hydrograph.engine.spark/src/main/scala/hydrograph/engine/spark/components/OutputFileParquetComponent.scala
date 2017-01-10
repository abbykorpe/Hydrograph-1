package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.OutputFileParquetEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, SaveMode}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class OutputFileParquetComponent(oFileParquetEntity: OutputFileParquetEntity, componentsParams: BaseComponentParams)
    extends SparkFlow {

  val LOG: Logger = LoggerFactory.getLogger(classOf[OutputFileParquetComponent])

  def createSchema(fields: util.List[SchemaField]): Array[Column] = {
    val schema = new Array[Column](fields.size())
    fields.zipWithIndex.foreach { case (f, i) => schema(i) = col(f.getFieldName) }
    LOG.debug("Schema created for Output File Parquet Component : " + schema.mkString)
    schema
  }

  override def execute() = {
    try {

      val filePathToWrite = oFileParquetEntity.getPath()
      val fieldList = oFileParquetEntity.getFieldsList.asScala

      fieldList.foreach { field => LOG.debug("Field name '" + field.getFieldName + "for Component " + oFileParquetEntity.getComponentId) }

      if (oFileParquetEntity.isOverWrite())
        componentsParams.getDataFrame().select(createSchema(oFileParquetEntity.getFieldsList): _*).write.mode(SaveMode.Overwrite).parquet(filePathToWrite)
      else
        componentsParams.getDataFrame().select(createSchema(oFileParquetEntity.getFieldsList): _*).write.mode(SaveMode.Append).parquet(filePathToWrite)

      LOG.debug("Created Output File Parquet Component '" + oFileParquetEntity.getComponentId + "' in Batch" + oFileParquetEntity.getBatch
        + ", file path " + oFileParquetEntity.getPath)
    } catch {
      case ex: RuntimeException =>
        LOG.error("Error in Output  File Parquet component '" + oFileParquetEntity.getComponentId + "', Error" + ex.getMessage, ex)
        throw ex
    }
  }
}