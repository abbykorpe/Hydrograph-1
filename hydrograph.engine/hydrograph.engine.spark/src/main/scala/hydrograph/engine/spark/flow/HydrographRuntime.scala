package hydrograph.engine.spark.flow

import java.util.Properties

import hydrograph.engine.core.core.{HydrographDebugInfo, HydrographJob, HydrographRuntimeService}
import hydrograph.engine.core.flowmanipulation.FlowManipulationContext
import hydrograph.engine.core.helper.JAXBTraversal
import hydrograph.engine.core.schemapropagation.SchemaFieldHandler
import hydrograph.engine.spark.batchbreak.plugin.DefaultPluginManipulation
import hydrograph.engine.spark.components.adapter.factory.AdapterFactroy
import org.apache.spark.sql.SparkSession

/**
  * Created by gurdits on 10/17/2016.
  */

case class RuntimeContext(adapterFactroy: AdapterFactroy, traversal: JAXBTraversal, hydrographJob: HydrographJob, schemaFieldHandler:
SchemaFieldHandler,sparkSession: SparkSession)

class HydrographRuntime extends HydrographRuntimeService {

  private val EXECUTION_TRACKING: String = "hydrograph.execution.tracking"

  override def prepareToExecute(): Unit = {

  }

  override def kill(): Unit = {}

  override def execute(): Unit = {}

  override def initialize(properties: Properties, strings: Array[String], hydrographJob: HydrographJob,
                          hydrographDebugInfo: HydrographDebugInfo, jobId: String, basePath: String, s2: String): Unit
  = {

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName(hydrographJob.getJAXBObject.getName)
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()


    val schemaFieldHandler = new SchemaFieldHandler(
      hydrographJob.getJAXBObject().getInputsOrOutputsOrStraightPulls());

    val flowManipulationContext=new FlowManipulationContext(hydrographJob,hydrographDebugInfo,schemaFieldHandler,
      jobId,basePath)

    val flowManipulationHandler=new DefaultPluginManipulation

   val  updatedHydrographJob=flowManipulationHandler.execute(flowManipulationContext);

    val adapterFactroy = AdapterFactroy(updatedHydrographJob.getJAXBObject)


    val traversal = new JAXBTraversal(updatedHydrographJob.getJAXBObject());

    val runtimeContext=RuntimeContext(adapterFactroy, traversal, updatedHydrographJob, schemaFieldHandler,sparkSession)
    val flows = FlowBuilder(runtimeContext)
      .buildFlow()


//    val EXECUTION_TRACKING = "hydrograph.execution.tracking";

//    val oproperties = OrderedPropertiesHelper.getOrderedProperties("RegisterPlugin.properties")
//    val executionTrackingPluginName = oproperties.getProperty(EXECUTION_TRACKING)
//    val trackingInstance = Class.forName(executionTrackingPluginName).newInstance()
//    val executionTrackingListener = trackingInstance.asInstanceOf[ExecutionTrackingPlugin]
//    executionTrackingListener.addListener(runtimeContext.sparkSession)


    //    if (getExecutionTrackingClass(EXECUTION_TRACKING) != null) {
//     val executionTrackingListener = classLoader(getExecutionTrackingClass(EXECUTION_TRACKING)).asInstanceOf[ExecutionTrackingListener]
//      executionTrackingListener.addListener(sparkSession)
//    }


    for (sparkFLow <- flows) {
      sparkFLow.execute()
    }
    sparkSession.stop()
  }

  override def getExecutionStatus: AnyRef = {
    null
  }

  override def oncomplete(): Unit = {}

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }

//  def getExecutionTrackingClass(executionTrackingKey: String): String = {
//    var properties: OrderedProperties = new OrderedProperties
//    try {
//      properties = OrderedPropertiesHelper.getOrderedProperties("RegisterPlugin.properties")
//    }
//    catch {
//      case e: IOException => {
//        throw new RuntimeException("Error reading the properties file: RegisterPlugin.properties" + e)
//      }
//    }
//     properties.getProperty(executionTrackingKey)
//  }
}
