package hydrograph.engine.expression.antlr;// Generated from C:/Users/gurdits/git/ExpressionEditor\ExprssionEditor.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExprssionEditorParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExprssionEditorVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(ExprssionEditorParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(ExprssionEditorParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(ExprssionEditorParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(ExprssionEditorParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(ExprssionEditorParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#arithmeticOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticOperator(ExprssionEditorParser.ArithmeticOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(ExprssionEditorParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#statementExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementExpression(ExprssionEditorParser.StatementExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#parExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExpression(ExprssionEditorParser.ParExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#functions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctions(ExprssionEditorParser.FunctionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprssionEditorParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(ExprssionEditorParser.LiteralContext ctx);
}