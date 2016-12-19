package hydrograph.engine.spark.executiontracking.plugin

import org.apache.spark.scheduler.SparkListener
import org.apache.spark.sql.SparkSession

/**
  * Created by gurdits on 12/15/2016.
  */
abstract class ExecutionTrackingListener extends SparkListener{

  def addListener(sparkSession: SparkSession): Unit ={
    sparkSession.sparkContext.addSparkListener(this)
  }

}
