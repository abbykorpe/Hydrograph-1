package hydrograph.engine.spark.core.reusablerow

import org.apache.spark.sql.Row
import java.util.LinkedHashSet
import hydrograph.engine.transformation.userfunctions.base.ReusableRow

class OutputReusableRow(outputRow: Array[Any], fieldsIndexMap: Map[String, Int], fieldsIndexList: Array[Int], fields: LinkedHashSet[String])
    extends ReusableRow(fields) {

  def getFieldInternal(index: Int) = throw new UnsupportedOperationException("get methods are not supported on spark output reusable row")
  def getFieldInternal(field: String) = throw new UnsupportedOperationException("get methods are not supported on spark output reusable row")
  def setFieldInternal(index: Int, value: Comparable[_]) = { outputRow(fieldsIndexList(index)) = value }
  def setFieldInternal(field: String, value: Comparable[_]) = { outputRow(fieldsIndexMap(field)) = value }

}

object OutputReusableRow {

  def apply(outputRow: Array[Any], mapper: RowToReusableMapper): OutputReusableRow = new OutputReusableRow(outputRow, mapper.fieldIndexMap, mapper.fieldIndexList, mapper.requiredFieldsSet)
}