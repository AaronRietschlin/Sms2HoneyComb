package com.asa.sms2honeycomb.tablet;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.R;

public class ConversationFragment extends ListFragment {

	private Intent mIntent;
	private ArrayAdapter<String> conversationAdapter;
	private DatabaseAdapter dbAdapter;
	private final String TAG = "ConversationActivity";

	ListView conversationListView;
	Button newButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,
				dbAdapter.getConversationList()));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG, "Item clicked: " + id);

		// Gets the number from the ListView
		CharSequence numberSplit = (((TextView) v).getText());
		// Foing to use ( and ) to split the string the number should be
		// in the ()
		String delims = "[ ]";
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

	}

	@Override
	public void onDestroy() {
		// Closing the database adapter when the activity gets destroyed.
		super.onDestroy();
		dbAdapter.close();
	}
}