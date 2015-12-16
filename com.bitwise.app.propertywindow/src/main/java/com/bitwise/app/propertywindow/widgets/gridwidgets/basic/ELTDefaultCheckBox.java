package com.bitwise.app.propertywindow.widgets.gridwidgets.basic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

// TODO: Auto-generated Javadoc
/**
 * The Class ELTDefaultCheckBox.
 * 
 * @author Bitwise
 */
public class ELTDefaultCheckBox extends AbstractELTWidget {
	private Button defaultELTCheckBox;
	private int checkBoxWidth = 75;
	private String checkBoxLable = "Checkbox";
	private boolean grabExcessSpace = false;

	@Override
	public void attachWidget(Composite container) {
		defaultELTCheckBox = new Button(container, SWT.CHECK);
		GridData gd_defaultELTCheckBox = new GridData(SWT.FILL, SWT.CENTER,
				grabExcessSpace, false, 1, 1);
		gd_defaultELTCheckBox.widthHint = checkBoxWidth;
		defaultELTCheckBox.setLayoutData(gd_defaultELTCheckBox);

		defaultELTCheckBox.setText(checkBoxLable);
		widget = defaultELTCheckBox;
	}

	/**
	 * Instantiates a new ELT default check box.
	 * 
	 * @param checkBoxLable
	 *            the check box lable
	 */
	public ELTDefaultCheckBox(String checkBoxLable) {
		super();
		this.checkBoxLable = checkBoxLable;
	}

	/**
	 * Check box lable width.
	 * 
	 * @param width
	 *            the width
	 * @return the ELT default check box
	 */
	public ELTDefaultCheckBox checkBoxLableWidth(int width) {
		checkBoxWidth = width;
		return this;
	}

	/**
	 * Grab excess horizontal space.
	 * 
	 * @param grabExcessSpace
	 *            the grab excess space
	 * @return the ELT default check box
	 */
	public ELTDefaultCheckBox grabExcessHorizontalSpace(boolean grabExcessSpace) {
		this.grabExcessSpace = grabExcessSpace;
		return this;
	}
}
