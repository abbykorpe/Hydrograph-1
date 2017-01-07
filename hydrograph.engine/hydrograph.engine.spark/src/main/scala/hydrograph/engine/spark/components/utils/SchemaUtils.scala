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
        if (!(getDataType(inSchema.dataType).getOrElse(inSchema.dataType).typeName.equalsIgnoreCase(dbDataType.typeName))) {
          LOG.error("Field '" + inSchema.name + "', data type does not match expected type:" + dbDataType + ", got type:" + inSchema.dataType)
          throw SchemaMismatchException("Field '" + inSchema.name + "', data type does not match expected type:" + dbDataType + ", got type:" + inSchema.dataType)
        }
      } else {
        LOG.error("Field '" + inSchema.name + "' does not exist in metadata")
        throw SchemaMismatchException("Input schema does not match with metadata schema, "
          + "Field '" + inSchema.name + "' does not exist in metadata")
      }
    })
    true
  }

  // mapped datatype as in mysql float is mapped to real and in org.apache.spark.sql.execution.datasources.jdbc.JDBCRDD real is mapped to DoubleType
  // In Mysql Short data type is not there, instead of Short SMALLINT is used and in org.apache.spark.sql.execution.datasources.jdbc.JDBCRDD SMALLINT is mapped to IntegerType
  // for comparing purpose here float -> DoubleType AND short -> IntegerType
  private def getDataType(dataType: DataType): Option[DataType] = {
    val answer = dataType.typeName.toUpperCase match {
      case "FLOAT" => DoubleType
      case "SHORT" => IntegerType
      case _ => null
    }
    if (answer != null) Option(answer) else None

  }
}

case class SchemaMismatchException(message: String = "", cause: Throwable = null) extends Exception(message, cause)


