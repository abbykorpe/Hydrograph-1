package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.entity.base.HiveEntityBase
import hydrograph.engine.core.component.generator.base.OutputComponentGeneratorBase
import hydrograph.engine.core.component.generator.{OutputFileHiveParquetEntityGenerator, OutputFileHiveTextEntityGenerator}
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.jaxb.outputtypes.HiveTextFile
import hydrograph.engine.spark.components.HiveOutputComponent
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by arshadalis on 12/9/2016.
  */
class OutputHiveAdapter(typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase{

  private var sparkOHiveComponent:HiveOutputComponent=null
  private var generator : OutputComponentGeneratorBase=null



  override def getComponent(): SparkFlow = sparkOHiveComponent

  override def createGenerator(): Unit = {

    generator=mapComponent(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
       sparkOHiveComponent=new HiveOutputComponent((generator.getEntity).asInstanceOf[HiveEntityBase],baseComponentParams)

  }




  def mapComponent(typeBaseComponent: TypeBaseComponent): OutputComponentGeneratorBase={

      if(typeBaseComponent.isInstanceOf[HiveTextFile] )
        new OutputFileHiveTextEntityGenerator(typeBaseComponent)
      else
        new OutputFileHiveParquetEntityGenerator(typeBaseComponent)

  }


}


