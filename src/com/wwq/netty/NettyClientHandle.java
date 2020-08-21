package com.wwq.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandle extends ChannelInboundHandlerAdapter {

	/**
	 * �ص�-���ͷ�������ӳɹ�����
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf buf = Unpooled.copiedBuffer("helloServer!".getBytes(CharsetUtil.UTF_8));
		ctx.writeAndFlush(buf);
	}
	
	/**
	 * �ص�-��ȡ����˷��͵�����
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("�ͻ��˶�ȡ�̣߳�" + Thread.currentThread().getId());
		
		ByteBuf buffer = (ByteBuf)msg;
		System.out.println("��ȡ������˵����ݣ�" + buffer.toString(CharsetUtil.UTF_8));
	}
	
	/**
	 * �ص�-�쳣ʱ����
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}