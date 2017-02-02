
package hydrograph.engine.spark.datasource.mixedScheme

import java.text.SimpleDateFormat
import java.util.{Locale, TimeZone}

import hydrograph.engine.core.constants.Constants
import hydrograph.engine.spark.datasource.utils.{TextFile, TypeCast}
import hydrograph.engine.spark.helper.DelimitedAndFixedWidthHelper
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.slf4j.{Logger, LoggerFactory}


class DefaultSource extends RelationProvider
  with SchemaRelationProvider with CreatableRelationProvider with Serializable {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[DefaultSource])

  /**
    * Creates a new relation for data store in delimited given parameters.
    * Parameters have to include 'path' and optionally 'delimiter', 'quote', and 'header'
    */

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
    createRelation(sqlContext, parameters, null)
  }

  /**
    * Creates a new relation for data store in delimited given parameters and user supported schema.
    * Parameters have to include 'path' and optionally 'delimiter', 'quote', and 'header'
    */

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String], schema: StructType): MixedSchemeRelation = {

    val path = parameters.getOrElse("path", throw new RuntimeException("path option must be specified for Input File MixedScheme Component"))
    val inDateFormats: String = parameters.getOrElse("dateFormats", "null")
    val strict: Boolean = parameters.getOrElse("strict", "true").toBoolean
    val safe: Boolean = parameters.getOrElse("safe", "false").toBoolean
    val nullValue: String = parameters.getOrElse("nullValue", "")
    val quote: String = if (parameters.getOrElse("quote", "\"") == null) "\"" else parameters.getOrElse("quote", "\"")
    val treatEmptyValuesAsNulls: Boolean = parameters.getOrElse("treatEmptyValuesAsNulls", "false").toBoolean
    val charset: String = parameters.getOrElse("charset", TextFile.DEFAULT_CHARSET.name())
    val lengthsAndDelimiters = parameters.getOrElse("lengthsAndDelimiters", "")
    val lengthsAndDelimitersType = parameters.getOrElse("lengthsAndDelimitersType", "")

    if (path == null || path.equals("")) {
      LOG.error("MixedScheme Input File path cannot be null or empty")
      throw new RuntimeException("MixedScheme Input File path cannot be null or empty")
    }

    val dateFormat: List[SimpleDateFormat] = getDateFormats(inDateFormats.split("\t").toList)

    MixedSchemeRelation(
      Some(path),
      charset,
      quote,
      safe,
      strict,
      dateFormat,
      lengthsAndDelimiters,
      lengthsAndDelimitersType,
      nullValue,
      treatEmptyValuesAsNulls,
      schema
    )(sqlContext)
  }


  private def simpleDateFormat(dateFormat: String): SimpleDateFormat = if (!(dateFormat).equalsIgnoreCase("null")) {
    val date = new SimpleDateFormat(dateFormat, Locale.getDefault)
    date.setLenient(false)
    date.setTimeZone(TimeZone.getDefault)
    date
  } else null

  private def getDateFormats(dateFormats: List[String]): List[SimpleDateFormat] = dateFormats.map{ e =>
    if (e.equals("null")){
      null
    } else {
      simpleDateFormat(e)
    }
  }

  /*Saving Data in delimited format*/

  override def createRelation(sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String], data: DataFrame): BaseRelation = {
    LOG.trace("In method createRelation for creating MixedScheme Output File")
    val path: String = parameters.getOrElse("path", throw new RuntimeException("path option must be specified for Output File MixedScheme Component"))
    if (path == null || path.equals("")) {
      LOG.error("MixedScheme Output File path cannot be null or empty")
      throw new RuntimeException("MixedScheme Output File path cannot be null or empty")
    }

    val fsPath: Path = new Path(path)
    val fs: FileSystem = fsPath.getFileSystem(sqlContext.sparkContext.hadoopConfiguration)

    val isSave = if (fs.exists(fsPath)) {
      mode match {
        case SaveMode.Append => LOG.error("Output file append operation is not supported")
          throw new RuntimeException("Output file append operation is not supported")
        case SaveMode.Overwrite =>
          if (fs.delete(fsPath, true))
            true
          else {
            LOG.error("Output directory path '" + path + "' cannot be deleted")
            throw new RuntimeException("Output directory path '" + path + "' cannot be deleted")
          }
        case SaveMode.ErrorIfExists =>
          LOG.error("Output directory path '" + path + "' already exists")
          throw new RuntimeException("Output directory path '" + path + "' already exists")
        case SaveMode.Ignore => false
      }
    } else
      true


    if (isSave) {
      saveAsDelimitedFile(data, parameters, path)
    }
    createRelation(sqlContext, parameters, data.schema)
  }

  def saveAsDelimitedFile(dataFrame: DataFrame, parameters: Map[String, String], path: String) = {
    LOG.trace("In method saveAsFW for creating MixedScheme Output File")
    val outDateFormats: String = parameters.getOrElse("dateFormats", "null")
    val strict: Boolean = parameters.getOrElse("strict", "true").toBoolean
    val quote: String = if (parameters.getOrElse("quote", "\"") == null) "\"" else parameters.getOrElse("quote", "\"")
    val schema: StructType = dataFrame.schema
    val lengthsAndDelimiters = parameters.getOrElse("lengthsAndDelimiters", "")
    val lengthsAndDelimitersType = parameters.getOrElse("lengthsAndDelimitersType", "")
    val hasaNewLineField: Boolean = DelimitedAndFixedWidthHelper.hasaNewLineField(lengthsAndDelimiters.split(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR))
    val dateFormat: List[SimpleDateFormat] = getDateFormats(outDateFormats.split("\t").toList)
    var outputRow: String = ""
    var recordToBeSpilled: String = ""

    val strRDD = dataFrame.rdd.mapPartitionsWithIndex {

      case (index, iter) =>
        new Iterator[String] {
          override def hasNext: Boolean = iter.hasNext

          override def next: String = {
            val tuple = iter.next()
            if (strict && tuple.length != schema.fields.length) {
              LOG.error("Output row does not have enough length to parse all fields. Output length is "
                + tuple.length
                + ". Number of fields in output schema are "
                + schema.fields.length
                + "\nRow being parsed: " + tuple)
              throw new RuntimeException("Output row does not have enough length to parse all fields. Output length is "
                + tuple.length
                + ". Number of fields in output schema are "
                + schema.fields.length
                + "\nRow being parsed: " + tuple)
            }

            val values: Seq[AnyRef] = tuple.toSeq.zipWithIndex.map({
              case (value, i) =>
                val castedValue = TypeCast.outputValue(value, schema.fields(i).dataType, dateFormat(i))
                var string: String = ""
                if (castedValue != null) {
                  string = castedValue.toString
                }
                string
            })

            outputRow = outputRow + DelimitedAndFixedWidthHelper.createLine(values.mkString(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR),
              lengthsAndDelimiters.split(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR),
              lengthsAndDelimitersType.split(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR),
              strict, ' ', quote)
            if ((hasaNewLineField || DelimitedAndFixedWidthHelper.containsNewLine(outputRow)) && hasNext) {
              recordToBeSpilled = DelimitedAndFixedWidthHelper.spillOneLineToOutput(outputRow, lengthsAndDelimiters.split(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR))
              outputRow = outputRow.replace(recordToBeSpilled, "").trim
            } else if (hasNext){
              recordToBeSpilled = Constants.LENGTHS_AND_DELIMITERS_SEPARATOR
            } else {
              recordToBeSpilled = outputRow
            }
            recordToBeSpilled
          }
        }
    }
    strRDD.filter(e => !e.equals(Constants.LENGTHS_AND_DELIMITERS_SEPARATOR)).saveAsTextFile(path)
    LOG.info("MixedScheme Output File is successfully created at path : " + path)
  }

}
