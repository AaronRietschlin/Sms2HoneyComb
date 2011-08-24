package com.asa.sms2honeycomb;

import com.asa.sms2honeycomb.Util.Util;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class RegistrationTask extends AsyncTask<String, Object, Object>{

	private ProgressDialog progressDialog;
	
	public RegistrationTask(ProgressDialog dialog){
		progressDialog = dialog;
	}
	
	@Override
	protected Object doInBackground(String... args) {
		Util.pushToTable(args[0], args[1], args[2]);
		return null;
	}
	
	@Override
	protected void onPostExecute(Object object){
		progressDialog.dismiss();
	}

}
