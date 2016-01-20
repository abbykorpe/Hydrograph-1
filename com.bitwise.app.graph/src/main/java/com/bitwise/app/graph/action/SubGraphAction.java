package com.bitwise.app.graph.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import com.bitwise.app.common.util.ContributionItemManager;
import com.bitwise.app.graph.command.ComponentCreateCommand;
import com.bitwise.app.graph.command.SubGraphCommand;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.controller.LinkEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.components.SubgraphComponent;

// TODO: Auto-generated Javadoc
/**
 * The Class CutAction.
 * 
 * @author Bitwise
 */
public class SubGraphAction extends SelectionAction{
	PasteAction pasteAction;
	
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
		Iterator<Object> it = selectedObjects.iterator();
		while (it.hasNext()) {
			Object ep = it.next();
			if (ep instanceof ComponentEditPart) {
				node = (Component) ((EditPart)ep).getModel();
			}
			if (ep instanceof LinkEditPart) {
				link = (Link) ((EditPart)ep).getModel();
			}

			if (!cutCommand.isCutNode(node)){
				return null;
			}
				
			cutCommand.addElement(node);  
			cutCommand.addElementLink(link);
			}
		return cutCommand;
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
		Container containerOld=((ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()).getContainer(); 
	   	execute(createCutCommand(getSelectedObjects())); 
    	List bList = (ArrayList) Clipboard.getDefault().getContents();
    	int inCount=1;
    	int outCount=1;
    	int inPort=1;
    	int outPort=1;
       	SubgraphComponent subgraphComponent= new SubgraphComponent();
    	for (Object object : bList) {
			Component component = (Component)object;
			if(component!= null){
			List<Link> links= component.getTargetConnections();
			for(int i=0;i<links.size();i++){
				if (!bList.contains(links.get(i).getSource())) {
					inCount++;
					Component oldTarget=links.get(i).getTarget();
					Link link = links.get(i);
					link.detachTarget();
					link.getTarget().freeInputPort(link.getTargetTerminal());
					
					link.setTarget(subgraphComponent);
					link.setTargetTerminal("in"+inPort);
								
					oldTarget.freeInputPort(link.getTargetTerminal());
					oldTarget.disconnectInput(link);

					link.attachTarget();
					subgraphComponent.engageInputPort("in"+inPort);
					inPort++;
				}
				else if (!bList.contains(links.get(i).getTarget())) {
					outCount++;
					Component oldSource=links.get(i).getSource();
					Link link = links.get(i);
					link.detachSource();
					link.getSource().freeOutputPort(link.getTargetTerminal());
					
					link.setSource(subgraphComponent);
					link.setSourceTerminal("out"+outPort);
								
					oldSource.freeOutputPort(link.getSourceTerminal());
					oldSource.disconnectOutput(link);

					link.attachSource();
					subgraphComponent.engageOutputPort("out"+outPort);
					outPort++;
				}
				 
			}   
			}  
		}
    	
    	subgraphComponent.inputPortSettings(inCount);
    	subgraphComponent.outputPortSettings(outCount);
		ComponentCreateCommand createComponent = new ComponentCreateCommand(subgraphComponent,containerOld,new Rectangle(((Component)bList.get(0)).getLocation(),((Component)bList.get(0)).getSize()));
		createComponent.execute(); 
	}
   }
