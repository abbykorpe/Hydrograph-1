package com.bitwise.app.propertywindow.widgets.xmlPropertiesContainer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.propertywindow.messages.Messages;

/**
 * @author Bitwise
 * 
 * This class is used to display XML content of dummy components on property window.
 *  
 */
public class XMLTextContainer {
	private Text text;
	private String xmlText;
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(XMLTextContainer.class);

	/**
	 * Launches the component property editor window for dummy components. It is used to display XML content of dummy
	 * components on property window.
	 * 
	 * @return XML content of component. 
	 */
	
	public String launchXMLTextContainerWindow() {
		try {
			String xmlText = this.xmlText;
			Shell shell = new Shell(Display.getDefault().getActiveShell(), SWT.WRAP | SWT.MAX | SWT.APPLICATION_MODAL);

			shell.setLayout(new GridLayout(1, false));
			shell.setText("XML Content");
			shell.setSize(439, 432);
			text = new Text(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
			text.setEditable(false);
			text.setBackground(new Color(shell.getDisplay(), 250, 250, 250));
			if (this.xmlText != null) {
				xmlText = xmlText.substring(xmlText.indexOf('\n') + 1);
				xmlText = xmlText.substring(xmlText.indexOf('\n') + 1, xmlText.lastIndexOf('\n') - 13);

				text.setText(xmlText);
			} else
				text.setText(Messages.EMPTY_XML_CONTENT);
			GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			gd_text.widthHint = 360;
			gd_text.heightHint = 360;
			text.setLayoutData(gd_text);

			Monitor primary = shell.getDisplay().getPrimaryMonitor();
			Rectangle bounds = primary.getBounds();
			Rectangle rect = shell.getBounds();

			int x = bounds.x + (bounds.width - rect.width) / 2;
			int y = bounds.y + (bounds.height - rect.height) / 2;

			shell.setLocation(x, y);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!shell.getDisplay().readAndDispatch()) {
					shell.getDisplay().sleep();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while creating XML text container widget", e);
		}
		return getXmlText();
	}

	public String getXmlText() {
		return this.xmlText;
	}

	public void setXmlText(String xmlText) {
		this.xmlText = xmlText;
	}

}
