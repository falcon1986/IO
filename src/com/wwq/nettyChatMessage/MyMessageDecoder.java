package com.wwq.nettyChatMessage;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

public class MyMessageDecoder extends MessageToMessageDecoder<ByteBuf> {
	
	private Charset charset = CharsetUtil.UTF_8;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		while(msg.readableBytes() > 8) { //判断前两个长度是否完整
			int userNameLen = msg.getInt(0);
			int contentLen = msg.getInt(4);
			int size = 8 + userNameLen + contentLen;
			if(msg.readableBytes() >= size) { //判断内容长度是否完整
				MyMessage en = new MyMessage();
				en.setUserNameLen(msg.readInt());
				en.setContentLen(msg.readInt());
				
				byte[] userNameBytes = new byte[en.getUserNameLen()];
				msg.readBytes(userNameBytes, 0, en.getUserNameLen());
				en.setUserName(new String(userNameBytes, charset));
				
				byte[] contentBytes = new byte[en.getContentLen()];
				msg.readBytes(contentBytes, 0, en.getContentLen());
				en.setContent(new String(contentBytes, charset));
				
				out.add(en);
			}
		}
		
	}

}