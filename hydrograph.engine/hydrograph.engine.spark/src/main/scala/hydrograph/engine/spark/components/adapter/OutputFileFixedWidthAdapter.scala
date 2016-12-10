package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.{OutputFileFixedWidthEntityGenerator, OutputFileDelimitedEntityGenerator}
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.{SparkOFileFixedWidthComponent, OutputFileDelimitedComponent}
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

class OutputFileFixedWidthAdapter(typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase{

  private  var outputFileFixedWidth:OutputFileFixedWidthEntityGenerator=null
private var sparkOFileFixedWidthComponent:SparkOFileFixedWidthComponent=null

  override def createGenerator(): Unit = {
    outputFileFixedWidth=new OutputFileFixedWidthEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkOFileFixedWidthComponent = new SparkOFileFixedWidthComponent(outputFileFixedWidth.getEntity,
      baseComponentParams)
  }

  override def getComponent(): SparkFlow = sparkOFileFixedWidthComponent
}
