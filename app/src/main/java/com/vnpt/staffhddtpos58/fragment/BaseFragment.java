/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vnpt.staffhddtpos58.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.vnpt.common.ModelEvent;

/** 
  * @Description: lop BaseFragment
  * @author:truonglt2
  * @since:Feb 7, 2014 4:24:20 PM
  * @version: 1.0
  * @since: 1.0
  * 
  */
public abstract class BaseFragment extends Fragment {
	Bundle mContent;
	public static final String TAG = "BaseFragment VNPT"; // This is tag to show log
	protected static boolean isDebug = true; // This is used to show log
	protected int mCountPage = 1; // This is use to count the number of page api
	protected boolean mIsHaveNoAPIData = false; // This is a flag variable, this is use to check
										// there is no data more
	/**
	 * This method is used to init all members of fragment
	 */
	protected abstract void init(View layout);

	/**
	 * This method is used to set value for all members of fragment
	 */
	protected abstract void setValueForMembers();

	/**
	 * This method is used to set event for member
	 */
	protected abstract void setEventForMembers();

	/**
	*  xu ly du lieu data model
	*  @author: truonglt2
	*  @param modelEvent
	*  @return: void
	*  @throws:
	*/
	public abstract void handleModelViewEvent(ModelEvent modelEvent);
	/**
	 *  xu ly loi du lieu data model
	 *  @author: truonglt2
	 *  @param modelEvent
	 *  @return: void
	 *  @throws:
	 */
	public abstract void handleErrorModelViewEvent(ModelEvent modelEvent);

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			//Restore the fragment's instance
//			mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		}
	}
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//
//		//Save the fragment's instance
//		getActivity().getSupportFragmentManager().putFragment(outState, "mContent", mContent);
//	}
}
