package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class HashCommandTest {
    
    private static final String REDIS_URL = "redis://:ii%407zY%24s%266Dg6%2A@192.168.100.13:6379/0";

    private Jedis createConnection() {
        int maxRetries = 3;
        int retryDelay = 1000;
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                URI redisUri = URI.create(REDIS_URL);
                Jedis jedis = new Jedis(redisUri, 300000, 300000);
                jedis.ping();
                return jedis;
            } catch (Exception e) {
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
            
            Long hsetResult1 = jedis.hset(key, field1, value1);
            assertEquals(1L, hsetResult1);
            
            Long hsetResult2 = jedis.hset(key, field2, value2);
            assertEquals(1L, hsetResult2);
            
            String hgetValue1 = jedis.hget(key, field1);
            assertEquals(value1, hgetValue1);
            
            var hgetallResult = jedis.hgetAll(key);
            assertEquals(2, hgetallResult.size());
            assertEquals(value1, hgetallResult.get(field1));
            assertEquals(value2, hgetallResult.get(field2));
            
            jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testHashOperations\n");
    }
    
    @Test
    void testAdvancedHashOperations() {
        System.out.println("[TEST START] testAdvancedHashOperations - 测试高级哈希操作(HKEYS/HVALS/HDEL等)");
        String key = "test:advhash:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            jedis.hset(key, "field1", "value1");
            jedis.hset(key, "field2", "value2");
            jedis.hset(key, "field3", "value3");

            Set<String> hkeysResult = jedis.hkeys(key);
            assertEquals(3, hkeysResult.size());
            assertTrue(hkeysResult.contains("field1"));
            assertTrue(hkeysResult.contains("field2"));
            assertTrue(hkeysResult.contains("field3"));

            List<String> hvalsResult = jedis.hvals(key);
            assertEquals(3, hvalsResult.size());
            assertTrue(hvalsResult.contains("value1"));
            assertTrue(hvalsResult.contains("value2"));
            assertTrue(hvalsResult.contains("value3"));

            Long hdelResult = jedis.hdel(key, "field1", "field2");
            assertEquals(2L, hdelResult);

            assertFalse(jedis.hexists(key, "field1"));
            assertFalse(jedis.hexists(key, "field2"));
            assertTrue(jedis.hexists(key, "field3"));

            jedis.del(key);
        }
        System.out.println("[TEST END] testAdvancedHashOperations\n");
    }
    
    @Test
    void testHashIncrByOperations() {
        System.out.println("[TEST START] testHashIncrByOperations - 测试HINCRBY命令");
        String key = "test:hincrby:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 初始设置一个数值字段
            jedis.hset(key, "counter", "10");
            
            // HINCRBY操作 - 递增字段值
            Long incrResult1 = jedis.hincrBy(key, "counter", 5);
            assertEquals(15L, incrResult1);
            
            // 再次递增
            Long incrResult2 = jedis.hincrBy(key, "counter", -3);
            assertEquals(12L, incrResult2);
            
            // 对不存在的字段进行递增（应该从0开始）
            Long incrResult3 = jedis.hincrBy(key, "newcounter", 100);
            assertEquals(100L, incrResult3);
            
            // 验证最终值
            String finalValue1 = jedis.hget(key, "counter");
            assertEquals("12", finalValue1);
            
            String finalValue2 = jedis.hget(key, "newcounter");
            assertEquals("100", finalValue2);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testHashIncrByOperations\n");
    }
    
    @Test
    void testHashLenOperations() {
        System.out.println("[TEST START] testHashLenOperations - 测试HLEN命令");
        String key = "test:hlen:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 空哈希的长度应该为0
            Long emptyLen = jedis.hlen(key);
            assertEquals(0L, emptyLen);
            
            // 添加字段并测试长度
            jedis.hset(key, "field1", "value1");
            Long len1 = jedis.hlen(key);
            assertEquals(1L, len1);
            
            jedis.hset(key, "field2", "value2");
            jedis.hset(key, "field3", "value3");
            Long len3 = jedis.hlen(key);
            assertEquals(3L, len3);
            
            // 删除一个字段后测试长度
            jedis.hdel(key, "field2");
            Long len2 = jedis.hlen(key);
            assertEquals(2L, len2);
            
            // 更新现有字段不应该改变长度
            jedis.hset(key, "field1", "newvalue1");
            Long lenAfterUpdate = jedis.hlen(key);
            assertEquals(2L, lenAfterUpdate);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testHashLenOperations\n");
    }
}