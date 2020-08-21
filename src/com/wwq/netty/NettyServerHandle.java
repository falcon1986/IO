package com.wwq.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyServerHandle extends ChannelInboundHandlerAdapter {

	/**
	 * 回调-读取客户端发送的数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("服务器读取线程：" + Thread.currentThread().getId());
		
		ByteBuf buffer = (ByteBuf)msg;
		System.out.println("读取到客户端的数据：" + buffer.toString(CharsetUtil.UTF_8));
	}
	
	/**
	 * 回调-数据读取完毕方法
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ByteBuf buffer = Unpooled.copiedBuffer("hello Client".getBytes(CharsetUtil.UTF_8));
		ctx.writeAndFlush(buffer);
	}
}