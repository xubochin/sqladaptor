package com.sqladaptor.ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class RedisCommandNode {
    private String command;
    private List<String> arguments;
    private CommandType type;
    
    // 静态命令类型映射表，提高查找效率
    private static final Map<String, CommandType> COMMAND_TYPE_MAP = new HashMap<>();
    
    static {
        // String commands
        COMMAND_TYPE_MAP.put("GET", CommandType.STRING);
        COMMAND_TYPE_MAP.put("SET", CommandType.STRING);
        COMMAND_TYPE_MAP.put("APPEND", CommandType.STRING);
        COMMAND_TYPE_MAP.put("STRLEN", CommandType.STRING);
        COMMAND_TYPE_MAP.put("INCR", CommandType.STRING);
        COMMAND_TYPE_MAP.put("DECR", CommandType.STRING);
        COMMAND_TYPE_MAP.put("INCRBY", CommandType.STRING);
        COMMAND_TYPE_MAP.put("DECRBY", CommandType.STRING);
        COMMAND_TYPE_MAP.put("MGET", CommandType.STRING);
        COMMAND_TYPE_MAP.put("MSET", CommandType.STRING);
        COMMAND_TYPE_MAP.put("GETRANGE", CommandType.STRING);
        COMMAND_TYPE_MAP.put("SETRANGE", CommandType.STRING);
        
        // Hash commands
        COMMAND_TYPE_MAP.put("HGET", CommandType.HASH);
        COMMAND_TYPE_MAP.put("HSET", CommandType.HASH);
        COMMAND_TYPE_MAP.put("HDEL", CommandType.HASH);
        COMMAND_TYPE_MAP.put("HGETALL", CommandType.HASH);
        COMMAND_TYPE_MAP.put("HKEYS", CommandType.HASH);
        COMMAND_TYPE_MAP.put("HVALS", CommandType.HASH);
        
        // List commands
        COMMAND_TYPE_MAP.put("LPUSH", CommandType.LIST);
        COMMAND_TYPE_MAP.put("RPUSH", CommandType.LIST);
        COMMAND_TYPE_MAP.put("LPOP", CommandType.LIST);
        COMMAND_TYPE_MAP.put("RPOP", CommandType.LIST);
        COMMAND_TYPE_MAP.put("LLEN", CommandType.LIST);
        COMMAND_TYPE_MAP.put("LRANGE", CommandType.LIST);
        
        // Set commands
        COMMAND_TYPE_MAP.put("SADD", CommandType.SET);
        COMMAND_TYPE_MAP.put("SREM", CommandType.SET);
        COMMAND_TYPE_MAP.put("SMEMBERS", CommandType.SET);
        COMMAND_TYPE_MAP.put("SCARD", CommandType.SET);
        COMMAND_TYPE_MAP.put("SISMEMBER", CommandType.SET);
        
        // Sorted Set commands
        COMMAND_TYPE_MAP.put("ZADD", CommandType.SORTED_SET);
        COMMAND_TYPE_MAP.put("ZREM", CommandType.SORTED_SET);
        COMMAND_TYPE_MAP.put("ZRANGE", CommandType.SORTED_SET);
        COMMAND_TYPE_MAP.put("ZCARD", CommandType.SORTED_SET);
        COMMAND_TYPE_MAP.put("ZSCORE", CommandType.SORTED_SET);
        
        // Key commands
        COMMAND_TYPE_MAP.put("DEL", CommandType.KEY);
        COMMAND_TYPE_MAP.put("EXISTS", CommandType.KEY);
        COMMAND_TYPE_MAP.put("EXPIRE", CommandType.KEY);
        COMMAND_TYPE_MAP.put("TTL", CommandType.KEY);
        COMMAND_TYPE_MAP.put("PERSIST", CommandType.KEY);
        COMMAND_TYPE_MAP.put("TYPE", CommandType.KEY);
        COMMAND_TYPE_MAP.put("PEXPIRE", CommandType.KEY);
        COMMAND_TYPE_MAP.put("PTTL", CommandType.KEY);
        
        // Connection commands
        COMMAND_TYPE_MAP.put("PING", CommandType.CONNECTION);
        COMMAND_TYPE_MAP.put("ECHO", CommandType.CONNECTION);
        COMMAND_TYPE_MAP.put("CLIENT", CommandType.CONNECTION);
        COMMAND_TYPE_MAP.put("SELECT", CommandType.CONNECTION);  // 修复：从SERVER移到CONNECTION
        
        // Server commands
        COMMAND_TYPE_MAP.put("FLUSHDB", CommandType.SERVER);
        COMMAND_TYPE_MAP.put("FLUSHALL", CommandType.SERVER);
        COMMAND_TYPE_MAP.put("INFO", CommandType.SERVER);
        COMMAND_TYPE_MAP.put("CONFIG", CommandType.SERVER);
        COMMAND_TYPE_MAP.put("DBSIZE", CommandType.SERVER);
        COMMAND_TYPE_MAP.put("KEYS", CommandType.SERVER);
        // 移除SELECT，因为已移到CONNECTION类型
    }
    
    public RedisCommandNode(String command) {
        this.command = command;
        this.arguments = new ArrayList<>();
        this.type = determineCommandType(command);
    }
    
    public RedisCommandNode(String command, List<String> arguments) {
        this.command = command;
        this.arguments = arguments != null ? new ArrayList<>(arguments) : new ArrayList<>();
        this.type = determineCommandType(command);
    }
    
    // 将private改为public static，使测试代码可以访问
    public static CommandType determineCommandType(String command) {
        if (command == null) {
            return CommandType.UNKNOWN;
        }
        return COMMAND_TYPE_MAP.getOrDefault(command.toUpperCase(), CommandType.UNKNOWN);
    }
    
    public String getCommand() {
        return command;
    }
    
    public List<String> getArguments() {
        return arguments;
    }
    
    public void addArgument(String argument) {
        this.arguments.add(argument);
    }
    
    public CommandType getType() {
        return type;
    }
    
    public enum CommandType {
        STRING,
        HASH,
        LIST,
        SET,
        SORTED_SET,
        KEY,
        CONNECTION,
        SERVER,
        UNKNOWN
    }
    
    @Override
    public String toString() {
        return "RedisCommandNode{" +
                "command='" + command + '\'' +
                ", arguments=" + arguments +
                ", type=" + type +
                '}';
    }
}