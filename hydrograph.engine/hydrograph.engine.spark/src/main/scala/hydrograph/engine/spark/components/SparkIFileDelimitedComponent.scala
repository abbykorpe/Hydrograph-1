package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputFileDelimitedEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.DataFrame

/**
  * Created by gurdits on 10/15/2016.
  */
class SparkIFileDelimitedComponent(iFileDelimitedEntity: InputFileDelimitedEntity,iComponentsParams: BaseComponentParams) extends
  InputComponentBase {


  override def createComponent(): Map[String,DataFrame] = {


    val schemaField = SchemaCreator(iFileDelimitedEntity).makeSchema()

    val df = iComponentsParams.getSparkSession().read
      .option("delimiter", iFileDelimitedEntity.getDelimiter)
      .option("quote", iFileDelimitedEntity.getQuote)
      .option("header", iFileDelimitedEntity.isHasHeader)
      .option("charset", iFileDelimitedEntity.getCharset)
      .schema(schemaField)
      .csv(iFileDelimitedEntity.getPath)



      val key=iFileDelimitedEntity.getOutSocketList.get(0).getSocketId
   Map(key->df)

//    val sp = SparkOutLinkContext(df, schemaField, iFileDelimitedEntity.getComponentId, iFileDelimitedEntity.getOutSocketList.get(0).getSocketId)
//
//    val outLink = new SparkOutLink()
//    outLink.createOutLink(sp)
//
//    outLink
  }

}

