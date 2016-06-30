// Generated from QueryParser.g4 by ANTLR 4.4
package hydrograph.server.sqlquery.parser.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class QueryParserLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__20=1, T__19=2, T__18=3, T__17=4, T__16=5, T__15=6, T__14=7, T__13=8, 
		T__12=9, T__11=10, T__10=11, T__9=12, T__8=13, T__7=14, T__6=15, T__5=16, 
		T__4=17, T__3=18, T__2=19, T__1=20, T__0=21, Identifier=22, WS=23;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'"
	};
	public static final String[] ruleNames = {
		"T__20", "T__19", "T__18", "T__17", "T__16", "T__15", "T__14", "T__13", 
		"T__12", "T__11", "T__10", "T__9", "T__8", "T__7", "T__6", "T__5", "T__4", 
		"T__3", "T__2", "T__1", "T__0", "Identifier", "JavaLetterOrDigit", "WS"
	};


	public QueryParserLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "QueryParser.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\31\u009d\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4"+
		"\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3"+
		"\21\3\21\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3"+
		"\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\6"+
		"\27\u0091\n\27\r\27\16\27\u0092\3\30\3\30\3\31\6\31\u0098\n\31\r\31\16"+
		"\31\u0099\3\31\3\31\2\2\32\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\2\61\31"+
		"\3\2\4\t\2&\')).\60\62;C\\aac|\4\2\13\f\16\17\u009d\2\3\3\2\2\2\2\5\3"+
		"\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2"+
		"\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3"+
		"\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'"+
		"\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2\61\3\2\2\2\3\63\3\2\2\2\5"+
		":\3\2\2\2\7?\3\2\2\2\tD\3\2\2\2\13M\3\2\2\2\rT\3\2\2\2\17W\3\2\2\2\21"+
		"Y\3\2\2\2\23[\3\2\2\2\25]\3\2\2\2\27_\3\2\2\2\31g\3\2\2\2\33j\3\2\2\2"+
		"\35m\3\2\2\2\37p\3\2\2\2!s\3\2\2\2#v\3\2\2\2%x\3\2\2\2\'\u0080\3\2\2\2"+
		")\u0082\3\2\2\2+\u0086\3\2\2\2-\u0090\3\2\2\2/\u0094\3\2\2\2\61\u0097"+
		"\3\2\2\2\63\64\7P\2\2\64\65\7Q\2\2\65\66\7V\2\2\66\67\7\"\2\2\678\7K\2"+
		"\289\7P\2\29\4\3\2\2\2:;\7n\2\2;<\7k\2\2<=\7m\2\2=>\7g\2\2>\6\3\2\2\2"+
		"?@\7N\2\2@A\7K\2\2AB\7M\2\2BC\7G\2\2C\b\3\2\2\2DE\7p\2\2EF\7q\2\2FG\7"+
		"v\2\2GH\7\"\2\2HI\7n\2\2IJ\7k\2\2JK\7m\2\2KL\7g\2\2L\n\3\2\2\2MN\7p\2"+
		"\2NO\7q\2\2OP\7v\2\2PQ\7\"\2\2QR\7k\2\2RS\7p\2\2S\f\3\2\2\2TU\7@\2\2U"+
		"V\7?\2\2V\16\3\2\2\2WX\7=\2\2X\20\3\2\2\2YZ\7>\2\2Z\22\3\2\2\2[\\\7?\2"+
		"\2\\\24\3\2\2\2]^\7@\2\2^\26\3\2\2\2_`\7D\2\2`a\7G\2\2ab\7V\2\2bc\7Y\2"+
		"\2cd\7G\2\2de\7G\2\2ef\7P\2\2f\30\3\2\2\2gh\7q\2\2hi\7t\2\2i\32\3\2\2"+
		"\2jk\7>\2\2kl\7?\2\2l\34\3\2\2\2mn\7>\2\2no\7@\2\2o\36\3\2\2\2pq\7K\2"+
		"\2qr\7P\2\2r \3\2\2\2st\7k\2\2tu\7p\2\2u\"\3\2\2\2vw\7*\2\2w$\3\2\2\2"+
		"xy\7d\2\2yz\7g\2\2z{\7v\2\2{|\7y\2\2|}\7g\2\2}~\7g\2\2~\177\7p\2\2\177"+
		"&\3\2\2\2\u0080\u0081\7+\2\2\u0081(\3\2\2\2\u0082\u0083\7c\2\2\u0083\u0084"+
		"\7p\2\2\u0084\u0085\7f\2\2\u0085*\3\2\2\2\u0086\u0087\7P\2\2\u0087\u0088"+
		"\7Q\2\2\u0088\u0089\7V\2\2\u0089\u008a\7\"\2\2\u008a\u008b\7N\2\2\u008b"+
		"\u008c\7K\2\2\u008c\u008d\7M\2\2\u008d\u008e\7G\2\2\u008e,\3\2\2\2\u008f"+
		"\u0091\5/\30\2\u0090\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0090\3\2"+
		"\2\2\u0092\u0093\3\2\2\2\u0093.\3\2\2\2\u0094\u0095\t\2\2\2\u0095\60\3"+
		"\2\2\2\u0096\u0098\t\3\2\2\u0097\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099"+
		"\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009c\b\31"+
		"\2\2\u009c\62\3\2\2\2\5\2\u0092\u0099\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}