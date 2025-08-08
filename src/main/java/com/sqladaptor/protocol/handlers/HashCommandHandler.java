package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class HashCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(HashCommandHandler.class);

    public HashCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }

    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();

        logger.info("[HASH] Processing HASH command: {} with {} arguments: {}", command, args.size(), args);
        System.out.println("[HASH DEBUG] Command: " + command + ", Args: " + args);

        switch (command) {
            case "HSET":
                System.out.println("[HASH DEBUG] Executing HSET with args: " + args);
                return handleHSet(args);
            case "HGET":
                System.out.println("[HASH DEBUG] Executing HGET with args: " + args);
                return handleHGet(args);
            case "HDEL":
                System.out.println("[HASH DEBUG] Executing HDEL with args: " + args);
                return handleHDel(args);
            case "HGETALL":
                System.out.println("[HASH DEBUG] Executing HGETALL with args: " + args);
                return handleHGetAll(args);
            case "HKEYS":
                System.out.println("[HASH DEBUG] Executing HKEYS with args: " + args);
                return handleHKeys(args);
            case "HVALS":
                System.out.println("[HASH DEBUG] Executing HVALS with args: " + args);
                return handleHVals(args);
            case "HEXISTS":
                System.out.println("[HASH DEBUG] Executing HEXISTS with args: " + args);
                return handleHExists(args);
            case "HINCRBY":
                System.out.println("[HASH DEBUG] Executing HINCRBY with args: " + args);
                return handleHIncrBy(args);
            case "HLEN":
                System.out.println("[HASH DEBUG] Executing HLEN with args: " + args);
                return handleHLen(args);
            default:
                logger.warn("Unsupported HASH command: {}", command);
                return "-ERR Unsupported hash command '" + command + "'\r\n";
        }
    }

    private String handleHSet(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'hset' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("HSET", args));
        logger.debug("Executing HSET command with SQL: {}", sql);
        
        try {
            int affected = databaseManager.executeUpdate(sql, args.get(0), args.get(1), args.get(2));
            return ":" + (affected > 0 ? 1 : 0) + "\r\n";
        } catch (SQLException e) {
            logger.error("HSET command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleHGet(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'hget' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("HGET", args));
        logger.debug("Executing HGET command with SQL: {}", sql);
        
        String result = databaseManager.executeQuery(sql, args.get(0), args.get(1));

        if (result == null) {
            return "$-1\r\n";
        }

        return "$" + result.length() + "\r\n" + result + "\r\n";
    }

    private String handleHDel(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'hdel' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("HDEL", args));
        logger.debug("Executing HDEL command with SQL: {}", sql);
        
        Object[] params = new Object[args.size()];
        params[0] = args.get(0); // key
        for (int i = 1; i < args.size(); i++) {
            params[i] = args.get(i); // fields
        }

        try {
            int affected = databaseManager.executeUpdate(sql, params);
            return ":" + affected + "\r\n";
        } catch (SQLException e) {
            logger.error("HDEL command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleHGetAll(List<String> args) throws Exception {
        if (args.size() < 1) {
            return "-ERR wrong number of arguments for 'hgetall' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("HGETALL", args));
        logger.debug("Executing HGETALL command with SQL: {}", sql);
        
        List<String[]> results = databaseManager.executeQueryMultiple(sql, args.get(0));

        if (results.isEmpty()) {
            return "*0\r\n";
        }

        StringBuilder response = new StringBuilder();
        response.append("*").append(results.size() * 2).append("\r\n");

        for (String[] row : results) {
            String field = row[0];
            String value = row[1];

            response.append("$").append(field.length()).append("\r\n")
                    .append(field).append("\r\n");
            response.append("$").append(value.length()).append("\r\n")
                    .append(value).append("\r\n");
        }

        return response.toString();
    }

    private String handleHKeys(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'hkeys' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("HKEYS", args));
        logger.debug("Executing HKEYS command with SQL: {}", sql);
        
        try {
            List<String[]> results = databaseManager.executeQueryMultiple(sql, args.get(0));
            
            if (results.isEmpty()) {
                return "*0\r\n";
            }
            
            StringBuilder response = new StringBuilder();
            response.append("*").append(results.size()).append("\r\n");
            
            for (String[] row : results) {
                String field = row[0];
                response.append("$").append(field.length()).append("\r\n")
                        .append(field).append("\r\n");
            }
            
            return response.toString();
        } catch (SQLException e) {
            logger.error("HKEYS command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleHVals(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'hvals' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("HVALS", args));
        logger.debug("Executing HVALS command with SQL: {}", sql);
        
        try {
            List<String[]> results = databaseManager.executeQueryMultiple(sql, args.get(0));
            
            if (results.isEmpty()) {
                return "*0\r\n";
            }
            
            StringBuilder response = new StringBuilder();
            response.append("*").append(results.size()).append("\r\n");
            
            for (String[] row : results) {
                String value = row[0];
                response.append("$").append(value.length()).append("\r\n")
                        .append(value).append("\r\n");
            }
            
            return response.toString();
        } catch (SQLException e) {
            logger.error("HVALS command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleHExists(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'hexists' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("HEXISTS", args));
        logger.debug("Executing HEXISTS command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql, args.get(0), args.get(1));
            if (result != null && result.equals("1")) {
                return ":1\r\n";
            } else {
                return ":0\r\n";
            }
        } catch (SQLException e) {
            logger.error("HEXISTS command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleHIncrBy(List<String> args) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'hincrby' command\r\n";
        }
        
        String increment = args.get(2);
        try {
            Integer.parseInt(increment);
        } catch (NumberFormatException e) {
            return "-ERR value is not an integer or out of range\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("HINCRBY", args));
        logger.debug("Executing HINCRBY command with SQL: {}", sql);
        
        try {
            // HINCRBY的SQL参数顺序是: increment, key, field
            int affected = databaseManager.executeUpdate(sql, args.get(2), args.get(0), args.get(1));
            
            // 获取更新后的值
            String getValueSql = "SELECT value_data FROM redis_hash WHERE key_name = ? AND field_name = ?";
            String newValue = databaseManager.executeQuery(getValueSql, args.get(0), args.get(1));
            
            return ":" + (newValue != null ? newValue : "0") + "\r\n";
        } catch (SQLException e) {
            logger.error("HINCRBY command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleHLen(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'hlen' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("HLEN", args));
        logger.debug("Executing HLEN command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql, args.get(0));
            return ":" + (result != null ? result : "0") + "\r\n";
        } catch (SQLException e) {
            logger.error("HLEN command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }
}