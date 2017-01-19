package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.OutputFileDelimitedEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.OutputFileCsvWithDateFormatsComponent
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

class OutputFileCsvWithDateFormatsAdapter(typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase{

  private  var outputFileDelimited:OutputFileDelimitedEntityGenerator=null
private var sparkOFileDelimitedComponent:OutputFileCsvWithDateFormatsComponent=null

  override def createGenerator(): Unit = {
    outputFileDelimited=new OutputFileDelimitedEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkOFileDelimitedComponent = new OutputFileCsvWithDateFormatsComponent(outputFileDelimited.getEntity,
      baseComponentParams)
  }

  override def getComponent(): SparkFlow = sparkOFileDelimitedComponent
}
