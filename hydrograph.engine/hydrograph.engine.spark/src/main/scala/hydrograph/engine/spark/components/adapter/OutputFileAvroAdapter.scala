package hydrograph.engine.spark.components.adapter

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.OutputFileDelimitedComponent
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.core.component.generator.OutputFileAvroEntityGenerator
import hydrograph.engine.spark.components.OutputFileAvroComponent
import hydrograph.engine.spark.components.base.SparkFlow

class OutputFileAvroAdapter(typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase{

  private var outputFileAvroEntityGenerator:OutputFileAvroEntityGenerator=null
  private var outputFileComponent:OutputFileAvroComponent=null

  override def createGenerator(): Unit = {
    outputFileAvroEntityGenerator=new OutputFileAvroEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    outputFileComponent = new OutputFileAvroComponent(outputFileAvroEntityGenerator.getEntity,
      baseComponentParams)
  }

  override def getComponent(): SparkFlow = outputFileComponent
}
