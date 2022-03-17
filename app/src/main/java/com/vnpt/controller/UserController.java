package com.vnpt.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.ErrorConstants;
import com.vnpt.common.ModelEvent;
import com.vnpt.models.UserModel;
import com.vnpt.staffhddtpos58.BaseActivity;
import com.vnpt.staffhddtpos58.WellcomeActivity;
import com.vnpt.staffhddtpos58.fragment.BaseFragment;
import com.vnpt.utils.ToastMessageUtil;

/**
 * @Description: lop controller dieu kien
 * @author:truonglt2
 * @since:Feb 7, 2014 3:53:57 PM
 * @version: 1.0
 * @since: 1.0
 */
public class UserController extends AbstractController {

    static UserController instance;

    protected UserController() {
    }

    /**
     * khoi tao instance of lop
     *
     * @return
     * @author: truonglt2
     * @return: UserController
     * @throws:
     */
    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    /**
     * chuyen doi activity
     *
     * @param e
     * @author: truonglt2
     * @return: UserController
     * @throws:
     */
    @Override
    public void handleSwitchActivity(ActionEvent e) {
        Activity base = (Activity) e.sender;
        Intent intent;
        Bundle extras;
        switch (e.action) {
            case ActionEventConstant.ACTION_CHANGE_VIEW_WELLCOME:
                intent = new Intent(base, WellcomeActivity.class);
                extras = (Bundle) e.viewData;
                intent.putExtras(extras);
                base.startActivity(intent);
                break;
            // case ActionEventConstant.ACTION_CHANGE_VIEW_MAIN_ACTIVTY:
            // intent = new Intent(base, MainActivity.class);
            // extras = (Bundle) e.viewData;
            // intent.putExtras(extras);
            // base.startActivity(intent);
            // base.overridePendingTransition(R.anim.slide_in_right,
            // R.anim.slide_out_left);
            // break;
            // case ActionEventConstant.ACTION_CHANGE_VIEW_DETAIL_BOOK_ACTIVTY:
            // intent = new Intent(base, DetailsTransactionActivity.class);
            // extras = (Bundle) e.viewData;
            // intent.putExtras(extras);
            // base.startActivity(intent);
            // break;
        }
    }

    /**
     * xu ly cac event tu view
     *
     * @param e
     * @author: truonglt2
     * @return: UserController
     * @throws:
     */
    public void handleViewEvent(final ActionEvent e) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                switch (e.action) {
                    // get data list catagory
                    case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST_LOAD_MORE:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_GET_DATA_INVOICE_SIGN_BY_LIST:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_FKEY:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST_SEARCH:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_GET_INFOR_OUTLET:
                        UserModel.getInstance().requestData(e);
                        break;

                    case ActionEventConstant.ACTION_UPATE_CERTIFIED_ON_INVOICE:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_UPATE_STATUS_PAYMENT_INVOICE:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_FILTER_AND_SORT_INVOICE:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_SEARCH_DATA_INVOICE:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_SEARCH_FULL_DATA_INVOICE:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_SEARCH_DATA_WITH_ID_CUSTOMER_INVOICE:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_GET_ALL_TERM:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_GET_DATA_DETAIL_INVOICE:
                        UserModel.getInstance().requestData(e);
                        break;
                    case ActionEventConstant.ACTION_UPATE_STATUS_PAYMENT_INVOICE_NEW:
                    {
                        UserModel.getInstance().requestData(e);
                        break;
                    }
                    case ActionEventConstant.ACTION_UPDATE_STATUS_RETURN_INVOICE:
                    {
                        UserModel.getInstance().requestData(e);
                        break;
                    }
                    case ActionEventConstant.ACTION_SAVE_NEW_DATA_INVOICE:
                    {
                        UserModel.getInstance().requestData(e);
                        break;
                    }
                    case ActionEventConstant.ACTION_GET_DATA_ORG_INVOICE:
                    {
                        UserModel.getInstance().requestData(e);
                        break;
                    }
                    case ActionEventConstant.ACTION_GET_DATA_FEE_INVOICE:
                    {
                        UserModel.getInstance().requestData(e);
                        break;
                    }

                    default:
                        break;
                }
                return null;
            }
        };
        task.execute();
    }

    /**
     * xu lu cac event model
     *
     * @param modelEvent
     * @author: truonglt2
     * @return: UserController
     * @throws:
     */
    @Override
    public void handleModelEvent(final ModelEvent modelEvent) {
        if (modelEvent.getModelCode() == ErrorConstants.ERROR_CODE_SUCCESS) {
            final ActionEvent e = modelEvent.getActionEvent();
            if (e.sender != null) {

                if (e.sender instanceof BaseActivity) {
                    final BaseActivity sender = (BaseActivity) e.sender;
                    if (sender.isFinished)
                        return;
                    sender.runOnUiThread(new Runnable() {
                        public void run() {
                            // TODO Auto-generated method stub
                            sender.handleModelViewEvent(modelEvent);
                        }
                    });
                }  else if (e.sender instanceof BaseFragment) {
                    final BaseFragment sender = (BaseFragment) e.sender;
                    if (sender.getActivity() == null) {
                        return;
                    }
                    sender.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            // TODO Auto-generated method stub
                            sender.handleModelViewEvent(modelEvent);
                        }
                    });
                }

            } else {
                handleErrorModelEvent(modelEvent);
            }
        } else {
            handleErrorModelEvent(modelEvent);
        }
    }

    /**
     * Xu ly cac su kien loi
     *
     * @param modelEvent
     * @author: truonglt2
     * @return: UserController
     * @throws:
     */
    @Override
    public void handleErrorModelEvent(final ModelEvent modelEvent) {
        if (modelEvent.getModelCode() == ErrorConstants.ERROR_COMMON) {
            final ActionEvent e = modelEvent.getActionEvent();
            if (e.sender != null) {
                if (e.sender instanceof BaseActivity) {
                    final BaseActivity sender = (BaseActivity) e.sender;
                    if (sender.isFinished)
                        return;
                    sender.runOnUiThread(new Runnable() {
                        public void run() {
                            ToastMessageUtil
                                    .showToastShort(sender,
                                            "Xảy ra lỗi trong quá trình xử lý dữ liệu.");
                            sender.handleErrorModelViewEvent(modelEvent);
                            try {
                                Log.d("Mã lỗi xảy ra:",
                                        "" + modelEvent.getModelMessage());
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    });
                } else if (e.sender instanceof BaseFragment) {
                    final BaseFragment sender = (BaseFragment) e.sender;
                    if (sender.getActivity() == null) {
                        return;
                    }
                    sender.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
							sender.handleErrorModelViewEvent(modelEvent);
                            try {
                                Log.d("Mã lỗi xảy ra:",
                                        "" + modelEvent.getModelMessage());
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }
}
