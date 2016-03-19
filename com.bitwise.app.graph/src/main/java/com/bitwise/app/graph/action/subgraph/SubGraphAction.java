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

 
package com.bitwise.app.graph.action.subgraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.action.ContributionItemManager;
import com.bitwise.app.graph.action.PasteAction;
import com.bitwise.app.graph.command.ComponentCreateCommand;
import com.bitwise.app.graph.command.SubGraphCommand;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.LinkComparatorBySourceLocation;
import com.bitwise.app.graph.model.components.SubgraphComponent;
import com.bitwise.app.graph.utility.SubGraphUtility;
/**
 * The Class SubGraphAction use to create sub graph.
 * 
 * @author Bitwise
 */
public class SubGraphAction extends SelectionAction{
	
	/** The paste action. */
	//TODO : remove pasteAction is not needed.
	PasteAction pasteAction;
	
	/** The ed component edit part. */
	ComponentEditPart componentEditPart;
	/**
	 * Instantiates a new cut action.
	 * 
	 * @param part
	 *            the part
	 * @param action
	 *            the action
	 */
	public SubGraphAction(IWorkbenchPart part, IAction action) {
		super(part);
		this.pasteAction = (PasteAction) action;
		setLazyEnablementCalculation(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init()
	 */
	@Override
	protected void init() {
		super.init();
		
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText(Constants.SUBGRAPH_CREATE); 
		setId(Constants.SUBGRAPH_CREATE);
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setEnabled(false);
	}

	/**
	 * Creates the sub graph command.
	 *
	 * @param selectedObjects the selected objects
	 * @return the command
	 */
	private Command createSubGraphCommand(List<Object> selectedObjects) {
		SubGraphCommand cutCommand =new SubGraphCommand();
		if (selectedObjects == null || selectedObjects.isEmpty()) {
			return null;
		}
		Component node = null;
		boolean enabled=false;
		for(Object obj:selectedObjects)
		{
			if(obj instanceof ComponentEditPart)
			{
				enabled=true;
				break;
			}	
		}
		if(enabled)
		{	
		for(Object obj:selectedObjects)
		{
			if (obj instanceof ComponentEditPart) {
				node = (Component) ((EditPart)obj).getModel();
				cutCommand.addElement(node);
			}
		}
		return cutCommand;
		}
		else 
    	return null;	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		Command cmd = createSubGraphCommand(getSelectedObjects());
		if (cmd == null){
			return false;
		}else{
			return true;
		}
 
	}

	
	/* 
	 * Create sub graph
	 */
	@Override  
	public void run() { 
		
		if(notConfirmedByUser())
			return;
		
		SubGraphUtility subGraphUtility = new SubGraphUtility();
	
		IFile file=subGraphUtility.openSubGraphSaveDialog();
		if(file!=null)
		{	
		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Container containerOld=editor.getContainer(); 
	   	execute(createSubGraphCommand(getSelectedObjects())); 
    	List clipboardList = (List) Clipboard.getDefault().getContents();
    	SubgraphComponent subgraphComponent= new SubgraphComponent();
		ComponentCreateCommand createComponent = new ComponentCreateCommand(subgraphComponent,containerOld,new Rectangle(((Component)clipboardList.get(0)).getLocation(),((Component)clipboardList.get(0)).getSize()));
		createComponent.execute(); 
		subgraphComponent.getProperties().put(Constants.VALIDITY_STATUS,"VALID");
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry().values().iterator(); ite.hasNext();)
		{
			EditPart editPart = (EditPart) ite.next();
			
			if(editPart instanceof ComponentEditPart && Constants.SUBGRAPH_COMPONENT_CATEGORY.equalsIgnoreCase(((ComponentEditPart) editPart).getCastedModel().getCategory())) 
			{ Component tempComponent=((ComponentEditPart) editPart).getCastedModel();
				if (StringUtils.equals(tempComponent.getComponentLabel().getLabelContents(), subgraphComponent.getComponentLabel().getLabelContents())) {
					componentEditPart= (ComponentEditPart) editPart;
				}
			} 
		}

		/*
		 * Collect all input and output links for missing target or source. 
		 */
		List< Link> inLinks = new ArrayList<>();
		List< Link> outLinks = new ArrayList<>();
		for (Object object : clipboardList) {
				Component component = (Component)object;
				if(component!= null){
					List<Link> tarLinks= component.getTargetConnections();
					for(int i=0;i<tarLinks.size();i++){
						if (!clipboardList.contains(tarLinks.get(i).getSource())) {
							inLinks.add(tarLinks.get(i));
						}
					}
					List<Link> sourLinks= component.getSourceConnections();
					for(int i=0;i<sourLinks.size();i++){
						if (!clipboardList.contains(sourLinks.get(i).getTarget())) {
							outLinks.add(sourLinks.get(i)); 
						}
					}
					   
				}   
		}  
		Collections.sort(inLinks, new LinkComparatorBySourceLocation());
		Collections.sort(outLinks, new LinkComparatorBySourceLocation());
		/*
		 * Update main sub graph component size and properties
		 */
		subGraphUtility.updateSubGraphModelProperties(componentEditPart, inLinks.size(), outLinks.size(), file);
		
		/*
		 * Create Input port in main sub graph component.
		 */
		subGraphUtility.createDynamicInputPort(inLinks, componentEditPart);
		/*
		 * Create output port in main subgraph component.
		 */
		subGraphUtility.createDynamicOutputPort(outLinks, componentEditPart)	;
		/*
		 * Generate subgraph target xml.
		 */
		subGraphUtility.createSubGraphXml(componentEditPart,clipboardList,file);
		finishSubgraphCreation(subGraphUtility,subgraphComponent,componentEditPart);
		}
	}

	
	private void finishSubgraphCreation(SubGraphUtility subGraphUtility,Component subgraphComponent, ComponentEditPart componentEditPart) {
		subgraphComponent.getProperties().put(Constants.SUBGRAPH_VERSION,1);
		subGraphUtility.getCurrentEditor().setDirty(true);
		componentEditPart.updateComponentStatus();
	}

	private boolean notConfirmedByUser() {
		MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_QUESTION | SWT.YES
				| SWT.NO);
		messageBox.setMessage(Messages.CONFIRM_TO_CREATE_SUBGRAPH_MESSAGE);
		messageBox.setText(Messages.CONFIRM_TO_CREATE_SUBGRAPH_WINDOW_TITLE);
		int response = messageBox.open();
		if (response == SWT.YES) {
			return false;
		} else
			return true;
	}
}
