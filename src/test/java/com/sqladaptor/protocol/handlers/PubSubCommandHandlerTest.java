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

public class PubSubCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    private PubSubCommandHandler handler;
    private AutoCloseable closeable;
    @Mock
    private RedisToSqlConverter mockConverter;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new PubSubCommandHandler(mockDatabaseManager,mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandlePublishCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("PUBLISH", Arrays.asList("channel1", "message1"));
        String response = handler.handle(command);
        assertTrue(response.startsWith(":"));
    }
    
    @Test
    void testHandleSubscribeCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("SUBSCRIBE", Arrays.asList("channel1"));
        String response = handler.handle(command);
        assertTrue(response.contains("subscribe"));
    }
    
    @Test
    void testHandleUnsubscribeCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("UNSUBSCRIBE", Arrays.asList("channel1"));
        String response = handler.handle(command);
        assertTrue(response.contains("unsubscribe"));
    }
    
    @Test
    void testHandlePSubscribeCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("PSUBSCRIBE", Arrays.asList("pattern*"));
        String response = handler.handle(command);
        assertTrue(response.contains("psubscribe"));
    }
    
    @Test
    void testHandlePUnsubscribeCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("PUNSUBSCRIBE", Arrays.asList("pattern*"));
        String response = handler.handle(command);
        assertTrue(response.contains("punsubscribe"));
    }
    
    @Test
    void testHandlePubSubChannelsCommand() throws Exception {
        RedisCommandNode command = new RedisCommandNode("PUBSUB", Arrays.asList("CHANNELS"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*"));
    }
}