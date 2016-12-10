package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputFileDelimitedEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.InputFileDelimitedComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

class InputFileDelimitedAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase{

  private var inputFileDelimited:InputFileDelimitedEntityGenerator=null
  private var sparkIFileDelimitedComponent:InputFileDelimitedComponent=null

  override def createGenerator(): Unit = {
     inputFileDelimited=new InputFileDelimitedEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkIFileDelimitedComponent= new InputFileDelimitedComponent(inputFileDelimited.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = sparkIFileDelimitedComponent
}
