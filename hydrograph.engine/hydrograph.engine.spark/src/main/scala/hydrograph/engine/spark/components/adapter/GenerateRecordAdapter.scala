package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.GenerateRecordEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.GenerateRecordComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

class GenerateRecordAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase {
  
   private var generateRecord:GenerateRecordEntityGenerator=null
  private var sparkGenerateRecordComponent:GenerateRecordComponent=null

  override def createGenerator(): Unit = {
     generateRecord=new GenerateRecordEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkGenerateRecordComponent= new GenerateRecordComponent(generateRecord.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = sparkGenerateRecordComponent
  
}