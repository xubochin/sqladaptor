package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class StringCommandTest {
    
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
    void testStringOperations() {
        System.out.println("[TEST START] testStringOperations - 测试字符串操作(SET/GET/DEL)");
        Jedis jedis = null;
        try {
            jedis = createConnection();
            String key = "test:string:" + System.currentTimeMillis();
            String value = "Hello Redis!";
            
            String setResult = jedis.set(key, value);
            assertEquals("OK", setResult);
            
            String getValue = jedis.get(key);
            assertEquals(value, getValue);
            
            Long delResult = jedis.del(key);
            assertEquals(1L, delResult);
            
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
    void testIncrementOperations() {
        System.out.println("[TEST START] testIncrementOperations - 测试增量操作(INCR/DECR)");
        String key = "test:counter:" + System.currentTimeMillis();
        Jedis jedis = null;
        try {
            jedis = createConnection();
            
            Long incrResult1 = jedis.incr(key);
            assertEquals(1L, incrResult1);
            
            Long incrResult2 = jedis.incr(key);
            assertEquals(2L, incrResult2);
            
            Long decrResult = jedis.decr(key);
            assertEquals(1L, decrResult);
            
            jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testIncrementOperations\n");
    }
    
    @Test
    void testAdvancedStringOperations() {
        System.out.println("[TEST START] testAdvancedStringOperations - 测试高级字符串操作(APPEND/STRLEN/GETRANGE等)");
        String key = "test:advanced:" + System.currentTimeMillis();
        Jedis jedis = null;
        try {
            jedis = createConnection();
            
            jedis.set(key, "Hello");
            Long appendResult = jedis.append(key, " World");
            assertEquals(11L, appendResult);
            assertEquals("Hello World", jedis.get(key));
            
            Long strlenResult = jedis.strlen(key);
            assertEquals(11L, strlenResult);
            
            String rangeResult = jedis.getrange(key, 0, 4);
            assertEquals("Hello", rangeResult);
            
            Long setrangeResult = jedis.setrange(key, 6, "Redis");
            assertEquals(11L, setrangeResult);
            assertEquals("Hello Redis", jedis.get(key));
            
            String numKey = "test:num:" + System.currentTimeMillis();
            jedis.set(numKey, "10");
            Long incrbyResult = jedis.incrBy(numKey, 5);
            assertEquals(15L, incrbyResult);
            
            Long decrbyResult = jedis.decrBy(numKey, 3);
            assertEquals(12L, decrbyResult);
            
            jedis.del(key, numKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testAdvancedStringOperations\n");
    }
    
    @Test
    void testBatchStringOperations() {
        System.out.println("[TEST START] testBatchStringOperations - 测试批量字符串操作(MSET/MGET/MSETNX)");
        String key1 = "test:batch1:" + System.currentTimeMillis();
        String key2 = "test:batch2:" + System.currentTimeMillis();
        String key3 = "test:batch3:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            String msetResult = jedis.mset(key1, "value1", key2, "value2", key3, "value3");
            assertEquals("OK", msetResult);

            List<String> mgetResult = jedis.mget(key1, key2, key3);
            assertEquals(3, mgetResult.size());
            assertEquals("value1", mgetResult.get(0));
            assertEquals("value2", mgetResult.get(1));
            assertEquals("value3", mgetResult.get(2));

            String key4 = "test:batch4:" + System.currentTimeMillis();
            Long msetnxResult = jedis.msetnx(key1, "newvalue1", key4, "value4");
            assertEquals(0L, msetnxResult);

            jedis.del(key1, key2, key3, key4);
        }
        System.out.println("[TEST END] testBatchStringOperations\n");
    }
}