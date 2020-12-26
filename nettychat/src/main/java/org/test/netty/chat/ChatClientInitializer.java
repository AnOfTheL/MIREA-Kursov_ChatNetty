package org.test.netty.chat;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatClientInitializer extends io.netty.channel.ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // создаем элемент класса ChannelPipeline, с помощью которого можно точно описать способ организации соединения
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // для обмена строками между клиентом и сервером
        pipeline.addLast("decoder", new StringDecoder()); // декодирование байов в String
        pipeline.addLast("encoder", new StringEncoder()); // кодирование String в байты

        // для управления всеми декодированными строками от сервера
        pipeline.addLast("handler", new ChatClientHandler());
    }
}
