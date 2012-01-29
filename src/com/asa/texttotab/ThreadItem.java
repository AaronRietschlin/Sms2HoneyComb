package com.asa.texttotab;

public class ThreadItem {

	String mHasAttachment;
	String mMessageCount;
	String mRead;
	String mThreadId;
	String mUsername;

	public ThreadItem(String hasAttachment, String messageCount, String read,
			String threadId, String username) {

		mHasAttachment = hasAttachment;
		mMessageCount = messageCount;
		mRead = read;
		mThreadId = threadId;
		mUsername = username;
	}

	public String getHasAttachment() {
		return mHasAttachment;
	}

	public String getMessageCount() {
		return mMessageCount;
	}

	public String getRead() {
		return mRead;
	}

	public String getUsername() {
		return mUsername;
	}
}
