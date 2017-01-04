package hydrograph.engine.spark.components

import java.util
import java.util.Properties

import hydrograph.engine.core.component.entity.PartitionByExpressionEntity
import hydrograph.engine.core.component.entity.elements.{Operation, OutSocket}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.testing.wrapper.{Bucket, DataBuilder, Fields}
import org.junit.Test

/**
  * Created by santlalg on 12/15/2016.
  */
class PartitionByExpressionComponentTest {

  @Test
  def PartitionByExpressionTestOutPort(): Unit = {

    // given
    val df1 = new DataBuilder(Fields(List("name", "accountType", "address"))
      .applyTypes(List(classOf[String], classOf[String], classOf[String])))
      .addData(List("AAA", "debit", "Malad"))
      .addData(List("BBB", "credit", "Kandivali"))
      .addData(List("CCC", "mix", "Borivali"))
      .build()


    val partitionByExpressionEntity: PartitionByExpressionEntity = new PartitionByExpressionEntity
    partitionByExpressionEntity.setComponentId("partitionByExpressionComponent")

    val outSocketList: util.List[OutSocket] = new util.ArrayList[OutSocket]
    outSocketList.add(new OutSocket("out_credit", "out"))
    outSocketList.add(new OutSocket("out_debit", "out"))
    outSocketList.add(new OutSocket("out_mix", "out"))
    partitionByExpressionEntity.setOutSocketList(outSocketList)

    val operationProperties: Properties = new Properties
    operationProperties.put("ABC", "xyz")

    val operationInputFields: Array[String] = Array("accountType")
    val operationClass: String = "hydrograph.engine.userfunctions.PartitionByExpressionTest"
    partitionByExpressionEntity.setNumPartitions(3)
    partitionByExpressionEntity.setRuntimeProperties(operationProperties)

    val operation: Operation = new Operation
    operation.setOperationClass(operationClass)
    operation.setOperationInputFields(operationInputFields)
    operation.setOperationProperties(operationProperties)

    partitionByExpressionEntity.setOperation(operation)

    val cp = new BaseComponentParams
    cp.addinputDataFrame(df1)

    // when
    val partitionByExpressionDF = new PartitionByExpressionComponent(partitionByExpressionEntity, cp).createComponent()

    //then
    val credit = Bucket(Fields(List("name", "accountType", "address")), partitionByExpressionDF.get("out_credit").get).result()
    val debit = Bucket(Fields(List("name", "accountType", "address")), partitionByExpressionDF.get("out_debit").get).result()
    val mix = Bucket(Fields(List("name", "accountType", "address")), partitionByExpressionDF.get("out_mix").get).result()

    val expectedCredit = "[BBB,credit,Kandivali]"
    val expectedDebit = "[AAA,debit,Malad]"
    val expectedMix = "[CCC,mix,Borivali]"

    credit.toList.toString() == expectedCredit
    debit.toList.toString() == expectedDebit
    mix.toList.toString() == expectedMix

  }
}
