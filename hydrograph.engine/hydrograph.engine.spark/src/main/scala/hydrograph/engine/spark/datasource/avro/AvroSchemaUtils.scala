package hydrograph.engine.spark.datasource.avro

import scala.beans.BeanProperty
import scala.collection.JavaConversions._


import org.apache.avro.Schema

object AvroSchemaUtils {

  object AvroTableProperties extends Enumeration {

    val SCHEMA_LITERAL = new AvroTableProperties("avro.schema.literal")

    val SCHEMA_URL = new AvroTableProperties("avro.schema.url")

    val SCHEMA_NAMESPACE = new AvroTableProperties("avro.schema.namespace")

    val SCHEMA_NAME = new AvroTableProperties("avro.schema.name")

    val SCHEMA_DOC = new AvroTableProperties("avro.schema.doc")

    val AVRO_SERDE_SCHEMA = new AvroTableProperties("avro.serde.schema")

    val SCHEMA_RETRIEVER = new AvroTableProperties("avro.schema.retriever")

    class AvroTableProperties(@BeanProperty val propName: String) extends Val

    implicit def convertValue(v: Value): AvroTableProperties = v.asInstanceOf[AvroTableProperties]
  }

  @Deprecated
  val SCHEMA_LITERAL = "avro.schema.literal"

  @Deprecated
  val SCHEMA_URL = "avro.schema.url"

  @Deprecated
  val SCHEMA_NAMESPACE = "avro.schema.namespace"

  @Deprecated
  val SCHEMA_NAME = "avro.schema.name"

  @Deprecated
  val SCHEMA_DOC = "avro.schema.doc"

  @Deprecated
  val AVRO_SERDE_SCHEMA = AvroTableProperties.AVRO_SERDE_SCHEMA.getPropName

  @Deprecated
  val SCHEMA_RETRIEVER = AvroTableProperties.SCHEMA_RETRIEVER.getPropName

  val SCHEMA_NONE = "none"

  val EXCEPTION_MESSAGE = "Neither " + AvroTableProperties.SCHEMA_LITERAL.getPropName + 
    " nor " + 
    AvroTableProperties.SCHEMA_URL.getPropName + 
    " specified, can't determine table schema"

  def getSchemaFor(str: String): Schema = {
    val parser = new Schema.Parser()
    parser.parse(str)
  }
}
