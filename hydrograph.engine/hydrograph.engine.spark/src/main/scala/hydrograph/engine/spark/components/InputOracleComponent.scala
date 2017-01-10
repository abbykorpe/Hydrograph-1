package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputRDBMSEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql.DataFrame
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

class InputOracleComponent(inputRDBMSEntity: InputRDBMSEntity, iComponentsParams: BaseComponentParams) extends
  InputComponentBase {
  val LOG: Logger = LoggerFactory.getLogger(classOf[InputOracleComponent])

  override def createComponent(): Map[String, DataFrame] = {
    val schemaField = SchemaCreator(inputRDBMSEntity).makeSchema()

    val sparkSession = iComponentsParams.getSparkSession()

    val properties = inputRDBMSEntity.getRuntimeProperties;
    properties.setProperty("user", inputRDBMSEntity.getUsername)
    properties.setProperty("password", inputRDBMSEntity.getPassword)

    LOG.info("Created Input Oracle Component '" + inputRDBMSEntity.getComponentId
      + "' in Batch " + inputRDBMSEntity.getBatch
      + " with output socket " + inputRDBMSEntity.getOutSocketList.get(0).getSocketId)

    val tableorQuery = if (inputRDBMSEntity.getTableName == null || inputRDBMSEntity.getSelectQuery != null) ("(" + inputRDBMSEntity.getSelectQuery + ")") else inputRDBMSEntity.getTableName

    if (inputRDBMSEntity.getTableName != null)
      LOG.debug("Component Id '" + inputRDBMSEntity.getComponentId
        + "' in Batch " + inputRDBMSEntity.getBatch
        + " having schema: [ " + inputRDBMSEntity.getFieldsList.asScala.mkString(",") + " ]"
        + " reading data from '" + tableorQuery + "' table")
    else
      LOG.debug("Component Id '" + inputRDBMSEntity.getComponentId
        + "' in Batch " + inputRDBMSEntity.getBatch
        + " having schema: [ " + inputRDBMSEntity.getFieldsList.asScala.mkString(",") + " ]"
        + " reading data from '" + tableorQuery + "' query")

    val connectionURL = "jdbc:oracle:" + inputRDBMSEntity.getDriverType + "://@" + inputRDBMSEntity.getHostName + ":" + inputRDBMSEntity.getPort() + "/" +
      inputRDBMSEntity.getSid;

    LOG.info("Connection  url for Oracle input component: " + connectionURL)

    try {
      val df = sparkSession.read.schema(schemaField).jdbc(connectionURL, tableorQuery, properties)
      val key = inputRDBMSEntity.getOutSocketList.get(0).getSocketId
      Map(key -> df)
    } catch {
      case e: Exception =>
        LOG.error("Error in Input  Oracle input component '" + inputRDBMSEntity.getComponentId + "', Error" + e.getMessage, e)
        throw new RuntimeException("Error in Input Oracle Component " + inputRDBMSEntity.getComponentId, e)
    }
  }
}
