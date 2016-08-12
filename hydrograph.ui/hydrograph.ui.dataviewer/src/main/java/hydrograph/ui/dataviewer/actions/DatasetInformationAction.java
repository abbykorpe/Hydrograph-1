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


import java.util.HashMap;
import java.util.Map;

import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.common.schema.Field;
import hydrograph.ui.common.schema.FieldDataTypes;
import hydrograph.ui.common.schema.Fields;
import hydrograph.ui.dataviewer.adapters.DataViewerAdapter;
import hydrograph.ui.dataviewer.constants.AdapterConstants;
import hydrograph.ui.dataviewer.datasetinformation.DatasetInformationDetail;
import hydrograph.ui.dataviewer.datasetinformation.DatasetInformationDialog;
import hydrograph.ui.dataviewer.filter.FilterHelper;
import hydrograph.ui.dataviewer.preferencepage.ViewDataPreferences;
import hydrograph.ui.dataviewer.utilities.ViewDataSchemaHelper;
import hydrograph.ui.dataviewer.window.DebugDataViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

/**
 * This class is responsible to show information for View Data debug file
 * @author Bitwise
 */

public class DatasetInformationAction extends Action {

	private static final String LABEL = "Dataset &Information";
	private final String DEBUG_DATA_FILE_EXTENTION=".csv";
	private DebugDataViewer debugDataViewer;
	/**
	 *
	 * @param debugDataViewer
	 */
	
	public DatasetInformationAction(DebugDataViewer debugDataViewer) {
		super(LABEL);
		this.debugDataViewer=debugDataViewer;
		
	}

	@Override
	public void run() {
		
			ViewDataPreferences viewDataPreferences;
			DataViewerAdapter csvAdapter;
			String debugFileLocation= new String();
			String debugFileName="";
			double downloadedFileSize;
			JobDetails jobDetails = debugDataViewer.getJobDetails();
 	
			debugFileLocation = debugDataViewer.getDebugFileLocation();
			debugFileName = debugDataViewer.getDebugFileName();
			downloadedFileSize = debugDataViewer.getDownloadedFileSize();
			csvAdapter=debugDataViewer.getDataViewerAdapter();
			viewDataPreferences=debugDataViewer.getViewDataPreferences();
  	
			DatasetInformationDialog datasetInformationDetailDialog = new DatasetInformationDialog(Display.getCurrent().getActiveShell());
 
			DatasetInformationDetail datasetInformationDetail = new DatasetInformationDetail();
			datasetInformationDetail.setChunkFilePath(debugFileLocation + debugFileName + DEBUG_DATA_FILE_EXTENTION);
			datasetInformationDetail.setDelimeter(viewDataPreferences.getDelimiter());
			datasetInformationDetail.setEdgeNode(jobDetails.getHost());
			datasetInformationDetail.setNoOfRecords(Long.toString(csvAdapter.getRowCount()));
			datasetInformationDetail.setPageSize(Integer.toString(viewDataPreferences.getPageSize()));
			datasetInformationDetail.setAcctualFileSize(String.valueOf(downloadedFileSize));
			datasetInformationDetail.setQuote(viewDataPreferences.getQuoteCharactor());
			datasetInformationDetail.setViewDataFilePath(jobDetails.getBasepath());
			datasetInformationDetail.setSizeOfData(Integer.toString(viewDataPreferences.getFileSize()));
			datasetInformationDetail.setUserName(jobDetails.getUsername());
			datasetInformationDetailDialog.setData(datasetInformationDetail,debugDataViewer,jobDetails);
		if (debugDataViewer.getConditions() != null) {
			StringBuffer remoteFilterCondition = FilterHelper.INSTANCE.getCondition(debugDataViewer.getConditions()
					.getRemoteConditions(), getFieldsAndTypes(), debugDataViewer.getConditions()
					.getRemoteGroupSelectionMap(), true);
			StringBuffer localFilterCondition = FilterHelper.INSTANCE.getCondition(debugDataViewer.getConditions()
					.getLocalConditions(), getFieldsAndTypes(), debugDataViewer.getConditions()
					.getLocalGroupSelectionMap(), true);
			datasetInformationDetail.setLocalFilter(localFilterCondition.toString());
			datasetInformationDetail.setRemoteFilter(remoteFilterCondition.toString());
		}
			datasetInformationDetailDialog.open();
	}
	private Map<String, String> getFieldsAndTypes() {
		Map<String, String> fieldsAndTypes = new HashMap<>();
		String debugFileName = debugDataViewer.getDebugFileName();
		String debugFileLocation = debugDataViewer.getDebugFileLocation();

		Fields dataViewerFileSchema = ViewDataSchemaHelper.INSTANCE.getFieldsFromSchema(debugFileLocation
				+ debugFileName + AdapterConstants.SCHEMA_FILE_EXTENTION);
		for (Field field : dataViewerFileSchema.getField()) {
			FieldDataTypes fieldDataTypes = field.getType();
			fieldsAndTypes.put(field.getName(), fieldDataTypes.value());
		}
		return fieldsAndTypes;
	}
}
