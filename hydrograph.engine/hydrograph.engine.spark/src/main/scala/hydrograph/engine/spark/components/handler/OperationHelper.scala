package hydrograph.engine.spark.components.handler

import scala.collection.JavaConverters.asScalaBufferConverter
import org.apache.spark.sql.types.StructType
import hydrograph.engine.core.component.entity.elements.Operation
import hydrograph.engine.spark.core.reusablerow.RowToReusableMapper
import java.util.ArrayList

case class SparkOperation[T](baseClassInstance: T, operationEntity: Operation, inputMapper: RowToReusableMapper, outputMapper: RowToReusableMapper)

trait OperationHelper[T] {

  def initializeTransformList(operationList: java.util.List[Operation], inputSchema: StructType, outputSchema: StructType): List[SparkOperation[T]] = {

    def transform(operationList: List[Operation]): List[SparkOperation[T]] = (operationList) match {
      case (List()) => List()
      case (x :: xs) =>

        val transformBase: T = classLoader[T](x.getOperationClass)

        SparkOperation[T](transformBase, x, new RowToReusableMapper(inputSchema, x.getOperationInputFields), new RowToReusableMapper(outputSchema, x.getOperationOutputFields)) ::
          transform(xs)
    }
    if (operationList != null)
      transform(operationList.asScala.toList)
    else
      List()
  }

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }

  implicit def arrayToList(arr: Array[String]): ArrayList[String] = {
    val lst = new ArrayList[String]()
    arr.foreach(v => lst.add(v))
    lst
  }

}

