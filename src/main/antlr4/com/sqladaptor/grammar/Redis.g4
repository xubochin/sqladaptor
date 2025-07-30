// 在语法文件顶部添加
grammar Redis;

// 解析规则 - 添加CLIENT命令
command : commandName argument* CRLF?;
commandName : GET | SET | HGET | CONFIG | DEL | EXISTS | EXPIRE | TTL 
            | HSET | HDEL | HGETALL | HKEYS | HVALS
            | LPUSH | RPUSH | LPOP | RPOP | LLEN | LRANGE
            | SADD | SREM | SMEMBERS | SCARD | SISMEMBER
            | ZADD | ZREM | ZRANGE | ZCARD | ZSCORE
            | PING | ECHO | SELECT | FLUSHDB | FLUSHALL | INFO | DBSIZE | KEYS | TYPE
            | INCR | DECR | INCRBY | DECRBY
            | APPEND | STRLEN | GETRANGE | SETRANGE
            | MGET | MSET | MSETNX
            | PERSIST | EXPIREAT | PEXPIRE | PEXPIREAT | PTTL
            | CLIENT;  // 添加CLIENT命令

// 添加大小写不敏感的片段
fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

// 修改命令定义 - 使用片段实现大小写不敏感
GET : G E T;
SET : S E T;
HGET : H G E T;
CONFIG : C O N F I G;
DEL : D E L;
EXISTS : E X I S T S;
EXPIRE : E X P I R E;
TTL : T T L;

// Hash命令
HSET : H S E T;
HDEL : H D E L;
HGETALL : H G E T A L L;
HKEYS : H K E Y S;
HVALS : H V A L S;

// List命令
LPUSH : L P U S H;
RPUSH : R P U S H;
LPOP : L P O P;
RPOP : R P O P;
LLEN : L L E N;
LRANGE : L R A N G E;

// Set命令
SADD : S A D D;
SREM : S R E M;
SMEMBERS : S M E M B E R S;
SCARD : S C A R D;
SISMEMBER : S I S M E M B E R;

// Sorted Set命令
ZADD : Z A D D;
ZREM : Z R E M;
ZRANGE : Z R A N G E;
ZCARD : Z C A R D;
ZSCORE : Z S C O R E;

// 连接和服务器命令
PING : P I N G;
ECHO : E C H O;
SELECT : S E L E C T;
FLUSHDB : F L U S H D B;
FLUSHALL : F L U S H A L L;
INFO : I N F O;
DBSIZE : D B S I Z E;
KEYS : K E Y S;
TYPE : T Y P E;
CLIENT : C L I E N T;  // 添加CLIENT命令定义

// 数值操作命令
INCR : I N C R;
DECR : D E C R;
INCRBY : I N C R B Y;
DECRBY : D E C R B Y;

// 字符串操作命令
APPEND : A P P E N D;
STRLEN : S T R L E N;
GETRANGE : G E T R A N G E;
SETRANGE : S E T R A N G E;

// 批量操作命令
MGET : M G E T;
MSET : M S E T;
MSETNX : M S E T N X;

// 过期时间命令
PERSIST : P E R S I S T;
EXPIREAT : E X P I R E A T;
PEXPIRE : P E X P I R E;
PEXPIREAT : P E X P I R E A T;
PTTL : P T T L;

// RESP协议符号
PLUS : '+';
MINUS : '-';
COLON : ':';
DOLLAR : '$';
ASTERISK : '*';
MINUS_ONE : '-1';

// 词法规则
QUOTED_STRING : '"' (~["\r\n])* '"';
UNQUOTED_STRING : [a-zA-Z0-9_.-]+;
IDENTIFIER : [a-zA-Z_][a-zA-Z0-9_]*;
NUMBER : [0-9]+;
FLOAT : [0-9]+ '.' [0-9]+;
DIGIT : [0-9];
CHAR : [a-zA-Z];
// 修复第137行的字符集定义，移除重复的反斜杠
SPECIAL :  '!' | '@' | '#' | '%' | '^' | '&' | '(' | ')' | '_' | '+' | '='
        | '{' | '}' | '[' | ']' | '|' | ':' | ';' | '"' | '\'' | '<' | '>'
        | ',' | '.' | '?' | '/' | '~' | '`';

// 行结束符
CRLF : '\\r'? '\\n';

// 空白字符
SPACE : ' ';
WHITESPACE : [ \t\r\n]+ -> skip;

// 定义缺失的规则
stringLiteral : QUOTED_STRING | UNQUOTED_STRING;
numberLiteral : NUMBER | FLOAT;
identifier : IDENTIFIER;

// 修改参数规则，允许关键字作为参数
argument
    : stringLiteral
    | numberLiteral
    | identifier
    | commandKeyword
    ;

// 完善命令关键字规则
// 在commandKeyword规则中添加CLIENT
commandKeyword
    : GET | SET | DEL | HGET | HSET | CONFIG | EXISTS | EXPIRE | TTL
    | HDEL | HGETALL | HKEYS | HVALS
    | LPUSH | RPUSH | LPOP | RPOP | LLEN | LRANGE
    | SADD | SREM | SMEMBERS | SCARD | SISMEMBER
    | ZADD | ZREM | ZRANGE | ZCARD | ZSCORE
    | PING | ECHO | SELECT | FLUSHDB | FLUSHALL | INFO | DBSIZE | KEYS | TYPE
    | INCR | DECR | INCRBY | DECRBY
    | APPEND | STRLEN | GETRANGE | SETRANGE
    | MGET | MSET | MSETNX
    | PERSIST | EXPIREAT | PEXPIRE | PEXPIREAT | PTTL
    | CLIENT;  // 添加CLIENT到关键字列表