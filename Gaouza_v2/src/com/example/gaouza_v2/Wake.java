package com.example.gaouza_v2;





import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Wake extends Activity {
	
	private static final String[] myStringArray = new String[] {"Fufu say Hi","Shelly","Bird say Hi","Fen","AWOO say Hi",
																"Song","MAMA say Hi","PAPA","Kate say Hi","Lily","Psy"};
	ListView listView;
	int click_count=0;
	
	private ViewGroup mContainerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wake);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, myStringArray);
		listView = (ListView) findViewById(R.id.WakeMessage_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(mMessageClickedHandler);
		
		///for(int i=1;i<myStringArray.length;i++){
		 // Instantiate a new "row" view.
       /* final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.mylistview1, mContainerView, false);

        // Set the text in the new row to a random country.
       // Toast.makeText(Wake.this, Integer.toString(myStringArray.length), Toast.LENGTH_SHORT).show();
        //for(int i=1;i<myStringArray.length;i++){
        	((TextView) newView.findViewById(R.id.user_name)).setText(myStringArray[1]);
        	
        	mContainerView.addView(newView, 0);
	//	}*/
		

		
	}
	
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
		@Override
	    public void onItemClick(AdapterView parent, View v, int position, long id) {
	        // Do something in response to the click, id is the item in list
			//Toast.makeText(Wake.this, "ListSize="+Integer.toString(listView.getCount())+" id="+id+" User Name="+listView.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
			
			click_count++;
			//listView.removeViews(position, position);
			//let the list view's background change color~
			
			if(click_count >= listView.getCount()){//click all mesaage
			 //Toast.makeText(Wake.this, "Clikck over listView", Toast.LENGTH_SHORT).show();
			
				//goto send Alarm and pass the friend name
				Intent gotoAlarmRequest = new Intent();
				gotoAlarmRequest.setClass(Wake.this,  MainM_AlarmRequest.class);
				//gotosendAlarm.putExtra("UserName", listView.getItemAtPosition(position).toString());
			
				startActivity(gotoAlarmRequest);
			}
	    }
	};
}
