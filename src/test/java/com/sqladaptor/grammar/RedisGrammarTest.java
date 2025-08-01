package com.sqladaptor.grammar;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.parser.RedisCommandParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class RedisGrammarTest {
    
    private RedisCommandParser parser;
    
    @BeforeEach
    void setUp() {
        parser = new RedisCommandParser();
    }
    
    @Test
    void testStringCommands() {
        // 基础字符串命令
        testCommand("GET key1", "GET", Arrays.asList("key1"));
        testCommand("SET key1 value1", "SET", Arrays.asList("key1", "value1"));
        testCommand("DEL key1 key2", "DEL", Arrays.asList("key1", "key2"));
        testCommand("INCR counter", "INCR", Arrays.asList("counter"));
        testCommand("DECR counter", "DECR", Arrays.asList("counter"));
        testCommand("INCRBY counter 5", "INCRBY", Arrays.asList("counter", "5"));
        testCommand("DECRBY counter 3", "DECRBY", Arrays.asList("counter", "3"));
        
        // 高级字符串命令
        testCommand("APPEND key value", "APPEND", Arrays.asList("key", "value"));
        testCommand("STRLEN key", "STRLEN", Arrays.asList("key"));
        testCommand("GETRANGE key 0 10", "GETRANGE", Arrays.asList("key", "0", "10"));
        testCommand("SETRANGE key 5 value", "SETRANGE", Arrays.asList("key", "5", "value"));
        
        // 批量字符串命令
        testCommand("MGET key1 key2 key3", "MGET", Arrays.asList("key1", "key2", "key3"));
        testCommand("MSET key1 value1 key2 value2", "MSET", Arrays.asList("key1", "value1", "key2", "value2"));
        testCommand("MSETNX key1 value1 key2 value2", "MSETNX", Arrays.asList("key1", "value1", "key2", "value2"));
    }
    
    @Test
    void testHashCommands() {
        testCommand("HGET hash field", "HGET", Arrays.asList("hash", "field"));
        testCommand("HSET hash field value", "HSET", Arrays.asList("hash", "field", "value"));
        testCommand("HDEL hash field1 field2", "HDEL", Arrays.asList("hash", "field1", "field2"));
        testCommand("HGETALL hash", "HGETALL", Arrays.asList("hash"));
        testCommand("HKEYS hash", "HKEYS", Arrays.asList("hash"));
        testCommand("HVALS hash", "HVALS", Arrays.asList("hash"));
    }
    
    @Test
    void testListCommands() {
        testCommand("LPUSH list value1 value2", "LPUSH", Arrays.asList("list", "value1", "value2"));
        testCommand("RPUSH list value1 value2", "RPUSH", Arrays.asList("list", "value1", "value2"));
        testCommand("LPOP list", "LPOP", Arrays.asList("list"));
        testCommand("RPOP list", "RPOP", Arrays.asList("list"));
        testCommand("LLEN list", "LLEN", Arrays.asList("list"));
        testCommand("LRANGE list 0 10", "LRANGE", Arrays.asList("list", "0", "10"));
    }
    
    @Test
    void testSetCommands() {
        testCommand("SADD set member1 member2", "SADD", Arrays.asList("set", "member1", "member2"));
        testCommand("SREM set member1 member2", "SREM", Arrays.asList("set", "member1", "member2"));
        testCommand("SMEMBERS set", "SMEMBERS", Arrays.asList("set"));
        testCommand("SCARD set", "SCARD", Arrays.asList("set"));
        testCommand("SISMEMBER set member", "SISMEMBER", Arrays.asList("set", "member"));
    }
    
    @Test
    void testSortedSetCommands() {
        testCommand("ZADD zset 1.0 member1 2.0 member2", "ZADD", Arrays.asList("zset", "1.0", "member1", "2.0", "member2"));
        testCommand("ZREM zset member1 member2", "ZREM", Arrays.asList("zset", "member1", "member2"));
        testCommand("ZRANGE zset 0 10", "ZRANGE", Arrays.asList("zset", "0", "10"));
        testCommand("ZCARD zset", "ZCARD", Arrays.asList("zset"));
        testCommand("ZSCORE zset member", "ZSCORE", Arrays.asList("zset", "member"));
    }
    
    @Test
    void testKeyCommands() {
        testCommand("EXISTS key1 key2", "EXISTS", Arrays.asList("key1", "key2"));
        testCommand("EXPIRE key 60", "EXPIRE", Arrays.asList("key", "60"));
        testCommand("TTL key", "TTL", Arrays.asList("key"));
        testCommand("PERSIST key", "PERSIST", Arrays.asList("key"));
        testCommand("EXPIREAT key 1234567890", "EXPIREAT", Arrays.asList("key", "1234567890"));
        testCommand("PEXPIRE key 60000", "PEXPIRE", Arrays.asList("key", "60000"));
        testCommand("PEXPIREAT key 1234567890000", "PEXPIREAT", Arrays.asList("key", "1234567890000"));
        testCommand("PTTL key", "PTTL", Arrays.asList("key"));
        testCommand("TYPE key", "TYPE", Arrays.asList("key"));
    }
    
    @Test
    void testConnectionCommands() {
        testCommand("PING", "PING", Arrays.asList());
        testCommand("PING message", "PING", Arrays.asList("message"));
        testCommand("ECHO message", "ECHO", Arrays.asList("message"));
        testCommand("SELECT 1", "SELECT", Arrays.asList("1"));
    }
    
    @Test
    void testServerCommands() {
        testCommand("FLUSHDB", "FLUSHDB", Arrays.asList());
        testCommand("FLUSHALL", "FLUSHALL", Arrays.asList());
        testCommand("INFO", "INFO", Arrays.asList());
        testCommand("INFO server", "INFO", Arrays.asList("server"));
        testCommand("CONFIG GET parameter", "CONFIG", Arrays.asList("GET", "parameter"));
        testCommand("CONFIG SET parameter value", "CONFIG", Arrays.asList("SET", "parameter", "value"));
        testCommand("DBSIZE", "DBSIZE", Arrays.asList());
        testCommand("KEYS pattern", "KEYS", Arrays.asList("pattern"));
    }
    
    @Test
    void testCaseInsensitivity() {
        // 测试大小写不敏感
        testCommand("get key", "GET", Arrays.asList("key"));
        testCommand("SET key value", "SET", Arrays.asList("key", "value"));
        testCommand("HgEt hash field", "HGET", Arrays.asList("hash", "field"));
        testCommand("ping", "PING", Arrays.asList());
    }
    
    @Test
    void testQuotedStrings() {
        // 测试带引号的字符串
        testCommand("SET \"key with spaces\" \"value with spaces\"", "SET", Arrays.asList("key with spaces", "value with spaces"));
        testCommand("ECHO \"Hello World\"", "ECHO", Arrays.asList("Hello World"));
    }
    
    @Test
    void testNumberArguments() {
        // 测试数字参数
        testCommand("INCRBY counter 42", "INCRBY", Arrays.asList("counter", "42"));
        testCommand("ZADD zset 3.14 member", "ZADD", Arrays.asList("zset", "3.14", "member"));
        testCommand("EXPIRE key 3600", "EXPIRE", Arrays.asList("key", "3600"));
    }
    
    @Test
    void testRespArrayFormat() {
        // 测试RESP数组格式
        String respArray = "*3\r\n$3\r\nSET\r\n$3\r\nkey\r\n$5\r\nvalue\r\n";
        RedisCommandNode node = parser.parse(respArray);
        assertEquals("SET", node.getCommand());
        assertEquals(2, node.getArguments().size());
        assertEquals("key", node.getArguments().get(0));
        assertEquals("value", node.getArguments().get(1));
    }
    
    private void testCommand(String input, String expectedCommand, java.util.List<String> expectedArgs) {
        RedisCommandNode node = parser.parse(input);
        assertEquals(expectedCommand, node.getCommand());
        assertEquals(expectedArgs.size(), node.getArguments().size());
        for (int i = 0; i < expectedArgs.size(); i++) {
            assertEquals(expectedArgs.get(i), node.getArguments().get(i));
        }
    }
}