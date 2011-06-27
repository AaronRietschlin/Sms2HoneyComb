package com.asa.sms2honeycomb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;


public class IncomingSmsReceiver extends BroadcastReceiver {
	//The intent to listen for.
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(SMS_RECEIVED)){
			SmsManager sms = SmsManager.getDefault();
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] smsMessages = new SmsMessage[pdus.length];
				for(int pos = 0; pos < pdus.length; pos++){
					smsMessages[pos] = SmsMessage.createFromPdu((byte[]) pdus[pos]);
				}
				
				for(SmsMessage message : smsMessages){
					String body = message.getMessageBody();
					String from = message.getOriginatingAddress();
					Log.i("BroadCastReceiver", body);
				}
			}
		}
	}

}
