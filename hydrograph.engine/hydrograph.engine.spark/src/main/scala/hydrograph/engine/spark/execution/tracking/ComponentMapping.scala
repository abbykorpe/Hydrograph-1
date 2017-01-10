package hydrograph.engine.spark.execution.tracking

import hydrograph.engine.core.core.HydrographJob
import hydrograph.engine.core.utilities.SocketUtilities
import hydrograph.engine.jaxb.commontypes.{TypeBaseComponent, TypeBaseInSocket, TypeBaseOutSocket}
import hydrograph.engine.jaxb.operationstypes.Filter
import hydrograph.engine.spark.executiontracking.plugin.Component
import hydrograph.engine.spark.flow.RuntimeContext

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by Aniketmo on 12/23/2016.
  */
object ComponentMapping {

  private val componentInfoMap = new mutable.ListBuffer[Component]

  def getComponentInfoMap(): mutable.ListBuffer[Component]= {
    componentInfoMap
  }

  def addComponent( component: Component): Unit = {
    componentInfoMap += (component)
  }
}
