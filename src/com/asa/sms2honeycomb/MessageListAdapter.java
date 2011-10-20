package com.asa.sms2honeycomb;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;

/**
 * A custom adapter that populates the Message ListView. We are going to be
 * using a custom list view (message_list_item). Thus need to create a
 * custom list adapter to accommodate the custom list view.
 * 
 * @author Aaron
 * 
 */
public class MessageListAdapter extends ArrayAdapter<String> {
	private ArrayList<MessageItem> mMessages;
	private Context mContext;
	private LayoutInflater inflater;
	private ListView mListView;

	public MessageListAdapter(Context context, int textViewResourceId, ListView list,
			ArrayList<MessageItem> messages) {
		super(context, textViewResourceId);
		mMessages = messages;
		mContext = context;
		mListView = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;// super.getView(position, convertView,
								// parent);
		MessageItem item = mMessages.get(position);

		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			v = inflater.inflate(R.layout.message_list_item, null);
		}
		TextView nameTv = (TextView) v.findViewById(R.id.message_list_name);
		TextView messageTv = (TextView) v.findViewById(R.id.message_list_body);
		messageTv.setText(item.getMessageBody());
		TextView timeTv = (TextView) v.findViewById(R.id.message_list_time);
		timeTv.setText(item.getMessageTime());
		switch (item.getMessageType()) {
		case Preferences.RECEIVED:
			nameTv.setText(item.getMessageAddress());
			break;
		case Preferences.SENT:
			nameTv.setText("Me");
			break;
		}
		return v;
	}

	@Override
	public int getCount() {
		return mMessages.size();
	}

	// /**
	// * Tells how many types of pools of messages to keep. Read:
	// *
	// http://logc.at/2011/10/10/handling-listviews-with-multiple-row-types/
	// *
	// * @return
	// */
	// @Override
	// public int getViewTypeCount() {
	// return Preferences.NUM_TYPE_OF_MESSAGES;
	// }
	//
	// /**
	// * Tells the list view which pool the view belongs to. Read:
	// *
	// http://logc.at/2011/10/10/handling-listviews-with-multiple-row-types/
	// *
	// * @param position
	// * @return
	// */
	// @Override
	// public int getItemViewType(int position) {
	// if (mMessages.get(position).getMessageType() == RECEIVED) {
	// return RECEIVED;
	// }
	// return SENT;
	// }
	
	@Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        // Sets the selected list item to be the very last one.
        mListView.setSelection(mListView.getCount());
    }
}
