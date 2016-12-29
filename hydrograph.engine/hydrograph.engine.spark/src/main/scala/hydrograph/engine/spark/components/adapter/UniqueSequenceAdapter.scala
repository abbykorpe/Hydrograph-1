package hydrograph.engine.spark.components.adapter


import hydrograph.engine.core.component.generator.UniqueSequenceEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.UniqueSequenceComponent
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
/**
  * Created by sandeepv on 12/28/2016.
  */
class UniqueSequenceAdapter(typeBaseComponent: TypeBaseComponent) extends OperationAdatperBase{

  private var uniqueSequenceGenerator: UniqueSequenceEntityGenerator = null
  private var sparkUniqueSequenceComponent: UniqueSequenceComponent = null


  override def createGenerator(): Unit = {
    uniqueSequenceGenerator=new UniqueSequenceEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkUniqueSequenceComponent = new UniqueSequenceComponent(uniqueSequenceGenerator.getEntity,baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = sparkUniqueSequenceComponent
}