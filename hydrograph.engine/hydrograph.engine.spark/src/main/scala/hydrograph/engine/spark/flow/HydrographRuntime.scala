package hydrograph.engine.spark.flow

import java.io.IOException
import java.util.Properties

import hydrograph.engine.core.core.{HydrographJob, HydrographRuntimeService}
import hydrograph.engine.core.flowmanipulation.{FlowManipulationContext, FlowManipulationHandler}
import hydrograph.engine.core.helper.JAXBTraversal
import hydrograph.engine.core.schemapropagation.SchemaFieldHandler
import hydrograph.engine.spark.components.adapter.factory.AdapterFactory
import hydrograph.engine.spark.components.base.SparkFlow
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}
import org.apache.spark.SparkConf
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/**
  * Created by gurdits on 10/17/2016.
  */

class HydrographRuntime extends HydrographRuntimeService {

  private val EXECUTION_TRACKING: String = "hydrograph.execution.tracking"
  private val LOG: Logger = LoggerFactory.getLogger(classOf[HydrographRuntime])
  private var flowManipulationContext: FlowManipulationContext = null;
  private var flows: ListBuffer[SparkFlow] = null



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

  override def initialize(properties: Properties, args: Array[String], hydrographJob: HydrographJob,
                          jobId: String, udfPath: String): Unit = {

    val configProperties = getSparkProperties(hydrographJob)

    val sparkSessionBuilder: SparkSession.Builder = SparkSession.builder()
      .master(properties.getProperty("hydrograph.spark.master"))
      .appName(hydrographJob.getJAXBObject.getName)
      .config(configProperties)



    val schemaFieldHandler = new SchemaFieldHandler(
      hydrographJob.getJAXBObject().getInputsOrOutputsOrStraightPulls());

    flowManipulationContext = new FlowManipulationContext(hydrographJob, args, schemaFieldHandler, jobId)

    val flowManipulationHandler = new FlowManipulationHandler

    val updatedHydrographJob = flowManipulationHandler.execute(flowManipulationContext);

    val adapterFactory = AdapterFactory(updatedHydrographJob.getJAXBObject)

    val traversal = new JAXBTraversal(updatedHydrographJob.getJAXBObject());

    val sparkSession: SparkSession = checkAndEnableHiveSupport(sparkSessionBuilder, traversal, properties).getOrCreate()

    val runtimeContext = RuntimeContext(adapterFactory, traversal, updatedHydrographJob,
      flowManipulationContext.getSchemaFieldHandler, sparkSession)



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

  }

  def checkAndEnableHiveSupport(sessionBuilder: SparkSession.Builder, traversal: JAXBTraversal, properties: Properties): SparkSession.Builder = {
    LOG.trace("In method checkAndEnableHiveSupport()")
    if (traversal.isHiveComponentPresentInFlow) {
      LOG.debug("Hive components are present in flow. Enabling Hive support in SparkSession with warehouse location "+properties.getProperty("hydrograph.hive.warehouse"))
      sessionBuilder
        .config("spark.sql.warehouse.dir", properties.getProperty("hydrograph.hive.warehouse"))
        .enableHiveSupport()
    } else {
      sessionBuilder
        .config("spark.sql.warehouse.dir", properties.getProperty("hydrograph.tmp.warehouse"))
    }
    sessionBuilder
  }

  override def prepareToExecute(): Unit = {
    LOG.info("Building spark flows")
    flows = FlowBuilder(RuntimeContext.instance).buildFlow()
    LOG.info("Spark flows built successfully")
  }

  override def execute(): Unit = {
    /*if (GeneralUtilities.IsArgOptionPresent(args, CommandLineOptionsProcessor.OPTION_NO_EXECUTION)) {
      LOG.info(CommandLineOptionsProcessor.OPTION_NO_EXECUTION + " option is provided so skipping execution")
      return
    }*/
    for (sparkFlow <- flows) {
      sparkFlow.execute()
    }
    RuntimeContext.instance.sparkSession.stop()
  }

  override def getExecutionStatus: AnyRef = {
    null
  }

  override def oncomplete(): Unit = {
    //Deleting TempPath For Debug
    if (flowManipulationContext != null && flowManipulationContext.getTmpPath != null) {
      flowManipulationContext.getTmpPath.asScala.foreach(tmpPath => {
        val fullPath: Path = new Path(tmpPath)
        // do not delete the root directory
        if (fullPath.depth != 0) {
          var fileSystem: FileSystem = null
          LOG.info("Deleting temp path:" + tmpPath)
          try {
            fileSystem = FileSystem.get(RuntimeContext.instance.sparkSession.sparkContext.hadoopConfiguration)
            fileSystem.delete(fullPath, true)
          }
          catch {
            case exception: NullPointerException => {
              throw new RuntimeException(exception)
            }
            case e: IOException => {
              throw new RuntimeException(e)
            }
          }
        }
      })
    }
  }

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }

  def getSparkProperties(hydrographJob: HydrographJob): SparkConf = {
    val configProperties = new SparkConf()
    val runttimeProperties = hydrographJob.getJAXBObject().getRuntimeProperties
    if (runttimeProperties != null) {
      for (runtimeProperty <- runttimeProperties.getProperty.asScala) {
        configProperties.set(runtimeProperty.getName, runtimeProperty.getValue)
      }
    }
    configProperties
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