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
import hydrograph.engine.spark.components.utils.{SchemaCreator, SchemaUtils, TeradataTableUtils}
import org.apache.spark.sql._
import org.apache.spark.sql.types._
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

    val schemaField = SchemaCreator(inputRDBMSEntity).makeSchema()

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

    val selectQuery = if (inputRDBMSEntity.getTableName == null) {
      LOG.debug("Select query :  " + inputRDBMSEntity.getSelectQuery)
      "(" + inputRDBMSEntity.getSelectQuery + ") as aliass"
    }
    else "(" + TeradataTableUtils()
      .getSelectQuery(inputRDBMSEntity.getFieldsList.asScala.toList,inputRDBMSEntity.getTableName) + ") as aliass"

    if (inputRDBMSEntity.getTableName != null)
      LOG.debug("Component Id '" + inputRDBMSEntity.getComponentId
        + "' in Batch " + inputRDBMSEntity.getBatch
        + " having schema: [ " + inputRDBMSEntity.getFieldsList.asScala.mkString(",") + " ]"
        + " reading data from '" + selectQuery + "' table")
    else
      LOG.debug("Component Id '" + inputRDBMSEntity.getComponentId
        + "' in Batch " + inputRDBMSEntity.getBatch
        + " having schema: [ " + inputRDBMSEntity.getFieldsList.asScala.mkString(",") + " ]"
        + " reading data from '" + selectQuery + "' query")



    val connectionURL = "jdbc:teradata://" + inputRDBMSEntity.getHostName() + "/DBS_PORT=" + inputRDBMSEntity.getPort() + ",DATABASE=" +
      inputRDBMSEntity.getDatabaseName()+",TYPE=DEFAULT";
    /*+inputRDBMSEntity.get_interface()+*/
    LOG.info("Connection  url for Teradata input component: " + connectionURL)


    try {
      val df = sparkSession.read.jdbc(connectionURL, selectQuery, properties)
      SchemaUtils().compareSchema(getMappedSchema(schemaField),df.schema)
      val key = inputRDBMSEntity.getOutSocketList.get(0).getSocketId
      Map(key -> df)
    } catch {
      case e: Exception =>
        LOG.error("Error in Input  Teradata input component '" + inputRDBMSEntity.getComponentId + "', Error" + e.getMessage, e)
        throw new RuntimeException("Error in Input Teradata Component " + inputRDBMSEntity.getComponentId, e)
    }
  }

  def getMappedSchema(schema:StructType) : StructType = StructType(schema.toList.map(stuctField=> new StructField(stuctField.name,getDataType(stuctField.dataType).getOrElse(stuctField.dataType))).toArray)

  private def getDataType(dataType: DataType): Option[DataType] = {
    val answer = dataType.typeName.toUpperCase match {
      case "DOUBLE" => FloatType  /** In teradata if we create a table with a field type as Double,
        *it creates a schema and replaces the Double datatype with Float datatype which is Teradata specific.
        * Contrary to that if we attempt to read the data from a Teradata table, we have created by using the
        * output schema as Double, the execution gets stopped
        * as the data gets exported from Teradata as Float. In order to get Double type data while reading from a Teradata
        * datanase, we mapped FLoatType to java.lang.Double*/
      case "SHORT" => IntegerType
      case "BOOLEAN" => IntegerType

      case _ => null
    }
    if (answer != null) Option(answer) else None

  }

}