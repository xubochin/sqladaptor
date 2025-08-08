package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class SetCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(SetCommandHandler.class);

    public SetCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }

    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();

        logger.debug("Processing SET command: {} with {} arguments", command, args.size());

        switch (command) {
            case "SADD":
                return handleSAdd(args);
            case "SREM":
                return handleSRem(args);
            case "SMEMBERS":
                return handleSMembers(args);
            case "SCARD":
                return handleSCard(args);
            case "SISMEMBER":
                return handleSIsMember(args);
            case "SDIFF":
                return handleSDiff(args);
            case "SDIFFSTORE":
                return handleSDiffStore(args);
            case "SINTER":
                return handleSInter(args);
            case "SINTERSTORE":
                return handleSInterStore(args);
            case "SMOVE":
                return handleSMove(args);
            case "SPOP":
                return handleSPop(args);
            case "SRANDMEMBER":
                return handleSRandMember(args);
            case "SUNION":
                return handleSUnion(args);
            case "SUNIONSTORE":
                return handleSUnionStore(args);
            case "SSCAN":
                return handleSScan(args);
            default:
                logger.warn("Unsupported SET command: {}", command);
                return "-ERR Unsupported set command '" + command + "'\r\n";
        }
    }

    private String handleSAdd(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'sadd' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SADD", args));
        logger.debug("Executing SADD command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("SADD command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSRem(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'srem' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SREM", args));
        logger.debug("Executing SREM command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("SREM command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSMembers(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'smembers' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SMEMBERS", args));
        logger.debug("Executing SMEMBERS command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "*0\r\n";
        } catch (SQLException e) {
            logger.error("SMEMBERS command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSCard(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'scard' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SCARD", args));
        logger.debug("Executing SCARD command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("SCARD command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSIsMember(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'sismember' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SISMEMBER", args));
        logger.debug("Executing SISMEMBER command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("SISMEMBER command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSDiff(List<String> args) throws Exception {
        if (args.size() < 1) {
            return "-ERR wrong number of arguments for 'sdiff' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SDIFF", args));
        logger.debug("Executing SDIFF command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "*0\r\n";
        } catch (SQLException e) {
            logger.error("SDIFF command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSDiffStore(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'sdiffstore' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SDIFFSTORE", args));
        logger.debug("Executing SDIFFSTORE command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("SDIFFSTORE command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSInter(List<String> args) throws Exception {
        if (args.size() < 1) {
            return "-ERR wrong number of arguments for 'sinter' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SINTER", args));
        logger.debug("Executing SINTER command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "*0\r\n";
        } catch (SQLException e) {
            logger.error("SINTER command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSInterStore(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'sinterstore' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SINTERSTORE", args));
        logger.debug("Executing SINTERSTORE command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("SINTERSTORE command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSMove(List<String> args) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'smove' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SMOVE", args));
        logger.debug("Executing SMOVE command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("SMOVE command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSPop(List<String> args) throws Exception {
        if (args.size() < 1 || args.size() > 2) {
            return "-ERR wrong number of arguments for 'spop' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SPOP", args));
        logger.debug("Executing SPOP command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "$-1\r\n";
        } catch (SQLException e) {
            logger.error("SPOP command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSRandMember(List<String> args) throws Exception {
        if (args.size() < 1 || args.size() > 2) {
            return "-ERR wrong number of arguments for 'srandmember' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SRANDMEMBER", args));
        logger.debug("Executing SRANDMEMBER command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "$-1\r\n";
        } catch (SQLException e) {
            logger.error("SRANDMEMBER command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSUnion(List<String> args) throws Exception {
        if (args.size() < 1) {
            return "-ERR wrong number of arguments for 'sunion' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SUNION", args));
        logger.debug("Executing SUNION command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "*0\r\n";
        } catch (SQLException e) {
            logger.error("SUNION command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSUnionStore(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'sunionstore' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SUNIONSTORE", args));
        logger.debug("Executing SUNIONSTORE command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : ":0\r\n";
        } catch (SQLException e) {
            logger.error("SUNIONSTORE command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }

    private String handleSScan(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'sscan' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SSCAN", args));
        logger.debug("Executing SSCAN command with SQL: {}", sql);
        
        try {
            String result = databaseManager.executeQuery(sql);
            return result != null ? result : "*2\r\n$1\r\n0\r\n*0\r\n";
        } catch (SQLException e) {
            logger.error("SSCAN command failed: {}", e.getMessage());
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }
}