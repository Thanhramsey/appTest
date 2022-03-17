package com.vnpt.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.vnpt.common.Common;
import com.vnpt.common.EnglishNumberToWords;
import com.vnpt.dto.InvoiceTrG;
import com.vnpt.staffhddtpos58.BuildConfig;
import com.vnpt.webservice.SoapUntils.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static com.vnpt.utils.ImageHelperUtil.TAG;

public class Helper {
    protected static Helper instance;
    public static boolean isDebug = true;
    /**
     * Khoi tao the hien cua lop DatabaseHelp
     *
     *
     * @return
     *
     * @author: truonglt2
     * @return: DatabaseHelp
     * @throws:
     */
    public static Helper getInstance() {

        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    public static String getB64Auth() {
        String source = Common.remoteUser + ":" + Common.password;
        String Authorization = "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        return Authorization;
    }

    public static String toString(InputStream stream) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * Shows the soft keyboard
     */
//    public void showSoftKeyboard(View view) {
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        view.requestFocus();
//        inputMethodManager.showSoftInput(view, 0);
//    }
    private static long mLastClickTime = 0;

    public static boolean checkTime(int second) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < second * 1000) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    /**
     * Hide keyboard
     *
     * @param activity
     *
     * @author: truonglt
     * @return: void
     * @throws:
     */
    public static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus()!=null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String formatPrice(double parsed) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String formatted = formatter.format(parsed);
        return formatted;
    }

    //create a temporary file in sd card path.
    //fileName - vd: image.tmp
    public static File getFile(Context context, String fileName) {
        final File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, fileName);
    }

    public static String getRealPathFromURI(Activity activity, Uri contentUri) {

        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        System.out.println("absolutepath audiopath in getRealPathFromURI : " + cursor.getString(column_index));
        return cursor.getString(column_index);
    }

    public static void actionCallPhoneCustomer(Context context, String numberPhone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        try {
            callIntent.setData(Uri.parse("tel:" + numberPhone));
//            context.startActivity(callIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static String doubleFormatter(double number) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);

        NumberFormat formatter = new DecimalFormat("#0.00");

        String formattedNumber = formatter.format(number);

        return formattedNumber;

    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = context.getAssets().open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
            e.printStackTrace();
        }

        return bitmap;
    }

    // method to check if you have a Camera
    public boolean hasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    // method to check you have Camera Apps
    public boolean hasDefualtCameraApp(String action, Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;

    }
    public void showLog(String strLog) {
        if (BuildConfig.DEBUG) {
            // do something for a debug build
            if (isDebug) {
                Log.e("---------", strLog);
            }
        }
    }
    public String GeneralXML_InvoiceView(InvoiceTrG invoice,Context context) throws Exception {
            //string xmlCus = cus.XML;
        String xmlInv = invoice.XML.replace("<Invoice>","<Content>").replace("</Invoice>","</Content>").replace("<Inv>","<Invoice>").replace("</Inv>","</Invoice>");
        xmlInv = xmlInv.replace("<RoomNo>0</RoomNo>","<RoomNo></RoomNo>");
        XMLParser xmlParser = new XMLParser();
        Document documentXMLParser = xmlParser.getDomElement(xmlInv);

        // retrieve the element 'link'
        Element element = (Element) documentXMLParser.getElementsByTagName("key").item(0);

        // remove the specific node
        element.getParentNode().removeChild(element);








        String name =  StoreSharePreferences.getInstance(
                context).loadStringSavedPreferences(
                Common.KEY_USER_FULLNAME);
        if(name == null || name.equals("")|| name.equals("null"))
        {
            name =  StoreSharePreferences.getInstance(
                    context).loadStringSavedPreferences(
                    Common.KEY_USER_NAME);
        }
        Element elementCashier = (Element) documentXMLParser.getElementsByTagName("Cashier").item(0);
        elementCashier.appendChild(documentXMLParser.createTextNode(" /"+name));

        Element elementContent = (Element) documentXMLParser.getElementsByTagName("Content").item(0);

        try {
            // retrieve the element 'link'
            Element elementRN = (Element) documentXMLParser.getElementsByTagName("RoomNo").item(0);

            // remove the specific node
            element.getParentNode().removeChild(elementRN);
            // retrieve the element 'link'
            Element elementCN = (Element) documentXMLParser.getElementsByTagName("CusName").item(0);

            // remove the specific node
            element.getParentNode().removeChild(elementCN);
            // retrieve the element 'link'
            Element elementRate = (Element) documentXMLParser.getElementsByTagName("Rate").item(0);

            // remove the specific node
            element.getParentNode().removeChild(elementRate);
        }catch (Exception ex)
        {
        }




        Element InvoiceName = documentXMLParser.createElement("InvoiceName");
        InvoiceName.appendChild(documentXMLParser.createTextNode("HÓA ĐƠN GIÁ TRỊ GIA TĂNG"));
        elementContent.appendChild(InvoiceName);


        Element InvoicePattern = documentXMLParser.createElement("InvoicePattern");
        InvoicePattern.appendChild(documentXMLParser.createTextNode(invoice.Pattern));
        elementContent.appendChild(InvoicePattern);

        Element SerialNo = documentXMLParser.createElement("SerialNo");
        SerialNo.appendChild(documentXMLParser.createTextNode(invoice.Serial));
        elementContent.appendChild(SerialNo);

        Element InvoiceNo = documentXMLParser.createElement("InvoiceNo");
        InvoiceNo.appendChild(documentXMLParser.createTextNode("0000000"));
        elementContent.appendChild(InvoiceNo);

        try{
            Element AmountInWordsEnglish = documentXMLParser.createElement("AmountInWordsEnglish");
            AmountInWordsEnglish.appendChild(documentXMLParser.createTextNode(EnglishNumberToWords.convert(invoice.getAmount())+ " VND"));
            elementContent.appendChild(AmountInWordsEnglish);
        }catch (Exception ex)
        {
            Element AmountInWordsEnglish = documentXMLParser.createElement("AmountInWordsEnglish");
            AmountInWordsEnglish.appendChild(documentXMLParser.createTextNode("Zero VND"));
            elementContent.appendChild(AmountInWordsEnglish);
        }

        try{
            String roomNo = StoreSharePreferences.getInstance(context).loadStringSavedPreferences(Common.KEY_DATA_ROOM_NO);
            if (roomNo==null)
            {
                roomNo=" ";
            }
            Element RoomNo = (Element) documentXMLParser.getElementsByTagName("RoomNo").item(0);
            RoomNo.appendChild(documentXMLParser.createTextNode(" "+roomNo));
//            Element RoomNo =  documentXMLParser.createElement("RoomNo");
//            RoomNo.appendChild(documentXMLParser.createTextNode(roomNo));
//            elementContent.appendChild(RoomNo);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        try{
            String roomNo = StoreSharePreferences.getInstance(context).loadStringSavedPreferences(Common.KEY_DATA_ROOM_NO);
            if (roomNo==null)
            {
                roomNo=" ";
            }
            Element ERate = (Element) documentXMLParser.getElementsByTagName("Rate").item(0);
            if (ERate== null)
            {
                ERate = documentXMLParser.createElement("Rate");
            }
            ERate.appendChild(documentXMLParser.createTextNode(" "+roomNo));
//            Element RoomNo =  documentXMLParser.createElement("RoomNo");
//            RoomNo.appendChild(documentXMLParser.createTextNode(roomNo));
//            elementContent.appendChild(RoomNo);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        try{
            String nameCus = StoreSharePreferences.getInstance(context).loadStringSavedPreferences(Common.KEY_DATA_NAME_CUS);
            if (nameCus==null)
            {
                nameCus=" ";
            }
            Element CusName = (Element) documentXMLParser.getElementsByTagName("CusName").item(0);
            CusName.appendChild(documentXMLParser.createTextNode(" "+nameCus));
//            Element NameCus = documentXMLParser.createElement("CusName");
//            NameCus.appendChild(documentXMLParser.createTextNode(nameCus));
//            elementContent.appendChild(NameCus);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        String pathAbsoluteImg = StoreSharePreferences.getInstance(context).loadStringSavedPreferences(Common.KEY_PATH_IMAGE_SIGN_SERVER_RECENTLY);

        if(pathAbsoluteImg!=null && pathAbsoluteImg.length()>0 && !pathAbsoluteImg.contains("An error has occurred."))
        {
            pathAbsoluteImg = pathAbsoluteImg.substring(2,pathAbsoluteImg.length()-2);
        }
        else
            {
                pathAbsoluteImg = "";
            }

//        String base64= readFileAsBase64String(pathAbsoluteImg);
//        String base64="http://metropolehanoi.vscmt.vn/Report/Metropole_8_2018/01GTKT0_001ML_18E_0.png";
//        showLog("pathAbsoluteImg:"+base64);



        Element imageSignedCus = documentXMLParser.createElement("imageSignedCus");
        imageSignedCus.appendChild(documentXMLParser.createTextNode(pathAbsoluteImg));
        elementContent.appendChild(imageSignedCus);
        // Normalize the DOM tree, puts all text nodes in the
        // full depth of the sub-tree underneath this node
        documentXMLParser.normalize();


// Navigate down the hierarchy to get to the CEO node


        String xml =prettyPrint(documentXMLParser,context,invoice);

//        String xmlInv = xml.replace("<Invoice>","").replace("</Invoice>","").replace("<Inv>","").replace("</Inv>","");
//
//
//            String XML = "";
//            XML += "<Invoice>";
//            XML += "<Content>";
//            XML += "<InvoiceName>HÓA ĐƠN GIÁ TRỊ GIA TĂNG</InvoiceName>";
//            XML += "<InvoicePattern>" + invoice.Pattern + "</InvoicePattern>";
//            XML += "<SerialNo>" + invoice.Serial + "</SerialNo>";
//            XML += "<InvoiceNo>0000000</InvoiceNo>";
////            XML += xmlCompany;
//
//            XML += "</Content>";
//            XML += "</Invoice>";

            return xml;
    }
    public String prettyPrint(Document xml,Context context,InvoiceTrG invoice) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        //tf.transform(new DOMSource(xml), new StreamResult(out));
        tf.transform(new DOMSource(xml), new StreamResult(GeneralsUtils.getInstance().getOutputXmlFile(context,invoice)));
        Log.e("XXXXX-","XXXXXXXX:"+out.toString());
        return out.toString();
    }
    // You probably don't want to do this with large files
// (will allocate a large string and can cause an OOM crash).
    private String readFileAsBase64String(String path) throws IOException {
        try {
            InputStream is = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Base64OutputStream b64os = new Base64OutputStream(baos, Base64.DEFAULT);
            byte[] buffer = new byte[8192];
            int bytesRead;
            try {
                while ((bytesRead = is.read(buffer)) > -1) {
                    b64os.write(buffer, 0, bytesRead);
                }
                return baos.toString();
            } catch (IOException e) {
                //Log.e(TAG, "Cannot read file " + path, e);
                // Or throw if you prefer
                return "";
            } finally {
                is.close();
                closeQuietly(b64os); // This also closes baos
            }
        } catch (IOException e) {
            Log.e(TAG, "File not found " + path, e);
            // Or throw if you prefer
            return "";
        }
    }
    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
        }
    }
    public static boolean isStringEmpty(String str){
        boolean isEmpty = false;
        if (str == null || str.trim().length() == 0 || str.equals("null")) {
            isEmpty = true;
        }
        return  isEmpty;
    }
}
