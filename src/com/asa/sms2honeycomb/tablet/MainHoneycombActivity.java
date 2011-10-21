package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util.Util;
import com.parse.PushService;

public class MainHoneycombActivity extends ListActivity {

	private final String TAG = "MainHoneycombActivity";
	private DatabaseAdapter dbAdapter;
	Intent mIntent;
	ListView list;
	Dialog listDialog;
	IncomingPushReceiver pushReceiver;

	public static ArrayList<MessageItem> messageResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// In the layout file all the fragments are listed.
		// The weights add up so 3 + 1 = 4, so 1/4 and 3/4.
		// Also the first one listed is the one that is on the far left.
		setContentView(R.layout.activity_tablet_main);

		// Want to send the messages to the phone so they can be sms messages
		PushService.subscribe(this, Util.getPushChannel(
				Util.getUsernameString(), Preferences.TABLET),
				MainHoneycombActivity.class);

		// If the user had been logged out, pull in ALL the users convo data.
		if (Preferences.LAUNCH_FROM_LOGIN) {
			String userName = Util.getUsernameString();
			// QueryParseAsyncTask task = new QueryParseAsyncTask(0, userName);
			// AsyncTask<Void, Void, ArrayList<MessageItem>> asyncTask = task
			// .execute();
			// try {
			// messageResults = asyncTask.get();
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (ExecutionException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// for (MessageItem item : messageResults) {
			// Log.e(TAG, item.toString());
			// }
			// Preferences.LAUNCH_FROM_LOGIN = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// this will inflate the layout of the actionbar
		getMenuInflater().inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout:
			Util.logoutUser();
			
			unregisterReceiver(pushReceiver);
			
			mIntent = new Intent(MainHoneycombActivity.this,
					LoginActivityTab.class);
			startActivity(mIntent);
			finish();
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}
}