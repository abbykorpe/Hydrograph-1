package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputFileFixedWidthEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.DataFrame
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

class InputFileFixedWidthComponent(fileFixedWidthEntity: InputFileFixedWidthEntity, iComponentsParams: BaseComponentParams) extends
  InputComponentBase {

  private val LOG:Logger = LoggerFactory.getLogger(classOf[InputFileFixedWidthComponent])
  override def createComponent(): Map[String,DataFrame] = {
    val dateFormats=getDateFormats()
    val schemaField = SchemaCreator(fileFixedWidthEntity).makeSchema()

    val fieldsLen=new Array[Int](fileFixedWidthEntity.getFieldsList.size())

    fileFixedWidthEntity.getFieldsList().asScala.zipWithIndex.foreach{ case(s,i)=>
      fieldsLen(i)=s.getFieldLength
    }

    val df = iComponentsParams.getSparkSession().read
      .option("charset", fileFixedWidthEntity.getCharset)
      .option("length",fieldsLen.mkString(","))
      .option("strict", fileFixedWidthEntity.isStrict)
      .option("safe", fileFixedWidthEntity.isSafe)
      .option("dateFormats", dateFormats)
      .schema(schemaField)
      .format("hydrograph.engine.spark.fixedwidth.datasource")
      .load(fileFixedWidthEntity.getPath)

    val key=fileFixedWidthEntity.getOutSocketList.get(0).getSocketId
    LOG.info("Component Id: '"+ fileFixedWidthEntity.getComponentId
      +"' in Batch: " + fileFixedWidthEntity.getBatch
      + " having schema: [ " + fileFixedWidthEntity.getFieldsList.asScala.mkString(",")
      + " ] at Path: " + fileFixedWidthEntity.getPath)
    Map(key->df)
  }
  def getDateFormats(): String = {

    var dateFormats: String = ""
    for (i <- 0 until fileFixedWidthEntity.getFieldsList.size()) {
      dateFormats += fileFixedWidthEntity.getFieldsList.get(i).getFieldFormat + "\t"
    }

    return dateFormats
  }

}

