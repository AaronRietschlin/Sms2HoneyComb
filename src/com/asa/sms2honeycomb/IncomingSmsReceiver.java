package com.asa.sms2honeycomb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

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

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED)) {
			//Instantiate the SmsManager, which handles all Sms messages
			SmsManager sms = SmsManager.getDefault();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] smsMessages = new SmsMessage[pdus.length];
				for (int pos = 0; pos < pdus.length; pos++) {
					smsMessages[pos] = SmsMessage
							.createFromPdu((byte[]) pdus[pos]);
				}

				for (SmsMessage message : smsMessages) {
					String body = message.getMessageBody();
					String from = message.getOriginatingAddress();
					Log.i("BroadCastReceiver", from + " ~ " + body);

					// send message manually
					sms.sendTextMessage("9376237833", null, body, null, null);
				}
			}

		}
	}
}
