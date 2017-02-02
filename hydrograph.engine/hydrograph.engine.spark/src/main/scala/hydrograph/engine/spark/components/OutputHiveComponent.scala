package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.OutputFileHiveTextEntity
import hydrograph.engine.core.component.entity.base.HiveEntityBase
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._


/**
  * Created by arshadalis on 12/9/2016.
  */
class OutputHiveComponent(entity: HiveEntityBase, oComponentParameters: BaseComponentParams) extends SparkFlow {

  val TEMPTABLE = "tempTable"
  val HIVETEXTTABLE="TEXTFILE"
  val HIVEPARQUETTABLE="PARQUET"
  val LOG = LoggerFactory.getLogger(classOf[OutputHiveComponent])

  override def execute(): Unit = {
    LOG.trace("In method execute()")
    val sparkSession = oComponentParameters.getSparkSession()
    import sparkSession.sql

    oComponentParameters.getDataFrame().createOrReplaceTempView(TEMPTABLE)

    sql("set hive.exec.dynamic.partition.mode=nonstrict")
    sql("CREATE DATABASE IF NOT EXISTS "+entity.getDatabaseName)
    sql(constructCreateTableQuery(entity))
    sql(constructInsertIntoTableQuery(entity))

    LOG.info("Created Output Hive Component " + entity.getComponentId + " in batch " + entity.getBatch + " to write Hive table " + entity.getDatabaseName + "." + entity.getTableName)

  }



  def constructCreateTableQuery(entity: HiveEntityBase): String = {
    LOG.trace("In method constructCreateTableQuery() which returns constructed query to execute with spark-sql")
    var query = ""
    val database = entity.getDatabaseName
    val table = entity.getTableName
    val isExternalTable=entity.getExternalTablePathUri!=null
    val componentName=getComponentName(entity)


    if(isExternalTable)
     query =query+ "CREATE EXTERNAL TABLE IF NOT EXISTS " + database + "." + table + " "
    else
    query = query+"CREATE TABLE IF NOT EXISTS " + database + "." + table + " "


    val listOfAllFields = entity.getFieldsList.asScala.toList
    val partitionKeys = entity.getPartitionKeys

    query = query + getFieldsForCreateTableHiveQuery(listOfAllFields.filter(field => (!partitionKeys.contains(field.getFieldName))))

    if (partitionKeys != null && partitionKeys.length > 0) {
      query = query + " PARTITIONED BY " + getFieldsForCreateTableHiveQuery(listOfAllFields.filter(field => (partitionKeys.contains(field.getFieldName))))
    }

    if (componentName.equals(HIVETEXTTABLE))
      query = query + " ROW FORMAT DELIMITED FIELDS TERMINATED BY '" + (entity.asInstanceOf[OutputFileHiveTextEntity]).getDelimiter + "'"

    query = query + " STORED AS " + componentName

    if(isExternalTable)
    query = query +" LOCATION '"+entity.getExternalTablePathUri+"'"

    query
  }


  def getComponentName(entity:HiveEntityBase):String={
    LOG.trace("In method getComponentName() which returns type of hive table")
    if (entity.isInstanceOf[OutputFileHiveTextEntity])
      HIVETEXTTABLE
    else
      HIVEPARQUETTABLE
  }



  def constructInsertIntoTableQuery(entity: HiveEntityBase): String = {
    LOG.trace("In method constructInsertIntoTableQuery() which returns constructed query to execute with spark-sql")
    var query = ""
    val database = entity.getDatabaseName
    val table = entity.getTableName

    if(entity.getOverWrite)
      query = "INSERT OVERWRITE TABLE " + database + "." + table + " "
    else
      query = "INSERT INTO TABLE " + database + "." + table + " "

    if (entity.getPartitionKeys.length > 0)
      query = query + " PARTITION (" + entity.getPartitionKeys.mkString(",") + ") "

    query = query + "select " + getFieldsForSelectHiveQuery(entity.getFieldsList.asScala.toList) + " from "+TEMPTABLE

    query
  }

  def getFieldsForSelectHiveQuery(listOfFields: List[SchemaField]): String = {

    listOfFields.map(field => field.getFieldName).mkString(",")
  }

  def getFieldsForCreateTableHiveQuery(listOfFields: List[SchemaField]): String = {

    "(" + listOfFields.map(field => field.getFieldName + " " + mapDataType(field.getFieldDataType,field)).mkString(",") + ")"
  }

  def mapDataType(str: String,schemaField:SchemaField): String = (str,schemaField) match {
    case ("java.lang.Integer",_) => "INT"
    case ("java.lang.String",_) => "STRING"
    case ("java.lang.Float",_)  => "FLOAT"
    case ("java.lang.Double",_)  => "DOUBLE"
    case ("java.lang.Short",_)  => "SMALLINT"
    case ("java.lang.Long",_)  => "BIGINT"
    case ("java.lang.Boolean",_)  => "BOOLEAN"
    case ("java.math.BigDecimal",sf) => "DECIMAL("+sf.getFieldPrecision+","+sf.getFieldScale+")"
    case ("java.util.Date",sf) => if(sf.getFieldFormat.matches(".*[H|m|s|S].*")) "TIMESTAMP" else "DATE"

  }

}
