package org.test.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final ChannelGroup channels;

    static {
        channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    @Override
    // вызывается тогда, когда присоединяется новый клиент
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " has joined.\n");
        }
        channels.add(ctx.channel());
    }

    @Override
    // вызывается тогда, когда клиент разрывает соединение
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " has left.\n");
        }
        channels.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String message) {
        // для определения того, кто отправляет сообщения
        Channel incoming = channelHandlerContext.channel();

        // отправляем сообщение всем остальным
        for (Channel channel : channels){
            if (channel != incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "] ("
                        + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
                        + "): " + message + "\n");
            }
        }
    }
}
