package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;

import com.asa.sms2honeycomb.ConversationListAdapter;
import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util.Util;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConversationFragment extends ListFragment {
	DatabaseAdapter dbAdapter;

	private static final String TAG = "CoverstationFragment";
	private ArrayAdapter<String> conversationAdapter;
	private ListView mConversationListView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Open up the database
		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();

		// Get the list of contact numbers
		ArrayList<String> conversationList = dbAdapter.getConversationList();

		// get the adapter
		conversationAdapter = new ConversationListAdapter(getActivity(),
				R.layout.conversation_list_item, mConversationListView,
				conversationList);

		// set the adapter
		setListAdapter(conversationAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Log.d("ConversationFragment", "Item clicked: " + id + l.toString());

		// Create the bundle so the phonenumber can be pushed on the
		// MessageFragment
		// gets the hidden phone number from the list item
		CharSequence number = ((TextView) v
				.findViewById(R.id.conversation_list_contact_number)).getText();

		Bundle bundle = new Bundle();
		bundle.putString("phoneNumber", number.toString());

		Util.displayToast(getActivity(),
				"Displaying messages from " + number.toString());

		// Lauching a new fragment that will show all of the messages from the
		// given numbers
		MessageFragment message = (MessageFragment) getFragmentManager()
				.findFragmentById(R.id.message);

		message = MessageFragment.newInstance(bundle);

		// Execute a transaction, replacing any existing fragment with
		// this one inside the frame
		Log.d(TAG, "about to run FragmentTransaction...");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		// ft.addToBackStatc("message");
		ft.replace(R.id.message, message);
		ft.commit();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}
}
