package hydrograph.engine.spark.components.base

import java.util

import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.core.helper.LinkGenerator
import hydrograph.engine.jaxb.commontypes.{TypeBaseInSocket, TypeBaseOutSocket}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.flow.RuntimeContext
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Created by gurdits on 10/21/2016.
  */
object ComponentParameterBuilder {

  def apply(componentID: String, runtimeContext: RuntimeContext, outLinkMap: mutable.HashMap[String, Map[String,DataFrame]],
            baseComponent:
  BaseComponentParams): Builder = {
    new Builder(componentID, runtimeContext, outLinkMap, baseComponent)
  }

  class Builder(componentID: String, runtimeContext: RuntimeContext, outLinkMap: mutable.HashMap[String, Map[String,DataFrame]], baseComponent: BaseComponentParams) {

    def setInputDataFrame(): Builder = {
      val linkGenerator = new LinkGenerator(runtimeContext.hydrographJob.getJAXBObject)

      val inSocketList: util.List[_ <: TypeBaseInSocket] = linkGenerator.getLink().get(componentID).getInSocket

      for (inSocket: TypeBaseInSocket <- inSocketList.asScala) {
        val fromCompID = inSocket.getFromComponentId
        baseComponent.addinputDataFrame(outLinkMap.get(fromCompID).get.get(inSocket.getFromSocketId
          ).get)
      }
      this
    }


    def setInputDataFrameWithCompID(): Builder = {
      val linkGenerator = new LinkGenerator(runtimeContext.hydrographJob.getJAXBObject)

      val inSocketList: util.List[_ <: TypeBaseInSocket] = linkGenerator.getLink().get(componentID).getInSocket

      for (inSocket: TypeBaseInSocket <- inSocketList.asScala) {
        val fromCompID = inSocket.getFromComponentId
        baseComponent.addCompIDAndInputDataFrame(fromCompID,outLinkMap.get(fromCompID).get.get(inSocket.getFromSocketId
        ).get)
      }
      this
    }

    def setSchemaFields(): Builder = {
      val linkGenerator = new LinkGenerator(runtimeContext.hydrographJob.getJAXBObject)

      val inSocketList: util.List[_ <: TypeBaseInSocket] = linkGenerator.getLink().get(componentID).getInSocket
      val outSocketList: util.List[_ <: TypeBaseOutSocket] = linkGenerator.getLink().get(componentID).getOutSocket

      for (outSocket: TypeBaseOutSocket <- outSocketList.asScala) {
        val schemaFieldList = runtimeContext.schemaFieldHandler.getSchemaFieldMap.get(componentID + "_" + outSocket.getId)
        baseComponent.addSchemaFields(schemaFieldList.toArray[SchemaField](new Array[SchemaField](schemaFieldList.size())))
      }
      this
    }


    def setSparkSession(sparkSession: SparkSession): Builder = {
      baseComponent.setSparkSession(sparkSession)
      this
    }


    def build(): BaseComponentParams = {
      baseComponent
    }
  }


}
