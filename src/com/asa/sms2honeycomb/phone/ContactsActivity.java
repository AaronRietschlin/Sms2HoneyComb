package com.asa.sms2honeycomb.phone;

import java.util.ArrayList;
import com.asa.sms2honeycomb.R;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends ListActivity {

	private ListView contactsListView;
	private Intent mIntent;
	private final String TAG = "ContactsActivity";

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.message_list);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				getCotactArrayList()));

		contactsListView = getListView();
		contactsListView.setTextFilterEnabled(true);

		contactsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Gets the number from the ListView
				CharSequence numberSplit = (((TextView) view).getText());
				// Foing to use ( and ) to split the string the number should be
				// in the ()
				String delims = "[()]";
				// make a String array of the results (should only be one)
				String[] tokens = numberSplit.toString().split(delims);
				// get the results out of the array with StringBuilder (better
				// way)??
				StringBuilder tokenString = new StringBuilder();
				for (String s : tokens) {
					tokenString.append(s);
					tokenString.append("\n");
				}
				Log.d(TAG, "The number selected is: " + tokenString.toString());

				// TODO this is for testing
				String number = "1234567";
				// This is the actual code, but my test phone has no contacts
				// on it
				// String number = tokenString.toString();

				// Create the Intent to launch the activity
				mIntent = new Intent(ContactsActivity.this,
						MainPhoneActivity.class);

				// Put the phone number into the bundle.
				Bundle b = new Bundle();
				b.putString("phonenumber", number);

				// Put the bundle into the intent
				mIntent.putExtras(b);

				// Start the activity
				startActivity(mIntent);

				// TODO Need to get ALL contact information. Picture, name, etc
			}
		});
	}

	public ArrayList<String> getCotactArrayList() {
		// Get the cursor over every aggregated contact data.
		Cursor dataCursor = getContentResolver().query(
				ContactsContract.Data.CONTENT_URI, null, null, null, null);

		// Let the activity manage the cursor lifecycle.
		startManagingCursor(dataCursor);

		// Use the convenience properites to get the index of the columns
		int nameIdx = dataCursor
				.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		// Get the phonenumber
		int phoneIdx = dataCursor
				.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

		ArrayList<String> contacts = new ArrayList<String>();
		if (dataCursor.moveToFirst()) {
			do {
				// Extract the name.
				String name = dataCursor.getString(nameIdx);
				// Extract the phone number.
				String phone = dataCursor.getString(phoneIdx);
				// Combine the name and phone number
				String contact = name + " (" + phone + ")";
				// Add the contact to contacts
				contacts.add(contact);
				// Remove the contact if there is no number
				if (phone == null) {
					contacts.remove(contact);
				}

			} while (dataCursor.moveToNext());

			stopManagingCursor(dataCursor);
			dataCursor.close();
		}
		return contacts;

	}
}