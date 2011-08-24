package com.asa.sms2honeycomb.tablet;

import java.util.ArrayList;
import com.asa.sms2honeycomb.DatabaseAdapter;
import com.asa.sms2honeycomb.R;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageListFragment extends ListFragment {

	ListView messageListView;
	ArrayList<String> messageArrayList;
	DatabaseAdapter dbAdapter;
	Intent mIntent;

	private final String TAG = "MessageListFragment";

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		// The intent passes on the data to the Bundle when the Activity is
		// created, you have to getExtras from the intent
		Bundle b = mIntent.getExtras();
		// The phone number is gotten by the key "phonenumber" and put into the
		// String
		String phonenumber = b.getString("phonenumber");
		if (phonenumber == null) {
			Log.e(TAG, "The intent from the contacts did not work.");
		}
		
		// TODO get the phonenumber into the contacts name
		// do a user query when clicked to get both the number and the name into the intent?
		// problem is with the database doesnt have the name already stored?
		
		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.list_item, dbAdapter.getMessageArrayList(phonenumber)));
		messageListView = getListView();
		messageListView.setTextFilterEnabled(true);

		messageListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO IDK delete from the local database? or even parse or both?
				// Start sending messages? 
				// When clicked, show a toast with the TextView text
				Toast.makeText(getActivity(), ((TextView) view).getText(),
						Toast.LENGTH_SHORT).show();

			}
		});
	}
}