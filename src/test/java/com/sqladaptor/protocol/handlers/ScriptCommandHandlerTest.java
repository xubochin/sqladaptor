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

import java.util.Arrays;

public class ScriptCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    private ScriptCommandHandler handler;
    private AutoCloseable closeable;
    @Mock
    private RedisToSqlConverter mockConverter;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new ScriptCommandHandler(mockDatabaseManager,mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandleEvalCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("EVAL", Arrays.asList("return 'hello'", "0"));
        String response = handler.handle(command);
        assertNotNull(response);
    }
    
    @Test
    void testHandleEvalShaCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("EVALSHA", Arrays.asList("sha1hash", "0"));
        String response = handler.handle(command);
        assertNotNull(response);
    }
    
    @Test
    void testHandleScriptLoadCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("SCRIPT", Arrays.asList("LOAD", "return 'hello'"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("+"));
    }
    
    @Test
    void testHandleScriptExistsCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("SCRIPT", Arrays.asList("EXISTS", "sha1hash"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*"));
    }
    
    @Test
    void testHandleScriptFlushCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("SCRIPT", Arrays.asList("FLUSH"));
        String response = handler.handle(command);
        assertEquals("+OK\r\n", response);
    }
}