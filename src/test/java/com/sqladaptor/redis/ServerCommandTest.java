package com.sqladaptor.redis;

import com.sqladaptor.BaseIntegrationTest;
import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;
import redis.clients.jedis.resps.Slowlog;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ServerCommandTest extends BaseIntegrationTest {
    
    @Test
    void testServerOperations() {
        System.out.println("[TEST START] testServerOperations - 测试服务器操作(INFO/DBSIZE/FLUSHDB)");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // INFO操作
            String infoResult = jedis.info();
            assertNotNull(infoResult);
            assertFalse(infoResult.isEmpty());
            
            // DBSIZE操作
            long dbsizeResult = jedis.dbSize();
            assertTrue(dbsizeResult >= 0);
            
            // TIME操作
            List<String> timeResult = jedis.time();
            assertEquals(2, timeResult.size());
            
        }
        System.out.println("[TEST END] testServerOperations\n");
    }
    
    @Test
    void testDatabaseOperations() {
        System.out.println("[TEST START] testDatabaseOperations - 测试数据库操作");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // SELECT操作 - 选择数据库
            String selectResult = jedis.select(1);
            assertEquals("OK", selectResult);
            
            // 切换回数据库0
            jedis.select(0);
            
            // FLUSHDB操作 - 清空当前数据库
            String flushDbResult = jedis.flushDB();
            assertEquals("OK", flushDbResult);
            
            // FLUSHALL操作 - 清空所有数据库
            String flushAllResult = jedis.flushAll();
            assertEquals("OK", flushAllResult);
            
        }
        System.out.println("[TEST END] testDatabaseOperations\n");
    }
    
    @Test
    void testConfigOperations() {
        System.out.println("[TEST START] testConfigOperations - 测试配置操作");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // CONFIG GET操作
            Map<String, String> configGet = jedis.configGet("timeout");
            assertNotNull(configGet);
            
            // CONFIG SET操作
            String configSet = jedis.configSet("timeout", "300");
            assertEquals("OK", configSet);
            
            // CONFIG REWRITE操作
            String configRewrite = jedis.configRewrite();
            assertEquals("OK", configRewrite);
            
            // CONFIG RESETSTAT操作
            String configResetStat = jedis.configResetStat();
            assertEquals("OK", configResetStat);
            
        }
        System.out.println("[TEST END] testConfigOperations\n");
    }
    
    @Test
    void testPersistenceOperations() {
        System.out.println("[TEST START] testPersistenceOperations - 测试持久化操作");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // SAVE操作 - 同步保存
            String saveResult = jedis.save();
            assertEquals("OK", saveResult);
            
            // BGSAVE操作 - 后台保存
            String bgsaveResult = jedis.bgsave();
            assertEquals("Background saving started", bgsaveResult);
            
            // BGREWRITEAOF操作 - 后台重写AOF
            String bgrewriteaofResult = jedis.bgrewriteaof();
            assertTrue(bgrewriteaofResult.contains("rewriting") || bgrewriteaofResult.equals("OK"));
            
            // LASTSAVE操作 - 最后保存时间
            long lastnameResult = jedis.lastsave();
            assertTrue(lastnameResult > 0);
            
        }
        System.out.println("[TEST END] testPersistenceOperations\n");
    }
    
    @Test
    void testReplicationOperations() {
        System.out.println("[TEST START] testReplicationOperations - 测试复制操作");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // ROLE操作 - 获取角色信息
            List<Object> roleResult = jedis.role();
            assertNotNull(roleResult);
            assertFalse(roleResult.isEmpty());
            
            // SLAVEOF操作 - 设置主从复制 (测试NO ONE)
            String slaveofResult = jedis.slaveofNoOne();
            assertEquals("OK", slaveofResult);
            
        }
        System.out.println("[TEST END] testReplicationOperations\n");
    }
    
    @Test
    void testMonitoringOperations() {
        System.out.println("[TEST START] testMonitoringOperations - 测试监控操作");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // SLOWLOG操作
            List<Slowlog> slowlogGet = jedis.slowlogGet();
            assertNotNull(slowlogGet);
            
            long slowlogLen = jedis.slowlogLen();
            assertTrue(slowlogLen >= 0);
            
            String slowlogReset = jedis.slowlogReset();
            assertEquals("OK", slowlogReset);
            
        }
        System.out.println("[TEST END] testMonitoringOperations\n");
    }
    
    @Test
    void testCommandOperations() {
        System.out.println("[TEST START] testCommandOperations - 测试命令操作");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // COMMAND COUNT操作
            long commandCount = jedis.commandCount();
            assertTrue(commandCount > 0);
            // 移除不存在的command()方法调用
            // 可以使用其他方式测试命令相关功能
            
        }
        System.out.println("[TEST END] testCommandOperations\n");
    }
    
    @Test
    void testAdvancedInfoOperations() {
        System.out.println("[TEST START] testAdvancedInfoOperations - 测试高级INFO操作");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // INFO server
            String infoServer = jedis.info("server");
            assertNotNull(infoServer);
            assertTrue(infoServer.contains("# Server"));
            
            // INFO clients
            String infoClients = jedis.info("clients");
            assertNotNull(infoClients);
            assertTrue(infoClients.contains("# Clients"));
            
            // INFO memory
            String infoMemory = jedis.info("memory");
            assertNotNull(infoMemory);
            assertTrue(infoMemory.contains("# Memory"));
            
            // INFO persistence
            String infoPersistence = jedis.info("persistence");
            assertNotNull(infoPersistence);
            assertTrue(infoPersistence.contains("# Persistence"));
            
            // INFO stats
            String infoStats = jedis.info("stats");
            assertNotNull(infoStats);
            assertTrue(infoStats.contains("# Stats"));
            
            // INFO replication
            String infoReplication = jedis.info("replication");
            assertNotNull(infoReplication);
            assertTrue(infoReplication.contains("# Replication"));
            
        }
        System.out.println("[TEST END] testAdvancedInfoOperations\n");
    }
    
    @Test
    void testErrorHandling() {
        System.out.println("[TEST START] testErrorHandling - 测试错误处理");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 测试无效的数据库选择
            try {
                jedis.select(999); // 超出范围的数据库
                fail("应该抛出异常");
            } catch (Exception e) {
                // 预期的异常
            }
            
            // 测试CONFIG GET不存在的配置
            Map<String, String> nonExistentConfig = jedis.configGet("nonexistent");
            assertTrue(nonExistentConfig.isEmpty());
            
        }
        System.out.println("[TEST END] testErrorHandling\n");
    }
}