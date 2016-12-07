package hydrograph.engine.spark.components

import java.util
import java.util.Properties
import scala.collection.JavaConversions._
import hydrograph.engine.core.component.entity.{ NormalizeEntity, TransformEntity }
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.{ OutputDispatcher, NormalizeTransformBase, ReusableRow, TransformBase }
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{ Column, DataFrame, Row }

import scala.collection.mutable.ListBuffer

/**
 * Created by gurdits on 10/18/2016.
 */
class SparkNormalizeComponent(normalizeEntity: NormalizeEntity, componentsParams: BaseComponentParams) extends OperationComponentBase with Serializable {

  override def createComponent(): Map[String, DataFrame] = {

    val op = OperationSchemaCreator[NormalizeEntity](normalizeEntity, componentsParams, normalizeEntity.getOutSocketList().get(0))

    val fm = FieldManupulating(op.getOperationInputFields(), op.getOperationOutputFields(), op.getPassThroughFields(), op.getMapFields(), op.getOperationFields(), null)

    val outRow = new Array[Any](fm.getOutputFields().size)

    val inputColumn = new Array[Column](fm.getinputFields().size)
    fm.getinputFields().zipWithIndex.foreach(f => {
      inputColumn(f._2) = col(f._1)
    })

    var outputDispatcher: NormalizeOutputCollector = null

    val df = componentsParams.getDataFrame.select(inputColumn: _*).mapPartitions(itr => {

      val opr = normalizeEntity.getOperationsList()(0)
      val props: Properties = opr.getOperationProperties
      val outRR = ReusableRowHelper(opr, fm).convertToOutputReusableRow()
      outputDispatcher = new NormalizeOutputCollector(outRR, outRow, fm)
      val normalizeTransformBase: NormalizeTransformBase = classLoader[NormalizeTransformBase](opr.getOperationClass)

      normalizeTransformBase.prepare(props)

      val it = itr.flatMap(row => {

        outputDispatcher.initialize
        //Map Fields
        RowHelper.setTupleFromRow(outRow, fm.determineMapSourceFieldsPos(), row, fm.determineMapTargetFieldsPos())
        //Passthrough Fields
        RowHelper.setTupleFromRow(outRow, fm.determineInputPassThroughFieldsPos(), row, fm.determineOutputPassThroughFieldsPos())

        normalizeTransformBase.Normalize(RowHelper.convertToReusebleRow(ReusableRowHelper(opr, fm).determineInputFieldPositions(), row, ReusableRowHelper(opr, fm).convertToInputReusableRow()), outRR, outputDispatcher)

        if(itr.isEmpty) 
          normalizeTransformBase.cleanup()
          
        outputDispatcher.getOutRows
      })

      it
    })(RowEncoder(EncoderHelper().getEncoder(fm.getOutputFields(), componentsParams.getSchemaFields())))

    val key = normalizeEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)
  }

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors();
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }

}

class NormalizeOutputCollector(outputReusableRow: ReusableRow, outRow: Array[Any], fieldManupulating: FieldManupulating) extends OutputDispatcher {

  private val list = new ListBuffer[Row]()
  override def sendOutput(): Unit = {

    RowHelper.setTupleFromReusableRow(outRow, outputReusableRow, fieldManupulating
      .determineOutputFieldPositions()(0))

    val clonedRow = outRow.clone()
    list += Row.fromSeq(clonedRow)
    
  }

    def initialize: Unit = {

    list.clear()
  }
  
  def getOutRows: ListBuffer[Row] = {

    list
  }
}
