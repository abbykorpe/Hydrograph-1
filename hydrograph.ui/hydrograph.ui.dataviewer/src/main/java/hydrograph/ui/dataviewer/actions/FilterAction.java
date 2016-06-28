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

package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.common.schema.Field;
import hydrograph.ui.common.schema.FieldDataTypes;
import hydrograph.ui.common.schema.Fields;
import hydrograph.ui.dataviewer.filter.FilterConditions;
import hydrograph.ui.dataviewer.filter.FilterConditionsDialog;
import hydrograph.ui.dataviewer.utilities.ViewDataSchemaHelper;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.logging.factory.LogFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

public class FilterAction extends Action {
	
	private static final String LABEL="Filter";
	private DebugDataViewer debugDataViewer;
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterAction.class);
	private String SCHEMA_FILE_EXTENTION=".xml";
	private FilterConditions filterConditions;
	
	
	public FilterAction(DebugDataViewer debugDataViewer) {
    	super(LABEL);
    	this.debugDataViewer = debugDataViewer;
    	filterConditions=new FilterConditions();
	}
	@Override
	public void run() {
		FilterConditionsDialog filterConditionsDialog=new FilterConditionsDialog(new Shell());

		filterConditionsDialog.setFieldsAndTypes(getFieldsAndTypes());
		try {
			filterConditionsDialog.setDebugDataViewerAdapterAndViewer(debugDataViewer.getDataViewerAdapter(),debugDataViewer);
			if (debugDataViewer.getConditions()!=null){
				filterConditionsDialog.setFilterConditions(debugDataViewer.getConditions());
			}
			if(filterConditionsDialog.open() !=1){
				if(filterConditionsDialog.ifSetLocalFilter()){
					filterConditions.setLocalConditions(filterConditionsDialog.getLocalConditionsList());
					filterConditions.setRetainLocal(filterConditionsDialog.ifSetLocalFilter());
				}
				if(filterConditionsDialog.ifSetRemoteFilter()){
					filterConditions.setRemoteConditions(filterConditionsDialog.getRemoteConditionsList());
					filterConditions.setRetainRemote(filterConditionsDialog.ifSetRemoteFilter());
				}
				filterConditionsDialog.setOriginalFilterConditions(filterConditions);
			}
			debugDataViewer.setConditions(filterConditionsDialog.getOriginalFilterConditions());
		} catch (ClassNotFoundException | SQLException e) {
			logger.error("Error while setting debug data viewer and debug data viewer adaptor",e);
		}
		
		
	}

	private Map<String, String> getFieldsAndTypes() {
		Map<String, String> fieldsAndTypes = new HashMap<>();
		String debugFileName = debugDataViewer.getDebugFileName();
		String debugFileLocation = debugDataViewer.getDebugFileLocation();

		Fields dataViewerFileSchema = ViewDataSchemaHelper.INSTANCE.getFieldsFromSchema(debugFileLocation
				+ debugFileName + SCHEMA_FILE_EXTENTION);
		for (Field field : dataViewerFileSchema.getField()) {
			FieldDataTypes fieldDataTypes = field.getType();
			fieldsAndTypes.put(field.getName(), fieldDataTypes.value());
		}
		return fieldsAndTypes;
	}
}
