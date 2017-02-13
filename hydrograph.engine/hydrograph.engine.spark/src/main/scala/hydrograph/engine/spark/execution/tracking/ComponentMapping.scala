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
package hydrograph.engine.spark.execution.tracking

import hydrograph.engine.core.core.HydrographJob
import hydrograph.engine.core.utilities.SocketUtilities
import hydrograph.engine.jaxb.commontypes.{TypeBaseComponent, TypeBaseInSocket, TypeBaseOutSocket}
import hydrograph.engine.jaxb.operationstypes.Filter
import hydrograph.engine.spark.executiontracking.plugin.Component
import hydrograph.engine.spark.flow.RuntimeContext

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by Aniketmo on 12/23/2016.
  */
object ComponentMapping {

  private val listOfComponents = new mutable.ListBuffer[Component]

  private val mapOfOutCompAndPreviousComps : mutable.HashMap[String,ListBuffer[String]] = new mutable.HashMap[String,ListBuffer[String]]

  def addComps(map : mutable.HashMap[String,ListBuffer[String]]){
    mapOfOutCompAndPreviousComps++=map
  }

  def getComps(): mutable.HashMap[String,ListBuffer[String]] ={
    mapOfOutCompAndPreviousComps
  }

  def getListOfComponents(): mutable.ListBuffer[Component]= {
    listOfComponents
  }

  def addComponent( component: Component): Unit = {
    listOfComponents += (component)
  }
}