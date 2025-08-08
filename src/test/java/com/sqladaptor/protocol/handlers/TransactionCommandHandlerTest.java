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

public class TransactionCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    private TransactionCommandHandler handler;
    private AutoCloseable closeable;
    @Mock
    private RedisToSqlConverter mockConverter;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new TransactionCommandHandler(mockDatabaseManager,mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandleMultiCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("MULTI", Arrays.asList());
        String response = handler.handle(command);
        assertEquals("+OK\r\n", response);
    }
    
    @Test
    void testHandleExecCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("EXEC", Arrays.asList());
        String response = handler.handle(command);
        assertTrue(response.startsWith("*"));
    }
    
    @Test
    void testHandleDiscardCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("DISCARD", Arrays.asList());
        String response = handler.handle(command);
        assertEquals("+OK\r\n", response);
    }
    
    @Test
    void testHandleWatchCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("WATCH", Arrays.asList("key1"));
        String response = handler.handle(command);
        assertEquals("+OK\r\n", response);
    }
    
    @Test
    void testHandleUnwatchCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("UNWATCH", Arrays.asList());
        String response = handler.handle(command);
        assertEquals("+OK\r\n", response);
    }
}