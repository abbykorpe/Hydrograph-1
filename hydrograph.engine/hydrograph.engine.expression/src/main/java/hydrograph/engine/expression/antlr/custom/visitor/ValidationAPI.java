package hydrograph.engine.expression.antlr.custom.visitor;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import hydrograph.engine.expression.antlr.DescriptiveErrorListener;
import hydrograph.engine.expression.antlr.ExprssionEditorLexer;
import hydrograph.engine.expression.antlr.ExprssionEditorParser;

public class ValidationAPI {

	private ValidationAPI() {
	}

	public static String getValidExpression(String expr) {
		ANTLRInputStream stream = new ANTLRInputStream(expr);
		ExprssionEditorLexer lexer = new ExprssionEditorLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ExprssionEditorParser parser = new ExprssionEditorParser(tokenStream);
		parser.removeErrorListeners();
		parser.addErrorListener(DescriptiveErrorListener.INSTANCE);
		lexer.removeErrorListeners();
		lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);

		ExprssionEditorParser.BlockContext tree = parser.block();

		CustomExpressionVisitor customExpressionVisitor = new CustomExpressionVisitor();
		customExpressionVisitor.visit(tree);
		return customExpressionVisitor.getExpression();
	}

	public static boolean isExpressionValid(String expr) {
		try {
			getValidExpression(expr);
			return true;
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return false;

	}

}
