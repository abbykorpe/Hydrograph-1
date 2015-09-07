package com.bitwise.app.graph.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionFactory;

import com.bitwise.app.common.component.config.Component;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.factory.ComponentsEditPartFactory;
import com.bitwise.app.graph.model.Connection;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.processor.DynamicClassProcessor;

/**
 *	Responsible to render the pellet and container.
 *	   
 */
public class ETLGraphicalEditor extends GraphicalEditorWithFlyoutPalette{
	
	public static final String ID = "com.bitwise.app.graph.etlgraphicaleditor";
	private Container container;
	
	public ETLGraphicalEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}
	
	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		if (getEditorInput() instanceof ETLGraphicalEditorInput) {
			setPartName(getEditorInput().getName());
		}
		setPartName(input.getName());
		container = new Container();
	}
	
	@Override
	protected PaletteRoot getPaletteRoot() {
		PaletteRoot palette = new PaletteRoot();
		createToolsGroup(palette);
		createShapesDrawer(palette);
		return palette;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		firePropertyChange(PROP_DIRTY);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(container);
			oos.close();
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();

			file.setContents(new ByteArrayInputStream(out.toByteArray()), true, 
					false, // dont keep history
					monitor);

			getCommandStack().markSaveLocation();
		} catch (CoreException | IOException e) {
			//TODO add logger
			throw new RuntimeException("Could not save the file", e); 
		} 
	}

	/**
	 * Initialize the viewer with container 
	 */
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(container);
		// listen for dropped parts
		viewer.addDropTargetListener(createTransferDropTargetListener());
	}
	

	/**
	 * Configure the graphical viewer with
	 * <ul>
	 * <li>ComponentEditPartFactory</li>
	 * <li>Zoom Contributions</li>
	 * <li>HandleKeyStrokes</li>
	 * </ul>
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		final GraphicalViewer viewer = getGraphicalViewer();
		configureViewer(viewer);
		prepareZoomContributions(viewer);
		handleKeyStrokes(viewer);
	}
	
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				// create a drag source listener for this palette viewer
				// together with an appropriate transfer drop target listener,
				// this will enable
				// model element creation by dragging a
				// CombinatedTemplateCreationEntries
				// from the palette into the editor
				// @see ShapesEditor#createTransferDropTargetListener()
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(
						viewer));
			}
		};
	}
	
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}
	
	private void createShapesDrawer(PaletteRoot palette) {
		PaletteDrawer componentsDrawer = new PaletteDrawer("Components");
		List<Component> componentsConfig = XMLConfigUtil.INSTANCE.getComponentConfig();
		for (Component componentConfig : componentsConfig) {
			Class<?> clazz = DynamicClassProcessor.INSTANCE.createClass(componentConfig);
			
			CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
					componentConfig.getName(), "Custom components", clazz, new SimpleFactory(clazz), 
					ImageDescriptor.createFromURL(prepareIconPathURL(componentConfig.getIconPath())), 
					ImageDescriptor.createFromURL(prepareIconPathURL(componentConfig.getIconPath())));
			componentsDrawer.add(component);
		}
		palette.add(componentsDrawer);
	}

	private void createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");

		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);

		// Add a marquee tool to the group
		toolbar.add(new MarqueeToolEntry());

		// Add (solid-line) connection tool
		tool = new ConnectionCreationToolEntry("Solid connection",
				"Create a solid-line connection", 
				new CreationFactory() {
					public Object getNewObject() {
						return null;
					}
		
					// see ShapeEditPart#createEditPolicies()
					// this is abused to transmit the desired line style
					public Object getObjectType() {
						return Connection.SOLID_CONNECTION;
					}
				}, 
				ImageDescriptor.createFromURL(prepareIconPathURL("/icons/connection_s16.gif")),
				ImageDescriptor.createFromURL(prepareIconPathURL("/icons/connection_s24.gif")));
		toolbar.add(tool);

		palette.add(toolbar);
	}

	private URL prepareIconPathURL(String iconPath){
		URL iconUrl = null;
		
		try {
			iconUrl = new URL("file", null, (XMLConfigUtil.CONFIG_FILES_PATH  + iconPath));
		} catch (MalformedURLException e) {
			//TODO : Add Logger
			throw new RuntimeException();
		}
		return iconUrl;
	}
	
	/**
	 * Create a transfer drop target listener. When using a
	 * CombinedTemplateCreationEntry tool in the palette, this will enable model
	 * element creation by dragging from the palette.
	 * 
	 * @see #createPaletteViewerProvider()
	 */
	private TransferDropTargetListener createTransferDropTargetListener() {
		return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
			protected CreationFactory getFactory(Object template) {
				return new SimpleFactory((Class) template);
			}
		};
	}
	
	private void handleKeyStrokes(GraphicalViewer viewer) {
		KeyHandler keyHandler = new KeyHandler();
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
				getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		keyHandler.put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0),
				getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
		keyHandler.put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0),
				getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));

		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE), MouseWheelZoomHandler.SINGLETON);
		viewer.setKeyHandler(keyHandler);
	}

	private void configureViewer(GraphicalViewer viewer){
		viewer.setEditPartFactory(new ComponentsEditPartFactory());
		ContextMenuProvider cmProvider = new ComponentsEditorContextMenuProvider(viewer, getActionRegistry());
		viewer.setContextMenu(cmProvider);
		getSite().registerContextMenu(cmProvider, viewer);
	}

	private void prepareZoomContributions(GraphicalViewer viewer){
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
		viewer.setRootEditPart(rootEditPart);
		ZoomManager manager = rootEditPart.getZoomManager();
		getActionRegistry().registerAction(new ZoomInAction(manager));
		getActionRegistry().registerAction(new ZoomOutAction(manager));

		double[] zoomLevels = new double[] {0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0};
		manager.setZoomLevels(zoomLevels);

		ArrayList<String> zoomContributions = new ArrayList<>();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomContributions);
	}
}
