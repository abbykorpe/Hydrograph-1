package hydrograph.engine.expression.antlr.custom.visitor;

import java.util.ArrayList;
import java.util.List;

import hydrograph.engine.expression.antlr.ExprssionEditorBaseVisitor;
import hydrograph.engine.expression.antlr.ExprssionEditorParser;
import hydrograph.engine.expression.antlr.ExprssionEditorVisitor;

/**
 * Created by gurdits on 6/1/2016.
 */
class CustomExpressionVisitor extends ExprssionEditorBaseVisitor<String> implements ExprssionEditorVisitor<String> {

	private String PACKAGE_NAME = "import hydrograph.engine.transformation.standardfunctions; ";
	private String expression;

	@Override
	public String visitBlock(ExprssionEditorParser.BlockContext ctx) {
		expression = PACKAGE_NAME + ctx.getText();
		return visitChildren(ctx);
	}

	public String getExpression() {
		return expression;
	}

}
