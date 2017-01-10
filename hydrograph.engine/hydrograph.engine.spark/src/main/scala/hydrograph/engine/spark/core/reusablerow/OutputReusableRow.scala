package hydrograph.engine.spark.core.reusablerow

import java.util.LinkedHashSet

import hydrograph.engine.transformation.userfunctions.base.ReusableRow

class OutputReusableRow(var outputRow: Array[Any], fieldsIndexMap: Map[String, Int], fieldsIndexList: Array[Int], fields: LinkedHashSet[String])
    extends ReusableRow(fields) with Serializable {

  def setRow(row: Array[Any]): OutputReusableRow = { outputRow = row; this }
  def getFieldInternal(index: Int) = outputRow(fieldsIndexList(index)).asInstanceOf[Comparable[_]]
  def getFieldInternal(field: String) = outputRow(fieldsIndexMap(field)).asInstanceOf[Comparable[_]]
  def setFieldInternal(index: Int, value: Comparable[_]) = { outputRow(fieldsIndexList(index)) = value }
  def setFieldInternal(field: String, value: Comparable[_]) = { outputRow(fieldsIndexMap(field)) = value }

}

object OutputReusableRow {

  def apply(outputRow: Array[Any], mapper: RowToReusableMapper): OutputReusableRow = new OutputReusableRow(outputRow, mapper.fieldIndexMap, mapper.fieldIndexList, mapper.requiredFieldsSet)
}