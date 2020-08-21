package com.wwq.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	public static void main(String[] args) {
		System.out.println("-------Server--------");
		ServerSocket service = null;
		try {
			service = new ServerSocket(8888);
			
			while(true) {
				System.out.println("-------Server�ȴ��ͻ��˷�������--------");
				
				//��������
				Socket socket = service.accept();
				
				//���߳�ģʽ��������
				handle(socket);
				
				//���߳�ģʽ�޷���������
				//ȱ�㣺һ���ͻ������Ӷ�Ӧһ���̣߳��ͻ����������ܶ��ʱ��������Ҫ�����ܶ��̣߳����ǲ���ϵͳ�߳������޵ģ����Բ���������ǿ
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						handle(socket);
//					}
//				}).start();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(!service.isClosed()) {
				try {
					service.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("-------Server|End--------");
	}

	private static void handle(Socket socket) {
		System.out.println("-------Server���յ��ͻ�������--------");
		byte[] content = new byte[1024];
		try {
			//��������
			int endIndex = socket.getInputStream().read(content);
			
			if(endIndex > 0) {
				System.out.println(new String(content, 0, endIndex));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("-------Server���յ��ͻ������ݡ����--------");
	}
}
