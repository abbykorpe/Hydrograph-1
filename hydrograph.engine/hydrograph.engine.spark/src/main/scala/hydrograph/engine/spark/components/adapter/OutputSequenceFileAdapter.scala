package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.{OutputFileSequenceFormatEntityGenerator, OutputFileDelimitedEntityGenerator}
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.{OutputSequenceFileComponent, OutputFileDelimitedComponent}
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

class OutputSequenceFileAdapter(typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase{

  private  var outputFileDelimited:OutputFileSequenceFormatEntityGenerator=null
private var sparkOFileDelimitedComponent:OutputSequenceFileComponent=null

  override def createGenerator(): Unit = {
    outputFileDelimited=new OutputFileSequenceFormatEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkOFileDelimitedComponent = new OutputSequenceFileComponent(outputFileDelimited.getEntity,
      baseComponentParams)
  }

  override def getComponent(): SparkFlow = sparkOFileDelimitedComponent
}
