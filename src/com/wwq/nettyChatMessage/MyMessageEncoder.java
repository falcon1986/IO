package com.wwq.nettyChatMessage;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class MyMessageEncoder extends MessageToByteEncoder<MyMessage> {
	
	private Charset charset = CharsetUtil.UTF_8;

	@Override
	protected void encode(ChannelHandlerContext ctx, MyMessage msg, ByteBuf out) throws Exception {
		byte[] userNameBytes = msg.getUserName().getBytes(charset);
		byte[] contentBytes = msg.getContent().getBytes(charset);
		
		out.writeInt(userNameBytes.length);
		out.writeInt(contentBytes.length);
		out.writeBytes(userNameBytes);
		out.writeBytes(contentBytes);
	}
}
