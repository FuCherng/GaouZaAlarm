package com.example.gaouza_v2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	Button Login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Login = (Button)findViewById(R.id.Btn_login);
		Login.setOnClickListener(login_btn);
	    
		
	}
	public OnClickListener login_btn = new OnClickListener() {
		public void onClick(View v) {
			Intent gotoMainRequest = new Intent();
			gotoMainRequest.setClass(Login.this, MainM_AlarmRequest.class);
			startActivity(gotoMainRequest);
			
		}
	};
	

}
