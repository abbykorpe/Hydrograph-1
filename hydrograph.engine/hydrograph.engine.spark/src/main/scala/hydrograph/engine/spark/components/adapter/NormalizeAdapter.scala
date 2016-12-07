package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.{NormalizeEntityGenerator, TransformEntityGenerator}
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.{SparkNormalizeComponent, SparkTransformComponent}
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by gurdits on 10/27/2016.
  */
class NormalizeAdapter(typeBaseComponent: TypeBaseComponent) extends OperationAdatperBase{

  var transform:NormalizeEntityGenerator=null;
var sparkNormalizeComponent:SparkNormalizeComponent=null;

  override def createGenerator(): Unit = {
    transform=  new NormalizeEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkNormalizeComponent= new SparkNormalizeComponent(transform.getEntity,baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = sparkNormalizeComponent
}
