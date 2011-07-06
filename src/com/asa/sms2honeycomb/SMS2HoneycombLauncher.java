package com.asa.sms2honeycomb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SMS2HoneycombLauncher extends Activity {
	// Checks the version number of the phone.
	private static boolean DEVICE_IS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent startCorrectActivity;
		// If the phone is honeycomb based, then start the Honeycomb activity,
		// if not, start phone activity
		if (DEVICE_IS_HONEYCOMB) {
			startCorrectActivity = new Intent(this, MainHoneycombActivity.class);
		} else {
			startCorrectActivity = new Intent(this, MainPhoneActivity.class);
		}
		startActivity(startCorrectActivity);
		finish();
	}

}
