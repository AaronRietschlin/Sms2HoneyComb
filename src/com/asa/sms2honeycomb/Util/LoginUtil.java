package com.asa.sms2honeycomb.Util;

import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.phone.LoginActivity;
import com.asa.sms2honeycomb.phone.MainPhoneActivity;
import com.asa.sms2honeycomb.tablet.LoginActivityTab;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class LoginUtil {

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

	public static void displayLoginToast(Context context, int message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		toast.show();
	}

}
