package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.UnionAllEntity
import hydrograph.engine.core.component.entity.elements.OutSocket
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.testing.wrapper.{Bucket, DataBuilder}
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.junit.{Assert, Test}

/**
  * Created by gurdits on 12/7/2016.
  */
class SparkUnionAllComponentTest {

  @Test
  def TestSimpleUnionAllComponentWorking(): Unit ={


    val schema = StructType(List(StructField("col1", DataTypes.StringType, true)
      , StructField("col2", DataTypes.StringType, true)
      , StructField("col3", DataTypes.StringType, true)
      , StructField("col4", DataTypes.StringType, true)))

    val df1= new DataBuilder(schema).addData(List("1", "C2R1", "C3Rx", "C4R1"))
      .addData(List("2", "C2R2", "C3Rx", "C4R2"))
      .addData(List("3", "C2R3", "C3Rx", "C4R3"))
      .build()


    val df2= new DataBuilder(schema).addData(List("4", "C2R1", "C3Rx", "C4R1"))
      .addData(List("5", "C2R2", "C3Rx", "C4R2"))
      .addData(List("6", "C2R3", "C3Rx", "C4R3"))
      .build()

    val unionAllEntity: UnionAllEntity = new UnionAllEntity
    unionAllEntity.setComponentId("unionAll")
    unionAllEntity.setOutSocket(new OutSocket("out1"))

    val cp= new BaseComponentParams
    cp.addinputDataFrame(df1)
    cp.addinputDataFrame(df2)

    val unionDF=new SparkUnionAllComponent(unionAllEntity,cp).createComponent()

    val rows=Bucket(schema,unionDF.get("out1").get).result()

    Assert.assertEquals(6,rows.size)
  }

}
