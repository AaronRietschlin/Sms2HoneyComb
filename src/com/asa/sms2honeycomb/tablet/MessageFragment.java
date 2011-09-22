package com.asa.sms2honeycomb.tablet;

import java.util.Date;
import java.util.List;

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
import com.parse.SaveCallback;
import com.parse.SendCallback;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageFragment extends ListFragment {
	
	private Intent mIntent;
	private final String TAG = "MessageFragment";
	private DatabaseAdapter dbAdapter;
	private ArrayAdapter<String> messageAdapter;
	
	private ListView messageListView;
	private EditText toField;
	private EditText messageField;
	private Button sendButton;
	
	private String phonenumber;
	private String timeDB;
	private String toDB;
	private String fromDB;
	private String bodyDB;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getActivity().setContentView(R.layout.fragment_message_view);
		
		// TODO TESTING SHIT
		phonenumber = "1234567";
		
		// Open up the database needs to be above the conversationAdapter
		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();
		
		// IT FUCKING HAS TO BE andriod.R.id.list fucking POS differences
		messageListView = (ListView) getActivity().findViewById(android.R.id.list);
		toField = (EditText) getActivity().findViewById(R.id.phone_to_field);
		messageField = (EditText) getActivity().findViewById(R.id.main_message_felid);
		sendButton = (Button) getActivity().findViewById(R.id.main_send_btn);
		
		// Get the Adapter for the list so iy can be updated separately
		// I dont know why simple_list_item_1 works i copied it from an example
		messageAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,  dbAdapter.getMessageArrayList(phonenumber));
		
		messageAdapter.notifyDataSetChanged();
		
		setListAdapter(messageAdapter);
		
		sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				// TODO: get strings from the text feilds
				// then send them on to parse and to the push channel(phone)
				// on then on the phone send the messages via a sms message to
				// the to number
				toField.setText(phonenumber);
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
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG, "Item clicked: " + id);
		// When clicked, show a toast with the TextView text
		Toast.makeText(getActivity().getApplicationContext(),
				((TextView) v).getText(), Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	public void onDestroy() {
		// Closing the database adapter when the activity gets destroyed.
		super.onDestroy();
		dbAdapter.close();
	}
}
