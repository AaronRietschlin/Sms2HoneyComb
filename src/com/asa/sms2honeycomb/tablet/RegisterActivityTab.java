package com.asa.sms2honeycomb.tablet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.asa.sms2honeycomb.R;

public class RegisterActivityTab extends Activity {
	private Button registerButton; 
	private EditText emailField;
	private EditText usernameField;
	private EditText passwordField;
	
	private Intent mIntent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity_tablet);

		registerButton = (Button) findViewById(R.id.register_button_tablet);
		emailField = (EditText) findViewById(R.id.register_email_field_tab);
		usernameField = (EditText) findViewById(R.id.register_username_field_tab);
		passwordField = (EditText) findViewById(R.id.register_password_field_tab);
		
		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				mIntent = new Intent(RegisterActivityTab.this,
						MainHoneycombActivity.class);
				startActivity(mIntent);
			}
		});
		
	}
}
