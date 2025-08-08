package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.converter.RedisToSqlConverter;
import com.sqladaptor.database.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class StringCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    private StringCommandHandler handler;
    private AutoCloseable closeable;
    @Mock
    private RedisToSqlConverter mockConverter;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new StringCommandHandler(mockDatabaseManager,mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandleSetCommand() throws Exception {
        // 模拟数据库操作
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("SET", Arrays.asList("key1", "value1"));
        String response = handler.handle(command);
        assertEquals("+OK\r\n", response);
        
        verify(mockDatabaseManager).executeUpdate(anyString(), any());
    }
    
    @Test
    void testHandleGetCommand() throws Exception {
        // 模拟数据库查询返回值
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("value1");
        
        RedisCommandNode command = new RedisCommandNode("GET", Arrays.asList("key1"));
        String response = handler.handle(command);
        assertEquals("+value1\r\n", response);
        
        verify(mockDatabaseManager).executeQuery(anyString(), any());
    }
    
    @Test
    void testHandleGetCommandNotFound() throws Exception {
        // 模拟键不存在
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn(null);
        
        RedisCommandNode command = new RedisCommandNode("GET", Arrays.asList("nonexistent"));
        String response = handler.handle(command);
        assertEquals("$-1\r\n", response);
    }
    
    @Test
    void testHandleIncrCommand() throws Exception {
        // 模拟递增操作
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("5");
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("INCR", Arrays.asList("counter"));
        String response = handler.handle(command);
        assertEquals(":6\r\n", response);
    }
    
    @Test
    void testHandleDecrCommand() throws Exception {
        // 模拟递减操作
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("5");
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("DECR", Arrays.asList("counter"));
        String response = handler.handle(command);
        assertEquals(":4\r\n", response);
    }
    
    @Test
    void testHandleIncrByCommand() throws Exception {
        // 模拟按指定值递增
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("10");
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("INCRBY", Arrays.asList("counter", "3"));
        String response = handler.handle(command);
        assertEquals(":13\r\n", response);
    }
    
    @Test
    void testHandleAppendCommand() throws Exception {
        // 模拟字符串追加
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("hello");
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("APPEND", Arrays.asList("key1", " world"));
        String response = handler.handle(command);
        assertEquals(":11\r\n", response); // "hello world"的长度
    }
    
    @Test
    void testHandleStrlenCommand() throws Exception {
        // 模拟字符串长度查询
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("hello");
        
        RedisCommandNode command = new RedisCommandNode("STRLEN", Arrays.asList("key1"));
        String response = handler.handle(command);
        assertEquals(":5\r\n", response);
    }
    
    @Test
    void testDatabaseException() throws Exception {
        // 测试数据库异常处理
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenThrow(new SQLException("Database error"));
        
        RedisCommandNode command = new RedisCommandNode("SET", Arrays.asList("key1", "value1"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("-ERR"));
    }
}