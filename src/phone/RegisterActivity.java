package phone;

import java.util.List;

import android.app.Activity;
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

	private Button registerButton;
	private Button cancelButton;
	private EditText emailField;
	private EditText usernameField;
	private EditText passwordField;
	private TextView invalidEmail;
	private TextView invalidUsername;
	private TextView invalidPassword;

	private boolean registrationSuccess;
	private int errorReason = 0;
	private final int EMPTY_USERNAME = 1;
	private final int EMPTY_PASSWORD = 2;
	private final int EMPTY_EMAIL = 3;
	private final String TAG = "RegisterActivity";
	private boolean isInTable = false;
	private boolean DEBUG = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity_phone);

		registerButton = (Button) findViewById(R.id.login_login_btn_phone);
		cancelButton = (Button) findViewById(R.id.login_cancel_btn_phone);
		emailField = (EditText) findViewById(R.id.register_email_phone);
		usernameField = (EditText) findViewById(R.id.register_username_phone);
		passwordField = (EditText) findViewById(R.id.register_password_phone);

		// TODO: For Testing purposes
		emailField.setText("email@email.com");
		usernameField.setText("Nemisis");
		passwordField.setText("12345");

		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				registrationSuccess = registerUser();
				finish();
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});

	}
	
	private boolean registerUser() {
		final String emailText = emailField.getText().toString().trim();
		String usernameText = usernameField.getText().toString().trim();
		String passwordText = passwordField.getText().toString().trim();
		ParseObject userTable = new ParseObject("UserTable");

		
		ParseQuery query = new ParseQuery("UserTable");
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for(ParseObject object : objects){
						if(object.getString("email").compareTo(emailText) == 0){
							isInTable = true;
							break;
						}
					}
					Log.e(TAG, "IsInTable = " + String.valueOf(isInTable));
				} else {
					Log.e(TAG, "Retrieve from database failed.");
				}
			}
		});

		userTable.put("email", emailText);
		userTable.put("username", usernameText);
		userTable.put("password", passwordText); // TODO: Encrypt passwords!
		userTable.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Log.e(TAG, "Save failed.");
					e.printStackTrace();
				} else {
					if (DEBUG)
						Log.e(TAG, "Save success.");
				}

			}
		});
		return true;
	}
}
