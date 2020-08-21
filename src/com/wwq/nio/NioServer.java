package com.wwq.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {
	
	public static void main(String[] args) {
		System.out.println("-------Server--------");
		try {
			//开启一个通道
			ServerSocketChannel ssc = ServerSocketChannel.open();
			//设置成非阻塞
			ssc.configureBlocking(false);
			//绑定监听端口
			ssc.socket().bind(new InetSocketAddress(8888));
			//生成一个多路复用器
			Selector selector = Selector.open();
			//把ServerSocketChannel注册到selector上，并绑定SelectionKey（连接事件）
			//selector可以感知ServerSocketChannel变化
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			
			while(true) {
				System.out.println("...等待客户端连接");
				//监听客户端，阻塞方法
				selector.select();
				
				//收集所有channel事件，轮询进行处理
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()) {
					SelectionKey key = it.next();
					//删除本地轮询的key，防止重复获取
					it.remove();
					//处理
					handle(key);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("-------Server|End--------");
	}

	private static void handle(SelectionKey key) throws IOException {
		if(key.isAcceptable()) { //是否是连接事件
			System.out.println("...服务端进入客户端连接事件");
			//获取绑定的服务端channel
			ServerSocketChannel channel = (ServerSocketChannel) key.channel();
			//获取到客户端channel，accept是阻塞方法，但是只有在有连接时，才会执行这段代码，所以相当于不阻塞
			System.out.println("...服务端等待客户端发送数据");
			SocketChannel socketChannel = channel.accept();
			//设置成非阻塞
			socketChannel.configureBlocking(false);
			//把SocketChannel注册到selector上，并绑定SelectionKey（读事件）
			//selector可以感知ServerSocketChannel变化
			socketChannel.register(key.selector(), SelectionKey.OP_READ);
			System.out.println("...服务端进入客户端连接事件-结束");
		} else if(key.isReadable()) { //是否是读事件
			System.out.println("...服务端进入客户端写入事件|服务端读事件");
			SocketChannel socketChannel = (SocketChannel) key.channel();
			
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int len = socketChannel.read(buffer);
			if(len != -1) {
				System.out.println("...服务端接收到客户端的数据如下：");
				System.out.println(new String(buffer.array(), 0, len));
			}
			
			//往客户端写入数据
			buffer = ByteBuffer.wrap("hello back".getBytes());
			socketChannel.write(buffer);
			
			//设置下一次继续监听读、写事件
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			socketChannel.close();
			System.out.println("...服务端进入客户端写入事件|服务端读事件-结束");
		}
	}
}
