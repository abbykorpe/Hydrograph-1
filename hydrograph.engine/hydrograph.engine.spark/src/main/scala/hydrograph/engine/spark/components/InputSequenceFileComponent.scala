package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.InputFileSequenceFormatEntity
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaCreator
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by gurdits on 12/28/2016.
  */
class InputSequenceFileComponent(iSequenceEntity: InputFileSequenceFormatEntity, iComponentsParams: BaseComponentParams) extends
  InputComponentBase {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[InputSequenceFileComponent])

  override def createComponent(): Map[String, DataFrame] = {

    try {
      val key = iSequenceEntity.getOutSocketList.get(0).getSocketId
      LOG.trace("Creating input file sequence format assembly for '"
        + iSequenceEntity.getComponentId() + "' for socket: '" + key)
      val schemaField = SchemaCreator(iSequenceEntity).makeSchema()
      val inputRdd: RDD[Row] = iComponentsParams.sparkSession.sparkContext.objectFile(iSequenceEntity.getPath)
      val df = iComponentsParams.sparkSession.createDataFrame(inputRdd, schemaField)
      Map(key -> df)
    }
    catch {
      case e:Exception =>
        LOG.error("Error in Input Sequence File Component "+ iSequenceEntity.getComponentId, e)
        throw new RuntimeException("Error in Input Sequence File Component "+ iSequenceEntity.getComponentId, e)
    }
  }
}
