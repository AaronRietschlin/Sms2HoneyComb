package com.asa.sms2honeycomb.phone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.SendCallback;

public class MainPhoneActivity extends Activity {

	private final String TAG = "MainPhoneActivity";

	TextView messageListText;
	TextView toText;
	TextView messageText;
	EditText toField;
	EditText messageField;
	Button sendButton;
	Button logoutButton;

	Intent mIntent;

	SharedPreferences prefs;

	private boolean logoutSuccess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: Start the phone activity
		// I'm thinking this will just direct to the settings page.
		setContentView(R.layout.main_phone);

		messageListText = (TextView) findViewById(R.id.main_message_list_text);
		toText = (TextView) findViewById(R.id.main_to_text);
		messageText = (TextView) findViewById(R.id.main_message_text);
		toField = (EditText) findViewById(R.id.main_to_felid);
		messageField = (EditText) findViewById(R.id.main_message_felid);
		sendButton = (Button) findViewById(R.id.main_send_btn);
		logoutButton = (Button) findViewById(R.id.main_logout_btn);

		PushService.subscribe(this,
				getPushChannel(Preferences.PARSE_USERNAME_ROW, Preferences.TABLET),
				MainPhoneActivity.class);

		
		sendButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				// TODO: get strings from the text feilds
				// then send them on to parse and to the push channel(phone)
				// on then on the phone send the messages via a sms message to
				// the to number
				final String to = toField.getText().toString().trim();
				final String body = messageField.getText().toString().trim();

				ParseObject outgoingMessage = new ParseObject("OutgoingMessage");
				outgoingMessage.put("messageTo", to);
				outgoingMessage.put("messageBody", body);
				outgoingMessage.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e != null) {
							// Did not save correctly.
							Log.e(TAG, "Message not successfully saved.");
						} else {
							ParsePush push = new ParsePush();
							push.setChannel(getPushChannel(
									Preferences.PARSE_USERNAME_ROW,
									Preferences.TABLET));
							push.setMessage("To: " + to + "Message: " + body);
							push.sendInBackground(new SendCallback() {
								@Override
								public void done(ParseException e) {
									if (e != null) {
										// Did not send push notification
										// correctly.
										Log.e(TAG,
												"Push notification did not send correctly.",
												e);
										e.printStackTrace();
									} else {
										Log.d(TAG, "Push sent.");
									}
								}
							});
						}
					}
				});
			}
		});

		logoutButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				logoutUser(Preferences.PARSE_USERNAME_ROW);
				if (logoutSuccess) {
					// unsubscribe to the push channel
					PushService.unsubscribe(
							MainPhoneActivity.this,
							getPushChannel(Preferences.PARSE_USERNAME_ROW,
									Preferences.TABLET));
					Log.d(TAG, "Log out of user a success");
					mIntent = new Intent(MainPhoneActivity.this,
							LauncherActivity.class);
					startActivity(mIntent);
				} else {
					Log.e(TAG, "Logout of user is a failure");
				}
			}
		});

		if (Preferences.DEBUG) {
			toField.setText("1234567");
			messageField.setText("Just sending a texty text?!?!?");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0, Preferences.MENU_LOGOUT, 0, "Logout");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){
		switch(menuItem.getItemId()){
		case Preferences.MENU_LOGOUT:
			ParseUser.logOut();
			mIntent = new Intent(MainPhoneActivity.this, LauncherActivity.class);
			startActivity(mIntent);
			finish();
			
		}
		return false;
	}
	
	/**
	 * Creates a string from the SharedPreferences using the key and a given
	 * name of the wanted push channel. Returns a string
	 * "keyitem_nameOfPushChannel".
	 * 
	 * @param key
	 * @param nameOfPushChannel
	 * @return
	 */
	// TODO: this doesnt work right returns the key_nameOfPushChannel on the
	// login
	// part of the app it saves as key:item it should return the item(user's
	// name)
	public String getPushChannel(String key, String nameOfPushChannel) {
		SharedPreferences sharedPreferences = getSharedPreferences(
				Preferences.PREFS_NAME, 0);
		String savedPreference = sharedPreferences.getString(key, "");
		Log.d(TAG, "SharedPreference is loaded: " + key);
		String pushChannel = savedPreference + "_" + nameOfPushChannel;
		Log.d(TAG, "Push channel has been created for: " + key + "_"
				+ nameOfPushChannel);
		return pushChannel;
	}

	/**
	 * Removes the key from the SharedPreferences
	 * 
	 * @param key
	 */
	public void logoutUser(String key) {
		// TODO delete the key from the SharredPreferences or remove the value
		logoutSuccess = true;
	}
}
