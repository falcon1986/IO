package com.wwq.nettyChatMessage;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 服务端
 * 涉及编解码
 * 涉及粘包拆包
 * 
 * @功能描述
 *       TODO
 * @作者 
 *       wwq
 * @创建时间 
 *       2019年12月26日 下午5:27:57
 */
public class ChatServer {
	
	public static void main(String[] args) {
		System.out.println("-------Server--------");
		//初始化线程组
		//bossGroup负责处理连接请求，真正处理交给workerGroup
		//含有的线程默认为cpu的两倍
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			//初始化启动器
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class) //使用NioServerSocketChannel进行通道实现
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sc) throws Exception {
						sc.pipeline().addLast(new MyMessageEncoder());//用于编码、粘包
						sc.pipeline().addLast(new MyMessageDecoder());//用户解码、拆包
						//心跳判断（自动释放已经断开的客户端）
						sc.pipeline().addLast(new IdleStateHandler(3, 0, 0, TimeUnit.SECONDS));
						sc.pipeline().addLast(new HeartBeatServerHandle());
						//业务处理
						sc.pipeline().addLast(new ChatServerHandle());
					}
				});
			
			ChannelFuture future = bootstrap.bind(8888).sync();
			//对管道关闭进行监听
			future.channel().closeFuture().sync();
	 	} catch(Exception e) {
	 		e.printStackTrace();
	 	} finally {
	 		bossGroup.shutdownGracefully();
	 		workerGroup.shutdownGracefully();
	 	}
	}
	
}