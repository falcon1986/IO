package com.wwq.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyServerHandle extends ChannelInboundHandlerAdapter {

	/**
	 * �ص�-��ȡ�ͻ��˷��͵�����
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("��������ȡ�̣߳�" + Thread.currentThread().getId());
		
		ByteBuf buffer = (ByteBuf)msg;
		System.out.println("��ȡ���ͻ��˵����ݣ�" + buffer.toString(CharsetUtil.UTF_8));
	}
	
	/**
	 * �ص�-���ݶ�ȡ��Ϸ���
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ByteBuf buffer = Unpooled.copiedBuffer("hello Client".getBytes(CharsetUtil.UTF_8));
		ctx.writeAndFlush(buffer);
	}
}