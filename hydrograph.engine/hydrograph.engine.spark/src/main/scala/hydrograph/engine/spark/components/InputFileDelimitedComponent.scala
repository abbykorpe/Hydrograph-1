package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputFileDelimitedEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.DataFrame

class InputFileDelimitedComponent(iFileDelimitedEntity: InputFileDelimitedEntity, iComponentsParams: BaseComponentParams) extends InputComponentBase {

  override def createComponent(): Map[String, DataFrame] = {
    val dateFormats=getDateFormats()
    val schemaField = SchemaCreator(iFileDelimitedEntity).makeSchema()

    val df = iComponentsParams.getSparkSession().read
      .option("delimiter", iFileDelimitedEntity.getDelimiter)
      .option("quote", iFileDelimitedEntity.getQuote)
      .option("header", iFileDelimitedEntity.isHasHeader)
      .option("charset", iFileDelimitedEntity.getCharset)
      .option("safe", iFileDelimitedEntity.isSafe)
      .option("strict", iFileDelimitedEntity.isStrict)
      .option("dateFormats", dateFormats)
      .schema(schemaField)
      .format("hydrograph.engine.spark.delimited.datasource")
      .load(iFileDelimitedEntity.getPath)

    val key = iFileDelimitedEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)

  }

  def getDateFormats(): String =
    {

      var dateFormats: String = ""
      for (i <- 0 until iFileDelimitedEntity.getFieldsList.size()) {
        dateFormats += iFileDelimitedEntity.getFieldsList.get(i).getFieldFormat + "\t"
      }

      return dateFormats
    }

}

