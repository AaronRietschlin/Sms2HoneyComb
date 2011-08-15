package com.asa.sms2honeycomb.phone;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.asa.sms2honeycomb.TableLookupTask;
import com.asa.sms2honeycomb.Util;

// TODO: Return an error if Parse doesnt save the data (right now the app still fowards you on to the login activity when Parse does not save.) 

public class RegisterActivity extends Activity {
	private boolean registrationSuccess = true;
	private final int EMPTY = 0;
	private final int INVALID = 1;
	private final int IN_TABLE = 2;
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
					emailErrorType = EMPTY;
				} else {
					// Check if email is valid.
					if (!isValidEmail(emailText)
							|| containsWhiteSpace(emailText)) {
						invalidEmail = true;
						emailErrorType = INVALID;
					}
				}

				// Check if username was entered.
				if (usernameText.length() == 0) {
					invalidUsername = true;
					nameErrorType = EMPTY;
				} else {
					// Check if username contains whitespace.
					if (containsWhiteSpace(usernameText)) {
						invalidUsername = true;
						nameErrorType = INVALID;
					}
				}

				// Checks if the password was entered.
				if (passwordText.length() == 0) {
					invalidPassword = true;
					passwordErrorType = EMPTY;
				} else {
					// Check if password contains whitespace.
					if (containsWhiteSpace(passwordText)) {
						invalidPassword = true;
						passwordErrorType = INVALID;
					}
				}

				// Checks if email is already in the table.
				// if (Util.isInTable(Preferences.EMAIL_ROW, emailText)) {
				// invalidEmail = true;
				// emailErrorType = IN_TABLE;
				// }

				/*
				 * Check if username and email is already taken by using the
				 * LookupAsyncTask. Doing lookups for username AND email in this
				 * single AsyncTask call to minimize number of background
				 * threads.
				 */
				String[] params = { emailText, usernameText,
						Preferences.LOOKUP_EMAIL };
				TableLookupTask lookupTask = new TableLookupTask(RegisterActivity.this);
				try {
					invalidType = lookupTask.execute(params).get();
					if (invalidType == Preferences.INVALID_EMAIL) {
						invalidEmail = true;
						emailErrorType = IN_TABLE;
					} else if (invalidType == Preferences.INVALID_USERNAME) {
						invalidUsername = true;
						nameErrorType = IN_TABLE;
					} else if (invalidType == Preferences.INVALID_BOTH) {
						invalidEmail = true;
						invalidUsername = true;
						emailErrorType = IN_TABLE;
						nameErrorType = IN_TABLE;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
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
				
				while(lookupTask.getStatus() != AsyncTask.Status.FINISHED){
					Log.d(TAG, "Status is: " + lookupTask.getStatus());
				} 
				
				Log.e(TAG, lookupTask.getStatus().toString());
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
	 * Checks to see if there is white space within the input (both username and
	 * email). If there is, then it is an invalid input. Returns true if there
	 * is white space, false if there is no white space.
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
	}
}
