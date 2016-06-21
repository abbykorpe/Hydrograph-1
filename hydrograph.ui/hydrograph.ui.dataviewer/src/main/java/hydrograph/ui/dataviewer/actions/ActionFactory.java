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
import java.util.LinkedHashMap;

import org.eclipse.jface.action.Action;

import hydrograph.ui.dataviewer.window.DebugDataViewer;

/**
 * 
 * ActionFactory instantiates all actions and make them accessible wherever needed 
 * 
 * @author Bitwise
 *
 */
public class ActionFactory {
	private DebugDataViewer debugDataViewer;
	private HashMap<String,Action> actionMap;
		
	public ActionFactory(DebugDataViewer debugDataViewer) {
		this.debugDataViewer = debugDataViewer;
		actionMap = new LinkedHashMap<>();
		createAllActions();
	}
	
	private void createAllActions(){
		ExportAction exportAction=new ExportAction(debugDataViewer);
		FilterAction filterAction =new FilterAction(debugDataViewer);
		SelectAllAction selectAllAction=new SelectAllAction(debugDataViewer);
		CopyAction copyAction=new CopyAction(debugDataViewer);
		FindAction findAction=new FindAction(debugDataViewer);		
		GridViewAction gridViewAction =new GridViewAction(debugDataViewer);
		HorizontalViewAction horizontalViewAction = new HorizontalViewAction(debugDataViewer);
		UnformattedViewAction unformattedViewAction = new UnformattedViewAction(debugDataViewer);
		FormattedViewAction formattedViewAction = new FormattedViewAction(debugDataViewer);
		ReloadAction reloadAction = new ReloadAction(debugDataViewer);
		PreferencesAction preferencesAction= new PreferencesAction(debugDataViewer);
		ResetSort resetSort = new ResetSort(debugDataViewer);
		
		
		actionMap.put(ExportAction.class.getName(), exportAction);
		actionMap.put(FilterAction.class.getName(), filterAction);
		actionMap.put(SelectAllAction.class.getName(), selectAllAction);
		actionMap.put(CopyAction.class.getName(), copyAction);
		actionMap.put(FindAction.class.getName(), findAction);
		actionMap.put(GridViewAction.class.getName(), gridViewAction);
		actionMap.put(HorizontalViewAction.class.getName(), horizontalViewAction);
		actionMap.put(UnformattedViewAction.class.getName(), unformattedViewAction);
		actionMap.put(FormattedViewAction.class.getName(), formattedViewAction);
		actionMap.put(ReloadAction.class.getName(), reloadAction);
		actionMap.put(PreferencesAction.class.getName(), preferencesAction);
		actionMap.put(ResetSort.class.getName(), resetSort);
		
	}
	
	/**
	 * 
	 * Get action
	 * 
	 * @param action
	 * @return {@link Action}
	 */
	public Action getAction(String action){
		return actionMap.get(action);
	}
	
	public void enableAllActions(boolean enabled){
		for(String action: actionMap.keySet()){
			actionMap.get(action).setEnabled(enabled);
		}
	}
	
}
