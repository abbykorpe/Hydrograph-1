/*******************************************************************************
 *    Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputRDBMSEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.sql._
import org.slf4j.{Logger, LoggerFactory}
import scala.collection.JavaConverters._
/**
  * Created by AniruddhaS on 12/27/2016.
  */
class InputTeradataComponent(inputRDBMSEntity: InputRDBMSEntity,
                             iComponentsParams: BaseComponentParams) extends InputComponentBase {
  val LOG: Logger = LoggerFactory.getLogger(classOf[InputTeradataComponent])
  val driverName = null

  override def createComponent(): Map[String, DataFrame] = {

    //    val schemaField = SchemaCreator(inputRDBMSEntity).makeSchema()

    val sparkSession = iComponentsParams.getSparkSession()

    val properties = inputRDBMSEntity.getRuntimeProperties
    properties.setProperty("user", inputRDBMSEntity.getUsername)
    properties.setProperty("password", inputRDBMSEntity.getPassword)
    val driverName = "com.teradata.jdbc.TeraDriver"

    if (inputRDBMSEntity.getJdbcDriver().equals("TeraJDBC4")) {
      properties.setProperty("driver", driverName)
    }

    LOG.info("Created Input Teradata Component '" + inputRDBMSEntity.getComponentId
      + "' in Batch " + inputRDBMSEntity.getBatch
      + " with output socket " + inputRDBMSEntity.getOutSocketList.get(0).getSocketId)

    val tableorQuery = if (inputRDBMSEntity.getTableName == null)
      "(" + inputRDBMSEntity.getSelectQuery + ") as alias"
      else inputRDBMSEntity.getTableName

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


    val connectionURL = "jdbc:teradata://" + inputRDBMSEntity.getHostName() + "/DBS_PORT=" + inputRDBMSEntity.getPort() + ",DATABASE=" +
      inputRDBMSEntity.getDatabaseName()+",TYPE="+inputRDBMSEntity.get_interface()+"";

    LOG.info("Connection  url for Teradata input component: " + connectionURL)

    try {
      val df = sparkSession.read.jdbc(connectionURL, tableorQuery, properties)
      val key = inputRDBMSEntity.getOutSocketList.get(0).getSocketId
      Map(key -> df)
    } catch {
      case e: Exception =>
        LOG.error("Error in Input  Teradata input component '" + inputRDBMSEntity.getComponentId + "', Error" + e.getMessage, e)
        throw new RuntimeException("Error in Input Teradata Component " + inputRDBMSEntity.getComponentId, e)
    }
  }

}