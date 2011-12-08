package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.MessageListAdapter;
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util.Util;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.parse.SendCallback;

public class MessageFragment extends ListFragment {
	LayoutInflater inflater;
	ViewGroup container;

	private final String TAG = "MessageFragment";
	private DatabaseAdapter dbAdapter;
	public static ArrayAdapter<String> messageAdapter;

	private EditText toField;
	private EditText messageField;
	private Button sendButton;
	private Button addContactButton;

	private Context mContext;
	private ListView mMessageListView;

	private static String phoneNumber;
	private int threadId = 1;

	private final int CONTACT_PICKER_RESULT = 0;

	private int mIndex = 0;

	public static MessageFragment newInstance(String phoneNumber) {
		Log.d(MainHoneycombActivity.TAG, "in MessageFragment newInstance("
				+ phoneNumber + ")");

		MessageFragment mf = new MessageFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putString("phoneNumber", phoneNumber);
		mf.setArguments(args);
		return mf;
	}

	public static MessageFragment newInstance(Bundle bundle) {
		phoneNumber = bundle.getString("phoneNumber");
		return newInstance(phoneNumber);
	}

	public int getShownIndex() {
		return mIndex;
	}

	public void onCreate(Bundle myBundle) {
		if (myBundle != null) {
			for (String key : myBundle.keySet()) {
				Log.d(TAG, "	" + key);
			}
			// This is the phonenumber
			phoneNumber = myBundle.getString("phoneNumber");
			// set the toField for the phoneNumber
			Log.d(TAG, "myBundle contains:" + phoneNumber);
		} else {
			Log.d(TAG, "myBundle is null");
		}
		super.onCreate(myBundle);

		mIndex = getArguments().getInt("index", 0);

		// This will update the reciver when a broadcast is sent from the
		// IncomingPushRecvier telling the listview to update and will only do
		// it if
		// the address is the same as the one being received

		getActivity().registerReceiver(broadcastReceiver,
				new IntentFilter("com.asa.sms2honeycomb.UPDATE_LIST"));
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(TAG, "BROADCAST TO UPDATE LIST RECEIVED");
			QueryParseAsyncTask task = new QueryParseAsyncTask(phoneNumber);
			AsyncTask<Void, Void, ArrayList<MessageItem>> asyncTask = task
					.execute();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d(TAG, " onCreateView.container = " + container);

		// DONT tie this fragment to anyting through the inflater. Andriod takes
		// care of attaching fragments for us. The container isonly passed in so
		// you can know about the container where this View hierarchy is going
		// to go.

		View v = inflater.inflate(R.layout.fragment_message_view_new,
				container, false);
		// inflater.inflate(R.layout.fragment_message_view_new, container);
		mMessageListView = (ListView) v.findViewById(android.R.id.list);

		// IT FUCKING HAS TO BE andriod.R.id.list fucking POS differences
		toField = (EditText) v.findViewById(R.id.phone_to_field);
		messageField = (EditText) v.findViewById(R.id.main_message_felid);

		// Open up the database
		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();

		// TODO for testing only
		// phoneNumber = "1234567890";
		if (phoneNumber == null) {
			Log.e(TAG, "There is no phonenumber.");
		} else {
			Log.d(TAG, "The phonenumber is: " + phoneNumber);
			toField.setText(phoneNumber);
		}

		QueryParseAsyncTask task = new QueryParseAsyncTask(phoneNumber);
		AsyncTask<Void, Void, ArrayList<MessageItem>> asyncTask = task
				.execute();

		// BEGIN Adding contact stuff
		addContactButton = (Button) v
				.findViewById(R.id.message_add_contact_button);

		addContactButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
						Contacts.CONTENT_URI);
				startActivityForResult(contactPickerIntent,
						CONTACT_PICKER_RESULT);
			}
		});
		// END Adding contact stuff.

		sendButton = (Button) v.findViewById(R.id.main_send_btn);

		// This is the send button
		sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final String to = toField.getText().toString().trim();
				final String body = messageField.getText().toString().trim();
				if (to.length() == 0) {
					// User has not specified a user, thus, nothing happens.
					Toast toast = Toast.makeText(getActivity(),
							R.string.message_list_empty_target,
							Toast.LENGTH_LONG);
					toast.show();
				} else {
					if (phoneNumber.length() == 0) {
						phoneNumber = to;
					}

					// update the messageAdapter
					messageAdapter.notifyDataSetChanged();

					// Send the message though parse
					ParseObject outgoingMessage = new ParseObject(
							Preferences.PARSE_TABLE_SMS);
					// Send the phoneNumber
					outgoingMessage.put(Preferences.PARSE_SMS_ADDRESS,
							phoneNumber);
					// Send the messages body
					outgoingMessage.put(Preferences.PARSE_SMS_BODY, body);
					// Send the Username to sort out the messages
					outgoingMessage.put(Preferences.PARSE_USERNAME_ROW,
							Util.getUsernameString());

					// Always mark the message as read.
					outgoingMessage.put(Preferences.PARSE_SMS_READ,
							Preferences.READ);

					outgoingMessage.put(Preferences.PARSE_SMS_THREAD_ID,
							threadId);

					// Always mark the message type as a sent type.
					outgoingMessage.put(Preferences.PARSE_SMS_TYPE,
							Preferences.SENT);

					// Always mark the message as not on the deivce since it
					// will be put on when the push is recived.
					outgoingMessage.put(Preferences.PARSE_SMS_ONDEVICE,
							Preferences.ONDEVICE_FALSE);

					// Save (send) the message to parse.
					outgoingMessage.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							if (e != null) {
								/*
								 * Did not save correctly. Informing user
								 * through toast
								 */
								Log.e(TAG, "Message not successfully saved.");
								Toast toast = Toast.makeText(getActivity(),
										R.string.message_list_unsuccessful,
										Toast.LENGTH_LONG);
								toast.show();
							} else {
								// Send the parsepush if the message was sent to
								// the server right
								ParsePush push = new ParsePush();
								push.setChannel(Util.getPushChannel(
										Util.getUsernameString(),
										Preferences.TABLET));
								// put the address and message within the push
								// message
								push.setMessage("Address: " + to + " Message: "
										+ body);

								// Clear the messageField if/when the text is
								// send
								// It will not clear if the message is not send
								// right
								messageField.setText("");

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
			}
		});
		return v;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Log.d(TAG, "Item clicked: " + id);
		// // When clicked, show a toast with the TextView text
		// Toast.makeText(getActivity().getApplicationContext(),
		// ((TextView) v).getText(), Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onDestroy() {
		// Closing the database adapter and the broadcastreciver when the
		// activity gets destroyed.
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
		dbAdapter.close();
	}

	/**
	 * This is called after the user selects a contact.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Activity activity = getActivity();
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case CONTACT_PICKER_RESULT:
				Cursor cursor = null;
				Uri uri = data.getData();
				Preferences.URI = uri;
				// Get the contactId from the URI
				String contactId = uri.getLastPathSegment();

				/*
				 * Query the Contacts database with the Phone content_uri. The
				 * selection is the Contact_ID that is equal to the contactId
				 * from the uri returned
				 */
				cursor = activity.getContentResolver().query(Phone.CONTENT_URI,
						null, Phone.CONTACT_ID + "=?",
						new String[] { contactId }, null);
				if (cursor.moveToFirst()) {
					String columns[] = cursor.getColumnNames();

					// Iterates throguh the cursor
					// for (String column : columns) {
					// int index = cursor.getColumnIndex(column);
					// Log.v(TAG, "Contact Info - Column: " + column + " == ["
					// + cursor.getString(index) + "]");
					// }
					int phoneIndex = cursor.getColumnIndex(Phone.DATA1);
					int nameIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
					phoneNumber = cursor.getString(phoneIndex);
					String displayName = cursor.getString(nameIndex);
					displayName = displayName + " <" + phoneNumber + ">, ";
					String currentText = toField.getText().toString();
					// If there is current text, then concatenage the new number
					if (currentText.length() > 0) {
						displayName = currentText + displayName;
					}
					toField.setText(displayName);

					// pull the contacts messages into the listview
					QueryParseAsyncTask task = new QueryParseAsyncTask(
							phoneNumber);
					AsyncTask<Void, Void, ArrayList<MessageItem>> asyncTask = task
							.execute();

				} else {
					// Contact has no phone information.
					// TODO : Prompt user to input phone info?
					Util.displayToast(mContext,
							"Contact has no phone information.");
				}
				break;
			}
		} else {
			Log.w(TAG, "Activity returned with no data.");
		}
	}

	public class QueryParseAsyncTask extends
			AsyncTask<Void, Void, ArrayList<MessageItem>> {
		private final String TAG = "MessageFragment.QueryParseAsyncTask";

		private String address;

		private ArrayList<MessageItem> messageResults;

		public QueryParseAsyncTask(String number) {
			address = number;
		}

		@Override
		protected ArrayList<MessageItem> doInBackground(Void... arg0) {
			messageResults = dbAdapter.getMessageArrayList(address);
			Log.d(TAG + ".doInBackground", messageResults.toString());
			return messageResults;
		}

		@Override
		protected void onPostExecute(ArrayList<MessageItem> messageList) {
			messageAdapter = new MessageListAdapter(getActivity(),
					R.layout.message_list_item, mMessageListView, messageList);
			setListAdapter(messageAdapter);
			messageAdapter.notifyDataSetChanged();
		}

	}

}