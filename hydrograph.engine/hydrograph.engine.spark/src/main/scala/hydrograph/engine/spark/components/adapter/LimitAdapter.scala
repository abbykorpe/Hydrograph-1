package hydrograph.engine.spark.components.adapter


import hydrograph.engine.core.component.generator.LimitEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.SparkAggregateComponent
import hydrograph.engine.spark.components.SparkLimitComponent

import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.SparkLimitComponent
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.adapter.base.StraightPullAdatperBase
import hydrograph.engine.spark.components.SparkLimitComponent
import hydrograph.engine.spark.components.platform.BaseComponentParams

class LimitAdapter(typeBaseComponent: TypeBaseComponent) extends StraightPullAdatperBase {

  var limit:LimitEntityGenerator=null;
  var sparkLimitComponent:SparkLimitComponent=null;

  override def createGenerator(): Unit = {
    limit=  new LimitEntityGenerator(typeBaseComponent)
  }

   def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkLimitComponent= new SparkLimitComponent(limit.getEntity,baseComponentParams)
  }

   def getComponent(): SparkLimitComponent = sparkLimitComponent
}
