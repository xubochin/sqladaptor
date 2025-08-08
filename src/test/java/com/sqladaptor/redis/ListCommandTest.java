package com.sqladaptor.redis;

import com.sqladaptor.BaseIntegrationTest;
import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import redis.clients.jedis.args.ListPosition;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ListCommandTest extends BaseIntegrationTest {
    
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
    void testListIndexOperations() {
        System.out.println("[TEST START] testListIndexOperations - 测试LINDEX/LSET命令");
        String key = "test:index:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据
            jedis.rpush(key, "value0", "value1", "value2", "value3");
            
            // LINDEX操作 - 获取指定索引的元素
            String indexResult0 = jedis.lindex(key, 0);
            assertEquals("value0", indexResult0);
            
            String indexResult2 = jedis.lindex(key, 2);
            assertEquals("value2", indexResult2);
            
            String indexResultNeg1 = jedis.lindex(key, -1);
            assertEquals("value3", indexResultNeg1);
            
            // 超出范围的索引应该返回null
            String indexResultOut = jedis.lindex(key, 10);
            assertNull(indexResultOut);
            
            // LSET操作 - 设置指定索引的元素值
            String lsetResult = jedis.lset(key, 1, "newvalue1");
            assertEquals("OK", lsetResult);
            
            // 验证设置结果
            String verifyResult = jedis.lindex(key, 1);
            assertEquals("newvalue1", verifyResult);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testListIndexOperations\n");
    }
    
    @Test
    void testListRemoveOperations() {
        System.out.println("[TEST START] testListRemoveOperations - 测试LREM命令");
        String key = "test:lrem:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据（包含重复元素）
            jedis.rpush(key, "a", "b", "a", "c", "a", "b");
            
            // LREM操作 - 从左侧移除2个"a"
            Long lremResult1 = jedis.lrem(key, 2, "a");
            assertEquals(2L, lremResult1);
            
            // 验证移除结果
            List<String> afterRem1 = jedis.lrange(key, 0, -1);
            assertEquals(4, afterRem1.size());
            assertEquals("b", afterRem1.get(0));
            assertEquals("c", afterRem1.get(1));
            assertEquals("a", afterRem1.get(2));
            assertEquals("b", afterRem1.get(3));
            
            // LREM操作 - 移除所有"b"（count=0表示移除所有）
            Long lremResult2 = jedis.lrem(key, 0, "b");
            assertEquals(2L, lremResult2);
            
            // 验证最终结果
            List<String> finalResult = jedis.lrange(key, 0, -1);
            assertEquals(2, finalResult.size());
            assertEquals("c", finalResult.get(0));
            assertEquals("a", finalResult.get(1));
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testListRemoveOperations\n");
    }
    
    @Test
    void testListTrimOperations() {
        System.out.println("[TEST START] testListTrimOperations - 测试LTRIM命令");
        String key = "test:ltrim:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据
            jedis.rpush(key, "v0", "v1", "v2", "v3", "v4", "v5");
            
            // LTRIM操作 - 只保留索引1到3的元素
            String ltrimResult = jedis.ltrim(key, 1, 3);
            assertEquals("OK", ltrimResult);
            
            // 验证修剪结果
            List<String> trimmedList = jedis.lrange(key, 0, -1);
            assertEquals(3, trimmedList.size());
            assertEquals("v1", trimmedList.get(0));
            assertEquals("v2", trimmedList.get(1));
            assertEquals("v3", trimmedList.get(2));
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testListTrimOperations\n");
    }
    
    @Test
    void testListInsertOperations() {
        System.out.println("[TEST START] testListInsertOperations - 测试LINSERT命令");
        String key = "test:linsert:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据
            jedis.rpush(key, "a", "c", "e");
            
            // LINSERT操作 - 在"c"前插入"b"
            Long insertResult1 = jedis.linsert(key, redis.clients.jedis.args.ListPosition.BEFORE, "c", "b");
            assertTrue(insertResult1 > 0);
            
            // LINSERT操作 - 在"c"后插入"d"
            Long insertResult2 = jedis.linsert(key, redis.clients.jedis.args.ListPosition.AFTER, "c", "d");
            assertTrue(insertResult2 > 0);
            
            // 验证插入结果
            List<String> insertedList = jedis.lrange(key, 0, -1);
            assertEquals(5, insertedList.size());
            assertEquals("a", insertedList.get(0));
            assertEquals("b", insertedList.get(1));
            assertEquals("c", insertedList.get(2));
            assertEquals("d", insertedList.get(3));
            assertEquals("e", insertedList.get(4));
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testListInsertOperations\n");
    }
    
    @Test
    void testListConditionalPushOperations() {
        System.out.println("[TEST START] testListConditionalPushOperations - 测试LPUSHX/RPUSHX命令");
        String existingKey = "test:pushx:existing:" + System.currentTimeMillis();
        String nonExistingKey = "test:pushx:nonexisting:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 创建一个已存在的列表
            jedis.lpush(existingKey, "initial");
            
            // LPUSHX操作 - 对已存在的列表进行左侧推入
            Long lpushxResult1 = jedis.lpushx(existingKey, "left");
            assertEquals(2L, lpushxResult1);
            
            // LPUSHX操作 - 对不存在的列表进行左侧推入（应该失败）
            Long lpushxResult2 = jedis.lpushx(nonExistingKey, "left");
            assertEquals(0L, lpushxResult2);
            
            // RPUSHX操作 - 对已存在的列表进行右侧推入
            Long rpushxResult1 = jedis.rpushx(existingKey, "right");
            assertEquals(3L, rpushxResult1);
            
            // RPUSHX操作 - 对不存在的列表进行右侧推入（应该失败）
            Long rpushxResult2 = jedis.rpushx(nonExistingKey, "right");
            assertEquals(0L, rpushxResult2);
            
            // 验证结果
            List<String> finalList = jedis.lrange(existingKey, 0, -1);
            assertEquals(3, finalList.size());
            assertEquals("left", finalList.get(0));
            assertEquals("initial", finalList.get(1));
            assertEquals("right", finalList.get(2));
            
            // 验证不存在的键确实没有被创建
            assertFalse(jedis.exists(nonExistingKey));
            
            jedis.del(existingKey);
        }
        System.out.println("[TEST END] testListConditionalPushOperations\n");
    }
    
    @Test
    void testListTransferOperations() {
        System.out.println("[TEST START] testListTransferOperations - 测试RPOPLPUSH命令");
        String sourceKey = "test:transfer:source:" + System.currentTimeMillis();
        String destKey = "test:transfer:dest:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备源列表
            jedis.rpush(sourceKey, "a", "b", "c");
            jedis.rpush(destKey, "x", "y");
            
            // RPOPLPUSH操作 - 从源列表右侧弹出并推入目标列表左侧
            String transferResult = jedis.rpoplpush(sourceKey, destKey);
            assertEquals("c", transferResult);
            
            // 验证源列表
            List<String> sourceList = jedis.lrange(sourceKey, 0, -1);
            assertEquals(2, sourceList.size());
            assertEquals("a", sourceList.get(0));
            assertEquals("b", sourceList.get(1));
            
            // 验证目标列表
            List<String> destList = jedis.lrange(destKey, 0, -1);
            assertEquals(3, destList.size());
            assertEquals("c", destList.get(0));
            assertEquals("x", destList.get(1));
            assertEquals("y", destList.get(2));
            
            jedis.del(sourceKey, destKey);
        }
        System.out.println("[TEST END] testListTransferOperations\n");
    }
}