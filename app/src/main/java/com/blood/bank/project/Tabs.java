package com.blood.bank.project;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import static android.support.v7.app.ActionBar.*;


public class Tabs extends AppCompatActivity{

	ActionBar ab;
	ViewPager vp;
	tabspageradapt tabsad;
	private TabLayout tabLayout;
	
	String tabs[]={"Search Donor","Blood Bank","My Details"};
	int[] pic={R.drawable.search,R.drawable.blood,R.drawable.det};
	//Drawable[] a=new;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);
		
		vp=(ViewPager) findViewById(R.id.pager);

		tabLayout = (TabLayout) findViewById(R.id.tabs);

		tabLayout.setSelectedTabIndicatorHeight(4);

		tabsad = new tabspageradapt(getSupportFragmentManager());

		vp.setAdapter(tabsad);

		tabLayout.setupWithViewPager(vp);
		tabLayout.getTabAt(0).setIcon(pic[0]);
		tabLayout.getTabAt(1).setIcon(pic[1]);
		tabLayout.getTabAt(2).setIcon(pic[2]);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		
		MenuInflater mi=getMenuInflater();
		mi.inflate(R.menu.tabs, menu);
		
		return true;
		
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch(item.getItemId())
		{
		case R.id.logout:
			Intent i=new Intent(Tabs.this,Login.class);
			startActivity(i);
			finish();
			break;
		
		}
		
		return false;
	}
}
