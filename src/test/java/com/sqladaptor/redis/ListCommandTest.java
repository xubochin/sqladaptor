package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ListCommandTest {
    
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
    void testListOperations() {
        System.out.println("[TEST START] testListOperations - 测试列表操作(LPUSH/RPUSH/LRANGE等)");
        String key = "test:list:" + System.currentTimeMillis();
        Jedis jedis = createConnection();
        
        Long lpushResult = jedis.lpush(key, "left1", "left2");
        assertEquals(2L, lpushResult);
        
        Long rpushResult = jedis.rpush(key, "right1", "right2");
        assertEquals(4L, rpushResult);
        
        Long llenResult = jedis.llen(key);
        assertEquals(4L, llenResult);
        
        List<String> lrangeResult = jedis.lrange(key, 0, -1);
        assertEquals(4, lrangeResult.size());
        assertEquals("left2", lrangeResult.get(0));
        assertEquals("left1", lrangeResult.get(1));
        assertEquals("right1", lrangeResult.get(2));
        assertEquals("right2", lrangeResult.get(3));
        
        String lpopResult = jedis.lpop(key);
        assertEquals("left2", lpopResult);
        
        String rpopResult = jedis.rpop(key);
        assertEquals("right2", rpopResult);
        
        assertEquals(2L, jedis.llen(key));
        
        jedis.del(key);
        jedis.close();
        System.out.println("[TEST END] testListOperations\n");
    }
}