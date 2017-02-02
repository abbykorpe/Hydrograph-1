package hydrograph.engine.spark.components.base

import org.apache.spark.util.LongAccumulator

import scala.collection.mutable.ListBuffer

/**
  * Created by gurdits on 10/27/2016.
  */
abstract class CommandComponentSparkFlow extends SparkFlow{

    var exitStatus : Int = -3


}
