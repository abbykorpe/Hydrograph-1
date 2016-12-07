package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.UnionAllEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.SparkUnionAllComponent
import hydrograph.engine.spark.components.adapter.base.StraightPullAdatperBase
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by gurdits on 10/27/2016.
  */
class UnionAllAdapter(typeBaseComponent: TypeBaseComponent) extends StraightPullAdatperBase{

  private var unionAllGenerator:UnionAllEntityGenerator=null
  private var unionAllComponent:SparkUnionAllComponent=null

  override def getComponent(): StraightPullComponentBase = unionAllComponent

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    unionAllComponent=new SparkUnionAllComponent(unionAllGenerator.getEntity,baseComponentParams)
  }

  override def createGenerator(): Unit = {
    unionAllGenerator=new UnionAllEntityGenerator(typeBaseComponent)
  }
}
