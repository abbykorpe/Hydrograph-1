package hydrograph.engine.spark.components.utils

import hydrograph.engine.core.component.entity.JoinEntity
import hydrograph.engine.core.component.entity.elements.JoinKeyFields
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.{ DataFrame, Column }
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{ StructField, StructType }
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import hydrograph.engine.core.component.entity.elements.PassThroughField
import scala.collection.mutable.ListBuffer
import hydrograph.engine.core.component.entity.base.OperationEntityBase
import hydrograph.engine.core.component.entity.LookupEntity

/**
 * Created by gurdits on 9/26/2016.
 */

case class JoinOperation(compID: String, inSocketId: String, dataFrame: DataFrame, keyFields: Array[String], unused: Boolean, recordRequired: Boolean, outSocketId: String, unusedSocketId: String)

class JoinUtils(operationEntity: OperationEntityBase, baseComponentParams: BaseComponentParams) {

  def prepareJoinOperation(): Array[JoinOperation] = {

    val joinHelperArray = new Array[JoinOperation](operationEntity.getInSocketList.size())

    operationEntity.getInSocketList().asScala.zipWithIndex.foreach {
      case (in, i) =>
        val unusedSocket = operationEntity.getOutSocketList.asScala.filter(out => out.getSocketType.equals("unused") && out
          .getCopyOfInSocketId
          .equals(in.getInSocketId))

        val unusedSocketId = if (unusedSocket.size > 0) unusedSocket(0).getSocketId else ""

        val keyFields = {
          if (operationEntity.isInstanceOf[JoinEntity])
            operationEntity.asInstanceOf[JoinEntity].getKeyFields.asScala.filter(key => key.getInSocketId.equals(in.getInSocketId))(0)
          else
            operationEntity.asInstanceOf[LookupEntity].getKeyFields.asScala.filter(key => key.getInSocketId.equals(in.getInSocketId))(0)
        }

        val outSocket = operationEntity.getOutSocketList.asScala.filter(out => out.getSocketType.equals("out"))

        val outSocketId = if (outSocket.size > 0) outSocket(0).getSocketId else ""

        val dataFrame = baseComponentParams.getDataFrameMap().getOrElse(in.getFromComponentId, sys.error("input data " +
          "frame should be present"))

        joinHelperArray(i) = JoinOperation(in.getFromComponentId, in.getInSocketId, dataFrame, keyFields.getFields, unusedSocket.size > 0, keyFields
          .isRecordRequired, outSocketId, unusedSocketId)

    }
    joinHelperArray
  }

  def getCopyOfInSocketFields(): List[(String, String)] = {
    val copyOfInSocketFields = ListBuffer[(String, String)]()
    val outSocket = operationEntity.getOutSocketList.asScala.filter(out => out.getSocketType.equals("out"))(0)
    val copyOfInSocketId = outSocket.getCopyOfInSocketId
    if (copyOfInSocketId != null) {
      val inSocket = operationEntity.getInSocketList.filter { soc => soc.getInSocketId.equals(copyOfInSocketId) }(0)
      val fromCompId = inSocket.getFromComponentId
      val tempMap = baseComponentParams.getSchemaFieldMap()
      val fieldsList = tempMap(fromCompId)
      fieldsList.foreach { f => copyOfInSocketFields += ((inSocket.getInSocketId + "_" + f.getFieldName, f.getFieldName)) }
    }
    copyOfInSocketFields.toList
  }

  def getPassthroughFields(): List[(String, String)] = {
    val passthroughFields = List[(String, String)]()
    val outSocket = operationEntity.getOutSocketList.asScala.filter(out => out.getSocketType.equals("out"))(0)
    val inSocket = operationEntity.getInSocketList

    def populatePassthroughFields(outList: List[(String, String)], passthroughFieldsList: java.util.List[hydrograph.engine.core.component.entity.elements.PassThroughField]): List[(String, String)] = {
      if (passthroughFieldsList.isEmpty()) outList
      else if (passthroughFieldsList.head.getName.equals("*")) {
        val fromCompId = inSocket.filter { soc => soc.getInSocketId.equals(passthroughFieldsList.head.getInSocketId) }(0).getFromComponentId
        val tempMap = baseComponentParams.getSchemaFieldMap()

        val expandedList = tempMap(fromCompId).map(f => new PassThroughField(f.getFieldName, passthroughFieldsList.head.getInSocketId))

        populatePassthroughFields(outList, expandedList.toList ++ passthroughFieldsList.tail)
      } else populatePassthroughFields(outList :+ (passthroughFieldsList.head.getInSocketId + "_" + passthroughFieldsList.head.getName, passthroughFieldsList.head.getName), passthroughFieldsList.tail)
    }
    populatePassthroughFields(passthroughFields, outSocket.getPassThroughFieldsList).distinct
  }

  def getMapFields(): List[(String, String)] = {
    val mapFields = List[(String, String)]()
    val outSocket = operationEntity.getOutSocketList.asScala.filter(out => out.getSocketType.equals("out"))(0)

    def populateMapFields(outList: List[(String, String)], mapFieldsList: java.util.List[hydrograph.engine.core.component.entity.elements.MapField]): List[(String, String)] = {
      if (mapFieldsList.isEmpty()) outList
      else populateMapFields(outList :+ (mapFieldsList.head.getInSocketId + "_" + mapFieldsList.head.getSourceName, mapFieldsList.head.getName), mapFieldsList.tail)
    }

    populateMapFields(mapFields, outSocket.getMapFieldsList).distinct
  }

}
