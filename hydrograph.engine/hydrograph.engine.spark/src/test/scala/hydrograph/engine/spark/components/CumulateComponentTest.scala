package hydrograph.engine.spark.components

import java.util
import java.util.Properties

import hydrograph.engine.core.component.entity.CumulateEntity
import hydrograph.engine.core.component.entity.elements.{SchemaField, _}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.testing.wrapper.{Bucket, DataBuilder, Fields}
import org.apache.spark.sql.Row
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.Test

/**
  * Created by vaijnathp on 20/12/2016.
  */
class CumulateComponentTest {

  @Test
  def CumulateCountOfResultsAndMapFields(): Unit = {
    val df1 = new DataBuilder(Fields(List("col1", "col2", "col3", "col4")).applyTypes(List(classOf[String],
      classOf[String], classOf[String], classOf[String])))
      .addData(List("C1R1", "C2R1", "C3Rx", "C4R1"))
      .addData(List("C1R1", "C2R2", "C3Rx", "C4R2"))
      .addData(List("C1R1", "C2R3", "C3Rx", "C4R3"))
      .build()

    val cumulateEntity: CumulateEntity = new CumulateEntity
    cumulateEntity.setComponentId("CumulateTest")

    val keyField: KeyField = new KeyField
    keyField.setName("col1")
    keyField.setSortOrder("asc")
    cumulateEntity.setKeyFields(Array[KeyField](keyField))

    val operationList: util.ArrayList[Operation] = new util.ArrayList[Operation]

    val operation: Operation = new Operation
    operation.setOperationId("operation1")
    operation.setOperationInputFields(Array[String]("col2"))
    operation.setOperationOutputFields(Array[String]("count"))
    operation.setOperationClass("hydrograph.engine.transformation.userfunctions.cumulate.Count")
    operation.setOperationProperties(new Properties)
    operationList.add(operation)

    cumulateEntity.setOperationsList(operationList)

    cumulateEntity.setNumOperations(1)
    cumulateEntity.setOperationPresent(true)

    // create outSocket
    val outSocket1: OutSocket = new OutSocket("out0")

    // set map fields
    val mapFieldsList: util.List[MapField] = new util.ArrayList[MapField]
    mapFieldsList.add(new MapField("col4", "col4_new", "in0"))
    outSocket1.setMapFieldsList(mapFieldsList)

    // set pass through fields
    val passThroughFieldsList1: util.List[PassThroughField] = new util.ArrayList[PassThroughField]
    passThroughFieldsList1.add(new PassThroughField("col1", "in0"))
    passThroughFieldsList1.add(new PassThroughField("col3", "in0"))

    outSocket1.setPassThroughFieldsList(passThroughFieldsList1)

    // set Operation Field
    val operationFieldsList: util.List[OperationField] = new util.ArrayList[OperationField]
    val operationField: OperationField = new OperationField("count", "operation1")
    operationFieldsList.add(operationField)
    outSocket1.setOperationFieldList(operationFieldsList)

    // add outSocket in list
    val outSocketList: util.List[OutSocket] = new util.ArrayList[OutSocket]
    outSocketList.add(outSocket1)
    cumulateEntity.setOutSocketList(outSocketList)

    val cp = new BaseComponentParams
    cp.addinputDataFrame(df1)
    cp.addSchemaFields(Array(new SchemaField("col1", "java.lang.String"), new SchemaField("count", "java.lang.Long"), new SchemaField("col4_new", "java.lang.String"), new SchemaField("col3", "java.lang.String")))

    val cumulateDF = new CumulateComponent(cumulateEntity, cp).createComponent()

    val rows = Bucket(Fields(List("col1", "count", "col4_new", "col3")), cumulateDF.get("out0").get).result()
    assertThat(rows.size, is(3))
    assertThat(rows(0), is(Row("C1R1", ("1").toLong, "C4R3", "C3Rx")))
    assertThat(rows(1), is(Row("C1R1", ("2").toLong, "C4R2", "C3Rx")))
    assertThat(rows(2), is(Row("C1R1", ("3").toLong, "C4R1", "C3Rx")))
  }

  @Test
  def itShouldCumulateAndDoCountAndMapFieldsWithWildCardPassthroughFields(): Unit = {
    val df1 = new DataBuilder(Fields(List("col1", "col2", "col3", "col4")).applyTypes(List(classOf[String],
      classOf[String], classOf[String], classOf[String])))
      .addData(List("C1R1", "C2R1", "C3Rx", "C4R1"))
      .addData(List("C1R1", "C2R2", "C3Rx", "C4R2"))
      .addData(List("C1R1", "C2R3", "C3Rx", "C4R3"))
      .build()

    val cumulateEntity: CumulateEntity = new CumulateEntity
    cumulateEntity.setComponentId("CumulateTest")

    val keyField: KeyField = new KeyField
    keyField.setName("col1")
    keyField.setSortOrder("asc")
    cumulateEntity.setKeyFields(Array[KeyField](keyField))

    val operationList: util.ArrayList[Operation] = new util.ArrayList[Operation]

    val operation: Operation = new Operation
    operation.setOperationId("operation1")
    operation.setOperationInputFields(Array[String]("col2"))
    operation.setOperationOutputFields(Array[String]("count"))
    operation.setOperationClass("hydrograph.engine.transformation.userfunctions.cumulate.Count")
    operation.setOperationProperties(new Properties)
    operationList.add(operation)

    cumulateEntity.setOperationsList(operationList)

    cumulateEntity.setNumOperations(1)
    cumulateEntity.setOperationPresent(true)

    // create outSocket
    val outSocket1: OutSocket = new OutSocket("out0")

    // set map fields
    val mapFieldsList: util.List[MapField] = new util.ArrayList[MapField]
    mapFieldsList.add(new MapField("col4", "col4_new", "in0"))
    outSocket1.setMapFieldsList(mapFieldsList)

    // set pass through fields
    val passThroughFieldsList1: util.List[PassThroughField] = new util.ArrayList[PassThroughField]
    passThroughFieldsList1.add(new PassThroughField("*", "in0"))

    outSocket1.setPassThroughFieldsList(passThroughFieldsList1)

    // set Operation Field
    val operationFieldsList: util.List[OperationField] = new util.ArrayList[OperationField]
    val operationField: OperationField = new OperationField("count", "operation1")
    operationFieldsList.add(operationField)
    outSocket1.setOperationFieldList(operationFieldsList)

    // add outSocket in list
    val outSocketList: util.List[OutSocket] = new util.ArrayList[OutSocket]
    outSocketList.add(outSocket1)
    cumulateEntity.setOutSocketList(outSocketList)

    val cp = new BaseComponentParams
    cp.addinputDataFrame(df1)
    cp.addSchemaFields(Array(new SchemaField("col1", "java.lang.String"), new SchemaField("col2", "java.lang.String"), new SchemaField("col3", "java.lang.String"), new SchemaField("col4", "java.lang.String"), new SchemaField("col4_new", "java.lang.String"), new SchemaField("count", "java.lang.Long")))

    val cumulateDF = new CumulateComponent(cumulateEntity, cp).createComponent()

    val rows = Bucket(Fields(List("col1", "col2", "col3", "col4", "col4_new", "count")), cumulateDF.get("out0").get).result()
    assertThat(rows.size, is(3))
    assertThat(rows(0), is(Row("C1R1", "C2R3", "C3Rx", "C4R3", "C4R3", ("1").toLong)))
    assertThat(rows(1), is(Row("C1R1", "C2R2", "C3Rx", "C4R2", "C4R2", ("2").toLong)))
    assertThat(rows(2), is(Row("C1R1", "C2R1", "C3Rx", "C4R1", "C4R1", ("3").toLong)))
  }

  @Test
  def itShouldCumulateAndCountWithWildCardPassthroughFieldsWithPriority(): Unit = {
    val df1 = new DataBuilder(Fields(List("col1", "col2", "col3", "count")).applyTypes(List(classOf[String],
      classOf[String], classOf[String], classOf[Integer])))
      .addData(List("C1R1", "C2R1", "C3Rx", 1))
      .addData(List("C1R1", "C2R2", "C3Rx", 1))
      .addData(List("C1R1", "C2R3", "C3Rx", 1))
      .build()

    val cumulateEntity: CumulateEntity = new CumulateEntity
    cumulateEntity.setComponentId("CumulateTest")

    val keyField: KeyField = new KeyField
    keyField.setName("col1")
    keyField.setSortOrder("asc")
    cumulateEntity.setKeyFields(Array[KeyField](keyField))

    val operationList: util.ArrayList[Operation] = new util.ArrayList[Operation]

    val operation: Operation = new Operation
    operation.setOperationId("operation1")
    operation.setOperationInputFields(Array[String]("col2"))
    operation.setOperationOutputFields(Array[String]("count"))
    operation.setOperationClass("hydrograph.engine.transformation.userfunctions.cumulate.Count")
    operation.setOperationProperties(new Properties)
    operationList.add(operation)

    cumulateEntity.setOperationsList(operationList)

    cumulateEntity.setNumOperations(1)
    cumulateEntity.setOperationPresent(true)

    // create outSocket
    val outSocket1: OutSocket = new OutSocket("out0")

    // set map fields
    val mapFieldsList: util.List[MapField] = new util.ArrayList[MapField]
    mapFieldsList.add(new MapField("col3", "col3_new", "in0"))
    outSocket1.setMapFieldsList(mapFieldsList)

    // set pass through fields
    val passThroughFieldsList1: util.List[PassThroughField] = new util.ArrayList[PassThroughField]
    passThroughFieldsList1.add(new PassThroughField("*", "in0"))

    outSocket1.setPassThroughFieldsList(passThroughFieldsList1)

    // set Operation Field
    val operationFieldsList: util.List[OperationField] = new util.ArrayList[OperationField]
    val operationField: OperationField = new OperationField("count", "operation1")
    operationFieldsList.add(operationField)
    outSocket1.setOperationFieldList(operationFieldsList)

    // add outSocket in list
    val outSocketList: util.List[OutSocket] = new util.ArrayList[OutSocket]
    outSocketList.add(outSocket1)
    cumulateEntity.setOutSocketList(outSocketList)

    val cp = new BaseComponentParams
    cp.addinputDataFrame(df1)
    cp.addSchemaFields(Array( new SchemaField("count", "java.lang.Long"),new SchemaField("col3_new", "java.lang.String"),new SchemaField("col1", "java.lang.String"), new SchemaField("col2", "java.lang.String"), new SchemaField("col3", "java.lang.String")))

    val cumulateDF = new CumulateComponent(cumulateEntity, cp).createComponent()

    val rows = Bucket(Fields(List( "count","col3_new","col1", "col2", "col3")), cumulateDF.get("out0").get).result()
    assertThat(rows.size, is(3))
    assertThat(rows(0), is(Row(("1").toLong, "C3Rx", "C1R1", "C2R3", "C3Rx")))
    assertThat(rows(2), is(Row(("3").toLong, "C3Rx", "C1R1", "C2R1", "C3Rx")))

  }
}