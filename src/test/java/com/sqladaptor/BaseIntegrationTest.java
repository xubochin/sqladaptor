package com.sqladaptor;

import com.sqladaptor.database.DatabaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class BaseIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseIntegrationTest.class);
    private static final int SERVER_PORT = 6379;
    private static RedisToJdbcServer server;
    private static Thread serverThread;
    private static DatabaseManager databaseManager;
    
    @BeforeAll
    static void startServer() throws Exception {
        logger.info("Starting Redis to JDBC Adapter Server for tests...");
        
        // 初始化数据库管理器
        databaseManager = new DatabaseManager();
        
        // 创建服务器实例
        server = new RedisToJdbcServer(SERVER_PORT, databaseManager);
        
        // 在单独的线程中启动服务器
        CompletableFuture<Void> serverFuture = CompletableFuture.runAsync(() -> {
            try {
                server.start();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Server thread interrupted");
            } catch (Exception e) {
                logger.error("Failed to start server", e);
            }
        });
        
        // 等待服务器启动
        Thread.sleep(2000); // 给服务器2秒时间启动
        logger.info("Redis to JDBC Adapter Server started on port {}", SERVER_PORT);
    }
    
    @AfterAll
    static void stopServer() throws Exception {
        logger.info("Stopping Redis to JDBC Adapter Server...");
        
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
        }
        
        if (databaseManager != null) {
            databaseManager.close();
        }
        
        logger.info("Redis to JDBC Adapter Server stopped");
    }
    
    protected static int getServerPort() {
        return SERVER_PORT;
    }
}