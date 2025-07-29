package com.sqladaptor.protocol;

import com.sqladaptor.database.DatabaseManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class RedisProtocolHandlerTest {
    
    @Mock
    private DatabaseManager mockDatabaseManager;
    
    @Mock
    private ChannelHandlerContext mockCtx;
    
    private RedisProtocolHandler handler;
    private AutoCloseable closeable;
    
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        handler = new RedisProtocolHandler(mockDatabaseManager);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testHandlePingCommand() throws Exception {
        String command = "PING";
        ByteBuf buffer = Unpooled.copiedBuffer(command, CharsetUtil.UTF_8);
        
        handler.channelRead(mockCtx, buffer);
        
        verify(mockCtx).writeAndFlush(any(ByteBuf.class));
    }
    
    @Test
    void testHandleSetCommand() throws Exception {
        String command = "SET key1 value1";
        ByteBuf buffer = Unpooled.copiedBuffer(command, CharsetUtil.UTF_8);
        
        when(mockDatabaseManager.executeUpdate(anyString(), anyString(), anyString()))
            .thenReturn(1);
        
        handler.channelRead(mockCtx, buffer);
        
        verify(mockDatabaseManager).executeUpdate(anyString(), eq("key1"), eq("value1"));
        verify(mockCtx).writeAndFlush(any(ByteBuf.class));
    }
    
    @Test
    void testHandleGetCommand() throws Exception {
        String command = "GET key1";
        ByteBuf buffer = Unpooled.copiedBuffer(command, CharsetUtil.UTF_8);
        
        when(mockDatabaseManager.executeQuery(anyString(), eq("key1")))
            .thenReturn("value1");
        
        handler.channelRead(mockCtx, buffer);
        
        verify(mockDatabaseManager).executeQuery(anyString(), eq("key1"));
        verify(mockCtx).writeAndFlush(any(ByteBuf.class));
    }
    
    @Test
    void testHandleGetCommandNotFound() throws Exception {
        String command = "GET nonexistent";
        ByteBuf buffer = Unpooled.copiedBuffer(command, CharsetUtil.UTF_8);
        
        when(mockDatabaseManager.executeQuery(anyString(), eq("nonexistent")))
            .thenReturn(null);
        
        handler.channelRead(mockCtx, buffer);
        
        verify(mockDatabaseManager).executeQuery(anyString(), eq("nonexistent"));
        verify(mockCtx).writeAndFlush(any(ByteBuf.class));
    }
    
    @Test
    void testHandleHSetCommand() throws Exception {
        String command = "HSET hash1 field1 value1";
        ByteBuf buffer = Unpooled.copiedBuffer(command, CharsetUtil.UTF_8);
        
        when(mockDatabaseManager.executeUpdate(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(1);
        
        handler.channelRead(mockCtx, buffer);
        
        verify(mockDatabaseManager).executeUpdate(anyString(), eq("hash1"), eq("field1"), eq("value1"));
        verify(mockCtx).writeAndFlush(any(ByteBuf.class));
    }
    
    @Test
    void testHandleHGetAllCommand() throws Exception {
        String command = "HGETALL hash1";
        ByteBuf buffer = Unpooled.copiedBuffer(command, CharsetUtil.UTF_8);
        
        List<String[]> mockResults = Arrays.asList(
            new String[]{"field1", "value1"},
            new String[]{"field2", "value2"}
        );
        
        when(mockDatabaseManager.executeQueryMultiple(anyString(), eq("hash1")))
            .thenReturn(mockResults);
        
        handler.channelRead(mockCtx, buffer);
        
        verify(mockDatabaseManager).executeQueryMultiple(anyString(), eq("hash1"));
        verify(mockCtx).writeAndFlush(any(ByteBuf.class));
    }
    
    @Test
    void testHandleErrorCommand() throws Exception {
        String command = "SET key1 value1";
        ByteBuf buffer = Unpooled.copiedBuffer(command, CharsetUtil.UTF_8);
        
        when(mockDatabaseManager.executeUpdate(anyString(), anyString(), anyString()))
            .thenThrow(new SQLException("Database error"));
        
        handler.channelRead(mockCtx, buffer);
        
        verify(mockCtx).writeAndFlush(any(ByteBuf.class));
    }
}