package com.bitwise.app.propertywindow.widgets.gridwidgets.basic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

// TODO: Auto-generated Javadoc
/**
 * 
 * @author Bitwise
 * Sep 18, 2015
 * 
 */

public class ELTDefaultButton extends AbstractELTWidget{
	private Button defaultELTButton;
	
	private String buttonText="Button";
	private int buttonWidth=92;
	private int buttonHeight=25;
	private boolean grabExcessSpace=false;
	
	/**
	 * Instantiates a new ELT default button.
	 * 
	 * @param buttonText
	 *            the button text
	 */
	public ELTDefaultButton(String buttonText){
		this.buttonText = buttonText;
	}
	
	@Override
	public void attachWidget(Composite container) {
		defaultELTButton = new Button(container, SWT.CENTER);
		GridData gd_defaultELTButton = new GridData(SWT.FILL, SWT.FILL, grabExcessSpace, true, 1, 1);
		gd_defaultELTButton.widthHint = buttonWidth;
		gd_defaultELTButton.horizontalIndent=-3;
		gd_defaultELTButton.heightHint = buttonHeight;
		defaultELTButton.setLayoutData(gd_defaultELTButton);
		defaultELTButton.setText(buttonText);
		
		widget = defaultELTButton;
	}
		
	/**
	 * Button width.
	 * 
	 * @param buttonWidth
	 *            the button width
	 * @return the ELT default button
	 */
	public ELTDefaultButton buttonWidth(int buttonWidth){
		this.buttonWidth = buttonWidth;
		return this;
	}
	
	public ELTDefaultButton buttonHeight(int buttonHeight){
		this.buttonHeight = buttonHeight;
		return this;
	}
	/**
	 * Grab excess horizontal space.
	 * 
	 * @param grabExcessSpace
	 *            the grab excess space
	 * @return the ELT default button
	 */
	public ELTDefaultButton grabExcessHorizontalSpace(boolean grabExcessSpace){
		this.grabExcessSpace = grabExcessSpace;
		return this;
	}
	
	/**
	 * Visible.
	 * 
	 * @param visiblity
	 *            the visiblity
	 */
	public void visible(boolean visiblity){
		defaultELTButton.setVisible(visiblity);
	}
	
}
