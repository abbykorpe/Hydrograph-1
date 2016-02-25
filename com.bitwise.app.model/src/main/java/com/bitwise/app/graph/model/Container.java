package com.bitwise.app.graph.model;

import java.awt.color.CMMException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.helper.LoggerUtil;
import com.bitwise.app.logging.factory.LogFactory;


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
	private final Hashtable<String, Integer> componentNextNameSuffixes = new Hashtable<>();
	private ArrayList<String> componentNames = new ArrayList<>();
	private Logger logger=LogFactory.INSTANCE.getLogger(Container.class);
		
	
	/**
	 * Add a component to this graph.
	 * @return true, if the component was added, false otherwise
	 */
	public boolean addChild(Component component) {
		if (component != null && components.add(component)) {
			component.setParent(this);
			String compNewName = getDefaultNameForComponent(component.getPrefix());
			component.setComponentLabel(compNewName);
			
			if (component.isNewInstance()) {
				component.setNewInstance(false);
			}
			firePropertyChange(CHILD_ADDED_PROP, null, component);
			return true;
		}
		return false;
	}

	/**
	 * Add a subgraph to this graph.
	 * @return true, if the component was added, false otherwise
	 */
	public boolean addSubGraphChild(Component component) {
		if (component != null && components.add(component)) {
			component.setParent(this);
			firePropertyChange(CHILD_ADDED_PROP, null, component);
			return true;
		}
		return false;
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
			Integer nextSuffix = componentNextNameSuffixes.get(component.getPrefix()) - 1;
			componentNextNameSuffixes.put(component.getPrefix(), nextSuffix);
			firePropertyChange(CHILD_REMOVED_PROP, null, component);
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
	
	private String getDefaultNameForComponentOld(String componentName, String baseName, boolean isNewInstance) {

		if (componentName == null) {
			// TODO shouldn't be the case but what should be done if name is null
			return null;
		}
		
		LoggerUtil.getLoger(this.getClass()).debug("baseName: " + baseName + ", isNewInstance: " + isNewInstance);

		if (!isNewInstance) {
			// OK, so it's not a new instance of the component (probably undo ), check if the component name is still
			// unique
			if (isUniqueCompName(componentName)) {
				componentNames.add(componentName);
				return componentName;
			} else {
				// not a new instance nor the name is unique. get the default name using base name
				componentName = baseName;
			}

		}

		componentName = componentName.trim();
		String newName = "";
		Integer nextSuffix = componentNextNameSuffixes.get(componentName);
		LoggerUtil.getLoger(this.getClass()).debug(
				"componentNextNameSuffixes.size(): " + componentNextNameSuffixes.size());
		int next = 1;

		if (nextSuffix == null) {
			LoggerUtil.getLoger(this.getClass())
					.debug( "component "
							+ componentName
							+ " not present in the map! will check if default component name is already taken by some other component. If not, then return default name.");

		} else {
			LoggerUtil.getLoger(this.getClass()).debug(
					"component exists in the map. value of nextSuffix: " + nextSuffix.intValue());
			next = nextSuffix.intValue();
		}

		newName = componentName + "_" + (next < 10 ? "0" : "") + next;

		while (true) {
			boolean continueFor = false;
			for (String cname : componentNames) {
				if (cname.equalsIgnoreCase(newName)) {
					LoggerUtil.getLoger(this.getClass()).debug("Found duplicate name: " + cname);
					continueFor = true;
					break;
				}

			}
			if (continueFor) {
				next++;
				newName = componentName + "_" + (next < 10 ? "0" : "") + next;
				LoggerUtil.getLoger(this.getClass()).debug(
						"still didn't get the new name for the component, now checking for " + newName);
			} else {
				LoggerUtil.getLoger(this.getClass()).debug("Got the new name for the component! " + newName);
				break;
			}

		}

		// populate Hashtable
		nextSuffix = new Integer(++next);
		Integer i = componentNextNameSuffixes.put(componentName, nextSuffix);
		LoggerUtil.getLoger(this.getClass()).debug("previous value for component " + componentName + " in map: " + i);
		LoggerUtil.getLoger(this.getClass()).debug("Adding New component name to the list: " + newName);
		componentNames.add(newName);

		return newName;

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
	public Hashtable<String, Integer> getComponentNextNameSuffixes() {
		return componentNextNameSuffixes;
	}

}
