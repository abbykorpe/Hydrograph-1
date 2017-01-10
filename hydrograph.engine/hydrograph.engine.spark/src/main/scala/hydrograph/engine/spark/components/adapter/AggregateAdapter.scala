package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.AggregateEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.AggregateComponent
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by gurdits on 10/27/2016.
  */
class AggregateAdapter(typeBaseComponent: TypeBaseComponent) extends OperationAdatperBase{

  var aggregate:AggregateEntityGenerator=null;
var sparkAggregateComponent:AggregateComponent=null;

  override def createGenerator(): Unit = {
    aggregate=  new AggregateEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkAggregateComponent= new AggregateComponent(aggregate.getEntity,baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = sparkAggregateComponent
}
