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
}




