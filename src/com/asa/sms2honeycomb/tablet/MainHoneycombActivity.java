package com.asa.sms2honeycomb.tablet;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.asa.sms2honeycomb.R;
import com.parse.PushService;

public class MainHoneycombActivity extends Activity{
	private final String TAG = "MainHoneycombActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: Start the Honeycomb activity
		// This is going to be the main part of the app.
        //TODO: Rename the view to something more specific to honeycomb (i.e. xlarge_main.xml)
		setContentView(R.layout.activity_tablet_main);
		Log.e(TAG, "HoneycombActivity started.");
		PushService.subscribe(this, "tabletChannel", MainHoneycombActivity.class);
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		MessageViewFragment fragment = new MessageViewFragment();
		ft.add(R.id.fragment_container, fragment);
		ft.commit();
	}
}
