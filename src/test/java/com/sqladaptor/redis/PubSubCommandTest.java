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
import java.util.List;
import java.util.Map;

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

    @Test
    void testUnsubscribeOperations() {
        System.out.println("[TEST START] testUnsubscribeOperations - 测试取消订阅操作(UNSUBSCRIBE)");
        String channel1 = "test:channel1:" + System.currentTimeMillis();
        String channel2 = "test:channel2:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 订阅多个频道
            CountDownLatch subscribeLatch = new CountDownLatch(2);
            JedisPubSub pubSub = new JedisPubSub() {
                @Override
                public void onSubscribe(String channel, int subscribedChannels) {
                    subscribeLatch.countDown();
                }
                
                @Override
                public void onUnsubscribe(String channel, int subscribedChannels) {
                    // 处理取消订阅
                }
            };
            
            Thread subscriberThread = new Thread(() -> {
                jedis.subscribe(pubSub, channel1, channel2);
            });
            subscriberThread.start();
            
            // 等待订阅建立
            boolean subscribed = subscribeLatch.await(3, TimeUnit.SECONDS);
            assertTrue(subscribed);
            
            // 取消订阅单个频道
            pubSub.unsubscribe(channel1);
            Thread.sleep(500);
            
            // 取消订阅所有频道
            pubSub.unsubscribe();
            
            subscriberThread.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted: " + e.getMessage());
        }
        System.out.println("[TEST END] testUnsubscribeOperations\n");
    }
    
    @Test
    void testPatternSubscribeOperations() {
        System.out.println("[TEST START] testPatternSubscribeOperations - 测试模式订阅操作(PSUBSCRIBE/PUNSUBSCRIBE)");
        String pattern = "test:pattern:*";
        String channel = "test:pattern:" + System.currentTimeMillis();
        
        try (Jedis publisher = createConnection(); Jedis subscriber = createConnection()) {
            assertNotNull(publisher);
            assertNotNull(subscriber);
            
            CountDownLatch latch = new CountDownLatch(1);
            final String[] receivedMessage = new String[1];
            final String[] receivedChannel = new String[1];
            
            JedisPubSub pubSub = new JedisPubSub() {
                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    receivedMessage[0] = message;
                    receivedChannel[0] = channel;
                    latch.countDown();
                    punsubscribe();
                }
            };
            
            Thread subscriberThread = new Thread(() -> {
                subscriber.psubscribe(pubSub, pattern);
            });
            subscriberThread.start();
            
            // 等待模式订阅建立
            Thread.sleep(1000);
            
            // 发布消息到匹配模式的频道
            Long publishResult = publisher.publish(channel, "Hello Pattern!");
            assertTrue(publishResult >= 0);
            
            // 等待消息接收
            boolean received = latch.await(5, TimeUnit.SECONDS);
            assertTrue(received);
            assertEquals("Hello Pattern!", receivedMessage[0]);
            assertEquals(channel, receivedChannel[0]);
            
            subscriberThread.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted: " + e.getMessage());
        }
        System.out.println("[TEST END] testPatternSubscribeOperations\n");
    }
    
    @Test
    void testPubSubInfoOperations() {
        System.out.println("[TEST START] testPubSubInfoOperations - 测试发布订阅信息查询(PUBSUB)");
        String channel = "test:info:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 测试 PUBSUB CHANNELS
            List<String> channels = jedis.pubsubChannels("test:*");
            assertNotNull(channels);
            
            // 测试 PUBSUB NUMSUB
            Map<String, Long> numSub = jedis.pubsubNumSub(channel);
            assertNotNull(numSub);
            assertTrue(numSub.containsKey(channel));
            
            // 测试 PUBSUB NUMPAT
            Long numPat = jedis.pubsubNumPat();
            assertNotNull(numPat);
            assertTrue(numPat >= 0);
            
        }
        System.out.println("[TEST END] testPubSubInfoOperations\n");
    }
    
    @Test
    void testMultiplePublishersSubscribers() {
        System.out.println("[TEST START] testMultiplePublishersSubscribers - 测试多发布者多订阅者");
        String channel = "test:multi:" + System.currentTimeMillis();
        
        try (Jedis publisher1 = createConnection(); 
             Jedis publisher2 = createConnection();
             Jedis subscriber1 = createConnection();
             Jedis subscriber2 = createConnection()) {
            
            assertNotNull(publisher1);
            assertNotNull(publisher2);
            assertNotNull(subscriber1);
            assertNotNull(subscriber2);
            
            CountDownLatch latch = new CountDownLatch(4); // 2个订阅者 × 2条消息
            
            JedisPubSub pubSub1 = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    latch.countDown();
                }
            };
            
            JedisPubSub pubSub2 = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    latch.countDown();
                }
            };
            
            // 启动订阅者
            Thread subscriber1Thread = new Thread(() -> {
                subscriber1.subscribe(pubSub1, channel);
            });
            Thread subscriber2Thread = new Thread(() -> {
                subscriber2.subscribe(pubSub2, channel);
            });
            
            subscriber1Thread.start();
            subscriber2Thread.start();
            
            // 等待订阅建立
            Thread.sleep(1000);
            
            // 多个发布者发布消息
            Long result1 = publisher1.publish(channel, "Message from Publisher 1");
            Long result2 = publisher2.publish(channel, "Message from Publisher 2");
            
            assertTrue(result1 >= 0);
            assertTrue(result2 >= 0);
            
            // 等待所有消息接收
            boolean allReceived = latch.await(5, TimeUnit.SECONDS);
            assertTrue(allReceived);
            
            pubSub1.unsubscribe();
            pubSub2.unsubscribe();
            subscriber1Thread.interrupt();
            subscriber2Thread.interrupt();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted: " + e.getMessage());
        }
        System.out.println("[TEST END] testMultiplePublishersSubscribers\n");
    }
}