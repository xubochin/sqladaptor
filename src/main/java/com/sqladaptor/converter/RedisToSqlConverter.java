package com.sqladaptor.converter;

import com.sqladaptor.ast.RedisCommandNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RedisToSqlConverter {
    private static final Logger logger = LoggerFactory.getLogger(RedisToSqlConverter.class);
    
    public String convertToSql(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand();
        List<String> args = commandNode.getArguments();
        
        switch (commandNode.getType()) {
            case STRING:
                return convertStringCommand(command, args);
            case HASH:
                return convertHashCommand(command, args);
            case KEY:
                return convertKeyCommand(command, args);
            case CONNECTION:
                return convertConnectionCommand(command, args);
            default:
                throw new UnsupportedOperationException("Unsupported command type: " + commandNode.getType());
        }
    }
    
    private String convertStringCommand(String command, List<String> args) throws Exception {
        switch (command) {
            case "SET":
                return convertSetToSql(args);
            case "GET":
                return convertGetToSql(args);
            case "INCR":
                return convertIncrToSql(args);
            case "DECR":
                return convertDecrToSql(args);
            default:
                throw new UnsupportedOperationException("Unsupported string command: " + command);
        }
    }
    
    private String convertHashCommand(String command, List<String> args) throws Exception {
        switch (command) {
            case "HSET":
                return convertHSetToSql(args);
            case "HGET":
                return convertHGetToSql(args);
            case "HDEL":
                return convertHDelToSql(args);
            case "HGETALL":
                return convertHGetAllToSql(args);
            case "HKEYS":
                return convertHKeysToSql(args);
            case "HVALS":
                return convertHValsToSql(args);
            case "HEXISTS":
                return convertHExistsToSql(args);
            case "HINCRBY":
                return convertHIncrByToSql(args);
            case "HLEN":
                return convertHLenToSql(args);
            default:
                throw new UnsupportedOperationException("Unsupported hash command: " + command);
        }
    }
    
    private String convertKeyCommand(String command, List<String> args) throws Exception {
        switch (command) {
            case "DEL":
                return convertDelToSql(args);
            case "EXISTS":
                return convertExistsToSql(args);
            case "EXPIRE":
                return convertExpireToSql(args);
            case "TTL":
                return convertTtlToSql(args);
            default:
                throw new UnsupportedOperationException("Unsupported key command: " + command);
        }
    }
    
    private String convertConnectionCommand(String command, List<String> args) {
        return "SELECT 'PONG' as response";
    }
    
    // 字符串命令转换方法
    private String convertSetToSql(List<String> args) throws Exception {
        if (args.size() < 2) {
            throw new IllegalArgumentException("SET command requires at least 2 arguments");
        }
        
        String key = args.get(0);
        String value = args.get(1);
        
        // 处理可选参数（如EX, PX, NX, XX等）
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT OR REPLACE INTO redis_kv (key_name, value_data, updated_at) ");
        sql.append("VALUES (?, ?, CURRENT_TIMESTAMP)");
        
        logger.debug("Converting SET to SQL: key={}, value={}", key, value);
        return sql.toString();
    }
    
    private String convertGetToSql(List<String> args) throws Exception {
        if (args.size() < 1) {
            throw new IllegalArgumentException("GET command requires 1 argument");
        }
        
        String key = args.get(0);
        return "SELECT value_data FROM redis_kv WHERE key_name = ?";
    }
    
    private String convertDelToSql(List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("DEL command requires at least 1 argument");
        }
        
        // 单个键的情况
        if (args.size() == 1) {
            return "DELETE FROM redis_kv WHERE key_name = ?";
        }
        
        // 多个键的情况
        StringBuilder sql = new StringBuilder("DELETE FROM redis_kv WHERE key_name IN (");
        for (int i = 0; i < args.size(); i++) {
            if (i > 0) sql.append(", ");
            sql.append("?");
        }
        sql.append(")");
        
        return sql.toString();
    }
    
    private String convertIncrToSql(List<String> args) throws Exception {
        if (args.size() < 1) {
            throw new IllegalArgumentException("INCR command requires 1 argument");
        }
        
        return "UPDATE redis_kv SET value_data = CAST(COALESCE(value_data, '0') AS INTEGER) + 1, " +
               "updated_at = CURRENT_TIMESTAMP WHERE key_name = ?";
    }
    
    private String convertDecrToSql(List<String> args) throws Exception {
        if (args.size() < 1) {
            throw new IllegalArgumentException("DECR command requires 1 argument");
        }
        
        return "UPDATE redis_kv SET value_data = CAST(COALESCE(value_data, '0') AS INTEGER) - 1, " +
               "updated_at = CURRENT_TIMESTAMP WHERE key_name = ?";
    }
    
    // 哈希命令转换方法
    private String convertHSetToSql(List<String> args) throws Exception {
        if (args.size() < 3) {
            throw new IllegalArgumentException("HSET command requires at least 3 arguments");
        }
        
        return "INSERT OR REPLACE INTO redis_hash (key_name, field_name, value_data, updated_at) " +
               "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
    }
    
    private String convertHGetToSql(List<String> args) throws Exception {
        if (args.size() < 2) {
            throw new IllegalArgumentException("HGET command requires 2 arguments");
        }
        
        return "SELECT value_data FROM redis_hash WHERE key_name = ? AND field_name = ?";
    }
    
    private String convertHDelToSql(List<String> args) throws Exception {
        if (args.size() < 2) {
            throw new IllegalArgumentException("HDEL command requires at least 2 arguments");
        }
        
        String key = args.get(0);
        List<String> fields = args.subList(1, args.size());
        
        StringBuilder sql = new StringBuilder("DELETE FROM redis_hash WHERE key_name = ? AND field_name IN (");
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) sql.append(", ");
            sql.append("?");
        }
        sql.append(")");
        
        return sql.toString();
    }
    
    private String convertHGetAllToSql(List<String> args) throws Exception {
        if (args.size() < 1) {
            throw new IllegalArgumentException("HGETALL command requires 1 argument");
        }
        
        return "SELECT field_name, value_data FROM redis_hash WHERE key_name = ? ORDER BY field_name";
    }
    
    private String convertHKeysToSql(List<String> args) throws Exception {
        if (args.size() != 1) {
            throw new IllegalArgumentException("HKEYS command requires exactly 1 argument");
        }
        
        return "SELECT field_name FROM redis_hash WHERE key_name = ? ORDER BY field_name";
    }
    
    private String convertHValsToSql(List<String> args) throws Exception {
        if (args.size() != 1) {
            throw new IllegalArgumentException("HVALS command requires exactly 1 argument");
        }
        
        return "SELECT value_data FROM redis_hash WHERE key_name = ? ORDER BY field_name";
    }
    
    private String convertHExistsToSql(List<String> args) throws Exception {
        if (args.size() != 2) {
            throw new IllegalArgumentException("HEXISTS command requires exactly 2 arguments");
        }
        
        return "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END as exists_result FROM redis_hash WHERE key_name = ? AND field_name = ?";
    }
    
    private String convertHIncrByToSql(List<String> args) throws Exception {
        if (args.size() != 3) {
            throw new IllegalArgumentException("HINCRBY command requires exactly 3 arguments");
        }
        
        return "UPDATE redis_hash SET value_data = CAST(COALESCE(value_data, '0') AS INTEGER) + CAST(? AS INTEGER), " +
               "updated_at = CURRENT_TIMESTAMP WHERE key_name = ? AND field_name = ?";
    }
    
    private String convertHLenToSql(List<String> args) throws Exception {
        if (args.size() != 1) {
            throw new IllegalArgumentException("HLEN command requires exactly 1 argument");
        }
        
        return "SELECT COUNT(*) as hash_length FROM redis_hash WHERE key_name = ?";
    }
    
    // 工具方法
    private String escapeString(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }
    
    // 兼容性方法（保持向后兼容）
    @Deprecated
    public String convertSetToSql(String key, String value) {
        return String.format(
            "INSERT OR REPLACE INTO redis_kv (key_name, value_data, updated_at) " +
            "VALUES ('%s', '%s', CURRENT_TIMESTAMP)",
            escapeString(key), escapeString(value)
        );
    }
    
    @Deprecated
    public String convertGetToSql(String key) {
        return String.format(
            "SELECT value_data FROM redis_kv WHERE key_name = '%s'",
            escapeString(key)
        );
    }
    
    @Deprecated
    public String convertDelToSql(String key) {
        return String.format(
            "DELETE FROM redis_kv WHERE key_name = '%s'",
            escapeString(key)
        );
    }
    
    @Deprecated
    public String convertHSetToSql(String key, String field, String value) {
        return String.format(
            "INSERT OR REPLACE INTO redis_hash (key_name, field_name, value_data, updated_at) " +
            "VALUES ('%s', '%s', '%s', CURRENT_TIMESTAMP)",
            escapeString(key), escapeString(field), escapeString(value)
        );
    }
    
    @Deprecated
    public String convertHGetToSql(String key, String field) {
        return String.format(
            "SELECT value_data FROM redis_hash WHERE key_name = '%s' AND field_name = '%s'",
            escapeString(key), escapeString(field)
        );
    }

    private String convertExistsToSql(List<String> args) throws Exception {
        if (args.size() != 1) {
            throw new IllegalArgumentException("EXISTS command requires exactly 1 argument");
        }
        return "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END as exists_result FROM redis_kv WHERE key_name = ?";
    }

    private String convertExpireToSql(List<String> args) throws Exception {
        if (args.size() != 2) {
            throw new IllegalArgumentException("EXPIRE command requires exactly 2 arguments");
        }
        return "UPDATE redis_kv SET expires_at = datetime('now', '+' || ? || ' seconds') WHERE key_name = ?";
    }

    private String convertTtlToSql(List<String> args) throws Exception {
        if (args.size() != 1) {
            throw new IllegalArgumentException("TTL command requires exactly 1 argument");
        }
        return "SELECT CASE WHEN expires_at IS NULL THEN -1 WHEN expires_at > datetime('now') THEN CAST((julianday(expires_at) - julianday('now')) * 86400 AS INTEGER) ELSE -2 END as ttl_result FROM redis_kv WHERE key_name = ?";
    }
} // 这里才是类的真正结束