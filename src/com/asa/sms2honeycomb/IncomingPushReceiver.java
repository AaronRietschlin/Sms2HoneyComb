package com.asa.sms2honeycomb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.asa.sms2honeycomb.util.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class IncomingPushReceiver extends BroadcastReceiver {

	// TODO: The intent to listen for
	public static final String PUSH_RECEIVED = "com.asa.IncomingPushReceiver.PUSH_RECEIVED";

	private final String TAG = "IncomingPushReceiver";

	private DatabaseAdapter dbAdapter;

	public static String timeDB;
	public static String toDB;
	public static String fromDB;
	public static String bodyDB;

	@Override
	public void onReceive(Context context, Intent intent) {
		// Get the context from the onReceive
		dbAdapter = new DatabaseAdapter(context);
		// Open the database
		dbAdapter.open();

		Log.d(TAG, "IncomingPushReceiver has been triggered");
		if (Preferences.DEVICE_IS_HONEYCOMB) {
			/*
			 * If device is a tablet it will query the server on the receving of
			 * the push intent. When this happends the message will be pulled
			 * from the server, then stored in the sms2honeycomb.db so it can
			 * later be used in the application. We what to query the
			 * IncommingMessage table
			 */
			final ParseQuery query = new ParseQuery("IncommingMessage");
			// Sort the Parse Object so only the username of the current user
			// can be accessed.
			query.whereEqualTo(Preferences.PARSE_USERNAME_ROW,
					Util.getUsernameString());
			// The most recent message added will be on top.
			query.orderByDescending("createdAt");
			// Only need to get one message from the server, each message will
			// be from a push
			query.setLimit(1);
			query.findInBackground(new FindCallback() {
				public void done(List<ParseObject> messageList, ParseException e) {
					if (e == null) {
						// For the ParseObjects quering get all that needs to be
						// done.
						for (ParseObject messageObject : messageList) {
							// Get the parse object id
							String objectId = messageObject.objectId();
							try {
								// with the objectid you can query the server
								ParseObject message = query.get(objectId);
								// Get the time the message was added to the server
								Date time = message.createdAt();
								// Format the time to (Fri Jan 13 12:00)
								SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd hh:mm");
								String formatedTime = sdf.format(time);
								timeDB = formatedTime.toString();
								// Do not need this since it will be null
								toDB = message.getString("messageTo");
								// Get who the message is from (phonenumber).
								fromDB = message.getString("messageFrom");
								// Get the body of the message
								bodyDB = message.getString("messageBody");
								// Display the total message queryed for logging
								String totalMessage = "Sent: " + timeDB + "\n"
										+ "To: " + fromDB + "\n" + "Message : "
										+ bodyDB + "\n";
								Log.d(TAG, "New message is: " + totalMessage);
								// Get the MessageItem object so you can create
								// the db entry.
								MessageItem item = new MessageItem(timeDB,
										toDB, fromDB, bodyDB);
								// Insert the MessageItem into the
								// sms2honeycomb.db.
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
		} else {
			// If the device is not a tablet it is a phone so you pull from the
			// server, but then send a sms message from the data recived.
			// We want to query the OutgoingMessage table
			final ParseQuery query = new ParseQuery("OutgoingMessage");
			// Sort the Parse Object so only the username of the current user
			// can be accessed.
			query.whereEqualTo(Preferences.PARSE_USERNAME_ROW,
					Util.getUsernameString());
			// The most recent message added will be on top.
			query.orderByDescending("createdAt");
			// Only need to get one message from the server, each message will
			// be from a push
			query.setLimit(1);
			query.findInBackground(new FindCallback() {
				public void done(List<ParseObject> messageList, ParseException e) {
					if (e == null) {
						// For the ParseObjects quering get all that needs to be
						// done.
						for (ParseObject messageObject : messageList) {
							// Get the parse object id
							String objectId = messageObject.objectId();
							try {
								// with the objectid you can query the server
								ParseObject message = query.get(objectId);
								// Get the time the message was created at for
								// logging, do not need a time to send a
								// message.
								Date time = message.createdAt();
								String timeString = time.toString();
								// Get who the message is coming from
								// (phonenumber).
								String to = message.getString("messageTo");
								// Get the body of the message
								String body = message.getString("messageBody");
								// Display the total message queryed for logging
								String totalMessage = "Sent: " + timeString
										+ "\n" + "To: " + to + "\n"
										+ "Message : " + body + "\n";
								Log.d(TAG, "New message is: " + totalMessage);
								// get the smsmanager as sms
								SmsManager sms = SmsManager.getDefault();
								// If the message is over the 160 Char limit it
								// will be choped up.
								if (body.length() > 160) {
									// Chops up the message
									sms.divideMessage(body);
									// Send the sms message in its parts
									sms.sendMultipartTextMessage(to, null,
											sms.divideMessage(body), null, null);
								} else {
									// Sends the message without cutting it
									sms.sendTextMessage(to, null, body, null,
											null);
								}

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

	}

}
