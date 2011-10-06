package com.asa.sms2honeycomb;

import java.util.Date;

public class MessageItem {
<<<<<<< HEAD

	String mAddress;
	String mTime;
	String mBody;
	String mRead;
	String mSmsId;
	String mSubject;
	String mThreadId;
	String mType;
	String mUsername;

	public MessageItem(String address, String time, String body, String read,
			String smsId, String subject, String threadId, String type,
			String username) {
		
		mAddress = address;
		mTime = time;
		mBody = body;
		mRead = read;
		mSmsId = smsId;
		mSubject = subject;
		mThreadId = threadId;
		mType = type;
		mUsername = username;
	}

	public String getAddress() {
		return mAddress;
=======
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
>>>>>>> origin/master
	}

	public String getMessageFrom() {
		return messageFrom;
	}

<<<<<<< HEAD
	public String getBody() {
		return mBody;
	}

	public String getRead() {
		return mRead;
	}

	public String getSmsId() {
		return mSmsId;
	}

	public String getSubject() {
		return mSubject;
	}

	public String getThreadId() {
		return mThreadId;
	}

	public String getType() {
		return mType;
	}

	public String getUsername() {
		return mUsername;
=======
	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
>>>>>>> origin/master
	}

	@Override
	public String toString() {
		return "MessageItem [messageTime=" + messageTime + ", messageTo="
				+ messageTo + ", messageFrom=" + messageFrom + ", messageBody="
				+ messageBody + "]";
	}

	
}
