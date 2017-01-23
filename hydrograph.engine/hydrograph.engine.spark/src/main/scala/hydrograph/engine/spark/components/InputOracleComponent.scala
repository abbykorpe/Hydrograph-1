package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputRDBMSEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{SchemaCreator, SchemaMismatchException}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._
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
      val df = sparkSession.read.jdbc(connectionURL, tableorQuery, properties)

      compareSchema(getSchema(schemaField), getMappedSchema(df.schema))
      val key = inputRDBMSEntity.getOutSocketList.get(0).getSocketId
      Map(key -> df)
    } catch {
      case e: Exception =>
        LOG.error("Error in Input  Oracle input component '" + inputRDBMSEntity.getComponentId + "', Error" + e.getMessage, e)
        throw new RuntimeException("Error in Input Oracle Component " + inputRDBMSEntity.getComponentId, e)
    }
  }

  def getSchema(schema: StructType): StructType = StructType(schema.toList.map(stuctField => new StructField(stuctField.name, getInputDataType(stuctField.dataType).getOrElse(stuctField.dataType))).toArray)

  private def getInputDataType(dataType: DataType): Option[DataType] = {
    val answer = dataType.typeName.toUpperCase match {
      case "SHORT" => IntegerType
      case "BOOLEAN" => StringType
      case "DATE" => TimestampType
      case _ => null
    }
    if (answer != null) Option(answer) else None

  }

  def getMappedSchema(schema: StructType): StructType = StructType(schema.toList.map(stuctField => new StructField(stuctField.name, getDataType(stuctField.dataType).getOrElse(stuctField.dataType))).toArray)

  private def getDataType(dataType: DataType): Option[DataType] = {
    val datatype = dataType.typeName.toUpperCase
    val answer = getDataTypes(datatype)
    if (answer != null) Option(answer) else None
  }

  def getDataTypes(datatype: String): DataType = {
    if (datatype.matches("[(DECIMAL(]+[0-5],0[)]")) IntegerType else if (datatype.matches("[(DECIMAL(]+([6-9]|10),0[)]")) IntegerType else if (datatype.matches("[(DECIMAL(]+(1[1-9]),0[)]")) LongType else null
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
        if (!(inSchema.dataType.typeName.equalsIgnoreCase("Float") || inSchema.dataType.typeName.equalsIgnoreCase("double")))
          if (!(inSchema.dataType.typeName.equalsIgnoreCase(dbDataType.typeName))) {
            LOG.error("Field '" + inSchema.name + "', data type does not match expected type:" + dbDataType + ", got type:" + inSchema.dataType)
            throw SchemaMismatchException("Field '" + inSchema.name + "' data type does not match expected type:" + dbDataType + ", got type:" + inSchema.dataType)
          }
      }
      else {
        LOG.error("Field '" + inSchema.name + "' does not exist in metadata")
        throw SchemaMismatchException("Input schema does not match with metadata schema, "
          + "Field '" + inSchema.name + "' does not exist in metadata")
      }
    })
    true
  }
}
