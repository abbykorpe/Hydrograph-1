package hydrograph.engine.spark.components.utils

import java.sql.{Connection, ResultSetMetaData, SQLException}
import java.util.Properties

import hydrograph.engine.core.component.entity.OutputRDBMSEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import org.apache.derby.impl.sql.compile.BooleanTypeCompiler
import org.apache.spark.sql.execution.datasources.jdbc.{JDBCRDD, JdbcUtils}
import org.apache.spark.sql.jdbc.JdbcDialects
import org.apache.spark.sql.types._
import org.slf4j.{Logger, LoggerFactory}

import scala.math._

/**
  * Created by santlalg on 12/12/2016.
  */
case class DbTableUtils() {

  val LOG: Logger = LoggerFactory.getLogger(classOf[DbTableUtils])

  /*
   * This will generate crate table query
   * @param outputRDBMSEntity
   * @return String create query
   */
  def getCreateTableQuery(outputRDBMSEntity: OutputRDBMSEntity): String = {

    val fieldsCreator = new InputOutputFieldsAndTypesCreator[OutputRDBMSEntity](outputRDBMSEntity);
    val fieldsDataType = fieldsCreator.getFieldDataTypes();
    val fieldsScale = fieldsCreator.getFieldScale();
    val fieldsPrecision = fieldsCreator.getFieldPrecision();
    val fieldFormat = fieldsCreator.getFieldFormat();
    val columnDefs = JavaToSQLTypeMapping.createTypeMapping(outputRDBMSEntity.getDatabaseType(), fieldsDataType, fieldsScale, fieldsPrecision, fieldFormat);
    val DB_TYPE_ORACLE = "oracle"
    LOG.trace("Generating create query for " + outputRDBMSEntity.getDatabaseName
      + " database for table '" + outputRDBMSEntity.getTableName
      + "' with column name [" + fieldsCreator.getFieldNames.toList.mkString + "] "
      + " data type [ " + fieldsDataType.toList.mkString + "] "
      + " with column defination [" + columnDefs.toList.mkString + "] ")

    if (outputRDBMSEntity.getPrimaryKeys() != null) {
      LOG.debug("Generating create query for " + outputRDBMSEntity.getDatabaseName
        + " database for table '" + outputRDBMSEntity.getTableName
        + "' with column name [" + fieldsCreator.getFieldNames.toList.mkString + "] "
        + " primary key [" + outputRDBMSEntity.getPrimaryKeys + "] ")

      val primaryKeys = new Array[String](outputRDBMSEntity.getPrimaryKeys.size())
      val iterator = outputRDBMSEntity.getPrimaryKeys.iterator()
      var index: Int = 0
      if (outputRDBMSEntity.getDatabaseType.equalsIgnoreCase(DB_TYPE_ORACLE))
        while (iterator.hasNext) {
          primaryKeys(index) = iterator.next().getName.toUpperCase
          index += 1
        }
      else
        while (iterator.hasNext) {
          primaryKeys(index) = iterator.next().getName
          index += 1
        }
      new DbTableDescriptor(outputRDBMSEntity.getTableName, fieldsCreator.getFieldNames, columnDefs, primaryKeys, outputRDBMSEntity.getDatabaseType).getCreateTableStatement()
    }
    else {
      LOG.debug("Generating create query for " + outputRDBMSEntity.getDatabaseName
        + " database for table '" + outputRDBMSEntity.getTableName
        + "' with column name [" + fieldsCreator.getFieldNames.toList.mkString + "] ")

      new DbTableDescriptor(outputRDBMSEntity.getTableName, fieldsCreator.getFieldNames, columnDefs, null, outputRDBMSEntity.getDatabaseType).getCreateTableStatement()
    }
  }

  /*
   * This will return metadata schema
   * @param connectionURL database connection
   * @param tableName
   * @param properties
   * @return structType schema from metadata
   */
  def getTableSchema(connectionURL: String, tableName: String, properties: Properties): StructType = {
    //JDBCRDD.resolveTable(connectionURL, tableName, properties)
    resolveTable(connectionURL, tableName, properties)
  }

  /*
   * This will generate select query
   * @param fieldList
   * @param tableName
   * @return String select query
   */
  def getSelectQuery(fieldList: List[SchemaField], tableName: String): String = {
    val query = "select " + fieldList.map(f => f.getFieldName).mkString(", ") + " from " + tableName
    LOG.debug("Select query :  " + query)
    query
  }

  def resolveTable(url: String, table: String, properties: Properties): StructType = {
    //val dialect = JdbcDialects.get(url)

    val conn: Connection = JdbcUtils.createConnectionFactory(url, properties)()
    try {
      val statement = conn.prepareStatement(s"SELECT * FROM $table WHERE 1=0")
      try {
        val rs = statement.executeQuery()
        try {
          val rsmd = rs.getMetaData
          val ncols = rsmd.getColumnCount
          val fields = new Array[StructField](ncols)
          var i = 0
          while (i < ncols) {
            val columnName = rsmd.getColumnLabel(i + 1)
            val dataType = rsmd.getColumnType(i + 1)
            val typeName = rsmd.getColumnTypeName(i + 1)
            val fieldSize = rsmd.getPrecision(i + 1)
            val fieldScale = rsmd.getScale(i + 1)
            val isSigned = {
              try {
                rsmd.isSigned(i + 1)
              } catch {
                // Workaround for HIVE-14684:
                case e: SQLException if
                e.getMessage == "Method not supported" &&
                  rsmd.getClass.getName == "org.apache.hive.jdbc.HiveResultSetMetaData" => true
              }
            }
            val nullable = rsmd.isNullable(i + 1) != ResultSetMetaData.columnNoNulls
            val metadata = new MetadataBuilder()
              .putString("name", columnName)
              .putLong("scale", fieldScale)
            val columnType =
              //dialect.getCatalystType(dataType, typeName, fieldSize, metadata).getOrElse(
               // getCatalystType(dataType, fieldSize, fieldScale, isSigned) //)
              getDataType (typeName,fieldSize,fieldScale,isSigned)
           fields(i) = StructField(columnName, columnType, nullable, metadata.build())
            i = i + 1
          }
          return new StructType(fields)
        } finally {
          rs.close()
        }
      } finally {
        statement.close()
      }
    } finally {
      conn.close()
    }

    throw new RuntimeException("This line is unreachable.")
  }

  private def getDataType(sqlType: String, precision: Int, scale: Int, signed: Boolean) : DataType = {

    val answer = sqlType.toUpperCase match {
      case "FLOAT"  => FloatType
      case "SMALLINT" => ShortType
      case "TINYINT" => BooleanType
      case "BIGINT" => LongType
      case "INT"  => IntegerType
      case "VARCHAR" =>StringType
      case "DOUBLE" => DoubleType
      case "DATE" => DateType
      case "TIMESTAMP" => TimestampType
      case "DECIMAL"  if precision != 0 || scale != 0  => getDecimalType(precision, scale)
    }
    answer
  }

 def getDecimalType(precision: Int, scale: Int): DecimalType = {
    val MAX_PRECISION = 38
    val MAX_SCALE = 38
    DecimalType(min(precision, MAX_PRECISION), min(scale, MAX_SCALE))
  }
}




