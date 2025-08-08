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

import java.util.Arrays;

public class ConnectionCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    @Mock
    private RedisToSqlConverter mockConverter;
    
    private ConnectionCommandHandler handler;
    private AutoCloseable closeable;
    
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new ConnectionCommandHandler(mockDatabaseManager, mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandlePingCommand() throws Exception {
        // Given
        RedisCommandNode pingCommand = new RedisCommandNode("PING");
        
        // When
        String result = handler.handle(pingCommand);
        
        // Then
        assertEquals("+PONG\r\n", result);
    }
    
    @Test
    void testHandlePingWithMessage() throws Exception {
        // Given
        RedisCommandNode pingCommand = new RedisCommandNode("PING", Arrays.asList("Hello"));
        
        // When
        String result = handler.handle(pingCommand);
        
        // Then
        assertEquals("+Hello\r\n", result);
    }
    
    @Test
    void testHandleEchoCommand() throws Exception {
        // Given
        RedisCommandNode echoCommand = new RedisCommandNode("ECHO", Arrays.asList("test message"));
        
        // When
        String result = handler.handle(echoCommand);
        
        // Then
        assertEquals("$12\r\ntest message\r\n", result);
    }
    
    @Test
    void testHandleQuitCommand() throws Exception {
        // Given
        RedisCommandNode quitCommand = new RedisCommandNode("QUIT");
        
        // When
        String result = handler.handle(quitCommand);
        
        // Then
        assertEquals("+OK\r\n", result);
    }
    
    @Test
    void testHandleAuthCommand() throws Exception {
        // Given
        RedisCommandNode authCommand = new RedisCommandNode("AUTH", Arrays.asList("password123"));
        
        // When
        String result = handler.handle(authCommand);
        
        // Then
        assertEquals("+OK\r\n", result);
    }
    
    @Test
    void testHandleSelectCommand() throws Exception {
        // Given
        RedisCommandNode selectCommand = new RedisCommandNode("SELECT", Arrays.asList("1"));
        
        // When
        String result = handler.handle(selectCommand);
        
        // Then
        assertEquals("+OK\r\n", result);
    }
    
    @Test
    void testHandleClientCommand() throws Exception {
        // Given
        RedisCommandNode clientCommand = new RedisCommandNode("CLIENT", Arrays.asList("LIST"));
        
        // When
        String result = handler.handle(clientCommand);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("$"));
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
    
    @Test
    void testEchoWithEmptyMessage() throws Exception {
        // Given
        RedisCommandNode echoCommand = new RedisCommandNode("ECHO", Arrays.asList(""));
        
        // When
        String result = handler.handle(echoCommand);
        
        // Then
        assertEquals("$0\r\n\r\n", result);
    }
    
    @Test
    void testSelectWithInvalidDatabase() throws Exception {
        // Given
        RedisCommandNode selectCommand = new RedisCommandNode("SELECT", Arrays.asList("invalid"));
        
        // When
        String result = handler.handle(selectCommand);
        
        // Then
        assertTrue(result.startsWith("-ERR") || result.equals("+OK\r\n"));
    }
}
