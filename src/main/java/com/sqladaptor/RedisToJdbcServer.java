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
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;
    
    public RedisToJdbcServer(int port, DatabaseManager databaseManager) {
        this.port = port;
        this.databaseManager = databaseManager;
    }
    
    public void start() throws InterruptedException {
        logger.info("正在启动Redis到JDBC适配器服务器，端口: {}", port);
        
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        
        logger.debug("已创建EventLoopGroup - Boss线程组: 1个线程, Worker线程组: {}个线程", 
                    Runtime.getRuntime().availableProcessors() * 2);
        
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            logger.debug("新客户端连接初始化: {}", ch.remoteAddress());
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new RedisProtocolHandler(databaseManager));
                            logger.debug("已为客户端 {} 添加RedisProtocolHandler", ch.remoteAddress());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            logger.info("服务器配置完成 - SO_BACKLOG: 128, SO_KEEPALIVE: true");
            
            ChannelFuture future = bootstrap.bind(port).sync();
            serverChannel = future.channel();
            logger.info("Redis到JDBC适配器服务器已成功启动，监听端口: {}", port);
            logger.info("服务器已准备好接受客户端连接...");
            
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("启动服务器时发生错误，端口: {}", port, e);
            throw e;
        } finally {
            stop();
        }
    }
    
    public void stop() {
        logger.info("正在关闭服务器资源...");
        
        if (serverChannel != null) {
            serverChannel.close();
        }
        
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        
        logger.info("服务器资源已释放");
    }
    
    public static void main(String[] args) {
        logger.info("=== Redis到JDBC适配器服务器启动 ===");
        try {
            logger.info("正在初始化数据库管理器...");
            DatabaseManager dbManager = new DatabaseManager();
            logger.info("数据库管理器初始化完成");
            
            RedisToJdbcServer server = new RedisToJdbcServer(6379, dbManager);
            logger.info("服务器实例创建完成，准备启动...");
            server.start();
        } catch (Exception e) {
            logger.error("服务器启动失败", e);
            System.exit(1);
        }
    }
}