package hydrograph.engine.spark.components.utils

import hydrograph.engine.core.component.entity.OutputRDBMSEntity
import hydrograph.engine.core.component.utils.JavaToSQLTypeMapping
/**
  * Created by santlalg on 12/12/2016.
  */
object TableCreator {
  def getCreateTableQuery(outputRDBMSEntity: OutputRDBMSEntity): String = {
    val fieldsCreator = new InputOutputFieldsAndTypesCreator[OutputRDBMSEntity](outputRDBMSEntity);
    val fieldsDataType = fieldsCreator.getFieldDataTypes();
    val fieldsScale = fieldsCreator.getFieldScale();
    val fieldsPrecision = fieldsCreator.getFieldPrecision();
    val columnDefs = JavaToSQLTypeMapping.createTypeMapping(outputRDBMSEntity.getDatabaseType(), fieldsDataType, fieldsScale, fieldsPrecision);

    if (outputRDBMSEntity.getPrimaryKeys() != null) {
      val primaryKeys = new Array[String](outputRDBMSEntity.getPrimaryKeys.size())
      val iterator = outputRDBMSEntity.getPrimaryKeys.iterator()
      var index: Int = 0
      while (iterator.hasNext) {
        primaryKeys(index) = iterator.next().getName
        index += 1
      }
      new DbTableDescriptor(outputRDBMSEntity.getTableName, fieldsCreator.getFieldNames, columnDefs, primaryKeys).getCreateTableStatement()
    }
    else
      new DbTableDescriptor(outputRDBMSEntity.getTableName, fieldsCreator.getFieldNames, columnDefs, null).getCreateTableStatement()
  }
}
