package com.asa.sms2honeycomb;

public class MessageItem {

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
	}

	public String getTime() {
		return mTime;
	}

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
	}
}
