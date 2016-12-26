package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.core.component.entity.{InputFileDelimitedEntity, InputFileMixedSchemeEntity}
import hydrograph.engine.core.constants.Constants
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.DataFrame
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

class InputFileMixedSchemeComponent(iFileMixedSchemeEntity: InputFileMixedSchemeEntity, iComponentsParams: BaseComponentParams)
  extends InputComponentBase with Serializable {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[InputFileMixedSchemeComponent])

  def extractLengthsAndDelimiters(getFieldsList: util.List[SchemaField]):String = {
    def extract(schemafields:List[SchemaField],lengthsAndDelimiters:List[String]):List[String] = (schemafields,lengthsAndDelimiters) match {
      case (List(),y) => y
      case (x::xs,y) => extract(xs,(y ++ List(x.getFieldLengthDelimiter)))
    }
    extract(getFieldsList.asScala.toList,List[String]()).mkString(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR)
  }

  def extractLengthsAndDelimitersType(getFieldsList: util.List[SchemaField]):String = {
    def extract(schemafields:List[SchemaField],lengthsAndDelimiters:List[String]):List[String] = (schemafields,lengthsAndDelimiters) match {
      case (List(),y) => y
      case (x::xs,y) => extract(xs,(y ++ List(x.getTypeFieldLengthDelimiter.toString)))
    }
    extract(getFieldsList.asScala.toList,List[String]()).mkString(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR)
  }

  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")
    val dateFormats=getDateFormats()
    val schemaField = SchemaCreator(iFileMixedSchemeEntity).makeSchema()
    try {
      val df = iComponentsParams.getSparkSession().read
        .option("quote", iFileMixedSchemeEntity.getQuote)
        .option("charset", iFileMixedSchemeEntity.getCharset)
        .option("safe", iFileMixedSchemeEntity.getSafe)
        .option("strict", iFileMixedSchemeEntity.getStrict)
        .option("dateFormats", dateFormats)
        .option("lengthsAndDelimiters", extractLengthsAndDelimiters(iFileMixedSchemeEntity.getFieldsList))
        .option("lengthsAndDelimitersType", extractLengthsAndDelimitersType(iFileMixedSchemeEntity.getFieldsList))
        .schema(schemaField)
        .format("hydrograph.engine.spark.datasource.mixedScheme")
        .load(iFileMixedSchemeEntity.getPath)

      val key = iFileMixedSchemeEntity.getOutSocketList.get(0).getSocketId
      LOG.info("Created Input File MixedScheme Component "+ iFileMixedSchemeEntity.getComponentId
        + " in Batch "+ iFileMixedSchemeEntity.getBatch +" with output socket " + key
        + " and path "  + iFileMixedSchemeEntity.getPath)
      LOG.debug("Component Id: '"+ iFileMixedSchemeEntity.getComponentId
        +"' in Batch: " + iFileMixedSchemeEntity.getBatch
        + " having schema: [ " + iFileMixedSchemeEntity.getFieldsList.asScala.mkString(",")
        + " ] with quote: " + iFileMixedSchemeEntity.getQuote
        + " strict as " + iFileMixedSchemeEntity.getStrict + " safe as " + iFileMixedSchemeEntity.getSafe
        + " at Path: " + iFileMixedSchemeEntity.getPath)
      Map(key -> df)
    } catch {

      case e =>
        LOG.error("Error in Input File MixedScheme Component "+ iFileMixedSchemeEntity.getComponentId, e)
        throw new RuntimeException("Error in Input File MixedScheme Component "+ iFileMixedSchemeEntity.getComponentId, e)
    }

  }

  def getDateFormats(): String = {
      LOG.trace("In method getDateFormats() which returns \\t separated date formats for Date fields")
      var dateFormats: String = ""
      for (i <- 0 until iFileMixedSchemeEntity.getFieldsList.size()) {
        dateFormats += iFileMixedSchemeEntity.getFieldsList.get(i).getFieldFormat + "\t"
      }
      LOG.debug("Date Formats for Date fields : " + dateFormats)
      dateFormats
  }

}

