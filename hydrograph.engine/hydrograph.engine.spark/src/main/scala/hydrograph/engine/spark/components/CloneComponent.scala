package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.CloneEntity
import hydrograph.engine.core.component.entity.elements.OutSocket
import hydrograph.engine.core.custom.exceptions.FieldNotFoundException
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.DataFrame
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

/**
  * The Class CloneComponent.
  *
  * @author Bitwise
  *
  */
class CloneComponent(cloneEntity: CloneEntity, componentsParams: BaseComponentParams)
  extends
    StraightPullComponentBase {

  val LOG = LoggerFactory.getLogger(classOf[CloneComponent])

  override def createComponent(): Map[String, DataFrame] = {
    try {
      LOG.trace(cloneEntity.toString)
      LOG.info("Created Clone Component "+ cloneEntity.getComponentId
        + " in Batch "+ cloneEntity.getBatch )
      def generateDataFrame(outSocketList:List[OutSocket]):Map[String,DataFrame] = outSocketList match {
        case List() => Map()
        case x::xs => Map(x.getSocketId->componentsParams.getDataFrame()) ++ generateDataFrame(xs)
      }
      generateDataFrame(cloneEntity.getOutSocketList.toList)
    } catch {
      case e: Exception =>
        LOG.error("\nException in Clone Component - \nComponent Id:[\"" + cloneEntity.getComponentId + "\"]" +
          "\nComponent Name:[\"" + cloneEntity.getComponentName + "\"]\nBatch:[\"" + cloneEntity.getBatch + "\"]" + e.getMessage(), e)
        throw new RuntimeException(
        "\nException in Clone Component - \nComponent Id:[\"" + cloneEntity.getComponentId + "\"]" +
          "\nComponent Name:[\"" + cloneEntity.getComponentName + "\"]\nBatch:[\"" + cloneEntity.getBatch + "\"]" + e.getMessage(), e)
    }
  }

}

