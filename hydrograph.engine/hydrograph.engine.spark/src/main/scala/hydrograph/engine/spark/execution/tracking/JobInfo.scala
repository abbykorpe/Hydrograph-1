package hydrograph.engine.spark.execution.tracking

import hydrograph.engine.execution.tracking
import hydrograph.engine.execution.tracking.ComponentInfo
import hydrograph.engine.spark.executiontracking.plugin.Component
import org.apache.spark.scheduler._

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by Aniketmo on 12/22/2016.
  */
class JobInfo(componentInfoMap: mutable.ListBuffer[Component]) {

  val componentInfoList = new java.util.ArrayList[tracking.ComponentInfo]
  val componentInfoHashSet = new mutable.HashSet[tracking.ComponentInfo]

  def storeComponentStatsForTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = {
    generateStatsForTaskEnd(taskEnd)
  }

  def storeComponentStatsForTaskStart(taskStart: SparkListenerTaskStart): Unit = {
    generateStatsForTaskStart(taskStart)
  }

  def updateStatusOfComponentsOnStageSubmitted(stageSubmitted: SparkListenerStageSubmitted): Unit = {
    val Status = if(stageSubmitted.stageInfo.failureReason.equals(None))"SUCCESSFUL"
    else "FAILED"
    val listOfStatus : mutable.ListBuffer[String] = new ListBuffer[String]
    componentInfoList.asScala.filter(cInfo => cInfo.getStageId.
      equals(stageSubmitted.stageInfo.stageId)).
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

  private def generateStatsForTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = {
    //    ElementGraph elementGraph = extractElementGraphFromCascadeStats(cascadingStats);
    val Status = if(taskEnd.taskInfo.status.equals("SUCCESS"))"SUCCESSFUL"
    else taskEnd.taskInfo.status
//    println("Status in TaskEnd : "+Status)
    taskEnd.taskInfo.accumulables.foreach(f => {

//      println("acumulator name : "+f.name.get + "accumulator value : "+ f.value.get)
      componentInfoMap.filter(c => c.newComponentId.equals(f.name.get)).foreach(component => {


        val alreadyPresentCompInfo  = componentInfoList.asScala
          .filter(compInfo=> compInfo.getComponentId.equals(component.compId))

//        println("Reached after alreadyPresentCompInfo checking of taskEnd")
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
//            println("mark status as running of taskEnd")
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
//          println("mark status as PENDING of taskEnd")
//          componentInfoHashSet.add(componentInfo)
          componentInfoList.add(componentInfo)
        }


      })
    })
  }

  private def generateStatsForTaskStart(taskStart: SparkListenerTaskStart): Unit = {
    //    ElementGraph elementGraph = extractElementGraphFromCascadeStats(cascadingStats);
    val Status = taskStart.taskInfo.status
//    println("Status in TaskStart : "+Status)
    /*if(taskGettingResult.taskInfo.status.equals("SUCCESS"))"SUCCESSFUL"
    else taskStart.taskInfo.status*/
    taskStart.taskInfo.accumulables.foreach(f => {
//      println("acumulator name : "+f.name.get + "accumulator value : "+ f.value.get)
      componentInfoMap.filter(c => c.newComponentId.equals(f.name.get)).foreach(component => {


        val alreadyPresentCompInfo  = componentInfoList.asScala
          .filter(compInfo=> compInfo.getComponentId.equals(component.compId))

//        println("Reached after alreadyPresentCompInfo checking of taskStart")
        if (alreadyPresentCompInfo.size > 0) {

          componentInfoList.asScala
            .filter(compInfo => {
              compInfo.getComponentId.equals(component.compId)
            }).foreach(componentInfo => {
            componentInfo.setStageId(taskStart.stageId)
            componentInfo.setComponentId(component.compId)
            componentInfo.setBatch(component.batch)
            componentInfo.setComponentName(component.compName)
            componentInfo.setProcessedRecordCount(component.outSocket, f.value.get.asInstanceOf[Long])
            componentInfo.setStatusPerSocketMap(component.outSocket, Status)
            componentInfo.setCurrentStatus("RUNNING")
//            println("mark status as running of taskStart")
            //           componentInfoHashSet.add(componentInfo)
          })
        }
        else {
          val componentInfo = new ComponentInfo()
          componentInfo.setStageId(taskStart.stageId)
          componentInfo.setComponentId(component.compId)
          componentInfo.setBatch(component.batch)
          componentInfo.setComponentName(component.compName)
          componentInfo.setProcessedRecordCount(component.outSocket, f.value.get.asInstanceOf[Long])
          componentInfo.setStatusPerSocketMap(component.outSocket, Status)
          componentInfo.setCurrentStatus("PENDING")
//          println("mark status as PENDING of taskStart")
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
