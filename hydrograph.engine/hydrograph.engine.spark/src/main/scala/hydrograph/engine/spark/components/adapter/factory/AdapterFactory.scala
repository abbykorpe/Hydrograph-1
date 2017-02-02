package hydrograph.engine.spark.components.adapter.factory

import java.io.IOException
import java.util.Properties

import hydrograph.engine.core.utilities.PropertiesHelper
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.jaxb.main.Graph
import hydrograph.engine.spark.components.adapter.base.AdapterBase
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
  * Created by gurdits on 10/26/2016.
  */
class AdapterFactory(graph: Graph) {

  val LOG : Logger = LoggerFactory.getLogger(classOf[AdapterFactory])
  val COMPONENT_ASSEMBLY_MAP_PROPERTIES: String = "componentMapping.properties"
  val componentMap=new mutable.HashMap[String,AdapterBase]()
  var props : Properties = _

 private def loadProps(): Unit = {
   try {
     props = PropertiesHelper.getProperties(COMPONENT_ASSEMBLY_MAP_PROPERTIES)
   }
   catch {
     case e: IOException =>
       LOG.error("Error reading properties file: " + COMPONENT_ASSEMBLY_MAP_PROPERTIES)
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

  /**
    *
    * @param typeBaseComponent The component whose adapter object is to be returned
    * @return
    * @throws NullPointerException if component mapping for the <code>typeBaseComponent<code> is not present in the properties file
    */
  private def getAdapterObject(typeBaseComponent: TypeBaseComponent): AdapterBase = {
    val clazz = props.get(typeBaseComponent.getClass.getName)
    if(clazz == null){
      throw new NullPointerException("Component mapping not found for: " + typeBaseComponent.getClass.getName)
    }
    val adapterClass = Class.forName(clazz.toString)
    val constructor = adapterClass.getDeclaredConstructor(classOf[TypeBaseComponent])
    val adapterBase = constructor.newInstance(typeBaseComponent).asInstanceOf[AdapterBase]
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

