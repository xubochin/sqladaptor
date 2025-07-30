package com.sqladaptor.protocol;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import com.sqladaptor.parser.RedisCommandParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RedisProtocolHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RedisProtocolHandler.class);
    
    private final DatabaseManager databaseManager;
    private final RedisToSqlConverter converter;
    private final RedisCommandParser commandParser;
    
    // 会话状态
    private int currentDatabase = 0;
    private final Map<String, String> configSettings = new HashMap<>();
    private final Map<String, String> loadedScripts = new HashMap<>();
    
    public RedisProtocolHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.converter = new RedisToSqlConverter();
        this.commandParser = new RedisCommandParser();
        
        // 初始化默认配置
        initializeDefaultConfig();
    }
    
    private void initializeDefaultConfig() {
        configSettings.put("timeout", "0");
        configSettings.put("databases", "16");
        configSettings.put("maxmemory", "0");
        configSettings.put("maxmemory-policy", "noeviction");
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        try {
            String request = buf.toString(CharsetUtil.UTF_8);
            logger.debug("Received Redis command: {}", request);
            
            RedisCommandNode commandNode = commandParser.parse(request);
            String response = processCommand(commandNode);
            
            // Check if channel is still active before writing response
            if (ctx.channel().isActive()) {
                ByteBuf responseBuf = Unpooled.copiedBuffer(response, CharsetUtil.UTF_8);
                ctx.writeAndFlush(responseBuf);
            }
        } catch (Exception e) {
            logger.error("Error processing Redis command", e);
            if (ctx.channel().isActive()) {
                String errorResponse = "-ERR " + e.getMessage() + "\r\n";
                ByteBuf errorBuf = Unpooled.copiedBuffer(errorResponse, CharsetUtil.UTF_8);
                ctx.writeAndFlush(errorBuf);
            }
        } finally {
            buf.release();
        }
    }
    
    private String processCommand(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();  // 确保大写
        List<String> args = commandNode.getArguments();
        
        logger.debug("Processing command: {} with args: {}", command, args);
        logger.debug("Command type: {}", commandNode.getType());
        
        switch (commandNode.getType()) {
            case CONNECTION:
                return handleConnectionCommand(command, args);
            case STRING:
                return handleStringCommand(commandNode);
            case HASH:
                return handleHashCommand(commandNode);
            case SERVER:
                return handleDatabaseCommand(command, args);
            default:
                // 处理CLIENT命令和其他未分类命令
                if ("CLIENT".equals(command)) {  // 现在command已经是大写的
                    return handleClientCommand(args);
                }
                return "-ERR Unknown command '" + command + "'\r\n";
        }
    }
    
    // 添加CLIENT命令处理方法
    private String handleClientCommand(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'client' command\r\n";
        }
        
        String subCommand = args.get(0).toUpperCase();
        switch (subCommand) {
            case "SETINFO":
                // CLIENT SETINFO 命令，用于设置客户端信息
                if (args.size() >= 3) {
                    String infoType = args.get(1);
                    String infoValue = args.get(2);
                    logger.debug("Client info set: {} = {}", infoType, infoValue);
                }
                return "+OK\r\n";
            case "GETNAME":
                // 返回客户端名称，默认为null
                return "$-1\r\n";
            case "SETNAME":
                // 设置客户端名称
                return "+OK\r\n";
            case "LIST":
                // 返回客户端列表，简化实现返回空数组
                return "*0\r\n";
            case "ID":
                // 返回客户端ID
                return ":1\r\n";
            case "KILL":
                // 客户端终止命令
                return "+OK\r\n";
            case "PAUSE":
                // 客户端暂停命令
                return "+OK\r\n";
            case "REPLY":
                // 客户端回复控制
                return "+OK\r\n";
            default:
                return "-ERR Unknown CLIENT subcommand '" + subCommand + "'\r\n";
        }
    }
    
    private String handleConnectionCommand(String command, List<String> args) {
        switch (command.toUpperCase()) {  // 添加 toUpperCase()
            case "PING":
                if (args.isEmpty()) {
                    return "+PONG\r\n";
                } else {
                    String message = args.get(0);
                    return "$" + message.length() + "\r\n" + message + "\r\n";
                }
            case "ECHO":
                if (args.size() >= 1) {
                    String message = args.get(0);
                    return "$" + message.length() + "\r\n" + message + "\r\n";
                } else {
                    return "-ERR wrong number of arguments for 'echo' command\r\n";
                }
            case "AUTH":
                // 简单实现，接受任何密码
                return "+OK\r\n";
            default:
                return "-ERR Unknown connection command '" + command + "'\r\n";
        }
    }
    
    private String handleStringCommand(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();  // 添加 toUpperCase()
        List<String> args = commandNode.getArguments();
        
        switch (command) {
            case "SET":
                return handleSet(args);
            case "GET":
                return handleGet(args);
            case "DEL":
                return handleDel(args);
            case "INCR":
                return handleIncr(args);
            case "DECR":
                return handleDecr(args);
            default:
                return "-ERR Unsupported string command '" + command + "'\r\n";
        }
    }
    
    private String handleHashCommand(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();  // 添加 toUpperCase()
        List<String> args = commandNode.getArguments();
        
        switch (command) {
            case "HSET":
                return handleHSet(args);
            case "HGET":
                return handleHGet(args);
            case "HDEL":
                return handleHDel(args);
            case "HGETALL":
                return handleHGetAll(args);
            default:
                return "-ERR Unsupported hash command '" + command + "'\r\n";
        }
    }
    
    // 字符串命令处理方法
    private String handleSet(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'set' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("SET", args));
        databaseManager.executeUpdate(sql, args.get(0), args.get(1));
        
        return "+OK\r\n";
    }
    
    private String handleGet(List<String> args) throws Exception {
        if (args.size() < 1) {
            return "-ERR wrong number of arguments for 'get' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("GET", args));
        String result = databaseManager.executeQuery(sql, args.get(0));
        
        if (result == null) {
            return "$-1\r\n";
        }
        
        return "$" + result.length() + "\r\n" + result + "\r\n";
    }
    
    private String handleDel(List<String> args) throws Exception {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'del' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("DEL", args));
        int affected = databaseManager.executeUpdate(sql, args.toArray());
        
        return ":" + affected + "\r\n";
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
    
    // 哈希命令处理方法
    private String handleHSet(List<String> args) throws Exception {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'hset' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("HSET", args));
        int affected = databaseManager.executeUpdate(sql, args.get(0), args.get(1), args.get(2));
        
        return ":" + (affected > 0 ? 1 : 0) + "\r\n";
    }
    
    private String handleHGet(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'hget' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("HGET", args));
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
        Object[] params = new Object[args.size()];
        params[0] = args.get(0); // key
        for (int i = 1; i < args.size(); i++) {
            params[i] = args.get(i); // fields
        }
        
        int affected = databaseManager.executeUpdate(sql, params);
        return ":" + affected + "\r\n";
    }
    
    private String handleHGetAll(List<String> args) throws Exception {
        if (args.size() < 1) {
            return "-ERR wrong number of arguments for 'hgetall' command\r\n";
        }
        
        String sql = converter.convertToSql(new RedisCommandNode("HGETALL", args));
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
    
    // 新增命令处理方法
    private String handleDatabaseCommand(String command, List<String> args) {
        switch (command) {
            case "SELECT":
                return handleSelect(args);
            default:
                return "-ERR Unknown database command '" + command + "'\r\n";
        }
    }
    
    private String handleConfigCommand(String command, List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'config' command\r\n";
        }
        
        String subCommand = args.get(0).toUpperCase();
        switch (subCommand) {
            case "GET":
                return handleConfigGet(args.subList(1, args.size()));
            case "SET":
                return handleConfigSet(args.subList(1, args.size()));
            default:
                return "-ERR Unknown CONFIG subcommand '" + subCommand + "'\r\n";
        }
    }
    
    private String handleHyperLogLogCommand(String command, List<String> args) {
        switch (command) {
            case "PFADD":
                return handlePfAdd(args);
            case "PFCOUNT":
                return handlePfCount(args);
            case "PFMERGE":
                return handlePfMerge(args);
            default:
                return "-ERR Unknown HyperLogLog command '" + command + "'\r\n";
        }
    }
    
    private String handleGeoCommand(String command, List<String> args) {
        switch (command) {
            case "GEOADD":
                return handleGeoAdd(args);
            case "GEOPOS":
                return handleGeoPos(args);
            case "GEODIST":
                return handleGeoDist(args);
            case "GEORADIUS":
                return handleGeoRadius(args);
            case "GEORADIUSBYMEMBER":
                return handleGeoRadiusByMember(args);
            default:
                return "-ERR Unknown GEO command '" + command + "'\r\n";
        }
    }
    
    private String handleStreamCommand(String command, List<String> args) {
        switch (command) {
            case "XADD":
                return handleXAdd(args);
            case "XRANGE":
                return handleXRange(args);
            case "XREAD":
                return handleXRead(args);
            case "XGROUP":
                return handleXGroup(args);
            case "XREADGROUP":
                return handleXReadGroup(args);
            default:
                return "-ERR Unknown Stream command '" + command + "'\r\n";
        }
    }
    
    private String handleBitfieldCommand(String command, List<String> args) {
        return handleBitfield(args);
    }
    
    private String handleScriptCommand(String command, List<String> args) {
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
    
    // 具体命令实现
    private String handleSelect(List<String> args) {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'select' command\r\n";
        }
        
        try {
            int dbIndex = Integer.parseInt(args.get(0));
            if (dbIndex < 0 || dbIndex >= 16) {
                return "-ERR DB index is out of range\r\n";
            }
            currentDatabase = dbIndex;
            return "+OK\r\n";
        } catch (NumberFormatException e) {
            return "-ERR invalid DB index\r\n";
        }
    }
    
    private String handleConfigGet(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'config get' command\r\n";
        }
        
        String pattern = args.get(0);
        List<String> result = new ArrayList<>();
        
        for (Map.Entry<String, String> entry : configSettings.entrySet()) {
            if (entry.getKey().matches(pattern.replace("*", ".*"))) {
                result.add(entry.getKey());
                result.add(entry.getValue());
            }
        }
        
        StringBuilder response = new StringBuilder();
        response.append("*").append(result.size()).append("\r\n");
        for (String item : result) {
            response.append("$").append(item.length()).append("\r\n");
            response.append(item).append("\r\n");
        }
        
        return response.toString();
    }
    
    private String handleConfigSet(List<String> args) {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'config set' command\r\n";
        }
        
        String key = args.get(0);
        String value = args.get(1);
        
        // 模拟设置配置（实际不生效）
        configSettings.put(key, value);
        return "+OK\r\n";
    }
    
    // HyperLogLog命令实现（模拟）
    private String handlePfAdd(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'pfadd' command\r\n";
        }
        // 模拟实现，总是返回1表示添加了新元素
        return ":1\r\n";
    }
    
    private String handlePfCount(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'pfcount' command\r\n";
        }
        // 模拟返回一个基数估计值
        return ":42\r\n";
    }
    
    private String handlePfMerge(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'pfmerge' command\r\n";
        }
        return "+OK\r\n";
    }
    
    // 地理位置命令实现（模拟）
    private String handleGeoAdd(List<String> args) {
        if (args.size() < 4 || (args.size() - 1) % 3 != 0) {
            return "-ERR wrong number of arguments for 'geoadd' command\r\n";
        }
        // 模拟添加地理位置数据
        int added = (args.size() - 1) / 3;
        return ":" + added + "\r\n";
    }
    
    private String handleGeoPos(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'geopos' command\r\n";
        }
        // 模拟返回位置坐标
        StringBuilder response = new StringBuilder();
        response.append("*").append(args.size() - 1).append("\r\n");
        for (int i = 1; i < args.size(); i++) {
            response.append("*2\r\n");
            response.append("$9\r\n116.40387\r\n"); // 模拟经度
            response.append("$8\r\n39.91488\r\n");  // 模拟纬度
        }
        return response.toString();
    }
    
    private String handleGeoDist(List<String> args) {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'geodist' command\r\n";
        }
        // 模拟返回距离
        return "$7\r\n1000.50\r\n";
    }
    
    private String handleGeoRadius(List<String> args) {
        if (args.size() < 4) {
            return "-ERR wrong number of arguments for 'georadius' command\r\n";
        }
        // 模拟返回范围内的成员
        return "*1\r\n$7\r\nmember1\r\n";
    }
    
    private String handleGeoRadiusByMember(List<String> args) {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'georadiusbymember' command\r\n";
        }
        // 模拟返回范围内的成员
        return "*1\r\n$7\r\nmember1\r\n";
    }
    
    // 流命令实现（模拟）
    private String handleXAdd(List<String> args) {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'xadd' command\r\n";
        }
        // 模拟生成流ID
        long timestamp = System.currentTimeMillis();
        return "$" + String.valueOf(timestamp).length() + "-0\r\n" + timestamp + "-0\r\n";
    }
    
    private String handleXRange(List<String> args) {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'xrange' command\r\n";
        }
        // 模拟返回流条目
        return "*0\r\n"; // 空结果
    }
    
    private String handleXRead(List<String> args) {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'xread' command\r\n";
        }
        // 模拟返回流数据
        return "*0\r\n"; // 空结果
    }
    
    private String handleXGroup(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'xgroup' command\r\n";
        }
        return "+OK\r\n";
    }
    
    private String handleXReadGroup(List<String> args) {
        if (args.size() < 5) {
            return "-ERR wrong number of arguments for 'xreadgroup' command\r\n";
        }
        // 模拟返回组读取结果
        return "*0\r\n"; // 空结果
    }
    
    // 位字段命令实现（模拟）
    private String handleBitfield(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'bitfield' command\r\n";
        }
        // 模拟返回位字段操作结果
        return "*1\r\n:0\r\n";
    }
    
    // Lua脚本命令实现
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
                return handleScriptFlush();
            default:
                return "-ERR Unknown SCRIPT subcommand '" + subCommand + "'\r\n";
        }
    }
    
    private String handleScriptLoad(List<String> args) {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'script load' command\r\n";
        }
        
        String script = args.get(0);
        String sha1 = generateSHA1(script);
        loadedScripts.put(sha1, script);
        
        return "$" + sha1.length() + "\r\n" + sha1 + "\r\n";
    }
    
    private String handleScriptExists(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'script exists' command\r\n";
        }
        
        StringBuilder response = new StringBuilder();
        response.append("*").append(args.size()).append("\r\n");
        
        for (String sha1 : args) {
            response.append(":").append(loadedScripts.containsKey(sha1) ? "1" : "0").append("\r\n");
        }
        
        return response.toString();
    }
    
    private String handleScriptFlush() {
        loadedScripts.clear();
        return "+OK\r\n";
    }
    
    private String handleEval(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'eval' command\r\n";
        }
        
        // 模拟Lua脚本执行错误
        return "-ERR Error running script (call to f_xxx): @user_script:1: Script execution not supported in JDBC adapter\r\n";
    }
    
    private String handleEvalSha(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'evalsha' command\r\n";
        }
        
        String sha1 = args.get(0);
        if (!loadedScripts.containsKey(sha1)) {
            return "-NOSCRIPT No matching script. Please use EVAL.\r\n";
        }
        
        // 模拟Lua脚本执行错误
        return "-ERR Error running script (call to f_" + sha1.substring(0, 8) + "): @user_script:1: Script execution not supported in JDBC adapter\r\n";
    }
    
    private String generateSHA1(String input) {
        // 简化的SHA1生成（实际应使用真正的SHA1算法）
        return String.format("%040x", input.hashCode() & 0xFFFFFFFFL);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof java.net.SocketException && 
            cause.getMessage().contains("Connection reset")) {
            // Log as debug instead of error for connection resets
            logger.debug("Client disconnected: {}", cause.getMessage());
        } else {
            logger.error("Exception in Redis protocol handler", cause);
        }
        ctx.close();
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Channel inactive: {}", ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Channel unregistered: {}", ctx.channel().remoteAddress());
        super.channelUnregistered(ctx);
    }
}