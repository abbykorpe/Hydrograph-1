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
		LPAREN=25, RPAREN=26, DOT=27, IntegerLiteral=28, FloatingPointLiteral=29, 
		BooleanLiteral=30, CharacterLiteral=31, StringLiteral=32, NullLiteral=33, 
		Identifier=34, WS=35, COMMENT=36, LINE_COMMENT=37;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "LPAREN", 
		"RPAREN", "DOT", "IntegerLiteral", "DecimalIntegerLiteral", "HexIntegerLiteral", 
		"OctalIntegerLiteral", "BinaryIntegerLiteral", "IntegerTypeSuffix", "DecimalNumeral", 
		"Digits", "Digit", "NonZeroDigit", "DigitOrUnderscore", "Underscores", 
		"HexNumeral", "HexDigits", "HexDigit", "HexDigitOrUnderscore", "OctalNumeral", 
		"OctalDigits", "OctalDigit", "OctalDigitOrUnderscore", "BinaryNumeral", 
		"BinaryDigits", "BinaryDigit", "BinaryDigitOrUnderscore", "FloatingPointLiteral", 
		"DecimalFloatingPointLiteral", "ExponentPart", "ExponentIndicator", "SignedInteger", 
		"Sign", "FloatTypeSuffix", "HexadecimalFloatingPointLiteral", "HexSignificand", 
		"BinaryExponent", "BinaryExponentIndicator", "BooleanLiteral", "CharacterLiteral", 
		"SingleCharacter", "StringLiteral", "StringCharacters", "StringCharacter", 
		"EscapeSequence", "OctalEscape", "UnicodeEscape", "ZeroToThree", "NullLiteral", 
		"Identifier", "JavaLetter", "JavaLetterOrDigit", "WS", "COMMENT", "LINE_COMMENT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'++'", "'--'", "'+'", "'-'", "'~'", "'!'", "'*'", "'/'", "'%'", 
		"'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&'", "'^'", "'|'", "'&&'", 
		"'||'", "'?'", "':'", "','", "';'", "'('", "')'", "'.'", null, null, null, 
		null, null, "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "LPAREN", "RPAREN", "DOT", "IntegerLiteral", "FloatingPointLiteral", 
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
		case 74:
			return JavaLetter_sempred((RuleContext)_localctx, predIndex);
		case 75:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\'\u021e\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\3\2\3\2\3\2\3\3\3\3\3\3"+
		"\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f"+
		"\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21"+
		"\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\27\3\27"+
		"\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\35\3\35"+
		"\5\35\u00e4\n\35\3\36\3\36\5\36\u00e8\n\36\3\37\3\37\5\37\u00ec\n\37\3"+
		" \3 \5 \u00f0\n \3!\3!\5!\u00f4\n!\3\"\3\"\3#\3#\3#\5#\u00fb\n#\3#\3#"+
		"\3#\5#\u0100\n#\5#\u0102\n#\3$\3$\7$\u0106\n$\f$\16$\u0109\13$\3$\5$\u010c"+
		"\n$\3%\3%\5%\u0110\n%\3&\3&\3\'\3\'\5\'\u0116\n\'\3(\6(\u0119\n(\r(\16"+
		"(\u011a\3)\3)\3)\3)\3*\3*\7*\u0123\n*\f*\16*\u0126\13*\3*\5*\u0129\n*"+
		"\3+\3+\3,\3,\5,\u012f\n,\3-\3-\5-\u0133\n-\3-\3-\3.\3.\7.\u0139\n.\f."+
		"\16.\u013c\13.\3.\5.\u013f\n.\3/\3/\3\60\3\60\5\60\u0145\n\60\3\61\3\61"+
		"\3\61\3\61\3\62\3\62\7\62\u014d\n\62\f\62\16\62\u0150\13\62\3\62\5\62"+
		"\u0153\n\62\3\63\3\63\3\64\3\64\5\64\u0159\n\64\3\65\3\65\5\65\u015d\n"+
		"\65\3\66\3\66\3\66\5\66\u0162\n\66\3\66\5\66\u0165\n\66\3\66\5\66\u0168"+
		"\n\66\3\66\3\66\3\66\5\66\u016d\n\66\3\66\5\66\u0170\n\66\3\66\3\66\3"+
		"\66\5\66\u0175\n\66\3\66\3\66\3\66\5\66\u017a\n\66\3\67\3\67\3\67\38\3"+
		"8\39\59\u0182\n9\39\39\3:\3:\3;\3;\3<\3<\3<\5<\u018d\n<\3=\3=\5=\u0191"+
		"\n=\3=\3=\3=\5=\u0196\n=\3=\3=\5=\u019a\n=\3>\3>\3>\3?\3?\3@\3@\3@\3@"+
		"\3@\3@\3@\3@\3@\5@\u01aa\n@\3A\3A\3A\3A\3A\3A\3A\3A\5A\u01b4\nA\3B\3B"+
		"\3C\3C\5C\u01ba\nC\3C\3C\3D\6D\u01bf\nD\rD\16D\u01c0\3E\3E\5E\u01c5\n"+
		"E\3F\3F\3F\3F\5F\u01cb\nF\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\5G\u01d8\n"+
		"G\3H\3H\3H\3H\3H\3H\3H\3I\3I\3J\3J\3J\3J\3J\3K\3K\7K\u01ea\nK\fK\16K\u01ed"+
		"\13K\3L\3L\3L\3L\3L\3L\5L\u01f5\nL\3M\3M\3M\3M\3M\3M\5M\u01fd\nM\3N\6"+
		"N\u0200\nN\rN\16N\u0201\3N\3N\3O\3O\3O\3O\7O\u020a\nO\fO\16O\u020d\13"+
		"O\3O\3O\3O\3O\3O\3P\3P\3P\3P\7P\u0218\nP\fP\16P\u021b\13P\3P\3P\3\u020b"+
		"\2Q\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36"+
		";\2=\2?\2A\2C\2E\2G\2I\2K\2M\2O\2Q\2S\2U\2W\2Y\2[\2]\2_\2a\2c\2e\2g\2"+
		"i\37k\2m\2o\2q\2s\2u\2w\2y\2{\2}\2\177 \u0081!\u0083\2\u0085\"\u0087\2"+
		"\u0089\2\u008b\2\u008d\2\u008f\2\u0091\2\u0093#\u0095$\u0097\2\u0099\2"+
		"\u009b%\u009d&\u009f\'\3\2\30\4\2NNnn\3\2\63;\4\2ZZzz\5\2\62;CHch\3\2"+
		"\629\4\2DDdd\3\2\62\63\4\2GGgg\4\2--//\6\2FFHHffhh\4\2RRrr\4\2))^^\4\2"+
		"$$^^\n\2$$))^^ddhhppttvv\3\2\62\65\6\2&&C\\aac|\4\2\2\u0081\ud802\udc01"+
		"\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aac|\5\2\13\f\16\17\"\"\4"+
		"\2\f\f\17\17\u022c\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2"+
		"\29\3\2\2\2\2i\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0085\3\2\2\2\2"+
		"\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f"+
		"\3\2\2\2\3\u00a1\3\2\2\2\5\u00a4\3\2\2\2\7\u00a7\3\2\2\2\t\u00a9\3\2\2"+
		"\2\13\u00ab\3\2\2\2\r\u00ad\3\2\2\2\17\u00af\3\2\2\2\21\u00b1\3\2\2\2"+
		"\23\u00b3\3\2\2\2\25\u00b5\3\2\2\2\27\u00b7\3\2\2\2\31\u00b9\3\2\2\2\33"+
		"\u00bc\3\2\2\2\35\u00bf\3\2\2\2\37\u00c2\3\2\2\2!\u00c5\3\2\2\2#\u00c7"+
		"\3\2\2\2%\u00c9\3\2\2\2\'\u00cb\3\2\2\2)\u00ce\3\2\2\2+\u00d1\3\2\2\2"+
		"-\u00d3\3\2\2\2/\u00d5\3\2\2\2\61\u00d7\3\2\2\2\63\u00d9\3\2\2\2\65\u00db"+
		"\3\2\2\2\67\u00dd\3\2\2\29\u00e3\3\2\2\2;\u00e5\3\2\2\2=\u00e9\3\2\2\2"+
		"?\u00ed\3\2\2\2A\u00f1\3\2\2\2C\u00f5\3\2\2\2E\u0101\3\2\2\2G\u0103\3"+
		"\2\2\2I\u010f\3\2\2\2K\u0111\3\2\2\2M\u0115\3\2\2\2O\u0118\3\2\2\2Q\u011c"+
		"\3\2\2\2S\u0120\3\2\2\2U\u012a\3\2\2\2W\u012e\3\2\2\2Y\u0130\3\2\2\2["+
		"\u0136\3\2\2\2]\u0140\3\2\2\2_\u0144\3\2\2\2a\u0146\3\2\2\2c\u014a\3\2"+
		"\2\2e\u0154\3\2\2\2g\u0158\3\2\2\2i\u015c\3\2\2\2k\u0179\3\2\2\2m\u017b"+
		"\3\2\2\2o\u017e\3\2\2\2q\u0181\3\2\2\2s\u0185\3\2\2\2u\u0187\3\2\2\2w"+
		"\u0189\3\2\2\2y\u0199\3\2\2\2{\u019b\3\2\2\2}\u019e\3\2\2\2\177\u01a9"+
		"\3\2\2\2\u0081\u01b3\3\2\2\2\u0083\u01b5\3\2\2\2\u0085\u01b7\3\2\2\2\u0087"+
		"\u01be\3\2\2\2\u0089\u01c4\3\2\2\2\u008b\u01ca\3\2\2\2\u008d\u01d7\3\2"+
		"\2\2\u008f\u01d9\3\2\2\2\u0091\u01e0\3\2\2\2\u0093\u01e2\3\2\2\2\u0095"+
		"\u01e7\3\2\2\2\u0097\u01f4\3\2\2\2\u0099\u01fc\3\2\2\2\u009b\u01ff\3\2"+
		"\2\2\u009d\u0205\3\2\2\2\u009f\u0213\3\2\2\2\u00a1\u00a2\7-\2\2\u00a2"+
		"\u00a3\7-\2\2\u00a3\4\3\2\2\2\u00a4\u00a5\7/\2\2\u00a5\u00a6\7/\2\2\u00a6"+
		"\6\3\2\2\2\u00a7\u00a8\7-\2\2\u00a8\b\3\2\2\2\u00a9\u00aa\7/\2\2\u00aa"+
		"\n\3\2\2\2\u00ab\u00ac\7\u0080\2\2\u00ac\f\3\2\2\2\u00ad\u00ae\7#\2\2"+
		"\u00ae\16\3\2\2\2\u00af\u00b0\7,\2\2\u00b0\20\3\2\2\2\u00b1\u00b2\7\61"+
		"\2\2\u00b2\22\3\2\2\2\u00b3\u00b4\7\'\2\2\u00b4\24\3\2\2\2\u00b5\u00b6"+
		"\7>\2\2\u00b6\26\3\2\2\2\u00b7\u00b8\7@\2\2\u00b8\30\3\2\2\2\u00b9\u00ba"+
		"\7>\2\2\u00ba\u00bb\7?\2\2\u00bb\32\3\2\2\2\u00bc\u00bd\7@\2\2\u00bd\u00be"+
		"\7?\2\2\u00be\34\3\2\2\2\u00bf\u00c0\7?\2\2\u00c0\u00c1\7?\2\2\u00c1\36"+
		"\3\2\2\2\u00c2\u00c3\7#\2\2\u00c3\u00c4\7?\2\2\u00c4 \3\2\2\2\u00c5\u00c6"+
		"\7(\2\2\u00c6\"\3\2\2\2\u00c7\u00c8\7`\2\2\u00c8$\3\2\2\2\u00c9\u00ca"+
		"\7~\2\2\u00ca&\3\2\2\2\u00cb\u00cc\7(\2\2\u00cc\u00cd\7(\2\2\u00cd(\3"+
		"\2\2\2\u00ce\u00cf\7~\2\2\u00cf\u00d0\7~\2\2\u00d0*\3\2\2\2\u00d1\u00d2"+
		"\7A\2\2\u00d2,\3\2\2\2\u00d3\u00d4\7<\2\2\u00d4.\3\2\2\2\u00d5\u00d6\7"+
		".\2\2\u00d6\60\3\2\2\2\u00d7\u00d8\7=\2\2\u00d8\62\3\2\2\2\u00d9\u00da"+
		"\7*\2\2\u00da\64\3\2\2\2\u00db\u00dc\7+\2\2\u00dc\66\3\2\2\2\u00dd\u00de"+
		"\7\60\2\2\u00de8\3\2\2\2\u00df\u00e4\5;\36\2\u00e0\u00e4\5=\37\2\u00e1"+
		"\u00e4\5? \2\u00e2\u00e4\5A!\2\u00e3\u00df\3\2\2\2\u00e3\u00e0\3\2\2\2"+
		"\u00e3\u00e1\3\2\2\2\u00e3\u00e2\3\2\2\2\u00e4:\3\2\2\2\u00e5\u00e7\5"+
		"E#\2\u00e6\u00e8\5C\"\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8"+
		"<\3\2\2\2\u00e9\u00eb\5Q)\2\u00ea\u00ec\5C\"\2\u00eb\u00ea\3\2\2\2\u00eb"+
		"\u00ec\3\2\2\2\u00ec>\3\2\2\2\u00ed\u00ef\5Y-\2\u00ee\u00f0\5C\"\2\u00ef"+
		"\u00ee\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0@\3\2\2\2\u00f1\u00f3\5a\61\2"+
		"\u00f2\u00f4\5C\"\2\u00f3\u00f2\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4B\3\2"+
		"\2\2\u00f5\u00f6\t\2\2\2\u00f6D\3\2\2\2\u00f7\u0102\7\62\2\2\u00f8\u00ff"+
		"\5K&\2\u00f9\u00fb\5G$\2\u00fa\u00f9\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb"+
		"\u0100\3\2\2\2\u00fc\u00fd\5O(\2\u00fd\u00fe\5G$\2\u00fe\u0100\3\2\2\2"+
		"\u00ff\u00fa\3\2\2\2\u00ff\u00fc\3\2\2\2\u0100\u0102\3\2\2\2\u0101\u00f7"+
		"\3\2\2\2\u0101\u00f8\3\2\2\2\u0102F\3\2\2\2\u0103\u010b\5I%\2\u0104\u0106"+
		"\5M\'\2\u0105\u0104\3\2\2\2\u0106\u0109\3\2\2\2\u0107\u0105\3\2\2\2\u0107"+
		"\u0108\3\2\2\2\u0108\u010a\3\2\2\2\u0109\u0107\3\2\2\2\u010a\u010c\5I"+
		"%\2\u010b\u0107\3\2\2\2\u010b\u010c\3\2\2\2\u010cH\3\2\2\2\u010d\u0110"+
		"\7\62\2\2\u010e\u0110\5K&\2\u010f\u010d\3\2\2\2\u010f\u010e\3\2\2\2\u0110"+
		"J\3\2\2\2\u0111\u0112\t\3\2\2\u0112L\3\2\2\2\u0113\u0116\5I%\2\u0114\u0116"+
		"\7a\2\2\u0115\u0113\3\2\2\2\u0115\u0114\3\2\2\2\u0116N\3\2\2\2\u0117\u0119"+
		"\7a\2\2\u0118\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u0118\3\2\2\2\u011a"+
		"\u011b\3\2\2\2\u011bP\3\2\2\2\u011c\u011d\7\62\2\2\u011d\u011e\t\4\2\2"+
		"\u011e\u011f\5S*\2\u011fR\3\2\2\2\u0120\u0128\5U+\2\u0121\u0123\5W,\2"+
		"\u0122\u0121\3\2\2\2\u0123\u0126\3\2\2\2\u0124\u0122\3\2\2\2\u0124\u0125"+
		"\3\2\2\2\u0125\u0127\3\2\2\2\u0126\u0124\3\2\2\2\u0127\u0129\5U+\2\u0128"+
		"\u0124\3\2\2\2\u0128\u0129\3\2\2\2\u0129T\3\2\2\2\u012a\u012b\t\5\2\2"+
		"\u012bV\3\2\2\2\u012c\u012f\5U+\2\u012d\u012f\7a\2\2\u012e\u012c\3\2\2"+
		"\2\u012e\u012d\3\2\2\2\u012fX\3\2\2\2\u0130\u0132\7\62\2\2\u0131\u0133"+
		"\5O(\2\u0132\u0131\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0134\3\2\2\2\u0134"+
		"\u0135\5[.\2\u0135Z\3\2\2\2\u0136\u013e\5]/\2\u0137\u0139\5_\60\2\u0138"+
		"\u0137\3\2\2\2\u0139\u013c\3\2\2\2\u013a\u0138\3\2\2\2\u013a\u013b\3\2"+
		"\2\2\u013b\u013d\3\2\2\2\u013c\u013a\3\2\2\2\u013d\u013f\5]/\2\u013e\u013a"+
		"\3\2\2\2\u013e\u013f\3\2\2\2\u013f\\\3\2\2\2\u0140\u0141\t\6\2\2\u0141"+
		"^\3\2\2\2\u0142\u0145\5]/\2\u0143\u0145\7a\2\2\u0144\u0142\3\2\2\2\u0144"+
		"\u0143\3\2\2\2\u0145`\3\2\2\2\u0146\u0147\7\62\2\2\u0147\u0148\t\7\2\2"+
		"\u0148\u0149\5c\62\2\u0149b\3\2\2\2\u014a\u0152\5e\63\2\u014b\u014d\5"+
		"g\64\2\u014c\u014b\3\2\2\2\u014d\u0150\3\2\2\2\u014e\u014c\3\2\2\2\u014e"+
		"\u014f\3\2\2\2\u014f\u0151\3\2\2\2\u0150\u014e\3\2\2\2\u0151\u0153\5e"+
		"\63\2\u0152\u014e\3\2\2\2\u0152\u0153\3\2\2\2\u0153d\3\2\2\2\u0154\u0155"+
		"\t\b\2\2\u0155f\3\2\2\2\u0156\u0159\5e\63\2\u0157\u0159\7a\2\2\u0158\u0156"+
		"\3\2\2\2\u0158\u0157\3\2\2\2\u0159h\3\2\2\2\u015a\u015d\5k\66\2\u015b"+
		"\u015d\5w<\2\u015c\u015a\3\2\2\2\u015c\u015b\3\2\2\2\u015dj\3\2\2\2\u015e"+
		"\u015f\5G$\2\u015f\u0161\7\60\2\2\u0160\u0162\5G$\2\u0161\u0160\3\2\2"+
		"\2\u0161\u0162\3\2\2\2\u0162\u0164\3\2\2\2\u0163\u0165\5m\67\2\u0164\u0163"+
		"\3\2\2\2\u0164\u0165\3\2\2\2\u0165\u0167\3\2\2\2\u0166\u0168\5u;\2\u0167"+
		"\u0166\3\2\2\2\u0167\u0168\3\2\2\2\u0168\u017a\3\2\2\2\u0169\u016a\7\60"+
		"\2\2\u016a\u016c\5G$\2\u016b\u016d\5m\67\2\u016c\u016b\3\2\2\2\u016c\u016d"+
		"\3\2\2\2\u016d\u016f\3\2\2\2\u016e\u0170\5u;\2\u016f\u016e\3\2\2\2\u016f"+
		"\u0170\3\2\2\2\u0170\u017a\3\2\2\2\u0171\u0172\5G$\2\u0172\u0174\5m\67"+
		"\2\u0173\u0175\5u;\2\u0174\u0173\3\2\2\2\u0174\u0175\3\2\2\2\u0175\u017a"+
		"\3\2\2\2\u0176\u0177\5G$\2\u0177\u0178\5u;\2\u0178\u017a\3\2\2\2\u0179"+
		"\u015e\3\2\2\2\u0179\u0169\3\2\2\2\u0179\u0171\3\2\2\2\u0179\u0176\3\2"+
		"\2\2\u017al\3\2\2\2\u017b\u017c\5o8\2\u017c\u017d\5q9\2\u017dn\3\2\2\2"+
		"\u017e\u017f\t\t\2\2\u017fp\3\2\2\2\u0180\u0182\5s:\2\u0181\u0180\3\2"+
		"\2\2\u0181\u0182\3\2\2\2\u0182\u0183\3\2\2\2\u0183\u0184\5G$\2\u0184r"+
		"\3\2\2\2\u0185\u0186\t\n\2\2\u0186t\3\2\2\2\u0187\u0188\t\13\2\2\u0188"+
		"v\3\2\2\2\u0189\u018a\5y=\2\u018a\u018c\5{>\2\u018b\u018d\5u;\2\u018c"+
		"\u018b\3\2\2\2\u018c\u018d\3\2\2\2\u018dx\3\2\2\2\u018e\u0190\5Q)\2\u018f"+
		"\u0191\7\60\2\2\u0190\u018f\3\2\2\2\u0190\u0191\3\2\2\2\u0191\u019a\3"+
		"\2\2\2\u0192\u0193\7\62\2\2\u0193\u0195\t\4\2\2\u0194\u0196\5S*\2\u0195"+
		"\u0194\3\2\2\2\u0195\u0196\3\2\2\2\u0196\u0197\3\2\2\2\u0197\u0198\7\60"+
		"\2\2\u0198\u019a\5S*\2\u0199\u018e\3\2\2\2\u0199\u0192\3\2\2\2\u019az"+
		"\3\2\2\2\u019b\u019c\5}?\2\u019c\u019d\5q9\2\u019d|\3\2\2\2\u019e\u019f"+
		"\t\f\2\2\u019f~\3\2\2\2\u01a0\u01a1\7v\2\2\u01a1\u01a2\7t\2\2\u01a2\u01a3"+
		"\7w\2\2\u01a3\u01aa\7g\2\2\u01a4\u01a5\7h\2\2\u01a5\u01a6\7c\2\2\u01a6"+
		"\u01a7\7n\2\2\u01a7\u01a8\7u\2\2\u01a8\u01aa\7g\2\2\u01a9\u01a0\3\2\2"+
		"\2\u01a9\u01a4\3\2\2\2\u01aa\u0080\3\2\2\2\u01ab\u01ac\7)\2\2\u01ac\u01ad"+
		"\5\u0083B\2\u01ad\u01ae\7)\2\2\u01ae\u01b4\3\2\2\2\u01af\u01b0\7)\2\2"+
		"\u01b0\u01b1\5\u008bF\2\u01b1\u01b2\7)\2\2\u01b2\u01b4\3\2\2\2\u01b3\u01ab"+
		"\3\2\2\2\u01b3\u01af\3\2\2\2\u01b4\u0082\3\2\2\2\u01b5\u01b6\n\r\2\2\u01b6"+
		"\u0084\3\2\2\2\u01b7\u01b9\7$\2\2\u01b8\u01ba\5\u0087D\2\u01b9\u01b8\3"+
		"\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb\u01bc\7$\2\2\u01bc"+
		"\u0086\3\2\2\2\u01bd\u01bf\5\u0089E\2\u01be\u01bd\3\2\2\2\u01bf\u01c0"+
		"\3\2\2\2\u01c0\u01be\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1\u0088\3\2\2\2\u01c2"+
		"\u01c5\n\16\2\2\u01c3\u01c5\5\u008bF\2\u01c4\u01c2\3\2\2\2\u01c4\u01c3"+
		"\3\2\2\2\u01c5\u008a\3\2\2\2\u01c6\u01c7\7^\2\2\u01c7\u01cb\t\17\2\2\u01c8"+
		"\u01cb\5\u008dG\2\u01c9\u01cb\5\u008fH\2\u01ca\u01c6\3\2\2\2\u01ca\u01c8"+
		"\3\2\2\2\u01ca\u01c9\3\2\2\2\u01cb\u008c\3\2\2\2\u01cc\u01cd\7^\2\2\u01cd"+
		"\u01d8\5]/\2\u01ce\u01cf\7^\2\2\u01cf\u01d0\5]/\2\u01d0\u01d1\5]/\2\u01d1"+
		"\u01d8\3\2\2\2\u01d2\u01d3\7^\2\2\u01d3\u01d4\5\u0091I\2\u01d4\u01d5\5"+
		"]/\2\u01d5\u01d6\5]/\2\u01d6\u01d8\3\2\2\2\u01d7\u01cc\3\2\2\2\u01d7\u01ce"+
		"\3\2\2\2\u01d7\u01d2\3\2\2\2\u01d8\u008e\3\2\2\2\u01d9\u01da\7^\2\2\u01da"+
		"\u01db\7w\2\2\u01db\u01dc\5U+\2\u01dc\u01dd\5U+\2\u01dd\u01de\5U+\2\u01de"+
		"\u01df\5U+\2\u01df\u0090\3\2\2\2\u01e0\u01e1\t\20\2\2\u01e1\u0092\3\2"+
		"\2\2\u01e2\u01e3\7p\2\2\u01e3\u01e4\7w\2\2\u01e4\u01e5\7n\2\2\u01e5\u01e6"+
		"\7n\2\2\u01e6\u0094\3\2\2\2\u01e7\u01eb\5\u0097L\2\u01e8\u01ea\5\u0099"+
		"M\2\u01e9\u01e8\3\2\2\2\u01ea\u01ed\3\2\2\2\u01eb\u01e9\3\2\2\2\u01eb"+
		"\u01ec\3\2\2\2\u01ec\u0096\3\2\2\2\u01ed\u01eb\3\2\2\2\u01ee\u01f5\t\21"+
		"\2\2\u01ef\u01f0\n\22\2\2\u01f0\u01f5\6L\2\2\u01f1\u01f2\t\23\2\2\u01f2"+
		"\u01f3\t\24\2\2\u01f3\u01f5\6L\3\2\u01f4\u01ee\3\2\2\2\u01f4\u01ef\3\2"+
		"\2\2\u01f4\u01f1\3\2\2\2\u01f5\u0098\3\2\2\2\u01f6\u01fd\t\25\2\2\u01f7"+
		"\u01f8\n\22\2\2\u01f8\u01fd\6M\4\2\u01f9\u01fa\t\23\2\2\u01fa\u01fb\t"+
		"\24\2\2\u01fb\u01fd\6M\5\2\u01fc\u01f6\3\2\2\2\u01fc\u01f7\3\2\2\2\u01fc"+
		"\u01f9\3\2\2\2\u01fd\u009a\3\2\2\2\u01fe\u0200\t\26\2\2\u01ff\u01fe\3"+
		"\2\2\2\u0200\u0201\3\2\2\2\u0201\u01ff\3\2\2\2\u0201\u0202\3\2\2\2\u0202"+
		"\u0203\3\2\2\2\u0203\u0204\bN\2\2\u0204\u009c\3\2\2\2\u0205\u0206\7\61"+
		"\2\2\u0206\u0207\7,\2\2\u0207\u020b\3\2\2\2\u0208\u020a\13\2\2\2\u0209"+
		"\u0208\3\2\2\2\u020a\u020d\3\2\2\2\u020b\u020c\3\2\2\2\u020b\u0209\3\2"+
		"\2\2\u020c\u020e\3\2\2\2\u020d\u020b\3\2\2\2\u020e\u020f\7,\2\2\u020f"+
		"\u0210\7\61\2\2\u0210\u0211\3\2\2\2\u0211\u0212\bO\2\2\u0212\u009e\3\2"+
		"\2\2\u0213\u0214\7\61\2\2\u0214\u0215\7\61\2\2\u0215\u0219\3\2\2\2\u0216"+
		"\u0218\n\27\2\2\u0217\u0216\3\2\2\2\u0218\u021b\3\2\2\2\u0219\u0217\3"+
		"\2\2\2\u0219\u021a\3\2\2\2\u021a\u021c\3\2\2\2\u021b\u0219\3\2\2\2\u021c"+
		"\u021d\bP\2\2\u021d\u00a0\3\2\2\2\64\2\u00e3\u00e7\u00eb\u00ef\u00f3\u00fa"+
		"\u00ff\u0101\u0107\u010b\u010f\u0115\u011a\u0124\u0128\u012e\u0132\u013a"+
		"\u013e\u0144\u014e\u0152\u0158\u015c\u0161\u0164\u0167\u016c\u016f\u0174"+
		"\u0179\u0181\u018c\u0190\u0195\u0199\u01a9\u01b3\u01b9\u01c0\u01c4\u01ca"+
		"\u01d7\u01eb\u01f4\u01fc\u0201\u020b\u0219\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}