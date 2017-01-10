package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.OutputFileSequenceFormatEntity
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by gurdits on 12/28/2016.
  */
class OutputSequenceFileComponent(outputSequenceEntity: OutputFileSequenceFormatEntity, baseComponentParams:
BaseComponentParams) extends SparkFlow {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[OutputFileDelimitedComponent])

  override def execute(): Unit = {
    try {
      LOG.info("Created Output File Delimited Component " + outputSequenceEntity.getComponentId
        + " in Batch " + outputSequenceEntity.getBatch)
      baseComponentParams.getDataFrame().rdd.saveAsObjectFile(outputSequenceEntity.getPath);
    } catch {
      case
        e: Exception =>
        LOG.error("Error in Output File Delimited Component " + outputSequenceEntity.getComponentId, e)
        throw new RuntimeException("Error in Output File Delimited Component "
          + outputSequenceEntity.getComponentId, e)
    }
  }
}
