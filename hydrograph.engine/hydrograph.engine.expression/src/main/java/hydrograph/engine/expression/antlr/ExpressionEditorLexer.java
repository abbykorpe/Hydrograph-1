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
		T__24=25, T__25=26, LPAREN=27, RPAREN=28, IntegerLiteral=29, FloatingPointLiteral=30, 
		BooleanLiteral=31, CharacterLiteral=32, StringLiteral=33, NullLiteral=34, 
		Identifier=35, WS=36, COMMENT=37, LINE_COMMENT=38;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
		"T__25", "LPAREN", "RPAREN", "IntegerLiteral", "DecimalIntegerLiteral", 
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
		"'||'", "'?'", "':'", "','", "'if'", "'else'", "';'", "'('", "')'", null, 
		null, null, null, null, "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, "LPAREN", "RPAREN", "IntegerLiteral", "FloatingPointLiteral", 
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2(\u0226\b\1\4\2\t"+
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
		"\27\3\27\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3"+
		"\34\3\34\3\35\3\35\3\36\3\36\3\36\3\36\5\36\u00ec\n\36\3\37\3\37\5\37"+
		"\u00f0\n\37\3 \3 \5 \u00f4\n \3!\3!\5!\u00f8\n!\3\"\3\"\5\"\u00fc\n\""+
		"\3#\3#\3$\3$\3$\5$\u0103\n$\3$\3$\3$\5$\u0108\n$\5$\u010a\n$\3%\3%\7%"+
		"\u010e\n%\f%\16%\u0111\13%\3%\5%\u0114\n%\3&\3&\5&\u0118\n&\3\'\3\'\3"+
		"(\3(\5(\u011e\n(\3)\6)\u0121\n)\r)\16)\u0122\3*\3*\3*\3*\3+\3+\7+\u012b"+
		"\n+\f+\16+\u012e\13+\3+\5+\u0131\n+\3,\3,\3-\3-\5-\u0137\n-\3.\3.\5.\u013b"+
		"\n.\3.\3.\3/\3/\7/\u0141\n/\f/\16/\u0144\13/\3/\5/\u0147\n/\3\60\3\60"+
		"\3\61\3\61\5\61\u014d\n\61\3\62\3\62\3\62\3\62\3\63\3\63\7\63\u0155\n"+
		"\63\f\63\16\63\u0158\13\63\3\63\5\63\u015b\n\63\3\64\3\64\3\65\3\65\5"+
		"\65\u0161\n\65\3\66\3\66\5\66\u0165\n\66\3\67\3\67\3\67\5\67\u016a\n\67"+
		"\3\67\5\67\u016d\n\67\3\67\5\67\u0170\n\67\3\67\3\67\3\67\5\67\u0175\n"+
		"\67\3\67\5\67\u0178\n\67\3\67\3\67\3\67\5\67\u017d\n\67\3\67\3\67\3\67"+
		"\5\67\u0182\n\67\38\38\38\39\39\3:\5:\u018a\n:\3:\3:\3;\3;\3<\3<\3=\3"+
		"=\3=\5=\u0195\n=\3>\3>\5>\u0199\n>\3>\3>\3>\5>\u019e\n>\3>\3>\5>\u01a2"+
		"\n>\3?\3?\3?\3@\3@\3A\3A\3A\3A\3A\3A\3A\3A\3A\5A\u01b2\nA\3B\3B\3B\3B"+
		"\3B\3B\3B\3B\5B\u01bc\nB\3C\3C\3D\3D\5D\u01c2\nD\3D\3D\3E\6E\u01c7\nE"+
		"\rE\16E\u01c8\3F\3F\5F\u01cd\nF\3G\3G\3G\3G\5G\u01d3\nG\3H\3H\3H\3H\3"+
		"H\3H\3H\3H\3H\3H\3H\5H\u01e0\nH\3I\3I\3I\3I\3I\3I\3I\3J\3J\3K\3K\3K\3"+
		"K\3K\3L\3L\7L\u01f2\nL\fL\16L\u01f5\13L\3M\3M\3M\3M\3M\3M\5M\u01fd\nM"+
		"\3N\3N\3N\3N\3N\3N\5N\u0205\nN\3O\6O\u0208\nO\rO\16O\u0209\3O\3O\3P\3"+
		"P\3P\3P\7P\u0212\nP\fP\16P\u0215\13P\3P\3P\3P\3P\3P\3Q\3Q\3Q\3Q\7Q\u0220"+
		"\nQ\fQ\16Q\u0223\13Q\3Q\3Q\3\u0213\2R\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37=\2?\2A\2C\2E\2G\2I\2K\2M\2O\2Q\2"+
		"S\2U\2W\2Y\2[\2]\2_\2a\2c\2e\2g\2i\2k m\2o\2q\2s\2u\2w\2y\2{\2}\2\177"+
		"\2\u0081!\u0083\"\u0085\2\u0087#\u0089\2\u008b\2\u008d\2\u008f\2\u0091"+
		"\2\u0093\2\u0095$\u0097%\u0099\2\u009b\2\u009d&\u009f\'\u00a1(\3\2\30"+
		"\4\2NNnn\3\2\63;\4\2ZZzz\5\2\62;CHch\3\2\629\4\2DDdd\3\2\62\63\4\2GGg"+
		"g\4\2--//\6\2FFHHffhh\4\2RRrr\4\2))^^\4\2$$^^\n\2$$))^^ddhhppttvv\3\2"+
		"\62\65\7\2&&\60\60C\\aac|\4\2\2\u0081\ud802\udc01\3\2\ud802\udc01\3\2"+
		"\udc02\ue001\b\2&&\60\60\62;C\\aac|\5\2\13\f\16\17\"\"\4\2\f\f\17\17\u0234"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3"+
		"\2\2\2\2k\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0087\3\2\2\2\2\u0095"+
		"\3\2\2\2\2\u0097\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1\3\2\2"+
		"\2\3\u00a3\3\2\2\2\5\u00a6\3\2\2\2\7\u00a9\3\2\2\2\t\u00ab\3\2\2\2\13"+
		"\u00ad\3\2\2\2\r\u00af\3\2\2\2\17\u00b1\3\2\2\2\21\u00b3\3\2\2\2\23\u00b5"+
		"\3\2\2\2\25\u00b7\3\2\2\2\27\u00b9\3\2\2\2\31\u00bb\3\2\2\2\33\u00be\3"+
		"\2\2\2\35\u00c1\3\2\2\2\37\u00c4\3\2\2\2!\u00c7\3\2\2\2#\u00c9\3\2\2\2"+
		"%\u00cb\3\2\2\2\'\u00cd\3\2\2\2)\u00d0\3\2\2\2+\u00d3\3\2\2\2-\u00d5\3"+
		"\2\2\2/\u00d7\3\2\2\2\61\u00d9\3\2\2\2\63\u00dc\3\2\2\2\65\u00e1\3\2\2"+
		"\2\67\u00e3\3\2\2\29\u00e5\3\2\2\2;\u00eb\3\2\2\2=\u00ed\3\2\2\2?\u00f1"+
		"\3\2\2\2A\u00f5\3\2\2\2C\u00f9\3\2\2\2E\u00fd\3\2\2\2G\u0109\3\2\2\2I"+
		"\u010b\3\2\2\2K\u0117\3\2\2\2M\u0119\3\2\2\2O\u011d\3\2\2\2Q\u0120\3\2"+
		"\2\2S\u0124\3\2\2\2U\u0128\3\2\2\2W\u0132\3\2\2\2Y\u0136\3\2\2\2[\u0138"+
		"\3\2\2\2]\u013e\3\2\2\2_\u0148\3\2\2\2a\u014c\3\2\2\2c\u014e\3\2\2\2e"+
		"\u0152\3\2\2\2g\u015c\3\2\2\2i\u0160\3\2\2\2k\u0164\3\2\2\2m\u0181\3\2"+
		"\2\2o\u0183\3\2\2\2q\u0186\3\2\2\2s\u0189\3\2\2\2u\u018d\3\2\2\2w\u018f"+
		"\3\2\2\2y\u0191\3\2\2\2{\u01a1\3\2\2\2}\u01a3\3\2\2\2\177\u01a6\3\2\2"+
		"\2\u0081\u01b1\3\2\2\2\u0083\u01bb\3\2\2\2\u0085\u01bd\3\2\2\2\u0087\u01bf"+
		"\3\2\2\2\u0089\u01c6\3\2\2\2\u008b\u01cc\3\2\2\2\u008d\u01d2\3\2\2\2\u008f"+
		"\u01df\3\2\2\2\u0091\u01e1\3\2\2\2\u0093\u01e8\3\2\2\2\u0095\u01ea\3\2"+
		"\2\2\u0097\u01ef\3\2\2\2\u0099\u01fc\3\2\2\2\u009b\u0204\3\2\2\2\u009d"+
		"\u0207\3\2\2\2\u009f\u020d\3\2\2\2\u00a1\u021b\3\2\2\2\u00a3\u00a4\7-"+
		"\2\2\u00a4\u00a5\7-\2\2\u00a5\4\3\2\2\2\u00a6\u00a7\7/\2\2\u00a7\u00a8"+
		"\7/\2\2\u00a8\6\3\2\2\2\u00a9\u00aa\7-\2\2\u00aa\b\3\2\2\2\u00ab\u00ac"+
		"\7/\2\2\u00ac\n\3\2\2\2\u00ad\u00ae\7\u0080\2\2\u00ae\f\3\2\2\2\u00af"+
		"\u00b0\7#\2\2\u00b0\16\3\2\2\2\u00b1\u00b2\7,\2\2\u00b2\20\3\2\2\2\u00b3"+
		"\u00b4\7\61\2\2\u00b4\22\3\2\2\2\u00b5\u00b6\7\'\2\2\u00b6\24\3\2\2\2"+
		"\u00b7\u00b8\7>\2\2\u00b8\26\3\2\2\2\u00b9\u00ba\7@\2\2\u00ba\30\3\2\2"+
		"\2\u00bb\u00bc\7>\2\2\u00bc\u00bd\7?\2\2\u00bd\32\3\2\2\2\u00be\u00bf"+
		"\7@\2\2\u00bf\u00c0\7?\2\2\u00c0\34\3\2\2\2\u00c1\u00c2\7?\2\2\u00c2\u00c3"+
		"\7?\2\2\u00c3\36\3\2\2\2\u00c4\u00c5\7#\2\2\u00c5\u00c6\7?\2\2\u00c6 "+
		"\3\2\2\2\u00c7\u00c8\7(\2\2\u00c8\"\3\2\2\2\u00c9\u00ca\7`\2\2\u00ca$"+
		"\3\2\2\2\u00cb\u00cc\7~\2\2\u00cc&\3\2\2\2\u00cd\u00ce\7(\2\2\u00ce\u00cf"+
		"\7(\2\2\u00cf(\3\2\2\2\u00d0\u00d1\7~\2\2\u00d1\u00d2\7~\2\2\u00d2*\3"+
		"\2\2\2\u00d3\u00d4\7A\2\2\u00d4,\3\2\2\2\u00d5\u00d6\7<\2\2\u00d6.\3\2"+
		"\2\2\u00d7\u00d8\7.\2\2\u00d8\60\3\2\2\2\u00d9\u00da\7k\2\2\u00da\u00db"+
		"\7h\2\2\u00db\62\3\2\2\2\u00dc\u00dd\7g\2\2\u00dd\u00de\7n\2\2\u00de\u00df"+
		"\7u\2\2\u00df\u00e0\7g\2\2\u00e0\64\3\2\2\2\u00e1\u00e2\7=\2\2\u00e2\66"+
		"\3\2\2\2\u00e3\u00e4\7*\2\2\u00e48\3\2\2\2\u00e5\u00e6\7+\2\2\u00e6:\3"+
		"\2\2\2\u00e7\u00ec\5=\37\2\u00e8\u00ec\5? \2\u00e9\u00ec\5A!\2\u00ea\u00ec"+
		"\5C\"\2\u00eb\u00e7\3\2\2\2\u00eb\u00e8\3\2\2\2\u00eb\u00e9\3\2\2\2\u00eb"+
		"\u00ea\3\2\2\2\u00ec<\3\2\2\2\u00ed\u00ef\5G$\2\u00ee\u00f0\5E#\2\u00ef"+
		"\u00ee\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0>\3\2\2\2\u00f1\u00f3\5S*\2\u00f2"+
		"\u00f4\5E#\2\u00f3\u00f2\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4@\3\2\2\2\u00f5"+
		"\u00f7\5[.\2\u00f6\u00f8\5E#\2\u00f7\u00f6\3\2\2\2\u00f7\u00f8\3\2\2\2"+
		"\u00f8B\3\2\2\2\u00f9\u00fb\5c\62\2\u00fa\u00fc\5E#\2\u00fb\u00fa\3\2"+
		"\2\2\u00fb\u00fc\3\2\2\2\u00fcD\3\2\2\2\u00fd\u00fe\t\2\2\2\u00feF\3\2"+
		"\2\2\u00ff\u010a\7\62\2\2\u0100\u0107\5M\'\2\u0101\u0103\5I%\2\u0102\u0101"+
		"\3\2\2\2\u0102\u0103\3\2\2\2\u0103\u0108\3\2\2\2\u0104\u0105\5Q)\2\u0105"+
		"\u0106\5I%\2\u0106\u0108\3\2\2\2\u0107\u0102\3\2\2\2\u0107\u0104\3\2\2"+
		"\2\u0108\u010a\3\2\2\2\u0109\u00ff\3\2\2\2\u0109\u0100\3\2\2\2\u010aH"+
		"\3\2\2\2\u010b\u0113\5K&\2\u010c\u010e\5O(\2\u010d\u010c\3\2\2\2\u010e"+
		"\u0111\3\2\2\2\u010f\u010d\3\2\2\2\u010f\u0110\3\2\2\2\u0110\u0112\3\2"+
		"\2\2\u0111\u010f\3\2\2\2\u0112\u0114\5K&\2\u0113\u010f\3\2\2\2\u0113\u0114"+
		"\3\2\2\2\u0114J\3\2\2\2\u0115\u0118\7\62\2\2\u0116\u0118\5M\'\2\u0117"+
		"\u0115\3\2\2\2\u0117\u0116\3\2\2\2\u0118L\3\2\2\2\u0119\u011a\t\3\2\2"+
		"\u011aN\3\2\2\2\u011b\u011e\5K&\2\u011c\u011e\7a\2\2\u011d\u011b\3\2\2"+
		"\2\u011d\u011c\3\2\2\2\u011eP\3\2\2\2\u011f\u0121\7a\2\2\u0120\u011f\3"+
		"\2\2\2\u0121\u0122\3\2\2\2\u0122\u0120\3\2\2\2\u0122\u0123\3\2\2\2\u0123"+
		"R\3\2\2\2\u0124\u0125\7\62\2\2\u0125\u0126\t\4\2\2\u0126\u0127\5U+\2\u0127"+
		"T\3\2\2\2\u0128\u0130\5W,\2\u0129\u012b\5Y-\2\u012a\u0129\3\2\2\2\u012b"+
		"\u012e\3\2\2\2\u012c\u012a\3\2\2\2\u012c\u012d\3\2\2\2\u012d\u012f\3\2"+
		"\2\2\u012e\u012c\3\2\2\2\u012f\u0131\5W,\2\u0130\u012c\3\2\2\2\u0130\u0131"+
		"\3\2\2\2\u0131V\3\2\2\2\u0132\u0133\t\5\2\2\u0133X\3\2\2\2\u0134\u0137"+
		"\5W,\2\u0135\u0137\7a\2\2\u0136\u0134\3\2\2\2\u0136\u0135\3\2\2\2\u0137"+
		"Z\3\2\2\2\u0138\u013a\7\62\2\2\u0139\u013b\5Q)\2\u013a\u0139\3\2\2\2\u013a"+
		"\u013b\3\2\2\2\u013b\u013c\3\2\2\2\u013c\u013d\5]/\2\u013d\\\3\2\2\2\u013e"+
		"\u0146\5_\60\2\u013f\u0141\5a\61\2\u0140\u013f\3\2\2\2\u0141\u0144\3\2"+
		"\2\2\u0142\u0140\3\2\2\2\u0142\u0143\3\2\2\2\u0143\u0145\3\2\2\2\u0144"+
		"\u0142\3\2\2\2\u0145\u0147\5_\60\2\u0146\u0142\3\2\2\2\u0146\u0147\3\2"+
		"\2\2\u0147^\3\2\2\2\u0148\u0149\t\6\2\2\u0149`\3\2\2\2\u014a\u014d\5_"+
		"\60\2\u014b\u014d\7a\2\2\u014c\u014a\3\2\2\2\u014c\u014b\3\2\2\2\u014d"+
		"b\3\2\2\2\u014e\u014f\7\62\2\2\u014f\u0150\t\7\2\2\u0150\u0151\5e\63\2"+
		"\u0151d\3\2\2\2\u0152\u015a\5g\64\2\u0153\u0155\5i\65\2\u0154\u0153\3"+
		"\2\2\2\u0155\u0158\3\2\2\2\u0156\u0154\3\2\2\2\u0156\u0157\3\2\2\2\u0157"+
		"\u0159\3\2\2\2\u0158\u0156\3\2\2\2\u0159\u015b\5g\64\2\u015a\u0156\3\2"+
		"\2\2\u015a\u015b\3\2\2\2\u015bf\3\2\2\2\u015c\u015d\t\b\2\2\u015dh\3\2"+
		"\2\2\u015e\u0161\5g\64\2\u015f\u0161\7a\2\2\u0160\u015e\3\2\2\2\u0160"+
		"\u015f\3\2\2\2\u0161j\3\2\2\2\u0162\u0165\5m\67\2\u0163\u0165\5y=\2\u0164"+
		"\u0162\3\2\2\2\u0164\u0163\3\2\2\2\u0165l\3\2\2\2\u0166\u0167\5I%\2\u0167"+
		"\u0169\7\60\2\2\u0168\u016a\5I%\2\u0169\u0168\3\2\2\2\u0169\u016a\3\2"+
		"\2\2\u016a\u016c\3\2\2\2\u016b\u016d\5o8\2\u016c\u016b\3\2\2\2\u016c\u016d"+
		"\3\2\2\2\u016d\u016f\3\2\2\2\u016e\u0170\5w<\2\u016f\u016e\3\2\2\2\u016f"+
		"\u0170\3\2\2\2\u0170\u0182\3\2\2\2\u0171\u0172\7\60\2\2\u0172\u0174\5"+
		"I%\2\u0173\u0175\5o8\2\u0174\u0173\3\2\2\2\u0174\u0175\3\2\2\2\u0175\u0177"+
		"\3\2\2\2\u0176\u0178\5w<\2\u0177\u0176\3\2\2\2\u0177\u0178\3\2\2\2\u0178"+
		"\u0182\3\2\2\2\u0179\u017a\5I%\2\u017a\u017c\5o8\2\u017b\u017d\5w<\2\u017c"+
		"\u017b\3\2\2\2\u017c\u017d\3\2\2\2\u017d\u0182\3\2\2\2\u017e\u017f\5I"+
		"%\2\u017f\u0180\5w<\2\u0180\u0182\3\2\2\2\u0181\u0166\3\2\2\2\u0181\u0171"+
		"\3\2\2\2\u0181\u0179\3\2\2\2\u0181\u017e\3\2\2\2\u0182n\3\2\2\2\u0183"+
		"\u0184\5q9\2\u0184\u0185\5s:\2\u0185p\3\2\2\2\u0186\u0187\t\t\2\2\u0187"+
		"r\3\2\2\2\u0188\u018a\5u;\2\u0189\u0188\3\2\2\2\u0189\u018a\3\2\2\2\u018a"+
		"\u018b\3\2\2\2\u018b\u018c\5I%\2\u018ct\3\2\2\2\u018d\u018e\t\n\2\2\u018e"+
		"v\3\2\2\2\u018f\u0190\t\13\2\2\u0190x\3\2\2\2\u0191\u0192\5{>\2\u0192"+
		"\u0194\5}?\2\u0193\u0195\5w<\2\u0194\u0193\3\2\2\2\u0194\u0195\3\2\2\2"+
		"\u0195z\3\2\2\2\u0196\u0198\5S*\2\u0197\u0199\7\60\2\2\u0198\u0197\3\2"+
		"\2\2\u0198\u0199\3\2\2\2\u0199\u01a2\3\2\2\2\u019a\u019b\7\62\2\2\u019b"+
		"\u019d\t\4\2\2\u019c\u019e\5U+\2\u019d\u019c\3\2\2\2\u019d\u019e\3\2\2"+
		"\2\u019e\u019f\3\2\2\2\u019f\u01a0\7\60\2\2\u01a0\u01a2\5U+\2\u01a1\u0196"+
		"\3\2\2\2\u01a1\u019a\3\2\2\2\u01a2|\3\2\2\2\u01a3\u01a4\5\177@\2\u01a4"+
		"\u01a5\5s:\2\u01a5~\3\2\2\2\u01a6\u01a7\t\f\2\2\u01a7\u0080\3\2\2\2\u01a8"+
		"\u01a9\7v\2\2\u01a9\u01aa\7t\2\2\u01aa\u01ab\7w\2\2\u01ab\u01b2\7g\2\2"+
		"\u01ac\u01ad\7h\2\2\u01ad\u01ae\7c\2\2\u01ae\u01af\7n\2\2\u01af\u01b0"+
		"\7u\2\2\u01b0\u01b2\7g\2\2\u01b1\u01a8\3\2\2\2\u01b1\u01ac\3\2\2\2\u01b2"+
		"\u0082\3\2\2\2\u01b3\u01b4\7)\2\2\u01b4\u01b5\5\u0085C\2\u01b5\u01b6\7"+
		")\2\2\u01b6\u01bc\3\2\2\2\u01b7\u01b8\7)\2\2\u01b8\u01b9\5\u008dG\2\u01b9"+
		"\u01ba\7)\2\2\u01ba\u01bc\3\2\2\2\u01bb\u01b3\3\2\2\2\u01bb\u01b7\3\2"+
		"\2\2\u01bc\u0084\3\2\2\2\u01bd\u01be\n\r\2\2\u01be\u0086\3\2\2\2\u01bf"+
		"\u01c1\7$\2\2\u01c0\u01c2\5\u0089E\2\u01c1\u01c0\3\2\2\2\u01c1\u01c2\3"+
		"\2\2\2\u01c2\u01c3\3\2\2\2\u01c3\u01c4\7$\2\2\u01c4\u0088\3\2\2\2\u01c5"+
		"\u01c7\5\u008bF\2\u01c6\u01c5\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8\u01c6"+
		"\3\2\2\2\u01c8\u01c9\3\2\2\2\u01c9\u008a\3\2\2\2\u01ca\u01cd\n\16\2\2"+
		"\u01cb\u01cd\5\u008dG\2\u01cc\u01ca\3\2\2\2\u01cc\u01cb\3\2\2\2\u01cd"+
		"\u008c\3\2\2\2\u01ce\u01cf\7^\2\2\u01cf\u01d3\t\17\2\2\u01d0\u01d3\5\u008f"+
		"H\2\u01d1\u01d3\5\u0091I\2\u01d2\u01ce\3\2\2\2\u01d2\u01d0\3\2\2\2\u01d2"+
		"\u01d1\3\2\2\2\u01d3\u008e\3\2\2\2\u01d4\u01d5\7^\2\2\u01d5\u01e0\5_\60"+
		"\2\u01d6\u01d7\7^\2\2\u01d7\u01d8\5_\60\2\u01d8\u01d9\5_\60\2\u01d9\u01e0"+
		"\3\2\2\2\u01da\u01db\7^\2\2\u01db\u01dc\5\u0093J\2\u01dc\u01dd\5_\60\2"+
		"\u01dd\u01de\5_\60\2\u01de\u01e0\3\2\2\2\u01df\u01d4\3\2\2\2\u01df\u01d6"+
		"\3\2\2\2\u01df\u01da\3\2\2\2\u01e0\u0090\3\2\2\2\u01e1\u01e2\7^\2\2\u01e2"+
		"\u01e3\7w\2\2\u01e3\u01e4\5W,\2\u01e4\u01e5\5W,\2\u01e5\u01e6\5W,\2\u01e6"+
		"\u01e7\5W,\2\u01e7\u0092\3\2\2\2\u01e8\u01e9\t\20\2\2\u01e9\u0094\3\2"+
		"\2\2\u01ea\u01eb\7p\2\2\u01eb\u01ec\7w\2\2\u01ec\u01ed\7n\2\2\u01ed\u01ee"+
		"\7n\2\2\u01ee\u0096\3\2\2\2\u01ef\u01f3\5\u0099M\2\u01f0\u01f2\5\u009b"+
		"N\2\u01f1\u01f0\3\2\2\2\u01f2\u01f5\3\2\2\2\u01f3\u01f1\3\2\2\2\u01f3"+
		"\u01f4\3\2\2\2\u01f4\u0098\3\2\2\2\u01f5\u01f3\3\2\2\2\u01f6\u01fd\t\21"+
		"\2\2\u01f7\u01f8\n\22\2\2\u01f8\u01fd\6M\2\2\u01f9\u01fa\t\23\2\2\u01fa"+
		"\u01fb\t\24\2\2\u01fb\u01fd\6M\3\2\u01fc\u01f6\3\2\2\2\u01fc\u01f7\3\2"+
		"\2\2\u01fc\u01f9\3\2\2\2\u01fd\u009a\3\2\2\2\u01fe\u0205\t\25\2\2\u01ff"+
		"\u0200\n\22\2\2\u0200\u0205\6N\4\2\u0201\u0202\t\23\2\2\u0202\u0203\t"+
		"\24\2\2\u0203\u0205\6N\5\2\u0204\u01fe\3\2\2\2\u0204\u01ff\3\2\2\2\u0204"+
		"\u0201\3\2\2\2\u0205\u009c\3\2\2\2\u0206\u0208\t\26\2\2\u0207\u0206\3"+
		"\2\2\2\u0208\u0209\3\2\2\2\u0209\u0207\3\2\2\2\u0209\u020a\3\2\2\2\u020a"+
		"\u020b\3\2\2\2\u020b\u020c\bO\2\2\u020c\u009e\3\2\2\2\u020d\u020e\7\61"+
		"\2\2\u020e\u020f\7,\2\2\u020f\u0213\3\2\2\2\u0210\u0212\13\2\2\2\u0211"+
		"\u0210\3\2\2\2\u0212\u0215\3\2\2\2\u0213\u0214\3\2\2\2\u0213\u0211\3\2"+
		"\2\2\u0214\u0216\3\2\2\2\u0215\u0213\3\2\2\2\u0216\u0217\7,\2\2\u0217"+
		"\u0218\7\61\2\2\u0218\u0219\3\2\2\2\u0219\u021a\bP\2\2\u021a\u00a0\3\2"+
		"\2\2\u021b\u021c\7\61\2\2\u021c\u021d\7\61\2\2\u021d\u0221\3\2\2\2\u021e"+
		"\u0220\n\27\2\2\u021f\u021e\3\2\2\2\u0220\u0223\3\2\2\2\u0221\u021f\3"+
		"\2\2\2\u0221\u0222\3\2\2\2\u0222\u0224\3\2\2\2\u0223\u0221\3\2\2\2\u0224"+
		"\u0225\bQ\2\2\u0225\u00a2\3\2\2\2\64\2\u00eb\u00ef\u00f3\u00f7\u00fb\u0102"+
		"\u0107\u0109\u010f\u0113\u0117\u011d\u0122\u012c\u0130\u0136\u013a\u0142"+
		"\u0146\u014c\u0156\u015a\u0160\u0164\u0169\u016c\u016f\u0174\u0177\u017c"+
		"\u0181\u0189\u0194\u0198\u019d\u01a1\u01b1\u01bb\u01c1\u01c8\u01cc\u01d2"+
		"\u01df\u01f3\u01fc\u0204\u0209\u0213\u0221\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}