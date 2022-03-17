package com.vnpt.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.inputmethod.InputMethodManager;

import com.vnpt.common.Common;
import com.vnpt.dto.InvoiceTrG;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Chua cac ham util ve keyboard
 * 
 * @author: Truonglt2
 * @version: 1.0
 * @since: 1.0
 */
public class GeneralsUtils {
	protected static GeneralsUtils instance;
	public static boolean isDebug = true;
	/**
	 * Khoi tao the hien cua lop DatabaseHelp
	 *
	 * @param context
	 *
	 * @return
	 *
	 * @author: truonglt2
	 * @return: DatabaseHelp
	 * @throws:
	 */
	public static GeneralsUtils getInstance() {

		if (instance == null) {
			instance = new GeneralsUtils();
		}
		return instance;
	}
	static boolean keyboardVisible = false;
	public static void turnOffKeyboard(Activity activity) {
		InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		//if (mInputMethodManager.isAcceptingText())
		if (mInputMethodManager.isActive()|| mInputMethodManager.isAcceptingText())
		{
			//"Software Keyboard was shown"
			try {
				mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			//"Software Keyboard was not shown"
			
		}
	}
	/**
	*
	* FOrce hide Keyboard
	* @author: truonglt2
	* @datetime: Dec 16, 2013 5:06:44 PM
	* @return: void
	* @param activityTODO
	* 
	*/
	public static void forceHideKeyboard(Activity activity) {
		if (activity != null && activity.getCurrentFocus() != null) {
			InputMethodManager inputManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	*hide keyboard when before user show keyboard use Toggle.
	* 
	* @author: truonglt2
	* @datetime: Dec 16, 2013 5:06:31 PM
	* @return: void
	* @param activityTODO
	* 
	*/
	public static void forceHideKeyboardUseToggle(Activity activity) {
		try {
			if (activity != null && activity.getCurrentFocus() != null) {
				if (keyboardVisible) {
					InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm != null) {
						imm.toggleSoftInput(0, 0);
					}
					keyboardVisible = false;
				} else {
					InputMethodManager inputManager = (InputMethodManager) activity
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	*
	* show keyboard use ToogleSoftInput
	* @author: truonglt2
	* @datetime: Dec 16, 2013 5:06:15 PM
	* @return: void
	* @param activityTODO
	* 
	*/
	public void showKeyboardUseToggle(Activity activity) {
		if (activity != null && activity.getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			keyboardVisible = true;
		}
	}
	public File getOutputXmlFile(Context context, InvoiceTrG invoiceTrG){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
				+ "/"
				+ context.getPackageName()
				+ "/SignManualHDDT/XMLHDDT");
		StoreSharePreferences.getInstance(context).saveStringPreferences(Common.KEY_PATH_DEFAULT_XML,mediaStorageDir.getPath()+ File.separator);

		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
		File mediaFile;
		String mImageName ="";
		mImageName =  invoiceTrG.getPattern().replace("/","_")+ invoiceTrG.getSerial().replace("/","_")+"_"+invoiceTrG.getInvNo()+".xml";
		mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
		Helper.getInstance().showLog("path3:"+mediaFile.getAbsolutePath());
		StoreSharePreferences.getInstance(context).saveStringPreferences(Common.KEY_PATH_DEFAULT_XML_RECENTLY,mediaFile.getAbsolutePath());
		return mediaFile;
	}

}
