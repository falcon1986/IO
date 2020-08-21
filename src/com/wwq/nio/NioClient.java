package com.wwq.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;

public class NioClient {

	public static void main(String[] args) throws IOException {
		System.out.println("-------Client--------");
		SocketChannel socket = null;
		try {
			socket = SocketChannel.open();
			socket.configureBlocking(false);
			
			Selector selector = Selector.open();
			
			socket.connect(new InetSocketAddress("127.0.0.1", 8888));
			
			socket.register(selector, SelectionKey.OP_CONNECT);
			
			while(true) {
				selector.select();
				
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()) {
					SelectionKey key = it.next();
					
					it.remove();
					handle(key);
				}
			}	
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(socket != null) {
				socket.close();
			}
		}
		System.out.println("-------Client|End--------");
	}
	
	private static void handle(SelectionKey key) throws IOException {
		if(key.isConnectable()) { //是否是连接成功事件
			System.out.println("...客户端连接事件");
			//获取绑定的服务端channel
			SocketChannel channel = (SocketChannel) key.channel();
			//如果正在连接，就完成连接
			if(channel.isConnectionPending()) {
				channel.finishConnect();
			}
			System.out.println("...发送数据给服务端");
			//设置成非阻塞
			channel.configureBlocking(false);
			//把SocketChannel注册到selector上，并绑定SelectionKey（读事件）
			//selector可以感知ServerSocketChannel变化
			channel.register(key.selector(), SelectionKey.OP_READ);
			
			String content = new Random().nextInt() + "";
			ByteBuffer buffer = ByteBuffer.wrap(content.getBytes());
			channel.write(buffer);
			
			System.out.println("...客户端连接事件-结束");
		} else if(key.isReadable()) { //是否是读事件
			System.out.println("...客户端写入事件|服务端读事件");
			SocketChannel socketChannel = (SocketChannel) key.channel();
			
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int len = socketChannel.read(buffer);
			if(len != -1) {
				System.out.println("...接收到客户端的数据如下：");
				System.out.println(new String(buffer.array(), 0, len));
			}
			
			//往客户端写入数据
			buffer = ByteBuffer.wrap("client hello back".getBytes());
			socketChannel.write(buffer);
			
			//设置下一次继续监听读、写事件
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			socketChannel.close();
			System.out.println("...客户端写入事件|服务端读事件-结束");
		}
	}
}
