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

public class KeyCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    private KeyCommandHandler handler;
    private AutoCloseable closeable;
    @Mock
    private RedisToSqlConverter mockConverter;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new KeyCommandHandler(mockDatabaseManager,mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandleDelCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("DEL", Arrays.asList("key1"));
        String response = handler.handle(command);
        assertEquals(":1\r\n", response);
    }
    
    @Test
    void testHandleExistsCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("value");
        
        RedisCommandNode command = new RedisCommandNode("EXISTS", Arrays.asList("key1"));
        String response = handler.handle(command);
        assertEquals(":1\r\n", response);
    }
    
    @Test
    void testHandleExpireCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("EXPIRE", Arrays.asList("key1", "60"));
        String response = handler.handle(command);
        assertEquals(":1\r\n", response);
    }
    
    @Test
    void testHandleTtlCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("30");
        
        RedisCommandNode command = new RedisCommandNode("TTL", Arrays.asList("key1"));
        String response = handler.handle(command);
        assertEquals(":30\r\n", response);
    }
    
    @Test
    void testHandleKeysCommand() throws Exception {
        when(mockDatabaseManager.executeListQuery(anyString(), any())).thenReturn(Arrays.asList("key1", "key2", "key3"));
        
        RedisCommandNode command = new RedisCommandNode("KEYS", Arrays.asList("*"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*3\r\n"));
    }
    
    @Test
    void testHandleTypeCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("string");
        
        RedisCommandNode command = new RedisCommandNode("TYPE", Arrays.asList("key1"));
        String response = handler.handle(command);
        assertEquals("+string\r\n", response);
    }
    
    @Test
    void testHandleRenameCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("RENAME", Arrays.asList("oldkey", "newkey"));
        String response = handler.handle(command);
        assertEquals("+OK\r\n", response);
    }
}