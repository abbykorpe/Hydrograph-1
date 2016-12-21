package hydrograph.engine.spark.components

import java.sql.SQLException

import hydrograph.engine.core.component.entity.OutputRDBMSEntity
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.TableCreator
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
  * Created by amiyam on 16-12-2016.
  */
class OracleOutputComponent(outputRDBMSEntity: OutputRDBMSEntity, oComponentsParams: BaseComponentParams) extends
  SparkFlow {
  val LOG: Logger = LoggerFactory.getLogger(classOf[OracleOutputComponent])

  override def execute(): Unit = {
    val prop = new java.util.Properties
    prop.setProperty("user", outputRDBMSEntity.getUsername)
    prop.setProperty("password", outputRDBMSEntity.getPassword)

    val connectionURL = "jdbc:oracle:" + outputRDBMSEntity.getDriverType + "://@" + outputRDBMSEntity.getHostName + ":" + outputRDBMSEntity.getPort() + "/" +
      outputRDBMSEntity.getSid;

    LOG.info("Created Output Oracle Component '" + outputRDBMSEntity.getComponentId
      + "' in Batch " + outputRDBMSEntity.getBatch
      + " with Connection url " + connectionURL
      + " with data load option " + outputRDBMSEntity.getLoadType)
    LOG.debug("Component Id '" + outputRDBMSEntity.getComponentId
      + "' in Batch " + outputRDBMSEntity.getBatch
      + " having schema [ " + outputRDBMSEntity.getFieldsList.asScala.mkString(",")
      + " ] with load type " + outputRDBMSEntity.getLoadType
      + " at connection url  " + connectionURL)

    outputRDBMSEntity.getLoadType match {
      case "newTable" =>
        executeQuery(connectionURL, prop, TableCreator().getCreateTableQuery(outputRDBMSEntity))
        oComponentsParams.getDataFrame().write.mode("append").jdbc(connectionURL, outputRDBMSEntity.getTableName, prop)
      case "insert" => oComponentsParams.getDataFrame().write.mode("append").jdbc(connectionURL, outputRDBMSEntity.getTableName, prop)
      case "truncateLoad" =>
        executeQuery(connectionURL, prop, getTruncateQuery)
        oComponentsParams.getDataFrame().write.mode("append").jdbc(connectionURL, outputRDBMSEntity.getTableName, prop)
    }

  }

  def executeQuery(connectionURL: String, properties: java.util.Properties, query: String): Unit = {
    LOG.debug("Executing '" + query + "' query for Oracle output component")
    LOG.trace("In method executeQuery() executing '" + query + "' query with connection url " + connectionURL)
    try {
      val connection = JdbcUtils.createConnectionFactory(connectionURL, properties)()
      val statment = connection.prepareStatement(query)
      val resultSet = statment.executeUpdate()
      connection.close()
    } catch {
      case e: SQLException =>
        LOG.error("Error while connecting to database " + e.getMessage)
        throw new RuntimeException("Error message " + e.getMessage, e)
      case e: Exception =>
        LOG.error("Error while executing '" + query + "' query in executeQuery()")
        throw new RuntimeException("Error message " + e.getMessage, e)
    }
  }

  def getTruncateQuery(): String = "truncate table " + outputRDBMSEntity.getTableName
}

