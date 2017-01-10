
package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.RemoveDupsEntity
import hydrograph.engine.core.component.entity.elements.KeyField
import hydrograph.engine.core.constants.Keep
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{EncoderHelper, RowHelper}
import org.apache.spark.sql.{Column, DataFrame, Row}
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{BooleanType, StructField}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.mutable.ListBuffer

class RemoveDupsComponent(removeDupsEntity: RemoveDupsEntity, componentsParams: BaseComponentParams)
    extends StraightPullComponentBase {
  val logger = LoggerFactory.getLogger(classOf[RemoveDupsComponent])

  override def createComponent(): Map[String, DataFrame] = {

    try {
      logger.trace(removeDupsEntity.toString());
      val fm = RemoveDupsFieldManupulating(removeDupsEntity, componentsParams)
      val prevOutRow = new Array[Any](fm.getOutputFields().size + 2)
      var firstRowFlag: Boolean = true
      var prevKeysArray: Array[Any] = null
      var map: Map[String, DataFrame] = Map()

      val flag1Pos = fm.getOutputFields().size
      val flag2Pos = fm.getOutputFields().size + 1

      val inputColumn = new Array[Column](fm.getinputFields().size)
      fm.getinputFields().zipWithIndex.foreach(f => {
        inputColumn(f._2) = col(f._1)
      })

      val keep = removeDupsEntity.getKeep
      val isUnusedRequired = removeDupsEntity.getOutSocketList.asScala.filter(p => p.getSocketType.equals("unused")).size > 0
      val primaryKeys = if (removeDupsEntity.getKeyFields == null) (Array[KeyField]()) else (removeDupsEntity.getKeyFields)
      val secondaryKeys = if (removeDupsEntity.getSecondaryKeyFields == null) (Array[KeyField]()) else (removeDupsEntity.getSecondaryKeyFields)
      val sourceDf = componentsParams.getDataFrame().select(inputColumn: _*)
      val repartitionedDf = if (primaryKeys.isEmpty) (sourceDf.repartition(1)) else (sourceDf.repartition(primaryKeys.map { field => col(field.getName) }: _*))
      val sortedDf = repartitionedDf.sortWithinPartitions(populateSortKeys(primaryKeys ++ secondaryKeys): _*)
      val intermediateDf = sortedDf.mapPartitions(itr => {

        itr.flatMap { row =>
          {
            val currKeysArray: Array[Any] = RowHelper.extractKeyFields(row, fm.determineKeyFieldPos())
            val isPrevKeyDifferent = {
              if (prevKeysArray == null)
                (true)
              else if (!(prevKeysArray.zip(currKeysArray).forall(p => p._1.equals(p._2))))
                (true)
              else (false)
            }
            val isItrEmpty = itr.isEmpty
            prevOutRow(flag1Pos) = if (prevKeysArray == null) null else if (!isPrevKeyDifferent) (false) else (true)

            // to find first row
            prevOutRow(flag2Pos) = if (prevKeysArray == null) null else firstRowFlag
            var tempOutRow = prevOutRow.clone
            prevKeysArray = currKeysArray

            //Assign Fields
            RowHelper.setTupleFromRow(prevOutRow, fm.determineFieldsPos(), row, fm.determineFieldsPos())

            firstRowFlag = if (!isPrevKeyDifferent) (false) else (true)

            if (itr.isEmpty) {
              prevOutRow(flag1Pos) = if (prevKeysArray == null) null else if (!isPrevKeyDifferent) (false) else (true)
              // to find first row
              prevOutRow(flag2Pos) = if (prevKeysArray == null) null else firstRowFlag
              Iterator(Row.fromSeq(tempOutRow), Row.fromSeq(prevOutRow))
            } else
              Iterator(Row.fromSeq(tempOutRow))
          }
        }
      })(RowEncoder(EncoderHelper().getEncoder(fm.getOutputFields(), componentsParams.getSchemaFields()).add(StructField("flag1", BooleanType, true)).add(StructField("flag2", BooleanType, true))))

      val outputDf = {
        if (keep == Keep.first)
          (intermediateDf.filter("flag2 == true")).drop("flag1", "flag2")
        else if (keep == Keep.last)
          (intermediateDf.filter("flag1 == true")).drop("flag1", "flag2")
        else
          (intermediateDf.filter("flag1 == true AND flag2 == true")).drop("flag1", "flag2")
      }
      val outKey = removeDupsEntity.getOutSocketList.asScala.filter(p => p.getSocketType.equals("out"))(0).getSocketId
      map += (outKey -> outputDf)

      if (isUnusedRequired) {
        val unusedDf = {
          if (keep == Keep.first)
            (intermediateDf.filter("flag2 == false")).drop("flag1", "flag2")
          else if (keep == Keep.last)
            (intermediateDf.filter("flag1 == false")).drop("flag1", "flag2")
          else
            (intermediateDf.filter("flag2 == false OR flag1 == false")).drop("flag1", "flag2")
        }

        val unusedKey = removeDupsEntity.getOutSocketList.asScala.filter(p => p.getSocketType.equals("unused"))(0).getSocketId
        map += (unusedKey -> unusedDf)
      }
      map
    } catch {
      case e: RuntimeException => logger.error("Error in RemoveDups Component : " + removeDupsEntity.getComponentId() + "\n" + e.getMessage, e); throw e
    }
  }

  def populateSortKeys(keysArray: Array[KeyField]): Array[Column] = {
    keysArray.map { field => if (field.getSortOrder.toLowerCase() == "desc") (col(field.getName).desc) else (col(field.getName)) }
  }
}

class RemoveDupsFieldManupulating(removeDupsEntity: RemoveDupsEntity, componentsParams: BaseComponentParams) extends Serializable {

  val inputFields = ListBuffer() ++ componentsParams.getSchemaFields().map(x => x.getFieldName)
  val keyFields = removeDupsEntity.getKeyFields

  def getinputFields(): ListBuffer[String] = {
    inputFields
  }

  def getOutputFields(): ListBuffer[String] = {
    inputFields
  }

  def determineFieldsPos(): ListBuffer[Int] = {
    val inputPos = new ListBuffer[Int]()
    inputFields.foreach(v => {
      inputFields.zipWithIndex.foreach(f => {
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
        inputFields.zipWithIndex.foreach(f => {
          if (f._1.equals(k.getName))
            inputPos += f._2
        })
      })
    }
    inputPos
  }
}

object RemoveDupsFieldManupulating {

  def apply(removeDupsEntity: RemoveDupsEntity, componentsParams: BaseComponentParams): RemoveDupsFieldManupulating = {
    val fm = new RemoveDupsFieldManupulating(removeDupsEntity, componentsParams)
    fm
  }
}
    
 