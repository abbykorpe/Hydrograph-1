package hydrograph.engine.spark.components

import org.apache.spark.sql.DataFrame
import org.slf4j.LoggerFactory

import hydrograph.engine.core.component.entity.LimitEntity
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
 * @author ankitsh
 *
 */
class SparkLimitComponent(limitEntity : LimitEntity,componentsParams : BaseComponentParams) extends StraightPullComponentBase with Serializable{

	val logger = LoggerFactory.getLogger(classOf[SparkLimitComponent])

	/*
	 * This method is used for creating limit component
	 * with a given no. of records to be fetched in xml
	 */
	override def createComponent(): Map[String, DataFrame] = {
    try {
      if (logger.isTraceEnabled()) {
        logger.trace(limitEntity.toString());
      }
      val output = componentsParams.getDataFrame().limit(limitEntity.getMaxRecord().toInt)
      val key = limitEntity.getOutSocketList.get(0).getSocketId()
      
      logger.trace("Creating limit assembly for '"
        + limitEntity.getComponentId() + "' for socket: '"
        + key + "' of type: '"
        + limitEntity.getOutSocketList.get(0).getSocketType + "'");
      
      Map(key -> output)
    } catch {
      case e: RuntimeException => logger.error(e.getMessage, e); throw e
    }
  }
}
