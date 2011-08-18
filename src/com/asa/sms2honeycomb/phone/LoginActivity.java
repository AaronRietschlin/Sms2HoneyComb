package com.asa.sms2honeycomb.phone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	private final String TAG = "LoginActivity";

	private Button loginButton;
	private Button cancelButton;
	private EditText usernameField;
	private EditText passwordField;

	private boolean validLogin;

	private Intent mIntent;
	private Context mContext;
	private ProgressDialog loginProgress;
	private SharedPreferences prefs;
	private Toast invalidLoginToast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_phone);

		prefs = getSharedPreferences(Preferences.PREFS_NAME, MODE_PRIVATE);
		loginButton = (Button) findViewById(R.id.login_login_btn_phone);
		cancelButton = (Button) findViewById(R.id.login_cancel_btn_phone);
		usernameField = (EditText) findViewById(R.id.login_username_field_phone);
		passwordField = (EditText) findViewById(R.id.login_password_field_phone);

		mContext = getApplicationContext();

		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String usernameText = usernameField.getText().toString().trim();
				String passwordText = passwordField.getText().toString().trim();
				validLogin = true;

				// Check if the password has whitespace.
				if (Util.containsWhiteSpace(usernameText)) {
					validLogin = false;
					invalidLoginToast = Toast
							.makeText(
									mContext,
									"Invalid username. You cannot have whitespace in your username.",
									Toast.LENGTH_LONG);
					invalidLoginToast.show();
				}
				// Check if the username has been entered.
				if (usernameText.length() == 0) {
					validLogin = false;
					invalidLoginToast = Toast.makeText(mContext,
							"Invalid username. You must enter a username.",
							Toast.LENGTH_LONG);
					invalidLoginToast.show();

				}
				// Check if the password has been entered.
				if (passwordText.length() == 0) {
					validLogin = false;
					invalidLoginToast = Toast.makeText(mContext,
							"Invalid password. You must enter a password.",
							Toast.LENGTH_LONG);
					invalidLoginToast.show();
				}

				if (validLogin) {
					loginProgress = ProgressDialog.show(
							LoginActivity.this,
							"",
							getResources().getString(
									R.string.dialog_login_message), true);
					loginUser(usernameText, passwordText);
				}

			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});
		// Allow user to click on the DPAD, or enter button within the password
		// field login.
		passwordField.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				int action = event.getAction();
				if (actionId == EditorInfo.IME_ACTION_GO
						|| action == KeyEvent.KEYCODE_DPAD_CENTER
						|| action == KeyEvent.KEYCODE_ENTER) {
					Log.e(TAG, "Enter button hit.");
					loginButton.performClick();
				}
				return true;
			}
		});

		if (Preferences.DEBUG) {
			usernameField.setText("TestName");
			passwordField.setText("12345");
		}
	}

	/**
	 * Creates an entry in the Shared Preferences under sms2honeycomb_key with
	 * the assignment of a of the item. the SharedPreferences is listed under
	 * the key is key.
	 * 
	 * @param key
	 * @param item
	 */
	public void saveSharedPreference(String key, String item) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, item);
		editor.commit();
	}

	private void loginUser(String usernameText, String passwordText) {
		ParseUser.logInInBackground(usernameText, passwordText,
				new LogInCallback() {
					@Override
					public void done(ParseUser user, ParseException e) {
						loginProgress.dismiss();
						if (e == null && user != null) {
							// Successful login.
							mIntent = new Intent(LoginActivity.this,
									MainPhoneActivity.class);
							startActivity(mIntent);
						} else if (user == null) {
							// Username or password is incorrect.
							Log.d(TAG, "Username is incorrect...");
							invalidLoginToast = Toast
									.makeText(
											mContext,
											"Username or password is incorrect. Please try again.",
											Toast.LENGTH_LONG);
							invalidLoginToast.show();
						} else {
							// Error with connecting server.
							invalidLoginToast = Toast
									.makeText(
											mContext,
											"An error occurred when attempting to connect to servers. Please try again later.",
											Toast.LENGTH_LONG);
							invalidLoginToast.show();
						}
					}
				});
	}
}
