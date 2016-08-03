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


import java.util.List;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class QueryParserParser extends Parser {
	static {
		RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__22 = 1, T__21 = 2, T__20 = 3, T__19 = 4, T__18 = 5, T__17 = 6, T__16 = 7, T__15 = 8,
			T__14 = 9, T__13 = 10, T__12 = 11, T__11 = 12, T__10 = 13, T__9 = 14, T__8 = 15, T__7 = 16, T__6 = 17,
			T__5 = 18, T__4 = 19, T__3 = 20, T__2 = 21, T__1 = 22, T__0 = 23, FieldIdentifier = 24, Identifier = 25,
			WS = 26;
	public static final String[] tokenNames = { "<INVALID>", "' and '", "'NOT IN'", "'like'", "'LIKE'", "'not like'",
			"' OR '", "'not in'", "'>='", "';'", "'<'", "'='", "'>'", "'BETWEEN'", "'<='", "'<>'", "'IN'", "'in'",
			"' or '", "'('", "'between'", "')'", "' AND '", "'NOT LIKE'", "FieldIdentifier", "Identifier", "WS" };
	public static final int RULE_eval = 0, RULE_expression = 1, RULE_leftBrace = 2, RULE_rightBrace = 3,
			RULE_specialexpr = 4, RULE_andOr = 5, RULE_condition = 6, RULE_javaiden = 7, RULE_fieldname = 8;
	public static final String[] ruleNames = { "eval", "expression", "leftBrace", "rightBrace", "specialexpr", "andOr",
			"condition", "javaiden", "fieldname" };

	@Override
	public String getGrammarFileName() {
		return "QueryParser.g4";
	}

	@Override
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override
	public String[] getRuleNames() {
		return ruleNames;
	}

	@Override
	public String getSerializedATN() {
		return _serializedATN;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	public QueryParserParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	public static class EvalContext extends ParserRuleContext {
		public List<RightBraceContext> rightBrace() {
			return getRuleContexts(RightBraceContext.class);
		}

		public List<LeftBraceContext> leftBrace() {
			return getRuleContexts(LeftBraceContext.class);
		}

		public List<AndOrContext> andOr() {
			return getRuleContexts(AndOrContext.class);
		}

		public EvalContext eval() {
			return getRuleContext(EvalContext.class, 0);
		}

		public LeftBraceContext leftBrace(int i) {
			return getRuleContext(LeftBraceContext.class, i);
		}

		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class, i);
		}

		public AndOrContext andOr(int i) {
			return getRuleContext(AndOrContext.class, i);
		}

		public RightBraceContext rightBrace(int i) {
			return getRuleContext(RightBraceContext.class, i);
		}

		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}

		public EvalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_eval;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).enterEval(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).exitEval(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof QueryParserVisitor)
				return ((QueryParserVisitor<? extends T>) visitor).visitEval(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final EvalContext eval() throws RecognitionException {
		return eval(0);
	}

	private EvalContext eval(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		EvalContext _localctx = new EvalContext(_ctx, _parentState);
		EvalContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_eval, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				{
					setState(22);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la == T__4) {
						{
							{
								setState(19);
								leftBrace();
							}
						}
						setState(24);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(25);
					expression();
					setState(42);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
					while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
						if (_alt == 1) {
							{
								{
									setState(26);
									andOr();
									setState(30);
									_errHandler.sync(this);
									_la = _input.LA(1);
									while (_la == T__4) {
										{
											{
												setState(27);
												leftBrace();
											}
										}
										setState(32);
										_errHandler.sync(this);
										_la = _input.LA(1);
									}
									setState(33);
									expression();
									setState(37);
									_errHandler.sync(this);
									_alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
									while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
										if (_alt == 1) {
											{
												{
													setState(34);
													rightBrace();
												}
											}
										}
										setState(39);
										_errHandler.sync(this);
										_alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
									}
								}
							}
						}
						setState(44);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
					}
					setState(48);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
					while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
						if (_alt == 1) {
							{
								{
									setState(45);
									rightBrace();
								}
							}
						}
						setState(50);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
					}
				}
				_ctx.stop = _input.LT(-1);
				setState(55);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
				while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (_parseListeners != null)
							triggerExitRuleEvent();
						_prevctx = _localctx;
						{
							{
								_localctx = new EvalContext(_parentctx, _parentState);
								pushNewRecursionContext(_localctx, _startState, RULE_eval);
								setState(51);
								if (!(precpred(_ctx, 1)))
									throw new FailedPredicateException(this, "precpred(_ctx, 1)");
								setState(52);
								match(T__14);
							}
						}
					}
					setState(57);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public JavaidenContext javaiden() {
			return getRuleContext(JavaidenContext.class, 0);
		}

		public FieldnameContext fieldname(int i) {
			return getRuleContext(FieldnameContext.class, i);
		}

		public SpecialexprContext specialexpr() {
			return getRuleContext(SpecialexprContext.class, 0);
		}

		public List<FieldnameContext> fieldname() {
			return getRuleContexts(FieldnameContext.class);
		}

		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class, 0);
		}

		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_expression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).enterExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).exitExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof QueryParserVisitor)
				return ((QueryParserVisitor<? extends T>) visitor).visitExpression(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(58);
				fieldname();
				setState(64);
				switch (getInterpreter().adaptivePredict(_input, 7, _ctx)) {
				case 1: {
					setState(59);
					condition();
					setState(62);
					switch (_input.LA(1)) {
					case Identifier: {
						setState(60);
						javaiden();
					}
						break;
					case FieldIdentifier: {
						setState(61);
						fieldname();
					}
						break;
					default:
						throw new NoViableAltException(this);
					}
				}
					break;
				}
				setState(67);
				switch (getInterpreter().adaptivePredict(_input, 8, _ctx)) {
				case 1: {
					setState(66);
					specialexpr();
				}
					break;
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LeftBraceContext extends ParserRuleContext {
		public LeftBraceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_leftBrace;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).enterLeftBrace(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).exitLeftBrace(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof QueryParserVisitor)
				return ((QueryParserVisitor<? extends T>) visitor).visitLeftBrace(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final LeftBraceContext leftBrace() throws RecognitionException {
		LeftBraceContext _localctx = new LeftBraceContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_leftBrace);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(69);
				match(T__4);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RightBraceContext extends ParserRuleContext {
		public RightBraceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_rightBrace;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).enterRightBrace(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).exitRightBrace(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof QueryParserVisitor)
				return ((QueryParserVisitor<? extends T>) visitor).visitRightBrace(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final RightBraceContext rightBrace() throws RecognitionException {
		RightBraceContext _localctx = new RightBraceContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_rightBrace);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(71);
				match(T__2);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpecialexprContext extends ParserRuleContext {
		public RightBraceContext rightBrace() {
			return getRuleContext(RightBraceContext.class, 0);
		}

		public LeftBraceContext leftBrace() {
			return getRuleContext(LeftBraceContext.class, 0);
		}

		public List<JavaidenContext> javaiden() {
			return getRuleContexts(JavaidenContext.class);
		}

		public AndOrContext andOr() {
			return getRuleContext(AndOrContext.class, 0);
		}

		public JavaidenContext javaiden(int i) {
			return getRuleContext(JavaidenContext.class, i);
		}

		public SpecialexprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_specialexpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).enterSpecialexpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).exitSpecialexpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof QueryParserVisitor)
				return ((QueryParserVisitor<? extends T>) visitor).visitSpecialexpr(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final SpecialexprContext specialexpr() throws RecognitionException {
		SpecialexprContext _localctx = new SpecialexprContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_specialexpr);
		int _la;
		try {
			setState(133);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1); {
				setState(73);
				match(T__6);
				setState(74);
				leftBrace();
				setState(75);
				javaiden();
				setState(76);
				rightBrace();
			}
				break;
			case T__16:
				enterOuterAlt(_localctx, 2); {
				setState(78);
				match(T__16);
				setState(79);
				leftBrace();
				setState(80);
				javaiden();
				setState(81);
				rightBrace();
			}
				break;
			case T__3:
				enterOuterAlt(_localctx, 3); {
				setState(83);
				match(T__3);
				setState(84);
				javaiden();
				setState(85);
				andOr();
				setState(86);
				javaiden();
			}
				break;
			case T__20:
				enterOuterAlt(_localctx, 4); {
				setState(88);
				match(T__20);
				setState(90);
				_la = _input.LA(1);
				if (_la == T__4) {
					{
						setState(89);
						leftBrace();
					}
				}

				setState(92);
				javaiden();
				setState(94);
				switch (getInterpreter().adaptivePredict(_input, 10, _ctx)) {
				case 1: {
					setState(93);
					rightBrace();
				}
					break;
				}
			}
				break;
			case T__18:
				enterOuterAlt(_localctx, 5); {
				setState(96);
				match(T__18);
				setState(98);
				_la = _input.LA(1);
				if (_la == T__4) {
					{
						setState(97);
						leftBrace();
					}
				}

				setState(100);
				javaiden();
				setState(102);
				switch (getInterpreter().adaptivePredict(_input, 12, _ctx)) {
				case 1: {
					setState(101);
					rightBrace();
				}
					break;
				}
			}
				break;
			case T__7:
				enterOuterAlt(_localctx, 6); {
				setState(104);
				match(T__7);
				setState(105);
				leftBrace();
				setState(106);
				javaiden();
				setState(107);
				rightBrace();
			}
				break;
			case T__21:
				enterOuterAlt(_localctx, 7); {
				setState(109);
				match(T__21);
				setState(110);
				leftBrace();
				setState(111);
				javaiden();
				setState(112);
				rightBrace();
			}
				break;
			case T__10:
				enterOuterAlt(_localctx, 8); {
				setState(119);
				match(T__10);
				setState(122);
				switch (_input.LA(1)) {
				case Identifier: {
					setState(120);
					javaiden();
				}
					break;
				case FieldIdentifier: {
					setState(121);
					fieldname();
				}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(124);
				andOr();
				setState(127);
				switch (_input.LA(1)) {
				case Identifier: {
					setState(125);
					javaiden();
				}
					break;
				case FieldIdentifier: {
					setState(126);
					fieldname();
				}
					break;
				default:
					throw new NoViableAltException(this);
				}
			}
				break;
			case T__19:
				enterOuterAlt(_localctx, 9); {
				setState(129);
				match(T__11);
				setState(130);
				javaiden();
			}
				break;
			case T__0:
				enterOuterAlt(_localctx, 10); {
				setState(131);
				match(T__12);
				setState(132);
				javaiden();
			}
				break;
			default:
				throw new NoViableAltException(this);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndOrContext extends ParserRuleContext {
		public AndOrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_andOr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).enterAndOr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).exitAndOr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof QueryParserVisitor)
				return ((QueryParserVisitor<? extends T>) visitor).visitAndOr(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final AndOrContext andOr() throws RecognitionException {
		AndOrContext _localctx = new AndOrContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_andOr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(135);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0
						&& ((1L << _la) & ((1L << T__22) | (1L << T__17) | (1L << T__5) | (1L << T__1))) != 0))) {
					_errHandler.recoverInline(this);
				}
				consume();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_condition;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).enterCondition(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).exitCondition(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof QueryParserVisitor)
				return ((QueryParserVisitor<? extends T>) visitor).visitCondition(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_condition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(137);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__15) | (1L << T__13) | (1L << T__12)
						| (1L << T__11) | (1L << T__9) | (1L << T__8))) != 0))) {
					_errHandler.recoverInline(this);
				}
				consume();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JavaidenContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() {
			return getTokens(QueryParserParser.Identifier);
		}

		public TerminalNode Identifier(int i) {
			return getToken(QueryParserParser.Identifier, i);
		}

		public JavaidenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_javaiden;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).enterJavaiden(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).exitJavaiden(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof QueryParserVisitor)
				return ((QueryParserVisitor<? extends T>) visitor).visitJavaiden(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final JavaidenContext javaiden() throws RecognitionException {
		JavaidenContext _localctx = new JavaidenContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_javaiden);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(140);
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1: {
						{
							setState(139);
							match(Identifier);
						}
					}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(142);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 18, _ctx);
				} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldnameContext extends ParserRuleContext {
		public TerminalNode FieldIdentifier() {
			return getToken(QueryParserParser.FieldIdentifier, 0);
		}

		public FieldnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_fieldname;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).enterFieldname(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof QueryParserListener)
				((QueryParserListener) listener).exitFieldname(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof QueryParserVisitor)
				return ((QueryParserVisitor<? extends T>) visitor).visitFieldname(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final FieldnameContext fieldname() throws RecognitionException {
		FieldnameContext _localctx = new FieldnameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_fieldname);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(144);
				match(FieldIdentifier);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0:
			return eval_sempred((EvalContext) _localctx, predIndex);
		}
		return true;
	}

	private boolean eval_sempred(EvalContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\34\u0095\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3"+
		"\2\7\2\27\n\2\f\2\16\2\32\13\2\3\2\3\2\3\2\7\2\37\n\2\f\2\16\2\"\13\2"+
		"\3\2\3\2\7\2&\n\2\f\2\16\2)\13\2\7\2+\n\2\f\2\16\2.\13\2\3\2\7\2\61\n"+
		"\2\f\2\16\2\64\13\2\3\2\3\2\7\28\n\2\f\2\16\2;\13\2\3\3\3\3\3\3\3\3\5"+
		"\3A\n\3\5\3C\n\3\3\3\5\3F\n\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6Y\n\6\3\6\3\6\3\6\5\6^\n\6\3\6\3\6\5\6"+
		"b\n\6\3\6\3\6\5\6f\n\6\3\6\3\6\5\6j\n\6\3\6\3\6\5\6n\n\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6}\n\6\3\6\3\6\3\6\5\6\u0082"+
		"\n\6\3\6\3\6\3\6\3\6\5\6\u0088\n\6\3\7\3\7\3\b\3\b\3\t\6\t\u008f\n\t\r"+
		"\t\16\t\u0090\3\n\3\n\3\n\2\3\2\13\2\4\6\b\n\f\16\20\22\2\4\3\2\20\23"+
		"\3\2\24\31\u00a6\2\24\3\2\2\2\4<\3\2\2\2\6G\3\2\2\2\bI\3\2\2\2\n\u0087"+
		"\3\2\2\2\f\u0089\3\2\2\2\16\u008b\3\2\2\2\20\u008e\3\2\2\2\22\u0092\3"+
		"\2\2\2\24\30\b\2\1\2\25\27\5\6\4\2\26\25\3\2\2\2\27\32\3\2\2\2\30\26\3"+
		"\2\2\2\30\31\3\2\2\2\31\33\3\2\2\2\32\30\3\2\2\2\33,\5\4\3\2\34 \5\f\7"+
		"\2\35\37\5\6\4\2\36\35\3\2\2\2\37\"\3\2\2\2 \36\3\2\2\2 !\3\2\2\2!#\3"+
		"\2\2\2\" \3\2\2\2#\'\5\4\3\2$&\5\b\5\2%$\3\2\2\2&)\3\2\2\2\'%\3\2\2\2"+
		"\'(\3\2\2\2(+\3\2\2\2)\'\3\2\2\2*\34\3\2\2\2+.\3\2\2\2,*\3\2\2\2,-\3\2"+
		"\2\2-\62\3\2\2\2.,\3\2\2\2/\61\5\b\5\2\60/\3\2\2\2\61\64\3\2\2\2\62\60"+
		"\3\2\2\2\62\63\3\2\2\2\639\3\2\2\2\64\62\3\2\2\2\65\66\f\3\2\2\668\7\3"+
		"\2\2\67\65\3\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2:\3\3\2\2\2;9\3\2\2"+
		"\2<B\5\22\n\2=@\5\16\b\2>A\5\20\t\2?A\5\22\n\2@>\3\2\2\2@?\3\2\2\2AC\3"+
		"\2\2\2B=\3\2\2\2BC\3\2\2\2CE\3\2\2\2DF\5\n\6\2ED\3\2\2\2EF\3\2\2\2F\5"+
		"\3\2\2\2GH\7\4\2\2H\7\3\2\2\2IJ\7\5\2\2J\t\3\2\2\2KL\7\6\2\2LM\5\6\4\2"+
		"MN\5\20\t\2NO\5\b\5\2O\u0088\3\2\2\2PQ\7\7\2\2QR\5\6\4\2RS\5\20\t\2ST"+
		"\5\b\5\2T\u0088\3\2\2\2UX\7\b\2\2VY\5\20\t\2WY\5\22\n\2XV\3\2\2\2XW\3"+
		"\2\2\2YZ\3\2\2\2Z]\5\f\7\2[^\5\20\t\2\\^\5\22\n\2][\3\2\2\2]\\\3\2\2\2"+
		"^\u0088\3\2\2\2_a\7\t\2\2`b\5\6\4\2a`\3\2\2\2ab\3\2\2\2bc\3\2\2\2ce\5"+
		"\20\t\2df\5\b\5\2ed\3\2\2\2ef\3\2\2\2f\u0088\3\2\2\2gi\7\n\2\2hj\5\6\4"+
		"\2ih\3\2\2\2ij\3\2\2\2jk\3\2\2\2km\5\20\t\2ln\5\b\5\2ml\3\2\2\2mn\3\2"+
		"\2\2n\u0088\3\2\2\2op\7\13\2\2pq\5\6\4\2qr\5\20\t\2rs\5\b\5\2s\u0088\3"+
		"\2\2\2tu\7\f\2\2uv\5\6\4\2vw\5\20\t\2wx\5\b\5\2x\u0088\3\2\2\2y|\7\r\2"+
		"\2z}\5\20\t\2{}\5\22\n\2|z\3\2\2\2|{\3\2\2\2}~\3\2\2\2~\u0081\5\f\7\2"+
		"\177\u0082\5\20\t\2\u0080\u0082\5\22\n\2\u0081\177\3\2\2\2\u0081\u0080"+
		"\3\2\2\2\u0082\u0088\3\2\2\2\u0083\u0084\7\16\2\2\u0084\u0088\5\20\t\2"+
		"\u0085\u0086\7\17\2\2\u0086\u0088\5\20\t\2\u0087K\3\2\2\2\u0087P\3\2\2"+
		"\2\u0087U\3\2\2\2\u0087_\3\2\2\2\u0087g\3\2\2\2\u0087o\3\2\2\2\u0087t"+
		"\3\2\2\2\u0087y\3\2\2\2\u0087\u0083\3\2\2\2\u0087\u0085\3\2\2\2\u0088"+
		"\13\3\2\2\2\u0089\u008a\t\2\2\2\u008a\r\3\2\2\2\u008b\u008c\t\3\2\2\u008c"+
		"\17\3\2\2\2\u008d\u008f\7\33\2\2\u008e\u008d\3\2\2\2\u008f\u0090\3\2\2"+
		"\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\21\3\2\2\2\u0092\u0093"+
		"\7\32\2\2\u0093\23\3\2\2\2\25\30 \',\629@BEX]aeim|\u0081\u0087\u0090";
	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}