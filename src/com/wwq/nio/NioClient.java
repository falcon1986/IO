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
		if(key.isConnectable()) { //�Ƿ������ӳɹ��¼�
			System.out.println("...�ͻ��������¼�");
			//��ȡ�󶨵ķ����channel
			SocketChannel channel = (SocketChannel) key.channel();
			//����������ӣ����������
			if(channel.isConnectionPending()) {
				channel.finishConnect();
			}
			System.out.println("...�������ݸ������");
			//���óɷ�����
			channel.configureBlocking(false);
			//��SocketChannelע�ᵽselector�ϣ�����SelectionKey�����¼���
			//selector���Ը�֪ServerSocketChannel�仯
			channel.register(key.selector(), SelectionKey.OP_READ);
			
			String content = new Random().nextInt() + "";
			ByteBuffer buffer = ByteBuffer.wrap(content.getBytes());
			channel.write(buffer);
			
			System.out.println("...�ͻ��������¼�-����");
		} else if(key.isReadable()) { //�Ƿ��Ƕ��¼�
			System.out.println("...�ͻ���д���¼�|����˶��¼�");
			SocketChannel socketChannel = (SocketChannel) key.channel();
			
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int len = socketChannel.read(buffer);
			if(len != -1) {
				System.out.println("...���յ��ͻ��˵��������£�");
				System.out.println(new String(buffer.array(), 0, len));
			}
			
			//���ͻ���д������
			buffer = ByteBuffer.wrap("client hello back".getBytes());
			socketChannel.write(buffer);
			
			//������һ�μ�����������д�¼�
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			socketChannel.close();
			System.out.println("...�ͻ���д���¼�|����˶��¼�-����");
		}
	}
}
