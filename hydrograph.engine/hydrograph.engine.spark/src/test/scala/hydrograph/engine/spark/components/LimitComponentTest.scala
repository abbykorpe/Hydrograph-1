package hydrograph.engine.spark.components

import scala.collection.JavaConverters.seqAsJavaListConverter

import org.junit.Assert
import org.junit.Test

import hydrograph.engine.core.component.entity.LimitEntity
import hydrograph.engine.core.component.entity.elements.OutSocket
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.testing.wrapper.Bucket
import hydrograph.engine.testing.wrapper.DataBuilder
import hydrograph.engine.testing.wrapper.Fields

class LimitComponentTest{
  
  @Test
  def TestSimpleLimitComponentWorking(): Unit ={

    val df = new DataBuilder(Fields(List("col1", "col2", "col3", "col4")).applyTypes(List(classOf[String],
      classOf[String], classOf[String], classOf[String]))).addData(List("1", "C2R1", "C3Rx", "C4R1"))
      .addData(List("2", "C2R2", "C3Rx", "C4R2"))
      .addData(List("3", "C2R3", "C3Rx", "C4R3"))
      .addData(List("4", "C2R4", "C4Rx", "C4R4"))
      .build()

    val limitEntity: LimitEntity = new LimitEntity
    limitEntity.setComponentId("limit")
    limitEntity.setMaxRecord(3)

    val outSocket1:OutSocket  = new OutSocket("out0")
    val outSocketList: List[OutSocket] = List(outSocket1)

    limitEntity.setOutSocketList(outSocketList.asJava)
		
    val baseComponentParams = new BaseComponentParams
    baseComponentParams.addinputDataFrame(df)

    val limitDF = new SparkLimitComponent(limitEntity, baseComponentParams).createComponent()

    val rows = Bucket(Fields(List("col1", "col2", "col3", "col4")), limitDF.get("out0").get).result()

    Assert.assertEquals(3,rows.size)
     }
}
