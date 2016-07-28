/*******************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
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
package hydrograph.engine.transformation.userfunctions.base;

import java.util.ArrayList;
import java.util.Properties;

/**
 * The AggregateTransformBase interface is the base interface for all the custom
 * classes defined for transformation in aggregate component in Hydrograph. This
 * interface exposes methods that enable users to perform custom aggregate
 * functions.
 * 
 * For a sample implementation of this interface refer any class under
 * {@link hydrograph.engine.transformation.userfunctions.aggregate} package.
 * 
 * 
 * @author bitwise
 *
 */
public interface AggregateTransformBase {

	/**
	 * @param props
	 *            the properties object which holds the operation properties
	 *            passed in xml
	 * @param inputFields
	 *            the list of input fields to the aggregate operation
	 * @param outputFields
	 *            the list of output fields of the aggregate operation
	 * @param keyFields
	 *            the list of key fields for the aggregate operation
	 */
	public void prepare(Properties props, ArrayList<String> inputFields, ArrayList<String> outputFields,
			ArrayList<String> keyFields);

	/**
	 * This method is the operation function and is called for each input row.
	 * The custom aggregate logic should be written in this function. Since this
	 * function is called for each record in the input, the values of variables
	 * local to this function are not persisted for every call. Use
	 * {@link #prepare(Properties, ArrayList, ArrayList, ArrayList)} and
	 * {@link #onCompleteGroup(ReusableRow)} functions to initialize / reset the
	 * required variables.
	 * 
	 * @param input
	 *            the {@link ReusableRow} object that holds the current input
	 *            row for the operation.
	 */
	public void aggregate(ReusableRow input);

	/**
	 * This method is called after processing each group. Any assignment to the
	 * output row should happen within this method as the output row is emitted
	 * at the end of this method.
	 * 
	 * @param output
	 *            the {@link ReusableRow} object that holds the output row for
	 *            the operation.
	 */
	public void onCompleteGroup(ReusableRow output);

	/**
	 * The method cleanup() is called after processing all the records in the
	 * input. This function can be typically used to do cleanup activities as
	 * the name suggests
	 */
	public void cleanup();

}
