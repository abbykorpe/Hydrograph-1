package hydrograph.engine.spark.performance.components

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

object InputOutputDelimeted {
  def main(args: Array[String]): Unit = {

    val sparkSession = SparkSession.builder().master("local")
      .getOrCreate()

    val readDF = sparkSession.read
      .option("delimiter", ",")
      .option("header", false)
      .option("charset", "ISO-8859-1")
      .schema(StructType(Array(StructField("id", LongType, false), StructField("name", StringType, false), StructField("number", LongType, false), StructField("city", StringType, false))))
      .csv("C:/PushpenderG/DART/POC/hydrograph_spark/testfiles/input/aggregateInputFile.txt")

    //val odf = readDF.drop("number")

    readDF.write
      .option("delimiter", ",")
      .option("header", false)
      .option("charset", "")
      .mode(SaveMode.Overwrite)
      .csv("testData/output/ioperformance")

  }

}