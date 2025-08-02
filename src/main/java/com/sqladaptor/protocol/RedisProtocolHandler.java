package com.sqladaptor.protocol;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import com.sqladaptor.parser.RedisCommandParser;
import com.sqladaptor.protocol.handlers.ConnectionCommandHandler;
import com.sqladaptor.protocol.handlers.StringCommandHandler;
import com.sqladaptor.protocol.handlers.HashCommandHandler;
import com.sqladaptor.protocol.handlers.ListCommandHandler;
import com.sqladaptor.protocol.handlers.KeyCommandHandler;
import com.sqladaptor.protocol.handlers.SetCommandHandler;
import com.sqladaptor.protocol.handlers.SortedSetCommandHandler;
import com.sqladaptor.protocol.handlers.ServerCommandHandler;
import com.sqladaptor.protocol.handlers.PubSubCommandHandler;
import com.sqladaptor.protocol.handlers.TransactionCommandHandler;
import com.sqladaptor.protocol.handlers.ScriptCommandHandler;
import com.sqladaptor.protocol.handlers.GeoCommandHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RedisProtocolHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RedisProtocolHandler.class);

    private final DatabaseManager databaseManager;
    private final RedisToSqlConverter converter;
    private final RedisCommandParser commandParser;
    private final ConnectionCommandHandler connectionCommandHandler;
    private final StringCommandHandler stringCommandHandler;
    private final HashCommandHandler hashCommandHandler;
    private final ListCommandHandler listCommandHandler;
    private final KeyCommandHandler keyCommandHandler;
    private final SetCommandHandler setCommandHandler;
    private final SortedSetCommandHandler sortedSetCommandHandler;
    private final ServerCommandHandler serverCommandHandler;
    private final PubSubCommandHandler pubSubCommandHandler;
    private final TransactionCommandHandler transactionCommandHandler;
    private final ScriptCommandHandler scriptCommandHandler;
    private final GeoCommandHandler geoCommandHandler;
    private static final AtomicInteger activeConnections = new AtomicInteger(0);
    private static final AtomicLong totalConnections = new AtomicLong(0);

    private final Map<String, String> configSettings = new HashMap<>();
    private final Map<String, String> loadedScripts = new HashMap<>();

    public RedisProtocolHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.converter = new RedisToSqlConverter();
        this.commandParser = new RedisCommandParser();
        this.connectionCommandHandler = new ConnectionCommandHandler(databaseManager, converter);
        this.stringCommandHandler = new StringCommandHandler(databaseManager, converter);
        this.hashCommandHandler = new HashCommandHandler(databaseManager, converter);
        this.listCommandHandler = new ListCommandHandler(databaseManager, converter);
        this.keyCommandHandler = new KeyCommandHandler(databaseManager, converter);
        this.setCommandHandler = new SetCommandHandler(databaseManager, converter);
        this.sortedSetCommandHandler = new SortedSetCommandHandler(databaseManager, converter);
        this.serverCommandHandler = new ServerCommandHandler(databaseManager, converter);
        this.pubSubCommandHandler = new PubSubCommandHandler(databaseManager, converter);
        this.transactionCommandHandler = new TransactionCommandHandler(databaseManager, converter);
        this.scriptCommandHandler = new ScriptCommandHandler(databaseManager, converter);
        this.geoCommandHandler = new GeoCommandHandler(databaseManager, converter);

        // Initialize default config
        initializeDefaultConfig();
    }

    private void initializeDefaultConfig() {
        configSettings.put("timeout", "0");
        configSettings.put("databases", "16");
        configSettings.put("maxmemory", "0");
        configSettings.put("maxmemory-policy", "noeviction");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        serverCommandHandler.incrementActiveConnections();
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Channel已注册: {}", ctx.channel().remoteAddress());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        String clientAddress = ctx.channel().remoteAddress().toString();
        logger.debug("Channel已注销: {}", clientAddress);
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            String rawContent = buf.toString(StandardCharsets.UTF_8);
            logger.info("[DEBUG] Raw message length: {}, content: [{}]", rawContent.length(),
                    rawContent.replace("\\r", "\\\\r").replace("\\n", "\\\\n"));

            // 检查是否包含PING
            if (rawContent.toUpperCase().contains("PING")) {
                logger.warn("[ALERT] PING command detected in raw message!");
            }
        }

        // 添加空值检查
        if (ctx == null) {
            logger.error("ChannelHandlerContext为null");
            return;
        }

        io.netty.channel.Channel channel = ctx.channel();
        if (channel == null) {
            logger.error("从上下文获取的Channel为null");
            return;
        }

        String clientAddress = (channel.remoteAddress() != null) ? channel.remoteAddress().toString() : "未知客户端";

        if (!channel.isActive()) {
            logger.warn("Channel不活跃，忽略消息 - 客户端: {}", clientAddress);
            return;
        }

        ByteBuf buf = (ByteBuf) msg;
        try {
            String request = buf.toString(CharsetUtil.UTF_8);
            logger.debug("收到来自 {} 的Redis命令: {}", clientAddress, request.trim());

            RedisCommandNode commandNode = commandParser.parse(request);
            logger.debug("命令解析完成 - 客户端: {}, 命令: {}, 参数数量: {}",
                    clientAddress, commandNode.getCommand(), commandNode.getArguments().size());

            String response = processCommand(ctx, commandNode);

            // Check if channel is still active before writing response
            if (ctx.channel() != null && ctx.channel().isActive()) {
                ByteBuf responseBuf = Unpooled.copiedBuffer(response, CharsetUtil.UTF_8);
                ctx.writeAndFlush(responseBuf);
                logger.debug("响应已发送给客户端: {}", clientAddress);
            } else {
                logger.warn("Channel上下文无效或不活跃，跳过响应发送 - 客户端: {}", clientAddress);
            }
        } catch (Exception e) {
            logger.error("处理来自 {} 的Redis命令时发生错误", clientAddress, e);
            if (ctx.channel().isActive()) {
                String errorResponse = "-ERR " + e.getMessage() + "\r\n";
                ByteBuf errorBuf = Unpooled.copiedBuffer(errorResponse, CharsetUtil.UTF_8);
                ctx.writeAndFlush(errorBuf);
                logger.debug("错误响应已发送给客户端: {}", clientAddress);
            }
        } finally {
            buf.release();
        }
    }

    private String processCommand(ChannelHandlerContext ctx, RedisCommandNode commandNode) throws Exception {
        long startTime = System.currentTimeMillis();
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();

        logger.debug("Processing command: {} with args: {}", command, args);
        logger.debug("Command type: {}", commandNode.getType());

        switch (commandNode.getType()) {
            case CONNECTION:
                return connectionCommandHandler.handleCommand(ctx, commandNode);
            case STRING:
                return stringCommandHandler.handle(commandNode);
            case HASH:
                return hashCommandHandler.handle(commandNode);
            case LIST:
                return listCommandHandler.handle(commandNode);
            case SET:
                return setCommandHandler.handle(commandNode);
            case SORTED_SET:
                return sortedSetCommandHandler.handle(commandNode);
            case KEY:
                return keyCommandHandler.handle(commandNode);
            case SERVER:
                return serverCommandHandler.handle(commandNode);
            case PUBSUB:
                return pubSubCommandHandler.handle(commandNode);
            case TRANSACTION:
                return transactionCommandHandler.handle(commandNode);
            case SCRIPT:
                return scriptCommandHandler.handle(commandNode);
            case GEO:
                return geoCommandHandler.handle(commandNode);
            default:
                return "-ERR Unsupported command type\r\n";
        }
    }
}
