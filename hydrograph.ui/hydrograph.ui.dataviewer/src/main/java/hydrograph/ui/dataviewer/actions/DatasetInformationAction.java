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


import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.dataviewer.adapters.DataViewerAdapter;
import hydrograph.ui.dataviewer.datasetinformation.DatasetInformationDetail;
import hydrograph.ui.dataviewer.datasetinformation.DatasetInformationDialog;
import hydrograph.ui.dataviewer.preferencepage.ViewDataPreferences;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * @author Bitwise
 */

public class DatasetInformationAction extends Action {

	private static final String LABEL="Dataset Information";
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
		
		 Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		 if(shell != null){
			 	ViewDataPreferences viewDataPreferences;
				DataViewerAdapter csvAdapter;
				String debugFileLocation= new String();
				String debugFileName="";
				final String DEBUG_DATA_FILE_EXTENTION=".csv";
				
			   JobDetails jobDetails = debugDataViewer.getJobDetails();
			 	
			 	debugFileLocation = debugDataViewer.getDebugFileLocation();
			 	debugFileName = debugDataViewer.getDebugFileName();
			 	csvAdapter=debugDataViewer.getDataViewerAdapter();
			  	viewDataPreferences=debugDataViewer.getViewDataPreferences();
			  	
	    	  	DatasetInformationDialog datasetInformationDetailDialog = new DatasetInformationDialog(Display.getCurrent().getActiveShell());
			 
					DatasetInformationDetail datasetInformationDetail = new DatasetInformationDetail();
					datasetInformationDetail.setChunkFilePath(debugFileLocation+debugFileName+DEBUG_DATA_FILE_EXTENTION);
					datasetInformationDetail.setDelimeter(viewDataPreferences.getDelimiter());
					datasetInformationDetail.setEdgeNode(jobDetails.getHost());
					datasetInformationDetail.setNoOfRecords(Long.toString(csvAdapter.getRowCount()));
					datasetInformationDetail.setPageSize(Integer.toString(viewDataPreferences.getPageSize()));
					datasetInformationDetail.setQuote(viewDataPreferences.getQuoteCharactor());
					datasetInformationDetail.setViewDataFilePath(jobDetails.getBasepath());
					datasetInformationDetail.setSizeOfData(Integer.toString(viewDataPreferences.getFileSize()));
					datasetInformationDetail.setUserName(jobDetails.getUsername());
					datasetInformationDetailDialog.setData(datasetInformationDetail,debugDataViewer,jobDetails);
	    	  	
					datasetInformationDetailDialog.open();
					super.run();
					
		 }
	}
	
}
