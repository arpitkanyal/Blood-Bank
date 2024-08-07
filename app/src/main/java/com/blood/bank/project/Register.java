package com.blood.bank.project;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream.PutField;

import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends Activity{
	String gender="a",image="abc",donor="NO",blood;
	EditText name,no,email,add,city,user,pass,uid;
	RadioGroup rg;
	CheckBox check;
	ImageView img;
	ScrollView scroll;
	Spinner spin;
	TextView txt,txth;
    private Dialog mDialog;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		txth=(TextView) findViewById(R.id.textView1);
		name=(EditText) findViewById(R.id.name);
		email=(EditText) findViewById(R.id.email);
		no=(EditText) findViewById(R.id.no);
		add=(EditText) findViewById(R.id.add);
		city=(EditText) findViewById(R.id.city);
		user=(EditText) findViewById(R.id.uname);
		pass=(EditText) findViewById(R.id.pass);
		uid=(EditText) findViewById(R.id.uid);
		img=(ImageView) findViewById(R.id.imageView1);
		rg=(RadioGroup) findViewById(R.id.radioGroup1);
		check=(CheckBox) findViewById(R.id.regcheck);
		scroll=(ScrollView) findViewById(R.id.scrollid);
		spin=(Spinner) findViewById(R.id.regspin);
		txt=(TextView) findViewById(R.id.bloodtextview);

        mDialog = new Dialog(Register.this, R.style.AppTheme);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.circular_dialog);
        mDialog.setCancelable(false);

		new autoid().execute();
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				RadioButton rb=(RadioButton) findViewById(arg1);
				gender=rb.getText().toString();
				//Toast.makeText(Register.this, gender, Toast.LENGTH_SHORT).show();
			}
		});
		check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(check.isChecked())
				{
					check.setChecked(true);
					donor="YES";
					//Toast.makeText(Register.this, donor, Toast.LENGTH_SHORT).show();
					
				}
				else
				{
					check.setChecked(false);
					donor="NO";
					//Toast.makeText(Register.this, donor, Toast.LENGTH_SHORT).show();
					
				}
			}
		});
		
		ArrayAdapter aa=ArrayAdapter.createFromResource(this, R.array.spindata, R.layout.spinneritem);
		aa.setDropDownViewResource(R.layout.dropdownitem);
		spin.setAdapter(aa);
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {
				
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
					scroll.fullScroll(View.FOCUS_DOWN);
					TextView txt1=(TextView) arg1;
					blood=txt1.getText().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	public void subtn(View v)
	{
		if(name.getText().toString().compareTo("")!=0 && gender.toString().compareTo("a")!=0 && email.getText().toString().compareTo("")!=0 && no.getText().toString().compareTo("")!=0 && add.getText().toString().compareTo("")!=0 && city.getText().toString().compareTo("")!=0 && user.getText().toString().compareTo("")!=0 && pass.getText().toString().compareTo("")!=0 && uid.getText().toString().compareTo("")!=0 && donor.toString().compareTo("")!=0)
		{
			if(blood.toString().compareTo("Select...")!=0)
			{
				if(image.toString().compareTo("abc")!=0)
				{
					new registerasync().execute(name.getText().toString(),gender,email.getText().toString(),no.getText().toString(),add.getText().toString(),city.getText().toString(),user.getText().toString(),pass.getText().toString(),uid.getText().toString(),image,donor,blood);
				}
				else
				{
					Toast.makeText(this,"Browse & Select a Image", Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(this,"Select a Blood Group", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(this,"All Fields are Mandatory", Toast.LENGTH_SHORT).show();
		}
	}

	public void browse(View v)
	{
		Intent i=new Intent();
    	i.setType("image/*");
    	i.setAction(Intent.ACTION_GET_CONTENT);
    	startActivityForResult(Intent.createChooser(i, "Select a Picture"),100);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null)
    	{
    		Uri uri=data.getData();
    		
    		try {
				Bitmap bt=MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
				img.setImageBitmap(bt);
				scroll.fullScroll(View.FOCUS_DOWN);
				check.requestFocus();
				//saveintosd();
				convertbase64();
		
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
    		
    		
    	}
	}
	
	public void saveintosd() throws IOException
    {
    	BitmapDrawable drawable=(BitmapDrawable) img.getDrawable();
    	Bitmap bt=drawable.getBitmap();
    	
    	File f=Environment.getExternalStorageDirectory();
    	File dir=new File(f.getAbsolutePath()+"/BBIMAGES/");
    	dir.mkdirs();
    	
    	//String imgnameauto=uid+".png";
    	String imgnameauto="aaa.png";
    	File imgname=new File(dir,imgnameauto);
    	image=imgname.getAbsolutePath();
    	try {
			OutputStream out=new FileOutputStream(imgname);
			bt.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();			
		} catch (FileNotFoundException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
    }
	
	public void convertbase64()
	{
		BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
        Bitmap bitMapNew = getResizedBitmap(bitmap, 500, 500);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitMapNew.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bimg = stream.toByteArray();
		image = Base64.encodeToString(bimg,Base64.DEFAULT);
	}

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
	
	public class registerasync extends AsyncTask<String, JSONObject, String>
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
				JSONObject json=api.register(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11]);
				JSONParser jp=new JSONParser();
				a=jp.regdata(json);
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
			//Toast.makeText(Register.this, ""+b, Toast.LENGTH_SHORT).show();
			if(b.compareTo("true")==0)
			{
				Intent i=new Intent(Register.this,Login.class);
				i.putExtra("userreg",user.getText().toString());
				startActivity(i);
				finish();
			}
			else if(b.compareTo("email")==0)
			{
				email.setText("");
				email.requestFocus();
				email.setError("Already Exist!");
			}
			else if(b.compareTo("uname")==0)
			{
				user.setText("");
				user.requestFocus();
				user.setError("Already Exist!");
			}
			else
			{
				if(result.contains("Unable to resolve host"))
                {
                    AlertDialog.Builder ad=new AlertDialog.Builder(Register.this);
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
					Toast.makeText(Register.this,result, Toast.LENGTH_SHORT).show();
				}
			}
			
		}
	}
	
	
	public class autoid extends AsyncTask<Void, JSONObject, String>
	{

		@Override
		protected String doInBackground(Void... params) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.uid();
				JSONParser jp=new JSONParser();
				a=jp.getid(json);
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
			//Toast.makeText(Register.this, b, Toast.LENGTH_SHORT).show();
			
			if(result.contains("Unable to resolve host"))
            {
                AlertDialog.Builder ad=new AlertDialog.Builder(Register.this);
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
				uid.setText(b);
			}
		
		}
		
	}

}
