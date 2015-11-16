package com.bitwise.app.graph.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import com.bitwise.app.common.component.config.CategoryType;
import com.bitwise.app.common.component.config.Component;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;

/**
 *Creates Custom Palette Viewer 
 * @author Bitwise
 *
 */
public class CustomPaletteViewer extends PaletteViewer {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(CustomPaletteViewer.class);
	private Label label;

	/**
	 * create searchTextBox and show components according to text entered in search box
	 * @param parent
	 * @param paletteRoot
	 * @param editor
	 * @return Control
	 */
	public Control creatSearchTextBox(Composite parent, final PaletteRoot paletteRoot, final ETLGraphicalEditor editor) {
		final Map<String, PaletteDrawer> categoryPaletteConatiner = new HashMap<>();
		Composite container = createCompositeForSearchTextBox(parent);
		final Text text = createSearchTextBox(container,SWT.BORDER);
		try {
			final List<Component> componentsConfig = XMLConfigUtil.INSTANCE.getComponentConfig();
			refreshThePaletteBasedOnSearchString(paletteRoot, editor, categoryPaletteConatiner, text,
					componentsConfig, container);

		} catch (RuntimeException|SAXException|IOException exception) {
			LOGGER.error(exception.getMessage(),exception);
		}
		return container;
	}

	private void refreshThePaletteBasedOnSearchString(final PaletteRoot paletteRoot,
			final ETLGraphicalEditor editor, final Map<String, PaletteDrawer> categoryPaletteConatiner,
			final Text text, final List<Component> componentsConfig, final Composite container) {
		text.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				boolean matchFound = false;
				final List<Component> matchingComponents = new ArrayList<>();
				paletteRoot.getChildren().clear();
				String currentText = ((Text) e.widget).getText();
				createPaletteContainers(paletteRoot, categoryPaletteConatiner, editor);
				String searchedString = (currentText.substring(0, e.start) + e.text + currentText.substring(e.end))
						.toUpperCase();
					if (searchedString.isEmpty() || "".equals(searchedString)) {
						showAllContainers(paletteRoot, editor, categoryPaletteConatiner, componentsConfig);
					} else {
						matchFound = collectMatchingContainers(editor, categoryPaletteConatiner, componentsConfig,
								 matchingComponents, searchedString);
						showMessageWhenComponentNotFound(container, matchFound);
						showMatchingContainers(paletteRoot, categoryPaletteConatiner, matchingComponents);
					}
			}
		});
	}
	
	private void showMessageWhenComponentNotFound(final Composite container, boolean matchFound) {
		if (!matchFound) {
			if (label != null)
				label.dispose();
			label = new Label(container, SWT.LEFT);
			label.setText("No component found");
		}
	}

	private boolean collectMatchingContainers(final ETLGraphicalEditor editor,
			final Map<String, PaletteDrawer> categoryPaletteConatiner, final List<Component> componentsConfig,
			 List<Component> matchingComponents, String searchedString) {
		boolean matchFound = false;
		for (Component componentConfig : componentsConfig) {
			String componentName = componentConfig.getName().toUpperCase();
			if (componentName.contains(searchedString.trim())) {
				CombinedTemplateCreationEntry component = getComponentToAddInContainer(editor,
						componentConfig);
				categoryPaletteConatiner.get(componentConfig.getCategory().name()).add(component);
				matchingComponents.add(componentConfig);
				matchFound = true;
				if (label != null) {
					label.dispose();

				}
			}
		}
		return matchFound;
	}

	private void showAllContainers(final PaletteRoot paletteRoot, final ETLGraphicalEditor editor,
			final Map<String, PaletteDrawer> categoryPaletteConatiner, final List<Component> componentsConfig) {
		for (Component componentConfig : componentsConfig) {
			CombinedTemplateCreationEntry component = getComponentToAddInContainer(editor,
					componentConfig);
			categoryPaletteConatiner.get(componentConfig.getCategory().name()).add(component);
			showClosedPaletteContainersWhenSearchTextBoxIsEmpty(paletteRoot.getChildren());
		}
		if (label != null) {
			label.dispose();
		}
	}

	private void showClosedPaletteContainersWhenSearchTextBoxIsEmpty(List list) {
		for (int i = 0; i < list.size(); i++) {
			PaletteDrawer paletteDrawer = (PaletteDrawer) list.get(i);
			paletteDrawer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
			paletteDrawer.setVisible(true);
		}
	}

	private void showMatchingContainers(PaletteRoot paletteRoot,
			Map<String, PaletteDrawer> categoryPaletteConatiner, List<Component> matchingComponents) {
		List<PaletteContainer> children = paletteRoot.getChildren();
		for (PaletteContainer paletteContainer : children) {
			paletteContainer.setVisible(false);

		}
		for (Component component : matchingComponents) {
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i).equals(categoryPaletteConatiner.get(component.getCategory().name()))) {
					children.get(i).setVisible(true);
				}
			}
		}

	}

	private void createPaletteContainers(PaletteRoot paletteRoot, Map<String, PaletteDrawer> categoryPaletteConatiner,
			ETLGraphicalEditor eLEtlGraphicalEditor) {
		for (CategoryType category : CategoryType.values()) {
			PaletteDrawer paletteDrawer = eLEtlGraphicalEditor.createPaletteContainer(category.name());
			paletteDrawer.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
			eLEtlGraphicalEditor.addContainerToPalette(paletteRoot, paletteDrawer);
			categoryPaletteConatiner.put(category.name(), paletteDrawer);
		}
	}

	private CombinedTemplateCreationEntry getComponentToAddInContainer(ETLGraphicalEditor eLEtlGraphicalEditor,
			Component componentConfig) {
		Class<?> clazz = DynamicClassProcessor.INSTANCE.createClass(componentConfig);

		CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(componentConfig.getName(),
				"Custom components", clazz, new SimpleFactory(clazz),
				ImageDescriptor.createFromURL(eLEtlGraphicalEditor.prepareIconPathURL(componentConfig
						.getPaletteIconPath())), ImageDescriptor.createFromURL(eLEtlGraphicalEditor
						.prepareIconPathURL(componentConfig.getPaletteIconPath())));
		return component;
	}

	private Composite createCompositeForSearchTextBox(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		FillLayout layout = new FillLayout();
		layout.type = SWT.VERTICAL;
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		layout.spacing = 7;
		container.setLayout(layout);
		return container;
	}

	private Text createSearchTextBox(Composite container,int border) {
		Text text = new Text(container, border);
		text.setToolTipText("Enter component name");
		text.setMessage("Search component");
		return text;
	}

	public LightweightSystem getLightweightSys() {
		return getLightweightSystem();
	}

	public void setFigureCanvas(FigureCanvas canvas) {
		setControl(canvas);
		callHookRootFigure();
	}


	private void callHookRootFigure() {
		if (getFigureCanvas() == null)
			return;
		if (((GraphicalEditPart) getRootEditPart()).getFigure() instanceof Viewport) {
			getFigureCanvas().setViewport((Viewport) ((GraphicalEditPart) getRootEditPart()).getFigure());
		} else {
			getFigureCanvas().setContents(((GraphicalEditPart) getRootEditPart()).getFigure());
		}
	}
}
