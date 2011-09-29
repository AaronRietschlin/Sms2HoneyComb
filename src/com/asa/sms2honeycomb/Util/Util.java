package com.asa.sms2honeycomb.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.asa.sms2honeycomb.MessageItem;
import com.asa.sms2honeycomb.Preferences;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Util {
	private final static String TAG = "Util";

	/**
	 * Checks to see if there is white space within the input (both username and
	 * email). If there is, then it is an invalid input. Returns true if there
	 * is white space, false if there is no white space.
	 * 
	 * @param input
	 * @return
	 */
	public static boolean containsWhiteSpace(String input) {
		if (input != null) {
			for (int i = 0; i < input.length(); i++) {
				if (Character.isWhitespace(input.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Pushes the email, username, and password to the Parse UserTable object.
	 * 
	 * @param email
	 * @param username
	 * @param password
	 */
	public static void pushToTable(String email, String username,
			String password) {
		ParseObject userTable = new ParseObject("UserTable");
		userTable.put(Preferences.PARSE_EMAIL_ROW, email);
		userTable.put(Preferences.PARSE_USERNAME_ROW, username);
		userTable.put("password", password); // TODO: Encrypt passwords!
		try {
			userTable.save();
			if (Preferences.DEBUG)
				Log.d(TAG, "Save success.");
		} catch (ParseException e1) {
			e1.printStackTrace();
			Log.d(TAG, "Save failed.");
		}
	}

	// TODO work on this
	/**
	 * Gets from the table given the messages (limit 10) sorted by decending
	 * created times and returns a list of them or something
	 * 
	 * @param table
	 * @return totalmessages
	 */
	public String getMessages(String table) {
		final ParseQuery query = new ParseQuery(table);
		query.whereEqualTo(Preferences.PARSE_USERNAME_ROW, getUsernameString());
		query.orderByDescending("createdAt");
		query.setLimit(10);
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> messageList, ParseException e) {
				if (e == null) {
					Log.d(TAG, "Retrieved " + messageList.size() + " messages.");
					for (ParseObject messageObject : messageList) {
						String objectId = messageObject.objectId();
						try {
							ParseObject message = query.get(objectId);
							Date time = message.createdAt();
							String to = message.getString("messageTo");
							String body = message.getString("messageBody");
							String totalMessage = "Sent: " + time + " To: "
									+ to + " Message : " + body;
							System.out.println(totalMessage);
						} catch (ParseException e1) {
							Log.e(TAG, e1.getMessage());
						}
					}
				} else {
					Log.d(TAG, "Error: " + e.getMessage());
				}
			}
		});
		return table;
	}
	
	private static ArrayList<MessageItem> outgoingMessageList; 
	private static ArrayList<MessageItem> incomingMessageList; 
	private static ArrayList<ArrayList<MessageItem>> results;
	
	public static ArrayList<ArrayList<MessageItem>> getAllMessages(final String username){
		ParseQuery queryOutgoing = new ParseQuery("OutgoingMessage");
		ParseQuery queryIncoming = new ParseQuery("IncomingMessage");
		
		queryOutgoing.whereEqualTo(Preferences.PARSE_USERNAME_ROW, username);
		final String outgoingMessageBody, outgoingMessageTo;
		final Date outgoingMessageDate;
		outgoingMessageList = new ArrayList<MessageItem>();
		incomingMessageList = new ArrayList<MessageItem>();
		results = new ArrayList<ArrayList<MessageItem>>();
		// Begin query for messages. 
		queryOutgoing.findInBackground(new FindCallback(){
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if(e == null){
					if(Preferences.DEBUG) Log.d(TAG, "Message size: " + objects.size()); 
					for(ParseObject messageObject : objects){
						MessageItem messageItem = new MessageItem();
						messageItem.setMessageBody(messageObject.getString("messageBody"));
						messageItem.setMessageFrom(messageObject.getString(username));
						messageItem.setMessageTo(messageObject.getString("messageTo"));
						messageItem.setMessageTime(messageObject.createdAt().toLocaleString());
						outgoingMessageList.add(messageItem);
					}
					results.add(outgoingMessageList);
				}else{
					// Error occurred finding ALL objects. TODO
				}
			}
		});
		
		queryIncoming.whereEqualTo(Preferences.PARSE_EMAIL_ROW, username);
		queryIncoming.findInBackground(new FindCallback(){
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if(e == null){
					for(ParseObject messageObject : objects){
						MessageItem messageItem = new MessageItem();
						messageItem.setMessageBody(messageObject.getString("messageBody"));
						messageItem.setMessageFrom(messageObject.getString(username));
						messageItem.setMessageTo(messageObject.getString("messageTo"));
						messageItem.setMessageTime(messageObject.createdAt().toLocaleString());
						incomingMessageList.add(messageItem);
					}
					if(incomingMessageList.size() > 0){
						results.add(incomingMessageList);
					}
				}else{
					// Error occurding finding objects TODO
					Log.e(TAG, "Error finding incoming messages.");
				}
				
			}
		});
		return results;
	}

	/**
	 * Creates a string using the uername and a given name of the wanted push
	 * channel. Returns a string "keyitem_nameOfPushChannel".
	 * 
	 * @param username
	 * @param nameOfPushChannel
	 * @return String username_pushchannel
	 */
	public static String getPushChannel(String username,
			String nameOfPushChannel) {
		String pushChannel = username + "_" + nameOfPushChannel;
		Log.d(TAG, "Push channel has been created for: " + username + "_"
				+ nameOfPushChannel);
		return pushChannel;
	}

	/**
	 * Logouts the current user via the Parse command returns true if logged
	 * out.
	 * 
	 * @return boolean
	 */
	public static boolean logoutUser() {
		ParseUser.logOut();
		if (ParseUser.getCurrentUser() == null) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the current users username via the Parse command
	 * 
	 * @return String currentUser
	 */
	public static String getUsernameString() {
		return ParseUser.getCurrentUser().getUsername();
	}
	
	public static Date formatStringToDate(String stringDate){
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
		Date date = null; 
		try {
			date = formatter.parse(stringDate);
		} catch (java.text.ParseException e) {
			// TODO : Handle this exception
			e.printStackTrace();
		}
		return date;
	}	
	
	static Toast mToast;
	public static void displayToast(Context context, String message){
		mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		mToast.show();
	}
}
