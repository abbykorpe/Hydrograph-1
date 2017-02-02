package hydrograph.engine.spark.core.reusablerow

import java.util.LinkedHashSet

import hydrograph.engine.transformation.userfunctions.base.ReusableRow
import org.apache.spark.sql.Row

class InputReusableRow(var inputRow: Row, fieldsIndexMap: Map[String, Int], fieldsIndexList: Array[Int], fields: LinkedHashSet[String])
    extends ReusableRow(fields) with Serializable {

  def setRow(row: Row): InputReusableRow = { inputRow = row; this }
  def getFieldInternal(index: Int) = inputRow.get(fieldsIndexList(index)).asInstanceOf[Comparable[_]]
  def getFieldInternal(field: String) = inputRow.get(fieldsIndexMap(field)).asInstanceOf[Comparable[_]]
  def setFieldInternal(index: Int, value: Comparable[_]) = throw new UnsupportedOperationException("Set methods are not supported on spark input reusable row")
  def setFieldInternal(field: String, value: Comparable[_]) = throw new UnsupportedOperationException("Set methods are not supported on spark input reusable row")

}

object InputReusableRow {

  def apply(inputRow: Row, mapper: RowToReusableMapper): InputReusableRow = new InputReusableRow(inputRow, mapper.fieldIndexMap, mapper.fieldIndexList, mapper.requiredFieldsSet)
}