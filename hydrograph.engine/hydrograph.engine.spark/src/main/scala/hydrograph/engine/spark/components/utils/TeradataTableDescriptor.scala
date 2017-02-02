/** *****************************************************************************
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
  * *****************************************************************************/

package hydrograph.engine.spark.components.utils

/**
  * Created by AniruddhaS on 1/6/2017.
  */

import org.slf4j.{Logger, LoggerFactory}


class TeradataTableDescriptor(tableName: String,
                              columnNames: Array[String],
                              columnDefs: Array[String],
                              primaryKeys: Array[String],
                              databaseType: String) extends Serializable {
  val LOG: Logger = LoggerFactory.getLogger(classOf[TeradataTableDescriptor])

  var createTableStatement: List[String] = Nil
  var teradataCreateTableStatement = Nil
  var field: String = ""
  var primaryIndex: String = ""

  def getCreateTableStatement(): String = {
    createTableStatement = addCreateTableBody()
    val createTableStatment = String.format("CREATE TABLE %s ( %s )",
      tableName,
      joinField(createTableStatement.reverse, ",")) + addPrimaryKey()




    LOG.info("Create query '" + createTableStatment + "' for " + databaseType + " output component")
    createTableStatment
  }

  def addCreateTableBody(): List[String] = {
    createTableStatement = addDefinitions();

    createTableStatement;
  }



  def addDefinitions():List[String] = {
    for(i <- 0 until columnNames.length) {
      if(hasUniquePrimaryIndex()){
        /**Checking conditions for suffixing NOT NULL with the primary key fields*/
        if(primaryKeys.toList.contains(columnNames(i))){
          createTableStatement = (quoteIdentifier(columnNames(i)) + " " + columnDefs(i) + "  NOT NULL") :: createTableStatement

        }else createTableStatement = (quoteIdentifier(columnNames(i))+" "+ columnDefs(i)) :: createTableStatement
      } else {
        /**
          * to create a table in absence of a primary key
          * */
        createTableStatement = (quoteIdentifier(columnNames(i))+" "+ columnDefs(i)) :: createTableStatement
      }
    }
    createTableStatement
  }
  def quoteIdentifier(colName: String): String = {
    s""""$colName""""
  }

  /**
    * PRIMARY KEY IN TERADATA IS ACTUALLY A UNIQUE PRIMARY INDEX WITH NON ACCEPTANCE  OF
    * A NULL VALUE
    **/
  def addPrimaryKey(): String = {

    if (hasUniquePrimaryIndex)
      primaryIndex = String.format(" UNIQUE PRIMARY INDEX( %s )", joinField(primaryKeys.toList, ","))

    primaryIndex
  }

  private def hasUniquePrimaryIndex(): Boolean = primaryKeys != null && primaryKeys.length != 0

  def joinField(createTableStatement: List[String], s: String): String = {
    field = ""
    for (in <- 0 until createTableStatement.length) {
      if (in != 0) field = field + s
      if (createTableStatement(in) != null) field = field + createTableStatement(in)
    }
    field
  }
}
