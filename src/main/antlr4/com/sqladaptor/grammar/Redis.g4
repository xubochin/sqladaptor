grammar Redis;

// 主要解析规则
command : commandName argument* CRLF?;

// 按类型分类的命令名规则
commandName : stringCommand
            | hashCommand
            | listCommand
            | setCommand
            | sortedSetCommand
            | keyCommand
            | connectionCommand
            | serverCommand
            | pubsubCommand
            | transactionCommand
            | scriptCommand
            | geoCommand;

// String命令类别
stringCommand : GET | SET | GETRANGE | GETSET | GETBIT | MGET | SETBIT | SETEX | SETNX 
              | SETRANGE | STRLEN | MSET | MSETNX | PSETEX | INCR | INCRBY | INCRBYFLOAT 
              | DECR | DECRBY | APPEND;

// Hash命令类别
hashCommand : HGET | HSET | HDEL | HGETALL | HKEYS | HVALS | HEXISTS | HINCRBY | HLEN;

// List命令类别
listCommand : LPUSH | RPUSH | LPOP | RPOP | LLEN | LRANGE | BLPOP | BRPOP | BRPOPLPUSH 
            | LINDEX | LINSERT | LPUSHX | LREM | LSET | LTRIM | RPOPLPUSH | RPUSHX;

// Set命令类别
setCommand : SADD | SREM | SMEMBERS | SCARD | SISMEMBER | SINTER | SINTERSTORE 
           | SUNION | SUNIONSTORE | SDIFF | SDIFFSTORE | SMOVE | SPOP | SRANDMEMBER | SSCAN;

// Sorted Set命令类别
sortedSetCommand : ZADD | ZREM | ZRANGE | ZCARD | ZSCORE | ZCOUNT | ZINCRBY | ZINTERSTORE 
                 | ZLEXCOUNT | ZRANGEBYLEX | ZRANGEBYSCORE | ZRANK | ZREMRANGEBYLEX 
                 | ZREMRANGEBYRANK | ZREMRANGEBYSCORE | ZREVRANGE | ZREVRANGEBYSCORE 
                 | ZREVRANK | ZUNIONSTORE | ZSCAN;

// Key命令类别
keyCommand : DEL | EXPIRE | TTL | PERSIST | TYPE | PEXPIRE | PTTL | DUMP | EXISTS 
           | EXPIREAT | PEXPIREAT | KEYS | MOVE | RANDOMKEY | RENAME | RENAMENX;

// Connection命令类别
connectionCommand : COMMAND | AUTH | PING | ECHO | CLIENT | SELECT | QUIT;

// Server命令类别
serverCommand : FLUSHDB | FLUSHALL | INFO | CONFIG | DBSIZE | BGREWRITEAOF | BGSAVE 
              | TIME | LASTSAVE | MONITOR | ROLE | SAVE | SHUTDOWN | SLAVEOF | SLOWLOG | SYNC;

// Pub/Sub命令类别
pubsubCommand : PSUBSCRIBE | PUBSUB | PUBLISH | PUNSUBSCRIBE | SUBSCRIBE | UNSUBSCRIBE;

// Transaction命令类别
transactionCommand : DISCARD | EXEC | MULTI | UNWATCH | WATCH;

// Script命令类别
scriptCommand : EVAL | EVALSHA | SCRIPT;

// Geo命令类别
geoCommand : GEOHASH | GEOPOS | GEODIST | GEORADIUS | GEOADD | GEORADIUSBYMEMBER;

// 大小写不敏感的片段定义
fragment A : [aA]; fragment B : [bB]; fragment C : [cC]; fragment D : [dD];
fragment E : [eE]; fragment F : [fF]; fragment G : [gG]; fragment H : [hH];
fragment I : [iI]; fragment J : [jJ]; fragment K : [kK]; fragment L : [lL];
fragment M : [mM]; fragment N : [nN]; fragment O : [oO]; fragment P : [pP];
fragment Q : [qQ]; fragment R : [rR]; fragment S : [sS]; fragment T : [tT];
fragment U : [uU]; fragment V : [vV]; fragment W : [wW]; fragment X : [xX];
fragment Y : [yY]; fragment Z : [zZ];

// String命令词法定义
GET : G E T;
SET : S E T;
GETRANGE : G E T R A N G E;
GETSET : G E T S E T;
GETBIT : G E T B I T;
MGET : M G E T;
SETBIT : S E T B I T;
SETEX : S E T E X;
SETNX : S E T N X;
SETRANGE : S E T R A N G E;
STRLEN : S T R L E N;
MSET : M S E T;
MSETNX : M S E T N X;
PSETEX : P S E T E X;
INCR : I N C R;
INCRBY : I N C R B Y;
INCRBYFLOAT : I N C R B Y F L O A T;
DECR : D E C R;
DECRBY : D E C R B Y;
APPEND : A P P E N D;

// Hash命令词法定义
HGET : H G E T;
HSET : H S E T;
HDEL : H D E L;
HGETALL : H G E T A L L;
HKEYS : H K E Y S;
HVALS : H V A L S;
HEXISTS : H E X I S T S;
HINCRBY : H I N C R B Y;
HLEN : H L E N;

// List命令词法定义
LPUSH : L P U S H;
RPUSH : R P U S H;
LPOP : L P O P;
RPOP : R P O P;
LLEN : L L E N;
LRANGE : L R A N G E;
BLPOP : B L P O P;
BRPOP : B R P O P;
BRPOPLPUSH : B R P O P L P U S H;
LINDEX : L I N D E X;
LINSERT : L I N S E R T;
LPUSHX : L P U S H X;
LREM : L R E M;
LSET : L S E T;
LTRIM : L T R I M;
RPOPLPUSH : R P O P L P U S H;
RPUSHX : R P U S H X;

// Set命令词法定义
SADD : S A D D;
SREM : S R E M;
SMEMBERS : S M E M B E R S;
SCARD : S C A R D;
SISMEMBER : S I S M E M B E R;
SINTER : S I N T E R;
SINTERSTORE : S I N T E R S T O R E;
SUNION : S U N I O N;
SUNIONSTORE : S U N I O N S T O R E;
SDIFF : S D I F F;
SDIFFSTORE : S D I F F S T O R E;
SMOVE : S M O V E;
SPOP : S P O P;
SRANDMEMBER : S R A N D M E M B E R;
SSCAN : S S C A N;

// Sorted Set命令词法定义
ZADD : Z A D D;
ZREM : Z R E M;
ZRANGE : Z R A N G E;
ZCARD : Z C A R D;
ZSCORE : Z S C O R E;
ZCOUNT : Z C O U N T;
ZINCRBY : Z I N C R B Y;
ZINTERSTORE : Z I N T E R S T O R E;
ZLEXCOUNT : Z L E X C O U N T;
ZRANGEBYLEX : Z R A N G E B Y L E X;
ZRANGEBYSCORE : Z R A N G E B Y S C O R E;
ZRANK : Z R A N K;
ZREMRANGEBYLEX : Z R E M R A N G E B Y L E X;
ZREMRANGEBYRANK : Z R E M R A N G E B Y R A N K;
ZREMRANGEBYSCORE : Z R E M R A N G E B Y S C O R E;
ZREVRANGE : Z R E V R A N G E;
ZREVRANGEBYSCORE : Z R E V R A N G E B Y S C O R E;
ZREVRANK : Z R E V R A N K;
ZUNIONSTORE : Z U N I O N S T O R E;
ZSCAN : Z S C A N;

// Key命令词法定义
DEL : D E L;
EXPIRE : E X P I R E;
TTL : T T L;
PERSIST : P E R S I S T;
TYPE : T Y P E;
PEXPIRE : P E X P I R E;
PTTL : P T T L;
DUMP : D U M P;
EXISTS : E X I S T S;
EXPIREAT : E X P I R E A T;
PEXPIREAT : P E X P I R E A T;
KEYS : K E Y S;
MOVE : M O V E;
RANDOMKEY : R A N D O M K E Y;
RENAME : R E N A M E;
RENAMENX : R E N A M E N X;

// Connection命令词法定义
COMMAND : C O M M A N D;
AUTH : A U T H;
PING : P I N G;
ECHO : E C H O;
CLIENT : C L I E N T;
SELECT : S E L E C T;
QUIT : Q U I T;

// Server命令词法定义
FLUSHDB : F L U S H D B;
FLUSHALL : F L U S H A L L;
INFO : I N F O;
CONFIG : C O N F I G;
DBSIZE : D B S I Z E;
BGREWRITEAOF : B G R E W R I T E A O F;
BGSAVE : B G S A V E;
TIME : T I M E;
LASTSAVE : L A S T S A V E;
MONITOR : M O N I T O R;
ROLE : R O L E;
SAVE : S A V E;
SHUTDOWN : S H U T D O W N;
SLAVEOF : S L A V E O F;
SLOWLOG : S L O W L O G;
SYNC : S Y N C;

// Pub/Sub命令词法定义
PSUBSCRIBE : P S U B S C R I B E;
PUBSUB : P U B S U B;
PUBLISH : P U B L I S H;
PUNSUBSCRIBE : P U N S U B S C R I B E;
SUBSCRIBE : S U B S C R I B E;
UNSUBSCRIBE : U N S U B S C R I B E;

// Transaction命令词法定义
DISCARD : D I S C A R D;
EXEC : E X E C;
MULTI : M U L T I;
UNWATCH : U N W A T C H;
WATCH : W A T C H;

// Script命令词法定义
EVAL : E V A L;
EVALSHA : E V A L S H A;
SCRIPT : S C R I P T;

// Geo命令词法定义
GEOHASH : G E O H A S H;
GEOPOS : G E O P O S;
GEODIST : G E O D I S T;
GEORADIUS : G E O R A D I U S;
GEOADD : G E O A D D;
GEORADIUSBYMEMBER : G E O R A D I U S B Y M E M B E R;

// RESP协议符号
PLUS : '+';
MINUS : '-';
COLON : ':';
DOLLAR : '$';
ASTERISK : '*';
MINUS_ONE : '-1';

// 词法规则
QUOTED_STRING : '"' ( '\\"' | ~['"\r\n] )* '"';
UNQUOTED_STRING : [a-zA-Z0-9_-]+;
IDENTIFIER : [a-zA-Z_][a-zA-Z0-9_]*;
NUMBER : [0-9]+;
FLOAT : [0-9]+ '.' [0-9]+;
DIGIT : [0-9];
CHAR : [a-zA-Z];
CRLF : '\r'? '\n';

SPECIAL :  '!' | '@' | '#' | '%' | '^' | '&' | '(' | ')' | '_' | '+' | '='
        | '{' | '}' | '[' | ']' | '|' | ':' | ';' | '\'' | '<' | '>'
        | ',' | '.' | '?' | '/' | '~' | '`';

// 空白字符
WHITESPACE : [ \t\r\n]+ -> skip;

// 参数规则
stringLiteral : QUOTED_STRING | UNQUOTED_STRING;
numberLiteral : NUMBER | FLOAT;
identifier : IDENTIFIER;

// 首先添加commandKeyword规则定义
commandKeyword
    : stringCommand | hashCommand | listCommand | setCommand | sortedSetCommand
    | keyCommand | connectionCommand | serverCommand | pubsubCommand 
    | transactionCommand | scriptCommand | geoCommand;

// 然后修改argument规则
argument
    : stringLiteral
    | numberLiteral
    | identifier
    | commandKeyword  // 添加这一行
    ;