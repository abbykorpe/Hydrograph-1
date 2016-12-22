package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.entity.base.HiveEntityBase
import hydrograph.engine.core.component.generator.base.{InputComponentGeneratorBase, OutputComponentGeneratorBase}
import hydrograph.engine.core.component.generator.{InputFileHiveParquetEntityGenerator, InputFileHiveTextEntityGenerator, OutputFileHiveParquetEntityGenerator, OutputFileHiveTextEntityGenerator}
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.jaxb.inputtypes.HiveTextFile
import hydrograph.engine.spark.components.{HiveInputComponent, HiveOutputComponent}
import hydrograph.engine.spark.components.adapter.base.{InputAdatperBase, OutputAdatperBase}
import hydrograph.engine.spark.components.base.{InputComponentBase, SparkFlow}
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by arshadalis on 12/9/2016.
  */
class InputHiveAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase{

  private var inputHiveComponent:HiveInputComponent=null
  private var generator : InputComponentGeneratorBase=null


  override def getComponent(): InputComponentBase = inputHiveComponent

  override def createGenerator(): Unit = {
    generator=mapComponent(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    inputHiveComponent=new HiveInputComponent((generator.getEntity).asInstanceOf[HiveEntityBase],baseComponentParams)
  }

  def mapComponent(typeBaseComponent: TypeBaseComponent): InputComponentGeneratorBase={

    if(typeBaseComponent.isInstanceOf[HiveTextFile] )
      new InputFileHiveTextEntityGenerator(typeBaseComponent)
    else
      new InputFileHiveParquetEntityGenerator(typeBaseComponent)

  }
}
