package com.example.httptest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class HttpTest extends Activity {
	
	Handler handler = new Handler();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_test);
        /*if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/        
        new Thread(runnable).start(); 
        //handler.post(runnable);
    }
    
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.
        	String str = "";
    		str = "http://140.113.207.184:8080/data_test?data_test=668";
    		HttpGet request = new HttpGet(str);
    		String result = "Not 200";
    		try {
    			DefaultHttpClient client = new DefaultHttpClient();
    			HttpResponse response = client.execute(request);
    			if (response.getStatusLine().getStatusCode() == 200) {
    				result = EntityUtils.toString(response.getEntity());   				
    			}
    		} catch (Exception e) {
    			Toast.makeText(HttpTest.this, e.toString(), Toast.LENGTH_LONG).show();   		
    			return;
    		}
            //handler.sendMessage(msg);    		
    		//runnable=null;
    		//Log.d("Get Response", result);
        }
        //handler.removeCallbacks(runnable);
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
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.http_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
