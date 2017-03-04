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

import hydrograph.engine.jaxb.commontypes._
import hydrograph.engine.jaxb.main.Graph
import hydrograph.engine.spark.components.adapter.ExecutionTrackingAdapter
import hydrograph.engine.spark.components.adapter.base.{RunProgramAdapterBase, _}
import hydrograph.engine.spark.components.base.{ComponentParameterBuilder, SparkFlow}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.execution.tracking.PartitionStageAccumulator
import org.apache.spark.sql.DataFrame
import org.apache.spark.storage.StorageLevel

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
        setCacheLevel(extractRuntimeProperties(compID,runtimeContext.hydrographJob.getJAXBObject),inputDataFrame)
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
        setCacheLevel(extractRuntimeProperties(compID,runtimeContext.hydrographJob.getJAXBObject),opDataFrame)
        outLinkMap +=(compID -> opDataFrame)

      }
      else if (adapterBase.isInstanceOf[StraightPullAdatperBase]) {
        baseComponentParams = ComponentParameterBuilder(compID, runtimeContext, outLinkMap,new BaseComponentParams())
          .setInputDataFrame().setOutputSchemaFields().setInputSchemaFields().build()

        adapterBase.createComponent(baseComponentParams)
        val opDataFrame= adapterBase.asInstanceOf[StraightPullAdatperBase].getComponent().createComponent()
        setCacheLevel(extractRuntimeProperties(compID,runtimeContext.hydrographJob.getJAXBObject),opDataFrame)

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

  private def extractRuntimeProperties(compId:String,graph: Graph): TypeProperties ={
    val jaxbObject=graph.getInputsOrOutputsOrStraightPulls.asScala

    jaxbObject.filter(c=>c.getId.equals(compId)).head match {
      case op: TypeInputComponent =>
        op.asInstanceOf[TypeInputComponent].getRuntimeProperties
      case op: TypeOutputComponent =>
        op.asInstanceOf[TypeOutputComponent].getRuntimeProperties
      case st: TypeStraightPullComponent =>
        st.asInstanceOf[TypeStraightPullComponent].getRuntimeProperties
      case op: TypeOperationsComponent =>
        op.asInstanceOf[TypeOperationsComponent].getRuntimeProperties

    }
  }

  private def setCacheLevel(typeProperty: TypeProperties,dataFrameMap:Map[String,DataFrame]): Unit ={
    val splitter=":"
    if(typeProperty!= null && typeProperty.getProperty!=null){
      typeProperty.getProperty.asScala.filter(tp=> tp.getName.equalsIgnoreCase("cache")).foreach(tp=>{
        if(tp.getValue.contains(splitter)) {
          val storageType = tp.getValue.trim.substring(tp.getValue.trim.lastIndexOf(splitter) + 1, tp.getValue.trim.length)
          val outSocketId = tp.getValue.trim.substring(0, tp.getValue.trim.lastIndexOf(splitter))
          if(storageType.equalsIgnoreCase("disk"))
            dataFrameMap(outSocketId).persist(StorageLevel.DISK_ONLY)
          else if(storageType.equalsIgnoreCase("memory"))
            dataFrameMap(outSocketId).persist(StorageLevel.MEMORY_ONLY_SER)
          else if(storageType.equalsIgnoreCase("memory_and_disk"))
            dataFrameMap(outSocketId).persist(StorageLevel.MEMORY_AND_DISK_SER)
        }
        else{
          throw new RuntimeException("Cache Property value should be in proper format eg: outSocketID : One of \"disk,memory,memory_and_disk\", But user provides: "+ tp.getValue)
        }
      })
    }
  }
}

object FlowBuilder {
  def apply(runtimeContext: RuntimeContext): FlowBuilder = {
    new FlowBuilder(runtimeContext)
  }
}
