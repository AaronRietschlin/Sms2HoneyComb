package com.asa.sms2honeycomb;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConversationListAdapter extends ArrayAdapter<String> {
	private ArrayList<String> mConversations;
	private Context mContext;
	private LayoutInflater inflater;
	private ListView mListView;
	
	public ConversationListAdapter(Context context, int textViewResourceId, ListView list,
			ArrayList<String> conversations) {
		super(context, textViewResourceId);
		mConversations = conversations;
		mContext = context;
		mListView = list;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;// super.getView(position, convertView,
								// parent);
		String mPosition = mConversations.get(position);

		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			v = inflater.inflate(R.layout.conversation_list_item, null);
		}
		
		TextView nameTv = (TextView) v.findViewById(R.id.conversation_list_contact_name);
		nameTv.setText("1234567890");
		return v;
	}
	
	@Override
	public int getCount() {
		return mConversations.size();
	}
	
	@Override
	public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        // Sets the selected list item to be the very last one.
        mListView.setSelection(mListView.getCount());
    }
}
