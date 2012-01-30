package com.asa.texttotab;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent itent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
	    super.onCreate();
	    
		String intentString = "com.asa.texttotab.UPDATE_LIST";
		Intent updateIntent = new Intent();
		updateIntent.setAction(intentString);
		Bundle bundle = new Bundle();
		bundle.putString("phoneNumber", "1234567890");
		getApplicationContext().sendBroadcast(updateIntent);
		
		Log.e("UpdateService", "onStart");
		
		Intent stopServiceIntent = new Intent(this, UpdateService.class);
		getApplicationContext().stopService(stopServiceIntent);
		
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    Log.e("UpdateService", "onDestroy");
	    
	}
}
