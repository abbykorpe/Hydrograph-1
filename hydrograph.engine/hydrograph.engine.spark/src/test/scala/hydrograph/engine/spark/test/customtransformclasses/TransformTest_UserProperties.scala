package hydrograph.engine.spark.test.customtransformclasses

import java.util
import java.util.Properties
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

import hydrograph.engine.transformation.userfunctions.base.{ReusableRow, TransformBase}

/**
  * Created by sandeepv on 1/11/2017.
  */
class TransformTest_UserProperties extends TransformBase{
  private[customtransformclasses] var load_id: Int = 0

  override def prepare(props: Properties, inputFields: util.ArrayList[String], outputFields: util.ArrayList[String]): Unit = {
    load_id = props.getProperty("LOAD_ID").toInt
  }

  override def transform(inputRow: ReusableRow, outputRow: ReusableRow): Unit = {
    outputRow.setField("name", inputRow.getString("name").trim)
    outputRow.setField("load_id", load_id)
  }

  override def cleanup(): Unit = {


  }
}
