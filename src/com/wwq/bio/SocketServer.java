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
				System.out.println("-------Server等待客户端发送数据--------");
				
				//方法阻塞
				Socket socket = service.accept();
				
				//单线程模式阻塞处理
				handle(socket);
				
				//多线程模式修非阻塞处理
				//缺点：一个客户端连接对应一个线程，客户端连接数很多的时候服务端需要开启很多线程，但是操作系统线程是有限的，所以并发能力不强
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
		System.out.println("-------Server接收到客户端数据--------");
		byte[] content = new byte[1024];
		try {
			//方法阻塞
			int endIndex = socket.getInputStream().read(content);
			
			if(endIndex > 0) {
				System.out.println(new String(content, 0, endIndex));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("-------Server接收到客户端数据》完成--------");
	}
}
