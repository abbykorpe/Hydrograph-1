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
		"'||'", "'?'", "':'", "','", "'new'", "'('", "')'", "'.'", null, null, 
		null, null, null, "'null'"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\'\u0220\b\1\4\2\t"+
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
		"\3\30\3\30\3\31\3\31\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35"+
		"\3\35\3\35\5\35\u00e6\n\35\3\36\3\36\5\36\u00ea\n\36\3\37\3\37\5\37\u00ee"+
		"\n\37\3 \3 \5 \u00f2\n \3!\3!\5!\u00f6\n!\3\"\3\"\3#\3#\3#\5#\u00fd\n"+
		"#\3#\3#\3#\5#\u0102\n#\5#\u0104\n#\3$\3$\7$\u0108\n$\f$\16$\u010b\13$"+
		"\3$\5$\u010e\n$\3%\3%\5%\u0112\n%\3&\3&\3\'\3\'\5\'\u0118\n\'\3(\6(\u011b"+
		"\n(\r(\16(\u011c\3)\3)\3)\3)\3*\3*\7*\u0125\n*\f*\16*\u0128\13*\3*\5*"+
		"\u012b\n*\3+\3+\3,\3,\5,\u0131\n,\3-\3-\5-\u0135\n-\3-\3-\3.\3.\7.\u013b"+
		"\n.\f.\16.\u013e\13.\3.\5.\u0141\n.\3/\3/\3\60\3\60\5\60\u0147\n\60\3"+
		"\61\3\61\3\61\3\61\3\62\3\62\7\62\u014f\n\62\f\62\16\62\u0152\13\62\3"+
		"\62\5\62\u0155\n\62\3\63\3\63\3\64\3\64\5\64\u015b\n\64\3\65\3\65\5\65"+
		"\u015f\n\65\3\66\3\66\3\66\5\66\u0164\n\66\3\66\5\66\u0167\n\66\3\66\5"+
		"\66\u016a\n\66\3\66\3\66\3\66\5\66\u016f\n\66\3\66\5\66\u0172\n\66\3\66"+
		"\3\66\3\66\5\66\u0177\n\66\3\66\3\66\3\66\5\66\u017c\n\66\3\67\3\67\3"+
		"\67\38\38\39\59\u0184\n9\39\39\3:\3:\3;\3;\3<\3<\3<\5<\u018f\n<\3=\3="+
		"\5=\u0193\n=\3=\3=\3=\5=\u0198\n=\3=\3=\5=\u019c\n=\3>\3>\3>\3?\3?\3@"+
		"\3@\3@\3@\3@\3@\3@\3@\3@\5@\u01ac\n@\3A\3A\3A\3A\3A\3A\3A\3A\5A\u01b6"+
		"\nA\3B\3B\3C\3C\5C\u01bc\nC\3C\3C\3D\6D\u01c1\nD\rD\16D\u01c2\3E\3E\5"+
		"E\u01c7\nE\3F\3F\3F\3F\5F\u01cd\nF\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\5"+
		"G\u01da\nG\3H\3H\3H\3H\3H\3H\3H\3I\3I\3J\3J\3J\3J\3J\3K\3K\7K\u01ec\n"+
		"K\fK\16K\u01ef\13K\3L\3L\3L\3L\3L\3L\5L\u01f7\nL\3M\3M\3M\3M\3M\3M\5M"+
		"\u01ff\nM\3N\6N\u0202\nN\rN\16N\u0203\3N\3N\3O\3O\3O\3O\7O\u020c\nO\f"+
		"O\16O\u020f\13O\3O\3O\3O\3O\3O\3P\3P\3P\3P\7P\u021a\nP\fP\16P\u021d\13"+
		"P\3P\3P\3\u020d\2Q\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r"+
		"\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33"+
		"\65\34\67\359\36;\2=\2?\2A\2C\2E\2G\2I\2K\2M\2O\2Q\2S\2U\2W\2Y\2[\2]\2"+
		"_\2a\2c\2e\2g\2i\37k\2m\2o\2q\2s\2u\2w\2y\2{\2}\2\177 \u0081!\u0083\2"+
		"\u0085\"\u0087\2\u0089\2\u008b\2\u008d\2\u008f\2\u0091\2\u0093#\u0095"+
		"$\u0097\2\u0099\2\u009b%\u009d&\u009f\'\3\2\30\4\2NNnn\3\2\63;\4\2ZZz"+
		"z\5\2\62;CHch\3\2\629\4\2DDdd\3\2\62\63\4\2GGgg\4\2--//\6\2FFHHffhh\4"+
		"\2RRrr\4\2))^^\4\2$$^^\n\2$$))^^ddhhppttvv\3\2\62\65\6\2&&C\\aac|\4\2"+
		"\2\u0081\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aac|"+
		"\5\2\13\f\16\17\"\"\4\2\f\f\17\17\u022e\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2"+
		"\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2i\3\2\2\2\2\177\3\2\2\2\2\u0081\3"+
		"\2\2\2\2\u0085\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u009b\3\2\2\2"+
		"\2\u009d\3\2\2\2\2\u009f\3\2\2\2\3\u00a1\3\2\2\2\5\u00a4\3\2\2\2\7\u00a7"+
		"\3\2\2\2\t\u00a9\3\2\2\2\13\u00ab\3\2\2\2\r\u00ad\3\2\2\2\17\u00af\3\2"+
		"\2\2\21\u00b1\3\2\2\2\23\u00b3\3\2\2\2\25\u00b5\3\2\2\2\27\u00b7\3\2\2"+
		"\2\31\u00b9\3\2\2\2\33\u00bc\3\2\2\2\35\u00bf\3\2\2\2\37\u00c2\3\2\2\2"+
		"!\u00c5\3\2\2\2#\u00c7\3\2\2\2%\u00c9\3\2\2\2\'\u00cb\3\2\2\2)\u00ce\3"+
		"\2\2\2+\u00d1\3\2\2\2-\u00d3\3\2\2\2/\u00d5\3\2\2\2\61\u00d7\3\2\2\2\63"+
		"\u00db\3\2\2\2\65\u00dd\3\2\2\2\67\u00df\3\2\2\29\u00e5\3\2\2\2;\u00e7"+
		"\3\2\2\2=\u00eb\3\2\2\2?\u00ef\3\2\2\2A\u00f3\3\2\2\2C\u00f7\3\2\2\2E"+
		"\u0103\3\2\2\2G\u0105\3\2\2\2I\u0111\3\2\2\2K\u0113\3\2\2\2M\u0117\3\2"+
		"\2\2O\u011a\3\2\2\2Q\u011e\3\2\2\2S\u0122\3\2\2\2U\u012c\3\2\2\2W\u0130"+
		"\3\2\2\2Y\u0132\3\2\2\2[\u0138\3\2\2\2]\u0142\3\2\2\2_\u0146\3\2\2\2a"+
		"\u0148\3\2\2\2c\u014c\3\2\2\2e\u0156\3\2\2\2g\u015a\3\2\2\2i\u015e\3\2"+
		"\2\2k\u017b\3\2\2\2m\u017d\3\2\2\2o\u0180\3\2\2\2q\u0183\3\2\2\2s\u0187"+
		"\3\2\2\2u\u0189\3\2\2\2w\u018b\3\2\2\2y\u019b\3\2\2\2{\u019d\3\2\2\2}"+
		"\u01a0\3\2\2\2\177\u01ab\3\2\2\2\u0081\u01b5\3\2\2\2\u0083\u01b7\3\2\2"+
		"\2\u0085\u01b9\3\2\2\2\u0087\u01c0\3\2\2\2\u0089\u01c6\3\2\2\2\u008b\u01cc"+
		"\3\2\2\2\u008d\u01d9\3\2\2\2\u008f\u01db\3\2\2\2\u0091\u01e2\3\2\2\2\u0093"+
		"\u01e4\3\2\2\2\u0095\u01e9\3\2\2\2\u0097\u01f6\3\2\2\2\u0099\u01fe\3\2"+
		"\2\2\u009b\u0201\3\2\2\2\u009d\u0207\3\2\2\2\u009f\u0215\3\2\2\2\u00a1"+
		"\u00a2\7-\2\2\u00a2\u00a3\7-\2\2\u00a3\4\3\2\2\2\u00a4\u00a5\7/\2\2\u00a5"+
		"\u00a6\7/\2\2\u00a6\6\3\2\2\2\u00a7\u00a8\7-\2\2\u00a8\b\3\2\2\2\u00a9"+
		"\u00aa\7/\2\2\u00aa\n\3\2\2\2\u00ab\u00ac\7\u0080\2\2\u00ac\f\3\2\2\2"+
		"\u00ad\u00ae\7#\2\2\u00ae\16\3\2\2\2\u00af\u00b0\7,\2\2\u00b0\20\3\2\2"+
		"\2\u00b1\u00b2\7\61\2\2\u00b2\22\3\2\2\2\u00b3\u00b4\7\'\2\2\u00b4\24"+
		"\3\2\2\2\u00b5\u00b6\7>\2\2\u00b6\26\3\2\2\2\u00b7\u00b8\7@\2\2\u00b8"+
		"\30\3\2\2\2\u00b9\u00ba\7>\2\2\u00ba\u00bb\7?\2\2\u00bb\32\3\2\2\2\u00bc"+
		"\u00bd\7@\2\2\u00bd\u00be\7?\2\2\u00be\34\3\2\2\2\u00bf\u00c0\7?\2\2\u00c0"+
		"\u00c1\7?\2\2\u00c1\36\3\2\2\2\u00c2\u00c3\7#\2\2\u00c3\u00c4\7?\2\2\u00c4"+
		" \3\2\2\2\u00c5\u00c6\7(\2\2\u00c6\"\3\2\2\2\u00c7\u00c8\7`\2\2\u00c8"+
		"$\3\2\2\2\u00c9\u00ca\7~\2\2\u00ca&\3\2\2\2\u00cb\u00cc\7(\2\2\u00cc\u00cd"+
		"\7(\2\2\u00cd(\3\2\2\2\u00ce\u00cf\7~\2\2\u00cf\u00d0\7~\2\2\u00d0*\3"+
		"\2\2\2\u00d1\u00d2\7A\2\2\u00d2,\3\2\2\2\u00d3\u00d4\7<\2\2\u00d4.\3\2"+
		"\2\2\u00d5\u00d6\7.\2\2\u00d6\60\3\2\2\2\u00d7\u00d8\7p\2\2\u00d8\u00d9"+
		"\7g\2\2\u00d9\u00da\7y\2\2\u00da\62\3\2\2\2\u00db\u00dc\7*\2\2\u00dc\64"+
		"\3\2\2\2\u00dd\u00de\7+\2\2\u00de\66\3\2\2\2\u00df\u00e0\7\60\2\2\u00e0"+
		"8\3\2\2\2\u00e1\u00e6\5;\36\2\u00e2\u00e6\5=\37\2\u00e3\u00e6\5? \2\u00e4"+
		"\u00e6\5A!\2\u00e5\u00e1\3\2\2\2\u00e5\u00e2\3\2\2\2\u00e5\u00e3\3\2\2"+
		"\2\u00e5\u00e4\3\2\2\2\u00e6:\3\2\2\2\u00e7\u00e9\5E#\2\u00e8\u00ea\5"+
		"C\"\2\u00e9\u00e8\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea<\3\2\2\2\u00eb\u00ed"+
		"\5Q)\2\u00ec\u00ee\5C\"\2\u00ed\u00ec\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee"+
		">\3\2\2\2\u00ef\u00f1\5Y-\2\u00f0\u00f2\5C\"\2\u00f1\u00f0\3\2\2\2\u00f1"+
		"\u00f2\3\2\2\2\u00f2@\3\2\2\2\u00f3\u00f5\5a\61\2\u00f4\u00f6\5C\"\2\u00f5"+
		"\u00f4\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6B\3\2\2\2\u00f7\u00f8\t\2\2\2"+
		"\u00f8D\3\2\2\2\u00f9\u0104\7\62\2\2\u00fa\u0101\5K&\2\u00fb\u00fd\5G"+
		"$\2\u00fc\u00fb\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u0102\3\2\2\2\u00fe"+
		"\u00ff\5O(\2\u00ff\u0100\5G$\2\u0100\u0102\3\2\2\2\u0101\u00fc\3\2\2\2"+
		"\u0101\u00fe\3\2\2\2\u0102\u0104\3\2\2\2\u0103\u00f9\3\2\2\2\u0103\u00fa"+
		"\3\2\2\2\u0104F\3\2\2\2\u0105\u010d\5I%\2\u0106\u0108\5M\'\2\u0107\u0106"+
		"\3\2\2\2\u0108\u010b\3\2\2\2\u0109\u0107\3\2\2\2\u0109\u010a\3\2\2\2\u010a"+
		"\u010c\3\2\2\2\u010b\u0109\3\2\2\2\u010c\u010e\5I%\2\u010d\u0109\3\2\2"+
		"\2\u010d\u010e\3\2\2\2\u010eH\3\2\2\2\u010f\u0112\7\62\2\2\u0110\u0112"+
		"\5K&\2\u0111\u010f\3\2\2\2\u0111\u0110\3\2\2\2\u0112J\3\2\2\2\u0113\u0114"+
		"\t\3\2\2\u0114L\3\2\2\2\u0115\u0118\5I%\2\u0116\u0118\7a\2\2\u0117\u0115"+
		"\3\2\2\2\u0117\u0116\3\2\2\2\u0118N\3\2\2\2\u0119\u011b\7a\2\2\u011a\u0119"+
		"\3\2\2\2\u011b\u011c\3\2\2\2\u011c\u011a\3\2\2\2\u011c\u011d\3\2\2\2\u011d"+
		"P\3\2\2\2\u011e\u011f\7\62\2\2\u011f\u0120\t\4\2\2\u0120\u0121\5S*\2\u0121"+
		"R\3\2\2\2\u0122\u012a\5U+\2\u0123\u0125\5W,\2\u0124\u0123\3\2\2\2\u0125"+
		"\u0128\3\2\2\2\u0126\u0124\3\2\2\2\u0126\u0127\3\2\2\2\u0127\u0129\3\2"+
		"\2\2\u0128\u0126\3\2\2\2\u0129\u012b\5U+\2\u012a\u0126\3\2\2\2\u012a\u012b"+
		"\3\2\2\2\u012bT\3\2\2\2\u012c\u012d\t\5\2\2\u012dV\3\2\2\2\u012e\u0131"+
		"\5U+\2\u012f\u0131\7a\2\2\u0130\u012e\3\2\2\2\u0130\u012f\3\2\2\2\u0131"+
		"X\3\2\2\2\u0132\u0134\7\62\2\2\u0133\u0135\5O(\2\u0134\u0133\3\2\2\2\u0134"+
		"\u0135\3\2\2\2\u0135\u0136\3\2\2\2\u0136\u0137\5[.\2\u0137Z\3\2\2\2\u0138"+
		"\u0140\5]/\2\u0139\u013b\5_\60\2\u013a\u0139\3\2\2\2\u013b\u013e\3\2\2"+
		"\2\u013c\u013a\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013f\3\2\2\2\u013e\u013c"+
		"\3\2\2\2\u013f\u0141\5]/\2\u0140\u013c\3\2\2\2\u0140\u0141\3\2\2\2\u0141"+
		"\\\3\2\2\2\u0142\u0143\t\6\2\2\u0143^\3\2\2\2\u0144\u0147\5]/\2\u0145"+
		"\u0147\7a\2\2\u0146\u0144\3\2\2\2\u0146\u0145\3\2\2\2\u0147`\3\2\2\2\u0148"+
		"\u0149\7\62\2\2\u0149\u014a\t\7\2\2\u014a\u014b\5c\62\2\u014bb\3\2\2\2"+
		"\u014c\u0154\5e\63\2\u014d\u014f\5g\64\2\u014e\u014d\3\2\2\2\u014f\u0152"+
		"\3\2\2\2\u0150\u014e\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0153\3\2\2\2\u0152"+
		"\u0150\3\2\2\2\u0153\u0155\5e\63\2\u0154\u0150\3\2\2\2\u0154\u0155\3\2"+
		"\2\2\u0155d\3\2\2\2\u0156\u0157\t\b\2\2\u0157f\3\2\2\2\u0158\u015b\5e"+
		"\63\2\u0159\u015b\7a\2\2\u015a\u0158\3\2\2\2\u015a\u0159\3\2\2\2\u015b"+
		"h\3\2\2\2\u015c\u015f\5k\66\2\u015d\u015f\5w<\2\u015e\u015c\3\2\2\2\u015e"+
		"\u015d\3\2\2\2\u015fj\3\2\2\2\u0160\u0161\5G$\2\u0161\u0163\7\60\2\2\u0162"+
		"\u0164\5G$\2\u0163\u0162\3\2\2\2\u0163\u0164\3\2\2\2\u0164\u0166\3\2\2"+
		"\2\u0165\u0167\5m\67\2\u0166\u0165\3\2\2\2\u0166\u0167\3\2\2\2\u0167\u0169"+
		"\3\2\2\2\u0168\u016a\5u;\2\u0169\u0168\3\2\2\2\u0169\u016a\3\2\2\2\u016a"+
		"\u017c\3\2\2\2\u016b\u016c\7\60\2\2\u016c\u016e\5G$\2\u016d\u016f\5m\67"+
		"\2\u016e\u016d\3\2\2\2\u016e\u016f\3\2\2\2\u016f\u0171\3\2\2\2\u0170\u0172"+
		"\5u;\2\u0171\u0170\3\2\2\2\u0171\u0172\3\2\2\2\u0172\u017c\3\2\2\2\u0173"+
		"\u0174\5G$\2\u0174\u0176\5m\67\2\u0175\u0177\5u;\2\u0176\u0175\3\2\2\2"+
		"\u0176\u0177\3\2\2\2\u0177\u017c\3\2\2\2\u0178\u0179\5G$\2\u0179\u017a"+
		"\5u;\2\u017a\u017c\3\2\2\2\u017b\u0160\3\2\2\2\u017b\u016b\3\2\2\2\u017b"+
		"\u0173\3\2\2\2\u017b\u0178\3\2\2\2\u017cl\3\2\2\2\u017d\u017e\5o8\2\u017e"+
		"\u017f\5q9\2\u017fn\3\2\2\2\u0180\u0181\t\t\2\2\u0181p\3\2\2\2\u0182\u0184"+
		"\5s:\2\u0183\u0182\3\2\2\2\u0183\u0184\3\2\2\2\u0184\u0185\3\2\2\2\u0185"+
		"\u0186\5G$\2\u0186r\3\2\2\2\u0187\u0188\t\n\2\2\u0188t\3\2\2\2\u0189\u018a"+
		"\t\13\2\2\u018av\3\2\2\2\u018b\u018c\5y=\2\u018c\u018e\5{>\2\u018d\u018f"+
		"\5u;\2\u018e\u018d\3\2\2\2\u018e\u018f\3\2\2\2\u018fx\3\2\2\2\u0190\u0192"+
		"\5Q)\2\u0191\u0193\7\60\2\2\u0192\u0191\3\2\2\2\u0192\u0193\3\2\2\2\u0193"+
		"\u019c\3\2\2\2\u0194\u0195\7\62\2\2\u0195\u0197\t\4\2\2\u0196\u0198\5"+
		"S*\2\u0197\u0196\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u0199\3\2\2\2\u0199"+
		"\u019a\7\60\2\2\u019a\u019c\5S*\2\u019b\u0190\3\2\2\2\u019b\u0194\3\2"+
		"\2\2\u019cz\3\2\2\2\u019d\u019e\5}?\2\u019e\u019f\5q9\2\u019f|\3\2\2\2"+
		"\u01a0\u01a1\t\f\2\2\u01a1~\3\2\2\2\u01a2\u01a3\7v\2\2\u01a3\u01a4\7t"+
		"\2\2\u01a4\u01a5\7w\2\2\u01a5\u01ac\7g\2\2\u01a6\u01a7\7h\2\2\u01a7\u01a8"+
		"\7c\2\2\u01a8\u01a9\7n\2\2\u01a9\u01aa\7u\2\2\u01aa\u01ac\7g\2\2\u01ab"+
		"\u01a2\3\2\2\2\u01ab\u01a6\3\2\2\2\u01ac\u0080\3\2\2\2\u01ad\u01ae\7)"+
		"\2\2\u01ae\u01af\5\u0083B\2\u01af\u01b0\7)\2\2\u01b0\u01b6\3\2\2\2\u01b1"+
		"\u01b2\7)\2\2\u01b2\u01b3\5\u008bF\2\u01b3\u01b4\7)\2\2\u01b4\u01b6\3"+
		"\2\2\2\u01b5\u01ad\3\2\2\2\u01b5\u01b1\3\2\2\2\u01b6\u0082\3\2\2\2\u01b7"+
		"\u01b8\n\r\2\2\u01b8\u0084\3\2\2\2\u01b9\u01bb\7$\2\2\u01ba\u01bc\5\u0087"+
		"D\2\u01bb\u01ba\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd"+
		"\u01be\7$\2\2\u01be\u0086\3\2\2\2\u01bf\u01c1\5\u0089E\2\u01c0\u01bf\3"+
		"\2\2\2\u01c1\u01c2\3\2\2\2\u01c2\u01c0\3\2\2\2\u01c2\u01c3\3\2\2\2\u01c3"+
		"\u0088\3\2\2\2\u01c4\u01c7\n\16\2\2\u01c5\u01c7\5\u008bF\2\u01c6\u01c4"+
		"\3\2\2\2\u01c6\u01c5\3\2\2\2\u01c7\u008a\3\2\2\2\u01c8\u01c9\7^\2\2\u01c9"+
		"\u01cd\t\17\2\2\u01ca\u01cd\5\u008dG\2\u01cb\u01cd\5\u008fH\2\u01cc\u01c8"+
		"\3\2\2\2\u01cc\u01ca\3\2\2\2\u01cc\u01cb\3\2\2\2\u01cd\u008c\3\2\2\2\u01ce"+
		"\u01cf\7^\2\2\u01cf\u01da\5]/\2\u01d0\u01d1\7^\2\2\u01d1\u01d2\5]/\2\u01d2"+
		"\u01d3\5]/\2\u01d3\u01da\3\2\2\2\u01d4\u01d5\7^\2\2\u01d5\u01d6\5\u0091"+
		"I\2\u01d6\u01d7\5]/\2\u01d7\u01d8\5]/\2\u01d8\u01da\3\2\2\2\u01d9\u01ce"+
		"\3\2\2\2\u01d9\u01d0\3\2\2\2\u01d9\u01d4\3\2\2\2\u01da\u008e\3\2\2\2\u01db"+
		"\u01dc\7^\2\2\u01dc\u01dd\7w\2\2\u01dd\u01de\5U+\2\u01de\u01df\5U+\2\u01df"+
		"\u01e0\5U+\2\u01e0\u01e1\5U+\2\u01e1\u0090\3\2\2\2\u01e2\u01e3\t\20\2"+
		"\2\u01e3\u0092\3\2\2\2\u01e4\u01e5\7p\2\2\u01e5\u01e6\7w\2\2\u01e6\u01e7"+
		"\7n\2\2\u01e7\u01e8\7n\2\2\u01e8\u0094\3\2\2\2\u01e9\u01ed\5\u0097L\2"+
		"\u01ea\u01ec\5\u0099M\2\u01eb\u01ea\3\2\2\2\u01ec\u01ef\3\2\2\2\u01ed"+
		"\u01eb\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee\u0096\3\2\2\2\u01ef\u01ed\3\2"+
		"\2\2\u01f0\u01f7\t\21\2\2\u01f1\u01f2\n\22\2\2\u01f2\u01f7\6L\2\2\u01f3"+
		"\u01f4\t\23\2\2\u01f4\u01f5\t\24\2\2\u01f5\u01f7\6L\3\2\u01f6\u01f0\3"+
		"\2\2\2\u01f6\u01f1\3\2\2\2\u01f6\u01f3\3\2\2\2\u01f7\u0098\3\2\2\2\u01f8"+
		"\u01ff\t\25\2\2\u01f9\u01fa\n\22\2\2\u01fa\u01ff\6M\4\2\u01fb\u01fc\t"+
		"\23\2\2\u01fc\u01fd\t\24\2\2\u01fd\u01ff\6M\5\2\u01fe\u01f8\3\2\2\2\u01fe"+
		"\u01f9\3\2\2\2\u01fe\u01fb\3\2\2\2\u01ff\u009a\3\2\2\2\u0200\u0202\t\26"+
		"\2\2\u0201\u0200\3\2\2\2\u0202\u0203\3\2\2\2\u0203\u0201\3\2\2\2\u0203"+
		"\u0204\3\2\2\2\u0204\u0205\3\2\2\2\u0205\u0206\bN\2\2\u0206\u009c\3\2"+
		"\2\2\u0207\u0208\7\61\2\2\u0208\u0209\7,\2\2\u0209\u020d\3\2\2\2\u020a"+
		"\u020c\13\2\2\2\u020b\u020a\3\2\2\2\u020c\u020f\3\2\2\2\u020d\u020e\3"+
		"\2\2\2\u020d\u020b\3\2\2\2\u020e\u0210\3\2\2\2\u020f\u020d\3\2\2\2\u0210"+
		"\u0211\7,\2\2\u0211\u0212\7\61\2\2\u0212\u0213\3\2\2\2\u0213\u0214\bO"+
		"\2\2\u0214\u009e\3\2\2\2\u0215\u0216\7\61\2\2\u0216\u0217\7\61\2\2\u0217"+
		"\u021b\3\2\2\2\u0218\u021a\n\27\2\2\u0219\u0218\3\2\2\2\u021a\u021d\3"+
		"\2\2\2\u021b\u0219\3\2\2\2\u021b\u021c\3\2\2\2\u021c\u021e\3\2\2\2\u021d"+
		"\u021b\3\2\2\2\u021e\u021f\bP\2\2\u021f\u00a0\3\2\2\2\64\2\u00e5\u00e9"+
		"\u00ed\u00f1\u00f5\u00fc\u0101\u0103\u0109\u010d\u0111\u0117\u011c\u0126"+
		"\u012a\u0130\u0134\u013c\u0140\u0146\u0150\u0154\u015a\u015e\u0163\u0166"+
		"\u0169\u016e\u0171\u0176\u017b\u0183\u018e\u0192\u0197\u019b\u01ab\u01b5"+
		"\u01bb\u01c2\u01c6\u01cc\u01d9\u01ed\u01f6\u01fe\u0203\u020d\u021b\3\b"+
		"\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}