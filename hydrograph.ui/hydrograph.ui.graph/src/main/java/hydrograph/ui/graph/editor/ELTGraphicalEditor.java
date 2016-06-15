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

package hydrograph.ui.graph.editor;

import hydrograph.ui.common.component.config.CategoryType;
import hydrograph.ui.common.component.config.Component;
import hydrograph.ui.common.interfaces.console.IHydrographConsole;
import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.common.interfaces.tooltip.ComponentCanvas;
import hydrograph.ui.common.util.CanvasDataAdpater;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.engine.exceptions.EngineException;
import hydrograph.ui.engine.util.ConverterUtil;
import hydrograph.ui.graph.action.ComponentHelpAction;
import hydrograph.ui.graph.action.ComponentPropertiesAction;
import hydrograph.ui.graph.action.ContributionItemManager;
import hydrograph.ui.graph.action.CopyAction;
import hydrograph.ui.graph.action.CutAction;
import hydrograph.ui.graph.action.DeleteAction;
import hydrograph.ui.graph.action.GraphRuntimePropertiesAction;
import hydrograph.ui.graph.action.PasteAction;
import hydrograph.ui.graph.action.debug.AddWatcherAction;
import hydrograph.ui.graph.action.debug.RemoveWatcherAction;
import hydrograph.ui.graph.action.debug.WatchRecordAction;
import hydrograph.ui.graph.action.subjob.SubJobAction;
import hydrograph.ui.graph.action.subjob.SubJobOpenAction;
import hydrograph.ui.graph.action.subjob.SubJobUpdateAction;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.debugconverter.DebugHelper;
import hydrograph.ui.graph.editorfactory.GenrateContainerData;
import hydrograph.ui.graph.factory.ComponentsEditPartFactory;
import hydrograph.ui.graph.factory.CustomPaletteEditPartFactory;
import hydrograph.ui.graph.handler.DebugHandler;
import hydrograph.ui.graph.handler.RemoveDebugHandler;
import hydrograph.ui.graph.handler.RunJobHandler;
import hydrograph.ui.graph.handler.StopJobHandler;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.job.JobStatus;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.processor.DynamicClassProcessor;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.graph.utility.SubJobUtility;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.parametergrid.utils.ParameterFileManager;
import hydrograph.ui.tooltip.tooltips.ComponentTooltip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.filesystem.IFileStore;
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
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.statushandlers.StatusManager;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;

/**
 * Responsible to render the palette and container.
 * 
 */
public class ELTGraphicalEditor extends GraphicalEditorWithFlyoutPalette implements ComponentCanvas, DefaultGEFCanvas{

	private boolean dirty=false;
	private PaletteRoot paletteRoot = null;

	private Logger logger = LogFactory.INSTANCE.getLogger(ELTGraphicalEditor.class);
	public static final String ID = "hydrograph.ui.graph.etlgraphicaleditor";
	private Container container;
	private final Point defaultComponentLocation = new Point(0, 0);

	private GraphicalViewer viewer;

	private ComponentTooltip componentTooltip;
	private Rectangle toolTipComponentBounds;
	private String currentParameterFilePath=null;
	private boolean stopButtonStatus;
	
	private static final String DEFAULT_CONSOLE = "NewConsole";
	private static final String CONSOLE_VIEW_ID = "hydrograph.ui.project.structure.console.HydrographConsole";

	private String uniqueJobId;
	private String jobId;

	private static final Color palatteTextColor=new Color(null,51,51,51);
	

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
	public void initializeGraphicalViewer() {
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

	/**
	 * 
	 * Hide component tooltip
	 * 
	 */
	public void hideToolTip(){
		if(toolTipComponentBounds !=null && componentTooltip != null){
			componentTooltip.setVisible(false);
			componentTooltip=null;
			toolTipComponentBounds=null;
		}
	}
	
	@Override
	public boolean isFocused(){
		return viewer.getControl().isFocusControl();
	}
	
	/**
	 * Add mouse listener on canvas
	 * 
	 */
	public void attachCanvasMouseListeners(){

		//TODO - please do not remove this code for now. This code will be useful in resolving stop button bugs 
		/*viewer.getControl().addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent e) {

				String consoleName;
				consoleName = (getActiveProject() + "." + getTitle().replace(".job", ""));
				
				Job job = JobManager.INSTANCE.getRunningJob(consoleName);
				
				if(job!=null){
					if(JobStatus.KILLED.equals(job.getJobStatus())){
						((RunJobHandler)RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(false);
						((StopJobHandler)RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
					}else{
						if(job.isRemoteMode()){
							enableRunJob(false);
						}else{
							((RunJobHandler)RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(false);
							((StopJobHandler)RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
						}						
					}
					
				}else{
					if(RunStopButtonCommunicator.StopJob.getHandler()!=null)
					enableRunJob(true);
				}
			
			};
			
			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
		viewer.getControl().addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(componentTooltip!=null && !componentTooltip.isToolBarToolTip()){
						hideToolTip();
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				viewer.getEditDomain().loadDefaultTool();				
			}
		});
		
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
				setCustomToolUndoRedoStatus();
				if (toolTipComponentBounds != null && componentTooltip != null) {
					if (!componentTooltip.hasToolBarManager()) {
						org.eclipse.swt.graphics.Point point = new org.eclipse.swt.graphics.Point(
								e.x, e.y);
						if (!toolTipComponentBounds.contains(point)) {
							hideToolTip();
						}
					}
				}
			}
		});
	}

	private void enableRunJob(boolean enabled){
		((RunJobHandler)RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(enabled);
		((StopJobHandler)RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(!enabled);
		((DebugHandler)RunStopButtonCommunicator.RunDebugJob.getHandler()).setDebugJobEnabled(enabled);
		if (enabled) {
			((RemoveDebugHandler) RunStopButtonCommunicator.Removewatcher.getHandler())
					.setRemoveWatcherEnabled(DebugHelper.INSTANCE.hasMoreWatchPoints());
		} else {
			((RemoveDebugHandler) RunStopButtonCommunicator.Removewatcher.getHandler())
					.setRemoveWatcherEnabled(enabled);
		}
	}

	
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		boolean isWatch = false;
		IWorkbenchPart partView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();

		IHydrographConsole currentConsoleView = (IHydrographConsole) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(CONSOLE_VIEW_ID);

		if (partView instanceof ELTGraphicalEditor) {
			if (getActiveProject() != null) {
				isWatch = DebugHelper.INSTANCE.hasMoreWatchPoints();
				ConsolePlugin plugin = ConsolePlugin.getDefault();
				IConsoleManager consoleManager = plugin.getConsoleManager();

				String consoleName;
				if (part.getTitle().contains(".job")) {
					consoleName = getActiveProject() + "." + part.getTitle().replace(".job", "");
				} else {
					consoleName = DEFAULT_CONSOLE;
				}

				JobManager.INSTANCE.setActiveCanvasId(consoleName);
				IConsole consoleToShow = getConsole(consoleName, consoleManager);

				if (currentConsoleView != null) {
					if (consoleToShow != null && !currentConsoleView.isConsoleClosed()) {
//						Fix for : Console window is getting displayed if user maximize canvas window and then try to create new job (Ctrl+J)
//						consoleManager.showConsoleView(consoleToShow);
					} else {
						if (consoleToShow == null || !consoleToShow.getName().equalsIgnoreCase(consoleName)) {
							if (!currentConsoleView.isConsoleClosed()) {
								addDummyConsole();
							}
						}

					}

				}
				
				Job job = JobManager.INSTANCE.getRunningJob(consoleName);
				
				logger.debug("job.isDebugMode: {}",job==null?"FALSE":job.isDebugMode());
				if(job!=null){
					if(JobStatus.KILLED.equals(job.getJobStatus()) || JobStatus.SUCCESS.equals(job.getJobStatus())){
						enableRunJob(true);
					}else{
						if(job.isRemoteMode()){
							enableRunJob(false);
						}else{
                            ((RunJobHandler)RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(false);
                            ((StopJobHandler)RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
                            ((DebugHandler)RunStopButtonCommunicator.RunDebugJob.getHandler()).setDebugJobEnabled(false);
                           	((RemoveDebugHandler)RunStopButtonCommunicator.Removewatcher.getHandler()).setRemoveWatcherEnabled(false);
                      }
					}
				}else{
					logger.debug("enabling run job button");
					enableRunJob(true);
					if(isWatch){
						((RemoveDebugHandler)RunStopButtonCommunicator.Removewatcher.getHandler()).setRemoveWatcherEnabled(true);
					}else{
						((RemoveDebugHandler)RunStopButtonCommunicator.Removewatcher.getHandler()).setRemoveWatcherEnabled(false);
					}
				}
			}

		}

		super.selectionChanged(part, selection);
		
		
	}

	
	private void addDummyConsole(){
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager consoleManager = plugin.getConsoleManager();

		IConsole consoleToShow = getConsole(DEFAULT_CONSOLE, consoleManager);	

		if(consoleToShow == null){
			consoleToShow = createNewMessageConsole(DEFAULT_CONSOLE,consoleManager);
		}
//		Fix for : Console window is getting displayed if user maximize canvas window and then try to create new job (Ctrl+J) 
//		consoleManager.showConsoleView(consoleToShow);
	}

	private IConsole getConsole(String consoleName,IConsoleManager consoleManager){		
		IConsole[] existing = consoleManager.getConsoles();
		MessageConsole messageConsole=null;
		for (int i = 0; i < existing.length; i++) {
			if (existing[i].getName().equals(consoleName)){
				messageConsole=(MessageConsole) existing[i];
				return messageConsole;
			}	
		}

		return null;
	}


	private MessageConsole createNewMessageConsole(String consoleName,IConsoleManager consoleManager) {
		MessageConsole messageConsole;
		messageConsole = new MessageConsole(consoleName, null);
		consoleManager.addConsoles(new IConsole[] { messageConsole });
		logger.debug("Created message console");
		return messageConsole;
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
		configureKeyboardShortcuts();
		//handleKeyStrokes(viewer);
	}

	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		final ELTGraphicalEditor editor = this;
		return new PaletteViewerProvider(getEditDomain()) {

			@Override
			protected void configurePaletteViewer(final PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.setEditPartFactory(new CustomPaletteEditPartFactory(palatteTextColor));
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

	private void createShapesDrawer(PaletteRoot palette) throws RuntimeException, SAXException, IOException {
		Map<String, PaletteDrawer> categoryPaletteConatiner = new HashMap<>();
		for (CategoryType category : CategoryType.values()) {
			if(category.name().equalsIgnoreCase(Constants.DUMMY_COMPONENT_CATEGORY)){
				continue;
			}				
			PaletteDrawer p = createPaletteContainer(category.name());
			addContainerToPalette(palette, p);
			categoryPaletteConatiner.put(category.name(), p);
		}
		List<Component> componentsConfig = XMLConfigUtil.INSTANCE.getComponentConfig();
		
		//To show the components in sorted order in palette
		Collections.sort(componentsConfig, new Comparator<Component>() {
			public int compare(Component component1, Component component2) {
				return 	component1.getNameInPalette().compareToIgnoreCase(component2.getNameInPalette());			
			};
		});
				
		for (Component componentConfig : componentsConfig) {
			Class<?> clazz = DynamicClassProcessor.INSTANCE.createClass(componentConfig);

			if(componentConfig.getName().equalsIgnoreCase(Constants.DUMMY_COMPONENT)){
				continue;
			}

			CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
					componentConfig.getNameInPalette(), null, clazz,
					new SimpleFactory(clazz),
					ImageDescriptor.createFromURL(
							prepareIconPathURL(componentConfig.getPaletteIconPath())),
							ImageDescriptor.createFromURL(
									prepareIconPathURL(componentConfig.getPaletteIconPath())));
			categoryPaletteConatiner.get(componentConfig.getCategory().name()).add(component);
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
	public TransferDropTargetListener createTransferDropTargetListener() {
		return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
			@Override
			protected CreationFactory getFactory(Object template) {
				return new SimpleFactory((Class) template);
			}
		};
	}

	public ISelectionChangedListener createISelectionChangedListener() {
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

	@Override
	public void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		// ...
		
		IAction deleteAction;
		deleteAction = new DeleteAction(this);
		registry.registerAction(deleteAction);
		getSelectionActions().add(deleteAction.getId());
		
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

		action=new SubJobAction(this, pasteAction);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action=new SubJobOpenAction(this, pasteAction);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action=new SubJobUpdateAction(this, pasteAction);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new AddWatcherAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new WatchRecordAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new RemoveWatcherAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action=new ComponentHelpAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action=new ComponentPropertiesAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
 
		action= new GraphRuntimePropertiesAction(this,pasteAction);
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

		List<String> zoomLevels = new ArrayList<String>(3);
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
		if (dirty){
			setMainGraphDirty(dirty);
		}			
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}


	private void setMainGraphDirty(boolean dirty) {
		if(container.getLinkedMainGraphPath()!=null && container.isCurrentGraphIsSubjob()){
			for(IEditorReference editorReference:PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences()){
				if(StringUtils.equals(editorReference.getTitleToolTip(), container.getLinkedMainGraphPath())){
					if(editorReference.getEditor(false) instanceof ELTGraphicalEditor);
					((ELTGraphicalEditor)editorReference.getEditor(false)).setDirty(true);
				}
			}
			
		}
	}

	@Override
	public void setInput(IEditorInput input) {
		

		try {
			GenrateContainerData genrateContainerData = new GenrateContainerData();
			genrateContainerData.setEditorInput(input, this);
			container = genrateContainerData.getContainerData();
			super.setInput(input);
		} catch (CoreException | IOException ce) {
			logger.error("Exception while setting input", ce);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().dispose();
			MessageDialog.openError(new Shell(), "Error", "Exception occured while opening the graph -\n"+ce.getMessage());
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		String METHOD_NAME = "doSave -";
		logger.debug(METHOD_NAME);

		firePropertyChange(PROP_DIRTY);
		try {
			GenrateContainerData genrateContainerData = new GenrateContainerData();
			genrateContainerData.setEditorInput(getEditorInput(), this);
			genrateContainerData.storeContainerData();
			
			saveParameters();
			updateMainGraphOnSavingSubjob();
		} catch (CoreException | IOException ce) {
			logger.error(METHOD_NAME , ce);
			MessageDialog.openError(new Shell(), "Error", "Exception occured while saving the graph -\n"+ce.getMessage());
		}	
	}

	public void saveParameters() {

		//get map from file
		Map<String,String> currentParameterMap = getCurrentParameterMap();
		if(currentParameterMap == null){
			return;
		}
		List<String> letestParameterList = getLatestParameterList();

		Map<String,String> newParameterMap = new LinkedHashMap<>();

		for(int i=0;i<letestParameterList.size();i++){
			newParameterMap.put(letestParameterList.get(i), "");
		}

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
		    jobId = activeProjectName.concat("_").concat(getPartName().replace(".job", "_"));

			return parameterFileIPath;
		}else{
			return null;
		}

	}


	
	public String  generateUniqueJobId() throws NoSuchAlgorithmException{
		SecureRandom random =  SecureRandom.getInstance("SHA1PRNG");
		int number=0;
		 for (int i = 0; i < 20; i++) {
			 number= random.nextInt(21);
		     }
		 	byte[] secureRandom = random.getSeed(number);
		    long milliSeconds = System.currentTimeMillis();
		    String timeStampLong = Long.toString(milliSeconds);
		    this.uniqueJobId=jobId.concat(""+secureRandom.hashCode()).concat("_"+timeStampLong);
		    
		    return uniqueJobId;
	}
	
	@Override
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


	@Override
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
		}else{
			return null;
		}
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

		out.write(CanvasUtils.INSTANCE.fromObjectToXML(getContainer()).getBytes());
	}

	/**
	 * Returns job continer
	 * @return {@link Container}
	 */
	public Container getContainer() {
		return container;
	}

	@Override
	public void doSaveAs() {
		IFile file=opeSaveAsDialog();
		saveJob(file);
	}

	public void saveJob(IFile file) {
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
				genrateTargetXml(file,null,null);
				getCommandStack().markSaveLocation();
			} catch (CoreException  | IOException ce) {
				logger.error("Failed to Save the file : ", ce);
				MessageDialog.openError(new Shell(), "Error", "Exception occured while saving the graph -\n"+ce.getMessage());
			}
			setDirty(false);
		}
	}

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

	/**
	 * 
	 * Validates length of job name
	 * 
	 * @param {@link SaveAsDialog}
	 */
	public void validateLengthOfJobName(SaveAsDialog saveAsDialog) {
		String jobName=saveAsDialog.getResult().removeFileExtension().lastSegment();
		while(jobName.length()>50)
		{
			jobName=saveAsDialog.getResult().removeFileExtension().lastSegment();
			if(jobName.length()>50)
			{
				MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
				messageBox.setText("Error");
				messageBox.setMessage("File Name Too Long");
				if(messageBox.open()==SWT.OK)
				{
					saveAsDialog.setOriginalName(jobName+".job");
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(saveAsDialog.getResult());
					saveAsDialog.setOriginalFile(file);
					saveAsDialog.open();
					if(saveAsDialog.getReturnCode()==1)
						break;
				}
			}
		}
	}

	/**
	 * Genrate target xml.
	 * 
	 * @param ifile
	 *            the ifile
	 */
	public void genrateTargetXml(IFile ifile, IFileStore fileStore, Container container) {
		logger.debug("Genrating target XML");
		if (ifile != null)
			generateTargetXMLInWorkspace(ifile, container);
		else if (fileStore != null)
			generateTargetXMLInLocalFileSystem(fileStore, container);
	}

	private void generateTargetXMLInWorkspace(IFile ifile, Container container) {
		IFile outPutFile = ResourcesPlugin.getWorkspace().getRoot().getFile(ifile.getFullPath().removeFileExtension().addFileExtension("xml"));
		try {
			if(container!=null)
				ConverterUtil.INSTANCE.convertToXML(container, false, outPutFile,null);
			else
				ConverterUtil.INSTANCE.convertToXML(this.container, false, outPutFile,null);
		} catch (EngineException eexception) {
			logger.warn("Failed to create the engine xml", eexception);
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Failed to create the engine xml", eexception.getMessage());
			//			
		}catch (InstantiationException|IllegalAccessException| InvocationTargetException| NoSuchMethodException exception) {
			logger.error("Failed to create the engine xml", exception);
			Status status = new Status(IStatus.ERROR, "hydrograph.ui.graph",
					"Failed to create Engine XML " + exception.getMessage());
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		}
		
	}



	private void generateTargetXMLInLocalFileSystem(IFileStore fileStore, Container container) {

		try {
			if(container!=null)
				ConverterUtil.INSTANCE.convertToXML(container, false, null,fileStore);
			else
				ConverterUtil.INSTANCE.convertToXML(this.container, false,null,fileStore);
		} catch (EngineException eexception) {
			logger.warn("Failed to create the engine xml", eexception);
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Failed to create the engine xml", eexception.getMessage());
		}catch (InstantiationException| IllegalAccessException| InvocationTargetException| NoSuchMethodException exception) {
			logger.error("Failed to create the engine xml", exception);
			Status status = new Status(IStatus.ERROR, "hydrograph.ui.graph",
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
		removeSubjobProperties(isDirty());
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(new ResourceChangeListener(this));
		logger.debug("Job closed");
		
		closeDataViewerWindows();
		
		deleteDebugFiles();
		
		closeSocket();
	}
	
	private void closeDataViewerWindows() {
		String currentJob = getEditorInput().getName().replace(Constants.JOB_EXTENSION, "");
		Job job = DebugHandler.getJob(currentJob);
		
		for(String windowName:JobManager.INSTANCE.getDataViewerMap().keySet()){
			if(StringUtils.contains(windowName, job.getConsoleName().replace(".", "_"))){
				JobManager.INSTANCE.getDataViewerMap().get(windowName).close();
			}
		}
	}

	private void closeSocket()  {
        int portPID;
        try {
              portPID = Integer.parseInt(DebugHelper.INSTANCE.getServicePortPID());
              DebugHelper.INSTANCE.killPortProcess(portPID);
        } catch (NumberFormatException e) {
              logger.debug("Socket id is not correct");
        } catch (IOException e) {
              logger.debug("Socket is not closed.");
        }
  }

	
	private void deleteDebugFiles() {
		String currentJob = getEditorInput().getName().replace(Constants.JOB_EXTENSION, "");
		Job job = DebugHandler.getJob(currentJob);
		deleteDebugFileFromWorkspace();
		if(job == null){
			logger.debug("current job {} wasn't found in Debughandler's map",currentJob);
			return ;
		}
		
		DebugHandler.getJobMap().remove(currentJob);
		
	}

	private void deleteDebugFileFromWorkspace() {
		if (getEditorInput() instanceof FileEditorInput) {
			IPath fileIpath = ((FileEditorInput) getEditorInput()).getFile().getFullPath();
			if (fileIpath != null) {
				String debugFileName = fileIpath.removeFileExtension().lastSegment() + Constants.DEBUG_EXTENSION;
				IPath debugFileiPath = fileIpath.removeLastSegments(1).append(debugFileName);						
				try {
					ResourcesPlugin.getWorkspace().getRoot().getFile(debugFileiPath).delete(true, null);
				} catch (CoreException e) {
					logger.warn("CoreException occurred while deleting debug file", e);
				}
			}
		}
	}
	

	
	private void removeSubjobProperties(Boolean isDirty) {
		if (isDirty) {
			loadFileAndDeleteSubjobProperties();
		} else if (deleteSubjobProperties(getContainer())!=null)
			doSave(null);
	}		

	private void loadFileAndDeleteSubjobProperties() {
		if(getEditorInput() instanceof IFileEditorInput){
			FileEditorInput fileEditorInput=(FileEditorInput) getEditorInput();
				stroeFileInWorkspace(fileEditorInput.getFile());
		}else if(getEditorInput() instanceof FileStoreEditorInput){
				FileStoreEditorInput fileStoreEditorInput=(FileStoreEditorInput) getEditorInput();
					stroeFileInLocalFS(fileStoreEditorInput.getToolTipText());
		}
	}

	private void stroeFileInLocalFS(String jobFilePath) {
		File file=null;
		if ( StringUtils.isNotBlank(jobFilePath)) {
			file = new File(jobFilePath);
		}
		if (file != null) {
			XStream xStream = new XStream();
			Container container = (Container) xStream.fromXML(file);
			container = deleteSubjobProperties(container);
			if (container != null) {
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					xStream.toXML(container, fileOutputStream);

				} catch (FileNotFoundException eFileNotFoundException) {
					logger.error("Exception occurred while saving sub-graph into local file-system when editor disposed",
							eFileNotFoundException);
				}
			}
		}

	}
	
	private void stroeFileInWorkspace(IFile iFile) {
		InputStream filenputStream = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (iFile != null) {
			try {
				filenputStream = iFile.getContents(true);
				if (filenputStream != null) {
					XStream xStream = new XStream();
					Container container = (Container) xStream.fromXML(filenputStream);
					filenputStream.close();
					container = deleteSubjobProperties(container);
					if (container != null) {
						xStream.toXML(container, out);
						if (iFile.exists())
							iFile.setContents(new ByteArrayInputStream(out.toByteArray()), true,	false, null);
					}
				}
			} catch (FileNotFoundException eFileNotFoundException) {
				logger.error("Exception occurred while saving sub-graph into local file-system when editor disposed",
						eFileNotFoundException);
			} catch (IOException ioException) {
				logger.error("Exception occurred while saving sub-graph into local file-system when editor disposed",
						ioException);
			} catch (CoreException coreException) {
				logger.error("Exception occurred while saving sub-graph into local file-system when editor disposed",
						coreException);
			}
		}
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
		IPath xmlPath = null;
		if (getEditorInput() instanceof IFileEditorInput) {
			xmlPath = ((IFileEditorInput) getEditorInput()).getFile().getLocation();
		} else if (getEditorInput() instanceof FileStoreEditorInput) {
			xmlPath = new Path(getEditorInput().getToolTipText());
		}
		return getStringValueFromXMLFile(xmlPath);
	}

	/**
	 * This method ret
	 * 
	 * @param xmlPath
	 * @return
	 */
	public String getStringValueFromXMLFile(IPath xmlPath) {
		if (xmlPath != null) {
			InputStream inputStream = null;
			String content = "";
			try {
				xmlPath = xmlPath.removeFileExtension().addFileExtension(Constants.XML_EXTENSION_FOR_IPATH);
				if (xmlPath.toFile().exists())
					inputStream = new FileInputStream(xmlPath.toFile());
				else if (ResourcesPlugin.getWorkspace().getRoot().getFile(xmlPath).exists())
					inputStream = ResourcesPlugin.getWorkspace().getRoot().getFile(xmlPath).getContents();
				if (inputStream != null)
					content = new Scanner(inputStream).useDelimiter("\\Z").next();
				return content;
			} catch (FileNotFoundException | CoreException exception) {
				logger.error("Exception occurred while fetching data from " + xmlPath.toString(), exception);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException ioException) {
						logger.warn("Exception occurred while closing the inpustream", ioException);
					}
				}

			}
		}
		return "";
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
				logger.error("Unable to disable running job resources", e);
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
			logger.error("Unable to enable locked job resources",e);
		}

	}



	@Override
	public void setStopButtonStatus(boolean enabled) {
		stopButtonStatus = enabled;		
	}



	@Override
	public boolean getStopButtonStatus() {
		return stopButtonStatus;
	}

	public String getJobId() {
		return uniqueJobId;
	}
 
	public Container deleteSubjobProperties(Container container) {
		hydrograph.ui.graph.model.Component oldSubjob=null;
		if (container!=null && container.isCurrentGraphIsSubjob()) {

			for (int i = 0; i < container.getChildren().size(); i++) {
				if (Constants.INPUT_SUBJOB.equalsIgnoreCase(container.getChildren().get(i).getComponentName())) {
					container.getChildren().get(i).getProperties().put(Constants.SCHEMA_TO_PROPAGATE, new HashMap<>());
				}
				if (Constants.OUTPUT_SUBJOB.equalsIgnoreCase(container.getChildren().get(i).getComponentName())) {
					oldSubjob=(hydrograph.ui.graph.model.Component) container.getChildren().get(i).getProperties().put(Constants.SUBJOB_COMPONENT, null);
				}
			}
			return container;
		} else
			return null;
	}

	private void updateMainGraphOnSavingSubjob() {
		hydrograph.ui.graph.model.Component subjobComponent=null;
		if (container != null && container.isCurrentGraphIsSubjob()) {
			for (int i = 0; i < container.getChildren().size(); i++) {
				if (Constants.OUTPUT_SUBJOB.equalsIgnoreCase(container.getChildren().get(i).getComponentName())) {
					subjobComponent = (hydrograph.ui.graph.model.Component) container.getChildren().get(i)
							.getProperties().get(Constants.SUBJOB_COMPONENT);
					break;
				}
			}
			if(subjobComponent!=null){
				String path=getEditorInput().getToolTipText();
				if (getEditorInput() instanceof IFileEditorInput)
					path = ((IFileEditorInput) getEditorInput()).getFile().getFullPath().toString();
				IPath subJobFilePath=new Path(path);
				SubJobUtility subJobUtility=new SubJobUtility();
				subJobUtility.updateContainerAndSubjob(container, subjobComponent, subJobFilePath);
				((ComponentEditPart)container.getSubjobComponentEditPart()).changePortSettings();
			}
		}
	}
	
	private void configureKeyboardShortcuts() {
	    GraphicalViewerKeyHandler keyHandler = new GraphicalViewerKeyHandler(getGraphicalViewer());
	    keyHandler.put(KeyStroke.getPressed(SWT.F4,0), getActionRegistry().getAction(Constants.SUBJOB_OPEN));
	    getGraphicalViewer().setKeyHandler(keyHandler);

	  }

	public GraphicalViewer getViewer() {
		return viewer;
	}

	@Override
	public void restoreMenuToolContextItemsState() {
		ContributionItemManager.UndoRedoDefaultBarManager.changeUndoRedoStatus(getViewer());
	}
	
}
