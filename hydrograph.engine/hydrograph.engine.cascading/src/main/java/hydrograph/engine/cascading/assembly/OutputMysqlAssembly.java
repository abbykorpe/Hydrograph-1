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
package hydrograph.engine.cascading.assembly;

import cascading.jdbc.db.MySqlDBInputFormat;
import hydrograph.engine.assembly.entity.OutputRDBMSEntity;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;

public class OutputMysqlAssembly extends OutputRDBMSAssembly {

	/**
	 * Mysql Output Component - Output records to MySQl database.
	 * 
	 */
	private static final long serialVersionUID = -2946197683137950707L;

	public OutputMysqlAssembly(OutputRDBMSEntity baseComponentEntity, ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	public void intializeRdbmsSpecificDrivers() {
		// For MySQL
		inputFormatClass = MySqlDBInputFormat.class;
		driverName = "com.mysql.jdbc.Driver";
	}
}