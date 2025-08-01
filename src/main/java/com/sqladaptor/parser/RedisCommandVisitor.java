package com.sqladaptor.parser;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.grammar.RedisBaseVisitor;
import com.sqladaptor.grammar.RedisParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class RedisCommandVisitor extends RedisBaseVisitor<RedisCommandNode> {
    
    @Override
    public RedisCommandNode visitCommand(RedisParser.CommandContext ctx) {
        System.out.println("[DEBUG] 访问者开始处理命令上下文");
        
        if (ctx.commandName() == null) {
            System.out.println("[ERROR] 命令名称上下文为null");
            return null;
        }
        
        String command = ctx.commandName().getText();
        System.out.println("[DEBUG] 提取的命令名称: \"" + command + "\"");
        
        List<String> arguments = new ArrayList<>();
        
        if (ctx.argument() != null) {
            System.out.println("[DEBUG] 找到 " + ctx.argument().size() + " 个参数上下文");
            for (int i = 0; i < ctx.argument().size(); i++) {
                RedisParser.ArgumentContext argCtx = ctx.argument().get(i);
                System.out.println("[DEBUG] 处理第 " + (i+1) + " 个参数");
                String arg = extractArgumentValue(argCtx);
                if (arg != null) {
                    System.out.println("[DEBUG] 提取的参数值: \"" + arg + "\"");
                    arguments.add(arg);
                } else {
                    System.out.println("[WARNING] 第 " + (i+1) + " 个参数提取失败");
                }
            }
        } else {
            System.out.println("[DEBUG] 没有找到参数上下文");
        }
        
        System.out.println("[DEBUG] 访问者处理完成 - 命令: \"" + command + "\", 参数数量: " + arguments.size());
        return new RedisCommandNode(command, arguments);
    }
    
    private String extractArgumentValue(RedisParser.ArgumentContext ctx) {
        System.out.println("[DEBUG] 开始提取参数值");
        
        if (ctx.stringLiteral() != null) {
            System.out.println("[DEBUG] 参数类型: 字符串字面量");
            return extractStringLiteral(ctx.stringLiteral());
        } else if (ctx.numberLiteral() != null) {
            System.out.println("[DEBUG] 参数类型: 数字字面量");
            String value = ctx.numberLiteral().getText();
            System.out.println("[DEBUG] 数字值: \"" + value + "\"");
            return value;
        } else if (ctx.identifier() != null) {
            System.out.println("[DEBUG] 参数类型: 标识符");
            String value = ctx.identifier().getText();
            System.out.println("[DEBUG] 标识符值: \"" + value + "\"");
            return value;
        } else if (ctx.commandKeyword() != null) {
            System.out.println("[DEBUG] 参数类型: 命令关键字");
            String value = ctx.commandKeyword().getText();
            System.out.println("[DEBUG] 关键字值: \"" + value + "\"");
            return value;
        }
        
        System.out.println("[WARNING] 无法识别的参数类型");
        return null;
    }
    
    // 添加缺失的extractStringLiteral方法
    private String extractStringLiteral(RedisParser.StringLiteralContext ctx) {
        System.out.println("[DEBUG] 开始提取字符串字面量");
        
        if (ctx.QUOTED_STRING() != null) {
            String quoted = ctx.QUOTED_STRING().getText();
            System.out.println("[DEBUG] 带引号的字符串: \"" + quoted + "\"");
            // 移除引号
            String unquoted = quoted.substring(1, quoted.length() - 1);
            System.out.println("[DEBUG] 去除引号后: \"" + unquoted + "\"");
            return unquoted;
        } else if (ctx.UNQUOTED_STRING() != null) {
            String unquoted = ctx.UNQUOTED_STRING().getText();
            System.out.println("[DEBUG] 无引号字符串: \"" + unquoted + "\"");
            return unquoted;
        }
        
        System.out.println("[WARNING] 字符串字面量提取失败");
        return null;
    }
}