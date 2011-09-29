package com.asa.sms2honeycomb;

import java.util.Date;

public class MessageItem {
	// TODO : Change to Date object
	String messageTime;
	String messageTo;
	String messageFrom;
	String messageBody;

	public MessageItem(String time, String to, String from, String body) {
		// TODO Auto-generated constructor stub
		messageTime = time;
		messageTo = to;
		messageFrom = from;
		messageBody = body;
	}

	public MessageItem() {
	}

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	public String getMessageTo() {
		return messageTo;
	}

	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo;
	}

	public String getMessageFrom() {
		return messageFrom;
	}

	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	@Override
	public String toString() {
		return "MessageItem [messageTime=" + messageTime + ", messageTo="
				+ messageTo + ", messageFrom=" + messageFrom + ", messageBody="
				+ messageBody + "]";
	}

	
}
