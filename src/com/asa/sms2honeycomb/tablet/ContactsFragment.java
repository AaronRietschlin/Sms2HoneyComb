package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsFragment extends ListFragment {

	private Intent mIntent;
	private final String TAG = "TestFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, getCotactArrayList()));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG, "Item clicked: " + id);

		// Gets the number from the ListView
		CharSequence numberSplit = (((TextView) v).getText());
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

		Toast.makeText(getActivity().getApplicationContext(),
				tokenString.toString(), Toast.LENGTH_SHORT).show();

		Log.d(TAG, "The number selected is: " + tokenString.toString());

		// TODO Need to get ALL contact information. Picture, name, etc
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
