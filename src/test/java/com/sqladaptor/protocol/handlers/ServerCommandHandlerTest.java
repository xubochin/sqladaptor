package com.sqladaptor.protocol.handlers;

import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.ast.RedisCommandNode.CommandType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

public class ServerCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    @Mock
    private RedisToSqlConverter mockConverter;
    
    private ServerCommandHandler handler;
    private AutoCloseable closeable;
    
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new ServerCommandHandler(mockDatabaseManager, mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandleInfoCommand() throws Exception {
        // Given
        RedisCommandNode infoCommand = new RedisCommandNode("INFO");
        
        // When
        String result = handler.handle(infoCommand);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("$"));
    }
    
    @Test
    void testHandleConfigGetCommand() throws Exception {
        // Given
        RedisCommandNode configCommand = new RedisCommandNode("CONFIG", Arrays.asList("GET", "timeout"));
        
        // When
        String result = handler.handle(configCommand);
        
        // Then
        assertNotNull(result);
    }
    
    @Test
    void testHandleConfigSetCommand() throws Exception {
        // Given
        RedisCommandNode configCommand = new RedisCommandNode("CONFIG", Arrays.asList("SET", "timeout", "300"));
        
        // When
        String result = handler.handle(configCommand);
        
        // Then
        assertEquals("+OK\r\n", result);
    }
    
    @Test
    void testHandleFlushDbCommand() throws Exception {
        // Given
        RedisCommandNode flushDbCommand = new RedisCommandNode("FLUSHDB");
        
        // When
        String result = handler.handle(flushDbCommand);
        
        // Then
        assertEquals("+OK\\r\\n", result);
    }
    
    @Test
    void testHandleFlushAllCommand() throws Exception {
        // Given
        RedisCommandNode flushAllCommand = new RedisCommandNode("FLUSHALL");
        
        // When
        String result = handler.handle(flushAllCommand);
        
        // Then
        assertEquals("+OK\r\n", result);
    }
    
    @Test
    void testHandleDbSizeCommand() throws Exception {
        // Given
        RedisCommandNode dbSizeCommand = new RedisCommandNode("DBSIZE");
        
        // When
        String result = handler.handle(dbSizeCommand);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith(":"));
    }
    
    @Test
    void testHandleTimeCommand() throws Exception {
        // Given
        RedisCommandNode timeCommand = new RedisCommandNode("TIME");
        
        // When
        String result = handler.handle(timeCommand);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("*2"));
    }
    
    @Test
    void testHandleUnsupportedCommand() throws Exception {
        // Given
        RedisCommandNode unsupportedCommand = new RedisCommandNode("UNSUPPORTED");
        
        // When
        String result = handler.handle(unsupportedCommand);
        
        // Then
        assertTrue(result.startsWith("-ERR"));
    }
}