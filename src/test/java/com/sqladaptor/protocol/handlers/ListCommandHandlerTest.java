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

public class ListCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    private ListCommandHandler handler;
    private AutoCloseable closeable;
    @Mock
    private RedisToSqlConverter mockConverter;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new ListCommandHandler(mockDatabaseManager,mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandleLPushCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        when(mockDatabaseManager.executeCountQuery(anyString(), any())).thenReturn(1L);
        
        RedisCommandNode command = new RedisCommandNode("LPUSH", Arrays.asList("list1", "value1"));
        String response = handler.handle(command);
        assertEquals(":1\r\n", response);
    }
    
    @Test
    void testHandleRPushCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        when(mockDatabaseManager.executeCountQuery(anyString(), any())).thenReturn(2L);
        
        RedisCommandNode command = new RedisCommandNode("RPUSH", Arrays.asList("list1", "value2"));
        String response = handler.handle(command);
        assertEquals(":2\r\n", response);
    }
    
    @Test
    void testHandleLPopCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("value1");
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("LPOP", Arrays.asList("list1"));
        String response = handler.handle(command);
        assertEquals("+value1\r\n", response);
    }
    
    @Test
    void testHandleRPopCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("value2");
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("RPOP", Arrays.asList("list1"));
        String response = handler.handle(command);
        assertEquals("+value2\r\n", response);
    }
    
    @Test
    void testHandleLLenCommand() throws Exception {
        when(mockDatabaseManager.executeCountQuery(anyString(), any())).thenReturn(5L);
        
        RedisCommandNode command = new RedisCommandNode("LLEN", Arrays.asList("list1"));
        String response = handler.handle(command);
        assertEquals(":5\r\n", response);
    }
    
    @Test
    void testHandleLRangeCommand() throws Exception {
        when(mockDatabaseManager.executeListQuery(anyString(), any())).thenReturn(Arrays.asList("value1", "value2", "value3"));
        
        RedisCommandNode command = new RedisCommandNode("LRANGE", Arrays.asList("list1", "0", "2"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*3\r\n"));
    }
    
    @Test
    void testHandleLIndexCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("value2");
        
        RedisCommandNode command = new RedisCommandNode("LINDEX", Arrays.asList("list1", "1"));
        String response = handler.handle(command);
        assertEquals("+value2\r\n", response);
    }
    
    @Test
    void testHandleLSetCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("LSET", Arrays.asList("list1", "1", "newvalue"));
        String response = handler.handle(command);
        assertEquals("+OK\r\n", response);
    }
}