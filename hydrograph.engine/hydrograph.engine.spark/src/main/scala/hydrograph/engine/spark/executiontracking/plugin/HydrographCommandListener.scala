package hydrograph.engine.spark.executiontracking.plugin

import hydrograph.engine.spark.components.base.SparkFlow

import scala.collection.mutable

/**
  * Created by gurdits on 12/15/2016.
  */
trait HydrographCommandListener {

  def initialize(startSparkFlow: mutable.LinkedHashSet[SparkFlow])

//  def start(flow : SparkFlow)

  def end(flow : SparkFlow)

}
