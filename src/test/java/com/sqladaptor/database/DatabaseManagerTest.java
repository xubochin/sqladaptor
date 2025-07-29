package com.sqladaptor.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

public class DatabaseManagerTest {
    
    private DatabaseManager databaseManager;
    
    @BeforeEach
    void setUp() {
        // 使用内存SQLite数据库进行测试
        System.setProperty("database.type", "sqlite");
        System.setProperty("database.path", ":memory:");
        databaseManager = new DatabaseManager();
    }
    
    @AfterEach
    void tearDown() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        System.clearProperty("database.type");
        System.clearProperty("database.path");
    }
    
    @Test
    void testExecuteUpdate() throws SQLException {
        String sql = "INSERT OR REPLACE INTO redis_kv (key_name, value_data) VALUES (?, ?)";
        int result = databaseManager.executeUpdate(sql, "testkey", "testvalue");
        assertEquals(1, result);
    }
    
    @Test
    void testExecuteQuery() throws SQLException {
        // 先插入数据
        String insertSql = "INSERT OR REPLACE INTO redis_kv (key_name, value_data) VALUES (?, ?)";
        databaseManager.executeUpdate(insertSql, "testkey", "testvalue");
        
        // 查询数据
        String selectSql = "SELECT value_data FROM redis_kv WHERE key_name = ?";
        String result = databaseManager.executeQuery(selectSql, "testkey");
        assertEquals("testvalue", result);
    }
    
    @Test
    void testExecuteQueryNotFound() throws SQLException {
        String selectSql = "SELECT value_data FROM redis_kv WHERE key_name = ?";
        String result = databaseManager.executeQuery(selectSql, "nonexistent");
        assertNull(result);
    }
    
    @Test
    void testExecuteQueryMultiple() throws SQLException {
        // 插入哈希数据
        String insertSql = "INSERT OR REPLACE INTO redis_hash (key_name, field_name, value_data) VALUES (?, ?, ?)";
        databaseManager.executeUpdate(insertSql, "testhash", "field1", "value1");
        databaseManager.executeUpdate(insertSql, "testhash", "field2", "value2");
        
        // 查询多行数据
        String selectSql = "SELECT field_name, value_data FROM redis_hash WHERE key_name = ?";
        List<String[]> results = databaseManager.executeQueryMultiple(selectSql, "testhash");
        
        assertEquals(2, results.size());
        assertEquals("field1", results.get(0)[0]);
        assertEquals("value1", results.get(0)[1]);
        assertEquals("field2", results.get(1)[0]);
        assertEquals("value2", results.get(1)[1]);
    }
    
    @Test
    void testExecuteUpdateWithObjectArray() throws SQLException {
        String sql = "INSERT OR REPLACE INTO redis_kv (key_name, value_data) VALUES (?, ?)";
        Object[] params = {"testkey", "testvalue"};
        int result = databaseManager.executeUpdate(sql, params);
        assertEquals(1, result);
    }
}