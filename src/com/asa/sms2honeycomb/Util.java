package com.asa.sms2honeycomb;

import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class Util {
	private final static String TAG = "Util";
	private static boolean isInTable;
	
	public static boolean isInTable(final String item, final String input) {
		ParseQuery query = new ParseQuery("UserTable");
//		Log.d(TAG, "Beginning check to see if " + )
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (ParseObject object : objects) {
						if (object.getString(item).compareTo(input) == 0) {
							isInTable = true;
							break;
						} else {
							isInTable = false;
							break;
						}
					}
				} else {
					Log.d(TAG, "Retrieve from database failed.");
				}
			}
		});
		return isInTable;
	}
	

	public static void pushToTable(String email, String username, String password) {
		ParseObject userTable = new ParseObject("UserTable");
		userTable.put(Preferences.EMAIL_ROW, email);
		userTable.put(Preferences.USERNAME_ROW, username);
		userTable.put("password", password); // TODO: Encrypt passwords!
		userTable.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Log.d(TAG, "Save failed.");
					e.printStackTrace();
				} else {
					if (Preferences.DEBUG)
						Log.d(TAG, "Save success.");
				}
			}
		});
	}
}
