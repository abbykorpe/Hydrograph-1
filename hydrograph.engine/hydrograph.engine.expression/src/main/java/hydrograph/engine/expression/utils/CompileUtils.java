/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.engine.expression.utils;

import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * @author gurdits
 *
 */
public class CompileUtils {

	private final static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	private final static DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
	private final static StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, null);
	private static CompilationTask task;

	/**
	 * @param fields
	 *            object contains all fields name w.r.t default values.
	 * @param expression
	 *            is a construct made up of fields, operators, and method
	 *            invocations
	 * @param packageName
	 *            contains all the imported classes.
	 * @return a DiagnosticCollector of {@link JavaFileObject} objects which
	 *         contains all the compile time information .
	 */
	public static DiagnosticCollector<JavaFileObject> javaCompile(String fields, String expression,
			String packageName) {

		StringWriter writer = generateRuntimeClass(fields, expression, packageName);
		JavaFileObject file = new JavaSourceFromString("Expression", writer.toString());
		final Iterable<? extends JavaFileObject> sources = Arrays.asList(file);
		task = compiler.getTask(null, manager, diagnostics, null, null, sources);
		task.call();
		return diagnostics;
	}

	/**
	 * @param fields
	 *            object contains all fields name w.r.t default values.
	 * @param expression
	 *            is a construct made up of fields, operators, and method
	 *            invocations
	 * @param externalJarPath
	 *            is used to include custom classes.
	 * @param packageName
	 *            contains all the imported classes.
	 * @return a DiagnosticCollector of {@link JavaFileObject} objects which
	 *         contains all the compile time information .
	 */
	public static DiagnosticCollector<JavaFileObject> javaCompile(String fields, String expression,
			String externalJarPath, String packageName) {

		StringWriter writer = generateRuntimeClass(fields, expression, packageName);
		JavaFileObject file = new JavaSourceFromString("Expression", writer.toString());
		final Iterable<? extends JavaFileObject> sources = Arrays.asList(file);
		if ((externalJarPath != null) && (!externalJarPath.isEmpty())) {
			String classpath = System.getProperty("java.class.path");
			List<String> optionList = new ArrayList();
			optionList.addAll(Arrays.asList(new String[] { "-classpath", externalJarPath }));
			task = compiler.getTask(null, null, diagnostics, optionList, null, sources);
			task.call();
			return diagnostics;
		}
		throw new RuntimeException("Missing External Jar path");

	}

	private static StringWriter generateRuntimeClass(String fields, String expression, String packageName) {
		StringWriter writer = new StringWriter();
		writer.append(packageName);
		writer.append("class Expression {");
		writer.append("public static Object validate(){ try{");
		writer.append(fields != null ? fields : "");
		writer.append("return  " + expression + "}catch(Exception e){return null;}}");
		writer.append("public static void main(String args[]) {");
		writer.append("validate();");
		writer.append("}");
		writer.append("}");
		return writer;
	}

	static Iterable<JavaSourceFromString> getJavaSourceFromString(String code) {
		final JavaSourceFromString jsfs;
		jsfs = new JavaSourceFromString("code", code);
		return new Iterable<JavaSourceFromString>() {
			public Iterator<JavaSourceFromString> iterator() {
				return new Iterator<JavaSourceFromString>() {
					boolean isNext = true;

					public boolean hasNext() {
						return isNext;
					}

					public JavaSourceFromString next() {
						if (!isNext)
							throw new NoSuchElementException();
						isNext = false;
						return jsfs;
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
}

class JavaSourceFromString extends SimpleJavaFileObject {
	final String code;

	JavaSourceFromString(String name, String code) {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}