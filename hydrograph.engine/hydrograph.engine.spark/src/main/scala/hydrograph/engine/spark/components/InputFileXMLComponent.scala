package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputFileXMLEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Column, DataFrame}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._


/**
  * Created by bitwise on 1/12/2017.
  */
class InputFileXMLComponent (iFileXMLEntity: InputFileXMLEntity, iComponentsParams: BaseComponentParams)
  extends InputComponentBase with Serializable {

  def flattenSchema(schema: StructType, prefix: String = null) : Array[Column] = {
    schema.fields.flatMap(f => {
      val colName = if (prefix == null) f.name else (prefix + "." + f.name)

      f.dataType match {
        case st: StructType => flattenSchema(st, colName)
        case _ => Array(new Column(colName))
      }
    })
  }

  private val LOG:Logger = LoggerFactory.getLogger(classOf[InputFileXMLComponent])
  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")

    val schemaCreator = SchemaCreator(iFileXMLEntity)
    val writeMode:String= iFileXMLEntity.asInstanceOf[InputFileXMLEntity].isStrict match {
      case x if(x) => "FAILFAST"
      case x if(!x) => "PERMISSIVE"
    }

    try {
      val df = iComponentsParams.getSparkSession().read
        .option("charset", iFileXMLEntity.getCharset)
        .option("rowTag", iFileXMLEntity.getRowTag)
        .option("rootTag", iFileXMLEntity.getRootTag)
        .option("mode", writeMode)
        .option("dateFormats", schemaCreator.getDateFormats)
        .schema(schemaCreator.makeSchema)
        .format("com.databricks.spark.xml")
        .load(iFileXMLEntity.getPath)

      val key = iFileXMLEntity.getOutSocketList.get(0).getSocketId
      LOG.info("Created Input File XML Component "+ iFileXMLEntity.getComponentId
        + " in Batch "+ iFileXMLEntity.getBatch +" with output socket " + key
        + " and path "  + iFileXMLEntity.getPath)
      LOG.debug("Component Id: '"+ iFileXMLEntity.getComponentId
        +"' in Batch: " + iFileXMLEntity.getBatch
        + " having schema: [ " + iFileXMLEntity.getFieldsList.asScala.mkString(",")
        + " ] with strict as " + iFileXMLEntity.isStrict
        + " safe as " + iFileXMLEntity.isSafe
        + " rowTag as " + iFileXMLEntity.getRowTag
        + " rootTag as " + iFileXMLEntity.getRootTag
        + " absoluteXPath as " + iFileXMLEntity.getAbsoluteXPath
        + " at Path: " + iFileXMLEntity.getPath)
      Map(key -> df)

//      val flattenedSchema = flattenSchema(df.schema)
//      val renamedCols = flattenedSchema.map(name => new Column(name.toString()).as(name.toString().replace(".","_")))
//      val df_new: DataFrame = df.select(renamedCols:_*)

//      Map(key -> df_new)

    } catch {

      case e : Exception =>
        LOG.error("Error in Input File XML Component "+ iFileXMLEntity.getComponentId, e)
        throw new RuntimeException("Error in Input File XML Component "+ iFileXMLEntity.getComponentId, e)
    }

  }

}