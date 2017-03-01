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
package hydrograph.engine.spark.flow

import hydrograph.engine.spark.components.adapter.ExecutionTrackingAdapter
import hydrograph.engine.spark.components.adapter.base.{RunProgramAdapterBase, _}
import hydrograph.engine.spark.components.base.{ComponentParameterBuilder, SparkFlow}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.execution.tracking.PartitionStageAccumulator
import org.apache.spark.sql.DataFrame

import scala.collection.JavaConverters._
import scala.collection.mutable
/**
  * The Class FlowBuilder.
  *
  * @author Bitwise
  *
  */

class FlowBuilder(runtimeContext: RuntimeContext) {


  def buildFlow(): mutable.LinkedHashSet[SparkFlow] ={
    val flow=new mutable.LinkedHashSet[SparkFlow]()
    for(batch<- runtimeContext.traversal.getFlowsNumber.asScala){
     flow ++= createAndConnect(batch)
    }

    flow
  }


  def createAndConnect(batch:String): mutable.LinkedHashSet[SparkFlow] = {
    val flow=new mutable.LinkedHashSet[SparkFlow]()
    val outLinkMap = new mutable.HashMap[String, Map[String,DataFrame]]()


    for (compID <- runtimeContext.traversal.getOrderedComponentsList(batch).asScala) {

      var baseComponentParams: BaseComponentParams = null;
      val adapterBase:AdapterBase = runtimeContext.adapterFactory.getAdapterMap().get(compID).get



      if (adapterBase.isInstanceOf[InputAdatperBase]) {
        baseComponentParams = ComponentParameterBuilder(compID, runtimeContext, outLinkMap,new BaseComponentParams())
          .setSparkSession(runtimeContext.sparkSession).build()

        adapterBase.createComponent(baseComponentParams)
        val inputDataFrame= adapterBase.asInstanceOf[InputAdatperBase].getComponent().createComponent()
        outLinkMap +=(compID -> inputDataFrame)
      }
        else if(adapterBase.isInstanceOf[ExecutionTrackingAdapter]){

//        val accumulator = runtimeContext.sparkSession.sparkContext.longAccumulator(compID)
        val partAcc = new PartitionStageAccumulator
        runtimeContext.sparkSession.sparkContext.register(partAcc, compID)

        baseComponentParams = ComponentParameterBuilder(compID, runtimeContext, outLinkMap,new BaseComponentParams())
          .setInputDataFrame().setSparkSession(runtimeContext.sparkSession).setInputDataFrameWithCompID().setInputSchemaFieldsWithCompID()
          .setOutputSchemaFields().setInputSchemaFields().setAccumulator(partAcc).build()
        //.setLongAccumulator(accumulator)


        adapterBase.createComponent(baseComponentParams)
        val opDataFrame= adapterBase.asInstanceOf[OperationAdatperBase].getComponent().createComponent()
        outLinkMap +=(compID -> opDataFrame)


      }

      else if (adapterBase.isInstanceOf[OperationAdatperBase]) {
        baseComponentParams = ComponentParameterBuilder(compID, runtimeContext, outLinkMap,new BaseComponentParams())
          .setInputDataFrame().setSparkSession(runtimeContext.sparkSession).setInputDataFrameWithCompID().setInputSchemaFieldsWithCompID()
          .setOutputSchemaFieldsForOperation().setInputSchemaFields().build()

        adapterBase.createComponent(baseComponentParams)
        val opDataFrame= adapterBase.asInstanceOf[OperationAdatperBase].getComponent().createComponent()
        outLinkMap +=(compID -> opDataFrame)

      }
      else if (adapterBase.isInstanceOf[StraightPullAdatperBase]) {
        baseComponentParams = ComponentParameterBuilder(compID, runtimeContext, outLinkMap,new BaseComponentParams())
          .setInputDataFrame().setOutputSchemaFields().setInputSchemaFields().build()

        adapterBase.createComponent(baseComponentParams)
        val opDataFrame= adapterBase.asInstanceOf[StraightPullAdatperBase].getComponent().createComponent()
        outLinkMap +=(compID -> opDataFrame)
      }
      else if (adapterBase.isInstanceOf[OutputAdatperBase]) {
        baseComponentParams = ComponentParameterBuilder(compID, runtimeContext, outLinkMap,new BaseComponentParams())
            .setSparkSession(runtimeContext.sparkSession).setInputDataFrame().build()

        adapterBase.createComponent(baseComponentParams)
      flow += adapterBase.asInstanceOf[OutputAdatperBase].getComponent()
        adapterBase.asInstanceOf[OutputAdatperBase].getComponent().setSparkFlowName(compID)

      }
      else if (adapterBase.isInstanceOf[RunProgramAdapterBase]) {
        baseComponentParams = ComponentParameterBuilder(compID, runtimeContext, outLinkMap, new BaseComponentParams())
          .build()
        adapterBase.createComponent(baseComponentParams)
        flow += adapterBase.asInstanceOf[RunProgramAdapterBase].getComponent()
        adapterBase.asInstanceOf[RunProgramAdapterBase].getComponent().setSparkFlowName(compID)
      }

    }

flow
  }


}

object FlowBuilder {
  def apply(runtimeContext: RuntimeContext): FlowBuilder = {
    new FlowBuilder(runtimeContext)
  }
}
