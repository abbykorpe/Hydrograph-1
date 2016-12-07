package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.{InputFileFixedWidthEntity, InputFileDelimitedEntity}
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.DataFrame
import scala.collection.JavaConverters._
/**
  * Created by gurdits on 10/15/2016.
  */
class SparkIFileFixedWidthComponent(fileFixedWidthEntity: InputFileFixedWidthEntity, iComponentsParams: BaseComponentParams) extends
  InputComponentBase {


  override def createComponent(): Map[String,DataFrame] = {


    val schemaField = SchemaCreator(fileFixedWidthEntity).makeSchema()

    val fieldsLen=new Array[Int](fileFixedWidthEntity.getFieldsList.size())

    fileFixedWidthEntity.getFieldsList().asScala.zipWithIndex.foreach{ case(s,i)=>
      fieldsLen(i)=s.getFieldLength
    }


    val df = iComponentsParams.getSparkSession().read
      .option("charset", fileFixedWidthEntity.getCharset)
        .option("length",fieldsLen.mkString(","))
      .schema(schemaField)
      .format("hydrograph.engine.spark.fixedwidth.datasource").load(fileFixedWidthEntity.getPath)


      val key=fileFixedWidthEntity.getOutSocketList.get(0).getSocketId
   Map(key->df)
  }

}

