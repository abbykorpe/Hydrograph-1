/**
 * *****************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************
 */
package hydrograph.engine.spark.components

import org.apache.spark.sql.Column
import org.apache.spark.sql.functions._
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import hydrograph.engine.core.component.entity.SortEntity
import hydrograph.engine.core.component.entity.elements.KeyField
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.DataFrame

class SparkSortComponent(sortEntity: SortEntity, componentsParams: BaseComponentParams)
    extends StraightPullComponentBase {

  val logger = LoggerFactory.getLogger(classOf[SparkSortComponent])

  override def createComponent(): Map[String, DataFrame] = {

    try {
      if (logger.isTraceEnabled()) {
        logger.trace(sortEntity.toString());
      }
      logger.trace("Creating sort component for '" + sortEntity.getComponentId());

      val key = sortEntity.getOutSocketList.get(0).getSocketId

      val dataFrame = componentsParams.getDataFrame()

      val keysList = sortEntity.getKeyFields
      
      if(keysList!=null && logger.isDebugEnabled())
      keysList.foreach { field => logger.debug("Key fields for sort component: '" + sortEntity.getComponentId() + "':  "
							+field.getName) }
      
      val sortedDF = dataFrame.sort(populateSortKeys(keysList): _*)
      Map(key -> sortedDF)

    } catch {
      case ex: RuntimeException => logger.error(ex.getMessage); throw ex
    }
  }

  /**
   * Creates an array of type {@link Column} from array of {@link KeyField}
   *
   * @param keysArray
   *            an array of {@link KeyField} containing the field name and
   *            sort order
   * @return an an array of {@link Column}
   */
  def populateSortKeys(keysArray: Array[KeyField]): Array[Column] = {
    keysArray.map { field => if (field.getSortOrder.toLowerCase() == "desc") (col(field.getName).desc) else (col(field.getName)) }
  }

}