/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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