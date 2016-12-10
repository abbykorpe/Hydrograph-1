package hydrograph.engine.spark.datasource.utils

import java.nio.charset.Charset
import org.apache.hadoop.io.{ LongWritable, Text }
import org.apache.hadoop.mapred.TextInputFormat
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * Utility class to read text file 
 *
 */
object TextFile {

  val DEFAULT_CHARSET = Charset.forName("UTF-8")

  /**
   * Reads a TextFile with specified Charset
 * @param context
 * @param location
 * @param charset
 * @return RDD[String]
 */
def withCharset(context: SparkContext, location: String, charset: String): RDD[String] = {
    if (Charset.forName(charset) == DEFAULT_CHARSET) {
      context.textFile(location)
    } else {
      // can't pass a Charset object here cause its not serializable
      context.hadoopFile[LongWritable, Text, TextInputFormat](location).mapPartitions {
        iter =>
          {

            iter.map(pair => new String(pair._2.getBytes, 0, pair._2.getLength, charset))

          }
      }
    }
  }
}
