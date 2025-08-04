package com.sqladaptor.redis;

import com.sqladaptor.BaseIntegrationTest;
import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class KeyCommandTest extends BaseIntegrationTest {
    
    @Test
    void testKeyOperations() {
        System.out.println("[TEST START] testKeyOperations - 测试键操作(EXISTS/EXPIRE/TTL)");
        String key1 = "test:key1:" + System.currentTimeMillis();
        String key2 = "test:key2:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            jedis.set(key1, "value1");
            jedis.set(key2, "value2");
            
            Boolean exists1 = jedis.exists(key1);
            assertTrue(exists1);
            
            Boolean existsNonExistent = jedis.exists("non:existent:key");
            assertFalse(existsNonExistent);
            
            Long expireResult = jedis.expire(key1, 60);
            assertEquals(1L, expireResult);
            
            Long ttl = jedis.ttl(key1);
            assertTrue(ttl >= 0 && ttl <= 60);
    
            jedis.del(key1, key2);
        }
        System.out.println("[TEST END] testKeyOperations\n");
    }
    
    @Test
    void testMultipleKeyDeletion() {
        System.out.println("[TEST START] testMultipleKeyDeletion - 测试多键删除操作");
        String key1 = "test:multi1:" + System.currentTimeMillis();
        String key2 = "test:multi2:" + System.currentTimeMillis();
        String key3 = "test:multi3:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            jedis.set(key1, "value1");
            jedis.set(key2, "value2");
            jedis.set(key3, "value3");
            
            Long delResult = jedis.del(key1, key2, key3);
            assertEquals(3L, delResult);
        }
        System.out.println("[TEST END] testMultipleKeyDeletion\n");
    }
    
    @Test
    void testKeyPersistOperations() {
        System.out.println("[TEST START] testKeyPersistOperations - 测试PERSIST命令");
        String key = "test:persist:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 设置键和过期时间
            jedis.set(key, "value");
            jedis.expire(key, 60);
            
            // 验证键有过期时间
            Long ttlBefore = jedis.ttl(key);
            assertTrue(ttlBefore > 0);
            
            // PERSIST操作 - 移除过期时间
            Long persistResult = jedis.persist(key);
            assertEquals(1L, persistResult);
            
            // 验证键不再有过期时间
            Long ttlAfter = jedis.ttl(key);
            assertEquals(-1L, ttlAfter);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testKeyPersistOperations\n");
    }
    
    @Test
    void testKeyTypeOperations() {
        System.out.println("[TEST START] testKeyTypeOperations - 测试TYPE命令");
        String stringKey = "test:type:string:" + System.currentTimeMillis();
        String hashKey = "test:type:hash:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 设置不同类型的键
            jedis.set(stringKey, "value");
            jedis.hset(hashKey, "field", "value");
            
            // TYPE操作 - 获取键的数据类型
            String stringType = jedis.type(stringKey);
            assertEquals("string", stringType);
            
            String hashType = jedis.type(hashKey);
            assertEquals("hash", hashType);
            
            // 不存在的键应该返回none
            String noneType = jedis.type("nonexistent:key");
            assertEquals("none", noneType);
            
            jedis.del(stringKey, hashKey);
        }
        System.out.println("[TEST END] testKeyTypeOperations\n");
    }
    
    @Test
    void testKeyMillisecondOperations() {
        System.out.println("[TEST START] testKeyMillisecondOperations - 测试PEXPIRE/PTTL命令");
        String key = "test:pexpire:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            jedis.set(key, "value");
            
            // PEXPIRE操作 - 以毫秒为单位设置过期时间
            Long pexpireResult = jedis.pexpire(key, 60000); // 60秒 = 60000毫秒
            assertEquals(1L, pexpireResult);
            
            // PTTL操作 - 以毫秒为单位获取剩余生存时间
            Long pttl = jedis.pttl(key);
            assertTrue(pttl > 0 && pttl <= 60000);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testKeyMillisecondOperations\n");
    }
    
    @Test
    void testKeyRenameOperations() {
        System.out.println("[TEST START] testKeyRenameOperations - 测试RENAME/RENAMENX命令");
        String oldKey = "test:rename:old:" + System.currentTimeMillis();
        String newKey = "test:rename:new:" + System.currentTimeMillis();
        String existingKey = "test:rename:existing:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            jedis.set(oldKey, "value");
            jedis.set(existingKey, "existing_value");
            
            // RENAME操作 - 重命名键
            String renameResult = jedis.rename(oldKey, newKey);
            assertEquals("OK", renameResult);
            
            // 验证重命名结果
            assertFalse(jedis.exists(oldKey));
            assertTrue(jedis.exists(newKey));
            assertEquals("value", jedis.get(newKey));
            
            // RENAMENX操作 - 仅当新键名不存在时重命名
            jedis.set(oldKey, "new_value");
            Long renamenxResult1 = jedis.renamenx(oldKey, existingKey); // 目标键已存在
            assertEquals(0L, renamenxResult1);
            
            String anotherNewKey = "test:rename:another:" + System.currentTimeMillis();
            Long renamenxResult2 = jedis.renamenx(oldKey, anotherNewKey); // 目标键不存在
            assertEquals(1L, renamenxResult2);
            
            jedis.del(newKey, existingKey, anotherNewKey);
        }
        System.out.println("[TEST END] testKeyRenameOperations\n");
    }
    
    @Test
    void testKeyUtilityOperations() {
        System.out.println("[TEST START] testKeyUtilityOperations - 测试KEYS/RANDOMKEY命令");
        String keyPattern = "test:utility:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 创建一些测试键
            jedis.set(keyPattern + ":1", "value1");
            jedis.set(keyPattern + ":2", "value2");
            jedis.set(keyPattern + ":3", "value3");
            
            // KEYS操作 - 查找匹配模式的键
            java.util.Set<String> keys = jedis.keys(keyPattern + ":*");
            assertTrue(keys.size() >= 3);
            
            // RANDOMKEY操作 - 随机返回一个键
            String randomKey = jedis.randomKey();
            // randomKey可能为null（如果数据库为空）或者是一个存在的键
            if (randomKey != null) {
                assertTrue(jedis.exists(randomKey));
            }
            
            jedis.del(keyPattern + ":1", keyPattern + ":2", keyPattern + ":3");
        }
        System.out.println("[TEST END] testKeyUtilityOperations\n");
    }
}