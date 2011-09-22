package com.asa.sms2honeycomb.tablet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.util.Util;
import com.parse.PushService;

public class MainHoneycombActivity extends Activity {
	
	private final String TAG = "MainHoneycombActivity";

	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: Start the Honeycomb activity
		// This is going to be the main part of the app.
		// TODO: Rename the view to something more specific to honeycomb (i.e.
		// xlarge_main.xml)
		setContentView(R.layout.activity_tablet_main);
		// Want to send the messages to the phone so they can be sms messages
		PushService.subscribe(this, Util.getPushChannel(
				Util.getUsernameString(), Preferences.TABLET),
				MainHoneycombActivity.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Preferences.MENU_LOGOUT, 0, "Logout");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case Preferences.MENU_LOGOUT:
			Util.logoutUser();
			mIntent = new Intent(MainHoneycombActivity.this,
					LoginActivityTab.class);
			startActivity(mIntent);
			finish();
			return true;
		}
		return false;
	}
}
