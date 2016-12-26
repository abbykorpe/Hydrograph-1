package hydrograph.engine.spark.components.adapter.factory

import java.io.FileInputStream
import java.util.Properties

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.jaxb.main.Graph
import hydrograph.engine.spark.components.adapter.base.AdapterBase

import scala.collection.JavaConversions._
import scala.collection.mutable
import java.io.IOException
import java.io.FileReader

import hydrograph.engine.core.utilities.PropertiesHelper
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by gurdits on 10/26/2016.
  */
class AdapterFactory(graph: Graph) {

  val LOG : Logger = LoggerFactory.getLogger(classOf[AdapterFactory])
  val COMPONENT_ASSEMBLY_MAP_PROPERTIES: String = "componentMapping.properties"
  val componentMap=new mutable.HashMap[String,AdapterBase]()
  var props = null

 private def loadProps(): Unit = {
   try {
     props = PropertiesHelper.getProperties(COMPONENT_ASSEMBLY_MAP_PROPERTIES)
   }
   catch {
     case e: IOException =>
       LOG.error("Error reading properties file: " + COMPONENT_ASSEMBLY_MAP_PROPERTIES, e)
       throw new RuntimeException(e)
   }
 }


  def generatedAdapterMap(typeBaseComponentList: List[TypeBaseComponent]): Unit= {
    typeBaseComponentList.foreach(x=>{
      componentMap.+=(x.getId->getAdapterObject(x))
    })
  }

  def getAdapterMap(): mutable.HashMap[String,AdapterBase] ={
    componentMap
  }

 private def getAdapterObject(typeBaseComponent: TypeBaseComponent): AdapterBase = {
    val clazz = props.get(typeBaseComponent.getClass.getName).toString
    val adapterClass = Class.forName(clazz)
    val constructor=adapterClass.getDeclaredConstructor(classOf[TypeBaseComponent])
    val adapterBase=constructor.newInstance(typeBaseComponent).asInstanceOf[AdapterBase]
   adapterBase.createGenerator()
   adapterBase
  }
}
object AdapterFactory{

  def apply(graph: Graph): AdapterFactory ={
    val af=new AdapterFactory(graph)
    af.loadProps()
    af.generatedAdapterMap(graph.getInputsOrOutputsOrStraightPulls.toList)
    af
  }
}

