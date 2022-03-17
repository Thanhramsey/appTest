package com.vnpt.webservice.SoapUntils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLParser {
	/**
	 *
	 * Kết quả trả về	Mô tả	Ghi chú
	 ERR:1	Tài khoản đăng nhập sai hoặc không có quyền thêm khách hàng	Kiểu string
	 ERR:2	Hóa đơn cần điều chỉnh không tồn tại
	 ERR:3	Dữ liệu xml đầu vào không đúng quy định
	 ERR:5	Không phát hành được hóa đơn
	 ERR:6	Dải hóa đơn cũ đã hết
	 ERR:7	User name không phù hợp, không tìm thấy company tương ứng cho user.	Kiểu string
	 ERR:8	Hóa đơn cần điều chỉnh đã bị thay thế. Không thể điều chỉnh được nữa.
	 ERR:9
	 Trạng thái hóa đơn không được điều chỉnh
	 OK: pattern; serial; invNumber
	 (Ví dụ:
	 OK:01GTKT3/001; AA/12E;0000002)	OK  đã phát hành hóa đơn thành công
	 Patter Mẫu số của hóa đơn điều chỉnh
	 Serial  serial của hóa đơn điều chỉnh
	 invNumber: số hóa đơn điều chỉnh
	 * */
	// constructor
	public XMLParser() {

	}

	/**
	 * Getting XML from URL making HTTP request
	 * @param url string
	 * */
	public String getXmlFromUrl(String url) {
		String xml = null;

		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			xml = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return XML
		return xml;
	}
	
	/**
	 * Getting XML DOM element
	 * @param XML string
	 * */
	public Document getDomElement(String xml){
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
		        is.setCharacterStream(new StringReader(xml));
		        doc = db.parse(is); 

			} catch (ParserConfigurationException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			} catch (SAXException e) {
				Log.e("Error: ", e.getMessage());
	            return null;
			} catch (IOException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			}

	        return doc;
	}
	
	/** Getting node value
	  * @param elem element
	  *//*
	 public final String getElementValue( Node elem ) {
	     Node child;
	     if( elem != null){
	         if (elem.hasChildNodes()){
	             for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
	                 if( child.getNodeType() == Node.TEXT_NODE  ){
	                     return child.getNodeValue();
	                 }
	             }
	         }
	     }
	     return "";
	 }*/
	 
	 /**
	  * Getting node value
	  * @param Element node
	  * @param key string
	  * */
	 /*public String getValue(Element item, String str) {
			NodeList n = item.getElementsByTagName(str);		
			return this.getElementValue(n.item(0));
	 }*/
	public String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}
	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return getElementValue(n.item(0));
	}

	public final String getElementValue(Node elem ) {
		try {
			Node child;
			if( elem != null){
				if (elem.hasChildNodes()){
					for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
						if( child.getNodeType() == Node.CDATA_SECTION_NODE
								|| child.getNodeType() == Node.TEXT_NODE )
						{
							return child.getNodeValue().trim();
						}
					}
				}
			}
			return "";
		} catch (DOMException e) {
			//Logger.logError(e);
			return "";
		}
	}
}
