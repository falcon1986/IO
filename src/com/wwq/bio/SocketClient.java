package com.wwq.bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class SocketClient {

	public static void main(String[] args) {
		System.out.println("-------Client--------");
		
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1", 8888);
			socket.setSoTimeout(5 * 1000);
			String content = new Random().nextInt() + "";
			socket.getOutputStream().write(content.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("-------Client|End--------");
	}
}
