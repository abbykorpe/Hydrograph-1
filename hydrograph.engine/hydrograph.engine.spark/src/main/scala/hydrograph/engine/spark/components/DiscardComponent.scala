package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.DiscardEntity
import hydrograph.engine.core.custom.exceptions.UserFunctionClassNotFoundException
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.slf4j.LoggerFactory

/**
  * The Class DiscardComponent.
  *
  * @author Bitwise
  *
  */
class DiscardComponent(discardEntity: DiscardEntity, componentsParams: BaseComponentParams) extends SparkFlow
{
  val LOG = LoggerFactory.getLogger(classOf[DiscardComponent])

  override def execute(): Unit = {
    try {
      LOG.trace(discardEntity.toString)
      LOG.info("Created Discard Component " + discardEntity.getComponentId
        + " in Batch " + discardEntity.getBatch)
      componentsParams.getDataFrame().count()
    } catch {
      case e: Exception => throw new RuntimeException("\nException in Discard Component - \nComponent Id:[\""
        + discardEntity.getComponentId + "\"]" + "\nComponent Name:[\"" + discardEntity.getComponentName + "\"]\nBatch:[\""
        + discardEntity.getBatch + "\"]" + e.getMessage(), e)
    }
  }
}
