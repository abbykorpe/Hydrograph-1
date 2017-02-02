package hydrograph.engine.spark.components.utils

import hydrograph.engine.core.component.entity.elements.KeyField

import scala.collection.mutable.ListBuffer

/**
 * Created by gurdits on 10/19/2016.
 */
class FieldManupulating(operationInputFields: ListBuffer[ListBuffer[String]], operationOutputFields: ListBuffer[ListBuffer[String]], passthrougFields: ListBuffer[String], mapFields: ListBuffer[(String, String)], operationFields: ListBuffer[String], keyFields: Array[KeyField]) extends Serializable {

  val mapSourceFields = new ListBuffer[String]()
  val mapTargetFields = new ListBuffer[String]()
  val inputField = new ListBuffer[String]()
  val outputOperationField = new ListBuffer[String]()
  val inputPosition = new ListBuffer[Array[Int]]()
  val opInputFields = operationInputFields
  val opOutputFields = operationOutputFields
  var outputField = new ListBuffer[String]()

  def extractMapSourceAndTarget(): Unit = {
    /*mapFields.keySet.foreach(key => mapSourceFields += key)
    mapFields.values.foreach(value => mapTargetFields += value)*/
    mapFields.foreach(field => {
      mapSourceFields += field._1; mapTargetFields += field._2
    })
  }

  def determineIO(): Unit = {

    outputField = outputField.++:(operationFields.distinct)

    if (operationInputFields != null && operationInputFields.size > 0)
      operationInputFields.foreach(l => l.foreach(f => {
        if (!inputField.contains(f)) inputField += f
      }))

    if (operationOutputFields != null && operationOutputFields.size > 0)
      operationOutputFields.foreach(l => l.foreach(f => {
        if (!outputOperationField.contains(f)) outputOperationField += f
      }))

    if (mapSourceFields != null)
      mapSourceFields.foreach(f => {
        if (!inputField.contains(f)) inputField += f
      })

    if (mapTargetFields != null)
      mapTargetFields.foreach(f => {
        if (!outputField.contains(f)) outputField += f
      })

    if (passthrougFields != null)
      passthrougFields.foreach(f => {
        if (!outputField.contains(f)) outputField += f
        if (!inputField.contains(f)) inputField += f
      })

  }

  def getinputFields(): ListBuffer[String] = {
    inputField
  }

  def getOperationOutputFields(): ListBuffer[String] = {
    outputOperationField
  }

  def getOutputFields(): ListBuffer[String] = {
    outputField
  }

  def getPos(inputFields: ListBuffer[String], fields: ListBuffer[String]): Unit = {
    val inputPos = new ListBuffer[Int]()
    fields.foreach(f => {
      inputFields.zipWithIndex.foreach(value => {
        //        if(f.equals(value._1))
        //          inputPos.
        value
      })
    })
  }

  def determineOutputFieldPositions(): ListBuffer[ListBuffer[Int]] = {
    val inputPos = new ListBuffer[ListBuffer[Int]]()
    if (operationInputFields != null)
      operationOutputFields.foreach(f => {
        val pos = new ListBuffer[Int]()
        f.foreach(l => {
          outputField.zipWithIndex.foreach(v => {
            if (l.equals(v._1))
              pos += v._2
            /*            else
                          pos += -1*/
          })
        })
        inputPos += pos
      })
    inputPos
  }

  def determineInputFieldPositions(): ListBuffer[ListBuffer[Int]] = {
    val inputPos = new ListBuffer[ListBuffer[Int]]()
    if (operationInputFields != null)
      operationInputFields.foreach(f => {
        val pos = new ListBuffer[Int]()
        f.foreach(l => {
          inputField.zipWithIndex.foreach(v => {
            if (l.equals(v._1))
              pos += v._2
          })
        })
        inputPos += pos
      })
    inputPos
  }

  def determineInputPassThroughFieldsPos(): ListBuffer[Int] = {
    val inputPos = new ListBuffer[Int]()
    passthrougFields.foreach(v => {
      inputField.zipWithIndex.foreach(f => {
        if (v.equals(f._1))
          inputPos += f._2
      })
    })
    inputPos
  };

  def determineOutputPassThroughFieldsPos(): ListBuffer[Int] = {
    val inputPos = new ListBuffer[Int]()
    passthrougFields.foreach(v => {
      outputField.zipWithIndex.foreach(f => {
        if (v.equals(f._1))
          inputPos += f._2
      })
    })
    inputPos
  }

  def determineMapSourceFieldsPos(): ListBuffer[Int] = {
    val inputPos = new ListBuffer[Int]()
    mapSourceFields.foreach(v => {
      inputField.zipWithIndex.foreach(f => {
        if (f._1.equals(v))
          inputPos += f._2
      })
    })
    inputPos
  }

  def determineMapTargetFieldsPos(): ListBuffer[Int] = {
    val inputPos = new ListBuffer[Int]()
    mapTargetFields.foreach(v => {
      outputField.zipWithIndex.foreach(f => {
        if (f._1.equals(v))
          inputPos += f._2
      })
    })
    inputPos
  }

  def determineKeyFieldPos(): ListBuffer[Int] = {
    val inputPos = new ListBuffer[Int]()

    if (keyFields != null) {
      keyFields.foreach(k => {
        inputField.zipWithIndex.foreach(f => {
          if (f._1.equals(k.getName))
            inputPos += f._2
        })
      })
    }

    inputPos
  }

  def getKeyFields(): ListBuffer[String] = {
    val keyFieldList = new ListBuffer[String]()
    if (keyFields != null)
      keyFields.foreach(k => keyFieldList += k.getName)
    keyFieldList
  }

}

object FieldManupulating {

  def apply(operationInputFields: ListBuffer[ListBuffer[String]], operationOutputFields: ListBuffer[ListBuffer[String]], passthrougFields: ListBuffer[String], mapFields: ListBuffer[(String, String)], operationFields: ListBuffer[String], keyFields: Array[KeyField]): FieldManupulating = {
    val fm = new FieldManupulating(operationInputFields, operationOutputFields, passthrougFields, mapFields, operationFields, keyFields)
    fm.extractMapSourceAndTarget()
    fm.determineIO()

    fm
  }
}
