package com.asa.sms2honeycomb.util;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.Preferences;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

	// TODO work on this
	/**
	 * Gets from the table given the messages (limit 10) sorted by decending
	 * created times and returns a list of them or something
	 * 
	 * @param table
	 * @return totalmessages
	 */
	public String getMessages(String table) {
		final ParseQuery query = new ParseQuery(table);
		query.whereEqualTo(Preferences.PARSE_USERNAME_ROW, getUsernameString());
		query.orderByDescending("createdAt");
		query.setLimit(10);
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> messageList, ParseException e) {
				if (e == null) {
					Log.d(TAG, "Retrieved " + messageList.size() + " messages.");
					for (ParseObject messageObject : messageList) {
						String objectId = messageObject.objectId();
						try {
							ParseObject message = query.get(objectId);
							Date time = message.createdAt();
							String to = message.getString("messageTo");
							String body = message.getString("messageBody");
							String totalMessage = "Sent: " + time + " To: "
									+ to + " Message : " + body;
							System.out.println(totalMessage);
						} catch (ParseException e1) {
							Log.e(TAG, e1.getMessage());
						}
					}
				} else {
					Log.d(TAG, "Error: " + e.getMessage());
				}
			}
		});
		return table;
	}

	/**
	 * Creates a string using the uername and a given name of the wanted push
	 * channel. Returns a string "keyitem_nameOfPushChannel".
	 * 
	 * @param username
	 * @param nameOfPushChannel
	 * @return String username_pushchannel
	 */
	public static String getPushChannel(String username,
			String nameOfPushChannel) {
		String pushChannel = username + "_" + nameOfPushChannel;
		Log.d(TAG, "Push channel has been created for: " + username + "_"
				+ nameOfPushChannel);
		return pushChannel;
	}

	/**
	 * Logouts the current user via the Parse command returns true if logged
	 * out.
	 * 
	 * @return boolean
	 */
	public static boolean logoutUser() {
		ParseUser.logOut();
		if (ParseUser.getCurrentUser() == null) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the current users username via the Parse command
	 * 
	 * @return String currentUser
	 */
	public static String getUsernameString() {
		return ParseUser.getCurrentUser().getUsername();
	}
}
