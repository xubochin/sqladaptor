package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class KeyCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(KeyCommandHandler.class);

    public KeyCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }

    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();

        logger.debug("Processing KEY command: {} with {} arguments", command, args.size());

        switch (command) {
            case "DEL":
                return handleDel(args);
            case "EXISTS":
                return handleExists(args);
            case "EXPIRE":
                return handleExpire(args);
            case "TTL":
                return handleTtl(args);
            case "PERSIST":
                return handlePersist(args);
            case "TYPE":
                return handleType(args);
            case "PEXPIRE":
                return handlePExpire(args);
            case "PTTL":
                return handlePTtl(args);
            case "DUMP":
                return handleDump(args);
            case "EXPIREAT":
                return handleExpireAt(args);
            case "PEXPIREAT":
                return handlePExpireAt(args);
            case "KEYS":
                return handleKeys(args);
            case "MOVE":
                return handleMove(args);
            case "RANDOMKEY":
                return handleRandomKey(args);
            case "RENAME":
                return handleRename(args);
            case "RENAMENX":
                return handleRenameNx(args);
            default:
                logger.warn("Unsupported KEY command: {}", command);
                return "-ERR Unsupported key command '" + command + "'\r\n";
        }
    }

    private String handleDel(List<String> args) throws Exception {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'del' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("DEL", args));
        logger.debug("Executing DEL command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("DEL command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleExists(List<String> args) throws Exception {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'exists' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("EXISTS", args));
        logger.debug("Executing EXISTS command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("EXISTS command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleExpire(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'expire' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("EXPIRE", args));
        logger.debug("Executing EXPIRE command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("EXPIRE command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleTtl(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'ttl' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("TTL", args));
        logger.debug("Executing TTL command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":-1\r\n";
        } catch (SQLException e) {
            logger.error("TTL command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handlePersist(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'persist' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("PERSIST", args));
        logger.debug("Executing PERSIST command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("PERSIST command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleType(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'type' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("TYPE", args));
        logger.debug("Executing TYPE command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "+none\r\n";
        } catch (SQLException e) {
            logger.error("TYPE command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handlePExpire(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'pexpire' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("PEXPIRE", args));
        logger.debug("Executing PEXPIRE command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("PEXPIRE command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handlePTtl(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'pttl' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("PTTL", args));
        logger.debug("Executing PTTL command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":-1\r\n";
        } catch (SQLException e) {
            logger.error("PTTL command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleDump(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'dump' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("DUMP", args));
        logger.debug("Executing DUMP command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "$-1\r\n";
        } catch (SQLException e) {
            logger.error("DUMP command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleExpireAt(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'expireat' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("EXPIREAT", args));
        logger.debug("Executing EXPIREAT command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("EXPIREAT command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handlePExpireAt(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'pexpireat' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("PEXPIREAT", args));
        logger.debug("Executing PEXPIREAT command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("PEXPIREAT command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleKeys(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'keys' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("KEYS", args));
        logger.debug("Executing KEYS command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "*0\r\n";
        } catch (SQLException e) {
            logger.error("KEYS command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleMove(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'move' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("MOVE", args));
        logger.debug("Executing MOVE command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("MOVE command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleRandomKey(List<String> args) throws Exception {
        if (!args.isEmpty()) {
            return "-ERR wrong number of arguments for 'randomkey' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("RANDOMKEY", args));
        logger.debug("Executing RANDOMKEY command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "$-1\r\n";
        } catch (SQLException e) {
            logger.error("RANDOMKEY command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleRename(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'rename' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("RENAME", args));
        logger.debug("Executing RENAME command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "+OK\r\n";
        } catch (SQLException e) {
            logger.error("RENAME command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleRenameNx(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'renamenx' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("RENAMENX", args));
        logger.debug("Executing RENAMENX command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("RENAMENX command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }
}