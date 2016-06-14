package hydrograph.engine.expression.antlr;// Generated from C:/Users/gurdits/git/ExpressionEditor\ExprssionEditor.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExprssionEditorParser}.
 */
public interface ExprssionEditorListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(ExprssionEditorParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(ExprssionEditorParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(ExprssionEditorParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(ExprssionEditorParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ExprssionEditorParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ExprssionEditorParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(ExprssionEditorParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(ExprssionEditorParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(ExprssionEditorParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(ExprssionEditorParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#arithmeticOperator}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticOperator(ExprssionEditorParser.ArithmeticOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#arithmeticOperator}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticOperator(ExprssionEditorParser.ArithmeticOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(ExprssionEditorParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(ExprssionEditorParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void enterStatementExpression(ExprssionEditorParser.StatementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void exitStatementExpression(ExprssionEditorParser.StatementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void enterParExpression(ExprssionEditorParser.ParExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void exitParExpression(ExprssionEditorParser.ParExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#functions}.
	 * @param ctx the parse tree
	 */
	void enterFunctions(ExprssionEditorParser.FunctionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#functions}.
	 * @param ctx the parse tree
	 */
	void exitFunctions(ExprssionEditorParser.FunctionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprssionEditorParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(ExprssionEditorParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprssionEditorParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(ExprssionEditorParser.LiteralContext ctx);
}