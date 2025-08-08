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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    private HashCommandHandler handler;
    private AutoCloseable closeable;
    @Mock
    private RedisToSqlConverter mockConverter;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new HashCommandHandler(mockDatabaseManager,mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandleHSetCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("HSET", Arrays.asList("hash1", "field1", "value1"));
        String response = handler.handle(command);
        assertEquals(":1\r\n", response);
        
        verify(mockDatabaseManager).executeUpdate(anyString(), any());
    }
    
    @Test
    void testHandleHGetCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("value1");
        
        RedisCommandNode command = new RedisCommandNode("HGET", Arrays.asList("hash1", "field1"));
        String response = handler.handle(command);
        assertEquals("+value1\r\n", response);
    }
    
    @Test
    void testHandleHGetAllCommand() throws Exception {
        Map<String, String> hashData = new HashMap<>();
        hashData.put("field1", "value1");
        hashData.put("field2", "value2");
        when(mockDatabaseManager.executeHashQuery(anyString(), any())).thenReturn(hashData);
        
        RedisCommandNode command = new RedisCommandNode("HGETALL", Arrays.asList("hash1"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*4\r\n")); // 4个元素：2个字段+2个值
    }
    
    @Test
    void testHandleHDelCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("HDEL", Arrays.asList("hash1", "field1"));
        String response = handler.handle(command);
        assertEquals(":1\r\n", response);
    }
    
    @Test
    void testHandleHExistsCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("value1");
        
        RedisCommandNode command = new RedisCommandNode("HEXISTS", Arrays.asList("hash1", "field1"));
        String response = handler.handle(command);
        assertEquals(":1\r\n", response);
    }

    @Test
    void testHandleHKeysCommand() throws Exception {
        // Fix: Use correct method signature for executeQueryMultiple
        when(mockDatabaseManager.executeQueryMultiple(anyString(), any(String[].class)))
                .thenReturn(Arrays.asList(new String[]{"field1"}, new String[]{"field2"}));

        RedisCommandNode command = new RedisCommandNode("HKEYS", Arrays.asList("hash1"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*2\r\n"));
    }
    
    @Test
    void testHandleHValsCommand() throws Exception {
        when(mockDatabaseManager.executeListQuery(anyString(), any())).thenReturn(Arrays.asList("value1", "value2"));
        
        RedisCommandNode command = new RedisCommandNode("HVALS", Arrays.asList("hash1"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*2\r\n"));
    }
    
    @Test
    void testHandleHLenCommand() throws Exception {
        when(mockDatabaseManager.executeCountQuery(anyString(), any())).thenReturn(3L);
        
        RedisCommandNode command = new RedisCommandNode("HLEN", Arrays.asList("hash1"));
        String response = handler.handle(command);
        assertEquals(":3\r\n", response);
    }
}