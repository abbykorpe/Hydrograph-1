package hydrograph.engine.spark.flow

import java.io.IOException
import java.util.Properties

import hydrograph.engine.core.core.{HydrographJob, HydrographRuntimeService}
import hydrograph.engine.core.flowmanipulation.{FlowManipulationContext, FlowManipulationHandler}
import hydrograph.engine.core.helper.JAXBTraversal
import hydrograph.engine.core.schemapropagation.SchemaFieldHandler
import hydrograph.engine.spark.components.adapter.factory.AdapterFactory
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}
import scala.collection.JavaConverters._
/**
  * Created by gurdits on 10/17/2016.
  */

class HydrographRuntime extends HydrographRuntimeService {

  private val EXECUTION_TRACKING: String = "hydrograph.execution.tracking"
  private val LOG: Logger = LoggerFactory.getLogger(classOf[HydrographRuntime])
  private var flowManipulationContext:FlowManipulationContext=null;
  private var sparkSession:SparkSession=null;

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

     sparkSession = SparkSession.builder()
      .master(properties.getProperty("spark_master"))
      .appName(hydrographJob.getJAXBObject.getName)
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "/user/hive/warehouse")
      .enableHiveSupport()
      .getOrCreate()

    val schemaFieldHandler = new SchemaFieldHandler(
      hydrographJob.getJAXBObject().getInputsOrOutputsOrStraightPulls());

     flowManipulationContext = new FlowManipulationContext(hydrographJob, args, schemaFieldHandler, jobId)

    val flowManipulationHandler = new FlowManipulationHandler

    val updatedHydrographJob = flowManipulationHandler.execute(flowManipulationContext);

    flowManipulationContext.getTmpPath.asScala.foreach(println)

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

  override def oncomplete(): Unit = {
    //Deleting TempPath For Debug
    if(flowManipulationContext!=null){
      flowManipulationContext.getTmpPath.asScala.foreach(tmpPath=>{
        val fullPath: Path = new Path(tmpPath)
        // do not delete the root directory
        if (fullPath.depth != 0) {
          var fileSystem: FileSystem = null
          LOG.info("Deleting temp path:" + tmpPath)
          try {
            fileSystem = FileSystem.get(sparkSession.sparkContext.hadoopConfiguration)
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
