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

<<<<<<< HEAD
// TODO: Return an error if Parse doesnt save the data (right now the app still fowards you on to the login activity when Parse does not save.) 
=======
// TODO: Check for empty password.
>>>>>>> origin/master

public class RegisterActivity extends Activity {
	private boolean registrationSuccess = true;
	private final int EMPTY = 0;
	private final int INVALID = 1;
	private final int IN_TABLE = 2;
	private final String TAG = "RegisterActivity";
	boolean isInTable;

	private boolean invalidEmail;
	private boolean invalidUsername;
<<<<<<< HEAD
	private boolean invalidPassword;

	private Button registerButton;
	private Button cancelButton;
	private LinearLayout invalidUsernameLL;
	private LinearLayout invalidEmailLL;
	private LinearLayout invalidPasswordLL;
	private TextView invalidUsernameText;
	private TextView invalidEmailText;
	private TextView invalidPasswordText;
=======

	private Button registerButton;
	private Button cancelButton;
	private TextView invalidUsernameText;
	private LinearLayout invalidUsernameLL;
	private LinearLayout invalidEmailLL;
	private TextView invalidEmailText;
>>>>>>> origin/master
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
<<<<<<< HEAD
		invalidPasswordLL = (LinearLayout) findViewById(R.id.register_invaild_password_ll);
		invalidPasswordText = (TextView) findViewById(R.id.register_invaild_password_phone);
=======
>>>>>>> origin/master
		emailField = (EditText) findViewById(R.id.register_email_phone);
		usernameField = (EditText) findViewById(R.id.register_username_phone);
		passwordField = (EditText) findViewById(R.id.register_password_phone);

		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
<<<<<<< HEAD
				String emailText = emailField.getText().toString().trim();
				String usernameText = usernameField.getText().toString().trim();
				String passwordText = passwordField.getText().toString().trim();
				System.out.println(registrationSuccess);

				int emailErrorType = -1;
				int nameErrorType = -1;
				int passwordErrorType = -1;

				// Check if email field was entered.
=======
				String emailText = emailField.getText().toString();
				String usernameText = usernameField.getText().toString().trim();
				String passwordText = passwordField.getText().toString().trim();
				System.out.println(registrationSuccess);
				// Check if email field was entered.
				int emailErrorType = -1;
				int nameErrorType = -1;
>>>>>>> origin/master
				if (emailText.length() == 0) {
					invalidEmail = true;
					emailErrorType = EMPTY;
					Log.e(TAG, "Email length is 0");
				} else {
					// Check if email is valid.
					if (!isValidEmail(emailText)
<<<<<<< HEAD
							|| containsWhiteSpace(emailText)) {
=======
							|| !containsWhiteSpace(emailText)) {
>>>>>>> origin/master
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
<<<<<<< HEAD
				}

				// Checks if the password was entered.
				if (passwordText.length() == 0) {
					invalidPassword = true;
					passwordErrorType = EMPTY;
					Log.e(TAG, "Password length is 0");
				} else {
					// Check if password contains whitespace.
					if (containsWhiteSpace(passwordText)) {
						invalidPassword = true;
						passwordErrorType = INVALID;
						Log.e(TAG, "Password is invalid");
					}
=======
>>>>>>> origin/master
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

<<<<<<< HEAD
				// If there is an invaild email display the error text.
=======
>>>>>>> origin/master
				if (invalidEmail) {
					displayEmailFailureText(emailErrorType);
					System.out.println("Email Error type = " + emailErrorType);
					registrationSuccess = false;
				} else {
					invalidEmailLL.setVisibility(View.GONE);
				}

<<<<<<< HEAD
				// If there is an invaild username display the error text.
=======
>>>>>>> origin/master
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

<<<<<<< HEAD
				// If there is an invaild password displat the error text.
				if (invalidPassword) {
					displayPasswordFailureText(passwordErrorType);
					System.out.println("Password Error type = "
							+ passwordErrorType);
					registrationSuccess = false;
				} else {
					invalidPasswordLL.setVisibility(View.GONE);
					if (!invalidPassword) {
						registrationSuccess = true;
					}
				}

=======
>>>>>>> origin/master
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

<<<<<<< HEAD
	/**
	 * Checks to see if the inputed email is vaild by checking to see if there
	 * is an "@" or a "." If the email is vaild returns true and returns false
	 * if the email is not vaild.
	 * 
	 * @param email
	 * @return
	 */
=======
>>>>>>> origin/master
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
<<<<<<< HEAD
	 * email). If there is, then it is an invalid input. Returns true if there
	 * is white space, false if there is no white space.
=======
	 * email). If there is, then it is an invalid input.
>>>>>>> origin/master
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
<<<<<<< HEAD
	}

	private void displayPasswordFailureText(int errorType) {
		String message = "";
		switch (errorType) {
		case EMPTY:
			message = getResources().getString(
					R.string.register_no_password_entered);
			break;
		case INVALID:
			message = getResources().getString(
					R.string.register_invalid_password);
		}
		invalidPasswordText.setText(message);
		invalidPasswordLL.setVisibility(View.VISIBLE);
=======
>>>>>>> origin/master
	}
}
