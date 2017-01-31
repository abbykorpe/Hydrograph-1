package hydrograph.engine.spark.components

import java.sql.Timestamp
import java.util.Date

import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.core.component.entity.OutputFileSequenceFormatEntity
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import hydrograph.engine.testing.wrapper.{DataBuilder, Fields}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}
import org.junit.{Assert, Test}

import scala.collection.JavaConverters._

/**
  * Created by santlalg on 1/30/2017.
  */
class OutputSequenceFileComponentTest {

  @Test
  def itShouldWriteInputDataInSequenceFile() = {
    //given
    val df = new DataBuilder(Fields(List("col1", "col2", "col3", "col4", "col5", "col6", "col7", "col8", "col9", "col10"))
      .applyTypes(List(classOf[String], classOf[Double], classOf[Float], classOf[Short], classOf[Integer], classOf[Long],
        classOf[Boolean], classOf[BigDecimal], classOf[Date], classOf[Timestamp])))
      .addData(List("aaa", 1.25, 0.25, 25, 35, 147258, true, 12.15, "2014-02-05", "2014-02-05 14:25:36"))
      .addData(List("bbb", 5.25, 1.25, 45, 85, 369852, true, 26.35, "2015-02-05", "2016-02-05 14:35:46"))
      .build()


    val outputFileSequenceFormatEntity: OutputFileSequenceFormatEntity = new OutputFileSequenceFormatEntity
    outputFileSequenceFormatEntity.setPath("testData/outputFiles/sequenceOutput")
    val sf0: SchemaField = new SchemaField("col1", "java.lang.String")
    val sf1: SchemaField = new SchemaField("col2", "java.lang.Double")
    val sf2: SchemaField = new SchemaField("col3", "java.lang.Float")
    val sf3: SchemaField = new SchemaField("col4", "java.lang.Short")
    val sf4: SchemaField = new SchemaField("col5", "java.lang.Integer")
    val sf5: SchemaField = new SchemaField("col6", "java.lang.Long")
    val sf6: SchemaField = new SchemaField("col7", "java.lang.Boolean")
    val sf7: SchemaField = new SchemaField("col8", "java.math.BigDecimal");
    sf7.setFieldPrecision(5)
    sf7.setFieldScale(2)
    val sf8: SchemaField = new SchemaField("col9", "java.util.Date");
    sf8.setFieldFormat("yyyy-MM-dd")
    val sf9: SchemaField = new SchemaField("col10", "java.util.Date");
    sf9.setFieldFormat("yyyy-MM-dd HH:mm:ss")

    val list: List[SchemaField] = List(sf0, sf1, sf2, sf3, sf4, sf5, sf6, sf7, sf8, sf9)
    val javaList = list.asJava
    outputFileSequenceFormatEntity.setFieldsList(javaList)
    outputFileSequenceFormatEntity.setComponentName("Output Sequence File")
    outputFileSequenceFormatEntity.setComponentId("OutputSequence")

    val baseComponentParams = new BaseComponentParams
    baseComponentParams.addinputDataFrame(df)

    val comp = new OutputSequenceFileComponent(outputFileSequenceFormatEntity, baseComponentParams)
    comp.execute()

    val spark = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    val schemaField = SchemaCreator(outputFileSequenceFormatEntity).makeSchema()

    // when
    val inputRdd: RDD[Row] = spark.sparkContext.objectFile("testData/outputFiles/sequenceOutput")
    val inputDf = spark.createDataFrame(inputRdd, schemaField)
    val row = inputDf.collect()

    //then
    val expectedFirstRow = "[bbb,5.25,1.25,45,85,369852,true,26.35,2015-02-05,2016-02-05 14:35:46.0]"
    val expectedSecondRow = "[aaa,1.25,0.25,25,35,147258,true,12.15,2014-02-05,2014-02-05 14:25:36.0]"

    Assert.assertEquals(row.length, 2)
    Assert.assertEquals(expectedFirstRow, row(0).toString())
    Assert.assertEquals(expectedSecondRow, row(1).toString())
  }
}
