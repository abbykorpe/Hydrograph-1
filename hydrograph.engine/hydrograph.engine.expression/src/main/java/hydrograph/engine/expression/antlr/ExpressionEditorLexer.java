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
// Generated from C:/Users/gurdits/git/ExpressionEditor\ExpressionEditor.g4 by ANTLR 4.5.1
package hydrograph.engine.expression.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExpressionEditorLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, LPAREN=26, RPAREN=27, DOT=28, IntegerLiteral=29, FloatingPointLiteral=30, 
		BooleanLiteral=31, CharacterLiteral=32, StringLiteral=33, NullLiteral=34, 
		Identifier=35, WS=36, COMMENT=37, LINE_COMMENT=38;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
		"LPAREN", "RPAREN", "DOT", "IntegerLiteral", "DecimalIntegerLiteral", 
		"HexIntegerLiteral", "OctalIntegerLiteral", "BinaryIntegerLiteral", "IntegerTypeSuffix", 
		"DecimalNumeral", "Digits", "Digit", "NonZeroDigit", "DigitOrUnderscore", 
		"Underscores", "HexNumeral", "HexDigits", "HexDigit", "HexDigitOrUnderscore", 
		"OctalNumeral", "OctalDigits", "OctalDigit", "OctalDigitOrUnderscore", 
		"BinaryNumeral", "BinaryDigits", "BinaryDigit", "BinaryDigitOrUnderscore", 
		"FloatingPointLiteral", "DecimalFloatingPointLiteral", "ExponentPart", 
		"ExponentIndicator", "SignedInteger", "Sign", "FloatTypeSuffix", "HexadecimalFloatingPointLiteral", 
		"HexSignificand", "BinaryExponent", "BinaryExponentIndicator", "BooleanLiteral", 
		"CharacterLiteral", "SingleCharacter", "StringLiteral", "StringCharacters", 
		"StringCharacter", "EscapeSequence", "OctalEscape", "UnicodeEscape", "ZeroToThree", 
		"NullLiteral", "Identifier", "JavaLetter", "JavaLetterOrDigit", "WS", 
		"COMMENT", "LINE_COMMENT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'++'", "'--'", "'+'", "'-'", "'~'", "'!'", "'*'", "'/'", "'%'", 
		"'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&'", "'^'", "'|'", "'&&'", 
		"'||'", "'?'", "':'", "','", "';'", "'new'", "'('", "')'", "'.'", null, 
		null, null, null, null, "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "LPAREN", "RPAREN", "DOT", "IntegerLiteral", "FloatingPointLiteral", 
		"BooleanLiteral", "CharacterLiteral", "StringLiteral", "NullLiteral", 
		"Identifier", "WS", "COMMENT", "LINE_COMMENT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public ExpressionEditorLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ExpressionEditor.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 75:
			return JavaLetter_sempred((RuleContext)_localctx, predIndex);
		case 76:
			return JavaLetterOrDigit_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean JavaLetter_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return Character.isJavaIdentifierStart(_input.LA(-1));
		case 1:
			return Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}
	private boolean JavaLetterOrDigit_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return Character.isJavaIdentifierPart(_input.LA(-1));
		case 3:
			return Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2(\u0224\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\3\2\3\2\3\2\3\3\3"+
		"\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3"+
		"\13\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3"+
		"\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3"+
		"\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3"+
		"\35\3\35\3\36\3\36\3\36\3\36\5\36\u00ea\n\36\3\37\3\37\5\37\u00ee\n\37"+
		"\3 \3 \5 \u00f2\n \3!\3!\5!\u00f6\n!\3\"\3\"\5\"\u00fa\n\"\3#\3#\3$\3"+
		"$\3$\5$\u0101\n$\3$\3$\3$\5$\u0106\n$\5$\u0108\n$\3%\3%\7%\u010c\n%\f"+
		"%\16%\u010f\13%\3%\5%\u0112\n%\3&\3&\5&\u0116\n&\3\'\3\'\3(\3(\5(\u011c"+
		"\n(\3)\6)\u011f\n)\r)\16)\u0120\3*\3*\3*\3*\3+\3+\7+\u0129\n+\f+\16+\u012c"+
		"\13+\3+\5+\u012f\n+\3,\3,\3-\3-\5-\u0135\n-\3.\3.\5.\u0139\n.\3.\3.\3"+
		"/\3/\7/\u013f\n/\f/\16/\u0142\13/\3/\5/\u0145\n/\3\60\3\60\3\61\3\61\5"+
		"\61\u014b\n\61\3\62\3\62\3\62\3\62\3\63\3\63\7\63\u0153\n\63\f\63\16\63"+
		"\u0156\13\63\3\63\5\63\u0159\n\63\3\64\3\64\3\65\3\65\5\65\u015f\n\65"+
		"\3\66\3\66\5\66\u0163\n\66\3\67\3\67\3\67\5\67\u0168\n\67\3\67\5\67\u016b"+
		"\n\67\3\67\5\67\u016e\n\67\3\67\3\67\3\67\5\67\u0173\n\67\3\67\5\67\u0176"+
		"\n\67\3\67\3\67\3\67\5\67\u017b\n\67\3\67\3\67\3\67\5\67\u0180\n\67\3"+
		"8\38\38\39\39\3:\5:\u0188\n:\3:\3:\3;\3;\3<\3<\3=\3=\3=\5=\u0193\n=\3"+
		">\3>\5>\u0197\n>\3>\3>\3>\5>\u019c\n>\3>\3>\5>\u01a0\n>\3?\3?\3?\3@\3"+
		"@\3A\3A\3A\3A\3A\3A\3A\3A\3A\5A\u01b0\nA\3B\3B\3B\3B\3B\3B\3B\3B\5B\u01ba"+
		"\nB\3C\3C\3D\3D\5D\u01c0\nD\3D\3D\3E\6E\u01c5\nE\rE\16E\u01c6\3F\3F\5"+
		"F\u01cb\nF\3G\3G\3G\3G\5G\u01d1\nG\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\5"+
		"H\u01de\nH\3I\3I\3I\3I\3I\3I\3I\3J\3J\3K\3K\3K\3K\3K\3L\3L\7L\u01f0\n"+
		"L\fL\16L\u01f3\13L\3M\3M\3M\3M\3M\3M\5M\u01fb\nM\3N\3N\3N\3N\3N\3N\5N"+
		"\u0203\nN\3O\6O\u0206\nO\rO\16O\u0207\3O\3O\3P\3P\3P\3P\7P\u0210\nP\f"+
		"P\16P\u0213\13P\3P\3P\3P\3P\3P\3Q\3Q\3Q\3Q\7Q\u021e\nQ\fQ\16Q\u0221\13"+
		"Q\3Q\3Q\3\u0211\2R\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r"+
		"\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33"+
		"\65\34\67\359\36;\37=\2?\2A\2C\2E\2G\2I\2K\2M\2O\2Q\2S\2U\2W\2Y\2[\2]"+
		"\2_\2a\2c\2e\2g\2i\2k m\2o\2q\2s\2u\2w\2y\2{\2}\2\177\2\u0081!\u0083\""+
		"\u0085\2\u0087#\u0089\2\u008b\2\u008d\2\u008f\2\u0091\2\u0093\2\u0095"+
		"$\u0097%\u0099\2\u009b\2\u009d&\u009f\'\u00a1(\3\2\30\4\2NNnn\3\2\63;"+
		"\4\2ZZzz\5\2\62;CHch\3\2\629\4\2DDdd\3\2\62\63\4\2GGgg\4\2--//\6\2FFH"+
		"Hffhh\4\2RRrr\4\2))^^\4\2$$^^\n\2$$))^^ddhhppttvv\3\2\62\65\6\2&&C\\a"+
		"ac|\4\2\2\u0081\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;"+
		"C\\aac|\5\2\13\f\16\17\"\"\4\2\f\f\17\17\u0232\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2"+
		"\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2"+
		"\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2k\3\2\2\2\2\u0081"+
		"\3\2\2\2\2\u0083\3\2\2\2\2\u0087\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2"+
		"\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\3\u00a3\3\2\2\2\5\u00a6"+
		"\3\2\2\2\7\u00a9\3\2\2\2\t\u00ab\3\2\2\2\13\u00ad\3\2\2\2\r\u00af\3\2"+
		"\2\2\17\u00b1\3\2\2\2\21\u00b3\3\2\2\2\23\u00b5\3\2\2\2\25\u00b7\3\2\2"+
		"\2\27\u00b9\3\2\2\2\31\u00bb\3\2\2\2\33\u00be\3\2\2\2\35\u00c1\3\2\2\2"+
		"\37\u00c4\3\2\2\2!\u00c7\3\2\2\2#\u00c9\3\2\2\2%\u00cb\3\2\2\2\'\u00cd"+
		"\3\2\2\2)\u00d0\3\2\2\2+\u00d3\3\2\2\2-\u00d5\3\2\2\2/\u00d7\3\2\2\2\61"+
		"\u00d9\3\2\2\2\63\u00db\3\2\2\2\65\u00df\3\2\2\2\67\u00e1\3\2\2\29\u00e3"+
		"\3\2\2\2;\u00e9\3\2\2\2=\u00eb\3\2\2\2?\u00ef\3\2\2\2A\u00f3\3\2\2\2C"+
		"\u00f7\3\2\2\2E\u00fb\3\2\2\2G\u0107\3\2\2\2I\u0109\3\2\2\2K\u0115\3\2"+
		"\2\2M\u0117\3\2\2\2O\u011b\3\2\2\2Q\u011e\3\2\2\2S\u0122\3\2\2\2U\u0126"+
		"\3\2\2\2W\u0130\3\2\2\2Y\u0134\3\2\2\2[\u0136\3\2\2\2]\u013c\3\2\2\2_"+
		"\u0146\3\2\2\2a\u014a\3\2\2\2c\u014c\3\2\2\2e\u0150\3\2\2\2g\u015a\3\2"+
		"\2\2i\u015e\3\2\2\2k\u0162\3\2\2\2m\u017f\3\2\2\2o\u0181\3\2\2\2q\u0184"+
		"\3\2\2\2s\u0187\3\2\2\2u\u018b\3\2\2\2w\u018d\3\2\2\2y\u018f\3\2\2\2{"+
		"\u019f\3\2\2\2}\u01a1\3\2\2\2\177\u01a4\3\2\2\2\u0081\u01af\3\2\2\2\u0083"+
		"\u01b9\3\2\2\2\u0085\u01bb\3\2\2\2\u0087\u01bd\3\2\2\2\u0089\u01c4\3\2"+
		"\2\2\u008b\u01ca\3\2\2\2\u008d\u01d0\3\2\2\2\u008f\u01dd\3\2\2\2\u0091"+
		"\u01df\3\2\2\2\u0093\u01e6\3\2\2\2\u0095\u01e8\3\2\2\2\u0097\u01ed\3\2"+
		"\2\2\u0099\u01fa\3\2\2\2\u009b\u0202\3\2\2\2\u009d\u0205\3\2\2\2\u009f"+
		"\u020b\3\2\2\2\u00a1\u0219\3\2\2\2\u00a3\u00a4\7-\2\2\u00a4\u00a5\7-\2"+
		"\2\u00a5\4\3\2\2\2\u00a6\u00a7\7/\2\2\u00a7\u00a8\7/\2\2\u00a8\6\3\2\2"+
		"\2\u00a9\u00aa\7-\2\2\u00aa\b\3\2\2\2\u00ab\u00ac\7/\2\2\u00ac\n\3\2\2"+
		"\2\u00ad\u00ae\7\u0080\2\2\u00ae\f\3\2\2\2\u00af\u00b0\7#\2\2\u00b0\16"+
		"\3\2\2\2\u00b1\u00b2\7,\2\2\u00b2\20\3\2\2\2\u00b3\u00b4\7\61\2\2\u00b4"+
		"\22\3\2\2\2\u00b5\u00b6\7\'\2\2\u00b6\24\3\2\2\2\u00b7\u00b8\7>\2\2\u00b8"+
		"\26\3\2\2\2\u00b9\u00ba\7@\2\2\u00ba\30\3\2\2\2\u00bb\u00bc\7>\2\2\u00bc"+
		"\u00bd\7?\2\2\u00bd\32\3\2\2\2\u00be\u00bf\7@\2\2\u00bf\u00c0\7?\2\2\u00c0"+
		"\34\3\2\2\2\u00c1\u00c2\7?\2\2\u00c2\u00c3\7?\2\2\u00c3\36\3\2\2\2\u00c4"+
		"\u00c5\7#\2\2\u00c5\u00c6\7?\2\2\u00c6 \3\2\2\2\u00c7\u00c8\7(\2\2\u00c8"+
		"\"\3\2\2\2\u00c9\u00ca\7`\2\2\u00ca$\3\2\2\2\u00cb\u00cc\7~\2\2\u00cc"+
		"&\3\2\2\2\u00cd\u00ce\7(\2\2\u00ce\u00cf\7(\2\2\u00cf(\3\2\2\2\u00d0\u00d1"+
		"\7~\2\2\u00d1\u00d2\7~\2\2\u00d2*\3\2\2\2\u00d3\u00d4\7A\2\2\u00d4,\3"+
		"\2\2\2\u00d5\u00d6\7<\2\2\u00d6.\3\2\2\2\u00d7\u00d8\7.\2\2\u00d8\60\3"+
		"\2\2\2\u00d9\u00da\7=\2\2\u00da\62\3\2\2\2\u00db\u00dc\7p\2\2\u00dc\u00dd"+
		"\7g\2\2\u00dd\u00de\7y\2\2\u00de\64\3\2\2\2\u00df\u00e0\7*\2\2\u00e0\66"+
		"\3\2\2\2\u00e1\u00e2\7+\2\2\u00e28\3\2\2\2\u00e3\u00e4\7\60\2\2\u00e4"+
		":\3\2\2\2\u00e5\u00ea\5=\37\2\u00e6\u00ea\5? \2\u00e7\u00ea\5A!\2\u00e8"+
		"\u00ea\5C\"\2\u00e9\u00e5\3\2\2\2\u00e9\u00e6\3\2\2\2\u00e9\u00e7\3\2"+
		"\2\2\u00e9\u00e8\3\2\2\2\u00ea<\3\2\2\2\u00eb\u00ed\5G$\2\u00ec\u00ee"+
		"\5E#\2\u00ed\u00ec\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee>\3\2\2\2\u00ef\u00f1"+
		"\5S*\2\u00f0\u00f2\5E#\2\u00f1\u00f0\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2"+
		"@\3\2\2\2\u00f3\u00f5\5[.\2\u00f4\u00f6\5E#\2\u00f5\u00f4\3\2\2\2\u00f5"+
		"\u00f6\3\2\2\2\u00f6B\3\2\2\2\u00f7\u00f9\5c\62\2\u00f8\u00fa\5E#\2\u00f9"+
		"\u00f8\3\2\2\2\u00f9\u00fa\3\2\2\2\u00faD\3\2\2\2\u00fb\u00fc\t\2\2\2"+
		"\u00fcF\3\2\2\2\u00fd\u0108\7\62\2\2\u00fe\u0105\5M\'\2\u00ff\u0101\5"+
		"I%\2\u0100\u00ff\3\2\2\2\u0100\u0101\3\2\2\2\u0101\u0106\3\2\2\2\u0102"+
		"\u0103\5Q)\2\u0103\u0104\5I%\2\u0104\u0106\3\2\2\2\u0105\u0100\3\2\2\2"+
		"\u0105\u0102\3\2\2\2\u0106\u0108\3\2\2\2\u0107\u00fd\3\2\2\2\u0107\u00fe"+
		"\3\2\2\2\u0108H\3\2\2\2\u0109\u0111\5K&\2\u010a\u010c\5O(\2\u010b\u010a"+
		"\3\2\2\2\u010c\u010f\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e"+
		"\u0110\3\2\2\2\u010f\u010d\3\2\2\2\u0110\u0112\5K&\2\u0111\u010d\3\2\2"+
		"\2\u0111\u0112\3\2\2\2\u0112J\3\2\2\2\u0113\u0116\7\62\2\2\u0114\u0116"+
		"\5M\'\2\u0115\u0113\3\2\2\2\u0115\u0114\3\2\2\2\u0116L\3\2\2\2\u0117\u0118"+
		"\t\3\2\2\u0118N\3\2\2\2\u0119\u011c\5K&\2\u011a\u011c\7a\2\2\u011b\u0119"+
		"\3\2\2\2\u011b\u011a\3\2\2\2\u011cP\3\2\2\2\u011d\u011f\7a\2\2\u011e\u011d"+
		"\3\2\2\2\u011f\u0120\3\2\2\2\u0120\u011e\3\2\2\2\u0120\u0121\3\2\2\2\u0121"+
		"R\3\2\2\2\u0122\u0123\7\62\2\2\u0123\u0124\t\4\2\2\u0124\u0125\5U+\2\u0125"+
		"T\3\2\2\2\u0126\u012e\5W,\2\u0127\u0129\5Y-\2\u0128\u0127\3\2\2\2\u0129"+
		"\u012c\3\2\2\2\u012a\u0128\3\2\2\2\u012a\u012b\3\2\2\2\u012b\u012d\3\2"+
		"\2\2\u012c\u012a\3\2\2\2\u012d\u012f\5W,\2\u012e\u012a\3\2\2\2\u012e\u012f"+
		"\3\2\2\2\u012fV\3\2\2\2\u0130\u0131\t\5\2\2\u0131X\3\2\2\2\u0132\u0135"+
		"\5W,\2\u0133\u0135\7a\2\2\u0134\u0132\3\2\2\2\u0134\u0133\3\2\2\2\u0135"+
		"Z\3\2\2\2\u0136\u0138\7\62\2\2\u0137\u0139\5Q)\2\u0138\u0137\3\2\2\2\u0138"+
		"\u0139\3\2\2\2\u0139\u013a\3\2\2\2\u013a\u013b\5]/\2\u013b\\\3\2\2\2\u013c"+
		"\u0144\5_\60\2\u013d\u013f\5a\61\2\u013e\u013d\3\2\2\2\u013f\u0142\3\2"+
		"\2\2\u0140\u013e\3\2\2\2\u0140\u0141\3\2\2\2\u0141\u0143\3\2\2\2\u0142"+
		"\u0140\3\2\2\2\u0143\u0145\5_\60\2\u0144\u0140\3\2\2\2\u0144\u0145\3\2"+
		"\2\2\u0145^\3\2\2\2\u0146\u0147\t\6\2\2\u0147`\3\2\2\2\u0148\u014b\5_"+
		"\60\2\u0149\u014b\7a\2\2\u014a\u0148\3\2\2\2\u014a\u0149\3\2\2\2\u014b"+
		"b\3\2\2\2\u014c\u014d\7\62\2\2\u014d\u014e\t\7\2\2\u014e\u014f\5e\63\2"+
		"\u014fd\3\2\2\2\u0150\u0158\5g\64\2\u0151\u0153\5i\65\2\u0152\u0151\3"+
		"\2\2\2\u0153\u0156\3\2\2\2\u0154\u0152\3\2\2\2\u0154\u0155\3\2\2\2\u0155"+
		"\u0157\3\2\2\2\u0156\u0154\3\2\2\2\u0157\u0159\5g\64\2\u0158\u0154\3\2"+
		"\2\2\u0158\u0159\3\2\2\2\u0159f\3\2\2\2\u015a\u015b\t\b\2\2\u015bh\3\2"+
		"\2\2\u015c\u015f\5g\64\2\u015d\u015f\7a\2\2\u015e\u015c\3\2\2\2\u015e"+
		"\u015d\3\2\2\2\u015fj\3\2\2\2\u0160\u0163\5m\67\2\u0161\u0163\5y=\2\u0162"+
		"\u0160\3\2\2\2\u0162\u0161\3\2\2\2\u0163l\3\2\2\2\u0164\u0165\5I%\2\u0165"+
		"\u0167\7\60\2\2\u0166\u0168\5I%\2\u0167\u0166\3\2\2\2\u0167\u0168\3\2"+
		"\2\2\u0168\u016a\3\2\2\2\u0169\u016b\5o8\2\u016a\u0169\3\2\2\2\u016a\u016b"+
		"\3\2\2\2\u016b\u016d\3\2\2\2\u016c\u016e\5w<\2\u016d\u016c\3\2\2\2\u016d"+
		"\u016e\3\2\2\2\u016e\u0180\3\2\2\2\u016f\u0170\7\60\2\2\u0170\u0172\5"+
		"I%\2\u0171\u0173\5o8\2\u0172\u0171\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0175"+
		"\3\2\2\2\u0174\u0176\5w<\2\u0175\u0174\3\2\2\2\u0175\u0176\3\2\2\2\u0176"+
		"\u0180\3\2\2\2\u0177\u0178\5I%\2\u0178\u017a\5o8\2\u0179\u017b\5w<\2\u017a"+
		"\u0179\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u0180\3\2\2\2\u017c\u017d\5I"+
		"%\2\u017d\u017e\5w<\2\u017e\u0180\3\2\2\2\u017f\u0164\3\2\2\2\u017f\u016f"+
		"\3\2\2\2\u017f\u0177\3\2\2\2\u017f\u017c\3\2\2\2\u0180n\3\2\2\2\u0181"+
		"\u0182\5q9\2\u0182\u0183\5s:\2\u0183p\3\2\2\2\u0184\u0185\t\t\2\2\u0185"+
		"r\3\2\2\2\u0186\u0188\5u;\2\u0187\u0186\3\2\2\2\u0187\u0188\3\2\2\2\u0188"+
		"\u0189\3\2\2\2\u0189\u018a\5I%\2\u018at\3\2\2\2\u018b\u018c\t\n\2\2\u018c"+
		"v\3\2\2\2\u018d\u018e\t\13\2\2\u018ex\3\2\2\2\u018f\u0190\5{>\2\u0190"+
		"\u0192\5}?\2\u0191\u0193\5w<\2\u0192\u0191\3\2\2\2\u0192\u0193\3\2\2\2"+
		"\u0193z\3\2\2\2\u0194\u0196\5S*\2\u0195\u0197\7\60\2\2\u0196\u0195\3\2"+
		"\2\2\u0196\u0197\3\2\2\2\u0197\u01a0\3\2\2\2\u0198\u0199\7\62\2\2\u0199"+
		"\u019b\t\4\2\2\u019a\u019c\5U+\2\u019b\u019a\3\2\2\2\u019b\u019c\3\2\2"+
		"\2\u019c\u019d\3\2\2\2\u019d\u019e\7\60\2\2\u019e\u01a0\5U+\2\u019f\u0194"+
		"\3\2\2\2\u019f\u0198\3\2\2\2\u01a0|\3\2\2\2\u01a1\u01a2\5\177@\2\u01a2"+
		"\u01a3\5s:\2\u01a3~\3\2\2\2\u01a4\u01a5\t\f\2\2\u01a5\u0080\3\2\2\2\u01a6"+
		"\u01a7\7v\2\2\u01a7\u01a8\7t\2\2\u01a8\u01a9\7w\2\2\u01a9\u01b0\7g\2\2"+
		"\u01aa\u01ab\7h\2\2\u01ab\u01ac\7c\2\2\u01ac\u01ad\7n\2\2\u01ad\u01ae"+
		"\7u\2\2\u01ae\u01b0\7g\2\2\u01af\u01a6\3\2\2\2\u01af\u01aa\3\2\2\2\u01b0"+
		"\u0082\3\2\2\2\u01b1\u01b2\7)\2\2\u01b2\u01b3\5\u0085C\2\u01b3\u01b4\7"+
		")\2\2\u01b4\u01ba\3\2\2\2\u01b5\u01b6\7)\2\2\u01b6\u01b7\5\u008dG\2\u01b7"+
		"\u01b8\7)\2\2\u01b8\u01ba\3\2\2\2\u01b9\u01b1\3\2\2\2\u01b9\u01b5\3\2"+
		"\2\2\u01ba\u0084\3\2\2\2\u01bb\u01bc\n\r\2\2\u01bc\u0086\3\2\2\2\u01bd"+
		"\u01bf\7$\2\2\u01be\u01c0\5\u0089E\2\u01bf\u01be\3\2\2\2\u01bf\u01c0\3"+
		"\2\2\2\u01c0\u01c1\3\2\2\2\u01c1\u01c2\7$\2\2\u01c2\u0088\3\2\2\2\u01c3"+
		"\u01c5\5\u008bF\2\u01c4\u01c3\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01c4"+
		"\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u008a\3\2\2\2\u01c8\u01cb\n\16\2\2"+
		"\u01c9\u01cb\5\u008dG\2\u01ca\u01c8\3\2\2\2\u01ca\u01c9\3\2\2\2\u01cb"+
		"\u008c\3\2\2\2\u01cc\u01cd\7^\2\2\u01cd\u01d1\t\17\2\2\u01ce\u01d1\5\u008f"+
		"H\2\u01cf\u01d1\5\u0091I\2\u01d0\u01cc\3\2\2\2\u01d0\u01ce\3\2\2\2\u01d0"+
		"\u01cf\3\2\2\2\u01d1\u008e\3\2\2\2\u01d2\u01d3\7^\2\2\u01d3\u01de\5_\60"+
		"\2\u01d4\u01d5\7^\2\2\u01d5\u01d6\5_\60\2\u01d6\u01d7\5_\60\2\u01d7\u01de"+
		"\3\2\2\2\u01d8\u01d9\7^\2\2\u01d9\u01da\5\u0093J\2\u01da\u01db\5_\60\2"+
		"\u01db\u01dc\5_\60\2\u01dc\u01de\3\2\2\2\u01dd\u01d2\3\2\2\2\u01dd\u01d4"+
		"\3\2\2\2\u01dd\u01d8\3\2\2\2\u01de\u0090\3\2\2\2\u01df\u01e0\7^\2\2\u01e0"+
		"\u01e1\7w\2\2\u01e1\u01e2\5W,\2\u01e2\u01e3\5W,\2\u01e3\u01e4\5W,\2\u01e4"+
		"\u01e5\5W,\2\u01e5\u0092\3\2\2\2\u01e6\u01e7\t\20\2\2\u01e7\u0094\3\2"+
		"\2\2\u01e8\u01e9\7p\2\2\u01e9\u01ea\7w\2\2\u01ea\u01eb\7n\2\2\u01eb\u01ec"+
		"\7n\2\2\u01ec\u0096\3\2\2\2\u01ed\u01f1\5\u0099M\2\u01ee\u01f0\5\u009b"+
		"N\2\u01ef\u01ee\3\2\2\2\u01f0\u01f3\3\2\2\2\u01f1\u01ef\3\2\2\2\u01f1"+
		"\u01f2\3\2\2\2\u01f2\u0098\3\2\2\2\u01f3\u01f1\3\2\2\2\u01f4\u01fb\t\21"+
		"\2\2\u01f5\u01f6\n\22\2\2\u01f6\u01fb\6M\2\2\u01f7\u01f8\t\23\2\2\u01f8"+
		"\u01f9\t\24\2\2\u01f9\u01fb\6M\3\2\u01fa\u01f4\3\2\2\2\u01fa\u01f5\3\2"+
		"\2\2\u01fa\u01f7\3\2\2\2\u01fb\u009a\3\2\2\2\u01fc\u0203\t\25\2\2\u01fd"+
		"\u01fe\n\22\2\2\u01fe\u0203\6N\4\2\u01ff\u0200\t\23\2\2\u0200\u0201\t"+
		"\24\2\2\u0201\u0203\6N\5\2\u0202\u01fc\3\2\2\2\u0202\u01fd\3\2\2\2\u0202"+
		"\u01ff\3\2\2\2\u0203\u009c\3\2\2\2\u0204\u0206\t\26\2\2\u0205\u0204\3"+
		"\2\2\2\u0206\u0207\3\2\2\2\u0207\u0205\3\2\2\2\u0207\u0208\3\2\2\2\u0208"+
		"\u0209\3\2\2\2\u0209\u020a\bO\2\2\u020a\u009e\3\2\2\2\u020b\u020c\7\61"+
		"\2\2\u020c\u020d\7,\2\2\u020d\u0211\3\2\2\2\u020e\u0210\13\2\2\2\u020f"+
		"\u020e\3\2\2\2\u0210\u0213\3\2\2\2\u0211\u0212\3\2\2\2\u0211\u020f\3\2"+
		"\2\2\u0212\u0214\3\2\2\2\u0213\u0211\3\2\2\2\u0214\u0215\7,\2\2\u0215"+
		"\u0216\7\61\2\2\u0216\u0217\3\2\2\2\u0217\u0218\bP\2\2\u0218\u00a0\3\2"+
		"\2\2\u0219\u021a\7\61\2\2\u021a\u021b\7\61\2\2\u021b\u021f\3\2\2\2\u021c"+
		"\u021e\n\27\2\2\u021d\u021c\3\2\2\2\u021e\u0221\3\2\2\2\u021f\u021d\3"+
		"\2\2\2\u021f\u0220\3\2\2\2\u0220\u0222\3\2\2\2\u0221\u021f\3\2\2\2\u0222"+
		"\u0223\bQ\2\2\u0223\u00a2\3\2\2\2\64\2\u00e9\u00ed\u00f1\u00f5\u00f9\u0100"+
		"\u0105\u0107\u010d\u0111\u0115\u011b\u0120\u012a\u012e\u0134\u0138\u0140"+
		"\u0144\u014a\u0154\u0158\u015e\u0162\u0167\u016a\u016d\u0172\u0175\u017a"+
		"\u017f\u0187\u0192\u0196\u019b\u019f\u01af\u01b9\u01bf\u01c6\u01ca\u01d0"+
		"\u01dd\u01f1\u01fa\u0202\u0207\u0211\u021f\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}