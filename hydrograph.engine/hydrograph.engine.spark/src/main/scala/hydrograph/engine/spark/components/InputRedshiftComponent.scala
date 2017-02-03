/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputRDBMSEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{SchemaCreator, SchemaMismatchException}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

class InputRedshiftComponent(inputRDBMSEntity: InputRDBMSEntity, iComponentsParams: BaseComponentParams) extends
  InputComponentBase {
  val LOG: Logger = LoggerFactory.getLogger(classOf[InputRedshiftComponent])

  override def createComponent(): Map[String, DataFrame] = {
    val schemaField = SchemaCreator(inputRDBMSEntity).makeSchema()

    val sparkSession = iComponentsParams.getSparkSession()

    val properties = inputRDBMSEntity.getRuntimeProperties;
    properties.setProperty("user", inputRDBMSEntity.getUsername)
    properties.setProperty("password", inputRDBMSEntity.getPassword)
    properties.setProperty("driver", inputRDBMSEntity.getJdbcDriver)
    val driverName = "com.amazon.redshift.jdbc42.Driver"

    if (inputRDBMSEntity.getJdbcDriver.equals("JDBC 4.2")) {
      properties.setProperty("driver", driverName)
    }

    LOG.info("Created Input Redshift Component '" + inputRDBMSEntity.getComponentId
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

    val connectionURL = "jdbc:redshift://" + inputRDBMSEntity.getHostName + ":" + inputRDBMSEntity.getPort() + "/" +
      inputRDBMSEntity.getDatabaseName;

    LOG.info("Connection  url for input Redshift  component: " + connectionURL)

    try {
      val df = sparkSession.read.jdbc(connectionURL, tableorQuery, properties)
      compareSchema(getMappedSchema(schemaField), df.schema)
      val key = inputRDBMSEntity.getOutSocketList.get(0).getSocketId
      Map(key -> df)
    } catch {
      case e: Exception =>
        LOG.error("Error in Input  Redshift component '" + inputRDBMSEntity.getComponentId + "', Error" + e.getMessage, e)
        throw new RuntimeException("Error in Input Redshift Component " + inputRDBMSEntity.getComponentId, e)
    }
  }

  def getMappedSchema(schema: StructType): StructType = StructType(schema.toList.map(stuctField => new StructField(stuctField.name, getDataType(stuctField.dataType).getOrElse(stuctField.dataType))).toArray)

  private def getDataType(dataType: DataType): Option[DataType] = {
    val answer = dataType.typeName.toUpperCase match {
      case "FLOAT" => DoubleType
      case "SHORT" => IntegerType
      case _ => null
    }
    if (answer != null) Option(answer) else None

  }

  /*
   * This will compare two schema and check whether @readSchema is exist in @mdSchema
   * @param readSchema schema from input
   * @param mdSchema MetaData schema from metadata
   * @return Boolean true or false(Exception)
   */
  def compareSchema(readSchema: StructType, mdSchema: StructType): Boolean = {

    val metaDataSchema = mdSchema.toList
    val inputReadSchema = readSchema.toList

    var dbDataType: DataType = null
    var dbFieldName: String = null

    inputReadSchema.foreach(f = inSchema => {
      var fieldExist = metaDataSchema.exists(ds => {
        dbDataType = ds.dataType
        dbFieldName = ds.name
        ds.name.equalsIgnoreCase(inSchema.name)
      })
      if (fieldExist) {
        if (!(inSchema.dataType.typeName.equalsIgnoreCase(dbDataType.typeName))) {
          LOG.error("Field '" + inSchema.name + "', data type does not match expected type:" + dbDataType + ", got type:" + inSchema.dataType)
          throw SchemaMismatchException("Field '" + inSchema.name + "' data type does not match expected type:" + dbDataType + ", got type:" + inSchema.dataType)
        }
      } else {
        LOG.error("Field '" + inSchema.name + "' does not exist in metadata")
        throw SchemaMismatchException("Input schema does not match with metadata schema, "
          + "Field '" + inSchema.name + "' does not exist in metadata")
      }
    })
    true
  }
}
