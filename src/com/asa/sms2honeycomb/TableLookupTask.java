package com.asa.sms2honeycomb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class TableLookupTask extends AsyncTask<String, Void, Integer> {

	Context mContext;
	ProgressDialog progressDialog;
	
	public TableLookupTask(Context context){
		mContext = context;
	}
	
	//This will be run BEFORE the doInBackground. 
	@Override
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(mContext, "", 
                "Loading. Please wait...", true);
	}

	@Override
	protected Integer doInBackground(String... args) {
		int type = -1;
		// Log.e("Task", "Email: " + args[0] + " Username: " + args[1]);
//		if (args[2].compareTo(Preferences.LOOKUP_EMAIL) == 0) {
//			if (Util.isInTable(Preferences.PARSE_EMAIL_ROW, args[0])) {
//				type = Preferences.INVALID_EMAIL;
//			}
//		}
//		if (Util.isInTable(Preferences.PARSE_USERNAME_ROW, args[1])) {
//			if (type == Preferences.INVALID_EMAIL) {
//				type = Preferences.INVALID_BOTH;
//			} else {
//				type = Preferences.INVALID_USERNAME;
//			}
//		}

		return type;
	}

	@Override
	protected void onPostExecute(Integer result) {
		RegistrationTask regTask = new RegistrationTask(progressDialog);
		regTask.execute();
	}

}
