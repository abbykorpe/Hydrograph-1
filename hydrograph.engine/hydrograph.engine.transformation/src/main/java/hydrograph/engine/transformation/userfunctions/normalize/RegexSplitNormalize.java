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
package hydrograph.engine.transformation.userfunctions.normalize;

import java.util.Properties;

import hydrograph.engine.transformation.userfunctions.base.NormalizeTransformBase;
import hydrograph.engine.transformation.userfunctions.base.OutputDispatcher;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;
import hydrograph.engine.transformation.userfunctions.helpers.RegexSplitter;

public class RegexSplitNormalize implements NormalizeTransformBase {

	private RegexSplitter regexSplitter;

	@Override
	public void prepare(Properties props) {
		String regex = props.getProperty("regex");
		if (regex == null) {
			throw new RegexNotAvailableException(
					"Property regex is not available to to splitting");
		}
		regexSplitter = new RegexSplitter(regex);

	}

	@Override
	public void Normalize(ReusableRow inputRow, ReusableRow outputRow,
			OutputDispatcher outputDispatcher) {
		String[] splits = regexSplitter.split(inputRow.getString(0));
		
		if (splits==null)
			return;
		
		for (String value : splits) {
			outputRow.setField(0, value);
			outputDispatcher.sendOutput();
		}

	}

	@Override
	public void cleanup() {
		regexSplitter = null;

	}

	private class RegexNotAvailableException extends RuntimeException {

		private static final long serialVersionUID = 2027127198378451848L;

		public RegexNotAvailableException(String msg) {
			super(msg);
		}
	}

}
