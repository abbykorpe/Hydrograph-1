package hydrograph.engine.expression.antlr.custom.visitor;

import java.util.Map;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import hydrograph.engine.expression.antlr.ExpressionEditorLexer;
import hydrograph.engine.expression.antlr.ExpressionEditorParser;

public class ValidationAPI {

	private static String PACKAGE_NAME = "import hydrograph.engine.transformation.standardfunctions.StringFunctions; "
			+ "import hydrograph.engine.transformation.standardfunctions.DateFunctions;"
			+ "import hydrograph.engine.transformation.standardfunctions.NumericFunctions; ";

	private ValidationAPI() {
	}

	private static ExpressionEditorParser.BlockContext generateAntlrTree(String expr) {
		ANTLRInputStream stream = new ANTLRInputStream(expr);
		ExpressionEditorLexer lexer = new ExpressionEditorLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ExpressionEditorParser parser = new ExpressionEditorParser(tokenStream);
		parser.removeErrorListeners();
		parser.addErrorListener(DescriptiveErrorListener.INSTANCE);
		lexer.removeErrorListeners();
		lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);
		return parser.block();
	}

	public static boolean isExpressionValid(String expr) {
		try {
			CustomExpressionVisitor customExpressionVisitor = new CustomExpressionVisitor();
			customExpressionVisitor.visit(generateAntlrTree(expr));
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getValidExpression(String expr) {
		if (isExpressionValid(expr))
			return PACKAGE_NAME + expr;
		return expr;

	}

	public static void checkExpressionValid(String expr) {
		isExpressionValid(expr);
	}

	public static DiagnosticCollector<JavaFileObject> compile(String expr, Map<String, Class<?>> schemaFields) {
		String fields = "";
		CustomExpressionVisitor customExpressionVisitor = new CustomExpressionVisitor();
		customExpressionVisitor.visit(generateAntlrTree(expr));
		for (String field : customExpressionVisitor.getFieldList()) {
			if (schemaFields.get(field) != null) {
				fields += ClassToDataTypeConversion.valueOf(schemaFields.get(field).getSimpleName()).getValue(field);
			}
		}
		return CompileUtils.javaCompile(fields, expr);

	}

}
