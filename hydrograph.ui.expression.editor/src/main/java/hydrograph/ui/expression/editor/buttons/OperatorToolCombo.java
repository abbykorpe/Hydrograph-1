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

package hydrograph.ui.expression.editor.buttons;

import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.expression.editor.Messages;
import hydrograph.ui.expression.editor.message.CustomMessageBox;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;

public class OperatorToolCombo extends Combo {

	private static final String OPERATOR_CONFIG_FILE = "resources/expression_operator.properties";
	private Logger LOGGER = LogFactory.INSTANCE.getLogger(OperatorToolCombo.class);
	private static final String ITEM_TEXT = "Operators";
	private StyledText expressionEditor;

	public OperatorToolCombo(Composite composite, int style, StyledText expressionEditor) {
		super(composite, style);
		this.expressionEditor = expressionEditor;
		this.add(ITEM_TEXT);
		this.select(0);
		loadDropDownItems();
		
		addSelectionListener();
		
		getParent().pack();

	}

	private void addSelectionListener() {
		this.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getData(getItem(getSelectionIndex())) != null) {
					expressionEditor.insert((String)getData(getItem(getSelectionIndex())));
				}
				select(0);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				setListVisible(true);
			}
		});
	}
	
	private void loadDropDownItems() {
		
		Properties properties=new Properties();
		InputStream inStream;
		try {
			inStream = getPropertyFilePath();
			properties.load(inStream);
			for(Object key:properties.keySet()){
				if(key!=null && properties.get(key)!=null){
				this.add((String) key);
				this.setData((String) key, (String)properties.get(key));
				}
			}
		} catch (IOException ioException) {
			LOGGER.error("Exception occurred while loading "+XMLConfigUtil.CONFIG_FILES_PATH+File.separatorChar+OPERATOR_CONFIG_FILE+" property file.");
			StringBuffer buffer=new StringBuffer();
			buffer.append(Messages.OPERATOR_FILE_NOT_FOUND);
			buffer.append(XMLConfigUtil.CONFIG_FILES_PATH+File.separatorChar+OPERATOR_CONFIG_FILE);
			new CustomMessageBox(SWT.ICON_WARNING,buffer.toString() , Messages.WARNING).open();
		}
	}

	protected void checkSubclass() {
		// Allow subclassing 
	}

	private InputStream getPropertyFilePath() throws IOException{
		URL location = FileLocator.find(Platform.getBundle(Constants.EXPRESSION_EDITOR_PLUGIN_ID), new Path(OPERATOR_CONFIG_FILE), null);
		return location.openStream();
	}
	
}
