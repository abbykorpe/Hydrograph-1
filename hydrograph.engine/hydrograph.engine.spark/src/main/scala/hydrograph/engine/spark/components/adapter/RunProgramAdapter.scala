package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.RunProgramEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.RunProgramComponent
import hydrograph.engine.spark.components.adapter.base.RunProgramAdapterBase
import hydrograph.engine.spark.components.base.{CommandComponentSparkFlow}
import hydrograph.engine.spark.components.platform.BaseComponentParams

class RunProgramAdapter(typeBaseComponent: TypeBaseComponent) extends RunProgramAdapterBase{

  private  var runProgramEntityGenerator:RunProgramEntityGenerator=null
  private var runProgramComponent:RunProgramComponent=null

  override def createGenerator(): Unit = {
    runProgramEntityGenerator=new RunProgramEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    runProgramComponent = new RunProgramComponent(runProgramEntityGenerator.getEntity)
  }

  override def getComponent(): CommandComponentSparkFlow = runProgramComponent
}
