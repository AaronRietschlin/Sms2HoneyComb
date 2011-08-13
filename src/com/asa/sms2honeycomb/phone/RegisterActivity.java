package com.asa.sms2honeycomb.phone;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asa.sms2honeycomb.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class RegisterActivity extends Activity {

	private boolean registrationSuccess;
	private int errorReason = 0;
	private final int EMPTY_USERNAME = 1;
	private final int EMPTY_PASSWORD = 2;
	private final int EMPTY_EMAIL = 3;
	private final String TAG = "RegisterActivity";
	boolean isInTable;
	private boolean DEBUG = true;

	private Button registerButton;
	private Button cancelButton;
	private TextView invalidUsername;
	private TextView invalidEmail;
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
		invalidUsername = (TextView) findViewById(R.id.register_invalid_username_phone);
		invalidEmail = (TextView) findViewById(R.id.register_invalid_email_phone);
		emailField = (EditText) findViewById(R.id.register_email_phone);
		usernameField = (EditText) findViewById(R.id.register_username_phone);
		passwordField = (EditText) findViewById(R.id.register_password_phone);

		final String emailText = emailField.getText().toString().trim();
		final String usernameText = usernameField.getText().toString().trim();
		final String passwordText = passwordField.getText().toString().trim();

		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

				if (checkEmail(emailText) == false) {
					invalidEmail.setText("Inputed email is not an email.");
				}

				if (checkWhiteSpace(emailText) == true) {
					invalidEmail.setText("Inputed email is not vaild.");
				}

				if (checkWhiteSpace(usernameText) == true) {
					invalidUsername.setText("Inputed username is not vaild.");
				}

				if (checkWhiteSpace(passwordText) == true) {
					// TODO textview
				}

				if ((checkEmail(emailText) == true)
						&& (checkWhiteSpace(emailText) == false)
						&& (checkWhiteSpace(usernameText) == false)
						&& (checkWhiteSpace(passwordText) == false)
						&& (checkInTable("email", emailText) == false)
						&& (checkInTable("username", usernameText) == false)) {

					pushToTable(emailText, usernameText, passwordText);
					mIntent = new Intent(RegisterActivity.this,
							LoginActivity.class);
					startActivity(mIntent);
				} else {
					invalidEmail.setText("Inputed email is already in use.");
					invalidUsername
							.setText("Inputed username is already in use.");
				}
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});

	}

	public boolean checkEmail(String email) {
		String atChar = "@";
		String dotChar = ".";
		int index1 = email.indexOf(atChar);
		int index2 = email.indexOf(dotChar);
		if ((index1 != -1) && (index2 != -1)) {
			// contatins @ and a . so vaild
			Log.d(TAG, "The inputed email is vaild: " + email);
			return true;
		} else {
			Log.d(TAG, "The inputed email is not vaild: " + email);
			return false;
		}
	}

	public boolean checkWhiteSpace(String input) {
		if (input != null) {
			for (int i = 0; i < input.length(); i++) {
				if (Character.isWhitespace(input.charAt(i))) {
					Log.d(TAG, "There is whitespace in the input: " + input);
					return true;
				} else {
					Log.d(TAG, "There is no whitespace in the input: " + input);
					return false;
				}
			}
		}
		Log.d(TAG, "There is no input, it is blank: " + input);
		return true;
	}

	public boolean checkInTable(final String item, final String input) {
		ParseQuery query = new ParseQuery("UserTable");
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (ParseObject object : objects) {
						if (object.getString(item).compareTo(input) == 0) {
							isInTable = true;
							break;
						} else {
							isInTable = false;
							break;
						}
					}
					Log.d(TAG,
							"IsInTable " + input + " = "
									+ String.valueOf(isInTable));
				} else {
					Log.d(TAG, "Retrieve from database failed.");
				}
			}
		});
		if (isInTable == true) {
			return true;
		} else {
			return false;
		}
	}

	public void pushToTable(String email, String username, String password) {
		ParseObject userTable = new ParseObject("UserTable");
		userTable.put("email", email);
		userTable.put("username", username);
		userTable.put("password", password); // TODO: Encrypt passwords!
		userTable.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Log.d(TAG, "Save failed.");
					e.printStackTrace();
				} else {
					if (DEBUG)
						Log.d(TAG, "Save success.");
				}
			}
		});
	}
}
