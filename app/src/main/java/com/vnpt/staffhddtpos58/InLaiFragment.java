package com.vnpt.staffhddtpos58;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.izettle.html2bitmap.Html2Bitmap;
import com.izettle.html2bitmap.content.WebViewContent;
import com.vnpt.common.Common;
import com.vnpt.common.ModelEvent;
import com.vnpt.dto.BienLai;
import com.vnpt.printproject.pos58bus.command.sdk.Command;
import com.vnpt.printproject.pos58bus.command.sdk.PrintPicture;
import com.vnpt.printproject.pos58bus.command.sdk.PrinterCommand;
import com.vnpt.staffhddtpos58.fragment.BaseFragment;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.StringBienLai;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.vnpt.staffhddtpos58.MainActivity.mPOSPrinter;
import static com.vnpt.utils.Helper.hideSoftKeyboard;


public class InLaiFragment extends BaseFragment {

    EditText edtMauSo, edtKyHieu, edtTuSo, edtDenSo;
    Button btnInLai;

    StoreSharePreferences preferences = null;

    public static String TAG = InLaiFragment.class.getName();

    public InLaiFragment() {
        // Required empty public constructor
    }

    public static InLaiFragment newInstance(String param1, String param2) {
        InLaiFragment fragment = new InLaiFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    protected void init(View layout) {
        edtMauSo = layout.findViewById(R.id.edtMauSo);
        edtKyHieu = layout.findViewById(R.id.edtKyHieu);
        edtTuSo = layout.findViewById(R.id.edtTuSo);
        edtDenSo = layout.findViewById(R.id.edtDenSo);
        btnInLai = layout.findViewById(R.id.btnInLai);
    }

    @Override
    protected void setValueForMembers() {

    }

    @Override
    protected void setEventForMembers() {
        btnInLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFormDataAndPrint();
            }
        });
    }

    private void getFormDataAndPrint() {
        String pattern = edtMauSo.getText().toString().trim();
        String serial = edtKyHieu.getText().toString().trim();
        String fromNum = edtTuSo.getText().toString().trim();
        String toNum = edtDenSo.getText().toString().trim();

        if (TextUtils.isEmpty(pattern) || TextUtils.isEmpty(serial) || TextUtils.isEmpty(fromNum) || TextUtils.isEmpty(toNum)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ nội dung", Toast.LENGTH_SHORT).show();
        } else {
            int start = Integer.parseInt(fromNum);
            int end = Integer.parseInt(toNum);

            if (start > end) {
                Toast.makeText(getContext(), "Số cuối phải lớn hơn hoặc bằng số đầu", Toast.LENGTH_SHORT).show();
            } else {
                if (mPOSPrinter.isBTActivated()) {
                    if (mPOSPrinter.getState() == com.vnpt.printproject.pos58bus.BluetoothService.STATE_CONNECTED) {
                        printInvoice(pattern, serial, start, end);
                    } else {
                        Toast.makeText(getContext(), "Vui lòng kết nối với máy in", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String pattern = preferences.loadStringSavedPreferences(Common.KEY_DEFAULT_PATTERN_INVOICES);
        String serial = preferences.loadStringSavedPreferences(Common.KEY_DEFAULT_SERIAL_INVOICES);

        edtMauSo.setText(pattern);
        edtKyHieu.setText(serial);
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {

    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_in_lai, container, false);

        setupUI(layout.findViewById(R.id.layout_fragment_inlai));
        init(layout);
        setEventForMembers();
        ((MainActivity) getActivity()).showProccessbar(false);

        preferences = StoreSharePreferences.getInstance(getContext());

        return layout;
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

    public void printInvoice(String pattern, String serial, int firstPos, int lastPos) {

        int soBL = lastPos - firstPos + 1;
        int price = 10000 * soBL;
        BienLai bienLai = new BienLai();
        bienLai.setMoTa("Tham quan");
        bienLai.setGiaTien(price);
        bienLai.setMau(pattern);
        bienLai.setSo(firstPos + " -> " + lastPos);
        bienLai.setKyHieu(serial);

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
                "<h5 style=\\\"text-align: center\\\">Biên lai in lại<h5/>" +
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

    private class converHTMLTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MainActivity) getActivity()).showProccessbar(true);
        }

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
            ((MainActivity) getActivity()).showProccessbar(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            ((MainActivity) getActivity()).showProccessbar(false);
            Toast.makeText(getContext(), "Quá trình in bị huỷ", Toast.LENGTH_SHORT).show();
        }
    }
}