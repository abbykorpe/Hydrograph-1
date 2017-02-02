package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.LimitEntity
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.DataFrame
import org.slf4j.LoggerFactory

class SparkLimitComponent(limitEntity : LimitEntity,componentsParams : BaseComponentParams) extends StraightPullComponentBase with Serializable{

	val logger = LoggerFactory.getLogger(classOf[SparkLimitComponent])

	override def createComponent(): Map[String, DataFrame] = {
    try {
      logger.trace(limitEntity.toString());
      val output = componentsParams.getDataFrame().limit(limitEntity.getMaxRecord().toInt)
      val key = limitEntity.getOutSocketList.get(0).getSocketId()

      logger.info("Created limit component '"
        + limitEntity.getComponentId() + "'with records: '" + limitEntity.getMaxRecord().toInt)
      logger.debug("Executing limit component for socketId " + key + " of type: '" + limitEntity.getOutSocketList.get(0).getSocketType + "'");

      Map(key -> output)
      
    } catch {
      case e: RuntimeException => logger.error("Error in Limit Component : " + limitEntity.getComponentId() + "\n" + e.getMessage, e); throw e
    }
  }
}
