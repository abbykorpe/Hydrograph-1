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

package hydrograph.ui.engine.converter.impl;

import hydrograph.engine.jaxb.commandtypes.Hplsql;
import hydrograph.engine.jaxb.commandtypes.Hplsql.Command;
import hydrograph.engine.jaxb.commandtypes.Hplsql.Execute;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.engine.converter.CommandConverter;
import hydrograph.ui.engine.helper.ConverterHelper;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.logging.factory.LogFactory;

import org.slf4j.Logger;

/**
 * 
 * Converter for SQL type component.
 *
 * @author Bitwise
 */
public class SQLConverter extends CommandConverter {

	private static final String FILE = "file";
	private static final String SQL_COMMAND = "./hplsql/hplsql-0.3.13/hplsql -f";
	private static final Logger logger = LogFactory.INSTANCE
			.getLogger(SQLConverter.class);

	public SQLConverter(Component component) {
		super(component);
		this.baseComponent = new Hplsql();
		this.component = component;
		this.properties = component.getProperties();
		converterHelper = new ConverterHelper(component);
	}
	
	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Hplsql router=(Hplsql) baseComponent;
		
		Command cmd = new Command();
		cmd.setCmd(SQL_COMMAND);
		
		//Hp.Query query = new Hp.Query();
		//query.setValue((String) properties.get("path"));
		//Hp hp = new Hp();
		//hp.setQuery(query);
		Execute.Uri sqlFileUri = new Execute.Uri();
		sqlFileUri.setValue((String) properties.get(FILE));
		Execute exec = new Execute();
		exec.setUri(sqlFileUri);
		
		router.setCommand(cmd);
		router.setExecute(exec);
		
	}

}