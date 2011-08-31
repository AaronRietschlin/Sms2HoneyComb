package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;
import com.asa.sms2honeycomb.Preferences;

import com.asa.sms2honeycomb.R;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsListFragment extends ListFragment {

	private ListView contactsListView;
	private ArrayList<String> contacts;
	private Intent mIntent;

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item,
				getCotactArrayList()));

		contactsListView = getListView();
		contactsListView.setTextFilterEnabled(true);

		contactsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getActivity(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				// TODO parse the number out of the contact
				String number = "1234567";
				mIntent = new Intent(getActivity(), MessageListFragment.class);
				
				// Put the phone number into the bundle.
				if (Preferences.DEBUG) {
					Bundle b= new Bundle();
					b.putString("1234567", number);
					// put the bundle into the intent to pass it along to the MessageListFragment.
					mIntent.putExtras(b);
				} else {
					Bundle b= new Bundle();
					b.putString("phonenumber", number);
					mIntent.putExtras(b);
				}
				
				// Start the activity
				startActivity(mIntent);
				
			}
		});
	}

	public ArrayList<String> getCotactArrayList() {
		// Get the cursor over every aggregated contact data.
		Cursor dataCursor = getActivity().getContentResolver().query(
				ContactsContract.Data.CONTENT_URI, null, null, null, null);

		// Let the activity manage the cursor lifecycle.
		getActivity().startManagingCursor(dataCursor);

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

			getActivity().stopManagingCursor(dataCursor);
			dataCursor.close();
		}
		return contacts;

	}
}