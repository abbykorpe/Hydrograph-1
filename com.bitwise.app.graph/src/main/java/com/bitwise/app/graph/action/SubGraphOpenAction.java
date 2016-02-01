package com.bitwise.app.graph.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.command.ComponentCreateCommand;
import com.bitwise.app.graph.controller.ComponentEditPart;
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
public class SubGraphOpenAction extends SelectionAction{
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
	public SubGraphOpenAction(IWorkbenchPart part, IAction action) {
		super(part);
		this.pasteAction = (PasteAction) action;
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();
		
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("open"); 
		setId("open");
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setEnabled(false);
	}



	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override  
	public void run() { 
	List<Object> selectedObjects =getSelectedObjects();
	
	if (selectedObjects != null || !selectedObjects.isEmpty()) {
		for(Object obj:selectedObjects)
		{
			if(obj instanceof ComponentEditPart)
			{
				if (((ComponentEditPart) obj).getCastedModel().getCategory().equalsIgnoreCase(Constants.SUBGRAPH_COMPONENT_CATEGORY)) {
				
					IWorkbenchPage page = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();
				List bList = (ArrayList) Clipboard
						.getDefault().getContents();
				IHandlerService handlerService = (IHandlerService) PlatformUI
						.getWorkbench().getService(IHandlerService.class);
				try {
					IPath jobFilePath=new Path((((ComponentEditPart) obj).getCastedModel()).getProperties().get("path").toString());
					IFile jobFile = ResourcesPlugin.getWorkspace().getRoot().getFile(jobFilePath);
					IFileEditorInput input = new FileEditorInput(jobFile);  
					page.openEditor(input, ELTGraphicalEditor.ID, false);
					//For selecting the created editor so it will trigger the event to activate and load the Palette
					IWorkbench workbench = PlatformUI.getWorkbench();
				    IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
				    if (activeWindow != null) {
				        final IWorkbenchPage activePage = activeWindow.getActivePage();
				        if (activePage != null) {
				            activePage.activate(activePage.findEditor(input));
				        }
				    }  
				} catch (PartInitException e) {
				}
				Container container = ((ELTGraphicalEditor) page.getActiveEditor())
						.getContainer();  
				ELTGraphicalEditor editor=	(ELTGraphicalEditor) page.getActiveEditor();
				editor.viewer.setContents(container);
				
				editor.viewer.addDropTargetListener(editor.createTransferDropTargetListener());
				// listener for selection on canvas
				editor.viewer.addSelectionChangedListener(editor.createISelectionChangedListener());
				 
				if(container.getChildren().size()==0 && bList.size()!=0){
				   	SubgraphComponent subgraphComponent= new SubgraphComponent();
				   	
/*					ComponentCreateCommand createComponent = new ComponentCreateCommand(subgraphComponent,container,new Rectangle(((Component)bList.get(0)).getLocation(),((Component)bList.get(0)).getSize()));
					createComponent.execute(); 
*/				Component component=null;
				String type =(((ComponentEditPart) obj).getCastedModel()).getProperties().get("type").toString();
				int i=0;
					if (type.equalsIgnoreCase("input")) { 
					for(Object l:bList)
					{						
						component=(Component) l;
						List<Link> links = ((Component) l).getSourceConnections();
						if(links.size()<=0){
								Link linkNew = new Link();
								linkNew.setSource(component);
								linkNew.setTarget(subgraphComponent);
								linkNew.setSourceTerminal("out0");
								linkNew.setTargetTerminal("out"+i);
								component.connectOutput(linkNew);
								subgraphComponent.connectInput(linkNew);
								i++;
							}
								
//						((Component) l).setSourceConnections(newLinks); 
						container.addChild((Component) l);
					}
				   	subgraphComponent.setProperties(new LinkedHashMap<String,Object>());
				   	subgraphComponent.getProperties().put("name", "subgraph");
				   	subgraphComponent.setComponentLabel("subgraph");
				   	subgraphComponent.getProperties().put("type", "outputsubgraph");			
					subgraphComponent.outputPortSettings(i);				   	
					subgraphComponent.setParent(container);
					container.addChild(subgraphComponent);

					} 
					
					if (type.equalsIgnoreCase("output")) { 
					for(Object l:bList)
					{						
						component=(Component) l;
						List<Link> links = ((Component) l).getTargetConnections();
						if(links.size()<=0){
								Link linkNew = new Link();
								linkNew.setTarget(component);
								linkNew.setSource(subgraphComponent);
								linkNew.setTargetTerminal("in0");
								linkNew.setSourceTerminal("in"+i);
								component.connectInput(linkNew);
								subgraphComponent.connectOutput(linkNew);
								i++;
							}
								
//						((Component) l).setSourceConnections(newLinks); 
						container.addChild((Component) l);
					}
				   	subgraphComponent.setProperties(new LinkedHashMap<String,Object>());
				   	subgraphComponent.getProperties().put("name", "subgraph");
				   	subgraphComponent.setComponentLabel("subgraph");
				   	subgraphComponent.getProperties().put("type", "inputsubgraph");			
					subgraphComponent.inputPortSettings(i);				   	
					subgraphComponent.setParent(container);
					container.addChild(subgraphComponent);

					}
				}

				
			}	
		}

	}
	}
	}

	@Override
	protected boolean calculateEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
   }
