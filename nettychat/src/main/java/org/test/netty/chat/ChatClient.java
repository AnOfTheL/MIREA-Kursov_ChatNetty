package org.test.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient {

    private final String host; // сервер, к которому подключаемся
    private final int port; // порт, по которому подключаемся

    public ChatClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new ChatClient("localhost",8765).run();
    }


    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(); // NIO поток

        try {
            // создаем элемент класса Bootstrap для настройки канала
            // задаем использование NIO сокетов (для реализации "общения" клиента с сервером)
            // и задаем управление классом ChatClientInitializer
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInitializer());

            // объявляем элемент класса Channel, используя bootstrap,
            // тем самым устанавливая соединение с сервером, передавая заданные значения переменных host и port
            Channel channel = bootstrap.connect(host, port).sync().channel();
            // объявляем элемент класса BufferedReader для чтения с консоли введенных пользователем данных
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                channel.writeAndFlush(in.readLine() + "\r\n");
            }
        }
        finally {
            // закрываем поток, если по какой-то причине выходим из цикла
            group.shutdownGracefully();
        }
    }
}
