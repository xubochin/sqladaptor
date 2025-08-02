package com.sqladaptor.database;

import com.sqladaptor.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseManager {
    private static volatile DatabaseManager instance;
    private static final Object lock = new Object();
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    
    private final DatabaseConfig config;
    private Connection connection;
    
    public DatabaseManager() {
        this.config = new DatabaseConfig();
        initializeDatabase();
    }
    
    // 获取单例实例的方法
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }
    
    private void initializeDatabase() {
        try {
            switch (config.getDatabaseType().toLowerCase()) {
                case "sqlite":
                    initializeSqlite();
                    break;
                case "voltdb":
                    initializeVoltdb();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported database type: " + config.getDatabaseType());
            }
            
            createTables();
            logger.info("Database initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException(e);
        }
    }
    
    private void initializeSqlite() throws SQLException {
        logger.info("正在初始化SQLite数据库连接...");
        
        try {
            Class.forName("org.sqlite.JDBC");
            logger.debug("SQLite JDBC驱动加载成功");
        } catch (ClassNotFoundException e) {
            logger.error("无法加载SQLite JDBC驱动", e);
            throw new SQLException("SQLite JDBC driver not found", e);
        }
        
        String url = "jdbc:sqlite:" + config.getDatabasePath() + "?busy_timeout=30000";
        logger.info("数据库连接URL: {}", url);
        
        Properties props = new Properties();
        props.setProperty("busy_timeout", "30000");
        
        try {
            connection = DriverManager.getConnection(url, props);
            logger.info("数据库连接建立成功");
            
            // 设置忙等待超时
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA busy_timeout = 30000");
                logger.debug("数据库忙等待超时设置为30秒");
            }
            
            logger.info("SQLite数据库初始化完成");
        } catch (SQLException e) {
            logger.error("建立数据库连接失败 - URL: {}", url, e);
            throw e;
        }
    }
    
    private void initializeVoltdb() throws SQLException {
        String url = "jdbc:voltdb://" + config.getHost() + ":" + config.getPort();
        Properties props = new Properties();
        props.setProperty("user", config.getUsername());
        props.setProperty("password", config.getPassword());
        connection = DriverManager.getConnection(url, props);
    }
    
    private void createTables() throws SQLException {
        String createKvTable = "CREATE TABLE IF NOT EXISTS redis_kv (" +
                "key_name TEXT PRIMARY KEY, " +
                "value_data TEXT, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        
        String createHashTable = "CREATE TABLE IF NOT EXISTS redis_hash (" +
                "hash_key TEXT, " +
                "field_name TEXT, " +
                "field_value TEXT, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "PRIMARY KEY (hash_key, field_name))";
        
        try (Statement stmt = connection.createStatement()) {
            logger.debug("Creating tables...");
            stmt.execute(createKvTable);
            logger.debug("redis_kv table created/verified");
            stmt.execute(createHashTable);
            logger.debug("redis_hash table created/verified");
            
            // 验证表是否存在
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='redis_kv'");
            if (!rs.next()) {
                throw new SQLException("Failed to create redis_kv table");
            }
            logger.info("Database tables initialized successfully");
        }
    }
    
    // 为所有公共方法添加同步锁
    public int executeUpdate(String sql, Object... params) throws SQLException {
        synchronized (this) {
            logger.debug("Executing update: {} with params: {}", sql, java.util.Arrays.toString(params));
            
            // 添加连接状态检查
            if (connection == null) {
                logger.error("Database connection is null - initialization failed");
                throw new SQLException("Database connection is null - initialization may have failed");
            }
            
            if (connection.isClosed()) {
                logger.error("Database connection is closed");
                throw new SQLException("Database connection is closed");
            }
            
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setString(i + 1, params[i] != null ? params[i].toString() : null);
                }
                
                logger.debug("About to execute: {}", stmt.toString());
                int result = stmt.executeUpdate();
                logger.debug("SQL executed successfully, rows affected: {}", result);
                return result;
                
            } catch (SQLException e) {
                logger.error("SQL execution failed - SQL: {}, Params: {}, Error: {}", 
                            sql, java.util.Arrays.toString(params), e.getMessage());
                logger.error("SQLException details: SQLState={}, ErrorCode={}", 
                            e.getSQLState(), e.getErrorCode());
                throw new SQLException("Database error: " + e.getMessage() + " (SQLState: " + e.getSQLState() + ")", e);
            }
        }
    }
    
    
    public String executeQuery(String sql, String... params) throws SQLException {
        synchronized (this) {
            logger.debug("Executing query: {} with params: {}", sql, java.util.Arrays.toString(params));
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setString(i + 1, params[i]);
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString(1);
                    }
                }
            }
            return null;
        }
    }
    
    public List<String[]> executeQueryMultiple(String sql, String... params) throws SQLException {
        synchronized (this) {
            logger.debug("Executing query multiple: {} with params: {}", sql, java.util.Arrays.toString(params));
            List<String[]> results = new ArrayList<>();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setString(i + 1, params[i]);
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    while (rs.next()) {
                        String[] row = new String[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            row[i - 1] = rs.getString(i);
                        }
                        results.add(row);
                    }
                }
            }
            return results;
        }
    }
    
    public synchronized void close() {
        synchronized (this) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing database connection", e);
                }
            }
        }
    }

    public Object executeListQuery(String s, Object any) {
        return null;
    }

    public Object executeHashQuery(String s, Object any) {
        return null;
    }

    public Object executeCountQuery(String s, Object any) {
        return null;
    }
}