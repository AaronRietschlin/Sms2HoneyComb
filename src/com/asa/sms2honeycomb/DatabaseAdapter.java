package com.asa.sms2honeycomb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import android.content.ClipData.Item;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseAdapter {
	// TAG for debugging
	private final String TAG = "DatabaseHandler";

	// Basic Database information
	private static final String DATABASE_NAME = "texttotab.db";
	private static final String MESSAGE_TABLE = "messageTable";
	private static final String THREAD_TABLE = "threadTable";
	private static final int DATABASE_VERSION = 1;

	// Used in all of the tables
	public static final String KEY_ID = "_id";

	// The index (key) column name for use in where clauses.
	// Column names for SMS table:
	public static final String KEY_TIME = "_time";
	public static final String KEY_ADDRESS = "_address";
	public static final String KEY_BODY = "_body";
	public static final String KEY_READ_S = "_reads";
	public static final String KEY_SMSID = "_smsid";
	public static final String KEY_SUBJECT = "_subject";
	public static final String KEY_THREADID_S = "_threadids";
	public static final String KEY_TYPE = "_type";
	public static final String KEY_ONDEVICE = "_ondevice";
	public static final String KEY_USERNAME_S = "_usernames";

	// The index (key) column name for use in where clauses.
	// Column names for THREAD table:
	public static final String KEY_HAS_ATTACHMENT = "_hasattachment";
	public static final String KEY_MESSAGE_COUNT = "_messagecount";
	public static final String KEY_READ_T = "_readt";
	public static final String KEY_THREADID_T = "_thread_id";
	public static final String KEY_USERNAME_T = "_usernamet";
	public static final String KEY_CREATED_AT = "_created_at";

	// Used to identify the columns in other classes.
	// SMS table
	public static final int ID_COLUMN_S = 0;
	public static final int TIME_COLUMN = 1;
	public static final int ADDRESS_COLUMN = 2;
	public static final int BODY_COLUMN = 3;
	public static final int READ_S_COLUMN = 4;
	public static final int SMSID_COLUMN = 5;
	public static final int SUBJECT_COLUMN = 6;
	public static final int THREADID_S_COLUMN = 7;
	public static final int TYPE_COLUMN = 8;
	public static final int ON_DEVICE_COLUMN = 9;
	public static final int USERNAME_S_COLUMN = 10;
	// THREAD table
	public static final int ID_COLUMN_T = 0;
	public static final int HAS_ATTACHMENT = 1;
	public static final int MESSAGE_COUNT = 2;
	public static final int READ_T = 3;
	public static final int THREADID_T = 4;
	public static final int USERNAME_T = 5;

	// SQL Statement to create a new database.
	private static final String DATABASE_CREATE_SMS = "create table "
			+ MESSAGE_TABLE + " (" + KEY_ID
			+ " integer primary key autoincrement, " + KEY_TIME + " date, "
			+ KEY_ADDRESS + " String, " + KEY_BODY + " String, " + KEY_READ_S
			+ " int, " + KEY_SMSID + " int, " + KEY_SUBJECT + " String, "
			+ KEY_THREADID_S + " int, " + KEY_TYPE + " int, " + KEY_ONDEVICE
			+ " int, " + KEY_USERNAME_S + " String);";

	private static final String DATABASE_CREATE_THREAD = "create table "
			+ THREAD_TABLE + " (" + KEY_ID
			+ " integer primary key autoincrement, " + KEY_HAS_ATTACHMENT
			+ " int, " + KEY_MESSAGE_COUNT + " int, " + KEY_READ_T + " int, "
			+ KEY_THREADID_T + " int, " + KEY_USERNAME_T + " String);";

	// Variables to hold the database instance
	private SQLiteDatabase db;
	// Context of the appliction using the database
	private final Context context;
	// Database open/upgrade helper
	private myDbHelper dbHelper;

	public DatabaseAdapter(Context _context) {
		context = _context;
		dbHelper = new myDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public void open() throws SQLiteException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		Log.d(TAG, "Closing: " + DATABASE_NAME);
		db.close();
	}

	// Insert a new MessageItem into Database
	public long insertMessageItem(MessageItem item) {
		ContentValues newMessageValues = new ContentValues();
		newMessageValues.put(KEY_TIME, item.getMessageTime());
		newMessageValues.put(KEY_ADDRESS, item.getMessageAddress());
		newMessageValues.put(KEY_BODY, item.getMessageBody());
		newMessageValues.put(KEY_READ_S, item.getMessageRead());
		newMessageValues.put(KEY_SMSID, item.getMessageSmsId());
		newMessageValues.put(KEY_SUBJECT, item.getMessageSubject());
		newMessageValues.put(KEY_THREADID_S, item.getMessageThreadId());
		newMessageValues.put(KEY_TYPE, item.getMessageType());
		newMessageValues.put(KEY_ONDEVICE, item.getMessageOnDevice());
		newMessageValues.put(KEY_USERNAME_S, item.getMessageUsername());
		// Inserts the new row into the database
		Log.d(TAG, "Values: " + newMessageValues.toString()
				+ " have been put in to: " + DATABASE_NAME);
		return db.insert(MESSAGE_TABLE, null, newMessageValues);
	}

	public long insertThreadItem(ThreadItem item) {
		ContentValues newThreadValues = new ContentValues();
		newThreadValues.put(KEY_HAS_ATTACHMENT, item.getHasAttachment());
		newThreadValues.put(KEY_MESSAGE_COUNT, item.getMessageCount());
		newThreadValues.put(KEY_READ_T, item.getRead());
		newThreadValues.put(KEY_USERNAME_T, item.getUsername());
		Log.d(TAG, "Values: " + newThreadValues.toString() +  " have been put in to: ");
		return db.insert(THREAD_TABLE, null, newThreadValues);
	}

	/*
	 * Returns the cursor for the given table MESSAGE_TABLE or THREAD_TABLE if
	 * not those returns null
	 */
	public Cursor getAllEntries(String table) {
		Log.d(TAG, "Getting all entries from: " + table);
		if (table == MESSAGE_TABLE) {
			return db.query(table, new String[] { KEY_ID, KEY_ADDRESS,
					KEY_TIME, KEY_BODY, KEY_READ_S, KEY_SMSID, KEY_SUBJECT,
					KEY_THREADID_S, KEY_TYPE, KEY_ONDEVICE, KEY_USERNAME_S },
					null, null, null, null, null);
		}
		if (table == THREAD_TABLE) {
			return db.query(table, new String[] { KEY_ID, KEY_HAS_ATTACHMENT,
					KEY_BODY, KEY_MESSAGE_COUNT, KEY_READ_T, KEY_THREADID_T,
					KEY_USERNAME_T }, null, null, null, null, null);
		}
		Log.e(TAG, table + " is not vaild");
		return null;
	}

	/*
	 * Removes entry in a table using the row index tables: THREAD_TABLE and
	 * MESSAGE_TABLE
	 */
	public boolean removeEntry(long _rowIndex, String table) {
		Log.d(TAG, "Removed entry: " + _rowIndex);
		return db.delete(table, KEY_ID + "=" + _rowIndex, null) > 0;
	}

	public boolean updateEntry(long _rowIndex, MessageItem _messageItem,
			ThreadItem _threadItem) {
		// TODO: Create a new ContentValues based on the new object
		// and use it to update a row e database.
		return true;
	}

	// TODO error mixing up time and address
	/**
	 * Querys the Database for the number given getting both the TO and the
	 * FROM.
	 * 
	 * @param String
	 *            number
	 * 
	 * @return ArrayList<String> list
	 */
	public ArrayList<MessageItem> getMessageArrayList(String number) {
		ArrayList<MessageItem> messageItemList = new ArrayList<MessageItem>();
		// Only get values from the to and from number given and sort by time
		// oldest at
		// the end of the list
		Cursor cursor = db.query(true, MESSAGE_TABLE, new String[] { KEY_ID,
				KEY_TIME, KEY_ADDRESS, KEY_BODY, KEY_READ_S, KEY_SMSID,
				KEY_SUBJECT, KEY_THREADID_S, KEY_TYPE, KEY_ONDEVICE,
				KEY_USERNAME_S }, KEY_ADDRESS + "=" + number, null, null, null,
				KEY_TIME, null);
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			Log.e(TAG, "No messages for number: " + number);
		}
		if (cursor.moveToFirst()) {
			do {
				MessageItem messageItem = new MessageItem();

				String address = cursor.getString(ADDRESS_COLUMN);
				messageItem.setMessageAddress(address);

				String time = cursor.getString(TIME_COLUMN);
				messageItem.setMessageTime(time);

				String body = cursor.getString(BODY_COLUMN);
				messageItem.setMessageBody(body);

				int type = cursor.getInt(TYPE_COLUMN);
				messageItem.setMessageType(type);

				int onDevice = cursor.getInt(ON_DEVICE_COLUMN);
				messageItem.setMessageOnDevice(onDevice);

				messageItemList.add(messageItem);

				// add the message parts together
				String message = "Address: " + number + "\n" + "Time: " + time
						+ "\n" + "Type: " + type + "\n" + "Body : " + body;
				Log.d(TAG + ".getMessageArrayList", message);
			} while (cursor.moveToNext());
			{
				cursor.close();
			}
		}
		return messageItemList;
	}

	// TODO update
	public ArrayList<String> getConversationList() {
		// use a set first becasue there can not be any duplicates
		Set<String> set = new HashSet<String>();
		
		// TODO get the cursor to query the db and get the number of entries for
		// both the to and from that are from the same person MAKE IT WORK
		// TODO bug counts get number's conversation then adds that name entries
		// public Cursor query (boolean distinct, String table, String[]
		// columns, String selection, String[] selectionArgs, String groupBy,
		// String having, String orderBy, String limit)
		Cursor cursor = db.query(true, MESSAGE_TABLE, new String[] { KEY_ID,
				KEY_TIME, KEY_ADDRESS, KEY_BODY, KEY_READ_S, KEY_SMSID,
				KEY_SUBJECT, KEY_THREADID_S, KEY_TYPE, KEY_ONDEVICE,
				KEY_USERNAME_S }, null, null, null, null, null, null);
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			Log.e(TAG, "No conversations.");
		}

		if (cursor.moveToFirst()) {
			// This will be used to start the new conversations within the
			// ConversationFragment
			do {
				String address = cursor.getString(ADDRESS_COLUMN);
				
				set.add(address);
				
			} while (cursor.moveToNext());
			{
				cursor.close();
			}

		}
		ArrayList<String> list = new ArrayList<String>(set);
		
		Log.d(TAG, list.toString());

		return list;
	}

	/*
	 * Check if the entry is on the device returns true if yes (1) false if no
	 * (0)
	 */
	public boolean onDevice() {
		Cursor cursor = db.query(true, MESSAGE_TABLE, new String[] { KEY_ID,
				KEY_TIME, KEY_ADDRESS, KEY_BODY, KEY_READ_S, KEY_SMSID,
				KEY_SUBJECT, KEY_THREADID_S, KEY_TYPE, KEY_ONDEVICE,
				KEY_USERNAME_S }, null, null, null, null, null, null);

		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			Log.e(TAG, "onDevice: nothing in the table.");
		}

		if (cursor.moveToFirst()) {
			// This will be used to start the new conversations within the
			// ConversationFragment
			do {
				int onDevice = cursor.getInt(ON_DEVICE_COLUMN);
				// if on device then dont pull it
				if (onDevice == 1) {
					return true;
				} else {
					// it is a 2 then pull it from the database and then chage
					// the value to 1

					// change the 2 to a 1
					ContentValues newMessageValues = new ContentValues();
					newMessageValues.put(KEY_ONDEVICE, 1);
					db.insert(MESSAGE_TABLE, null, newMessageValues);
				}

			} while (cursor.moveToNext());
			{
				cursor.close();
			}
		}
		return false;
	}

	private static class myDbHelper extends SQLiteOpenHelper {
		private final String TAG = "myDbHelper";

		public myDbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		// Called when no database exists in disk and the helper class needs
		// to create a new one.
		@Override
		public void onCreate(SQLiteDatabase _db) {
			try {
				Log.d(TAG, "Creation: Trying to create SMS table");
				Log.d(TAG, "Creation: " + DATABASE_CREATE_SMS);
				_db.execSQL(DATABASE_CREATE_SMS);
				Map<String, String> map = _db.getSyncedTables();
				if (map.size() == 0) {
					Log.d(TAG, "Creation: Empty.");
				}
				for (Map.Entry<String, String> entry : map.entrySet()) {
					Log.d(TAG,
							"Creation: " + entry.getKey() + "/"
									+ entry.getValue());
				}
			} catch (SQLException e) {
				Log.d(TAG, "Creation: Failed trying to create SMS table");
			}
			try {
				// TODO : It's failing in the creation of this table.
				Log.d(TAG, "Creation: Trying to create thread table");
				_db.execSQL(DATABASE_CREATE_THREAD);
				Log.d(TAG, "Creation: " + DATABASE_CREATE_THREAD);
			} catch (SQLException e) {
				Log.d(TAG, "Creation: Failed trying to create thread table");
			}
		}

		/*
		 * Called when there is a database wersion mismatch meaning that the
		 * version of the database on disk needs to be upgraded to the current
		 * version.
		 */
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			// Log the version upgrade.
			Log.w("TaskDBAdapter", "Upgrade from version " + _oldVersion
					+ " to " + _newVersion
					+ ", which will destroy all old data");

			/*
			 * Upgrade the existing database to conform to the new version.
			 * Multiple previous version can be handled by comparing _oldVersion
			 * and _newVersion values.
			 */

			// The simplest case is to drop the old table and create a new one.
			_db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_TABLE);
			_db.execSQL("DROP TABLE IF EXISTS " + THREAD_TABLE);
			// Create a new one.
			onCreate(_db);
		}
	}
}
