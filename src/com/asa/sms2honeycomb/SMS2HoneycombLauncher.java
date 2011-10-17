package com.asa.sms2honeycomb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.asa.sms2honeycomb.tablet.MainHoneycombActivity;
import com.parse.Parse;
import com.parse.PushService;

public class SMS2HoneycombLauncher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);// Checks the version number of the
											// phone.
		Preferences.DEVICE_IS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;

		// Initialize Parse to allow server access.
		Parse.initialize(this, "CDu0jepIuQQrZBJwIItLNYs7B2RntnwdqvjmsFB0",
				"cLBmzNyXWj0Fn3ItWR8sq79JMVJfIiD0udntjBku");
		PushService.subscribe(this, "", MainHoneycombActivity.class);
		Intent startCorrectActivity;
		// If the phone is honeycomb based, then start the Honeycomb activity,
		// if not, start phone activity
		if (Preferences.DEVICE_IS_HONEYCOMB) {
			startCorrectActivity = new Intent(this,
					com.asa.sms2honeycomb.tablet.LoginActivityTab.class);
		} else {
			startCorrectActivity = new Intent(this,
					com.asa.sms2honeycomb.phone.LoginActivity.class);
		}
		startActivity(startCorrectActivity);
		finish();
	}

}
