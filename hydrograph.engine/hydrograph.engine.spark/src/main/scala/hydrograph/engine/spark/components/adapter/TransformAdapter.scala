package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.TransformEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.SparkTransformComponent
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.UpdatedTransformComponent
import hydrograph.engine.spark.components.SparkTransformComponent
import hydrograph.engine.spark.components.UpdatedTransformComponent
import hydrograph.engine.spark.components.SparkTransformComponent
import hydrograph.engine.spark.components.UpdatedTransformComponent
import hydrograph.engine.spark.components.ToBeTransformComponent

/**
 * Created by gurdits on 10/27/2016.
 */
class TransformAdapter(typeBaseComponent: TypeBaseComponent) extends OperationAdatperBase {

  var transform: TransformEntityGenerator = null;
  var sparkTransformComponent: UpdatedTransformComponent = null;

  override def createGenerator(): Unit = {
    transform = new TransformEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkTransformComponent = new UpdatedTransformComponent(transform.getEntity, baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = sparkTransformComponent
}
