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

public class SetCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    @Mock
    private RedisToSqlConverter mockConverter;
    
    private SetCommandHandler handler;
    private AutoCloseable closeable;
    
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new SetCommandHandler(mockDatabaseManager, mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandleSaddCommand() throws Exception {
        // Given
        RedisCommandNode saddCommand = new RedisCommandNode("SADD", Arrays.asList("myset", "member1"));
        
        // When
        String result = handler.handle(saddCommand);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith(":"));
    }
    
    @Test
    void testHandleSmembersCommand() throws Exception {
        // Given
        RedisCommandNode smembersCommand = new RedisCommandNode("SMEMBERS", Arrays.asList("myset"));
        
        // When
        String result = handler.handle(smembersCommand);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("*"));
    }
    
    @Test
    void testHandleSismemberCommand() throws Exception {
        // Given
        RedisCommandNode sismemberCommand = new RedisCommandNode("SISMEMBER", Arrays.asList("myset", "member1"));
        
        // When
        String result = handler.handle(sismemberCommand);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith(":"));
    }
    
    @Test
    void testHandleScardCommand() throws Exception {
        // Given
        RedisCommandNode scardCommand = new RedisCommandNode("SCARD", Arrays.asList("myset"));
        
        // When
        String result = handler.handle(scardCommand);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith(":"));
    }
    
    @Test
    void testHandleSremCommand() throws Exception {
        // Given
        RedisCommandNode sremCommand = new RedisCommandNode("SREM", Arrays.asList("myset", "member1"));
        
        // When
        String result = handler.handle(sremCommand);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith(":"));
    }
    
    @Test
    void testHandleSpopCommand() throws Exception {
        // Given
        RedisCommandNode spopCommand = new RedisCommandNode("SPOP", Arrays.asList("myset"));
        
        // When
        String result = handler.handle(spopCommand);
        
        // Then
        assertNotNull(result);
    }
}