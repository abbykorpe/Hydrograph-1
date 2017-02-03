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

package hydrograph.engine.spark.components.utils

import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by santlal and amiya on 1/14/2017.
  */
case class DbUpdateTableDescriptor(tableName: String, columnNames: Array[String], updateKeys: Array[String]) {

  val LOG: Logger = LoggerFactory.getLogger(classOf[DbUpdateTableDescriptor])
  var setColumn: List[String] = Nil
  var whereColumn: List[String] = Nil

  def makeUpdateQuery(): String = {
    if (validateUpdateKeys(columnNames, updateKeys)) {
      "update " + tableName + " set " + setColumnBody.mkString(", ") + " where " + setWhereColumnBody.mkString(" and ")
    } else {
      " "
    }
  }

  def setWhereColumnBody(): List[String] = updateKeys.map(field => field + "=?").toList

  def setColumnBody(): List[String] = columnNames.filter(field => !updateKeys.contains(field)).map(field => field + "=?").toList

  def validateUpdateKeys(columnNames: Array[String], updateKeys: Array[String]): Boolean = {
    for (i <- 0 until updateKeys.length) {
      if (!columnNames.contains(updateKeys(i))) {
        LOG.error("Update key '" + updateKeys(i) + "' does not exist in user provided schema")
        throw UpdateKeyFieldNotExistInUserSpecifiedColumnField("Exception : Update key '"
          + updateKeys(i) + "' does not exist in user provided schema ")
        false
      }
    }
    true
  }
}

case class UpdateKeyFieldNotExistInUserSpecifiedColumnField(message: String = "", cause: Throwable = null) extends Exception(message, cause)
