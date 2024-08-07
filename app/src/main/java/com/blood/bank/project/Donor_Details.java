package com.blood.bank.project;

import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class Donor_Details extends Fragment{
	ImageView img;
	String donor="",username;
	EditText name,gender,email,no,add,city,blood;
	TextView id;
	CheckBox check;
	int z=0;
	Button update,cancel;
	ScrollView scroll;
	private Dialog mDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.details, container, false);

		mDialog = new Dialog(getActivity(), R.style.AppTheme);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mDialog.setContentView(R.layout.circular_dialog);
		mDialog.setCancelable(false);


		img=(ImageView) v.findViewById(R.id.detailsimg);
		Intent i=getActivity().getIntent();
		username=i.getStringExtra("username");
		new Detailsasync().execute(username);
		name=(EditText) v.findViewById(R.id.dname);
		gender=(EditText) v.findViewById(R.id.dgen);
		blood=(EditText) v.findViewById(R.id.blood);
		email=(EditText) v.findViewById(R.id.demail);
		no=(EditText) v.findViewById(R.id.dcon);
		add=(EditText) v.findViewById(R.id.dadd);
		city=(EditText) v.findViewById(R.id.dcity);
		check=(CheckBox) v.findViewById(R.id.check);
		update=(Button) v.findViewById(R.id.edit);
		cancel=(Button) v.findViewById(R.id.cancel);
		scroll=(ScrollView) v.findViewById(R.id.scroll);
		update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(z==0)
				{
					update.setText("Update");
					cancel.setAlpha(128);
					email.requestFocus();
					email.setEnabled(true);
					add.setEnabled(true);
					no.setEnabled(true);
					city.setEnabled(true);
					if(donor.compareTo("NO")==0)
					{
						check.setAlpha(128);
						check.setEnabled(true);
					}
					z=1;
				}
				else if(z==1)
				{
					new  detailsupdate().execute(username,email.getText().toString(),no.getText().toString(),donor,add.getText().toString(),city.getText().toString());
					z=0;
					
				}
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				update.setText("Edit");
				cancel.setAlpha(0);
				email.setEnabled(false);
				add.setEnabled(false);
				no.setEnabled(false);
				city.setEnabled(false);
				check.setAlpha(0);
				z=0;
			}
		});
		
		check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(check.isChecked())
				{
					donor="YES";
							
				}
				else
				{
					donor="NO";
				}
				
			}
		});
		

		return v;
	}
	
	public class Detailsasync extends AsyncTask<String, JSONObject, String>
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
				JSONObject json=api.MyDetails(params[0]);
				JSONParser jp=new JSONParser();
				a=jp.detailsdata(json);
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
			//Toast.makeText(getActivity(), b, Toast.LENGTH_SHORT).show();
			
			if(b.contains("$"))
			{
				try{
				String str[]=b.split("\\$");
				name.setText(str[0]);
				email.setText(str[1]);
				no.setText(str[2]);
				city.setText(str[3]);
				add.setText(str[4]);
				gender.setText(str[5]);
				blood.setText(str[6]);
				donor=str[8].trim();
				//Toast.makeText(getActivity(), str[8], Toast.LENGTH_SHORT).show();
				check.setAlpha(0);
				if(donor.compareTo("NO")==0)
				{
					check.setAlpha(128);
					check.setEnabled(false);
				}
				String image=str[7];
				byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
		        img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
				}
				catch(Exception e)
				{
					Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
			
			else
			{
				if(result.contains("Unable to resolve host"))
                {
                    AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
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
					Toast.makeText(getActivity(),result, Toast.LENGTH_SHORT).show();
				}
			}
			
		}
		
	}
	
	public class detailsupdate extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... params) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.updateDetails(params[0], params[1], params[2], params[3], params[4], params[5]);
				JSONParser jp=new JSONParser();
				a=jp.updatedetails(json);
			} catch (Exception e) {
				a=e.getMessage();
			}
			return a;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			String b="post";
			b=result;
			//Toast.makeText(getActivity(), b, Toast.LENGTH_SHORT).show();
			
			if(b.compareTo("true")==0)
			{
				update.setText("Edit");
				cancel.setAlpha(0);
				email.setEnabled(false);
				add.setEnabled(false);
				no.setEnabled(false);
				city.setEnabled(false);
				check.setAlpha(0);
				Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(result.contains("Unable to resolve host"))
                {
                    AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
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
					Toast.makeText(getActivity(),result, Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	}
	
	

}
