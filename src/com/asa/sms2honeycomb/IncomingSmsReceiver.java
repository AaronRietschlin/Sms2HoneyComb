package com.asa.sms2honeycomb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.parse.SendCallback;

/**
 * Broadcast Receiver that forwards incoming messages to server. This class
 * simply takes messages that come from other people (SMS/MMS messages) and
 * forwards those messages to the server. The server then forwards it to the
 * tablet. This class does nothing with incoming messages from the server.
 * 
 * @author Aaron
 * 
 */
public class IncomingSmsReceiver extends BroadcastReceiver {
	// The intent to listen for.
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private final String TAG = "IncomingSmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED)) {
			// Instantiate the SmsManager, which handles all Sms messages
			SmsManager sms = SmsManager.getDefault();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] smsMessages = new SmsMessage[pdus.length];
				for (int pos = 0; pos < pdus.length; pos++) {
					smsMessages[pos] = SmsMessage
							.createFromPdu((byte[]) pdus[pos]);
				}

				String body = "";
				String from = "";
				for (SmsMessage message : smsMessages) {
					body = message.getMessageBody();
					from = message.getOriginatingAddress();
				}
				Log.i(TAG, from + " ~ " + body);

				// Retrieve the device id.
				TelephonyManager manager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String deviceId = manager.getDeviceId();

				// Send to server.
				ParseObject incomingMessage = new ParseObject("IncomingMessage");
				incomingMessage.put("messageBody", body);
				incomingMessage.put("messageFrom", from);
				// TODO: Get device number? incomingMessage.put("messageTo", );
				incomingMessage.put("deviceId", deviceId);
				final String pushBody = body;
				incomingMessage.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e != null) {
							// Did not save correctly.
							Log.e(TAG, "Message not successfully saved.");
						} else {
							// Saved to server correctly.
							ParsePush push = new ParsePush();
							// Set the channel to tablet channel.
							push.setChannel("tabletChannel");
							push.setMessage(pushBody);
							push.sendInBackground(new SendCallback() {
								@Override
								public void done(ParseException e) {
									if (e != null) {
										// Did not send push notification
										// correctly.
										Log.e(TAG,
												"Push notification did not send correctly.", e);
										e.printStackTrace();
									}else{
										Log.d(TAG, "Push sent.");
									}
								}
							});
						}
					}
				});
			}
		}
	}
}
