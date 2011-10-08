package com.asa.sms2honeycomb;

public class Preferences {

	public static boolean DEVICE_IS_HONEYCOMB = false;

	// TODO: BEGIN Get rid of these...
	public static final String PARSE_EMAIL_ROW = "email";
	public static final String PARSE_USERNAME_ROW = "username";
	public static final String PARSE_PASSWORD_ROW = "password";
	// TODO: END Get rid of...

	// BEGIN Parse database object names
	// SMS table
	public static final String PARSE_TABLE_SMS = "sms";
	public static final String PARSE_SMS_SMSID = "smsId";
	public static final String PARSE_SMS_THREAD_ID = "threadId";
	public static final String PARSE_SMS_ADDRESS = "address";
	public static final String PARSE_SMS_CREATEDAT = "createdAt";
	public static final String PARSE_SMS_READ = "read";
	public static final String PARSE_SMS_TYPE = "type";
	public static final String PARSE_SMS_SUBJECT = "subject";
	public static final String PARSE_SMS_BODY = "body";
	
	// Thread Table
	public static final String PARSE_TABLE_THREAD = "thread";
	public static final String PARSE_THREAD_THREADID = "threadId";
	public static final String PARSE_THREAD_CREATEDAT = "createdAt";
	public static final String PARSE_THREAD_MESSAGE_COUNT = "messageCount";
	public static final String PARSE_THREAD_SNIPPET = "snippet";
	public static final String PARSE_THREAD_READ = "read";
	public static final String PARSE_THREAD_HAS_ATTACHMENT = "hasAttachment";
	
	// END Parse database object names
	
	public static final String PARSE_INSTALLATION_ID = "installation_id";

	public static final String TABLET = "TABLET";
	public static final String PHONE = "PHONE";

	public static final int INVALID_NONE = 0;
	public static final int INVALID_EMAIL = 1;
	public static final int INVALID_USERNAME = 2;
	public static final int INVALID_BOTH = 3;
	public static final String LOOKUP_EMAIL = "LOOKUP_EMAIL";
	public static final String LOOKUP_USERNAME = "LOOKUP_USERNAME";

	public static final String PREFS_NAME = "TTT_PREFS";
	public static final String PREFS_USERNAME = "PREFS_USERNAME";
	public static final String PREFS_EMAIL = "PREFS_EMAIL";
	public static final String PREFS_SESSIONID = "SESSIONID";

	public static final int REG_USERNAME_TAKEN = 202;
	public static final int REG_EMAIL_TAKEN = 203;
	public static final int REG_NO_CONNECTION = 110;
	public static final int REG_EMPTY = 0;
	public static final int REG_INVALID = 1;
	public static final int REG_IN_TABLE = 2;

	public static final int MENU_LOGOUT = 0;

	
	public static boolean LAUNCH_FROM_LOGIN = true;
	
	
	public static boolean DEBUG = true;
}