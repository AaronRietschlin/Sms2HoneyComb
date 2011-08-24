package com.asa.sms2honeycomb.phone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util.LoginUtil;
import com.asa.sms2honeycomb.Util.Util;
import com.asa.sms2honeycomb.phone.MainPhoneActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity {
	private Button registerButton;
	private EditText emailField;
	private EditText usernameField;
	private EditText passwordField;

	private Intent mIntent;
	private Context mContext;
	private ProgressDialog registrationProgress;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;

	private boolean validRegistration;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity_phone);

		registerButton = (Button) findViewById(R.id.register_btn_phone);
		emailField = (EditText) findViewById(R.id.register_email_phone);
		usernameField = (EditText) findViewById(R.id.register_username_phone);
		passwordField = (EditText) findViewById(R.id.register_password_phone);

		prefs = this.getSharedPreferences(Preferences.PREFS_NAME,
				Context.MODE_PRIVATE);
		editor = prefs.edit();

		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String emailText = emailField.getText().toString().trim();
				String usernameText = usernameField.getText().toString().trim();
				String passwordText = passwordField.getText().toString().trim();
				validRegistration = true;
				mContext = RegisterActivity.this;

				// Check if email has whitespace
				if (Util.containsWhiteSpace(emailText) && validRegistration) {
					validRegistration = false;
					LoginUtil.displayLoginToast(mContext,
							R.string.invalid_email);
				}

				// Check if username has whitespace
				if (Util.containsWhiteSpace(usernameText) && validRegistration) {
					validRegistration = false;
					LoginUtil.displayLoginToast(mContext,
							R.string.invalid_username_whitespace);
				}

				// Check if email is entered
				if (emailText.length() == 0 && validRegistration) {
					validRegistration = false;
					LoginUtil.displayLoginToast(mContext,
							R.string.invalid_email_none_entered);
				}

				// Check if username is entered
				if (usernameText.length() == 0 && validRegistration) {
					validRegistration = false;
					LoginUtil.displayLoginToast(mContext,
							R.string.invalid_username_none_entered);
				}

				// Check if password is entered
				if (passwordText.length() == 0 && validRegistration) {
					validRegistration = false;
					LoginUtil.displayLoginToast(mContext,
							R.string.invalid_password_none_entered);
				}

				// Check if email is a valid email
				if (!LoginUtil.isValidEmail(emailText)) {
					validRegistration = false;
					LoginUtil.displayLoginToast(mContext,
							R.string.invalid_email);
				}

				if (validRegistration) {
					registrationProgress = ProgressDialog.show(
							mContext,
							"",
							getResources().getString(
									R.string.dialog_register_message), true);
					registerUser(emailText, usernameText, passwordText);
				}
			}
		});

		if (Preferences.DEBUG) {
			emailField.setText("test.@testing.com");
			usernameField.setText("TestName");
			passwordField.setText("12345");
		}

	}

	private void registerUser(final String email, final String username,
			String password) {
		final ParseUser newUser = new ParseUser();
		newUser.setUsername(username);
		newUser.setPassword(password);
		newUser.setEmail(email);
		newUser.put(Preferences.PARSE_INSTALLATION_ID,
				ParseUser.getInstallationId(mContext));

		newUser.signUpInBackground(new SignUpCallback() {
			@Override
			public void done(ParseException e) {
				registrationProgress.dismiss();
				if (e == null) {
					/*
					 * Successful signup Put registered information in the users
					 * SharedPreferences.
					 */
					editor.putString(Preferences.PREFS_EMAIL, email);
					editor.putString(Preferences.PREFS_USERNAME, username);
					editor.putString(Preferences.PREFS_SESSIONID,
							newUser.getSessionToken());
					editor.commit();
					validRegistration = true;

					mIntent = new Intent(mContext, MainPhoneActivity.class);
					startActivity(mIntent);
					finish();
				} else {
					if (e.getCode() == Preferences.REG_EMAIL_TAKEN) {
						LoginUtil.displayLoginToast(mContext,
								R.string.invalid_email_taken);
					} else if (e.getCode() == Preferences.REG_USERNAME_TAKEN) {
						LoginUtil.displayLoginToast(mContext,
								R.string.invalid_username_taken);
					} else if (e.getCode() == Preferences.REG_NO_CONNECTION) {
						LoginUtil.displayLoginToast(mContext,
								R.string.parse_connection_error);
					}
				}
			}
		});
	}
}
