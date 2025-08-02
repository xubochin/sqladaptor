package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Set;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class SetCommandTest {
    
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
    void testSetOperations() {
        System.out.println("[TEST START] testSetOperations - 测试集合操作(SADD/SMEMBERS/SREM)");
        String key = "test:set:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // SADD操作
            Long saddResult = jedis.sadd(key, "member1", "member2", "member3");
            assertEquals(3L, saddResult);
            
            // SMEMBERS操作
            Set<String> smembersResult = jedis.smembers(key);
            assertEquals(3, smembersResult.size());
            assertTrue(smembersResult.contains("member1"));
            assertTrue(smembersResult.contains("member2"));
            assertTrue(smembersResult.contains("member3"));
            
            // SCARD操作
            Long scardResult = jedis.scard(key);
            assertEquals(3L, scardResult);
            
            // SREM操作
            Long sremResult = jedis.srem(key, "member1");
            assertEquals(1L, sremResult);
            assertEquals(2L, jedis.scard(key));
            
            // SISMEMBER操作
            Boolean sismemberResult1 = jedis.sismember(key, "member2");
            assertTrue(sismemberResult1);
            
            Boolean sismemberResult2 = jedis.sismember(key, "member1");
            assertFalse(sismemberResult2);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testSetOperations\n");
    }
    
    @Test
    void testSetUnionIntersectionOperations() {
        System.out.println("[TEST START] testSetUnionIntersectionOperations - 测试集合并集交集操作");
        String key1 = "test:set1:" + System.currentTimeMillis();
        String key2 = "test:set2:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            jedis.sadd(key1, "a", "b", "c");
            jedis.sadd(key2, "c", "d", "e");
            
            // SUNION操作
            Set<String> sunionResult = jedis.sunion(key1, key2);
            assertEquals(5, sunionResult.size());
            
            // SINTER操作
            Set<String> sinterResult = jedis.sinter(key1, key2);
            assertEquals(1, sinterResult.size());
            assertTrue(sinterResult.contains("c"));
            
            // SDIFF操作
            Set<String> sdiffResult = jedis.sdiff(key1, key2);
            assertEquals(2, sdiffResult.size());
            assertTrue(sdiffResult.contains("a"));
            assertTrue(sdiffResult.contains("b"));
            
            jedis.del(key1, key2);
        }
        System.out.println("[TEST END] testSetUnionIntersectionOperations\n");
    }
}