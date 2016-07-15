// Generated from QueryParser.g4 by ANTLR 4.4
package hydrograph.server.sqlquery.parser.antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link QueryParserParser}.
 */
public interface QueryParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link QueryParserParser#andOr}.
	 * @param ctx the parse tree
	 */
	void enterAndOr(@NotNull QueryParserParser.AndOrContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParserParser#andOr}.
	 * @param ctx the parse tree
	 */
	void exitAndOr(@NotNull QueryParserParser.AndOrContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParserParser#fieldname}.
	 * @param ctx the parse tree
	 */
	void enterFieldname(@NotNull QueryParserParser.FieldnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParserParser#fieldname}.
	 * @param ctx the parse tree
	 */
	void exitFieldname(@NotNull QueryParserParser.FieldnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParserParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(@NotNull QueryParserParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParserParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(@NotNull QueryParserParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParserParser#eval}.
	 * @param ctx the parse tree
	 */
	void enterEval(@NotNull QueryParserParser.EvalContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParserParser#eval}.
	 * @param ctx the parse tree
	 */
	void exitEval(@NotNull QueryParserParser.EvalContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParserParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull QueryParserParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParserParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull QueryParserParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParserParser#leftBrace}.
	 * @param ctx the parse tree
	 */
	void enterLeftBrace(@NotNull QueryParserParser.LeftBraceContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParserParser#leftBrace}.
	 * @param ctx the parse tree
	 */
	void exitLeftBrace(@NotNull QueryParserParser.LeftBraceContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParserParser#rightBrace}.
	 * @param ctx the parse tree
	 */
	void enterRightBrace(@NotNull QueryParserParser.RightBraceContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParserParser#rightBrace}.
	 * @param ctx the parse tree
	 */
	void exitRightBrace(@NotNull QueryParserParser.RightBraceContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParserParser#specialexpr}.
	 * @param ctx the parse tree
	 */
	void enterSpecialexpr(@NotNull QueryParserParser.SpecialexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParserParser#specialexpr}.
	 * @param ctx the parse tree
	 */
	void exitSpecialexpr(@NotNull QueryParserParser.SpecialexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParserParser#javaiden}.
	 * @param ctx the parse tree
	 */
	void enterJavaiden(@NotNull QueryParserParser.JavaidenContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParserParser#javaiden}.
	 * @param ctx the parse tree
	 */
	void exitJavaiden(@NotNull QueryParserParser.JavaidenContext ctx);
}