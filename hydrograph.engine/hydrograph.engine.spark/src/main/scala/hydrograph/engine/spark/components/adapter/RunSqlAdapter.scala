package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.RunSqlGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.RunSQLComponent
import hydrograph.engine.spark.components.adapter.base.RunProgramAdapterBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

class RunSqlAdapter(typeBaseComponent: TypeBaseComponent) extends RunProgramAdapterBase {

  private var runProgramEntityGenerator: RunSqlGenerator = null
  private var runProgramComponent: RunSQLComponent = null

  override def createGenerator(): Unit = {
    runProgramEntityGenerator = new RunSqlGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    runProgramComponent = new RunSQLComponent(runProgramEntityGenerator.getEntity)
  }

  override def getComponent(): SparkFlow = runProgramComponent
}
