/********************************************************************************
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
 ******************************************************************************/

 
package com.bitwise.app.engine.exceptions;

public class ConverterNotFoundException extends EngineException {
	
	private static final long serialVersionUID = -7050865517559484599L;
	private static final String MSG_SUFFIX = "No converter found for - ";

	public ConverterNotFoundException(String message, Throwable cause) {
		super(MSG_SUFFIX + message, cause);
	}
}
