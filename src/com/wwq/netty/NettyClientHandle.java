package com.wwq.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandle extends ChannelInboundHandlerAdapter {

	/**
	 * 回调-当和服务端连接成功触发
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf buf = Unpooled.copiedBuffer("helloServer!".getBytes(CharsetUtil.UTF_8));
		ctx.writeAndFlush(buf);
	}
	
	/**
	 * 回调-读取服务端发送的数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("客户端读取线程：" + Thread.currentThread().getId());
		
		ByteBuf buffer = (ByteBuf)msg;
		System.out.println("读取到服务端的数据：" + buffer.toString(CharsetUtil.UTF_8));
	}
	
	/**
	 * 回调-异常时调用
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}