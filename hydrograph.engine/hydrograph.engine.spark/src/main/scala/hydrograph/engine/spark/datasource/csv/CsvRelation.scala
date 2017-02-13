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
package hydrograph.engine.spark.datasource.csv

import java.sql.Timestamp
import java.text.SimpleDateFormat

import hydrograph.engine.spark.datasource.utils.{TextFile, TypeCast}
import org.apache.commons.csv._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types._
import org.json4s.ParserUtil.ParseException

import scala.collection.JavaConversions._
import scala.util.control.NonFatal

case class CsvRelation( componentId:String,
                        charset: String,
                        path: String,
                        useHeader: Boolean,
                        delimiter: Char,
                        quote: Character,
                        treatEmptyValuesAsNullsFlag:Boolean,
                        dateFormats:List[SimpleDateFormat],
                        userSchema: StructType = null
    )(@transient val sqlContext: SQLContext)
  extends BaseRelation with TableScan{


  override val schema: StructType = userSchema
  val csvFormat:CSVFormat = CSVFormat.DEFAULT.withQuote(quote).withDelimiter(delimiter).withSkipHeaderRecord(true);
  private def tokenRdd(baseRDD: RDD[String], header: Array[String]): RDD[String] = {



      // If header is set, make sure firstLine is materialized before sending to executors.
    val firstLine=baseRDD.first
      val filterLine = if (useHeader) firstLine else null

    if (useHeader) baseRDD.filter(_ != filterLine) else baseRDD
      /*baseRDD.mapPartitions { iter =>
        val csvIter = if (useHeader) {
          iter.filter(_ != filterLine)
        } else {
          iter
        }*/
//        parseCSV(csvIter, csvFormat)

//    }
  }

  @throws(classOf[RuntimeException])
  override def buildScan: RDD[Row] = {
    val baseRDD = TextFile.withCharset(sqlContext.sparkContext, path, charset)
    val schemaFields:Array[StructField] = schema.fields
    val rowArray = new Array[Any](schemaFields.length)

    tokenRdd(baseRDD, schemaFields.map(_.name)).map {
      var tokens:Array[Any] = null
      line:String => {
        try{
          tokens = parseCSV(line, schemaFields)
        }catch{
          case e:RuntimeException => {
            throw new RuntimeException("Error in Input Delimited Component:[\""+
              componentId+"\"] for Record:[\""+line+"\"] , "+e.getMessage)
          }
        }

        var index: Int = 0
      /*  while (index < schemaFields.length) {
          val field = schemaFields(index)
          /*rowArray(index) = TypeCast.castTo(tokens(index), field.dataType,field.nullable,treatEmptyValuesAsNullsFlag, dateFormatter)*/
          rowArray(index) = TypeCast.inputValue(tokens(index), field.dataType, field.nullable,"", true, dateFormats(index))
          index = index + 1
        }*/
       /* val tuple = if (fields.isEmpty) {
          LOG.warn(s"Ignoring empty line: $line")
          List()
        } else {
          fields.toList
        }*/
        Row.fromSeq(tokens)
      }

/*
      tokens =>
            var index: Int = 0
            while (index < schemaFields.length) {
            val field = schemaFields(index)
            /*rowArray(index) = TypeCast.castTo(tokens(index), field.dataType,field.nullable,treatEmptyValuesAsNullsFlag, dateFormatter)*/
              rowArray(index) = TypeCast.inputValue(tokens(index), field.dataType, field.nullable,"", true, dateFormats(index))
            index = index + 1
          }
          Some(Row.fromSeq(rowArray))
*/

      }
    }

/*
  private def parseCSV(
      iter: Iterator[String],
      csvFormat: CSVFormat): Iterator[Array[String]] = {

*/
  @throws(classOf[Exception])
    private def parseCSV(line: String, schemaFields: Array[StructField]): Array[Any] = {
    val tokenArray = new Array[Any](schemaFields.length)
    val records = CSVParser.parse(line, csvFormat).getRecords
    if (records.isEmpty) {
              Array("")
    } else {
      var index = 0
      val record = records.head
      while (index < schemaFields.length) {
        val field = schemaFields(index)
        /*rowArray(index) = TypeCast.castTo(tokens(index), field.dataType,field.nullable,treatEmptyValuesAsNullsFlag, dateFormatter)*/
        try{
          tokenArray(index) = TypeCast.inputValue(record.get(index), field.dataType, field.nullable, "", true, dateFormats(index))
        }catch{
          case e:RuntimeException => {
            throw new RuntimeException("Field Name:[\""+field.name+"\"] "+e.getMessage)
          }
        }

        index = index + 1
      }
      tokenArray
          /*records.head.toArray.zipWithIndex.map(
            token => TypeCast.inputValue(token._1, schemaFields(token._2).dataType,
              schemaFields(token._2).nullable,"", true, dateFormats(token._2)))*/
    }

  }


}
