package com.sqladaptor.parser;

import com.sqladaptor.ast.RedisCommandNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

public class RedisCommandParserTest {
    
    private RedisCommandParser parser;
    
    @BeforeEach
    void setUp() {
        parser = new RedisCommandParser();
    }
    
    @AfterEach
    void tearDown() {
        parser = null; // 显式清理引用
    }
    
    @Test
    void testParseSimpleSetCommand() {
        String input = "SET key1 value1";
        RedisCommandNode node = parser.parse(input);
        
        assertEquals("SET", node.getCommand());
        assertEquals(2, node.getArguments().size());
        assertEquals("key1", node.getArguments().get(0));
        assertEquals("value1", node.getArguments().get(1));
    }
    
    @Test
    void testParseSimpleGetCommand() {
        String input = "GET key1";
        RedisCommandNode node = parser.parse(input);
        
        assertEquals("GET", node.getCommand());
        assertEquals(1, node.getArguments().size());
        assertEquals("key1", node.getArguments().get(0));
    }
    
    @Test
    void testParseRespArrayCommand() {
        String input = "*3\r\n$3\r\nSET\r\n$4\r\nkey1\r\n$6\r\nvalue1\r\n";
        RedisCommandNode node = parser.parse(input);
        
        assertEquals("SET", node.getCommand());
        assertEquals(2, node.getArguments().size());
        assertEquals("key1", node.getArguments().get(0));
        assertEquals("value1", node.getArguments().get(1));
    }
    
    @Test
    void testParseHashCommand() {
        String input = "HSET hash1 field1 value1";
        RedisCommandNode node = parser.parse(input);
        
        assertEquals("HSET", node.getCommand());
        assertEquals(3, node.getArguments().size());
        assertEquals("hash1", node.getArguments().get(0));
        assertEquals("field1", node.getArguments().get(1));
        assertEquals("value1", node.getArguments().get(2));
    }
    
    @Test
    void testParsePingCommand() {
        String input = "PING";
        RedisCommandNode node = parser.parse(input);
        
        assertEquals("PING", node.getCommand());
        assertTrue(node.getArguments().isEmpty());
    }
    
    @Test
    void testParseEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse("");
        });
    }
    
    @Test
    void testParseNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse(null);
        });
    }
    
    @Test
    void testParseCommandWithQuotes() {
        String input = "SET \"key with spaces\" \"value with spaces\"";
        RedisCommandNode node = parser.parse(input);
        
        assertEquals("SET", node.getCommand());
        assertEquals(2, node.getArguments().size());
    }
}