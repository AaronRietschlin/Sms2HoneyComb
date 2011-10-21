package com.asa.sms2honeycomb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util.Util;
import com.asa.sms2honeycomb.phone.LoginActivity;
import com.asa.sms2honeycomb.tablet.LoginActivityTab;
import com.parse.PushService;

public class SettingsActivity extends PreferenceActivity {
	Button logoutButton;
	Intent mIntent;
	IncomingPushReceiver pushReceiver;
	private boolean isTablet;

	private final String TAG = "SettingsActvity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.pref_activity);
		// TODO make some settings?
		// TODO subscribe to the tablet channel just for testing
		// TODO change to phone? so when the tablet sends a message back the app
		// will recivie the push
		PushService.subscribe(getApplicationContext(), Util.getPushChannel(
				Util.getUsernameString(), Preferences.TABLET),
				SettingsActivity.class);
		Bundle extras = getIntent().getExtras();
		isTablet = extras.getBoolean("isTablet");
		// Takes the user to the Landing page of Text to tab
		Preference homepagePreference = findPreference("prefHomepage");
		homepagePreference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					// Launches homepage when clicked.
					public boolean onPreferenceClick(Preference preference) {
						// Sets the homepage to be launched as a new activity.
						String url = "http://app.net/texttotab";
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						startActivity(i);
						return true;
					}
				});

		Preference logoutPreference = findPreference("prefLogout");
		logoutPreference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					// Launches homepage when clicked.
					public boolean onPreferenceClick(Preference preference) {

						Util.logoutUser();
						// TODO : Unregister!
						if (isTablet) {
							mIntent = new Intent(SettingsActivity.this,
									LoginActivityTab.class);
						} else {
							mIntent = new Intent(SettingsActivity.this,
									LoginActivity.class);
						}
						startActivity(mIntent);
						finish();
						return true;
					}
				});

	}
}
