package hydrograph.engine.spark.components.adapter.base

import hydrograph.engine.spark.components.base.SparkFlow

/**
  * Created by gurdits on 10/27/2016.
  */
trait OutputAdatperBase extends AdapterBase{

  def getComponent():SparkFlow
}
