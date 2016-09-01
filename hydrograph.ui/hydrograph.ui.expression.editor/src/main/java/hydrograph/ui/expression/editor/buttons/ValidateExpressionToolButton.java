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

import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.expression.editor.Messages;
import hydrograph.ui.expression.editor.PathConstant;
import hydrograph.ui.expression.editor.dialogs.ExpressionEditorDialog;
import hydrograph.ui.expression.editor.jar.util.BuildExpressionEditorDataSturcture;
import hydrograph.ui.expression.editor.message.CustomMessageBox;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;

@SuppressWarnings("restriction")
public class ValidateExpressionToolButton extends Button {

	private static final String RETURN_STATEMENT = "\t\treturn \n";
	private static final String COMPILE_METHOD_OF_EXPRESSION_JAR = "compile";
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(ValidateExpressionToolButton.class);
	private static final String VALID_EXPRESSION = "Success";
	public static final String HYDROGRAPH_ENGINE_EXPRESSION_VALIDATION_API_CLASS = "hydrograph.engine.expression.antlr.custom.visitor.ValidationAPI";
	private static final String ITEM_TEXT = "Validate";
	private StyledText expressionEditor;

	public ValidateExpressionToolButton(Composite composite, int style, StyledText expressionEditor) {
		super(composite, style);
		setText(ITEM_TEXT);
		this.expressionEditor = expressionEditor;
		addSelectionListener();
	}

	private void addSelectionListener() {
		addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validation(expressionEditor);
			}
		});
	}

	protected void checkSubclass() {/* Allow subclassing*/}

	@SuppressWarnings({ "unchecked"})
	public static DiagnosticCollector<JavaFileObject> compileExpresion(StyledText expressionStyledText)
			throws JavaModelException, InvocationTargetException, ClassNotFoundException, MalformedURLException,IllegalAccessException, IllegalArgumentException {
		LOGGER.debug("Compiling expression using Java-Compiler");
		String expressiontext=getExpressionText(expressionStyledText.getText());
		Map<String, Class<?>> fieldMap = (Map<String, Class<?>>) expressionStyledText.getData(ExpressionEditorDialog.FIELD_DATA_TYPE_MAP);
		DiagnosticCollector<JavaFileObject> diagnostics = null;
		Object[] returObj=getBuildPathForMethodInvocation() ;
		List<URL> urlList=(List<URL>) returObj[0];
		String transfromJarPath = (String) returObj[1];
		String propertyFilePath= (String) returObj[2];
		ClassLoader child = new URLClassLoader(urlList.toArray(new URL[urlList.size()]));
		Class<?> class1 = Class.forName(HYDROGRAPH_ENGINE_EXPRESSION_VALIDATION_API_CLASS, true, child);
		Thread.currentThread().setContextClassLoader(child);
		Method[] methods = class1.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 4 && StringUtils.equals(method.getName(), COMPILE_METHOD_OF_EXPRESSION_JAR)) {
				method.getDeclaringClass().getClassLoader();
				diagnostics = (DiagnosticCollector<JavaFileObject>) method.invoke(null, expressiontext,propertyFilePath,
						fieldMap, transfromJarPath);
				break;
			}
		}
		return diagnostics;
	}


	public static String getExpressionText(String expressionText) {
		StringBuffer buffer=new StringBuffer(expressionText);
		int startIndex=buffer.indexOf(RETURN_STATEMENT);
		if(startIndex>-1){
			buffer.delete(0, startIndex);
			buffer.delete(0, buffer.indexOf("\n"));
		}
		return StringUtils.trim(buffer.toString());
	}

	public static String getAbsolutePathForJars(IPath iPath) {
		String absolutePath = iPath.toString();
		File file = iPath.toFile();
		if (!file.exists()) {
			String workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
			absolutePath = workspacePath + iPath.toString();
		}
		return absolutePath;
	}

	private void validation(StyledText expressionEditor) {
		try {
			DiagnosticCollector<JavaFileObject> diagnostics = compileExpresion(expressionEditor);
			if (diagnostics != null && !diagnostics.getDiagnostics().isEmpty())
				showDiagnostics(diagnostics);
			else {
				new CustomMessageBox(SWT.ICON_INFORMATION, VALID_EXPRESSION, Messages.VALID_EXPRESSION_TITLE).open();
			}
		} catch (JavaModelException | MalformedURLException | IllegalAccessException | IllegalArgumentException exception) {
			LOGGER.error("Exception occurred while compiling expression",exception);
			new CustomMessageBox(SWT.ERROR, exception.getCause().getMessage(), Messages.INVALID_EXPRESSION_TITLE).open();
		} catch (InvocationTargetException exception) {
			if(exception.getCause()!=null && StringUtils.isNotBlank(exception.getCause().getMessage()))
				new CustomMessageBox(SWT.ERROR, exception.getCause().getMessage(), Messages.INVALID_EXPRESSION_TITLE).open();
			LOGGER.error("Exception occurred while invoking compile method for compiling expression",exception);
		} catch (ClassNotFoundException classNotFoundException) {
			new CustomMessageBox(SWT.ERROR, "Cannot find validation jar in build path", Messages.INVALID_EXPRESSION_TITLE).open();
		}
	}

	private void showDiagnostics(DiagnosticCollector<JavaFileObject> diagnostics) {
		String message;
		for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
			if (StringUtils.equals(diagnostic.getKind().name(), Diagnostic.Kind.ERROR.name())) {
				message = diagnostic.getMessage(null);
				new CustomMessageBox(SWT.ERROR, message, Messages.INVALID_EXPRESSION_TITLE).open();
			} else {
				new CustomMessageBox(SWT.ICON_INFORMATION, VALID_EXPRESSION, Messages.VALID_EXPRESSION_TITLE).open();
			}
			break;
		}
	}

	public static Object[] getBuildPathForMethodInvocation() throws JavaModelException, MalformedURLException {
		String transfromJarPath = null;
		Object[] returnObj=new Object[3];
		IJavaProject iJavaProject = JavaCore.create(BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject());
		List<URL> urlList = new ArrayList<>();
		for (IPackageFragmentRoot iPackageFragmentRoot : iJavaProject.getAllPackageFragmentRoots()) {
			if (!iPackageFragmentRoot.isExternal()
					|| StringUtils.contains(iPackageFragmentRoot.getElementName(), Constants.JAR_FILE_NAME)
					|| StringUtils.contains(iPackageFragmentRoot.getElementName(), Constants.ANTLR_JAR_FILE_NAME)
					|| StringUtils.contains(iPackageFragmentRoot.getElementName(), Constants.BEAN_SHELLJAR_FILE_NAME)
					|| StringUtils.contains(iPackageFragmentRoot.getElementName(), Constants.SL4JLOG)
					|| StringUtils.contains(iPackageFragmentRoot.getElementName(), Constants.EXPRESSION_JAR_FILE_NAME)) {
				URL url = null;
				if (!iPackageFragmentRoot.isExternal()) {
					url = BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject()
							.getFile(iPackageFragmentRoot.getPath().removeFirstSegments(1)).getLocation().toFile()
							.toURI().toURL();
					urlList.add(url);
				} else {
					url = iPackageFragmentRoot.getPath().toFile().toURI().toURL();
					urlList.add(url);
				}

				if (!iPackageFragmentRoot.isExternal()
						|| StringUtils.contains(iPackageFragmentRoot.getElementName(), Constants.JAR_FILE_NAME)) {
					if (transfromJarPath == null) {
						if (OSValidator.isMac() || OSValidator.isUnix())
							transfromJarPath = url.getPath() + Constants.COLON;
						else
							transfromJarPath = url.getPath() + Constants.SEMICOLON;
					} else {
						if (OSValidator.isMac() || OSValidator.isUnix())
							transfromJarPath = transfromJarPath + url.getPath() + Constants.COLON;
						else
							transfromJarPath = transfromJarPath + url.getPath() + Constants.SEMICOLON;
					}
				}
			}
		}
		
		returnObj[0]=urlList;
		returnObj[1]=transfromJarPath;
		returnObj[2]=getPropertyFilePath(iJavaProject);
		return returnObj;
	}

	private static String getPropertyFilePath(IJavaProject project) {
		LOGGER.debug("Adding UserFunctions.propertis file URL to build-path");
		IFolder settingFolder=project.getProject().getFolder(PathConstant.PROJECT_RESOURCES_FOLDER);
		if(settingFolder.exists()){
			IFile file=settingFolder.getFile(PathConstant.EXPRESSION_EDITOR_EXTERNAL_JARS_PROPERTIES_FILES);
			if(file.exists()){
				return file.getLocation().toString();
			}
		}
		return Constants.EMPTY_STRING;
	}
}
