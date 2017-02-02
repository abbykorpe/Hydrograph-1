package hydrograph.engine.spark.flow

import hydrograph.engine.core.core.HydrographJob
import hydrograph.engine.core.helper.JAXBTraversal
import hydrograph.engine.core.schemapropagation.SchemaFieldHandler
import hydrograph.engine.spark.components.adapter.factory.AdapterFactory
import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}

/**
  * The RuntimeContext class holds the objects required across the entire job execution.
  * <p>
  * Created by prabodhm on 12/19/2016.
  * </p>
  */
class RuntimeContext private(val adapterFactory: AdapterFactory, var traversal: JAXBTraversal,
                             var hydrographJob: HydrographJob, var schemaFieldHandler: SchemaFieldHandler, var sparkSession: SparkSession)

/**
  * The companion object for {@link hydrograph.engine.spark.flow.RuntimeContext RuntimeContext} class
  */
object RuntimeContext {

  private val LOG: Logger = LoggerFactory.getLogger(classOf[RuntimeContext])
  var _runtimeContext: RuntimeContext = null

  def apply(adapterFactory: AdapterFactory, traversal: JAXBTraversal, hydrographJob: HydrographJob, schemaFieldHandler:
  SchemaFieldHandler, sparkSession: SparkSession): RuntimeContext = {
    if (_runtimeContext == null) {
      LOG.info("Initializing RuntimeContext.")
      _runtimeContext = new RuntimeContext(adapterFactory, traversal, hydrographJob, schemaFieldHandler, sparkSession)
    }
    else {
      LOG.warn("RuntimeContext already initialized. Returning the same object.")
    }
    _runtimeContext
  }

  /**
    * Returns an initialized instance of [[hydrograph.engine.spark.flow.RuntimeContext RuntimeContext]]
    *
    * @throws java.lang.RuntimeException if the [[hydrograph.engine.spark.flow.RuntimeContext RuntimeContext]] is not properly initialized
    * @return the initialized instance of [[hydrograph.engine.spark.flow.RuntimeContext RuntimeContext]]
    */
  @throws(classOf[RuntimeException])
  def instance: RuntimeContext = {
    if (_runtimeContext == null) {
      throw new RuntimeException("RuntimeContext is not initialized.")
    }
    _runtimeContext
  }
}