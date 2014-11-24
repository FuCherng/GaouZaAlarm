package com.example.gaouza_v2;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainM_AlarmRequest extends Activity {
	
	//private static final String[] myStringArray = new String[] {"11","22"};
	//"Fufu","Shelly","Bird","Fen","AWOO","Song","MAMA","PAPA","Kate","Lily","Psy"
	String[] myStringArray = new String[] {"11","22"};
	ListView listView;
	Handler handler = new Handler();
	ArrayAdapter<String> adapter; 
	
	private int load_over=1; //load_over=1=not load over yet, load_over=0=load over!
	
	//other id
	String Uid,UserName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_m__alarm_request);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("½Ö»Ý­n§Ú");
		
		//get the username pwd from login
		Intent intent = getIntent();
		UserName = intent.getStringExtra("UName");//from friend list in alarm request
		Uid = intent.getStringExtra("Uid");//from friend list in alarm request
		
		//push user name and pwd to server
		new Thread(runnable).start(); 
		//wait the thread to load the friend Name
		while(load_over==1){
			try {Thread.sleep(1000); }
			catch (InterruptedException e) {e.printStackTrace();}
		}
		
		//After get the friend list, insert to adaptor
		//myStringArray = new String[] {"11","22"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, myStringArray);
		listView = (ListView) findViewById(R.id.friend_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(mMessageClickedHandler);
		  
	}

	Runnable runnable = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.
        	String str = "";
    		str = "http://140.113.207.184:8080/search_alarms?user_id=001";
    		HttpGet request = new HttpGet(str);
    		String result = "Not 200";
    		try {
    			DefaultHttpClient client = new DefaultHttpClient();
    			HttpResponse response = client.execute(request);
    			if (response.getStatusLine().getStatusCode() == 200) {
    				result = EntityUtils.toString(response.getEntity());   				
    			}
    		} catch (Exception e) {
    			//Toast.makeText(HttpTest.this, e.toString(), Toast.LENGTH_LONG).show();   		
    			return;
    		}
            //handler.sendMessage(msg);    		
    		//runnable=null;
    		Log.d("Get Response", result);
    		// list View: set result to myStringArray
    		//after get result, split array from "" , ""
    		myStringArray = result.split("', '");
    		
    		
    		//load over!
    		load_over=0;
        }
        //handler.removeCallbacks(runnable);
    };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
		case R.id.action_new:
			//openSearch();
			//Toast.makeText(MainM_AlarmRequest.this, "New Alarm", Toast.LENGTH_SHORT).show();
			//after press new alarm, goto MyAlarm activity
			Intent gotoMyAlarm = new Intent();
			gotoMyAlarm.setClass(MainM_AlarmRequest.this, MainM_MyAlarm.class);
			startActivity(gotoMyAlarm);
			
			return true;
		case R.id.action_edit:
			//Toast.makeText(MainM_AlarmRequest.this, "go to wake up", Toast.LENGTH_SHORT).show();
			Intent gotoWake = new Intent();
			gotoWake.setClass(MainM_AlarmRequest.this, Wake.class);
			startActivity(gotoWake);
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
			
		}
		
	}
	
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
		@Override
	    public void onItemClick(AdapterView parent, View v, int position, long id) {
	        // Do something in response to the click, id is the item in list
			//Toast.makeText(MainM_AlarmRequest.this, "id="+id+" User Name="+listView.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
			
			//goto send Alarm and pass the friend name
			Intent gotosendAlarm = new Intent();
			gotosendAlarm.setClass(MainM_AlarmRequest.this,  SendAlarm.class);
			gotosendAlarm.putExtra("UserName", listView.getItemAtPosition(position).toString());
			
			startActivity(gotosendAlarm);
	    }
	};
	
	@Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        Thread.interrupted();
        runnable=null;
    }
	
}
