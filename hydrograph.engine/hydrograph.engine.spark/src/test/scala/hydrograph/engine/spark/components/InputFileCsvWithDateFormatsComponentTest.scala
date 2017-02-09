package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.InputFileDelimitedEntity
import hydrograph.engine.core.component.entity.elements.{OutSocket, SchemaField}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.SparkException
import org.apache.spark.sql._
import org.junit.{Assert, Before, Test}

class InputFileCsvWithDateFormatsComponentTest {

  var inputFileDelimitedEntity: InputFileDelimitedEntity = new InputFileDelimitedEntity
  var cp: BaseComponentParams = new BaseComponentParams

  @Before
  def executedBeforeEachTestCase() {

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    cp.setSparkSession(sparkSession)

    val sf1 = new SchemaField("ID", "java.lang.Short");

    val sf2 = new SchemaField("Name", "java.lang.String");

    val sf3 = new SchemaField("Role", "java.lang.String");

    val sf4 = new SchemaField("Address", "java.lang.String");

    val sf5 = new SchemaField("DOJ", "java.util.Date");
    sf5.setFieldFormat("yyyy-MM-dd")

    val sf6 = new SchemaField("DOR", "java.util.Date");
    sf6.setFieldFormat("yyyy/MM/dd HH:mm:ss.SSS")

    val sf7 = new SchemaField("Sal", "java.math.BigDecimal");
    sf7.setFieldPrecision(13)
    sf7.setFieldScale(3)

    val sf8 = new SchemaField("Rating", "java.lang.Integer");

    val fieldList: util.List[SchemaField] = new util.ArrayList[SchemaField]();
    fieldList.add(sf1)
    fieldList.add(sf2)
    fieldList.add(sf3)
    fieldList.add(sf4)
    fieldList.add(sf5)
    fieldList.add(sf6)
    fieldList.add(sf7)
    fieldList.add(sf8)

    inputFileDelimitedEntity.setFieldsList(fieldList)

    val outSockets = new util.ArrayList[OutSocket]();
    outSockets.add(new OutSocket("outSocket"));

    inputFileDelimitedEntity.setComponentId("inpuFileDelimited");
    inputFileDelimitedEntity.setOutSocketList(outSockets)
    inputFileDelimitedEntity.setCharset("UTF-8")
    inputFileDelimitedEntity.setDelimiter(",")
    inputFileDelimitedEntity.setQuote("\"")
    inputFileDelimitedEntity.setHasHeader(false)
  }

  /**
    * Test case for correct schema
    */

  @Test
  def itShouldCheckForCorrectInputFormatAndCorrectLength(): Unit = {

    //given
    val inputPathCase0: String = "testData/inputFiles/employees.txt"

    inputFileDelimitedEntity.setPath(inputPathCase0)

    //when
    val df: Map[String, DataFrame] = new InputFileCsvWithDateFormatsComponent(inputFileDelimitedEntity, cp).createComponent()

    //then
    val expectedSize: Int = 8
    val expectedResult: String = "[3,Hemant,Programmer,BBSR,OD,2015-06-25,2016-06-25 02:02:02.325,50.300,3]"
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
    Assert.assertEquals(expectedResult, df.get("outSocket").get.first().toString())
  }

  @Test
  def itShouldCheckForHeaderAndReadData(): Unit = {

    //given
    val inputPathCase0: String = "testData/inputFiles/employees5.txt"
    inputFileDelimitedEntity.setHasHeader(true)
    inputFileDelimitedEntity.setPath(inputPathCase0)

    //when
    val df: Map[String, DataFrame] = new InputFileCsvWithDateFormatsComponent(inputFileDelimitedEntity, cp).createComponent()

    //then
    val expectedSize: Int = 8
    val expectedResult: String = "[3,Hemant,Programmer,BBSR,OD,2015-06-25,2016-06-25 02:02:02.325,50.300,3]"
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
    Assert.assertEquals(expectedResult, df.get("outSocket").get.first().toString())
  }

  @Test
  def itShouldCheckForQuoteAndReadData(): Unit = {

    //given
    val inputPathCase0: String = "testData/inputFiles/employees.txt"
    inputFileDelimitedEntity.setQuote("\"")
    inputFileDelimitedEntity.setPath(inputPathCase0)

    //when
    val df: Map[String, DataFrame] = new InputFileCsvWithDateFormatsComponent(inputFileDelimitedEntity, cp).createComponent()

    //then
    val expectedSize: Int = 8
    val expectedResult: String = "[3,Hemant,Programmer,BBSR,OD,2015-06-25,2016-06-25 02:02:02.325,50.300,3]"
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
    Assert.assertEquals(expectedResult, df.get("outSocket").get.first().toString())
  }

  @Test(expected = classOf[SparkException])
  def itShouldThrowExceptionForIncorrectDataType(): Unit = {

    //Given
    val inputPathCase1: String = "testData/inputFiles/employees1.txt"

    inputFileDelimitedEntity.setPath(inputPathCase1)

    //when
    val df: Map[String, DataFrame] = new InputFileCsvWithDateFormatsComponent(inputFileDelimitedEntity, cp).createComponent()

    //Then
    val expectedSize: Int = 8
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
  }

  /*Test Cases For Blank Fields Input Starts Here*/

  @Test
  def itShouldReadNullWhenFieldIsNull(): Unit = {

    //given
    val inputPathCase3: String = "testData/inputFiles/employees3.txt"

    inputFileDelimitedEntity.setPath(inputPathCase3)

    //when
    val df: Map[String, DataFrame] = new InputFileCsvWithDateFormatsComponent(inputFileDelimitedEntity, cp).createComponent()

    //Then
    val expectedSize: Int = 8
    val expectedResult: String = "[3,Hemant,null,BBSR,OD,2015-06-25,2016-06-25 02:02:02.325,50.300,null]"
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
    Assert.assertEquals(expectedResult, df.get("outSocket").get.first().toString())

  }

  /*Test Cases For Missing Fields Input Starts Here*/

  @Test(expected = classOf[SparkException])
  def itShouldThrowExceptionWhenFieldMissing(): Unit = {

    //Given

    val inputPathCase4: String = "testData/inputFiles/employees4.txt"

    inputFileDelimitedEntity.setPath(inputPathCase4)

    //when

    val df: Map[String, DataFrame] = new InputFileCsvWithDateFormatsComponent(inputFileDelimitedEntity, cp).createComponent()

    //Then

    val expectedSize: Int = 8
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
  }


  @Test
  def itShouldReadInCharacterSet(): Unit = {

    //Given
    val inputPathCase4: String = "testData/inputFiles/employeesISO.txt"

    inputFileDelimitedEntity.setCharset("UTF-8");
    inputFileDelimitedEntity.setDelimiter("Ç")  // cedilla in ISO
    inputFileDelimitedEntity.setPath(inputPathCase4)

    //when
    val df: Map[String, DataFrame] = new InputFileCsvWithDateFormatsComponent(inputFileDelimitedEntity, cp).createComponent()

    //Then
    val expectedSize: Int = 8
    val expectedResult : String = "[3,Hemant,Programmer,BBSROD,2015-06-25,2016-06-25 02:02:02.325,50.300,3]"
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
    Assert.assertEquals(expectedResult,df.get("outSocket").get.first().toString)
  }

  @Test(expected = classOf[SparkException])
  def itShouldThrowExceptionWhenCharacterSetOfFileIsDiffernent(): Unit = {

    //Given
    val inputPathCase4: String = "testData/inputFiles/employeesISO.txt"

    inputFileDelimitedEntity.setCharset("ISO-8859-1");
    inputFileDelimitedEntity.setDelimiter("Ç")  // cedilla in ISO
    inputFileDelimitedEntity.setPath(inputPathCase4)

    //when
    val df: Map[String, DataFrame] = new InputFileCsvWithDateFormatsComponent(inputFileDelimitedEntity, cp).createComponent()

    //Then
    val expectedSize: Int = 8
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
  }

}