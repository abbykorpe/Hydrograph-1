package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.OutputFileParquetEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import org.junit.Test
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.testing.wrapper.DataBuilder
import hydrograph.engine.testing.wrapper.Fields
import scala.collection.JavaConverters._

class OutputFileParquetComponentTest {

  @Test
  def TestOutputFileParquetComponentWorking(): Unit = {

    val df = new DataBuilder(Fields(List("col1", "col2", "col3", "col4")).applyTypes(List(classOf[String],
      classOf[String], classOf[String], classOf[String]))).addData(List("1", "C2R1", "C3Rx", "C4R1"))
      .addData(List("2", "C2R2", "C3Rx", "C4R2"))
      .addData(List("3", "C2R3", "C3Rx", "C4R3"))
      .build()

    val outputFileParquetEntity = new OutputFileParquetEntity()
    outputFileParquetEntity.setPath("testData/inputFiles/delimitedInputFile.txt")
    val sf0: SchemaField = new SchemaField("col1", "java.lang.Integer")
    val sf1: SchemaField = new SchemaField("col2", "java.lang.String")

    val list: List[SchemaField] = List(sf0, sf1)
    val javaList = list.asJava
    outputFileParquetEntity.setFieldsList(javaList)

    val baseComponentParams = new BaseComponentParams
    baseComponentParams.addinputDataFrame(df)

    new OutputFileParquetComponent(outputFileParquetEntity, baseComponentParams).execute()

  }
}