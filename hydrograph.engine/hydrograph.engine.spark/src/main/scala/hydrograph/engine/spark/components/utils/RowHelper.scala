package hydrograph.engine.spark.components.utils

import hydrograph.engine.transformation.userfunctions.base.ReusableRow
import org.apache.spark.sql.Row

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
  * Created by gurdits on 10/11/2016.
  */
object RowHelper {

  def extractPassthroughFields(passthroughFieldsPos: Array[Int], row: Row): Array[Any] = {
    val passthroughFields = new Array[Any](passthroughFieldsPos.length)
    passthroughFieldsPos.zipWithIndex.foreach { case (p, i) =>
      passthroughFields(i) = row.get(p)
    }

    passthroughFields
  }

  def extractMapFields(mapFieldsPos: Array[Int], row: Row): Array[Any] = {
    val mapFields = new Array[Any](mapFieldsPos.length)
    mapFieldsPos.zipWithIndex.foreach { case (p, i) =>
      mapFields(i) = row.get(p)
    }
    mapFields
  }


  def convertToReusebleRow(fieldsPos: ListBuffer[Int], row: Row, reusableRow: ReusableRow): ReusableRow = {
    if (fieldsPos == null || row == null || reusableRow == null)
      reusableRow

    fieldsPos.zipWithIndex.foreach { case (f, i) =>
      reusableRow.setField(i, row.get(f).asInstanceOf[Comparable[ReusableRow]])
    }
    reusableRow
  }


  def setTupleFromRow(outRow: Array[Any], sourcePos: ListBuffer[Int], row: Row, targetPos: ListBuffer[Int]): Array[Any] = {
    sourcePos.zipWithIndex.foreach { case (v, i) =>
      outRow(targetPos(i)) = row.get(v)
    }
    outRow
  }

  def setTupleFromReusableRow(outRow: Array[Any], reusableRow: ReusableRow, sourcePos: ListBuffer[Int]): Unit = {

    if (reusableRow == null)
      outRow

    reusableRow.getFields.zipWithIndex.foreach(x => {
      if (sourcePos(x._2) != -1)
        outRow(sourcePos(x._2)) = x._1
    })
    reusableRow.reset()
  }


  def convetToRow(outRR: ReusableRow, mapValues: Array[Any], passthroughValues: Array[Any]): Row = {
    val operationValues = new Array[Any](outRR.getFields.size())
    val it = outRR.getFields.iterator()
    var c = 0;
    while (it.hasNext) {
      operationValues(c) = it.next()
      c += 1
    }

    Row.fromSeq(operationValues)
    //     Row.fromSeq(Array(operationValues,mapValues,passthroughValues).flatMap(x=>x))
  }


}
