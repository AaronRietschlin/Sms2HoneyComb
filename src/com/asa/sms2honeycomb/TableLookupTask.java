package com.asa.sms2honeycomb;

import android.os.AsyncTask;
import android.util.Log;

public class TableLookupTask extends AsyncTask<String, Void, Integer> {

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected Integer doInBackground(String... args) {
		int type = -1;
		// Log.e("Task", "Email: " + args[0] + " Username: " + args[1]);
		if (args[2].compareTo(Preferences.LOOKUP_EMAIL) == 0) {
			if (Util.isInTable(Preferences.EMAIL_ROW, args[0])) {
				type = Preferences.INVALID_EMAIL;
			}
		}
		if (Util.isInTable(Preferences.USERNAME_ROW, args[1])) {
			if (type == Preferences.INVALID_EMAIL) {
				type = Preferences.INVALID_BOTH;
			} else {
				type = Preferences.INVALID_USERNAME;
			}
		}

		return type;
	}

	@Override
	protected void onPostExecute(Integer result) {

	}

}
