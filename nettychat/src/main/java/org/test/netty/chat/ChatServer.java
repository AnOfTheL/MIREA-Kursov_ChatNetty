package org.test.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {

    public static void main(String[] args) throws InterruptedException {
        // используем порт для определения входящих подключений
        new ChatServer(8765).run();
    }

    private final int port;

    public ChatServer(int port){
        // порт для определения новых подключений
        this.port = port;
    }

    public void run() throws InterruptedException {
        // bossGroup принимает входящие соединения по мере их поступления и передает их на обработку в workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // используем ServerBootstrap, чтобы задать обрабаботку входящих соединений сервером
            ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChatServerInitializer());

            // используем bootstrap для привязки к указанному порту
            bootstrap.bind(port).sync().channel().closeFuture().sync();
        }
        finally {
            // очищаем bossGroup и workerGroup перед тем, как завершить run
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

