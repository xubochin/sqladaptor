package com.sqladaptor.parser;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.grammar.RedisLexer;
import com.sqladaptor.grammar.RedisParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RedisCommandParser {
    private final RedisCommandVisitor visitor;
    
    public RedisCommandParser() {
        this.visitor = new RedisCommandVisitor();
    }
    
    public RedisCommandNode parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        
        input = input.trim();
        
        // 处理RESP数组格式 (例如: *3\r\n$3\r\nSET\r\n$3\r\nkey\r\n$5\r\nvalue\r\n)
        if (input.startsWith("*")) {
            return parseRespArray(input);
        }
        
        // 处理简单字符串格式 (例如: "SET key value")
        return parseSimpleString(input);
    }
    
    private RedisCommandNode parseRespArray(String input) {
        String[] lines = input.split("\\r\\n");
        if (lines.length < 1) {
            throw new IllegalArgumentException("Invalid RESP array format");
        }
        
        int arrayLength;
        try {
            arrayLength = Integer.parseInt(lines[0].substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid array length in RESP format");
        }
        
        List<String> elements = new ArrayList<>();
        int lineIndex = 1;
        
        for (int i = 0; i < arrayLength && lineIndex < lines.length; i++) {
            if (lineIndex >= lines.length || !lines[lineIndex].startsWith("$")) {
                throw new IllegalArgumentException("Invalid RESP bulk string format");
            }
            
            int stringLength;
            try {
                stringLength = Integer.parseInt(lines[lineIndex].substring(1));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid string length in RESP format");
            }
            
            lineIndex++;
            if (lineIndex >= lines.length) {
                throw new IllegalArgumentException("Missing bulk string data");
            }
            
            String element = lines[lineIndex];
            elements.add(element);
            lineIndex++;
        }
        
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("Empty command array");
        }
        
        String command = elements.get(0).toUpperCase();  // 确保命令大写
        List<String> arguments = elements.subList(1, elements.size());
        
        return new RedisCommandNode(command, arguments);
    }
    
    private RedisCommandNode parseSimpleString(String input) {
        try {
            // 预处理：将命令部分转换为大写
            String[] parts = input.trim().split("\\s+", 2);
            if (parts.length > 0) {
                String command = parts[0].toUpperCase();  // 确保大写转换
                String rest = parts.length > 1 ? parts[1] : "";
                input = command + (rest.isEmpty() ? "" : " " + rest);
            }
            
            // 使用ANTLR4解析器读取内容
            CharStream charStream = CharStreams.fromString(input);
            RedisLexer lexer = new RedisLexer(charStream);
            // 移除默认错误监听器以避免控制台输出
            lexer.removeErrorListeners();
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            RedisParser parser = new RedisParser(tokens);
            parser.removeErrorListeners();
            
            // 解析命令
            RedisParser.CommandContext ctx = parser.command();
            if (ctx != null && ctx.exception == null) {
                return visitor.visit(ctx);
            }
        } catch (Exception e) {
            // 静默处理ANTLR4解析失败，回退到简单解析
        }
        
        // 如果ANTLR4解析失败，回退到简单解析
        String[] parts = input.split("\\s+");
        if (parts.length == 0) {
            throw new IllegalArgumentException("Empty command");
        }
        
        String command = parts[0].toUpperCase();  // 确保命令大写
        List<String> arguments = new ArrayList<>();
        if (parts.length > 1) {
            arguments.addAll(Arrays.asList(parts).subList(1, parts.length));
        }
        
        return new RedisCommandNode(command, arguments);
    }
}