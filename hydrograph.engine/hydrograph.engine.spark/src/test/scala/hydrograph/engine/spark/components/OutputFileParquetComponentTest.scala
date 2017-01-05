package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.OutputFileParquetEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import org.junit.Test
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.testing.wrapper.DataBuilder
import hydrograph.engine.testing.wrapper.Fields
import scala.collection.JavaConverters._
import org.apache.spark.sql.SparkSession
import hydrograph.engine.core.props.PropertiesLoader
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.junit.Assert

class OutputFileParquetComponentTest {

  @Test
  def TestOutputFileParquetComponentWorking(): Unit = {

    val df = new DataBuilder(Fields(List("col1", "col2", "col3", "col4")).applyTypes(List(classOf[String],
      classOf[String], classOf[String], classOf[String]))).addData(List("1", "C2R1", "C3Rx", "C4R1"))
      .addData(List("2", "C2R2", "C3Rx", "C4R2"))
      .addData(List("3", "C2R3", "C3Rx", "C4R3"))
      .build()

    val outputFileParquetEntity = new OutputFileParquetEntity()
    outputFileParquetEntity.setPath("testData/inputFiles/parquetOutput")
    val sf0: SchemaField = new SchemaField("col1", "java.lang.String")
    val sf1: SchemaField = new SchemaField("col2", "java.lang.String")

    val list: List[SchemaField] = List(sf0, sf1)
    val javaList = list.asJava
    outputFileParquetEntity.setFieldsList(javaList)
    outputFileParquetEntity.setOverWrite(true);

    val baseComponentParams = new BaseComponentParams
    baseComponentParams.addinputDataFrame(df)

    val comp = new OutputFileParquetComponent(outputFileParquetEntity, baseComponentParams)
    comp.execute()

    val runTimeServiceProp = PropertiesLoader.getInstance.getRuntimeServiceProperties
    val spark = SparkSession.builder()
      .appName("Test Class")
      .master(runTimeServiceProp.getProperty("hydrograph.spark.master"))
      .config("spark.sql.warehouse.dir", runTimeServiceProp.getProperty("hydrograph.tmp.warehouse"))
      .getOrCreate()

    val sch = StructType(List(StructField("col1", StringType, true), StructField("col2", StringType, true)))
    val outDF = spark.read.schema(sch).parquet("testData/inputFiles/parquetOutput")

    Assert.assertEquals(outDF.count(), 3)

  }
}