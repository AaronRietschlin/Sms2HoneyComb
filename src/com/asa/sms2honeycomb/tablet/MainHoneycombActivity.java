package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.util.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

public class MainHoneycombActivity extends ListActivity {

	private final String TAG = "MainHoneycombActivity";
	private DatabaseAdapter dbAdapter;
	Intent mIntent;
	ListView list;
	Dialog listDialog;

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
			mIntent = new Intent(MainHoneycombActivity.this,
					LoginActivityTab.class);
			startActivity(mIntent);
			finish();
			return (true);

		case R.id.query:
			// TODO add query fuction
			Toast.makeText(this, "Querying server...", Toast.LENGTH_LONG)
					.show();
			QueryMessages();
			return (true);

		case R.id.contacts:
			showDialog();
			Toast.makeText(this, "Contact list", Toast.LENGTH_LONG).show();
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	public void showDialog() {
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = ContactsDialogFragment.newInstance(0);
		newFragment.show(ft, "dialog");

	}

	// TODO TESTING
	public void QueryMessages() {
		final ParseQuery query = new ParseQuery("sms");
		query.whereEqualTo(Preferences.PARSE_USERNAME_ROW, "TestName");
		query.orderByDescending("createdAt");
		query.setLimit(10);
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> messageList, ParseException e) {
				if (e == null) {
					Log.d(TAG, "Retrieved " + messageList.size() + " messages.");
					for (ParseObject messageObject : messageList) {
						String objectId = messageObject.objectId();
						try {
							ParseObject message = query.get(objectId);
							Date time = message.createdAt();
							String timeDB = time.toString();
							String addressDB = message.getString("address");
							String bodyDB = message.getString("body");
							String readDB = message.getString("read");
							String smsIdDB = message.getString("smsId");
							String subjectDB = message.getString("subject");
							String threadIdDB = message.getString("threadId");
							String typeDB = message.getString("type");
							String usernameDB = message.getString("username");
							String totalMessage = "Sent: " + timeDB + "\n"
									+ "Address: " + addressDB + "\n"
									+ "Message : " + bodyDB + "\n";
							System.out.println(totalMessage);
							// add the shit to the sqlitedb
							MessageItem item = new MessageItem(timeDB,
									addressDB, bodyDB, readDB, smsIdDB,
									subjectDB, threadIdDB, typeDB, usernameDB);
							dbAdapter.insertMessageItem(item);
						} catch (ParseException e1) {
							Log.e(TAG, e1.getMessage());
						}
					}
				} else {
					Log.d(TAG, "Error: " + e.getMessage());
				}
			}
		});
	}
	
	// TODO this is old and doesnt work bitchs
	public ArrayList<String> getCotactArrayList() {
		// Get the cursor over every aggregated contact data.
		Cursor dataCursor = this.getContentResolver().query(
				ContactsContract.Data.CONTENT_URI, null, null, null, null);

		// Let the activity manage the cursor lifecycle.
		this.startManagingCursor(dataCursor);

		// Use the convenience properites to get the index of the columns
		int nameIdx = dataCursor
				.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		// Get the phonenumber
		int phoneIdx = dataCursor
				.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

		ArrayList<String> contacts = new ArrayList<String>();
		if (dataCursor.moveToFirst()) {
			do {
				// Extract the name.
				String name = dataCursor.getString(nameIdx);
				// Extract the phone number.
				String phone = dataCursor.getString(phoneIdx);
				// Combine the name and phone number
				String contact = name + " (" + phone + ")";
				// Add the contact to contacts
				contacts.add(contact);
				// Remove the contact if there is no number
				if (phone == null) {
					contacts.remove(contact);
				}

			} while (dataCursor.moveToNext());

			this.stopManagingCursor(dataCursor);
			dataCursor.close();
		}
		return contacts;

	}
}
