package hydrograph.engine.expression.antlr.custom.visitor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import bsh.Interpreter;
import hydrograph.engine.expression.antlr.ExpressionEditorLexer;
import hydrograph.engine.expression.antlr.ExpressionEditorParser;

public class ValidationAPI {

	private static String PACKAGE_NAME = "import hydrograph.engine.transformation.standardfunctions.StringFunctions; "
			+ "import hydrograph.engine.transformation.standardfunctions.DateFunctions;"
			+ "import hydrograph.engine.transformation.standardfunctions.NumericFunctions; ";
	private static Map<Class<?>, Object> schemaFieldsMap = new HashMap<Class<?>, Object>();

	private static void put() {
		schemaFieldsMap.put(String.class, "Hello World");
		schemaFieldsMap.put(Integer.class, 100);
		schemaFieldsMap.put(Float.class, 100.55);
		schemaFieldsMap.put(Double.class, 100.33);
		schemaFieldsMap.put(Boolean.class, true);
		schemaFieldsMap.put(Long.class, 100000);
		schemaFieldsMap.put(Short.class, 10);
		schemaFieldsMap.put(BigDecimal.class, 10000.55);
		schemaFieldsMap.put(Date.class, new Date());
	}

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

	public static <T> int compile(String expr, Map<String, Class<?>> schemaFields) {
		try {
			Interpreter interpreter = new Interpreter();
			CustomExpressionVisitor customExpressionVisitor = new CustomExpressionVisitor();
			customExpressionVisitor.visit(generateAntlrTree(expr));
			for (String field : customExpressionVisitor.getFieldList()) {
				if (schemaFields.get(field) != null)
					interpreter.set(field, get(schemaFields.get(field)));
			}
			interpreter.eval(getValidExpression(expr));
			return 1;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T get(Class<T> type) {
		put();
		return type.cast(schemaFieldsMap.get(type));
	}

}
