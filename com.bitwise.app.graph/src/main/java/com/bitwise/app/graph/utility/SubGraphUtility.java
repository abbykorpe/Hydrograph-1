package com.bitwise.app.graph.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.figure.ComponentFigure;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.logging.factory.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class SubGraphUtility contain business logic to create sub graph.
 */
public class SubGraphUtility {

	/** The cache input subgraph comp. */
	private Map<Component, Integer> inputSubgraphCompCache = new LinkedHashMap<>();
	
	/** The cache out subgraph comp. */
	private Map<Component, List<String>> outputSubgraphCompCache = new LinkedHashMap<>();
	
	/** The Constant logger. */
	private static final Logger logger = LogFactory.INSTANCE.getLogger(SubGraphUtility.class);

	/**
	 * Open sub graph save dialog.
	 * 
	 * @return the i file
	 */
	public IFile openSubGraphSaveDialog() {

		SaveAsDialog obj = new SaveAsDialog(Display.getDefault().getActiveShell());
		IFile file = null;
		obj.setOriginalName(Constants.SUBGRAPH_NAME);
		obj.open();

		if (obj.getReturnCode() == 0) {
			getCurrentEditor().validateLengthOfJobName(obj);
		}

		if (obj.getResult() != null && obj.getReturnCode() != 1) {
			IPath filePath = obj.getResult().removeFileExtension().addFileExtension("job");
			file = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
		}

		return file;
	}

	/**
	 * Gets the current editor.
	 * 
	 * @return the current editor
	 */
	private static ELTGraphicalEditor getCurrentEditor() {
		return (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
	}

	/**
	 * Do save as sub graph.
	 *
	 * @param file the file
	 * @param container the container
	 * @return the i file
	 */
	public IFile doSaveAsSubGraph(IFile file, Container container) {

		if (file != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				out.write(getCurrentEditor().fromObjectToXML(container).getBytes());
				if (file.exists())
					file.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, null);
				else
					file.create(new ByteArrayInputStream(out.toByteArray()), true, null);
				getCurrentEditor().genrateTargetXml(file, container);
			} catch (CoreException | IOException ce) {
				MessageDialog.openError(new Shell(), "Error",
						"Exception occured while saving the graph -\n" + ce.getMessage());
			}
			getCurrentEditor().setDirty(false);
		}
		return file;

	}

	/**
	 * Creates the dynamic input port.
	 * 
	 * @param inLinks
	 *            the in links
	 * @param edComponentEditPart
	 *            the ed component edit part
	 */
	public void createDynamicInputPort(List<Link> inLinks, ComponentEditPart edComponentEditPart) {
		int sourceTerminal;
		for (int i = 0; i < inLinks.size(); i++) {
			Component oldTarget = inLinks.get(i).getTarget();
			sourceTerminal = oldTarget.getInputportTerminals().size();
			inLinks.get(i).getSource();
			Link link = inLinks.get(i);
			link.detachTarget();
			link.setTarget(edComponentEditPart.getCastedModel());
			link.setTargetTerminal(Constants.INPUT_SOCKET_TYPE + i);
			oldTarget.freeInputPort(link.getTargetTerminal());
			oldTarget.disconnectInput(link);
			link.attachTarget();
			edComponentEditPart.getCastedModel().engageInputPort(Constants.INPUT_SOCKET_TYPE + i);
			edComponentEditPart.refresh();
			Integer returnedValue = inputSubgraphCompCache.put(oldTarget, sourceTerminal);
			if (returnedValue != null) {
				inputSubgraphCompCache.put(oldTarget, returnedValue);
			}

		}

	}

	/**
	 * Creates the dynamic output port.
	 *
	 * @param outLinks            the out links
	 * @param edComponentEditPart            the component edit part
	 * @return the map
	 */
	public Map<Component, List<String>> createDynamicOutputPort(List<Link> outLinks,
			ComponentEditPart edComponentEditPart) {
		List<String> targetTerminal = new ArrayList<>();
		for (int i = 0; i < outLinks.size(); i++) {
			Component oldSource = outLinks.get(i).getSource();
			Link link = outLinks.get(i);

			List<String> returnedValue = outputSubgraphCompCache.put(oldSource, targetTerminal);
			if (returnedValue == null) {
				targetTerminal = new ArrayList<>();
				targetTerminal.add(link.getSourceTerminal());
				outputSubgraphCompCache.put(oldSource, targetTerminal);
			} else
				targetTerminal.add(link.getSourceTerminal());

			link.detachSource();
			link.setSource(edComponentEditPart.getCastedModel());
			link.setSourceTerminal(Constants.OUTPUT_SOCKET_TYPE + i);
			oldSource.freeOutputPort(link.getTargetTerminal());
			oldSource.disconnectOutput(link);
			link.attachSource();
			edComponentEditPart.getCastedModel().engageOutputPort(Constants.OUTPUT_SOCKET_TYPE + i);
			edComponentEditPart.refresh();

		}
		return outputSubgraphCompCache;

	}

	/**
	 * Update sub graph model properties.
	 * 
	 * @param edComponentEditPart
	 *            the ed component edit part
	 * @param inPort
	 *            the in port
	 * @param outPort
	 *            the out port
	 * @param file
	 *            the file
	 */
	public void updateSubGraphModelProperties(ComponentEditPart edComponentEditPart, int inPort, int outPort, IFile file) {
		edComponentEditPart.getCastedModel().inputPortSettings(inPort);
		edComponentEditPart.getCastedModel().outputPortSettings(outPort);
		ComponentFigure compFig = (ComponentFigure) edComponentEditPart.getFigure();
		compFig.setHeight(inPort, outPort);
		Dimension newSize = new Dimension(compFig.getWidth(), compFig.getHeight()
				+ edComponentEditPart.getCastedModel().getComponentLabelMargin());

		edComponentEditPart.getCastedModel().setSize(newSize);
		edComponentEditPart.getCastedModel().setComponentLabel(file.getName());

		String subGraphFilePath = file.getFullPath().toOSString();
		edComponentEditPart.getCastedModel().getProperties().put(Constants.PATH, subGraphFilePath);
		if (inPort != 0 && outPort != 0)
			edComponentEditPart.getCastedModel().getProperties().put(Constants.TYPE, Constants.OPERATION);
		if (inPort != 0 && outPort == 0)
			edComponentEditPart.getCastedModel().getProperties().put(Constants.TYPE, Constants.OUTPUT);
		if (inPort == 0 && outPort != 0)
			edComponentEditPart.getCastedModel().getProperties().put(Constants.TYPE, Constants.INPUT);
		edComponentEditPart.refresh();
	}

	/**
	 * Create sub graph xml and open the subgraph in new editor.
	 *
	 * @param componentEditPart the component edit part
	 * @param clipboardList the clipboard list
	 * @param file the file
	 */
	public void createSubGraphXml(ComponentEditPart componentEditPart, List clipboardList, IFile file) {

		Container container = new Container(true);
		/*
		 * Add sub graph join component in subgraph that use to link main graph with sub graph.
		 */
		Component inputSubComponent = SubGraphPortLinkUtilty.addInputSubGraphComponentAndLink(container,
				inputSubgraphCompCache, clipboardList);
		Component outSubComponent = SubGraphPortLinkUtilty.addOutputSubGraphComponentAndLink(container,
				inputSubgraphCompCache, outputSubgraphCompCache, clipboardList);

		/*
		 * Add all remaining component those not linked with main graph.
		 */
		for (Object object : clipboardList) {
			container.addSubGraphChild((Component) object);
		}

		doSaveAsSubGraph(file, container);
		inputSubComponent.getProperties().put(Constants.SCHEMA_TO_PROPAGATE, new HashMap<>());
		propogateSchemaToSubgraph((((ComponentEditPart) componentEditPart).getCastedModel()), outSubComponent);
	}

	/**
	 * Propogate schema to subgraph.
	 *
	 * @param subgraphComponent the subgraph component
	 * @param component the component
	 */
	public void propogateSchemaToSubgraph(Component subgraphComponent, Component component) {

		if (Constants.INPUT_SUBGRAPH.equalsIgnoreCase(component.getComponentName())) {
			Map<String, ComponentsOutputSchema> inputSchemaMap = new HashMap<String, ComponentsOutputSchema>();
			for (Link innerLink : component.getSourceConnections()) {
				Link mainLink = null;
				for (Link link : subgraphComponent.getTargetConnections()) {
					if (link.getTargetTerminal().replaceAll(Constants.INPUT_SOCKET_TYPE, Constants.OUTPUT_SOCKET_TYPE)
							.equalsIgnoreCase(innerLink.getSourceTerminal())) {
						mainLink = link;
						ComponentsOutputSchema componentsOutputSchema = SchemaPropagation.INSTANCE
								.getComponentsOutputSchema(mainLink);
						inputSchemaMap.put(innerLink.getSourceTerminal(), componentsOutputSchema);
					}
				}

			}
			component.getProperties().put(Constants.SCHEMA_TO_PROPAGATE, inputSchemaMap);
			subgraphComponent.getProperties().put(Constants.INPUT_SUBGRAPH, component);
			SchemaPropagation.INSTANCE.continuousSchemaPropagation(subgraphComponent, inputSchemaMap);
		}
		if (Constants.OUTPUT_SUBGRAPH.equalsIgnoreCase(component.getComponentName())) {
			Map<String, ComponentsOutputSchema> outputSchemaMap = new HashMap<String, ComponentsOutputSchema>();
			for (Link innerLink : component.getSourceConnections()) {
				ComponentsOutputSchema componentsOutputSchema = SchemaPropagation.INSTANCE
						.getComponentsOutputSchema(innerLink);
				outputSchemaMap.put(innerLink.getTargetTerminal().replaceAll(Constants.INPUT_SOCKET_TYPE, Constants.OUTPUT_SOCKET_TYPE), componentsOutputSchema);
			}
			component.getProperties().put(Constants.SCHEMA_TO_PROPAGATE, outputSchemaMap);
			subgraphComponent.getProperties().put(Constants.SCHEMA_TO_PROPAGATE, outputSchemaMap);
			subgraphComponent.getProperties().put(Constants.OUTPUT_SUBGRAPH, component);
			component.getProperties().put(Constants.SUBGRAPH_COMPONENT, subgraphComponent);
		}
	}

	/**
	 * Update subgraph port.
	 *
	 * @param componentEditPart the component edit part
	 */
	public void updateSubgraphPort(ComponentEditPart componentEditPart) {
		String pathProperty = componentEditPart.getCastedModel().getProperties().get(Constants.PATH_PROPERTY_NAME)
				.toString();
		Component selectedSubgraphComponent = componentEditPart.getCastedModel();
		int inPort = 0;
		int outPort = 0;
		if (StringUtils.isNotBlank(pathProperty)) {
			try {
				IPath jobFilePath = new Path(pathProperty);
				IFile jobFile = ResourcesPlugin.getWorkspace().getRoot().getFile(jobFilePath);
				Object obj = getCurrentEditor().fromXMLToObject(jobFile.getContents());
				Container container = (Container) obj;

				for (Component subComponent : container.getChildren()) {
					if (Constants.INPUT_SUBGRAPH.equalsIgnoreCase(subComponent.getComponentName())) {
						inPort = subComponent.getOutPortCount();
						break;
					}
				}
				for (Component subComponent : container.getChildren()) {
					if (Constants.OUTPUT_SUBGRAPH.equalsIgnoreCase(subComponent.getComponentName())) {
						outPort = subComponent.getInPortCount();
						break;
					}
				}

				selectedSubgraphComponent.getProperties().put(Constants.INPUT_PORT_COUNT_PROPERTY,
						String.valueOf(inPort));
				selectedSubgraphComponent.getProperties().put(Constants.OUTPUT_PORT_COUNT_PROPERTY,
						String.valueOf(outPort));
				linkSubGraphToMainGraph(selectedSubgraphComponent, container);
			} catch (Exception e) {
				logger.error("Failed to convert from XML to Graph due to : {}", e);
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", "Invalid graph file.");
			}
		}
	}

	/**
	 * Link sub graph to main graph.
	 *
	 * @param selectedSubgraphComponent the selected subgraph component
	 * @param container the container
	 */
	private void linkSubGraphToMainGraph(Component selectedSubgraphComponent, Container container) {
		for (Component component : container.getChildren()) {
			propogateSchemaToSubgraph(selectedSubgraphComponent, component);
		}
	}

}
