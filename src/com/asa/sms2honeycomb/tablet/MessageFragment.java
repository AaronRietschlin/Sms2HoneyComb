package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
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

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util.Util;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SendCallback;

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
	private Button addContactButton;

	private Context mContext;

	private String phoneNumber;
	private final int CONTACT_PICKER_RESULT = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflates the main.xml resource, but the default ListView is still
		// generated on top of this view.
		mContext = inflater.getContext();
		inflater.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.fragment_message_view, container);
		View view = inflater.inflate(R.layout.fragment_message_view, null);

		// TODO TESTING SHIT
		phoneNumber = "1234567";

		// Open up the database needs to be above the conversationAdapter
		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();

		// Get the Adapter for the list so iy can be updated separately
		// I dont know why simple_list_item_1 works i copied it from an example
		// messageAdapter = new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1,
		// dbAdapter.getMessageArrayList(phoneNumber));
		//
		// messageAdapter.notifyDataSetChanged();
		
		QueryParseAsyncTask task = new QueryParseAsyncTask(0, Util.getUsernameString());
		AsyncTask<Void, Void, ArrayList<MessageItem>> asyncTask = task
				.execute();

		// BEGIN Adding contact stuff
		addContactButton = (Button) view
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
				toField.setText(phoneNumber);
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
//		Log.d(TAG, "Item clicked: " + id);
//		// When clicked, show a toast with the TextView text
//		Toast.makeText(getActivity().getApplicationContext(),
//				((TextView) v).getText(), Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onDestroy() {
		// Closing the database adapter when the activity gets destroyed.
		super.onDestroy();
		dbAdapter.close();
	}

	/**
	 * A custom adapter that populates the Message ListView. We are going to be
	 * using a custom list view (message_list_item). Thus need to create a
	 * custom list adapter to accommodate the custom list view.
	 * 
	 * @author Aaron
	 * 
	 */
	public class MessageListAdapter extends ArrayAdapter<String> {
		private ArrayList<MessageItem> mMessages;
		private Context mContext;
		private LayoutInflater inflater;

		public MessageListAdapter(Context context, int textViewResourceId,
				ArrayList<MessageItem> messages) {
			super(context, textViewResourceId);
			mMessages = messages;
			mContext = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;//super.getView(position, convertView, parent);

			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(convertView == null){
				v = inflater.inflate(R.layout.message_list_item, null);
			}
			TextView nameTv = (TextView) v.findViewById(R.id.message_list_name);
			nameTv.setText(mMessages.get(position).getMessageUsername());
			TextView messageTv = (TextView) v
					.findViewById(R.id.message_list_body);
			messageTv.setText(mMessages.get(position).getMessageBody());
			TextView timeTv = (TextView) v.findViewById(R.id.message_list_sent);
			timeTv.setText(mMessages.get(position).getMessageTime());
			return v;
		}

		@Override
		public int getCount(){
			return mMessages.size();
		}
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Activity activity = (Activity) mContext;
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case CONTACT_PICKER_RESULT:
				Cursor cursor = null;
				Uri uri = data.getData();
				Log.d(TAG, "Contact Info - URI: " + uri);
				// Get the contactId from the URI
				String contactId = uri.getLastPathSegment();
				Log.d(TAG, "Contact Info - ID: " + contactId);

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
		private final String TAG = "QueryParseAsyncTask";
		private final int SMS = 0;
		private final int THREAD = 1;

		private int queryType;
		private String username;

		private ArrayList<MessageItem> messageResults;

		public QueryParseAsyncTask(int type, String user) {
			queryType = type;
			username = user;
		}

		@Override
		protected ArrayList<MessageItem> doInBackground(Void... arg0) {
			if (queryType == SMS) {
				ParseQuery querySms = new ParseQuery(
						Preferences.PARSE_TABLE_SMS);
				ParseQuery queryThread = new ParseQuery(
						Preferences.PARSE_TABLE_THREAD);

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
						Log.e(TAG,
								"MessageItem - Size: " + messageResults.size());
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return messageResults;
		}
		
		@Override
		protected void onPostExecute(ArrayList<MessageItem> messageList){
			messageAdapter = new MessageListAdapter(getActivity(),
					R.layout.message_list_item, messageList);

			setListAdapter(messageAdapter);
		}
		
	}
}