package com.blood.bank.project;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

	public String regdata(JSONObject json) {
		String p="parse";
		try {
			p=json.getString("Value");
		} catch (JSONException e) {
			p=e.getMessage();
		}
		return p;
	}

	public String getid(JSONObject json) {
		String p="parse";
		try {
			p=json.getString("Value");
		} catch (JSONException e) {
			p=e.getMessage();
		}
		return p;
	}

	public String checkusername(JSONObject json) {
		String p="parse";
		try {
			p=json.getString("Value");
		} catch (JSONException e) {
			p=e.getMessage();
		}
		return p;
	}

	public String logindata(JSONObject json) {
		String p="parse";
		try {
			p=json.getString("Value");
		} catch (JSONException e) {
			p=e.getMessage();
		}
		return p;
	}

	public String detailsdata(JSONObject json) {
		String p="parse";
		try {
			p=json.getString("Value");
		} catch (JSONException e) {
			p=e.getMessage();
		}
		return p;
	}

	public String updatedetails(JSONObject json) {
		String p="parse";
		try {
			p=json.getString("Value");
		} catch (JSONException e) {
			p=e.getMessage();
		}
		return p;
	}

	public String donorsearch(JSONObject json) {
		String p="parse";
		try {
			p=json.getString("Value");
		} catch (JSONException e) {
			//p=e.getMessage();
		}
		return p;
	}

	public String bbdata(JSONObject json) {
		String p="parse";
		try {
			p=json.getString("Value");
		} catch (JSONException e) {
			//p=e.getMessage();
		}
		return p;
	}

	public String namenodata(JSONObject json) {
		String p="parse";
		try {
			p=json.getString("Value");
		} catch (JSONException e) {
			//p=e.getMessage();
		}
		return p;
	}

	public String sendr(JSONObject json) {
		String p="parse";
		try {
			p=json.getString("Value");
		} catch (JSONException e) {
			p=e.getMessage();
		}
		return p;
	}

}
