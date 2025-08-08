package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class ListCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(ListCommandHandler.class);

    public ListCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }

    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();

        logger.info("[LIST] Processing command: {} with {} args: {}", command, args.size(), args);

        switch (command) {
            case "LPUSH":
                return handleLPush(args, commandNode);
            case "RPUSH":
                return handleRPush(args, commandNode);
            case "LPOP":
                return handleLPop(args, commandNode);
            case "RPOP":
                return handleRPop(args, commandNode);
            case "LLEN":
                return handleLLen(args, commandNode);
            case "LRANGE":
                return handleLRange(args, commandNode);
            case "LINDEX":
                return handleLIndex(args, commandNode);
            case "LSET":
                return handleLSet(args, commandNode);
            case "LREM":
                return handleLRem(args, commandNode);
            case "LTRIM":
                return handleLTrim(args, commandNode);
            case "LINSERT":
                return handleLInsert(args, commandNode);
            case "LPUSHX":
                return handleLPushX(args, commandNode);
            case "RPUSHX":
                return handleRPushX(args, commandNode);
            case "RPOPLPUSH":
                return handleRPopLPush(args, commandNode);
            case "BLPOP":
                return handleBLPop(args, commandNode);
            case "BRPOP":
                return handleBRPop(args, commandNode);
            case "BRPOPLPUSH":
                return handleBRPopLPush(args, commandNode);
            default:
                logger.warn("Unsupported LIST command: {}", command);
                return "-ERR Unsupported list command: " + command + "\r\n";
        }
    }

    private String handleLPush(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'lpush' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing LPUSH command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("LPUSH command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleRPush(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'rpush' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing RPUSH command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("RPUSH command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleLPop(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'lpop' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing LPOP command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            if (result == null) {
                return "$-1\r\n";
            }
            return "$" + result.length() + "\r\n" + result + "\r\n";
        } catch (SQLException e) {
            logger.error("LPOP command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleRPop(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'rpop' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing RPOP command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            if (result == null) {
                return "$-1\r\n";
            }
            return "$" + result.length() + "\r\n" + result + "\r\n";
        } catch (SQLException e) {
            logger.error("RPOP command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleLLen(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'llen' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing LLEN command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("LLEN command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleLRange(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'lrange' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing LRANGE command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "*0\r\n";
        } catch (SQLException e) {
            logger.error("LRANGE command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleLIndex(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'lindex' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing LINDEX command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            if (result == null) {
                return "$-1\r\n";
            }
            return "$" + result.length() + "\r\n" + result + "\r\n";
        } catch (SQLException e) {
            logger.error("LINDEX command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleLSet(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'lset' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing LSET command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return "+OK\r\n";
        } catch (SQLException e) {
            logger.error("LSET command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleLRem(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'lrem' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing LREM command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("LREM command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleLTrim(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'ltrim' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing LTRIM command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return "+OK\r\n";
        } catch (SQLException e) {
            logger.error("LTRIM command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleLInsert(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 4) {
            return "-ERR wrong number of arguments for 'linsert' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing LINSERT command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":-1\r\n";
        } catch (SQLException e) {
            logger.error("LINSERT command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleLPushX(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'lpushx' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing LPUSHX command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("LPUSHX command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleRPushX(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'rpushx' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing RPUSHX command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("RPUSHX command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleRPopLPush(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'rpoplpush' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing RPOPLPUSH command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            if (result == null) {
                return "$-1\r\n";
            }
            return "$" + result.length() + "\r\n" + result + "\r\n";
        } catch (SQLException e) {
            logger.error("RPOPLPUSH command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleBLPop(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'blpop' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing BLPOP command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "*-1\r\n";
        } catch (SQLException e) {
            logger.error("BLPOP command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleBRPop(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'brpop' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing BRPOP command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "*-1\r\n";
        } catch (SQLException e) {
            logger.error("BRPOP command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleBRPopLPush(List<String> args, RedisCommandNode commandNode) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'brpoplpush' command\r\n";
        }

        String sql = converter.convertToSql(commandNode);
        logger.debug("Executing BRPOPLPUSH command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            if (result == null) {
                return "$-1\r\n";
            }
            return "$" + result.length() + "\r\n" + result + "\r\n";
        } catch (SQLException e) {
            logger.error("BRPOPLPUSH command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }
}