package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.PartitionByExpressionEntity
import hydrograph.engine.core.component.entity.elements.OutSocket
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{ReusableRowHelper, RowHelper, SparkReusableRow}
import hydrograph.engine.transformation.userfunctions.base.{CustomPartitionExpression, FilterBase}
import org.apache.spark.sql.DataFrame
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
  * Created by santlalg on 12/15/2016.
  */
class PartitionByExpressionComponent(partitionByExpressionEntity:PartitionByExpressionEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase   with Serializable {

  private val LOG:Logger = LoggerFactory.getLogger(classOf[PartitionByExpressionComponent])

  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")
    val scheme = componentsParams.getDataFrame.schema.map(e => e.name)

    LOG.info("Created PartitionByExpression Component '"+ partitionByExpressionEntity.getComponentId
      + "' in Batch "+ partitionByExpressionEntity.getBatch
      + " with output socket [" +  getOutSocketId(partitionByExpressionEntity.getOutSocketList.asScala.toList).mkString(",")
      + "] operation [operationClass : " + partitionByExpressionEntity.getOperationsList.get(0).getOperationClass
      + ", operationInputField : " + partitionByExpressionEntity.getOperationsList.get(0).getOperationInputFields.toList.mkString
      + "]")

    LOG.debug("Component id '" + partitionByExpressionEntity.getComponentId
      + "' in Batch " + partitionByExpressionEntity.getBatch
      + " with output socket [" +  getOutSocketId(partitionByExpressionEntity.getOutSocketList.asScala.toList).mkString(",")
      + "] operation [operationClass : " + partitionByExpressionEntity.getOperationsList.get(0).getOperationClass
      + ", operationInputField : " + partitionByExpressionEntity.getOperationsList.get(0).getOperationInputFields.toList.mkString
      + "] ")


    val fieldNameSet = new util.LinkedHashSet[String]()
    scheme.foreach(e => fieldNameSet.add(e))
    var map: Map[String, DataFrame] = Map()

    LOG.trace("Component id '" + partitionByExpressionEntity.getComponentId
      + "' scheme " + fieldNameSet.asScala.toList.mkString(",")
    )

    val partitionClass = classLoader[CustomPartitionExpression](partitionByExpressionEntity.getOperation.getOperationClass)
    val fieldPosition = ReusableRowHelper(partitionByExpressionEntity.getOperation, null).determineInputFieldPositionsForFilter(scheme)
    val inputReusableRow = new SparkReusableRow(fieldNameSet)

    LOG.trace("Operation input Field " + partitionByExpressionEntity.getOperationsList.get(0).getOperationInputFields.toList.mkString
      + " having field position [" + fieldPosition.mkString + "]")

    partitionByExpressionEntity.getOutSocketList.asScala.foreach { outSocket =>

      val df = componentsParams.getDataFrame().filter(row =>
        partitionClass.getPartition(RowHelper.convertToReusebleRow(fieldPosition, row, inputReusableRow), partitionByExpressionEntity.getNumPartitions.toInt).equals(outSocket.getSocketId)
      )
      map += (outSocket.getSocketId -> df)
    }
    map
  }

  def getOutSocketId(toList: List[OutSocket]): List[String] = {
    var outSocketId : List[String] = Nil

    toList.foreach(e=>outSocketId=e.getSocketId :: outSocketId)

    outSocketId
  }

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }

}
