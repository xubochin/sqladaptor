package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import java.net.URI;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ServerCommandTest {
    
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
    void testServerOperations() {
        System.out.println("[TEST START] testServerOperations - 测试服务器操作(INFO/DBSIZE/FLUSHDB)");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // INFO操作
            String infoResult = jedis.info();
            assertNotNull(infoResult);
            assertTrue(infoResult.length() > 0);
            
            // DBSIZE操作
            Long dbsizeResult = jedis.dbSize();
            assertNotNull(dbsizeResult);
            assertTrue(dbsizeResult >= 0);
            
            // TIME操作
            List<String> timeResult = jedis.time();
            assertEquals(2, timeResult.size());
            
        }
        System.out.println("[TEST END] testServerOperations\n");
    }
}