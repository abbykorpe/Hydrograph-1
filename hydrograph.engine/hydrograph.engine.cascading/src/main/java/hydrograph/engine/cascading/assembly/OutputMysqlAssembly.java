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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Collections2;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import cascading.flow.FlowDef;
import cascading.jdbc.JDBCScheme;
import cascading.jdbc.JDBCTap;
import cascading.jdbc.TableDesc;
import cascading.jdbc.db.DBInputFormat;
import cascading.jdbc.db.DBOutputFormat;
import cascading.jdbc.db.MySqlDBInputFormat;
import cascading.pipe.Pipe;
import cascading.tap.SinkMode;
import cascading.tuple.Fields;
import hydrograph.engine.assembly.entity.OutputRDBMSEntity;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.assembly.entity.elements.SchemaField;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.InputOutputFieldsAndTypesCreator;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;
import java.sql.*;



public class OutputMysqlAssembly extends OutputRDBMSAssembly {

	/**
	 * Mysql Output Component - Output records to MySQl database.
	 * 
	 */
	private static final long serialVersionUID = -2946197683137950707L;
	
	
	
	public OutputMysqlAssembly(OutputRDBMSEntity baseComponentEntity,
			ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

@Override
	public void intializeRdbmsSpecificDrivers() {
		// For MySQL
		inputFormatClass = MySqlDBInputFormat.class;
		driverName = "com.mysql.jdbc.Driver";
			}
	
	
}

	