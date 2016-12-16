package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.elements.JoinKeyFields
import hydrograph.engine.core.component.entity.{ JoinEntity, TransformEntity }
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.{ ReusableRow, TransformBase }
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{ Column, DataFrame, Row }

import scala.collection.mutable

/**
 * Created by gurdits on 10/18/2016.
 *
 */

class SparkJoinComponent(joinEntity: JoinEntity, componentsParams: BaseComponentParams) extends OperationComponentBase with Serializable {

  override def createComponent(): Map[String, DataFrame] = {

    val joinUtils = new JoinUtils(joinEntity, componentsParams)
    val joinOperations = joinUtils.prepareJoinOperation()

    val passthroughFields = joinUtils.getPassthroughFields()
    val mapFields = joinUtils.getMapFields()

    val joinOperationsSorted = joinOperations.sortBy(j => (j.recordRequired, !j.unused)).reverse

    join(joinOperationsSorted, passthroughFields, mapFields)
  }

  def join(joinOperations: Array[JoinOperation], passthroughFields: List[(String, String)], mapFields: List[(String, String)]): Map[String, DataFrame] = {
    var outMap = Map[String, DataFrame]()

    def dfJoin(headJoin: JoinOperation, tailJoins: Array[JoinOperation]): JoinOperation = {

      if (tailJoins.isEmpty) {
        getJoinOperationOfOutRecords(headJoin)
      } else if ((headJoin.recordRequired == false) && (tailJoins.head.recordRequired == false)) {
        dfJoin(fullOuterJoin(headJoin, tailJoins.head), tailJoins.tail)
      } else if ((headJoin.recordRequired == true) && (tailJoins.head.recordRequired == false) && (tailJoins.head.unused == false)) {
        dfJoin(leftOuterJoin(getJoinOperationOfOutRecords(headJoin), tailJoins.head), tailJoins.tail)
      } else if ((headJoin.recordRequired == true) && (tailJoins.head.recordRequired == false) && (tailJoins.head.unused == true)) {
        dfJoin(leftOuterJoinForUnused(getJoinOperationOfOutRecords(headJoin), tailJoins.head), tailJoins.tail)
      } else if ((headJoin.recordRequired == true) && (headJoin.unused == false) && (tailJoins.head.recordRequired == true) && (tailJoins.head.unused == false)) {
        dfJoin(innerJoin(headJoin, tailJoins.head), tailJoins.tail)
      } else {
        dfJoin(innerJoinForUnused(headJoin, tailJoins.head), tailJoins.tail)
      }

    }

    def innerJoin(lhs: JoinOperation, rhs: JoinOperation): JoinOperation = {
      val rhsModified = getJoinOpWithPrefixAdded(rhs, rhs.inSocketId)

      val lhsDF = lhs.dataFrame
      val rhsDF = rhsModified.dataFrame
      val lhsKeys = lhs.keyFields
      val rhsKeys = rhsModified.keyFields

      val joinedDF = lhsDF.join(rhsDF, createJoinKey(lhsKeys, rhsKeys), "inner")

      JoinOperation("join", "in", joinedDF, lhsKeys, false, true, lhs.outSocketId, "")
    }

    def leftOuterJoin(lhs: JoinOperation, rhs: JoinOperation): JoinOperation = {
      val rhsModified = getJoinOpWithPrefixAdded(rhs, rhs.inSocketId)

      val lhsDF = lhs.dataFrame
      val rhsDF = rhsModified.dataFrame
      val lhsKeys = lhs.keyFields
      val rhsKeys = rhsModified.keyFields

      val joinedDF = lhsDF.join(rhsDF, createJoinKey(lhsKeys, rhsKeys), "leftouter")

      JoinOperation("join", "in", joinedDF, lhsKeys, false, true, lhs.outSocketId, "")
    }

    def fullOuterJoin(lhs: JoinOperation, rhs: JoinOperation): JoinOperation = {
      val rhsModified = getJoinOpWithPrefixAdded(rhs, rhs.inSocketId)

      val lhsDF = lhs.dataFrame
      val rhsDF = rhsModified.dataFrame
      val lhsKeys = lhs.keyFields
      val rhsKeys = rhsModified.keyFields

      val joinedDF = lhsDF.join(rhsDF, createJoinKey(lhsKeys, rhsKeys), "outer")

      val blankDF = joinedDF.filter("false")
      val blankDF_lhs = blankDF.select(convertStructFieldsTOString(lhsDF.schema): _*)
      val blankDF_rhs = blankDF.select(convertStructFieldsTOString(rhsDF.schema): _*)

      val blankDF_lhs_prefixRemoved = blankDF_lhs.select(blankDF_lhs.columns.map(c => col(c).as(c.replaceFirst(lhs.inSocketId + "_", ""))): _*)
      val blankDF_rhs_prefixRemoved = blankDF_rhs.select(blankDF_rhs.columns.map(c => col(c).as(c.replaceFirst(rhs.inSocketId + "_", ""))): _*)

      if (lhs.unused) (outMap += (lhs.unusedSocketId -> blankDF_lhs_prefixRemoved))
      if (rhs.unused) (outMap += (rhs.unusedSocketId -> blankDF_rhs_prefixRemoved))

      JoinOperation("join", "in", joinedDF, lhsKeys, false, false, lhs.outSocketId, "")
    }

    def innerJoinForUnused(lhs: JoinOperation, rhs: JoinOperation): JoinOperation = {
      val rhsModified = getJoinOpWithPrefixAdded(rhs, rhs.inSocketId)

      val lhsDF = if (lhs.dataFrame.columns.contains("required")) (lhs.dataFrame.withColumnRenamed("required", "input1")) else (lhs.dataFrame.withColumn("input1", lit(1)))
      val rhsDF = rhsModified.dataFrame
      val lhsKeys = lhs.keyFields
      val rhsKeys = rhsModified.keyFields

      val joinedDF = lhsDF.join(rhsDF.withColumn("input2", lit(1)), createJoinKey(lhsKeys, rhsKeys), "outer")
      val joinedDF1 = joinedDF.withColumn("required", when((col("input1") === 1) && (col("input2") === 1), 1).otherwise(null))
      val joinedDF2 = joinedDF1.drop("input1", "input2")

      JoinOperation("join", "in", joinedDF2, lhsKeys, false, true, lhs.outSocketId, "")

    }

    def leftOuterJoinForUnused(lhs: JoinOperation, rhs: JoinOperation): JoinOperation = {
      val rhsModified = getJoinOpWithPrefixAdded(rhs, rhs.inSocketId)

      val lhsDF = lhs.dataFrame
      val rhsDF = rhsModified.dataFrame
      val lhsKeys = lhs.keyFields
      val rhsKeys = rhsModified.keyFields

      val joinedDF = lhsDF.withColumn("input1", lit(1)).join(rhsDF.withColumn("input2", lit(1)), createJoinKey(lhsKeys, rhsKeys), "outer")

      val unusedDF_rhs = joinedDF.filter("(input1 is null) and (input2 == 1)").select(convertStructFieldsTOString(rhsDF.schema): _*)

      val unusedDF_rhs_prefixRemoved = unusedDF_rhs.select(unusedDF_rhs.columns.map(c => col(c).as(c.replaceFirst(rhs.inSocketId + "_", ""))): _*)

      val outputDF = joinedDF.filter("(input1 == 1)").select(convertStructFieldsTOString(lhsDF.schema) ++ convertStructFieldsTOString(rhsDF.schema): _*)

      if (rhs.unused) (outMap += (rhs.unusedSocketId -> unusedDF_rhs_prefixRemoved))

      JoinOperation("join", "in", outputDF, lhsKeys, false, true, lhs.outSocketId, "")
    }

    def getJoinOpWithPrefixAdded(joinOp: JoinOperation, prefix: String): JoinOperation = {
      val originalDF = joinOp.dataFrame
      val modifiedDF = originalDF.select(originalDF.columns.map { c => col(c).as(prefix + "_" + c) }: _*)

      val originalKeys = joinOp.keyFields
      val modifiedKeys = originalKeys.map { colName => prefix + "_" + colName }

      JoinOperation(joinOp.compID, joinOp.inSocketId, modifiedDF, modifiedKeys, joinOp.unused, joinOp.recordRequired, joinOp.outSocketId, joinOp.unusedSocketId)
    }

    def getDFWithRequiredFields(df: DataFrame): DataFrame = {

      val mergedFields = (passthroughFields ++ mapFields).distinct
      df.select(mergedFields.map(field => col(field._1).as(field._2)): _*)

    }

    def getJoinOperationOfOutRecords(joinOp: JoinOperation): JoinOperation = {

      val requiredUnusedInputs = joinOperations.filter { j => ((j.recordRequired == true) && (j.unused == true)) }
      if ((requiredUnusedInputs.size > 0) && (joinOp.dataFrame.columns.contains("required"))) {

        val unusedDF = joinOp.dataFrame.filter("(required is null)")

        requiredUnusedInputs.foreach { j =>
          {
            outMap += (j.unusedSocketId -> unusedDF.select(j.dataFrame.columns.map(c => col(j.inSocketId + "_" + c).as(c)): _*).na.drop("all"))
          }
        }

        val outDF = joinOp.dataFrame.filter("(required == 1)").drop("required")
        JoinOperation(joinOp.compID, joinOp.inSocketId, outDF, joinOp.keyFields, joinOp.unused, joinOp.recordRequired, joinOp.outSocketId, joinOp.unusedSocketId)

      } else {
        if (joinOp.unused) {
          outMap += (joinOp.unusedSocketId -> joinOp.dataFrame.filter("false").select(joinOp.dataFrame.columns.map(c => col(c).as(c.replaceFirst(joinOp.inSocketId + "_", ""))): _*))
          joinOp
        } else
          joinOp
      }
    }

    val headJoinOp = getJoinOpWithPrefixAdded(joinOperations.head, joinOperations.head.inSocketId)

    val outputJoin = dfJoin(headJoinOp, joinOperations.tail)

    val outputResultDF = getDFWithRequiredFields(outputJoin.dataFrame)

    outMap += (joinOperations.head.outSocketId -> outputResultDF)

    outMap
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

}
