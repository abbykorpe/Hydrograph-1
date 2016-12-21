package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.OutputOracleEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.OracleOutputComponent
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by amiyam on 16-12-2016.
  */
class OutputOracleAdapter(typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase {
  var outputOracleEntityGenerator: OutputOracleEntityGenerator = null
  var outputOracleComponent: OracleOutputComponent = null;

  override def getComponent(): SparkFlow = outputOracleComponent

  override def createGenerator(): Unit = {
    outputOracleEntityGenerator = new OutputOracleEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    outputOracleComponent = new OracleOutputComponent(outputOracleEntityGenerator.getEntity, baseComponentParams)
  }
}
