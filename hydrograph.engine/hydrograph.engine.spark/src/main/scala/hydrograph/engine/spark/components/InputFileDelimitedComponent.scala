package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputFileDelimitedEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.DataFrame
import org.slf4j.{Logger, LoggerFactory}
import scala.collection.JavaConverters._

class InputFileDelimitedComponent(iFileDelimitedEntity: InputFileDelimitedEntity, iComponentsParams: BaseComponentParams)
  extends InputComponentBase with Serializable {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[InputFileDelimitedComponent])
  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")
    val dateFormats=getDateFormats()
    val schemaField = SchemaCreator(iFileDelimitedEntity).makeSchema()
    try {
      val df = iComponentsParams.getSparkSession().read
        .option("delimiter", iFileDelimitedEntity.getDelimiter)
        .option("quote", iFileDelimitedEntity.getQuote)
        .option("header", iFileDelimitedEntity.isHasHeader)
        .option("charset", iFileDelimitedEntity.getCharset)
        .option("safe", iFileDelimitedEntity.isSafe)
        .option("strict", iFileDelimitedEntity.isStrict)
        .option("dateFormats", dateFormats)
        .schema(schemaField)
        .format("hydrograph.engine.spark.datasource.delimited")
        .load(iFileDelimitedEntity.getPath)

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

  def getDateFormats(): String = {
      LOG.trace("In method getDateFormats() which returns \\t separated date formats for Date fields")
      var dateFormats: String = ""
      for (i <- 0 until iFileDelimitedEntity.getFieldsList.size()) {
        dateFormats += iFileDelimitedEntity.getFieldsList.get(i).getFieldFormat + "\t"
      }
      LOG.debug("Date Formats for Date fields : " + dateFormats)
      dateFormats
  }

}

