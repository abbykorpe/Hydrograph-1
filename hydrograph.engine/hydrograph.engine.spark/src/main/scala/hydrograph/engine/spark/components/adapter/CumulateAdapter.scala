package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.CumulateEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.CumulateComponent
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by gurdits on 10/27/2016.
  */
class CumulateAdapter(typeBaseComponent: TypeBaseComponent) extends OperationAdatperBase{

  var cumulate:CumulateEntityGenerator=null;
var CumulateComponent:CumulateComponent=null;

  override def createGenerator(): Unit = {
    cumulate=  new CumulateEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    CumulateComponent= new CumulateComponent(cumulate.getEntity,baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = CumulateComponent
}
