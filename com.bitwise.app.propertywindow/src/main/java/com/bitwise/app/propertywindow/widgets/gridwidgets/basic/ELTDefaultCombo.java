package com.bitwise.app.propertywindow.widgets.gridwidgets.basic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

// TODO: Auto-generated Javadoc
/**
 * The Class ELTDefaultCombo.
 * 
 * @author Bitwise
 */
public class ELTDefaultCombo extends AbstractELTWidget {
	private Combo defaultELTcom;
	private int textboxWidth = 65;
	private String[] defaultTextMessage;

	@Override
	public void attachWidget(Composite container) {
		defaultELTcom = new Combo(container, SWT.READ_ONLY);
		defaultELTcom.setItems(defaultTextMessage);
		// defaultELTcom.setItems(new String[] {"True","false"});
		// defaultELTcom.setItem(0, "");
		GridData gd_defaultELTTextBox = new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1);
		gd_defaultELTTextBox.widthHint = textboxWidth;
		gd_defaultELTTextBox.horizontalIndent=-2;
		defaultELTcom.setLayoutData(gd_defaultELTTextBox);
		gd_defaultELTTextBox.horizontalIndent = -2;

		widget = defaultELTcom;
	}

	/**
	 * Default text.
	 * 
	 * @param defaultTextMessage
	 *            the default text message
	 * @return the ELT default combo
	 */
	public ELTDefaultCombo defaultText(String[] defaultTextMessage) {
		this.defaultTextMessage = defaultTextMessage;
		return this;
	}

	/**
	 * Grab excess horizontal space.
	 * 
	 * @param grabExcessSpace
	 *            the grab excess space
	 * @return the ELT default combo
	 */
	public ELTDefaultCombo grabExcessHorizontalSpace(boolean grabExcessSpace) {
		return this;
	}

	/**
	 * Combo box width.
	 * 
	 * @param textboxWidth
	 *            the textbox width
	 * @return the ELT default combo
	 */
	public ELTDefaultCombo comboBoxWidth(int textboxWidth) {
		this.textboxWidth = textboxWidth;
		return this;
	}
}
