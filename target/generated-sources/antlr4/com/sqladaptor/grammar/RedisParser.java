// Generated from com/sqladaptor/grammar/Redis.g4 by ANTLR 4.13.1
package com.sqladaptor.grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class RedisParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		GET=1, SET=2, HGET=3, CONFIG=4, DEL=5, EXISTS=6, EXPIRE=7, TTL=8, HSET=9, 
		HDEL=10, HGETALL=11, HKEYS=12, HVALS=13, LPUSH=14, RPUSH=15, LPOP=16, 
		RPOP=17, LLEN=18, LRANGE=19, SADD=20, SREM=21, SMEMBERS=22, SCARD=23, 
		SISMEMBER=24, ZADD=25, ZREM=26, ZRANGE=27, ZCARD=28, ZSCORE=29, PING=30, 
		ECHO=31, SELECT=32, FLUSHDB=33, FLUSHALL=34, INFO=35, DBSIZE=36, KEYS=37, 
		TYPE=38, CLIENT=39, INCR=40, DECR=41, INCRBY=42, DECRBY=43, APPEND=44, 
		STRLEN=45, GETRANGE=46, SETRANGE=47, MGET=48, MSET=49, MSETNX=50, PERSIST=51, 
		EXPIREAT=52, PEXPIRE=53, PEXPIREAT=54, PTTL=55, PLUS=56, MINUS=57, COLON=58, 
		DOLLAR=59, ASTERISK=60, MINUS_ONE=61, QUOTED_STRING=62, UNQUOTED_STRING=63, 
		IDENTIFIER=64, NUMBER=65, FLOAT=66, DIGIT=67, CHAR=68, SPECIAL=69, CRLF=70, 
		SPACE=71, WHITESPACE=72;
	public static final int
		RULE_command = 0, RULE_commandName = 1, RULE_stringLiteral = 2, RULE_numberLiteral = 3, 
		RULE_identifier = 4, RULE_argument = 5, RULE_commandKeyword = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"command", "commandName", "stringLiteral", "numberLiteral", "identifier", 
			"argument", "commandKeyword"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, "'+'", "'-'", "':'", 
			"'$'", "'*'", "'-1'", null, null, null, null, null, null, null, null, 
			null, "' '"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "GET", "SET", "HGET", "CONFIG", "DEL", "EXISTS", "EXPIRE", "TTL", 
			"HSET", "HDEL", "HGETALL", "HKEYS", "HVALS", "LPUSH", "RPUSH", "LPOP", 
			"RPOP", "LLEN", "LRANGE", "SADD", "SREM", "SMEMBERS", "SCARD", "SISMEMBER", 
			"ZADD", "ZREM", "ZRANGE", "ZCARD", "ZSCORE", "PING", "ECHO", "SELECT", 
			"FLUSHDB", "FLUSHALL", "INFO", "DBSIZE", "KEYS", "TYPE", "CLIENT", "INCR", 
			"DECR", "INCRBY", "DECRBY", "APPEND", "STRLEN", "GETRANGE", "SETRANGE", 
			"MGET", "MSET", "MSETNX", "PERSIST", "EXPIREAT", "PEXPIRE", "PEXPIREAT", 
			"PTTL", "PLUS", "MINUS", "COLON", "DOLLAR", "ASTERISK", "MINUS_ONE", 
			"QUOTED_STRING", "UNQUOTED_STRING", "IDENTIFIER", "NUMBER", "FLOAT", 
			"DIGIT", "CHAR", "SPECIAL", "CRLF", "SPACE", "WHITESPACE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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

	@Override
	public String getGrammarFileName() { return "Redis.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RedisParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CommandContext extends ParserRuleContext {
		public CommandNameContext commandName() {
			return getRuleContext(CommandNameContext.class,0);
		}
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public TerminalNode CRLF() { return getToken(RedisParser.CRLF, 0); }
		public CommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_command; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommandContext command() throws RecognitionException {
		CommandContext _localctx = new CommandContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_command);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			commandName();
			setState(18);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4539628424389459970L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 7L) != 0)) {
				{
				{
				setState(15);
				argument();
				}
				}
				setState(20);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(22);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CRLF) {
				{
				setState(21);
				match(CRLF);
				}
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

	@SuppressWarnings("CheckReturnValue")
	public static class CommandNameContext extends ParserRuleContext {
		public TerminalNode GET() { return getToken(RedisParser.GET, 0); }
		public TerminalNode SET() { return getToken(RedisParser.SET, 0); }
		public TerminalNode HGET() { return getToken(RedisParser.HGET, 0); }
		public TerminalNode CONFIG() { return getToken(RedisParser.CONFIG, 0); }
		public TerminalNode DEL() { return getToken(RedisParser.DEL, 0); }
		public TerminalNode EXISTS() { return getToken(RedisParser.EXISTS, 0); }
		public TerminalNode EXPIRE() { return getToken(RedisParser.EXPIRE, 0); }
		public TerminalNode TTL() { return getToken(RedisParser.TTL, 0); }
		public TerminalNode HSET() { return getToken(RedisParser.HSET, 0); }
		public TerminalNode HDEL() { return getToken(RedisParser.HDEL, 0); }
		public TerminalNode HGETALL() { return getToken(RedisParser.HGETALL, 0); }
		public TerminalNode HKEYS() { return getToken(RedisParser.HKEYS, 0); }
		public TerminalNode HVALS() { return getToken(RedisParser.HVALS, 0); }
		public TerminalNode LPUSH() { return getToken(RedisParser.LPUSH, 0); }
		public TerminalNode RPUSH() { return getToken(RedisParser.RPUSH, 0); }
		public TerminalNode LPOP() { return getToken(RedisParser.LPOP, 0); }
		public TerminalNode RPOP() { return getToken(RedisParser.RPOP, 0); }
		public TerminalNode LLEN() { return getToken(RedisParser.LLEN, 0); }
		public TerminalNode LRANGE() { return getToken(RedisParser.LRANGE, 0); }
		public TerminalNode SADD() { return getToken(RedisParser.SADD, 0); }
		public TerminalNode SREM() { return getToken(RedisParser.SREM, 0); }
		public TerminalNode SMEMBERS() { return getToken(RedisParser.SMEMBERS, 0); }
		public TerminalNode SCARD() { return getToken(RedisParser.SCARD, 0); }
		public TerminalNode SISMEMBER() { return getToken(RedisParser.SISMEMBER, 0); }
		public TerminalNode ZADD() { return getToken(RedisParser.ZADD, 0); }
		public TerminalNode ZREM() { return getToken(RedisParser.ZREM, 0); }
		public TerminalNode ZRANGE() { return getToken(RedisParser.ZRANGE, 0); }
		public TerminalNode ZCARD() { return getToken(RedisParser.ZCARD, 0); }
		public TerminalNode ZSCORE() { return getToken(RedisParser.ZSCORE, 0); }
		public TerminalNode PING() { return getToken(RedisParser.PING, 0); }
		public TerminalNode ECHO() { return getToken(RedisParser.ECHO, 0); }
		public TerminalNode SELECT() { return getToken(RedisParser.SELECT, 0); }
		public TerminalNode FLUSHDB() { return getToken(RedisParser.FLUSHDB, 0); }
		public TerminalNode FLUSHALL() { return getToken(RedisParser.FLUSHALL, 0); }
		public TerminalNode INFO() { return getToken(RedisParser.INFO, 0); }
		public TerminalNode DBSIZE() { return getToken(RedisParser.DBSIZE, 0); }
		public TerminalNode KEYS() { return getToken(RedisParser.KEYS, 0); }
		public TerminalNode TYPE() { return getToken(RedisParser.TYPE, 0); }
		public TerminalNode INCR() { return getToken(RedisParser.INCR, 0); }
		public TerminalNode DECR() { return getToken(RedisParser.DECR, 0); }
		public TerminalNode INCRBY() { return getToken(RedisParser.INCRBY, 0); }
		public TerminalNode DECRBY() { return getToken(RedisParser.DECRBY, 0); }
		public TerminalNode APPEND() { return getToken(RedisParser.APPEND, 0); }
		public TerminalNode STRLEN() { return getToken(RedisParser.STRLEN, 0); }
		public TerminalNode GETRANGE() { return getToken(RedisParser.GETRANGE, 0); }
		public TerminalNode SETRANGE() { return getToken(RedisParser.SETRANGE, 0); }
		public TerminalNode MGET() { return getToken(RedisParser.MGET, 0); }
		public TerminalNode MSET() { return getToken(RedisParser.MSET, 0); }
		public TerminalNode MSETNX() { return getToken(RedisParser.MSETNX, 0); }
		public TerminalNode PERSIST() { return getToken(RedisParser.PERSIST, 0); }
		public TerminalNode EXPIREAT() { return getToken(RedisParser.EXPIREAT, 0); }
		public TerminalNode PEXPIRE() { return getToken(RedisParser.PEXPIRE, 0); }
		public TerminalNode PEXPIREAT() { return getToken(RedisParser.PEXPIREAT, 0); }
		public TerminalNode PTTL() { return getToken(RedisParser.PTTL, 0); }
		public TerminalNode CLIENT() { return getToken(RedisParser.CLIENT, 0); }
		public CommandNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commandName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterCommandName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitCommandName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitCommandName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommandNameContext commandName() throws RecognitionException {
		CommandNameContext _localctx = new CommandNameContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_commandName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 72057594037927934L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	@SuppressWarnings("CheckReturnValue")
	public static class StringLiteralContext extends ParserRuleContext {
		public TerminalNode QUOTED_STRING() { return getToken(RedisParser.QUOTED_STRING, 0); }
		public TerminalNode UNQUOTED_STRING() { return getToken(RedisParser.UNQUOTED_STRING, 0); }
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_stringLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			_la = _input.LA(1);
			if ( !(_la==QUOTED_STRING || _la==UNQUOTED_STRING) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	@SuppressWarnings("CheckReturnValue")
	public static class NumberLiteralContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(RedisParser.NUMBER, 0); }
		public TerminalNode FLOAT() { return getToken(RedisParser.FLOAT, 0); }
		public NumberLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numberLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterNumberLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitNumberLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitNumberLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberLiteralContext numberLiteral() throws RecognitionException {
		NumberLiteralContext _localctx = new NumberLiteralContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_numberLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			_la = _input.LA(1);
			if ( !(_la==NUMBER || _la==FLOAT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	@SuppressWarnings("CheckReturnValue")
	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(RedisParser.IDENTIFIER, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			match(IDENTIFIER);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentContext extends ParserRuleContext {
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public NumberLiteralContext numberLiteral() {
			return getRuleContext(NumberLiteralContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public CommandKeywordContext commandKeyword() {
			return getRuleContext(CommandKeywordContext.class,0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_argument);
		try {
			setState(36);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QUOTED_STRING:
			case UNQUOTED_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(32);
				stringLiteral();
				}
				break;
			case NUMBER:
			case FLOAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(33);
				numberLiteral();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(34);
				identifier();
				}
				break;
			case GET:
			case SET:
			case HGET:
			case CONFIG:
			case DEL:
			case EXISTS:
			case EXPIRE:
			case TTL:
			case HSET:
			case HDEL:
			case HGETALL:
			case HKEYS:
			case HVALS:
			case LPUSH:
			case RPUSH:
			case LPOP:
			case RPOP:
			case LLEN:
			case LRANGE:
			case SADD:
			case SREM:
			case SMEMBERS:
			case SCARD:
			case SISMEMBER:
			case ZADD:
			case ZREM:
			case ZRANGE:
			case ZCARD:
			case ZSCORE:
			case PING:
			case ECHO:
			case SELECT:
			case FLUSHDB:
			case FLUSHALL:
			case INFO:
			case DBSIZE:
			case KEYS:
			case TYPE:
			case CLIENT:
			case INCR:
			case DECR:
			case INCRBY:
			case DECRBY:
			case APPEND:
			case STRLEN:
			case GETRANGE:
			case SETRANGE:
			case MGET:
			case MSET:
			case MSETNX:
			case PERSIST:
			case EXPIREAT:
			case PEXPIRE:
			case PEXPIREAT:
			case PTTL:
				enterOuterAlt(_localctx, 4);
				{
				setState(35);
				commandKeyword();
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

	@SuppressWarnings("CheckReturnValue")
	public static class CommandKeywordContext extends ParserRuleContext {
		public TerminalNode GET() { return getToken(RedisParser.GET, 0); }
		public TerminalNode SET() { return getToken(RedisParser.SET, 0); }
		public TerminalNode DEL() { return getToken(RedisParser.DEL, 0); }
		public TerminalNode HGET() { return getToken(RedisParser.HGET, 0); }
		public TerminalNode HSET() { return getToken(RedisParser.HSET, 0); }
		public TerminalNode CONFIG() { return getToken(RedisParser.CONFIG, 0); }
		public TerminalNode EXISTS() { return getToken(RedisParser.EXISTS, 0); }
		public TerminalNode EXPIRE() { return getToken(RedisParser.EXPIRE, 0); }
		public TerminalNode TTL() { return getToken(RedisParser.TTL, 0); }
		public TerminalNode HDEL() { return getToken(RedisParser.HDEL, 0); }
		public TerminalNode HGETALL() { return getToken(RedisParser.HGETALL, 0); }
		public TerminalNode HKEYS() { return getToken(RedisParser.HKEYS, 0); }
		public TerminalNode HVALS() { return getToken(RedisParser.HVALS, 0); }
		public TerminalNode LPUSH() { return getToken(RedisParser.LPUSH, 0); }
		public TerminalNode RPUSH() { return getToken(RedisParser.RPUSH, 0); }
		public TerminalNode LPOP() { return getToken(RedisParser.LPOP, 0); }
		public TerminalNode RPOP() { return getToken(RedisParser.RPOP, 0); }
		public TerminalNode LLEN() { return getToken(RedisParser.LLEN, 0); }
		public TerminalNode LRANGE() { return getToken(RedisParser.LRANGE, 0); }
		public TerminalNode SADD() { return getToken(RedisParser.SADD, 0); }
		public TerminalNode SREM() { return getToken(RedisParser.SREM, 0); }
		public TerminalNode SMEMBERS() { return getToken(RedisParser.SMEMBERS, 0); }
		public TerminalNode SCARD() { return getToken(RedisParser.SCARD, 0); }
		public TerminalNode SISMEMBER() { return getToken(RedisParser.SISMEMBER, 0); }
		public TerminalNode ZADD() { return getToken(RedisParser.ZADD, 0); }
		public TerminalNode ZREM() { return getToken(RedisParser.ZREM, 0); }
		public TerminalNode ZRANGE() { return getToken(RedisParser.ZRANGE, 0); }
		public TerminalNode ZCARD() { return getToken(RedisParser.ZCARD, 0); }
		public TerminalNode ZSCORE() { return getToken(RedisParser.ZSCORE, 0); }
		public TerminalNode PING() { return getToken(RedisParser.PING, 0); }
		public TerminalNode ECHO() { return getToken(RedisParser.ECHO, 0); }
		public TerminalNode SELECT() { return getToken(RedisParser.SELECT, 0); }
		public TerminalNode FLUSHDB() { return getToken(RedisParser.FLUSHDB, 0); }
		public TerminalNode FLUSHALL() { return getToken(RedisParser.FLUSHALL, 0); }
		public TerminalNode INFO() { return getToken(RedisParser.INFO, 0); }
		public TerminalNode DBSIZE() { return getToken(RedisParser.DBSIZE, 0); }
		public TerminalNode KEYS() { return getToken(RedisParser.KEYS, 0); }
		public TerminalNode TYPE() { return getToken(RedisParser.TYPE, 0); }
		public TerminalNode INCR() { return getToken(RedisParser.INCR, 0); }
		public TerminalNode DECR() { return getToken(RedisParser.DECR, 0); }
		public TerminalNode INCRBY() { return getToken(RedisParser.INCRBY, 0); }
		public TerminalNode DECRBY() { return getToken(RedisParser.DECRBY, 0); }
		public TerminalNode APPEND() { return getToken(RedisParser.APPEND, 0); }
		public TerminalNode STRLEN() { return getToken(RedisParser.STRLEN, 0); }
		public TerminalNode GETRANGE() { return getToken(RedisParser.GETRANGE, 0); }
		public TerminalNode SETRANGE() { return getToken(RedisParser.SETRANGE, 0); }
		public TerminalNode MGET() { return getToken(RedisParser.MGET, 0); }
		public TerminalNode MSET() { return getToken(RedisParser.MSET, 0); }
		public TerminalNode MSETNX() { return getToken(RedisParser.MSETNX, 0); }
		public TerminalNode PERSIST() { return getToken(RedisParser.PERSIST, 0); }
		public TerminalNode EXPIREAT() { return getToken(RedisParser.EXPIREAT, 0); }
		public TerminalNode PEXPIRE() { return getToken(RedisParser.PEXPIRE, 0); }
		public TerminalNode PEXPIREAT() { return getToken(RedisParser.PEXPIREAT, 0); }
		public TerminalNode PTTL() { return getToken(RedisParser.PTTL, 0); }
		public TerminalNode CLIENT() { return getToken(RedisParser.CLIENT, 0); }
		public CommandKeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commandKeyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterCommandKeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitCommandKeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitCommandKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommandKeywordContext commandKeyword() throws RecognitionException {
		CommandKeywordContext _localctx = new CommandKeywordContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_commandKeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 72057594037927934L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	public static final String _serializedATN =
		"\u0004\u0001H)\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002"+
		"\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005"+
		"\u0007\u0005\u0002\u0006\u0007\u0006\u0001\u0000\u0001\u0000\u0005\u0000"+
		"\u0011\b\u0000\n\u0000\f\u0000\u0014\t\u0000\u0001\u0000\u0003\u0000\u0017"+
		"\b\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0003\u0005%\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0000"+
		"\u0000\u0007\u0000\u0002\u0004\u0006\b\n\f\u0000\u0003\u0001\u0000\u0001"+
		"7\u0001\u0000>?\u0001\u0000AB&\u0000\u000e\u0001\u0000\u0000\u0000\u0002"+
		"\u0018\u0001\u0000\u0000\u0000\u0004\u001a\u0001\u0000\u0000\u0000\u0006"+
		"\u001c\u0001\u0000\u0000\u0000\b\u001e\u0001\u0000\u0000\u0000\n$\u0001"+
		"\u0000\u0000\u0000\f&\u0001\u0000\u0000\u0000\u000e\u0012\u0003\u0002"+
		"\u0001\u0000\u000f\u0011\u0003\n\u0005\u0000\u0010\u000f\u0001\u0000\u0000"+
		"\u0000\u0011\u0014\u0001\u0000\u0000\u0000\u0012\u0010\u0001\u0000\u0000"+
		"\u0000\u0012\u0013\u0001\u0000\u0000\u0000\u0013\u0016\u0001\u0000\u0000"+
		"\u0000\u0014\u0012\u0001\u0000\u0000\u0000\u0015\u0017\u0005F\u0000\u0000"+
		"\u0016\u0015\u0001\u0000\u0000\u0000\u0016\u0017\u0001\u0000\u0000\u0000"+
		"\u0017\u0001\u0001\u0000\u0000\u0000\u0018\u0019\u0007\u0000\u0000\u0000"+
		"\u0019\u0003\u0001\u0000\u0000\u0000\u001a\u001b\u0007\u0001\u0000\u0000"+
		"\u001b\u0005\u0001\u0000\u0000\u0000\u001c\u001d\u0007\u0002\u0000\u0000"+
		"\u001d\u0007\u0001\u0000\u0000\u0000\u001e\u001f\u0005@\u0000\u0000\u001f"+
		"\t\u0001\u0000\u0000\u0000 %\u0003\u0004\u0002\u0000!%\u0003\u0006\u0003"+
		"\u0000\"%\u0003\b\u0004\u0000#%\u0003\f\u0006\u0000$ \u0001\u0000\u0000"+
		"\u0000$!\u0001\u0000\u0000\u0000$\"\u0001\u0000\u0000\u0000$#\u0001\u0000"+
		"\u0000\u0000%\u000b\u0001\u0000\u0000\u0000&\'\u0007\u0000\u0000\u0000"+
		"\'\r\u0001\u0000\u0000\u0000\u0003\u0012\u0016$";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}