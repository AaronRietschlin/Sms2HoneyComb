package com.asa.sms2honeycomb;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;

public class Util {
	private final static String TAG = "Util";

	
	/**
	 * Checks to see if there is white space within the input (both username and
	 * email). If there is, then it is an invalid input. Returns true if there
	 * is white space, false if there is no white space.
	 * 
	 * @param input
	 * @return
	 */
	public static boolean containsWhiteSpace(String input) {
		if (input != null) {
			for (int i = 0; i < input.length(); i++) {
				if (Character.isWhitespace(input.charAt(i))) {
					return true;
				} 
			}
		}
		return false;
	}
	
	/**
	 * Pushes the email, username, and password to the Parse UserTable object.
	 * 
	 * @param email
	 * @param username
	 * @param password
	 */
	public static void pushToTable(String email, String username,
			String password) {
		ParseObject userTable = new ParseObject("UserTable");
		userTable.put(Preferences.PARSE_EMAIL_ROW, email);
		userTable.put(Preferences.PARSE_USERNAME_ROW, username);
		userTable.put("password", password); // TODO: Encrypt passwords!
		try {
			userTable.save();
			if (Preferences.DEBUG)
				Log.d(TAG, "Save success.");
		} catch (ParseException e1) {
			e1.printStackTrace();
			Log.d(TAG, "Save failed.");
		}
	}
}
