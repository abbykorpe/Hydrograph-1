package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.elements.{OutSocket, SchemaField}
import hydrograph.engine.core.component.entity.InputFileParquetEntity
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.testing.wrapper.{Bucket, DataBuilder, Fields}
import org.apache.spark.sql.SparkSession
import org.junit.Assert
import org.junit.Test

import scala.collection.JavaConverters._

class InputFileParquetComponentTest {

  @Test
  def TestInputFileParquetComponentWorking(): Unit = {
    val spark = SparkSession.builder().appName("Spark Test Class").master("local").config("spark.sql.warehouse.dir", "file:///c:/tmp/spark-warehouse").getOrCreate()

    val df = new DataBuilder(Fields(List("col1", "col2", "col3", "col4")).applyTypes(List(classOf[String],
      classOf[String], classOf[String], classOf[String]))).addData(List("1", "C2R1", "C3Rx", "C4R1"))
      .addData(List("2", "C2R2", "C3Rx", "C4R2"))
      .addData(List("3", "C2R3", "C3Rx", "C4R3"))
      .build()

    val inputFileParquetEntity = new InputFileParquetEntity
    inputFileParquetEntity.setPath("C:\\Users\\kalyanr\\Desktop\\par\\output\\part-r-00000-dc8b58df-fd48-4b55-9ad2-583a70aaa148.snappy.parquet")

    inputFileParquetEntity.setComponentId("Inout File parquet")
    val outSocket1: OutSocket = new OutSocket("out0")
    val outSocketList: List[OutSocket] = List(outSocket1)

    inputFileParquetEntity.setOutSocketList(outSocketList.asJava)

    val sf0: SchemaField = new SchemaField("id","java.lang.Integer")
    val sf1: SchemaField = new SchemaField("name","java.lang.String")

    val list: List[SchemaField] = List(sf0, sf1)
    val javaList = list.asJava
    inputFileParquetEntity.setFieldsList(javaList)

    val baseComponentParams = new BaseComponentParams
    baseComponentParams.setSparkSession(spark)
    baseComponentParams.addinputDataFrame(df)

    val pdf = new InputFileParquetComponent(inputFileParquetEntity, baseComponentParams).createComponent()

    val rows = Bucket(Fields(List("id", "name")), pdf.get("out0").get).result()

    Assert.assertEquals(1, rows.size)
  }
}
