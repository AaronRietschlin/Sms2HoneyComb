package com.asa.sms2honeycomb;

import com.asa.sms2honeycomb.tablet.MainHoneycombActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class NotificationService extends Service {
	private final static String TAG = "NotificationService";
	
    private NotificationManager notificationManager;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = 1;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Override
    public void onCreate() {
    	notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
    	notificationManager.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
		// Choose a drawable to display as the status bar icon
		int icon = R.drawable.icon;
		// Text to display in the status bar when the notification is launched
		CharSequence tickerText = "Message received from: "; // this.getString(R.string.tickerNotificationText);
		String message = "TODO PUT THE MESSAGE IN HERE";
		// The extended status bar orders notification in time order
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		Context context = getApplicationContext();
		// Text to display in the extended status window
		String expandedText = tickerText.toString(); // R.string.expandedNotificationText
		// Title for the expanded status
		String expandedTitle = message; // R.string.expandedNotificationTitle
		// Intent to launch an activity when the extended text is clicked

		// Launch into a new activity where you can say you closed your tab and
		// cancled the alarm!
		Intent intent = new Intent(this, MainHoneycombActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		notification.setLatestEventInfo(context, expandedTitle, expandedText,
				pendingIntent);
		
		// TODO make settings for this OFF/ON and for the notifications
		// Causes the phone to vibrate
		long[] vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 };
		notification.vibrate = vibrate;

		// Causes the phone to ring based on the default notification sound
		Uri ringURI = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		notification.sound = ringURI;

		//int notificationRef = 1;
		notificationManager.notify(NOTIFICATION, notification);
    }
}
