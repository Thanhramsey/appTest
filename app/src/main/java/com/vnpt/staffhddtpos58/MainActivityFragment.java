package com.vnpt.staffhddtpos58;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.vnpt.adapters.RecyclerViewInvoiceAdapter;
import com.vnpt.asysn.AsysncTaskDataService;
import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.common.ErrorConstants;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.dto.InvoiceCadminBL;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.listener.OnLoadMoreListener;
import com.vnpt.staffhddtpos58.dialogs.SingleChoiceDialog;
import com.vnpt.staffhddtpos58.fragment.BaseFragment;
import com.vnpt.utils.DateTimeUtil;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.MyTimeUtils;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.ToastMessageUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.vnpt.utils.Helper.hideSoftKeyboard;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment implements View.OnClickListener, OnEventControlListener {
    public static String TAG = MainActivityFragment.class.getName();
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerViewInvoiceAdapter recyclerViewInvoiceAdapter;
    //    GridViewCustomAdapter adapter;
//    PullAndLoadListView listviewSearch;
//    ListInvoiceAdapter lsAdapter;
    Button btnFilter, btnSort;
    ProgressBar progressbarSearch;
    LinearLayout noDataLayout;
    TextView txtSumItem;
    //    GridView gridTable;
    Button spnTuNgay, spnDenNgay;
    Button btnPattern, btnSerial;

    ImageButton btnSeachInvoice;
    EditText edtSearchCheckNoTableNo;
    private AwesomeProgressDialog dg;
    public int statusPayment;
    public ArrayList<InvoiceCadmin> mArrTable;// dùng để searching
    public String mPattern;
    public String mSerial;
    public String mFromDate;
    public String mToDate;
    //    public final int ITEM = 40;
//    public int mPageIndex = 0;
//    public int mPageSize = Common.PAGE_SIZE;
    int totalItem;
    public int mOrgId = 0;
    boolean isLoadMore = false;
    RecyclerView recyclerView;
//    RecyclerViewInvoiceAdapter recyclerViewAdapter;
//    ArrayList<String> rowsArrayList = new ArrayList<>();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        if (isAdded()) {
            Bundle args = getArguments();
            if (args != null) {
                statusPayment = args.getInt(Common.BUNDLE_ITEM_STATUS_PAYMENTED_INVOICE, Common.STATUS_NOT_PAYMENT);
            }
        }
        setupUI(layout.findViewById(R.id.layout_frament_main));
        init(layout);
        initValueDefaults();
        setValueForMembers();
        setEventForMembers();
        return layout;
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    void initValueDefaults() {
        mPattern = "" + StoreSharePreferences.getInstance(
                getActivity()).loadStringSavedPreferences(
                Common.KEY_DEFAULT_PATTERN_INVOICES);
        mSerial = "" + StoreSharePreferences.getInstance(
                getActivity()).loadStringSavedPreferences(
                Common.KEY_DEFAULT_SERIAL_INVOICES);
//        mFromDate = DateTimeUtil.getCurrentDate();
//        mToDate = DateTimeUtil.getCurrentDate();
//        btnPattern.setText(""+mPattern);
//        btnSerial.setText(""+mSerial);
//        loadDataInvoice();
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
        txtSumItem = (TextView) layout.findViewById(R.id.txtSumItem);
        btnFilter = (Button) layout.findViewById(R.id.btnFilter);
        btnSort = (Button) layout.findViewById(R.id.btnSort);
//        listviewSearch = (PullAndLoadListView) layout.findViewById(R.id.lvData);
        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swiperefresh);
//        gridTable = (GridView) layout.findViewById(R.id.gridTable);
        noDataLayout = (LinearLayout) layout.findViewById(R.id.noDataLayout);
        progressbarSearch = (ProgressBar) layout.findViewById(R.id.progressbarSearch);
        spnTuNgay = (Button) layout.findViewById(R.id.spnTuNgay);
        spnDenNgay = (Button) layout.findViewById(R.id.spnDenNgay);
        btnPattern = (Button) layout.findViewById(R.id.btnPattern);
        btnSerial = (Button) layout.findViewById(R.id.btnSerial);
        btnSeachInvoice = (ImageButton) layout.findViewById(R.id.btnSeachInvoice);
        edtSearchCheckNoTableNo = (EditText) layout.findViewById(R.id.edtSearchCheckNoTableNo);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
    }

    void checkTimeToAsysncTaskData() {
        // Gọi lịch đồng bộ dữ liệu lúc 12h trưa.
        // check time 12h
        String dateSync = StoreSharePreferences.getInstance(getActivity()).loadStringSavedPreferences(Common.REF_KEY_DATE_SYNC);
        if (dateSync == null || dateSync.equals("")) { // Chưa đồng bộ lần nào
            asysncTaskData();
            return;
        }
        String dateCurr = DateTimeUtil.getCurrentDate();
        int vlCompare = DateTimeUtil.compareDate(dateSync, dateCurr);
        if (vlCompare == ConstantsApp.DATE_BEFOR) {
            // thưc hiện đồng bộ
            asysncTaskData();
        } else {
            // lấy dữ liệu từ sharePreference local để hiển thị.
            loadDataInvoice();
        }
    }

    void asysncTaskData() {
        AsysncTaskDataService asysncTaskDataService = new AsysncTaskDataService(getActivity(), this);
        asysncTaskDataService.execute();
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
        // TODO Auto-generated method stub
        SimpleDateFormat fmt = new SimpleDateFormat(MyTimeUtils.DATE_FORMAT, Locale.getDefault());
        spnTuNgay.setText(fmt.format(Calendar.getInstance().getTime()));
        spnDenNgay.setText(fmt.format(Calendar.getInstance().getTime()));
        mArrTable = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewInvoiceAdapter = new RecyclerViewInvoiceAdapter(recyclerView, mArrTable, getActivity());
        recyclerView.setAdapter(recyclerViewInvoiceAdapter);
        //set load more listener for the RecyclerView adapter
        recyclerViewInvoiceAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                String txtSearch = edtSearchCheckNoTableNo.getText().toString();
                //Khi list ở trạng thái ko search mới cho load more
                if(txtSearch.isEmpty()||txtSearch.equals(""))
                {

                    if (mArrTable.size() <= totalItem) {
                        mArrTable.add(null);
                        recyclerViewInvoiceAdapter.notifyItemInserted(mArrTable.size() - 1);
                        Common.currentPageInvoice += 1;
                        //Generating more data
                        loadmoreDataInvoice();
                    } else {
                        ToastMessageUtil.showToastShort(getActivity(), "Loading data completed");
//                    Toast.makeText(MainActivity.this, "Loading data completed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
//        lsAdapter = new ListInvoiceAdapter(getActivity());
//        listviewSearch.setAdapter(lsAdapter);

        btnFilter.setOnClickListener(this);
        btnSort.setOnClickListener(this);

        spnTuNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.showDatePickerDialog(getContext(), spnTuNgay, Calendar.getInstance(), "Chọn ngày");
            }
        });
        spnDenNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.showDatePickerDialog(getContext(), spnDenNgay, Calendar.getInstance(), "Chọn ngày");
            }
        });
        btnSeachInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                swipeRefreshLayout.setRefreshing(true);
                String strSearch = edtSearchCheckNoTableNo.toString();
                recyclerViewInvoiceAdapter.getFilter().filter(strSearch);
            }
        });
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */

        txtSumItem.setText("" + 0);
        edtSearchCheckNoTableNo.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                // Tìm kiếm theo Tên, cuscode và số hoa đơn
                String strSearch = s.toString();
                recyclerViewInvoiceAdapter.getFilter().filter(strSearch);
                txtSumItem.setText("" + recyclerViewInvoiceAdapter.getItemCount());
                if (recyclerViewInvoiceAdapter.getItemCount() == 0) {
                    noDataLayout.setVisibility(View.VISIBLE);
                } else {
                    noDataLayout.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                txtSumItem.setText("" + recyclerViewInvoiceAdapter.getItemCount());
            }
        });
        edtSearchCheckNoTableNo.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Identifier of the action. This will be either the identifier you supplied,
                        // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                            noDataLayout.setVisibility(View.GONE);
//                            loadDataInvoice();
                            recyclerViewInvoiceAdapter.getFilter().filter(edtSearchCheckNoTableNo.getText().toString());
                            txtSumItem.setText("" + recyclerViewInvoiceAdapter.getItemCount());
                            if (recyclerViewInvoiceAdapter.getItemCount() == 0) {
                                noDataLayout.setVisibility(View.VISIBLE);
                            } else {
                                noDataLayout.setVisibility(View.GONE);
                            }
                            return true;
                        }
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });
        checkTimeToAsysncTaskData();
    }

    void loadmoreDataInvoice() {
        if (checkValidateDateTime() == false) {
            DialogUtils.showErrorDialog(getContext(), getString(R.string.message), getString(R.string.txt_validate_time));
//            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        callServerLoadMore();
    }

    void callServerLoadMore() {
        // lấy các hóa đơn của tháng hiện tại.
//            mFromDate = "07/05/2019";
//            mToDate = "15/06/2019";
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        mFromDate = StoreSharePreferences.getInstance(
                getActivity()).loadStringSavedPreferences(
                Common.REF_KEY_DATE_SYNC_FROM);
        mToDate = StoreSharePreferences.getInstance(
                getActivity()).loadStringSavedPreferences(
                Common.REF_KEY_DATE_SYNC_TO);
        int changeDate = StoreSharePreferences.getInstance(
                getActivity()).loadIntegerSavedPreferences(
                Common.REF_KEY_CHANGE_DATE);
        if (changeDate == 1) {
            mFromDate = StoreSharePreferences.getInstance(
                    getActivity()).loadStringSavedPreferences(
                    Common.REF_KEY_DATE_SYNC_FROM);
            mToDate = StoreSharePreferences.getInstance(
                    getActivity()).loadStringSavedPreferences(
                    Common.REF_KEY_DATE_SYNC_TO);
            //StoreSharePreferences.getInstance(getActivity()).saveIntPreferences(Common.REF_KEY_CHANGE_DATE, 0);
        }
        Log.d("mFromDate", "mFromDate:" + mFromDate);
        Log.d("mToDate", "mToDate:" + mToDate);
        mOrgId = 0;
//            mPageIndex = 1;
        // lay du lieu tren database
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST_LOAD_MORE;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putString(Common.BUNDLE_KEY_PATTERN, mPattern);
        bundle.putString(Common.BUNDLE_KEY_SERIAL, mSerial);
        bundle.putInt(Common.BUNDLE_KEY_ORG_ID, mOrgId);
        bundle.putString(Common.BUNDLE_KEY_FROMDATE, mFromDate);
        bundle.putString(Common.BUNDLE_KEY_TODATE, mToDate);
        bundle.putInt(Common.BUNDLE_KEY_PAGEINDEX, Common.currentPageInvoice);
        bundle.putInt(Common.BUNDLE_KEY_PAGESIZE, Common.PAGE_SIZE);
        bundle.putBoolean(Common.BUNDLE_KEY_LOAD_FROM_SERVER, true);
        Log.e(TAG + "Tham so dau vao:", bundle.toString());
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }

    void loadDataInvoice() {
        if (checkValidateDateTime() == false) {
            DialogUtils.showErrorDialog(getContext(), getString(R.string.message), getString(R.string.txt_validate_time));
//            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        noDataLayout.setVisibility(View.GONE);
        dg = DialogUtils.showLoadingDialog(getString(R.string.text_loading_data_invoice), getActivity(), null);
        actionShowListTable();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnFilter: {
                showDialogFilter();
                break;
            }
            case R.id.btnSort: {
                showDialogSort();
                break;
            }
            default:
                break;
        }
    }

    //Kiem tra thoi gian
    boolean checkValidateDateTime() {
        String tuNgayGio = spnTuNgay.getText().toString().trim();
        String denNgayGio = spnDenNgay.getText().toString().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date date1 = sdf.parse(tuNgayGio);
            Date date2 = sdf.parse(denNgayGio);
            return !date2.before(date1);
        } catch (ParseException e) {
            // the format of the read dates is not the expected one
        }
        return true;

    }

    void showDialogFilter() {
        //1 - "Theo tên tên bàn",
        //2 - "Theo số checkno",
        //3 - "Theo TT chưa thanh toán",

        final CharSequence[] arrFilter = {"Theo tên tên bàn", "Theo số CheckNo"};
        int typeFilter = StoreSharePreferences.getInstance(
                getActivity()).loadIntegerSavedPreferences(
                Common.REF_KEY_TYPE_FILTER);
        if (typeFilter <= 0) {
            typeFilter = 0;
        } else {
            typeFilter -= 1;
        }
        final SingleChoiceDialog sd = SingleChoiceDialog.newInstance(arrFilter, typeFilter, "Lọc theo", Common.DIALOG_SINGLE_FILTER_INVOICE);
        // mActivity is just a reference to the activity attached to SingleChoiceDialog
        sd.setCancelable(false);
        sd.show(this.getActivity().getSupportFragmentManager(), SingleChoiceDialog.TAG);

    }

    void showDialogSort() {
        final CharSequence[] arrSort = {"Giảm dần", "Tăng dần"};
        boolean typeSort = StoreSharePreferences.getInstance(
                getActivity()).loadBooleandSavedPreferences(
                Common.REF_KEY_TYPE_SORT);
        int position = typeSort ? 1 : 0;
        final SingleChoiceDialog sd = SingleChoiceDialog.newInstance(arrSort, position, "Sắp xếp", Common.DIALOG_SINGLE_SORT_INVOICE);
        // mActivity is just a reference to the activity attached to SingleChoiceDialog
        sd.setCancelable(false);
        sd.show(this.getActivity().getSupportFragmentManager(), SingleChoiceDialog.TAG);
    }

    public void actionSort(boolean isAscending) {
        if (recyclerViewInvoiceAdapter == null || recyclerViewInvoiceAdapter.getItemCount() == 0) {
            return;
        }
        int typeFilter = 0;
        typeFilter = Common.FILTER_BY_TABLE_NO;
        Log.e("actionFilterSort", "typeFilter:" + typeFilter + "-" + "isAscending:" + isAscending);
        progressbarSearch.setVisibility(View.VISIBLE);
        recyclerViewInvoiceAdapter.sortBy(isAscending, typeFilter);
        progressbarSearch.setVisibility(View.GONE);

    }

    public void actionShowListTable() {
        ((MainActivity) getActivity()).showProccessbar(true);
        // lấy các hóa đơn của tháng hiện tại.
//            mFromDate = "07/05/2019";
//            mToDate = "15/06/2019";
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        mFromDate = StoreSharePreferences.getInstance(
                getActivity()).loadStringSavedPreferences(
                Common.REF_KEY_DATE_SYNC_FROM);
        mToDate = StoreSharePreferences.getInstance(
                getActivity()).loadStringSavedPreferences(
                Common.REF_KEY_DATE_SYNC_TO);
        int changeDate = StoreSharePreferences.getInstance(
                getActivity()).loadIntegerSavedPreferences(
                Common.REF_KEY_CHANGE_DATE);
        if (changeDate == 1) {
            mFromDate = StoreSharePreferences.getInstance(
                    getActivity()).loadStringSavedPreferences(
                    Common.REF_KEY_DATE_SYNC_FROM);
            mToDate = StoreSharePreferences.getInstance(
                    getActivity()).loadStringSavedPreferences(
                    Common.REF_KEY_DATE_SYNC_TO);
            //StoreSharePreferences.getInstance(getActivity()).saveIntPreferences(Common.REF_KEY_CHANGE_DATE, 0);
        }
        Log.d("mFromDate", "mFromDate:" + mFromDate);
        Log.d("mToDate", "mToDate:" + mToDate);
        mOrgId = 0;
//            mPageIndex = 1;
        // lay du lieu tren database
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST;
        e.sender = this;
        Bundle bundle = new Bundle();
        bundle.putString(Common.BUNDLE_KEY_PATTERN, mPattern);
        bundle.putString(Common.BUNDLE_KEY_SERIAL, mSerial);
        bundle.putInt(Common.BUNDLE_KEY_ORG_ID, mOrgId);
        bundle.putString(Common.BUNDLE_KEY_FROMDATE, mFromDate);
        bundle.putString(Common.BUNDLE_KEY_TODATE, mToDate);
        bundle.putInt(Common.BUNDLE_KEY_PAGEINDEX, Common.currentPageInvoice);
        bundle.putInt(Common.BUNDLE_KEY_PAGESIZE, Common.PAGE_SIZE);
        bundle.putBoolean(Common.BUNDLE_KEY_LOAD_FROM_SERVER, true);
        Log.e(TAG + "Tham so dau vao:", bundle.toString());
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
    }


    /**
     * This method is called when swipe refresh is pulled down
     */
//    @Override
//    public void onRefresh() {
//        getDataShowList();
//    }


    /**
     * set su kien cho cac thanh phan
     *
     * @author: truonglt2
     * @return: MainActivityFragment
     * @throws:
     */
    @Override
    protected void setEventForMembers() {
//         TODO Auto-generated method stub
//        listviewSearch.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                isLoadMore = false;
//                listviewSearch.setmIsLoadingMore(false);
//                Common.currentPageInvoice = 0;
//                loadDataInvoice();
////                actionShowListTable("",Common.currentPageInvoice);
//            }
//        });
//        listviewSearch.setOnLoadMoreListener(new PullAndLoadListView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                int size = lsAdapter.getCount();
//                if(totalItem==lsAdapter.getCount())
//                {
//                    listviewSearch.onLoadMoreComplete();
//                    return;
//                }
//                isLoadMore = true;
//                Common.currentPageInvoice += 1;
////                mPageIndex += 1;
////                actionShowListTable("",Common.currentPageInvoice);
//                loadmoreDataInvoice();
////                if (totalItem > lsAdapter.getCount()) {
////                    listviewSearch.setmIsLoadingMore(false);
////                }
//            }
//        });
//        // TODO Auto-generated method stub
//        listviewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // do làm với pull to refresh nên position bắt đầu = 1 chứ ko phải = 0
//                InvoiceCadmin item = lsAdapter.getItem(position - 1);
//                showActivityDetailsInvoice(item);
//                GeneralsUtils.forceHideKeyboard(getActivity());
//            }
//        });
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
//        swipeRefreshLayout.post(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        noDataLayout.setVisibility(View.GONE);
//                        swipeRefreshLayout.setRefreshing(true);
//                        loadDataInvoice();
//                    }
//                });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                noDataLayout.setVisibility(View.GONE);
                Common.currentPageInvoice = 0;
                loadDataInvoice();
            }
        });
    }

    public void showActivityDetailsInvoice(InvoiceCadmin item) {
        Intent itent = new Intent(getActivity(), DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, item);
        itent.putExtras(bundle);
        if (item.getStatus() == 0) {
            getActivity().startActivityForResult(itent, Common.REQUEST_CODE_ACTIVITY_DETAILS_INVOICE);
        } else {
            getActivity().startActivityForResult(itent, Common.REQUEST_CODE_ACTIVITY_DETAILS_FOR_RETURN_INVOICE);
        }
    }

    void startActivityContentInvoice(InvoiceCadmin item) {
        Intent itent = new Intent(getActivity(), ContentInvoiceActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(Common.KEY_DATA_ITEM_INVOICE, item);
        args.putInt(Common.BUNDLE_ITEM_STATUS_PAYMENTED_INVOICE, statusPayment);
        itent.putExtras(args);
        startActivity(itent);
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
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST: {
                InvoiceCadminBL invoiceCadminBL = (InvoiceCadminBL) modelEvent.getModelData();
                if (isLoadMore == false) {
                    recyclerViewInvoiceAdapter.clearAllData();
//                    mArrTable.clear();
                }
                if (invoiceCadminBL != null && invoiceCadminBL.getArrData().size() > 0) {
                    totalItem = invoiceCadminBL.totalRecords;
                    recyclerViewInvoiceAdapter.addAllData(invoiceCadminBL.getArrData());
//                    mArrTable.addAll(invoiceCadminBL.getArrData());
                    actionSort(false);
                }
                Log.d("totalItem",""+totalItem);
                recyclerView.setAdapter(recyclerViewInvoiceAdapter);
                if (recyclerViewInvoiceAdapter.getItemCount() == 0) {
                    noDataLayout.setVisibility(View.VISIBLE);
//                    recyclerView.onRefreshComplete();
                } else {
                    noDataLayout.setVisibility(View.GONE);
                }
                recyclerViewInvoiceAdapter.notifyDataSetChanged();
                recyclerViewInvoiceAdapter.setLoaded();
                txtSumItem.setText("" + recyclerViewInvoiceAdapter.getItemCount());
                swipeRefreshLayout.setRefreshing(false);
//                lsAdapter.notifyDataSetChanged();

//                if (lsAdapter.getCount() < Common.PAGE_SIZE) {
//                    listviewSearch.onLoadMoreComplete();
//                }
//                MainActivity mainActivity = (MainActivity) getActivity();
//                mainActivity.setTitleHome();
//                listviewSearch.onRefreshComplete();

                break;
            }
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST_LOAD_MORE: {
                InvoiceCadminBL invoiceCadminBL = (InvoiceCadminBL) modelEvent.getModelData();
                if (invoiceCadminBL != null && invoiceCadminBL.getArrData().size() > 0) {
                    recyclerViewInvoiceAdapter.removeLastItem();// de remove cai load more
                    totalItem = invoiceCadminBL.totalRecords;
                    recyclerViewInvoiceAdapter.addAllData(invoiceCadminBL.getArrData());
//                    mArrTable.addAll(invoiceCadminBL.getArrData());
//                    actionSort(false);
                }
                recyclerView.setAdapter(recyclerViewInvoiceAdapter);
                if (recyclerViewInvoiceAdapter.getItemCount() == 0) {
                    noDataLayout.setVisibility(View.VISIBLE);
                } else {
                    noDataLayout.setVisibility(View.GONE);
                }
                txtSumItem.setText("" + recyclerViewInvoiceAdapter.getItemCount());
                recyclerViewInvoiceAdapter.notifyDataSetChanged();
                recyclerViewInvoiceAdapter.setLoaded();
                swipeRefreshLayout.setRefreshing(false);

//                MainActivity mainActivity = (MainActivity) getActivity();
//                mainActivity.setTitleHome();

                break;
            }

            default:

                break;
        }

        ((MainActivity) getActivity()).showProccessbar(false);
        if (dg != null) {
            //if (dg.isShowing())
            dg.hide();
        }
    }
    /**
     * Hides the soft keyboard
     */
//    public void hideSoftKeyboard() {
//        if(getCurrentFocus()!=null) {
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//    }


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
        ((MainActivity) getActivity()).showProccessbar(false);
        switch (act.action) {
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST: {
                ToastMessageUtil.showToastShort(getActivity(), getString(R.string.text_no_data));
                // stopping swipe refresh
//                swipeRefreshLayout.setRefreshing(false);
                txtSumItem.setText("" + recyclerViewInvoiceAdapter.getItemCount());
//                getDataDemo();
                break;
            }
            case ErrorConstants.ERROR_COMMON: {
                ToastMessageUtil.showToastShort(getActivity(), getString(R.string.text_process_err_sys));
//                progressbarSearch.setVisibility(View.GONE);
                // stopping swipe refresh
//                swipeRefreshLayout.setRefreshing(false);
                txtSumItem.setText("" + recyclerViewInvoiceAdapter.getItemCount());
                break;
            }

            default:
                ToastMessageUtil.showToastShort(getActivity(), getString(R.string.text_process_err_sys));
//                progressbarSearch.setVisibility(View.GONE);
//                swipeRefreshLayout.setRefreshing(false);
                txtSumItem.setText("" + recyclerViewInvoiceAdapter.getItemCount());
                break;
        }
        progressbarSearch.setVisibility(View.GONE);
        if (recyclerViewInvoiceAdapter.getItemCount() == 0) {
            noDataLayout.setVisibility(View.VISIBLE);
        } else {
            noDataLayout.setVisibility(View.GONE);
        }
        if (dg != null) {
            //if (dg.isShowing())
            dg.hide();
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        ((MainActivity) getActivity()).showProccessbar(false);
        switch (eventType) {
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_SYNC_SUCESSS: {
                //load page đầu tiên sau khi sys
                InvoiceCadminBL invoiceCadminBL = (InvoiceCadminBL) data;
                if (isLoadMore == false) {
                    recyclerViewInvoiceAdapter.clearAllData();
//                    mArrTable.clear();
                }
                Common.currentPageInvoice = 0;
                if (invoiceCadminBL != null && invoiceCadminBL.getArrData().size() > 0) {
                    totalItem = invoiceCadminBL.totalRecords;
                    recyclerViewInvoiceAdapter.addAllData(invoiceCadminBL.getArrData());
//                    mArrTable.addAll(invoiceCadminBL.getArrData());
//                    Common.currentPageInvoice +=1;
                    actionSort(false);
                }
                Log.d("totalItem",""+totalItem);
                recyclerView.setAdapter(recyclerViewInvoiceAdapter);
                if (recyclerViewInvoiceAdapter.getItemCount() == 0) {
                    noDataLayout.setVisibility(View.VISIBLE);
                } else {
                    noDataLayout.setVisibility(View.GONE);
                }
                txtSumItem.setText("" + recyclerViewInvoiceAdapter.getItemCount());
                swipeRefreshLayout.setRefreshing(false);
                if (dg != null) {
                    //if (dg.isShowing())
                    dg.hide();
                }
                progressbarSearch.setVisibility(View.GONE);
                recyclerViewInvoiceAdapter.notifyDataSetChanged();
                recyclerViewInvoiceAdapter.setLoaded();
//                if (lsAdapter.getCount() < Common.PAGE_SIZE) {
//                    listviewSearch.onRefreshComplete();
//                    listviewSearch.onLoadMoreComplete();
//                }


                break;
            }
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_SYNC_FAILED: {
                DialogUtils.showInfoDialog(getActivity(), "Thông báo", "Không thể đồng bộ dữ liệu hệ thống, vui lòng kiểm tra lại!", false, new Closure() {
                    @Override
                    public void exec() {
                        // click
                    }
                });
                if (recyclerViewInvoiceAdapter.getItemCount() == 0) {
                    noDataLayout.setVisibility(View.VISIBLE);
                } else {
                    noDataLayout.setVisibility(View.GONE);
                }
                txtSumItem.setText("" + recyclerViewInvoiceAdapter.getItemCount());
                if (dg != null) {
                    //if (dg.isShowing())
                    dg.hide();
                }
                progressbarSearch.setVisibility(View.GONE);
                loadDataInvoice();
                break;
            }
            default:
                ((MainActivity) getActivity()).showProccessbar(false);
                break;
        }
    }
}
