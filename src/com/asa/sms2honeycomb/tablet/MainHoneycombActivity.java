package com.asa.sms2honeycomb.tablet;

import android.app.Activity;
import android.os.Bundle;

import com.asa.sms2honeycomb.R;
import com.parse.PushService;

public class MainHoneycombActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: Start the Honeycomb activity
		// This is going to be the main part of the app.
        //TODO: Rename the view to something more specific to honeycomb (i.e. xlarge_main.xml)
		setContentView(R.layout.main);
		PushService.subscribe(this, "tabletChannel", MainHoneycombActivity.class);
	}
}
