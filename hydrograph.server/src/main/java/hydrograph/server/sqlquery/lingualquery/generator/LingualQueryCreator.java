/*******************************************************************************
 *  * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *******************************************************************************/
package hydrograph.server.sqlquery.lingualquery.generator;

import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import hydrograph.server.debug.lingual.json.GridRow;
import hydrograph.server.sqlquery.parser.antlr.QueryParserBaseVisitor;
import hydrograph.server.sqlquery.parser.antlr.QueryParserParser;
import hydrograph.server.sqlquery.parser.antlr.QueryParserParser.JavaidenContext;
import hydrograph.server.sqlquery.parser.antlr.QueryParserParser.SpecialexprContext;
import hydrograph.server.sqlquery.parser.antlr.QueryParserVisitor;

/**
 * Created by bhaveshs on 6/22/2016.
 */
public class LingualQueryCreator extends QueryParserBaseVisitor<String> implements QueryParserVisitor<String> {

	String str = "";
	HashMap<String, String> fieldDataMap;

	public LingualQueryCreator(List<GridRow> schema) {
		fieldDataMap = new HashMap<String, String>();
		for (int i = 0; i < schema.size(); i++) {
			fieldDataMap.put(schema.get(i).getFieldName(), schema.get(i).getDataTypeValue());
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
		String fieldName = ctx.fieldname().get(0).getText();
		String dataType = fieldDataMap.get(fieldName);
		fieldName = "\"" + fieldName + "\"";

		if (dataType.toLowerCase().contains("date")) {
			expr = generateDateSyntax(ctx, expr, fieldName, dataType);
		} else if (dataType.toLowerCase().contains("float")) {
			expr = generateFloatSytntax(ctx, expr, fieldName, dataType);
		} else if (dataType.toLowerCase().contains("double")) {
			expr = generateDoubleSyntax(ctx, expr, fieldName, dataType);
		} else if (dataType.toLowerCase().contains("boolean")) {
			expr = generateBooleanSyntax(ctx, expr, fieldName, dataType);
		} else {
			expr = generateOtherDatatypeSyntax(ctx, expr, fieldName, dataType);
		}
		return expr;
	}

	private String generateOtherDatatypeSyntax(QueryParserParser.ExpressionContext ctx, String expr, String fieldName,
			String dataType) {
		if (ctx.condition() == null && ctx.specialexpr() != null) {
			boolean notPresent = isNotClausePresentInExpression(ctx.specialexpr().getText());
			if (notPresent) {
				expr = generateNotClauseForField(fieldName);
			}
			expr += fieldName + " " + addSpace(ctx.specialexpr().getChild(0).getText())
					+ getLeftBrace(ctx.specialexpr()) + generateIdentifierText(ctx.specialexpr().javaiden(), 0)
					+ generateBetweenText(ctx.specialexpr(), dataType) + getRightBrace(ctx.specialexpr())
					+ (notPresent ? ")" : "");
		} else {
			if (ctx.javaiden() != null) {
				expr = fieldName + " " + ctx.condition().getText() + " " + generateJavaIdentifierText(ctx.javaiden())
						+ "";
			} else {
				expr = fieldName + " " + ctx.condition().getText() + " \"" + ctx.fieldname().get(1).getText() + "\"";
			}
		}
		return expr;
	}

	private String generateBooleanSyntax(QueryParserParser.ExpressionContext ctx, String expr, String fieldName,
			String dataType) {
		if (ctx.condition() == null && ctx.specialexpr() != null) {
			boolean notPresent = isNotClausePresentInExpression(ctx.specialexpr().getText());
			if (notPresent) {
				expr = generateNotClauseForField(fieldName);
			}
			expr += fieldName + " " + addSpace(ctx.specialexpr().getChild(0).getText())
					+ generateIdentifierText(ctx.specialexpr().javaiden(), 0)
					+ generateBetweenText(ctx.specialexpr(), dataType) + (notPresent ? ")" : "");
		} else {
			if (ctx.javaiden() != null) {
				expr = fieldName + " " + ctx.condition().getText() + " " + ctx.javaiden().getText().replaceAll("'", "")
						+ "";
			} else {
				expr = fieldName + " " + ctx.condition().getText() + " \"" + ctx.fieldname().get(1).getText() + "\"";
			}
		}
		return expr;
	}

	private String generateDoubleSyntax(QueryParserParser.ExpressionContext ctx, String expr, String fieldName,
			String dataType) {
		if (ctx.condition() == null && ctx.specialexpr() != null) {
			boolean notPresent = isNotClausePresentInExpression(ctx.specialexpr().getText());
			if (notPresent) {
				expr = generateNotClauseForField(fieldName);
			}
			expr += fieldName + " " + addSpace(ctx.specialexpr().getChild(0).getText())
					+ getLeftBrace(ctx.specialexpr()) + " cast("
					+ generateIdentifierText(ctx.specialexpr().javaiden(), 0) + " as double) "
					+ generateBetweenText(ctx.specialexpr(), dataType) + getRightBrace(ctx.specialexpr())
					+ (notPresent ? ")" : "");
		} else {
			if (ctx.javaiden() != null) {
				expr = fieldName + " " + ctx.condition().getText() + " cast(" + ctx.javaiden().getText()
						+ " as double)";
			} else {
				expr = fieldName + " " + ctx.condition().getText() + " \"" + ctx.fieldname().get(1).getText() + "\"";
			}
		}
		return expr;
	}

	private String generateDateSyntax(QueryParserParser.ExpressionContext ctx, String expr, String fieldName,
			String dataType) {
		if (ctx.condition() == null && ctx.specialexpr() != null) {
			boolean notPresent = isNotClausePresentInExpression(ctx.specialexpr().getText());
			if (notPresent) {
				expr = generateNotClauseForField(fieldName);
			}
			expr += fieldName + " " + addSpace(ctx.specialexpr().getChild(0).getText())
					+ getLeftBrace(ctx.specialexpr())
					+ (ctx.specialexpr().fieldname() != null && ctx.specialexpr().fieldname().size() > 0
							? "\"" + ctx.specialexpr().fieldname().get(0).getText() + "\""
							: "date " + generateIdentifierText(ctx.specialexpr().javaiden(), 0))
					+ generateBetweenText(ctx.specialexpr(), dataType.toLowerCase()) + getRightBrace(ctx.specialexpr())
					+ (notPresent ? ")" : "");
		} else {
			if (ctx.javaiden() != null) {
				expr = fieldName + " " + ctx.condition().getText() + " date " + ctx.javaiden().getText() + "";
			} else {
				expr = fieldName + " " + ctx.condition().getText() + " \"" + ctx.fieldname().get(1).getText() + "\"";
			}
		}
		return expr;
	}

	private String generateFloatSytntax(QueryParserParser.ExpressionContext ctx, String expr, String fieldName,
			String dataType) {
		if (ctx.condition() == null && ctx.specialexpr() != null) {
			boolean notPresent = isNotClausePresentInExpression(ctx.specialexpr().getText());
			if (notPresent) {
				expr = generateNotClauseForField(fieldName);
			}
			expr += fieldName + " " + addSpace(ctx.specialexpr().getChild(0).getText())
					+ getLeftBrace(ctx.specialexpr()) + " cast("
					+ generateIdentifierText(ctx.specialexpr().javaiden(), 0) + " as float) "
					+ generateBetweenText(ctx.specialexpr(), dataType) + getRightBrace(ctx.specialexpr())
					+ (notPresent ? ")" : "");

		} else {
			if (ctx.javaiden() != null) {
				expr = fieldName + " " + ctx.condition().getText() + " cast(" + ctx.javaiden().getText() + " as float)";
			} else {
				expr = fieldName + " " + ctx.condition().getText() + " \"" + ctx.fieldname().get(1).getText() + "\"";
			}
		}
		return expr;
	}

	// private String generateBetweenText(SpecialexprContext specialexprContext,
	// String datType) {
	// String expr = "";
	// datType = datType.toLowerCase();
	// if (isBetweenPresent(specialexprContext.getText())) {
	// String field = specialexprContext.fieldname() != null &&
	// specialexprContext.fieldname().size()>0
	// ? "\""+specialexprContext.fieldname().get(0).getText()+"\""
	// :generateIdentifierText(specialexprContext.javaiden(), 1);
	// expr = getAndOr(specialexprContext)+" ";
	// if (datType.contains("date")) {
	// expr += "date " + field;
	// } else if (datType.contains("float")) {
	// expr += "cast(" + field + " as float)";
	// } else if (datType.contains("double")) {
	// expr += "cast(" + field + " as double)";
	// } else {
	// expr += field;
	// }
	// }
	// return expr;
	// }

	private String generateBetweenText(SpecialexprContext specialexprContext, String datType) {
		String expr = "";
		datType = datType.toLowerCase();
		if (isBetweenPresent(specialexprContext.getText())) {
			expr = getAndOr(specialexprContext) + " ";
			if (datType.contains("date")) {
				expr += specialexprContext.fieldname() != null && specialexprContext.fieldname().size() > 0
						? "\"" + specialexprContext.fieldname().get(1).getText() + "\""
						: "date " + generateIdentifierText(specialexprContext.javaiden(), 1);
			} else if (datType.contains("float")) {				expr += "cast(" + generateIdentifierText(specialexprContext.javaiden(), 1) + " as float)";
			} else if (datType.contains("double")) {
				expr += "cast(" + generateIdentifierText(specialexprContext.javaiden(), 1) + " as double)";
			} else {
				expr += generateIdentifierText(specialexprContext.javaiden(), 1);
			}
		}
		return expr;
	}

	private boolean isBetweenPresent(String text) {
		return text.toLowerCase().contains("between");
	}

	private String getLeftBrace(SpecialexprContext specialexprContext) {
		String left = "";
		if (specialexprContext.leftBrace() != null) {
			left = left + specialexprContext.leftBrace().getText();
		}
		return left;
	}

	private String getRightBrace(SpecialexprContext specialexprContext) {
		String right = "";
		if (specialexprContext.rightBrace() != null) {
			right = right + specialexprContext.rightBrace().getText();
		}
		return right;
	}

	private String getAndOr(SpecialexprContext specialexprContext) {
		String andOr = "";
		if (specialexprContext.andOr() != null) {
			andOr = andOr + specialexprContext.andOr().getText();
		}
		return andOr;
	}

	private String generateIdentifierText(List<JavaidenContext> list, int index) {
		String iden = "";
		int i = 0;
		for (TerminalNode identifier : list.get(index).Identifier()) {
			if (i == 0)
				iden = iden + identifier.getText();
			else
				iden = iden + " " + identifier.getText();
			i++;
		}
		return iden;
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
		} else if (splExpression.contains("AND")) {
			splExpression = splExpression.replaceAll("AND", " AND ");
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

	public String generateJavaIdentifierText(QueryParserParser.JavaidenContext ctx) {
		String iden = "";
		int i = 0;
		for (TerminalNode identifier : ctx.Identifier()) {
			if (i == 0)
				iden = iden + identifier.getText();
			else
				iden = iden + " " + identifier.getText();
			i++;
		}
		return iden;
	}
}
