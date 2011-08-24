package com.asa.sms2honeycomb;

import java.util.ArrayList;

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
	private static final String DATABASE_NAME = "sms2honeycomb.db";
	private static final String DATABASE_TABLE = "messageTable";
	private static final int DATABASE_VERSION = 1;

	// The index (key) column name for use in where clauses.
	// Column names:
	public static final String KEY_ID = "_id";
	public static final String KEY_TIME = "_time";
	public static final String KEY_TO = "_to";
	public static final String KEY_FROM = "_from";
	public static final String KEY_BODY = "_body";

	// Used to identify the columns in other classes.
	public static final int ID_COLUMN = 0;
	public static final int TIME_COLUMN = 1;
	public static final int TO_COLUMN = 2;
	public static final int FROM_COLUMN = 3;
	public static final int BODY_COLUMN = 4;

	// SQL Statement to create a new database.
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ID
			+ " integer primary key autoincrement, " + KEY_TIME + " text, "
			+ KEY_TO + " text, " + KEY_FROM + " text, " + KEY_BODY + " text);";

	// Variables to hold the database instance
	private SQLiteDatabase db;
	// Context of the appliction using the database
	private final Context context;
	// Database open/upgrade helper
	private myDbHelper dbHelper;

	public DatabaseAdapter(Context _context) {
		Log.d(TAG, "DatabaseHandler constructor is working");
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
		newMessageValues.put(KEY_TIME, item.getTime());
		newMessageValues.put(KEY_TO, item.getTo());
		newMessageValues.put(KEY_FROM, item.getFrom());
		newMessageValues.put(KEY_BODY, item.getBody());
		// Inserts the new row into the database
		Log.d(TAG, "Values: " + item.getTime() + item.getTo() + item.getFrom()
				+ item.getBody() + "has been put in to: " + DATABASE_NAME);
		return db.insert(DATABASE_TABLE, null, newMessageValues);
	}

	public Cursor getAllEntries() {
		Log.d(TAG, "Getting all entries from: " + DATABASE_TABLE);
		return db.query(DATABASE_TABLE, new String[] { KEY_ID, KEY_TIME,
				KEY_TO, KEY_FROM, KEY_BODY }, null, null, null, null, null);
	}

	public boolean removeEntry(long _rowIndex) {
		Log.d(TAG, "Removed entry: " + _rowIndex);
		return db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null) > 0;
	}

	public boolean updateEntry(long _rowIndex, MessageItem _messageItem) {
		// TODO: Create a new ContentValues based on the new object
		// and use it to update a row e database.
		return true;
	}

	/**
	 * Returns the MessageItem at the given index in the database.
	 */
	public MessageItem getMessageItem(long index) throws SQLException {
		Cursor cursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ID,
				KEY_TIME, KEY_TO, KEY_FROM, KEY_BODY }, KEY_ID + "=" + index,
				null, null, null, null, null);
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("No location item found for row #" + index);
		}
		String time = cursor.getString(TIME_COLUMN);
		String to = cursor.getString(TO_COLUMN);
		String from = cursor.getString(FROM_COLUMN);
		String body = cursor.getString(BODY_COLUMN);
		Log.d(TAG, "MessageItem containing: " + time + to + from + body
				+ " queryed and pulled from the table: " + DATABASE_TABLE);
		return new MessageItem(time, to, from, body);

	}

	/**
	 * Querys the Database for the key and what to sortBy and the values are
	 * given by the Time descending. The keys are: KEY_ID, KEY_TIME, KEY_TO,
	 * KEY_FROM, KEY_BODY. Returns an ArrayList that can be used with a ListView
	 * to display the messages
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            sortBy
	 * @return ArrayList<String> list
	 */
	public ArrayList<String> getMessageArrayList(String key, String sortBy) {
		ArrayList<String> list = new ArrayList<String>();
		// Only get values from the to number given and sort by time oldest at
		// the end of the list
		// TODO get both of the TO and FROM messages combined into one ArrayList
		Cursor cursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ID,
				KEY_TIME, KEY_TO, KEY_FROM, KEY_BODY }, KEY_FROM + " AND "
				+ KEY_TO + "=" + sortBy, null, null, null, KEY_TIME, null);
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("No location item found for " + key + " #"
					+ sortBy);
		}
		if (cursor.moveToFirst()) {
			do {
				String time = cursor.getString(TIME_COLUMN);
				String to = cursor.getString(TO_COLUMN);
				String from = cursor.getString(FROM_COLUMN);
				String body = cursor.getString(BODY_COLUMN);
				Log.d(TAG, "MessageItem containing: " + time + to + from + body
						+ " queryed and pulled from the table: "
						+ DATABASE_TABLE);
				if (to == null) {
					String message = "Received: " + time + "\n" + "From: "
							+ from + "\n" + "Message : " + body + "\n";
					list.add(message);
					Log.d(TAG, message
							+ " was added to the ArrayList with the length of "
							+ list.size());
				}
				if (from == null) {
					String message = "Sent: " + time + "\n" + "To: " + to
							+ "\n" + "Message : " + body + "\n";
					list.add(message);
					Log.d(TAG, message
							+ " was added to the ArrayList with the length of "
							+ list.size());
				}
			} while (cursor.moveToNext());
			{
				cursor.close();
			}
		}
		return list;
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
			Log.d(TAG, "Created: " + DATABASE_CREATE);
			_db.execSQL(DATABASE_CREATE);
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
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			// Create a new one.
			onCreate(_db);
		}
	}
}
