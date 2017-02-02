package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.RemoveDupsEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.RemoveDupsComponent
import hydrograph.engine.spark.components.adapter.base.StraightPullAdatperBase
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

class RemoveDupsAdapter(typeBaseComponent: TypeBaseComponent) extends StraightPullAdatperBase{

  private var removeDupsGenerator:RemoveDupsEntityGenerator=null
  private var removeDupsComponent:RemoveDupsComponent=null

  override def getComponent(): StraightPullComponentBase = removeDupsComponent

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    removeDupsComponent=new RemoveDupsComponent(removeDupsGenerator.getEntity,baseComponentParams)
  }

  override def createGenerator(): Unit = {
    removeDupsGenerator=new RemoveDupsEntityGenerator(typeBaseComponent)
  }
}
