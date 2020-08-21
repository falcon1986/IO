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
			//����һ��ͨ��
			ServerSocketChannel ssc = ServerSocketChannel.open();
			//���óɷ�����
			ssc.configureBlocking(false);
			//�󶨼����˿�
			ssc.socket().bind(new InetSocketAddress(8888));
			//����һ����·������
			Selector selector = Selector.open();
			//��ServerSocketChannelע�ᵽselector�ϣ�����SelectionKey�������¼���
			//selector���Ը�֪ServerSocketChannel�仯
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			
			while(true) {
				System.out.println("...�ȴ��ͻ�������");
				//�����ͻ��ˣ���������
				selector.select();
				
				//�ռ�����channel�¼�����ѯ���д���
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()) {
					SelectionKey key = it.next();
					//ɾ��������ѯ��key����ֹ�ظ���ȡ
					it.remove();
					//����
					handle(key);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("-------Server|End--------");
	}

	private static void handle(SelectionKey key) throws IOException {
		if(key.isAcceptable()) { //�Ƿ��������¼�
			System.out.println("...����˽���ͻ��������¼�");
			//��ȡ�󶨵ķ����channel
			ServerSocketChannel channel = (ServerSocketChannel) key.channel();
			//��ȡ���ͻ���channel��accept����������������ֻ����������ʱ���Ż�ִ����δ��룬�����൱�ڲ�����
			System.out.println("...����˵ȴ��ͻ��˷�������");
			SocketChannel socketChannel = channel.accept();
			//���óɷ�����
			socketChannel.configureBlocking(false);
			//��SocketChannelע�ᵽselector�ϣ�����SelectionKey�����¼���
			//selector���Ը�֪ServerSocketChannel�仯
			socketChannel.register(key.selector(), SelectionKey.OP_READ);
			System.out.println("...����˽���ͻ��������¼�-����");
		} else if(key.isReadable()) { //�Ƿ��Ƕ��¼�
			System.out.println("...����˽���ͻ���д���¼�|����˶��¼�");
			SocketChannel socketChannel = (SocketChannel) key.channel();
			
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int len = socketChannel.read(buffer);
			if(len != -1) {
				System.out.println("...����˽��յ��ͻ��˵��������£�");
				System.out.println(new String(buffer.array(), 0, len));
			}
			
			//���ͻ���д������
			buffer = ByteBuffer.wrap("hello back".getBytes());
			socketChannel.write(buffer);
			
			//������һ�μ�����������д�¼�
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			socketChannel.close();
			System.out.println("...����˽���ͻ���д���¼�|����˶��¼�-����");
		}
	}
}
