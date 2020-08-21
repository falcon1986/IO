package com.wwq.nettyChatMessage;

/**
 * 消息封包
 * 涉及粘包拆包
 * 通过记录长度实现
 * 
 * @功能描述
 *       TODO
 * @作者 
 *       wwq
 * @创建时间 
 *       2019年12月26日 下午5:29:43
 */
public class MyMessage {

	private int userNameLen;
	private int contentLen;
	private String userName;
	private String content;
	
	public int getUserNameLen() {
		return userNameLen;
	}
	public void setUserNameLen(int userNameLen) {
		this.userNameLen = userNameLen;
	}
	public int getContentLen() {
		return contentLen;
	}
	public void setContentLen(int contentLen) {
		this.contentLen = contentLen;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
