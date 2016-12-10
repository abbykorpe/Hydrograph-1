
package hydrograph.engine.spark.delimited.datasource

import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.DataFrame
import hydrograph.engine.spark.datasource.utils.TypeCast

import hydrograph.engine.spark.datasource.utils.TextFile


class DefaultSource extends RelationProvider with SchemaRelationProvider with CreatableRelationProvider {

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

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String], schema: StructType): DelimitedRelation = {

    val path: String = checkPath(parameters)
    val delimiter: String = parameters.getOrElse("delimiter", ",")
    val inDateFormats: String = parameters.getOrElse("dateFormats", "null")
    val useHeader: Boolean = parameters.getOrElse("header", "false").toBoolean
    val strict: Boolean = parameters.getOrElse("strict", "true").toBoolean
    val safe: Boolean = parameters.getOrElse("safe", "false").toBoolean
    val nullValue: String = parameters.getOrElse("nullValue", "")
    val quote: String = if (parameters.getOrElse("quote", "\"") == null ) "\"" else parameters.getOrElse("quote", "\"")

    val delimitedParser = new HydrographDelimitedParser(
      delimiter,
      quote, null, strict,
      safe, inDateFormats.split("\t"), schema)

    val treatEmptyValuesAsNulls: Boolean = parameters.getOrElse("treatEmptyValuesAsNulls", "false").toBoolean

    val charset: String = parameters.getOrElse("charset", TextFile.DEFAULT_CHARSET.name())
    DelimitedRelation(
      () => TextFile.withCharset(sqlContext.sparkContext, path, charset),
      Some(path),
      useHeader,
      delimitedParser,
      nullValue,
      treatEmptyValuesAsNulls,
      schema
    )(sqlContext)
  }

  private def checkPath(parameters: Map[String, String]): String = {
    parameters.getOrElse("path", sys.error("'path' must be specified for CSV data."))
  }

  /*Saving Data in delimited format*/

  override def createRelation(sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String], data: DataFrame): BaseRelation = {
    parameters.getOrElse("path", sys.error("'path' must be specified for CSV data."))
    val path: String = parameters.get("path").get
    val filesystemPath: Path = new Path(path)
    val fs: FileSystem = filesystemPath.getFileSystem(sqlContext.sparkContext.hadoopConfiguration)

    val doSave: Boolean = if (fs.exists(filesystemPath)) {
      mode match {
        case SaveMode.Append =>
          sys.error(s"Append mode is not supported by ${this.getClass.getCanonicalName}")
        case SaveMode.Overwrite =>
          fs.delete(filesystemPath, true)
          true
        case SaveMode.ErrorIfExists =>
          sys.error(s"path $path already exists.")
        case SaveMode.Ignore => false
      }
    } else {
      true
    }

    if (doSave) {
      saveAsCsvFile(data, parameters, path)
    }
    createRelation(sqlContext, parameters, data.schema)
  }

  def saveAsCsvFile(dataFrame: DataFrame, parameters: Map[String, String], path: String) = {

    val delimiter: String = parameters.getOrElse("delimiter", ",")
    val outDateFormats: String = parameters.getOrElse("dateFormats","null")
    val strict: Boolean = parameters.getOrElse("strict", "true").toBoolean
    val quote: String = if (parameters.getOrElse("quote", "\"") == null ) "\"" else parameters.getOrElse("quote", "\"")
    val generateHeader: Boolean = parameters.getOrElse("header", "true").toBoolean
    val schema: StructType = dataFrame.schema

    val header: String = if (generateHeader) {
      dataFrame.columns.mkString(delimiter)
    } else {
      "" // There is no need to generate header in this case
    }

   val strRDD = dataFrame.rdd.mapPartitionsWithIndex {

      case (index, iter) =>
        new Iterator[String] {
          var firstRow: Boolean = generateHeader

          override def hasNext: Boolean = iter.hasNext || firstRow
          override def next: String = {
            if (iter.nonEmpty) {
              val tuple = iter.next()
              if (strict && tuple.length != schema.fields.length){
                throw new RuntimeException("Output row does not have enough length to parse all fields. Output length is "
                  + tuple.length
                  + ". Number of fields in output schema are "
                  + schema.fields.length
                  + "\nRow being parsed: " + tuple)

              }

              val values: Seq[AnyRef] = tuple.toSeq.zipWithIndex.map({
                case (value, i) =>{
                  val castedValue = TypeCast.castingOutputData(value, schema.fields(i).dataType, outDateFormats.split("\t")(i))
                  var string:String = ""
                  if (castedValue != null ){
                    string = castedValue.toString
                    if (string.contains(quote))
                      string = string.replaceAll(quote, quote + quote)
                    if (string.contains(delimiter))
                      string = quote + string + quote
                  }
                  string
                }

              })

              val row: String = values.mkString(delimiter)
              if (firstRow) {
                firstRow = false
                header + System.getProperty("line.separator") + row
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

  }

}
