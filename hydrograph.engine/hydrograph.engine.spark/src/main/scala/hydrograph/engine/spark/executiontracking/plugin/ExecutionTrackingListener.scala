package hydrograph.engine.spark.executiontracking.plugin

import hydrograph.engine.execution.tracking.ComponentInfo
import hydrograph.engine.spark.flow.RuntimeContext
import org.apache.spark.scheduler.SparkListener
import org.apache.spark.sql.SparkSession

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by gurdits on 12/15/2016.
  */
trait ExecutionTrackingListener extends SparkListener{

  def addListener(runtimeContext: RuntimeContext): Unit ={
//    sparkSession.sparkContext.addSparkListener(this)
  }

  def getStatus():mutable.HashSet[ComponentInfo]

}
