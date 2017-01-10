package hydrograph.engine.spark.datasource.delimited

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.{StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.slf4j.{Logger, LoggerFactory}

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
  LOG.trace("In method tokenRdd for creating tokens of fields from input row")

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
          LOG.warn("Ignoring empty line: $line")
          None
        } else {
          Some(fields.toList)
        }
      } catch {
        case e : Exception =>
          LOG.error("Exception while parsing line: $line. ", e)
          throw e
        }
    }
  }


}
