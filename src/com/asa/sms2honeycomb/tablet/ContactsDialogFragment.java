package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;

import com.asa.sms2honeycomb.R;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsDialogFragment extends DialogFragment implements
		OnItemClickListener {
	// used to pick out a layout
	int mNum;
	ListView contactsList;

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an argument
	 * letting you pick out what layout you want.
	 */
	static ContactsDialogFragment newInstance(int num) {
		ContactsDialogFragment f = new ContactsDialogFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_contacts_view, container,
				false);

		String[] items = { "lorem", "ipsum", "dolor", "sit", "amet",
				"consectetuer", "adipiscing", "elit", "morbi", "vel", "ligula",
				"vitae", "arcu", "aliquet", "mollis", "etiam", "vel", "erat",
				"placerat", "ante", "porttitor", "sodales", "pellentesque",
				"augue", "purus" };

		contactsList = (ListView) v.findViewById(android.R.id.list);

		contactsList.setAdapter(new ArrayAdapter<String>(v.getContext(),
				android.R.layout.simple_list_item_1, getCotactArrayList()));

		return v;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");

		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		switch ((mNum - 1) % 6) {
		case 1:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 2:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 3:
			style = DialogFragment.STYLE_NO_INPUT;
			break;
		case 4:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 5:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 6:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 7:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 8:
			style = DialogFragment.STYLE_NORMAL;
			break;
		}
		switch ((mNum - 1) % 6) {
		case 4:
			theme = android.R.style.Theme_Holo;
			break;
		case 5:
			theme = android.R.style.Theme_Holo_Light_Dialog;
			break;
		case 6:
			theme = android.R.style.Theme_Holo_Light;
			break;
		case 7:
			theme = android.R.style.Theme_Holo_Light_Panel;
			break;
		case 8:
			theme = android.R.style.Theme_Holo_Light;
			break;
		}
		setStyle(style, theme);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
