package com.asa.sms2honeycomb.tablet;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.Util.Util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConversationFragment extends ListFragment {
	DatabaseAdapter dbAdapter;
	private static final String TAG = "CoverstationFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Open up the database
		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,
				dbAdapter.getConversationList()));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("FragmentList", "Item clicked: " + id + l.toString());
		
		// Create the bundle so the phonenumber can be pushed on the MessageFragment
		CharSequence number = ((TextView) v).getText();
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
