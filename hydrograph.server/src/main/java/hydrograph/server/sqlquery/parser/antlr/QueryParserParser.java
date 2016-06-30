/*******************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
// Generated from QueryParser.g4 by ANTLR 4.4
package hydrograph.server.sqlquery.parser.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class QueryParserParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__20=1, T__19=2, T__18=3, T__17=4, T__16=5, T__15=6, T__14=7, T__13=8, 
		T__12=9, T__11=10, T__10=11, T__9=12, T__8=13, T__7=14, T__6=15, T__5=16, 
		T__4=17, T__3=18, T__2=19, T__1=20, T__0=21, Identifier=22, WS=23;
	public static final String[] tokenNames = {
		"<INVALID>", "'NOT IN'", "'like'", "'LIKE'", "'not like'", "'not in'", 
		"'>='", "';'", "'<'", "'='", "'>'", "'BETWEEN'", "'or'", "'<='", "'<>'", 
		"'IN'", "'in'", "'('", "'between'", "')'", "'and'", "'NOT LIKE'", "Identifier", 
		"WS"
	};
	public static final int
		RULE_eval = 0, RULE_expression = 1, RULE_leftBrace = 2, RULE_rightBrace = 3, 
		RULE_specialexpr = 4, RULE_andOr = 5, RULE_condition = 6, RULE_javaiden = 7, 
		RULE_fieldname = 8;
	public static final String[] ruleNames = {
		"eval", "expression", "leftBrace", "rightBrace", "specialexpr", "andOr", 
		"condition", "javaiden", "fieldname"
	};

	@Override
	public String getGrammarFileName() { return "QueryParser.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public QueryParserParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
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
			return getRuleContext(EvalContext.class,0);
		}
		public LeftBraceContext leftBrace(int i) {
			return getRuleContext(LeftBraceContext.class,i);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AndOrContext andOr(int i) {
			return getRuleContext(AndOrContext.class,i);
		}
		public RightBraceContext rightBrace(int i) {
			return getRuleContext(RightBraceContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public EvalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterEval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitEval(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitEval(this);
			else return visitor.visitChildren(this);
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
			setState(20);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(19); leftBrace();
				}
			}

			setState(22); expression();
			setState(39);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(23); andOr();
					setState(27);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__4) {
						{
						{
						setState(24); leftBrace();
						}
						}
						setState(29);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(30); expression();
					setState(34);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(31); rightBrace();
							}
							} 
						}
						setState(36);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
					}
					}
					} 
				}
				setState(41);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(43);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(42); rightBrace();
				}
				break;
			}
			}
			_ctx.stop = _input.LT(-1);
			setState(49);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new EvalContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_eval);
					setState(45);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(46); match(T__14);
					}
					} 
				}
				setState(51);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public JavaidenContext javaiden() {
			return getRuleContext(JavaidenContext.class,0);
		}
		public SpecialexprContext specialexpr() {
			return getRuleContext(SpecialexprContext.class,0);
		}
		public FieldnameContext fieldname() {
			return getRuleContext(FieldnameContext.class,0);
		}
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52); fieldname();
			setState(56);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(53); condition();
				setState(54); javaiden();
				}
				break;
			}
			setState(59);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(58); specialexpr();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LeftBraceContext extends ParserRuleContext {
		public LeftBraceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_leftBrace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterLeftBrace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitLeftBrace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitLeftBrace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LeftBraceContext leftBrace() throws RecognitionException {
		LeftBraceContext _localctx = new LeftBraceContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_leftBrace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61); match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RightBraceContext extends ParserRuleContext {
		public RightBraceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rightBrace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterRightBrace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitRightBrace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitRightBrace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RightBraceContext rightBrace() throws RecognitionException {
		RightBraceContext _localctx = new RightBraceContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_rightBrace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63); match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpecialexprContext extends ParserRuleContext {
		public RightBraceContext rightBrace() {
			return getRuleContext(RightBraceContext.class,0);
		}
		public LeftBraceContext leftBrace() {
			return getRuleContext(LeftBraceContext.class,0);
		}
		public List<JavaidenContext> javaiden() {
			return getRuleContexts(JavaidenContext.class);
		}
		public AndOrContext andOr() {
			return getRuleContext(AndOrContext.class,0);
		}
		public JavaidenContext javaiden(int i) {
			return getRuleContext(JavaidenContext.class,i);
		}
		public SpecialexprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specialexpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterSpecialexpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitSpecialexpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitSpecialexpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecialexprContext specialexpr() throws RecognitionException {
		SpecialexprContext _localctx = new SpecialexprContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_specialexpr);
		try {
			setState(103);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(65); match(T__5);
				setState(66); leftBrace();
				setState(67); javaiden();
				setState(68); rightBrace();
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 2);
				{
				setState(70); match(T__16);
				setState(71); leftBrace();
				setState(72); javaiden();
				setState(73); rightBrace();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 3);
				{
				setState(75); match(T__3);
				setState(76); javaiden();
				setState(77); andOr();
				setState(78); javaiden();
				}
				break;
			case T__19:
				enterOuterAlt(_localctx, 4);
				{
				setState(80); match(T__19);
				setState(81); javaiden();
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 5);
				{
				setState(82); match(T__17);
				setState(83); javaiden();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 6);
				{
				setState(84); match(T__6);
				setState(85); leftBrace();
				setState(86); javaiden();
				setState(87); rightBrace();
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 7);
				{
				setState(89); match(T__20);
				setState(90); leftBrace();
				setState(91); javaiden();
				setState(92); rightBrace();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 8);
				{
				setState(94); match(T__10);
				setState(95); javaiden();
				setState(96); andOr();
				setState(97); javaiden();
				}
				break;
			case T__18:
				enterOuterAlt(_localctx, 9);
				{
				setState(99); match(T__18);
				setState(100); javaiden();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 10);
				{
				setState(101); match(T__0);
				setState(102); javaiden();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndOrContext extends ParserRuleContext {
		public AndOrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andOr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterAndOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitAndOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitAndOr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndOrContext andOr() throws RecognitionException {
		AndOrContext _localctx = new AndOrContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_andOr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			_la = _input.LA(1);
			if ( !(_la==T__9 || _la==T__1) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_condition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__15) | (1L << T__13) | (1L << T__12) | (1L << T__11) | (1L << T__8) | (1L << T__7))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JavaidenContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(QueryParserParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(QueryParserParser.Identifier, i);
		}
		public JavaidenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_javaiden; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterJavaiden(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitJavaiden(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitJavaiden(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JavaidenContext javaiden() throws RecognitionException {
		JavaidenContext _localctx = new JavaidenContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_javaiden);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(110); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(109); match(Identifier);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(112); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldnameContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(QueryParserParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(QueryParserParser.Identifier, i);
		}
		public FieldnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldname; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterFieldname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitFieldname(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitFieldname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldnameContext fieldname() throws RecognitionException {
		FieldnameContext _localctx = new FieldnameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_fieldname);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(115); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(114); match(Identifier);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(117); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0: return eval_sempred((EvalContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean eval_sempred(EvalContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\31z\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2\5\2"+
		"\27\n\2\3\2\3\2\3\2\7\2\34\n\2\f\2\16\2\37\13\2\3\2\3\2\7\2#\n\2\f\2\16"+
		"\2&\13\2\7\2(\n\2\f\2\16\2+\13\2\3\2\5\2.\n\2\3\2\3\2\7\2\62\n\2\f\2\16"+
		"\2\65\13\2\3\3\3\3\3\3\3\3\5\3;\n\3\3\3\5\3>\n\3\3\4\3\4\3\5\3\5\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\5\6j\n\6\3\7\3\7\3\b\3\b\3\t\6\tq\n\t\r\t\16\tr\3\n\6\nv\n\n\r"+
		"\n\16\nw\3\n\2\3\2\13\2\4\6\b\n\f\16\20\22\2\4\4\2\16\16\26\26\5\2\b\b"+
		"\n\f\17\20\u0083\2\24\3\2\2\2\4\66\3\2\2\2\6?\3\2\2\2\bA\3\2\2\2\ni\3"+
		"\2\2\2\fk\3\2\2\2\16m\3\2\2\2\20p\3\2\2\2\22u\3\2\2\2\24\26\b\2\1\2\25"+
		"\27\5\6\4\2\26\25\3\2\2\2\26\27\3\2\2\2\27\30\3\2\2\2\30)\5\4\3\2\31\35"+
		"\5\f\7\2\32\34\5\6\4\2\33\32\3\2\2\2\34\37\3\2\2\2\35\33\3\2\2\2\35\36"+
		"\3\2\2\2\36 \3\2\2\2\37\35\3\2\2\2 $\5\4\3\2!#\5\b\5\2\"!\3\2\2\2#&\3"+
		"\2\2\2$\"\3\2\2\2$%\3\2\2\2%(\3\2\2\2&$\3\2\2\2\'\31\3\2\2\2(+\3\2\2\2"+
		")\'\3\2\2\2)*\3\2\2\2*-\3\2\2\2+)\3\2\2\2,.\5\b\5\2-,\3\2\2\2-.\3\2\2"+
		"\2.\63\3\2\2\2/\60\f\3\2\2\60\62\7\t\2\2\61/\3\2\2\2\62\65\3\2\2\2\63"+
		"\61\3\2\2\2\63\64\3\2\2\2\64\3\3\2\2\2\65\63\3\2\2\2\66:\5\22\n\2\678"+
		"\5\16\b\289\5\20\t\29;\3\2\2\2:\67\3\2\2\2:;\3\2\2\2;=\3\2\2\2<>\5\n\6"+
		"\2=<\3\2\2\2=>\3\2\2\2>\5\3\2\2\2?@\7\23\2\2@\7\3\2\2\2AB\7\25\2\2B\t"+
		"\3\2\2\2CD\7\22\2\2DE\5\6\4\2EF\5\20\t\2FG\5\b\5\2Gj\3\2\2\2HI\7\7\2\2"+
		"IJ\5\6\4\2JK\5\20\t\2KL\5\b\5\2Lj\3\2\2\2MN\7\24\2\2NO\5\20\t\2OP\5\f"+
		"\7\2PQ\5\20\t\2Qj\3\2\2\2RS\7\4\2\2Sj\5\20\t\2TU\7\6\2\2Uj\5\20\t\2VW"+
		"\7\21\2\2WX\5\6\4\2XY\5\20\t\2YZ\5\b\5\2Zj\3\2\2\2[\\\7\3\2\2\\]\5\6\4"+
		"\2]^\5\20\t\2^_\5\b\5\2_j\3\2\2\2`a\7\r\2\2ab\5\20\t\2bc\5\f\7\2cd\5\20"+
		"\t\2dj\3\2\2\2ef\7\5\2\2fj\5\20\t\2gh\7\27\2\2hj\5\20\t\2iC\3\2\2\2iH"+
		"\3\2\2\2iM\3\2\2\2iR\3\2\2\2iT\3\2\2\2iV\3\2\2\2i[\3\2\2\2i`\3\2\2\2i"+
		"e\3\2\2\2ig\3\2\2\2j\13\3\2\2\2kl\t\2\2\2l\r\3\2\2\2mn\t\3\2\2n\17\3\2"+
		"\2\2oq\7\30\2\2po\3\2\2\2qr\3\2\2\2rp\3\2\2\2rs\3\2\2\2s\21\3\2\2\2tv"+
		"\7\30\2\2ut\3\2\2\2vw\3\2\2\2wu\3\2\2\2wx\3\2\2\2x\23\3\2\2\2\r\26\35"+
		"$)-\63:=irw";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}