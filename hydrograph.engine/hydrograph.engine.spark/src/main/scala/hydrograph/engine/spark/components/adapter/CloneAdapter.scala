package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.CloneEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.SparkCloneComponent
import hydrograph.engine.spark.components.adapter.base.StraightPullAdatperBase
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by gurdits on 10/27/2016.
  */
class CloneAdapter(typeBaseComponent: TypeBaseComponent) extends StraightPullAdatperBase{

  private var cloneGenerator:CloneEntityGenerator=null
  private var cloneComponent:SparkCloneComponent=null

  override def getComponent(): StraightPullComponentBase = cloneComponent

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    cloneComponent=new SparkCloneComponent(cloneGenerator.getEntity,baseComponentParams)
  }

  override def createGenerator(): Unit = {
    cloneGenerator=new CloneEntityGenerator(typeBaseComponent)
  }
}
