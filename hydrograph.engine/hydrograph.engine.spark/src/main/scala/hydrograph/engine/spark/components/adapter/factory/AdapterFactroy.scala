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

/**
  * Created by gurdits on 10/26/2016.
  */
class AdapterFactroy(graph: Graph) {

  val props = new Properties()
  val componentMap=new mutable.HashMap[String,AdapterBase]()

 private def loadProps(): Unit = {
    val fileName="componentMapping.properties"
		var propFileName = System.getProperty(fileName);
		if (propFileName == null) {
			propFileName = fileName;
				props.load(ClassLoader
						.getSystemResourceAsStream(propFileName));
		} else {
			
				val reader = new FileReader(propFileName);
				props.load(reader);
				if (reader != null) {
						reader.close();
			}
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
object AdapterFactroy{

  def apply(graph: Graph): AdapterFactroy ={
    val af=new AdapterFactroy(graph)
    af.loadProps()
    af.generatedAdapterMap(graph.getInputsOrOutputsOrStraightPulls.toList)
    af
  }
}

