package hydrograph.engine.spark.components.adapter.base

import hydrograph.engine.spark.components.base.{CommandComponentSparkFlow}

/**
  * Created by vaijnathp on 12/23/2016.
  */
trait RunProgramAdapterBase extends AdapterBase{
    def getComponent():CommandComponentSparkFlow
  }
