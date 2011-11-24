package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.IncomingPushReceiver;
import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.MessageListAdapter;
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.SettingsActivity;
import com.asa.sms2honeycomb.Util.Util;
import com.asa.sms2honeycomb.tablet.MessageFragment.QueryParseAsyncTask;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

public class MainHoneycombActivity extends Activity {

	public static final String TAG = "MainHoneycombActivity";
	private DatabaseAdapter dbAdapter;
	Intent mIntent;
	ListView list;
	Dialog listDialog;

	public static ArrayList<MessageItem> messageResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// In the layout file all the fragments are listed.
		// The weights add up so 3 + 1 = 4, so 1/4 and 3/4.
		// Also the first one listed is the one that is on the far left.
		setContentView(R.layout.activity_tablet_main);

		// Open up the database
		dbAdapter = new DatabaseAdapter(this);
		dbAdapter.open();

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

	public void onAttachFragment(Fragment fragment) {
		Log.d(TAG, "onAttachFragment. fragment id = " + fragment.getId());
		super.onAttachFragment(fragment);
	}

	// landscape = true, portrait = false
	public boolean isMultiPane() {
		return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

	}

	public void showMessageFragment(int index) {
		Log.d(TAG, "showMessageFragment ( " + index + ")");

		if (isMultiPane()) {
			// Check what fragment is show
			MessageFragment message = (MessageFragment) getFragmentManager()
					.findFragmentById(R.id.message);
			if (message == null) {
				// Make a new fragment to show this selection.
				message = MessageFragment.newInstance(index);

				// Execute a transaction, replacing any existing fragment with
				// this one inside the frame
				Log.d(TAG, "about to run FragmentTransaction...");
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				// ft.addToBackStatc("message");
				ft.replace(R.id.message, message);
				ft.commit();
			}
		} else {
			// In portate mode you need to lauch the messageactivty to display
			// the messageview
			Intent intent = new Intent();
			intent.setClass(this, MessageActivity.class);
			intent.putExtra("index", index);
			startActivity(intent);
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
			PushService.unsubscribe(this, Util.getPushChannel(
					Util.getUsernameString(), Preferences.TABLET));

			Util.logoutUser();

			mIntent = new Intent(MainHoneycombActivity.this,
					LoginActivityTab.class);
			startActivity(mIntent);
			finish();
			return (true);
		case R.id.menu_new_message:
			showMessageFragment(0);
			return (true);
		case R.id.settings:
			mIntent = new Intent(MainHoneycombActivity.this,
					SettingsActivity.class);
			Bundle extras = new Bundle();
			extras.putBoolean("isTablet", true);
			mIntent.putExtras(extras);
			startActivity(mIntent);
			return true;

		case R.id.menu_query_all:
			QueryParseAsyncTask task = new QueryParseAsyncTask(0,
					Util.getUsernameString());
			AsyncTask<Void, Void, ArrayList<MessageItem>> asyncTask = task
					.execute();
		}

		return (super.onOptionsItemSelected(item));
	}
	
	// TODO delete this for TESTING
	public class QueryParseAsyncTask extends
			AsyncTask<Void, Void, ArrayList<MessageItem>> {
		private final String TAG = "QueryParseAsyncTask";
		private final int SMS = 0;
		private final int THREAD = 1;

		private int queryType;
		private String username;

		// private ArrayList<MessageItem> messageResults;

		public QueryParseAsyncTask(int type, String user) {
			queryType = type;
			username = user;
		}

		@Override
		protected ArrayList<MessageItem> doInBackground(Void... arg0) {
			if (queryType == SMS) {
				ParseQuery querySms = new ParseQuery(
						Preferences.PARSE_TABLE_SMS);

				querySms.whereEqualTo(Preferences.PARSE_USERNAME_ROW, username);
				messageResults = new ArrayList<MessageItem>();
				// Begin query for messages.
				List<ParseObject> objects;
				try {
					objects = querySms.find();
					if (Preferences.DEBUG)
						Log.d(TAG, "Message size: " + objects.size());
					for (ParseObject messageObject : objects) {
						// TODO: Add the rest of the db items.
						MessageItem messageItem = new MessageItem();
						
						messageItem.setMessageBody(messageObject
								.getString(Preferences.PARSE_SMS_BODY));
						
						messageItem.setMessageUsername(messageObject
								.getString(username));
						
						messageItem.setMessageAddress(messageObject
								.getString(Preferences.PARSE_SMS_ADDRESS));
						
						messageItem.setMessageTime(messageObject.createdAt()
								.toLocaleString());
						
						messageItem.setMessageType(messageObject
								.getInt(Preferences.PARSE_SMS_TYPE));
						
						messageItem.setMessageOnDevice(messageObject
								
								.getInt(Preferences.PARSE_SMS_ONDEVICE));
						messageResults.add(messageItem);
						
						String str = messageObject.toString();
						
						Log.d("MainHoneyCombActivity." + TAG, "TextToTab - " + str);

						dbAdapter.insertMessageItem(messageItem);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dbAdapter.close();
			return messageResults;
		}

		@Override
		protected void onPostExecute(ArrayList<MessageItem> messageList) {
			Log.d(TAG, "QueryAll completed");
		}

	}

	@Override
	public void onDestroy() {
		// Closing the database adapter when the activity gets destroyed.
		super.onDestroy();
		dbAdapter.close();
	}
}