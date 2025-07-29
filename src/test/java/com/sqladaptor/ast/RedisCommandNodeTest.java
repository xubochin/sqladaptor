package com.sqladaptor.ast;

import com.sqladaptor.ast.RedisCommandNode.CommandType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class RedisCommandNodeTest {
    
    private RedisCommandNode commandNode;
    
    @BeforeEach
    void setUp() {
        commandNode = new RedisCommandNode("SET");
    }
    
    @Test
    void testConstructorWithCommand() {
        assertEquals("SET", commandNode.getCommand());
        assertEquals(CommandType.STRING, commandNode.getType());
        assertTrue(commandNode.getArguments().isEmpty());
    }
    
    @Test
    void testConstructorWithCommandAndArguments() {
        List<String> args = Arrays.asList("key1", "value1");
        RedisCommandNode node = new RedisCommandNode("SET", args);
        
        assertEquals("SET", node.getCommand());
        assertEquals(args, node.getArguments());
        assertEquals(CommandType.STRING, node.getType());
    }
    
    @Test
    void testDetermineCommandTypeString() {
        RedisCommandNode setNode = new RedisCommandNode("SET");
        assertEquals(CommandType.STRING, setNode.getType());
        
        RedisCommandNode getNode = new RedisCommandNode("GET");
        assertEquals(CommandType.STRING, getNode.getType());
        
        // DEL 应该是 KEY 类型，不是 STRING 类型
        RedisCommandNode delNode = new RedisCommandNode("DEL");
        assertEquals(CommandType.KEY, delNode.getType());
    }
    
    @Test
    void testDetermineCommandTypeKey() {
        RedisCommandNode delNode = new RedisCommandNode("DEL");
        assertEquals(CommandType.KEY, delNode.getType());
        
        RedisCommandNode existsNode = new RedisCommandNode("EXISTS");
        assertEquals(CommandType.KEY, existsNode.getType());
        
        RedisCommandNode expireNode = new RedisCommandNode("EXPIRE");
        assertEquals(CommandType.KEY, expireNode.getType());
    }
    
    @Test
    void testDetermineCommandTypeHash() {
        RedisCommandNode hsetNode = new RedisCommandNode("HSET");
        assertEquals(CommandType.HASH, hsetNode.getType());
        
        RedisCommandNode hgetNode = new RedisCommandNode("HGET");
        assertEquals(CommandType.HASH, hgetNode.getType());
    }
    
    @Test
    void testDetermineCommandTypeConnection() {
        RedisCommandNode pingNode = new RedisCommandNode("PING");
        assertEquals(CommandType.CONNECTION, pingNode.getType());
        
        RedisCommandNode echoNode = new RedisCommandNode("ECHO");
        assertEquals(CommandType.CONNECTION, echoNode.getType());
    }
    
    @Test
    void testDetermineCommandTypeUnknown() {
        RedisCommandNode unknownNode = new RedisCommandNode("UNKNOWN");
        assertEquals(CommandType.UNKNOWN, unknownNode.getType());
    }
    
    @Test
    void testCaseInsensitiveCommand() {
        RedisCommandNode lowerNode = new RedisCommandNode("set");
        assertEquals(CommandType.STRING, lowerNode.getType());
        
        RedisCommandNode mixedNode = new RedisCommandNode("HsEt");
        assertEquals(CommandType.HASH, mixedNode.getType());
    }
}