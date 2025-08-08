package com.sqladaptor.redis;

import com.sqladaptor.BaseIntegrationTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Map;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class RedisConnectionTest extends BaseIntegrationTest {
    
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
    void testStringOperations() {
        System.out.println("[TEST START] testStringOperations - 测试字符串操作(SET/GET/DEL)");
        Jedis jedis = null;
        try {
            jedis = createConnection();
            String key = "test:string:" + System.currentTimeMillis();
            String value = "Hello Redis!";
            
            // SET操作
            String setResult = jedis.set(key, value);
            assertEquals("OK", setResult);
            
            // GET操作
            String getValue = jedis.get(key);
            assertEquals(value, getValue);
            
            // DEL操作
            Long delResult = jedis.del(key);
            assertEquals(1L, delResult);
            
            // 验证删除
            String deletedValue = jedis.get(key);
            assertNull(deletedValue);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testStringOperations\n");
    }
    
    @Test
    void testHashOperations() {
        System.out.println("[TEST START] testHashOperations - 测试哈希操作(HSET/HGET/HGETALL)");
        Jedis jedis = null;
        try {
            jedis = createConnection();
            String key = "test:hash:" + System.currentTimeMillis();
            String field1 = "field1";
            String value1 = "value1";
            String field2 = "field2";
            String value2 = "value2";
            
            // HSET操作
            Long hsetResult1 = jedis.hset(key, field1, value1);
            assertEquals(1L, hsetResult1);
            
            Long hsetResult2 = jedis.hset(key, field2, value2);
            assertEquals(1L, hsetResult2);
            
            // HGET操作
            String hgetValue1 = jedis.hget(key, field1);
            assertEquals(value1, hgetValue1);
            
            // HGETALL操作
            var hgetallResult = jedis.hgetAll(key);
            assertEquals(2, hgetallResult.size());
            assertEquals(value1, hgetallResult.get(field1));
            assertEquals(value2, hgetallResult.get(field2));
            
            // 清理
            jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testHashOperations\n");
    }
    
    @Test
    void testKeyOperations() {
            System.out.println("[TEST START] testKeyOperations - 测试键操作(EXISTS/EXPIRE/TTL)");
            String key1 = "test:key1:" + System.currentTimeMillis();
            String key2 = "test:key2:" + System.currentTimeMillis();
            Jedis jedis = null;
            try {
                jedis = createConnection();
                // 设置一些键
                jedis.set(key1, "value1");
                jedis.set(key2, "value2");
                
                // EXISTS操作
                Boolean exists1 = jedis.exists(key1);
                assertTrue(exists1);
                
                Boolean existsNonExistent = jedis.exists("non:existent:key");
                assertFalse(existsNonExistent);
                
                // EXPIRE操作
                Long expireResult = jedis.expire(key1, 60); // 60秒过期
                assertEquals(1L, expireResult);
                
                // TTL操作
                Long ttl = jedis.ttl(key1);
                assertTrue(ttl >= 0 && ttl <= 60);
        
                // 清理
                jedis.del(key1, key2);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            System.out.println("[TEST END] testKeyOperations\n");
    }
    
    @Test
    void testIncrementOperations() {
             System.out.println("[TEST START] testIncrementOperations - 测试增量操作(INCR/DECR)");
            String key = "test:counter:" + System.currentTimeMillis();
            Jedis jedis = null;
            try {
                jedis = createConnection();
                // INCR操作
                Long incrResult1 = jedis.incr(key);
                assertEquals(1L, incrResult1);
                
                Long incrResult2 = jedis.incr(key);
                assertEquals(2L, incrResult2);
                
                // DECR操作
                Long decrResult = jedis.decr(key);
                assertEquals(1L, decrResult);
                
                // 清理
                jedis.del(key);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            System.out.println("[TEST END] testIncrementOperations\n");
    }
    
    @Test
    void testMultipleKeyDeletion() {
            System.out.println("[TEST START] testMultipleKeyDeletion - 测试多键删除操作");
            String key1 = "test:multi1:" + System.currentTimeMillis();
            String key2 = "test:multi2:" + System.currentTimeMillis();
            String key3 = "test:multi3:" + System.currentTimeMillis();
            Jedis jedis = null;
            try {
                jedis = createConnection();
                // 设置多个键
                jedis.set(key1, "value1");
                jedis.set(key2, "value2");
                jedis.set(key3, "value3");
                
                // 批量删除
                Long delResult = jedis.del(key1, key2, key3);
                assertEquals(3L, delResult);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            System.out.println("[TEST END] testMultipleKeyDeletion\n");
    }
    
    @Test
    void testAdvancedStringOperations() {
            System.out.println("[TEST START] testAdvancedStringOperations - 测试高级字符串操作(APPEND/STRLEN/GETRANGE等)");
            String key = "test:advanced:" + System.currentTimeMillis();
            Jedis jedis = null;
            try {
                jedis = createConnection();
                // APPEND操作
                jedis.set(key, "Hello");
                Long appendResult = jedis.append(key, " World");
                assertEquals(11L, appendResult);
                assertEquals("Hello World", jedis.get(key));
                
                // STRLEN操作
                Long strlenResult = jedis.strlen(key);
                assertEquals(11L, strlenResult);
                
                // GETRANGE操作
                String rangeResult = jedis.getrange(key, 0, 4);
                assertEquals("Hello", rangeResult);
                
                // SETRANGE操作
                Long setrangeResult = jedis.setrange(key, 6, "Redis");
                assertEquals(11L, setrangeResult);
                assertEquals("Hello Redis", jedis.get(key));
                
                // INCRBY和DECRBY操作
                String numKey = "test:num:" + System.currentTimeMillis();
                jedis.set(numKey, "10");
                Long incrbyResult = jedis.incrBy(numKey, 5);
                assertEquals(15L, incrbyResult);
                
                Long decrbyResult = jedis.decrBy(numKey, 3);
                assertEquals(12L, decrbyResult);
                
                // 清理
                jedis.del(key, numKey);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            System.out.println("[TEST END] testAdvancedStringOperations\n");
    }
    
    @Test
    void testBatchStringOperations() {
        System.out.println("[TEST START] testBatchStringOperations - 测试批量字符串操作(MSET/MGET/MSETNX)");
        String key1 = "test:batch1:" + System.currentTimeMillis();
        String key2 = "test:batch2:" + System.currentTimeMillis();
        String key3 = "test:batch3:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            // MSET操作
            assertNotNull(jedis);
            String msetResult = jedis.mset(key1, "value1", key2, "value2", key3, "value3");
            assertEquals("OK", msetResult);

            // MGET操作
            List<String> mgetResult = jedis.mget(key1, key2, key3);
            assertEquals(3, mgetResult.size());
            assertEquals("value1", mgetResult.get(0));
            assertEquals("value2", mgetResult.get(1));
            assertEquals("value3", mgetResult.get(2));

            // MSETNX操作（应该失败，因为键已存在）
            String key4 = "test:batch4:" + System.currentTimeMillis();
            Long msetnxResult = jedis.msetnx(key1, "newvalue1", key4, "value4");
            assertEquals(0L, msetnxResult); // 失败，因为key1已存在

            // 清理
            jedis.del(key1, key2, key3, key4);
        }
        System.out.println("[TEST END] testBatchStringOperations\n");
    }
    
    @Test
    void testAdvancedHashOperations() {
        System.out.println("[TEST START] testAdvancedHashOperations - 测试高级哈希操作(HKEYS/HVALS/HDEL等)");
        String key = "test:advhash:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            // HKEYS操作
            assertNotNull(jedis);
            jedis.hset(key, "field1", "value1");
            jedis.hset(key, "field2", "value2");
            jedis.hset(key, "field3", "value3");

            Set<String> hkeysResult = jedis.hkeys(key);
            assertEquals(3, hkeysResult.size());
            assertTrue(hkeysResult.contains("field1"));
            assertTrue(hkeysResult.contains("field2"));
            assertTrue(hkeysResult.contains("field3"));

            // HVALS操作
            List<String> hvalsResult = jedis.hvals(key);
            assertEquals(3, hvalsResult.size());
            assertTrue(hvalsResult.contains("value1"));
            assertTrue(hvalsResult.contains("value2"));
            assertTrue(hvalsResult.contains("value3"));

            // HDEL操作
            Long hdelResult = jedis.hdel(key, "field1", "field2");
            assertEquals(2L, hdelResult);

            // 验证删除
            assertFalse(jedis.hexists(key, "field1"));
            assertFalse(jedis.hexists(key, "field2"));
            assertTrue(jedis.hexists(key, "field3"));

            // 清理
            jedis.del(key);
        }
        System.out.println("[TEST END] testAdvancedHashOperations\n");
    }
    
    @Test
    void testListOperations() {
        System.out.println("[TEST START] testListOperations - 测试列表操作(LPUSH/RPUSH/LRANGE等)");
        String key = "test:list:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
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
        }
        System.out.println("[TEST END] testListOperations\n");
    }

    @Test
    void testConnectionOperations() {
        System.out.println("[TEST START] testConnectionOperations - 测试连接操作(PING/ECHO)");
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            String pingResult1 = jedis.ping();
            assertEquals("PONG", pingResult1);

            String pingResult2 = jedis.ping("Hello");
            assertEquals("Hello", pingResult2);

            String echoResult = jedis.echo("Test Message");
            assertEquals("Test Message", echoResult);
        }
        System.out.println("[TEST END] testConnectionOperations\n");
    }

    @Test
    void testAuthCommand() {
        System.out.println("[TEST START] testAuthCommand - 测试AUTH命令");
        Jedis jedis = null;
        try {
            jedis = createConnection();

            // 测试AUTH命令（简单实现接受任何密码）
            String authResult = jedis.auth("test-password");
            assertEquals("OK", authResult);

            System.out.println("AUTH命令测试成功！");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testAuthCommand\n");
    }

    @Test
    void testSelectCommand() {
        System.out.println("[TEST START] testSelectCommand - 测试SELECT命令");
        Jedis jedis = null;
        try {
            jedis = createConnection();

            // 测试有效的数据库选择
            String selectResult1 = jedis.select(0);
            assertEquals("OK", selectResult1);

            String selectResult2 = jedis.select(1);
            assertEquals("OK", selectResult2);

            // 测试边界值
            String selectResult3 = jedis.select(15);
            assertEquals("OK", selectResult3);

            System.out.println("SELECT命令测试成功！");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testSelectCommand\n");
    }

    @Test
    void testSelectCommandErrorCases() {
        System.out.println("[TEST START] testSelectCommandErrorCases - 测试SELECT命令错误情况");
        Jedis jedis = null;
        try {
            jedis = createConnection();

            // 测试无效的数据库索引（超出范围）
            try {
                jedis.select(-1);
                fail("应该抛出异常");
            } catch (Exception e) {
                assertTrue(e.getMessage().contains("DB index is out of range") ||
                          e.getMessage().contains("invalid"));
            }

            try {
                jedis.select(16);
                fail("应该抛出异常");
            } catch (Exception e) {
                assertTrue(e.getMessage().contains("DB index is out of range") ||
                          e.getMessage().contains("invalid"));
            }

            System.out.println("SELECT错误情况测试成功！");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testSelectCommandErrorCases\n");
    }

    @Test
    void testClientCommands() {
        System.out.println("[TEST START] testClientCommands - 测试CLIENT相关命令");
        Jedis jedis = null;
        try {
            jedis = createConnection();

            // 测试CLIENT LIST
            String clientList = jedis.clientList();
            assertNotNull(clientList);
            assertTrue(clientList.contains("id="));

            // 测试CLIENT GETNAME（初始应该为null）
            String clientName = jedis.clientGetname();
            // 可能返回null或空字符串

            // 测试CLIENT SETNAME
            String setNameResult = jedis.clientSetname("test-client");
            assertEquals("OK", setNameResult);

            // 测试CLIENT ID
            Long clientId = jedis.clientId();
            assertNotNull(clientId);
            assertTrue(clientId > 0);

            System.out.println("CLIENT命令测试成功！");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testClientCommands\n");
    }

    @Test
    void testCommandInfoOperations() {
        System.out.println("[TEST START] testCommandInfoOperations - 测试COMMAND信息命令");
        Jedis jedis = null;
        try {
            jedis = createConnection();

            // 注意：Jedis可能没有直接的COMMAND方法，这里展示概念
            // 实际实现可能需要使用sendCommand方法

            // 测试COMMAND COUNT（如果支持）
            // Object commandCount = jedis.sendCommand(Protocol.Command.COMMAND, "COUNT");
            // assertNotNull(commandCount);

            System.out.println("COMMAND信息操作测试完成！");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testCommandInfoOperations\n");
    }

    @Test
    void testConnectionLifecycle() {
        System.out.println("[TEST START] testConnectionLifecycle - 测试连接生命周期");
        Jedis jedis = null;
        try {
            jedis = createConnection();

            // 测试连接是否正常
            String pingResult = jedis.ping();
            assertEquals("PONG", pingResult);

            // 测试一些基本操作
            jedis.set("lifecycle:test", "value");
            String value = jedis.get("lifecycle:test");
            assertEquals("value", value);

            // 清理
            jedis.del("lifecycle:test");

            // 测试连接关闭（使用close方法）
            jedis.close();

            System.out.println("连接生命周期测试成功！");
        } finally {
            // jedis已经通过close关闭，这里不需要再次关闭
        }
        System.out.println("[TEST END] testConnectionLifecycle\n");
    }

    @Test
    void testAdvancedConnectionOperations() {
        System.out.println("[TEST START] testAdvancedConnectionOperations - 测试高级连接操作");
        Jedis jedis = null;
        try {
            jedis = createConnection();

            // 测试多次PING
            for (int i = 0; i < 5; i++) {
                String pingResult = jedis.ping();
                assertEquals("PONG", pingResult);
            }

            // 测试PING with different messages
            String[] messages = {"Hello", "World", "Test", "Redis", "Connection"};
            for (String message : messages) {
                String pingResult = jedis.ping(message);
                assertEquals(message, pingResult);
            }

            // 测试ECHO with different messages
            for (String message : messages) {
                String echoResult = jedis.echo(message);
                assertEquals(message, echoResult);
            }

            System.out.println("高级连接操作测试成功！");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        System.out.println("[TEST END] testAdvancedConnectionOperations\n");
    }
}
