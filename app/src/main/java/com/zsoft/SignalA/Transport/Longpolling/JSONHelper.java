package com.zsoft.SignalA.Transport.Longpolling;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {

	public static JSONObject ToJSONObject(String text)
	{
		if(text == null){
			return null;
		}
		JSONObject json;
		try {
			json = new JSONObject(text);
		} catch (JSONException e) {
			json = null;
		}
		return json;
	}
}