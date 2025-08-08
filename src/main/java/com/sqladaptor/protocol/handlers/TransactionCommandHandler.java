package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * 处理 Redis 事务命令的处理器
 * 支持的命令：MULTI, EXEC, DISCARD, WATCH, UNWATCH
 */
public class TransactionCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(TransactionCommandHandler.class);
    
    // 事务状态管理
    private boolean inTransaction = false;
    private List<RedisCommandNode> queuedCommands = new ArrayList<>();
    private Set<String> watchedKeys = new HashSet<>();
    private Map<String, String> keyVersions = new HashMap<>();
    
    public TransactionCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }
    
    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();
        
        logger.info("[TRANSACTION] Processing command: {} with {} args: {}", command, args.size(), args);
        
        switch (command) {
            case "MULTI":
                return handleMulti(args);
            case "EXEC":
                return handleExec(args);
            case "DISCARD":
                return handleDiscard(args);
            case "WATCH":
                return handleWatch(args);
            case "UNWATCH":
                return handleUnwatch(args);
            default:
                return "-ERR Unsupported transaction command: " + command + "\r\n";
        }
    }
    
    /**
     * 处理 MULTI 命令 - 开始事务
     */
    private String handleMulti(List<String> args) {
        if (args.size() != 0) {
            return "-ERR wrong number of arguments for 'multi' command\r\n";
        }
        
        if (inTransaction) {
            return "-ERR MULTI calls can not be nested\r\n";
        }
        
        inTransaction = true;
        queuedCommands.clear();
        logger.debug("[TRANSACTION] Started new transaction");
        
        return "+OK\r\n";
    }
    
    /**
     * 处理 EXEC 命令 - 执行事务
     */
    private String handleExec(List<String> args) {
        if (args.size() != 0) {
            return "-ERR wrong number of arguments for 'exec' command\r\n";
        }
        
        if (!inTransaction) {
            return "-ERR EXEC without MULTI\r\n";
        }
        
        try {
            // 检查被监视的键是否被修改
            if (isWatchedKeysModified()) {
                // 如果被监视的键被修改，返回空结果
                resetTransaction();
                return "*-1\r\n"; // null bulk reply
            }
            
            // 执行队列中的命令（模拟）
            StringBuilder result = new StringBuilder();
            result.append("*").append(queuedCommands.size()).append("\r\n");
            
            for (RedisCommandNode cmd : queuedCommands) {
                // 这里应该实际执行命令，目前返回模拟结果
                result.append("+OK\r\n");
            }
            
            logger.debug("[TRANSACTION] Executed {} commands in transaction", queuedCommands.size());
            resetTransaction();
            
            return result.toString();
        } catch (Exception e) {
            logger.error("[TRANSACTION] Error executing transaction", e);
            resetTransaction();
            return "-ERR Error executing transaction: " + e.getMessage() + "\r\n";
        }
    }
    
    /**
     * 处理 DISCARD 命令 - 取消事务
     */
    private String handleDiscard(List<String> args) {
        if (args.size() != 0) {
            return "-ERR wrong number of arguments for 'discard' command\r\n";
        }
        
        if (!inTransaction) {
            return "-ERR DISCARD without MULTI\r\n";
        }
        
        resetTransaction();
        logger.debug("[TRANSACTION] Transaction discarded");
        
        return "+OK\r\n";
    }
    
    /**
     * 处理 WATCH 命令 - 监视键
     */
    private String handleWatch(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'watch' command\r\n";
        }
        
        if (inTransaction) {
            return "-ERR WATCH inside MULTI is not allowed\r\n";
        }
        
        for (String key : args) {
            watchedKeys.add(key);
            // 记录当前键的版本（模拟）
            keyVersions.put(key, getCurrentKeyVersion(key));
        }
        
        logger.debug("[TRANSACTION] Watching {} keys: {}", args.size(), args);
        
        return "+OK\r\n";
    }
    
    /**
     * 处理 UNWATCH 命令 - 取消监视所有键
     */
    private String handleUnwatch(List<String> args) {
        if (args.size() != 0) {
            return "-ERR wrong number of arguments for 'unwatch' command\r\n";
        }
        
        watchedKeys.clear();
        keyVersions.clear();
        logger.debug("[TRANSACTION] Unwatched all keys");
        
        return "+OK\r\n";
    }
    
    /**
     * 重置事务状态
     */
    private void resetTransaction() {
        inTransaction = false;
        queuedCommands.clear();
        watchedKeys.clear();
        keyVersions.clear();
    }
    
    /**
     * 检查被监视的键是否被修改
     */
    private boolean isWatchedKeysModified() {
        for (String key : watchedKeys) {
            String currentVersion = getCurrentKeyVersion(key);
            String watchedVersion = keyVersions.get(key);
            if (!currentVersion.equals(watchedVersion)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取键的当前版本（模拟实现）
     */
    private String getCurrentKeyVersion(String key) {
        // 这里应该从数据库获取键的实际版本
        // 目前返回模拟版本
        return "version_1";
    }
    
    /**
     * 添加命令到事务队列（供其他处理器调用）
     */
    public void queueCommand(RedisCommandNode command) {
        if (inTransaction) {
            queuedCommands.add(command);
        }
    }
    
    /**
     * 检查是否在事务中
     */
    public boolean isInTransaction() {
        return inTransaction;
    }
}