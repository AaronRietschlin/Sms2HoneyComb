package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.Preferences;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class QueryParseAsyncTask extends
		AsyncTask<Void, Void, ArrayList<MessageItem>> {
	private final String TAG = "QueryParseAsyncTask";
	private final int SMS = 0;
	private final int THREAD = 1;

	private int queryType;
	private String username;
	private DatabaseAdapter dbAdapter;
	private Context context;
	
	private static ArrayList<MessageItem> messageResults;

	public QueryParseAsyncTask(int type, String user) {
		queryType = type;
		username = user;
	}

	@Override
	protected ArrayList<MessageItem> doInBackground(Void... arg0) {
		if (queryType == SMS) {
			ParseQuery querySms = new ParseQuery(Preferences.PARSE_TABLE_SMS);
			ParseQuery queryThread = new ParseQuery(
					Preferences.PARSE_TABLE_THREAD);
			
			dbAdapter = new DatabaseAdapter(context);
			dbAdapter.open();
			
			querySms.whereEqualTo(Preferences.PARSE_USERNAME_ROW, username);
			final String messageBody, messageAddress;
			final Date outgoingMessageDate;
			messageResults = new ArrayList<MessageItem>();
			// Begin query for messages.
			List<ParseObject> objects;
			try {
				objects = querySms.find();
				if (Preferences.DEBUG)
					Log.d(TAG, "Message size: " + objects.size());
				for (ParseObject messageObject : objects) {
					MessageItem messageItem = new MessageItem();
					messageItem.setMessageBody(messageObject
							.getString(Preferences.PARSE_SMS_SUBJECT));
					messageItem.setMessageUsername(messageObject
							.getString(username));
					messageItem.setMessageAddress(messageObject
							.getString(Preferences.PARSE_SMS_ADDRESS));
					messageItem.setMessageTime(messageObject.createdAt()
							.toLocaleString());
					messageResults.add(messageItem);
					String str = messageItem.toString();
					Log.d(TAG, "MessageItem - Size: " + messageResults.size());
					
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dbAdapter.close();
		return messageResults;
	}
}
