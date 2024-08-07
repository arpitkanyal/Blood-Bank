package com.blood.bank.project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class tabspageradapt extends FragmentPagerAdapter{

	public tabspageradapt(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return new Search_Donor();
		case 1:
			return new Blood_Bank();
		case 2:
			return new Donor_Details();
			
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {

		switch (position){
			case 0 :
				return "Search Donor";
			case 1 :
				return "Blood Bank";
			case 2 :
				return "My Details";
		}
		return null;
	}

}
