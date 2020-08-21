package com.wwq.nettyChatMessage;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * �ͻ��˷�������ping
 * 
 * @��������
 *       TODO
 * @���� 
 *       wwq
 * @����ʱ�� 
 *       2019��12��27�� ����11:03:55
 */
public class HeartBeatClientHandle extends ChannelInboundHandlerAdapter {
	
	private static final String USER_NAME = "system";
	private static final String CONTENT = "HeartBeat Packet";

    private Random random = new Random();
    private int baseRandom = 2;

    private Channel channel = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.channel = ctx.channel();

        ctx.fireChannelActive();
        ping(ctx.channel());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void ping(Channel channel) {
        int second = Math.max(1, random.nextInt(baseRandom));
        System.out.println("next heart beat will send after " + second + "s.");
        ScheduledFuture<?> future = channel.eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                if (channel.isActive()) {
                    System.out.println("sending heart beat to the server...");
                    MyMessage msg = new MyMessage();
    				msg.setUserName(USER_NAME);
    				msg.setContent(CONTENT);
                    channel.writeAndFlush(msg);
                } else {
                    System.err.println("The connection had broken, cancel the task that will send a heart beat.");
                    channel.closeFuture();
                    throw new RuntimeException();
                }
            }
        }, second, TimeUnit.SECONDS);

        future.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if (future.isSuccess()) {
                    ping(channel);
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // ��Channel�Ѿ��Ͽ��������, ��Ȼ��������, �����쳣, �÷����ᱻ����.
        cause.printStackTrace();
        ctx.close();
    }
}
