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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

// TODO: Return an error if Parse doesnt save the data (right now the app still fowards you on to the login activity when Parse does not save.) 

public class RegisterActivity extends Activity {
	private boolean registrationSuccess = true;
	private final int EMAIL = 0;
	private final int USERNAME = 1;
	private final int BOTH = 2;
	private final String TAG = "RegisterActivity";

	private boolean isInTable;
	private boolean invalidEmail;
	private boolean invalidUsername;
	private boolean invalidPassword;
	private int invalidType;

	private Button registerButton;
	private Button cancelButton;
	private LinearLayout invalidUsernameLL;
	private LinearLayout invalidEmailLL;
	private LinearLayout invalidPasswordLL;
	private TextView invalidUsernameText;
	private TextView invalidEmailText;
	private TextView invalidPasswordText;
	private EditText emailField;
	private EditText usernameField;
	private EditText passwordField;

	private Intent mIntent;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private ProgressDialog registerProgress;
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity_phone);

		registerButton = (Button) findViewById(R.id.register_btn_phone);
		cancelButton = (Button) findViewById(R.id.register_cancel_btn_phone);
		invalidUsernameText = (TextView) findViewById(R.id.register_invalid_username_phone);
		invalidUsernameLL = (LinearLayout) findViewById(R.id.register_invalid_username_ll);
		invalidEmailLL = (LinearLayout) findViewById(R.id.register_invalid_email_ll);
		invalidEmailText = (TextView) findViewById(R.id.register_invalid_email_phone);
		invalidPasswordLL = (LinearLayout) findViewById(R.id.register_invaild_password_ll);
		invalidPasswordText = (TextView) findViewById(R.id.register_invaild_password_phone);
		emailField = (EditText) findViewById(R.id.register_email_phone);
		usernameField = (EditText) findViewById(R.id.register_username_phone);
		passwordField = (EditText) findViewById(R.id.register_password_phone);

		prefs = this.getSharedPreferences(Preferences.PREFS_NAME, MODE_PRIVATE);
		editor = prefs.edit();
		mContext = getApplicationContext();

		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String emailText = emailField.getText().toString().trim();
				String usernameText = usernameField.getText().toString().trim();
				String passwordText = passwordField.getText().toString().trim();

				invalidEmail = false;
				invalidUsername = false;
				invalidPassword = false;
				isInTable = false;
				int emailErrorType = -1;
				int nameErrorType = -1;
				int passwordErrorType = -1;

				/*
				 * May have to move all the work that is not pushing to the
				 * server into a separate thread. I'll look into it more.
				 */

				// Check if email field was entered.
				if (emailText.length() == 0) {
					invalidEmail = true;
					emailErrorType = Preferences.REG_EMPTY;
				} else {
					// Check if email is valid.
					if (!isValidEmail(emailText)
							|| Util.containsWhiteSpace(emailText)) {
						invalidEmail = true;
						emailErrorType = Preferences.REG_INVALID;
					}
				}

				// Check if username was entered.
				if (usernameText.length() == 0) {
					invalidUsername = true;
					nameErrorType = Preferences.REG_EMPTY;
				} else {
					// Check if username contains whitespace.
					if (Util.containsWhiteSpace(usernameText)) {
						invalidUsername = true;
						nameErrorType = Preferences.REG_INVALID;
					}
				}

				// Checks if the password was entered.
				if (passwordText.length() == 0) {
					invalidPassword = true;
					passwordErrorType = Preferences.REG_EMPTY;
				} else {
					// Check if password contains whitespace.
					if (Util.containsWhiteSpace(passwordText)) {
						invalidPassword = true;
						passwordErrorType = Preferences.REG_INVALID;
					}
				}

				// If there is an invaild email display the error text.
				if (invalidEmail) {
					displayEmailFailureText(emailErrorType);
				} else {
					invalidEmailLL.setVisibility(View.GONE);
				}

				// If there is an invaild username display the error text.
				if (invalidUsername) {
					displayUsernameFailureText(nameErrorType);
				} else {
					invalidUsernameLL.setVisibility(View.GONE);
				}

				// If there is an invaild password displat the error text.
				if (invalidPassword) {
					displayPasswordFailureText(passwordErrorType);
					System.out.println("Password Error type = "
							+ passwordErrorType);
				} else {
					invalidPasswordLL.setVisibility(View.GONE);
				}

				// Log.e(TAG, "Email: " + String.valueOf(invalidEmail));
				// Log.e(TAG, "Username: " + String.valueOf(invalidUsername));
				// Log.e(TAG, "Password: " + String.valueOf(invalidPassword));

				if (invalidPassword || invalidEmail || invalidUsername) {
					registrationSuccess = false;
				} else {
					registrationSuccess = true;
				}

				// Log.e(TAG,
				// "Registration success is "
				// + String.valueOf(registrationSuccess));
				if (registrationSuccess) {
					registerProgress = ProgressDialog.show(
							RegisterActivity.this,
							"",
							getResources().getString(
									R.string.dialog_register_message), true);
					registerUser(usernameText, passwordText, emailText);
				}
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});

		// Allow user to click on the DPAD, or enter button within the password
		// field Register.
		passwordField.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event != null) {
					int action = event.getAction();
					if (actionId == EditorInfo.IME_ACTION_GO
							|| action == KeyEvent.KEYCODE_DPAD_CENTER
							|| action == KeyEvent.KEYCODE_ENTER) {
						Log.e(TAG, "Enter button hit.");
						registerButton.performClick();
					}
					return true;
				} else {
					return false;

				}
			}
		});

		if (Preferences.DEBUG) {
			emailField.setText("test.@testing.com");
			usernameField.setText("TestName");
			passwordField.setText("12345");
		}

	}

	/**
	 * Checks to see if the inputed email is vaild by checking to see if there
	 * is an "@" or a "." If the email is vaild returns true and returns false
	 * if the email is not vaild.
	 * 
	 * @param email
	 * @return
	 */
	private boolean isValidEmail(String email) {
		String atChar = "@";
		String dotChar = ".";
		if (email != null) {
			int index1 = email.indexOf(atChar);
			int index2 = email.indexOf(dotChar);
			if ((index1 != -1) && (index2 != -1)) {
				// contatins @ and a . so vaild
				return true;
			} else {
				return false;
			}
		}
		Log.d(TAG, "The inputed email is null.");
		return false;
	}

	/**
	 * Sets the text and makes visible the email error message based on the
	 * input from the user.
	 * 
	 * @param errorType
	 *            The type of error. The errors are:
	 *            <ul>
	 *            <li>0 - No email was given.</li>
	 *            <li>1 - An invalid email was given (most likely @ nor .* was
	 *            used).</li>
	 *            <li>2 - The desired email is already registered.</li>
	 *            </ul>
	 */
	private void displayEmailFailureText(int errorType) {
		String message = "";
		switch (errorType) {
		case Preferences.REG_EMPTY:
			message = getResources().getString(
					R.string.register_no_email_entered);
			break;
		case Preferences.REG_INVALID:
			message = getResources().getString(R.string.register_invalid_email);
			break;
		case Preferences.REG_IN_TABLE:
			message = getResources().getString(
					R.string.register_duplicate_email);
			break;
		}
		invalidEmailText.setText(message);
		invalidEmailLL.setVisibility(View.VISIBLE);
	}

	/**
	 * Sets the text and makes visible the username error message based on the
	 * input from the user.
	 * 
	 * @param errorType
	 *            The type of error. The errors are:
	 *            <ul>
	 *            <li>0 - No username was given.</li>
	 *            <li>1 - An invalid username was given (white space was used).</li>
	 *            <li>2 - The desired username is already registered.</li>
	 *            </ul>
	 */
	private void displayUsernameFailureText(int errorType) {
		String message = "";
		switch (errorType) {
		case Preferences.REG_EMPTY:
			message = getResources().getString(
					R.string.register_no_username_entered);
			break;
		case Preferences.REG_INVALID:
			message = getResources().getString(
					R.string.register_invalid_username);
		case Preferences.REG_IN_TABLE:
			message = getResources()
					.getString(R.string.register_username_taken);
			break;
		}
		invalidUsernameText.setText(message);
		invalidUsernameLL.setVisibility(View.VISIBLE);
	}

	/**
	 * Sets the text and makes visible the password error message based on the
	 * input from the user.
	 * 
	 * @param errorType
	 *            The type of error. The errors are:
	 *            <ul>
	 *            <li>0 - No password was given.</li>
	 *            <li>1 - An invalid password was given.</li>
	 *            </ul>
	 */
	private void displayPasswordFailureText(int errorType) {
		String message = "";
		switch (errorType) {
		case Preferences.REG_EMPTY:
			message = getResources().getString(
					R.string.register_no_password_entered);
			break;
		case Preferences.REG_INVALID:
			message = getResources().getString(
					R.string.register_invalid_password);
		}
		invalidPasswordText.setText(message);
		invalidPasswordLL.setVisibility(View.VISIBLE);
	}

	private void registerUser(final String username, String password,
			final String email) {
		final ParseUser newUser = new ParseUser();
		newUser.setUsername(username);
		newUser.setPassword(password);
		newUser.setEmail(email);
		newUser.put(Preferences.PARSE_INSTALLATION_ID,
				ParseUser.getInstallationId(mContext));

		newUser.signUpInBackground(new SignUpCallback() {
			@Override
			public void done(ParseException e) {
				registerProgress.dismiss();
				if (e == null) {
					// Successful signup
					// Put registered information in the users
					// SharedPreferences.
					editor.putString(Preferences.PREFS_EMAIL, email);
					editor.putString(Preferences.PREFS_USERNAME, username);
					editor.putString(Preferences.PREFS_SESSIONID,
							newUser.getSessionToken());
					editor.commit();
					registrationSuccess = true;

					Log.d(TAG, "Registration was a success...Pushing to table.");
					// Util.pushToTable(emailText, usernameText, passwordText);
					mIntent = new Intent(mContext, MainPhoneActivity.class);
					startActivity(mIntent);
					finish();
				} else {
					Log.e(TAG, String.valueOf(e.getCode()));
					Log.e(TAG, e.getMessage());
					if (e.getCode() == Preferences.REG_EMAIL_TAKEN) {
						displayEmailFailureText(Preferences.REG_IN_TABLE);
						Toast invalidLoginToast = Toast.makeText(mContext,
								"Login failed.", Toast.LENGTH_LONG);
						invalidLoginToast.show();
					} else if (e.getCode() == Preferences.REG_USERNAME_TAKEN) {
						displayUsernameFailureText(Preferences.REG_IN_TABLE);
						Toast invalidLoginToast = Toast.makeText(mContext,
								"Login failed.", Toast.LENGTH_LONG);
						invalidLoginToast.show();
					} else if (e.getCode() == Preferences.REG_NO_CONNECTION) {
						Toast invalidLoginToast = Toast
								.makeText(
										mContext,
										"No Internet connection...Please connect to the Internet and try again.",
										Toast.LENGTH_LONG);
						invalidLoginToast.show();
					}
				}

			}
		});
	}
}
