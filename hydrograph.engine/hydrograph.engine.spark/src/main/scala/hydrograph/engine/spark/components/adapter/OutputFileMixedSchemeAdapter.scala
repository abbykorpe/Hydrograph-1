package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.OutputFileMixedSchemeEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.OutputFileMixedSchemeComponent
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

class OutputFileMixedSchemeAdapter(typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase{

  private  var outputFileMixedScheme:OutputFileMixedSchemeEntityGenerator=null
private var sparkOFileMixedSchemeComponent:OutputFileMixedSchemeComponent=null

  override def createGenerator(): Unit = {
    outputFileMixedScheme=new OutputFileMixedSchemeEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkOFileMixedSchemeComponent = new OutputFileMixedSchemeComponent(outputFileMixedScheme.getEntity,
      baseComponentParams)
  }

  override def getComponent(): SparkFlow = sparkOFileMixedSchemeComponent
}
