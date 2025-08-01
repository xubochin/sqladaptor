package com.sqladaptor.ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class RedisCommandNode {
    private String command;
    private List<String> arguments;
    private CommandType type;
    
    // 静态命令类型映射表，提高查找效率
    private static final Map<String, CommandType> COMMAND_TYPE_MAP = new HashMap<>();
    
    static {
        // String commands
        COMMAND_TYPE_MAP.put("SET", CommandType.STRING);           // 设置指定 key 的值
        COMMAND_TYPE_MAP.put("GET", CommandType.STRING);           // 获取指定 key 的值
        COMMAND_TYPE_MAP.put("GETRANGE", CommandType.STRING);      // 返回 key 中字符串值的子字符
        COMMAND_TYPE_MAP.put("GETSET", CommandType.STRING);        // 将给定 key 的值设为 value ，并返回 key 的旧值
        COMMAND_TYPE_MAP.put("GETBIT", CommandType.STRING);        // 对 key 所储存的字符串值，获取指定偏移量上的位
        COMMAND_TYPE_MAP.put("MGET", CommandType.STRING);          // 获取所有(一个或多个)给定 key 的值
        COMMAND_TYPE_MAP.put("SETBIT", CommandType.STRING);        // 对 key 所储存的字符串值，设置或清除指定偏移量上的位
        COMMAND_TYPE_MAP.put("SETEX", CommandType.STRING);         // 设置 key 的值为 value 同时将过期时间设为 seconds
        COMMAND_TYPE_MAP.put("SETNX", CommandType.STRING);         // 只有在 key 不存在时设置 key 的值
        COMMAND_TYPE_MAP.put("SETRANGE", CommandType.STRING);      // 从偏移量 offset 开始用 value 覆写给定 key 所储存的字符串值
        COMMAND_TYPE_MAP.put("STRLEN", CommandType.STRING);        // 返回 key 所储存的字符串值的长度
        COMMAND_TYPE_MAP.put("MSET", CommandType.STRING);          // 同时设置一个或多个 key-value 对
        COMMAND_TYPE_MAP.put("MSETNX", CommandType.STRING);        // 同时设置一个或多个 key-value 对
        COMMAND_TYPE_MAP.put("PSETEX", CommandType.STRING);        // 以毫秒为单位设置 key 的生存时间
        COMMAND_TYPE_MAP.put("INCR", CommandType.STRING);          // 将 key 中储存的数字值增一
        COMMAND_TYPE_MAP.put("INCRBY", CommandType.STRING);        // 将 key 所储存的值加上给定的增量值
        COMMAND_TYPE_MAP.put("INCRBYFLOAT", CommandType.STRING);   // 将 key 所储存的值加上给定的浮点增量值
        COMMAND_TYPE_MAP.put("DECR", CommandType.STRING);          // 将 key 中储存的数字值减一
        COMMAND_TYPE_MAP.put("DECRBY", CommandType.STRING);        // 将 key 所储存的值减去给定的减量值
        COMMAND_TYPE_MAP.put("APPEND", CommandType.STRING);        // 将 value 追加到 key 原来的值的末尾
        
        // Hash commands
        COMMAND_TYPE_MAP.put("HGET", CommandType.HASH);           // 获取存储在哈希表中指定字段的值
        COMMAND_TYPE_MAP.put("HSET", CommandType.HASH);           // 设置存储在 key 中的哈希表字段的值
        COMMAND_TYPE_MAP.put("HDEL", CommandType.HASH);           // 删除哈希表中一个或多个字段
        COMMAND_TYPE_MAP.put("HGETALL", CommandType.HASH);        // 获取在哈希表中指定 key 的所有字段和值
        COMMAND_TYPE_MAP.put("HKEYS", CommandType.HASH);          // 获取存储在 key 中的哈希表的所有字段
        COMMAND_TYPE_MAP.put("HVALS", CommandType.HASH);          // 获取哈希表中的所有值
        COMMAND_TYPE_MAP.put("HEXISTS", CommandType.HASH);        // 判断哈希表中字段是否存在
        COMMAND_TYPE_MAP.put("HINCRBY", CommandType.HASH);        // 为存储在 key 中的哈希表指定字段做整数增量运算
        COMMAND_TYPE_MAP.put("HLEN", CommandType.HASH);           // 获取存储在 key 中的哈希表的字段数量
        
        // List commands
        COMMAND_TYPE_MAP.put("LPUSH", CommandType.LIST);          // 将一个或多个值插入到列表头部
        COMMAND_TYPE_MAP.put("RPUSH", CommandType.LIST);          // 在列表中添加一个或多个值
        COMMAND_TYPE_MAP.put("LPOP", CommandType.LIST);           // 移出并获取列表的第一个元素
        COMMAND_TYPE_MAP.put("RPOP", CommandType.LIST);           // 移除并获取列表最后一个元素
        COMMAND_TYPE_MAP.put("LLEN", CommandType.LIST);           // 获取列表长度
        COMMAND_TYPE_MAP.put("LRANGE", CommandType.LIST);         // 获取列表指定范围内的元素
        COMMAND_TYPE_MAP.put("BLPOP", CommandType.LIST);          // 移出并获取列表的第一个元素
        COMMAND_TYPE_MAP.put("BRPOP", CommandType.LIST);          // 移出并获取列表的最后一个元素
        COMMAND_TYPE_MAP.put("BRPOPLPUSH", CommandType.LIST);     // 从列表中弹出一个值，并将该值插入到另外一个列表中并返回它
        COMMAND_TYPE_MAP.put("LINDEX", CommandType.LIST);         // 通过索引获取列表中的元素
        COMMAND_TYPE_MAP.put("LINSERT", CommandType.LIST);        // 在列表的元素前或者后插入元素
        COMMAND_TYPE_MAP.put("LPUSHX", CommandType.LIST);         // 将一个值插入到已存在的列表头部
        COMMAND_TYPE_MAP.put("LREM", CommandType.LIST);           // 移除列表元素
        COMMAND_TYPE_MAP.put("LSET", CommandType.LIST);           // 通过索引设置列表元素的值
        COMMAND_TYPE_MAP.put("LTRIM", CommandType.LIST);          // 对一个列表进行修剪(trim)
        COMMAND_TYPE_MAP.put("RPOPLPUSH", CommandType.LIST);      // 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
        COMMAND_TYPE_MAP.put("RPUSHX", CommandType.LIST);         // 为已存在的列表添加值
        
        // Set commands
        COMMAND_TYPE_MAP.put("SADD", CommandType.SET);            // 向集合添加一个或多个成员
        COMMAND_TYPE_MAP.put("SREM", CommandType.SET);            // 移除集合中一个或多个成员
        COMMAND_TYPE_MAP.put("SMEMBERS", CommandType.SET);        // 返回集合中的所有成员
        COMMAND_TYPE_MAP.put("SCARD", CommandType.SET);           // 获取集合的成员数
        COMMAND_TYPE_MAP.put("SISMEMBER", CommandType.SET);       // 判断 member 元素是否是集合 key 的成员
        COMMAND_TYPE_MAP.put("SDIFF", CommandType.SET);           // 返回给定所有集合的差集
        COMMAND_TYPE_MAP.put("SDIFFSTORE", CommandType.SET);      // 返回给定所有集合的差集并存储在 destination 中
        COMMAND_TYPE_MAP.put("SINTER", CommandType.SET);          // 返回给定所有集合的交集
        COMMAND_TYPE_MAP.put("SINTERSTORE", CommandType.SET);     // 返回给定所有集合的交集并存储在 destination 中
        COMMAND_TYPE_MAP.put("SMOVE", CommandType.SET);           // 将 member 元素从 source 集合移动到 destination 集合
        COMMAND_TYPE_MAP.put("SPOP", CommandType.SET);            // 移除并返回集合中的一个随机元素
        COMMAND_TYPE_MAP.put("SRANDMEMBER", CommandType.SET);     // 返回集合中一个或多个随机数
        COMMAND_TYPE_MAP.put("SUNION", CommandType.SET);          // 返回所有给定集合的并集
        COMMAND_TYPE_MAP.put("SUNIONSTORE", CommandType.SET);     // 所有给定集合的并集存储在 destination 集合中
        COMMAND_TYPE_MAP.put("SSCAN", CommandType.SET);           // 迭代集合中的元素
        
        // Sorted Set commands
        COMMAND_TYPE_MAP.put("ZADD", CommandType.SORTED_SET);        // 向有序集合添加一个或多个成员，或者更新已存在成员的分数
        COMMAND_TYPE_MAP.put("ZREM", CommandType.SORTED_SET);        // 移除有序集合中的一个或多个成员
        COMMAND_TYPE_MAP.put("ZRANGE", CommandType.SORTED_SET);      // 通过索引区间返回有序集合成指定区间内的成员
        COMMAND_TYPE_MAP.put("ZCARD", CommandType.SORTED_SET);       // 获取有序集合的成员数
        COMMAND_TYPE_MAP.put("ZSCORE", CommandType.SORTED_SET);      // 返回有序集中，成员的分数值
        COMMAND_TYPE_MAP.put("ZCOUNT", CommandType.SORTED_SET);      // 计算在有序集合中指定区间分数的成员数
        COMMAND_TYPE_MAP.put("ZINCRBY", CommandType.SORTED_SET);     // 有序集合中对指定成员的分数加上增量 increment
        COMMAND_TYPE_MAP.put("ZINTERSTORE", CommandType.SORTED_SET); // 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中
        COMMAND_TYPE_MAP.put("ZLEXCOUNT", CommandType.SORTED_SET);   // 在有序集合中计算指定字典区间内成员数量
        COMMAND_TYPE_MAP.put("ZRANGEBYLEX", CommandType.SORTED_SET); // 通过字典区间返回有序集合的成员
        COMMAND_TYPE_MAP.put("ZRANGEBYSCORE", CommandType.SORTED_SET); // 通过分数返回有序集合指定区间内的成员
        COMMAND_TYPE_MAP.put("ZRANK", CommandType.SORTED_SET);       // 返回有序集合中指定成员的索引
        COMMAND_TYPE_MAP.put("ZREMRANGEBYLEX", CommandType.SORTED_SET); // 移除有序集合中给定的字典区间的所有成员
        COMMAND_TYPE_MAP.put("ZREMRANGEBYRANK", CommandType.SORTED_SET); // 移除有序集合中给定的排名区间的所有成员
        COMMAND_TYPE_MAP.put("ZREMRANGEBYSCORE", CommandType.SORTED_SET); // 移除有序集合中给定的分数区间的所有成员
        COMMAND_TYPE_MAP.put("ZREVRANGE", CommandType.SORTED_SET);   // 返回有序集中指定区间内的成员，通过索引，分数从高到底
        COMMAND_TYPE_MAP.put("ZREVRANGEBYSCORE", CommandType.SORTED_SET); // 返回有序集中指定分数区间内的成员，分数从高到低排序
        COMMAND_TYPE_MAP.put("ZREVRANK", CommandType.SORTED_SET);    // 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序
        COMMAND_TYPE_MAP.put("ZUNIONSTORE", CommandType.SORTED_SET); // 计算一个或多个有序集的并集，并存储在新的 key 中
        COMMAND_TYPE_MAP.put("ZSCAN", CommandType.SORTED_SET);       // 迭代有序集合中的元素（包括元素成员和元素分值）
        
        // Key commands

        COMMAND_TYPE_MAP.put("DEL", CommandType.KEY);        // 删除一个或多个指定的key
        COMMAND_TYPE_MAP.put("EXPIRE", CommandType.KEY);     // 为给定 key 设置过期时间
        COMMAND_TYPE_MAP.put("TTL", CommandType.KEY);        // 以秒为单位，返回给定 key 的剩余生存时间
        COMMAND_TYPE_MAP.put("PERSIST", CommandType.KEY);    // 移除 key 的过期时间，key 将持久保持
        COMMAND_TYPE_MAP.put("TYPE", CommandType.KEY);       // 返回 key 所储存的值的类型
        COMMAND_TYPE_MAP.put("PEXPIRE", CommandType.KEY);    // 设置 key 的过期时间，以毫秒计
        COMMAND_TYPE_MAP.put("PTTL", CommandType.KEY);       // 以毫秒为单位返回 key 的剩余的过期时间
        COMMAND_TYPE_MAP.put("DUMP", CommandType.KEY);       // 序列化给定 key ，并返回被序列化的值
        COMMAND_TYPE_MAP.put("EXISTS", CommandType.KEY);     // 检查给定 key 是否存在
        COMMAND_TYPE_MAP.put("EXPIREAT", CommandType.KEY);   // 用于为 key 设置过期时间，接受的时间参数是 UNIX 时间戳
        COMMAND_TYPE_MAP.put("PEXPIREAT", CommandType.KEY);  // 设置 key 过期时间的时间戳(unix timestamp)，以毫秒计
        COMMAND_TYPE_MAP.put("KEYS", CommandType.KEY);       // 查找所有符合给定模式的 key
        COMMAND_TYPE_MAP.put("MOVE", CommandType.KEY);       // 将当前数据库的 key 移动到给定的数据库中
        COMMAND_TYPE_MAP.put("RANDOMKEY", CommandType.KEY);  // 从当前数据库中随机返回一个 key
        COMMAND_TYPE_MAP.put("RENAME", CommandType.KEY);     // 修改 key 的名称
        COMMAND_TYPE_MAP.put("RENAMENX", CommandType.KEY);   // 仅当 newkey 不存在时，将 key 改名为 newkey

        // Connection commands
        COMMAND_TYPE_MAP.put("COMMAND", CommandType.CONNECTION);
        COMMAND_TYPE_MAP.put("AUTH", CommandType.CONNECTION);        // 验证密码是否正确
        COMMAND_TYPE_MAP.put("PING", CommandType.CONNECTION);        // 查看服务是否运行
        COMMAND_TYPE_MAP.put("ECHO", CommandType.CONNECTION);        // 打印字符串
        COMMAND_TYPE_MAP.put("CLIENT", CommandType.CONNECTION);
        COMMAND_TYPE_MAP.put("SELECT", CommandType.CONNECTION);      // 切换到指定的数据库
        COMMAND_TYPE_MAP.put("QUIT", CommandType.CONNECTION);        // 关闭当前连接
        
        // Server commands
        COMMAND_TYPE_MAP.put("FLUSHDB", CommandType.SERVER);         // 删除当前数据库的所有 key
        COMMAND_TYPE_MAP.put("FLUSHALL", CommandType.SERVER);        // 删除所有数据库的所有 key
        COMMAND_TYPE_MAP.put("INFO", CommandType.SERVER);            // 获取 Redis 服务器的各种信息和统计数值
        COMMAND_TYPE_MAP.put("CONFIG", CommandType.SERVER);          // Redis 配置相关命令
        COMMAND_TYPE_MAP.put("DBSIZE", CommandType.SERVER);          // 返回当前数据库的 key 的数量
        COMMAND_TYPE_MAP.put("BGREWRITEAOF", CommandType.SERVER);    // 异步执行一个 AOF（AppendOnly File） 文件重写操作
        COMMAND_TYPE_MAP.put("BGSAVE", CommandType.SERVER);          // 在后台异步保存当前数据库的数据到磁盘
        COMMAND_TYPE_MAP.put("CLIENT LIST", CommandType.SERVER);     // 获取连接到服务器的客户端连接列表
        COMMAND_TYPE_MAP.put("CLIENT GETNAME", CommandType.SERVER);  // 获取连接的名称
        COMMAND_TYPE_MAP.put("CLIENT PAUSE", CommandType.SERVER);    // 在指定时间内终止运行来自客户端的命令
        COMMAND_TYPE_MAP.put("CLIENT SETNAME", CommandType.SERVER);  // 设置当前连接的名称
        COMMAND_TYPE_MAP.put("CLUSTER SLOTS", CommandType.SERVER);   // 获取集群节点的映射数组
        COMMAND_TYPE_MAP.put("COMMAND COUNT", CommandType.SERVER);   // 获取 Redis 命令总数
        COMMAND_TYPE_MAP.put("COMMAND GETKEYS", CommandType.SERVER); // 获取给定命令的所有键
        COMMAND_TYPE_MAP.put("TIME", CommandType.SERVER);            // 返回当前服务器时间
        COMMAND_TYPE_MAP.put("COMMAND INFO", CommandType.SERVER);    // 获取指定 Redis 命令描述的数组
        COMMAND_TYPE_MAP.put("CONFIG GET", CommandType.SERVER);      // 获取指定配置参数的值
        COMMAND_TYPE_MAP.put("CONFIG REWRITE", CommandType.SERVER);  // 修改 redis.conf 配置文件
        COMMAND_TYPE_MAP.put("CONFIG SET", CommandType.SERVER);      // 修改 redis 配置参数，无需重启
        COMMAND_TYPE_MAP.put("CONFIG RESETSTAT", CommandType.SERVER); // 重置 INFO 命令中的某些统计数据
        COMMAND_TYPE_MAP.put("DEBUG OBJECT", CommandType.SERVER);    // 获取 key 的调试信息
        COMMAND_TYPE_MAP.put("DEBUG SEGFAULT", CommandType.SERVER);  // 让 Redis 服务崩溃
        COMMAND_TYPE_MAP.put("LASTSAVE", CommandType.SERVER);        // 返回最近一次 Redis 成功将数据保存到磁盘上的时间
        COMMAND_TYPE_MAP.put("MONITOR", CommandType.SERVER);         // 实时打印出 Redis 服务器接收到的命令，调试用
        COMMAND_TYPE_MAP.put("ROLE", CommandType.SERVER);            // 返回主从实例所属的角色
        COMMAND_TYPE_MAP.put("SAVE", CommandType.SERVER);            // 异步保存数据到硬盘
        COMMAND_TYPE_MAP.put("SHUTDOWN", CommandType.SERVER);        // 异步保存数据到硬盘，并关闭服务器
        COMMAND_TYPE_MAP.put("SLAVEOF", CommandType.SERVER);         // 将当前服务器转变从属服务器(slave server)
        COMMAND_TYPE_MAP.put("SLOWLOG", CommandType.SERVER);         // 管理 redis 的慢日志
        COMMAND_TYPE_MAP.put("SYNC", CommandType.SERVER);            // 用于复制功能 ( replication ) 的内部命令
        COMMAND_TYPE_MAP.put("KEYS", CommandType.SERVER);
        // 移除SELECT，因为已移到CONNECTION类型
        
        // Pub/Sub commands
        COMMAND_TYPE_MAP.put("PSUBSCRIBE", CommandType.PUBSUB);      // 订阅一个或多个符合给定模式的频道
        COMMAND_TYPE_MAP.put("PUBSUB", CommandType.PUBSUB);          // 查看订阅与发布系统状态
        COMMAND_TYPE_MAP.put("PUBLISH", CommandType.PUBSUB);         // 将信息发送到指定的频道
        COMMAND_TYPE_MAP.put("PUNSUBSCRIBE", CommandType.PUBSUB);    // 退订所有给定模式的频道
        COMMAND_TYPE_MAP.put("SUBSCRIBE", CommandType.PUBSUB);       // 订阅给定的一个或多个频道的信息
        COMMAND_TYPE_MAP.put("UNSUBSCRIBE", CommandType.PUBSUB);     // 指退订给定的频道
        
        // Transaction commands
        COMMAND_TYPE_MAP.put("DISCARD", CommandType.TRANSACTION);    // 取消事务，放弃执行事务块内的所有命令
        COMMAND_TYPE_MAP.put("EXEC", CommandType.TRANSACTION);       // 执行所有事务块内的命令
        COMMAND_TYPE_MAP.put("MULTI", CommandType.TRANSACTION);      // 标记一个事务块的开始
        COMMAND_TYPE_MAP.put("UNWATCH", CommandType.TRANSACTION);    // 取消 WATCH 命令对所有 key 的监视
        COMMAND_TYPE_MAP.put("WATCH", CommandType.TRANSACTION);      // 监视一个(或多个) key
    
            // Script commands
        COMMAND_TYPE_MAP.put("SCRIPT KILL", CommandType.SCRIPT);     // 杀死当前正在运行的 Lua 脚本
        COMMAND_TYPE_MAP.put("SCRIPT LOAD", CommandType.SCRIPT);     // 将脚本 script 添加到脚本缓存中，但并不立即执行这个脚本
        COMMAND_TYPE_MAP.put("EVAL", CommandType.SCRIPT);            // 执行 Lua 脚本
        COMMAND_TYPE_MAP.put("EVALSHA", CommandType.SCRIPT);         // 执行 Lua 脚本
        COMMAND_TYPE_MAP.put("SCRIPT EXISTS", CommandType.SCRIPT);   // 查看指定的脚本是否已经被保存在缓存当中
        COMMAND_TYPE_MAP.put("SCRIPT FLUSH", CommandType.SCRIPT);    // 从脚本缓存中移除所有脚本
        
        // Geo commands
        COMMAND_TYPE_MAP.put("GEOHASH", CommandType.GEO);            // 返回一个或多个位置元素的 Geohash 表示
        COMMAND_TYPE_MAP.put("GEOPOS", CommandType.GEO);             // 从key里返回所有给定位置元素的位置（经度和纬度）
        COMMAND_TYPE_MAP.put("GEODIST", CommandType.GEO);            // 返回两个给定位置之间的距离
        COMMAND_TYPE_MAP.put("GEORADIUS", CommandType.GEO);          // 以给定的经纬度为中心， 找出某一半径内的元素
        COMMAND_TYPE_MAP.put("GEOADD", CommandType.GEO);             // 将指定的地理空间位置（纬度、经度、名称）添加到指定的key中
        COMMAND_TYPE_MAP.put("GEORADIUSBYMEMBER", CommandType.GEO);  // 找出位于指定范围内的元素，中心点是由给定的位置元素决定
    
    }
    
    public RedisCommandNode(String command) {
        this.command = command;
        this.arguments = new ArrayList<>();
        this.type = determineCommandType(command);
    }
    
    public RedisCommandNode(String command, List<String> arguments) {
        this.command = command;
        this.arguments = arguments != null ? new ArrayList<>(arguments) : new ArrayList<>();
        this.type = determineCommandType(command);
    }
    
    // 将private改为public static，使测试代码可以访问
    public static CommandType determineCommandType(String command) {
        if (command == null) {
            return CommandType.UNKNOWN;
        }
        return COMMAND_TYPE_MAP.getOrDefault(command.toUpperCase(), CommandType.UNKNOWN);
    }
    
    public String getCommand() {
        return command;
    }
    
    public List<String> getArguments() {
        return arguments;
    }
    
    public void addArgument(String argument) {
        this.arguments.add(argument);
    }
    
    public CommandType getType() {
        return type;
    }
    
    public enum CommandType {
        STRING,
        HASH,
        LIST,
        SET,
        SORTED_SET,
        KEY,
        CONNECTION,
        SERVER,
        PUBSUB,        // 新增：发布订阅命令类型
        SCRIPT,        // 新增：脚本命令类型
        TRANSACTION,   // 新增：事务命令类型
        GEO,           // 新增：地理空间命令类型
        UNKNOWN
    }
    
    @Override
    public String toString() {
        return "RedisCommandNode{" +
                "command='" + command + '\'' +
                ", arguments=" + arguments +
                ", type=" + type +
                '}';
    }
}

