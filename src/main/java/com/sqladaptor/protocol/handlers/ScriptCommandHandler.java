package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 处理 Redis 脚本命令的处理器
 * 支持的命令：SCRIPT (LOAD, EXISTS, FLUSH, KILL), EVAL, EVALSHA
 */
public class ScriptCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(ScriptCommandHandler.class);
    
    // 脚本缓存管理
    private final Map<String, String> loadedScripts = new HashMap<>();
    
    public ScriptCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }
    
    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();
        
        logger.info("[SCRIPT] Processing command: {} with {} args: {}", command, args.size(), args);
        
        switch (command) {
            case "SCRIPT":
                return handleScriptManagement(args);
            case "EVAL":
                return handleEval(args);
            case "EVALSHA":
                return handleEvalSha(args);
            default:
                return "-ERR Unknown Script command '" + command + "'\r\n";
        }
    }
    
    /**
     * 处理 SCRIPT 命令及其子命令
     */
    private String handleScriptManagement(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'script' command\r\n";
        }
        
        String subCommand = args.get(0).toUpperCase();
        switch (subCommand) {
            case "LOAD":
                return handleScriptLoad(args.subList(1, args.size()));
            case "EXISTS":
                return handleScriptExists(args.subList(1, args.size()));
            case "FLUSH":
                return handleScriptFlush(args.subList(1, args.size()));
            case "KILL":
                return handleScriptKill(args.subList(1, args.size()));
            default:
                return "-ERR Unknown SCRIPT subcommand '" + subCommand + "'\r\n";
        }
    }
    
    /**
     * 处理 SCRIPT LOAD 命令 - 加载脚本到缓存
     */
    private String handleScriptLoad(List<String> args) {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'script load' command\r\n";
        }
        
        String script = args.get(0);
        String sha1 = generateSHA1(script);
        loadedScripts.put(sha1, script);
        
        logger.debug("[SCRIPT] Loaded script with SHA1: {}", sha1);
        
        return "$" + sha1.length() + "\r\n" + sha1 + "\r\n";
    }
    
    /**
     * 处理 SCRIPT EXISTS 命令 - 检查脚本是否存在
     */
    private String handleScriptExists(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'script exists' command\r\n";
        }
        
        StringBuilder response = new StringBuilder();
        response.append("*").append(args.size()).append("\r\n");
        
        for (String sha1 : args) {
            boolean exists = loadedScripts.containsKey(sha1);
            response.append(":").append(exists ? "1" : "0").append("\r\n");
        }
        
        logger.debug("[SCRIPT] Checked existence of {} scripts", args.size());
        
        return response.toString();
    }
    
    /**
     * 处理 SCRIPT FLUSH 命令 - 清空脚本缓存
     */
    private String handleScriptFlush(List<String> args) {
        if (args.size() > 1) {
            return "-ERR wrong number of arguments for 'script flush' command\r\n";
        }
        
        // Redis 6.2+ 支持 ASYNC 和 SYNC 选项
        String mode = "SYNC"; // 默认同步模式
        if (args.size() == 1) {
            mode = args.get(0).toUpperCase();
            if (!"ASYNC".equals(mode) && !"SYNC".equals(mode)) {
                return "-ERR Syntax error in SCRIPT FLUSH\r\n";
            }
        }
        
        int clearedCount = loadedScripts.size();
        loadedScripts.clear();
        
        logger.debug("[SCRIPT] Flushed {} scripts from cache (mode: {})", clearedCount, mode);
        
        return "+OK\r\n";
    }
    
    /**
     * 处理 SCRIPT KILL 命令 - 终止正在运行的脚本
     */
    private String handleScriptKill(List<String> args) {
        if (args.size() != 0) {
            return "-ERR wrong number of arguments for 'script kill' command\r\n";
        }
        
        // 模拟没有正在运行的脚本
        logger.debug("[SCRIPT] Attempted to kill running script");
        
        return "-NOTBUSY No scripts in execution right now.\r\n";
    }
    
    /**
     * 处理 EVAL 命令 - 执行 Lua 脚本
     */
    private String handleEval(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'eval' command\r\n";
        }
        
        String script = args.get(0);
        String numKeysStr = args.get(1);
        
        try {
            int numKeys = Integer.parseInt(numKeysStr);
            if (numKeys < 0) {
                return "-ERR Number of keys can't be negative\r\n";
            }
            
            if (args.size() < 2 + numKeys) {
                return "-ERR Number of keys can't be greater than number of args\r\n";
            }
            
            // 提取键和参数
            List<String> keys = args.subList(2, 2 + numKeys);
            List<String> argv = args.subList(2 + numKeys, args.size());
            
            logger.debug("[SCRIPT] Executing script with {} keys: {}, {} args: {}", 
                        numKeys, keys, argv.size(), argv);
            
            // 模拟脚本执行错误（因为不支持 Lua 脚本执行）
            return "-ERR Error running script (call to f_xxx): @user_script:1: Script execution not supported in JDBC adapter\r\n";
            
        } catch (NumberFormatException e) {
            return "-ERR value is not an integer or out of range\r\n";
        }
    }
    
    /**
     * 处理 EVALSHA 命令 - 通过 SHA1 执行脚本
     */
    private String handleEvalSha(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'evalsha' command\r\n";
        }
        
        String sha1 = args.get(0);
        String numKeysStr = args.get(1);
        
        // 检查脚本是否存在
        if (!loadedScripts.containsKey(sha1)) {
            return "-NOSCRIPT No matching script. Please use EVAL.\r\n";
        }
        
        try {
            int numKeys = Integer.parseInt(numKeysStr);
            if (numKeys < 0) {
                return "-ERR Number of keys can't be negative\r\n";
            }
            
            if (args.size() < 2 + numKeys) {
                return "-ERR Number of keys can't be greater than number of args\r\n";
            }
            
            // 提取键和参数
            List<String> keys = args.subList(2, 2 + numKeys);
            List<String> argv = args.subList(2 + numKeys, args.size());
            
            logger.debug("[SCRIPT] Executing script SHA1: {} with {} keys: {}, {} args: {}", 
                        sha1, numKeys, keys, argv.size(), argv);
            
            // 模拟脚本执行错误（因为不支持 Lua 脚本执行）
            return "-ERR Error running script (call to f_" + sha1.substring(0, 8) + 
                   "): @user_script:1: Script execution not supported in JDBC adapter\r\n";
            
        } catch (NumberFormatException e) {
            return "-ERR value is not an integer or out of range\r\n";
        }
    }
    
    /**
     * 生成脚本的 SHA1 哈希值
     */
    private String generateSHA1(String script) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(script.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("[SCRIPT] SHA-1 algorithm not available", e);
            // 返回一个模拟的 SHA1 值
            return "da39a3ee5e6b4b0d3255bfef95601890afd80709";
        }
    }
    
    /**
     * 获取已加载的脚本数量
     */
    public int getLoadedScriptCount() {
        return loadedScripts.size();
    }
    
    /**
     * 检查脚本是否已加载
     */
    public boolean isScriptLoaded(String sha1) {
        return loadedScripts.containsKey(sha1);
    }
}