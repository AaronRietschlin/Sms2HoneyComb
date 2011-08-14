package com.asa.sms2honeycomb.phone;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.R.layout;
import com.asa.sms2honeycomb.tablet.MainHoneycombActivity;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;

public class MainPhoneActivity extends Activity {

	private final String TAG = "MainPhoneActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: Start the phone activity
		// I'm thinking this will just direct to the settings page.
		setContentView(R.layout.main);
		// Initialize the parse object.

		PushService.subscribe(this,
				getPushChannel(Preferences.USERNAME_ROW, Preferences.TABLET),
				MainHoneycombActivity.class);
		// Log.e("TEST", "TEST");
		// ParseObject testObject = new ParseObject("TestObject");
		// testObject.put("foo", "bar");
		// testObject.saveInBackground();
		// ParseObject newObject = new ParseObject("Message");
		// newObject
		// .put("Message",
		// "You get the idea, get over here right now, its friday and we are starting early as fuck. lets have fun, lets get shwasted. We are gona pregame here then maybe hit up some bars, so be here.You get the idea, get over here right now, its friday and we are starting early as fuck. lets have fun, lets get shwasted. We are gona pregame here then maybe hit up some bars, so be here.You get the idea, get over here right now, its friday and we are starting early as fuck. lets have fun, lets get shwasted. We are gona pregame here then maybe hit up some bars, so be here.You get the idea, get over here right now, its friday and we are starting early as fuck. lets have fun, lets get shwasted. We are gona pregame here then maybe hit up some bars, so be here.You get the idea, get over here right now, its friday and we are starting early as fuck. lets have fun, lets get shwasted. We are gona pregame here then maybe hit up some bars, so be here.");
		// newObject.saveInBackground();
	}

	/**
	 * Creates a string from the SharedPreferences using the key and a given
	 * name of the wanted push channel. Returns a string "keyitem_nameOfPushChannel".
	 * 
	 * @param key
	 * @param nameOfPushChannel
	 * @return
	 */
	public String getPushChannel(String key, String nameOfPushChannel) {
		SharedPreferences sharedPreferences = getSharedPreferences(
				Preferences.PREFS_NAME, 0);
		String savedPreference = sharedPreferences.getString("sms2honeycomb_"
				+ key, "");
		Log.d(TAG, "SharedPreference is loaded: " + key);
		String pushChannel = savedPreference + "_" + nameOfPushChannel;
		Log.d(TAG, "Push channel has been created for: " + key + "_"
				+ nameOfPushChannel);
		return pushChannel;
	}
}
