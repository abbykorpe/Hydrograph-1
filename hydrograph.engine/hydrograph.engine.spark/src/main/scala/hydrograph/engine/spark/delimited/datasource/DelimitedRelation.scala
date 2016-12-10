package hydrograph.engine.spark.delimited.datasource

import scala.util.control.NonFatal

import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.BaseRelation
import org.apache.spark.sql.sources.TableScan
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import org.slf4j.LoggerFactory

case class DelimitedRelation(
                        baseRDD: () => RDD[String],
                        location: Option[String],
                        useHeader: Boolean,
                        delimitedParser: HydrographDelimitedParser,
                        nullValue: String,
                        treatEmptyValuesAsNullsFlag: Boolean,
                        userSchema: StructType
                      )(@transient val sqlContext: SQLContext)
  extends BaseRelation with TableScan {

  private val logger = LoggerFactory.getLogger(DelimitedRelation.getClass)
  override val schema: StructType = userSchema

  private def tokenRdd(header: Array[String]): RDD[List[Any]] = {


    val filterLine = if (useHeader) baseRDD().first() else null

    baseRDD().mapPartitions { iter =>

      val csvIter = if (useHeader) {
        iter.filter(_ != filterLine)
      } else {
        iter
      }

      parseCSV(csvIter, delimitedParser)
    }
  }

  override def buildScan: RDD[Row] = {
    val schemaFields:Array[StructField] = schema.fields
    val rowArray:Array[Any] = new Array[Any](schemaFields.length)
    
    tokenRdd(schemaFields.map(_.name)).flatMap { tokens => {

      Some(Row.fromSeq(tokens))

     }
    }
  }

  private def parseCSV(iter: Iterator[String],delimitedParser: HydrographDelimitedParser): Iterator[List[Any]] = {
    iter.flatMap { line =>
      try {

        val fields = delimitedParser.parseLine(line)
        if (fields.isEmpty) {
          logger.warn(s"Ignoring empty line: $line")
          None
        } else {

          Some(fields.toList)
        }
      } catch {
        case NonFatal(e) =>
          logger.error(s"Exception while parsing line: $line. ", e)
          None
      }
    }
  }


}
