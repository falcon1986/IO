package com.wwq.nettyChatMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 服务端心跳判断
 * 
 * @功能描述
 *       TODO
 * @作者 
 *       wwq
 * @创建时间 
 *       2019年12月27日 上午10:20:10
 */
public class HeartBeatServerHandle extends ChannelInboundHandlerAdapter{
	private static final String USER_NAME = "system";
	private static final String CONTENT = "HeartBeat Packet";
	
	private int readIdeaTime = 0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MyMessage imsg = (MyMessage) msg;
		System.out.println("内容：" + imsg.getContent());
        if(CONTENT.equals(imsg.getContent())) {
			MyMessage msgBack = new MyMessage();
			msgBack.setUserName(USER_NAME);
			msgBack.setContent("ok");
			ctx.channel().writeAndFlush(msgBack);
			System.out.println("收到心跳ping");
		} else {
			super.channelRead(ctx, msg);
		}
	}

	/**
	 * IdleStateHandler触发此事件（心跳处理）
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		IdleStateEvent event = (IdleStateEvent) evt;
		String eventType = null;
		switch(event.state()) {
			case READER_IDLE:
				eventType = "读空闲";
				readIdeaTime++;
				break;
			case WRITER_IDLE:
				eventType = "写空闲";
				break;
			case ALL_IDLE:
				eventType = "读写空闲";
				break;
		}
		System.out.println("心跳事件：" + eventType);
		
		if(readIdeaTime > 3) {
			System.out.println("读空闲超过3次，关闭连接！");
			MyMessage msg = new MyMessage();
			msg.setUserName("system");
			msg.setContent("idle close");
			ctx.channel().writeAndFlush(msg);
			ctx.channel().close();
		}
	}
}
