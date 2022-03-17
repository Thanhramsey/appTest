package com.vnpt.staffhddtpos58;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.dto.HoaDon;
import com.vnpt.utils.ToastMessageUtil;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    // kiem tra activity da finish hay chua
    public boolean isFinished = false;
    private GoogleMap mMap;
    HoaDon mHoadon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null)
//            mHoadon = (HoaDon) bundle.getSerializable(Common.KEY_DATA_ITEM_INVOICE);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        getDataShowMap();
    }
    protected void getDataShowMap() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST;
        e.sender = this;
        Bundle bundle = new Bundle();
        e.viewData = bundle;
        UserController.getInstance().handleViewEvent(e);
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
    public void handleModelViewEvent(ModelEvent modelEvent) {
        ActionEvent act = modelEvent.getActionEvent();
        switch (act.action) {
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST: {
//                mMap.clear();
                ArrayList<HoaDon> arrHoaDon = (ArrayList<HoaDon>) modelEvent
                        .getModelData();
                LatLng mylocation = null ;
                for (int i = 0; i < arrHoaDon.size(); i++) {
                    HoaDon hoaDon = arrHoaDon.get(i);
                    LatLng latLng = new LatLng(hoaDon.getLongitude(),hoaDon.getLatitude());
//                  getAddressFromLatLng(latLng)
                    MarkerOptions options = new MarkerOptions().position(latLng);
                    options.title(hoaDon.getNumberInvoice()+"/n"+hoaDon.getAddress());
                    options.icon(BitmapDescriptorFactory.defaultMarker());
                    mMap.addMarker(options);
                    mylocation = new LatLng(hoaDon.getLatitude(), hoaDon.getLongitude());
                    Log.e("Add map","vi tri "+i);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
                // stopping swipe refresh
            }
            break;
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
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {
        ActionEvent act = modelEvent.getActionEvent();
        switch (act.action) {
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST: {
                ToastMessageUtil.showToastShort(MapsActivity.this, "Không có dữ liệu!");
                // stopping swipe refresh
            }

            break;
            default:
                break;
        }
    }
    /**
     * ket thuc activity
     *
     * @author: truonglt2
     * @return: BaseActivity
     * @throws:
     */
    @Override
    public void finish() {
        isFinished = true;
        super.finish();
    }

    /**
     * finish activity
     *
     * @param requestCode
     * @author: truonglt2
     * @return: BaseActivity
     * @throws:
     */
    @Override
    public void finishActivity(int requestCode) {
        isFinished = true;
        super.finishActivity(requestCode);
    }
}
