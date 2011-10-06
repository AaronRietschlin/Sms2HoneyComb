package com.asa.sms2honeycomb.tablet;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.util.Util;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageFragment extends ListFragment {
	LayoutInflater inflater;
	ViewGroup container;

	private final String TAG = "MessageFragment";
	private DatabaseAdapter dbAdapter;
	private ArrayAdapter<String> messageAdapter;

	private ListView messageListView;
	private EditText toField;
	private EditText messageField;
	private Button sendButton;

	private String phonenumber;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflates the main.xml resource, but the default ListView is still
		// generated on top of this view.

		inflater.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.fragment_message_view, container);
		View view = inflater.inflate(R.layout.fragment_message_view, null);

		// TODO TESTING SHIT
		phonenumber = "1234567";

		// Open up the database needs to be above the conversationAdapter
		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();

		// Get the Adapter for the list so iy can be updated separately
		// I dont know why simple_list_item_1 works i copied it from an example
		messageAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,
				dbAdapter.getMessageArrayList(phonenumber));

		messageAdapter.notifyDataSetChanged();

		setListAdapter(messageAdapter);

		// IT FUCKING HAS TO BE andriod.R.id.list fucking POS differences
		messageListView = (ListView) view.findViewById(android.R.id.list);
		toField = (EditText) view.findViewById(R.id.phone_to_field);
		messageField = (EditText) view.findViewById(R.id.main_message_felid);
		sendButton = (Button) view.findViewById(R.id.main_send_btn);

		sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
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
			}
		});
		return view;
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
