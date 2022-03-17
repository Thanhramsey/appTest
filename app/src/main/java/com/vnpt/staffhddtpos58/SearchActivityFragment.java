package com.vnpt.staffhddtpos58;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.vnpt.adapters.ListHoaDonAdapter;
import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ErrorConstants;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.HoaDon;
import com.vnpt.staffhddtpos58.fragment.BaseFragment;
import com.vnpt.utils.DateTimeUtil;
import com.vnpt.utils.GeneralsUtils;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.ToastMessageUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public static String TAG = SearchActivityFragment.class.getName();

    private Spinner spnBillType;
    private ImageView btnBillTime_from, btnBillTime_to;
    private EditText edtNameCustomer,edtNumInvoice, edtBillTimeFrom, edtBillTimeTo;
    private Button btnToLookup;

    ListView listviewSearch;
    SwipeRefreshLayout swipeRefreshLayout;
    ListHoaDonAdapter adapter;
    ProgressBar progressbarSearch;
    TextView txtNoData;

    private int mTypeInvoice;
    public SearchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search, container, false);
        init(layout);
        setValueForMembers();
        setEventForMembers();
        return layout;
    }

    /**
     * kho tao cac thanh phan giao dien
     *
     * @param layout
     * @author: truonglt2
     * @return: MainActivityFragment
     * @throws:
     */
    @Override
    protected void init(View layout) {
        listviewSearch = (ListView) layout.findViewById(R.id.lvData);
        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_refresh_layout);
        txtNoData = (TextView) layout.findViewById(R.id.txtNoData);
        progressbarSearch = (ProgressBar) layout.findViewById(R.id.progressbarSearch);


        spnBillType = (Spinner) layout.findViewById(R.id.spnBillType);
        edtBillTimeTo = (EditText) layout.findViewById(R.id.edtBillTime_to);
        edtBillTimeFrom = (EditText) layout.findViewById(R.id.edtBillTime_from);
        edtNumInvoice = (EditText) layout.findViewById(R.id.edtNumInvoice);
        edtNameCustomer = (EditText)layout.findViewById(R.id.edtNameCustomer);

        btnToLookup = (Button) layout.findViewById(R.id.btnToLookup);
        btnBillTime_from = (ImageView) layout.findViewById(R.id.btnBillTime_from);
        btnBillTime_to = (ImageView) layout.findViewById(R.id.btnBillTime_to);
    }

    /**
     * set gia tri cho cac thanh phan
     *
     * @author: truonglt2
     * @return: MainActivityFragment
     * @throws:
     */
    @Override
    protected void setValueForMembers() {
        mTypeInvoice = 0;
        // TODO Auto-generated method stub
        adapter = new ListHoaDonAdapter(getActivity());
        swipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        validateDataSearch();
                    }
                });
        validateDataSearch();
        spnBillType.setSelection(mTypeInvoice-1);
        edtBillTimeFrom.setText(""+ DateTimeUtil.getCurrentDate());
        edtBillTimeTo.setText(""+DateTimeUtil.getCurrentDate());
    }
    /**
     * set su kien cho cac thanh phan
     *
     * @author: truonglt2
     * @return: MainActivityFragment
     * @throws:
     */
    @Override
    protected void setEventForMembers() {
        btnBillTime_from.setOnClickListener(this);
        btnBillTime_to.setOnClickListener(this);
        btnToLookup.setOnClickListener(this);
        // TODO Auto-generated method stub
        listviewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HoaDon item = adapter.getItem(position);
                showActivityDetailsInvoice(item);
            }
        });
        spnBillType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();*/
                mTypeInvoice = position ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnBillTime_from:
                DialogFragment dateDialogFrag = new SelectDateFragment(1);
                dateDialogFrag.show(getFragmentManager(), "SelectDateFragmentFrom");
                break;
            case R.id.btnBillTime_to:
                DialogFragment dateDialogFragTo = new SelectDateFragment(2);
                dateDialogFragTo.show(getFragmentManager(), "SelectDateFragmentTo");
                break;
            case R.id.btnToLookup:
                validateDataSearch();
                break;
            default:
                break;
        }
    }


    void validateDataSearch() {
        GeneralsUtils.forceHideKeyboard(getActivity());
        int typeFilter = StoreSharePreferences.getInstance(
                getActivity()).loadIntegerSavedPreferences(
                Common.REF_KEY_TYPE_FILTER);
        boolean typerSort = StoreSharePreferences.getInstance(
                getActivity()).loadBooleandSavedPreferences(
                Common.REF_KEY_TYPE_SORT);
        String textCusSearch = edtNameCustomer.getText().toString();
        String textSearch = edtNumInvoice.getText().toString();
        int typeInvoice = mTypeInvoice;
        String dateFrom = edtBillTimeFrom.getText().toString();
        String dateTo = edtBillTimeTo.getText().toString();
        if (textCusSearch == null || textCusSearch.equals("")) {
            actionSearchDataInvoice("", typeFilter, typerSort);
        } else {
            actionSearchDataInvoice(textCusSearch, typeFilter, typerSort);
        }
    }
    public void actionSearchDataInvoice(String textSearch,int typeFilter, boolean isAscending) {
        Log.e("actionFilterSort","typeFilter:"+typeFilter +"-"+"isAscending:"+isAscending);
        progressbarSearch.setVisibility(View.VISIBLE);
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_SEARCH_DATA_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putInt(Common.BUNDLE_KEY_FILTER, typeFilter);
        bundle.putBoolean(Common.BUNDLE_KEY_SORT, isAscending);
        bundle.putString(Common.BUNDLE_KEY_SEARCH, textSearch);
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }
    void validateDataSearchFull() {
        GeneralsUtils.forceHideKeyboard(getActivity());

        String textCusSearch = edtNameCustomer.getText().toString();
        String textSearch = edtNumInvoice.getText().toString();
        int typeInvoice = mTypeInvoice;
        String dateFrom = edtBillTimeFrom.getText().toString();
        String dateTo = edtBillTimeTo.getText().toString();
        if (textSearch == null || textSearch.equals("")) {
            actionSearch("", 0, dateFrom, dateTo);//search all
        } else {
            actionSearch(textCusSearch, typeInvoice, dateFrom, dateTo);
        }
    }
    public void actionSearch(String textSearch, int typeInvoice, String dateFrom, String dateTo) {
        progressbarSearch.setVisibility(View.VISIBLE);
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_SEARCH_FULL_DATA_INVOICE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putString(Common.BUNDLE_KEY_SEARCH, textSearch);
        bundle.putInt(Common.BUNDLE_KEY_FILTER, typeInvoice);
        bundle.putString(Common.BUNDLE_KEY_SEARCH_FROM, dateFrom);
        bundle.putString(Common.BUNDLE_KEY_SEARCH_TO, dateTo);
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        validateDataSearch();
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    public void showActivityDetailsInvoice(HoaDon item) {
        Intent itent = new Intent(getActivity(), DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, item);
        itent.putExtras(bundle);
        getActivity().startActivityForResult(itent, Common.REQUEST_CODE_ACTIVITY_DETAILS_INVOICE);
    }

    /**
     * xu ly su kien cua model
     *
     * @param modelEvent
     * @author: truonglt2
     * @return: BaseFragment
     * @throws:
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
        ActionEvent act = modelEvent.getActionEvent();
        switch (act.action) {
            case ActionEventConstant.ACTION_SEARCH_FULL_DATA_INVOICE: {
                adapter.clearAllData();
                adapter.notifyDataSetChanged();
                ArrayList<HoaDon> arrHoaDon = (ArrayList<HoaDon>) modelEvent
                        .getModelData();
                for (int i = 0; i < arrHoaDon.size(); i++) {
                    HoaDon hoadon = arrHoaDon.get(i);
                    adapter.addItem(hoadon);
                }
                listviewSearch.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                progressbarSearch.setVisibility(View.GONE);
                if (adapter.getCount() == 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                } else {
                    txtNoData.setVisibility(View.GONE);
                }
                break;
            }
            case ActionEventConstant.ACTION_SEARCH_DATA_INVOICE: {
                adapter.clearAllData();
                adapter.notifyDataSetChanged();
                ArrayList<HoaDon> arrHoaDon = (ArrayList<HoaDon>) modelEvent
                        .getModelData();
                for (int i = 0; i < arrHoaDon.size(); i++) {
                    HoaDon hoadon = arrHoaDon.get(i);
                    adapter.addItem(hoadon);
                }
                listviewSearch.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                progressbarSearch.setVisibility(View.GONE);
                if (adapter.getCount() == 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                } else {
                    txtNoData.setVisibility(View.GONE);
                }
                break;
            }

            default:
                break;
        }

    }


    /**
     * Mo ta chuc nang cua ham
     *
     * @param modelEvent
     * @author: apple
     * @return: BaseFragment
     * @throws:
     */
    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {
        ActionEvent act = modelEvent.getActionEvent();
        switch (act.action) {
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST: {
                ToastMessageUtil.showToastShort(getActivity(), "Không có dữ liệu!");
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }

            break;
            case ErrorConstants.ERROR_COMMON: {
                ToastMessageUtil.showToastShort(getActivity(), "Lỗi xảy ra trong quá trình xử lý!");
                progressbarSearch.setVisibility(View.GONE);
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
            break;
            default:
                ToastMessageUtil.showToastShort(getActivity(), "Lỗi xảy ra trong quá trình xử lý!");
                progressbarSearch.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    private class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        int mTypeSelected;

        public SelectDateFragment(int typeSelected) {
            this.mTypeSelected = typeSelected;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int month  = monthOfYear + 1;
            String strMonth = "";
            if(month<9)
            {
                strMonth = "0"+month;
            }
            else
            {
                strMonth = ""+ month;
            }
            if (mTypeSelected == 1) {
                edtBillTimeFrom.setText(new StringBuilder().append(strMonth)
                        .append("/").append(year));
            } else {
                edtBillTimeTo.setText(new StringBuilder().append(strMonth)
                        .append("/").append(year));
            }

        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }
    }
}
