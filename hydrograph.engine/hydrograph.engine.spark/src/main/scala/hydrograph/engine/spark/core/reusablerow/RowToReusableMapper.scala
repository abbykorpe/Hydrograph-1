package hydrograph.engine.spark.core.reusablerow

import org.apache.spark.sql.types.StructType
import java.util.LinkedHashSet
import scala.collection.immutable.HashMap

class RowToReusableMapper(allFields: StructType, requiredFields: Array[String]) {

  val requiredFieldsSet: LinkedHashSet[String] = {
    val arr = new LinkedHashSet[String]()
    requiredFields.foreach { str => arr.add(str) }
    arr
  }

  val fieldIndexList: Array[Int] = requiredFields.map { x => allFields.fieldIndex(x) }

  val fieldIndexMap: Map[String, Int] = requiredFields.map { x => (x, allFields.fieldIndex(x)) }.toMap
}