package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputFileDelimitedEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.SparkIFileDelimitedComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by gurdits on 10/26/2016.
  */
class InputFileDelimitedAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase{

  private var inputFileDelimited:InputFileDelimitedEntityGenerator=null
  private var sparkIFileDelimitedComponent:SparkIFileDelimitedComponent=null

  override def createGenerator(): Unit = {
     inputFileDelimited=new InputFileDelimitedEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkIFileDelimitedComponent= new SparkIFileDelimitedComponent(inputFileDelimited.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = sparkIFileDelimitedComponent
}
