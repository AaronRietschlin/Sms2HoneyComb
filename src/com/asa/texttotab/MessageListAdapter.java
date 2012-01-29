package com.asa.texttotab;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.asa.texttotab.R;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

/**
 * A custom adapter that populates the Message ListView. We are going to be
 * using a custom list view (message_list_item). Thus need to create a custom
 * list adapter to accommodate the custom list view.
 * 
 * @author Aaron
 * 
 */
public class MessageListAdapter extends ArrayAdapter<String> {
	private ArrayList<MessageItem> mMessages;
	private Context mContext;
	private LayoutInflater inflater;
	private ListView mListView;

	private final String TAG = "MessageListAdapter";

	public MessageListAdapter(Context context, int textViewResourceId,
			ListView list, ArrayList<MessageItem> messages) {
		super(context, textViewResourceId);
		mMessages = messages;
		mContext = context;
		mListView = list;
	}

	/**
	 * A holder class that holds the View widgets. It optimizes the ListView.
	 * 
	 */
	static class ViewHolder {
		TextView nameTv;
		TextView messageTv;
		TextView timeTv;
		QuickContactBadge contactImage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MessageItem item = mMessages.get(position);
		ViewHolder holder;

		if (convertView == null) {
			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.message_list_item, null);

			holder = new ViewHolder();
			holder.nameTv = (TextView) convertView
					.findViewById(R.id.message_list_name);
			holder.messageTv = (TextView) convertView
					.findViewById(R.id.message_list_body);
			holder.timeTv = (TextView) convertView
					.findViewById(R.id.message_list_time);
			holder.contactImage = (QuickContactBadge) convertView
					.findViewById(R.id.contactBadgeMessageList);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.messageTv.setText(item.getMessageBody());
		holder.timeTv.setText(item.getMessageTime());

		ArrayList<String> contactInfo = getContactInfo(item.getMessageAddress());
		switch (item.getMessageType()) {
		case Preferences.RECEIVED:
			holder.nameTv.setText(contactInfo.get(0));
			holder.contactImage
					.setImageBitmap(getContactPhoto(openPhoto(contactInfo
							.get(1))));
			
			Bitmap image = loadContactPhoto(mContext.getContentResolver(), contactInfo.get(2));
			
			holder.contactImage.setImageBitmap(getContactPhoto(getPhotoInputStream(contactInfo.get(2))));
			holder.contactImage.setImageBitmap(image);
			
			break;
		case Preferences.SENT:
			holder.nameTv.setText("Me");
			holder.contactImage
					.setImageBitmap(getContactPhoto(openPhoto(contactInfo
							.get(1))));
			break;
		}
		return convertView;
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
			
			String contactId = cursor.getString(cursor.getColumnIndex(Phone.CONTACT_ID));
			namePhoto.add(contactId);

		}
		cursor.close();
		return namePhoto;
	}

	public static Bitmap loadContactPhoto(ContentResolver cr, String  id) {
	    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
	    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
	    if (input == null) {
	        return null;
	    }
	    return BitmapFactory.decodeStream(input);
	}
	
	public InputStream getPhotoInputStream(String contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				Long.parseLong(contactId));
		Uri photoUri = Uri.withAppendedPath(contactUri,
				Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = mContext.getContentResolver().query(photoUri,
				new String[] { Contacts.Photo.PHOTO }, null, null, null);
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

	// decodes the photoURI from the contacts shit and gives an inputstream
	public InputStream openPhoto(String photoURI) {
		Log.d(TAG, "Inside openPhoto");
		if (photoURI == null) {
			return null;
		} else {
			if (photoURI.length() == 0) {
				return null;
			}
		}
		Log.d(TAG, photoURI);
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
