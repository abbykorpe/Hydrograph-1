package hydrograph.engine.spark.components

import org.apache.spark.SparkException
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.junit.{Assert, Test}

class InputFileFixedWidthComponentTest {
  //given
  val spark = SparkSession.builder()
    .appName("Spark Test Class")
    .master("local")
    .config("spark.sql.warehouse.dir", "file:///c:/tmp/spark-warehouse")
    .getOrCreate()

  val schema: StructType = StructType(List(
    StructField("ID", IntegerType, nullable = true),
    StructField("Name", StringType, nullable = true)))

  val incorrectSchema: StructType = StructType(List(
    StructField("ID", IntegerType, nullable = true),
    StructField("Name", IntegerType, nullable = true)))

  val inputPathCase: String = "testData/inputFiles/fixed.txt"

  val dateFormats: String = "null" + "\t" + "null"


  /**
    * Test case for correct schema
    */
  @Test
  def itShouldCheckStrictAndSafeForCorrectInputFormatAndCorrectLength(): Unit = {

    //when

    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.datasource.fixedwidth")
      .option("dateFormats", dateFormats)
      .option("charset", "UTF-8")
      .option("length", Array(3,3).mkString(","))
      .option("strict", "true")
      .option("safe", "false")
      .load(inputPathCase)

    //Then
    Assert.assertEquals(2, df.first().size)
    Assert.assertEquals("[123,abc]", df.first().toString())

  }

  /**
    * Test case for incorrect data type
    */
  @Test(expected = classOf[SparkException])
  def itShouldThrowExceptionForIncorrectDataTypeWhenSafeFalse(): Unit = {

    //when
    val df = spark.read.schema(incorrectSchema)
      .format("hydrograph.engine.spark.datasource.fixedwidth")
      .option("dateFormats", dateFormats)
      .option("charset", "UTF-8")
      .option("length", Array(3,3).mkString(","))
      .option("strict", "true")
      .option("safe", "false")
      .load(inputPathCase)

    //Then
    Assert.assertEquals(2, df.first().size)
    //    Assert.assertEquals("[123,null]", df.first().toString())

  }

  /**
    * Test case for malformed row
    */
  @Test(expected = classOf[SparkException])
  def itShouldTrowExceptionForMalformedRowWhenStrictTrue(): Unit = {

    //when

    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.datasource.fixedwidth")
      .option("dateFormats", dateFormats)
      .option("charset", "UTF-8")
      .option("length", Array(3,4).mkString(","))
      .option("strict", "true")
      .option("safe", "false")
      .load(inputPathCase)

    //Then
    Assert.assertEquals(2, df.first().size)
    //    Assert.assertEquals("[123,null]", df.first().toString())

  }

  /**
    * Test case for malformed row
    */
  @Test
  def itShouldNotTrowExceptionForMalformedRowWhenStrictFalse(): Unit = {

    //when

    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.datasource.fixedwidth")
      .option("dateFormats", dateFormats)
      .option("charset", "UTF-8")
      .option("length", Array(3, 2).mkString(","))
      .option("strict", "false")
      .option("safe", "false")
      .load(inputPathCase)

    //Then
    Assert.assertEquals(2, df.first().size)
    //    Assert.assertEquals("[123,null]", df.first().toString())

  }
}