package com.sqladaptor.parser;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.grammar.RedisLexer;
import com.sqladaptor.grammar.RedisParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RedisCommandParser {
    private final RedisCommandVisitor visitor;
    
    public RedisCommandParser() {
        this.visitor = new RedisCommandVisitor();
    }
    
    public RedisCommandNode parse(String input) {
        System.out.println("[DEBUG] 开始解析输入: \"" + input + "\"");
        
        if (input == null || input.trim().isEmpty()) {
            System.out.println("[ERROR] 输入为空或null");
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        
        input = input.trim();
        System.out.println("[DEBUG] 处理后的输入: " + input );
        
        // 处理RESP数组格式 (例如: *3\r\n$3\r\nSET\r\n$3\r\nkey\r\n$5\r\nvalue\r\n)
        if (input.startsWith("*")) {
            System.out.println("[DEBUG] 检测到RESP数组格式，使用parseRespArray方法");
            return parseRespArray(input);
        }
        
        // 处理简单字符串格式 (例如: "SET key value")
        System.out.println("[DEBUG] 检测到简单字符串格式，使用parseSimpleString方法");
        return parseSimpleString(input);
    }
    
    private RedisCommandNode parseRespArray(String input) {
        System.out.println("[DEBUG] 开始解析RESP数组格式");
        //String[] lines = input.split("\\r\\n");
        List<String> lines = Arrays.stream(input.split("\\r\\n"))
                            .filter(l -> !l.isEmpty())
                            .collect(Collectors.toList());
        System.out.println("[DEBUG] 分割后的行数: " + lines.size());
        
        if (lines.isEmpty() || !lines.get(0).startsWith("*")) {
            throw new IllegalArgumentException("Invalid RESP array format");
        }
        
        int arrayLength;
        try {
            arrayLength = Integer.parseInt(lines.get(0).substring(1));
            System.out.println("[DEBUG] 数组长度: " + arrayLength);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] 无法解析数组长度: " + lines.get(0));
            throw new IllegalArgumentException("Invalid array length in RESP format");
        }
        
        List<String> elements = new ArrayList<>();
        int lineIndex = 1;
        
        for (int i = 0; i < arrayLength && lineIndex < lines.size(); i++) {
            System.out.println("[DEBUG] 处理第 " + (i+1) + " 个元素，当前行索引: " + lineIndex);
            
            if (lineIndex >= lines.size() || !lines.get(lineIndex).startsWith("$")) {
                System.out.println("[ERROR] 无效的RESP bulk string格式，行: " + (lineIndex < lines.size() ? lines.get(lineIndex) : "超出范围"));
                throw new IllegalArgumentException("Invalid RESP bulk string format");
            }
            
            int stringLength;
            try {
                stringLength = Integer.parseInt(lines.get(lineIndex).substring(1));
                System.out.println("[DEBUG] 字符串长度: " + stringLength);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 无法解析字符串长度: " + lines.get(lineIndex));
                throw new IllegalArgumentException("Invalid string length in RESP format");
            }
            
            lineIndex++;
            if (lineIndex >= lines.size()) {
                System.out.println("[ERROR] 缺少bulk string数据");
                throw new IllegalArgumentException("Missing bulk string data");
            }
            
            String element = lines.get(lineIndex);
            System.out.println("[DEBUG] 提取的元素: \"" + element + "\"");
            elements.add(element);
            lineIndex++;
        }
        
        if (elements.isEmpty()) {
            System.out.println("[ERROR] 命令数组为空");
            throw new IllegalArgumentException("Empty command array");
        }
        
        String command = elements.get(0).toUpperCase();
        List<String> arguments = elements.subList(1, elements.size());
        
        System.out.println("[DEBUG] RESP解析结果 - 命令: \"" + command + "\", 参数数量: " + arguments.size());
        for (int i = 0; i < arguments.size(); i++) {
            System.out.println("[DEBUG] 参数[" + i + "]: \"" + arguments.get(i) + "\"");
        }
        
        return new RedisCommandNode(command, arguments);
    }
    
    private RedisCommandNode parseSimpleString(String input) {
        System.out.println("[DEBUG] 开始解析简单字符串格式");
        
        try {
            // 预处理：将命令部分转换为大写
            String[] parts = input.trim().split("\\s+", 2);
            System.out.println("[DEBUG] 分割后的部分数量: " + parts.length);
            System.out.println("[DEBUG] parts内容: " + Arrays.toString(parts));
            if (parts.length > 0) {
                String command = parts[0].toUpperCase();
                String rest = parts.length > 1 ? parts[1] : "";
                input = command + (rest.isEmpty() ? "" : " " + rest);
                System.out.println("[DEBUG] 预处理后的输入: \"" + input + "\"");
            }
            
            System.out.println("[DEBUG] 尝试使用ANTLR4解析器");
            // 使用ANTLR4解析器读取内容
            CharStream charStream = CharStreams.fromString(input);
            RedisLexer lexer = new RedisLexer(charStream);
            // 移除默认错误监听器以避免控制台输出
            lexer.removeErrorListeners();
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            
            // 添加Token打印功能
            tokens.fill();
            System.out.println("[DEBUG] ========== Token分析结果 ==========");
            System.out.println("[DEBUG] 总Token数量: " + tokens.size());
            for (int i = 0; i < tokens.size(); i++) {
                Token token = tokens.get(i);
                String tokenName = lexer.getVocabulary().getDisplayName(token.getType());
                String tokenText = token.getText();
                int tokenType = token.getType();
                int line = token.getLine();
                int column = token.getCharPositionInLine();
                
                System.out.println(String.format("[DEBUG] Token[%d]: Type=%d(%s), Text='%s', Line=%d, Column=%d", 
                    i, tokenType, tokenName, tokenText, line, column));
            }
            System.out.println("[DEBUG] ====================================");
            
            RedisParser parser = new RedisParser(tokens);
            parser.removeErrorListeners();
            
            System.out.println("[DEBUG] 创建词法分析器和语法分析器完成");
            
            // 解析命令
            RedisParser.CommandContext ctx = parser.command();
            System.out.println("[DEBUG] 解析上下文: " + (ctx != null ? "成功" : "失败"));
            System.out.println("[DEBUG] 解析异常: " + (ctx != null && ctx.exception != null ? ctx.exception.getMessage() : "无"));
            
            // 添加解析树打印
            if (ctx != null) {
                System.out.println("[DEBUG] 解析树结构: " + ctx.toStringTree(parser));
            }
            
            if (ctx != null && ctx.exception == null) {
                System.out.println("[DEBUG] ANTLR4解析成功，使用访问者模式处理");
                RedisCommandNode result = visitor.visit(ctx);
                System.out.println("[DEBUG] 访问者处理结果: " + (result != null ? "成功" : "失败"));
                if (result != null) {
                    System.out.println("[DEBUG] ANTLR4解析结果 - 命令: \"" + result.getCommand() + "\", 参数数量: " + result.getArguments().size());
                    for (int i = 0; i < result.getArguments().size(); i++) {
                        System.out.println("[DEBUG] 参数[" + i + "]: \"" + result.getArguments().get(i) + "\"");
                    }
                }
                return result;
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] ANTLR4解析失败，异常: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace(); // 添加堆栈跟踪以便更好地调试
            // 静默处理ANTLR4解析失败，回退到简单解析
        }
        
        // 如果ANTLR4解析失败，回退到简单解析
        System.out.println("[DEBUG] 回退到简单字符串分割解析");
        String[] parts = input.split("\\s+");
        System.out.println("[DEBUG] 分割后的部分数量: " + parts.length);
        System.out.println("[DEBUG] parts内容: " + Arrays.toString(parts));
        
        if (parts.length == 0) {
            System.out.println("[ERROR] 空命令");
            throw new IllegalArgumentException("Empty command");
        }
        
        String command = parts[0].toUpperCase();
        List<String> arguments = new ArrayList<>();
        if (parts.length > 1) {
            arguments.addAll(Arrays.asList(parts).subList(1, parts.length));
        }
        
        System.out.println("[DEBUG] 简单解析结果 - 命令: \"" + command + "\", 参数数量: " + arguments.size());
        for (int i = 0; i < arguments.size(); i++) {
            System.out.println("[DEBUG] 参数[" + i + "]: \"" + arguments.get(i) + "\"");
        }
        
        return new RedisCommandNode(command, arguments);
    }
}