package hydrograph.engine.spark.core.reusablerow

import org.apache.spark.sql.types.StructType
import java.util.LinkedHashSet
import scala.collection.immutable.HashMap

class RowToReusableMapper(allFields: StructType, requiredFields: Array[String]) extends Serializable {

  val requiredFieldsSet: LinkedHashSet[String] = {
    val arr = new LinkedHashSet[String]()
    requiredFields.foreach { str => arr.add(str) }
    arr
  }

  val fieldIndexList: Array[Int] = requiredFields.map { x => val index = allFields.fieldIndex(x); index }

  val fieldIndexMap: Map[String, Int] = requiredFields.map { x => val index = allFields.fieldIndex(x); (x, index) }.toMap

  val anythingToMap: Boolean = requiredFields.size > 0
}