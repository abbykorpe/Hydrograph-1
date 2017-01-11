package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputRedshiftEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.InputRedshiftComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams


class InputRedshiftAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase {

  var inputRedshiftEntityGenerator: InputRedshiftEntityGenerator = null
  var inputRedshiftComponent: InputRedshiftComponent = null;

  override def getComponent(): InputComponentBase = inputRedshiftComponent

  override def createGenerator(): Unit = {
    inputRedshiftEntityGenerator = new InputRedshiftEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    inputRedshiftComponent = new InputRedshiftComponent(inputRedshiftEntityGenerator.getEntity, baseComponentParams)
  }
}
