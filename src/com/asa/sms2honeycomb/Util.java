package com.asa.sms2honeycomb;

import java.util.List;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class Util {
	private final static String TAG = "Util";
	private static boolean isInTable;

	/**
	 * Checks if the input is the same as the item within the Parse object
	 * UserTable. Returns true if the input is in the table and false if the
	 * item is not in the the table.
	 * 
	 * @param item
	 * @param input
	 * @return
	 */
	public static boolean isInTable(final String item, final String input) {
		ParseQuery query = new ParseQuery("UserTable");
		// Log.d(TAG, "Beginning check to see if " + ) 
		try {
			List<ParseObject> objectList = query.find();
			int i = 1;
			if(item == null) {
				Log.e(TAG, "ITEM IS NULL!");
			}else{
				Log.e(TAG, "ITEM IS NOT NULL!");
			}
			if(input == null){
				Log.e(TAG, "INPUT IS NULL!");
			}
			for (ParseObject object : objectList) {
				if (object.getString(item).compareTo(input) == 0) {
					isInTable = true;
					break;
				} else {
					isInTable = false;
					break;
				}
			}
			Log.i(TAG, "Done with table lookup.");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}		
		return isInTable;
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
		userTable.put(Preferences.EMAIL_ROW, email);
		userTable.put(Preferences.USERNAME_ROW, username);
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
