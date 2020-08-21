package com.wwq.nettyChat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandle extends SimpleChannelInboundHandler<String> {
	
	private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String message = "客户端上线";
		System.out.println(message);
		channelGroup.writeAndFlush(message);
		channelGroup.add(ctx.channel());
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		channelGroup.remove(ctx.channel());
		String message = "客户端下线";
		channelGroup.writeAndFlush(message);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		channelGroup.writeAndFlush(msg);
	}
}