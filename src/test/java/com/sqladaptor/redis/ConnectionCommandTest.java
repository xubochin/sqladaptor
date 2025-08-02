package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ConnectionCommandTest {
    
    private static final String REDIS_URL = "redis://:ii%407zY%24s%266Dg6%2A@192.168.100.13:6379/0";

    private Jedis createConnection() {
        int maxRetries = 3;
        int retryDelay = 1000;
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                URI redisUri = URI.create(REDIS_URL);
                System.out.println("Connection string "+redisUri);
                Jedis jedis = new Jedis(redisUri, 300000, 300000);
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
        System.out.println("[TEST START] testConnection - 测试Redis基本连接");
        Jedis jedis = null;
        try {
            System.out.println("Testing Redis connection...");
            jedis = createConnection();

            assertNotNull(jedis);
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
    
    @Test
    void testConnectionOperations() {
        System.out.println("[TEST START] testConnectionOperations - 测试连接操作(PING/ECHO)");
        Jedis jedis = createConnection();
        
        assertNotNull(jedis);
        String pingResult1 = jedis.ping();
        assertEquals("PONG", pingResult1);

        String pingResult2 = jedis.ping("Hello");
        assertEquals("Hello", pingResult2);

        String echoResult = jedis.echo("Test Message");
        assertEquals("Test Message", echoResult);
        
        jedis.close();
        System.out.println("[TEST END] testConnectionOperations\n");
    }
    
    @Test
    void testPingCommand() {
        System.out.println("[TEST START] testPingCommand - 单独测试PING命令");
        Jedis jedis = null;
        try {
            URI redisUri = URI.create(REDIS_URL);
            jedis = new Jedis(redisUri, 5000, 5000);
            
            Thread.sleep(1000);
            
            System.out.println("测试简单PING命令...");
            long startTime = System.currentTimeMillis();
            String response1 = jedis.ping();
            long endTime = System.currentTimeMillis();
            System.out.println("PING响应: " + response1 + " (耗时: " + (endTime - startTime) + "ms)");
            assertEquals("PONG", response1);
            
            System.out.println("PING命令测试成功！");
        } catch (Exception e) {
            System.err.println("PING测试失败: " + e.getMessage());
            fail("PING命令测试失败: " + e.getMessage());
        } finally {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Exception e) {
                    System.err.println("关闭连接时出错: " + e.getMessage());
                }
            }
        }
        System.out.println("[TEST END] testPingCommand\n");
    }
}