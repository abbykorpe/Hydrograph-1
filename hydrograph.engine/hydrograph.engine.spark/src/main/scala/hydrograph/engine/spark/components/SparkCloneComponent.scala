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

import hydrograph.engine.core.component.entity.CloneEntity
import hydrograph.engine.core.component.entity.elements.OutSocket
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.DataFrame

import scala.collection.JavaConversions._
/**
  * Created by gurdits on 10/15/2016.
  */
class SparkCloneComponent(cloneEntity: CloneEntity, componentsParams: BaseComponentParams)
  extends
    StraightPullComponentBase {


  override def createComponent(): Map[String, DataFrame] = {

    def generateDataFrame(outSocketList:List[OutSocket]):Map[String,DataFrame] = outSocketList match {
      case List() => Map()
      case x::xs => Map(x.getSocketId->componentsParams.getDataFrame()) ++ generateDataFrame(xs)
    }
    generateDataFrame(cloneEntity.getOutSocketList.toList)
  }

}

