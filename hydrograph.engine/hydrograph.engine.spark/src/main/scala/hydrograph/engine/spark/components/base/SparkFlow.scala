package hydrograph.engine.spark.components.base

import org.apache.spark.util.LongAccumulator

import scala.collection.mutable.ListBuffer

/**
  * Created by gurdits on 10/27/2016.
  */
abstract class SparkFlow {

  var accList=new ListBuffer[LongAccumulator]

  def setAccumulatorOnFlow(acc : ListBuffer[LongAccumulator]): Unit ={
    accList = acc
  }

  def getAccumulatorOnFlow():ListBuffer[LongAccumulator] ={
    accList
  }



  def execute()
}
