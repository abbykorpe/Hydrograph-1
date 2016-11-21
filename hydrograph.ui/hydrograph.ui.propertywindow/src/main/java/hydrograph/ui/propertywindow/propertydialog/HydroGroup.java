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

package hydrograph.ui.propertywindow.propertydialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Font;

/**
 * The custom SWT group widget
 * 
 * @author shrirangk
 *
 */
public class HydroGroup extends Composite {
	private CLabel hydroGroupLabel;
	private Composite hydroGroupBoder;
	private Composite hydroGroupClientArea;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public HydroGroup(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		setData("org.eclipse.e4.ui.css.id", "HydrographGroup");
		
		hydroGroupLabel = new CLabel(this, SWT.NONE);
		hydroGroupLabel.setText("Hydro Group Label");
		hydroGroupLabel.setData("org.eclipse.e4.ui.css.id", "HydroGroupLabel");
		
		hydroGroupBoder = new Composite(this, SWT.NONE);
		GridLayout gl_hydroGroupBoder = new GridLayout(1, false);
		gl_hydroGroupBoder.verticalSpacing = 1;
		gl_hydroGroupBoder.marginWidth = 1;
		gl_hydroGroupBoder.marginHeight = 1;
		gl_hydroGroupBoder.horizontalSpacing = 1;
		hydroGroupBoder.setLayout(gl_hydroGroupBoder);
		hydroGroupBoder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		hydroGroupBoder.setBackground(new Color(null, 0, 0, 0));
		hydroGroupBoder.setData("org.eclipse.e4.ui.css.id", "HydroGroupBorder");
		
		hydroGroupClientArea = new Composite(hydroGroupBoder, SWT.NONE);
		hydroGroupClientArea.setLayout(new GridLayout(1, false));
		hydroGroupClientArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		hydroGroupClientArea.setData("org.eclipse.e4.ui.css.id", "HydroGroupClientArea");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	public String getHydroGroupText() {
		return hydroGroupLabel.getText();
	}
	public void setHydroGroupText(String text) {
		hydroGroupLabel.setText(text);
	}
	public Image getHydroGroupImage() {
		return hydroGroupLabel.getImage();
	}
	public void setHydroGroupImage(Image image) {
		hydroGroupLabel.setImage(image);
	}	
	
	public Color getHydroGroupBoderBackground() {
		return hydroGroupBoder.getBackground();
	}
	public void setHydroGroupBoderBackground(Color background) {
		hydroGroupBoder.setBackground(background);
	}
	
	public Color getHydroGroupWidgetBackground() {
		return getBackground();
	}
	public void setHydroGroupWidgetBackground(Color background) {
		setBackground(background);
		hydroGroupLabel.setBackground(background);
	}	
	public Color getHydroGroupWidgetForeground() {
		return hydroGroupLabel.getBackground();
	}
	public void setHydroGroupWidgetForeground(Color background) {
		hydroGroupLabel.setForeground(background);
	}
	public Font getHydroGroupLabelFont() {
		return hydroGroupLabel.getFont();
	}
	public void setHydroGroupLabelFont(Font font) {
		hydroGroupLabel.setFont(font);
	}
	public Color getHydroGroupClientAreaBackgroundColor() {
		return hydroGroupClientArea.getBackground();
	}
	public void setHydroGroupClientAreaBackgroundColor(Color background) {
		hydroGroupClientArea.setBackground(background);
	}
	public Composite getHydroGroupClientArea() {
		return hydroGroupClientArea;
	}
}
