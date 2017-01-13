package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.{OutputFileDelimitedEntityGenerator, OutputFileXMLEntityGenerator}
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.{OutputFileDelimitedComponent, OutputFileXMLComponent}
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by bitwise on 1/12/2017.
  */
class OutputFileXMLAdapter (typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase {

  private  var outputFileXML:OutputFileXMLEntityGenerator=null
  private var sparkOFileXMLComponent:OutputFileXMLComponent=null

  override def createGenerator(): Unit = {
    outputFileXML=new OutputFileXMLEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkOFileXMLComponent = new OutputFileXMLComponent(outputFileXML.getEntity,
      baseComponentParams)
  }

  override def getComponent(): SparkFlow = sparkOFileXMLComponent

}
