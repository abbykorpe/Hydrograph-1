package hydrograph.engine.spark.batchbreak.plugin

import java.util

import hydrograph.engine.core.flowmanipulation.FlowManipulationHandler

/**
  * Created by gurdits on 12/15/2016.
  */
class DefaultPluginManipulation extends FlowManipulationHandler{
  override def addDefaultPlugins(): util.List[String] = new util.LinkedList[String]()

}
