package com.sqladaptor;

import com.sqladaptor.protocol.RedisProtocolHandler;
import com.sqladaptor.database.DatabaseManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisToJdbcServer {
    private static final Logger logger = LoggerFactory.getLogger(RedisToJdbcServer.class);
    
    private final int port;
    private final DatabaseManager databaseManager;
    
    public RedisToJdbcServer(int port, DatabaseManager databaseManager) {
        this.port = port;
        this.databaseManager = databaseManager;
    }
    
    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new RedisProtocolHandler(databaseManager));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            ChannelFuture future = bootstrap.bind(port).sync();
            logger.info("Redis to JDBC Adapter Server started on port {}", port);
            
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            RedisToJdbcServer server = new RedisToJdbcServer(6379, dbManager);
            server.start();
        } catch (Exception e) {
            logger.error("Failed to start server", e);
        }
    }
}