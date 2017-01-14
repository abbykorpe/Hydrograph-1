package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputFileXMLEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.InputFileXMLComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by bitwise on 1/12/2017.
  */
class InputFileXMLAdapter (typeBaseComponent: TypeBaseComponent) extends InputAdatperBase {

  private var inputFileXML:InputFileXMLEntityGenerator=null
  private var sparkIFileXMLComponent:InputFileXMLComponent=null

  override def createGenerator(): Unit = {
    inputFileXML=new InputFileXMLEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkIFileXMLComponent= new InputFileXMLComponent(inputFileXML.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = sparkIFileXMLComponent

}
