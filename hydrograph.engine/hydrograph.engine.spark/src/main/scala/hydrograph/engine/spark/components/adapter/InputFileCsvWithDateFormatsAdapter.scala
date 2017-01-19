package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputFileDelimitedEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.InputFileCsvWithDateFormatsComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

class InputFileCsvWithDateFormatsAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase{

  private var inputFileDelimited:InputFileDelimitedEntityGenerator=null
  private var sparkIFileDelimitedComponent:InputFileCsvWithDateFormatsComponent=null

  override def createGenerator(): Unit = {
     inputFileDelimited=new InputFileDelimitedEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkIFileDelimitedComponent= new InputFileCsvWithDateFormatsComponent(inputFileDelimited.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = sparkIFileDelimitedComponent
}
