package hydrograph.engine.spark.executiontracking.plugin

import java.util
import java.util.Properties

import hydrograph.engine.transformation.userfunctions.base.{ReusableRow, FilterBase}
import org.apache.spark.util.LongAccumulator

/**
  * Created by gurdits on 12/19/2016.
  */
class ExecutionCounter(longAccumulator: LongAccumulator) extends FilterBase{
  override def prepare(props: Properties, inputFields: util.ArrayList[String]): Unit = {
  }

  override def cleanup(): Unit = {
  }

  override def isRemove(inputRow: ReusableRow): Boolean = {
    longAccumulator.add(1)
    false
  }
}
