package hydrograph.engine.spark.flow

import java.util.Properties

import hydrograph.engine.core.core.{HydrographDebugInfo, HydrographJob, HydrographRuntimeService}
import hydrograph.engine.core.helper.JAXBTraversal
import hydrograph.engine.core.schemapropagation.SchemaFieldHandler
import hydrograph.engine.spark.components.adapter.factory.AdapterFactroy
import hydrograph.engine.spark.flow.utils.{FlowManipulationHandler, FlowManipulationContext}
import org.apache.spark.sql.SparkSession

/**
  * Created by gurdits on 10/17/2016.
  */

case class RuntimeContext(adapterFactroy: AdapterFactroy, traversal: JAXBTraversal, hydrographJob: HydrographJob, schemaFieldHandler:
SchemaFieldHandler,sparkSession: SparkSession)

class HydrographRuntime extends HydrographRuntimeService {

  override def prepareToExecute(): Unit = {

  }

  override def kill(): Unit = {}

  override def execute(): Unit = {}

  override def initialize(properties: Properties, strings: Array[String], hydrographJob: HydrographJob,
                          hydrographDebugInfo: HydrographDebugInfo, jobId: String, basePath: String, s2: String): Unit
  = {

    val sparkSession = SparkSession.builder()
      .master(properties.getProperty("spark_master"))
      .appName(hydrographJob.getJAXBObject.getName)
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()


    val schemaFieldHandler = new SchemaFieldHandler(
      hydrographJob.getJAXBObject().getInputsOrOutputsOrStraightPulls());


    val flowManipulationContext=new FlowManipulationContext(hydrographJob,hydrographDebugInfo,schemaFieldHandler,
      jobId,basePath,sparkSession
      .sparkContext.hadoopConfiguration)

   val  updatedHydrographJob=FlowManipulationHandler.execute(flowManipulationContext);

    val adapterFactroy = AdapterFactroy(updatedHydrographJob.getJAXBObject)


    val traversal = new JAXBTraversal(updatedHydrographJob.getJAXBObject());

    val flows = FlowBuilder(RuntimeContext(adapterFactroy, traversal, updatedHydrographJob, schemaFieldHandler,sparkSession))
      .buildFlow()

    for (sparkFLow <- flows) {
      sparkFLow.execute()
    }
    sparkSession.stop()
  }

  override def getExecutionStatus: AnyRef = {
    null
  }

  override def oncomplete(): Unit = {}
}
