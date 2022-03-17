package com.vnpt.webservice;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.vnpt.common.Common;
import com.vnpt.utils.Helper;

public class UsersServices extends BaseWebservice{


	public UsersServices()
	{
		Common.AUTHORIZE_STRING = Helper.getB64Auth();
		Log.e("co vo day  Common.AUTHORIZE_STRING", Common.AUTHORIZE_STRING + "");
	}
	public JSONObject login(String username, String password)
    {
		String wsUrl = URL_SERVER + USER_API_LOGIN;
		String errorLog = "Error while login";
		Log.e("wsUrl", wsUrl);
		List<NameValuePair> listParams = new ArrayList<>(2);
		listParams.add(new BasicNameValuePair("username", username));
		listParams.add(new BasicNameValuePair("password", password));

		return doGetJSONObjPOST(wsUrl, errorLog, listParams);
    }
}
