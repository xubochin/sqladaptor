package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class SortedSetCommandTest {
    
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
    void testSortedSetOperations() {
        System.out.println("[TEST START] testSortedSetOperations - 测试有序集合操作(ZADD/ZRANGE/ZREM)");
        String key = "test:zset:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // ZADD操作
            Long zaddResult = jedis.zadd(key, 1.0, "member1");
            assertEquals(1L, zaddResult);
            jedis.zadd(key, 2.0, "member2");
            jedis.zadd(key, 3.0, "member3");
            
            // ZCARD操作
            Long zcardResult = jedis.zcard(key);
            assertEquals(3L, zcardResult);
            
            // ZRANGE操作
            List<String> zrangeResult = jedis.zrange(key, 0, -1);
            assertEquals(3, zrangeResult.size());
            assertEquals("member1", zrangeResult.get(0));
            assertEquals("member2", zrangeResult.get(1));
            assertEquals("member3", zrangeResult.get(2));
            
            // ZSCORE操作
            Double zscoreResult = jedis.zscore(key, "member2");
            assertEquals(2.0, zscoreResult);
            
            // ZREM操作
            Long zremResult = jedis.zrem(key, "member1");
            assertEquals(1L, zremResult);
            assertEquals(2L, jedis.zcard(key));
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testSortedSetOperations\n");
    }
    
    @Test
    void testSortedSetRangeOperations() {
        System.out.println("[TEST START] testSortedSetRangeOperations - 测试有序集合范围操作");
        String key = "test:zset:range:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            jedis.zadd(key, 10.0, "member1");
            jedis.zadd(key, 20.0, "member2");
            jedis.zadd(key, 30.0, "member3");
            jedis.zadd(key, 40.0, "member4");
            
            // ZRANGEBYSCORE操作
            List<String> zrangebyscoreResult = jedis.zrangeByScore(key, 15.0, 35.0);
            assertEquals(2, zrangebyscoreResult.size());
            assertTrue(zrangebyscoreResult.contains("member2"));
            assertTrue(zrangebyscoreResult.contains("member3"));
            
            // ZCOUNT操作
            Long zcountResult = jedis.zcount(key, 15.0, 35.0);
            assertEquals(2L, zcountResult);
            
            // ZRANK操作
            Long zrankResult = jedis.zrank(key, "member3");
            assertEquals(2L, zrankResult);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testSortedSetRangeOperations\n");
    }
}