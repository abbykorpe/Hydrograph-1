package hydrograph.engine.spark.executiontracking.plugin
import hydrograph.engine.spark.components.base.SparkFlow

import scala.collection.mutable

/**
  * Created by aniketmo on 2/2/2017.
  */
class CommandComponentsDefaultPlugin extends HydrographCommandListener{
  override def initialize(startSparkFlow: mutable.LinkedHashSet[SparkFlow]): Unit = {}

  override def end(flow: SparkFlow): Unit = {}
}
