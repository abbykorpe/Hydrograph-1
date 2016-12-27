package hydrograph.engine.spark.components

import java.util
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.{ ReusableRow, TransformBase }
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{ Column, DataFrame, Row }
import scala.collection.mutable
import hydrograph.engine.core.component.entity.LookupEntity
import scala.collection.JavaConverters._

/**
 * Created by gurdits on 10/18/2016.
 *
 */

class LookupComponent(lookupEntity: LookupEntity, componentsParams: BaseComponentParams) extends OperationComponentBase with Serializable {

  override def createComponent(): Map[String, DataFrame] = {

    val joinUtils = new JoinUtils(lookupEntity, componentsParams)
    val joinOperations = joinUtils.prepareJoinOperation().map { joinOp => getJoinOpWithPrefixAdded(joinOp, joinOp.inSocketId) }

    val passthroughFields = joinUtils.getPassthroughFields()
    val mapFields = joinUtils.getMapFields()
    val copyOfInSocketFields = joinUtils.getCopyOfInSocketFields()
    val matchType = lookupEntity.getMatch

    def getDFWithRequiredFields(df: DataFrame): DataFrame = {
      val mergedFields = (passthroughFields ++ mapFields ++ copyOfInSocketFields).distinct
      df.select(mergedFields.map(field => col(field._1).as(field._2)): _*)
    }

    val driverInSocketId = lookupEntity.getInSocketList.asScala.filter { in => (in.getInSocketType == "driver") }(0).getInSocketId
    val lookupInSocketId = lookupEntity.getInSocketList.asScala.filter { in => (in.getInSocketType == "lookup") }(0).getInSocketId

    val driverJoinOp = joinOperations.filter { j => (j.inSocketId == driverInSocketId) }(0)
    val lookupJoinOp = joinOperations.filter { j => (j.inSocketId == lookupInSocketId) }(0)

    val broadcastDF = broadcast({
      if (matchType == "all")
        lookupJoinOp.dataFrame
      else {
        val lookupKeyFields = lookupJoinOp.keyFields.map { str => col(str) }
        val lookupOtherFields = lookupJoinOp.dataFrame.columns
          .filter { str => !lookupJoinOp.keyFields.contains(str) }
          .map { str => if (matchType == "first") (first(str).as(str)) else (last(str).as(str)) }

        lookupJoinOp.dataFrame.groupBy(lookupKeyFields: _*).agg(lookupOtherFields.head, lookupOtherFields.tail: _*)
      }
    })

    val outputDF = getDFWithRequiredFields(driverJoinOp.dataFrame.join(broadcastDF, createJoinKey(driverJoinOp.keyFields, lookupJoinOp.keyFields), "leftouter"))

    val key = driverJoinOp.outSocketId

    Map(key -> outputDF)
  }

  def convertStructFieldsTOString(structType: StructType): Array[Column] = {
    val inputColumns = new Array[Column](structType.length)
    structType.zipWithIndex.foreach {
      case (sf, i) =>
        inputColumns(i) = col(sf.name)
    }
    inputColumns
  }

  def createJoinKey(lhsKeys: Array[String], rhsKeys: Array[String]): Column = (lhsKeys, rhsKeys) match {
    case (l, r) if l.length != r.length => sys.error("key fields should be same")
    case (l, r) if r.tail.length == 0   => return col(l.head) === col(r.head)
    case (l, r)                         => return (col(l.head) === col(r.head)).&&(createJoinKey(l.tail, r.tail))
  }

  def getJoinOpWithPrefixAdded(joinOp: JoinOperation, prefix: String): JoinOperation = {
    val originalDF = joinOp.dataFrame
    val modifiedDF = originalDF.select(originalDF.columns.map { c => col(c).as(prefix + "_" + c) }: _*)

    val originalKeys = joinOp.keyFields
    val modifiedKeys = originalKeys.map { colName => prefix + "_" + colName }

    JoinOperation(joinOp.compID, joinOp.inSocketId, modifiedDF, modifiedKeys, joinOp.unused, joinOp.recordRequired, joinOp.outSocketId, joinOp.unusedSocketId)
  }

}
