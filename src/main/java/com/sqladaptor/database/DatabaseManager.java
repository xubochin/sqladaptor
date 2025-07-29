package com.sqladaptor.database;

import com.sqladaptor.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    
    private final DatabaseConfig config;
    private Connection connection;
    
    public DatabaseManager() {
        this.config = new DatabaseConfig();
        initializeDatabase();
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
        String url = "jdbc:sqlite:" + config.getDatabasePath();
        connection = DriverManager.getConnection(url);
    }
    
    private void initializeVoltdb() throws SQLException {
        String url = "jdbc:voltdb://" + config.getHost() + ":" + config.getPort();
        Properties props = new Properties();
        props.setProperty("user", config.getUsername());
        props.setProperty("password", config.getPassword());
        connection = DriverManager.getConnection(url, props);
    }
    
    private void createTables() throws SQLException {
        // 创建键值对表
        String createKvTable = "CREATE TABLE IF NOT EXISTS redis_kv (" +
                "key_name VARCHAR(255) PRIMARY KEY," +
                "value_data TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        
        // 创建哈希表
        String createHashTable = "CREATE TABLE IF NOT EXISTS redis_hash (" +
                "key_name VARCHAR(255)," +
                "field_name VARCHAR(255)," +
                "value_data TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "PRIMARY KEY (key_name, field_name)" +
                ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createKvTable);
            stmt.execute(createHashTable);
        }
    }
    
    public int executeUpdate(String sql, String... params) throws SQLException {
        logger.debug("Executing update: {} with params: {}", sql, java.util.Arrays.toString(params));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        }
    }
    
    public int executeUpdate(String sql, Object[] params) throws SQLException {
        logger.debug("Executing update: {} with params: {}", sql, java.util.Arrays.toString(params));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        }
    }
    
    public String executeQuery(String sql, String... params) throws SQLException {
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
    
    public List<String[]> executeQueryMultiple(String sql, String... params) throws SQLException {
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
    
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }
}