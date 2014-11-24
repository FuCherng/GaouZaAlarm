package com.example.gaouza_v2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainM_MyAlarm extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_m__my_alarm);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("§Úªº¾xÄÁ");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_alarm_actionbar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
		case R.id.action_accept:
			//openSearch();
			Toast.makeText(MainM_MyAlarm.this, "New Alarm Confirm", Toast.LENGTH_SHORT).show();
			//after press new alarm, goto MyAlarm activity
			Intent gotoAlarmRequest = new Intent();
			gotoAlarmRequest.setClass(MainM_MyAlarm.this, MainM_AlarmRequest.class);
			startActivity(gotoAlarmRequest);			
			return true;
		case R.id.action_remove:
			Toast.makeText(MainM_MyAlarm.this, "Remove Alarm", Toast.LENGTH_SHORT).show();
			Intent gotoAlarmRequest2 = new Intent();
			gotoAlarmRequest2.setClass(MainM_MyAlarm.this, MainM_AlarmRequest.class);
			startActivity(gotoAlarmRequest2);		
			return true;
		default:
			return super.onOptionsItemSelected(item);
			
		}
	}
}
