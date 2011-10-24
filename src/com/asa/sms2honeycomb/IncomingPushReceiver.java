package com.asa.sms2honeycomb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asa.sms2honeycomb.Util.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

public class IncomingPushReceiver extends BroadcastReceiver {

	private final String TAG = "IncomingPushReceiver";

	private DatabaseAdapter dbAdapter;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e(TAG, "Received intent: " + Preferences.PUSH_RECEIVED);

		Preferences.DEVICE_IS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;

<<<<<<< HEAD
=======
		// Get the context from the onReceive
		dbAdapter = new DatabaseAdapter(context);
		// Open the database
		dbAdapter.open();

>>>>>>> origin/master
		QueryParseAsyncTask queryParse = new QueryParseAsyncTask(
				Preferences.DEVICE_IS_HONEYCOMB);
		queryParse.execute();
	}

	public class QueryParseAsyncTask extends AsyncTask<Void, Void, Void> {

		private boolean isHoneycomb;

		public QueryParseAsyncTask(boolean honeycomb) {
			isHoneycomb = honeycomb;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if (isHoneycomb) {
				final ParseQuery query = new ParseQuery(
						Preferences.PARSE_TABLE_SMS);
				// Sort the Parse Object so only the username of the current
				// user
				// can be accessed.
				query.whereEqualTo(Preferences.PARSE_USERNAME_ROW,
						Util.getUsernameString());
				// The most recent message added will be on top.
				query.orderByDescending("createdAt");
				// Only need to get one message from the server, each message
				// will
				// be from a push
				query.setLimit(1);
				try {
					List<ParseObject> messageList = query.find();
					for (ParseObject messageObject : messageList) {
						// Get the parse object id
						String objectId = messageObject.objectId();
						// with the objectid you can query the
						// server
						ParseObject message = query.get(objectId);
						// Get the time the message was added to the
						// server
						Date time = message.createdAt();
						// Format the time to (Fri Jan 13 12:00)
						SimpleDateFormat sdf = new SimpleDateFormat(
								"E MMM dd hh:mm");
						String formatedTime = sdf.format(time);
						String timeDB = formatedTime.toString();
						String addressDB = message.getString("address");
						String bodyDB = message.getString("body");
						int readDB = message.getInt("read");
						int smsIdDB = message.getInt("smsId");
						String subjectDB = message.getString("subject");
						int threadIdDB = message.getInt("threadId");
						int typeDB = message.getInt("type");
						String usernameDB = message.getString("username");
						// Display the total message queryed for
						// logging
						String totalMessage = "Sent: " + timeDB + "\n"
								+ "Address: " + addressDB + "\n" + "Message : "
								+ bodyDB + "\n";
						Log.d(TAG, "New message is: " + totalMessage);
						// Get the MessageItem object so you can
						// create
						// the db entry.
						MessageItem item = new MessageItem(timeDB, addressDB,
								bodyDB, readDB, smsIdDB, subjectDB, threadIdDB,
								typeDB, usernameDB);
						// Insert the MessageItem into the
						// sms2honeycomb.db.
						dbAdapter.insertMessageItem(item);

					}
				} catch (ParseException e) {
					// TODO - Handle situation where querying server failed
					e.printStackTrace();
				}
			} else {
				Log.e(TAG, "The device is not honeycomb is its a phone.");
				// If the device is not a tablet it is a phone so you pull from
				// the
				// server, but then send a sms message from the data recived.
				// We want to query the OutgoingMessage table
				final ParseQuery query = new ParseQuery(
						Preferences.PARSE_TABLE_SMS);
				// Sort the Parse Object so only the username of the current
				// user
				// can be accessed.
				query.whereEqualTo(Preferences.PARSE_USERNAME_ROW,
						Util.getUsernameString());
				// The most recent message added will be on top.
				query.orderByDescending("createdAt");
				// Only need to get one message from the server, each message
				// will be from a push
				query.setLimit(1);
				try {
					List<ParseObject> messageList = query.find();
					// For the ParseObjects quering get all that needs
					// to be done.
					for (ParseObject messageObject : messageList) {
						// Get the parse object id
						String objectId = messageObject.objectId();
						// with the objectid you can query the
						// server
						ParseObject message = query.get(objectId);
						// Get the time the message was created at
						// for
						// logging, do not need a time to send a
						// message.
						Date time = message.createdAt();
						String timeString = time.toString();
						// Get who the message is coming from
						// (phonenumber).
						String address = message
								.getString(Preferences.PARSE_SMS_ADDRESS);
						// Get the body of the message
						String body = message
								.getString(Preferences.PARSE_SMS_BODY);
						// Display the total message queryed for
						// logging
						String totalMessage = "Sent: " + timeString + "\n"
								+ "To: " + address + "\n" + "Message : " + body
								+ "\n";
						Log.d(TAG, "New message is: " + totalMessage);
						// get the smsmanager as sms
						SmsManager sms = SmsManager.getDefault();
						// If the message is over the 160 Char limit
						// it
						// will be choped up.
						if (body.length() > 160) {
							// Chops up the message
							sms.divideMessage(body);
							// Send the sms message in its parts
							sms.sendMultipartTextMessage(address, null,
									sms.divideMessage(body), null, null);
						} else {
							// Sends the message without cutting it
							sms.sendTextMessage(address, null, body, null, null);
						}
					}
				} catch (ParseException e) {
					// TODO - Handle situation where querying server failed
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void params) {
			if (isHoneycomb) {
				Log.d(TAG,
						"TextToTab - IncomingPushReceiver in honeycomb is done.");
			} else {
				Log.d(TAG,
						"TextToTab - IncomingPushReceiver in honeycomb is done.");
			}
		}

	}

}
