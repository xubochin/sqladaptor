package com.sqladaptor.integration;

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
        String key = "test:incr:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            jedis.set(key, "10");
            Long incrResult = jedis.incr(key);
            assertEquals(11L, incrResult);
            
            Long decrResult = jedis.decr(key);
            assertEquals(10L, decrResult);
            
            jedis.del(key);
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

    @Test
    public void testStringFloatOperations() throws Exception {
        try (Jedis jedis = createConnection()) {
            // 测试 INCRBYFLOAT
            jedis.set("float_key", "10.5");
            Double result = jedis.incrByFloat("float_key", 2.5);
            assertEquals(13.0, result, 0.001);
            
            // 测试负数增量
            result = jedis.incrByFloat("float_key", -1.5);
            assertEquals(11.5, result, 0.001);
            
            // 测试不存在的键
            result = jedis.incrByFloat("new_float_key", 3.14);
            assertEquals(3.14, result, 0.001);
        }
    }

    @Test
    public void testStringGetSetOperations() throws Exception {
        try (Jedis jedis = createConnection()) {
            // 测试 GETSET
            jedis.set("getset_key", "old_value");
            String oldValue = jedis.getSet("getset_key", "new_value");
            assertEquals("old_value", oldValue);
            assertEquals("new_value", jedis.get("getset_key"));
            
            // 测试不存在的键
            String result = jedis.getSet("nonexistent_key", "value");
            assertNull(result);
            assertEquals("value", jedis.get("nonexistent_key"));
        }
    }

    @Test
    public void testStringExpirationOperations() throws Exception {
        try (Jedis jedis = createConnection()) {
            // 测试 SETEX (秒级过期)
            String result = jedis.setex("setex_key", 2, "expire_value");
            assertEquals("OK", result);
            assertEquals("expire_value", jedis.get("setex_key"));
            
            // 验证TTL设置
            Long ttl = jedis.ttl("setex_key");
            assertTrue(ttl > 0 && ttl <= 2);
            
            // 测试 PSETEX (毫秒级过期)
            result = jedis.psetex("psetex_key", 2000, "pexpire_value");
            assertEquals("OK", result);
            assertEquals("pexpire_value", jedis.get("psetex_key"));
            
            // 验证PTTL设置
            Long pttl = jedis.pttl("psetex_key");
            assertTrue(pttl > 0 && pttl <= 2000);
        }
    }

    @Test
    public void testStringConditionalOperations() throws Exception {
        try (Jedis jedis = createConnection()) {
            // 测试 SETNX - 键不存在时设置成功
            Long result = jedis.setnx("setnx_key", "first_value");
            assertEquals(1L, result.longValue());
            assertEquals("first_value", jedis.get("setnx_key"));
            
            // 测试 SETNX - 键存在时设置失败
            result = jedis.setnx("setnx_key", "second_value");
            assertEquals(0L, result.longValue());
            assertEquals("first_value", jedis.get("setnx_key")); // 值未改变
        }
    }

    @Test
    public void testStringBitOperations() throws Exception {
        try (Jedis jedis = createConnection()) {
            // 测试 SETBIT
            Boolean result = jedis.setbit("bit_key", 7, true);
            assertFalse(result); // 原来的位值为0
            
            result = jedis.setbit("bit_key", 0, true);
            assertFalse(result);
            
            // 测试 GETBIT
            Boolean bit = jedis.getbit("bit_key", 7);
            assertTrue(bit);
            
            bit = jedis.getbit("bit_key", 0);
            assertTrue(bit);
            
            bit = jedis.getbit("bit_key", 1);
            assertFalse(bit); // 未设置的位
            
            // 测试不存在键的位操作
            bit = jedis.getbit("nonexistent_bit_key", 0);
            assertFalse(bit);
        }
    }

    @Test
    public void testStringComplexScenarios() throws Exception {
        try (Jedis jedis = createConnection()) {
            // 综合场景：计数器 + 条件设置 + 位操作
            
            // 1. 使用SETNX创建计数器
            Long created = jedis.setnx("counter", "0");
            assertEquals(1L, created.longValue());
            
            // 2. 使用INCRBYFLOAT增加计数
            Double count = jedis.incrByFloat("counter", 1.5);
            assertEquals(1.5, count, 0.001);
            
            // 3. 使用GETSET获取并重置
            String oldCount = jedis.getSet("counter", "0");
            assertEquals("1.5", oldCount);
            
            // 4. 设置带过期时间的临时数据
            jedis.setex("temp_data", 1, "temporary");
            assertEquals("temporary", jedis.get("temp_data"));
            
            // 5. 位操作标记状态
            jedis.setbit("status_flags", 0, true); // 标记状态1
            jedis.setbit("status_flags", 2, true); // 标记状态3
            
            assertTrue(jedis.getbit("status_flags", 0));
            assertFalse(jedis.getbit("status_flags", 1));
            assertTrue(jedis.getbit("status_flags", 2));
        }
    }
}