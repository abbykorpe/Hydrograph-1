package hydrograph.engine.spark.delimited.datasource

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.{StructField, StructType}
import org.slf4j.{Logger, LoggerFactory}

import scala.util.control.NonFatal

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

  private val LOG:Logger = LoggerFactory.getLogger(classOf[DelimitedRelation])
  override val schema: StructType = userSchema

  private def tokenRdd(header: Array[String]): RDD[List[Any]] = {


    val filterLine = if (useHeader) baseRDD().first() else null

    baseRDD().mapPartitions { iter =>

      val delimitedIter = if (useHeader) {
        iter.filter(_ != filterLine)
      } else {
        iter
      }

      parseDelimited(delimitedIter, delimitedParser)
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

  private def parseDelimited(iter: Iterator[String],delimitedParser: HydrographDelimitedParser): Iterator[List[Any]] = {
    iter.flatMap { line =>
      try {

        val fields = delimitedParser.parseLine(line)
        if (fields.isEmpty) {
          LOG.warn(s"Ignoring empty line: $line")
          None
        } else {

          Some(fields.toList)
        }
      } catch {
        case NonFatal(e) =>
          LOG.error(s"Exception while parsing line: $line. ", e)
          None
      }
    }
  }


}
