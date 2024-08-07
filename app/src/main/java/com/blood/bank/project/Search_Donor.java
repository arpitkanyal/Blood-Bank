package com.blood.bank.project;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.OAEPParameterSpec;

import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Search_Donor extends Fragment {
	Spinner spin;
	String blood;
	List<String> lst;
	ArrayAdapter<String> aadapt;
	ListView list;
	String[] name;
	String[] pic;
	String[] add;
	String[] no;
	int indexno;
	private Dialog mDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.search_donor, container, false);

		mDialog = new Dialog(getActivity(), R.style.AppTheme);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mDialog.setContentView(R.layout.circular_dialog);
		mDialog.setCancelable(false);

		spin=(Spinner) v.findViewById(R.id.spinner1);
		list=(ListView) v.findViewById(R.id.searchdonorlist);
		lst=new ArrayList<String>();
		
		ArrayAdapter aa=ArrayAdapter.createFromResource(getActivity(), R.array.spindata, R.layout.spinneritem);
		aa.setDropDownViewResource(R.layout.dropdownitem);
		spin.setAdapter(aa);
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {
				
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try
				{
					TextView txt=(TextView) arg1;
					blood=txt.getText().toString();
					if(arg2!=0)
					{	
						list.setAlpha(128);
						if(aadapt.getCount()>0)
						{
							aadapt.clear();
						}
						new alldonor().execute(blood);
					}
					else
					{
						list.setAlpha(0);
					}
				}
				catch(Exception e)
				{
					//Toast.makeText(getActivity(), "catch", Toast.LENGTH_SHORT).show();
					if(arg2!=0)
					{
						new alldonor().execute(blood);
					}
				}
				//Toast.makeText(getActivity(), blood, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				indexno=arg2;
				//Toast.makeText(getActivity(),""+arg2,Toast.LENGTH_SHORT).show();
				dailog();
			}
		});
		return v;
	}
	
	public class alldonor extends AsyncTask<String, JSONObject, String>
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
				JSONObject json=api.SearchDoner(params[0]);
				JSONParser jp=new JSONParser();
				a=jp.donorsearch(json);
			} catch (Exception e) {
				//a=e.getMessage();
			}
			return a;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mDialog.dismiss();
			String b="post";
			b=result;
			//Toast.makeText(getActivity(),b, Toast.LENGTH_SHORT).show();
			if(b.compareTo("")==0)
			{
				Toast.makeText(getActivity(),"No Donors Found for the BloodGroup "+blood, Toast.LENGTH_SHORT).show();
			}
			else if(b.contains("*"))
			{
				
				String all[]=b.split("\\*");
				//Toast.makeText(getActivity(),""+all.length, Toast.LENGTH_SHORT).show();
				name = new String[all.length];
				no = new String[all.length];
				add = new String[all.length];
				pic = new String[all.length];
				
				for(int i=0;i<all.length;i++)
				{
					//Toast.makeText(getActivity(), ""+i, Toast.LENGTH_SHORT).show();
					String named[]=all[i].toString().split("\\$");
					lst.add(named[0]);
					try{
					name[i]=named[0];
					no[i]=named[1];
					add[i]=named[2];
					pic[i]=named[3];
					}catch(Exception e)
					{
						//Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
				
				aadapt=new ArrayAdapter<String>(getActivity(), R.layout.donor_name,R.id.onlydonname, lst);
				list.setAdapter(aadapt);
				
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
	
	public void dailog()
	{
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.donor_fulldetails_list);
		dialog.setTitle(name[indexno]);
		
		TextView text = (TextView) dialog.findViewById(R.id.dondet);
		text.setText(add[indexno]);
		TextView text1 = (TextView) dialog.findViewById(R.id.docon);
		text1.setText(no[indexno]);
		ImageView img = (ImageView) dialog.findViewById(R.id.donimg);
		ImageView call=(ImageView) dialog.findViewById(R.id.call);
		
		String image=pic[indexno];
		byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
		img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
		call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+no[indexno]));
				startActivity(intent); 
			}
		});
		
		text1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+no[indexno]));
				startActivity(intent); 
			}
		});
		
		dialog.show();
	}
	
	@Override
	public void onResume() {
		
		try
		{
			if(aadapt.getCount()>0)
			{
				aadapt.clear();
			}
			//new alldonor().execute(blood);
		}
		catch(Exception e)
		{
			
		}
		super.onResume();
	}
}
