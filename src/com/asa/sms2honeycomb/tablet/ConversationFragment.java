package com.asa.sms2honeycomb.tablet;

import com.asa.sms2honeycomb.DatabaseAdapter;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ConversationFragment extends ListFragment {
	DatabaseAdapter dbAdapter;

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
		Log.d("FragmentList", "Item clicked: " + id);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}
}
