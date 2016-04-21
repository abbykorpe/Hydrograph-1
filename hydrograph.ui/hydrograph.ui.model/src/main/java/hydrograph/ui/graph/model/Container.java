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

 
package hydrograph.ui.graph.model;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.model.helper.LoggerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.thoughtworks.xstream.annotations.XStreamOmitField;


/**
 * The Class Container(Graph).
 * 
 * @author Bitwise
 */
public class Container extends Model {
	
	private static final long serialVersionUID = 8825716379098354511L;
	public static final String CHILD_ADDED_PROP = "ComponentsDiagram.ChildAdded";
	public static final String CHILD_REMOVED_PROP = "ComponentsDiagram.ChildRemoved";
	
	private final List<Component> components = new ArrayList<>();
	private final Map<String, Integer> componentNextNameSuffixes = new HashMap<>();
	private ArrayList<String> componentNames = new ArrayList<>();
	@XStreamOmitField
	private boolean isVersionAlreadyUpdated;
	@XStreamOmitField
	private String linkedMainGraphPath;
	@XStreamOmitField
	private Object subjobComponentEditPart;
	private long subjobVersion=1;
	private Map<String,String> graphRuntimeProperties;
	
	public Container(){
		
	}
	
	
	/**
	 * Add a component to this graph.
	 * @return true, if the component was added, false otherwise
	 */
	public boolean addChild(Component component) {
			if (isIOSubjobAlreadyNotPresent(component.getComponentName()) && component != null
				&& components.add(component)) {
			component.setParent(this);
			String compNewName = getDefaultNameForComponent(component.getPrefix());
			component.setComponentLabel(compNewName);
			
			if (component.isNewInstance()) {
				component.setNewInstance(false);
			}
			firePropertyChange(CHILD_ADDED_PROP, null, component);
			updateSubjobVersion();
			return true;
			}
		return false;
	}

	/**
	 * Add a subjob to this graph.
	 * @return true, if the component was added, false otherwise
	 */
	public boolean addSubJobChild(Component component) {
		if (isIOSubjobAlreadyNotPresent(component.getComponentName()) && component != null && components.add(component)) {
			component.setParent(this);
			firePropertyChange(CHILD_ADDED_PROP, null, component);
			updateSubjobVersion();
			return true;
		}
		return false;
	}

	
	private boolean isIOSubjobAlreadyNotPresent(String ioSubjobComponentName) {

		if (StringUtils.equalsIgnoreCase(Constants.INPUT_SUBJOB_COMPONENT_NAME, ioSubjobComponentName)
				|| StringUtils.equalsIgnoreCase(Constants.OUTPUT_SUBJOB, ioSubjobComponentName)) {
			for (Component component : components) {
				if (StringUtils.equalsIgnoreCase(ioSubjobComponentName, component.getComponentName())) {
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", ioSubjobComponentName
							+ " [" + component.getComponentLabel().getLabelContents() + "]"
							+ Constants.SUBJOB_ALREADY_PRESENT_IN_CANVAS);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Return a List of Components in this graph. The returned List should not be
	 * modified.
	 */
	public List<Component> getChildren() {
		return components;
	}

	/**
	 * Remove a component from this diagram.
	 * @return true, if the component was removed, false otherwise
	 */
	public boolean removeChild(Component component) {
		if (component != null && components.remove(component)) {
			componentNames.remove(component.getPropertyValue(Component.Props.NAME_PROP.getValue()));
			if(componentNextNameSuffixes.get(component.getPrefix())!=null){
			Integer nextSuffix = componentNextNameSuffixes.get(component.getPrefix()) - 1;
			componentNextNameSuffixes.put(component.getPrefix(), nextSuffix);
			}
			firePropertyChange(CHILD_REMOVED_PROP, null, component);
			updateSubjobVersion();
			return true;
		}
		return false;
	}

	/**
	 * Get default name for the component for particular prefix type.
	 * @return generated default name.
	 */
	private String getDefaultNameForComponent(String prefix){

		String newName = "";
		Integer nextSuffix = componentNextNameSuffixes.get(prefix);
		LoggerUtil.getLoger(this.getClass()).debug(
				"componentNextNameSuffixes.size(): " + componentNextNameSuffixes.size());
		int next = 1;
		
		if (nextSuffix == null) {
			LoggerUtil.getLoger(this.getClass())
					.debug( "component "
							+ prefix
							+ " not present in the map! will check if default component name is already taken by some other component. If not, then return default name.");

		} else {
			LoggerUtil.getLoger(this.getClass()).debug(
					"component exists in the map. value of nextSuffix: " + nextSuffix.intValue());
			next = nextSuffix.intValue();
		}

		newName = prefix + "_" + (next < 10 ? "0" : "") + next;


		// populate Hashtable
		nextSuffix = new Integer(++next);
		Integer i = componentNextNameSuffixes.put(prefix, nextSuffix);
		LoggerUtil.getLoger(this.getClass()).debug("previous value for component " + prefix + " in map: " + i);
		LoggerUtil.getLoger(this.getClass()).debug("Adding New component name to the list: " + newName);
		

		Boolean duplicate = checkIfDuplicateComponentExists(newName);
		if (!duplicate) {
			componentNames.add(newName);
		} else {
			int maxSequenceNo = getMaximumSequenceNoFromComponentName();
			for (String componentName : componentNames) {
				if (componentName.equalsIgnoreCase(newName)) {
					String newValue = Integer.toString(maxSequenceNo + 1);
					String componenetSequenceNo = newName.substring(newName.lastIndexOf("_") + 1);
					if (componenetSequenceNo.startsWith("0") && !componenetSequenceNo.endsWith("9")
							&& newValue.length() == 1) {
						newName = newName.replace(newName.substring(newName.lastIndexOf("_") + 2), newValue);
					} else {
						newName = newName.replace(newName.substring(newName.lastIndexOf("_") + 1), newValue);
					}
				}
			}
			componentNames.add(newName);
		}
			
		return newName;
		
	}

	private boolean checkIfDuplicateComponentExists(String newName) {
		boolean duplicate = false;
		if (!componentNames.isEmpty()) {
			for (String componentName : componentNames) {
				if (componentName.equalsIgnoreCase(newName)) {
					duplicate = true;
				}
			}
		}
		return duplicate;
	}

	private int getMaximumSequenceNoFromComponentName() {
		int maxSequenceValue = 0;
		List<Integer> list = new ArrayList<>();
		if (!componentNames.isEmpty()) {
				for (String componentName : componentNames) {
					String componentSequenceNo = componentName.substring(componentName.lastIndexOf("_") + 1);
					if (componentSequenceNo.matches("[0-9]+")) {
						list.add(Integer.parseInt(componentSequenceNo));
					}
				}
				maxSequenceValue = Collections.max(list);
		}
		return maxSequenceValue;
	}
	
	
	
	/**
	 * Return a List of Component names in this graph. The returned List should not be
	 * modified.
	 */
	public ArrayList<String> getComponentNames() {
		return componentNames;
	}
	
	private boolean isUniqueCompName(String componentName) {
		componentName = componentName.trim();
		boolean result = true;

		for (String cname : componentNames) {
			if (cname.equalsIgnoreCase(componentName)) {
				result = false;
				break;
			}

		}
		LoggerUtil.getLoger(this.getClass()).debug("Conainer.isUniqueCompName(): result: " + result);

		return result;
	}
	
	/**
	 * Return a HashTable of Component prefix as key and next suffix as value.
	 */
	public Map<String, Integer> getComponentNextNameSuffixes() {
		return componentNextNameSuffixes;
	}

	/**
	 * Checks whether the graph is main or subjob
	 */
	public boolean isCurrentGraphIsSubjob() {
		for (Component component : getChildren()) {
			if (StringUtils.equalsIgnoreCase(Constants.INPUT_SUBJOB, component.getComponentName())
					|| StringUtils.equalsIgnoreCase(Constants.OUTPUT_SUBJOB, component.getComponentName()))
				return true;
		}
		return false;
	}


	public String getLinkedMainGraphPath() {
		return linkedMainGraphPath;
	}


	public void setLinkedMainGraphPath(String linkedMainGraphPath) {
		this.linkedMainGraphPath = linkedMainGraphPath;
	}


	public Object getSubjobComponentEditPart() {
		return subjobComponentEditPart;
	}


	public void setSubjobComponentEditPart(Object subjobComponentEditPart) {
		this.subjobComponentEditPart = subjobComponentEditPart;
	}


	public long getSubjobVersion() {
		return subjobVersion;
	}


	public void updateSubjobVersion() {
		if(!isVersionAlreadyUpdated && isCurrentGraphIsSubjob()){
			this.subjobVersion++;
			isVersionAlreadyUpdated=true;
		}
	}

	public Map<String, String> getGraphRuntimeProperties() {
		if (graphRuntimeProperties == null)
			graphRuntimeProperties = new LinkedHashMap<String, String>();
		return graphRuntimeProperties;
	}
	
}
