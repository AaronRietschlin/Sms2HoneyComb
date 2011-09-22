package com.asa.sms2honeycomb.tablet;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.asa.sms2honeycomb.Preferences;
import com.asa.sms2honeycomb.R;
import com.asa.sms2honeycomb.util.Util;

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

		getActivity().setContentView(R.layout.conversation_list);

		// Open up the database needs to be above the conversationAdapter
		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();

		conversationListView = (ListView) getActivity().findViewById(
				android.R.id.list);
		newButton = (Button) getActivity().findViewById(
				R.id.conversation_new_btn);

		// ListView stuff
		conversationAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,  dbAdapter.getConversationList());

		setListAdapter(conversationAdapter);

		conversationListView = getListView();
		conversationListView.setTextFilterEnabled(true);

		newButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Launch the ContactsActivity
				mIntent = new Intent(getActivity(), ContactsFragment.class);
				startActivity(mIntent);
				getActivity().finish();

			}

		});

		conversationListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Gets the number from the ListView
				CharSequence numberSplit = (((TextView) view).getText());
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
				Log.d(TAG, "The number selected is: " + tokenString.toString());

				// TODO this is for testing
				String number = "1234567";
				// This is the actual code, but my test phone has no contacts
				// on it
				// String number = tokenString.toString();

				// Create the Intent to launch the activity
				mIntent = new Intent(getActivity(), MessageFragment.class);

				// Put the phone number into the bundle.
				Bundle b = new Bundle();
				b.putString("phonenumber", number);

				// Put the bundle into the intent
				mIntent.putExtras(b);

				// Start the activity
				startActivity(mIntent);

				// When clicked, show a toast with the TextView text
				Toast.makeText(getActivity().getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();

			}
		});
	}

	@Override
	public void onDestroy() {
		// Closing the database adapter when the activity gets destroyed.
		super.onDestroy();
		dbAdapter.close();
	}
}
