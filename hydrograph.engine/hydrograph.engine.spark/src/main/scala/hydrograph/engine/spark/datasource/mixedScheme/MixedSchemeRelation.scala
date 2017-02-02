package hydrograph.engine.spark.datasource.mixedScheme

import java.text.SimpleDateFormat

import hydrograph.engine.core.constants.Constants
import hydrograph.engine.spark.helper.DelimitedAndFixedWidthHelper
import hydrograph.engine.spark.input.format.DelimitedAndFixedWidthInputFormat
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapred.InputFormat
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.{StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions._

case class MixedSchemeRelation(
                                location: Option[String],
                                charset: String,
                                quote: String,
                                safe: Boolean,
                                strict: Boolean,
                                dateFormats:  List[SimpleDateFormat],
                                lengthsAndDelimiters: String,
                                lengthsAndDelimitersType: String,
                                nullValue: String,
                                treatEmptyValuesAsNullsFlag: Boolean,
                                userSchema: StructType
                              )(@transient val sqlContext: SQLContext)
  extends BaseRelation with TableScan {

  private val LOG: Logger = LoggerFactory.getLogger(classOf[MixedSchemeRelation])
  override val schema: StructType = userSchema

  override def buildScan: RDD[Row] = {
//    val schemaFields: Array[StructField] = schema.fields
    //    val rowArray: Array[Any] = new Array[Any](schemaFields.length)
    val sc = sqlContext.sparkContext
    implicit val conf: Configuration = sc.hadoopConfiguration
    conf.setBoolean("mapred.mapper.new-api", false)
    conf.setClass("mapred.input.format.class", classOf[DelimitedAndFixedWidthInputFormat], classOf[InputFormat[_, _]])
    conf.set("charsetName", charset)
    conf.set("quote", quote)
    conf.set("lengthsAndDelimiters", lengthsAndDelimiters)
    conf.setStrings("lengthsAndDelimitersType", lengthsAndDelimitersType)

    val input = sc.hadoopFile(location.get, classOf[DelimitedAndFixedWidthInputFormat], classOf[LongWritable], classOf[Text])

    val tokens = input.values.map( line =>
      DelimitedAndFixedWidthHelper.getFields(schema,
        line.toString, lengthsAndDelimiters.split(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR),
        lengthsAndDelimitersType.split(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR),
        safe, quote, dateFormats))

    tokens.flatMap { t => {
      Some(Row.fromSeq(t))
    }
    }
  }

}
