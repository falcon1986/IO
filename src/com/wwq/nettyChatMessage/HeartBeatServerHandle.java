package com.wwq.nettyChatMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * ����������ж�
 * 
 * @��������
 *       TODO
 * @���� 
 *       wwq
 * @����ʱ�� 
 *       2019��12��27�� ����10:20:10
 */
public class HeartBeatServerHandle extends ChannelInboundHandlerAdapter{
	private static final String USER_NAME = "system";
	private static final String CONTENT = "HeartBeat Packet";
	
	private int readIdeaTime = 0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MyMessage imsg = (MyMessage) msg;
		System.out.println("���ݣ�" + imsg.getContent());
        if(CONTENT.equals(imsg.getContent())) {
			MyMessage msgBack = new MyMessage();
			msgBack.setUserName(USER_NAME);
			msgBack.setContent("ok");
			ctx.channel().writeAndFlush(msgBack);
			System.out.println("�յ�����ping");
		} else {
			super.channelRead(ctx, msg);
		}
	}

	/**
	 * IdleStateHandler�������¼�����������
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		IdleStateEvent event = (IdleStateEvent) evt;
		String eventType = null;
		switch(event.state()) {
			case READER_IDLE:
				eventType = "������";
				readIdeaTime++;
				break;
			case WRITER_IDLE:
				eventType = "д����";
				break;
			case ALL_IDLE:
				eventType = "��д����";
				break;
		}
		System.out.println("�����¼���" + eventType);
		
		if(readIdeaTime > 3) {
			System.out.println("�����г���3�Σ��ر����ӣ�");
			MyMessage msg = new MyMessage();
			msg.setUserName("system");
			msg.setContent("idle close");
			ctx.channel().writeAndFlush(msg);
			ctx.channel().close();
		}
	}
}
