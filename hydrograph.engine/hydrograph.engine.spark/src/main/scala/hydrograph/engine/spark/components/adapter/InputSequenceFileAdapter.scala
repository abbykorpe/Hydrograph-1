package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputFileSequenceFormatEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.InputSequenceFileComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

class InputSequenceFileAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase{

  private var inputFileDelimited:InputFileSequenceFormatEntityGenerator=null
  private var sparkIFileDelimitedComponent:InputSequenceFileComponent=null

  override def createGenerator(): Unit = {
     inputFileDelimited=new InputFileSequenceFormatEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkIFileDelimitedComponent= new InputSequenceFileComponent(inputFileDelimited.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = sparkIFileDelimitedComponent
}
