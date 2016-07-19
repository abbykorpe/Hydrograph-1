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
// Generated from QueryParser.g4 by ANTLR 4.4
package hydrograph.server.sqlquery.parser.antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link QueryParserParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface QueryParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link QueryParserParser#andOr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndOr(@NotNull QueryParserParser.AndOrContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryParserParser#fieldname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldname(@NotNull QueryParserParser.FieldnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryParserParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(@NotNull QueryParserParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryParserParser#eval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEval(@NotNull QueryParserParser.EvalContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryParserParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(@NotNull QueryParserParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryParserParser#leftBrace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeftBrace(@NotNull QueryParserParser.LeftBraceContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryParserParser#rightBrace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRightBrace(@NotNull QueryParserParser.RightBraceContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryParserParser#specialexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecialexpr(@NotNull QueryParserParser.SpecialexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryParserParser#javaiden}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJavaiden(@NotNull QueryParserParser.JavaidenContext ctx);
}