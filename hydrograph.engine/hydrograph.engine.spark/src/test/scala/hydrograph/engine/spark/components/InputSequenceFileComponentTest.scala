package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.InputFileSequenceFormatEntity
import hydrograph.engine.core.component.entity.elements.{OutSocket, SchemaField}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql._
import org.junit.{Assert, Test}

/**
  * Created by santlalg on 1/30/2017.
  */
class InputSequenceFileComponentTest {

  @Test
  def itShouldReadSequeceFile() = {

    //given
    val inputFileSequenceFormatEntity:InputFileSequenceFormatEntity = new InputFileSequenceFormatEntity
    val cp: BaseComponentParams = new BaseComponentParams

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    cp.setSparkSession(sparkSession)

    val sf1 = new SchemaField("rollno", "java.lang.Short");
    val sf2 = new SchemaField("havePassport", "java.lang.Boolean");
    val sf3 = new SchemaField("salary", "java.lang.Long");
    val sf4 = new SchemaField("emi", "java.lang.Float");
    val sf5 = new SchemaField("rating", "java.lang.Integer");
    val sf6 = new SchemaField("name", "java.lang.String");
    val sf7 = new SchemaField("deposite", "java.lang.Double");
    val sf8 = new SchemaField("DOJ", "java.util.Date");
    sf8.setFieldFormat("yyyy-MM-dd")
    val sf9 = new SchemaField("DOR", "java.util.Date");
    sf9.setFieldFormat("yyyy/MM/dd HH:mm:ss.SSS")
    val sf10 = new SchemaField("pf", "java.math.BigDecimal");
    sf10.setFieldPrecision(10)
    sf10.setFieldScale(2)

    val fieldList: util.List[SchemaField] = new util.ArrayList[SchemaField]();
    fieldList.add(sf1)
    fieldList.add(sf2)
    fieldList.add(sf3)
    fieldList.add(sf4)
    fieldList.add(sf5)
    fieldList.add(sf6)
    fieldList.add(sf7)
    fieldList.add(sf8)
    fieldList.add(sf9)
    fieldList.add(sf10)

    inputFileSequenceFormatEntity.setFieldsList(fieldList)
    val outSockets = new util.ArrayList[OutSocket]();
    outSockets.add(new OutSocket("outSocket"));
    inputFileSequenceFormatEntity.setComponentId("inpuSequenceFile");
    inputFileSequenceFormatEntity.setOutSocketList(outSockets)
    inputFileSequenceFormatEntity.setPath("testData/inputFiles/sequenceInputFile")

    //when
    val df: Map[String, DataFrame] = new InputSequenceFileComponent(inputFileSequenceFormatEntity, cp).createComponent()

    //then
    val expectedSize: Int = 10
    val expectedResult: String = "[14,true,251414,25.25,25,ddeeff,25.25,2014-02-08,2015-02-05 14:12:54.0,56435.36]"
    Assert.assertEquals(expectedSize, df.get("outSocket").get.first().size)
    Assert.assertEquals(expectedResult, df.get("outSocket").get.first().toString())
  }
}
