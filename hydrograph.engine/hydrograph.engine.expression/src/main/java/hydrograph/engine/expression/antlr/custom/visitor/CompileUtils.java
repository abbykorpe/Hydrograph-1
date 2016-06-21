package hydrograph.engine.expression.antlr.custom.visitor;

import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

class CompileUtils {

	private static String PACKAGE_NAME = "import hydrograph.engine.transformation.standardfunctions.StringFunctions; "
			+ "import hydrograph.engine.transformation.standardfunctions.DateFunctions;"
			+ "import hydrograph.engine.transformation.standardfunctions.NumericFunctions; " + "import java.util.Date;";

	public static DiagnosticCollector<JavaFileObject> javaCompile(String fields, String expression) {
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		final StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, null);
		StringWriter writer = new StringWriter();
		writer.append(PACKAGE_NAME);
		writer.append("class Expression {");
		writer.append("public static Object validate(){ try{");
		writer.append(fields != null ? fields : "");
		writer.append("return  " + expression + "}catch(Exception e){return null;}}");
		writer.append("public static void main(String args[]) {");
		writer.append("validate();");
		writer.append("}");
		writer.append("}");
		JavaFileObject file = new JavaSourceFromString("Expression", writer.toString());

		final Iterable<? extends JavaFileObject> sources = Arrays.asList(file);
		final CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, sources);
		task.call();
		return diagnostics;
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
