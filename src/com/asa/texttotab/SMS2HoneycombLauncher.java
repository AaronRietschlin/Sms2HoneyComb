package com.asa.texttotab;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.asa.texttotab.tablet.MainHoneycombActivity;
import com.parse.Parse;
import com.parse.PushService;

public class SMS2HoneycombLauncher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);// Checks the version number of the
											// phone.
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			// on a large screen device ...
			Preferences.DEVICE_IS_HONEYCOMB = true;
		} else {
			Preferences.DEVICE_IS_HONEYCOMB = false;
		}
		// Preferences.DEVICE_IS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >=
		// android.os.Build.VERSION_CODES.HONEYCOMB;

		// Initialize Parse to allow server access.
		Parse.initialize(this, "CDu0jepIuQQrZBJwIItLNYs7B2RntnwdqvjmsFB0",
				"cLBmzNyXWj0Fn3ItWR8sq79JMVJfIiD0udntjBku");
		PushService.subscribe(this, "", MainHoneycombActivity.class);
		Intent startCorrectActivity;
		// If the phone is honeycomb based, then start the Honeycomb activity,
		// if not, start phone activity
		if (Preferences.DEVICE_IS_HONEYCOMB) {
			startCorrectActivity = new Intent(this,
					com.asa.texttotab.tablet.LoginActivityTab.class);
		} else {
			startCorrectActivity = new Intent(this,
					com.asa.texttotab.phone.LoginActivity.class);
		}
		startActivity(startCorrectActivity);
		finish();
	}

}
