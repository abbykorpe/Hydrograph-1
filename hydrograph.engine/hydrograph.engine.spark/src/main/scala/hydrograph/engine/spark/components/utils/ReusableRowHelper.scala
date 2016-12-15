package hydrograph.engine.spark.components.utils

import java.util

import hydrograph.engine.transformation.userfunctions.base.ReusableRow

import scala.collection.mutable.ListBuffer
import hydrograph.engine.core.component.entity.elements.Operation

/**
 * Created by gurdits on 10/20/2016.
 */
class ReusableRowHelper(opr: Operation, fm: FieldManupulating) {
  def determineInputFieldPositionsForFilter(scheme: Seq[String]): ListBuffer[Int] ={
    val inputPos = new ListBuffer[Int]()
    scheme.zipWithIndex.foreach(e=>
      if(opr.getOperationInputFields.contains(e._1))
        inputPos += e._2
    )
    inputPos
  }

  def convertToInputReusableRow(): ReusableRow = {
    val arr = new util.LinkedHashSet[String]()

    opr.getOperationInputFields.foreach { str => arr.add(str) }
    val reusableRow = new SparkReusableRow(arr)

    reusableRow
  }

  def convertToReusableRow(list:List[String]): ReusableRow = {
    val arr = new util.LinkedHashSet[String]()
    list.foreach { str => arr.add(str) }
    val reusableRow = new SparkReusableRow(arr)
    reusableRow
  }

  def convertToOutputReusableRow(): ReusableRow = {

    val arr = new util.LinkedHashSet[String]()
    opr.getOperationOutputFields.foreach { str => arr.add(str) }
    val reusableRow = new SparkReusableRow(arr)

    reusableRow
  }

  def determineInputFieldPositions(): ListBuffer[Int] = {
    val inputPos = new ListBuffer[Int]()
    if (opr.getOperationInputFields != null)
      opr.getOperationInputFields.foreach { l =>
        fm.getinputFields().zipWithIndex.foreach(v => {
          if (l.equals(v._1))
            inputPos += v._2
        })
      }
    inputPos
  }

  def determineOutputFieldPositions(): ListBuffer[Int] = {
    val outputPos = new ListBuffer[Int]()
    if (opr.getOperationOutputFields != null)
      opr.getOperationOutputFields.foreach { l =>
        fm.getOutputFields().zipWithIndex.foreach(v => {
          if (l.equals(v._1))
            outputPos += v._2
        })
      }
    outputPos
  }

}
object ReusableRowHelper {
  def apply(opr: Operation, fm: FieldManupulating): ReusableRowHelper = {
    new ReusableRowHelper(opr, fm)
  }
}