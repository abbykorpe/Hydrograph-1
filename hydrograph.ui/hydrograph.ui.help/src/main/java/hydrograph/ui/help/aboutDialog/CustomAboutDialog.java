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

 
package hydrograph.ui.help.aboutDialog;

import hydrograph.ui.help.Activator;

import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.about.AboutFeaturesButtonManager;
import org.eclipse.ui.internal.about.AboutItem;
import org.eclipse.ui.internal.about.AboutTextManager;
import org.eclipse.ui.internal.about.InstallationDialog;
import org.eclipse.ui.internal.util.BundleUtility;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.osgi.framework.Bundle;


/**
 *Creates Custom AboutDialog of Application
 * @author Bitwise
 *
 */
public class CustomAboutDialog extends TrayDialog {
	private static final String ECLIPSE_BUILD_ID = "eclipse.buildId";
	private final static int MAX_IMAGE_WIDTH_FOR_TEXT = 250;
	private final static int TEXT_MARGIN = 5;

	private final static int DETAILS_ID = IDialogConstants.CLIENT_ID + 1;

	private String productName;

	private IProduct product;


	private ArrayList<Image> images = new ArrayList<Image>();

	private AboutFeaturesButtonManager buttonManager = new AboutFeaturesButtonManager();

	private StyledText text;

	private AboutTextManager aboutTextManager;

	private AboutItem item;

	/**
	 * Create an instance of the AboutDialog for the given window.
	 * @param parentShell The parent of the dialog.
	 */
	public CustomAboutDialog(Shell parentShell) {
		super(parentShell);

		product = Platform.getProduct();

		if (product != null) {
			productName = product.getName();
		}
		if (productName == null) {
			productName = WorkbenchMessages.AboutDialog_defaultProductName;
		}
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case DETAILS_ID:
			BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
				public void run() {
					IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					InstallationDialog dialog = new InstallationDialog(getShell(), workbenchWindow);
					dialog.setModalParent(CustomAboutDialog.this);
					dialog.open();	
				}
			});
			break;
		default:
			super.buttonPressed(buttonId);
			break;
		}
	}

	public boolean close() {
		// dispose all images
		for (int i = 0; i < images.size(); ++i) {
			Image image = (Image) images.get(i);
			image.dispose();
		}

		return super.close();
	}

	/*
	 * (non-Javadoc) Method declared on Window.
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(NLS.bind(WorkbenchMessages.AboutDialog_shellTitle,productName ));

	}

	/**
	 * Add buttons to the dialog's button bar.
	 * 
	 * Subclasses should override.
	 * 
	 * @param parent
	 *            the button bar composite
	 */
	//	    protected void createButtonsForButtonBar(Composite parent) {
	//	        parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	//
	//	        createButton(parent, DETAILS_ID, WorkbenchMessages.AboutDialog_DetailsButton, false); 
	//
	//	        Label l = new Label(parent, SWT.NONE);
	//	        l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	//	        GridLayout layout = (GridLayout) parent.getLayout();
	//	        layout.numColumns++;
	//	        layout.makeColumnsEqualWidth = false;
	//
	//	        Button b = createButton(parent, IDialogConstants.OK_ID,
	//	                IDialogConstants.OK_LABEL, true);
	//	        b.setFocus();
	//	    }

	/**
	 * Creates and returns the contents of the upper part 
	 * of the dialog (above the button bar).
	 *
	 * Subclasses should overide.
	 *
	 * @param parent  the parent composite to contain the dialog area
	 * @return the dialog area control
	 */
	protected Control createDialogArea(Composite parent) {
		// brand the about box if there is product info
		Image aboutImage = null;
		item = null;
		if (product != null) {
			Bundle bundle = Platform.getBundle(Constants.ABOUT_DIALOG_IMAGE_BUNDLE_NAME);
			URL fullPathString = BundleUtility.find(bundle,Constants.ABOUT_DIALOG_IMAGE_PATH);
			aboutImage=ImageDescriptor.createFromURL(fullPathString).createImage();

			// if the about image is small enough, then show the text
			if (aboutImage == null
					|| aboutImage.getBounds().width <= MAX_IMAGE_WIDTH_FOR_TEXT) {
				String aboutText=Constants.ABOUT_TEXT;


				if (aboutText != null) {
					String buildNumber = System.getProperty(ECLIPSE_BUILD_ID);
					if(StringUtils.isBlank(buildNumber)){
						buildNumber = Platform.getBundle(Activator.PLUGIN_ID).getVersion().toString();
					}
					item = AboutTextManager.scan(aboutText + "\n" + "Build Number: " + buildNumber);
				}
			}

			if (aboutImage != null) {
				images.add(aboutImage);
			}
		}

		// create a composite which is the parent of the top area and the bottom
		// button bar, this allows there to be a second child of this composite with 
		// a banner background on top but not have on the bottom
		Composite workArea = new Composite(parent, SWT.NONE);
		GridLayout workLayout = new GridLayout();
		workLayout.marginHeight = 0;
		workLayout.marginWidth = 0;
		workLayout.verticalSpacing = 0;
		workLayout.horizontalSpacing = 0;
		workArea.setLayout(workLayout);
		workArea.setLayoutData(new GridData(GridData.FILL_BOTH));

		// page group
		Color background = JFaceColors.getBannerBackground(parent.getDisplay());
		Color foreground = JFaceColors.getBannerForeground(parent.getDisplay());
		Composite top = (Composite) super.createDialogArea(workArea);

		// override any layout inherited from createDialogArea 
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		top.setLayout(layout);
		top.setLayoutData(new GridData(GridData.FILL_BOTH));
		top.setBackground(background);
		top.setForeground(foreground);

		// the image & text	
		final Composite topContainer = new Composite(top, SWT.NONE);
		topContainer.setBackground(background);
		topContainer.setForeground(foreground);

		layout = new GridLayout();
		layout.numColumns = (aboutImage == null || item == null ? 1 : 2);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		topContainer.setLayout(layout);


		// Calculate a good height for the text
		GC gc = new GC(parent);
		int lineHeight = gc.getFontMetrics().getHeight();
		gc.dispose();

		int topContainerHeightHint = 100;

		topContainerHeightHint = Math.max(topContainerHeightHint, lineHeight * 6);

		//image on left side of dialog
		if (aboutImage != null) {
			Label imageLabel = new Label(topContainer, SWT.NONE);
			imageLabel.setBackground(background);
			imageLabel.setForeground(foreground);

			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.verticalAlignment = GridData.BEGINNING;
			data.grabExcessHorizontalSpace = false;
			imageLabel.setLayoutData(data);
			imageLabel.setImage(aboutImage);
			topContainerHeightHint = Math.max(topContainerHeightHint, aboutImage.getBounds().height);
		}

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		topContainer.setLayoutData(data);
		// used only to drive initial size so that there are no hints in the
		// layout data
		topContainer.setSize(432, topContainerHeightHint);

		if (item != null) {
			text = new StyledText(topContainer, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
			configureText(topContainer);

			// computing trim for later
			Rectangle rect = text.computeTrim(0, 0, 100, 100);
			final int xTrim = rect.width - 100;
			final int yTrim = rect.height - 100;

			topContainer.addControlListener(new ControlAdapter() {
				public void controlResized(ControlEvent e) {
					text.setSize(SWT.DEFAULT, SWT.DEFAULT);
					topContainer.layout(true);
					// do we need a scroll bar?
					Point size = text.getSize();
					int availableHeight = size.y - yTrim;
					int availableWidth = size.x - xTrim - (2 * TEXT_MARGIN);
					Point newSize = text.computeSize(availableWidth, SWT.DEFAULT, true);
					int style = text.getStyle();
					if (newSize.y > availableHeight) {
						if ((style & SWT.V_SCROLL) == 0) {
							recreateWrappedText(topContainer, true);
						}
					} else {
						if ((style & SWT.V_SCROLL) != 0) {
							recreateWrappedText(topContainer, false);
						}
					}
					topContainer.layout(true);
				}
			});
		}

		// horizontal bar
		Label bar = new Label(workArea, SWT.HORIZONTAL | SWT.SEPARATOR);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		bar.setLayoutData(data);

		// add image buttons for bundle groups that have them
		Composite bottom = (Composite) super.createDialogArea(workArea);
		// override any layout inherited from createDialogArea 
		layout = new GridLayout();
		bottom.setLayout(layout);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.verticalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;

		bottom.setLayoutData(data);

		createFeatureImageButtonRow(bottom);

		// spacer
		bar = new Label(bottom, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		bar.setLayoutData(data);

		return workArea;
	}

	void recreateWrappedText(Composite parent, boolean withScrolling) {
		int style = text.getStyle();
		if (withScrolling) {
			style |= SWT.V_SCROLL;
		} else {
			style ^= SWT.V_SCROLL;
		}
		boolean hasFocus = text.isFocusControl();
		Point selection = text.getSelection();
		text.dispose();
		text = new StyledText(parent, style);
		configureText(parent);
		if (hasFocus) {
			text.setFocus();
		}
		text.setSelection(selection);
	}

	void configureText(final Composite parent) {
		// Don't set caret to 'null' as this causes
		// https://bugs.eclipse.org/293263.
		// text.setCaret(null);
		Color background = JFaceColors.getBannerBackground(parent.getDisplay());
		Color foreground = JFaceColors.getBannerForeground(parent.getDisplay());

		text.setFont(parent.getFont());
		text.setText(item.getText());
		text.setCursor(null);
		text.setBackground(background);
		text.setForeground(foreground);
		text.setMargins(TEXT_MARGIN, TEXT_MARGIN, TEXT_MARGIN, 0);

		aboutTextManager = new AboutTextManager(text);
		aboutTextManager.setItem(item);

		createTextMenu();

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		text.setLayoutData(gd);
	}

	/**
	 * Create the context menu for the text widget.
	 * 
	 * @since 3.4
	 */
	private void createTextMenu() {
		final MenuManager textManager = new MenuManager();
		textManager.add(new CommandContributionItem(
				new CommandContributionItemParameter(PlatformUI
						.getWorkbench(), null, IWorkbenchCommandConstants.EDIT_COPY,
						CommandContributionItem.STYLE_PUSH)));
		textManager.add(new CommandContributionItem(
				new CommandContributionItemParameter(PlatformUI
						.getWorkbench(), null, IWorkbenchCommandConstants.EDIT_SELECT_ALL,
						CommandContributionItem.STYLE_PUSH)));
		text.setMenu(textManager.createContextMenu(text));
		text.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				textManager.dispose();
			}
		});

	}

	private void createFeatureImageButtonRow(Composite parent) {
		Composite featureContainer = new Composite(parent, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.wrap = true;
		featureContainer.setLayout(rowLayout);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		featureContainer.setLayoutData(data);
		//add image on about dialog
		Bundle bundle = Platform.getBundle(Constants.ABOUT_DIALOG_IMAGE_BUNDLE_NAME);
		URL fullPathString = BundleUtility.find(bundle,Constants.ABOUT_DIALOG_FEATURE_IMAGE_PATH);
		Button button = new Button(featureContainer, SWT.FLAT | SWT.PUSH);
		Image image=ImageDescriptor.createFromURL(fullPathString).createImage();
		button.setImage(image);
		images.add(image);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	protected boolean isResizable() {
		return true;
	}

}



