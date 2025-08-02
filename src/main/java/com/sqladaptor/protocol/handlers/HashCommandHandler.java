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

        logger.debug("Processing HASH command: {} with {} arguments", command, args.size());

        switch (command) {
            case "HSET":
                return handleHSet(args);
            case "HGET":
                return handleHGet(args);
            case "HDEL":
                return handleHDel(args);
            case "HGETALL":
                return handleHGetAll(args);
            case "HKEYS":
                return handleHKeys(args);
            case "HVALS":
                return handleHVals(args);
            case "HEXISTS":
                return handleHExists(args);
            case "HINCRBY":
                return handleHIncrBy(args);
            case "HLEN":
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
            String result = databaseManager.executeQuery(sql, args.get(0));
            return result != null ? result : "*0\r\n";
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
            String result = databaseManager.executeQuery(sql, args.get(0));
            return result != null ? result : "*0\r\n";
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
            return result != null ? result : ":0\r\n";
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
            String result = databaseManager.executeQuery(sql, args.get(0), args.get(1), args.get(2));
            return result != null ? result : ":0\r\n";
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
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("HLEN command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }
}