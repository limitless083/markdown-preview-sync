package com.pingao.server;

import com.google.gson.Gson;
import com.pingao.model.WebSocketMsg;
import com.pingao.utils.HtmlUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


public class MarkDownServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkDownServer.class);
    private final ChannelGroup channelGroup =
        new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;
    private boolean isRunning;

    public ChannelFuture start(int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new MarkDownServerInitializer(this));
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(port));
        future.syncUninterruptibly();
        channel = future.channel();
        isRunning = true;
        LOGGER.info("Markdown server is running...");
        return future;
    }

    public void destroy() {
        channel.close();
        channelGroup.close();
        group.shutdownGracefully();
        LOGGER.info("Server shutdown success");
        System.exit(0);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void addChannel(Channel channel) {
        channelGroup.add(channel);
    }

    public void broadcast(String command, String path, String content, int bottom) {
        WebSocketMsg msg = new WebSocketMsg(command, path, HtmlUtils.markdown2Html(content, bottom));
        channelGroup.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(msg)));
        LOGGER.info("Broadcast WebSocket msg {} success", msg);
    }

    public static void main(String[] args) {
        final MarkDownServer endpoint = new MarkDownServer();
        endpoint.start(7788);
        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::destroy));
    }
}
