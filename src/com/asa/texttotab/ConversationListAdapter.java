package com.asa.texttotab;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.asa.texttotab.R;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class ConversationListAdapter extends ArrayAdapter<String> {
	private ArrayList<String> mConversations;
	private Context mContext;
	private LayoutInflater inflater;
	private ListView mListView;
	private static final String TAG = "ConversationListAdapter";

	public ConversationListAdapter(Context context, int textViewResourceId,
			ListView list, ArrayList<String> conversations) {
		super(context, textViewResourceId);
		mConversations = conversations;
		mContext = context;
		mListView = list;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;// super.getView(position, convertView,
								// parent);
		String conversationNumber = mConversations.get(position);

		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			v = inflater.inflate(R.layout.conversation_list_item, null);
		}

		TextView nameTv = (TextView) v
				.findViewById(R.id.conversation_list_contact_name);
		TextView numberTv = (TextView) v
				.findViewById(R.id.conversation_list_contact_number);
		QuickContactBadge contactImage = (QuickContactBadge) v
				.findViewById(R.id.conversation_list_contactBadge);

		String name = getContactInfo(conversationNumber).get(0);
		String photoURI = getContactInfo(conversationNumber).get(1);
		if (photoURI != null && photoURI.length() > 0) {
			nameTv.setText(name);
			numberTv.setText(conversationNumber);
			contactImage.setImageBitmap(getContactPhoto(openPhoto(photoURI
					.toString())));
		}
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

	public ArrayList<String> getContactInfo(String number) {
		final ArrayList<String> namePhoto = new ArrayList<String>();

		// Activity activity = getActivity();

		Cursor cursor = null;
		// search by the phoneNumber
		cursor = mContext.getContentResolver().query(Phone.CONTENT_URI, null,
				Phone.NUMBER + "=?", new String[] { number }, null);

		if (cursor.moveToFirst()) {
			String name = cursor.getString(cursor
					.getColumnIndex(Contacts.DISPLAY_NAME));
			namePhoto.add(name);
			String photoURI = cursor.getString(cursor
					.getColumnIndex(Contacts.PHOTO_URI));
			namePhoto.add(photoURI);

		}
		cursor.close();
		return namePhoto;
	}

	// decodes the photoURI from the contacts shit and gives an inputstream
	public InputStream openPhoto(String photoURI) {
		Cursor cursor = mContext.getContentResolver().query(
				Uri.parse(photoURI), new String[] { Contacts.Photo.PHOTO },
				null, null, null);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					return new ByteArrayInputStream(data);
				}
			}
		} finally {
			cursor.close();
		}
		return null;
	}

	// decodes the inputstream and returns the bitmap of the contact photo
	public Bitmap getContactPhoto(InputStream photoInputStream) {
		Bitmap photo = BitmapFactory.decodeStream(photoInputStream);
		return photo;
	}

}
