package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.{JoinEntityGenerator, TransformEntityGenerator}
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.{JoinComponent, TransformComponent}
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by gurdits on 10/27/2016.
  */
class JoinAdapter(typeBaseComponent: TypeBaseComponent) extends OperationAdatperBase{

  var joinEntity:JoinEntityGenerator=null;
var sparkJoinComponent:JoinComponent=null;

  override def createGenerator(): Unit = {
    joinEntity=  new JoinEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkJoinComponent= new JoinComponent(joinEntity.getEntity,baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = sparkJoinComponent
}
