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
	private ArrayList<MessageItem> mMessages;
	private Context mContext;
	private LayoutInflater inflater;
	private ListView mListView;
	
	public ConversationListAdapter(Context context, int textViewResourceId, ListView list,
			ArrayList<ConversationItem> messages) {
		super(context, textViewResourceId);
		mMessages = messages;
		mContext = context;
		mListView = list;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;// super.getView(position, convertView,
								// parent);
		MessageItem item = mMessages.get(position);

		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			v = inflater.inflate(R.layout.conversation_list_item, null);
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
}
