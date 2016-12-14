package hydrograph.engine.spark.components

import java.sql.SQLException

import hydrograph.engine.core.component.entity.OutputRDBMSEntity
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.TableCreator
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
import org.slf4j.{Logger, LoggerFactory}
/**
  * Created by santlalg on 12/8/2016.
  */
class SparkOMysqlComponent (outputRDBMSEntity: OutputRDBMSEntity, cp:
BaseComponentParams) extends SparkFlow {

  val LOG: Logger = LoggerFactory.getLogger(classOf[SparkOMysqlComponent])

  override def execute(): Unit = {

    val prop = new java.util.Properties
    prop.setProperty("user", outputRDBMSEntity.getUsername)
    prop.setProperty("password", outputRDBMSEntity.getPassword)
    prop.setProperty("driver", outputRDBMSEntity.getJdbcDriver)

    val connectionURL = "jdbc:mysql://" + outputRDBMSEntity.getHostName() + ":" + outputRDBMSEntity.getPort() + "/" +
      outputRDBMSEntity.getDatabaseName()

    LOG.info("connecting : " + connectionURL)
    LOG.info("load type : " + outputRDBMSEntity.getLoadType)

    outputRDBMSEntity.getLoadType match {
      case "newTable" =>
        executeQuery(connectionURL,prop,TableCreator.getCreateTableQuery(outputRDBMSEntity))
        cp.getDataFrame().write.mode("append").jdbc(connectionURL,outputRDBMSEntity.getTableName,prop)

      case "insert" => cp.getDataFrame().write.mode("append").jdbc(connectionURL,outputRDBMSEntity.getTableName,prop)
      case "truncateLoad" =>
        executeQuery(connectionURL,prop,getTruncateQuery)
        cp.getDataFrame().write.mode("append").jdbc(connectionURL,outputRDBMSEntity.getTableName,prop)
    }
  }

  def executeQuery(connectionURL:String, properties:java.util.Properties, query:String) : Unit = {

    LOG.info("excuting : " + query)

    try {
      val connection = JdbcUtils.createConnectionFactory(connectionURL, properties)()

      val statment = connection.prepareStatement(query)
      val resultSet = statment.executeUpdate()
      connection.close()
    }catch {
      case e: SQLException =>
        throw new RuntimeException(e)
      case e: Exception =>
        throw new RuntimeException(e)
    }
  }

  def getTruncateQuery() : String = "truncate " + outputRDBMSEntity.getTableName

}
