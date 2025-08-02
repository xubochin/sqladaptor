package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class PubSubCommandTest {
    
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
    void testPubSubOperations() {
        System.out.println("[TEST START] testPubSubOperations - 测试发布订阅操作(PUBLISH/SUBSCRIBE)");
        String channel = "test:channel:" + System.currentTimeMillis();
        
        try (Jedis publisher = createConnection(); Jedis subscriber = createConnection()) {
            assertNotNull(publisher);
            assertNotNull(subscriber);
            
            CountDownLatch latch = new CountDownLatch(1);
            final String[] receivedMessage = new String[1];
            
            // 订阅者
            JedisPubSub pubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    receivedMessage[0] = message;
                    latch.countDown();
                    unsubscribe();
                }
            };
            
            // 在新线程中订阅
            Thread subscriberThread = new Thread(() -> {
                subscriber.subscribe(pubSub, channel);
            });
            subscriberThread.start();
            
            // 等待订阅建立
            Thread.sleep(1000);
            
            // 发布消息
            Long publishResult = publisher.publish(channel, "Hello PubSub!");
            assertTrue(publishResult >= 0);
            
            // 等待消息接收
            boolean received = latch.await(5, TimeUnit.SECONDS);
            assertTrue(received);
            assertEquals("Hello PubSub!", receivedMessage[0]);
            
            subscriberThread.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted: " + e.getMessage());
        }
        System.out.println("[TEST END] testPubSubOperations\n");
    }
}