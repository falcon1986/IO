package com.wwq.nettyChatMessage;

/**
 * ��Ϣ���
 * �漰ճ�����
 * ͨ����¼����ʵ��
 * 
 * @��������
 *       TODO
 * @���� 
 *       wwq
 * @����ʱ�� 
 *       2019��12��26�� ����5:29:43
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
