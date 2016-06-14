package hydrograph.engine.expression.antlr;// Generated from C:/Users/gurdits/git/ExpressionEditor\ExprssionEditor.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExprssionEditorLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, LPAREN=28, RPAREN=29, IntegerLiteral=30, 
		FloatingPointLiteral=31, BooleanLiteral=32, CharacterLiteral=33, StringLiteral=34, 
		NullLiteral=35, Identifier=36, WS=37, COMMENT=38, LINE_COMMENT=39;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
		"T__25", "T__26", "LPAREN", "RPAREN", "IntegerLiteral", "DecimalIntegerLiteral", 
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
		null, "'.'", "'++'", "'--'", "'+'", "'-'", "'~'", "'!'", "'*'", "'/'", 
		"'%'", "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&'", "'^'", "'|'", 
		"'&&'", "'||'", "'?'", "':'", "','", "'if'", "'else'", "';'", "'('", "')'", 
		null, null, null, null, null, "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, "LPAREN", "RPAREN", "IntegerLiteral", "FloatingPointLiteral", 
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


	public ExprssionEditorLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ExprssionEditor.g4"; }

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
		case 76:
			return JavaLetter_sempred((RuleContext)_localctx, predIndex);
		case 77:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2)\u022a\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\3\2\3\2\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3"+
		"\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20"+
		"\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\33\3\33"+
		"\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3\37\3\37\5\37\u00f0\n\37"+
		"\3 \3 \5 \u00f4\n \3!\3!\5!\u00f8\n!\3\"\3\"\5\"\u00fc\n\"\3#\3#\5#\u0100"+
		"\n#\3$\3$\3%\3%\3%\5%\u0107\n%\3%\3%\3%\5%\u010c\n%\5%\u010e\n%\3&\3&"+
		"\7&\u0112\n&\f&\16&\u0115\13&\3&\5&\u0118\n&\3\'\3\'\5\'\u011c\n\'\3("+
		"\3(\3)\3)\5)\u0122\n)\3*\6*\u0125\n*\r*\16*\u0126\3+\3+\3+\3+\3,\3,\7"+
		",\u012f\n,\f,\16,\u0132\13,\3,\5,\u0135\n,\3-\3-\3.\3.\5.\u013b\n.\3/"+
		"\3/\5/\u013f\n/\3/\3/\3\60\3\60\7\60\u0145\n\60\f\60\16\60\u0148\13\60"+
		"\3\60\5\60\u014b\n\60\3\61\3\61\3\62\3\62\5\62\u0151\n\62\3\63\3\63\3"+
		"\63\3\63\3\64\3\64\7\64\u0159\n\64\f\64\16\64\u015c\13\64\3\64\5\64\u015f"+
		"\n\64\3\65\3\65\3\66\3\66\5\66\u0165\n\66\3\67\3\67\5\67\u0169\n\67\3"+
		"8\38\38\58\u016e\n8\38\58\u0171\n8\38\58\u0174\n8\38\38\38\58\u0179\n"+
		"8\38\58\u017c\n8\38\38\38\58\u0181\n8\38\38\38\58\u0186\n8\39\39\39\3"+
		":\3:\3;\5;\u018e\n;\3;\3;\3<\3<\3=\3=\3>\3>\3>\5>\u0199\n>\3?\3?\5?\u019d"+
		"\n?\3?\3?\3?\5?\u01a2\n?\3?\3?\5?\u01a6\n?\3@\3@\3@\3A\3A\3B\3B\3B\3B"+
		"\3B\3B\3B\3B\3B\5B\u01b6\nB\3C\3C\3C\3C\3C\3C\3C\3C\5C\u01c0\nC\3D\3D"+
		"\3E\3E\5E\u01c6\nE\3E\3E\3F\6F\u01cb\nF\rF\16F\u01cc\3G\3G\5G\u01d1\n"+
		"G\3H\3H\3H\3H\5H\u01d7\nH\3I\3I\3I\3I\3I\3I\3I\3I\3I\3I\3I\5I\u01e4\n"+
		"I\3J\3J\3J\3J\3J\3J\3J\3K\3K\3L\3L\3L\3L\3L\3M\3M\7M\u01f6\nM\fM\16M\u01f9"+
		"\13M\3N\3N\3N\3N\3N\3N\5N\u0201\nN\3O\3O\3O\3O\3O\3O\5O\u0209\nO\3P\6"+
		"P\u020c\nP\rP\16P\u020d\3P\3P\3Q\3Q\3Q\3Q\7Q\u0216\nQ\fQ\16Q\u0219\13"+
		"Q\3Q\3Q\3Q\3Q\3Q\3R\3R\3R\3R\7R\u0224\nR\fR\16R\u0227\13R\3R\3R\3\u0217"+
		"\2S\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36"+
		";\37= ?\2A\2C\2E\2G\2I\2K\2M\2O\2Q\2S\2U\2W\2Y\2[\2]\2_\2a\2c\2e\2g\2"+
		"i\2k\2m!o\2q\2s\2u\2w\2y\2{\2}\2\177\2\u0081\2\u0083\"\u0085#\u0087\2"+
		"\u0089$\u008b\2\u008d\2\u008f\2\u0091\2\u0093\2\u0095\2\u0097%\u0099&"+
		"\u009b\2\u009d\2\u009f\'\u00a1(\u00a3)\3\2\30\4\2NNnn\3\2\63;\4\2ZZzz"+
		"\5\2\62;CHch\3\2\629\4\2DDdd\3\2\62\63\4\2GGgg\4\2--//\6\2FFHHffhh\4\2"+
		"RRrr\4\2))^^\4\2$$^^\n\2$$))^^ddhhppttvv\3\2\62\65\6\2&&C\\aac|\4\2\2"+
		"\u0081\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aac|\5"+
		"\2\13\f\16\17\"\"\4\2\f\f\17\17\u0238\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2"+
		"\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2"+
		"\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3"+
		"\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3"+
		"\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65"+
		"\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2m\3\2\2\2\2\u0083"+
		"\3\2\2\2\2\u0085\3\2\2\2\2\u0089\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2"+
		"\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\3\u00a5\3\2\2\2\5\u00a7"+
		"\3\2\2\2\7\u00aa\3\2\2\2\t\u00ad\3\2\2\2\13\u00af\3\2\2\2\r\u00b1\3\2"+
		"\2\2\17\u00b3\3\2\2\2\21\u00b5\3\2\2\2\23\u00b7\3\2\2\2\25\u00b9\3\2\2"+
		"\2\27\u00bb\3\2\2\2\31\u00bd\3\2\2\2\33\u00bf\3\2\2\2\35\u00c2\3\2\2\2"+
		"\37\u00c5\3\2\2\2!\u00c8\3\2\2\2#\u00cb\3\2\2\2%\u00cd\3\2\2\2\'\u00cf"+
		"\3\2\2\2)\u00d1\3\2\2\2+\u00d4\3\2\2\2-\u00d7\3\2\2\2/\u00d9\3\2\2\2\61"+
		"\u00db\3\2\2\2\63\u00dd\3\2\2\2\65\u00e0\3\2\2\2\67\u00e5\3\2\2\29\u00e7"+
		"\3\2\2\2;\u00e9\3\2\2\2=\u00ef\3\2\2\2?\u00f1\3\2\2\2A\u00f5\3\2\2\2C"+
		"\u00f9\3\2\2\2E\u00fd\3\2\2\2G\u0101\3\2\2\2I\u010d\3\2\2\2K\u010f\3\2"+
		"\2\2M\u011b\3\2\2\2O\u011d\3\2\2\2Q\u0121\3\2\2\2S\u0124\3\2\2\2U\u0128"+
		"\3\2\2\2W\u012c\3\2\2\2Y\u0136\3\2\2\2[\u013a\3\2\2\2]\u013c\3\2\2\2_"+
		"\u0142\3\2\2\2a\u014c\3\2\2\2c\u0150\3\2\2\2e\u0152\3\2\2\2g\u0156\3\2"+
		"\2\2i\u0160\3\2\2\2k\u0164\3\2\2\2m\u0168\3\2\2\2o\u0185\3\2\2\2q\u0187"+
		"\3\2\2\2s\u018a\3\2\2\2u\u018d\3\2\2\2w\u0191\3\2\2\2y\u0193\3\2\2\2{"+
		"\u0195\3\2\2\2}\u01a5\3\2\2\2\177\u01a7\3\2\2\2\u0081\u01aa\3\2\2\2\u0083"+
		"\u01b5\3\2\2\2\u0085\u01bf\3\2\2\2\u0087\u01c1\3\2\2\2\u0089\u01c3\3\2"+
		"\2\2\u008b\u01ca\3\2\2\2\u008d\u01d0\3\2\2\2\u008f\u01d6\3\2\2\2\u0091"+
		"\u01e3\3\2\2\2\u0093\u01e5\3\2\2\2\u0095\u01ec\3\2\2\2\u0097\u01ee\3\2"+
		"\2\2\u0099\u01f3\3\2\2\2\u009b\u0200\3\2\2\2\u009d\u0208\3\2\2\2\u009f"+
		"\u020b\3\2\2\2\u00a1\u0211\3\2\2\2\u00a3\u021f\3\2\2\2\u00a5\u00a6\7\60"+
		"\2\2\u00a6\4\3\2\2\2\u00a7\u00a8\7-\2\2\u00a8\u00a9\7-\2\2\u00a9\6\3\2"+
		"\2\2\u00aa\u00ab\7/\2\2\u00ab\u00ac\7/\2\2\u00ac\b\3\2\2\2\u00ad\u00ae"+
		"\7-\2\2\u00ae\n\3\2\2\2\u00af\u00b0\7/\2\2\u00b0\f\3\2\2\2\u00b1\u00b2"+
		"\7\u0080\2\2\u00b2\16\3\2\2\2\u00b3\u00b4\7#\2\2\u00b4\20\3\2\2\2\u00b5"+
		"\u00b6\7,\2\2\u00b6\22\3\2\2\2\u00b7\u00b8\7\61\2\2\u00b8\24\3\2\2\2\u00b9"+
		"\u00ba\7\'\2\2\u00ba\26\3\2\2\2\u00bb\u00bc\7>\2\2\u00bc\30\3\2\2\2\u00bd"+
		"\u00be\7@\2\2\u00be\32\3\2\2\2\u00bf\u00c0\7>\2\2\u00c0\u00c1\7?\2\2\u00c1"+
		"\34\3\2\2\2\u00c2\u00c3\7@\2\2\u00c3\u00c4\7?\2\2\u00c4\36\3\2\2\2\u00c5"+
		"\u00c6\7?\2\2\u00c6\u00c7\7?\2\2\u00c7 \3\2\2\2\u00c8\u00c9\7#\2\2\u00c9"+
		"\u00ca\7?\2\2\u00ca\"\3\2\2\2\u00cb\u00cc\7(\2\2\u00cc$\3\2\2\2\u00cd"+
		"\u00ce\7`\2\2\u00ce&\3\2\2\2\u00cf\u00d0\7~\2\2\u00d0(\3\2\2\2\u00d1\u00d2"+
		"\7(\2\2\u00d2\u00d3\7(\2\2\u00d3*\3\2\2\2\u00d4\u00d5\7~\2\2\u00d5\u00d6"+
		"\7~\2\2\u00d6,\3\2\2\2\u00d7\u00d8\7A\2\2\u00d8.\3\2\2\2\u00d9\u00da\7"+
		"<\2\2\u00da\60\3\2\2\2\u00db\u00dc\7.\2\2\u00dc\62\3\2\2\2\u00dd\u00de"+
		"\7k\2\2\u00de\u00df\7h\2\2\u00df\64\3\2\2\2\u00e0\u00e1\7g\2\2\u00e1\u00e2"+
		"\7n\2\2\u00e2\u00e3\7u\2\2\u00e3\u00e4\7g\2\2\u00e4\66\3\2\2\2\u00e5\u00e6"+
		"\7=\2\2\u00e68\3\2\2\2\u00e7\u00e8\7*\2\2\u00e8:\3\2\2\2\u00e9\u00ea\7"+
		"+\2\2\u00ea<\3\2\2\2\u00eb\u00f0\5? \2\u00ec\u00f0\5A!\2\u00ed\u00f0\5"+
		"C\"\2\u00ee\u00f0\5E#\2\u00ef\u00eb\3\2\2\2\u00ef\u00ec\3\2\2\2\u00ef"+
		"\u00ed\3\2\2\2\u00ef\u00ee\3\2\2\2\u00f0>\3\2\2\2\u00f1\u00f3\5I%\2\u00f2"+
		"\u00f4\5G$\2\u00f3\u00f2\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4@\3\2\2\2\u00f5"+
		"\u00f7\5U+\2\u00f6\u00f8\5G$\2\u00f7\u00f6\3\2\2\2\u00f7\u00f8\3\2\2\2"+
		"\u00f8B\3\2\2\2\u00f9\u00fb\5]/\2\u00fa\u00fc\5G$\2\u00fb\u00fa\3\2\2"+
		"\2\u00fb\u00fc\3\2\2\2\u00fcD\3\2\2\2\u00fd\u00ff\5e\63\2\u00fe\u0100"+
		"\5G$\2\u00ff\u00fe\3\2\2\2\u00ff\u0100\3\2\2\2\u0100F\3\2\2\2\u0101\u0102"+
		"\t\2\2\2\u0102H\3\2\2\2\u0103\u010e\7\62\2\2\u0104\u010b\5O(\2\u0105\u0107"+
		"\5K&\2\u0106\u0105\3\2\2\2\u0106\u0107\3\2\2\2\u0107\u010c\3\2\2\2\u0108"+
		"\u0109\5S*\2\u0109\u010a\5K&\2\u010a\u010c\3\2\2\2\u010b\u0106\3\2\2\2"+
		"\u010b\u0108\3\2\2\2\u010c\u010e\3\2\2\2\u010d\u0103\3\2\2\2\u010d\u0104"+
		"\3\2\2\2\u010eJ\3\2\2\2\u010f\u0117\5M\'\2\u0110\u0112\5Q)\2\u0111\u0110"+
		"\3\2\2\2\u0112\u0115\3\2\2\2\u0113\u0111\3\2\2\2\u0113\u0114\3\2\2\2\u0114"+
		"\u0116\3\2\2\2\u0115\u0113\3\2\2\2\u0116\u0118\5M\'\2\u0117\u0113\3\2"+
		"\2\2\u0117\u0118\3\2\2\2\u0118L\3\2\2\2\u0119\u011c\7\62\2\2\u011a\u011c"+
		"\5O(\2\u011b\u0119\3\2\2\2\u011b\u011a\3\2\2\2\u011cN\3\2\2\2\u011d\u011e"+
		"\t\3\2\2\u011eP\3\2\2\2\u011f\u0122\5M\'\2\u0120\u0122\7a\2\2\u0121\u011f"+
		"\3\2\2\2\u0121\u0120\3\2\2\2\u0122R\3\2\2\2\u0123\u0125\7a\2\2\u0124\u0123"+
		"\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u0124\3\2\2\2\u0126\u0127\3\2\2\2\u0127"+
		"T\3\2\2\2\u0128\u0129\7\62\2\2\u0129\u012a\t\4\2\2\u012a\u012b\5W,\2\u012b"+
		"V\3\2\2\2\u012c\u0134\5Y-\2\u012d\u012f\5[.\2\u012e\u012d\3\2\2\2\u012f"+
		"\u0132\3\2\2\2\u0130\u012e\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u0133\3\2"+
		"\2\2\u0132\u0130\3\2\2\2\u0133\u0135\5Y-\2\u0134\u0130\3\2\2\2\u0134\u0135"+
		"\3\2\2\2\u0135X\3\2\2\2\u0136\u0137\t\5\2\2\u0137Z\3\2\2\2\u0138\u013b"+
		"\5Y-\2\u0139\u013b\7a\2\2\u013a\u0138\3\2\2\2\u013a\u0139\3\2\2\2\u013b"+
		"\\\3\2\2\2\u013c\u013e\7\62\2\2\u013d\u013f\5S*\2\u013e\u013d\3\2\2\2"+
		"\u013e\u013f\3\2\2\2\u013f\u0140\3\2\2\2\u0140\u0141\5_\60\2\u0141^\3"+
		"\2\2\2\u0142\u014a\5a\61\2\u0143\u0145\5c\62\2\u0144\u0143\3\2\2\2\u0145"+
		"\u0148\3\2\2\2\u0146\u0144\3\2\2\2\u0146\u0147\3\2\2\2\u0147\u0149\3\2"+
		"\2\2\u0148\u0146\3\2\2\2\u0149\u014b\5a\61\2\u014a\u0146\3\2\2\2\u014a"+
		"\u014b\3\2\2\2\u014b`\3\2\2\2\u014c\u014d\t\6\2\2\u014db\3\2\2\2\u014e"+
		"\u0151\5a\61\2\u014f\u0151\7a\2\2\u0150\u014e\3\2\2\2\u0150\u014f\3\2"+
		"\2\2\u0151d\3\2\2\2\u0152\u0153\7\62\2\2\u0153\u0154\t\7\2\2\u0154\u0155"+
		"\5g\64\2\u0155f\3\2\2\2\u0156\u015e\5i\65\2\u0157\u0159\5k\66\2\u0158"+
		"\u0157\3\2\2\2\u0159\u015c\3\2\2\2\u015a\u0158\3\2\2\2\u015a\u015b\3\2"+
		"\2\2\u015b\u015d\3\2\2\2\u015c\u015a\3\2\2\2\u015d\u015f\5i\65\2\u015e"+
		"\u015a\3\2\2\2\u015e\u015f\3\2\2\2\u015fh\3\2\2\2\u0160\u0161\t\b\2\2"+
		"\u0161j\3\2\2\2\u0162\u0165\5i\65\2\u0163\u0165\7a\2\2\u0164\u0162\3\2"+
		"\2\2\u0164\u0163\3\2\2\2\u0165l\3\2\2\2\u0166\u0169\5o8\2\u0167\u0169"+
		"\5{>\2\u0168\u0166\3\2\2\2\u0168\u0167\3\2\2\2\u0169n\3\2\2\2\u016a\u016b"+
		"\5K&\2\u016b\u016d\7\60\2\2\u016c\u016e\5K&\2\u016d\u016c\3\2\2\2\u016d"+
		"\u016e\3\2\2\2\u016e\u0170\3\2\2\2\u016f\u0171\5q9\2\u0170\u016f\3\2\2"+
		"\2\u0170\u0171\3\2\2\2\u0171\u0173\3\2\2\2\u0172\u0174\5y=\2\u0173\u0172"+
		"\3\2\2\2\u0173\u0174\3\2\2\2\u0174\u0186\3\2\2\2\u0175\u0176\7\60\2\2"+
		"\u0176\u0178\5K&\2\u0177\u0179\5q9\2\u0178\u0177\3\2\2\2\u0178\u0179\3"+
		"\2\2\2\u0179\u017b\3\2\2\2\u017a\u017c\5y=\2\u017b\u017a\3\2\2\2\u017b"+
		"\u017c\3\2\2\2\u017c\u0186\3\2\2\2\u017d\u017e\5K&\2\u017e\u0180\5q9\2"+
		"\u017f\u0181\5y=\2\u0180\u017f\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u0186"+
		"\3\2\2\2\u0182\u0183\5K&\2\u0183\u0184\5y=\2\u0184\u0186\3\2\2\2\u0185"+
		"\u016a\3\2\2\2\u0185\u0175\3\2\2\2\u0185\u017d\3\2\2\2\u0185\u0182\3\2"+
		"\2\2\u0186p\3\2\2\2\u0187\u0188\5s:\2\u0188\u0189\5u;\2\u0189r\3\2\2\2"+
		"\u018a\u018b\t\t\2\2\u018bt\3\2\2\2\u018c\u018e\5w<\2\u018d\u018c\3\2"+
		"\2\2\u018d\u018e\3\2\2\2\u018e\u018f\3\2\2\2\u018f\u0190\5K&\2\u0190v"+
		"\3\2\2\2\u0191\u0192\t\n\2\2\u0192x\3\2\2\2\u0193\u0194\t\13\2\2\u0194"+
		"z\3\2\2\2\u0195\u0196\5}?\2\u0196\u0198\5\177@\2\u0197\u0199\5y=\2\u0198"+
		"\u0197\3\2\2\2\u0198\u0199\3\2\2\2\u0199|\3\2\2\2\u019a\u019c\5U+\2\u019b"+
		"\u019d\7\60\2\2\u019c\u019b\3\2\2\2\u019c\u019d\3\2\2\2\u019d\u01a6\3"+
		"\2\2\2\u019e\u019f\7\62\2\2\u019f\u01a1\t\4\2\2\u01a0\u01a2\5W,\2\u01a1"+
		"\u01a0\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3\u01a4\7\60"+
		"\2\2\u01a4\u01a6\5W,\2\u01a5\u019a\3\2\2\2\u01a5\u019e\3\2\2\2\u01a6~"+
		"\3\2\2\2\u01a7\u01a8\5\u0081A\2\u01a8\u01a9\5u;\2\u01a9\u0080\3\2\2\2"+
		"\u01aa\u01ab\t\f\2\2\u01ab\u0082\3\2\2\2\u01ac\u01ad\7v\2\2\u01ad\u01ae"+
		"\7t\2\2\u01ae\u01af\7w\2\2\u01af\u01b6\7g\2\2\u01b0\u01b1\7h\2\2\u01b1"+
		"\u01b2\7c\2\2\u01b2\u01b3\7n\2\2\u01b3\u01b4\7u\2\2\u01b4\u01b6\7g\2\2"+
		"\u01b5\u01ac\3\2\2\2\u01b5\u01b0\3\2\2\2\u01b6\u0084\3\2\2\2\u01b7\u01b8"+
		"\7)\2\2\u01b8\u01b9\5\u0087D\2\u01b9\u01ba\7)\2\2\u01ba\u01c0\3\2\2\2"+
		"\u01bb\u01bc\7)\2\2\u01bc\u01bd\5\u008fH\2\u01bd\u01be\7)\2\2\u01be\u01c0"+
		"\3\2\2\2\u01bf\u01b7\3\2\2\2\u01bf\u01bb\3\2\2\2\u01c0\u0086\3\2\2\2\u01c1"+
		"\u01c2\n\r\2\2\u01c2\u0088\3\2\2\2\u01c3\u01c5\7$\2\2\u01c4\u01c6\5\u008b"+
		"F\2\u01c5\u01c4\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7"+
		"\u01c8\7$\2\2\u01c8\u008a\3\2\2\2\u01c9\u01cb\5\u008dG\2\u01ca\u01c9\3"+
		"\2\2\2\u01cb\u01cc\3\2\2\2\u01cc\u01ca\3\2\2\2\u01cc\u01cd\3\2\2\2\u01cd"+
		"\u008c\3\2\2\2\u01ce\u01d1\n\16\2\2\u01cf\u01d1\5\u008fH\2\u01d0\u01ce"+
		"\3\2\2\2\u01d0\u01cf\3\2\2\2\u01d1\u008e\3\2\2\2\u01d2\u01d3\7^\2\2\u01d3"+
		"\u01d7\t\17\2\2\u01d4\u01d7\5\u0091I\2\u01d5\u01d7\5\u0093J\2\u01d6\u01d2"+
		"\3\2\2\2\u01d6\u01d4\3\2\2\2\u01d6\u01d5\3\2\2\2\u01d7\u0090\3\2\2\2\u01d8"+
		"\u01d9\7^\2\2\u01d9\u01e4\5a\61\2\u01da\u01db\7^\2\2\u01db\u01dc\5a\61"+
		"\2\u01dc\u01dd\5a\61\2\u01dd\u01e4\3\2\2\2\u01de\u01df\7^\2\2\u01df\u01e0"+
		"\5\u0095K\2\u01e0\u01e1\5a\61\2\u01e1\u01e2\5a\61\2\u01e2\u01e4\3\2\2"+
		"\2\u01e3\u01d8\3\2\2\2\u01e3\u01da\3\2\2\2\u01e3\u01de\3\2\2\2\u01e4\u0092"+
		"\3\2\2\2\u01e5\u01e6\7^\2\2\u01e6\u01e7\7w\2\2\u01e7\u01e8\5Y-\2\u01e8"+
		"\u01e9\5Y-\2\u01e9\u01ea\5Y-\2\u01ea\u01eb\5Y-\2\u01eb\u0094\3\2\2\2\u01ec"+
		"\u01ed\t\20\2\2\u01ed\u0096\3\2\2\2\u01ee\u01ef\7p\2\2\u01ef\u01f0\7w"+
		"\2\2\u01f0\u01f1\7n\2\2\u01f1\u01f2\7n\2\2\u01f2\u0098\3\2\2\2\u01f3\u01f7"+
		"\5\u009bN\2\u01f4\u01f6\5\u009dO\2\u01f5\u01f4\3\2\2\2\u01f6\u01f9\3\2"+
		"\2\2\u01f7\u01f5\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8\u009a\3\2\2\2\u01f9"+
		"\u01f7\3\2\2\2\u01fa\u0201\t\21\2\2\u01fb\u01fc\n\22\2\2\u01fc\u0201\6"+
		"N\2\2\u01fd\u01fe\t\23\2\2\u01fe\u01ff\t\24\2\2\u01ff\u0201\6N\3\2\u0200"+
		"\u01fa\3\2\2\2\u0200\u01fb\3\2\2\2\u0200\u01fd\3\2\2\2\u0201\u009c\3\2"+
		"\2\2\u0202\u0209\t\25\2\2\u0203\u0204\n\22\2\2\u0204\u0209\6O\4\2\u0205"+
		"\u0206\t\23\2\2\u0206\u0207\t\24\2\2\u0207\u0209\6O\5\2\u0208\u0202\3"+
		"\2\2\2\u0208\u0203\3\2\2\2\u0208\u0205\3\2\2\2\u0209\u009e\3\2\2\2\u020a"+
		"\u020c\t\26\2\2\u020b\u020a\3\2\2\2\u020c\u020d\3\2\2\2\u020d\u020b\3"+
		"\2\2\2\u020d\u020e\3\2\2\2\u020e\u020f\3\2\2\2\u020f\u0210\bP\2\2\u0210"+
		"\u00a0\3\2\2\2\u0211\u0212\7\61\2\2\u0212\u0213\7,\2\2\u0213\u0217\3\2"+
		"\2\2\u0214\u0216\13\2\2\2\u0215\u0214\3\2\2\2\u0216\u0219\3\2\2\2\u0217"+
		"\u0218\3\2\2\2\u0217\u0215\3\2\2\2\u0218\u021a\3\2\2\2\u0219\u0217\3\2"+
		"\2\2\u021a\u021b\7,\2\2\u021b\u021c\7\61\2\2\u021c\u021d\3\2\2\2\u021d"+
		"\u021e\bQ\2\2\u021e\u00a2\3\2\2\2\u021f\u0220\7\61\2\2\u0220\u0221\7\61"+
		"\2\2\u0221\u0225\3\2\2\2\u0222\u0224\n\27\2\2\u0223\u0222\3\2\2\2\u0224"+
		"\u0227\3\2\2\2\u0225\u0223\3\2\2\2\u0225\u0226\3\2\2\2\u0226\u0228\3\2"+
		"\2\2\u0227\u0225\3\2\2\2\u0228\u0229\bR\2\2\u0229\u00a4\3\2\2\2\64\2\u00ef"+
		"\u00f3\u00f7\u00fb\u00ff\u0106\u010b\u010d\u0113\u0117\u011b\u0121\u0126"+
		"\u0130\u0134\u013a\u013e\u0146\u014a\u0150\u015a\u015e\u0164\u0168\u016d"+
		"\u0170\u0173\u0178\u017b\u0180\u0185\u018d\u0198\u019c\u01a1\u01a5\u01b5"+
		"\u01bf\u01c5\u01cc\u01d0\u01d6\u01e3\u01f7\u0200\u0208\u020d\u0217\u0225"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}