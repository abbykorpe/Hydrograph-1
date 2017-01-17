package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.CloneEntity
import hydrograph.engine.core.component.entity.elements.{OutSocket, SchemaField}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.testing.wrapper.{Bucket, DataBuilder, Fields}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.junit.{Assert, Test}

import scala.collection.JavaConverters._

/**
  * Created by shivarajn on 1/6/2017.
  */
class SparkCloneComponentTest {

  @Test
  def itShouldPopulateCorrectParameters() = {

    val dataFrameMain = new DataBuilder(Fields(List("col1", "col2", "col3")).applyTypes(List(classOf[String],
      classOf[String], classOf[String])))
      .addData(List("C1R1", "C2R1", "C3R1"))
      .addData(List("C1R1", "C2R2", "C3R2"))
      .addData(List("C1R1", "C2R3", "C3R3")).build()

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    val schema = Array(
      new SchemaField("col1", "java.lang.String"),
      new SchemaField("col2", "java.lang.String"),
      new SchemaField("col3", "java.lang.String"))

    val cloneEntity = new CloneEntity()
    cloneEntity.setComponentId("cloneTest")
    cloneEntity.setBatch("0")

    val outSocketList = List(new OutSocket("out1"),new OutSocket("out2"))
    cloneEntity.setOutSocketList(outSocketList.asJava)

    val compParams = new BaseComponentParams
    compParams.addinputDataFrame(dataFrameMain)
    compParams.addSchemaFields(schema)

    val sparkCloneComponent = new SparkCloneComponent(cloneEntity,compParams)

    val dataFrameMap: Map[String, DataFrame] = sparkCloneComponent.createComponent()

    val dataFrame1 = dataFrameMap.get("out1").get
    val dataFrame2 = dataFrameMap.get("out2").get

    // test 1st copy
    Assert.assertEquals(dataFrame1.count, 3)
    Assert.assertEquals(true, dataFrameMain.equals(dataFrame1))

    // test 2nd copy
    Assert.assertEquals(dataFrame2.count, 3)
    Assert.assertEquals(true, dataFrameMain.equals(dataFrame2))

  }

}
