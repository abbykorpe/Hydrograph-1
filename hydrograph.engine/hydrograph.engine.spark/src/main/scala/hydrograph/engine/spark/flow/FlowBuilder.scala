package hydrograph.engine.spark.flow

import com.google.inject.Guice
import hydrograph.engine.spark.components.adapter.OutputFileDelimitedAdapter
import hydrograph.engine.spark.components.adapter.base.{RunProgramAdapterBase, _}
import hydrograph.engine.spark.components.base.{ComponentParameterBuilder, SparkFlow}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.util.LongAccumulator

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by gurdits on 10/17/2016.
  */


class FlowBuilder(runtimeContext: RuntimeContext) {

//  val accList=new ListBuffer[LongAccumulator]

  def buildFlow(): mutable.ListBuffer[SparkFlow] ={
    val flow=new mutable.ListBuffer[SparkFlow]()
    for(batch<- runtimeContext.traversal.getFlowsNumber.asScala){
     flow ++= createAndConnect(batch)
    }

    flow
  }


  def createAndConnect(batch:String): mutable.ListBuffer[SparkFlow] = {
    val flow=new mutable.ListBuffer[SparkFlow]()
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
        /*else if(adapterBase.isInstanceOf[Exoodfl]){executiontrackingCode
        baseComponentParams = ComponentParameterBuilder(compID, runtimeContext, outLinkMap,new BaseComponentParams())
          .setInputDataFrame().setSparkSession(runtimeContext.sparkSession).setInputDataFrameWithCompID().setInputSchemaFieldsWithCompID()
          .setOutputSchemaFields().setInputSchemaFields().build()

        adapterBase.createComponent(baseComponentParams)
        val opDataFrame= adapterBase.asInstanceOf[OperationAdatperBase].getComponent().createComponent()
        outLinkMap +=(compID -> opDataFrame)

        val ac = runtimeContext.sparkSession.sparkContext.longAccumulator(compID+"accList")
        ac.add(1)
        ac.add(1)
        accList+=ac
      }*/

      else if (adapterBase.isInstanceOf[OperationAdatperBase]) {
        baseComponentParams = ComponentParameterBuilder(compID, runtimeContext, outLinkMap,new BaseComponentParams())
          .setInputDataFrame().setSparkSession(runtimeContext.sparkSession).setInputDataFrameWithCompID().setInputSchemaFieldsWithCompID()
          .setOutputSchemaFields().setInputSchemaFields().build()

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
//        adapterBase.asInstanceOf[OutputAdatperBase].getComponent().setAccumulatorOnFlow(accList)

      }
      else if (adapterBase.isInstanceOf[RunProgramAdapterBase]) {
        baseComponentParams = ComponentParameterBuilder(compID, runtimeContext, outLinkMap, new BaseComponentParams())
          .build()
        adapterBase.createComponent(baseComponentParams)
        flow += adapterBase.asInstanceOf[RunProgramAdapterBase].getComponent()
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
