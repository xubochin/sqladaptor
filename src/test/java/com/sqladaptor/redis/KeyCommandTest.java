package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class KeyCommandTest {
    
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
    void testKeyOperations() {
        System.out.println("[TEST START] testKeyOperations - 测试键操作(EXISTS/EXPIRE/TTL)");
        String key1 = "test:key1:" + System.currentTimeMillis();
        String key2 = "test:key2:" + System.currentTimeMillis();
        Jedis jedis = null;
        try {
            jedis = createConnection();
            
            jedis.set(key1, "value1");
            jedis.set(key2, "value2");
            
            Boolean exists1 = jedis.exists(key1);
            assertTrue(exists1);
            
            Boolean existsNonExistent = jedis.exists("non:existent:key");
            assertFalse(existsNonExistent);
            
            Long expireResult = jedis.expire(key1, 60);
            assertEquals(1L, expireResult);
            
            Long ttl = jedis.ttl(key1);
            assertTrue(ttl >= 0 && ttl <= 60);
    
            jedis.del(key1, key2);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testKeyOperations\n");
    }
    
    @Test
    void testMultipleKeyDeletion() {
        System.out.println("[TEST START] testMultipleKeyDeletion - 测试多键删除操作");
        String key1 = "test:multi1:" + System.currentTimeMillis();
        String key2 = "test:multi2:" + System.currentTimeMillis();
        String key3 = "test:multi3:" + System.currentTimeMillis();
        Jedis jedis = null;
        try {
            jedis = createConnection();
            
            jedis.set(key1, "value1");
            jedis.set(key2, "value2");
            jedis.set(key3, "value3");
            
            Long delResult = jedis.del(key1, key2, key3);
            assertEquals(3L, delResult);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testMultipleKeyDeletion\n");
    }
}