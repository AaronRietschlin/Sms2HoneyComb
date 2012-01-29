package com.asa.texttotab.tablet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.asa.texttotab.R;
import com.asa.texttotab.Preferences;
import com.asa.texttotab.Util.LoginUtil;
import com.asa.texttotab.Util.Util;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivityTab extends Activity {
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
		setContentView(R.layout.register_activity_tablet);

		registerButton = (Button) findViewById(R.id.register_button_tablet);
		emailField = (EditText) findViewById(R.id.register_email_field_tab);
		usernameField = (EditText) findViewById(R.id.register_username_field_tab);
		passwordField = (EditText) findViewById(R.id.register_password_field_tab);

		prefs = this.getSharedPreferences(Preferences.PREFS_NAME,
				Context.MODE_PRIVATE);
		editor = prefs.edit();

		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String emailText = emailField.getText().toString().trim();
				String usernameText = usernameField.getText().toString().trim();
				String passwordText = passwordField.getText().toString().trim();
				validRegistration = true;
				mContext = RegisterActivityTab.this;

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

					mIntent = new Intent(mContext, MainHoneycombActivity.class);
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
