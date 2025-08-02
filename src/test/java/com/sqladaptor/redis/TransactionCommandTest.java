package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TransactionCommandTest {
    
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
    void testTransactionOperations() {
        System.out.println("[TEST START] testTransactionOperations - 测试事务操作(MULTI/EXEC/DISCARD)");
        String key1 = "test:tx1:" + System.currentTimeMillis();
        String key2 = "test:tx2:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // MULTI/EXEC操作
            Transaction tx = jedis.multi();
            tx.set(key1, "value1");
            tx.set(key2, "value2");
            List<Object> execResult = tx.exec();
            
            assertEquals(2, execResult.size());
            assertEquals("OK", execResult.get(0));
            assertEquals("OK", execResult.get(1));
            
            // 验证事务结果
            assertEquals("value1", jedis.get(key1));
            assertEquals("value2", jedis.get(key2));
            
            jedis.del(key1, key2);
        }
        System.out.println("[TEST END] testTransactionOperations\n");
    }
    
    @Test
    void testTransactionDiscard() {
        System.out.println("[TEST START] testTransactionDiscard - 测试事务丢弃操作");
        String key = "test:discard:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // MULTI/DISCARD操作
            Transaction tx = jedis.multi();
            tx.set(key, "value");
            String discardResult = tx.discard();
            
            assertEquals("OK", discardResult);
            
            // 验证事务被丢弃
            assertNull(jedis.get(key));
        }
        System.out.println("[TEST END] testTransactionDiscard\n");
    }
}