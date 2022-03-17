package com.vnpt.staffhddtpos58;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vnpt.common.Common;
import com.vnpt.dto.InvoiceTrG;
import com.vnpt.utils.Helper;
import com.vnpt.utils.StoreSharePreferences;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import full.org.apache.xalan.processor.TransformerFactoryImpl;
import full.org.apache.xalan.transformer.TransformerImpl;


public class ContentInvoiceActivity2 extends AppCompatActivity {
    //http://stackoverflow.com/questions/6814268/android-share-on-facebook-twitter-mail-ecc
    // các action share qua tài khoản
    WebView webContent;
    String pathLoadHTML = "";
    LinearLayout layoutProccessing;
    InvoiceTrG mHoadon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutProccessing = (LinearLayout) findViewById(R.id.layout_proccessing);
        webContent = (WebView) findViewById(R.id.webContent);

        WebSettings webSettings = webContent.getSettings();
//        webContent.setVisibility(View.GONE);
//        layoutProccessing.setVisibility(View.VISIBLE);
        showViewWebContent(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        // set nhỏ view vừa đủ webview
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webContent.setInitialScale(1);
        //new AsysncTaskProccessingDataService(this).execute();
        xmlToHtml();
        showViewWebContent(true);

    }
    void showViewWebContent(boolean isShow)
    {
        if(isShow)
        {
            webContent.setVisibility(View.VISIBLE);
            layoutProccessing.setVisibility(View.GONE);
        }
        else
            {
                webContent.setVisibility(View.GONE);
                layoutProccessing.setVisibility(View.VISIBLE);
            }
    }
    private void xmlToHtml() {
        try {

            /*
             *
             * new solution, generated file is worst (empty) previously had some html data
             * */
            String pathFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mydata.html";
            Source xmlSource =null;
            Source xsltSource =null;
            xmlSource = new StreamSource(new BufferedInputStream(getAssets().open("HANOI_METROPOLE_FO_001.xml")));
//            String pathAbsoluteXml = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_PATH_DEFAULT_XML_RECENTLY);
            String pathAbsoluteXml = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_PATH_DEFAULT_XML_RECENTLY);
//            File file = new File("/storage/emulated/0/com.vnpt.staffhddt/XMLHDDT/01GTKT0_001AA_18E_13.xml");
            File file = new File(pathAbsoluteXml);
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            //xmlSource = new StreamSource(new BufferedInputStream(fis));
           // xsltSource = new StreamSource(new BufferedInputStream(getAssets().open("HANOI_METROPOLE_FO_001.xslt")));
            xsltSource = new StreamSource(new BufferedInputStream(getAssets().open("HANOI_METROPOLE_INT_06.xslt")));
            System.out.println("gia tri---2");
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
//            pathFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/metropole_"+timeStamp+".html";
            pathFile = StoreSharePreferences.getInstance(this).loadStringSavedPreferences(Common.KEY_PATH_DEFAULT_HTML_INVOICE)  + mHoadon.getPattern().replace("/","_")+ mHoadon.getSerial().replace("/","_")+"_"+mHoadon.getCheckNo()+"_"+mHoadon.getAmount()+".html";
            System.out.println("gia tri"+xsltSource);

            xmlSource.setSystemId(xmlSource.getSystemId());
            xsltSource.setSystemId(xsltSource.getSystemId());

            /*
             * I have added the Xalan-2.7.1 and Serializer-2.7.1 after using jarjar. The xslt file (spl-common.xsl)
             * requires additional extension which are not avaliable in android hence I had to add these extra jars.
             *
             * This is what I have come up with not sure if any of this is correct
             *
             * */

            TransformerFactoryImpl transFact = new TransformerFactoryImpl();
            transFact.setURIResolver(new URIResolver() {
                @Override
                public Source resolve(String href, String base) throws TransformerException {
                    try {
                        return new StreamSource(new BufferedInputStream(getAssets().open(href)));
                    } catch (IOException e) {
                        Log.e("tag",e.toString());
                        e.printStackTrace();
                        return null;
                    }
                }
            });

            TransformerImpl trans = (TransformerImpl) transFact.newTransformer(xsltSource);
            File f = new File(pathFile);

            StreamResult result = new StreamResult(f);
            trans.transform(xmlSource, result);
            pathLoadHTML = f.getPath();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content_invoice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            case R.id.action_print:
                // app icon in action bar clicked; go home
//                ToastMessageUtil.showToastLong(ContentInvoiceActivity.this,"Chức năng này sẽ hỗ trợ in qua máy in nhiệt hoặc Google Cloud Print");
                createWebPrintJob(webContent);
                return true;
//            case R.id.action_sign:
//                // app icon in action bar clicked; go home
//                startActivitySingatureManual();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void createWebPrintJob(WebView webView) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {

            PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

            PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

            String jobName = getString(R.string.app_name) + " Print Test";

            printManager.print(jobName, printAdapter,new PrintAttributes.Builder().build());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
                }
            }
            return;
        }
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AsysncTaskProccessingDataService extends AsyncTask<String, Void, Boolean>  {
        /**
         * khoi tao contructor
         *
         * @author: truonglt2
         * @version: 1.0
         * @since: 1.0
         */
        JSONArray mJsonArray;
        public AsysncTaskProccessingDataService(Context context) {
            mContext = context;
            dialog = new ProgressDialog(mContext);
        }

        Context mContext;
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(mContext.getResources().getString(R.string.text_proccessing));
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    onCancelled();
                }
            });
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //release list view
            if (this.isCancelled()) {
                return false;
            }
            try {
                String stringXML = null;
                try {
                    stringXML = Helper.getInstance().GeneralXML_InvoiceView(mHoadon,mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Helper.getInstance().showLog("stringXML:"+stringXML);
                xmlToHtml();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing())
                //dismiss dialog loading
                dialog.dismiss();
            if (success) {
                webContent.loadUrl("file:///" + pathLoadHTML);
            }
            showViewWebContent(true);
            onCancelled();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
