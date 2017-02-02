package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.elements.{OutSocket, SchemaField}
import hydrograph.engine.core.component.entity.{GenerateRecordEntity, UniqueSequenceEntity}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.testing.wrapper.{Bucket, DataBuilder, Fields}
import org.apache.spark.sql.SparkSession
import org.junit.{Assert, Test}

/**
  * Created by sandeepv on 1/6/2017.
  */
class UniqueSequenceComponentTest {

  @Test
  def itShouldCheckComponentExecution(): Unit = {

    val uniqueSequenceEntity: UniqueSequenceEntity = new UniqueSequenceEntity
    uniqueSequenceEntity.setComponentId("1")
    uniqueSequenceEntity.setComponentName("Unique Sequence")

    val df1 = new DataBuilder(Fields(List("col1", "col2", "col3", "col4")).applyTypes(List(classOf[String],
      classOf[String], classOf[String], classOf[String])))
      .addData(List("1", "C2R1", "C3Rx1", "C4R1"))
      .addData(List("2", "C2R2", "C3Rx2", "C4R2"))
      .addData(List("3", "C2R3", "C3Rx3", "C4R3"))
      .addData(List("4", "C2R4", "C3Rx4", "C4R4"))
      .build()

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()
    val baseComponentParams = new BaseComponentParams
    baseComponentParams .setSparkSession(sparkSession)

    val outSocketList: util.ArrayList[OutSocket] = new util.ArrayList[OutSocket]();
    outSocketList.add(new OutSocket("out0", "out"));
    uniqueSequenceEntity.setOutSocketList(outSocketList);

    val schema = Array(
      new SchemaField("col1", "java.lang.String"),
      new SchemaField("col2", "java.lang.String"),
      new SchemaField("col3", "java.lang.String"),
      new SchemaField("col4", "java.lang.String"))

    baseComponentParams.addinputDataFrame(df1)
    baseComponentParams.addSchemaFields(schema)

    val uniqueSequenceDF = new UniqueSequenceComponent(uniqueSequenceEntity, baseComponentParams ).createComponent()
    val rows = Bucket(Fields(List("col1", "col2", "col3", "col4")), uniqueSequenceDF.get("out0").get).result()
    val checkUniqueValues = rows.distinct
    Assert.assertEquals(checkUniqueValues.size, rows.size)
  }
}