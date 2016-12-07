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

/**
 * Created by gurdits on 9/26/2016.
 */

case class JoinOperation(compID: String, inSocketId: String, dataFrame: DataFrame, keyFields: Array[String], unused: Boolean, recordRequired: Boolean, outSocketId: String, unusedSocketId: String)

class JoinUtils(joinEntity: JoinEntity, baseComponentParams: BaseComponentParams) {

  def prepareJoinOperation(): Array[JoinOperation] = {

    val joinHelperArray = new Array[JoinOperation](joinEntity.getInSocketList.size())

    joinEntity.getInSocketList().asScala.zipWithIndex.foreach {
      case (in, i) =>
        val unusedSocket = joinEntity.getOutSocketList.asScala.filter(out => out.getSocketType.equals("unused") && out
          .getCopyOfInSocketId
          .equals(in.getInSocketId))

        val unusedSocketId = if (unusedSocket.size > 0) unusedSocket(0).getSocketId else ""

        val keyFields = joinEntity.getKeyFields.asScala.filter(key => key.getInSocketId.equals(in.getInSocketId))(0)

        val outSocket = joinEntity.getOutSocketList.asScala.filter(out => out.getSocketType.equals("out"))

        val outSocketId = if (outSocket.size > 0) outSocket(0).getSocketId else ""

        val dataFrame = baseComponentParams.getDataFrameMap().getOrElse(in.getFromComponentId, sys.error("input data " +
          "frame should be present"))

        joinHelperArray(i) = JoinOperation(in.getFromComponentId, in.getInSocketId, dataFrame, keyFields.getFields, unusedSocket.size > 0, keyFields
          .isRecordRequired, outSocketId, unusedSocketId)

    }
    joinHelperArray
  }

  def getPassthroughFields(): List[(String, String)] = {
    val passthroughFields = List[(String, String)]()
    val outSocket = joinEntity.getOutSocketList.asScala.filter(out => out.getSocketType.equals("out"))(0)
    val inSocket = joinEntity.getInSocketList

    def populatePassthroughFields(outList: List[(String, String)], passthroughFieldsList: java.util.List[hydrograph.engine.core.component.entity.elements.PassThroughField]): List[(String, String)] = {
      if (passthroughFieldsList.isEmpty()) outList
      else if (passthroughFieldsList.head.getName.equals("*")) {
        val fromCompId = inSocket.filter { soc => soc.getInSocketId.equals(passthroughFieldsList.head.getInSocketId) }(0).getFromComponentId
        val tempMap = baseComponentParams.getDataFrameMap()

        val expandedList = tempMap(fromCompId).schema.fieldNames.map ( fn => new PassThroughField(fn, passthroughFieldsList.head.getInSocketId) )

        populatePassthroughFields(outList, expandedList.toList ++ passthroughFieldsList.tail)
      } else populatePassthroughFields(outList :+ (passthroughFieldsList.head.getInSocketId + "_" + passthroughFieldsList.head.getName, passthroughFieldsList.head.getName), passthroughFieldsList.tail)
    }
    populatePassthroughFields(passthroughFields, outSocket.getPassThroughFieldsList).distinct
  }

  def getMapFields(): List[(String, String)] = {
    val mapFields = List[(String, String)]()
    val outSocket = joinEntity.getOutSocketList.asScala.filter(out => out.getSocketType.equals("out"))(0)

    def populateMapFields(outList: List[(String, String)], mapFieldsList: java.util.List[hydrograph.engine.core.component.entity.elements.MapField]): List[(String, String)] = {
      if (mapFieldsList.isEmpty()) outList
      else populateMapFields(outList :+ (mapFieldsList.head.getInSocketId + "_" + mapFieldsList.head.getSourceName, mapFieldsList.head.getName), mapFieldsList.tail)
    }

    populateMapFields(mapFields, outSocket.getMapFieldsList).distinct
  }

}
