package com.example.httptest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class HttpTest extends Activity {
	
	Button Login;
	private ProgressBar login_probar;
	private TextView txtResult;
	private String url = "http://140.113.207.184:8080/pictures/pic_001.png";
	//"http://140.113.207.184:8080/download/?filepath=E:/GaouZaAlarm/python_shelly_server_db/pictures/pic_001.png"; 
	//"http://140.113.207.184:8080/pictures/pic_001.png";
	Bitmap rbm;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_test);
        
        Login = (Button)findViewById(R.id.Btn_login);
        Login.setOnClickListener(login_btn);
        
        login_probar = (ProgressBar)findViewById(R.id.ProBar_login);
        
        txtResult = (TextView) findViewById(R.id.txtTaskResult);    
    }
    
    public OnClickListener login_btn = new OnClickListener() {
		public void onClick(View v) {			
			//new LoadingDataAsyncTask().execute(100);
			login_probar.setVisibility(View.VISIBLE);
			new LoadDataFromServer().execute(url);			
		}
	};
	
	private String getData(String url){
		HttpGet request = new HttpGet(url);
		String result = "Not 200";
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				//result = EntityUtils.toString(response.getEntity());
				result = "Get the file.";
				
				/*Intent it = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.parse(url);
				it.setDataAndType(uri, "image/*");
				startActivity(it); // 直接撥放音檔*/
				
				/*Uri uri = Uri.parse("http://140.113.207.184:8080/download/?filepath=E:/GaouZaAlarm/python_shelly_server_db/pictures/pic_0011.png");
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it); // 由 internet下載*/
								
				//Toast.makeText(HttpTest.this, result, Toast.LENGTH_LONG).show();				
			}
		} catch (Exception e) {  		
			//return e.toString();
			result = e.toString();
		} finally {
			client.getConnectionManager().shutdown();
		}
		return result;
	}

	private void showData(String result){
		txtResult.setText(result);
		login_probar.setVisibility(View.INVISIBLE);
		// show ImageView
		ImageView imageView = (ImageView)findViewById(R.id.iv01);
		imageView.setImageBitmap(rbm);
	}
	
	private Bitmap getImageBitmap(String url) { 
        Bitmap bm = null; 
        try { 
            URL aURL = new URL(url); 
            URLConnection conn = aURL.openConnection(); 
            conn.connect(); 
            InputStream is = conn.getInputStream(); 
            BufferedInputStream bis = new BufferedInputStream(is); 
            bm = BitmapFactory.decodeStream(bis); 
            bis.close(); 
            is.close(); 
       } catch (IOException e) { 
    	   Log.e("Error getting bitmap", e.toString());
       } 
       return bm; 
    } 
	
	class LoadDataFromServer extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			//String res = getData(params[0]);
			rbm = getImageBitmap(params[0]);
			return "Get The File.";
		}
		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
		}
		@Override
		protected void onPostExecute(String result) { // background執行完後才會call
			//super.onPostExecute(result);
			//txtResult.setText(result);
			showData(result);
		}
	}
	
	class LoadingDataAsyncTask extends AsyncTask<Integer, Integer, String>{

		@Override
		protected String doInBackground(Integer... params) { // 一定必須覆寫的方法，把會花時間執行的內容放在這裡
			//getData(); // 這裡不能和UI有任何互動--取資料就好><
			for(int i=0;i<=100;i++){
				login_probar.setProgress(i);
				publishProgress(i,5);
				try {
					Thread.sleep(params[0]);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return "Loading done !";
		}

		@Override
		protected void onProgressUpdate(Integer... progress) { // 這個方法會取得一個數值，
			//可以用來計算目前執行的進度，通常用來改變進度列(ProgressBar)的顯示。 
			//super.onProgressUpdate(progress);
			//UpdateProgress(values);
			txtResult.setText(String.valueOf(progress[0]));
		}
		
		@Override
		protected void onPostExecute(String result) { // background執行完後才會call
			//super.onPostExecute(result);
			showData(result); // 用來把取得的資料送給UI來顯示
		}
		
		@Override
		protected void onPreExecute() { // 在背景執行之前要做什麼事都是寫在這裡。
			super.onPreExecute();
		}

	}
    /*Runnable runnable = new Runnable(){
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
    };*/
    
    /*@Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        Thread.interrupted();
        runnable=null;
    }*/
    
    
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.http_test, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
