package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputOracleEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.OracleInputComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams


class InputOracleAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase {

  var inputOracleEntityGenerator: InputOracleEntityGenerator = null
  var inputOracleComponent: OracleInputComponent = null;

  override def getComponent(): InputComponentBase = inputOracleComponent

  override def createGenerator(): Unit = {
    inputOracleEntityGenerator = new InputOracleEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    inputOracleComponent = new OracleInputComponent(inputOracleEntityGenerator.getEntity, baseComponentParams)
  }
}
