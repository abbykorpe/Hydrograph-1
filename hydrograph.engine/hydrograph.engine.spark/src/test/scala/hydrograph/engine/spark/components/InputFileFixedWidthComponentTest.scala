package hydrograph.engine.spark.components

import java.util
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.junit.{ Test, Assert, Before }
import org.junit.Assert
import org.apache.spark.SparkException
import org.apache.spark.sql._
import hydrograph.engine.core.component.entity.elements.OutSocket
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.core.component.entity.InputFileFixedWidthEntity

class InputFileFixedWidthComponentTest {

  /**
   * Test case for correct schema
   */
  
  @Test
  def itShouldCheckStrictAndSafeForCorrectInputFormatAndCorrectLength(): Unit = {

    //given

    val inputPathCase: String = "testData/inputFiles/fixed.txt"

    val sf1 = new SchemaField("ID", "java.lang.Integer");
    val sf2 = new SchemaField("Name", "java.lang.String");
    sf1.setFieldLength(3)
    sf2.setFieldLength(3)
    val fieldList: util.List[SchemaField] = new util.ArrayList[SchemaField]();
    fieldList.add(sf1)
    fieldList.add(sf2)

    val cp: BaseComponentParams = new BaseComponentParams

    val inputFileFixedWidthEntity: InputFileFixedWidthEntity = new InputFileFixedWidthEntity
    inputFileFixedWidthEntity.setComponentId("inpuFileFixedWidth");
    inputFileFixedWidthEntity.setPath(inputPathCase)
    inputFileFixedWidthEntity.setStrict(true)
    inputFileFixedWidthEntity.setSafe(false)
    inputFileFixedWidthEntity.setCharset("UTF-8")
    inputFileFixedWidthEntity.setFieldsList(fieldList)

    val outSockets = new util.ArrayList[OutSocket]();
    outSockets.add(new OutSocket("outSocket"));

    inputFileFixedWidthEntity.setOutSocketList(outSockets)

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    cp.setSparkSession(sparkSession)

    //when

    val df: Map[String, DataFrame] = new InputFileFixedWidthComponent(inputFileFixedWidthEntity, cp).createComponent()

    //Then

    val expectedSize: Int = 2
    val expectedResult: String = "[123,abc]"
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
    Assert.assertEquals(expectedResult, df.get("outSocket").get.first().toString())

  }

  /**
   * Test case for incorrect data type
   */
  @Test(expected = classOf[SparkException])
  def itShouldThrowExceptionForIncorrectDataTypeWhenSafeFalse(): Unit = {

    //given
    val inputPathCase: String = "testData/inputFiles/fixed.txt"

    val sf1 = new SchemaField("ID", "java.lang.Integer");
    val sf2 = new SchemaField("Name", "java.lang.Integer");
    sf1.setFieldLength(3)
    sf2.setFieldLength(3)

    val fieldList: util.List[SchemaField] = new util.ArrayList[SchemaField]();
    fieldList.add(sf1)
    fieldList.add(sf2)

    val cp: BaseComponentParams = new BaseComponentParams
    val inputFileFixedWidthEntity: InputFileFixedWidthEntity = new InputFileFixedWidthEntity
    inputFileFixedWidthEntity.setComponentId("inpuFileFixedWidth");
    inputFileFixedWidthEntity.setPath(inputPathCase)
    inputFileFixedWidthEntity.setStrict(true)
    inputFileFixedWidthEntity.setSafe(false)
    inputFileFixedWidthEntity.setCharset("UTF-8")
    inputFileFixedWidthEntity.setFieldsList(fieldList)

    val outSockets = new util.ArrayList[OutSocket]();
    outSockets.add(new OutSocket("outSocket"));

    inputFileFixedWidthEntity.setOutSocketList(outSockets)

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    cp.setSparkSession(sparkSession)

    //when

    val df: Map[String, DataFrame] = new InputFileFixedWidthComponent(inputFileFixedWidthEntity, cp).createComponent()

    //Then

    val expectedSize: Int = 2
    val expectedResult: String = "[123,null]"
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
    Assert.assertEquals(expectedResult, df.get("outSocket").get.first().toString())

  }

  /**
   * Test case for malformed row
   */
  @Test(expected = classOf[SparkException])
  def itShouldThrowExceptionForMalformedRowWhenStrictTrue(): Unit = {

    //given
    val inputPathCase: String = "testData/inputFiles/fixed.txt"

    val sf1 = new SchemaField("ID", "java.lang.Integer");
    val sf2 = new SchemaField("Name", "java.lang.String");
    sf1.setFieldLength(3)
    sf2.setFieldLength(4)

    val fieldList: util.List[SchemaField] = new util.ArrayList[SchemaField]();
    fieldList.add(sf1)
    fieldList.add(sf2)

    val cp: BaseComponentParams = new BaseComponentParams
    val inputFileFixedWidthEntity: InputFileFixedWidthEntity = new InputFileFixedWidthEntity
    inputFileFixedWidthEntity.setComponentId("inpuFileFixedWidth");

    inputFileFixedWidthEntity.setPath(inputPathCase)
    inputFileFixedWidthEntity.setStrict(true)
    inputFileFixedWidthEntity.setSafe(false)
    inputFileFixedWidthEntity.setCharset("UTF-8")
    inputFileFixedWidthEntity.setFieldsList(fieldList)
    val outSockets = new util.ArrayList[OutSocket]();
    outSockets.add(new OutSocket("outSocket"));

    inputFileFixedWidthEntity.setOutSocketList(outSockets)

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    cp.setSparkSession(sparkSession)

    //when

    val df: Map[String, DataFrame] = new InputFileFixedWidthComponent(inputFileFixedWidthEntity, cp).createComponent()

    //Then

    val expectedSize: Int = 2
    val expectedResult: String = "[123,abc]"
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
    Assert.assertEquals(expectedResult, df.get("outSocket").get.first().toString())

  }

  /**
   * Test case for malformed row
   */

  @Test
  def itShouldNotThrowExceptionForMalformedRowWhenStrictFalse(): Unit = {

    //given
    val inputPathCase: String = "testData/inputFiles/fixed.txt"

    val sf1 = new SchemaField("ID", "java.lang.Integer");
    val sf2 = new SchemaField("Name", "java.lang.String");
    sf1.setFieldLength(3)
    sf2.setFieldLength(2)
    val fieldList: util.List[SchemaField] = new util.ArrayList[SchemaField]();
    fieldList.add(sf1)
    fieldList.add(sf2)

    val cp: BaseComponentParams = new BaseComponentParams
    val inputFileFixedWidthEntity: InputFileFixedWidthEntity = new InputFileFixedWidthEntity
    inputFileFixedWidthEntity.setComponentId("inpuFileFixedWidth");

    inputFileFixedWidthEntity.setPath(inputPathCase)
    inputFileFixedWidthEntity.setStrict(false)
    inputFileFixedWidthEntity.setSafe(false)
    inputFileFixedWidthEntity.setCharset("UTF-8")
    inputFileFixedWidthEntity.setFieldsList(fieldList)
    val outSockets = new util.ArrayList[OutSocket]();
    outSockets.add(new OutSocket("outSocket"));

    inputFileFixedWidthEntity.setOutSocketList(outSockets)

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    cp.setSparkSession(sparkSession)

    //when

    val df: Map[String, DataFrame] = new InputFileFixedWidthComponent(inputFileFixedWidthEntity, cp).createComponent()

    //Then

    val expectedSize: Int = 2
    //val expectedResult: String = "[123,ab]"
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
   // Assert.assertEquals(expectedResult, df.get("outSocket").get.first().toString())

  }
}