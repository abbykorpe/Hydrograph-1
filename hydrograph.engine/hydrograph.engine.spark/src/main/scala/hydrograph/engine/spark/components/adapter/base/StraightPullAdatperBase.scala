package hydrograph.engine.spark.components.adapter.base

import hydrograph.engine.spark.components.base.StraightPullComponentBase

/**
  * Created by gurdits on 10/27/2016.
  */
trait StraightPullAdatperBase extends AdapterBase{

  def getComponent():StraightPullComponentBase
}
