package com.wwq.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AioClient {

	public static void main(String[] args) throws Exception {
		AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
		channel.connect(new InetSocketAddress("127.0.0.1", 8888)).get();
		
		channel.write(ByteBuffer.wrap("helloServer!".getBytes()));
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int len = channel.read(buffer).get();
		if(len > 0) {
			System.out.println("服务端接收到信息：" + new String(buffer.array(), 0, len));
		}
	}
}