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

public class SortedSetCommandHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    private SortedSetCommandHandler handler;
    private AutoCloseable closeable;
    @Mock
    private RedisToSqlConverter mockConverter;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new SortedSetCommandHandler(mockDatabaseManager,mockConverter);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandleZAddCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("ZADD", Arrays.asList("zset1", "1.5", "member1"));
        String response = handler.handle(command);
        assertEquals(":1\r\n", response);
    }
    
    @Test
    void testHandleZRemCommand() throws Exception {
        when(mockDatabaseManager.executeUpdate(anyString(), any())).thenReturn(1);
        
        RedisCommandNode command = new RedisCommandNode("ZREM", Arrays.asList("zset1", "member1"));
        String response = handler.handle(command);
        assertEquals(":1\r\n", response);
    }
    
    @Test
    void testHandleZScoreCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("1.5");
        
        RedisCommandNode command = new RedisCommandNode("ZSCORE", Arrays.asList("zset1", "member1"));
        String response = handler.handle(command);
        assertEquals("+1.5\r\n", response);
    }
    
    @Test
    void testHandleZRankCommand() throws Exception {
        when(mockDatabaseManager.executeQuery(anyString(), any())).thenReturn("2");
        
        RedisCommandNode command = new RedisCommandNode("ZRANK", Arrays.asList("zset1", "member1"));
        String response = handler.handle(command);
        assertEquals(":2\r\n", response);
    }
    
    @Test
    void testHandleZRangeCommand() throws Exception {
        when(mockDatabaseManager.executeListQuery(anyString(), any())).thenReturn(Arrays.asList("member1", "member2", "member3"));
        
        RedisCommandNode command = new RedisCommandNode("ZRANGE", Arrays.asList("zset1", "0", "2"));
        String response = handler.handle(command);
        assertTrue(response.startsWith("*3\r\n"));
    }
    
    @Test
    void testHandleZCardCommand() throws Exception {
        when(mockDatabaseManager.executeCountQuery(anyString(), any())).thenReturn(5L);
        
        RedisCommandNode command = new RedisCommandNode("ZCARD", Arrays.asList("zset1"));
        String response = handler.handle(command);
        assertEquals(":5\r\n", response);
    }
    
    @Test
    void testHandleZCountCommand() throws Exception {
        when(mockDatabaseManager.executeCountQuery(anyString(), any())).thenReturn(3L);
        
        RedisCommandNode command = new RedisCommandNode("ZCOUNT", Arrays.asList("zset1", "1", "3"));
        String response = handler.handle(command);
        assertEquals(":3\r\n", response);
    }
}