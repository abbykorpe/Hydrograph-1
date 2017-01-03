package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputFileParquetEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{SchemaCreator, SchemaUtils}
import hydrograph.engine.spark.components.utils.ParquetMetadataReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{StructField, StructType}

import scala.collection.JavaConverters._
import org.slf4j.{Logger, LoggerFactory}

class InputFileParquetComponent(iFileParquetEntity: InputFileParquetEntity, iComponentsParams: BaseComponentParams)
  extends InputComponentBase {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[InputFileParquetComponent])

  override def finalize(): Unit = super.finalize()


  override def createComponent(): Map[String, DataFrame] = {
    val schemaField = SchemaCreator(iFileParquetEntity).makeSchema()
    try {
      val fieldList = iFileParquetEntity.getFieldsList.asScala
      fieldList.foreach { field => LOG.debug("Field name '" + field.getFieldName + "for Component " + iFileParquetEntity.getComponentId) }
      val schemaFromMetadata: StructType = new ParquetMetadataReader().getParquetFileSchema(iFileParquetEntity.getPath, iComponentsParams.getSparkSession())
      SchemaUtils().compareSchema(schemaField, schemaFromMetadata)
      val df = iComponentsParams.getSparkSession().read.schema(schemaField).parquet(iFileParquetEntity.getPath)
      val key = iFileParquetEntity.getOutSocketList.get(0).getSocketId
      LOG.debug("Created Input File Parquet Component '" + iFileParquetEntity.getComponentId + "' in Batch" + iFileParquetEntity.getBatch
        + ", file path " + iFileParquetEntity.getPath)
      Map(key -> df)
    }
    catch {
      case ex: RuntimeException =>
        LOG.error("Error in Input  File Parquet component '" + iFileParquetEntity.getComponentId + "', Error" + ex.getMessage, ex); throw ex
    }
  }
}