package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.entity.base.HiveEntityBase
import hydrograph.engine.core.component.generator.base.InputComponentGeneratorBase
import hydrograph.engine.core.component.generator.{InputFileHiveParquetEntityGenerator, InputFileHiveTextEntityGenerator}
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.jaxb.inputtypes.HiveTextFile
import hydrograph.engine.spark.components.InputHiveComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams


class InputHiveAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase{

  private var inputHiveComponent:InputHiveComponent=null
  private var generator : InputComponentGeneratorBase=null


  override def getComponent(): InputComponentBase = inputHiveComponent

  override def createGenerator(): Unit = {
    generator=mapComponent(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    inputHiveComponent=new InputHiveComponent((generator.getEntity).asInstanceOf[HiveEntityBase],baseComponentParams)
  }

  def mapComponent(typeBaseComponent: TypeBaseComponent): InputComponentGeneratorBase={

    if(typeBaseComponent.isInstanceOf[HiveTextFile] )
      new InputFileHiveTextEntityGenerator(typeBaseComponent)
    else
      new InputFileHiveParquetEntityGenerator(typeBaseComponent)

  }
}
