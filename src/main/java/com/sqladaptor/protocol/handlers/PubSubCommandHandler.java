package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Set;
import java.util.HashSet;

public class PubSubCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(PubSubCommandHandler.class);
    
    // 模拟发布订阅状态管理
    private final ConcurrentHashMap<String, Set<String>> channelSubscribers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> patternSubscribers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<String>> subscribedChannels = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<String>> subscribedPatterns = new ConcurrentHashMap<>();
    
    public PubSubCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }
    
    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();
        
        logger.info("[PUBSUB] Processing command: {} with {} args: {}", command, args.size(), args);
        
        switch (command) {
            case "PUBLISH":
                return handlePublish(args);
            case "SUBSCRIBE":
                return handleSubscribe(args);
            case "UNSUBSCRIBE":
                return handleUnsubscribe(args);
            case "PSUBSCRIBE":
                return handlePSubscribe(args);
            case "PUNSUBSCRIBE":
                return handlePUnsubscribe(args);
            case "PUBSUB":
                return handlePubSub(args);
            default:
                return "-ERR Unsupported pub/sub command: " + command + "\r\n";
        }
    }
    
    private String handlePublish(List<String> args) throws Exception {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'publish' command\r\n";
        }
        
        String channel = args.get(0);
        String message = args.get(1);
        
        logger.info("[PUBSUB] Publishing message to channel: {}, message: {}", channel, message);
        
        // 模拟发布消息，返回接收消息的订阅者数量
        Set<String> subscribers = channelSubscribers.getOrDefault(channel, new HashSet<>());
        int subscriberCount = subscribers.size();
        
        // 检查模式订阅
        for (String pattern : patternSubscribers.keySet()) {
            if (matchPattern(channel, pattern)) {
                subscriberCount += patternSubscribers.get(pattern).size();
            }
        }
        
        return ":" + subscriberCount + "\r\n";
    }
    
    private String handleSubscribe(List<String> args) throws Exception {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'subscribe' command\r\n";
        }
        
        StringBuilder response = new StringBuilder();
        String clientId = "client1"; // 模拟客户端ID
        
        for (String channel : args) {
            // 添加订阅关系
            channelSubscribers.computeIfAbsent(channel, k -> new HashSet<>()).add(clientId);
            subscribedChannels.computeIfAbsent(clientId, k -> new CopyOnWriteArrayList<>()).add(channel);
            
            // 构建响应
            response.append("*3\r\n");
            response.append("$9\r\nsubscribe\r\n");
            response.append("$").append(channel.length()).append("\r\n").append(channel).append("\r\n");
            response.append(":").append(subscribedChannels.get(clientId).size()).append("\r\n");
            
            logger.info("[PUBSUB] Client {} subscribed to channel: {}", clientId, channel);
        }
        
        return response.toString();
    }
    
    private String handleUnsubscribe(List<String> args) throws Exception {
        StringBuilder response = new StringBuilder();
        String clientId = "client1"; // 模拟客户端ID
        
        if (args.isEmpty()) {
            // 取消所有订阅
            List<String> channels = subscribedChannels.getOrDefault(clientId, new CopyOnWriteArrayList<>());
            for (String channel : channels) {
                Set<String> subscribers = channelSubscribers.get(channel);
                if (subscribers != null) {
                    subscribers.remove(clientId);
                    if (subscribers.isEmpty()) {
                        channelSubscribers.remove(channel);
                    }
                }
                
                response.append("*3\r\n");
                response.append("$11\r\nunsubscribe\r\n");
                response.append("$").append(channel.length()).append("\r\n").append(channel).append("\r\n");
                response.append(":0\r\n");
            }
            subscribedChannels.remove(clientId);
        } else {
            for (String channel : args) {
                Set<String> subscribers = channelSubscribers.get(channel);
                if (subscribers != null) {
                    subscribers.remove(clientId);
                    if (subscribers.isEmpty()) {
                        channelSubscribers.remove(channel);
                    }
                }
                
                List<String> clientChannels = subscribedChannels.get(clientId);
                if (clientChannels != null) {
                    clientChannels.remove(channel);
                }
                
                response.append("*3\r\n");
                response.append("$11\r\nunsubscribe\r\n");
                response.append("$").append(channel.length()).append("\r\n").append(channel).append("\r\n");
                int remainingCount = clientChannels != null ? clientChannels.size() : 0;
                response.append(":").append(remainingCount).append("\r\n");
                
                logger.info("[PUBSUB] Client {} unsubscribed from channel: {}", clientId, channel);
            }
        }
        
        return response.toString();
    }
    
    private String handlePSubscribe(List<String> args) throws Exception {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'psubscribe' command\r\n";
        }
        
        StringBuilder response = new StringBuilder();
        String clientId = "client1"; // 模拟客户端ID
        
        for (String pattern : args) {
            // 添加模式订阅关系
            patternSubscribers.computeIfAbsent(pattern, k -> new HashSet<>()).add(clientId);
            subscribedPatterns.computeIfAbsent(clientId, k -> new CopyOnWriteArrayList<>()).add(pattern);
            
            // 构建响应
            response.append("*3\r\n");
            response.append("$10\r\npsubscribe\r\n");
            response.append("$").append(pattern.length()).append("\r\n").append(pattern).append("\r\n");
            response.append(":").append(subscribedPatterns.get(clientId).size()).append("\r\n");
            
            logger.info("[PUBSUB] Client {} subscribed to pattern: {}", clientId, pattern);
        }
        
        return response.toString();
    }
    
    private String handlePUnsubscribe(List<String> args) throws Exception {
        StringBuilder response = new StringBuilder();
        String clientId = "client1"; // 模拟客户端ID
        
        if (args.isEmpty()) {
            // 取消所有模式订阅
            List<String> patterns = subscribedPatterns.getOrDefault(clientId, new CopyOnWriteArrayList<>());
            for (String pattern : patterns) {
                Set<String> subscribers = patternSubscribers.get(pattern);
                if (subscribers != null) {
                    subscribers.remove(clientId);
                    if (subscribers.isEmpty()) {
                        patternSubscribers.remove(pattern);
                    }
                }
                
                response.append("*3\r\n");
                response.append("$12\r\npunsubscribe\r\n");
                response.append("$").append(pattern.length()).append("\r\n").append(pattern).append("\r\n");
                response.append(":0\r\n");
            }
            subscribedPatterns.remove(clientId);
        } else {
            for (String pattern : args) {
                Set<String> subscribers = patternSubscribers.get(pattern);
                if (subscribers != null) {
                    subscribers.remove(clientId);
                    if (subscribers.isEmpty()) {
                        patternSubscribers.remove(pattern);
                    }
                }
                
                List<String> clientPatterns = subscribedPatterns.get(clientId);
                if (clientPatterns != null) {
                    clientPatterns.remove(pattern);
                }
                
                response.append("*3\r\n");
                response.append("$12\r\npunsubscribe\r\n");
                response.append("$").append(pattern.length()).append("\r\n").append(pattern).append("\r\n");
                int remainingCount = clientPatterns != null ? clientPatterns.size() : 0;
                response.append(":").append(remainingCount).append("\r\n");
                
                logger.info("[PUBSUB] Client {} unsubscribed from pattern: {}", clientId, pattern);
            }
        }
        
        return response.toString();
    }
    
    private String handlePubSub(List<String> args) throws Exception {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'pubsub' command\r\n";
        }
        
        String subCommand = args.get(0).toUpperCase();
        
        switch (subCommand) {
            case "CHANNELS":
                return handlePubSubChannels(args.subList(1, args.size()));
            case "NUMSUB":
                return handlePubSubNumSub(args.subList(1, args.size()));
            case "NUMPAT":
                return handlePubSubNumPat();
            default:
                return "-ERR Unknown PUBSUB subcommand or wrong number of arguments for '" + subCommand + "'\r\n";
        }
    }
    
    private String handlePubSubChannels(List<String> args) throws Exception {
        String pattern = args.isEmpty() ? "*" : args.get(0);
        
        StringBuilder response = new StringBuilder();
        List<String> matchingChannels = new CopyOnWriteArrayList<>();
        
        for (String channel : channelSubscribers.keySet()) {
            if (matchPattern(channel, pattern)) {
                matchingChannels.add(channel);
            }
        }
        
        response.append("*").append(matchingChannels.size()).append("\r\n");
        for (String channel : matchingChannels) {
            response.append("$").append(channel.length()).append("\r\n").append(channel).append("\r\n");
        }
        
        return response.toString();
    }
    
    private String handlePubSubNumSub(List<String> args) throws Exception {
        StringBuilder response = new StringBuilder();
        response.append("*").append(args.size() * 2).append("\r\n");
        
        for (String channel : args) {
            response.append("$").append(channel.length()).append("\r\n").append(channel).append("\r\n");
            int subscriberCount = channelSubscribers.getOrDefault(channel, new HashSet<>()).size();
            response.append(":").append(subscriberCount).append("\r\n");
        }
        
        return response.toString();
    }
    
    private String handlePubSubNumPat() throws Exception {
        int patternCount = patternSubscribers.size();
        return ":" + patternCount + "\r\n";
    }
    
    // 简单的模式匹配实现（支持 * 和 ? 通配符）
    private boolean matchPattern(String text, String pattern) {
        if (pattern.equals("*")) {
            return true;
        }
        
        // 简化的模式匹配，实际实现可能需要更复杂的逻辑
        return text.matches(pattern.replace("*", ".*").replace("?", "."));
    }
}