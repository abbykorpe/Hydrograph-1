package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.ExecutionTrackingEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.EncoderHelper
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.util.LongAccumulator

/**
  * Created by vaijnathp on 12/12/2016.
  */
class ExecutionTrackingComponent(executionTrackingEntity: ExecutionTrackingEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with Serializable {
  override def createComponent(): Map[String, DataFrame] = {
    val key = executionTrackingEntity.getOutSocketList.get(0).getSocketId
     val fieldNameSet = new util.LinkedHashSet[String]()
    executionTrackingEntity.getOperation.getOperationInputFields.foreach(e => fieldNameSet.add(e))
    val df = componentsParams.getDataFrame()
    val longAccumulator: LongAccumulator = componentsParams.getAccumulator()

    longAccumulator.add(1)

    val dataFrame=df.map(row=> {longAccumulator.add(1)
  row
}) (RowEncoder(df.schema))

//      dataFrame.foreach(row => {longAccumulator.add(1)})

    /*val dataFrame=df.filter(row => {longAccumulator.add(1)
      true})*/
    //dataFrame.foreach(r => println("******" + r))

    Map(key -> dataFrame)
  }


}
