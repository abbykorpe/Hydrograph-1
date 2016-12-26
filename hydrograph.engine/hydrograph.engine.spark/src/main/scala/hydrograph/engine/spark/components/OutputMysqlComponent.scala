package hydrograph.engine.spark.components

import java.sql.SQLException
import java.util
import java.util.Properties

import hydrograph.engine.core.component.entity.OutputRDBMSEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.TableCreator
import org.apache.spark.sql.Column
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
import org.apache.spark.sql.functions._
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
  * Created by santlalg on 12/8/2016.
  */
class OutputMysqlComponent(outputRDBMSEntity: OutputRDBMSEntity, cp:
BaseComponentParams) extends SparkFlow {

  val LOG: Logger = LoggerFactory.getLogger(classOf[OutputMysqlComponent])

  override def execute(): Unit = {

    val properties = outputRDBMSEntity.getRuntimeProperties
    properties.setProperty("user", outputRDBMSEntity.getUsername)
    properties.setProperty("password", outputRDBMSEntity.getPassword)
    val driverName = "com.mysql.jdbc.Driver"

    if (outputRDBMSEntity.getJdbcDriver().equals("Connector/J")) {
      properties.setProperty("driver", driverName)
    }

    val connectionURL = "jdbc:mysql://" + outputRDBMSEntity.getHostName() + ":" + outputRDBMSEntity.getPort() + "/" +
      outputRDBMSEntity.getDatabaseName() +"?rewriteBatchedStatements=true"

    LOG.info("Created Output Mysql Component '"+ outputRDBMSEntity.getComponentId
      + "' in Batch "+ outputRDBMSEntity.getBatch
      +" with Connection url " + connectionURL
      + " with data load option " + outputRDBMSEntity.getLoadType)
    LOG.debug("Component Id '"+ outputRDBMSEntity.getComponentId
      +"' in Batch " + outputRDBMSEntity.getBatch
      + " having schema [ " + outputRDBMSEntity.getFieldsList.asScala.mkString(",")
      + " ] with load type " + outputRDBMSEntity.getLoadType
      +" at connection url  " + connectionURL)

    outputRDBMSEntity.getLoadType match {
      case "newTable" =>
        executeQuery(connectionURL, properties, TableCreator().getCreateTableQuery(outputRDBMSEntity))
        cp.getDataFrame().select(createSchema(outputRDBMSEntity.getFieldsList): _*).write.mode("append").jdbc(connectionURL, outputRDBMSEntity.getTableName, properties)

      case "insert" => cp.getDataFrame().select(createSchema(outputRDBMSEntity.getFieldsList): _*).write.mode("append").jdbc(connectionURL, outputRDBMSEntity.getTableName, properties)
      case "truncateLoad" =>
        executeQuery(connectionURL, properties, getTruncateQuery)
        cp.getDataFrame().select(createSchema(outputRDBMSEntity.getFieldsList): _*).write.mode("append").jdbc(connectionURL, outputRDBMSEntity.getTableName, properties)
    }
  }

  def executeQuery(connectionURL: String, properties: java.util.Properties, query: String): Unit = {

    LOG.debug("Executing '" + query + "' query for Mysql output component")

    LOG.trace("In method executeQuery() executing '" + query
        + "' query with connection url " + connectionURL )

    try {
      val connection = JdbcUtils.createConnectionFactory(connectionURL, properties)()
      val statment = connection.prepareStatement(query)
      val resultSet = statment.executeUpdate()
      connection.close()
    } catch {
      case e: SQLException =>
        LOG.error("Error while connecting to database " + e.getMessage)
        throw new RuntimeException("Error message " + e.getMessage ,e)
      case e: Exception =>
        LOG.error("Error while executing '"+ query + "' query in executeQuery()" )
        throw new RuntimeException("Error message " + e.getMessage , e)
    }
  }

  def getTruncateQuery(): String = "truncate " + outputRDBMSEntity.getTableName

  def createSchema(getFieldsList: util.List[SchemaField]): Array[Column] =  {
    LOG.trace("In method createSchema()")
    val schema = new Array[Column](getFieldsList.size())

    getFieldsList.asScala.zipWithIndex.foreach { case (f, i) => schema(i) =  col(f.getFieldName) }
    LOG.debug("Schema created for Output Oracle Component : " + schema.mkString)
    schema
  }
}
