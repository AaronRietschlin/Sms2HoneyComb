package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;

import com.asa.sms2honeycomb.R;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ConversationListFragment extends ListFragment {
	protected ArrayList<String> contactNames;
	protected ArrayList<String> contactPhoneNumbers;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		contactNames = new ArrayList<String>();
		contactPhoneNumbers = new ArrayList<String>();
		//getContactArrayList();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("FragmentList", "Item clicked: " + id);
	}

	public void getContactArrayList() {
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

		if (dataCursor.moveToFirst()) {
			do {
				// Extract the name.
				String name = dataCursor.getString(nameIdx);
				// Extract the phone number.
				String phone = dataCursor.getString(phoneIdx);

				contactNames.add(name);
				if (phone != null && phone.length() > 0) {
					this.contactPhoneNumbers.add(phone);
				} else {
					// TODO Adding random number.
					contactPhoneNumbers.add("0");
				}

			} while (dataCursor.moveToNext());
			getActivity().stopManagingCursor(dataCursor);
			dataCursor.close();
		}

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, contactNames));
	}

	public class MyArrayAdapter extends ArrayAdapter<String> {
		protected ArrayList<String> mNames;
		protected Context mContext;

		public MyArrayAdapter(Context context, int textViewResourceId,
				ArrayList<String> names) {
			super(context, textViewResourceId);
			mContext = context;
			mNames = names;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.conversation_list_item, null);
			}
			String name = mNames.get(position);
			
			return v;
		}
	}
}
