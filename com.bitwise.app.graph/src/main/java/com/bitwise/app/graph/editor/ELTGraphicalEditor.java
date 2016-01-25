package com.bitwise.app.graph.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.ViewportAwareConnectionLayerClippingStrategy;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.commands.ActionHandler;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.statushandlers.StatusManager;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import com.bitwise.app.common.component.config.CategoryType;
import com.bitwise.app.common.component.config.Component;
import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.common.interfaces.tooltip.ComponentCanvas;
import com.bitwise.app.common.util.CanvasDataAdpater;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ContributionItemManager;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.engine.exceptions.EngineException;
import com.bitwise.app.engine.util.ConverterUtil;
import com.bitwise.app.graph.action.CopyAction;
import com.bitwise.app.graph.action.CutAction;
import com.bitwise.app.graph.action.PasteAction;
import com.bitwise.app.graph.editorfactory.GenrateContainerData;
import com.bitwise.app.graph.factory.ComponentsEditPartFactory;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;
import com.bitwise.app.parametergrid.utils.ParameterFileManager;
import com.bitwise.app.tooltip.tooltips.ComponentTooltip;
import com.thoughtworks.xstream.XStream;

/**
 * Responsible to render the palette and container.
 * 
 */
public class ELTGraphicalEditor extends GraphicalEditorWithFlyoutPalette implements ComponentCanvas, DefaultGEFCanvas{

	private boolean dirty=false;
	private final Color palatteBackgroundColor= new Color(null,82,84,81);
	private PaletteRoot paletteRoot = null;

	Logger logger = LogFactory.INSTANCE.getLogger(ELTGraphicalEditor.class);
	public static final String ID = "com.bitwise.app.graph.etlgraphicaleditor";
	private Container container;
	private final Point defaultComponentLocation = new Point(0, 0);

	private GraphicalViewer viewer;

	private ComponentTooltip componentTooltip;
	private Rectangle toolTipComponentBounds;
	//private String parameterFilePath;
	private String currentParameterFilePath=null;
	//private IPath parameterFileIPath;
	
	/**
	 * Instantiates a new ETL graphical editor.
	 */
	public ELTGraphicalEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		PaletteRoot palette = new PaletteRoot();
		paletteRoot = palette;
		createToolsGroup(palette);
		try {
			createShapesDrawer(palette);
		} catch (RuntimeException | SAXException |IOException e) {
			logger.error(e.getMessage(),e);
		} 
		return palette;
	}
	protected PaletteRoot getPalettesRoot(){
		return paletteRoot;
	}

	/**
	 * Initialize the viewer with container
	 */
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		viewer = getGraphicalViewer();
		viewer.setContents(container);
		// listen for dropped parts
		viewer.addDropTargetListener(createTransferDropTargetListener());
		// listener for selection on canvas
		viewer.addSelectionChangedListener(createISelectionChangedListener());
		attachCanvasMouseListeners();
		setDefaultToolUndoRedoStatus();
	}

	private void hideToolTip(){
		if(toolTipComponentBounds !=null && componentTooltip != null){
			componentTooltip.setVisible(false);
			componentTooltip=null;
			toolTipComponentBounds=null;
		}
	}

	public void attachCanvasMouseListeners(){

		viewer.getControl().addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				setCustomToolUndoRedoStatus();

			}

			@Override
			public void keyPressed(KeyEvent e) {
				setCustomToolUndoRedoStatus();
				hideToolTip();
			}
		});

		viewer.getControl().addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				if(toolTipComponentBounds !=null && componentTooltip != null){
					org.eclipse.swt.graphics.Point point = new org.eclipse.swt.graphics.Point(e.x, e.y);
					if(!toolTipComponentBounds.contains(point)){
						hideToolTip();
					}
				}
				setCustomToolUndoRedoStatus();
			}

			@Override
			public void mouseDown(MouseEvent e) {
				setCustomToolUndoRedoStatus();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				setCustomToolUndoRedoStatus();
			}
		});

		viewer.getControl().addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {
				if(toolTipComponentBounds !=null && componentTooltip != null){
					if(!componentTooltip.hasToolBarManager()){
						org.eclipse.swt.graphics.Point point = new org.eclipse.swt.graphics.Point(e.x, e.y);
						if(!toolTipComponentBounds.contains(point)){
							hideToolTip();
						}
					}
				}
				setCustomToolUndoRedoStatus();
			}
		});

		viewer.getControl().addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(MouseEvent e) {
				if(toolTipComponentBounds !=null && componentTooltip != null){
					if(!componentTooltip.hasToolBarManager()){
						org.eclipse.swt.graphics.Point point = new org.eclipse.swt.graphics.Point(e.x, e.y);
						if(!toolTipComponentBounds.contains(point)){
							hideToolTip();
						}
					}
				}
			}

			@Override
			public void mouseExit(MouseEvent e) {		
			}

			@Override
			public void mouseEnter(MouseEvent e) {		
			}
		});
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

	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		final ELTGraphicalEditor editor = this;
		return new PaletteViewerProvider(getEditDomain()) {

			@Override
			protected void configurePaletteViewer(final PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				//viewer.setEditPartFactory(new CustomPaletteEditPartFactory(palatteBackgroundColor));
				// create a drag source listener for this palette viewer
				// together with an appropriate transfer drop target listener,
				// this will enable
				// model element creation by dragging a
				// CombinatedTemplateCreationEntries
				// from the palette into the editor
				// @see ShapesEditor#createTransferDropTargetListener()
				
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
				PaletteContainerListener paletteContainerListener = new PaletteContainerListener(viewer, getGraphicalViewer());
				viewer.getControl().addMouseListener(paletteContainerListener);
				viewer.getControl().addMouseTrackListener(paletteContainerListener);
				viewer.getControl().addMouseMoveListener(paletteContainerListener);
				setDefaultToolUndoRedoStatus();
			}
			
			@Override
			public PaletteViewer createPaletteViewer(Composite parent) {
				CustomPaletteViewer pViewer = new CustomPaletteViewer();
				CustomFigureCanvas figureCanvas=new CustomFigureCanvas(parent,pViewer.getLightweightSys(),pViewer, getPalettesRoot(),editor);
				pViewer.setFigureCanvas(figureCanvas);
				configurePaletteViewer(pViewer);
				hookPaletteViewer(pViewer);
				return pViewer;
			}
		};
	}

	@Override
	public void commandStackChanged(EventObject event) {		
		setCustomToolUndoRedoStatus();
		setDirty(true);
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	/**
	 * Creates the palette container.
	 * 
	 * @param CategoryName
	 *            the category name
	 * @return the palette drawer
	 */
	public PaletteDrawer createPaletteContainer(String CategoryName) {
		String name = CategoryName.substring(0, 1).toUpperCase()
				+ CategoryName.substring(1).toLowerCase();
		PaletteDrawer p = new PaletteDrawer(name, ImageDescriptor.createFromURL(prepareIconPathURL("/icons/"+ name + "_categoryIcon.png")));
		p.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
		return p;
	}

	/**
	 * Adds the container to palette.
	 * 
	 * @param p1
	 *            the p1
	 * @param p
	 *            the p
	 */
	public void addContainerToPalette(PaletteRoot p1, PaletteDrawer p) {
		p1.add(p);
	}

	private void createShapesDrawer(PaletteRoot palette)
			throws RuntimeException, SAXException, IOException {
		Map<String, PaletteDrawer> categoryPaletteConatiner = new HashMap<>();
		for (CategoryType category : CategoryType.values()) {
			if(category.name().equalsIgnoreCase(Constants.DUMMY_COMPONENT_CATEGORY))
				continue;
			PaletteDrawer p = createPaletteContainer(category.name());
			addContainerToPalette(palette, p);
			categoryPaletteConatiner.put(category.name(), p);
		}
		List<Component> componentsConfig = XMLConfigUtil.INSTANCE
				.getComponentConfig();
		for (Component componentConfig : componentsConfig) {
			Class<?> clazz = DynamicClassProcessor.INSTANCE
					.createClass(componentConfig);

			
			if(componentConfig.getName().equalsIgnoreCase(Constants.DUMMY_COMPONENT))
				continue;
			
			/*CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
					componentConfig.getNameInPalette(), componentConfig.getDescription(), clazz,
					new SimpleFactory(clazz),
					ImageDescriptor
					.createFromURL(prepareIconPathURL(componentConfig
							.getPaletteIconPath())),
							ImageDescriptor
							.createFromURL(prepareIconPathURL(componentConfig
									.getPaletteIconPath())));
			categoryPaletteConatiner.get(componentConfig.getCategory().name())
			.add(component);*/

			CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
					componentConfig.getNameInPalette(), null, clazz,
					new SimpleFactory(clazz),
					ImageDescriptor
					.createFromURL(prepareIconPathURL(componentConfig
							.getPaletteIconPath())),
							ImageDescriptor
							.createFromURL(prepareIconPathURL(componentConfig
									.getPaletteIconPath())));
			categoryPaletteConatiner.get(componentConfig.getCategory().name())
			.add(component);

		}

	}

	private void createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");

		// Add a selection tool to the group
		//		ToolEntry tool = new PanningSelectionToolEntry();
		//		toolbar.add(tool);
		//		palette.setDefaultEntry(tool);

		palette.add(toolbar);
	}

	protected URL prepareIconPathURL(String iconPath) {
		URL iconUrl = null;

		try {
			iconUrl = new URL("file", null,
					(XMLConfigUtil.CONFIG_FILES_PATH + iconPath));
		} catch (MalformedURLException e) {
			// TODO : Add Logger
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
			@Override
			protected CreationFactory getFactory(Object template) {
				return new SimpleFactory((Class) template);
			}
		};
	}

	private ISelectionChangedListener createISelectionChangedListener() {
		return new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection sSelection = (StructuredSelection) event
						.getSelection();

				AbstractGraphicalEditPart selectedEditPart = (AbstractGraphicalEditPart) sSelection
						.getFirstElement();

				defaultComponentLocation.setLocation(selectedEditPart
						.getFigure().getBounds().x, selectedEditPart
						.getFigure().getBounds().y);

			}
		};
	}

	private void handleKeyStrokes(GraphicalViewer viewer) {
		KeyHandler keyHandler = new KeyHandler();
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
				getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		keyHandler.put(KeyStroke.getPressed((char) ('z' - 'a' + 1),'z',SWT.CTRL), getActionRegistry().getAction(ActionFactory.UNDO.getId()));
		keyHandler.put(KeyStroke.getPressed((char) ('y' - 'a' + 1), 'y', SWT.CTRL), getActionRegistry().getAction(ActionFactory.REDO.getId()));
		keyHandler.put(KeyStroke.getPressed((char) ('a' - 'a' + 1), 'a', SWT.CTRL), getActionRegistry().getAction(ActionFactory.SELECT_ALL.getId()));
		keyHandler.put(KeyStroke.getPressed((char) ('c' - 'a' + 1), 'c', SWT.CTRL), getActionRegistry().getAction(ActionFactory.COPY.getId()));
		keyHandler.put(KeyStroke.getPressed((char) ('v' - 'a' + 1), 'v', SWT.CTRL), getActionRegistry().getAction(ActionFactory.PASTE.getId()));
		keyHandler.put(KeyStroke.getPressed((char) ('x' - 'a' + 1), 'x', SWT.CTRL), getActionRegistry().getAction(ActionFactory.CUT.getId()));
		viewer.setKeyHandler(keyHandler);
	}

	@Override
	public void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		// ...
		IAction pasteAction;
		pasteAction = new PasteAction(this);
		registry.registerAction(pasteAction);
		getSelectionActions().add(pasteAction.getId());

		IAction action;
		action=new CopyAction(this, pasteAction);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action=new CutAction(this, pasteAction);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}



	private void configureViewer(GraphicalViewer viewer) {
		viewer.setEditPartFactory(new ComponentsEditPartFactory());
		ContextMenuProvider cmProvider = new ComponentsEditorContextMenuProvider(
				viewer, getActionRegistry());
		viewer.setContextMenu(cmProvider);
		getSite().registerContextMenu(cmProvider, viewer);
	}

	private void prepareZoomContributions(GraphicalViewer viewer) {

		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();

		// set clipping strategy for connection layer
		ConnectionLayer connectionLayer = (ConnectionLayer) root
				.getLayer(LayerConstants.CONNECTION_LAYER);
		connectionLayer
		.setClippingStrategy(new ViewportAwareConnectionLayerClippingStrategy(
				connectionLayer));

		List zoomLevels = new ArrayList(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);

		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		viewer.setRootEditPart(root);
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);

		//zoom on key strokes: ctrl++ and ctrl--
		IHandlerService service = 
				(IHandlerService)getEditorSite().getService(IHandlerService. class);

		service.activateHandler(zoomIn.getActionDefinitionId(),
				new ActionHandler(zoomIn));

		service.activateHandler(zoomOut.getActionDefinitionId(),
				new ActionHandler(zoomOut));

		// Scroll-wheel Zoom
		getGraphicalViewer().setProperty(
				MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
				MouseWheelZoomHandler.SINGLETON);
	}


	@Override
	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty){
		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}


	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);

		try {
			GenrateContainerData genrateContainerData = new GenrateContainerData();
			genrateContainerData.setEditorInput(input, this);
			container = genrateContainerData.getContainerData();
		} catch (CoreException | IOException ce) {
			logger.error("Exception while setting input", ce);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		String METHOD_NAME = "doSave -";
		logger.debug(METHOD_NAME);
		//getParameterFile();

		firePropertyChange(PROP_DIRTY);
		try {
			GenrateContainerData genrateContainerData = new GenrateContainerData();
			genrateContainerData.setEditorInput(getEditorInput(), this);
			genrateContainerData.storeContainerData();
			//parameterFilePath = container.getFullParameterFilePath();
			//container.setParameterFileName(getPartName().replace(".job", ".properties"));
			//if(container.getParameterFileDirectory() !=null)
				saveParameters();

		} catch (CoreException | IOException ce) {
			logger.error(METHOD_NAME , ce);
			MessageDialog.openError(new Shell(), "Error", "Exception occured while saving the graph -\n"+ce.getMessage());
		}	
	}

	private void saveParameters() {

		//get map from file
		Map<String,String> currentParameterMap = getCurrentParameterMap();
		if(currentParameterMap == null) return;
		List<String> letestParameterList = getLatestParameterList();

		Map<String,String> newParameterMap = new LinkedHashMap<>();

		for(int i=0;i<letestParameterList.size();i++){
			newParameterMap.put(letestParameterList.get(i), "");
		}

		/*for(String parameterName : letestParameterList){
			if(currentParameterMap.containsKey(parameterName))
				newParameterMap.put(parameterName, currentParameterMap.get(parameterName));
		}*/

		for(String parameterName : currentParameterMap.keySet()){
			newParameterMap.put(parameterName, currentParameterMap.get(parameterName));
		}

		ParameterFileManager parameterFileManager = new ParameterFileManager(getParameterFile());
		try {
			parameterFileManager.storeParameters(newParameterMap);
		} catch (IOException e) {
			logger.error("Unable to store parameters to the file", e);
			
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK );
			messageBox.setText("Error");
			messageBox.setMessage("Unable to store parameters to the file - \n" + e.getMessage());
			messageBox.open();
		}	
				
		refreshParameterFileInProjectExplorer();
	}

	private void refreshParameterFileInProjectExplorer() {
		IFile file=ResourcesPlugin.getWorkspace().getRoot().getFile(getParameterFileIPath());
		try {
			file.refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (CoreException e) {
			logger.error("Error in refreshing the parameters in file", e);
		}
	}
	
	private IPath getParameterFileIPath(){
		if(getEditorInput() instanceof IFileEditorInput){
			IFileEditorInput input = (IFileEditorInput)getEditorInput() ;
		    IFile file = input.getFile();
		    IProject activeProject = file.getProject();
		    String activeProjectName = activeProject.getName();
		    
		    IPath parameterFileIPath =new Path("/"+activeProjectName+"/param/"+ getPartName().replace(".job", ".properties"));
		    
			return parameterFileIPath;
		}else{
			return null;
		}
		
	}

	
	
	public String getActiveProject(){
		if(getEditorInput() instanceof IFileEditorInput){
			IFileEditorInput input = (IFileEditorInput)getEditorInput() ;
		    IFile file = input.getFile();
		    IProject activeProject = file.getProject();
		    String activeProjectName = activeProject.getName();
		    
			return activeProjectName;
		}else{
			return null;
		}
		
	}
	
	
	public String getJobName(){
		return getPartName().replace(".job", "");
	}
	
	
	@Override
	public List<String> getLatestParameterList() {
		String canvasData =getXMLString();		
		CanvasDataAdpater canvasDataAdpater = new CanvasDataAdpater(canvasData);
		return canvasDataAdpater.getParameterList();
	}

	private Map<String, String> getCurrentParameterMap() {

		File parameterFile;
		String fileName = getParameterFile();
		if(StringUtils.isNotBlank(fileName)){
			parameterFile = new File(fileName);
		}else return null;
		//destinationFile.create(new FileInputStream(sourceFile), true, null);
		if(!parameterFile.exists()){
			try {
				parameterFile.createNewFile();
			} catch (IOException e) {
				logger.error("Failed while creating file", e);
			}
		}

		Map<String, String> parameters=new LinkedHashMap<>();
		ParameterFileManager parameterFileManager = new ParameterFileManager(getParameterFile());		
		try{
			parameters = parameterFileManager.getParameterMap();
		} catch (IOException e) {
			logger.error("Failed to load parameters from the file", e);
			
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK );
			messageBox.setText("Error");
			messageBox.setMessage("Unable to load parameter file - \n" + e.getMessage());
			messageBox.open();
		}	
		
		return parameters;
	}

	/**
	 * Creates the output stream.
	 * 
	 * @param out
	 *            the out
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void createOutputStream(OutputStream out) throws IOException {

		out.write(fromObjectToXML(getContainer()).getBytes());
	}

	public Container getContainer() {
		return container;
	}

	@Override
	public void doSaveAs() {
		IFile file=opeSaveAsDialog();
		//getParameterFile();
		//setParameterFileLocationInfo(file);

		if(file!=null){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				createOutputStream(out);
				if (file.exists())
					file.setContents(new ByteArrayInputStream(out.toByteArray()), true,	false, null);
				else
					file.create(new ByteArrayInputStream(out.toByteArray()),true, null);
				logger.info("Resetting EditorInput data from GraphicalEditorInput to FileEditorInput");
				setInput(new FileEditorInput(file));
				initializeGraphicalViewer();
				genrateTargetXml(file);
				getCommandStack().markSaveLocation();
			} catch (CoreException  | IOException ce) {
				logger.error("Failed to Save the file : ", ce);
				MessageDialog.openError(new Shell(), "Error", "Exception occured while saving the graph -\n"+ce.getMessage());
			}
			setDirty(false);
		}
	}

	/*private void setParameterFileLocationInfo(IFile file) {
		if(file!=null){

			String fileName = file.getName().replace("job", "properties");
			container.setParameterFileDirectory(file.getPathVariableManager().getURIValue("PROJECT_LOC").getPath() + "/" +  CustomMessages.ProjectSupport_PARAM + "/");
			container.setParameterFileName(fileName);
			
			String projectName=file.getFullPath().segment(0);
			parameterFileIPath =new Path("/"+projectName+"/param/"+fileName);
		}

	}*/

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	private IFile opeSaveAsDialog() {
		logger.debug("opeSaveAsDialog - Opening SaveAs dialog box.");
		SaveAsDialog obj = new SaveAsDialog(Display.getDefault().getActiveShell());
		IFile file=null;
		if (getEditorInput().getName().endsWith(".job"))
		{
			obj.setOriginalName(getEditorInput().getName());
		}
		else
			obj.setOriginalName(getEditorInput().getName() + ".job");
		obj.open();
		if (obj.getReturnCode() == 0) {
			validateLengthOfJobName(obj);
		}
		if(obj.getResult()!=null&&obj.getReturnCode()!=1)
		{
			IPath filePath = obj.getResult().removeFileExtension().addFileExtension("job");
			file= ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
		}
	
		return file;
	}

	private void validateLengthOfJobName(SaveAsDialog obj) {
		String jobName=obj.getResult().removeFileExtension().lastSegment();
		while(jobName.length()>50)
		{
			jobName=obj.getResult().removeFileExtension().lastSegment();
			if(jobName.length()>50)
			{
				MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
				messageBox.setText("Error");
				messageBox.setMessage("File Name Too Long");
				if(messageBox.open()==SWT.OK)
				{
					obj.setOriginalName(jobName+".job");
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(obj.getResult());
					obj.setOriginalFile(file);
					obj.open();
					if(obj.getReturnCode()==1)
						break;
				}
			}
		}
	}



	/**
	 * From xml to object.
	 * 
	 * @param xml
	 *            the xml
	 * @return the object
	 */
	public Object fromXMLToObject(InputStream xml) {

		Object obj = null;

		XStream xs = new XStream();
		xs.autodetectAnnotations(true);
		try {

			obj = xs.fromXML(xml);
			logger.debug("Sucessfully converted JAVA Object from XML Data");
			xml.close();
		} catch (Exception e) {
			logger.error("Failed to convert from XML to Graph due to : {}", e);
			MessageDialog.openError(new Shell(), "Error", "Invalid graph file.");
		}
		return obj;
	}

	/**
	 * From object to xml.
	 * 
	 * @param object
	 *            the object
	 * @return the string
	 */
	public String fromObjectToXML(Serializable object) {

		String str = "<!-- It is recommended to avoid changes to xml data -->\n\n";

		XStream xs = new XStream();
		xs.autodetectAnnotations(true);
		try {
			str = str + xs.toXML(object);
			logger.debug( "Sucessfully converted XML from JAVA Object");
		} catch (Exception e) {
			logger.error("Failed to convert from Object to XML", e);
		}
		return str;
	}

	/**
	 * Genrate target xml.
	 * 
	 * @param ifile
	 *            the ifile
	 */
	public void genrateTargetXml(IFile ifile) {

		logger.debug("Genrating target XML");
		IFile outPutFile = ResourcesPlugin.getWorkspace().getRoot().getFile(ifile.getFullPath().removeFileExtension().addFileExtension("xml"));
		try {
			ConverterUtil.INSTANCE.convertToXML(container, false, outPutFile);
		} catch (EngineException eexception) {
			logger.warn("Failed to create the engine xml", eexception);
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Failed to create the engine xml", eexception.getMessage());
			//			
		}catch (Exception exception) {
			logger.error("Failed to create the engine xml", exception);
			Status status = new Status(IStatus.ERROR, "com.bitwise.app.graph",
					"Failed to create Engine XML " + exception.getMessage());
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		}

	}

	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);
	}

	@Override
	public CommandStack getCommandStack() {
		return super.getCommandStack();
	}


	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new ResourceChangeListener(this), IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	public void dispose() {
		super.dispose();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(new ResourceChangeListener(this));
	}
	public void deleteSelection() {
		//getActionRegistry().getAction(DeleteAction.ID).run();
		getActionRegistry().getAction(ActionFactory.DELETE.getId()).run();
	}

	public void copySelection() {
		getActionRegistry().getAction(ActionFactory.COPY.getId()).run();
	}

	public void pasteSelection() {
		getActionRegistry().getAction(ActionFactory.PASTE.getId()).run();
	}
	public void undoSelection() {
		getActionRegistry().getAction(ActionFactory.UNDO.getId()).run();
	}

	public void redoSelection() {
		getActionRegistry().getAction(ActionFactory.REDO.getId()).run();
	}

	public void selectAllSelection() {
		getActionRegistry().getAction(ActionFactory.SELECT_ALL.getId()).run();	
	}

	@Override
	public Control getCanvasControl() {
		return viewer.getControl();
	}

	@Override
	public void issueToolTip(ComponentTooltip componentTooltip,Rectangle toolTipComponentBounds) {
		this.toolTipComponentBounds = toolTipComponentBounds;
		this.componentTooltip = componentTooltip;
	}

	@Override
	public ComponentTooltip getComponentTooltip() {
		return this.componentTooltip;
	}

	@Override
	public String getXMLString() {
		return fromObjectToXML(getContainer());	
	}

	private String getCurrentProjectDirectory(){
		IPath jobFileRelativePath =new Path(getTitleToolTip());
		IPath currentProjectDirectory = ( (FileEditorInput)getEditorInput()).getPath().removeLastSegments(jobFileRelativePath.segmentCount());
		return currentProjectDirectory.toString();
	}
	
	@Override
	public String getParameterFile(){		
		IPath paramterFileRelativePath=getParameterFileIPath();
		
		if(paramterFileRelativePath!=null)
			return getCurrentProjectDirectory() + paramterFileRelativePath.toFile().getPath().replace("\\", "/");
		else
			return null;
	}

	@Override
	public String getCurrentParameterFilePath() {
		return currentParameterFilePath;
	}

	@Override
	public void setCurrentParameterFilePath(String currentParameterFilePath) {
		this.currentParameterFilePath = currentParameterFilePath;
	}
	
	public void setCustomToolUndoRedoStatus(){
		ContributionItemManager.UndoRedoCustomToolBarManager.changeUndoRedoStatus(viewer);
		ContributionItemManager.UndoRedoCustomMenuBarManager.changeUndoRedoStatus(viewer);
	}
	public void setDefaultToolUndoRedoStatus(){
		ContributionItemManager.UndoRedoDefaultBarManager.changeUndoRedoStatus(viewer);	
	}

	@Override
	public void disableRunningJobResource() {
		viewer.getControl().setEnabled(false);
		disableRunningGraphResource(getEditorInput(), getPartName());
		
	}
	private void disableRunningGraphResource(IEditorInput editorInput,String partName){
		if(editorInput instanceof IFileEditorInput){
			IFileEditorInput input = (IFileEditorInput)editorInput ;
		    IFile fileJob = input.getFile();
		    IPath xmlFileIPath =new Path(input.getFile().getFullPath().toOSString().replace(".job", ".xml"));
		    IFile fileXml = ResourcesPlugin.getWorkspace().getRoot().getFile(xmlFileIPath);
		    ResourceAttributes attributes = new ResourceAttributes();
		    attributes.setReadOnly(true);
		    attributes.setExecutable(true);

			try {
				fileJob.setResourceAttributes(attributes);
				fileXml.setResourceAttributes(attributes);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		    
		}
		
	}

	@Override
	public void enableRunningJobResource() {
		viewer.getControl().setEnabled(true);
		enableRunningGraphResource(getEditorInput(), getPartName());
		
	}
	

	private void enableRunningGraphResource(IEditorInput editorInput,
			String partName) {
		IFileEditorInput input = (IFileEditorInput)editorInput ;
	    IFile fileJob = input.getFile();
	    IPath xmlFileIPath =new Path(input.getFile().getFullPath().toOSString().replace(".job", ".xml"));
	    IFile fileXml = ResourcesPlugin.getWorkspace().getRoot().getFile(xmlFileIPath);
	    ResourceAttributes attributes = new ResourceAttributes();
	    attributes.setReadOnly(false);
	    attributes.setExecutable(true);

		try {
			fileJob.setResourceAttributes(attributes);
			fileXml.setResourceAttributes(attributes);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
	}	
}
