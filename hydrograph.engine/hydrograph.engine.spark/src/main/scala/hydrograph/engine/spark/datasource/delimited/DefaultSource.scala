/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package hydrograph.engine.spark.datasource.delimited

import java.util.{Locale, TimeZone}

import hydrograph.engine.core.custom.exceptions._
import hydrograph.engine.spark.datasource.utils.{TextFile, TypeCast}
import org.apache.commons.lang3.time.FastDateFormat
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions._
/**
  * The Class DefaultSource.
  *
  * @author Bitwise
  *
  */
class DefaultSource extends RelationProvider
  with SchemaRelationProvider with CreatableRelationProvider with Serializable {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[DefaultSource])
  /**
    * Creates a new relation for data store in delimited given parameters.
    * Parameters have to include 'path' and optionally 'delimiter', 'quote', and 'header'
    */

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
    createRelation(sqlContext, parameters, null)
  }

  private def fastDateFormat(dateFormat: String): FastDateFormat = if (!(dateFormat).equalsIgnoreCase("null")) {

//    val date = new FastDateFormat(dateFormat, Locale.getDefault)
//    date.setLenient(false)
//    date.setTimeZone(TimeZone.getDefault)
    val date = {
      try {
        FastDateFormat.getInstance(dateFormat,TimeZone.getDefault,Locale.getDefault)
      } catch {
        case e: IllegalArgumentException => throw new DateFormatException("\nError being Unparseable Date Format:[\"" + dateFormat + "\"]")
      }
    }
    date
  } else null

  private def getDateFormats(dateFormats: List[String]): List[FastDateFormat] = dateFormats.map{ e =>
    if (e.equals("null")){
      null
    } else {
      fastDateFormat(e)
    }
  }

  /**
    * Creates a new relation for data store in delimited given parameters and user supported schema.
    * Parameters have to include 'path' and optionally 'delimiter', 'quote', and 'header'
    */

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String], schema: StructType): DelimitedRelation = {

   // val path = parameters.getOrElse("path", throw new PathNotFoundException("path option must be specified for Input File Delimited Component"))

    val path = parameters.get("path").get

    if (path == null || path.equals("")){

      throw new PathNotFoundException("\nPath:[\"" + path + "\"]\nError being: " + "path option must be specified for Input File Delimited Component")
    }

    val fsPath = new Path(path)
    val fs = fsPath.getFileSystem(sqlContext.sparkContext.hadoopConfiguration)
    if(!fs.exists(fsPath)){
      throw new PathNotFoundException("\nPath:[\"" + path + "\"]\nError being: " + "input file path does not exist")
    }

    val delimiter: String = parameters.getOrElse("delimiter", ",")

    val delimiterString = if (delimiter.length > 0) {
      delimiter
    } else {
      throw new BadDelimiterFoundException("\nDelimiter:[\"" + delimiter + "\"]\nError being: Bad Delimiter found")
    }

    val inDateFormats: String = parameters.getOrElse("dateFormats", "null")
    val useHeader = parameters.getOrElse("header", "false")

    val headerFlag = if (useHeader.equals("true")) {
      true
    } else if (useHeader.equals("false")) {
      false
    } else {
      throw new BadArgumentException("\nHeader:[\"" + useHeader + "\"]\nError being: Bad Header found")
    }

    val strict = parameters.getOrElse("strict", "true")
    val strictFlag = if (strict.equals("true")) {
      true
    } else if (strict.equals("false")) {
      false
    } else {
      throw new BadArgumentException("\nStrict:[\"" + strict + "\"]\nError being: Bad Strict found")
    }

    val safe = parameters.getOrElse("safe", "false")
    val safeFlag = if (safe.equals("true")) {
      true
    } else if (safe.equals("false")) {
      false
    } else {
      throw new BadArgumentException("\nSafe:[\"" + safe + "\"]\nError being: Bad Safe found")
    }

    val nullValue: String = parameters.getOrElse("nullValue", "")
    val quote = if(parameters.getOrElse("quote", "\"") == null)  "\"" else parameters.getOrElse("quote", "\"")

    val quoteChar: String = if (quote.length > 0) {
      quote
    } else {
      throw new BadQuoteFoundException("\nQuote:[\"" + quote + "\"]\nError being: Bad Quote found")
    }


    val dateFormat: List[FastDateFormat] = getDateFormats(inDateFormats.split("\t").toList)

    val delimitedParser = new HydrographDelimitedParser(
      delimiterString,
      quoteChar, null, strictFlag,
      safeFlag, dateFormat, schema)

    val treatEmptyValuesAsNulls: Boolean = parameters.getOrElse("treatEmptyValuesAsNulls", "false").toBoolean

    val charset: String = parameters.getOrElse("charset", TextFile.DEFAULT_CHARSET.name())
    DelimitedRelation(
      charset,
      path,
      headerFlag,
      delimitedParser,
      nullValue,
      treatEmptyValuesAsNulls,
      schema
    )(sqlContext)
  }

  /*Saving Data in delimited format*/

  override def createRelation(sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String], data: DataFrame): BaseRelation = {
    LOG.trace("In method createRelation for creating Delimited Output File")

    val path = parameters.get("path").get

    if (path == null || path.equals("")){

      throw new PathNotFoundException("\nPath:[\"" + path + "\"]\nError being: " + "path option must be specified for Output File Delimited Component")
    }

    val fsPath = new Path(path)
    val fs: FileSystem = fsPath.getFileSystem(sqlContext.sparkContext.hadoopConfiguration)

    val isSave = if (fs.exists(fsPath)) {
      mode match {
        case SaveMode.Append =>
          LOG.error("\nError being Output file append operation is not supported")
          throw new FileAppendException("\nError being Output file append operation is not supported")
        case SaveMode.Overwrite =>
          if (fs.delete(fsPath, true))
            true
          else{
           LOG.error("\nError being Output directory path :[\"" + path + "\"] "+" cannot be deleted")
            throw new FileDeleteException("\nError being Output directory path :[\"" + path + "\"] "+" cannot be deleted")
          }
        case SaveMode.ErrorIfExists =>
          LOG.error("\nError being Output directory path :[\"" + path + "\"] "+" already exists")
        throw new FileAlreadyExistsException("\nError being Output directory path :[\"" + path + "\"] "+" already exists")
        case SaveMode.Ignore => false


      }
    } else
      true

    if (isSave) {
      saveAsDelimitedFile(data, parameters, path)
    }
    createRelation(sqlContext, parameters, data.schema)
  }

  private def saveAsDelimitedFile(dataFrame: DataFrame, parameters: Map[String, String], path: String) = {
    LOG.trace("In method saveAsFW for creating Delimited Output File")
    val delimiter: String = parameters.getOrElse("delimiter", ",")

    val delimiterString = if (delimiter.length >0) {
      delimiter
    } else {
      throw new BadDelimiterFoundException("\nDelimiter:[\"" + delimiter + "\"]\nError being: Bad Delimiter found")
    }


    val outDateFormats: String = parameters.getOrElse("dateFormats","null")
    val strict: Boolean = parameters.getOrElse("strict", "true").toBoolean
    val quote = if(parameters.getOrElse("quote", "\"") == null)  "\"" else parameters.getOrElse("quote", "\"")

    val quoteChar: String = if (quote.length > 0) {
      quote
    } else {
      throw new BadQuoteFoundException("\nQuote:[\"" + quote + "\"]\nError being: Bad Quote found")
    }

    val generateHeader: Boolean = parameters.getOrElse("header", "true").toBoolean
    val schema: StructType = dataFrame.schema

    val dateFormat: List[FastDateFormat] = getDateFormats(outDateFormats.split("\t").toList)

    val header: String = if (generateHeader) {
      dataFrame.columns.mkString(delimiterString)
    } else {
      "" // There is no need to generate header in this case
    }
   val recordSeparator : String = System.getProperty("line.separator")
   val strRDD = dataFrame.rdd.mapPartitions {

      case (iter) =>
        new Iterator[String] {
          var firstRow: Boolean = generateHeader

          override def hasNext: Boolean = iter.hasNext || firstRow
          override def next: String = {
            if (iter.nonEmpty) {
              val tuple = iter.next()
              if (strict && tuple.length != schema.fields.length){
                throw new LengthMisMatchException("Output row does not have enough length to parse all fields. Output length is "
                  + tuple.length
                  + ". Number of fields in output schema are "
                  + schema.fields.length
                  + "\nRow being parsed: " + tuple)

              }

             /* val values: Seq[AnyRef] = tuple.toSeq.zipWithIndex.map({
                case (value, i) =>
                  val castedValue = TypeCast.outputValue(value, schema.fields(i).dataType, dateFormat(i))
                  var string:String = ""
                  if (castedValue != null ){
                    string = castedValue.toString
                    if (string.contains(quote))
                      string = string.replaceAll(quote, quote + quote)
                    if (string.contains(delimiter))
                      string = quote + string + quote
                  }
                  string
              })*/
              val fields = new Array[String](schema.fields.length)
              var i = 0
              while (i < tuple.length){
                val castedValue = TypeCast.outputValue(tuple.get(i), schema.fields(i).dataType, dateFormat(i))
                var string:String = ""
                if (castedValue != null ){
                  string = castedValue.toString
                  if (string.contains(quoteChar))
                    string = string.replaceAll(quoteChar, quoteChar + quoteChar)
                  if (string.contains(delimiterString))
                    string = quoteChar + string + quoteChar
                }
                fields(i) = string
                i = i + 1
                string
              }

              val row: String = fields.mkString(delimiterString)
              if (firstRow) {
                firstRow = false
                header + recordSeparator + row
              } else {
                row
              }
            } else {
              firstRow = false
              header
            }
          }
        }
    }
    strRDD.saveAsTextFile(path)
    LOG.info("Delimited Output File is successfully created at path : " + path)
  }

}
