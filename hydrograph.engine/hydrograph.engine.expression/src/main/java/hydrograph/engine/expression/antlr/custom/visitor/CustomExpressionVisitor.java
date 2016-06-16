package hydrograph.engine.expression.antlr.custom.visitor;

import java.util.HashSet;
import java.util.Set;

import hydrograph.engine.expression.antlr.ExpressionEditorBaseVisitor;
import hydrograph.engine.expression.antlr.ExpressionEditorParser;
import hydrograph.engine.expression.antlr.ExpressionEditorVisitor;

/**
 * Created by gurdits on 6/1/2016.
 */
class CustomExpressionVisitor extends ExpressionEditorBaseVisitor<String> implements ExpressionEditorVisitor<String> {

	private Set<String> fieldList;

	public CustomExpressionVisitor() {
		fieldList = new HashSet<String>();
	}

	@Override
	public String visitJavaIdentifier(ExpressionEditorParser.JavaIdentifierContext ctx) {
		fieldList.add(ctx.getText());
		return visitChildren(ctx);
	}

	public Set<String> getFieldList() {
		return fieldList;
	}

}
