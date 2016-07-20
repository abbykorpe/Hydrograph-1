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
package hydrograph.ui.dataviewer.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FilterConditions {
	
	private Map<Integer,List<List<Integer>>> localGroupSelectionMap = new TreeMap<>();
	private Map<Integer,List<List<Integer>>> remoteGroupSelectionMap = new TreeMap<>();
	private List<Condition> localConditions;
	private List<Condition> remoteConditions;
	private boolean retainLocal=false;
	private boolean retainRemote=false;
	private String localCondition;
	private String remoteCondition;
	private boolean isOverWritten = false;
	
	public boolean isOverWritten() {
		return isOverWritten;
	}
	public void setOverWritten(boolean isOverWritten) {
		this.isOverWritten = isOverWritten;
	}
	public FilterConditions() {
		localConditions = new ArrayList<>();
		remoteConditions = new ArrayList<>();
	}
	public List<Condition> getLocalConditions() {
		return localConditions;
	}
	public void setLocalConditions(List<Condition> localConditions) {
		this.localConditions.clear();
		this.localConditions = localConditions;
	}
	public List<Condition> getRemoteConditions() {
		return remoteConditions;
	}
	public void setRemoteConditions(List<Condition> remoteConditions) {
		this.remoteConditions = remoteConditions;
	}
	public boolean getRetainLocal() {
		return retainLocal;
	}
	public void setRetainLocal(boolean retainLocal) {
		this.retainLocal = retainLocal;
	}
	public boolean getRetainRemote() {
		return retainRemote;
	}
	public void setRetainRemote(boolean retainRemote) {
		this.retainRemote = retainRemote;
	}
	
	public void setLocalCondition(String localCondition){
		this.localCondition = localCondition;
	}

	public String getRemoteCondition() {
		return remoteCondition;
	}
	public void setRemoteCondition(String remoteCondition) {
		this.remoteCondition = remoteCondition;
	}
	public String getLocalCondition() {
		return localCondition;
	}
	
	public Map<Integer, List<List<Integer>>> getLocalGroupSelectionMap() {
		return localGroupSelectionMap;
	}
	public void setLocalGroupSelectionMap(
			Map<Integer, List<List<Integer>>> localGroupSelectionMap) {
		this.localGroupSelectionMap = localGroupSelectionMap;
	}
	public Map<Integer, List<List<Integer>>> getRemoteGroupSelectionMap() {
		return remoteGroupSelectionMap;
	}
	public void setRemoteGroupSelectionMap(
			Map<Integer, List<List<Integer>>> remoteGroupSelectionMap) {
		this.remoteGroupSelectionMap = remoteGroupSelectionMap;
	}
}

