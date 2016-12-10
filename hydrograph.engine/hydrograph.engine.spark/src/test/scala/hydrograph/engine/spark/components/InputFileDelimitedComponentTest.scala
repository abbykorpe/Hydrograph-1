package hydrograph.engine.spark.components

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.junit.Assert
import org.junit.Test

class InputFileDelimitedComponentTest {
  //given
  val spark = SparkSession.builder().appName("Spark Test Class").master("local").config("spark.sql.warehouse.dir", "file:///c:/tmp/spark-warehouse").getOrCreate()
  val schema: StructType = StructType(List(
    StructField("ID", ShortType, nullable = true),
    StructField("Name", StringType, nullable = true),
    StructField("Role", StringType, nullable = true),
    StructField("Address", StringType, nullable = true),
    StructField("DOJ", DateType, nullable = true),
    StructField("DOR", TimestampType, nullable = true),
    StructField("Sal", DecimalType(13, 3), nullable = true),
    StructField("Rating", IntegerType, nullable = true)))
    
  val inputPathCase0: String = "testData/inputFiles/employees.txt"
  val inputPathCase1: String = "testData/inputFiles/employees1.txt"
  val inputPathCase2: String = "testData/inputFiles/employees2.txt"
  val inputPathCase3: String = "testData/inputFiles/employees3.txt"
  val inputPathCase4: String = "testData/inputFiles/employees4.txt"
  val dateFormats: String = "null" + "\t" + "null" + "\t" + "null" + "\t" + "null" + "\t" + "yyyy-MM-dd" + "\t" + "yyyy/MM/dd HH:mm:ss.SSS" + "\t" + "null" + "\t" + "null"

  
  /**
 * Test case for correct schema
 */
  @Test
  def itShouldCheckStrictAndSafeForCorrectInputFormatAndCorrectLength(): Unit = {

    //when

    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.delimited.datasource")
      .option("header", "false")
      .option("dateFormats", dateFormats)
      .option("quote", "\"")
      .option("charset", "UTF-8")
      .option("delimiter", "ÇÇ")
      .option("strict", "true")
      .option("safe", "false")
      .load(inputPathCase0)

    //Then
    Assert.assertEquals(8, df.first().size)
    Assert.assertEquals("[3,Hemant,Programmer,BBSRÇÇOD,2015-06-25,2016-06-25 02:02:02.325,50.300,3]", df.first().toString())

  }
  
  @Test(expected = classOf[RuntimeException])
  def itShouldThrowExceptionForIncorrectDataTypeWhenSafeFalse(): Unit = {

    //when
    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.delimited.datasource")
      .option("header", "false")
      .option("dateFormats", dateFormats)
      .option("quote", "\"")
      .option("charset", "UTF-8")
      .option("delimiter", "ÇÇ")
      .option("strict", "true")
      .option("safe", "false")
      .load(inputPathCase1)

    //Then
    Assert.assertEquals(8, df.first().size)
    Assert.assertEquals("[3,Hemant,68.36,BBSRÇÇOD,2015-06-25,2016-06-25 02:02:02.325,50.300,null]", df.first().toString())

  }
 
 
 
 @Test
  def itShouldReadFieldAsNullForIncorrectDataTypeWhenSafeTrue(): Unit = {

    //when

    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.delimited.datasource")
      .option("header", "false")
      .option("dateFormats", dateFormats)
      .option("quote", "\"")
      .option("charset", "UTF-8")
      .option("delimiter", "ÇÇ")
      .option("strict", "true")
      .option("safe", "true")
      .load(inputPathCase2)

    //Then
    Assert.assertEquals(8, df.first().size)
    Assert.assertEquals("[3,Hemant,68.36,BBSRÇÇOD,2015-06-25,2016-06-25 02:02:02.325,50.300,null]", df.first().toString())

  }
 
 
 
 /*Test Cases For Blank Fields Input Starts Here*/
 
  @Test
  def itShouldReadNullWhenFieldIsNullAndSafeTrue(): Unit = {

    //when

    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.delimited.datasource")
      .option("header", "false")
      .option("dateFormats", dateFormats)
      .option("quote", "\"")
      .option("charset", "UTF-8")
      .option("delimiter", "ÇÇ")
      .option("strict", "true")
      .option("safe", "true")
      .load(inputPathCase3)

    //Then
    Assert.assertEquals(8, df.first().size)
    Assert.assertEquals("[3,Hemant,null,BBSRÇÇOD,2015-06-25,2016-06-25 02:02:02.325,50.300,null]", df.first().toString())

  }
  
  @Test(expected = classOf[RuntimeException])
  def itShouldThrowExceptionWhenFieldIsNullAndSafeFalse(): Unit = {
    //when
    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.delimited.datasource")
      .option("header", "false")
      .option("dateFormats", dateFormats)
      .option("quote", "\"")
      .option("charset", "UTF-8")
      .option("delimiter", "ÇÇ")
      .option("strict", "true")
      .option("safe", "false")
      .load(inputPathCase3)

    //Then
    Assert.assertEquals(8, df.first().size)
    Assert.assertEquals("[3,Hemant,null,BBSRÇÇOD,2015-06-25,2016-06-25 02:02:02.325,50.300,null]", df.first().toString())

  }
  
  
  
  /*Test Cases For Missing Fields Input Starts Here*/
  
    @Test(expected = classOf[RuntimeException])
  def itShouldThrowExceptionWhenFieldMissingAndStrictTrue(): Unit = {
    //when
    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.delimited.datasource")
      .option("header", "false")
      .option("dateFormats", dateFormats)
      .option("quote", "\"")
      .option("charset", "UTF-8")
      .option("delimiter", "ÇÇ")
      .option("strict", "true")
      .option("safe", "true")
      .load(inputPathCase4)

    //Then
    Assert.assertEquals(8, df.first().size)
    Assert.assertEquals("[3,Hemant,null,BBSRÇÇOD,2015-06-25,2016-06-25 02:02:02.325,null,null]", df.first().toString())

  }
    
    
  @Test
  def itShouldReadNullForMissingFieldWhenStrictFalseAndSafeTrue(): Unit = {
    //when
    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.delimited.datasource")
      .option("header", "false")
      .option("dateFormats", dateFormats)
      .option("quote", "\"")
      .option("charset", "UTF-8")
      .option("delimiter", "ÇÇ")
      .option("strict", "false")
      .option("safe", "true")
      .load(inputPathCase4)

    //Then
    Assert.assertEquals(8, df.first().size)
    Assert.assertEquals("[3,Hemant,68.36,BBSRÇÇOD,2015-06-25,2016-06-25 02:02:02.325,null,null]", df.first().toString())

  }

   @Test(expected = classOf[RuntimeException])
  def itShouldThrowExceptionForMissingFieldWhenStrictFalseAndSafeFalse(): Unit = {
    //when
    val df = spark.read.schema(schema)
      .format("hydrograph.engine.spark.delimited.datasource")
      .option("header", "false")
      .option("dateFormats", dateFormats)
      .option("quote", "\"")
      .option("charset", "UTF-8")
      .option("delimiter", "ÇÇ")
      .option("strict", "false")
      .option("safe", "false")
      .load(inputPathCase4)

    //Then
    Assert.assertEquals(8, df.first().size)
  Assert.assertEquals("[3,Hemant,null,BBSRÇÇOD,2015-06-25,2016-06-25 02:02:02.325,null,null]", df.first().toString())
  }

 
}