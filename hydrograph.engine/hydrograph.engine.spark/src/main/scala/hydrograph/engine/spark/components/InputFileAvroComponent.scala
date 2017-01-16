package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputFileAvroEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.SparkSession
import org.apache.avro.Schema
import org.apache.spark.sql._
import java.io.File
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class InputFileAvroComponent(inputFileAvroEntity: InputFileAvroEntity, baseComponentParams: BaseComponentParams) extends InputComponentBase {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[InputFileAvroComponent])

  override def finalize(): Unit = super.finalize()

  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method execute()")
    try {
      val schemaField = SchemaCreator(inputFileAvroEntity).makeSchema()
      val sparkSession = baseComponentParams.getSparkSession()
      val df = sparkSession.read.schema(schemaField).format("hydrograph.engine.spark.datasource.avro").load(inputFileAvroEntity.getPath)
      val key = inputFileAvroEntity.getOutSocketList.get(0).getSocketId
      LOG.debug("Created Input File Avro Component '" + inputFileAvroEntity.getComponentId + "' in Batch" + inputFileAvroEntity.getBatch
        + ", file path " + inputFileAvroEntity.getPath)
      Map(key -> df)
    } catch {
      case e: Exception =>
        LOG.error("Error in Input File Avro Component " + inputFileAvroEntity.getComponentId, e)
        throw new RuntimeException("Error in Input File Avro Component "
          + inputFileAvroEntity.getComponentId, e)
    }
  }
}
    