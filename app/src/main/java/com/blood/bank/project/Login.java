package com.blood.bank.project;

import org.json.JSONObject;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity{
	EditText user,pass;
	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		mDialog = new Dialog(Login.this, R.style.AppTheme);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mDialog.setContentView(R.layout.circular_dialog);
		mDialog.setCancelable(false);

		user=(EditText) findViewById(R.id.usertxt);
		pass=(EditText) findViewById(R.id.passtxt);

		Boolean ans = weHavePermission();
		if (!ans) {
			requestforPermissionFirst();
		}
		
		Intent i=getIntent();
		try
		{
			String s=i.getStringExtra("userreg");
			if(s.compareTo("")==0)
			{
				user.setText("");
			}
			else
			{
				user.setText(s);
				pass.requestFocus();
			}
		}
		catch(Exception e)
		{
			
		}
	}
	public void login(View v)
	{
		if(user.getText().toString().compareTo("")!=0 && pass.getText().toString().compareTo("")!=0)
		{
			new loginasync().execute(user.getText().toString(),pass.getText().toString());
		}
		else
		{
			Toast.makeText(Login.this, "All Fields are Mandatory", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void register(View v)
	{
		Intent i=new Intent(this,Register.class);
		startActivity(i);
	}
	
	public class loginasync extends AsyncTask<String, JSONObject, String>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.login(params[0], params[1]);
				JSONParser jp=new JSONParser();
				a=jp.logindata(json);
			} catch (Exception e) {
				a=e.getMessage();
			}
			
			return a;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mDialog.dismiss();
			String b="post";
			b=result;
			if(b.compareTo("true")==0)
			{
				Intent i=new Intent(Login.this,Tabs.class);
				i.putExtra("username", user.getText().toString());
				startActivity(i);
			}
			else if(b.compareTo("false")==0)
			{
				Toast.makeText(Login.this,"Invalid User Credentials", Toast.LENGTH_SHORT).show();
				user.setText("");
				pass.setText("");
			}
			else
			{
				if(result.contains("Unable to resolve host"))
                {
                    AlertDialog.Builder ad=new AlertDialog.Builder(Login.this);
                    ad.setTitle("Unable to Connect!");
                    ad.setMessage("Check your Internet Connection,Unable to connect the Server");
                    ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    ad.show();
                }
				else
				{
					Toast.makeText(Login.this,result, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Intent i=getIntent();
		try
		{
			String s=i.getStringExtra("userreg");
			if(s.compareTo("")==0)
			{
				user.setText("");
				pass.setText("");
			}
			else
			{
				user.setText(s);
				pass.requestFocus();
			}
		}
		catch(Exception e)
		{
			user.setText("");
			pass.setText("");
		}
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		Intent i=getIntent();
		try
		{
			String s=i.getStringExtra("userreg");
			if(s.compareTo("")==0)
			{
				user.setText("");
				pass.setText("");
			}
			else
			{
				user.setText(s);
				pass.requestFocus();
			}
		}
		catch(Exception e)
		{
			user.setText("");
			pass.setText("");
		}
		super.onResume();
	}

	//Android Runtime Permission
	private boolean weHavePermission() {
		return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
	}

	private void requestforPermissionFirst() {
		if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
			requestForResultContactsPermission();
		} else {
			requestForResultContactsPermission();
		}
	}

	private void requestForResultContactsPermission() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
	}

}
