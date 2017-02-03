/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
