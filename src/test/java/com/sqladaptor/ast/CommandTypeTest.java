package com.sqladaptor.ast;

import com.sqladaptor.ast.RedisCommandNode.CommandType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommandTypeTest {
    
    @Test
    void testStringCommandTypes() {
        assertEquals(CommandType.STRING, RedisCommandNode.determineCommandType("GET"));
        assertEquals(CommandType.STRING, RedisCommandNode.determineCommandType("SET"));
        assertEquals(CommandType.STRING, RedisCommandNode.determineCommandType("APPEND"));
        assertEquals(CommandType.STRING, RedisCommandNode.determineCommandType("STRLEN"));
        assertEquals(CommandType.STRING, RedisCommandNode.determineCommandType("INCR"));
        assertEquals(CommandType.STRING, RedisCommandNode.determineCommandType("DECR"));
        assertEquals(CommandType.STRING, RedisCommandNode.determineCommandType("INCRBY"));
        assertEquals(CommandType.STRING, RedisCommandNode.determineCommandType("DECRBY"));
    }
    
    @Test
    void testHashCommandTypes() {
        assertEquals(CommandType.HASH, RedisCommandNode.determineCommandType("HGET"));
        assertEquals(CommandType.HASH, RedisCommandNode.determineCommandType("HSET"));
        assertEquals(CommandType.HASH, RedisCommandNode.determineCommandType("HDEL"));
        assertEquals(CommandType.HASH, RedisCommandNode.determineCommandType("HGETALL"));
        assertEquals(CommandType.HASH, RedisCommandNode.determineCommandType("HKEYS"));
        assertEquals(CommandType.HASH, RedisCommandNode.determineCommandType("HVALS"));
    }
    
    @Test
    void testListCommandTypes() {
        assertEquals(CommandType.LIST, RedisCommandNode.determineCommandType("LPUSH"));
        assertEquals(CommandType.LIST, RedisCommandNode.determineCommandType("RPUSH"));
        assertEquals(CommandType.LIST, RedisCommandNode.determineCommandType("LPOP"));
        assertEquals(CommandType.LIST, RedisCommandNode.determineCommandType("RPOP"));
        assertEquals(CommandType.LIST, RedisCommandNode.determineCommandType("LLEN"));
        assertEquals(CommandType.LIST, RedisCommandNode.determineCommandType("LRANGE"));
    }
    
    @Test
    void testSetCommandTypes() {
        assertEquals(CommandType.SET, RedisCommandNode.determineCommandType("SADD"));
        assertEquals(CommandType.SET, RedisCommandNode.determineCommandType("SREM"));
        assertEquals(CommandType.SET, RedisCommandNode.determineCommandType("SMEMBERS"));
        assertEquals(CommandType.SET, RedisCommandNode.determineCommandType("SCARD"));
        assertEquals(CommandType.SET, RedisCommandNode.determineCommandType("SISMEMBER"));
    }
    
    @Test
    void testSortedSetCommandTypes() {
        assertEquals(CommandType.SORTED_SET, RedisCommandNode.determineCommandType("ZADD"));
        assertEquals(CommandType.SORTED_SET, RedisCommandNode.determineCommandType("ZREM"));
        assertEquals(CommandType.SORTED_SET, RedisCommandNode.determineCommandType("ZRANGE"));
        assertEquals(CommandType.SORTED_SET, RedisCommandNode.determineCommandType("ZCARD"));
        assertEquals(CommandType.SORTED_SET, RedisCommandNode.determineCommandType("ZSCORE"));
    }
    
    @Test
    void testKeyCommandTypes() {
        assertEquals(CommandType.KEY, RedisCommandNode.determineCommandType("DEL"));
        assertEquals(CommandType.KEY, RedisCommandNode.determineCommandType("EXISTS"));
        assertEquals(CommandType.KEY, RedisCommandNode.determineCommandType("EXPIRE"));
        assertEquals(CommandType.KEY, RedisCommandNode.determineCommandType("TTL"));
        assertEquals(CommandType.KEY, RedisCommandNode.determineCommandType("PERSIST"));
        assertEquals(CommandType.KEY, RedisCommandNode.determineCommandType("TYPE"));
    }
    
    @Test
    void testConnectionCommandTypes() {
        assertEquals(CommandType.CONNECTION, RedisCommandNode.determineCommandType("PING"));
        assertEquals(CommandType.CONNECTION, RedisCommandNode.determineCommandType("ECHO"));
        assertEquals(CommandType.CONNECTION, RedisCommandNode.determineCommandType("SELECT"));
    }
    
    @Test
    void testServerCommandTypes() {
        assertEquals(CommandType.SERVER, RedisCommandNode.determineCommandType("FLUSHDB"));
        assertEquals(CommandType.SERVER, RedisCommandNode.determineCommandType("FLUSHALL"));
        assertEquals(CommandType.SERVER, RedisCommandNode.determineCommandType("INFO"));
        assertEquals(CommandType.SERVER, RedisCommandNode.determineCommandType("CONFIG"));
        assertEquals(CommandType.SERVER, RedisCommandNode.determineCommandType("DBSIZE"));
        assertEquals(CommandType.SERVER, RedisCommandNode.determineCommandType("KEYS"));
    }
    
    @Test
    void testCaseInsensitivity() {
        assertEquals(CommandType.STRING, RedisCommandNode.determineCommandType("get"));
        assertEquals(CommandType.STRING, RedisCommandNode.determineCommandType("Set"));
        assertEquals(CommandType.HASH, RedisCommandNode.determineCommandType("hget"));
        assertEquals(CommandType.CONNECTION, RedisCommandNode.determineCommandType("PING"));
    }
    
    @Test
    void testUnknownCommandType() {
        assertEquals(CommandType.UNKNOWN, RedisCommandNode.determineCommandType("UNKNOWN"));
        assertEquals(CommandType.UNKNOWN, RedisCommandNode.determineCommandType("INVALID"));
        assertEquals(CommandType.UNKNOWN, RedisCommandNode.determineCommandType(null));
        assertEquals(CommandType.UNKNOWN, RedisCommandNode.determineCommandType(""));
    }
}