package hydrograph.engine.spark.fixedwidth.datasource

import hydrograph.engine.spark.datasource.utils.TypeCast
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.sources.{BaseRelation, CreatableRelationProvider, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.slf4j.{Logger, LoggerFactory}


class DefaultSource extends RelationProvider with SchemaRelationProvider with CreatableRelationProvider {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[DefaultSource])
  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation =
    createRelation(sqlContext, parameters, null)

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String], schema: StructType): BaseRelation = {
    parameters.getOrElse("path", throw new RuntimeException("path option must be specified for Input File Fixed Width Component"))
    parameters.getOrElse("length", throw new RuntimeException("length option must be specified for Input File Fixed Width Component"))
    val inDateFormats: String = parameters.getOrElse("dateFormats", "null")
    new FixedWidthRelation(parameters.get("path").get, parameters.get("charset").get,
      parameters.get("length").get, parameters.getOrElse("strict","true").toBoolean,
      parameters.getOrElse("safe","false").toBoolean, inDateFormats.split("\t"), schema)(sqlContext)
  }

  private def toIntLength(fieldsLen: String): Array[Int] = {
    val len = fieldsLen.split(",")
    len.map(x => x.toInt)
  }

  def saveAsFW(dataFrame: DataFrame, path: String, parameters: Map[String, String]) = {
    val inDateFormats: Array[String] = parameters.getOrElse("dateFormats", "null").split("\t")
    val strict: Boolean = parameters.getOrElse("strict","true").toBoolean
    val safe: Boolean = parameters.getOrElse("safe","false").toBoolean
    val schema = dataFrame.schema
    val fieldlen: Array[Int] = toIntLength( parameters.get("length").get)

    val valueRDD = dataFrame.rdd.mapPartitions(itr => {
      itr.map(row => {
        if (strict && (row.length != fieldlen.length)){
          LOG.error("Input row does not have enough length to parse all fields. Input length is "
            + row.length
            + ". Sum of length of all fields is "
            + fieldlen.sum
            + "\nRow being parsed: " + row)
          throw new RuntimeException("Input row does not have enough length to parse all fields. Input length is "
            + row.length
            + ". Sum of length of all fields is "
            + fieldlen.sum
            + "\nRow being parsed: " + row)
        }

        def getFieldValues(value: Any, acc: Int): Any = {
          if (acc == row.size)
            return value


          def getFiller(filler:String, length: Int): String ={
            var res:String = ""
            for (i <- 0 until (length * -1) ) {
              res += filler
            }
            res
          }

          val data = row.get(acc)
          if (data == null ) {
            if (!safe && !schema(acc).nullable) {
              LOG.error("Field " + schema(acc).name + " has value null. Field length specified in the schema is "
                + fieldlen(acc) + ". ")
              throw new RuntimeException("Field " + schema(acc).name + " has value null. Field length specified in the schema is "
                + fieldlen(acc) + ". ")
            } else {
              val res: String = getFiller(" ",fieldlen(acc))
              getFieldValues(value + res + data, acc + 1)
            }
          } else {
            val lengthDiff = data.toString.length - fieldlen(acc)
            val result = lengthDiff match {
              case _ if lengthDiff == 0 => TypeCast.castingOutputData(data, schema.fields(acc).dataType, inDateFormats(acc))
              case _ if lengthDiff > 0 =>
                TypeCast.castingOutputData(data.toString.substring(0, fieldlen(acc)), schema.fields(acc).dataType, inDateFormats(acc))
              case _ if lengthDiff < 0 =>
                schema(acc).dataType match {
                  case BooleanType | ByteType | DateType
                       | StringType | TimestampType | CalendarIntervalType => data + getFiller(" ",lengthDiff)
                  case _ =>  getFiller("0",lengthDiff) + data
                }
            }
            getFieldValues(value + result.toString, acc + 1)
          }
        }
        getFieldValues("", 0)
      })
    })
    valueRDD.saveAsTextFile(path)
    LOG.info("Fixed Width Output File is successfully written at path : " + path)
  }

  override def createRelation(sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String], data: DataFrame): BaseRelation = {

    parameters.getOrElse("path", throw new RuntimeException("path option must be specified for Output File Fixed Width Component"))
    parameters.getOrElse("length", throw new RuntimeException("length option must be specified for Output File Fixed Width Component"))
    val path = parameters.get("path").get
    val fsPath = new Path(path)
    val fs = fsPath.getFileSystem(sqlContext.sparkContext.hadoopConfiguration)

    val isSave = if (fs.exists(fsPath)) {
      mode match {
        case SaveMode.Append => throw new RuntimeException("Output file append operation is not supported")
        case SaveMode.Overwrite =>
          if (fs.delete(fsPath, true)) true else  throw new RuntimeException("Output directory path '"+ path +"' cannot be deleted")
        case SaveMode.ErrorIfExists => throw new RuntimeException("Output path already exists")
        case SaveMode.Ignore => false
      }
    } else
      true

    if (isSave)
      saveAsFW(data, path, parameters)

    createRelation(sqlContext, parameters, data.schema)
  }
}
