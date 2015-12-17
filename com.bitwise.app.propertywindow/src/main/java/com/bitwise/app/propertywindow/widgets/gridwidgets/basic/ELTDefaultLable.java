package com.bitwise.app.propertywindow.widgets.gridwidgets.basic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

// TODO: Auto-generated Javadoc
/**
 * 
 * @author Bitwise Sep 18, 2015
 * 
 */

public class ELTDefaultLable extends AbstractELTWidget {

	Label defaultELTLable;

	private int lableWidth = 75;
	private String lableText = "Lable";

	/**
	 * Instantiates a new ELT default lable.
	 * 
	 * @param lableText
	 *            the lable text
	 */
	public ELTDefaultLable(String lableText) {
		this.lableText = lableText;
	}

	public void setImage(String img) {
		defaultELTLable.setImage(new Image(null, img));
	}

	public void addMouseUpListener(MouseListener listener) {
		defaultELTLable.addMouseListener(listener);
	}

	@Override
	public void attachWidget(Composite container) {
		defaultELTLable = new Label(container, SWT.NONE);
		GridData gd_defaultELTLable = new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1);
		gd_defaultELTLable.widthHint = lableWidth;
		defaultELTLable.setLayoutData(gd_defaultELTLable);
		defaultELTLable.setText(lableText);

		widget = defaultELTLable;
	}

	/**
	 * Lable width.
	 * 
	 * @param lableWidth
	 *            the lable width
	 * @return the ELT default lable
	 */
	public ELTDefaultLable lableWidth(int lableWidth) {
		this.lableWidth = lableWidth;
		return this;
	}

}
