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

package hydrograph.ui.dataviewer.utilities;

import hydrograph.ui.dataviewer.actions.ResetSort;
import hydrograph.ui.dataviewer.window.DebugDataViewer;

/**
 * 
 * Data viewer utility class.
 * This class holds utility method specific to Data viewer UI only.
 * 
 * @author Bitwise
 *
 */
public class DataViewerUtility {
	public final static DataViewerUtility INSTANCE = new DataViewerUtility();
	
	private DataViewerUtility() {
	}
	
	/**
	 * 
	 * Reset sort on data viewer
	 * 
	 */
	public void resetSort(DebugDataViewer debugDataViewer) {
		if (debugDataViewer.getRecentlySortedColumn() != null
				&& !debugDataViewer.getRecentlySortedColumn().isDisposed()) {
			debugDataViewer.getRecentlySortedColumn().setImage(null);
			debugDataViewer.setSortedColumnName(null);
			debugDataViewer.setRecentlySortedColumn(null);
		}
		debugDataViewer.getDataViewLoader().updateDataViewLists();
		debugDataViewer.getDataViewLoader().reloadloadViews();
		debugDataViewer.getActionFactory().getAction(ResetSort.class.getName()).setEnabled(false);
	}
}
