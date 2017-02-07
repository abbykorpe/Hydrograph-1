/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package hydrograph.engine.spark.components.utils

import org.junit.{Assert, Test}


/**
  * Created by santlalg on 1/17/2017.
  */
class DbUpdateTableDescriptorTest {

  @Test
  def itShouldGenerateUpdateQueryWithSingleUpdateKeys(): Unit = {
    //given
    val tableName = "abc"
    val columnNames: List[String] = Array("name", "address", "zip", "mobileNumber", "Zipcode", "city", "ssn").toList
    val updateKeys: List[String] = Array("ssn").toList
    val expectedUpdateQuery = "update abc set name=?, address=?, zip=?, mobileNumber=?, Zipcode=?, city=? where ssn=?"

    //when
    val actualUpdateQuery = new DbUpdateTableDescriptor(tableName, columnNames, updateKeys).makeUpdateQuery()

    //then
    Assert.assertEquals(actualUpdateQuery, expectedUpdateQuery)
  }

  @Test
  def itShouldGenerateUpdateQueryWithMultipleUpdateKeys(): Unit = {
    //given
    val tableName = "employee"
    val columnNames: List[String] = Array("name", "address", "zip", "mobileNumber", "Zipcode", "city", "ssn").toList
    val updateKeys: List[String] = Array("zip", "mobileNumber").toList
    val expectedUpdateQuery = "update employee set name=?, address=?, Zipcode=?, city=?, ssn=? where zip=? and mobileNumber=?"

    //when
    val actualUpdateQuery = new DbUpdateTableDescriptor(tableName, columnNames, updateKeys).makeUpdateQuery()

    //then
    Assert.assertEquals(actualUpdateQuery, expectedUpdateQuery)
  }
    @Test(expected = classOf[hydrograph.engine.spark.components.utils.UpdateKeyFieldNotExistInUserSpecifiedColumnField])
    def itShouldGenerateExceptionWhenUpdateKeyDoesNotExistInUserDefinedSchema(): Unit = {
      //given
      val tableName = "employee"
      val columnNames: List[String] = Array("name", "address", "zip", "mobileNumber", "Zipcode", "city", "ssn").toList
      val expectedUpdateQuery = "update employee set name=?, address=?, Zipcode=?, city=?, ssn=? where zip=? and mobileNumber=?"

      //when
      val updateKeys: List[String] = Array("Salary").toList;

      //then
      val actualUpdateQuery = new DbUpdateTableDescriptor(tableName, columnNames, updateKeys).makeUpdateQuery()

    }
}
