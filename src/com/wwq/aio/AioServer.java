package com.wwq.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AioServer {

	public static void main(String[] args) throws IOException, InterruptedException {
		AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel.open()
				.bind(new InetSocketAddress(8888));
		
		channel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

			@Override
			public void completed(AsynchronousSocketChannel socket, Object attachment) {
				//如果不写这行代码后续的客户端会连接不上
				channel.accept(attachment, this);
				
				//直接读取客户端数据，异步读取，不阻塞，通过事件模型通知读取完成
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				socket.read(buffer, attachment, new CompletionHandler<Integer, Object>() {

					@Override
					public void completed(Integer result, Object attachment) {						
						buffer.flip();
						
						//连接成功！给客户端返回信息
						socket.write(ByteBuffer.wrap("connected successfully!hello client".getBytes()));
					}

					@Override
					public void failed(Throwable exc, Object attachment) {
					}
				});
			}

			@Override
			public void failed(Throwable exc, Object attachment) {
				
			}
		});
		
		Thread.sleep(Integer.MAX_VALUE);
	}
}
