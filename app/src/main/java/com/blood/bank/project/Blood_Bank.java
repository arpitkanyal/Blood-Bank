package com.blood.bank.project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class Blood_Bank extends Fragment{
	Spinner spin;
	String blood,username;
	String name,no;
	ListView list;
	String[] bgname;
	String[] bgno;
	List<HashMap<String, String>> lst;
	HashMap<String, String> hm;
	Button send;
	Calendar cal;
	String date,time;
	String[] from={"a","b"};
	int[] to={R.id.bgname,R.id.bgno};
	SimpleAdapter adapt;
	private Dialog mDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.bloodbank, container, false);
		mDialog = new Dialog(getActivity(), R.style.AppTheme);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mDialog.setContentView(R.layout.circular_dialog);
		mDialog.setCancelable(false);

		Intent i=getActivity().getIntent();
		username=i.getStringExtra("username");
		spin=(Spinner) v.findViewById(R.id.bloodspin);
		list=(ListView) v.findViewById(R.id.bblist);
		ArrayAdapter aa=ArrayAdapter.createFromResource(getActivity(), R.array.spindata, R.layout.spinneritem);
		aa.setDropDownViewResource(R.layout.dropdownitem);
		spin.setAdapter(aa);
		send=(Button) v.findViewById(R.id.sendr);
		lst=new ArrayList<HashMap<String,String>>();
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {
				
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try
				{
					TextView txt=(TextView) arg1;
					blood=txt.getText().toString();
				}
				catch(Exception e)
				{
					
				}
				//Toast.makeText(getActivity(), blood, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		new bb().execute();
		new nameno().execute(username);
		//getdate();
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Toast.makeText(getActivity(),"click", Toast.LENGTH_SHORT).show();
				if(blood.compareTo("")!=0)
				{
					//Toast.makeText(getActivity(),"if", Toast.LENGTH_SHORT).show();
					getdate();
					new senr().execute(username,name,blood,no,date,time);
				}
				else
				{
					
					Toast.makeText(getActivity(), "Select a BloodGroup", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return v;
	}
	
	public class bb extends AsyncTask<String, JSONObject, String>{

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
				JSONObject json=api.bloodbank();
				JSONParser jp=new  JSONParser();
				a=jp.bbdata(json);
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
			
			if(b.contains("*"))
			{
				String str[]=b.split("\\*");
				bgname=new String[str.length];
				bgno=new String[str.length];
				for(int i=0;i<str.length;i++)
				{
					String str1[]=str[i].split("\\$");
					bgname[i]=str1[0];
					bgno[i]=str1[1];
				}
				
				for(int j=0;j<bgname.length;j++)
				{
					hm=new HashMap<String, String>();
					hm.put("a",bgname[j]);
			    	hm.put("b",bgno[j]);
			    	lst.add(hm);
				}
				
				adapt=new SimpleAdapter(getActivity(), lst, R.layout.bloodbank_list_item, from, to);
			    list.setAdapter(adapt);
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
	
	public class nameno extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... params) {
			
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.GetNameNo(params[0]);
				JSONParser jp=new  JSONParser();
				a=jp.namenodata(json);
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
			
			if(b.contains("$"))
			{
				String str[]=b.split("\\$");
				name=str[0];
				no=str[1];
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
			
			//Toast.makeText(getActivity(),name+" "+no, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public class senr extends AsyncTask<String, JSONObject, String>
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
				JSONObject json=api.SendReq(params[0], params[1], params[2], params[3], params[4], params[5]);
				JSONParser jp=new  JSONParser();
				a=jp.sendr(json);
			} catch (Exception e) {
				a=e.getMessage();
			}
			
			return a;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//Toast.makeText(getActivity(),"post", Toast.LENGTH_SHORT).show();
			mDialog.dismiss();
			String b="post";
			b=result;
			//Toast.makeText(getActivity(), "bb:"+b, Toast.LENGTH_SHORT).show();
			if(b.compareTo("true")==0)
			{
				Toast.makeText(getActivity(), "Request Sent to the Bank", Toast.LENGTH_SHORT).show();
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
	
	public void getdate()
	{
		cal=Calendar.getInstance();
		int y=cal.get(Calendar.YEAR);
		int m=cal.get(Calendar.MONTH);
		int d=cal.get(Calendar.DAY_OF_MONTH);
		date=y+"/"+m+"/"+d;
		
		int h=cal.get(Calendar.HOUR);
		int min=cal.get(Calendar.MINUTE);
		int s=cal.get(Calendar.SECOND);
		time=h+":"+min+":"+s;
		//Toast.makeText(getActivity(), date+"\n"+time, Toast.LENGTH_SHORT).show();
	}
	
}
