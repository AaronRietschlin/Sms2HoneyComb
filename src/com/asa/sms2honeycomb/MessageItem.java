package com.asa.sms2honeycomb;

public class MessageItem {
	
	String mTime;
	String mTo;
	String mFrom;
	String mBody;
	
	public MessageItem(String time, String to, String from, String body) {
		// TODO Auto-generated constructor stub
		mTime = time;
		mTo = to;
		mFrom = from;
		mBody = body;
	}

	public String getTime() {
		return mTime;
	}

	public String getTo() {
		return mTo;
	}

	public String getFrom() {
		return mFrom;
	}

	public String getBody() {
		return mBody;
	}
}
