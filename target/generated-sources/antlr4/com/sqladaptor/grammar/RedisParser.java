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
		GET=1, SET=2, GETRANGE=3, GETSET=4, GETBIT=5, MGET=6, SETBIT=7, SETEX=8, 
		SETNX=9, SETRANGE=10, STRLEN=11, MSET=12, MSETNX=13, PSETEX=14, INCR=15, 
		INCRBY=16, INCRBYFLOAT=17, DECR=18, DECRBY=19, APPEND=20, HGET=21, HSET=22, 
		HDEL=23, HGETALL=24, HKEYS=25, HVALS=26, HEXISTS=27, HINCRBY=28, HLEN=29, 
		LPUSH=30, RPUSH=31, LPOP=32, RPOP=33, LLEN=34, LRANGE=35, BLPOP=36, BRPOP=37, 
		BRPOPLPUSH=38, LINDEX=39, LINSERT=40, LPUSHX=41, LREM=42, LSET=43, LTRIM=44, 
		RPOPLPUSH=45, RPUSHX=46, SADD=47, SREM=48, SMEMBERS=49, SCARD=50, SISMEMBER=51, 
		SINTER=52, SINTERSTORE=53, SUNION=54, SUNIONSTORE=55, SDIFF=56, SDIFFSTORE=57, 
		SMOVE=58, SPOP=59, SRANDMEMBER=60, SSCAN=61, ZADD=62, ZREM=63, ZRANGE=64, 
		ZCARD=65, ZSCORE=66, ZCOUNT=67, ZINCRBY=68, ZINTERSTORE=69, ZLEXCOUNT=70, 
		ZRANGEBYLEX=71, ZRANGEBYSCORE=72, ZRANK=73, ZREMRANGEBYLEX=74, ZREMRANGEBYRANK=75, 
		ZREMRANGEBYSCORE=76, ZREVRANGE=77, ZREVRANGEBYSCORE=78, ZREVRANK=79, ZUNIONSTORE=80, 
		ZSCAN=81, DEL=82, EXPIRE=83, TTL=84, PERSIST=85, TYPE=86, PEXPIRE=87, 
		PTTL=88, DUMP=89, EXISTS=90, EXPIREAT=91, PEXPIREAT=92, KEYS=93, MOVE=94, 
		RANDOMKEY=95, RENAME=96, RENAMENX=97, COMMAND=98, AUTH=99, PING=100, ECHO=101, 
		CLIENT=102, SELECT=103, QUIT=104, FLUSHDB=105, FLUSHALL=106, INFO=107, 
		CONFIG=108, DBSIZE=109, BGREWRITEAOF=110, BGSAVE=111, TIME=112, LASTSAVE=113, 
		MONITOR=114, ROLE=115, SAVE=116, SHUTDOWN=117, SLAVEOF=118, SLOWLOG=119, 
		SYNC=120, PSUBSCRIBE=121, PUBSUB=122, PUBLISH=123, PUNSUBSCRIBE=124, SUBSCRIBE=125, 
		UNSUBSCRIBE=126, DISCARD=127, EXEC=128, MULTI=129, UNWATCH=130, WATCH=131, 
		EVAL=132, EVALSHA=133, SCRIPT=134, GEOHASH=135, GEOPOS=136, GEODIST=137, 
		GEORADIUS=138, GEOADD=139, GEORADIUSBYMEMBER=140, PLUS=141, MINUS=142, 
		COLON=143, DOLLAR=144, ASTERISK=145, MINUS_ONE=146, QUOTED_STRING=147, 
		UNQUOTED_STRING=148, IDENTIFIER=149, NUMBER=150, FLOAT=151, DIGIT=152, 
		CHAR=153, CRLF=154, SPECIAL=155, WHITESPACE=156;
	public static final int
		RULE_command = 0, RULE_commandName = 1, RULE_stringCommand = 2, RULE_hashCommand = 3, 
		RULE_listCommand = 4, RULE_setCommand = 5, RULE_sortedSetCommand = 6, 
		RULE_keyCommand = 7, RULE_connectionCommand = 8, RULE_serverCommand = 9, 
		RULE_pubsubCommand = 10, RULE_transactionCommand = 11, RULE_scriptCommand = 12, 
		RULE_geoCommand = 13, RULE_stringLiteral = 14, RULE_numberLiteral = 15, 
		RULE_identifier = 16, RULE_commandKeyword = 17, RULE_argument = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"command", "commandName", "stringCommand", "hashCommand", "listCommand", 
			"setCommand", "sortedSetCommand", "keyCommand", "connectionCommand", 
			"serverCommand", "pubsubCommand", "transactionCommand", "scriptCommand", 
			"geoCommand", "stringLiteral", "numberLiteral", "identifier", "commandKeyword", 
			"argument"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "'+'", "'-'", "':'", 
			"'$'", "'*'", "'-1'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "GET", "SET", "GETRANGE", "GETSET", "GETBIT", "MGET", "SETBIT", 
			"SETEX", "SETNX", "SETRANGE", "STRLEN", "MSET", "MSETNX", "PSETEX", "INCR", 
			"INCRBY", "INCRBYFLOAT", "DECR", "DECRBY", "APPEND", "HGET", "HSET", 
			"HDEL", "HGETALL", "HKEYS", "HVALS", "HEXISTS", "HINCRBY", "HLEN", "LPUSH", 
			"RPUSH", "LPOP", "RPOP", "LLEN", "LRANGE", "BLPOP", "BRPOP", "BRPOPLPUSH", 
			"LINDEX", "LINSERT", "LPUSHX", "LREM", "LSET", "LTRIM", "RPOPLPUSH", 
			"RPUSHX", "SADD", "SREM", "SMEMBERS", "SCARD", "SISMEMBER", "SINTER", 
			"SINTERSTORE", "SUNION", "SUNIONSTORE", "SDIFF", "SDIFFSTORE", "SMOVE", 
			"SPOP", "SRANDMEMBER", "SSCAN", "ZADD", "ZREM", "ZRANGE", "ZCARD", "ZSCORE", 
			"ZCOUNT", "ZINCRBY", "ZINTERSTORE", "ZLEXCOUNT", "ZRANGEBYLEX", "ZRANGEBYSCORE", 
			"ZRANK", "ZREMRANGEBYLEX", "ZREMRANGEBYRANK", "ZREMRANGEBYSCORE", "ZREVRANGE", 
			"ZREVRANGEBYSCORE", "ZREVRANK", "ZUNIONSTORE", "ZSCAN", "DEL", "EXPIRE", 
			"TTL", "PERSIST", "TYPE", "PEXPIRE", "PTTL", "DUMP", "EXISTS", "EXPIREAT", 
			"PEXPIREAT", "KEYS", "MOVE", "RANDOMKEY", "RENAME", "RENAMENX", "COMMAND", 
			"AUTH", "PING", "ECHO", "CLIENT", "SELECT", "QUIT", "FLUSHDB", "FLUSHALL", 
			"INFO", "CONFIG", "DBSIZE", "BGREWRITEAOF", "BGSAVE", "TIME", "LASTSAVE", 
			"MONITOR", "ROLE", "SAVE", "SHUTDOWN", "SLAVEOF", "SLOWLOG", "SYNC", 
			"PSUBSCRIBE", "PUBSUB", "PUBLISH", "PUNSUBSCRIBE", "SUBSCRIBE", "UNSUBSCRIBE", 
			"DISCARD", "EXEC", "MULTI", "UNWATCH", "WATCH", "EVAL", "EVALSHA", "SCRIPT", 
			"GEOHASH", "GEOPOS", "GEODIST", "GEORADIUS", "GEOADD", "GEORADIUSBYMEMBER", 
			"PLUS", "MINUS", "COLON", "DOLLAR", "ASTERISK", "MINUS_ONE", "QUOTED_STRING", 
			"UNQUOTED_STRING", "IDENTIFIER", "NUMBER", "FLOAT", "DIGIT", "CHAR", 
			"CRLF", "SPECIAL", "WHITESPACE"
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
			setState(38);
			commandName();
			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 16261119L) != 0)) {
				{
				{
				setState(39);
				argument();
				}
				}
				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CRLF) {
				{
				setState(45);
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
		public StringCommandContext stringCommand() {
			return getRuleContext(StringCommandContext.class,0);
		}
		public HashCommandContext hashCommand() {
			return getRuleContext(HashCommandContext.class,0);
		}
		public ListCommandContext listCommand() {
			return getRuleContext(ListCommandContext.class,0);
		}
		public SetCommandContext setCommand() {
			return getRuleContext(SetCommandContext.class,0);
		}
		public SortedSetCommandContext sortedSetCommand() {
			return getRuleContext(SortedSetCommandContext.class,0);
		}
		public KeyCommandContext keyCommand() {
			return getRuleContext(KeyCommandContext.class,0);
		}
		public ConnectionCommandContext connectionCommand() {
			return getRuleContext(ConnectionCommandContext.class,0);
		}
		public ServerCommandContext serverCommand() {
			return getRuleContext(ServerCommandContext.class,0);
		}
		public PubsubCommandContext pubsubCommand() {
			return getRuleContext(PubsubCommandContext.class,0);
		}
		public TransactionCommandContext transactionCommand() {
			return getRuleContext(TransactionCommandContext.class,0);
		}
		public ScriptCommandContext scriptCommand() {
			return getRuleContext(ScriptCommandContext.class,0);
		}
		public GeoCommandContext geoCommand() {
			return getRuleContext(GeoCommandContext.class,0);
		}
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
		try {
			setState(60);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GET:
			case SET:
			case GETRANGE:
			case GETSET:
			case GETBIT:
			case MGET:
			case SETBIT:
			case SETEX:
			case SETNX:
			case SETRANGE:
			case STRLEN:
			case MSET:
			case MSETNX:
			case PSETEX:
			case INCR:
			case INCRBY:
			case INCRBYFLOAT:
			case DECR:
			case DECRBY:
			case APPEND:
				enterOuterAlt(_localctx, 1);
				{
				setState(48);
				stringCommand();
				}
				break;
			case HGET:
			case HSET:
			case HDEL:
			case HGETALL:
			case HKEYS:
			case HVALS:
			case HEXISTS:
			case HINCRBY:
			case HLEN:
				enterOuterAlt(_localctx, 2);
				{
				setState(49);
				hashCommand();
				}
				break;
			case LPUSH:
			case RPUSH:
			case LPOP:
			case RPOP:
			case LLEN:
			case LRANGE:
			case BLPOP:
			case BRPOP:
			case BRPOPLPUSH:
			case LINDEX:
			case LINSERT:
			case LPUSHX:
			case LREM:
			case LSET:
			case LTRIM:
			case RPOPLPUSH:
			case RPUSHX:
				enterOuterAlt(_localctx, 3);
				{
				setState(50);
				listCommand();
				}
				break;
			case SADD:
			case SREM:
			case SMEMBERS:
			case SCARD:
			case SISMEMBER:
			case SINTER:
			case SINTERSTORE:
			case SUNION:
			case SUNIONSTORE:
			case SDIFF:
			case SDIFFSTORE:
			case SMOVE:
			case SPOP:
			case SRANDMEMBER:
			case SSCAN:
				enterOuterAlt(_localctx, 4);
				{
				setState(51);
				setCommand();
				}
				break;
			case ZADD:
			case ZREM:
			case ZRANGE:
			case ZCARD:
			case ZSCORE:
			case ZCOUNT:
			case ZINCRBY:
			case ZINTERSTORE:
			case ZLEXCOUNT:
			case ZRANGEBYLEX:
			case ZRANGEBYSCORE:
			case ZRANK:
			case ZREMRANGEBYLEX:
			case ZREMRANGEBYRANK:
			case ZREMRANGEBYSCORE:
			case ZREVRANGE:
			case ZREVRANGEBYSCORE:
			case ZREVRANK:
			case ZUNIONSTORE:
			case ZSCAN:
				enterOuterAlt(_localctx, 5);
				{
				setState(52);
				sortedSetCommand();
				}
				break;
			case DEL:
			case EXPIRE:
			case TTL:
			case PERSIST:
			case TYPE:
			case PEXPIRE:
			case PTTL:
			case DUMP:
			case EXISTS:
			case EXPIREAT:
			case PEXPIREAT:
			case KEYS:
			case MOVE:
			case RANDOMKEY:
			case RENAME:
			case RENAMENX:
				enterOuterAlt(_localctx, 6);
				{
				setState(53);
				keyCommand();
				}
				break;
			case COMMAND:
			case AUTH:
			case PING:
			case ECHO:
			case CLIENT:
			case SELECT:
			case QUIT:
				enterOuterAlt(_localctx, 7);
				{
				setState(54);
				connectionCommand();
				}
				break;
			case FLUSHDB:
			case FLUSHALL:
			case INFO:
			case CONFIG:
			case DBSIZE:
			case BGREWRITEAOF:
			case BGSAVE:
			case TIME:
			case LASTSAVE:
			case MONITOR:
			case ROLE:
			case SAVE:
			case SHUTDOWN:
			case SLAVEOF:
			case SLOWLOG:
			case SYNC:
				enterOuterAlt(_localctx, 8);
				{
				setState(55);
				serverCommand();
				}
				break;
			case PSUBSCRIBE:
			case PUBSUB:
			case PUBLISH:
			case PUNSUBSCRIBE:
			case SUBSCRIBE:
			case UNSUBSCRIBE:
				enterOuterAlt(_localctx, 9);
				{
				setState(56);
				pubsubCommand();
				}
				break;
			case DISCARD:
			case EXEC:
			case MULTI:
			case UNWATCH:
			case WATCH:
				enterOuterAlt(_localctx, 10);
				{
				setState(57);
				transactionCommand();
				}
				break;
			case EVAL:
			case EVALSHA:
			case SCRIPT:
				enterOuterAlt(_localctx, 11);
				{
				setState(58);
				scriptCommand();
				}
				break;
			case GEOHASH:
			case GEOPOS:
			case GEODIST:
			case GEORADIUS:
			case GEOADD:
			case GEORADIUSBYMEMBER:
				enterOuterAlt(_localctx, 12);
				{
				setState(59);
				geoCommand();
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
	public static class StringCommandContext extends ParserRuleContext {
		public TerminalNode GET() { return getToken(RedisParser.GET, 0); }
		public TerminalNode SET() { return getToken(RedisParser.SET, 0); }
		public TerminalNode GETRANGE() { return getToken(RedisParser.GETRANGE, 0); }
		public TerminalNode GETSET() { return getToken(RedisParser.GETSET, 0); }
		public TerminalNode GETBIT() { return getToken(RedisParser.GETBIT, 0); }
		public TerminalNode MGET() { return getToken(RedisParser.MGET, 0); }
		public TerminalNode SETBIT() { return getToken(RedisParser.SETBIT, 0); }
		public TerminalNode SETEX() { return getToken(RedisParser.SETEX, 0); }
		public TerminalNode SETNX() { return getToken(RedisParser.SETNX, 0); }
		public TerminalNode SETRANGE() { return getToken(RedisParser.SETRANGE, 0); }
		public TerminalNode STRLEN() { return getToken(RedisParser.STRLEN, 0); }
		public TerminalNode MSET() { return getToken(RedisParser.MSET, 0); }
		public TerminalNode MSETNX() { return getToken(RedisParser.MSETNX, 0); }
		public TerminalNode PSETEX() { return getToken(RedisParser.PSETEX, 0); }
		public TerminalNode INCR() { return getToken(RedisParser.INCR, 0); }
		public TerminalNode INCRBY() { return getToken(RedisParser.INCRBY, 0); }
		public TerminalNode INCRBYFLOAT() { return getToken(RedisParser.INCRBYFLOAT, 0); }
		public TerminalNode DECR() { return getToken(RedisParser.DECR, 0); }
		public TerminalNode DECRBY() { return getToken(RedisParser.DECRBY, 0); }
		public TerminalNode APPEND() { return getToken(RedisParser.APPEND, 0); }
		public StringCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterStringCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitStringCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitStringCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringCommandContext stringCommand() throws RecognitionException {
		StringCommandContext _localctx = new StringCommandContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_stringCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2097150L) != 0)) ) {
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
	public static class HashCommandContext extends ParserRuleContext {
		public TerminalNode HGET() { return getToken(RedisParser.HGET, 0); }
		public TerminalNode HSET() { return getToken(RedisParser.HSET, 0); }
		public TerminalNode HDEL() { return getToken(RedisParser.HDEL, 0); }
		public TerminalNode HGETALL() { return getToken(RedisParser.HGETALL, 0); }
		public TerminalNode HKEYS() { return getToken(RedisParser.HKEYS, 0); }
		public TerminalNode HVALS() { return getToken(RedisParser.HVALS, 0); }
		public TerminalNode HEXISTS() { return getToken(RedisParser.HEXISTS, 0); }
		public TerminalNode HINCRBY() { return getToken(RedisParser.HINCRBY, 0); }
		public TerminalNode HLEN() { return getToken(RedisParser.HLEN, 0); }
		public HashCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hashCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterHashCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitHashCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitHashCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HashCommandContext hashCommand() throws RecognitionException {
		HashCommandContext _localctx = new HashCommandContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_hashCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1071644672L) != 0)) ) {
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
	public static class ListCommandContext extends ParserRuleContext {
		public TerminalNode LPUSH() { return getToken(RedisParser.LPUSH, 0); }
		public TerminalNode RPUSH() { return getToken(RedisParser.RPUSH, 0); }
		public TerminalNode LPOP() { return getToken(RedisParser.LPOP, 0); }
		public TerminalNode RPOP() { return getToken(RedisParser.RPOP, 0); }
		public TerminalNode LLEN() { return getToken(RedisParser.LLEN, 0); }
		public TerminalNode LRANGE() { return getToken(RedisParser.LRANGE, 0); }
		public TerminalNode BLPOP() { return getToken(RedisParser.BLPOP, 0); }
		public TerminalNode BRPOP() { return getToken(RedisParser.BRPOP, 0); }
		public TerminalNode BRPOPLPUSH() { return getToken(RedisParser.BRPOPLPUSH, 0); }
		public TerminalNode LINDEX() { return getToken(RedisParser.LINDEX, 0); }
		public TerminalNode LINSERT() { return getToken(RedisParser.LINSERT, 0); }
		public TerminalNode LPUSHX() { return getToken(RedisParser.LPUSHX, 0); }
		public TerminalNode LREM() { return getToken(RedisParser.LREM, 0); }
		public TerminalNode LSET() { return getToken(RedisParser.LSET, 0); }
		public TerminalNode LTRIM() { return getToken(RedisParser.LTRIM, 0); }
		public TerminalNode RPOPLPUSH() { return getToken(RedisParser.RPOPLPUSH, 0); }
		public TerminalNode RPUSHX() { return getToken(RedisParser.RPUSHX, 0); }
		public ListCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterListCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitListCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitListCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListCommandContext listCommand() throws RecognitionException {
		ListCommandContext _localctx = new ListCommandContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_listCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 140736414613504L) != 0)) ) {
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
	public static class SetCommandContext extends ParserRuleContext {
		public TerminalNode SADD() { return getToken(RedisParser.SADD, 0); }
		public TerminalNode SREM() { return getToken(RedisParser.SREM, 0); }
		public TerminalNode SMEMBERS() { return getToken(RedisParser.SMEMBERS, 0); }
		public TerminalNode SCARD() { return getToken(RedisParser.SCARD, 0); }
		public TerminalNode SISMEMBER() { return getToken(RedisParser.SISMEMBER, 0); }
		public TerminalNode SINTER() { return getToken(RedisParser.SINTER, 0); }
		public TerminalNode SINTERSTORE() { return getToken(RedisParser.SINTERSTORE, 0); }
		public TerminalNode SUNION() { return getToken(RedisParser.SUNION, 0); }
		public TerminalNode SUNIONSTORE() { return getToken(RedisParser.SUNIONSTORE, 0); }
		public TerminalNode SDIFF() { return getToken(RedisParser.SDIFF, 0); }
		public TerminalNode SDIFFSTORE() { return getToken(RedisParser.SDIFFSTORE, 0); }
		public TerminalNode SMOVE() { return getToken(RedisParser.SMOVE, 0); }
		public TerminalNode SPOP() { return getToken(RedisParser.SPOP, 0); }
		public TerminalNode SRANDMEMBER() { return getToken(RedisParser.SRANDMEMBER, 0); }
		public TerminalNode SSCAN() { return getToken(RedisParser.SSCAN, 0); }
		public SetCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterSetCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitSetCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitSetCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetCommandContext setCommand() throws RecognitionException {
		SetCommandContext _localctx = new SetCommandContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_setCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4611545280939032576L) != 0)) ) {
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
	public static class SortedSetCommandContext extends ParserRuleContext {
		public TerminalNode ZADD() { return getToken(RedisParser.ZADD, 0); }
		public TerminalNode ZREM() { return getToken(RedisParser.ZREM, 0); }
		public TerminalNode ZRANGE() { return getToken(RedisParser.ZRANGE, 0); }
		public TerminalNode ZCARD() { return getToken(RedisParser.ZCARD, 0); }
		public TerminalNode ZSCORE() { return getToken(RedisParser.ZSCORE, 0); }
		public TerminalNode ZCOUNT() { return getToken(RedisParser.ZCOUNT, 0); }
		public TerminalNode ZINCRBY() { return getToken(RedisParser.ZINCRBY, 0); }
		public TerminalNode ZINTERSTORE() { return getToken(RedisParser.ZINTERSTORE, 0); }
		public TerminalNode ZLEXCOUNT() { return getToken(RedisParser.ZLEXCOUNT, 0); }
		public TerminalNode ZRANGEBYLEX() { return getToken(RedisParser.ZRANGEBYLEX, 0); }
		public TerminalNode ZRANGEBYSCORE() { return getToken(RedisParser.ZRANGEBYSCORE, 0); }
		public TerminalNode ZRANK() { return getToken(RedisParser.ZRANK, 0); }
		public TerminalNode ZREMRANGEBYLEX() { return getToken(RedisParser.ZREMRANGEBYLEX, 0); }
		public TerminalNode ZREMRANGEBYRANK() { return getToken(RedisParser.ZREMRANGEBYRANK, 0); }
		public TerminalNode ZREMRANGEBYSCORE() { return getToken(RedisParser.ZREMRANGEBYSCORE, 0); }
		public TerminalNode ZREVRANGE() { return getToken(RedisParser.ZREVRANGE, 0); }
		public TerminalNode ZREVRANGEBYSCORE() { return getToken(RedisParser.ZREVRANGEBYSCORE, 0); }
		public TerminalNode ZREVRANK() { return getToken(RedisParser.ZREVRANK, 0); }
		public TerminalNode ZUNIONSTORE() { return getToken(RedisParser.ZUNIONSTORE, 0); }
		public TerminalNode ZSCAN() { return getToken(RedisParser.ZSCAN, 0); }
		public SortedSetCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sortedSetCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterSortedSetCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitSortedSetCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitSortedSetCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SortedSetCommandContext sortedSetCommand() throws RecognitionException {
		SortedSetCommandContext _localctx = new SortedSetCommandContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_sortedSetCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
			_la = _input.LA(1);
			if ( !(((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & 1048575L) != 0)) ) {
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
	public static class KeyCommandContext extends ParserRuleContext {
		public TerminalNode DEL() { return getToken(RedisParser.DEL, 0); }
		public TerminalNode EXPIRE() { return getToken(RedisParser.EXPIRE, 0); }
		public TerminalNode TTL() { return getToken(RedisParser.TTL, 0); }
		public TerminalNode PERSIST() { return getToken(RedisParser.PERSIST, 0); }
		public TerminalNode TYPE() { return getToken(RedisParser.TYPE, 0); }
		public TerminalNode PEXPIRE() { return getToken(RedisParser.PEXPIRE, 0); }
		public TerminalNode PTTL() { return getToken(RedisParser.PTTL, 0); }
		public TerminalNode DUMP() { return getToken(RedisParser.DUMP, 0); }
		public TerminalNode EXISTS() { return getToken(RedisParser.EXISTS, 0); }
		public TerminalNode EXPIREAT() { return getToken(RedisParser.EXPIREAT, 0); }
		public TerminalNode PEXPIREAT() { return getToken(RedisParser.PEXPIREAT, 0); }
		public TerminalNode KEYS() { return getToken(RedisParser.KEYS, 0); }
		public TerminalNode MOVE() { return getToken(RedisParser.MOVE, 0); }
		public TerminalNode RANDOMKEY() { return getToken(RedisParser.RANDOMKEY, 0); }
		public TerminalNode RENAME() { return getToken(RedisParser.RENAME, 0); }
		public TerminalNode RENAMENX() { return getToken(RedisParser.RENAMENX, 0); }
		public KeyCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterKeyCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitKeyCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitKeyCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyCommandContext keyCommand() throws RecognitionException {
		KeyCommandContext _localctx = new KeyCommandContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_keyCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			_la = _input.LA(1);
			if ( !(((((_la - 82)) & ~0x3f) == 0 && ((1L << (_la - 82)) & 65535L) != 0)) ) {
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
	public static class ConnectionCommandContext extends ParserRuleContext {
		public TerminalNode COMMAND() { return getToken(RedisParser.COMMAND, 0); }
		public TerminalNode AUTH() { return getToken(RedisParser.AUTH, 0); }
		public TerminalNode PING() { return getToken(RedisParser.PING, 0); }
		public TerminalNode ECHO() { return getToken(RedisParser.ECHO, 0); }
		public TerminalNode CLIENT() { return getToken(RedisParser.CLIENT, 0); }
		public TerminalNode SELECT() { return getToken(RedisParser.SELECT, 0); }
		public TerminalNode QUIT() { return getToken(RedisParser.QUIT, 0); }
		public ConnectionCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_connectionCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterConnectionCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitConnectionCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitConnectionCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConnectionCommandContext connectionCommand() throws RecognitionException {
		ConnectionCommandContext _localctx = new ConnectionCommandContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_connectionCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			_la = _input.LA(1);
			if ( !(((((_la - 98)) & ~0x3f) == 0 && ((1L << (_la - 98)) & 127L) != 0)) ) {
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
	public static class ServerCommandContext extends ParserRuleContext {
		public TerminalNode FLUSHDB() { return getToken(RedisParser.FLUSHDB, 0); }
		public TerminalNode FLUSHALL() { return getToken(RedisParser.FLUSHALL, 0); }
		public TerminalNode INFO() { return getToken(RedisParser.INFO, 0); }
		public TerminalNode CONFIG() { return getToken(RedisParser.CONFIG, 0); }
		public TerminalNode DBSIZE() { return getToken(RedisParser.DBSIZE, 0); }
		public TerminalNode BGREWRITEAOF() { return getToken(RedisParser.BGREWRITEAOF, 0); }
		public TerminalNode BGSAVE() { return getToken(RedisParser.BGSAVE, 0); }
		public TerminalNode TIME() { return getToken(RedisParser.TIME, 0); }
		public TerminalNode LASTSAVE() { return getToken(RedisParser.LASTSAVE, 0); }
		public TerminalNode MONITOR() { return getToken(RedisParser.MONITOR, 0); }
		public TerminalNode ROLE() { return getToken(RedisParser.ROLE, 0); }
		public TerminalNode SAVE() { return getToken(RedisParser.SAVE, 0); }
		public TerminalNode SHUTDOWN() { return getToken(RedisParser.SHUTDOWN, 0); }
		public TerminalNode SLAVEOF() { return getToken(RedisParser.SLAVEOF, 0); }
		public TerminalNode SLOWLOG() { return getToken(RedisParser.SLOWLOG, 0); }
		public TerminalNode SYNC() { return getToken(RedisParser.SYNC, 0); }
		public ServerCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_serverCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterServerCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitServerCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitServerCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ServerCommandContext serverCommand() throws RecognitionException {
		ServerCommandContext _localctx = new ServerCommandContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_serverCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			_la = _input.LA(1);
			if ( !(((((_la - 105)) & ~0x3f) == 0 && ((1L << (_la - 105)) & 65535L) != 0)) ) {
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
	public static class PubsubCommandContext extends ParserRuleContext {
		public TerminalNode PSUBSCRIBE() { return getToken(RedisParser.PSUBSCRIBE, 0); }
		public TerminalNode PUBSUB() { return getToken(RedisParser.PUBSUB, 0); }
		public TerminalNode PUBLISH() { return getToken(RedisParser.PUBLISH, 0); }
		public TerminalNode PUNSUBSCRIBE() { return getToken(RedisParser.PUNSUBSCRIBE, 0); }
		public TerminalNode SUBSCRIBE() { return getToken(RedisParser.SUBSCRIBE, 0); }
		public TerminalNode UNSUBSCRIBE() { return getToken(RedisParser.UNSUBSCRIBE, 0); }
		public PubsubCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pubsubCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterPubsubCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitPubsubCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitPubsubCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PubsubCommandContext pubsubCommand() throws RecognitionException {
		PubsubCommandContext _localctx = new PubsubCommandContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_pubsubCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			_la = _input.LA(1);
			if ( !(((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & 63L) != 0)) ) {
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
	public static class TransactionCommandContext extends ParserRuleContext {
		public TerminalNode DISCARD() { return getToken(RedisParser.DISCARD, 0); }
		public TerminalNode EXEC() { return getToken(RedisParser.EXEC, 0); }
		public TerminalNode MULTI() { return getToken(RedisParser.MULTI, 0); }
		public TerminalNode UNWATCH() { return getToken(RedisParser.UNWATCH, 0); }
		public TerminalNode WATCH() { return getToken(RedisParser.WATCH, 0); }
		public TransactionCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transactionCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterTransactionCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitTransactionCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitTransactionCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransactionCommandContext transactionCommand() throws RecognitionException {
		TransactionCommandContext _localctx = new TransactionCommandContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_transactionCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			_la = _input.LA(1);
			if ( !(((((_la - 127)) & ~0x3f) == 0 && ((1L << (_la - 127)) & 31L) != 0)) ) {
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
	public static class ScriptCommandContext extends ParserRuleContext {
		public TerminalNode EVAL() { return getToken(RedisParser.EVAL, 0); }
		public TerminalNode EVALSHA() { return getToken(RedisParser.EVALSHA, 0); }
		public TerminalNode SCRIPT() { return getToken(RedisParser.SCRIPT, 0); }
		public ScriptCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scriptCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterScriptCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitScriptCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitScriptCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScriptCommandContext scriptCommand() throws RecognitionException {
		ScriptCommandContext _localctx = new ScriptCommandContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_scriptCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			_la = _input.LA(1);
			if ( !(((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & 7L) != 0)) ) {
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
	public static class GeoCommandContext extends ParserRuleContext {
		public TerminalNode GEOHASH() { return getToken(RedisParser.GEOHASH, 0); }
		public TerminalNode GEOPOS() { return getToken(RedisParser.GEOPOS, 0); }
		public TerminalNode GEODIST() { return getToken(RedisParser.GEODIST, 0); }
		public TerminalNode GEORADIUS() { return getToken(RedisParser.GEORADIUS, 0); }
		public TerminalNode GEOADD() { return getToken(RedisParser.GEOADD, 0); }
		public TerminalNode GEORADIUSBYMEMBER() { return getToken(RedisParser.GEORADIUSBYMEMBER, 0); }
		public GeoCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_geoCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).enterGeoCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RedisListener ) ((RedisListener)listener).exitGeoCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RedisVisitor ) return ((RedisVisitor<? extends T>)visitor).visitGeoCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GeoCommandContext geoCommand() throws RecognitionException {
		GeoCommandContext _localctx = new GeoCommandContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_geoCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			_la = _input.LA(1);
			if ( !(((((_la - 135)) & ~0x3f) == 0 && ((1L << (_la - 135)) & 63L) != 0)) ) {
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
		enterRule(_localctx, 28, RULE_stringLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
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
		enterRule(_localctx, 30, RULE_numberLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
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
		enterRule(_localctx, 32, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
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
	public static class CommandKeywordContext extends ParserRuleContext {
		public StringCommandContext stringCommand() {
			return getRuleContext(StringCommandContext.class,0);
		}
		public HashCommandContext hashCommand() {
			return getRuleContext(HashCommandContext.class,0);
		}
		public ListCommandContext listCommand() {
			return getRuleContext(ListCommandContext.class,0);
		}
		public SetCommandContext setCommand() {
			return getRuleContext(SetCommandContext.class,0);
		}
		public SortedSetCommandContext sortedSetCommand() {
			return getRuleContext(SortedSetCommandContext.class,0);
		}
		public KeyCommandContext keyCommand() {
			return getRuleContext(KeyCommandContext.class,0);
		}
		public ConnectionCommandContext connectionCommand() {
			return getRuleContext(ConnectionCommandContext.class,0);
		}
		public ServerCommandContext serverCommand() {
			return getRuleContext(ServerCommandContext.class,0);
		}
		public PubsubCommandContext pubsubCommand() {
			return getRuleContext(PubsubCommandContext.class,0);
		}
		public TransactionCommandContext transactionCommand() {
			return getRuleContext(TransactionCommandContext.class,0);
		}
		public ScriptCommandContext scriptCommand() {
			return getRuleContext(ScriptCommandContext.class,0);
		}
		public GeoCommandContext geoCommand() {
			return getRuleContext(GeoCommandContext.class,0);
		}
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
		enterRule(_localctx, 34, RULE_commandKeyword);
		try {
			setState(104);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GET:
			case SET:
			case GETRANGE:
			case GETSET:
			case GETBIT:
			case MGET:
			case SETBIT:
			case SETEX:
			case SETNX:
			case SETRANGE:
			case STRLEN:
			case MSET:
			case MSETNX:
			case PSETEX:
			case INCR:
			case INCRBY:
			case INCRBYFLOAT:
			case DECR:
			case DECRBY:
			case APPEND:
				enterOuterAlt(_localctx, 1);
				{
				setState(92);
				stringCommand();
				}
				break;
			case HGET:
			case HSET:
			case HDEL:
			case HGETALL:
			case HKEYS:
			case HVALS:
			case HEXISTS:
			case HINCRBY:
			case HLEN:
				enterOuterAlt(_localctx, 2);
				{
				setState(93);
				hashCommand();
				}
				break;
			case LPUSH:
			case RPUSH:
			case LPOP:
			case RPOP:
			case LLEN:
			case LRANGE:
			case BLPOP:
			case BRPOP:
			case BRPOPLPUSH:
			case LINDEX:
			case LINSERT:
			case LPUSHX:
			case LREM:
			case LSET:
			case LTRIM:
			case RPOPLPUSH:
			case RPUSHX:
				enterOuterAlt(_localctx, 3);
				{
				setState(94);
				listCommand();
				}
				break;
			case SADD:
			case SREM:
			case SMEMBERS:
			case SCARD:
			case SISMEMBER:
			case SINTER:
			case SINTERSTORE:
			case SUNION:
			case SUNIONSTORE:
			case SDIFF:
			case SDIFFSTORE:
			case SMOVE:
			case SPOP:
			case SRANDMEMBER:
			case SSCAN:
				enterOuterAlt(_localctx, 4);
				{
				setState(95);
				setCommand();
				}
				break;
			case ZADD:
			case ZREM:
			case ZRANGE:
			case ZCARD:
			case ZSCORE:
			case ZCOUNT:
			case ZINCRBY:
			case ZINTERSTORE:
			case ZLEXCOUNT:
			case ZRANGEBYLEX:
			case ZRANGEBYSCORE:
			case ZRANK:
			case ZREMRANGEBYLEX:
			case ZREMRANGEBYRANK:
			case ZREMRANGEBYSCORE:
			case ZREVRANGE:
			case ZREVRANGEBYSCORE:
			case ZREVRANK:
			case ZUNIONSTORE:
			case ZSCAN:
				enterOuterAlt(_localctx, 5);
				{
				setState(96);
				sortedSetCommand();
				}
				break;
			case DEL:
			case EXPIRE:
			case TTL:
			case PERSIST:
			case TYPE:
			case PEXPIRE:
			case PTTL:
			case DUMP:
			case EXISTS:
			case EXPIREAT:
			case PEXPIREAT:
			case KEYS:
			case MOVE:
			case RANDOMKEY:
			case RENAME:
			case RENAMENX:
				enterOuterAlt(_localctx, 6);
				{
				setState(97);
				keyCommand();
				}
				break;
			case COMMAND:
			case AUTH:
			case PING:
			case ECHO:
			case CLIENT:
			case SELECT:
			case QUIT:
				enterOuterAlt(_localctx, 7);
				{
				setState(98);
				connectionCommand();
				}
				break;
			case FLUSHDB:
			case FLUSHALL:
			case INFO:
			case CONFIG:
			case DBSIZE:
			case BGREWRITEAOF:
			case BGSAVE:
			case TIME:
			case LASTSAVE:
			case MONITOR:
			case ROLE:
			case SAVE:
			case SHUTDOWN:
			case SLAVEOF:
			case SLOWLOG:
			case SYNC:
				enterOuterAlt(_localctx, 8);
				{
				setState(99);
				serverCommand();
				}
				break;
			case PSUBSCRIBE:
			case PUBSUB:
			case PUBLISH:
			case PUNSUBSCRIBE:
			case SUBSCRIBE:
			case UNSUBSCRIBE:
				enterOuterAlt(_localctx, 9);
				{
				setState(100);
				pubsubCommand();
				}
				break;
			case DISCARD:
			case EXEC:
			case MULTI:
			case UNWATCH:
			case WATCH:
				enterOuterAlt(_localctx, 10);
				{
				setState(101);
				transactionCommand();
				}
				break;
			case EVAL:
			case EVALSHA:
			case SCRIPT:
				enterOuterAlt(_localctx, 11);
				{
				setState(102);
				scriptCommand();
				}
				break;
			case GEOHASH:
			case GEOPOS:
			case GEODIST:
			case GEORADIUS:
			case GEOADD:
			case GEORADIUSBYMEMBER:
				enterOuterAlt(_localctx, 12);
				{
				setState(103);
				geoCommand();
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
		enterRule(_localctx, 36, RULE_argument);
		try {
			setState(110);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QUOTED_STRING:
			case UNQUOTED_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(106);
				stringLiteral();
				}
				break;
			case NUMBER:
			case FLOAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(107);
				numberLiteral();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(108);
				identifier();
				}
				break;
			case GET:
			case SET:
			case GETRANGE:
			case GETSET:
			case GETBIT:
			case MGET:
			case SETBIT:
			case SETEX:
			case SETNX:
			case SETRANGE:
			case STRLEN:
			case MSET:
			case MSETNX:
			case PSETEX:
			case INCR:
			case INCRBY:
			case INCRBYFLOAT:
			case DECR:
			case DECRBY:
			case APPEND:
			case HGET:
			case HSET:
			case HDEL:
			case HGETALL:
			case HKEYS:
			case HVALS:
			case HEXISTS:
			case HINCRBY:
			case HLEN:
			case LPUSH:
			case RPUSH:
			case LPOP:
			case RPOP:
			case LLEN:
			case LRANGE:
			case BLPOP:
			case BRPOP:
			case BRPOPLPUSH:
			case LINDEX:
			case LINSERT:
			case LPUSHX:
			case LREM:
			case LSET:
			case LTRIM:
			case RPOPLPUSH:
			case RPUSHX:
			case SADD:
			case SREM:
			case SMEMBERS:
			case SCARD:
			case SISMEMBER:
			case SINTER:
			case SINTERSTORE:
			case SUNION:
			case SUNIONSTORE:
			case SDIFF:
			case SDIFFSTORE:
			case SMOVE:
			case SPOP:
			case SRANDMEMBER:
			case SSCAN:
			case ZADD:
			case ZREM:
			case ZRANGE:
			case ZCARD:
			case ZSCORE:
			case ZCOUNT:
			case ZINCRBY:
			case ZINTERSTORE:
			case ZLEXCOUNT:
			case ZRANGEBYLEX:
			case ZRANGEBYSCORE:
			case ZRANK:
			case ZREMRANGEBYLEX:
			case ZREMRANGEBYRANK:
			case ZREMRANGEBYSCORE:
			case ZREVRANGE:
			case ZREVRANGEBYSCORE:
			case ZREVRANK:
			case ZUNIONSTORE:
			case ZSCAN:
			case DEL:
			case EXPIRE:
			case TTL:
			case PERSIST:
			case TYPE:
			case PEXPIRE:
			case PTTL:
			case DUMP:
			case EXISTS:
			case EXPIREAT:
			case PEXPIREAT:
			case KEYS:
			case MOVE:
			case RANDOMKEY:
			case RENAME:
			case RENAMENX:
			case COMMAND:
			case AUTH:
			case PING:
			case ECHO:
			case CLIENT:
			case SELECT:
			case QUIT:
			case FLUSHDB:
			case FLUSHALL:
			case INFO:
			case CONFIG:
			case DBSIZE:
			case BGREWRITEAOF:
			case BGSAVE:
			case TIME:
			case LASTSAVE:
			case MONITOR:
			case ROLE:
			case SAVE:
			case SHUTDOWN:
			case SLAVEOF:
			case SLOWLOG:
			case SYNC:
			case PSUBSCRIBE:
			case PUBSUB:
			case PUBLISH:
			case PUNSUBSCRIBE:
			case SUBSCRIBE:
			case UNSUBSCRIBE:
			case DISCARD:
			case EXEC:
			case MULTI:
			case UNWATCH:
			case WATCH:
			case EVAL:
			case EVALSHA:
			case SCRIPT:
			case GEOHASH:
			case GEOPOS:
			case GEODIST:
			case GEORADIUS:
			case GEOADD:
			case GEORADIUSBYMEMBER:
				enterOuterAlt(_localctx, 4);
				{
				setState(109);
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

	public static final String _serializedATN =
		"\u0004\u0001\u009cq\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0001\u0000\u0001\u0000\u0005\u0000)\b\u0000\n\u0000\f\u0000,\t\u0000"+
		"\u0001\u0000\u0003\u0000/\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001=\b\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001"+
		"\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0003\u0011i\b\u0011\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0003\u0012o\b\u0012\u0001\u0012\u0000\u0000\u0013"+
		"\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$\u0000\u000e\u0001\u0000\u0001\u0014\u0001\u0000\u0015"+
		"\u001d\u0001\u0000\u001e.\u0001\u0000/=\u0001\u0000>Q\u0001\u0000Ra\u0001"+
		"\u0000bh\u0001\u0000ix\u0001\u0000y~\u0001\u0000\u007f\u0083\u0001\u0000"+
		"\u0084\u0086\u0001\u0000\u0087\u008c\u0001\u0000\u0093\u0094\u0001\u0000"+
		"\u0096\u0097x\u0000&\u0001\u0000\u0000\u0000\u0002<\u0001\u0000\u0000"+
		"\u0000\u0004>\u0001\u0000\u0000\u0000\u0006@\u0001\u0000\u0000\u0000\b"+
		"B\u0001\u0000\u0000\u0000\nD\u0001\u0000\u0000\u0000\fF\u0001\u0000\u0000"+
		"\u0000\u000eH\u0001\u0000\u0000\u0000\u0010J\u0001\u0000\u0000\u0000\u0012"+
		"L\u0001\u0000\u0000\u0000\u0014N\u0001\u0000\u0000\u0000\u0016P\u0001"+
		"\u0000\u0000\u0000\u0018R\u0001\u0000\u0000\u0000\u001aT\u0001\u0000\u0000"+
		"\u0000\u001cV\u0001\u0000\u0000\u0000\u001eX\u0001\u0000\u0000\u0000 "+
		"Z\u0001\u0000\u0000\u0000\"h\u0001\u0000\u0000\u0000$n\u0001\u0000\u0000"+
		"\u0000&*\u0003\u0002\u0001\u0000\')\u0003$\u0012\u0000(\'\u0001\u0000"+
		"\u0000\u0000),\u0001\u0000\u0000\u0000*(\u0001\u0000\u0000\u0000*+\u0001"+
		"\u0000\u0000\u0000+.\u0001\u0000\u0000\u0000,*\u0001\u0000\u0000\u0000"+
		"-/\u0005\u009a\u0000\u0000.-\u0001\u0000\u0000\u0000./\u0001\u0000\u0000"+
		"\u0000/\u0001\u0001\u0000\u0000\u00000=\u0003\u0004\u0002\u00001=\u0003"+
		"\u0006\u0003\u00002=\u0003\b\u0004\u00003=\u0003\n\u0005\u00004=\u0003"+
		"\f\u0006\u00005=\u0003\u000e\u0007\u00006=\u0003\u0010\b\u00007=\u0003"+
		"\u0012\t\u00008=\u0003\u0014\n\u00009=\u0003\u0016\u000b\u0000:=\u0003"+
		"\u0018\f\u0000;=\u0003\u001a\r\u0000<0\u0001\u0000\u0000\u0000<1\u0001"+
		"\u0000\u0000\u0000<2\u0001\u0000\u0000\u0000<3\u0001\u0000\u0000\u0000"+
		"<4\u0001\u0000\u0000\u0000<5\u0001\u0000\u0000\u0000<6\u0001\u0000\u0000"+
		"\u0000<7\u0001\u0000\u0000\u0000<8\u0001\u0000\u0000\u0000<9\u0001\u0000"+
		"\u0000\u0000<:\u0001\u0000\u0000\u0000<;\u0001\u0000\u0000\u0000=\u0003"+
		"\u0001\u0000\u0000\u0000>?\u0007\u0000\u0000\u0000?\u0005\u0001\u0000"+
		"\u0000\u0000@A\u0007\u0001\u0000\u0000A\u0007\u0001\u0000\u0000\u0000"+
		"BC\u0007\u0002\u0000\u0000C\t\u0001\u0000\u0000\u0000DE\u0007\u0003\u0000"+
		"\u0000E\u000b\u0001\u0000\u0000\u0000FG\u0007\u0004\u0000\u0000G\r\u0001"+
		"\u0000\u0000\u0000HI\u0007\u0005\u0000\u0000I\u000f\u0001\u0000\u0000"+
		"\u0000JK\u0007\u0006\u0000\u0000K\u0011\u0001\u0000\u0000\u0000LM\u0007"+
		"\u0007\u0000\u0000M\u0013\u0001\u0000\u0000\u0000NO\u0007\b\u0000\u0000"+
		"O\u0015\u0001\u0000\u0000\u0000PQ\u0007\t\u0000\u0000Q\u0017\u0001\u0000"+
		"\u0000\u0000RS\u0007\n\u0000\u0000S\u0019\u0001\u0000\u0000\u0000TU\u0007"+
		"\u000b\u0000\u0000U\u001b\u0001\u0000\u0000\u0000VW\u0007\f\u0000\u0000"+
		"W\u001d\u0001\u0000\u0000\u0000XY\u0007\r\u0000\u0000Y\u001f\u0001\u0000"+
		"\u0000\u0000Z[\u0005\u0095\u0000\u0000[!\u0001\u0000\u0000\u0000\\i\u0003"+
		"\u0004\u0002\u0000]i\u0003\u0006\u0003\u0000^i\u0003\b\u0004\u0000_i\u0003"+
		"\n\u0005\u0000`i\u0003\f\u0006\u0000ai\u0003\u000e\u0007\u0000bi\u0003"+
		"\u0010\b\u0000ci\u0003\u0012\t\u0000di\u0003\u0014\n\u0000ei\u0003\u0016"+
		"\u000b\u0000fi\u0003\u0018\f\u0000gi\u0003\u001a\r\u0000h\\\u0001\u0000"+
		"\u0000\u0000h]\u0001\u0000\u0000\u0000h^\u0001\u0000\u0000\u0000h_\u0001"+
		"\u0000\u0000\u0000h`\u0001\u0000\u0000\u0000ha\u0001\u0000\u0000\u0000"+
		"hb\u0001\u0000\u0000\u0000hc\u0001\u0000\u0000\u0000hd\u0001\u0000\u0000"+
		"\u0000he\u0001\u0000\u0000\u0000hf\u0001\u0000\u0000\u0000hg\u0001\u0000"+
		"\u0000\u0000i#\u0001\u0000\u0000\u0000jo\u0003\u001c\u000e\u0000ko\u0003"+
		"\u001e\u000f\u0000lo\u0003 \u0010\u0000mo\u0003\"\u0011\u0000nj\u0001"+
		"\u0000\u0000\u0000nk\u0001\u0000\u0000\u0000nl\u0001\u0000\u0000\u0000"+
		"nm\u0001\u0000\u0000\u0000o%\u0001\u0000\u0000\u0000\u0005*.<hn";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}