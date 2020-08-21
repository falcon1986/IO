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
				//�����д���д�������Ŀͻ��˻����Ӳ���
				channel.accept(attachment, this);
				
				//ֱ�Ӷ�ȡ�ͻ������ݣ��첽��ȡ����������ͨ���¼�ģ��֪ͨ��ȡ���
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				socket.read(buffer, attachment, new CompletionHandler<Integer, Object>() {

					@Override
					public void completed(Integer result, Object attachment) {						
						buffer.flip();
						
						//���ӳɹ������ͻ��˷�����Ϣ
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
