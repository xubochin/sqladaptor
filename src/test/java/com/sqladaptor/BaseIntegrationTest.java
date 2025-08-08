package com.sqladaptor;

import com.sqladaptor.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class BaseIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseIntegrationTest.class);
    private static final int SERVER_PORT = 6379;
    private static RedisToJdbcServer server;
    private static Thread serverThread;
    private static DatabaseManager databaseManager; 
    
    // Redis连接配置
    protected static final String REDIS_URL = "redis://127.0.0.1:6379";
    
    // //@BeforeAll
    // static void startServer() throws Exception {
    //     logger.info("Starting Redis to JDBC Adapter Server for tests...");
        
    //     // 初始化数据库管理器
    //     databaseManager = new DatabaseManager();
        
    //     // 创建服务器实例
    //     server = new RedisToJdbcServer(SERVER_PORT, databaseManager);
        
    //     // 在单独的线程中启动服务器
    //     CompletableFuture<Void> serverFuture = CompletableFuture.runAsync(() -> {
    //         try {
    //             server.start();
    //         } catch (InterruptedException e) {
    //             Thread.currentThread().interrupt();
    //             logger.warn("Server thread interrupted");
    //         } catch (Exception e) {
    //             logger.error("Failed to start server", e);
    //         }
    //     });
        
    //     // 等待服务器启动
    //     Thread.sleep(2000); // 给服务器2秒时间启动
    //     logger.info("Redis to JDBC Adapter Server started on port {}", SERVER_PORT);
    // }
    
    //@AfterAll
    // static void stopServer() throws Exception {
    //     logger.info("Stopping Redis to JDBC Adapter Server...");
        
    //     if (server != null) {
    //         server.stop();
    //     }
        
    //     if (serverThread != null && serverThread.isAlive()) {
    //         serverThread.interrupt();
    //     }
        
    //     if (databaseManager != null) {
    //         databaseManager.close();
    //     }
    //
    //     logger.info("Redis to JDBC Adapter Server stopped");
    // }
    
//    protected static int getServerPort() {
//        return SERVER_PORT;
//    }
    
    /**
     * 创建Redis连接，包含重试逻辑
     */
    protected Jedis createConnection() {
        int maxRetries = 3;
        int retryDelay = 1000;
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                URI redisUri = URI.create(REDIS_URL);
                System.out.println("Connection string " + redisUri);
                Jedis jedis = new Jedis(redisUri, 300000, 300000);
                jedis.ping();
                return jedis;
            } catch (Exception e) {
                System.out.println("Connection attempt " + (i + 1) + " failed: " + e.getMessage());
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
    
    /**
     * 创建Redis连接（简化版本，不包含重试逻辑）
     */
    protected Jedis createSimpleConnection() {
        try {
            URI redisUri = URI.create(REDIS_URL);
            return new Jedis(redisUri, 300000, 300000);
        } catch (Exception e) {
            fail("Failed to create Redis connection: " + e.getMessage());
            return null;
        }
    }
}