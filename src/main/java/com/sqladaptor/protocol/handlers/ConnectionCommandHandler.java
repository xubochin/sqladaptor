package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * CONNECTION 命令处理器
 * 处理连接相关的 Redis 命令，如 PING、ECHO、AUTH、CLIENT、SELECT、QUIT 等
 */
public class ConnectionCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionCommandHandler.class);
    
    private static final AtomicInteger activeConnections = new AtomicInteger(0);
    private static final AtomicLong totalConnections = new AtomicLong(0);

    public ConnectionCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }

    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        return handleCommand(null, commandNode);
    }

    /**
     * 处理 CONNECTION 类型命令的重载方法，支持 ChannelHandlerContext
     */
    public String handleCommand(ChannelHandlerContext ctx, RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();
        
        logger.info("[CONNECTION] Processing command: {} with {} args: {}", command, args.size(), args);
        
        switch (command) {
            case "PING":
                return handlePing(args);
            case "ECHO":
                return handleEcho(args);
            case "AUTH":
                return handleAuth(args);
            case "COMMAND":
                return handleCommandInfo(args);
            case "CLIENT":
                return handleClient(args);
            case "SELECT":
                return handleSelect(args);
            case "QUIT":
                return handleQuit(ctx);
            default:
                logger.warn("[CONNECTION] Unknown connection command: {}", command);
                return "-ERR Unknown connection command '" + command + "'\r\n";
        }
    }

    /**
     * 处理 PING 命令
     */
    private String handlePing(List<String> args) {
        logger.info("[PING] Processing PING command, args size: {}", args.size());

        if (args.isEmpty()) {
            logger.info("[PING] Returning PONG for simple PING");
            return "+PONG\r\n";
        } else {
            String message = args.get(0);
            logger.info("[PING] Returning message '{}' for PING with argument", message);
            return "$" + message.length() + "\r\n" + message + "\r\n";
        }
    }

    /**
     * 处理 ECHO 命令
     */
    private String handleEcho(List<String> args) {
        logger.info("[ECHO] Processing ECHO command with {} args", args.size());
        
        if (!args.isEmpty()) {
            String message = args.get(0);
            logger.info("[ECHO] Echoing message: {}", message);
            return "$" + message.length() + "\r\n" + message + "\r\n";
        } else {
            logger.warn("[ECHO] No arguments provided for ECHO command");
            return "-ERR wrong number of arguments for 'echo' command\r\n";
        }
    }

    /**
     * 处理 AUTH 命令
     */
    private String handleAuth(List<String> args) {
        logger.info("[AUTH] Processing AUTH command with {} args", args.size());
        
        // 简单实现，接受任何密码
        if (!args.isEmpty()) {
            logger.info("[AUTH] Authentication successful for password: {}", args.get(0));
        } else {
            logger.info("[AUTH] Authentication without password");
        }
        
        return "+OK\r\n";
    }

    /**
     * 处理 COMMAND 命令
     */
    private String handleCommandInfo(List<String> args) {
        logger.info("[COMMAND] Processing COMMAND with {} args", args.size());
        
        if (args.isEmpty()) {
            // 返回支持的命令列表（简化版）
            return "*7\r\n" +
                   "$4\r\nPING\r\n" +
                   "$4\r\nECHO\r\n" +
                   "$4\r\nAUTH\r\n" +
                   "$6\r\nCLIENT\r\n" +
                   "$6\r\nSELECT\r\n" +
                   "$4\r\nQUIT\r\n" +
                   "$7\r\nCOMMAND\r\n";
        } else {
            String subCommand = args.get(0).toUpperCase();
            switch (subCommand) {
                case "COUNT":
                    return ":7\r\n"; // 返回支持的连接命令数量
                case "INFO":
                    if (args.size() > 1) {
                        String commandName = args.get(1).toUpperCase();
                        return getCommandInfo(commandName);
                    }
                    return "-ERR wrong number of arguments for 'command info' command\r\n";
                default:
                    return "-ERR Unknown COMMAND subcommand '" + subCommand + "'\r\n";
            }
        }
    }

    /**
     * 处理 CLIENT 命令
     */
    private String handleClient(List<String> args) {
        logger.debug("[CLIENT] Processing CLIENT command with {} args: {}", args.size(), args);
        
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'client' command\r\n";
        }
        
        String subCommand = args.get(0).toUpperCase();
        switch (subCommand) {
            case "LIST":
                return handleClientList();
            case "GETNAME":
                return handleClientGetName();
            case "SETNAME":
                return handleClientSetName(args);
            case "SETINFO":
                return handleClientSetInfo(args);
            case "PAUSE":
                return handleClientPause(args);
            case "KILL":
                return handleClientKill(args);
            case "ID":
                return handleClientId();
            default:
                logger.warn("[CLIENT] Unknown CLIENT subcommand: {}", subCommand);
                return "-ERR Unknown CLIENT subcommand '" + subCommand + "'\r\n";
        }
    }

    /**
     * 处理 SELECT 命令
     */
    private String handleSelect(List<String> args) {
        logger.info("[SELECT] Processing SELECT command with {} args", args.size());
        
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'select' command\r\n";
        }
        
        try {
            int dbIndex = Integer.parseInt(args.get(0));
            if (dbIndex < 0 || dbIndex > 15) {
                return "-ERR DB index is out of range\r\n";
            }
            
            logger.info("[SELECT] Switching to database {}", dbIndex);
            // 这里可以实现数据库切换逻辑
            return "+OK\r\n";
        } catch (NumberFormatException e) {
            logger.warn("[SELECT] Invalid database index: {}", args.get(0));
            return "-ERR invalid DB index\r\n";
        }
    }

    /**
     * 处理 QUIT 命令
     */
    private String handleQuit(ChannelHandlerContext ctx) {
        logger.info("[QUIT] Processing QUIT command");
        
        if (ctx != null) {
            // 关闭连接
            ctx.close();
            activeConnections.decrementAndGet();
            logger.info("[QUIT] Connection closed, active connections: {}", activeConnections.get());
        }
        
        return "+OK\r\n";
    }

    // CLIENT 子命令处理方法
    
    private String handleClientList() {
        logger.debug("[CLIENT LIST] Returning client list");
        
        // 简化的客户端列表
        String clientInfo = "id=1 addr=127.0.0.1:12345 fd=8 name= age=0 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=32768 obl=0 oll=0 omem=0 events=r cmd=client";
        return "$" + clientInfo.length() + "\r\n" + clientInfo + "\r\n";
    }
    
    private String handleClientGetName() {
        logger.debug("[CLIENT GETNAME] Returning client name");
        return "$-1\r\n"; // 没有设置名称
    }
    
    private String handleClientSetName(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'client setname' command\r\n";
        }
        
        String clientName = args.get(1);
        logger.info("[CLIENT SETNAME] Setting client name to: {}", clientName);
        return "+OK\r\n";
    }
    
    private String handleClientSetInfo(List<String> args) {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'client setinfo' command\r\n";
        }
        
        String attribute = args.get(1);
        String value = args.get(2);
        logger.info("[CLIENT SETINFO] Setting client info: {} = {}", attribute, value);
        
        // CLIENT SETINFO 命令用于设置客户端信息，通常返回 OK
        return "+OK\r\n";
    }
    
    private String handleClientPause(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'client pause' command\r\n";
        }
        
        try {
            int timeout = Integer.parseInt(args.get(1));
            logger.info("[CLIENT PAUSE] Pausing clients for {} milliseconds", timeout);
            return "+OK\r\n";
        } catch (NumberFormatException e) {
            return "-ERR timeout is not an integer or out of range\r\n";
        }
    }
    
    private String handleClientKill(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'client kill' command\r\n";
        }
        
        String target = args.get(1);
        logger.info("[CLIENT KILL] Killing client: {}", target);
        return ":1\r\n"; // 假设成功杀死1个客户端
    }
    
    private String handleClientId() {
        logger.debug("[CLIENT ID] Returning client ID");
        return ":1\r\n"; // 返回客户端ID
    }
    
    /**
     * 获取命令信息
     */
    private String getCommandInfo(String commandName) {
        switch (commandName) {
            case "PING":
                return "*6\r\n$4\r\nping\r\n:1\r\n*2\r\n+readonly\r\n+fast\r\n:1\r\n:1\r\n:1\r\n";
            case "ECHO":
                return "*6\r\n$4\r\necho\r\n:2\r\n*2\r\n+readonly\r\n+fast\r\n:1\r\n:1\r\n:1\r\n";
            default:
                return "*0\r\n"; // 命令不存在
        }
    }
    
    /**
     * 获取活跃连接数
     */
    public static int getActiveConnections() {
        return activeConnections.get();
    }
    
    /**
     * 获取总连接数
     */
    public static long getTotalConnections() {
        return totalConnections.get();
    }
    
    /**
     * 增加连接计数
     */
    public static void incrementConnections() {
        activeConnections.incrementAndGet();
        totalConnections.incrementAndGet();
    }
    
    /**
     * 减少连接计数
     */
    public static void decrementConnections() {
        activeConnections.decrementAndGet();
    }
}
