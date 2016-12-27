package hydrograph.engine.spark.components.adapter

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.core.component.generator.LookupEntityGenerator
import hydrograph.engine.spark.components.LookupComponent

/**
  * Created by gurdits on 10/27/2016.
  */
class LookupAdapter(typeBaseComponent: TypeBaseComponent) extends OperationAdatperBase{

  var lookupEntity:LookupEntityGenerator=null;
var sparkLookupComponent:LookupComponent=null;

  override def createGenerator(): Unit = {
    lookupEntity=  new LookupEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkLookupComponent= new LookupComponent(lookupEntity.getEntity,baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = sparkLookupComponent
}
