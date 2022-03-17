package com.vnpt.staffhddtpos58;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vnpt.common.Common;
import com.vnpt.dto.Invoice;

public class PreviousInvoiceActivity extends BaseActivity {
    boolean isShowChart = false;
    Menu mMenu;
    Invoice mHoadon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mHoadon = (Invoice) bundle.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
        showList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_previous_invoice, menu);
        this.mMenu = menu;
//        MenuItem menuItem = mMenu.getItem(0);
//        menuItem.setIcon(getResources().getDrawable(R.drawable.statistics));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            case R.id.action_chart:
                // search action
                showContentInvoicePrevious(isShowChart);
                isShowChart = !isShowChart;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void showContentInvoicePrevious(boolean isShowChart) {

        MenuItem menuItem = mMenu.getItem(0);
        if (isShowChart) {
            menuItem.setIcon(getResources().getDrawable(R.drawable.statistics));
            showList();
        } else {
            menuItem.setIcon(getResources().getDrawable(R.drawable.tasks));
            showChart();
        }

    }

    void showList() {
        PreviousInvoiceActivityFragment fragment = new PreviousInvoiceActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoadon);
        fragment.setArguments(bundle);
        setFragmentContent(fragment, PreviousInvoiceActivityFragment.TAG, R.id.root_layout_previous_invoice);
    }

    void showChart() {
        Bundle args = new Bundle();
//        fragment.setArguments(args);
        setFragmentContent(PreviousChartInvoiceFragment.newInstance("", ""), PreviousChartInvoiceFragment.TAG, R.id.root_layout_previous_invoice);
    }
    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments >= 1) {
            finish();
            return;
        }
        super.onBackPressed();
    }
}
