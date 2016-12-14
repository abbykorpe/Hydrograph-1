package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.OutputFileFixedWidthEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.OutputFileFixedWidthComponent
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

class OutputFileFixedWidthAdapter(typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase{

  private  var outputFileFixedWidth:OutputFileFixedWidthEntityGenerator=null
private var outputFileFixedWidthComponent:OutputFileFixedWidthComponent=null

  override def createGenerator(): Unit = {
    outputFileFixedWidth=new OutputFileFixedWidthEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    outputFileFixedWidthComponent = new OutputFileFixedWidthComponent(outputFileFixedWidth.getEntity,
      baseComponentParams)
  }

  override def getComponent(): SparkFlow = outputFileFixedWidthComponent
}
