package phone;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.R.layout;
import com.asa.sms2honeycomb.tablet.MainHoneycombActivity;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;

public class MainPhoneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: Start the phone activity
		// I'm thinking this will just direct to the settings page.
		setContentView(R.layout.main);
		// Initialize the parse object.

		PushService.subscribe(this, "tabletChannel", MainHoneycombActivity.class);
//		Log.e("TEST", "TEST");
//		ParseObject testObject = new ParseObject("TestObject");
//		testObject.put("foo", "bar");
//		testObject.saveInBackground();
//		ParseObject newObject = new ParseObject("Message");
//		newObject
//				.put("Message",
//						"You get the idea, get over here right now, its friday and we are starting early as fuck. lets have fun, lets get shwasted. We are gona pregame here then maybe hit up some bars, so be here.You get the idea, get over here right now, its friday and we are starting early as fuck. lets have fun, lets get shwasted. We are gona pregame here then maybe hit up some bars, so be here.You get the idea, get over here right now, its friday and we are starting early as fuck. lets have fun, lets get shwasted. We are gona pregame here then maybe hit up some bars, so be here.You get the idea, get over here right now, its friday and we are starting early as fuck. lets have fun, lets get shwasted. We are gona pregame here then maybe hit up some bars, so be here.You get the idea, get over here right now, its friday and we are starting early as fuck. lets have fun, lets get shwasted. We are gona pregame here then maybe hit up some bars, so be here.");
//		newObject.saveInBackground();
	}
}
