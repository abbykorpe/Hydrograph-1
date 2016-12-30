package hydrograph.engine.spark.components.utils

import java.util.Properties

import hydrograph.engine.core.component.entity.OutputRDBMSEntity
import org.apache.spark.sql.execution.datasources.jdbc.JDBCRDD
import org.apache.spark.sql.types.{DataType, StructType}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
  * Created by santlalg on 12/12/2016.
  */
case class TableCreator() {

  val LOG: Logger = LoggerFactory.getLogger(classOf[TableCreator])

  def getCreateTableQuery(outputRDBMSEntity: OutputRDBMSEntity): String = {

    val fieldsCreator = new InputOutputFieldsAndTypesCreator[OutputRDBMSEntity](outputRDBMSEntity);
    val fieldsDataType = fieldsCreator.getFieldDataTypes();
    val fieldsScale = fieldsCreator.getFieldScale();
    val fieldsPrecision = fieldsCreator.getFieldPrecision();
    val columnDefs = JavaToSQLTypeMapping.createTypeMapping(outputRDBMSEntity.getDatabaseType(), fieldsDataType, fieldsScale, fieldsPrecision);
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

  def getTableSchema(connectionURL: String, tableName: String, properties: Properties): StructType = {
    JDBCRDD.resolveTable(connectionURL, tableName, properties)
  }

  def compareSchema(dbSchema: StructType, readSchema: StructType): Boolean = {

      val databaseSchema = dbSchema.toList
      val inputReadSchema = readSchema.toList

      var dataType: DataType = null

      inputReadSchema.foreach(inSchema=>{
        var fieldExist = databaseSchema.exists(ds => {
          dataType = ds.dataType
          ds.name.equals(inSchema.name)
        })
        if (fieldExist) {
          println(dataType + "   " + dataType.typeName )
          if(!(dataType.typeName.equals(inSchema.dataType.typeName))){
            LOG.error("Field '"+inSchema.name + "', data type does not match expected:"
              + dataType + ", got:" + inSchema.dataType)
            throw SchemaMismatchException("Field  '"+inSchema.name + "', data type does not match expected:"
              + dataType + ", got:" + inSchema.dataType)
          }
        } else {
          LOG.error("Field '" + inSchema.name + "' does not exist in database")
          throw SchemaMismatchException("Input schema does not match with database schema, "
            + "Field '" + inSchema.name + "' does not exist in database")
        }
      })
    true
  }
}

case class SchemaMismatchException(message: String = "", cause: Throwable = null) extends Exception(message, cause)
