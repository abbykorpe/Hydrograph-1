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
package hydrograph.engine.transformation.userfunctions.transform;

import java.util.ArrayList;
import java.util.Properties;

import hydrograph.engine.transformation.userfunctions.base.ReusableRow;
import hydrograph.engine.transformation.userfunctions.base.TransformBase;

public class CopyFields implements TransformBase {

	@Override
	public void prepare(Properties props, ArrayList<String> inputFields,
			ArrayList<String> outputFields) {
		if (inputFields.size() != outputFields.size()) {
			throw new InvalidFields(
					"no of input and output fields should be same for CopyFields operation");
		}

	}

	@Override
	public void transform(ReusableRow inputRow, ReusableRow outputRow) {
		for (int i = 0; i < inputRow.getFieldNames().size(); i++) {
			outputRow.setField(i, inputRow.getField(i));
		}

	}

	@Override
	public void cleanup() {

	}

	private class InvalidFields extends RuntimeException {

		private static final long serialVersionUID = -3207057554059639165L;

		public InvalidFields(String msg) {
			super(msg);
		}
	}

}
