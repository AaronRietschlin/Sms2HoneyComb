package com.asa.sms2honeycomb.phone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util;

public class LoginActivity extends Activity {

	private final String TAG = "LoginActivity";
	private boolean isInTableUsername = false;
	private boolean isInTablePassword = false;
	private boolean isBothInTable = false;

	private Button loginButton;
	private Button cancelButton;
	private EditText usernameField;
	private EditText passwordField;
	SharedPreferences prefs;

	private Intent mIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_phone);

		prefs = getSharedPreferences(Preferences.PREFS_NAME, MODE_PRIVATE);
		loginButton = (Button) findViewById(R.id.login_login_btn_phone);
		cancelButton = (Button) findViewById(R.id.login_cancel_btn_phone);
		usernameField = (EditText) findViewById(R.id.login_username_field_phone);
		passwordField = (EditText) findViewById(R.id.login_password_field_phone);

		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String usernameText = usernameField.getText().toString().trim();
				String passwordText = passwordField.getText().toString().trim();
				if (Util.isInTable(Preferences.USERNAME_ROW, usernameText)
						&& Util.isInTable(Preferences.PASSWORD_ROW,
								passwordText)) {
					Log.d(TAG, "Login succesful for user : " + usernameText);
					// saves the username within the SharedPreferences so it can
					// be used to make the appropriate channel
					saveSharedPreference(Preferences.USERNAME_ROW, usernameText);
					mIntent = new Intent(LoginActivity.this,
							MainPhoneActivity.class);
					startActivity(mIntent);
					// TODO create shared preferences that will auto login and
					// auto register the push channels
				}
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});
	}

	/**
	 * Creates an entry in the Shared Preferences under sms2honeycomb_key with
	 * the assignment of a of the item. the SharedPreferences is listed under
	 * the key sms2honeycomb_key.
	 * 
	 * @param key
	 * @param item
	 */
	public void saveSharedPreference(String key, String item) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, item);
		editor.commit();
		Log.d(TAG, "SharedPreferences created and commited: " + item
				+ " in key: " + "sms2honeycomb_" + key);
	}
}
