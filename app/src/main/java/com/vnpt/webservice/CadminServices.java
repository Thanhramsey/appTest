package com.vnpt.webservice;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.vnpt.common.Common;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.dto.InvoiceHDDTDetails;
import com.vnpt.dto.InvoiceHDDTS;
import com.vnpt.dto.Outlet;
import com.vnpt.dto.ProductInvoiceDetails;
import com.vnpt.dto.User;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.webservice.SoapUntils.XMLParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;


public class CadminServices extends BaseSOAPWebservice {
	Context mcontext;

	public CadminServices(Context context)
	{
		this.mcontext = context;
//		Common.AUTHORIZE_STRING = Helper.getB64Auth();
	}
    public String login(String username, String password)
    {
//    	return getWS_getCus(username);
		String strServer = StoreSharePreferences.getInstance(mcontext).loadStringSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
        return getWS_HotelLogin(username,password, strServer);
    }
	public String getAllOutlet()
	{
		String strServer = StoreSharePreferences.getInstance(mcontext).loadStringSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
		return getWS_GetAllOutlet(strServer);
	}
    public String listInvoice(String strPattern, String strSerial, String strStatus, String strOutlet,String fromDate, String toDate, int start, int end)
    {
		String strToken=  StoreSharePreferences.getInstance(mcontext).loadStringSavedPreferences(Common.KEY_TOKEN_LOGIN);
        return getWS_ListInvoice(strToken,strPattern,  strSerial,  strStatus, strOutlet, fromDate,  toDate, start, end);
    }
    public String getInvoiceByFkey(String strFkey, String strCheckNo)
    {
		String strToken=  StoreSharePreferences.getInstance(mcontext).loadStringSavedPreferences(Common.KEY_TOKEN_LOGIN);
        return getWS_GetInvoiceByFkey(strToken,strFkey,  strCheckNo);
    }
    public String updateAndPublishInvByFkey(String strFkey, String strCheckNo,String strXmlInvData, String strPattern, String strSerial)
    {
        String strToken=  StoreSharePreferences.getInstance(mcontext).loadStringSavedPreferences(Common.KEY_TOKEN_LOGIN);
        return getWS_UpdateAndPublishInvByFkey(strToken, strFkey, strCheckNo, strXmlInvData,strPattern, strSerial);
    }
	public ArrayList<InvoiceHDDTS> parseDataListInvByCus(String strResult)
	{
		ArrayList<InvoiceHDDTS> arrayList = new ArrayList<>();
		XMLParser xmlParser = new XMLParser();
		Document doc = xmlParser.getDomElement(strResult);
		NodeList nodes = doc.getElementsByTagName("Data");
		for (int i = 0; i < nodes.getLength() ; i++) {
			Element element = (Element) nodes.item(i);
			NodeList childNodes = element.getChildNodes();
			for (int j = 0; j <childNodes.getLength() ; j++) {
				InvoiceHDDTS invoice = new InvoiceHDDTS();
				Element elementCode = (Element) childNodes.item(j);

				try {//<index>5</index>
					String index = elementCode.getElementsByTagName("index").item(0).getTextContent();
					invoice.setIndex(Integer.parseInt(index));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<status></status>
					String status = elementCode.getElementsByTagName("status").item(0).getTextContent();
					invoice.setStatus(Integer.parseInt(status));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<cusCode></cusCode>
					String cusCode = elementCode.getElementsByTagName("cusCode").item(0).getTextContent();
					invoice.setCusCode(cusCode);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<name></name>
					String name = elementCode.getElementsByTagName("name").item(0).getTextContent();
					invoice.setName(name);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<publishDate></publishDate>
					String publishDate = elementCode.getElementsByTagName("publishDate").item(0).getTextContent();
					invoice.setPublishDate(publishDate);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<signStatus></signStatus>
					String signStatus = elementCode.getElementsByTagName("signStatus").item(0).getTextContent();
					invoice.setSignStatus(Integer.parseInt(signStatus));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<pattern></pattern>
					String pattern = elementCode.getElementsByTagName("pattern").item(0).getTextContent();
					invoice.setPattern(pattern);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<serial></serial>
					String serial = elementCode.getElementsByTagName("serial").item(0).getTextContent();
					invoice.setSerial(serial);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<invNum></invNum>
					String invNum = elementCode.getElementsByTagName("invNum").item(0).getTextContent();
					invoice.setInvNum(invNum);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<cusname></cusname>
					String cusname = elementCode.getElementsByTagName("cusname").item(0).getTextContent();
					invoice.setCusname(cusname);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<payment></payment>
					String payment = elementCode.getElementsByTagName("payment").item(0).getTextContent();
					invoice.setPayment(Integer.parseInt(payment));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<converted></converted>
					String converted = elementCode.getElementsByTagName("converted").item(0).getTextContent();
					invoice.setConverted(Integer.parseInt(converted));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<month></month>
					String month = elementCode.getElementsByTagName("month").item(0).getTextContent();
					invoice.setMonth(Integer.parseInt(month));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<amount></amount>
					String amount = elementCode.getElementsByTagName("amount").item(0).getTextContent();
					invoice.setAmount(Double.parseDouble(amount));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				invoice.setTokenInv(invoice.getPattern()+";"+ invoice.getSerial()+";"+invoice.getInvNum());
				arrayList.add(invoice);
			}

		}
		return arrayList;
	}

	public void parseXMLGesCus(String strResult)
	{
		User user = new User();

		XMLParser xmlParser = new XMLParser();
		Document doc = xmlParser.getDomElement(strResult);
		NodeList nodes = doc.getElementsByTagName("Data");
		Element element = (Element) nodes.item(0);
		NodeList childNodes = element.getChildNodes();
		/** childNodes = 6
		 * <![CDATA[<Data>
		 <code>TRUONGLT</code>
		 <name><![CDATA[Lê Tấn Trương]]]]>><![CDATA[</name>
		 <address><![CDATA[Đà Nẵng]]]]>><![CDATA[</address>
		 <phone></phone>
		 <taxcode></taxcode>
		 <email>lttruong@vnpt.vn</email>
		 </Data>]]>
		 * */
		try {
			Element elementCode = (Element) childNodes.item(0);
			String code = xmlParser.getCharacterDataFromElement(elementCode);
			user.setMa_kh(code);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try {
			Element elementName = (Element) childNodes.item(1);
			String name = xmlParser.getCharacterDataFromElement(elementName);
			user.setName(name);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try {
			Element elementAddress = (Element) childNodes.item(2);
			String address = xmlParser.getCharacterDataFromElement(elementAddress);
			user.setAddress(address);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try {
			Element elementphone = (Element) childNodes.item(3);
			String phone = xmlParser.getCharacterDataFromElement(elementphone);
			user.setPhone(phone);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try {
			Element elementtaxcode = (Element) childNodes.item(4);
			String taxcode = xmlParser.getCharacterDataFromElement(elementtaxcode);
			user.setTaxcode(taxcode);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try {
			Element elementemail = (Element) childNodes.item(5);
			String email = xmlParser.getCharacterDataFromElement(elementemail);
			user.setEmail(email);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		Common.userInfo = user;
	}

	public InvoiceHDDTDetails parseDataXMLFromInvoiceTrG(String strResult) {
		InvoiceHDDTDetails invoiceHDDTDetails = new InvoiceHDDTDetails();
//		String xmlString;  // some XML String previously created
//		XmlToJson xmlToJson = new XmlToJson.Builder(strResult).build();
		String xmlRecords = "<data><terminal_id>1000099999</terminal_id><merchant_id>10004444</merchant_id><merchant_info>Mc Donald's - Abdoun</merchant_info></data>";



		Log.d("XML1-", strResult);
		Gson gson = new Gson();
		JSONObject jsonObj = null;
		try {
			jsonObj = XML.toJSONObject(strResult);
//			JSONObject jsInv = jsonObj.getJSONObject("Inv");
			JSONObject jsInvoice = jsonObj.getJSONObject("Invoice");
			JSONObject jsInvoiceContent = jsInvoice.getJSONObject("Content");
//			String keyInvoice = jsInvoice.getString("key");
			try {
				if (jsonObj != null) {
					String test = jsInvoiceContent.toString();
					Log.e("hsContent",test);
					invoiceHDDTDetails = gson.fromJson(jsInvoiceContent.toString(), InvoiceHDDTDetails.class);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				JSONObject jsonObject = jsInvoiceContent.getJSONObject("Products");
				JSONArray jsArr = jsonObject.getJSONArray("Product");
				ArrayList<ProductInvoiceDetails> arrProductInvoiceDetails = new ArrayList<>();
				for (int i = 0; i < jsArr.length(); i++) {
					ProductInvoiceDetails item = gson.fromJson(jsArr.get(i).toString(), ProductInvoiceDetails.class);
					arrProductInvoiceDetails.add(item);
				}
				invoiceHDDTDetails.setArrayListProduct(arrProductInvoiceDetails);
			} catch (Exception ex) {
				ex.printStackTrace();
				JSONObject jsArr = jsInvoiceContent.getJSONObject("Products");
				JSONObject jsonObject = jsArr.getJSONObject("Product");
				ArrayList<ProductInvoiceDetails> arrProductInvoiceDetails = new ArrayList<>();
				ProductInvoiceDetails item = gson.fromJson(jsonObject.toString(), ProductInvoiceDetails.class);
				arrProductInvoiceDetails.add(item);
				invoiceHDDTDetails.setArrayListProduct(arrProductInvoiceDetails);

			}

		} catch (JSONException e) {
			Log.e("JSON exception", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("JSON exception", e.getMessage());
			e.printStackTrace();
		}
		Log.d("XML1-", jsonObj.toString());
		Log.d("invoiceHDDTDetails-", invoiceHDDTDetails.toString());
		return invoiceHDDTDetails;
	}
	public List<InvoiceCadmin> parseDataListInv(String strResult) {
		Log.d("XML1-", strResult);
		Gson gson = new Gson();
		JSONObject jsonObj = null;
		try {
			jsonObj = XML.toJSONObject(strResult);
			JSONObject jsInvoice = jsonObj.getJSONObject("Data");
			JSONArray jsContent = jsInvoice.getJSONArray("Item");
			try {
                ArrayList<InvoiceCadmin> arrInvoiceCadmin = new ArrayList<>();
                for (int i = 0; i < jsContent.length(); i++) {
                    InvoiceCadmin item = gson.fromJson(jsContent.get(i).toString(), InvoiceCadmin.class);
                    arrInvoiceCadmin.add(item);
                }
                return arrInvoiceCadmin;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (JSONException e) {
			Log.e("JSON exception", e.getMessage());
			try
			{
				jsonObj = XML.toJSONObject(strResult);
				JSONObject jsInvoice = jsonObj.getJSONObject("Data");
				JSONObject jsContent = jsInvoice.getJSONObject("Item");
				try {
					ArrayList<InvoiceCadmin> arrInvoiceCadmin = new ArrayList<>();
					InvoiceCadmin item = gson.fromJson(jsContent.toString(), InvoiceCadmin.class);
					arrInvoiceCadmin.add(item);
					return arrInvoiceCadmin;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}catch (Exception ex)
			{

			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<Outlet> parseDataListOultet(String strResult) {
		Log.d("XML1-", strResult);
		Gson gson = new Gson();
		JSONObject jsonObj = null;
		try {
			jsonObj = XML.toJSONObject(strResult);
			JSONObject jsInvoice = jsonObj.getJSONObject("Data");
			JSONArray jsContent = jsInvoice.getJSONArray("Item");
			try {
				ArrayList<Outlet> arrInvoiceCadmin = new ArrayList<>();
				for (int i = 0; i < jsContent.length(); i++) {
					Outlet item = gson.fromJson(jsContent.get(i).toString(), Outlet.class);
					arrInvoiceCadmin.add(item);
				}
				return arrInvoiceCadmin;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (JSONException e) {
			Log.e("JSON exception", e.getMessage());
			try
            {
                jsonObj = XML.toJSONObject(strResult);
                JSONObject jsInvoice = jsonObj.getJSONObject("Data");
                JSONObject jsContent = jsInvoice.getJSONObject("Item");
                try {
                    ArrayList<Outlet> arrInvoiceCadmin = new ArrayList<>();
                    Outlet item = gson.fromJson(jsContent.toString(), Outlet.class);
                    arrInvoiceCadmin.add(item);
                    return arrInvoiceCadmin;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }catch (Exception ex)
            {

            }

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String parseDataGetStringDownloadHTMLInv(String strResult)
	{
		String strHTML = "";
		XMLParser xmlParser = new XMLParser();
		Document doc = xmlParser.getDomElement(strResult);
		NodeList nodes = doc.getChildNodes();
		Element element = (Element) nodes.item(0);
		strHTML = xmlParser.getCharacterDataFromElement(element);
		return strHTML;
	}
}
