package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.DiscardEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.SparkDiscardComponent
import hydrograph.engine.spark.components.adapter.base.{ OutputAdatperBase}
import hydrograph.engine.spark.components.base.{ SparkFlow}
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by snehada on 12/16/2016.
  */
class DiscardAdapter(typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase{

  var discardEntityGenerator:DiscardEntityGenerator=null;
  var sparkDiscardComponent:SparkDiscardComponent=null;

  override def createGenerator(): Unit = {
    discardEntityGenerator=  new DiscardEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkDiscardComponent= new SparkDiscardComponent(discardEntityGenerator.getEntity,baseComponentParams)
  }

  override def getComponent(): SparkFlow = sparkDiscardComponent
}