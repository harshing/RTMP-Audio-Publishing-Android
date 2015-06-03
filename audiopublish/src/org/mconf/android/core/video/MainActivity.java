package org.mconf.android.core.video;


import org.apache.log4j.BasicConfigurator;

import com.flazr.rtmp.client.ClientOptions;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	
    private Object[] args={"Test1","18.9750/72.8258","AB","1","Female","info",""};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_capture);
		BasicConfigurator.configure();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		ClientOptions options=new ClientOptions();
		options.setHost("10.129.200.81");
		options.setAppName("HariPanTest3");
		options.setStreamName("Test1");
		options.setArgs(args);
		options.publishLive();
		
		VideoDialog mVideoDialog = new VideoDialog(this, "1", "1", "Test1", 1, options);
		mVideoDialog.show();
		/*
		VideoCapture mVideoCapture = (VideoCapture) findViewById(R.id.video_capture);
		mVideoCapture.startCapture();*/
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
