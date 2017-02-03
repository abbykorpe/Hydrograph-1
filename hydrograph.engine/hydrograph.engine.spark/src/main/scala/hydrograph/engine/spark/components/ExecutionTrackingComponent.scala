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
package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.ExecutionTrackingEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.EncoderHelper
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.util.LongAccumulator

/**
  * Created by vaijnathp on 12/12/2016.
  */
class ExecutionTrackingComponent(executionTrackingEntity: ExecutionTrackingEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with Serializable {
  override def createComponent(): Map[String, DataFrame] = {
    val key = executionTrackingEntity.getOutSocketList.get(0).getSocketId
     val fieldNameSet = new util.LinkedHashSet[String]()
    executionTrackingEntity.getOperation.getOperationInputFields.foreach(e => fieldNameSet.add(e))
    val df = componentsParams.getDataFrame()
    val longAccumulator: LongAccumulator = componentsParams.getAccumulator()

    val dataFrame= df.mapPartitions(itr=>{
      val acc=longAccumulator
            acc.add(0)
      itr.map(row=> {acc.add(1)
        row
      })
    }) (RowEncoder(df.schema))


    /*val dataFrame=df.map(row=> {longAccumulator.add(1)
  row
}) (RowEncoder(df.schema))*/

//      dataFrame.foreach(row => {longAccumulator.add(1)})

    /*val dataFrame=df.filter(row => {longAccumulator.add(1)
      true})*/
    //dataFrame.foreach(r => println("******" + r))

    Map(key -> dataFrame)
  }


}
