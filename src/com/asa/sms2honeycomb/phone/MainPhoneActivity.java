package com.asa.sms2honeycomb.phone;

import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.util.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.SendCallback;

public class MainPhoneActivity extends ListActivity {

	private final String TAG = "MainPhoneActivity";

	private ListView messageListView;
	private EditText toField;
	private EditText messageField;
	private Button sendButton;

	private ArrayAdapter<String> messageAdapter;
	private DatabaseAdapter dbAdapter;
	private Intent mIntent;

	private String phonenumber;
	private String timeDB;
	private String toDB;
	private String fromDB;
	private String bodyDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_phone);
		// TODO use BaseAdapter instead of ArrayAdapter so we can update the
		// messages with in the ListView see api demo List8

		// The intent passes on the data to the Bundle when the Activity is
		// created, you have to getExtras from the intent
		/*
		 * mIntent = getIntent(); Bundle extras = mIntent.getExtras();
		 * 
		 * // The phone number is gotten by the key "phonenumber" and put into
		 * the // String phonenumber = extras.getString("phonenumber"); // check
		 * it there is a phone number testing if (phonenumber == null) {
		 * Log.e(TAG,
		 * "The intent from the contacts list did not work phonenumber: " +
		 * phonenumber); } else { Log.d(TAG,
		 * "The number selected from the contacts is: " + phonenumber); }
		 */

		// Open up the database
		dbAdapter = new DatabaseAdapter(MainPhoneActivity.this);
		dbAdapter.open();

		// Subscribe to the needed PushServer
		PushService.subscribe(this, Util.getPushChannel(
				Util.getUsernameString(), Preferences.TABLET),
				MainPhoneActivity.class);

		// IT FUCKING HAS TO BE andriod.R.id.list fucking POS differences
		messageListView = (ListView) findViewById(android.R.id.list);
		toField = (EditText) findViewById(R.id.phone_to_field);
		messageField = (EditText) findViewById(R.id.main_message_felid);
		sendButton = (Button) findViewById(R.id.main_send_btn);

		// Get the Adapter for the list so iy can be updated separately
		messageAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				dbAdapter.getMessageArrayList(phonenumber));

		// TODO I dont know if this works when the bd is updated to updated the
		// listview
		// if the messageAdapter is updated then refresh the data
		messageAdapter.notifyDataSetChanged();

		// Set the list's adapter
		setListAdapter(messageAdapter);

		messageListView = getListView();
		messageListView.setTextFilterEnabled(true);

		sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				// TODO: get strings from the text feilds
				// then send them on to parse and to the push channel(phone)
				// on then on the phone send the messages via a sms message to
				// the to number
//				toField.setText(phonenumber);
				final String to = toField.getText().toString().trim();
				final String body = messageField.getText().toString().trim();

				ParseObject outgoingMessage = new ParseObject(
						Preferences.PARSE_TABLE_SMS);
				Log.e(TAG, "Address to: " + to);
				outgoingMessage.put(Preferences.PARSE_SMS_ADDRESS, to);
				outgoingMessage.put(Preferences.PARSE_SMS_BODY, body);
				outgoingMessage.put(Preferences.PARSE_SMS_READ, 1);
				outgoingMessage.put(Preferences.PARSE_SMS_TYPE, 2);
				// TODO : Generate the
				// SMSID.outgoingMessage.put(Preferences.PARSE_SMS_SMSID, 0);
				// TODO : Figure out the ThreadID.
				// outgoingMessage.put(Preferences.PARSE_SMS_THREAD_ID, 1);
				outgoingMessage.put(Preferences.PARSE_USERNAME_ROW,
						Util.getUsernameString());
				outgoingMessage.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e != null) {
							// Did not save correctly.
							Log.e(TAG, "Message not successfully saved.");
						} else {
							ParsePush push = new ParsePush();
							push.setChannel(Util.getPushChannel(
									Util.getUsernameString(),
									Preferences.TABLET));
							push.setMessage("To: " + to + " Message: " + body);
							push.sendInBackground(new SendCallback() {
								@Override
								public void done(ParseException e) {
									if (e != null) {
										// Did not send push notification
										// correctly.
										Log.e(TAG,
												"Push notification did not send correctly.",
												e);
										e.printStackTrace();
									} else {
										Log.d(TAG, "Push sent.");
									}
								}
							});
						}
					}
				});
				// TODO this is for testing the querying and pulling the info
				// off of the server
				final ParseQuery query = new ParseQuery("OutgoingMessage");
				query.whereEqualTo(Preferences.PARSE_USERNAME_ROW, "TestName");
				query.orderByDescending("createdAt");
				query.setLimit(1);
				query.findInBackground(new FindCallback() {
					public void done(List<ParseObject> messageList,
							ParseException e) {
						if (e == null) {
							Log.d(TAG, "Retrieved " + messageList.size()
									+ " messages.");
							for (ParseObject messageObject : messageList) {
								String objectId = messageObject.objectId();
								try {
									ParseObject message = query.get(objectId);
									Date time = message.createdAt();
									timeDB = time.toString();
									toDB = message.getString("messageTo");
									fromDB = message.getString("messageFrom");
									bodyDB = message.getString("messageBody");
									String totalMessage = "Sent: " + timeDB
											+ "\n" + "To: " + toDB + "\n"
											+ "Message : " + bodyDB + "\n";
									System.out.println(totalMessage);
									// add the shit to the sqlitedb
									MessageItem item = new MessageItem(timeDB,
											toDB, fromDB, bodyDB);
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
		});

		messageListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Preferences.MENU_LOGOUT, 0, "Logout");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case Preferences.MENU_LOGOUT:
			Util.logoutUser();
			mIntent = new Intent(MainPhoneActivity.this, LoginActivity.class);
			startActivity(mIntent);
			finish();
		}
		return false;
	}

	@Override
	public void onDestroy() {
		// Closing the database adapter when the activity gets destroyed.
		super.onDestroy();
		dbAdapter.close();
	}
}
