package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputFileDelimitedEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.DataFrame
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

class InputFileCsvComponent(iFileDelimitedEntity: InputFileDelimitedEntity, iComponentsParams: BaseComponentParams)
  extends InputComponentBase with Serializable {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[InputFileCsvComponent])
  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")
    val schemaCreator = SchemaCreator(iFileDelimitedEntity)

    try {
      val df = iComponentsParams.getSparkSession().read
        .option("sep", iFileDelimitedEntity.getDelimiter)
        .option("quote", iFileDelimitedEntity.getQuote)
        .option("header", iFileDelimitedEntity.isHasHeader)
        .option("charset", iFileDelimitedEntity.getCharset)
        .option("safe", iFileDelimitedEntity.isSafe)
        .option("strict", iFileDelimitedEntity.isStrict)
        .option("dateFormat", "yyyy/MM/dd")
        .option("timestampFormat", "yyyy/MM/dd HH:mm:ss")
        .schema(schemaCreator.makeSchema)
        .csv(iFileDelimitedEntity.getPath)

      val key = iFileDelimitedEntity.getOutSocketList.get(0).getSocketId
      LOG.info("Created Input File Delimited Component "+ iFileDelimitedEntity.getComponentId
        + " in Batch "+ iFileDelimitedEntity.getBatch +" with output socket " + key
        + " and path "  + iFileDelimitedEntity.getPath)
      LOG.debug("Component Id: '"+ iFileDelimitedEntity.getComponentId
        +"' in Batch: " + iFileDelimitedEntity.getBatch
        + " having schema: [ " + iFileDelimitedEntity.getFieldsList.asScala.mkString(",")
        + " ] with delimiter: " + iFileDelimitedEntity.getDelimiter
        + " and quote: " + iFileDelimitedEntity.getQuote
        + " strict as " + iFileDelimitedEntity.isStrict + " safe as " + iFileDelimitedEntity.isSafe
        + " at Path: " + iFileDelimitedEntity.getPath)
      Map(key -> df)
    } catch {

      case e : Exception =>
        LOG.error("Error in Input File Delimited Component "+ iFileDelimitedEntity.getComponentId, e)
        throw new RuntimeException("Error in Input File Delimited Component "+ iFileDelimitedEntity.getComponentId, e)
    }

  }

}

