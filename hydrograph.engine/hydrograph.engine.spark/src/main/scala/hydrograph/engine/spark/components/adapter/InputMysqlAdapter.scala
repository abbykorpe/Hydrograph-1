package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputMysqlEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.InputMysqlComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by santlalg on 12/7/2016.
  */
class InputMysqlAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase{
  private var inputMysql:InputMysqlEntityGenerator=null
  private var sparkIMysqlComponent:InputMysqlComponent=null

  override def createGenerator(): Unit = {
    inputMysql=new InputMysqlEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkIMysqlComponent= new InputMysqlComponent(inputMysql.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = sparkIMysqlComponent
}
