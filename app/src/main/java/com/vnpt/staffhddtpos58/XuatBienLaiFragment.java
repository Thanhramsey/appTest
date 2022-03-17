package com.vnpt.staffhddtpos58;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.izettle.html2bitmap.Html2Bitmap;
import com.izettle.html2bitmap.content.WebViewContent;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vnpt.common.Common;
import com.vnpt.common.ModelEvent;
import com.vnpt.dto.BienLai;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.printproject.pos58bus.command.sdk.Command;
import com.vnpt.printproject.pos58bus.command.sdk.PrintPicture;
import com.vnpt.printproject.pos58bus.command.sdk.PrinterCommand;
import com.vnpt.staffhddtpos58.fragment.BaseFragment;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.StringBienLai;
import com.vnpt.webservice.AppServices;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.vnpt.staffhddtpos58.MainActivity.mPOSPrinter;
import static com.vnpt.utils.Helper.hideSoftKeyboard;

public class XuatBienLaiFragment extends BaseFragment implements View.OnClickListener, OnEventControlListener {

    private static final int REQUEST_BLUE_ADMIN = 888;
    MaterialBetterSpinner spMenhGia;
    MaterialEditText edtSoLuong;
    Button btnXuatBL, btnInThu, btnCheckTB;
    private AwesomeProgressDialog dg;
    StoreSharePreferences preferences = null;

    private GetInvTask getInvTask = null;

    public static String TAG = XuatBienLaiFragment.class.getName();
    private static final String CHINESE = "GBK";


    private static final String[] PRICES = new String[]{
            "10.000"
    };

    public XuatBienLaiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_xuat_bien_lai, container, false);

        setupUI(layout.findViewById(R.id.layout_frament_xuatbienlai));
        init(layout);
        setEventForMembers();
        ((MainActivity) getActivity()).showProccessbar(false);

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
            if (numOfInv <= 30) {
                StringBuilder sb = new StringBuilder();

                //lap lai so ve
                for (int i = 1; i <= numOfInv; i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                        long currentTime = System.currentTimeMillis();
                        String keyData = i + "_INVKBL" + currentTime;
                        String xmlChildData = "<Inv><key>" + keyData + "</key><Invoice><CusCode><![CDATA[" + keyData + "]]></CusCode><CusName><![CDATA[" + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME) + "]]></CusName><CusAddress><![CDATA[]]></CusAddress><CusPhone></CusPhone><CusTaxCode></CusTaxCode><PaymentMethod><![CDATA[]]></PaymentMethod><Products><Product><Code><![CDATA[]]></Code><ProdName><![CDATA[Phí tham quan]]></ProdName><ProdUnit>Lần</ProdUnit><ProdQuantity>1</ProdQuantity><ProdPrice>10000</ProdPrice><Amount>1</Amount></Product></Products><KindOfService><![CDATA[]]></KindOfService><Total>10000</Total><Amount>10000</Amount><AmountInWords><![CDATA[Mười ngàn đồng]]></AmountInWords></Invoice></Inv>";
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
            } else {
                showProgress(false);
                Toast.makeText(getContext(), "Vui lòng nhập số lượng nhỏ hơn hoặc bằng 30", Toast.LENGTH_SHORT).show();
            }

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
                if (mPOSPrinter.isBTActivated()) {

                    if (mPOSPrinter.getState() == com.vnpt.printproject.pos58bus.BluetoothService.STATE_CONNECTED) {

                    } else {
                        Toast.makeText(getContext(), "Vui lòng kết nối với máy in", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth ", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btnInThu: {
                if (mPOSPrinter.isBTActivated()) {
                    Print_Test();
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

    public static String withLargeIntegers(double value) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(value);
    }

    private void checkPrinter() {
        if (mPOSPrinter.getState() != com.vnpt.printproject.pos58bus.BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getContext(), R.string.connected, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        ((MainActivity) getActivity()).showProccessbar(false);
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

            printInvoice(listResults.length, listResults[0], listResults[listResults.length - 1]);
            getInvTask = null;
            if (dg != null) {
                //if (dg.isShowing())
                dg.hide();
            }

            edtSoLuong.setText(" ");
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
            Toast.makeText(getContext(), "Quá trình in bị huỷ", Toast.LENGTH_SHORT).show();
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

    public void printInvoice(int length, String firstPos, String lastPos) {

        int price = 10000 * (length);
        String lastIndx = lastPos.split("_")[2];
        String firstIdx = firstPos.split("_")[2];

        int soBL = length;
        BienLai bienLai = new BienLai();
        bienLai.setMoTa("Tham quan");
        bienLai.setGiaTien(price);
        bienLai.setMau(preferences.loadStringSavedPreferences(
                Common.KEY_DEFAULT_PATTERN_INVOICES));
        bienLai.setSo(firstIdx + " -> " + lastIndx);
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

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        //Get current locale information
//        Locale currentLocale = Locale.getDefault();
//
//        //Get currency instance from locale; This will have all currency related information
//        Currency currentCurrency = Currency.getInstance(currentLocale);
//
//        //Currency Formatter specific to locale
//        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);

        String html = "<html>\n" +
                "                <body>\n" +
                "                <style>\n" +
                "                 </style>\n" +
                "<div style=\"text-align:center;\">"+
                "                   <h2 style=\\\"text-align: center;font-size: 40px\\\">TT VH,TT&TT Pleiku</h2>\n" +
                "                       <h2 style=\\\"text-align: center;font-size: 40px\\\">MST: 5901111762</h2>\n" +
                "               <h1 style=\\\"text-align: center;font-size: 40px\\\">BIÊN LAI ĐIỆN TỬ</h1>\n" +
                "<h3 style='text-align:right;font-size: 20px'>(Phí tham quan)</h3>" +
                "<h3 style='text-align:right;font-size: 20px'>(BLĐT này không thay thế cho BL thu phí, lệ phí)</h3>" +
                "</div>"+
                "                \n" +"<h2 style=\\\"text-align: center;font-size: 40px\\\">Ngày: "+date+"</h2>"+
                "               <h3 style=\\\"text-align:right;font-size: 20px\\\">Mẫu: "+ bienLai.getMau() +"</h3>\n" +
                "                   <h3 style=\\\"text-align:right;font-size: 20px\\\">Ký hiệu: "+bienLai.getKyHieu()+"</h3>\n" +
                "                    <h2 style=\\\"text-align:right;font-size: 20px\\\">Số BL: "+bienLai.getSo()+"</h2>\n" +
                "                    <h2 style=\\\"text-align: right;font-size: 20px\\\">SL: "+soBL+"</h2>\n" +

                "                 <h2>Giá: "+ bienLai.getGiaTien() +" ₫</h2>\n" +
                "<h2>("+ StringBienLai.docSo(bienLai.getGiaTien()) +")</h2>" +

                "               <h1 style=\\\"text-align: center\\\">-------------------------- </h1>" +
                "<h3 style='text-align:right;font-size: 20px'>(Để thanh toán quý khách vui lòng cung cấp email, số điện thoại ở mặt sau BLĐT này cho quầy bán vé. Xin cảm ơn !)</h3>" +
                "                </body>\n" +
                "                </html>";
        new converHTMLTask().execute(html);

    }

    /*
     *SendDataByte
     */
    private void SendDataByte(byte[] data) {

        if (mPOSPrinter.getState() != com.vnpt.printproject.pos58bus.BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mPOSPrinter.write(data);
    }

    private void Print_Test() {
        String msg = "<html>\n" +
                "<body>\n" +
                "<style>\n" +
                "  </style>\n" +
                "<h1 style=\"text-align: center;font-size: 40px\">BIÊN LAI ĐIỆN TỬ</h1>\n" +
                "<h1 style=\"text-align: left;font-size: 40px\">STT: 1</h1>\n" +
                "<table style=\"text-align: center;width:100%;\">\n" +
                "  <tr>\n" +
                "    <th style=\"font-size:30px\">Phí:  Biên lai in thử</th>\n" +
                "  </tr>\n" +
                "</table>  <h1 style=\"text-align: center\">\n" +
                "    -----------------------------\n" +
                "  </h1>" +
                "\n" +
                "</body>\n" +
                "</html>";
        new converHTMLTask().execute(msg);
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

    private class converHTMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... str) {
            String html = str[0];
            //Toast.makeText(MainActivity.this, "Converting...", Toast.LENGTH_SHORT).show();
            return new Html2Bitmap.Builder()
                    .setContext(getContext())
                    .setContent(WebViewContent.html(html))
                    .setBitmapWidth(384)
                    .build()
                    .getBitmap();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                Print_BMP(bitmap);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getContext(), "Quá trình in bị huỷ", Toast.LENGTH_SHORT).show();
        }
    }

    private void Print_BMP(Bitmap mBitmap){
        //	byte[] buffer = PrinterCommand.POS_Set_PrtInit();
        int nMode = 0;
        int nPaperWidth = 384;

        if(mBitmap != null)
        {
            byte[] data = PrintPicture.POS_PrintBMP(mBitmap, nPaperWidth, nMode);
            //	SendDataByte(buffer);
            SendDataByte(Command.ESC_Init);
            SendDataByte(Command.LF);
            SendDataByte(data);
            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(50));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }
    }
}