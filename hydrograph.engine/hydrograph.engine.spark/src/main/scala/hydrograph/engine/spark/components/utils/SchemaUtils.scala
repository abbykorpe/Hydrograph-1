package hydrograph.engine.spark.components.utils

import org.apache.spark.sql.types._
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by santlalg on 1/2/2017.
  */
case class SchemaUtils() {

  val LOG: Logger = LoggerFactory.getLogger(classOf[SchemaUtils])

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
        ds.name.equals(inSchema.name)
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

case class SchemaMismatchException(message: String = "", cause: Throwable = null) extends Exception(message, cause)


