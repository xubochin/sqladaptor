package com.sqladaptor.redis;

import com.sqladaptor.BaseIntegrationTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class PubSubCommandTest extends BaseIntegrationTest {
    
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
        
        try (Jedis publisher = createConnection(); Jedis subscriber = createConnection()) {
            assertNotNull(publisher);
            assertNotNull(subscriber);
            
            CountDownLatch latch = new CountDownLatch(2);
            final int[] messageCount = {0};
            
            JedisPubSub pubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    messageCount[0]++;
                    latch.countDown();
                    if (messageCount[0] == 1) {
                        // 收到第一条消息后取消订阅channel1
                        unsubscribe(channel1);
                    }
                }
                
                @Override
                public void onUnsubscribe(String channel, int subscribedChannels) {
                    System.out.println("Unsubscribed from: " + channel + ", remaining: " + subscribedChannels);
                }
            };
            
            Thread subscriberThread = new Thread(() -> {
                subscriber.subscribe(pubSub, channel1, channel2);
            });
            subscriberThread.start();
            
            Thread.sleep(1000);
            
            // 发布到两个频道
            publisher.publish(channel1, "Message 1");
            Thread.sleep(500);
            publisher.publish(channel2, "Message 2");
            
            boolean received = latch.await(5, TimeUnit.SECONDS);
            assertTrue(received);
            assertEquals(2, messageCount[0]);
            
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
        String channel1 = "test:pattern:channel1";
        String channel2 = "test:pattern:channel2";
        
        try (Jedis publisher = createConnection(); Jedis subscriber = createConnection()) {
            assertNotNull(publisher);
            assertNotNull(subscriber);
            
            CountDownLatch latch = new CountDownLatch(2);
            final int[] messageCount = {0};
            
            JedisPubSub pubSub = new JedisPubSub() {
                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    messageCount[0]++;
                    latch.countDown();
                    System.out.println("Pattern message received: " + pattern + " -> " + channel + ": " + message);
                }
                
                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    System.out.println("Pattern subscribed: " + pattern + ", channels: " + subscribedChannels);
                }
            };
            
            Thread subscriberThread = new Thread(() -> {
                subscriber.psubscribe(pubSub, pattern);
            });
            subscriberThread.start();
            
            Thread.sleep(1000);
            
            // 发布到匹配模式的频道
            publisher.publish(channel1, "Pattern Message 1");
            Thread.sleep(500);
            publisher.publish(channel2, "Pattern Message 2");
            
            boolean received = latch.await(5, TimeUnit.SECONDS);
            assertTrue(received);
            assertEquals(2, messageCount[0]);
            
            subscriberThread.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted: " + e.getMessage());
        }
        System.out.println("[TEST END] testPatternSubscribeOperations\n");
    }

    @Test
    void testPubSubInfoOperations() {
        System.out.println("[TEST START] testPubSubInfoOperations - 测试发布订阅信息操作(PUBSUB)");
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 测试PUBSUB CHANNELS
            List<String> channels = jedis.pubsubChannels("*");
            assertNotNull(channels);
            System.out.println("Active channels: " + channels.size());
            
            // 测试PUBSUB NUMSUB - 修复类型错误
            Map<String, Long> numSub = jedis.pubsubNumSub("test:channel");
            assertNotNull(numSub);
            System.out.println("Channel subscribers: " + numSub);
            
            // 测试PUBSUB NUMPAT
            Long numPat = jedis.pubsubNumPat();
            assertNotNull(numPat);
            assertTrue(numPat >= 0);
            System.out.println("Pattern subscriptions: " + numPat);
            
        }
        System.out.println("[TEST END] testPubSubInfoOperations\n");
    }

    @Test
    void testMultiplePublishersSubscribers() {
        System.out.println("[TEST START] testMultiplePublishersSubscribers - 测试多发布者订阅者");
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
            final int[] messageCount = {0};
            
            JedisPubSub pubSub1 = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    messageCount[0]++;
                    latch.countDown();
                    System.out.println("Subscriber1 received: " + message);
                }
            };
            
            JedisPubSub pubSub2 = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    messageCount[0]++;
                    latch.countDown();
                    System.out.println("Subscriber2 received: " + message);
                }
            };
            
            Thread subscriber1Thread = new Thread(() -> {
                subscriber1.subscribe(pubSub1, channel);
            });
            
            Thread subscriber2Thread = new Thread(() -> {
                subscriber2.subscribe(pubSub2, channel);
            });
            
            subscriber1Thread.start();
            subscriber2Thread.start();
            
            Thread.sleep(1000);
            
            // 多个发布者发布消息
            publisher1.publish(channel, "Message from Publisher 1");
            Thread.sleep(500);
            publisher2.publish(channel, "Message from Publisher 2");
            
            boolean received = latch.await(10, TimeUnit.SECONDS);
            assertTrue(received);
            assertEquals(4, messageCount[0]);
            
            subscriber1Thread.interrupt();
            subscriber2Thread.interrupt();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted: " + e.getMessage());
        }
        System.out.println("[TEST END] testMultiplePublishersSubscribers\n");
    }
}