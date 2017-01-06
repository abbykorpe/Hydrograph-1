package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.{ExecutionTrackingEntity, FilterEntity}
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.DataFrame
import org.apache.spark.util.LongAccumulator
import org.slf4j.LoggerFactory

/**
  * Created by vaijnathp on 12/12/2016.
  */
class ExecutionTrackingComponent(filterEntity: ExecutionTrackingEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with Serializable {
  val LOG = LoggerFactory.getLogger(classOf[ExecutionTrackingComponent])
  override def createComponent(): Map[String, DataFrame] = {

    if (filterEntity.getOperation.getOperationInputFields==null) {
      LOG.error("Filter Operation Input Fields Can not be Empty")
      throw new Exception("Operation Input Fields are Empty Exception ")
    }
    val key = filterEntity.getOutSocketList.get(0).getSocketId
     val fieldNameSet = new util.LinkedHashSet[String]()
    filterEntity.getOperation.getOperationInputFields.foreach(e => fieldNameSet.add(e))
    val df = componentsParams.getDataFrame()
    val longAccumulator: LongAccumulator = componentsParams.getSparkSession().sparkContext.longAccumulator(filterEntity.getComponentId)
    df.foreach(row => longAccumulator.add(1))
    Map(key -> df)
  }


}
