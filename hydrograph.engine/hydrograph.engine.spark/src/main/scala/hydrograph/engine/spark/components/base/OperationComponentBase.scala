package hydrograph.engine.spark.components.base

import org.apache.spark.sql._

/**
  * Created by gurdits on 10/27/2016.
  */
abstract class OperationComponentBase {

  def createComponent():Map[String,DataFrame]
}
