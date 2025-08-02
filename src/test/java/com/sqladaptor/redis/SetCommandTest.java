package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Set;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class SetCommandTest {
    
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
    void testSetOperations() {
        System.out.println("[TEST START] testSetOperations - 测试集合操作(SADD/SMEMBERS/SREM)");
        String key = "test:set:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // SADD操作
            Long saddResult = jedis.sadd(key, "member1", "member2", "member3");
            assertEquals(3L, saddResult);
            
            // SMEMBERS操作
            Set<String> smembersResult = jedis.smembers(key);
            assertEquals(3, smembersResult.size());
            assertTrue(smembersResult.contains("member1"));
            assertTrue(smembersResult.contains("member2"));
            assertTrue(smembersResult.contains("member3"));
            
            // SCARD操作
            Long scardResult = jedis.scard(key);
            assertEquals(3L, scardResult);
            
            // SREM操作
            Long sremResult = jedis.srem(key, "member1");
            assertEquals(1L, sremResult);
            assertEquals(2L, jedis.scard(key));
            
            // SISMEMBER操作
            Boolean sismemberResult1 = jedis.sismember(key, "member2");
            assertTrue(sismemberResult1);
            
            Boolean sismemberResult2 = jedis.sismember(key, "member1");
            assertFalse(sismemberResult2);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testSetOperations\n");
    }
    
    @Test
    void testSetUnionIntersectionOperations() {
        System.out.println("[TEST START] testSetUnionIntersectionOperations - 测试集合并集交集操作");
        String key1 = "test:set1:" + System.currentTimeMillis();
        String key2 = "test:set2:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            jedis.sadd(key1, "a", "b", "c");
            jedis.sadd(key2, "c", "d", "e");
            
            // SUNION操作
            Set<String> sunionResult = jedis.sunion(key1, key2);
            assertEquals(5, sunionResult.size());
            
            // SINTER操作
            Set<String> sinterResult = jedis.sinter(key1, key2);
            assertEquals(1, sinterResult.size());
            assertTrue(sinterResult.contains("c"));
            
            // SDIFF操作
            Set<String> sdiffResult = jedis.sdiff(key1, key2);
            assertEquals(2, sdiffResult.size());
            assertTrue(sdiffResult.contains("a"));
            assertTrue(sdiffResult.contains("b"));
            
            jedis.del(key1, key2);
        }
        System.out.println("[TEST END] testSetUnionIntersectionOperations\n");
    }
    
    // 在现有的 SetCommandTest 类中添加以下测试方法
    
    @Test
    void testSetStoreOperations() {
        System.out.println("[TEST START] testSetStoreOperations - 测试集合存储操作(SDIFFSTORE/SINTERSTORE/SUNIONSTORE)");
        String key1 = "test:set1:" + System.currentTimeMillis();
        String key2 = "test:set2:" + System.currentTimeMillis();
        String diffKey = "test:diff:" + System.currentTimeMillis();
        String interKey = "test:inter:" + System.currentTimeMillis();
        String unionKey = "test:union:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据
            jedis.sadd(key1, "a", "b", "c", "d");
            jedis.sadd(key2, "c", "d", "e", "f");
            
            // SDIFFSTORE操作 - 差集存储
            Long sdiffstoreResult = jedis.sdiffstore(diffKey, key1, key2);
            assertEquals(2L, sdiffstoreResult); // a, b
            Set<String> diffMembers = jedis.smembers(diffKey);
            assertTrue(diffMembers.contains("a"));
            assertTrue(diffMembers.contains("b"));
            
            // SINTERSTORE操作 - 交集存储
            Long sinterstoreResult = jedis.sinterstore(interKey, key1, key2);
            assertEquals(2L, sinterstoreResult); // c, d
            Set<String> interMembers = jedis.smembers(interKey);
            assertTrue(interMembers.contains("c"));
            assertTrue(interMembers.contains("d"));
            
            // SUNIONSTORE操作 - 并集存储
            Long sunionstoreResult = jedis.sunionstore(unionKey, key1, key2);
            assertEquals(6L, sunionstoreResult); // a, b, c, d, e, f
            Set<String> unionMembers = jedis.smembers(unionKey);
            assertEquals(6, unionMembers.size());
            
            // 清理
            jedis.del(key1, key2, diffKey, interKey, unionKey);
        }
        System.out.println("[TEST END] testSetStoreOperations\n");
    }
    
    @Test
    void testSetMoveOperations() {
        System.out.println("[TEST START] testSetMoveOperations - 测试集合移动操作(SMOVE)");
        String sourceKey = "test:source:" + System.currentTimeMillis();
        String destKey = "test:dest:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据
            jedis.sadd(sourceKey, "member1", "member2", "member3");
            jedis.sadd(destKey, "member4");
            
            // SMOVE操作 - 移动存在的成员
            Long smoveResult1 = jedis.smove(sourceKey, destKey, "member1");
            assertEquals(1L, smoveResult1);
            assertFalse(jedis.sismember(sourceKey, "member1"));
            assertTrue(jedis.sismember(destKey, "member1"));
            
            // SMOVE操作 - 移动不存在的成员
            Long smoveResult2 = jedis.smove(sourceKey, destKey, "nonexistent");
            assertEquals(0L, smoveResult2);
            
            // 清理
            jedis.del(sourceKey, destKey);
        }
        System.out.println("[TEST END] testSetMoveOperations\n");
    }
    
    @Test
    void testSetRandomOperations() {
        System.out.println("[TEST START] testSetRandomOperations - 测试集合随机操作(SPOP/SRANDMEMBER)");
        String popKey = "test:pop:" + System.currentTimeMillis();
        String randKey = "test:rand:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据
            jedis.sadd(popKey, "a", "b", "c", "d", "e");
            jedis.sadd(randKey, "x", "y", "z");
            
            // SPOP操作 - 弹出单个成员
            String poppedMember = jedis.spop(popKey);
            assertNotNull(poppedMember);
            assertFalse(jedis.sismember(popKey, poppedMember));
            assertEquals(4L, jedis.scard(popKey));
            
            // SPOP操作 - 弹出多个成员
            Set<String> poppedMembers = jedis.spop(popKey, 2);
            assertNotNull(poppedMembers);
            assertEquals(2, poppedMembers.size());
            assertEquals(2L, jedis.scard(popKey));
            
            // SRANDMEMBER操作 - 随机获取单个成员
            String randomMember = jedis.srandmember(randKey);
            assertNotNull(randomMember);
            assertTrue(jedis.sismember(randKey, randomMember));
            assertEquals(3L, jedis.scard(randKey)); // 成员数量不变
            
            // SRANDMEMBER操作 - 随机获取多个成员
            java.util.List<String> randomMembers = jedis.srandmember(randKey, 2);
            assertNotNull(randomMembers);
            assertTrue(randomMembers.size() <= 2);
            assertEquals(3L, jedis.scard(randKey)); // 成员数量不变
            
            // 清理
            jedis.del(popKey, randKey);
        }
        System.out.println("[TEST END] testSetRandomOperations\n");
    }
    
    @Test
    void testSetScanOperations() {
        System.out.println("[TEST START] testSetScanOperations - 测试集合扫描操作(SSCAN)");
        String scanKey = "test:scan:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据
            jedis.sadd(scanKey, "member1", "member2", "member3", "test1", "test2");
            
            // SSCAN操作 - 基本扫描
            redis.clients.jedis.resps.ScanResult<String> scanResult = jedis.sscan(scanKey, "0");
            assertNotNull(scanResult);
            assertNotNull(scanResult.getResult());
            assertTrue(scanResult.getResult().size() > 0);
            
            // SSCAN操作 - 带模式匹配
            redis.clients.jedis.params.ScanParams scanParams = new redis.clients.jedis.params.ScanParams();
            scanParams.match("member*");
            scanParams.count(10);
            
            redis.clients.jedis.resps.ScanResult<String> patternScanResult = jedis.sscan(scanKey, "0", scanParams);
            assertNotNull(patternScanResult);
            assertNotNull(patternScanResult.getResult());
            
            // 验证匹配结果
            for (String member : patternScanResult.getResult()) {
                assertTrue(member.startsWith("member"));
            }
            
            // 清理
            jedis.del(scanKey);
        }
        System.out.println("[TEST END] testSetScanOperations\n");
    }
    
    @Test
    void testSetComplexScenarios() {
        System.out.println("[TEST START] testSetComplexScenarios - 测试集合复杂场景");
        String activeUsers = "test:users:active:" + System.currentTimeMillis();
        String premiumUsers = "test:users:premium:" + System.currentTimeMillis();
        String bannedUsers = "test:users:banned:" + System.currentTimeMillis();
        String validPremium = "test:users:valid_premium:" + System.currentTimeMillis();
        String tempInter = "test:temp:inter:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据
            jedis.sadd(activeUsers, "user1", "user2", "user3", "user4");
            jedis.sadd(premiumUsers, "user2", "user3", "user5", "user6");
            jedis.sadd(bannedUsers, "user3", "user7");
            
            // 复杂场景：找出活跃且付费但未被封禁的用户
            // 1. 先求活跃用户和付费用户的交集
            jedis.sinterstore(tempInter, activeUsers, premiumUsers);
            
            // 2. 再从交集中排除被封禁的用户
            jedis.sdiffstore(validPremium, tempInter, bannedUsers);
            
            // 验证结果
            Set<String> validPremiumMembers = jedis.smembers(validPremium);
            assertTrue(validPremiumMembers.contains("user2")); // 活跃+付费+未封禁
            assertFalse(validPremiumMembers.contains("user3")); // 被封禁
            assertFalse(validPremiumMembers.contains("user1")); // 非付费
            assertFalse(validPremiumMembers.contains("user5")); // 非活跃
            
            // 清理
            jedis.del(activeUsers, premiumUsers, bannedUsers, validPremium, tempInter);
        }
        System.out.println("[TEST END] testSetComplexScenarios\n");
    }
}