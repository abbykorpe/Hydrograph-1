package hydrograph.engine.spark.test.customtransformclasses

import java.util
import java.util.Properties

import hydrograph.engine.transformation.userfunctions.base.{ReusableRow, TransformBase}

/**
  * Created by sandeepv on 1/11/2017.
  */
class TransformTest_RenameCity extends TransformBase{
  override def prepare(props: Properties, inputFields: util.ArrayList[String], outputFields: util.ArrayList[String]): Unit = {
  }

  override def transform(inputRow: ReusableRow, outputRow: ReusableRow): Unit = {
    outputRow.setField("other_city", inputRow.getString("city").trim)
  }

  override def cleanup(): Unit = {
  }
}
