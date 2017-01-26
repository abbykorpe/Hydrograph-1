package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.RunSqlGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.RunSQLComponent
import hydrograph.engine.spark.components.adapter.base.RunProgramAdapterBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

class RunSqlAdapter(typeBaseComponent: TypeBaseComponent) extends RunProgramAdapterBase {

  private var runSqlEntityGenerator: RunSqlGenerator = null
  private var runSqlComponent: RunSQLComponent = null

  override def createGenerator(): Unit = {
    runSqlEntityGenerator = new RunSqlGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    runSqlComponent = new RunSQLComponent(runSqlEntityGenerator.getEntity)
  }

  override def getComponent(): SparkFlow = runSqlComponent
}
