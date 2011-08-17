package com.asa.sms2honeycomb;

public class Preferences {
	//TODO: BEGIN Get rid of these...
	public static final String PARSE_EMAIL_ROW = "email";
	public static final String PARSE_USERNAME_ROW = "username";
	public static final String PARSE_PASSWORD_ROW = "password";
	// TODO: END Get rid of...
	
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
	
	public static boolean DEBUG = true;
}
