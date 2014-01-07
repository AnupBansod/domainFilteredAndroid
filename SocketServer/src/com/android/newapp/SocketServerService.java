package com.android.newapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SocketServerService extends Service {
	String domain_name = "Invalid";
	String application = "Invalid";
	long allow_deny = 2;
	DataHelper db = null;
	Context c = null;
	String delims = ":";
	ServerSocket ss,ss2;
	BufferedReader is= null;
	PrintWriter out;
	String str;
	long allow1 = 2;
	@Override
	public void onCreate() {
		
		Log.e("Anup", "Created service");
		c = this;
		db = new DataHelper(c);
		try {
			ss = new ServerSocket(5000);
			ss2 = new ServerSocket(5001);
			Toast.makeText(c, "Created Socket", Toast.LENGTH_LONG).show();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Toast.makeText(c, "Unable to create Socket", Toast.LENGTH_LONG)
					.show();
		}
	}
/*	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("CheckStartActivity","onActivityResult and resultCode = "+resultCode);
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            Toast.makeText(this, "Pass", Toast.LENGTH_LONG).show();
		Log.d("ANup","Pass CAll Back");
        }
        else{
            Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
		Log.d("Anup","Fail Call Back");
        }
    }	
*/
	public int onStartCommand(Intent intent, int flags, int startId) {
		while (true) {
				Socket s,s2;
				Log.e("Anup", "Started service");
				allow1=2;
				Log.d("Anup", "Wating for data :");
				try {
					s = ss.accept();
					Log.e("Anup","Connection Establised ");					
					is = new BufferedReader(
						new InputStreamReader(s.getInputStream()));
					out = new PrintWriter(s.getOutputStream(), true);
					out.flush();
					Log.e("Anup","Inpput stream created");
					str = is.readLine();
					Log.d("Anup", "recieved Data =  : " + str);
				} 
				catch (Exception e) {
					Log.e("Anup","Accept failed: 5000");
					continue;
				}
				Log.d("Anup", "before get data");
				Log.d("Anup", "recieved Data =  : " + str);
				StringTokenizer st = new StringTokenizer(str, delims);
				domain_name = st.nextToken();
				application = st.nextToken();
				allow1 = db.getAllow(domain_name, application);
				Log.d("Anup", "Allow = " + String.valueOf(allow1));
				if (allow1 == 2) {
					Toast.makeText(
							c,
							"\nDomain: " + domain_name + "\nApplicaion: "
									+ application, Toast.LENGTH_LONG).show();
					Intent intent1 = new Intent(Intent.ACTION_VIEW);
//					Bundle b = new Bundle();
//					b.putString("domain_name", domain_name);
//					b.putString("application", application);
//					intent1.putExtras(b);
					intent1.setClass(getApplicationContext(),
							SocketServerActivity.class);
			//		intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					//intent1.setAction("android.intent.action.MAIN");
					Log.d("Anup", "Starting the activity");
					startActivity(intent1);
						Log.d("Anup", "Activity started from service");
					try{	
						s2 = ss2.accept();
					}
					catch(Exception e2)
					{
						Log.d("Anup","Socket accept error");
					}
					
					Log.d("Anup", "Domain :" + domain_name + "\nApplication"
							+ application);
					Log.d("Anup", "Allow =  : " + String.valueOf(allow1));
					
					} else if (allow1 == 1) {
					Log.e("Anup", "\nRequest Status : Approved");
					Toast.makeText(
							c,
							"\nDomain: " + domain_name + "\nApplicaion: "
									+ application + "Request Approved",
							Toast.LENGTH_LONG).show();
					} else if (allow1 == 0) {
					Log.e("Domain-Filter", "\nRequest Status : Denied");
					Toast.makeText(
							c,
							"\nDomain: " + domain_name + "\nApplicaion: "
									+ application + "Request Denied",
							Toast.LENGTH_LONG).show();
					}
					try
					{
			        	out.println(String.valueOf(allow1));
				    	Log.e("Anup", "\n Sent data" + allow1);
						s.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.e("Anup", "\n Send Gandlay");
					}	
			}
	
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("Domain-Filter", "Ending Server");
		c.startService(new Intent(c, SocketServerService.class));
		try {
			ss.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

