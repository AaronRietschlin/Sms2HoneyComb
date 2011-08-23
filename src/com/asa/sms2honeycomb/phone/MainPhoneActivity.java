package com.asa.sms2honeycomb.phone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.SendCallback;

public class MainPhoneActivity extends Activity {

	private final String TAG = "MainPhoneActivity";

	TextView messageListText;
	TextView toText;
	TextView messageText;
	EditText toField;
	EditText messageField;
	Button sendButton;
	Button logoutButton;

	ArrayList<String> messageArrayList;
	
	public static DatabaseAdapter dbAdapter;

	Intent mIntent;

	private boolean logoutSuccess;

	public static String timeDB;
	public static String toDB;
	public static String fromDB;
	public static String bodyDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_phone);

		messageListText = (TextView) findViewById(R.id.main_message_list);
		toText = (TextView) findViewById(R.id.main_to_text);
		messageText = (TextView) findViewById(R.id.main_message_text);
		toField = (EditText) findViewById(R.id.main_to_felid);
		messageField = (EditText) findViewById(R.id.main_message_felid);
		sendButton = (Button) findViewById(R.id.main_send_btn);
		logoutButton = (Button) findViewById(R.id.main_logout_btn);

		messageArrayList = new ArrayList<String>();
		
		dbAdapter = new DatabaseAdapter(MainPhoneActivity.this);
		dbAdapter.open();

		PushService.subscribe(this, Util.getPushChannel(
				Util.getUsernameString(), Preferences.TABLET),
				MainPhoneActivity.class);

		sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				// TODO: get strings from the text feilds
				// then send them on to parse and to the push channel(phone)
				// on then on the phone send the messages via a sms message to
				// the to number
				final String to = toField.getText().toString().trim();
				final String body = messageField.getText().toString().trim();

				ParseObject outgoingMessage = new ParseObject("OutgoingMessage");
				outgoingMessage.put("messageTo", to);
				outgoingMessage.put("messageBody", body);
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
				// off of the server will be replaced by the one in the Util
				// section and eventually used in a broadcast reciver to fetch
				// messages from the server
				final ParseQuery query = new ParseQuery("OutgoingMessage");
				query.whereEqualTo(Preferences.PARSE_USERNAME_ROW, "TestName");
				query.orderByDescending("createdAt");
				query.setLimit(10);
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
									//messageArrayList.add(totalMessage);
									//Log.d(TAG, "messageArrayList is : " +
									//messageArrayList.size() + " long.");
									// add the shit to the sqlitedb
									MessageItem item = new MessageItem(timeDB, toDB, fromDB, bodyDB);
									dbAdapter.insertMessageItem(item);
								} catch (ParseException e1) {
									Log.e(TAG, e1.getMessage());
								}
							}
							StringBuilder messageListString = new StringBuilder();
							for (String s : dbAdapter.getMessageArrayList("KEY_TO", "1234567")) {
								messageListString.append(s);
								messageListString.append("\n");
								Log.d(TAG, "String: " + s +
								" has been connected.");
							}
							System.out.println(messageListString.toString());
							messageListText.setText(messageListString
									.toString());
						} else {
							Log.d(TAG, "Error: " + e.getMessage());
						}
					}
				});
			}
		});

		logoutButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Util.logoutUser();
				if (Util.logoutUser()) {
					// unsubscribe to the push channel
					PushService.unsubscribe(MainPhoneActivity.this, Util
							.getPushChannel(Util.getUsernameString(),
									Preferences.TABLET));
					Log.d(TAG, "Log out of user a success");
					mIntent = new Intent(MainPhoneActivity.this,
							LauncherActivity.class);
					startActivity(mIntent);
				} else {
					Log.e(TAG, "Logout of user is a failure");
				}
			}
		});

		if (Preferences.DEBUG) {
			toField.setText("1234567");
			messageField.setText("Just sending a texty text?!?!?");
		}
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
			ParseUser.logOut();
			mIntent = new Intent(MainPhoneActivity.this, LauncherActivity.class);
			startActivity(mIntent);
			finish();

		}
		return false;
	}
}
