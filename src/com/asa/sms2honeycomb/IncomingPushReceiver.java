package com.asa.sms2honeycomb;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IncomingPushReceiver extends BroadcastReceiver {
	
	// TODO: The intent to listen for
	public static final String PUSH_RECEIVED = "com.asa.IncomingPushReceiver.PUSH_RECEIVED";
	
	private final String  TAG = "IncomingPushReceiver";
	private static boolean DEVICE_IS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	private String currentUser = "TestName";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO everything decided if the device is honeycomb or a phone
		// phone pulls the message from the server and sends the most recent one as a sms message
		// then in the table added sent feild = true from false
		// honeycomb onreceive pulls the most recent messages off of the server and displays them
		// return strings of from, to and the body.
		Log.d(TAG, "Is the IncomingPushReceiver working?");
		 if (DEVICE_IS_HONEYCOMB) {
			 // quries the server and gives the combined string with the 
			 // From: 1234567890
			 // Time: time
			 // Message: TextyText
			 
			 final String to = null;
			 final String body = null;
			 
			 ParseQuery query = new ParseQuery("IncomingMessage");
			 query.whereEqualTo(Preferences.PARSE_USERNAME_ROW, currentUser);
			 query.orderByDescending("createdAt");
			 query.findInBackground(new FindCallback() {
			     public void done(List<ParseObject> messageList, ParseException e) {
			         if (e == null) {
			             Log.d(TAG, "Retrieved " + messageList.size() + " messages");
			             Log.d(TAG, messageList.toString());
			         } else {
			             Log.d(TAG, "Error: " + e.getMessage());
			         }
			     }
			 });
		 } else {
			 ParseQuery query = new ParseQuery("OutgoingMessage");
			 query.whereEqualTo(Preferences.PARSE_USERNAME_ROW, currentUser);
			 query.orderByDescending("createdAt");
			 query.setLimit(1);
			 query.findInBackground(new FindCallback() {
			     public void done(List<ParseObject> messageList, ParseException e) {
			         if (e == null) {
			             Log.d(TAG, "Retrieved " + messageList.size() + " messages");
			             Log.d(TAG, messageList.toString());
//			             SmsManager smsManager = SmsManager.getDefault();
//			             // TODO: get the list to a string and get to and body out of it
//			             String smsNumber = to;
//			             String smsText = body;
//			             smsManager.sendTextMessage(smsNumber, null, smsText, null, null);
			         } else {
			             Log.d(TAG, "Error: " + e.getMessage());
			         }
			     }
			 });
		 }
		
	}

}
