package com.wwq.nettyChatMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatClientHandle extends SimpleChannelInboundHandler<MyMessage> {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i=0; i<=5; i++) {
			MyMessage message = new MyMessage();
			message.setUserName("张三");
			message.setContent("你好，我来了！");
			ctx.writeAndFlush(message);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) throws Exception {
		System.out.println("[" + msg.getUserName() + "]" + msg.getContent());
	}
}