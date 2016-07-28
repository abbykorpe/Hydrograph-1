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

import hydrograph.ui.expression.editor.jar.util.BuildExpressionEditorDataSturcture;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ValidateExpressionToolButton extends Button {

	private static final String ITEM_TEXT = "Validate";
	private StyledText expressionEditor;
	
	public ValidateExpressionToolButton(Composite composite, int style, StyledText expressionEditor) {
		super(composite, style);
		setText(ITEM_TEXT);
		this.expressionEditor=expressionEditor;
		addSelectionListener();
	}

	private void addSelectionListener() {
		addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				validateExpresion(expressionEditor);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/*Do-Nothing*/}
		});
	}

	protected void checkSubclass() {
		// Allow subclassing
	}

	@SuppressWarnings("unchecked")
	public void validateExpresion(StyledText expressionStyledText) {

		
		DiagnosticCollector<JavaFileObject> diagnostics = null;
		IPackageFragmentRoot[] packageFragments;
		String transfromJarPath=null;
		try {
			IJavaProject iJavaProject = JavaCore.create(BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject());
			packageFragments = iJavaProject.getAllPackageFragmentRoots();
			URL[] superSetURLs=new URL[packageFragments.length];
			for (int i=0;i<superSetURLs.length;i++) {
				if(transfromJarPath!=null){
					IPath iPath=packageFragments[i].getPath();
					iPath.isAbsolute();
					transfromJarPath=transfromJarPath+";"+getAbsolutePathForJars(iPath);
					}
				else
					transfromJarPath=getAbsolutePathForJars(packageFragments[i].getPath());
					superSetURLs[i]=packageFragments[i].getPath().toFile().toURI().toURL();
					System.out.println("==========="+packageFragments[i].getElementName()+"===========");
			}
					ClassLoader child = new URLClassLoader(superSetURLs);
					Class<?> class1 = Class.forName("hydrograph.engine.expression.antlr.custom.visitor.ValidationAPI",true, child);
					Method[] methods = class1.getDeclaredMethods();
					for (Method method : methods) {
						if (method.getParameterTypes().length ==3 && StringUtils.equals(method.getName(), "compile")) {
							Map<String, Class<?>> map = new LinkedHashMap<>();
							
							diagnostics = (DiagnosticCollector<JavaFileObject>) method.invoke(null, expressionStyledText.getText(),map,transfromJarPath);
							break;
						}
					}
			if (diagnostics != null && !diagnostics.getDiagnostics().isEmpty()) {
				String message="";
				for (Diagnostic diagnostic2 : diagnostics.getDiagnostics()) {
					message=message+diagnostic2.getMessage(null)
							+"\n Start:"+(diagnostic2.getColumnNumber()-351)+"\n, End"+(diagnostic2.getEndPosition()-351)+
							"\n\n====================================\n\n";
					
					expressionStyledText.setSelection((int)diagnostic2.getColumnNumber()-352, (int)diagnostic2.getEndPosition()-351);
				}
				MessageBox box = new MessageBox(Display.getCurrent().getActiveShell());
				box.setMessage(message);
				box.open();
			}else{
				MessageBox box = new MessageBox(Display.getCurrent().getActiveShell());
				box.setMessage("Success");
				box.open();
			}

		} catch (JavaModelException | ClassNotFoundException | MalformedURLException | IllegalAccessException
				| IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			MessageBox box = new MessageBox(new Shell(SWT.NONE));
			box.setMessage(e.getCause().getMessage());
			box.setText(e.getCause().getMessage());
			box.open();
		}
	}
	
	public static String getAbsolutePathForJars(IPath iPath){
		String absolutePath=iPath.toString();
		File file=iPath.toFile();
		if(!file.exists()){
			String workspacePath=ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
			absolutePath=workspacePath+iPath.toString();
		}
		return absolutePath;
	}
	
}
