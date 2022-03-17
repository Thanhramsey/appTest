package com.vnpt.staffhddtpos58;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vnpt.common.Common;
import com.vnpt.common.ModelEvent;
import com.vnpt.dto.BienLai;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.fragment.BaseFragment;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.webservice.AppServices;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.concurrent.TimeUnit;

import static com.vnpt.utils.Helper.hideSoftKeyboard;

public class XuatBienLaiFragment2 extends BaseFragment implements View.OnClickListener, OnEventControlListener {

    private static final int REQUEST_BLUE_ADMIN = 888;
    MaterialBetterSpinner spMenhGia;
    MaterialEditText edtSoLuong;
    Button btnXuatBL, btnInThu, btnCheckTB;
    private AwesomeProgressDialog dg;
    StoreSharePreferences preferences = null;

    private GetInvTask getInvTask = null;

    public static String TAG = XuatBienLaiFragment.class.getName();


    private static final String[] PRICES = new String[]{
            "10.000"
    };

    public XuatBienLaiFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_xuat_bien_lai, container, false);

        setupUI(layout.findViewById(R.id.layout_frament_xuatbienlai));
        init(layout);
        setEventForMembers();
        ((MainActivity2) getActivity()).showProccessbar(false);

        preferences = StoreSharePreferences.getInstance(getContext());

        return layout;
    }

    @Override
    protected void init(View layout) {
        spMenhGia = layout.findViewById(R.id.spMenhGia);
        edtSoLuong = layout.findViewById(R.id.edtSoLuong);
        btnXuatBL = layout.findViewById(R.id.btnXuatBienLai);
        btnInThu = layout.findViewById(R.id.btnInThu);
        btnCheckTB = layout.findViewById(R.id.btnCheck);
    }

    @Override
    protected void setValueForMembers() {

    }

    @Override
    protected void setEventForMembers() {
        btnXuatBL.setOnClickListener(this);
        btnInThu.setOnClickListener(this);
        btnCheckTB.setOnClickListener(this);
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, PRICES);
        spMenhGia.setAdapter(adapter);
    }

    private void attemptGetInv() {
        if (getInvTask != null) {
            return;
        }

        String num = edtSoLuong.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(num)) {
            edtSoLuong.setError(getString(R.string.error_empty_input));
            focusView = edtSoLuong;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            int numOfInv = Integer.parseInt(num);
            StringBuilder sb = new StringBuilder();

            //lap lai so ve
            for (int i = 1; i <= numOfInv; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    long currentTime = System.currentTimeMillis();
                    String keyData = i + "_INVKBL" + currentTime;
                    String xmlChildData = "<Inv><key>" + keyData + "</key><Invoice><CusCode><![CDATA[" + keyData + "]]></CusCode><CusName><![CDATA[" + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME) + "]]></CusName><CusAddress><![CDATA[]]></CusAddress><CusPhone></CusPhone><CusTaxCode></CusTaxCode><PaymentMethod><![CDATA[]]></PaymentMethod><Products><Product><Code><![CDATA[]]></Code><ProdName><![CDATA[Phí tham quan]]></ProdName><ProdUnit>Lần</ProdUnit><ProdQuantity>1</ProdQuantity><ProdPrice>10000</ProdPrice><Amount>10000</Amount></Product></Products><KindOfService><![CDATA[]]></KindOfService><Total>10000</Total><Amount>1</Amount><AmountInWords><![CDATA[Mười ngàn đồng]]></AmountInWords></Invoice></Inv>";
                    sb.append(xmlChildData);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String xmlData = "<Invoices>" + sb.toString() + "</Invoices>";

            getInvTask = new GetInvTask(
                    StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME),
                    StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_PASS),
                    preferences.loadStringSavedPreferences(Common.KEY_DEFAULT_PATTERN_INVOICES),
                    preferences.loadStringSavedPreferences(Common.KEY_DEFAULT_SERIAL_INVOICES),
                    0,
                    xmlData);
            getInvTask.execute((Void) null);
        }
    }

    private void setupUI(View view) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnXuatBienLai: {
                attemptGetInv();
                break;
            }
            case R.id.btnInThu: {
                if (MainActivity2.mPOSPrinter.isBTActivated()) {
                    printInvoiceTest();
                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth và kết nối máy in", Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case R.id.btnCheck: {
                checkPrinter();
                break;
            }
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        ((MainActivity2) getActivity()).showProccessbar(false);
    }

    //gui request len webservices de lay inv data
    public class GetInvTask extends AsyncTask<Void, Void, String> {

        //String username, String password, String strPattern, String strSerial, int convert, String strXmlInvData
        private String userName;
        private String password;
        private String strPattern;
        private String strSerial;
        private int convert;
        private String strXmlInvData;

        public GetInvTask(String userName, String password, String strPattern,
                          String strSerial, int convert, String strXmlInvData) {
            this.userName = userName;
            this.password = password;
            this.strPattern = strPattern;
            this.strSerial = strSerial;
            this.convert = convert;
            this.strXmlInvData = strXmlInvData;
        }

        @Override
        protected String doInBackground(Void... voids) {
            AppServices appServices = new AppServices(getActivity());
            String result = appServices.importAndPublishInv(userName, password, strPattern, strSerial, convert, strXmlInvData);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);

            String[] listResults = s.substring(s.lastIndexOf("-") + 1).split(",");

            for (String inv : listResults) {
                printInvoice(inv);
            }

            getInvTask = null;
            showProgress(false);
            if (dg != null) {
                //if (dg.isShowing())
                dg.hide();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            getInvTask = null;
            showProgress(false);
            if (dg != null) {
                //if (dg.isShowing())
                dg.hide();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                dg = DialogUtils.showLoadingDialog(getString(R.string.text_proccessing), getContext(), null);
            } else {
                dg = DialogUtils.showLoadingDialog(getString(R.string.text_proccessing), getContext(), null);
            }
        } else {
            if (dg != null) {
                //if (dg.isShowing())
                dg.hide();
            }
        }

    }

    public void printInvoice(String inv) {

        String[] soBL = inv.split("_");
        BienLai bienLai = new BienLai();
        bienLai.setMoTa("Tham quan");
        bienLai.setGiaTien(10000);
        bienLai.setMau(preferences.loadStringSavedPreferences(
                Common.KEY_DEFAULT_PATTERN_INVOICES));
        bienLai.setSo(soBL[2].trim());
        bienLai.setKyHieu(preferences.loadStringSavedPreferences(
                Common.KEY_DEFAULT_SERIAL_INVOICES));

        byte reset[] = {0x1b, 0x40};
        byte lineFeed[] = {0x00, 0x0A};
        // Command -- Font Size, Alignment
        byte normalSize[] = {0x1D, 0x21, 0x00};
        byte dWidthSize[] = {0x1D, 0x21, 0x10};
        byte dHeightSize[] = {0x1D, 0x21, 0x01};
        byte rightAlign[] = {0x1B, 0x61, 0x02};
        byte centerAlign[] = {0x1B, 0x61, 0x01};
        byte leftAlign[] = {0x1B, 0x61, 0x00};

		/*//check if bluetooth is connected or not before any operation
		if (mPOSPrinter.getState() != mPOSPrinter.STATE_CONNECTED) {
			Toast.makeText(getApplicationContext(), "bluetooth is not connected yet", Toast.LENGTH_LONG).show();
			return;
		}*/

        MainActivity2.mPOSPrinter.setEncoding("utf-16BE");
        // send reset command first in front of every receipt
        MainActivity2.mPOSPrinter.sendByte(reset);

        //receipt content getString(R.string.type_app_receipt) +
        MainActivity2.mPOSPrinter.printText("BIÊN LAI ĐIỆN TỬ\r\n\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_DEFAULT, (byte) (MainActivity2.mPOSPrinter.TXT_2WIDTH | MainActivity2.mPOSPrinter.TXT_2HEIGHT));
        MainActivity2.mPOSPrinter.printText("STT:" + soBL[0] + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_LEFT, MainActivity2.mPOSPrinter.FNT_FONTB, (byte) (MainActivity2.mPOSPrinter.TXT_2WIDTH | MainActivity2.mPOSPrinter.TXT_2HEIGHT));
        MainActivity2.mPOSPrinter.printText("Phí: " + bienLai.getMoTa() + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_BOLD, (byte) MainActivity2.mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.printText("Giá: " + bienLai.getGiaTien() + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_BOLD, (byte) MainActivity2.mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.printText("Số BL: " + bienLai.getSo() + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_BOLD, (byte) MainActivity2.mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.printText("Mẫu: " + bienLai.getMau() + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_BOLD, (byte) MainActivity2.mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.printText("Ký hiệu: " + bienLai.getKyHieu() + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_BOLD, (byte) MainActivity2.mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.sendByte(centerAlign);
        MainActivity2.mPOSPrinter.printString("-------------------------------\r\n");

        //feed paper to make sure receipt is exposed enough to tear off
        MainActivity2.mPOSPrinter.lineFeed(2);

    }


    public void printInvoiceTest() {
        BienLai bienLai = new BienLai();
        bienLai.setMoTa("Biên lai in thử");
        bienLai.setGiaTien(10000);
        bienLai.setMau(preferences.loadStringSavedPreferences(
                Common.KEY_DEFAULT_PATTERN_INVOICES));
        bienLai.setSo("0");
        bienLai.setKyHieu(preferences.loadStringSavedPreferences(
                Common.KEY_DEFAULT_SERIAL_INVOICES));

        byte reset[] = {0x1b, 0x40};
        byte lineFeed[] = {0x00, 0x0A};
        // Command -- Font Size, Alignment
        byte normalSize[] = {0x1D, 0x21, 0x00};
        byte dWidthSize[] = {0x1D, 0x21, 0x10};
        byte dHeightSize[] = {0x1D, 0x21, 0x01};
        byte rightAlign[] = {0x1B, 0x61, 0x02};
        byte centerAlign[] = {0x1B, 0x61, 0x01};
        byte leftAlign[] = {0x1B, 0x61, 0x00};

		/*//check if bluetooth is connected or not before any operation
		if (mPOSPrinter.getState() != mPOSPrinter.STATE_CONNECTED) {
			Toast.makeText(getApplicationContext(), "bluetooth is not connected yet", Toast.LENGTH_LONG).show();
			return;
		}*/

        MainActivity2.mPOSPrinter.setEncoding("utf-16BE");
        // send reset command first in front of every receipt
        MainActivity2.mPOSPrinter.sendByte(reset);

        //receipt content getString(R.string.type_app_receipt) +
        MainActivity2.mPOSPrinter.printText("BIÊN LAI ĐIỆN TỬ\r\n\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_DEFAULT, (byte) (MainActivity2.mPOSPrinter.TXT_2WIDTH | MainActivity2.mPOSPrinter.TXT_2HEIGHT));
        //mPOSPrinter.printText("TEL (123)-456-7890\r\n",(byte)mPOSPrinter.ALIGNMENT_RIGHT,mPOSPrinter.FNT_FONTB, (byte)mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.printText("Phí: " + bienLai.getMoTa() + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_BOLD, (byte) MainActivity2.mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.printText("Giá: " + bienLai.getGiaTien() + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_BOLD, (byte) MainActivity2.mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.printText("Số BL: " + bienLai.getSo() + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_BOLD, (byte) MainActivity2.mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.printText("Mẫu: " + bienLai.getMau() + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_BOLD, (byte) MainActivity2.mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.printText("Ký hiệu: " + bienLai.getKyHieu() + "\r\n", (byte) MainActivity2.mPOSPrinter.ALIGNMENT_CENTER, MainActivity2.mPOSPrinter.FNT_BOLD, (byte) MainActivity2.mPOSPrinter.TXT_1WIDTH);
        MainActivity2.mPOSPrinter.sendByte(centerAlign);
        MainActivity2.mPOSPrinter.printString("-------------------------------\r\n");

        //feed paper to make sure receipt is exposed enough to tear off
        MainActivity2.mPOSPrinter.lineFeed(3);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUE_ADMIN) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Cấp quyền thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Vui lòng cấp quyền cho ứng dụng", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkPrinter() {
        //check printer status before any operation.
        int result;
        result = MainActivity2.mPOSPrinter.checkPrinter();
        if (result == MainActivity2.mPOSPrinter.MP_SUCCESS) {
            int status;
            String errMsg = "";
            status = MainActivity2.mPOSPrinter.getStatus();
            if ((status & MainActivity2.mPOSPrinter.STS_PAPER_MASK) == MainActivity2.mPOSPrinter.STS_PAPER_EMPTY)
                errMsg += "\n\t * Giấy in trống";
            else
                errMsg += "\n\t * Giấy in sẵn sàng";

			/*
			if((status&mPOSPrinter.STS_COVER_MASK)==mPOSPrinter.STS_COVER_OPEN)
				errMsg += "\n\t * Cover is open";
			else
				errMsg += "\n\t * Cover is closed";

			if((status&mPOSPrinter.STS_BATTERY_MASK)==mPOSPrinter.STS_BATTERY_LOW)
				errMsg += "\n\t * Battery level is low";
			else if((status&mPOSPrinter.STS_BATTERY_MASK)==mPOSPrinter.STS_BATTERY_MEDIUM)
				errMsg += "\n\t * Battery level is medium";
			else if((status&mPOSPrinter.STS_BATTERY_MASK)==mPOSPrinter.STS_BATTERY_HIGH)
				errMsg += "\n\t * Battery level is high";
			*/
            if (errMsg.length() > 0) {
                //please consider the battery level status affection.
                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
                return;
            }
            //else
            //{
            //	Toast.makeText(BluetoothDemo.this, "\n\t * Printer is normal", Toast.LENGTH_LONG).show();
            //	return;
            //}
        } else if (result == MainActivity2.mPOSPrinter.MP_NO_CONNECTION) {
            Toast.makeText(getContext(), "Bluetooth không được kết nối", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Kiểm tra thất bại", Toast.LENGTH_LONG).show();
            return;
        }
    }
}