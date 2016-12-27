package hydrograph.engine.spark.flow

import java.util.Properties

import hydrograph.engine.core.core.{HydrographJob, HydrographRuntimeService}
import hydrograph.engine.core.flowmanipulation.{FlowManipulationContext, FlowManipulationHandler}
import hydrograph.engine.core.helper.JAXBTraversal
import hydrograph.engine.core.schemapropagation.SchemaFieldHandler
import hydrograph.engine.spark.components.adapter.factory.AdapterFactory
import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by gurdits on 10/17/2016.
  */

class HydrographRuntime extends HydrographRuntimeService {

  private val EXECUTION_TRACKING: String = "hydrograph.execution.tracking"
  private val LOG: Logger = LoggerFactory.getLogger(classOf[HydrographRuntime])

  override def prepareToExecute(): Unit = {

  }

  override def kill(): Unit = {
    LOG.info("Kill signal received")
    if (RuntimeContext.instance.sparkSession != null) {
      LOG.info("Killing Spark jobs")
      RuntimeContext.instance.sparkSession.stop()
    }
    else {
      LOG.info("No Spark jobs present to kill. Exiting code.")
      System.exit(0)
    }
  }

  override def execute(): Unit = {}

  override def initialize(properties: Properties, args: Array[String], hydrographJob: HydrographJob,
                          jobId: String, s2: String): Unit
  = {

    val sparkSession = SparkSession.builder()
      .master(properties.getProperty("spark_master"))
      .appName(hydrographJob.getJAXBObject.getName)
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    val schemaFieldHandler = new SchemaFieldHandler(
      hydrographJob.getJAXBObject().getInputsOrOutputsOrStraightPulls());

    val flowManipulationContext = new FlowManipulationContext(hydrographJob, args, schemaFieldHandler, jobId)

    val flowManipulationHandler = new FlowManipulationHandler

    val updatedHydrographJob = flowManipulationHandler.execute(flowManipulationContext);

    val adapterFactory = AdapterFactory(updatedHydrographJob.getJAXBObject)

    val traversal = new JAXBTraversal(updatedHydrographJob.getJAXBObject());

    val runtimeContext = RuntimeContext(adapterFactory, traversal, updatedHydrographJob, flowManipulationContext.getSchemaFieldHandler, sparkSession)
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
