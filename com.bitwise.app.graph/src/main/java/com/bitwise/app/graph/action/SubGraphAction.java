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
		setText("SubGraph"); 
		setId("SubGraph");
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

	@Override  
	public void run() { 
		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Container containerOld=editor.getContainer(); 
	   	execute(createCutCommand(getSelectedObjects())); 
    	List bList = (ArrayList) Clipboard.getDefault().getContents();
    	int inCount=1;
    	int outCount=1;
    	int inPort=0;
    	int outPort=0;
    	IFile file =SubGraphUtility.doSaveAsSubGraph();
       	SubgraphComponent subgraphComponent= new SubgraphComponent();
		ComponentCreateCommand createComponent = new ComponentCreateCommand(subgraphComponent,containerOld,new Rectangle(((Component)bList.get(0)).getLocation(),((Component)bList.get(0)).getSize()));
		createComponent.execute(); 
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry().values().iterator(); 
				ite.hasNext();)
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
	   	for (Object object : bList) {
				Component component = (Component)object;
				if(component!= null){
				List<Link> links= component.getTargetConnections();
				for(int i=0;i<links.size();i++){
					if (!bList.contains(links.get(i).getSource())) {
						inCount++;
						inLinks.add(links.get(i));
						/*Component oldTarget=links.get(i).getTarget();
						Link link = links.get(i);
						link.detachTarget();
						link.getTarget().freeInputPort(link.getTargetTerminal());
						
						link.setTarget(subgraphComponent);
						link.setTargetTerminal("in"+inPort);
									
						oldTarget.freeInputPort(link.getTargetTerminal());
						oldTarget.disconnectInput(link); 

						link.attachTarget();
						subgraphComponent.engageInputPort("in"+inPort);
						*/
						inPort++;
					}
					else if (!bList.contains(links.get(i).getTarget())) {
						outCount++;
						Component oldSource=links.get(i).getSource();
						Link link = links.get(i);
						link.detachSource();
						link.getSource().freeOutputPort(link.getSourceTerminal());
						
						link.setSource(subgraphComponent);
						link.setSourceTerminal("out"+outPort);
						
						link.attachSource();
						subgraphComponent.engageOutputPort("out"+outPort);
						outPort++;
					}
					   
				}   
				}  
			}
	   	
	   	ComponentFigure compFig = (ComponentFigure)edComponentEditPart.getFigure();
		compFig.setHeight(inCount, outCount);			
		Dimension newSize = new Dimension(compFig.getWidth(), compFig.getHeight()+edComponentEditPart.getCastedModel().getComponentLabelMargin());
		edComponentEditPart.getCastedModel().setSize(newSize);
		edComponentEditPart.getCastedModel().setComponentLabel(file.getName());
		edComponentEditPart.getCastedModel().getProperties().put("name", file.getFullPath().toOSString());
		edComponentEditPart.getCastedModel().inputPortSettings(inPort); 
		for(int i=0;i<inLinks.size();i++){
			Component oldTarget=inLinks.get(i).getTarget();
			Link link = inLinks.get(i);
			link.detachTarget();
			link.getTarget().freeInputPort(link.getTargetTerminal());
			
			link.setTarget(edComponentEditPart.getCastedModel());
			link.setTargetTerminal("in"+i);
						
			oldTarget.freeInputPort(link.getTargetTerminal());
			oldTarget.disconnectInput(link); 

			link.attachTarget();
			edComponentEditPart.getCastedModel().engageInputPort("in"+i);
		}
		edComponentEditPart.refresh();  
		
	}
   }
