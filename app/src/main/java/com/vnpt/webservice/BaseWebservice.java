package com.vnpt.webservice;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vnpt.common.Common;
import com.vnpt.utils.Helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseWebservice {

    private static final String TAG = "BaseWebservice";
    public static final int TIME_OUT = 5000;
//    public static final String URL_SERVER = "http://hcmlottehotelapi.vscmt.vn";
//    public static final String URL_SERVER = "http://metropolehanoi.vscmt.vn";
//    public static final String URL_SERVER = "http://localhost:8080/bntt/";
//    public static final String URL_SERVER = "http://api.metropole.e-invoicing.vn";
//    public static final String URL_SERVER = "http://172.21.105.179";//sòfitel
    public static final String URL_SERVER = "http://101.96.76.185";


    public static final String USER_API_LOGIN = "/api/user/login";
    public static final String USER_API_INFOR = "/api/user/infor-user";
    public static final String USER_API_UPDATE_SIGNED_MOBILE = "/api/invoice/UpdateSignedMobile";
    public static final String INVOICE_API_LIST_INVOICE_BY_USER = "/api/Portal/GetListInvoicesStatus";
    public static final String INVOICE_API_LIST_INVOICE_BY_DATE = "/api/Portal/GetFullListInvoicesByDate";
    public static final String INVOICE_API_LIST_INVOICE_SIGN_BY_DATE = "/api/Portal/GetFullListInvoicesSignByDate";

    public static final String INVOICE_API_LIST_INVOICE_UPLOAD_FILE= "/api/invoice/UpdateImageMobile";
    public static final String INVOICE_API_LIST_OUTLET_NAME= "/api/Portal/GetListOutletName";

//    public static final String INVOICE_API_LIST_INVOICE_BY_USER = "getInvoiceByUser";
    public static final String INVOICE_API_DETAIL_INVOICE_BY_USER = "getDetailsInvoiceByCustomer";
    public static final String INVOICE_API_GET_ALL_TERM = "getAllTermInvoice";

    public static final String INVOICE_API_UPDATE_STATUS_PAYMENTED_INVOICE = "updateStatusPaymentedInvoice";
    public static final String INVOICE_API_LIST_INVOICE_HISTORY = "listInvoiceHistoryByIdCustomer";
    public static final String INVOICE_API_UPDATE_STATUS_RETURN_INVOICE = "updateStatusPaymentedToNotPaymentInvoice";

    public static String authorizationString = "";

//	public static void changeServerPath(String newServerPath){
//		ApplicationConfig.SERVER = newServerPath;
//		ApplicationConfig.VIRTUAL_HOST = ApplicationConfig.SERVER;
//		if(ApplicationConfig.VIRTUAL_HOST.contains("http"))
//			ApplicationConfig.VIRTUAL_HOST.replace("http://", "");
//	}

    public static void init() {
        Common.AUTHORIZE_STRING = Helper.getB64Auth();
    }
    int serverResponseCode = 0;
    String upLoadServerUri = null;

    /**********  File Path *************/
    final String uploadFilePath = "/mnt/sdcard/";
    public BaseWebservice() {
        //Authorization user and pass admin
        Common.AUTHORIZE_STRING = Helper.getB64Auth();
        Log.e(TAG, Common.AUTHORIZE_STRING + "");
    }

    public String getWSAsString(String wsUrl, String errorLog) {
        String ret = null;
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("getWSAsString:" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                ret = Helper.toString(is).trim();
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * get Service from Server
     *
     * @param wsUrl
     * @param errorLog
     *
     * @return
     */
    protected JSONArray doGet(String wsUrl, String errorLog) {
        JSONArray ret = null;
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("doGet:::" + wsUrl);
        try {
//            HttpGet post = new HttpGet(wsUrl);
            HttpGet request = new HttpGet();
            URI website = new URI(wsUrl);
            request.setURI(website);
//            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is);
                ret = new JSONArray(str);
            }
            System.out.println("doGet:Result::" + ret.toString());
        } catch (Exception ex) {
            Log.e(TAG, errorLog+":"+ex);
        }
        return ret;
    }
    protected JSONObject doGetJSObject(String wsUrl, String errorLog) {
        JSONObject ret = null;
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("doGet:::" + wsUrl);
        try {
//            HttpGet post = new HttpGet(wsUrl);
            HttpGet request = new HttpGet();
            URI website = new URI(wsUrl);
            request.setURI(website);
//            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is);
                ret = new JSONObject(str);
            }
            System.out.println("doGet:Result::" + ret.toString());
        } catch (Exception ex) {
            Log.e(TAG, errorLog+":"+ex);
        }
        return ret;
    }
    /**
     * get Service from Server
     *
     * @param wsUrl
     * @param errorLog
     *
     * @return
     */
    protected JSONArray doGetMetro(String wsUrl, String errorLog) {
        JSONArray reta = null;
        JSONObject ret = null;
        BasicHttpParams httpParams = new BasicHttpParams();
        //this will set socket timeout
        HttpConnectionParams.setSoTimeout(httpParams, /*say*/ TIME_OUT);
        //this will set connection timeout
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        HttpClient client = new DefaultHttpClient(httpParams);
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("doGet:::" + wsUrl);
        try {
//            HttpGet post = new HttpGet(wsUrl);
            HttpGet request = new HttpGet();
            URI website = new URI(wsUrl);
            request.setURI(website);
//            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is);
                reta = new JSONArray(str);
                System.out.println("doGet:Result::" + str);
            }

        } catch (Exception ex) {
            Log.e(TAG, errorLog+":"+ex);
        }
        return reta;
    }
    protected JSONArray doGetJSONArrayPost(String wsUrl, String errorLog, List<NameValuePair> listParams) {
        JSONArray ret = null;
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("doPost:::" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            post.setEntity(new UrlEncodedFormEntity(listParams));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is);
                ret = new JSONArray(str);
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return ret;
    }

    protected JSONArray doGetJSONArrayPostUTF_8(String wsUrl, String errorLog, List<NameValuePair> listParams) {
        JSONArray ret = null;
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        params.setBooleanParameter("http.protocol.expect-continue", false);
        HttpClient client = new DefaultHttpClient(params);
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("doPost:::" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
//            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
//            post.setEntity(new UrlEncodedFormEntity(listParams));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                /*InputStream is = entity.getContent();
                String str = Helper.toString(is);*/
                String str = EntityUtils.toString(entity, HTTP.UTF_8);
                ret = new JSONArray(str);
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return ret;
    }

    public JSONObject doGetJSONObj(String wsUrl, String errorLog) {
        JSONObject ret = null;
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("doGetJSONObj" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is);

                ret = new JSONObject(str);
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return ret;
    }

    public JSONObject doGetJSONObjPOST(String wsUrl, String errorLog, List<NameValuePair> nameValuePairs) {
        JSONObject ret = null;
        HttpClient client = getNewHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is).trim();
                System.out.println("chuoi str:" + str);
                ret = new JSONObject(str);
                is.close();
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return ret;
    }

    public long doGetJSONObjPOST(String wsUrl, String errorLog, MultipartEntity nameValuePairs) {
        long ret = 0;
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpClient client = new DefaultHttpClient(params);
        System.out.println("WsURl:" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
//            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            post.setEntity(nameValuePairs);

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is).trim();
//                ret = Long.parseLong(str);
                System.out.println(str);
            }
            client.getConnectionManager().shutdown();
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return ret;
    }

    public boolean doBooleanPOST(String wsUrl, String errorLog, List<NameValuePair> nameValuePairs) {
        boolean ret = false;
        HttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is).trim();
                if (str.equals("true"))
                    ret = true;
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return ret;
    }


    public JSONObject doGetNotAuth(String wsUrl, String errorLog) {
        JSONObject ret = null;
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("doGetJSONObj" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is);

                ret = new JSONObject(str);
            }
        } catch (Exception ex) {

            Log.e(TAG, errorLog);
        }
        return ret;
    }

    /**
     * parse JSONArray to ArrayList<ArrayList<String>>
     *
     * @param jArr
     * @param params
     *
     * @return
     */
    public ArrayList<ArrayList<String>> jsonArrTo2DimenArrayList(JSONArray jArr, String[] params) {
        ArrayList<ArrayList<String>> ret = null;
        if (jArr != null) {
            try {
                ret = new ArrayList<ArrayList<String>>();
                int count = jArr.length();
                for (int i = 0; i < count; i++) {
                    ArrayList<String> item = new ArrayList<String>();
                    JSONObject obj = jArr.getJSONObject(i);
                    for (String iter : params) {
                        item.add(obj.getString(iter));
                    }
                    ret.add(item);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * parse JSONArray to ArrayList<ArrayList<String>>
     * print errorLog when error occur.
     *
     * @param jArr
     * @param params
     * @param errorLog
     *
     * @return
     */
    public ArrayList<ArrayList<String>> jsonArrTo2DimenArrayList(JSONArray jArr, String[] params, String errorLog) {
        ArrayList<ArrayList<String>> ret = null;
        if (jArr != null) {
            try {
                ret = new ArrayList<ArrayList<String>>();
                int count = jArr.length();
                for (int i = 0; i < count; i++) {
                    ArrayList<String> item = new ArrayList<String>();
                    JSONObject obj = jArr.getJSONObject(i);
                    for (String iter : params) {
                        item.add(obj.getString(iter));
                    }
                    ret.add(item);
                }
            } catch (Exception ex) {
                Log.e(TAG, errorLog);
                ex.printStackTrace();
            }
        }
        return ret;
    }

    public long doLong(String wsUrl, String errorLog) {
        long kq = 0;
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("getWSAsString:" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String ret = Helper.toString(is).trim();
                kq = Long.parseLong(ret);
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return kq;
    }
//	public long doLongPOST(String wsUrl, String merchantId, String firstName, String lastName, String middleName, 
//			String inputStr, String imageType, String email, String phone, String address, String birthday, String sex, String errorLog){
//		long kq = 0;
//		HttpClient client = new DefaultHttpClient();
//		try{
//			HttpPost post = new HttpPost(wsUrl);
//			post.setHeader("Authorization",Common.AUTHORIZE_STRING);
//			
//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(15);
//			nameValuePairs.add(new BasicNameValuePair("merchantId", merchantId));
//			nameValuePairs.add(new BasicNameValuePair("firstName", firstName));
//			nameValuePairs.add(new BasicNameValuePair("lastName", lastName));
//			nameValuePairs.add(new BasicNameValuePair("middleName", middleName));
//			nameValuePairs.add(new BasicNameValuePair("inputStr", inputStr));
//			nameValuePairs.add(new BasicNameValuePair("imageType", imageType));
//			nameValuePairs.add(new BasicNameValuePair("email", email));
//			nameValuePairs.add(new BasicNameValuePair("phone", phone));
//			nameValuePairs.add(new BasicNameValuePair("address", address));
//			nameValuePairs.add(new BasicNameValuePair("birthday", birthday));
//			nameValuePairs.add(new BasicNameValuePair("sex", sex));
//			nameValuePairs.add(new BasicNameValuePair("userId", ApplicationConfig.userId));
//			nameValuePairs.add(new BasicNameValuePair("groupId", ApplicationConfig.groupdId));
//			nameValuePairs.add(new BasicNameValuePair("companyId", ApplicationConfig.companyId));
//			nameValuePairs.add(new BasicNameValuePair("status", "0"));
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			
//			HttpResponse response = client.execute(post);
//			HttpEntity entity = response.getEntity();
//			if(entity != null){
//				InputStream is = entity.getContent();
//				String ret = Helper.toString(is).trim();	
//				kq = Long.parseLong(ret);
//			}
//		}
//		catch(Exception ex){
//			Log.e(TAG, errorLog);
//			ex.printStackTrace();
//		}
//		return kq;
//	}

    public long doLongPOST(String wsUrl, String errorLog, List<NameValuePair> nameValuePairs) {
        long kq = 0;
        HttpClient client = new DefaultHttpClient();
        System.out.println("WsURl:" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String ret = Helper.toString(is).trim();
                System.out.println("ket qua ret:" + ret);
                kq = Long.parseLong(ret);
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return kq;
    }

    //	public long doLongPOSTAdd(String wsUrl, String merchantId, String firstName, String lastName, String middleName,
//			String inputStr, String imageType, String email, String phone, String address, String birthday, String sex, String errorLog){
//		long kq = 0;
//		HttpClient client = new DefaultHttpClient();
//		try{
//			HttpPost post = new HttpPost(wsUrl);
//			post.setHeader("Authorization",Common.AUTHORIZE_STRING);
//			
//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(15);
//			nameValuePairs.add(new BasicNameValuePair("merchantId", merchantId));
//			nameValuePairs.add(new BasicNameValuePair("firstName", firstName));
//			nameValuePairs.add(new BasicNameValuePair("lastName", lastName));
//			nameValuePairs.add(new BasicNameValuePair("middleName", middleName));
//			nameValuePairs.add(new BasicNameValuePair("inputStr", inputStr));
//			nameValuePairs.add(new BasicNameValuePair("imageType", imageType));
//			nameValuePairs.add(new BasicNameValuePair("email", email));
//			nameValuePairs.add(new BasicNameValuePair("phone", phone));
//			nameValuePairs.add(new BasicNameValuePair("address", address));
//			nameValuePairs.add(new BasicNameValuePair("birthday", birthday));
//			nameValuePairs.add(new BasicNameValuePair("sex", sex));
//			nameValuePairs.add(new BasicNameValuePair("userId", ApplicationConfig.userId));
//			nameValuePairs.add(new BasicNameValuePair("groupId", ApplicationConfig.groupdId));
//			nameValuePairs.add(new BasicNameValuePair("companyId", ApplicationConfig.companyId));
//			nameValuePairs.add(new BasicNameValuePair("status", "0"));
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			
//			HttpResponse response = client.execute(post);
//			HttpEntity entity = response.getEntity();
//			if(entity != null){
//				InputStream is = entity.getContent();
//				String ret = Helper.toString(is).trim();	
//				kq = Long.parseLong(ret);
//			}
//		}
//		catch(Exception ex){
//			Log.e(TAG, errorLog);
//			ex.printStackTrace();
//		}
//		return kq;
//	}
//	public long doLongPOSTEdit(String wsUrl, String merchantId, String firstName, String lastName, String middleName, 
//			String inputStr, String imageType, String email, String phone, String address, String birthday, String sex, String errorLog){
//		long kq = 0;
//		HttpClient client = new DefaultHttpClient();
//		try{
//			HttpPost post = new HttpPost(wsUrl);
//			post.setHeader("Authorization",Common.AUTHORIZE_STRING);
//			
//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(15);
//			nameValuePairs.add(new BasicNameValuePair("merchantId", merchantId));
//			nameValuePairs.add(new BasicNameValuePair("firstName", firstName));
//			nameValuePairs.add(new BasicNameValuePair("lastName", lastName));
//			nameValuePairs.add(new BasicNameValuePair("middleName", middleName));
//			nameValuePairs.add(new BasicNameValuePair("inputStr", inputStr));
//			nameValuePairs.add(new BasicNameValuePair("imageType", imageType));
//			nameValuePairs.add(new BasicNameValuePair("email", email));
//			nameValuePairs.add(new BasicNameValuePair("phone", phone));
//			nameValuePairs.add(new BasicNameValuePair("address", address));
//			nameValuePairs.add(new BasicNameValuePair("birthday", birthday));
//			nameValuePairs.add(new BasicNameValuePair("sex", sex));
//			nameValuePairs.add(new BasicNameValuePair("userId", Common.userId));
//			nameValuePairs.add(new BasicNameValuePair("groupId", Common.groupdId));
//			nameValuePairs.add(new BasicNameValuePair("companyId", Common.companyId));
//			nameValuePairs.add(new BasicNameValuePair("status", "0"));
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			
//			HttpResponse response = client.execute(post);
//			HttpEntity entity = response.getEntity();
//			if(entity != null){
//				InputStream is = entity.getContent();
//				String ret = Helper.toString(is).trim();	
//				kq = Long.parseLong(ret);
//			}
//		}
//		catch(Exception ex){
//			Log.e(TAG, errorLog);
//			ex.printStackTrace();
//		}
//		return kq;
//	}
    public boolean doBoolean(String wsUrl, String errorLog) {
        boolean kq = false;
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("getWSAsString:" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String ret = Helper.toString(is).trim();
                kq = Boolean.parseBoolean(ret);
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return kq;
    }

    public String doStringPost(String wsUrl, String errorLog, List<NameValuePair> listParams) {
        String kq = "";
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("getWSAsString:" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            post.setEntity(new UrlEncodedFormEntity(listParams));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                kq = Helper.toString(is).trim();
                if (kq.startsWith("\""))
                    kq = kq.substring(1);
                if (kq.endsWith("\""))
                    kq = kq.substring(0, kq.length() - 1);

                Log.e("kq doStringPost", kq);

            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return kq;
    }

    public String doStringPOST(String wsUrl, String errorLog, List<NameValuePair> listParams) {
        String kq = "";
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("getWSAsString:" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            post.setEntity(new UrlEncodedFormEntity(listParams));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                kq = Helper.toString(is).trim();
                System.out.println("kq doStringPost:" + kq);
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return kq;
    }

    public void doPost(String wsUrl, String errorLog, List<NameValuePair> listParams) {
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        System.out.println("getWSAsString:" + wsUrl);
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            post.setEntity(new UrlEncodedFormEntity(listParams));
            client.execute(post);
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
    }

    public JSONObject doPostObjectPOST(String wsUrl, String errorLog, String strJason) {
        JSONObject ret = null;
        HttpClient client = new DefaultHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        try {

            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
            // Set HTTP parameters
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            StringEntity se = new StringEntity(strJason);
            post.setEntity(se);

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is).trim();
                System.out.println("chuoi str:" + str);
                ret = new JSONObject(str);
                is.close();
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return ret;
    }
    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public int doGetIntPOST_Metropole(String wsUrl, String errorLog, List<NameValuePair> nameValuePairs) {
        int ret = 0;
        HttpClient client = getNewHttpClient();
        wsUrl = wsUrl.replace(" ", "%20");
        try {
            HttpPost post = new HttpPost(wsUrl);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                String str = Helper.toString(is).trim();
                System.out.println("chuoi str:" + str);
                ret = Integer.parseInt(str);
                is.close();
            }
        } catch (Exception ex) {
            Log.e(TAG, errorLog);
            ex.printStackTrace();
        }
        return ret;
    }
//    public int uploadFile(String sourceFileUri) {
//           
//           
//          String fileName = sourceFileUri;
//  
//          HttpURLConnection conn = null;
//          DataOutputStream dos = null; 
//          String lineEnd = "\r\n";
//          String twoHyphens = "--";
//          String boundary = "*****";
//          int bytesRead, bytesAvailable, bufferSize;
//          byte[] buffer;
//          int maxBufferSize = 1 * 1024 * 1024;
//          File sourceFile = new File(sourceFileUri);
//           
//          if (!sourceFile.isFile()) {
//               Log.e("uploadFile", "Source File not exist :"+uploadFilePath );
//                
//               return 0;
//            
//          }
//          else
//          {
//               try {
//                    
//                     // open a URL connection to the Servlet
//                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
//                   URL url = new URL(upLoadServerUri);
//                    
//                   // Open a HTTP  connection to  the URL
//                   conn = (HttpURLConnection) url.openConnection();
//                   conn.setDoInput(true); // Allow Inputs
//                   conn.setDoOutput(true); // Allow Outputs
//                   conn.setUseCaches(false); // Don't use a Cached Copy
//                   conn.setRequestMethod("POST");
//                   conn.setRequestProperty("Connection", "Keep-Alive");
//                   conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//                   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                   conn.setRequestProperty("uploaded_file", fileName);
//                    
//                   dos = new DataOutputStream(conn.getOutputStream());
//          
//                   dos.writeBytes(twoHyphens + boundary + lineEnd);
//                   dos.writeBytes("Content-Disposition: form-data; name="uploaded_file";filename=""
//                                                                     + fileName + """ + lineEnd);
//                    
//                   dos.writeBytes(lineEnd);
//          
//                   // create a buffer of  maximum size
//                   bytesAvailable = fileInputStream.available();
//          
//                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                   buffer = new byte[bufferSize];
//          
//                   // read file and write it into form...
//                   bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
//                      
//                   while (bytesRead > 0) {
//                        
//                     dos.write(buffer, 0, bufferSize);
//                     bytesAvailable = fileInputStream.available();
//                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
//                      
//                    }
//          
//                   // send multipart form data necesssary after file data...
//                   dos.writeBytes(lineEnd);
//                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//          
//                   // Responses from the server (code and message)
//                   serverResponseCode = conn.getResponseCode();
//                   String serverResponseMessage = conn.getResponseMessage();
//                     
//                   Log.i("uploadFile", "HTTP Response is : "
//                                                   + serverResponseMessage + ": " + serverResponseCode);
//                    
//                   if(serverResponseCode == 200){
//                        
//                       runOnUiThread(new Runnable() {
//                            public void run() {
//                                 
//                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
//                                              +" http://www.androidexample.com/media/uploads/"
//                                              +uploadFileName;
//                                 
//                                messageText.setText(msg);
//                                Toast.makeText(UploadToServer.this, "File Upload Complete.",
//                                                                                 Toast.LENGTH_SHORT).show();
//                            }
//                        });               
//                   }   
//                    
//                   //close the streams //
//                   fileInputStream.close();
//                   dos.flush();
//                   dos.close();
//                     
//              } catch (MalformedURLException ex) {
//                   
//                  dialog.dismiss(); 
//                  ex.printStackTrace();
//                   
//                  runOnUiThread(new Runnable() {
//                      public void run() {
//                          messageText.setText("MalformedURLException Exception : check script url.");
//                          Toast.makeText(UploadToServer.this, "MalformedURLException",
//                                                                                              Toast.LENGTH_SHORT).show();
//                      }
//                  });
//                   
//                  Log.e("Upload file to server", "error: " + ex.getMessage(), ex); 
//              } catch (Exception e) {
//                   
//                  dialog.dismiss(); 
//                  e.printStackTrace();
//                   
//                  runOnUiThread(new Runnable() {
//                      public void run() {
//                          messageText.setText("Got Exception : see logcat ");
//                          Toast.makeText(UploadToServer.this, "Got Exception : see logcat ",
//                                                                  Toast.LENGTH_SHORT).show();
//                      }
//                  });
//                  Log.e("Upload file to server Exception", "Exception : "
//                                                                           + e.getMessage(), e); 
//              }
//              dialog.dismiss();      
//              return serverResponseCode;
//               
//           } // End else block
//         }
public String doUploadPOST(String wsUrl, String errorLog, MultipartEntity nameValuePairs) {
    String ret = "";
    HttpParams params = new BasicHttpParams();
    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
    HttpClient client = new DefaultHttpClient(params);
    System.out.println("WsURl:" + wsUrl);
    try {
        HttpPost post = new HttpPost(wsUrl);
//            post.setHeader("Authorization", Common.AUTHORIZE_STRING);
        post.setEntity(nameValuePairs);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream is = entity.getContent();
            String str = Helper.toString(is).trim();
//                ret = Long.parseLong(str);
            System.out.println(str);
           return str;

        }
        client.getConnectionManager().shutdown();
    } catch (Exception ex) {
        Log.e(TAG, errorLog);
        ex.printStackTrace();
    }
    return ret;
}
}
