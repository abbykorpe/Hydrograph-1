package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputRDBMSEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql._

/**
  * Created by santlalg on 12/7/2016.
  */
class SparkIMysqlComponent (inputRDBMSEntity: InputRDBMSEntity,iComponentsParams: BaseComponentParams) extends
  InputComponentBase {

  override def createComponent(): Map[String,DataFrame] = {
    val schemaField = SchemaCreator(inputRDBMSEntity).makeSchema()

    val sparkSession = iComponentsParams.getSparkSession()
    val prop = new java.util.Properties
    prop.setProperty("user", inputRDBMSEntity.getUsername)
    prop.setProperty("password", inputRDBMSEntity.getPassword)
    prop.setProperty("driver", inputRDBMSEntity.getJdbcDriver)


    val tableorQuery = if (inputRDBMSEntity.getTableName == null) ("(" + inputRDBMSEntity.getSelectQuery + ") as alias") else inputRDBMSEntity.getTableName

    val connectionURL = "jdbc:mysql://" + inputRDBMSEntity.getHostName() + ":" + inputRDBMSEntity.getPort() + "/" +
      inputRDBMSEntity.getDatabaseName();

    val df = sparkSession.read.jdbc(connectionURL, tableorQuery, prop)
    val key = inputRDBMSEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)
  }
}
