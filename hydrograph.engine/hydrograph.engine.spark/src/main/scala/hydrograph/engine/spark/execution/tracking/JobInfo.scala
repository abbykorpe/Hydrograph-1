package hydrograph.engine.spark.execution.tracking

import hydrograph.engine.execution.tracking
import hydrograph.engine.execution.tracking.ComponentInfo
import hydrograph.engine.spark.executiontracking.plugin.Component
import org.apache.spark.scheduler.{SparkListenerStageCompleted, SparkListenerTaskEnd}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by Aniketmo on 12/22/2016.
  */
class JobInfo(componentInfoMap: mutable.ListBuffer[Component]) {


  val componentInfoList = new java.util.ArrayList[tracking.ComponentInfo]
  val componentInfoHashSet = new mutable.HashSet[tracking.ComponentInfo]

  def storeComponentStats(taskEnd: SparkListenerTaskEnd): Unit = {
    generateStats(taskEnd)
  }

  def updateStatusOfComponents(stageCompleted: SparkListenerStageCompleted): Unit = {
    val Status = if(stageCompleted.stageInfo.failureReason.equals(None))"SUCCESSFUL"
    else "FAILED"
    val listOfStatus : mutable.ListBuffer[String] = new ListBuffer[String]
    componentInfoList.asScala.filter(cInfo => cInfo.getStageId.
      equals(stageCompleted.stageInfo.stageId)).
      foreach(compInfo=>{

        if(compInfo.getStatusPerSocketMap().values().iterator().hasNext){
        for(value <- compInfo.getStatusPerSocketMap().values().asScala){
          listOfStatus+=value
        }}

        if(listOfStatus.contains("FAILED")){
          for(key : String <- compInfo.getStatusPerSocketMap().keySet().asScala){
            compInfo.setProcessedRecordCount(key,-1)
          }
          compInfo.setCurrentStatus("FAILED")
        }
        else if (listOfStatus.contains("RUNNING")) {
          compInfo.setCurrentStatus("RUNNING")
        }
        else if (listOfStatus.contains("SUCCESSFUL")) {
          compInfo.setCurrentStatus("SUCCESSFUL")
        }

      })

  }

  private def generateStats(taskEnd: SparkListenerTaskEnd): Unit = {
    //    ElementGraph elementGraph = extractElementGraphFromCascadeStats(cascadingStats);
    val Status = if(taskEnd.taskInfo.status.equals("SUCCESS"))"SUCCESSFUL"
    else taskEnd.taskInfo.status
    taskEnd.taskInfo.accumulables.foreach(f => {
      componentInfoMap.filter(c => c.newComponentId.equals(f.name.get)).foreach(component => {


        val alreadyPresentCompInfo  = componentInfoList.asScala
          .filter(compInfo=> compInfo.getComponentId.equals(component.compId))


        if (alreadyPresentCompInfo.size > 0) {
          /*componentInfoHashSet
            .filter(compInfo => {
              compInfo.getComponentId.equals(component.compId)
            }).foreach(componentInfo => {
            componentInfo.setStageId(taskEnd.stageId)
            componentInfo.setComponentId(component.compId)
            componentInfo.setBatch(component.batch)
            componentInfo.setComponentName(component.compName)
            componentInfo.setProcessedRecordCount(component.outSocket, f.value.get.asInstanceOf[Long])
            componentInfo.setStatusPerSocketMap(component.outSocket, Status)
            componentInfo.setCurrentStatus("PENDING")
            //           componentInfoHashSet.add(componentInfo)
          })*/

          componentInfoList.asScala
            .filter(compInfo => {
              compInfo.getComponentId.equals(component.compId)
            }).foreach(componentInfo => {
            componentInfo.setStageId(taskEnd.stageId)
            componentInfo.setComponentId(component.compId)
            componentInfo.setBatch(component.batch)
            componentInfo.setComponentName(component.compName)
            componentInfo.setProcessedRecordCount(component.outSocket, f.value.get.asInstanceOf[Long])
            componentInfo.setStatusPerSocketMap(component.outSocket, Status)
            componentInfo.setCurrentStatus("RUNNING")
            //           componentInfoHashSet.add(componentInfo)
          })
        }
        else {
          val componentInfo = new ComponentInfo()
          componentInfo.setStageId(taskEnd.stageId)
          componentInfo.setComponentId(component.compId)
          componentInfo.setBatch(component.batch)
          componentInfo.setComponentName(component.compName)
          componentInfo.setProcessedRecordCount(component.outSocket, f.value.get.asInstanceOf[Long])
          componentInfo.setStatusPerSocketMap(component.outSocket, Status)
          componentInfo.setCurrentStatus("PENDING")
//          componentInfoHashSet.add(componentInfo)
          componentInfoList.add(componentInfo)
        }


      })
    })
  }


  def getStatus(): java.util.List[ComponentInfo] = {
    componentInfoList //componentInfoMap.values.toList
  }


}
