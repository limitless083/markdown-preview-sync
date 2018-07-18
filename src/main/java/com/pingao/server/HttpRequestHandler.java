package com.pingao.server;

import com.pingao.Main;
import com.pingao.utils.HtmlUtils;
import com.pingao.utils.MiMeType;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        String uri = request.uri();
        LOGGER.info("Request uri {}", uri);
        if (uri.startsWith("/index")) {
            index(ctx, request);
        } else if (uri.startsWith("/js") || uri.startsWith("/css")) {
            responseStaticFile(ctx, request);
        } else if (uri.startsWith("/ws")) {
            ctx.fireChannelRead(request.retain());
        } else {
            response(ctx, request, "How do you do", MiMeType.PLAIN);
        }
    }

    private void index(ChannelHandlerContext ctx, FullHttpRequest request) {
        response(ctx, request, HtmlUtils.buildIndexHtml(), MiMeType.HTML);
    }

    private void responseStaticFile(ChannelHandlerContext ctx, FullHttpRequest request) {
        String path = Main.ROOT_PATH + HtmlUtils.getRequestPath(request.uri());
        try {
            response(ctx, request, new String(Files.readAllBytes(Paths.get(path))),
                     HtmlUtils.getMiMeTypeOfStaticFile(path));
        } catch (IOException e) {
            error(ctx, HttpResponseStatus.NOT_FOUND);
        }
    }

    private void error(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
            HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void response(ChannelHandlerContext ctx, FullHttpRequest request, String content, MiMeType type) {
        if (HttpUtil.is100ContinueExpected(request)) {
            send100Continue(ctx);
        }
        FullHttpResponse response1 =
            new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
        response1.headers().set(HttpHeaderNames.CONTENT_TYPE, type.getType() + "; charset=UTF-8");
        response1.headers().set(HttpHeaderNames.CONTENT_LENGTH, response1.content().readableBytes());
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (keepAlive) {
            response1.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response1);
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
            HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
