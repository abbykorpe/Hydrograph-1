package hydrograph.engine.spark.executiontracking.plugin

import java.util
import java.util.Properties

import hydrograph.engine.transformation.userfunctions.base.{FilterBase, ReusableRow}
import org.apache.spark.util.LongAccumulator

/**
  * Created by gurdits on 12/19/2016.
  */
class ExecutionCounter(longAccumulator: LongAccumulator) extends FilterBase{
//  var incrementer : Long = _
  override def prepare(props: Properties, inputFields: util.ArrayList[String]): Unit = {

  }

  override def cleanup(): Unit = {
//    longAccumulator.reset()
//    longAccumulator.add(incrementer)
  }

  override def isRemove(inputRow: ReusableRow): Boolean = {
    longAccumulator.add(1)
//    incrementer+=1
//    println("incrementer : "+incrementer)
    false
  }
}
