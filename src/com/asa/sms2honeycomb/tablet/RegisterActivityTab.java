package com.asa.sms2honeycomb.tablet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.asa.sms2honeycomb.R;

public class RegisterActivityTab extends Activity {
	private Button registerButton; 
	private EditText emailField;
	private EditText usernameField;
	private EditText passwordField;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity_tablet);

		
		
	}
}
