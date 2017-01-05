package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.elements.{OutSocket, SchemaField}
import hydrograph.engine.core.component.entity.InputFileParquetEntity
import hydrograph.engine.core.props.PropertiesLoader
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.testing.wrapper.{Bucket, DataBuilder, Fields}
import org.apache.spark.sql.SparkSession
import org.junit.Assert
import org.junit.Test

import scala.collection.JavaConverters._

class InputFileParquetComponentTest {

  @Test
  def TestInputFileParquetComponentWorking(): Unit = {
    val runTimeServiceProp = PropertiesLoader.getInstance.getRuntimeServiceProperties
    val spark = SparkSession.builder()
      .appName("Test Class")
      .master(runTimeServiceProp.getProperty("hydrograph.spark.master"))
      .config("spark.sql.warehouse.dir", runTimeServiceProp.getProperty("hydrograph.tmp.warehouse"))
      .getOrCreate()


    val path: String = "testData/inputFiles/input.parquet"
    val inputFileParquetEntity = new InputFileParquetEntity
    inputFileParquetEntity.setPath(path)

    inputFileParquetEntity.setComponentId("Inout File parquet")
    val outSocket1: OutSocket = new OutSocket("out0")
    val outSocketList: List[OutSocket] = List(outSocket1)

    inputFileParquetEntity.setOutSocketList(outSocketList.asJava)

    val sf0: SchemaField = new SchemaField("id", "java.lang.Integer")
    val sf1: SchemaField = new SchemaField("name", "java.lang.String")

    val list: List[SchemaField] = List(sf0, sf1)
    val javaList = list.asJava
    inputFileParquetEntity.setFieldsList(javaList)

    val baseComponentParams = new BaseComponentParams
    baseComponentParams.setSparkSession(spark)

    val pdf = new InputFileParquetComponent(inputFileParquetEntity, baseComponentParams).createComponent()

    val rows = Bucket(Fields(List("id", "name")), pdf.get("out0").get).result()

    Assert.assertEquals(1, rows.size)
  }
}
