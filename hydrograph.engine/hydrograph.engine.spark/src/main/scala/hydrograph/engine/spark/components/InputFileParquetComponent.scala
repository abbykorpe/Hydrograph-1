package hydrograph.engine.spark.components

import java.io.File

import hydrograph.engine.core.component.entity.InputFileParquetEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{SchemaCreator, SchemaUtils}
import org.apache.spark.sql.DataFrame
import scala.collection.JavaConverters._
import org.slf4j.{Logger, LoggerFactory}

class InputFileParquetComponent(iFileParquetEntity: InputFileParquetEntity, iComponentsParams: BaseComponentParams)
  extends InputComponentBase {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[InputFileParquetComponent])

  override def finalize(): Unit = super.finalize()

  override def createComponent(): Map[String, DataFrame] = {
    val schemaField = SchemaCreator(iFileParquetEntity).makeSchema()
    try {

      val path: String = iFileParquetEntity.getPath

      val fieldList = iFileParquetEntity.getFieldsList.asScala
      fieldList.foreach { field => LOG.debug("Field name '" + field.getFieldName + "for Component " + iFileParquetEntity.getComponentId) }

      val listofFiles = getListOfFiles(path)

      var df: DataFrame = null

      if (!listofFiles.isEmpty) {
        for (i <- listofFiles.indices) {
          df = iComponentsParams.getSparkSession().read.parquet(listofFiles(i).toString)
          SchemaUtils().compareSchema(schemaField, df.schema)
        }
      }
      else {
        if (path.endsWith(".parquet")) {
          df = iComponentsParams.getSparkSession().read.parquet(path)
          SchemaUtils().compareSchema(schemaField, df.schema)
        }
        else {
          throw new EmptyFolderException("There is no Parquet files in given specified Path: '" + path + "'")
        }
      }

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

  /*
   * get the list of files in folder
   * @param dir String
   * @return List[] of files
   */
  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

}

case class EmptyFolderException(message: String = "", cause: Throwable = null) extends Exception(message, cause)