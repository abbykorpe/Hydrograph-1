package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputRDBMSEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{DbTableUtils, SchemaCreator, SchemaUtils}
import org.apache.spark.sql._
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._


/**
  * Created by santlalg on 12/7/2016.
  */
class InputMysqlComponent(inputRDBMSEntity: InputRDBMSEntity, iComponentsParams: BaseComponentParams) extends
  InputComponentBase {

  val LOG: Logger = LoggerFactory.getLogger(classOf[InputMysqlComponent])
  val driverName = null

  override def createComponent(): Map[String, DataFrame] = {

    val schemaField = SchemaCreator(inputRDBMSEntity).makeSchema()

    val sparkSession = iComponentsParams.getSparkSession()

    val properties = inputRDBMSEntity.getRuntimeProperties
    properties.setProperty("user", inputRDBMSEntity.getUsername)
    properties.setProperty("password", inputRDBMSEntity.getPassword)
    val driverName = "com.mysql.jdbc.Driver"

    if (inputRDBMSEntity.getJdbcDriver.equals("Connector/J")) {
      properties.setProperty("driver", driverName)
    }

    LOG.info("Created Input Mysql Component '" + inputRDBMSEntity.getComponentId
      + "' in Batch " + inputRDBMSEntity.getBatch
      + " with output socket " + inputRDBMSEntity.getOutSocketList.get(0).getSocketId)

    val selectQuery = if (inputRDBMSEntity.getTableName == null) {
      LOG.debug("Select query :  " + inputRDBMSEntity.getSelectQuery)
      "(" + inputRDBMSEntity.getSelectQuery + ") as alias"
    }
    else "(" + DbTableUtils().getSelectQuery(inputRDBMSEntity.getFieldsList.asScala.toList, inputRDBMSEntity.getTableName) + ") as alias"

    val connectionURL = "jdbc:mysql://" + inputRDBMSEntity.getHostName + ":" + inputRDBMSEntity.getPort + "/" +
      inputRDBMSEntity.getDatabaseName

    LOG.info("Connection url for Mysql input component: " + connectionURL)

    try {
      val df = sparkSession.read.jdbc(connectionURL, selectQuery, properties)
      SchemaUtils().compareSchema(schemaField,df.schema)
      val key = inputRDBMSEntity.getOutSocketList.get(0).getSocketId
      Map(key -> df)

    } catch {
      case e: Exception =>
        LOG.error("Error in Input  Mysql component '" + inputRDBMSEntity.getComponentId + "', " + e.getMessage, e)
        throw new RuntimeException("Error in Input Mysql Component " + inputRDBMSEntity.getComponentId, e)
    }
  }
}
