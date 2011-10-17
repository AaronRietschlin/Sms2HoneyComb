package com.asa.sms2honeycomb.phone;

import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util.Util;
import com.parse.PushService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsActivity extends Activity {
	Button logoutButton;
	Intent mIntent;
	
	private final String TAG = "SettingsActvity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_phone);
		// TODO make some settings?
		// TODO subscribe to the tablet channel just for testing
		// TODO change to phone? so when the tablet sends a message back the app will recivie the push
		PushService.subscribe(getApplicationContext(), Util.getPushChannel(
				Util.getUsernameString(), Preferences.TABLET),
				SettingsActivity.class);
		
		logoutButton = (Button) findViewById(R.id.logout_button1);
		
		logoutButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				mIntent = new Intent(SettingsActivity.this,
						LoginActivity.class);
				startActivity(mIntent);
				finish();
			}
		});
	}
}
