package com.vnpt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.vnpt.webservice.BaseWebservice;

import java.util.Map;

/**
 * @Description: lop Store SharePreferences luu tru cac gia tri can thiet vao
 *               SharePreferences
 * @author:truonglt2
 * @since:Feb 7, 2014 5:21:15 PM
 * @version: 1.0
 * @since: 1.0
 * 
 */
public class StoreInvoiceSharePreferences {

	/**
	 *
	 * @decription:
	 * @author: Truong.Le
	 * @datetime: Nov 14, 2013 11:21:53 AM
	 * @param
	 */

	private final static String NAME_PREF = "StoreInvoiceSharePreferences";
	private static StoreInvoiceSharePreferences instance;

	private SharedPreferences sharedPreferences;

	private StoreInvoiceSharePreferences(Context context) {
		this.sharedPreferences = context.getSharedPreferences(NAME_PREF,
				Context.MODE_PRIVATE);
	}

	/**
	 * Get current instance of class
	 * 
	 * @return
	 */
	public static synchronized StoreInvoiceSharePreferences getInstance(Context context) {
		if (instance == null) {
			instance = new StoreInvoiceSharePreferences(context);
		}
		return instance;
	}

	public void clearInfo() {
		Editor edit = sharedPreferences.edit();
		edit.clear();
		edit.commit();
	}

	/**
	 * load gia tri Boolean value
	 * 
	 * @author: truonglt2
	 * @param keyValue
	 * @return Mamadentx7
	 * @return: boolean
	 * @throws:
	 */
	public boolean loadBooleandSavedPreferences(String keyValue) {
		boolean value = sharedPreferences.getBoolean(keyValue, false);
		return value;
	}

	/**
	 * save gia tri boolean value
	 * 
	 * @author: truonglt2
	 * @param key
	 * @param value
	 * @return: void
	 * @throws:
	 */
	public void saveBooleanPreferences(String key, boolean value) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * load gia tri String value
	 * 
	 * @author: truonglt2
	 * @param keyValue
	 * @return
	 * @return: String
	 * @throws:
	 */
	public String loadStringSavedPreferences(String keyValue) {
		String value = "";
		try {
			value = sharedPreferences.getString(keyValue, "");
		} catch (Exception e) {
			value = "";
		}

		return value;
	}
	public String loadStringServerSavedPreferences(String keyValue) {
		String value = "";
		try {
			value = sharedPreferences.getString(keyValue, BaseWebservice.URL_SERVER);
		} catch (Exception e) {
			value = "";
		}

		return value;
	}
	/**
	 * load gia tri int value
	 * 
	 * @author: truonglt2
	 * @param keyValue
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int loadIntegerSavedPreferences(String keyValue) {
		int value = sharedPreferences.getInt(keyValue, 0);
		return value;
	}

	/**
	 * load gia tri float value
	 * 
	 * @author: truonglt2
	 * @param keyValue
	 * @return
	 * @return: float
	 * @throws:
	 */
	public float loadFloatSavedPreferences(String keyValue) {
		float value = sharedPreferences.getFloat(keyValue, 0);
		return value;
	}

	public void saveFloatPreferences(String key, float value) {
		Editor editor = sharedPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	public void saveIntPreferences(String key, int value) {
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	/**
	 * save gia tri String value
	 * 
	 * @author: truonglt2
	 * @param key
	 * @param value
	 * @return: void
	 * @throws:
	 */
	public void saveStringPreferences(String key, String value) {
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public Map<String, ?> loadAllInvoicesShareReferences()
	{
		return sharedPreferences.getAll();
	}
}
