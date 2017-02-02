package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.OutputFileParquetEntity
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.{ Column, SaveMode }
import org.slf4j.{ Logger, LoggerFactory }
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class OutputFileParquetComponent(oFileParquetEntity: OutputFileParquetEntity, componentsParams: BaseComponentParams)
    extends SparkFlow {

  val LOG: Logger = LoggerFactory.getLogger(classOf[OutputFileParquetComponent])

  override def execute() = {
    try {

      val schemaCreator = SchemaCreator(oFileParquetEntity)
      val filePathToWrite = oFileParquetEntity.getPath()
      val fieldList = oFileParquetEntity.getFieldsList.asScala

      fieldList.foreach { field => LOG.debug("Field name '" + field.getFieldName + "for Component " + oFileParquetEntity.getComponentId) }

      componentsParams.getDataFrame().select(schemaCreator.createSchema: _*)
          .write.mode( if (oFileParquetEntity.isOverWrite) SaveMode.Overwrite else SaveMode.Append )
          .parquet(filePathToWrite)

      LOG.debug("Created Output File Parquet Component '" + oFileParquetEntity.getComponentId + "' in Batch" + oFileParquetEntity.getBatch
        + ", file path " + oFileParquetEntity.getPath)
    } catch {
      case ex: RuntimeException =>
        LOG.error("Error in Output  File Parquet component '" + oFileParquetEntity.getComponentId + "', Error" + ex.getMessage, ex)
        throw ex
    }
  }
}