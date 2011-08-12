package com.asa.sms2honeycomb.phone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.asa.sms2honeycomb.R;

public class LauncherActivity extends Activity{
	Button registerButton;
	Button loginButton;
	Intent mIntent; 
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher_phone);
		
		registerButton = (Button) findViewById(R.id.launcher_register_btn_phone);
		loginButton = (Button) findViewById(R.id.launcher_login_btn_phone);
		
		registerButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				mIntent = new Intent(LauncherActivity.this, RegisterActivity.class);
				startActivity(mIntent);
			}
		});
		loginButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				mIntent = new Intent(LauncherActivity.this, LoginActivity.class);
				startActivity(mIntent);
			}
		});
	}
}
