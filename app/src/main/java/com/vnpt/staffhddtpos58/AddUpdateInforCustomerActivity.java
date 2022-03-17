package com.vnpt.staffhddtpos58;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vnpt.common.Common;
import com.vnpt.dto.InvoiceCadmin;

public class AddUpdateInforCustomerActivity extends BaseActivity {


    Menu mMenu;
    InvoiceCadmin mHoadon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_infor_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tạo biên lai");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mHoadon = (InvoiceCadmin) bundle.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
        showFragAddCustomer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_singnature_manual, menu);
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
            case R.id.action_save:
                // app icon in action bar clicked; go home
                AddUpdateInforCustomerFragment fragment = (AddUpdateInforCustomerFragment) getSupportFragmentManager().findFragmentByTag(AddUpdateInforCustomerFragment.TAG);
                fragment.saveDataDetailInvoice();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    void showFragAddCustomer() {
        AddUpdateInforCustomerFragment fragment = new AddUpdateInforCustomerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.KEY_DATA_ITEM_INVOICE, mHoadon);
        fragment.setArguments(bundle);
        setFragmentContent(fragment, AddUpdateInforCustomerFragment.TAG, R.id.root_layout);
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
