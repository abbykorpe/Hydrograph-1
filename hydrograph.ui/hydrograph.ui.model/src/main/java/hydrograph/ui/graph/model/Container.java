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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructures.parametergrid.ParameterFile;
import hydrograph.ui.graph.model.helper.LoggerUtil;



/**
 * The Class Container.
 * <p>
 * The model class for Graph container.
 * @author Bitwise
 */
public class Container extends Model {
	
	private static final long serialVersionUID = 8825716379098354511L;
	public static final String CHILD_ADDED_PROP = "ComponentsDiagram.ChildAdded";
	public static final String CHILD_REMOVED_PROP = "ComponentsDiagram.ChildRemoved";
	
	private String uniqueJobId;
	private int jobRunCount;
	
	private  final List<CommentBox> comments = new ArrayList<CommentBox>();
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
	
	private List<ParameterFile> jobLevelParameterFiles;
	
	@XStreamOmitField
	private boolean isOpenedForTracking;

	public Container(){
		
	}
	
	
	
	/**
	 * Adds the job level parameter files.
	 * 
	 * @param jobLevelParameterFiles
	 *            the job level parameter files
	 */
	public void addJobLevelParameterFiles(List<ParameterFile> jobLevelParameterFiles){
		
		if (this.jobLevelParameterFiles == null){
			this.jobLevelParameterFiles = new ArrayList<>();
		}
		
		this.jobLevelParameterFiles.clear();
		this.jobLevelParameterFiles.addAll(jobLevelParameterFiles);
	}
	
	/**
	 * Gets the job level parameter files.
	 * 
	 * @return the job level parameter files
	 */
	public List<ParameterFile> getJobLevelParameterFiles() {
		if (jobLevelParameterFiles == null){
			jobLevelParameterFiles = new ArrayList<>();
		}
		List<ParameterFile> jobLevelParamterFiles = new ArrayList<>();
		jobLevelParamterFiles.addAll(this.jobLevelParameterFiles);
		return jobLevelParamterFiles;
	}
	
	
	/**
	 * 
	 * Add a component to this graph.
	 * 
	 * @param component
	 *            the component
	 * @return true, if the component was successfully added, false otherwise
	 *
	 */
	public boolean addChild(Component component) {
		if(component != null){
			if (canAddSubjobToCanvas(component.getComponentName())
				&& components.add(component)) {
			component.setParent(this);
			String compNewName = getDefaultNameForComponent(component.getPrefix());
			//Check length and increment height
			Font font = new Font( Display.getDefault(), ModelConstants.labelFont, 10,
					SWT.NORMAL );
			int labelLength = TextUtilities.INSTANCE.getStringExtents(compNewName, font).width;
			ComponentLabel componentLabel = component.getComponentLabel();
			if(labelLength >= ModelConstants.compLabelOneLineLengthLimit && component.getSize().height<96 && labelLength!=97){
				component.setSize(new Dimension(component.getSize().width, component.getSize().height + ModelConstants.componentOneLineLabelMargin));
				componentLabel.setSize(new Dimension(componentLabel.getSize().width, componentLabel.getSize().height + ModelConstants.componentOneLineLabelMargin));
				component.setComponentLabelMargin(ModelConstants.componentTwoLineLabelMargin);
			}
			else{
				if(labelLength<ModelConstants.compLabelOneLineLengthLimit){
					if(!(component.getSize().height>96)){
				component.setSize(new Dimension(component.getSize().width,80));
					}
					else{
						component.setSize(new Dimension(component.getSize().width,component.getSize().height));
					}
				}
			}
				
			component.setComponentLabel(compNewName);
			component.setComponentId(compNewName);
			
			if (component.isNewInstance()) {
				component.setNewInstance(false);
			}
			firePropertyChange(CHILD_ADDED_PROP, null, component);
			updateSubjobVersion();
			return true;
			}
		}
		return false;
	}

	public boolean addChild(CommentBox comment){
		if (comment != null) {
			comments.add(comment);
			comment.setParent(this);
			firePropertyChange(CHILD_ADDED_PROP, null, comment);
			return true;
		   }
			return false;
		}
	
	public boolean removeChild(CommentBox comment){
		if (comment != null){
			comments.remove(comment);
		firePropertyChange(CHILD_REMOVED_PROP,null ,comment);
		return true;
		}
		return false;
	}
	
	/**
	 * Add a subjob to this graph.
	 * @return true, if the subjob component was added, false otherwise
	 */
	public boolean addSubJobChild(Component component){
	if(component != null){
		if (canAddSubjobToCanvas(component.getComponentName()) && components.add(component)) {
			component.setParent(this);
			firePropertyChange(CHILD_ADDED_PROP, null, component);
			updateSubjobVersion();
			return true;
		}
	}
		return false;
}

	
	
	/**
	 * Checks if given subjob can be added to canvas.
	 * 
	 * @param ioSubjobComponentName
	 *            the io subjob component name
	 * @return true, if successful
	 */
	private boolean canAddSubjobToCanvas(String ioSubjobComponentName) {

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
	public List<Object> getChildren() {
		List<Object> objects = new ArrayList<>();
		objects.addAll(components);
		objects.addAll(comments);
		return objects;
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
		if (!componentNames.isEmpty()) {
			for (String componentName : componentNames) {
				if (componentName.equalsIgnoreCase(newName)) {
					LoggerUtil.getLoger(this.getClass()).
						debug("Conainer.checkIfDuplicateComponentExists(): Duplicate component exists.");
					return true;
				}
			}
		}
		return false;
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
	
	
	
	/**
	 * Return a HashTable of Component prefix as key and next suffix as value.
	 */
	public Map<String, Integer> getComponentNextNameSuffixes() {
		return componentNextNameSuffixes;
	}

	
	/**
	 * Checks if current graph ia a subjob or main job.
	 * 
	 * @return true, if is current graph is a subjob
	 */
	public boolean isCurrentGraphSubjob() {
		for (Object obj : getChildren()) {
			Component component=null;
			if(obj instanceof Component){
				component = (Component) obj;
			}
			if (component != null && (StringUtils.equalsIgnoreCase(Constants.INPUT_SUBJOB, component.getComponentName())
					|| StringUtils.equalsIgnoreCase(Constants.OUTPUT_SUBJOB, component.getComponentName()))){
				return true;
			}
		}
		return false;
	}


	/**
	 * Gets the linked main graph path.
	 * 
	 * @return the linked main graph path
	 */
	public String getLinkedMainGraphPath() {
		return linkedMainGraphPath;
	}


	/**
	 * Sets the linked main graph path.
	 * 
	 * @param linkedMainGraphPath
	 *            the new linked main graph path
	 */
	public void setLinkedMainGraphPath(String linkedMainGraphPath) {
		this.linkedMainGraphPath = linkedMainGraphPath;
	}


	/**
	 * Gets the subjob component edit part.
	 * 
	 * @return the subjob component edit part
	 */
	public Object getSubjobComponentEditPart() {
		return subjobComponentEditPart;
	}


	/**
	 * Sets the subjob component edit part.
	 * 
	 * @param subjobComponentEditPart
	 *            the new subjob component edit part
	 */
	public void setSubjobComponentEditPart(Object subjobComponentEditPart) {
		this.subjobComponentEditPart = subjobComponentEditPart;
	}


	/**
	 * Gets the subjob version.
	 * 
	 * @return the subjob version
	 */
	public long getSubjobVersion() {
		return subjobVersion;
	}


	/**
	 * Update subjob version.
	 */
	public void updateSubjobVersion() {
		if(!isVersionAlreadyUpdated && isCurrentGraphSubjob()){
			this.subjobVersion++;
			isVersionAlreadyUpdated=true;
		}
	}

	/**
	 * Gets the graph runtime properties.
	 * 
	 * @return the graph runtime properties
	 */
	public Map<String, String> getGraphRuntimeProperties() {
		if (graphRuntimeProperties == null){
			graphRuntimeProperties = new LinkedHashMap<String, String>();
		}
		return graphRuntimeProperties;
	}


	public String getUniqueJobId() {
		return uniqueJobId;
	}


	public void setUniqueJobId(String uniqueJobId) {
		this.uniqueJobId = uniqueJobId;
	}


	public int getJobRunCount() {
		return jobRunCount;
	}


	public void setJobRunCount(int jobRunCount) {
		this.jobRunCount = jobRunCount;
	}
	
	/**
	 *Check if subjob opened for tracking 
	 *@return isOpenedForTracking
	 */
	public boolean isOpenedForTracking() {
		return isOpenedForTracking;
	}
	
	/**
	 * Set flag that indicate subjob opened for tracking 
	 * @param isOpenedForTracking
	 */
	public void openedForTracking(boolean isOpenedForTracking) {
		this.isOpenedForTracking=isOpenedForTracking;
	}


}
