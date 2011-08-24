package com.asa.sms2honeycomb.tablet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.asa.sms2honeycomb.R;

public class LoginActivityTab extends Activity {
	private Button loginButton;
	private Button createAccountButton;
	private EditText usernameField;
	private EditText passwordField;

	private Intent mIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_tablet);

		createAccountButton = (Button) findViewById(R.id.login_create_account_btn_tab);
		loginButton = (Button) findViewById(R.id.login_button_tablet);
		usernameField = (EditText) findViewById(R.id.login_username_field_tablet);
		usernameField = (EditText) findViewById(R.id.login_password_field_tablet);

		createAccountButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				mIntent = new Intent(LoginActivityTab.this,
						RegisterActivityTab.class);
				startActivity(mIntent);
			}
		});

		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				mIntent = new Intent(LoginActivityTab.this,
						MainHoneycombActivity.class);
				startActivity(mIntent);
			}
		});

	}
}
