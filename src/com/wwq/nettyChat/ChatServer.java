package com.wwq.nettyChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * �����
 * �漰�����
 * 
 * @��������
 *       TODO
 * @���� 
 *       wwq
 * @����ʱ�� 
 *       2019��12��26�� ����5:27:57
 */
public class ChatServer {
	
	public static void main(String[] args) {
		System.out.println("-------Server--------");
		//��ʼ���߳���
		//bossGroup������������������������workerGroup
		//���е��߳�Ĭ��Ϊcpu������
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			//��ʼ��������
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class) //ʹ��NioServerSocketChannel����ͨ��ʵ��
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sc) throws Exception {
						sc.pipeline().addLast(new StringEncoder());
						sc.pipeline().addLast(new StringDecoder());
						sc.pipeline().addLast(new ChatServerHandle());
					}
				});
			
			ChannelFuture future = bootstrap.bind(8888).sync();
			//�Թܵ��رս��м���
			future.channel().closeFuture().sync();
	 	} catch(Exception e) {
	 		e.printStackTrace();
	 	} finally {
	 		bossGroup.shutdownGracefully();
	 		workerGroup.shutdownGracefully();
	 	}
	}
	
}