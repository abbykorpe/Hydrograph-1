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
 * limitations under the License
 *******************************************************************************/
package hydrograph.engine.cascading.assembly;

import cascading.jdbc.RedshiftScheme;
import cascading.jdbc.RedshiftTableDesc;
import cascading.jdbc.db.DBInputFormat;
import cascading.tap.SinkMode;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.component.entity.OutputRDBMSEntity;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;

import java.util.Properties;


public class OutputRedshiftAssembly extends OutputRDBMSAssembly {

	protected RedshiftTableDesc tableDesc;
	protected RedshiftScheme scheme;
	
	 Properties props = new Properties();
	
//	@SuppressWarnings("rawtypes")
//	protected RedshiftTap rdbmsTap;
	
	public OutputRedshiftAssembly(OutputRDBMSEntity baseComponentEntity,
			ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}


//	public void castEntityFromBase(AssemblyEntityBase assemblyEntityBase) {
//		outputRDBMSEntity = (OutputRDBMSEntity) assemblyEntityBase;
//	}

	@Override
	public void intializeRdbmsSpecificDrivers()  {

		// For Redshift
		
		inputFormatClass = DBInputFormat.class;
		
		driverName = "RedshiftTap.DB_DRIVER";
		try {
			Class.forName("org.postgresql.Driver");
//			Class.forName("RedshiftTap.DB_DRIVER");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	
	
@Override
	protected void createTableDescAndScheme() {
//		tableDesc = new TableDesc(outputRDBMSEntity.getTableName(),
//				fieldsCreator.getFieldNames(), columnDefs, primaryKeys);
		tableDesc = new RedshiftTableDesc(outputRDBMSEntity.getTableName(),
				fieldsCreator.getFieldNames(), columnDefs, null,null);
	if(!outputRDBMSEntity.getLoadType().equals("update"))
		scheme = new RedshiftScheme(inputFormatClass, fields, columnNames);
	else {
		//String[] orderByColumns= {"lwr"};
		String[] updateByColumns = new String[outputRDBMSEntity.getUpdateByKeys().size()];
		int i = 0;
		for (TypeFieldName typeFieldName : outputRDBMSEntity.getUpdateByKeys()) {
			updateByColumns[i] = typeFieldName.getName();
			i++;
		}
	scheme =new RedshiftScheme( columnNames, null, updateByColumns ); 
	}
	}

@Override	
protected  void initializeRDBMSTap()
	{
		LOG.debug("Initializing RDBMS Tap");
		SinkMode sinkMode;
		if(outputRDBMSEntity.getLoadType().equals("truncateLoad") || outputRDBMSEntity.getLoadType().equals("newTable"))
			sinkMode = SinkMode.REPLACE;
		else
			sinkMode = SinkMode.UPDATE;
		
		/*rdbmsTap = new RedshiftTap(outputRDBMSEntity.getJdbcurl() + "/" + outputRDBMSEntity.getDatabaseName(),
				outputRDBMSEntity.getUsername(), outputRDBMSEntity.getPassword(),null,null
				,tableDesc, scheme, sinkMode, false, true);
		
		((RedshiftTap) rdbmsTap).setBatchSize(outputRDBMSEntity.getBatchSize());*/
	}

}

	