package hydrograph.engine.spark.components.adapter.base

import hydrograph.engine.spark.components.base.InputComponentBase

/**
  * Created by gurdits on 10/27/2016.
  */
trait InputAdatperBase extends AdapterBase{

  def getComponent():InputComponentBase
}
