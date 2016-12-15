package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputFileFixedWidthEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.InputFileFixedWidthComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

class InputFileFixedWidthAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase{

  private var inputFileFixedWidth:InputFileFixedWidthEntityGenerator=null
  private var inputFileFixedWidthComponent:InputFileFixedWidthComponent=null

  override def createGenerator(): Unit = {
     inputFileFixedWidth=new InputFileFixedWidthEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    inputFileFixedWidthComponent= new InputFileFixedWidthComponent(inputFileFixedWidth.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = inputFileFixedWidthComponent
}
