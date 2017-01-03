package hydrograph.engine.spark.performance.components

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.LongType
import org.apache.spark.sql.SaveMode

object SimpleTransform {
  def main(args: Array[String]): Unit = {

    val sparkSession = SparkSession.builder().master("local")
      .getOrCreate()

    val readDF = sparkSession.read
      .option("delimiter", ",")
      .option("header", false)
      .option("charset", "ISO-8859-1")
      .schema(StructType(Array(StructField("id", LongType, false), StructField("name", StringType, false), StructField("number", LongType, false), StructField("city", StringType, false))))
      .csv("C:/PushpenderG/DART/POC/hydrograph_spark/testfiles/input/aggregateInputFile.txt")

    readDF.createOrReplaceTempView("tmp")
    val odf = sparkSession.sql("select id, city, number, length(name) name_length from tmp")
    //val odf = readDF.withColumnRenamed("name", "new_name").withColumnRenamed("city", "new_city")//.drop("number")
    //odf.explain(true)

    odf.write
      .option("delimiter", ",")
      .option("header", false)
      .option("charset", "")
      .mode(SaveMode.Overwrite)
      .csv("testData/output/ioperformance")

  }

}