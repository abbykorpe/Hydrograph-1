package hydrograph.server.sqlquery.lingualquery.generator;

import hydrograph.server.debug.lingual.json.GridRow;
import hydrograph.server.sqlquery.parser.antlr.QueryParserBaseVisitor;
import hydrograph.server.sqlquery.parser.antlr.QueryParserParser;
import hydrograph.server.sqlquery.parser.antlr.QueryParserVisitor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by bhaveshs on 6/22/2016.
 */
public class LingualQueryCreator extends QueryParserBaseVisitor<String>
		implements QueryParserVisitor<String> {

	String str = "";
	HashMap<String, String> fieldDataMap;

	public LingualQueryCreator(List<GridRow> schema) {
		fieldDataMap = new HashMap<String, String>();
		for (int i = 0; i < schema.size(); i++) {
			fieldDataMap.put(schema.get(i).getFieldName(), schema.get(i)
					.getDataTypeValue());
		}
	}

	@Override
	public String visitEval(QueryParserParser.EvalContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			str += visit(ctx.getChild(i));
		}
		return str;
	}

	@Override
	public String visitExpression(QueryParserParser.ExpressionContext ctx) {
		String expr = "";
		String fieldName = ctx.fieldname().getText();
		String datType = fieldDataMap.get(fieldName);
		fieldName = "\"" + fieldName + "\"";
		if (datType.toLowerCase().contains("date")) {
			if (ctx.condition() == null && ctx.specialexpr() != null) {
				boolean notPresent = isNotClausePresentInExpression(ctx
						.specialexpr().getText());
				if (notPresent) {
					expr = generateNotClauseForField(fieldName);
				}
				expr += fieldName + " " + addSpace(ctx.specialexpr().getText())
						+ (notPresent ? ")" : "");
			} else {
				expr = fieldName + " " + ctx.condition().getText() + " date "
						+ ctx.javaiden().getText() + "";
			}
		} else if (datType.toLowerCase().contains("float")) {
			if (ctx.condition() == null && ctx.specialexpr() != null) {
				boolean notPresent = isNotClausePresentInExpression(ctx
						.specialexpr().getText());
				if (notPresent) {
					expr = generateNotClauseForField(fieldName);
				}
				expr += fieldName + " " + addSpace(ctx.specialexpr().getText())
						+ (notPresent ? ")" : "");

			} else {
				expr = fieldName + " " + ctx.condition().getText() + " cast("
						+ ctx.javaiden().getText() + " as float)";
			}
		} else if (datType.toLowerCase().contains("double")) {
			if (ctx.condition() == null && ctx.specialexpr() != null) {
				boolean notPresent = isNotClausePresentInExpression(ctx
						.specialexpr().getText());
				if (notPresent) {
					expr = generateNotClauseForField(fieldName);
				}
				expr += fieldName + " " + addSpace(ctx.specialexpr().getText())
						+ (notPresent ? ")" : "");
			} else {
				expr = fieldName + " " + ctx.condition().getText() + " cast("
						+ ctx.javaiden().getText() + " as double)";
			}
		} else {
			if (ctx.condition() == null && ctx.specialexpr() != null) {
				boolean notPresent = isNotClausePresentInExpression(ctx
						.specialexpr().getText());
				if (notPresent) {
					expr = generateNotClauseForField(fieldName);
				}
				expr += fieldName + " " + addSpace(ctx.specialexpr().getText())
						+ (notPresent ? ")" : "");
			} else {
				expr = fieldName + " " + ctx.condition().getText() + " "
						+ ctx.javaiden().getText() + "";
			}
		}
		return expr;
	}

	private boolean isNotClausePresentInExpression(String expression) {
		boolean notPresent = false;
		if (expression.contains("not") || expression.contains("NOT")) {
			notPresent = true;
		}
		return notPresent;
	}

	private String generateNotClauseForField(String fieldName) {
		String expr;
		expr = "(" + fieldName + " is not null and ";
		return expr;
	}

	private String addSpace(String splExpression) {
		if (splExpression.contains("LIKE")) {
			splExpression = splExpression.replaceAll("LIKE", "LIKE ");

		} else if (splExpression.contains("like")) {
			splExpression = splExpression.replaceAll("like", "like ");

		} else if (splExpression.contains("BETWEEN")) {
			splExpression = splExpression.replaceAll("BETWEEN", "BETWEEN ");

		} else if (splExpression.contains("between")) {
			splExpression = splExpression.replaceAll("between", "between ");
		}
		if (splExpression.contains("and")) {
			splExpression = splExpression.replaceAll("and", " and ");
		}
		return splExpression;
	}

	@Override
	public String visitAndOr(QueryParserParser.AndOrContext ctx) {
		return " " + ctx.getText() + " ";
	}

	@Override
	public String visitLeftBrace(QueryParserParser.LeftBraceContext ctx) {
		return ctx.getText();
	}

	@Override
	public String visitRightBrace(QueryParserParser.RightBraceContext ctx) {
		return ctx.getText();
	}
}
