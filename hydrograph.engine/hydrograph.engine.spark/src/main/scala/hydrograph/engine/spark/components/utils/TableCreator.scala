package hydrograph.engine.spark.components.utils

import hydrograph.engine.core.component.entity.OutputRDBMSEntity
import org.slf4j.{Logger, LoggerFactory}

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

}
