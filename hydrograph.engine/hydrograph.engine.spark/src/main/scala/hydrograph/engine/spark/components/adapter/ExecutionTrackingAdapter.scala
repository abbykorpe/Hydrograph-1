package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.ExecutionTrackingEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.ExecutionTrackingComponent
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by vaijnathp on 12/12/2016.
  */
class ExecutionTrackingAdapter(typeBaseComponent: TypeBaseComponent) extends OperationAdatperBase {

  var filter: ExecutionTrackingEntityGenerator = null;
  var sparkFilterComponent: ExecutionTrackingComponent = null;

  override def createGenerator(): Unit = {
    filter = new ExecutionTrackingEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkFilterComponent = new ExecutionTrackingComponent(filter.getEntity, baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = sparkFilterComponent
}
