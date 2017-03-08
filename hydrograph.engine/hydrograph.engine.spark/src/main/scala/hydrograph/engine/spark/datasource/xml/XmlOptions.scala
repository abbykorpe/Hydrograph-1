/*
 * Copyright 2014 Databricks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hydrograph.engine.spark.datasource.xml

import java.text.SimpleDateFormat

import hydrograph.engine.spark.datasource.xml.util.ParseModes
import org.slf4j.LoggerFactory

/**
  * The Class XmlOptions.
  *
  * @author Bitwise
  *
  */
private[xml] class XmlOptions(
                               @transient private val parameters: Map[String, String])
  extends Serializable{
  private val logger = LoggerFactory.getLogger(XmlRelation.getClass)

  val componentId=parameters("componentId")
  var dateFormat=XmlOptions.DEFAULT_DATE_FORMAT
  val charset = parameters.getOrElse("charset", XmlOptions.DEFAULT_CHARSET)
  val codec = parameters.get("compression").orElse(parameters.get("codec")).orNull
  val rowTag = parameters.getOrElse("rowTag", XmlOptions.DEFAULT_ROW_TAG)
  val rootTag = parameters.getOrElse("rootTag", XmlOptions.DEFAULT_ROOT_TAG)
  val samplingRatio = parameters.get("samplingRatio").map(_.toDouble).getOrElse(1.0)
  val excludeAttributeFlag = parameters.get("excludeAttribute").map(_.toBoolean).getOrElse(false)
  val treatEmptyValuesAsNulls =
    parameters.get("treatEmptyValuesAsNulls").map(_.toBoolean).getOrElse(false)
  val attributePrefix =
    parameters.getOrElse("attributePrefix", XmlOptions.DEFAULT_ATTRIBUTE_PREFIX)
  val valueTag = parameters.getOrElse("valueTag", XmlOptions.DEFAULT_VALUE_TAG)
  val nullValue = parameters.getOrElse("nullValue", XmlOptions.DEFAULT_NULL_VALUE)
  val columnNameOfCorruptRecord =
    parameters.getOrElse("columnNameOfCorruptRecord", "_corrupt_record")

  // Leave this option for backwards compatibility.
  private val failFastFlag = parameters.get("failFast").map(_.toBoolean).getOrElse(false)
  private val parseMode = if (failFastFlag) {
    parameters.getOrElse("mode", ParseModes.FAIL_FAST_MODE)
  } else {
    parameters.getOrElse("mode", ParseModes.PERMISSIVE_MODE)
  }



  var dateFormatMap=getDateFormats(parameters.get("dateFormats").getOrElse({
    logger.debug("No date formats provided. XmlOption.dateFormatMap map created with default 'yyyy-MM-dd' format")
    "yyyy-MM-dd"
  }),"\t")


  def getDateFormats(formats: String,delimiter:String):Map[String, SimpleDateFormat] = {
    val dateFormats=formats.split(delimiter).filter(format=>format!="null")

    dateFormats.map(format=>(format,new SimpleDateFormat(format))).toMap

  }

  // Parse mode flags
  if (!ParseModes.isValidMode(parseMode)) {
    logger.warn(s"$parseMode is not a valid parse mode. Using ${ParseModes.DEFAULT}.")
  }

  val failFast = ParseModes.isFailFastMode(parseMode)
  val dropMalformed = ParseModes.isDropMalformedMode(parseMode)
  val permissive = ParseModes.isPermissiveMode(parseMode)

  //If parse mode is FAILFAST then set parsing of dates as strict. By default parsing of date is Lenient
  //  if(failFast)
  dateFormatMap.foreach(date=>date._2.setLenient(false))

  require(rowTag.nonEmpty, "'rowTag' option should not be empty string.")
  require(attributePrefix.nonEmpty, "'attributePrefix' option should not be empty string.")
  require(valueTag.nonEmpty, "'valueTag' option should not be empty string.")
}

private[xml] object XmlOptions {
  val DEFAULT_ATTRIBUTE_PREFIX = "_"
  val DEFAULT_VALUE_TAG = "_VALUE"
  val DEFAULT_ROW_TAG = "ROW"
  val DEFAULT_ROOT_TAG = "ROWS"
  val DEFAULT_CHARSET = "UTF-8"
  val DEFAULT_NULL_VALUE = null
  val DEFAULT_DATE_FORMAT="yyyy-MM-dd"

  def apply(parameters: Map[String, String]): XmlOptions = new XmlOptions(parameters)
}
