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
        String command = ctx.commandName().getText();
        List<String> arguments = new ArrayList<>();
        
        if (ctx.argument() != null) {
            for (RedisParser.ArgumentContext argCtx : ctx.argument()) {
                String arg = extractArgumentValue(argCtx);
                if (arg != null) {
                    arguments.add(arg);
                }
            }
        }
        
        return new RedisCommandNode(command, arguments);
    }
    
    private String extractArgumentValue(RedisParser.ArgumentContext ctx) {
        if (ctx.stringLiteral() != null) {
            return extractStringLiteral(ctx.stringLiteral());
        } else if (ctx.numberLiteral() != null) {
            return ctx.numberLiteral().getText();
        } else if (ctx.identifier() != null) {
            return ctx.identifier().getText();
        } else if (ctx.commandKeyword() != null) {
            return ctx.commandKeyword().getText();
        }
        return null;
    }
    
    private String extractStringLiteral(RedisParser.StringLiteralContext ctx) {
        if (ctx.QUOTED_STRING() != null) {
            String quoted = ctx.QUOTED_STRING().getText();
            // 移除引号
            return quoted.substring(1, quoted.length() - 1);
        } else if (ctx.UNQUOTED_STRING() != null) {
            return ctx.UNQUOTED_STRING().getText();
        }
        return null;
    }
}