package hydrograph.engine.spark.components

import java.util
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.collection.mutable.ListBuffer
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.annotation.Experimental
import org.apache.spark.sql.Column
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.StructType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import hydrograph.engine.core.component.entity.NormalizeEntity
import hydrograph.engine.core.component.entity.elements.Operation
import hydrograph.engine.core.component.utils.OperationUtils
import hydrograph.engine.expression.api.ValidationAPI
import hydrograph.engine.expression.userfunctions.{AggregateForExpression, NormalizeForExpression}
import hydrograph.engine.expression.utils.ExpressionWrapper
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.{OperationHelper, SparkOperation}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.EncoderHelper
import hydrograph.engine.spark.components.utils.FieldManupulating
import hydrograph.engine.spark.components.utils.OperationSchemaCreator
import hydrograph.engine.transformation.userfunctions.base.{AggregateTransformBase, NormalizeTransformBase, OutputDispatcher, ReusableRow}
import oracle.jdbc.driver.OutRawAccessor

/**
  * Created by bitwise on 10/18/2016.
  */
class NormalizeComponent(normalizeEntity: NormalizeEntity, componentsParams: BaseComponentParams) 
 extends OperationComponentBase with Serializable with OperationHelper[NormalizeTransformBase] {

  private val LOG:Logger = LoggerFactory.getLogger(classOf[NormalizeComponent])
  val outSocketEntity = normalizeEntity.getOutSocketList.get(0)
  val inputSchema: StructType = componentsParams.getDataFrame().schema
  val outputFields = OperationUtils.getAllFields(normalizeEntity.getOutSocketList, inputSchema.map(_.name).asJava).asScala
    .toList
  val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())
  val inSocketId: String = normalizeEntity.getInSocketList.get(0).getInSocketId
  val mapFields = outSocketEntity.getMapFieldsList.asScala.toList
  val passthroughFields: Array[String] = OperationUtils.getPassThrougFields(outSocketEntity.getPassThroughFieldsList,
    inputSchema
      .map
      (_.name).asJava).asScala.toArray[String]
  val mapFieldIndexes = getIndexes(inputSchema, outputSchema, getMapSourceFields(mapFields, inSocketId), getMapTargetFields(mapFields, inSocketId))
  val passthroughIndexes = getIndexes(inputSchema, outputSchema, passthroughFields)

  def getAllInputFieldsForExpr(getOperationsList: util.List[Operation], list: List[String]): List[String] = {
    LOG.trace("In method getAllInputFieldsForExpr()")
    val list1 = getOperationsList.asScala.toList.flatMap(e => e.getOperationInputFields)
    list1
  }

  def getAllOutputFieldsForExpr(getOperationsList: util.List[Operation], list: List[String]): List[String] = {
    LOG.trace("In method getAllOutputFieldsForExpr()")
    val list1 = getOperationsList.asScala.toList.flatMap(e => e.getOperationOutputFields)
    list1
  }

  def unique[A](ls: List[A]) = {
    LOG.trace("In method unique()")
    def loop(set: Set[A], ls: List[A]): List[A] = ls match {
      case hd :: tail if set contains hd => loop(set, tail)
      case hd :: tail => hd :: loop(set + hd, tail)
      case Nil => Nil
    }
    loop(Set(), ls)
  }

  def convertToList(listBuffer: ListBuffer[String]): util.ArrayList[String] = {
    LOG.trace("In method convertToList()")
    def convert(list: List[String], arrayList: util.ArrayList[String]): util.ArrayList[String] = list match {
      case List() => arrayList
      case x :: xs => {
        arrayList.add(x)
        convert(xs, arrayList)
      }
    }
    convert(listBuffer.toList, new util.ArrayList[String]())
  }

  def getOperationOutputFields(strings: ListBuffer[ListBuffer[String]]): util.ArrayList[util.ArrayList[String]] = {
    LOG.trace("In method getOperationOutputFields()")

    def flattenBufferList(strings: ListBuffer[ListBuffer[String]]): ListBuffer[ListBuffer[String]] = {
      def flattenList(strings: ListBuffer[ListBuffer[String]], finalList: ListBuffer[ListBuffer[String]], count: Int): ListBuffer[ListBuffer[String]] = (finalList, count) match {
        case (f, c) if c == 0 => f
        case (f, count) => flattenList(strings, f += strings.flatten, count - 1)
      }
      flattenList(strings, ListBuffer[ListBuffer[String]](), strings.length)
    }

    def listBufferToArrayList(listBuffer: ListBuffer[java.util.ArrayList[String]]): util.ArrayList[util.ArrayList[String]] = {
      LOG.trace("In method listBufferToArrayList()")

      def convert(list: List[util.ArrayList[String]], arrayList: util.ArrayList[util.ArrayList[String]]): util.ArrayList[util.ArrayList[String]] = list match {
        case List() => arrayList
        case x :: xs => {
          arrayList.add(x)
          convert(xs, arrayList)
        }
      }
      convert(listBuffer.toList, new util.ArrayList[util.ArrayList[String]]())
    }
    val temp = listBufferToArrayList(flattenBufferList(strings).map(e => convertToList(e)))
    temp
  }

  def extractAllInputPositions(inputFields: List[String]): List[Int] = Seq(0 to (inputFields.length - 1)).toList.flatten

  def extractAllOutputPositions(outputFields: List[String]): List[Int] = Seq(0 to (outputFields.length - 1)).toList.flatten

  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")

    val outRow = new Array[Any](outputFields.size)
    var outputDispatcher: NormalizeOutputCollector = null

    val df = componentsParams.getDataFrame.mapPartitions(itr => {

      val normalizeList: List[SparkOperation[NormalizeTransformBase]] = initializeOperationList[NormalizeForExpression](normalizeEntity.getOperationsList, inputSchema, outputSchema)

      val nr1 = normalizeList.get(0)

      if(!nr1.baseClassInstance.isInstanceOf[NormalizeForExpression]){
        nr1.baseClassInstance.prepare(nr1.operationEntity.getOperationProperties)
      }
      val it = itr.flatMap(row => {
        copyFields(row, outRow, mapFieldIndexes)
        copyFields(row, outRow, passthroughIndexes)

        outputDispatcher = new NormalizeOutputCollector(outRow)

        if(nr1.baseClassInstance.isInstanceOf[NormalizeForExpression]){
            LOG.info("Normalize Operation contains Expressions, so NormalizeForExpression class will be used for processing.")
            val fieldNames: Array[String] = inputSchema.map(_.name).toArray[String]
            val tuples: Array[Object] = (0 to (fieldNames.length - 1)).toList.map(e => row.get(e).asInstanceOf[Object]).toArray
            val inputFields: List[String] = unique(getAllInputFieldsForExpr(normalizeEntity.getOperationsList, List[String]()))
            val outputFields: List[String] = getAllOutputFieldsForExpr(normalizeEntity.getOperationsList, List[String]())
            val inputPositions: List[Int] = extractAllInputPositions(inputFields)
            val outputPositions = extractAllOutputPositions(outputFields).to[ListBuffer]
            val x = normalizeList.map(sp => sp.validatioinAPI.getExpr)
            LOG.info("List of Expressions: [" + x.toString + "].")
            nr1.baseClassInstance.asInstanceOf[NormalizeForExpression].setValidationAPI(new ExpressionWrapper(nr1.validatioinAPI, fieldNames, tuples
              , normalizeEntity.getOutputRecordCount, normalizeList.length, nr1.operationOutFields, x.asJava))
          outputDispatcher = new NormalizeOutputCollector(outRow)
          }

        outputDispatcher.initialize
        LOG.trace("Calling Normalize() method of " + nr1.baseClassInstance.getClass.toString + " class.")
        nr1.baseClassInstance.Normalize(nr1.inputRow.setRow(row), nr1.outputRow.setRow(outRow), outputDispatcher)
        if (itr.isEmpty) {
          LOG.debug("Calling cleanup() method of " + nr1.baseClassInstance.getClass.toString + " class.")
          nr1.baseClassInstance.cleanup()
        }
        outputDispatcher.getOutRows
      })
      it
    })(RowEncoder(outputSchema))

    val key = normalizeEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)
  }
}

class NormalizeOutputCollector(outRow: Array[Any]) extends OutputDispatcher {

  private val list = new ListBuffer[Row]()
  private val LOG:Logger = LoggerFactory.getLogger(classOf[NormalizeComponent])

  override def sendOutput(): Unit = {
    LOG.trace("In method sendOutput()")
    val clonedRow = outRow.clone()
    list += Row.fromSeq(clonedRow)
  }

  def initialize: Unit = {
    LOG.trace("In method initialize()")
    list.clear()
  }

  def getOutRows: ListBuffer[Row] = {
    LOG.trace("In method getOutRows()")
    list
  }
}
