package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class SortedSetCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(SortedSetCommandHandler.class);
    
    public SortedSetCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }
    
    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();

        logger.debug("Processing SORTED_SET command: {} with {} arguments", command, args.size());

        switch (command) {
            case "ZADD":
                return handleZAdd(args);
            case "ZREM":
                return handleZRem(args);
            case "ZRANGE":
                return handleZRange(args);
            case "ZCARD":
                return handleZCard(args);
            case "ZSCORE":
                return handleZScore(args);
            case "ZCOUNT":
                return handleZCount(args);
            case "ZINCRBY":
                return handleZIncrBy(args);
            case "ZINTERSTORE":
                return handleZInterStore(args);
            case "ZLEXCOUNT":
                return handleZLexCount(args);
            case "ZRANGEBYLEX":
                return handleZRangeByLex(args);
            case "ZRANGEBYSCORE":
                return handleZRangeByScore(args);
            case "ZRANK":
                return handleZRank(args);
            case "ZREMRANGEBYLEX":
                return handleZRemRangeByLex(args);
            case "ZREMRANGEBYRANK":
                return handleZRemRangeByRank(args);
            case "ZREMRANGEBYSCORE":
                return handleZRemRangeByScore(args);
            case "ZREVRANGE":
                return handleZRevRange(args);
            case "ZREVRANGEBYSCORE":
                return handleZRevRangeByScore(args);
            case "ZREVRANK":
                return handleZRevRank(args);
            case "ZUNIONSTORE":
                return handleZUnionStore(args);
            case "ZSCAN":
                return handleZScan(args);
            default:
                logger.warn("Unsupported SORTED_SET command: {}", command);
                return "-ERR Unsupported sorted set command '" + command + "'\r\n";
        }
    }

    // ZADD - 向有序集合添加一个或多个成员，或者更新已存在成员的分数
    private String handleZAdd(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'zadd' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZADD", args));
        return databaseManager.executeQuery(sql);
    }

    // ZREM - 移除有序集合中的一个或多个成员
    private String handleZRem(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'zrem' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZREM", args));
        return databaseManager.executeQuery(sql);
    }

    // ZRANGE - 通过索引区间返回有序集合成指定区间内的成员
    private String handleZRange(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'zrange' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZRANGE", args));
        return databaseManager.executeQuery(sql);
    }

    // ZCARD - 获取有序集合的成员数
    private String handleZCard(List<String> args) throws Exception {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'zcard' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZCARD", args));
        return databaseManager.executeQuery(sql);
    }

    // ZSCORE - 返回有序集中，成员的分数值
    private String handleZScore(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'zscore' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZSCORE", args));
        return databaseManager.executeQuery(sql);
    }

    // ZCOUNT - 计算在有序集合中指定区间分数的成员数
    private String handleZCount(List<String> args) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'zcount' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZCOUNT", args));
        return databaseManager.executeQuery(sql);
    }

    // ZINCRBY - 有序集合中对指定成员的分数加上增量 increment
    private String handleZIncrBy(List<String> args) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'zincrby' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZINCRBY", args));
        return databaseManager.executeQuery(sql);
    }

    // ZINTERSTORE - 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中
    private String handleZInterStore(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'zinterstore' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZINTERSTORE", args));
        return databaseManager.executeQuery(sql);
    }

    // ZLEXCOUNT - 在有序集合中计算指定字典区间内成员数量
    private String handleZLexCount(List<String> args) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'zlexcount' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZLEXCOUNT", args));
        return databaseManager.executeQuery(sql);
    }

    // ZRANGEBYLEX - 通过字典区间返回有序集合的成员
    private String handleZRangeByLex(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'zrangebylex' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZRANGEBYLEX", args));
        return databaseManager.executeQuery(sql);
    }

    // ZRANGEBYSCORE - 通过分数返回有序集合指定区间内的成员
    private String handleZRangeByScore(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'zrangebyscore' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZRANGEBYSCORE", args));
        return databaseManager.executeQuery(sql);
    }

    // ZRANK - 返回有序集合中指定成员的索引
    private String handleZRank(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'zrank' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZRANK", args));
        return databaseManager.executeQuery(sql);
    }

    // ZREMRANGEBYLEX - 移除有序集合中给定的字典区间的所有成员
    private String handleZRemRangeByLex(List<String> args) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'zremrangebylex' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZREMRANGEBYLEX", args));
        return databaseManager.executeQuery(sql);
    }

    // ZREMRANGEBYRANK - 移除有序集合中给定的排名区间的所有成员
    private String handleZRemRangeByRank(List<String> args) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'zremrangebyrank' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZREMRANGEBYRANK", args));
        return databaseManager.executeQuery(sql);
    }

    // ZREMRANGEBYSCORE - 移除有序集合中给定的分数区间的所有成员
    private String handleZRemRangeByScore(List<String> args) throws Exception {
        if (args.size() != 3) {
            return "-ERR wrong number of arguments for 'zremrangebyscore' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZREMRANGEBYSCORE", args));
        return databaseManager.executeQuery(sql);
    }

    // ZREVRANGE - 返回有序集中指定区间内的成员，通过索引，分数从高到底
    private String handleZRevRange(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'zrevrange' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZREVRANGE", args));
        return databaseManager.executeQuery(sql);
    }

    // ZREVRANGEBYSCORE - 返回有序集中指定分数区间内的成员，分数从高到低排序
    private String handleZRevRangeByScore(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'zrevrangebyscore' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZREVRANGEBYSCORE", args));
        return databaseManager.executeQuery(sql);
    }

    // ZREVRANK - 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序
    private String handleZRevRank(List<String> args) throws Exception {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'zrevrank' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZREVRANK", args));
        return databaseManager.executeQuery(sql);
    }

    // ZUNIONSTORE - 计算一个或多个有序集的并集，并存储在新的 key 中
    private String handleZUnionStore(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'zunionstore' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZUNIONSTORE", args));
        return databaseManager.executeQuery(sql);
    }

    // ZSCAN - 迭代有序集合中的元素（包括元素成员和元素分值）
    private String handleZScan(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'zscan' command\r\n";
        }

        String sql = converter.convertToSql(new RedisCommandNode("ZSCAN", args));
        return databaseManager.executeQuery(sql);
    }
}