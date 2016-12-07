package hydrograph.engine.spark.components.adapter.base

import hydrograph.engine.spark.components.base.OperationComponentBase

/**
  * Created by gurdits on 10/27/2016.
  */
trait OperationAdatperBase extends AdapterBase{

  def getComponent():OperationComponentBase
}
