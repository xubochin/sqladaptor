package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class StringCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(StringCommandHandler.class);

    public StringCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }

    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();

        logger.debug("Processing STRING command: {} with {} arguments", command, args.size());

        switch (command) {
            case "SET":
                return handleSet(args);
            case "GET":
                return handleGet(args);
            case "INCR":
                return handleIncr(args);
            case "DECR":
                return handleDecr(args);
            case "INCRBY":
                return handleIncrBy(args);
            case "DECRBY":
                return handleDecrBy(args);
            case "INCRBYFLOAT":
                return handleIncrByFloat(args);
            case "APPEND":
                return handleAppend(args);
            case "STRLEN":
                return handleStrLen(args);
            case "GETRANGE":
                return handleGetRange(args);
            case "SETRANGE":
                return handleSetRange(args);
            case "GETSET":
                return handleGetSet(args);
            case "MGET":
                return handleMGet(args);
            case "MSET":
                return handleMSet(args);
            case "MSETNX":
                return handleMSetNx(args);
            case "SETEX":
                return handleSetEx(args);
            case "PSETEX":
                return handlePSetEx(args);
            case "SETNX":
                return handleSetNx(args);
            case "GETBIT":
                return handleGetBit(args);
            case "SETBIT":
                return handleSetBit(args);
            default:
                logger.warn("Unsupported STRING command: {}", command);
                return "-ERR Unsupported string command '" + command + "'\r\n";
        }
    }

    private String handleSet(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'set' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("SET", args));

        logger.debug("Executing SET command with SQL: {}", sql);
        logger.debug("Parameters: key='{}', value='{}'", args.get(0), args.get(1));

        try {
            databaseManager.executeUpdate(sql, args.get(0), args.get(1));
            logger.debug("SET command executed successfully");
            return "+OK\r\n";
        } catch (SQLException e) {
            logger.error("SET command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleGet(List<String> args) throws Exception {
        if (args.size() < 1) {
            return "-ERR wrong number of arguments for 'get' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("GET", args));
        logger.debug("Executing GET command with SQL: {}", sql);
        logger.debug("Query parameter: key='{}'", args.get(0));

        String result = databaseManager.executeQuery(sql, args.get(0));

        if (result == null) {
            return "$-1\r\n";
        }

        return "$" + result.length() + "\r\n" + result + "\r\n";
    }

    private String handleIncr(List<String> args) throws Exception {
        if (args.size() < 1) {
            return "-ERR wrong number of arguments for 'incr' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("INCR", args));
        int affected = databaseManager.executeUpdate(sql, args.get(0));

        if (affected == 0) {
            // 键不存在，插入新值
            String insertSql = "INSERT INTO redis_kv (key_name, value_data, updated_at) VALUES (?, '1', CURRENT_TIMESTAMP)";
            databaseManager.executeUpdate(insertSql, args.get(0));
            return ":1\r\n";
        } else {
            // 获取更新后的值
            String selectSql = "SELECT value_data FROM redis_kv WHERE key_name = ?";
            String result = databaseManager.executeQuery(selectSql, args.get(0));
            return ":" + result + "\r\n";
        }
    }

    private String handleDecr(List<String> args) throws Exception {
        if (args.size() < 1) {
            return "-ERR wrong number of arguments for 'decr' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("DECR", args));
        int affected = databaseManager.executeUpdate(sql, args.get(0));

        if (affected == 0) {
            // 键不存在，插入新值
            String insertSql = "INSERT INTO redis_kv (key_name, value_data, updated_at) VALUES (?, '-1', CURRENT_TIMESTAMP)";
            databaseManager.executeUpdate(insertSql, args.get(0));
            return ":-1\r\n";
        } else {
            // 获取更新后的值
            String selectSql = "SELECT value_data FROM redis_kv WHERE key_name = ?";
            String result = databaseManager.executeQuery(selectSql, args.get(0));
            return ":" + result + "\r\n";
        }
    }

    private String handleIncrBy(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'incrby' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("INCRBY", args));
        int affected = databaseManager.executeUpdate(sql, args.get(0), args.get(1));

        if (affected == 0) {
            // 键不存在，插入新值
            String insertSql = "INSERT INTO redis_kv (key_name, value_data, updated_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
            databaseManager.executeUpdate(insertSql, args.get(0), args.get(1));
            return ":" + args.get(1) + "\r\n";
        } else {
            // 获取更新后的值
            String selectSql = "SELECT value_data FROM redis_kv WHERE key_name = ?";
            String result = databaseManager.executeQuery(selectSql, args.get(0));
            return ":" + result + "\r\n";
        }
    }

    private String handleDecrBy(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'decrby' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("DECRBY", args));
        int affected = databaseManager.executeUpdate(sql, args.get(0), args.get(1));

        if (affected == 0) {
            // 键不存在，插入新值
            String negativeValue = "-" + args.get(1);
            String insertSql = "INSERT INTO redis_kv (key_name, value_data, updated_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
            databaseManager.executeUpdate(insertSql, args.get(0), negativeValue);
            return ":" + negativeValue + "\r\n";
        } else {
            // 获取更新后的值
            String selectSql = "SELECT value_data FROM redis_kv WHERE key_name = ?";
            String result = databaseManager.executeQuery(selectSql, args.get(0));
            return ":" + result + "\r\n";
        }
    }

    private String handleIncrByFloat(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'incrbyfloat' command\r\n";
        }

        try {
            Double.parseDouble(args.get(1));
        } catch (NumberFormatException e) {
            return "-ERR value is not a valid float\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("INCRBYFLOAT", args));
        int affected = databaseManager.executeUpdate(sql, args.get(0), args.get(1));

        if (affected == 0) {
            // 键不存在，插入新值
            String insertSql = "INSERT INTO redis_kv (key_name, value_data, updated_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
            databaseManager.executeUpdate(insertSql, args.get(0), args.get(1));
            return "$" + args.get(1).length() + "\r\n" + args.get(1) + "\r\n";
        } else {
            // 获取更新后的值
            String selectSql = "SELECT value_data FROM redis_kv WHERE key_name = ?";
            String result = databaseManager.executeQuery(selectSql, args.get(0));
            return "$" + result.length() + "\r\n" + result + "\r\n";
        }
    }

    private String handleAppend(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'append' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("APPEND", args));
        int affected = databaseManager.executeUpdate(sql, args.get(0), args.get(1));

        // 获取更新后的值长度
        String selectSql = "SELECT LENGTH(value_data) FROM redis_kv WHERE key_name = ?";
        String lengthResult = databaseManager.executeQuery(selectSql, args.get(0));
        
        return ":" + (lengthResult != null ? lengthResult : "0") + "\r\n";
    }

    private String handleStrLen(List<String> args) throws Exception {
        if (args.size() < 1) {
            return "-ERR wrong number of arguments for 'strlen' command\r\n";
        }

        String selectSql = "SELECT LENGTH(value_data) FROM redis_kv WHERE key_name = ?";
        String result = databaseManager.executeQuery(selectSql, args.get(0));
        
        return ":" + (result != null ? result : "0") + "\r\n";
    }

    private String handleGetRange(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'getrange' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("GETRANGE", args));
        String result = databaseManager.executeQuery(sql, args.get(0), args.get(1), args.get(2));
        
        if (result == null) {
            result = "";
        }
        
        return "$" + result.length() + "\r\n" + result + "\r\n";
    }

    private String handleSetRange(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'setrange' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("SETRANGE", args));
        databaseManager.executeUpdate(sql, args.get(0), args.get(1), args.get(2));
        
        // 返回字符串长度
        String selectSql = "SELECT LENGTH(value_data) FROM redis_kv WHERE key_name = ?";
        String lengthResult = databaseManager.executeQuery(selectSql, args.get(0));
        
        return ":" + (lengthResult != null ? lengthResult : "0") + "\r\n";
    }

    private String handleGetSet(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'getset' command\r\n";
        }

        // 先获取旧值
        String selectSql = "SELECT value_data FROM redis_kv WHERE key_name = ?";
        String oldValue = databaseManager.executeQuery(selectSql, args.get(0));
        
        // 设置新值
        String sql = converter.convertToSql(new RedisCommandNode("SET", args));
        databaseManager.executeUpdate(sql, args.get(0), args.get(1));
        
        if (oldValue == null) {
            return "$-1\r\n";
        }
        
        return "$" + oldValue.length() + "\r\n" + oldValue + "\r\n";
    }

    private String handleMGet(List<String> args) throws Exception {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'mget' command\r\n";
        }

        StringBuilder response = new StringBuilder("*" + args.size() + "\r\n");
        
        for (String key : args) {
            String selectSql = "SELECT value_data FROM redis_kv WHERE key_name = ?";
            String result = databaseManager.executeQuery(selectSql, key);
            
            if (result == null) {
                response.append("$-1\r\n");
            } else {
                response.append("$").append(result.length()).append("\r\n").append(result).append("\r\n");
            }
        }
        
        return response.toString();
    }

    private String handleMSet(List<String> args) throws Exception {
        if (args.size() < 2 || args.size() % 2 != 0) {
            return "-ERR wrong number of arguments for 'mset' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("MSET", args));
        databaseManager.executeUpdate(sql, args.toArray());
        
        return "+OK\r\n";
    }

    private String handleMSetNx(List<String> args) throws Exception {
        if (args.size() < 2 || args.size() % 2 != 0) {
            return "-ERR wrong number of arguments for 'msetnx' command\r\n";
        }

        // 检查所有键是否都不存在
        for (int i = 0; i < args.size(); i += 2) {
            String selectSql = "SELECT 1 FROM redis_kv WHERE key_name = ?";
            String exists = databaseManager.executeQuery(selectSql, args.get(i));
            if (exists != null) {
                return ":0\r\n"; // 至少有一个键存在
            }
        }

        // 所有键都不存在，执行设置
        String sql = converter.convertToSql(new RedisCommandNode("MSET", args));
        databaseManager.executeUpdate(sql, args.toArray());
        
        return ":1\r\n";
    }

    private String handleSetEx(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'setex' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("SETEX", args));
        databaseManager.executeUpdate(sql, args.get(0), args.get(1), args.get(2));
        
        return "+OK\r\n";
    }

    private String handlePSetEx(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'psetex' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("PSETEX", args));
        databaseManager.executeUpdate(sql, args.get(0), args.get(1), args.get(2));
        
        return "+OK\r\n";
    }

    private String handleSetNx(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'setnx' command\r\n";
        }

        // 检查键是否存在
        String selectSql = "SELECT 1 FROM redis_kv WHERE key_name = ?";
        String exists = databaseManager.executeQuery(selectSql, args.get(0));
        
        if (exists != null) {
            return ":0\r\n"; // 键已存在
        }

        // 键不存在，设置值
        String sql = converter.convertToSql(new RedisCommandNode("SET", args));
        databaseManager.executeUpdate(sql, args.get(0), args.get(1));
        
        return ":1\r\n";
    }

    private String handleGetBit(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'getbit' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("GETBIT", args));
        String result = databaseManager.executeQuery(sql, args.get(0), args.get(1));
        
        return ":" + (result != null ? result : "0") + "\r\n";
    }

    private String handleSetBit(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'setbit' command\r\n";
        }

        // 先获取旧的位值
        String selectSql = "SELECT SUBSTRING(value_data, ?, 1) FROM redis_kv WHERE key_name = ?";
        String oldBit = databaseManager.executeQuery(selectSql, args.get(1), args.get(0));
        
        String sql = converter.convertToSql(new RedisCommandNode("SETBIT", args));
        databaseManager.executeUpdate(sql, args.get(0), args.get(1), args.get(2));
        
        return ":" + (oldBit != null ? oldBit : "0") + "\r\n";
    }
}