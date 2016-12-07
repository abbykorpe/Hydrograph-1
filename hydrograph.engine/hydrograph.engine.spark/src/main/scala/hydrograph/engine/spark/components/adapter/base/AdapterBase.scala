package hydrograph.engine.spark.components.adapter.base

import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by gurdits on 10/26/2016.
  */
trait AdapterBase {

  def createGenerator()

  def createComponent(baseComponentParams: BaseComponentParams)



}
