package phone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.asa.sms2honeycomb.R;

public class LoginActivity extends Activity {

	private Button loginButton;
	private Button cancelButton;
	private EditText usernameField;
	private EditText passwordField;
	
	private Intent mIntent;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_phone);
		
		loginButton = (Button) findViewById(R.id.login_login_btn_phone);
		cancelButton = (Button) findViewById(R.id.login_cancel_btn_phone);
		
		loginButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				mIntent = new Intent(LoginActivity.this, MainPhoneActivity.class);
				startActivity(mIntent);
				finish();
			}
		});
		cancelButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				finish();
			}
		});
		
	}
	
	
}
