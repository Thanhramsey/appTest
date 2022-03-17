package com.vnpt.printproject.er58ai;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vnpt.common.Common;
import com.vnpt.dto.BienLai;
import com.vnpt.dto.InvoiceHDDTDetails;
import com.vnpt.dto.ProductInvoiceDetails;
import com.vnpt.staffhddtpos58.R;
import com.er.bt.BluetoothService;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class BluetoothPrinterActivity extends Activity {
	Button btnScan;
	EditText txtSpace;
	//Button btnSendTxt;
	TextView logView;
	Button btnPrintTestPage;
	Button btnPrinterCheck;
	Button btnCutPaper;
	Button btnOpenCashdrawer;
	Button btnDownloadNVLogo;
	Button btnPrintLogo;
	Button btnDisconnect;
	Button btnPickImg;            // Browse picture/NV Logo
	Button btnPrintImg;           // Print picture
	Button btnPrintSample;
	Button btnPrint_1D;
	Button btnPrint_2D;
	String imgPath = "";          // bitmap full path
	Bitmap imgMap = null;         // buffer
	private int timeout=3;
	private Spinner barcode_list; //
	private Spinner TwoD_list;
	private int barcode_type = 8; // default option is code128
	private int TwoD_type = 0;    // default option is QRCode
	private static int QRCODE = 0;
	private static int PDF417 = 1;
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<String> adapter1;
	private static String[] arr_barcode = { "UPC-A","UPC-E","EAN13", "EAN8","Code39","ITF","Codabar","Code93","Code128" };
	private static String[] arr_2d = { "QRCode", "PDF417" };
	//View llay;
	private static final int REQUEST_EXTERNAL = 3;
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	public static BluetoothService mPOSPrinter = null;
	BluetoothDevice btDevice = null;
	InvoiceHDDTDetails invoiceHDDTDetails;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_printer_activity);
		//This is the first important step
		mPOSPrinter = new BluetoothService(this, mHandler);

		if( mPOSPrinter.isBTAvailable() == false ){
			Toast.makeText(this, "Bluetooth is inavailable", Toast.LENGTH_LONG).show();
			finish();
		}
		else  //Go to the initiate step if Bluetooth is available.
		{
			Bundle bundle = getIntent().getExtras();
			if (bundle != null)
				invoiceHDDTDetails = (InvoiceHDDTDetails) bundle.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
			initial();
		}
	}


	@Override
	public void onStart() {
		super.onStart();

		if( mPOSPrinter.isBTActivated() == false)  //Make sure Device Bluetooth is activated.
		{
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
		else
		{
			if(mPOSPrinter == null)
			{
				Bundle bundle = getIntent().getExtras();
				if (bundle != null)
					invoiceHDDTDetails = (InvoiceHDDTDetails) bundle.getSerializable(Common.KEY_DATA_ITEM_INVOICE);
				initial();
			}
		}
	}

	public void refreshLogView(String msg)
	{
		logView.append(msg);
	}

	public void initial() {
		try {
			//llay = (View)findViewById(R.id.llay);
			btnScan = (Button) this.findViewById(R.id.btn_connect_bluetooth_device);
			btnScan.setOnClickListener(new ClickEvent());
			/*txtSpace = (EditText) findViewById(R.id.txtContent);
			btnSendTxt = (Button) this.findViewById(R.id.btnSendTxt);
			btnSendTxt.setOnClickListener(new ClickEvent());*/

			logView=(TextView)this.findViewById(R.id.txtPrinerStatus);
			btnPrintTestPage = (Button) this.findViewById(R.id.btn_print);
			btnPrintTestPage.setOnClickListener(new ClickEvent());

			btnDisconnect = (Button) this.findViewById(R.id.btn_dis_connect_print);
			btnDisconnect.setOnClickListener(new ClickEvent());
			/*btnDownloadNVLogo = (Button) this.findViewById(R.id.btnDownloadNVLogo);
			btnDownloadNVLogo.setOnClickListener(new ClickEvent());
			btnPrintLogo = (Button) this.findViewById(R.id.btnPrintLogo);
			btnPrintLogo.setOnClickListener(new ClickEvent());
			btnPrinterCheck = (Button) this.findViewById(R.id.btnPrinterCheck);
			btnPrinterCheck.setOnClickListener(new ClickEvent());
			btnOpenCashdrawer = (Button) this.findViewById(R.id.btnOpenCashdrawer);
			btnOpenCashdrawer.setOnClickListener(new ClickEvent());
			btnCutPaper = (Button) this.findViewById(R.id.btnCutPaper);
			btnCutPaper.setOnClickListener(new ClickEvent());
			btnPrintSample = (Button) this.findViewById(R.id.btnPrintSample);
			btnPrintSample.setOnClickListener(new ClickEvent());
			*/
/*
			btnDisconnect.setEnabled(false);
			btnSendTxt.setEnabled(false);
			btnPrintTestPage.setEnabled(false);
*/
			/*btnPickImg = (Button) this.findViewById(R.id.browseImg);
			btnPickImg.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, REQUEST_EXTERNAL);
				}
			});
			btnPrintImg =(Button)this.findViewById(R.id.prtImg);
			btnPrintImg.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (imgMap==null ) {
						Toast.makeText(BluetoothDemo.this, "Please browse and pick an image first", Toast.LENGTH_SHORT).show();
						return;
					}
					mPOSPrinter.printBitmap(imgMap,(byte)mPOSPrinter.ALIGNMENT_CENTER);
				}
			});*/
//			llay.setVisibility(View.GONE);
		} catch (Exception ex) {
		Log.e("exception",ex.getMessage());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/*if (mPOSPrinter != null)
			mPOSPrinter.disconnect();*/
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public void onBackPressed() {
		/*if (mPOSPrinter != null) {
			mPOSPrinter.disconnect();
			mPOSPrinter = null;
			btnDisconnect.setEnabled(false);
//			btnSendTxt.setEnabled(false);
			btnPrintTestPage.setEnabled(false);
//			llay.setVisibility(View.GONE);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);*/
		super.onBackPressed();
	}

	class ClickEvent implements View.OnClickListener {
		public void onClick(View v)
		{
			if (v == btnScan) 
			{			
				Intent serverIntent = new Intent(BluetoothPrinterActivity.this,DeviceListActivity.class);
				startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
			}
			/*else if (v == btnSendTxt)
			{
				String msg = txtSpace.getText().toString();
				if (msg==null || "".equals(msg)) 
				{
					Toast.makeText(BluetoothPrinterActivity.this, getString(R.string.content_null), Toast.LENGTH_SHORT).show();
					txtSpace.requestFocus();
					return;
				}								
				
				if( msg.length() > 0 )
				{
					//check if bluetooth is connected or not.
					if (mPOSPrinter.getState() != mPOSPrinter.STATE_CONNECTED) {
						Toast.makeText(getApplicationContext(), "bluetooth is not connected yet", Toast.LENGTH_LONG).show();
						return;
					}				
					//check printer status before any operation.				
					
					int result;
					*//*
					result = mPOSPrinter.checkPrinter( );
					if(result == mPOSPrinter.MP_SUCCESS)
					{
						int status;
						String errMsg="";
						status = mPOSPrinter.getStatus();
						if((status&mPOSPrinter.STS_PAPER_MASK)==mPOSPrinter.STS_PAPER_EMPTY)
							errMsg += "\n\t * Paper is empty";
						
						if((status&mPOSPrinter.STS_COVER_MASK)==mPOSPrinter.STS_COVER_OPEN)
							errMsg += "\n\t * Cover is open";
						
						
						//if((status&mPOSPrinter.STS_BATTERY_MASK)==mPOSPrinter.STS_BATTERY_LOW)
						//	errMsg += "\n\t * Battery level is low";

						
						//usually we will not continue if paper is empty or cover is open.
						if(errMsg.length()>0)
						{
							Toast.makeText(BluetoothDemo.this, errMsg, Toast.LENGTH_LONG).show();
							return;		
						}
					}
					else if(result == mPOSPrinter.MP_NO_CONNECTION)
					{
						Toast.makeText(BluetoothDemo.this, "Bluetooth is not connected", Toast.LENGTH_LONG).show();
						return;						
					}
					else
					{					
						//connection might be lost due to printer is powered off suddenly or battery is running out
						Toast.makeText(BluetoothDemo.this, "checkPrinter() error, connection might be lost or paper is running out", Toast.LENGTH_LONG).show();
						return;
					}
					*//*
					
					//set encoding for datas that would be sent to printer
					mPOSPrinter.setEncoding("utf-16BE");
									
					//reset printer
					mPOSPrinter.reset();	
					
					//send the edited text contents to printer.
					mPOSPrinter.printString(msg);
					
					//feed paper for 2 lines( 1 line=3.75mm) to make sure receipt is exposed enough to tear off.
					mPOSPrinter.lineFeed(2);
*//*
					//check if the receipt is printed successfully.
					result = mPOSPrinter.checkPrintTaskDone(150);//150x100ms =15 seconds.
					if(result==mPOSPrinter.MP_SUCCESS)
						Toast.makeText(getApplicationContext(), "receipt is successfully printed", 2000).show();
					else if(result==mPOSPrinter.MP_FAIL)
						Toast.makeText(getApplicationContext(), "receipt is not printed completely, please check if paper is out\r", 2000).show();
					else if(result==mPOSPrinter.MP_ERROR)
						Toast.makeText(getApplicationContext(), "no reply from printer,check again\r", 2000).show();
					else if(result==mPOSPrinter.MP_OTHER_ERROR)
						Toast.makeText(getApplicationContext(), "reply is wrong, check again\r", 2000).show();
*//*
				}
			}*/
			else if( (v == btnPrintTestPage))
			{
				
				//printTestPage();
//				printInvoice(invoiceHDDTDetails);
				printInvoiceTest();
			}

			else if(v == btnPrinterCheck)
			{
				checkPrinter();
			}

			else if (v == btnDisconnect) 
			{						
				mPOSPrinter.disconnect();
			}
		}

		private void sleep(int i) {
			// TODO Auto-generated method stub			
		}
	}	

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:     
				Toast.makeText(getApplicationContext(), getString(R.string.success_connect),
						Toast.LENGTH_LONG).show();
				btnDisconnect.setEnabled(true);
				//btnSendTxt.setEnabled(true);
				btnPrintTestPage.setEnabled(true);
				//llay.setVisibility(View.VISIBLE);
				//mPOSPrinter.openDisconnectMonitoring(); //This will enable monitoring of disconnection event.
				break;
				case BluetoothService.STATE_CONNECTING:     
					break;
				case BluetoothService.STATE_LISTEN:         
				case BluetoothService.STATE_NONE:
					break;
				//case BluetoothService.STATE_ERROR:
				//	Toast.makeText(getApplicationContext(), "Error occured during read/write", 2000).show();
				//	break;
				}
				break;
			case BluetoothService.MESSAGE_CONNECTION_LOST:    
				Toast.makeText(getApplicationContext(), getString(R.string.connect_lost),
						Toast.LENGTH_LONG).show();
/*								
				btnDisconnect.setEnabled(false);
				btnSendTxt.setEnabled(false);
				btnPrintTestPage.setEnabled(false);
				llay.setVisibility(View.GONE);
*/
				break;
			case BluetoothService.MESSAGE_UNABLE_CONNECT:     
				Toast.makeText(getApplicationContext(), getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
				break;
			//not used.
			case BluetoothService.MESSAGE_READ:              
				int printStatus;
				printStatus=(byte) Integer.parseInt(msg.obj.toString());
				break;
			case BluetoothService.MESSAGE_ERROR:              
				Toast.makeText(getApplicationContext(), "Error occured during write", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	public Toast BluetoothDemo(ClickEvent clickEvent, String string, int i) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void checkPrinter()
	{
		//check printer status before any operation.				
		int result;
		result = mPOSPrinter.checkPrinter();
		if(result == mPOSPrinter.MP_SUCCESS)
		{
			int status;
			String errMsg="";
			status = mPOSPrinter.getStatus();
			if((status&mPOSPrinter.STS_PAPER_MASK)==mPOSPrinter.STS_PAPER_EMPTY)
				errMsg += "\n\t * Paper is empty";
			else
				errMsg += "\n\t * Paper is available";
			
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
			if(errMsg.length()>0)
			{
				//please consider the battery level status affection.
				Toast.makeText(BluetoothPrinterActivity.this, errMsg, Toast.LENGTH_LONG).show();
				return;		
			}
			//else
			//{
			//	Toast.makeText(BluetoothDemo.this, "\n\t * Printer is normal", Toast.LENGTH_LONG).show();
			//	return;						
			//}
		}
		else if(result == mPOSPrinter.MP_NO_CONNECTION)
		{
			Toast.makeText(getApplicationContext(), "Bluetooth is not connected", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(BluetoothPrinterActivity.this, "checkPrinter() error", Toast.LENGTH_LONG).show();
			return;
		}			
	}

    //This method shows you how send raw data(ESC/POS command) to printer directly.
	public void printTestPage()
	{
	    byte reset[]={0x1b,0x40};
	    byte lineFeed[]={0x00,0x0A};
	    // Command -- Font Size, Alignment
	    byte normalSize[] = {0x1D,0x21,0x00};
	    byte dWidthSize[] = {0x1D,0x21,0x10};
	    byte dHeightSize[] = {0x1D,0x21,0x01};
	    byte rightAlign[] = {0x1B,0x61,0x02};
	    byte centerAlign[] = {0x1B,0x61,0x01};
	    byte leftAlign[] = {0x1B,0x61,0x00};
	    
        //check if bluetooth is connected or not before any operation	    	    
		if (mPOSPrinter.getState() != mPOSPrinter.STATE_CONNECTED) {
			Toast.makeText(getApplicationContext(), "bluetooth is not connected yet", Toast.LENGTH_LONG).show();
			return;
		}
		

		mPOSPrinter.setEncoding("utf-16BE");
		// send reset command first in front of every receipt
	    mPOSPrinter.sendByte(reset);
	    
	    //receipt content
	    mPOSPrinter.printText("Receipt\r\n\r\n",(byte)mPOSPrinter.ALIGNMENT_CENTER,mPOSPrinter.FNT_DEFAULT,(byte)(mPOSPrinter.TXT_2WIDTH|mPOSPrinter.TXT_2HEIGHT));
	    mPOSPrinter.printText("TEL (123)-456-7890\r\n",(byte)mPOSPrinter.ALIGNMENT_RIGHT,mPOSPrinter.FNT_FONTB, (byte)mPOSPrinter.TXT_1WIDTH);
	    mPOSPrinter.printText("Thanks for coming to our shop!\r\n\r\n",(byte)mPOSPrinter.ALIGNMENT_CENTER,mPOSPrinter.FNT_BOLD,(byte)mPOSPrinter.TXT_1WIDTH);
	    mPOSPrinter.sendByte(centerAlign);
	    mPOSPrinter.printString("-------------------------------\r\n");
	    mPOSPrinter.printString("Chicken                  $10.00\r\n");
	    mPOSPrinter.printString("Hamburger                $20.00\r\n");
	    mPOSPrinter.printString("Pizza                    $30.00\r\n");
	    mPOSPrinter.printString("Lemons                   $40.00\r\n");
	    mPOSPrinter.printString("Drink                    $50.00\r\n");
	    mPOSPrinter.printString("-------------------------------\r\n");
	    mPOSPrinter.printString("Excluded tax            $150.00\r\n\r\n");
	    mPOSPrinter.printText("Tax(5%)         $7.50\r\n",(byte)mPOSPrinter.ALIGNMENT_RIGHT,(mPOSPrinter.FNT_FONTB|mPOSPrinter.FNT_UNDERLINE2),(byte)mPOSPrinter.TXT_1WIDTH);
	    mPOSPrinter.printText("Total           $157.50\r\n",(byte)mPOSPrinter.ALIGNMENT_RIGHT,(mPOSPrinter.FNT_FONTB|mPOSPrinter.FNT_UNDERLINE2),(byte)mPOSPrinter.TXT_1WIDTH);
	    mPOSPrinter.printText("Payment         $200.00\r\n",(byte)mPOSPrinter.ALIGNMENT_RIGHT,(mPOSPrinter.FNT_FONTB|mPOSPrinter.FNT_UNDERLINE2),(byte)mPOSPrinter.TXT_1WIDTH);
	    mPOSPrinter.printText("Change      $42.50\r\n\r\n",(byte)mPOSPrinter.ALIGNMENT_CENTER,mPOSPrinter.FNT_UNDERLINE2,(byte)(mPOSPrinter.TXT_1WIDTH|mPOSPrinter.TXT_2HEIGHT));	    	    
	    
	    String imgFilePath="";//this requests the full path of image
	    //String imgfile1=this.getClass().getClassLoader().getResource("goodwork.bmp").getPath();
		if(imgFilePath==null)
			Toast.makeText(getApplicationContext(), "no image was found", Toast.LENGTH_LONG).show();
		else
		{
		    //print image (bmp/jpg/png)
		    String imgSample="Image test-->\r\n";
		    mPOSPrinter.sendByte(leftAlign);
		    mPOSPrinter.printString(imgSample);
		
			mPOSPrinter.printBitmap(imgFilePath,(byte)mPOSPrinter.ALIGNMENT_CENTER);
		}
		//feed paper to make sure receipt is exposed enough to tear off
	    mPOSPrinter.lineFeed(3);	
	    
	}

	public static void printInvoice(InvoiceHDDTDetails invoiceHDDTDetails)
	{
		byte reset[]={0x1b,0x40};
		byte lineFeed[]={0x00,0x0A};
		// Command -- Font Size, Alignment
		byte normalSize[] = {0x1D,0x21,0x00};
		byte dWidthSize[] = {0x1D,0x21,0x10};
		byte dHeightSize[] = {0x1D,0x21,0x01};
		byte rightAlign[] = {0x1B,0x61,0x02};
		byte centerAlign[] = {0x1B,0x61,0x01};
		byte leftAlign[] = {0x1B,0x61,0x00};

		/*//check if bluetooth is connected or not before any operation
		if (mPOSPrinter.getState() != mPOSPrinter.STATE_CONNECTED) {
			Toast.makeText(getApplicationContext(), "bluetooth is not connected yet", Toast.LENGTH_LONG).show();
			return;
		}*/

		mPOSPrinter.setEncoding("utf-16BE");
		// send reset command first in front of every receipt
		mPOSPrinter.sendByte(reset);

		//receipt content getString(R.string.type_app_receipt) +
		mPOSPrinter.printText("BIÊN LAI ĐIỆN TỬ\r\n\r\n",(byte)mPOSPrinter.ALIGNMENT_CENTER,mPOSPrinter.FNT_DEFAULT,(byte)(mPOSPrinter.TXT_2WIDTH|mPOSPrinter.TXT_2HEIGHT));
		//mPOSPrinter.printText("TEL (123)-456-7890\r\n",(byte)mPOSPrinter.ALIGNMENT_RIGHT,mPOSPrinter.FNT_FONTB, (byte)mPOSPrinter.TXT_1WIDTH);
		//mPOSPrinter.printText("Thanks for coming to our shop!\r\n\r\n",(byte)mPOSPrinter.ALIGNMENT_CENTER,mPOSPrinter.FNT_BOLD,(byte)mPOSPrinter.TXT_1WIDTH);
		mPOSPrinter.printText(invoiceHDDTDetails.getCusName() + "\r\n",(byte)mPOSPrinter.ALIGNMENT_CENTER,mPOSPrinter.FNT_BOLD,(byte)mPOSPrinter.TXT_1WIDTH);
		mPOSPrinter.printText(invoiceHDDTDetails.getCusAddress() + "\r\n",(byte)mPOSPrinter.ALIGNMENT_CENTER,mPOSPrinter.FNT_BOLD,(byte)mPOSPrinter.TXT_1WIDTH);
		mPOSPrinter.sendByte(centerAlign);
		mPOSPrinter.printString("-------------------------------\r\n");
		ArrayList<ProductInvoiceDetails> products = invoiceHDDTDetails.getArrayListProduct();
		for (int i = 0; i < products.size(); i++) {
			mPOSPrinter.printString(products.get(i).getProdName() + " - " + products.get(i).getProdQuantity() + " - " + products.get(i).getAmount() + "\r\n");
		}
		mPOSPrinter.printString("-------------------------------\r\n");
		//feed paper to make sure receipt is exposed enough to tear off
		mPOSPrinter.lineFeed(3);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_ENABLE_BT:   
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(this, getString(R.string.connect_bluetooth_device_btn), Toast.LENGTH_LONG).show();
			} else {              
				finish();
			}
			break;
		case  REQUEST_CONNECT_DEVICE:   
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras()
						.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS); 
				//mPOSPrinter.connect(mPOSPrinter.PORT_Bluetooth,"BP80");//connect bluetooth by friendly name
				//mPOSPrinter.connect(mPOSPrinter.PORT_Bluetooth,"00:19:56:79:15:E3");//connect bluetooth by mac address
				mPOSPrinter.connect(mPOSPrinter.PORT_Bluetooth,address);
			}
			break;
		case  REQUEST_EXTERNAL:		
			if (resultCode == RESULT_OK	&& null != data) {
				Uri selectedImg = data.getData();
				ContentResolver cr = this.getContentResolver();
                //BitmapFactory.decodeFile
				try {
					imgMap = BitmapFactory.decodeStream(cr
						.openInputStream(selectedImg));
				} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			break;
		}
	}

	public InvoiceHDDTDetails getInvoiceHDDTDetails() {
		return invoiceHDDTDetails;
	}

	public void setInvoiceHDDTDetails(InvoiceHDDTDetails invoiceHDDTDetails) {
		this.invoiceHDDTDetails = invoiceHDDTDetails;
	}

	public static void printInvoiceTest() {
		BienLai bienLai = new BienLai();
		bienLai.setMoTa("Biên lai in thử");
		bienLai.setGiaTien(10000);
		bienLai.setMau("01BLP0-001");
		bienLai.setSo("0");
		bienLai.setKyHieu("DT-20E");

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

		mPOSPrinter.setEncoding("utf-16BE");
		// send reset command first in front of every receipt
		mPOSPrinter.sendByte(reset);

		//receipt content getString(R.string.type_app_receipt) +
		mPOSPrinter.printText("BIÊN LAI ĐIỆN TỬ\r\n\r\n", (byte) mPOSPrinter.ALIGNMENT_CENTER, mPOSPrinter.FNT_DEFAULT, (byte) (mPOSPrinter.TXT_2WIDTH | mPOSPrinter.TXT_2HEIGHT));
		//mPOSPrinter.printText("TEL (123)-456-7890\r\n",(byte)mPOSPrinter.ALIGNMENT_RIGHT,mPOSPrinter.FNT_FONTB, (byte)mPOSPrinter.TXT_1WIDTH);
		mPOSPrinter.printText("Phí: " + bienLai.getMoTa() + "\r\n", (byte) mPOSPrinter.ALIGNMENT_CENTER, mPOSPrinter.FNT_BOLD, (byte) mPOSPrinter.TXT_1WIDTH);
		mPOSPrinter.printText("Giá: " + bienLai.getGiaTien() + "\r\n", (byte) mPOSPrinter.ALIGNMENT_CENTER, mPOSPrinter.FNT_BOLD, (byte) mPOSPrinter.TXT_1WIDTH);
		mPOSPrinter.printText("Số BL: " + bienLai.getSo() + "\r\n", (byte) mPOSPrinter.ALIGNMENT_CENTER, mPOSPrinter.FNT_BOLD, (byte) mPOSPrinter.TXT_1WIDTH);
		mPOSPrinter.printText("Mẫu: " + bienLai.getMau() + "\r\n", (byte) mPOSPrinter.ALIGNMENT_CENTER, mPOSPrinter.FNT_BOLD, (byte) mPOSPrinter.TXT_1WIDTH);
		mPOSPrinter.printText("Ký hiệu: " + bienLai.getKyHieu() + "\r\n", (byte) mPOSPrinter.ALIGNMENT_CENTER, mPOSPrinter.FNT_BOLD, (byte) mPOSPrinter.TXT_1WIDTH);
		mPOSPrinter.sendByte(centerAlign);
		mPOSPrinter.printString("-------------------------------\r\n");

		//feed paper to make sure receipt is exposed enough to tear off
		mPOSPrinter.lineFeed(3);

	}
}
