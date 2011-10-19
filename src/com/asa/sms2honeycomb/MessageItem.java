package com.asa.sms2honeycomb;

public class MessageItem {

	String messageAddress;
	String messageTime;
	String messageBody;
	int messageRead;
	int messageSmsId;
	String messageSubject;
	int messageThreadId;
	int messageType;
	String messageUsername;

	public MessageItem(String address, String time, String body, int read,
			int smsId, String subject, int threadId, int type,
			String username) {
		
		messageAddress = address;
		messageTime = time;
		messageBody = body;
		messageRead = read;
		messageSmsId = smsId;
		messageSubject = subject;
		messageThreadId = threadId;
		messageType = type;
		messageUsername = username;
	}

	public MessageItem(){
		
	}
	
	public String getMessageAddress() {
		return messageAddress;
	}

	public void setMessageAddress(String messageAddress) {
		this.messageAddress = messageAddress;
	}

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public int getMessageRead() {
		return messageRead;
	}

	public void setMessageRead(int messageRead) {
		this.messageRead = messageRead;
	}

	public int getMessageSmsId() {
		return messageSmsId;
	}

	public void setMessageSmsId(int messageSmsId) {
		this.messageSmsId = messageSmsId;
	}

	public String getMessageSubject() {
		return messageSubject;
	}

	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}

	public int getMessageThreadId() {
		return messageThreadId;
	}

	public void setMessageThreadId(int messageThreadId) {
		this.messageThreadId = messageThreadId;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getMessageUsername() {
		return messageUsername;
	}

	public void setMessageUsername(String messageUsername) {
		this.messageUsername = messageUsername;
	}

	@Override
	public String toString() {
		return "MessageItem [messageAddress=" + messageAddress
				+ ", messageTime=" + messageTime + ", messageBody="
				+ messageBody + ", messageRead=" + messageRead
				+ ", messageSmsId=" + messageSmsId + ", messageSubject="
				+ messageSubject + ", messageThreadId=" + messageThreadId
				+ ", messageType=" + messageType + ", messageUsername="
				+ messageUsername + "]";
	}

}
