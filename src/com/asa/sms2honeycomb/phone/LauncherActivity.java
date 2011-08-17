package com.asa.sms2honeycomb.phone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.asa.sms2honeycomb.R;
import com.parse.ParseUser;

public class LauncherActivity extends Activity{
	Button registerButton;
	Button loginButton;
	Intent mIntent; 
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser == null){
			//User is not logged in.
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
		}else{
			//User is logged in.
			mIntent = new Intent(LauncherActivity.this, MainPhoneActivity.class);
			startActivity(mIntent);
		}
	}
}
