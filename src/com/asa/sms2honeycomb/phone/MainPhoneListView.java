package com.asa.sms2honeycomb.phone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainPhoneListView extends ListActivity {

	ListView messageListView;
	ArrayList<String> messageArrayList;

	public static String timeDB;
	public static String toDB;
	public static String fromDB;
	public static String bodyDB;

	private final String TAG = "MainPhoneListView";

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				getMessageArrayList()));

		messageListView = getListView();
		messageListView.setTextFilterEnabled(true);
		
		messageListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();

			}
		});
	}

	public ArrayList<String> getMessageArrayList() {
		messageArrayList = new ArrayList<String>();
		final ParseQuery query = new ParseQuery("OutgoingMessage");
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
							timeDB = time.toString();
							toDB = message.getString("messageTo");
							fromDB = message.getString("messageFrom");
							bodyDB = message.getString("messageBody");
							String totalMessage = "Sent: " + timeDB + "\n"
									+ "To: " + toDB + "\n" + "Message : "
									+ bodyDB + "\n";
							System.out.println(totalMessage);
							messageArrayList.add(totalMessage);
							Log.d(TAG, "messageArrayList is : " +
							messageArrayList.size() + " long.");
						} catch (ParseException e1) {
							Log.e(TAG, e1.getMessage());
						}
					}
				} else {
					Log.d(TAG, "Error: " + e.getMessage());
				}
			}
		});
		return messageArrayList;
	}
}