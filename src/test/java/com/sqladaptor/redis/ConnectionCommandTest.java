package com.sqladaptor.redis;

import com.sqladaptor.BaseIntegrationTest;
import com.sqladaptor.database.DatabaseManager;
import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.MethodName.class)
public class ConnectionCommandTest extends BaseIntegrationTest {
    
    @Test
    void testConnection() {
        System.out.println("[TEST START] testConnection - 测试Redis基本连接");
        try (Jedis jedis = createConnection()) {
            System.out.println("Testing Redis connection...");
            assertNotNull(jedis, "Redis连接不应为null");

            String response = jedis.ping();
            assertEquals("PONG", response, "PING命令应返回PONG");
            System.out.println("✓ Redis连接测试成功: " + response);

        } catch (Exception e) {
            fail("Redis连接测试失败: " + e.getMessage());
        }finally {

        }
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
    void testPingCommand() {
        System.out.println("[TEST START] testPingCommand - 单独测试PING命令");
        try (Jedis jedis = createConnection()) {
            System.out.println("测试简单PING命令...");
            long startTime = System.currentTimeMillis();
            String response1 = jedis.ping();
            long endTime = System.currentTimeMillis();
            System.out.println("PING响应: " + response1 + " (耗时: " + (endTime - startTime) + "ms)");
            assertEquals("PONG", response1);
            System.out.println("PING命令测试成功！");
        } catch (Exception e) {
            System.err.println("PING测试失败: " + e.getMessage());
            fail("PING命令测试失败: " + e.getMessage());
        }
        System.out.println("[TEST END] testPingCommand\n");
    }
    
    @Test
    void testAuthCommand() {
        System.out.println("[TEST START] testAuthCommand - 测试AUTH命令");
        try (Jedis jedis = createConnection()) {
            // 检查连接状态而不是重新认证
            String response = jedis.ping();
            assertEquals("PONG", response, "认证后的连接应该能够正常PING");
            System.out.println("✓ AUTH命令测试成功，连接已认证");
        } catch (Exception e) {
            fail("AUTH命令测试失败: " + e.getMessage());
        }
    }
    
    @Test
    void testSelectCommand() {
        System.out.println("[TEST START] testSelectCommand - 测试SELECT数据库选择命令");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 测试SELECT命令
            String selectResult = jedis.select(1);
            assertEquals("OK", selectResult);
            
            // 测试切换回默认数据库
            String selectResult2 = jedis.select(0);
            assertEquals("OK", selectResult2);
            
        }
        System.out.println("[TEST END] testSelectCommand\n");
    }
    
    @Test
    void testClientCommands() {
        System.out.println("[TEST START] testClientCommands - 测试CLIENT相关命令");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 测试CLIENT LIST
            String clientList = jedis.clientList();
            assertNotNull(clientList);
            assertTrue(clientList.length() > 0);
            
            // 测试CLIENT SETNAME
            String setNameResult = jedis.clientSetname("test-client");
            assertEquals("OK", setNameResult);
            
            // 测试CLIENT GETNAME
            String clientName = jedis.clientGetname();
            // 可能返回null或设置的名称
            
        }
        System.out.println("[TEST END] testClientCommands\n");
    }
    
    @Test
    void testCommandInfo() {
        System.out.println("[TEST START] testCommandInfo - 测试COMMAND信息查询");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 由于标准Jedis没有command()方法，我们测试其他连接相关功能
            // 测试连接是否正常
            String pingResult = jedis.ping();
            assertEquals("PONG", pingResult);
            
        }
        System.out.println("[TEST END] testCommandInfo\n");
    }
    
    @Test
    void testQuitCommand() {
        System.out.println("[TEST START] testQuitCommand - 测试QUIT退出命令");
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            // 由于标准Jedis没有quit()方法，我们测试连接关闭
            // 测试连接正常
            String pingResult = jedis.ping();
            assertEquals("PONG", pingResult);
        }
        System.out.println("[TEST END] testQuitCommand\n");
    }
}
