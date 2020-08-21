package com.wwq.nettyChatMessage;

import java.net.InetSocketAddress;
import java.util.Random;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatClient {

	public static void main(String[] args) {
		System.out.println("-------Client--------");
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			//��ʼ��������
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
				.channel(NioSocketChannel.class) //ʹ��NioSocketChannel����ͨ��ʵ��
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sc) throws Exception {
						sc.pipeline().addLast(new MyMessageEncoder());
						sc.pipeline().addLast(new MyMessageDecoder());
						sc.pipeline().addLast(new HeartBeatClientHandle());
						sc.pipeline().addLast(new ChatClientHandle());
					}
				});
			//�����ͻ������ӷ����
			ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8888)).sync();
			//��ȡ�ܵ�
			Channel channel = future.channel();
			
//			while(true) {
//				Scanner scanner = new Scanner(System.in);
//				while(scanner.hasNext()) {
//					channel.writeAndFlush(Unpooled.copiedBuffer(scanner.next().getBytes(CharsetUtil.UTF_8)));
//				}
//			}
			channel.closeFuture().sync();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}