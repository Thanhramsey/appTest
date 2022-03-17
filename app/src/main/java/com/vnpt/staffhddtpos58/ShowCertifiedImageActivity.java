package com.vnpt.staffhddtpos58;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vnpt.adapters.ViewImageCertifiedAdapter;
import com.vnpt.common.Common;
import com.vnpt.dto.HoaDon;
import com.vnpt.dto.ImageCertified;

import java.util.ArrayList;

import uk.co.senab.photoview.sample.HackyViewPager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ShowCertifiedImageActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    ViewImageCertifiedAdapter adapter;
    static int typeView;
    HoaDon mHoaDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_certified_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);

        Bundle bundle = getIntent().getExtras();
        typeView = bundle.getInt(Common.KEY_TYPE_VIEW_CERFITICATE);
        mHoaDon = (HoaDon) bundle.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
        ArrayList<ImageCertified> arr = new ArrayList<>();
        ImageCertified item = new ImageCertified();
        if (typeView == Common.TYPE_VIEW_CERFITICATE_MANUAL) {
//            String pathImage = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_PATH_IMAGE_SING_MANUAL);
            item.setPathLocal(mHoaDon.getPathCertified());
            item.setmHoaDon(mHoaDon);
            arr.add(item);
        } else if (typeView == Common.TYPE_VIEW_CERFITICATE_CAMERA) {
//            String pathImage = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_PATH_IMAGE_SING_CAMERA);
            item.setmHoaDon(mHoaDon);
            item.setPathLocal(mHoaDon.getPathCertified());
            arr.add(item);
        }
        adapter = new ViewImageCertifiedAdapter();
        adapter.addItem(item);
        adapter.notifyDataSetChanged();
        mViewPager.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

}
