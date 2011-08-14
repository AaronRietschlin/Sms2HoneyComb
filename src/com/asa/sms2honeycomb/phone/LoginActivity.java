package com.asa.sms2honeycomb.phone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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

	private Intent mIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_phone);

		loginButton = (Button) findViewById(R.id.login_login_btn_phone);
		cancelButton = (Button) findViewById(R.id.login_cancel_btn_phone);
		usernameField = (EditText) findViewById(R.id.login_username_field_phone);
		passwordField = (EditText) findViewById(R.id.login_password_field_phone);
		final String usernameText = usernameField.getText().toString().trim();
		final String passwordText = passwordField.getText().toString().trim();

		// TODO testing
		usernameField.setText("Nemisis");
		passwordField.setText("12345");

		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (Util.isInTable("username", usernameText)
						&& Util.isInTable("password", passwordText)) {
					Log.d(TAG, "Login succesful for user : " + usernameText);
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
}
