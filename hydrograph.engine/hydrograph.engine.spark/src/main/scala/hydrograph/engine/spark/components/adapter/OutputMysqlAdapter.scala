package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.OutputMysqlEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.SparkOMysqlComponent
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by santlalg on 12/8/2016.
  */
class OutputMysqlAdapter (typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase{

  private  var outputMysqlEntityGenerator:OutputMysqlEntityGenerator=null
  private var sparkOMysqlComponent:SparkOMysqlComponent=null

  override def createGenerator(): Unit = {
    outputMysqlEntityGenerator=new OutputMysqlEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkOMysqlComponent = new SparkOMysqlComponent(outputMysqlEntityGenerator.getEntity,
      baseComponentParams)
  }

  override def getComponent(): SparkFlow = sparkOMysqlComponent
}
