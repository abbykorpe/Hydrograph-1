package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputFileMixedSchemeEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.InputFileMixedSchemeComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

class InputFileMixedSchemeAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase{

  private var inputFileMixedScheme:InputFileMixedSchemeEntityGenerator=null
  private var sparkIFileMixedSchemeComponent:InputFileMixedSchemeComponent=null

  override def createGenerator(): Unit = {
     inputFileMixedScheme=new InputFileMixedSchemeEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkIFileMixedSchemeComponent= new InputFileMixedSchemeComponent(inputFileMixedScheme.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = sparkIFileMixedSchemeComponent
}
