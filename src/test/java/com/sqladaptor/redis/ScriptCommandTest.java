package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ScriptCommandTest {
    
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
    void testScriptOperations() {
        System.out.println("[TEST START] testScriptOperations - 测试脚本操作(EVAL/EVALSHA)");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // EVAL操作
            String script = "return redis.call('set', KEYS[1], ARGV[1])";
            String key = "test:script:" + System.currentTimeMillis();
            Object evalResult = jedis.eval(script, Arrays.asList(key), Arrays.asList("scriptValue"));
            assertEquals("OK", evalResult);
            
            // 验证脚本执行结果
            assertEquals("scriptValue", jedis.get(key));
            
            // SCRIPT LOAD操作
            String sha = jedis.scriptLoad(script);
            assertNotNull(sha);
            assertTrue(sha.length() > 0);
            
            // EVALSHA操作
            String key2 = "test:script2:" + System.currentTimeMillis();
            Object evalshaResult = jedis.evalsha(sha, Arrays.asList(key2), Arrays.asList("scriptValue2"));
            assertEquals("OK", evalshaResult);
            assertEquals("scriptValue2", jedis.get(key2));
            
            jedis.del(key, key2);
        }
        System.out.println("[TEST END] testScriptOperations\n");
    }
}