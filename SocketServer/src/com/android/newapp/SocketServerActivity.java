package com.android.newapp;

import com.android.newapp.DataHelper;
import com.android.newapp.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.InetAddress;
public class SocketServerActivity extends Activity {
    /** Called when the activity is first created. */
	String domain_name;
	String application;
	long allow_deny;
	DataHelper db;
	static Context c;
	Bundle b;
	@Override
	protected void onResume() {
		super.onResume();
		  setContentView(R.layout.main);
	     Log.d("Anup", "Resumed activity");
	     db = new DataHelper(this);
	        c=this;
	        Log.e("Anup", "DB created in activity");
	        try
	        {
	        Log.e("Anup", "I was here");
//	        b = this.getIntent().getExtras();
			domain_name = "Anup";//b.getString("domain_name");
			application = "Bansod";//b.getString("application");
	        }
	        catch (Exception e) {
	        	Log.e("Anup", "Exception and creating service");
//	        	c.startService(new Intent(c, SocketServerService.class));
	        	finish();
	        	Log.e("Anup", "Exiting due to no extra values");
			}
	        Log.e("Anup", "I was here");
	        ((TextView)findViewById(R.id.domainname)).setText("Do you want to allow \n Domain :" + domain_name +"\nApplication" + application);

	        Button allow = (Button)findViewById(R.id.allow);
	        allow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					db.insert(domain_name,application, 1);
					Toast.makeText(c, "Allowed Domain" + domain_name, Toast.LENGTH_LONG).show();
		      //  	c.startService(new Intent(c, SocketServerService.class));
					finish();
				}
			});
	        Button deny = (Button)findViewById(R.id.deny);
	        deny.setOnClickListener(new OnClickListener() {
				 @Override
				public void onClick(View v) {
					db.insert(domain_name,application, 0);
					Toast.makeText(c, "Denied Domain name"+ domain_name, Toast.LENGTH_LONG).show();
		    //    	c.startService(new Intent(c, SocketServerService.class));

				finish();
				}
			});
	        Button cancel = (Button)findViewById(R.id.cancel);
	        cancel.setOnClickListener(new OnClickListener() {
				 @Override
				public void onClick(View v) {
					//db.insert(domain_name,application, 0);
					Toast.makeText(c, "Canceled", Toast.LENGTH_LONG).show();
		    //    	c.startService(new Intent(c, SocketServerService.class))
;
					finish();
				}
			});
	      
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		((TextView) findViewById(R.id.domainname))
			.setText("Do you want to allow \n Domain :");
		Log.e("Anup", "View is set");
//				try{
//			 Socket socket = new Socket(InetAddress.getByName("localhost"),5001);
//		}
//		catch(Exception e)
//		{
//			Log.e("Anup","Socket Gandlay");
//		}

//		finish();
	}
}
