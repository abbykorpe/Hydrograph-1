package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.OutputJdbcUpdateEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.OutputJdbcUpdateComponent
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by santlalg on 1/19/2017.
  */
class OutputJdbcUpdateAdapter(typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase {

  private var outputJdbcUpdateEntityGenerator: OutputJdbcUpdateEntityGenerator = null
  private var sparkOJdbcUpdateComponent: OutputJdbcUpdateComponent = null

  override def getComponent(): SparkFlow = sparkOJdbcUpdateComponent

  override def createGenerator(): Unit = {
    outputJdbcUpdateEntityGenerator = new OutputJdbcUpdateEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkOJdbcUpdateComponent = new OutputJdbcUpdateComponent(outputJdbcUpdateEntityGenerator.getEntity,
      baseComponentParams)
  }
}