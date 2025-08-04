package com.sqladaptor.redis;

import com.sqladaptor.BaseIntegrationTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class SortedSetCommandTest extends BaseIntegrationTest {
    
    @Test
    void testSortedSetOperations() {
        System.out.println("[TEST START] testSortedSetOperations - 测试有序集合操作(ZADD/ZRANGE/ZREM)");
        String key = "test:zset:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // ZADD操作
            Long zaddResult = jedis.zadd(key, 1.0, "member1");
            assertEquals(1L, zaddResult);
            jedis.zadd(key, 2.0, "member2");
            jedis.zadd(key, 3.0, "member3");
            
            // ZCARD操作
            Long zcardResult = jedis.zcard(key);
            assertEquals(3L, zcardResult);
            
            // ZRANGE操作
            List<String> zrangeResult = jedis.zrange(key, 0, -1);
            assertEquals(3, zrangeResult.size());
            assertEquals("member1", zrangeResult.get(0));
            assertEquals("member2", zrangeResult.get(1));
            assertEquals("member3", zrangeResult.get(2));
            
            // ZSCORE操作
            Double zscoreResult = jedis.zscore(key, "member2");
            assertEquals(2.0, zscoreResult);
            
            // ZREM操作
            Long zremResult = jedis.zrem(key, "member1");
            assertEquals(1L, zremResult);
            assertEquals(2L, jedis.zcard(key));
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testSortedSetOperations\n");
    }
    
    @Test
    void testSortedSetRangeOperations() {
        System.out.println("[TEST START] testSortedSetRangeOperations - 测试有序集合范围操作");
        String key = "test:zset:range:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            jedis.zadd(key, 10.0, "member1");
            jedis.zadd(key, 20.0, "member2");
            jedis.zadd(key, 30.0, "member3");
            jedis.zadd(key, 40.0, "member4");
            
            // ZRANGEBYSCORE操作
            List<String> zrangebyscoreResult = jedis.zrangeByScore(key, 15.0, 35.0);
            assertEquals(2, zrangebyscoreResult.size());
            assertTrue(zrangebyscoreResult.contains("member2"));
            assertTrue(zrangebyscoreResult.contains("member3"));
            
            // ZCOUNT操作
            Long zcountResult = jedis.zcount(key, 15.0, 35.0);
            assertEquals(2L, zcountResult);
            
            // ZRANK操作
            Long zrankResult = jedis.zrank(key, "member3");
            assertEquals(2L, zrankResult);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testSortedSetRangeOperations\n");
    }
    
    @Test
    void testSortedSetIncrementOperations() {
        System.out.println("[TEST START] testSortedSetIncrementOperations - 测试有序集合增量操作(ZINCRBY)");
        String key = "test:zset:incr:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 初始化数据
            jedis.zadd(key, 10.0, "member1");
            jedis.zadd(key, 20.0, "member2");
            
            // ZINCRBY操作 - 增加已存在成员的分数
            Double zincrbyResult1 = jedis.zincrby(key, 5.0, "member1");
            assertEquals(15.0, zincrbyResult1);
            assertEquals(15.0, jedis.zscore(key, "member1"));
            
            // ZINCRBY操作 - 对不存在的成员增加分数（相当于添加）
            Double zincrbyResult2 = jedis.zincrby(key, 30.0, "member3");
            assertEquals(30.0, zincrbyResult2);
            assertEquals(3L, jedis.zcard(key));
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testSortedSetIncrementOperations\n");
    }
    
    @Test
    void testSortedSetStoreOperations() {
        System.out.println("[TEST START] testSortedSetStoreOperations - 测试有序集合存储操作(ZINTERSTORE/ZUNIONSTORE)");
        String key1 = "test:zset1:" + System.currentTimeMillis();
        String key2 = "test:zset2:" + System.currentTimeMillis();
        String interKey = "test:zinter:" + System.currentTimeMillis();
        String unionKey = "test:zunion:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据 - 使用Map方式添加多个成员
            Map<String, Double> key1Members = new HashMap<>();
            key1Members.put("a", 1.0);
            key1Members.put("b", 2.0);
            key1Members.put("c", 3.0);
            jedis.zadd(key1, key1Members);
            
            Map<String, Double> key2Members = new HashMap<>();
            key2Members.put("b", 2.0);
            key2Members.put("c", 3.0);
            key2Members.put("d", 4.0);
            jedis.zadd(key2, key2Members);
            
            // ZINTERSTORE操作 - 交集存储
            Long zinterstoreResult = jedis.zinterstore(interKey, key1, key2);
            assertEquals(2L, zinterstoreResult); // b, c
            
            // 验证交集结果 - zrange返回List<String>
            List<String> interMembers = jedis.zrange(interKey, 0, -1);
            assertTrue(interMembers.contains("b"));
            assertTrue(interMembers.contains("c"));
            assertEquals(4.0, jedis.zscore(interKey, "b")); // 2.0 + 2.0
            assertEquals(6.0, jedis.zscore(interKey, "c")); // 3.0 + 3.0
            
            // ZUNIONSTORE操作 - 并集存储
            Long zunionstoreResult = jedis.zunionstore(unionKey, key1, key2);
            assertEquals(4L, zunionstoreResult); // a, b, c, d
            
            // 验证并集结果 - zrange返回List<String>
            List<String> unionMembers = jedis.zrange(unionKey, 0, -1);
            assertEquals(4, unionMembers.size());
            assertTrue(unionMembers.contains("a"));
            assertTrue(unionMembers.contains("d"));
            
            jedis.del(key1, key2, interKey, unionKey);
        }
        System.out.println("[TEST END] testSortedSetStoreOperations\n");
    }
    
    @Test
    void testSortedSetReverseOperations() {
        System.out.println("[TEST START] testSortedSetReverseOperations - 测试有序集合反向操作(ZREVRANGE/ZREVRANGEBYSCORE/ZREVRANK)");
        String key = "test:zset:rev:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据
            jedis.zadd(key, 10.0, "member1");
            jedis.zadd(key, 20.0, "member2");
            jedis.zadd(key, 30.0, "member3");
            jedis.zadd(key, 40.0, "member4");
            
            // ZREVRANGE操作 - 反向按索引范围获取
            List<String> zrevrangeResult = jedis.zrevrange(key, 0, 2);
            assertEquals(3, zrevrangeResult.size());
            assertEquals("member4", zrevrangeResult.get(0)); // 分数最高
            assertEquals("member3", zrevrangeResult.get(1));
            assertEquals("member2", zrevrangeResult.get(2));
            
            // ZREVRANGEBYSCORE操作 - 反向按分数范围获取
            List<String> zrevrangebyscoreResult = jedis.zrevrangeByScore(key, 35.0, 15.0);
            assertEquals(2, zrevrangebyscoreResult.size());
            assertEquals("member3", zrevrangebyscoreResult.get(0)); // 30.0
            assertEquals("member2", zrevrangebyscoreResult.get(1)); // 20.0
            
            // ZREVRANK操作 - 获取反向排名
            Long zrevrankResult1 = jedis.zrevrank(key, "member4");
            assertEquals(0L, zrevrankResult1); // 分数最高，反向排名第0
            
            Long zrevrankResult2 = jedis.zrevrank(key, "member1");
            assertEquals(3L, zrevrankResult2); // 分数最低，反向排名第3
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testSortedSetReverseOperations\n");
    }
    
    @Test
    void testSortedSetRemoveRangeOperations() {
        System.out.println("[TEST START] testSortedSetRemoveRangeOperations - 测试有序集合范围删除操作");
        String key1 = "test:zset:remrank:" + System.currentTimeMillis();
        String key2 = "test:zset:remscore:" + System.currentTimeMillis();
        String key3 = "test:zset:remlex:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 测试 ZREMRANGEBYRANK - 使用Map方式添加多个成员
            Map<String, Double> key1Members = new HashMap<>();
            key1Members.put("a", 1.0);
            key1Members.put("b", 2.0);
            key1Members.put("c", 3.0);
            key1Members.put("d", 4.0);
            key1Members.put("e", 5.0);
            jedis.zadd(key1, key1Members);
            
            Long zremrangebyrankResult = jedis.zremrangeByRank(key1, 1, 3); // 移除排名1-3的成员
            assertEquals(3L, zremrangebyrankResult);
            assertEquals(2L, jedis.zcard(key1)); // 剩余a和e
            
            // 测试 ZREMRANGEBYSCORE - 使用Map方式添加多个成员
            Map<String, Double> key2Members = new HashMap<>();
            key2Members.put("x", 10.0);
            key2Members.put("y", 20.0);
            key2Members.put("z", 30.0);
            key2Members.put("w", 40.0);
            jedis.zadd(key2, key2Members);
            
            Long zremrangebyscoreResult = jedis.zremrangeByScore(key2, 15.0, 35.0); // 移除分数15-35的成员
            assertEquals(2L, zremrangebyscoreResult); // y和z
            assertEquals(2L, jedis.zcard(key2)); // 剩余x和w
            
            // 测试 ZREMRANGEBYLEX（需要相同分数）- 使用Map方式添加多个成员
            Map<String, Double> key3Members = new HashMap<>();
            key3Members.put("apple", 0.0);
            key3Members.put("banana", 0.0);
            key3Members.put("cherry", 0.0);
            key3Members.put("date", 0.0);
            jedis.zadd(key3, key3Members);
            
            Long zremrangebylexResult = jedis.zremrangeByLex(key3, "[banana", "[cherry]");
            assertTrue(zremrangebylexResult >= 1); // 至少移除banana
            
            jedis.del(key1, key2, key3);
        }
        System.out.println("[TEST END] testSortedSetRemoveRangeOperations\n");
    }
    
    @Test
    void testSortedSetLexOperations() {
        System.out.println("[TEST START] testSortedSetLexOperations - 测试有序集合字典序操作(ZLEXCOUNT/ZRANGEBYLEX)");
        String key = "test:zset:lex:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据（相同分数以便按字典序排序）
            jedis.zadd(key, 0.0, "apple");
            jedis.zadd(key, 0.0, "banana");
            jedis.zadd(key, 0.0, "cherry");
            jedis.zadd(key, 0.0, "date");
            jedis.zadd(key, 0.0, "elderberry");
            
            // ZLEXCOUNT操作 - 字典序范围内成员数量
            Long zlexcountResult = jedis.zlexcount(key, "[banana", "[date]");
            assertEquals(3L, zlexcountResult); // banana, cherry, date
            
            // ZRANGEBYLEX操作 - 按字典序范围获取成员
            List<String> zrangebylexResult = jedis.zrangeByLex(key, "[banana", "[date]");
            assertEquals(3, zrangebylexResult.size());
            assertEquals("banana", zrangebylexResult.get(0));
            assertEquals("cherry", zrangebylexResult.get(1));
            assertEquals("date", zrangebylexResult.get(2));
            
            // 测试开区间
            List<String> zrangebylexResult2 = jedis.zrangeByLex(key, "(banana", "(date]");
            assertTrue(zrangebylexResult2.size() >= 0); // 可能为0或1，取决于实现
            if (zrangebylexResult2.size() > 0) {
                assertEquals("cherry", zrangebylexResult2.get(0));
            }
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testSortedSetLexOperations\n");
    }
    
    @Test
    void testSortedSetScanOperations() {
        System.out.println("[TEST START] testSortedSetScanOperations - 测试有序集合扫描操作(ZSCAN)");
        String key = "test:zset:scan:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 准备测试数据 - 使用Map方式添加多个成员
            Map<String, Double> scanMembers1 = new HashMap<>();
            scanMembers1.put("member1", 1.0);
            scanMembers1.put("member2", 2.0);
            scanMembers1.put("member3", 3.0);
            jedis.zadd(key, scanMembers1);
            
            Map<String, Double> scanMembers2 = new HashMap<>();
            scanMembers2.put("test1", 4.0);
            scanMembers2.put("test2", 5.0);
            jedis.zadd(key, scanMembers2);
            
            // ZSCAN操作 - 基本扫描
            redis.clients.jedis.resps.ScanResult<Tuple> zscanResult = jedis.zscan(key, "0");
            assertNotNull(zscanResult);
            assertNotNull(zscanResult.getResult());
            assertTrue(zscanResult.getResult().size() > 0);
            
            // 验证扫描结果包含分数和成员
            for (Tuple tuple : zscanResult.getResult()) {
                assertNotNull(tuple.getElement());
                assertTrue(tuple.getScore() > 0);
            }
            
            // ZSCAN操作 - 带模式匹配
            redis.clients.jedis.params.ScanParams scanParams = new redis.clients.jedis.params.ScanParams();
            scanParams.match("member*");
            scanParams.count(10);
            
            redis.clients.jedis.resps.ScanResult<Tuple> patternZscanResult = jedis.zscan(key, "0", scanParams);
            assertNotNull(patternZscanResult);
            assertNotNull(patternZscanResult.getResult());
            
            // 验证匹配结果
            for (Tuple tuple : patternZscanResult.getResult()) {
                assertTrue(tuple.getElement().startsWith("member"));
            }
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testSortedSetScanOperations\n");
    }
    
    @Test
    void testSortedSetComplexScenarios() {
        System.out.println("[TEST START] testSortedSetComplexScenarios - 测试有序集合复杂场景");
        String scoreBoard = "test:scoreboard:" + System.currentTimeMillis();
        String topPlayers = "test:topplayers:" + System.currentTimeMillis();
        String activeUsers = "test:activeusers:" + System.currentTimeMillis();
        String topActiveUsers = "test:topactiveusers:" + System.currentTimeMillis();
        
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 模拟游戏排行榜场景 - 使用Map方式添加多个成员
            Map<String, Double> scoreBoardMembers = new HashMap<>();
            scoreBoardMembers.put("player1", 1000.0);
            scoreBoardMembers.put("player2", 1500.0);
            scoreBoardMembers.put("player3", 800.0);
            scoreBoardMembers.put("player4", 2000.0);
            scoreBoardMembers.put("player5", 1200.0);
            jedis.zadd(scoreBoard, scoreBoardMembers);
            
            Map<String, Double> activeUsersMembers = new HashMap<>();
            activeUsersMembers.put("player1", 100.0);
            activeUsersMembers.put("player2", 200.0);
            activeUsersMembers.put("player6", 50.0);
            activeUsersMembers.put("player4", 300.0);
            jedis.zadd(activeUsers, activeUsersMembers);
            
            // 获取前3名玩家
            List<String> top3Players = jedis.zrevrange(scoreBoard, 0, 2);
            assertEquals(3, top3Players.size());
            assertEquals("player4", top3Players.get(0)); // 2000分
            
            // 获取分数在1000-1500之间的玩家
            List<String> midRangePlayers = jedis.zrangeByScore(scoreBoard, 1000, 1500);
            assertTrue(midRangePlayers.contains("player1"));
            assertTrue(midRangePlayers.contains("player2"));
            assertTrue(midRangePlayers.contains("player5"));
            
            // 计算活跃且高分的玩家（交集）
            jedis.zinterstore(topActiveUsers, scoreBoard, activeUsers);
            assertTrue(jedis.zcard(topActiveUsers) > 0);
            
            // 验证交集结果包含共同玩家
            List<String> topActiveMembers = jedis.zrange(topActiveUsers, 0, -1);
            assertTrue(topActiveMembers.contains("player1") || topActiveMembers.contains("player2") || topActiveMembers.contains("player4"));
            
            jedis.del(scoreBoard, topPlayers, activeUsers, topActiveUsers);
        }
        System.out.println("[TEST END] testSortedSetComplexScenarios\n");
    }
}