package com.bitwise.app.graph.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ContributionItemManager;
import com.bitwise.app.graph.command.ComponentCreateCommand;
import com.bitwise.app.graph.command.SubGraphCommand;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.figure.ComponentFigure;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.components.SubgraphComponent;
import com.bitwise.app.graph.utility.SubGraphUtility;

// TODO: Auto-generated Javadoc
/**
 * The Class CutAction.
 * 
 * @author Bitwise
 */
public class SubGraphAction extends SelectionAction{
	PasteAction pasteAction;
	ComponentEditPart edComponentEditPart;
	
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

	@Override
	protected void init() {
		super.init();
		
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("create"); 
		setId("create");
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setEnabled(false);
	}

	private Command createCutCommand(List<Object> selectedObjects) {
		SubGraphCommand cutCommand =new SubGraphCommand();
		if (selectedObjects == null || selectedObjects.isEmpty()) {
			return null;
		}
		Component node = null;
		Link link=null;
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

	@Override
	protected boolean calculateEnabled() {
		Command cmd = createCutCommand(getSelectedObjects());
		if (cmd == null){
			ContributionItemManager.CUT.setEnable(false);			
			return false;
		}else{
			ContributionItemManager.CUT.setEnable(true);			
			return true;
		}
 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override  
	public void run() { 
		
		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Container containerOld=editor.getContainer(); 
	   	execute(createCutCommand(getSelectedObjects())); 
    	List bList = (ArrayList) Clipboard.getDefault().getContents();
     	
    	IFile file =SubGraphUtility.doSaveAsSubGraph();
       	
    	SubgraphComponent subgraphComponent= new SubgraphComponent();
		ComponentCreateCommand createComponent = new ComponentCreateCommand(subgraphComponent,containerOld,new Rectangle(((Component)bList.get(0)).getLocation(),((Component)bList.get(0)).getSize()));
		createComponent.execute(); 
		
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry().values().iterator(); ite.hasNext();)
		{
			EditPart editPart = (EditPart) ite.next();
			if(editPart instanceof ComponentEditPart) 
			{
				if (((ComponentEditPart) editPart).getCastedModel().getCategory().equalsIgnoreCase(Constants.SUBGRAPH_COMPONENT_CATEGORY)) {
					edComponentEditPart= (ComponentEditPart) editPart;
				}
			} 
		}
		
		List< Link> inLinks = new ArrayList<>();
		List< Link> outLinks = new ArrayList<>();
		
		for (Object object : bList) {
				Component component = (Component)object;
				if(component!= null){
					List<Link> tarLinks= component.getTargetConnections();
					for(int i=0;i<tarLinks.size();i++){
						if (!bList.contains(tarLinks.get(i).getSource())) {
							inLinks.add(tarLinks.get(i));
						}
					}
					List<Link> sourLinks= component.getSourceConnections();
					for(int i=0;i<sourLinks.size();i++){
						if (!bList.contains(sourLinks.get(i).getTarget())) {
							outLinks.add(sourLinks.get(i)); 
						}
					}
					   
				}   
		}  
	   	
		SubGraphUtility.updateSubGraphModelProperties(edComponentEditPart, inLinks.size(), outLinks.size(), file);
		SubGraphUtility.createDynamicInputPort(inLinks, edComponentEditPart);
		SubGraphUtility.createDynamicOutputPort(outLinks, edComponentEditPart)	;
	}
   }
