package hydrograph.engine.spark.test.executionhack

import hydrograph.engine.core.xmlparser.HydrographXMLInputService
import hydrograph.engine.core.core.HydrographJob
import hydrograph.engine.core.props.PropertiesLoader
import hydrograph.engine.core.schemapropagation.SchemaFieldHandler
import hydrograph.engine.spark.components.adapter.factory.AdapterFactory
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.LongType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.SaveMode
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase

object Sample extends App {

  val propertiesLoader = PropertiesLoader.getInstance();
  val inputService: HydrographXMLInputService = new HydrographXMLInputService()

  val job: HydrographJob = inputService.parseHydrographJob(
    propertiesLoader.getInputServiceProperties(), Array("-xmlpath", "C:/PushpenderG/DART/POC/hydrograph_spark/spark_poc/hydrograph/hydrograph.engine/hydrograph.engine.command-line/testData/XMLFiles/TransformPerformanceCheck.xml"))

  val schemaFieldHandler: SchemaFieldHandler = new SchemaFieldHandler(
    job.getJAXBObject().getInputsOrOutputsOrStraightPulls());

  val adapterFactroy = AdapterFactory(job.getJAXBObject)

  val sparkSession = SparkSession.builder().master("local")
    .getOrCreate()

  val readDF = sparkSession.read
    .option("delimiter", ",")
    .option("header", false)
    .option("charset", "ISO-8859-1")
    .schema(StructType(Array(StructField("id", LongType, false), StructField("name", StringType, false), StructField("number", LongType, false), StructField("city", StringType, false))))
    .csv("C:/PushpenderG/DART/POC/hydrograph_spark/testfiles/input/aggregateInputFile.txt")

  val adapterBase = adapterFactroy.getAdapterMap().get("reformat").get

  val cp: BaseComponentParams = new BaseComponentParams()
  cp.addinputDataFrame(readDF)
  val schemaFieldList = schemaFieldHandler.getSchemaFieldMap.get("reformat" + "_" + "out0")
  cp.addSchemaFields(schemaFieldList.toArray[SchemaField](new Array[SchemaField](schemaFieldList.size())))

  adapterBase.createComponent(cp)
  val opDataFrame = adapterBase.asInstanceOf[OperationAdatperBase].getComponent().createComponent()("out0")

  opDataFrame.write
    .option("delimiter", ",")
    .option("header", false)
    .option("charset", "")
    .mode(SaveMode.Overwrite)
    .csv("testData/output/trperformance")

}