package hydrograph.engine.spark.components.adapter

import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.InputFileAvroComponent
import hydrograph.engine.core.component.generator.InputFileDelimitedEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.core.component.generator.InputFileAvroEntityGenerator
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase

class InputFileAvroAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase{

  private var inputFileAvroEntityGenerator:InputFileAvroEntityGenerator=null
  private var inputFileAvroComponent:InputFileAvroComponent=null

  override def createGenerator(): Unit = {
     inputFileAvroEntityGenerator=new InputFileAvroEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    inputFileAvroComponent= new InputFileAvroComponent(inputFileAvroEntityGenerator.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = inputFileAvroComponent
}