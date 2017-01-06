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

  var executionTracking: ExecutionTrackingEntityGenerator = null;
  var sparkEexecutionTrackingComponent: ExecutionTrackingComponent = null;

  override def createGenerator(): Unit = {
    executionTracking = new ExecutionTrackingEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkEexecutionTrackingComponent = new ExecutionTrackingComponent(executionTracking.getEntity, baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = sparkEexecutionTrackingComponent
}
