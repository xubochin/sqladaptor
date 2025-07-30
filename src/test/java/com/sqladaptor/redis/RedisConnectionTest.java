package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Map;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class RedisConnectionTest {
    
    private static final String REDIS_URL = "redis://localhost:6379";
    private static final Object REDIS_LOCK = new Object();
    
    private Jedis createConnection() {
        int maxRetries = 3;
        int retryDelay = 1000; // 1秒
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                URI redisUri = URI.create(REDIS_URL);
                Jedis jedis = new Jedis(redisUri, 30000, 30000);
                // 测试连接
                jedis.ping();
                return jedis;
            } catch (Exception e) {
                System.out.println("Connection attempt " + (i + 1) + " failed: " + e.getMessage());
                if (i < maxRetries - 1) {
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    fail("Failed to connect to Redis after " + maxRetries + " attempts: " + e.getMessage());
                }
            }
        }
        return null;
    }
    
    @Test
    void testConnection() {
        synchronized (REDIS_LOCK) {
            System.out.println("[TEST START] testConnection - 测试Redis基本连接");
            Jedis jedis = null;
            try {
                System.out.println("Testing Redis connection...");
                jedis = createConnection();
                
                String response = jedis.ping();
                System.out.println("Ping response: " + response);
                assertEquals("PONG", response);
                System.out.println("Redis connection test passed!");
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            System.out.println("[TEST END] testConnection\n");
        }
    }
    
    @Test
    void testStringOperations() {
        System.out.println("[TEST START] testStringOperations - 测试字符串操作(SET/GET/DEL)");
        Jedis jedis = null;
        try {
            jedis = createConnection();
            String key = "test:string:" + System.currentTimeMillis();
            String value = "Hello Redis!";
            
            // SET操作
            String setResult = jedis.set(key, value);
            assertEquals("OK", setResult);
            
            // GET操作
            String getValue = jedis.get(key);
            assertEquals(value, getValue);
            
            // DEL操作
            Long delResult = jedis.del(key);
            assertEquals(1L, delResult);
            
            // 验证删除
            String deletedValue = jedis.get(key);
            assertNull(deletedValue);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testStringOperations\n");
    }
    
    @Test
    void testHashOperations() {
        System.out.println("[TEST START] testHashOperations - 测试哈希操作(HSET/HGET/HGETALL)");
        Jedis jedis = null;
        try {
            jedis = createConnection();
            String key = "test:hash:" + System.currentTimeMillis();
            String field1 = "field1";
            String value1 = "value1";
            String field2 = "field2";
            String value2 = "value2";
            
            // HSET操作
            Long hsetResult1 = jedis.hset(key, field1, value1);
            assertEquals(1L, hsetResult1);
            
            Long hsetResult2 = jedis.hset(key, field2, value2);
            assertEquals(1L, hsetResult2);
            
            // HGET操作
            String hgetValue1 = jedis.hget(key, field1);
            assertEquals(value1, hgetValue1);
            
            // HGETALL操作
            var hgetallResult = jedis.hgetAll(key);
            assertEquals(2, hgetallResult.size());
            assertEquals(value1, hgetallResult.get(field1));
            assertEquals(value2, hgetallResult.get(field2));
            
            // 清理
            jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testHashOperations\n");
    }
    
    @Test
    void testIncrementOperations() {
        System.out.println("[TEST START] testIncrementOperations - 测试增量操作(INCR/DECR)");
        String key = "test:counter:" + System.currentTimeMillis();
        Jedis jedis =  createConnection();
        // INCR操作
        Long incrResult1 = jedis.incr(key);
        assertEquals(1L, incrResult1);
        
        Long incrResult2 = jedis.incr(key);
        assertEquals(2L, incrResult2);
        
        // DECR操作
        Long decrResult = jedis.decr(key);
        assertEquals(1L, decrResult);
        
        // 清理
        jedis.del(key);
        System.out.println("[TEST END] testIncrementOperations\n");
    }
    
    @Test
    void testKeyOperations() {
        System.out.println("[TEST START] testKeyOperations - 测试键操作(EXISTS/EXPIRE/TTL)");
        String key1 = "test:key1:" + System.currentTimeMillis();
        String key2 = "test:key2:" + System.currentTimeMillis();
        Jedis jedis =  createConnection();
        // 设置一些键
        jedis.set(key1, "value1");
        jedis.set(key2, "value2");
        
        // EXISTS操作
        Boolean exists1 = jedis.exists(key1);
        assertTrue(exists1);
        
        Boolean existsNonExistent = jedis.exists("non:existent:key");
        assertFalse(existsNonExistent);
        
        // EXPIRE操作
        Long expireResult = jedis.expire(key1, 60); // 60秒过期
        assertEquals(1L, expireResult);
        
        // TTL操作
        Long ttl = jedis.ttl(key1);
        assertTrue(ttl >= 0 && ttl <= 60);

        // 清理
        jedis.del(key1, key2);
        System.out.println("[TEST END] testKeyOperations\n");
    }
    
    @Test
    void testMultipleKeyDeletion() {
        System.out.println("[TEST START] testMultipleKeyDeletion - 测试多键删除操作");
        String key1 = "test:multi1:" + System.currentTimeMillis();
        String key2 = "test:multi2:" + System.currentTimeMillis();
        String key3 = "test:multi3:" + System.currentTimeMillis();
        Jedis jedis =  createConnection();
        // 设置多个键
        jedis.set(key1, "value1");
        jedis.set(key2, "value2");
        jedis.set(key3, "value3");
        
        // 批量删除
        Long delResult = jedis.del(key1, key2, key3);
        assertEquals(3L, delResult);
        System.out.println("[TEST END] testMultipleKeyDeletion\n");
    }
    
    @Test
    void testAdvancedStringOperations() {
        System.out.println("[TEST START] testAdvancedStringOperations - 测试高级字符串操作(APPEND/STRLEN/GETRANGE等)");
        String key = "test:advanced:" + System.currentTimeMillis();
        Jedis jedis =  createConnection();
        // APPEND操作
        jedis.set(key, "Hello");
        Long appendResult = jedis.append(key, " World");
        assertEquals(11L, appendResult);
        assertEquals("Hello World", jedis.get(key));
        
        // STRLEN操作
        Long strlenResult = jedis.strlen(key);
        assertEquals(11L, strlenResult);
        
        // GETRANGE操作
        String rangeResult = jedis.getrange(key, 0, 4);
        assertEquals("Hello", rangeResult);
        
        // SETRANGE操作
        Long setrangeResult = jedis.setrange(key, 6, "Redis");
        assertEquals(11L, setrangeResult);
        assertEquals("Hello Redis", jedis.get(key));
        
        // INCRBY和DECRBY操作
        String numKey = "test:num:" + System.currentTimeMillis();
        jedis.set(numKey, "10");
        Long incrbyResult = jedis.incrBy(numKey, 5);
        assertEquals(15L, incrbyResult);
        
        Long decrbyResult = jedis.decrBy(numKey, 3);
        assertEquals(12L, decrbyResult);
        
        // 清理
        jedis.del(key, numKey);
        System.out.println("[TEST END] testAdvancedStringOperations\n");
    }
    
    @Test
    void testBatchStringOperations() {
        System.out.println("[TEST START] testBatchStringOperations - 测试批量字符串操作(MSET/MGET/MSETNX)");
        String key1 = "test:batch1:" + System.currentTimeMillis();
        String key2 = "test:batch2:" + System.currentTimeMillis();
        String key3 = "test:batch3:" + System.currentTimeMillis();
        Jedis jedis =  createConnection();
        // MSET操作
        String msetResult = jedis.mset(key1, "value1", key2, "value2", key3, "value3");
        assertEquals("OK", msetResult);
        
        // MGET操作
        List<String> mgetResult = jedis.mget(key1, key2, key3);
        assertEquals(3, mgetResult.size());
        assertEquals("value1", mgetResult.get(0));
        assertEquals("value2", mgetResult.get(1));
        assertEquals("value3", mgetResult.get(2));
        
        // MSETNX操作（应该失败，因为键已存在）
        String key4 = "test:batch4:" + System.currentTimeMillis();
        Long msetnxResult = jedis.msetnx(key1, "newvalue1", key4, "value4");
        assertEquals(0L, msetnxResult); // 失败，因为key1已存在
        
        // 清理
        jedis.del(key1, key2, key3, key4);
        System.out.println("[TEST END] testBatchStringOperations\n");
    }
    
    @Test
    void testAdvancedHashOperations() {
        System.out.println("[TEST START] testAdvancedHashOperations - 测试高级哈希操作(HKEYS/HVALS/HDEL等)");
        String key = "test:advhash:" + System.currentTimeMillis();
        Jedis jedis =  createConnection();
        // HKEYS操作
        jedis.hset(key, "field1", "value1");
        jedis.hset(key, "field2", "value2");
        jedis.hset(key, "field3", "value3");
        
        Set<String> hkeysResult = jedis.hkeys(key);
        assertEquals(3, hkeysResult.size());
        assertTrue(hkeysResult.contains("field1"));
        assertTrue(hkeysResult.contains("field2"));
        assertTrue(hkeysResult.contains("field3"));
        
        // HVALS操作
        List<String> hvalsResult = jedis.hvals(key);
        assertEquals(3, hvalsResult.size());
        assertTrue(hvalsResult.contains("value1"));
        assertTrue(hvalsResult.contains("value2"));
        assertTrue(hvalsResult.contains("value3"));
        
        // HDEL操作
        Long hdelResult = jedis.hdel(key, "field1", "field2");
        assertEquals(2L, hdelResult);
        
        // 验证删除
        assertFalse(jedis.hexists(key, "field1"));
        assertFalse(jedis.hexists(key, "field2"));
        assertTrue(jedis.hexists(key, "field3"));
        
        // 清理
        jedis.del(key);
        System.out.println("[TEST END] testAdvancedHashOperations\n");
    }
    
    @Test
    void testListOperations() {
        System.out.println("[TEST START] testListOperations - 测试列表操作(LPUSH/RPUSH/LRANGE等)");
        String key = "test:list:" + System.currentTimeMillis();
        Jedis jedis =  createConnection();
        // LPUSH和RPUSH操作
        Long lpushResult = jedis.lpush(key, "left1", "left2");
        assertEquals(2L, lpushResult);
        
        Long rpushResult = jedis.rpush(key, "right1", "right2");
        assertEquals(4L, rpushResult);
        
        // LLEN操作
        Long llenResult = jedis.llen(key);
        assertEquals(4L, llenResult);
        
        // LRANGE操作
        List<String> lrangeResult = jedis.lrange(key, 0, -1);
        assertEquals(4, lrangeResult.size());
        assertEquals("left2", lrangeResult.get(0)); // 最后push的在前面
        assertEquals("left1", lrangeResult.get(1));
        assertEquals("right1", lrangeResult.get(2));
        assertEquals("right2", lrangeResult.get(3));
        
        // LPOP和RPOP操作
        String lpopResult = jedis.lpop(key);
        assertEquals("left2", lpopResult);
        
        String rpopResult = jedis.rpop(key);
        assertEquals("right2", rpopResult);
        
        // 验证剩余元素
        assertEquals(2L, jedis.llen(key));
        
        // 清理
        jedis.del(key);
        System.out.println("[TEST END] testListOperations\n");
    }
    
    @Test
    void testConnectionOperations() {
        System.out.println("[TEST START] testConnectionOperations - 测试连接操作(PING/ECHO)");
        Jedis jedis =  createConnection();
        // PING操作
        String pingResult1 = jedis.ping();
        assertEquals("PONG", pingResult1);
        
        // PING with message
        String pingResult2 = jedis.ping("Hello");
        assertEquals("Hello", pingResult2);
        
        // ECHO操作
        String echoResult = jedis.echo("Test Message");
        assertEquals("Test Message", echoResult);
        System.out.println("[TEST END] testConnectionOperations\n");
    }
}
