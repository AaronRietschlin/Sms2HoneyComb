package com.asa.sms2honeycomb.phone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util;

// TODO: Check for empty password.

public class RegisterActivity extends Activity {
	private boolean registrationSuccess = true;
	private final int EMPTY = 0;
	private final int INVALID = 1;
	private final int IN_TABLE = 2;
	private final String TAG = "RegisterActivity";
	boolean isInTable;

	private boolean invalidEmail;
	private boolean invalidUsername;

	private Button registerButton;
	private Button cancelButton;
	private TextView invalidUsernameText;
	private LinearLayout invalidUsernameLL;
	private LinearLayout invalidEmailLL;
	private TextView invalidEmailText;
	private EditText emailField;
	private EditText usernameField;
	private EditText passwordField;

	private Intent mIntent;

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
		emailField = (EditText) findViewById(R.id.register_email_phone);
		usernameField = (EditText) findViewById(R.id.register_username_phone);
		passwordField = (EditText) findViewById(R.id.register_password_phone);

		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String emailText = emailField.getText().toString();
				String usernameText = usernameField.getText().toString().trim();
				String passwordText = passwordField.getText().toString().trim();
				System.out.println(registrationSuccess);
				// Check if email field was entered.
				int emailErrorType = -1;
				int nameErrorType = -1;
				if (emailText.length() == 0) {
					invalidEmail = true;
					emailErrorType = EMPTY;
					Log.e(TAG, "Email length is 0");
				} else {
					// Check if email is valid.
					if (!isValidEmail(emailText)
							|| !containsWhiteSpace(emailText)) {
						invalidEmail = true;
						emailErrorType = INVALID;
						Log.e(TAG, "Email is invalid.");
					}
				}

				// Check if username was entered.
				if (usernameText.length() == 0) {
					invalidUsername = true;
					nameErrorType = EMPTY;
					Log.e(TAG, "Username length is 0");
				} else {
					// Check if username contains whitespace.
					if (containsWhiteSpace(usernameText)) {
						invalidUsername = true;
						nameErrorType = INVALID;
						Log.e(TAG, "Username is invalid");
					}
				}

				// Checks if email is already in the table.
				if (Util.isInTable(Preferences.EMAIL_ROW, emailText)) {
					invalidEmail = true;
					emailErrorType = IN_TABLE;
					Log.e(TAG, "Email is in table");
				}

				// Check if username is already taken.
				if (Util.isInTable(Preferences.USERNAME_ROW, usernameText)) {
					invalidUsername = true;
					nameErrorType = IN_TABLE;
					Log.e(TAG, "Username is in table.");
				}

				if (invalidEmail) {
					displayEmailFailureText(emailErrorType);
					System.out.println("Email Error type = " + emailErrorType);
					registrationSuccess = false;
				} else {
					invalidEmailLL.setVisibility(View.GONE);
				}

				if (invalidUsername) {
					displayUsernameFailureText(nameErrorType);
					System.out
							.println("Username Error type = " + nameErrorType);
					registrationSuccess = false;
				} else {
					invalidUsernameLL.setVisibility(View.GONE);
					if (!invalidEmail) {
						registrationSuccess = true;
					}
				}

				if (registrationSuccess) {
					Log.d(TAG, "Registration was a success...Pushing to table.");
					Util.pushToTable(emailText, usernameText, passwordText);
					mIntent = new Intent(RegisterActivity.this,
							LoginActivity.class);
					startActivity(mIntent);
				}
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});

	}

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
	 * Checks to see if there is white space within the input (both username and
	 * email). If there is, then it is an invalid input.
	 * 
	 * @param input
	 * @return
	 */
	private boolean containsWhiteSpace(String input) {
		if (input != null) {
			for (int i = 0; i < input.length(); i++) {
				if (Character.isWhitespace(input.charAt(i))) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	private void displayEmailFailureText(int errorType) {
		String message = "";
		switch (errorType) {
		case EMPTY:
			message = getResources().getString(
					R.string.register_no_email_entered);
			break;
		case INVALID:
			message = getResources().getString(R.string.register_invalid_email);
			break;
		case IN_TABLE:
			message = getResources().getString(
					R.string.register_duplicate_email);
			break;
		}
		invalidEmailText.setText(message);
		invalidEmailLL.setVisibility(View.VISIBLE);
	}

	private void displayUsernameFailureText(int errorType) {
		String message = "";
		switch (errorType) {
		case EMPTY:
			message = getResources().getString(
					R.string.register_no_username_entered);
			break;
		case INVALID:
			message = getResources().getString(
					R.string.register_invalid_username);
		case IN_TABLE:
			message = getResources()
					.getString(R.string.register_username_taken);
			break;
		}
		invalidUsernameText.setText(message);
		invalidUsernameLL.setVisibility(View.VISIBLE);
	}
}
