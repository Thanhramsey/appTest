package com.vnpt.printproject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vnpt.staffhddtpos58.R;
import com.woosim.bt.WoosimPrinter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class WoosimPrinterActivity extends AppCompatActivity implements OnClickListener {
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	private BluetoothAdapter mBtAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	private String address;

	public static WoosimPrinter woosim;
	private Button btnOpen;
	private Button btnClose;
	private Button btnPrint2;
	private Button btnPrint3;
	private Button btnMSR23;
	private Button btnMSR123;
	private Button btnCardCancel;
	private Button btnFinish;
	private CheckBox cheProtocol;
	private EditText editTrack1;
	private EditText editTrack2;
	private EditText editTrack3;


//	public final static String EUC_KR = "EUC-KR";
/*	private final static String ISO_8859_1 = "ISO-8859-1"; 
	private final static String US_ASCII = "US-ASCII"; 
	private final static String UTF_16 = "UTF-16"; 
	private final static String UTF_16BE = "UTF-16BE";
	private final static String UTF_16LE = "UTF-16LE"; */
	private final static String UTF_8 = "UTF-8";


	//card data buffer
	public static byte[] cardData;
	public static byte[] extractdata = new byte[300];


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		woosim = new WoosimPrinter();
		woosim.setHandle(acthandler);
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);
		setResult(Activity.RESULT_CANCELED);

		Button scanButton = (Button) findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doDiscovery();
				v.setVisibility(View.GONE);
			}
		});

		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

		// Find and set up the ListView for paired devices
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// Find and set up the ListView for newly discovered devices
		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		// Get the local Bluetooth adapter
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		} else {
			String noDevices = getResources().getText(R.string.none_paired).toString();
			mPairedDevicesArrayAdapter.add(noDevices);
		}
	}

	public Handler acthandler = new Handler(){

		public void handleMessage(Message msg){
			if(msg.what == 0x01){
				Log.e("+++Activity+++","******0x01");
				Object obj1 = msg.obj;
				cardData = (byte[]) obj1;
				ToastMessage();
			}else if(msg.what == 0x02){
				//ardData[msg.arg1] = (byte) msg.arg2;
				Log.e("+++Activity+++","MSRFAIL: [" + msg.arg1+"]: ");
			}else if(msg.what == 0x03){
				Log.e("+++Activity+++","******EOT");
			}else if(msg.what == 0x04){
				Log.e("+++Activity+++","******ETX");
			}else if(msg.what == 0x05){
				Log.e("+++Activity+++","******NACK");
			}
		}
	};

	private void ToastMessage(){

		byte[] track1Data = new byte[76];
		byte[] track2Data = new byte[37];
		byte[] track3Data = new byte[104];

		int dataLength = woosim.extractCardData(cardData, extractdata);
		int i = 0, j = 0, k = 0;
		if(dataLength == 76){
			String str = new String(extractdata);
			editTrack1.setText(str);
			editTrack2.setText("No Data");
			editTrack3.setText("No Data");
		}
		else if(dataLength == 37){
			String str = new String(extractdata);
			editTrack1.setText("No Data");
			editTrack2.setText(str);
			editTrack3.setText("No Data");
		}
		else if(dataLength == 104){
			String str = new String(extractdata);
			editTrack1.setText("No Data");
			editTrack2.setText("No Data");
			editTrack3.setText(str);
		}
		//1,2track
		else if(dataLength == 113){
			Log.e("+++Activitiy+++","dataLength: " + dataLength);
			for(i = 0; i < 113; i++){
				if(i < 76){
					track1Data[i] = extractdata[i];
				}else{
					track2Data[j++] = extractdata[i];
				}
			}

			String str1 = new String(track1Data);
			String str2 = new String(track2Data);
			String str3 = "No Data";

			editTrack1.setText(str1);
			editTrack2.setText(str2);
			editTrack3.setText(str3);
		}
		//1,3track
		else if(dataLength == 180){
			for(i = 0; i < 180; i++){
				if(i < 76){
					track1Data[i] = extractdata[i];
				}else{
					track3Data[j++] = extractdata[i];
				}
			}

			String str1 = new String(track1Data);
			String str2 = "No Data";
			String str3 = new String(track3Data);


			editTrack1.setText(str1);
			editTrack2.setText(str2);
			editTrack3.setText(str3);
		}
		//2,3track
		else if(dataLength == 141){
			for(i = 0; i < 141; i++){
				if(i < 37){
					track2Data[i] = extractdata[i];
				}else{
					track3Data[j++] = extractdata[i];
				}
			}

			String str1 = "No Data";
			String str2 = new String(track2Data);
			String str3 = new String(track3Data);


			editTrack1.setText(str1);
			editTrack2.setText(str2);
			editTrack3.setText(str3);
		}
		//1,2,3track
		else if(dataLength == 217){
			for(i = 0; i < 217; i++){
				if(i < 76){
					track1Data[i] = extractdata[i];
				}else if( i >= 76 && i < 113){
					track2Data[j++] = extractdata[i];
				}else{
					track3Data[k++] = extractdata[i];
				}
			}

			String str1 = new String(track1Data);
			String str2 = new String(track2Data);
			String str3 = new String(track3Data);

			editTrack1.setText(str1);
			editTrack2.setText(str2);
			editTrack3.setText(str3);
		}

	}
	public void onClick(View v){

		if(v.getId() == R.id.btn_open){

			int reVal = woosim.BTConnection(address, cheProtocol.isChecked());
			if(reVal== 1){
				Toast t = Toast.makeText(this,"SUCCESS CONNECTION!", Toast.LENGTH_SHORT);
				t.show();
			}else if(reVal== -2){
				Toast t = Toast.makeText(this,"NOT CONNECTED", Toast.LENGTH_SHORT);
				t.show();
			}else if(reVal== -5){
				Toast t = Toast.makeText(this,"DEVICE IS NOT BONDED", Toast.LENGTH_SHORT);
				t.show();
			}else if(reVal== -6){
				Toast t = Toast.makeText(this,"ALREADY CONNECTED", Toast.LENGTH_SHORT);
				t.show();
			}else if(reVal== -8){
				Toast t = Toast.makeText(this,"Please enable your Bluetooth and re-run this program!", Toast.LENGTH_LONG);
				t.show();
			}else{
				Toast t = Toast.makeText(this,"ELSE", Toast.LENGTH_SHORT);
				t.show();
			}


		}

		if(v.getId() == R.id.btn_close){

			closeForm();

			editTrack1.setText("");
			editTrack2.setText("");
			editTrack3.setText("");


		}

		if(v.getId() == R.id.btn_print2inch){

			Print_2inch();
			editTrack1.setText("");
			editTrack2.setText("");
			editTrack3.setText("");

		}

		if(v.getId() == R.id.btn_print3inch){

			Print_3inch();
			editTrack1.setText("");
			editTrack2.setText("");
			editTrack3.setText("");

		}


		if(v.getId() == R.id.btn_msr23){
			editTrack1.setText("");
			editTrack2.setText("");
			editTrack3.setText("");

			byte[] track23 = {0x1b,0x4d,0x45};
			woosim.controlCommand(track23, track23.length);
			woosim.printSpool(true);
		}


		if(v.getId() == R.id.btn_msr123){
			editTrack1.setText("");
			editTrack2.setText("");
			editTrack3.setText("");

			byte[] track123 = {0x1b,0x4d,0x46};
			woosim.controlCommand(track123, track123.length);
			woosim.printSpool(true);
		}

		if(v.getId()== R.id.btn_cardcancel){
			editTrack1.setText("");
			editTrack2.setText("");
			editTrack3.setText("");

			woosim.cardCancel();
		}


		if(v.getId() == R.id.btn_finish){

			woosim.closeConnection();

			finish();
		}
	}
	private void closeForm(){
		woosim.closeConnection();
		Toast t = Toast.makeText(this, "CLOSE", Toast.LENGTH_SHORT);
		t.show();
	}

//	private void Print_2inch(){
//
//		byte[] init = {0x1b,'@'};
//		woosim.controlCommand(init, init.length);
//
//		String regData = "";
//
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		Date dt = new Date();
//		regData = dateFormat.format(dt);
//
//		woosim.saveSpool(EUC_KR, " Sales Receipt\r\n\r\n\r\n", 0x11, true);
//
//		woosim.saveSpool(EUC_KR, "MERCHANT NAME     woosim coffee\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, "MASTER            Gil-dong Hong\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, "ADDRESS   #501, Daerung Techno\r\n          town3rd 448,Gasan-dong\r\n          Gumcheon-gu, Seoul\r\n          Korea\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, "HELP DESK      (+82-2)2107-3721\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, ""+regData+"\r\n", 0, false);
//
//		woosim.saveSpool(EUC_KR, "--------------------------------\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, "Product       Sale       Price\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, "--------------------------------\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, "Cafe mocha      2         7.5$\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, "Cafe latte      1         7.0$\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, "Cappuccino      1         7.5$\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, "--------------------------------\r\n", 0, false);
//		woosim.saveSpool(EUC_KR, "Total                    29.5$\r\n", 0, false);
//
//		woosim.saveSpool(EUC_KR, "--------------------------------\r\n", 0, false);
//		if(cardData != null){
//			byte[] card = {extractdata[0],extractdata[1],extractdata[2],extractdata[3],'-',extractdata[4],extractdata[5],extractdata[6],extractdata[7],'-','*','*','*','*','-','*','*','*','*',0x0a};
//
//			woosim.saveSpool(EUC_KR, "Credits                  29.5$\r\n", 0, false);
//			woosim.saveSpool(EUC_KR, "Credit no.\r\n", 0, false);
//			woosim.controlCommand(card,card.length);
//
//
//		}else{
//			woosim.saveSpool(EUC_KR, "Cash                     29.5$\r\n", 0, false);
//		}
//
//		byte[] lf = {0x0a};
//		woosim.controlCommand(lf, lf.length);
//		woosim.saveSpool(EUC_KR, "Thank you!\r\n\r\n\r\n", 0, true);
//
//		try {
//			woosim.printBitmap("/sdcard/images/woosim.bmp");
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		woosim.controlCommand(lf, lf.length);
//		woosim.controlCommand(lf, lf.length);
//		byte[] ff ={0x0c};
//		woosim.controlCommand(ff, 1);
//		woosim.printSpool(true);
//		cardData = null;
//	}
	private void Print_2inch(){

		byte[] init = {0x1b,'@'};
		woosim.controlCommand(init, init.length);
//		try {
//			woosim.printBitmap("/sdcard/images/woosim.bmp");
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String regData = "";

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy mm:hh");
		Date dt = new Date();
		regData = dateFormat.format(dt);

		woosim.saveSpool(UTF_8, "       HÓA ĐƠN VIỄN THÔNG\r\n\r\n\r\n", 0, true);

		woosim.saveSpool(UTF_8, "VNPT SOFTWARE ĐÀ NẴNG\r\n", 0, false);
		woosim.saveSpool(UTF_8, "346 ĐƯỜNG 2/9,Q.HẢI CHÂU,ĐÀ NẴNG\r\n", 0, false);
		woosim.saveSpool(UTF_8, "ĐIỆN THOẠI: 0121.6644955\r\n", 0, false);
		woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
		woosim.saveSpool(UTF_8, "KH:               LÊ TẤN TRƯƠNG\r\n", 0, false);
		woosim.saveSpool(UTF_8, "SỐ HĐ:            DNG-06-000123\r\n", 0, false);
		woosim.saveSpool(UTF_8, "T.GIAN:         "+regData+"\r\n", 0, false);
		byte[] lf = {0x0a};
		woosim.controlCommand(lf, lf.length);

		woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
		woosim.saveSpool(UTF_8, "D.VỤ S.DỤNG    S.LƯỢNG     GIÁ\r\n", 0, false);
		woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
		woosim.saveSpool(UTF_8, "CƯỚC INTERNET		\r\n", 0, false);
		woosim.saveSpool(UTF_8,	"                   1      99.000\r\n", 0, false);
		woosim.saveSpool(UTF_8, "CƯỚC VIỄN THÔNG CNTT T03/2016\r\n", 0, false);
		woosim.saveSpool(UTF_8, "                   1     165.000\r\n", 0, false);
		woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
		woosim.saveSpool(UTF_8, "TỔNG CƯỚC:              254.000\r\n", 0, false);
		woosim.saveSpool(UTF_8, "THUẾ (VAT):              25.400\r\n", 0, false);
		woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
		woosim.saveSpool(UTF_8, "TỔNG CỘNG:              279.400\r\n", 0, false);


		if(cardData != null){
			byte[] card = {extractdata[0],extractdata[1],extractdata[2],extractdata[3],'-',extractdata[4],extractdata[5],extractdata[6],extractdata[7],'-','*','*','*','*','-','*','*','*','*',0x0a};

			woosim.saveSpool(UTF_8, "Credits                  29.5$\r\n", 0, false);
			woosim.saveSpool(UTF_8, "Credit no.\r\n", 0, false);
			woosim.controlCommand(card,card.length);


		}else{
			byte[] lf2 = {0x0a};
			woosim.controlCommand(lf2, lf2.length);
			woosim.saveSpool(UTF_8, "TIỀN MẶT:           279.400 VNĐ\r\n", 0, false);
		}

		byte[] lf3 = {0x0a};
		woosim.controlCommand(lf3, lf3.length);
		woosim.saveSpool(UTF_8, "CẢM ƠN QUÝ KHÁCH!\r\n\r\n\r\n", 0, true);

//		try {
//			woosim.printBitmap("/sdcard/images/woosim.bmp");
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		woosim.controlCommand(lf, lf.length);
		woosim.controlCommand(lf, lf.length);
//		byte[] ff ={0x0c};
//		woosim.controlCommand(ff, 1);
		woosim.printSpool(true);
		cardData = null;
	}
	private void Print_3inch(){

		byte[] init = {0x1b,'@'};
		woosim.controlCommand(init, init.length);

		String regData = "";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = new Date();
		regData = dateFormat.format(dt);

		woosim.saveSpool(UTF_8, "      Hóa đơn viễn thông\r\n\r\n\r\n", 0x11, true);

		woosim.saveSpool(UTF_8, " MERCHANT NAME                   woosim coffee\r\n", 0, false);
		woosim.saveSpool(UTF_8, " MASTER                          Gil-dong Hong\r\n", 0, false);
		woosim.saveSpool(UTF_8, " Địa chỉ: 246 đường 2/9, Q. Hải Châu,\r\n             Đà Nẵng, Việt Nam\r\n", 0, false);
		woosim.saveSpool(UTF_8, " Điện thoại      0121 6644955\r\n", 0, false);
		woosim.saveSpool(UTF_8, " "+regData+"\r\n", 0, false);

		woosim.saveSpool(UTF_8, "-----------------------------------------------\r\n", 0, false);
		woosim.saveSpool(UTF_8, " Dịch vụ sử dụng    S.Lượng           Giá\r\n", 0, false);
		woosim.saveSpool(UTF_8, "-----------------------------------------------\r\n", 0, false);
		woosim.saveSpool(UTF_8, " Cước Internet        1         99.000 VND\r\n", 0, false);
		woosim.saveSpool(UTF_8, " Cước viễn thông CNTT Tháng 03/2016      \r\n", 0, false);
		woosim.saveSpool(UTF_8, "           			1        160.000 VND\r\n", 0, false);
		woosim.saveSpool(UTF_8, "-----------------------------------------------\r\n", 0, false);
		woosim.saveSpool(UTF_8, " Tổng cộng:                    259.000 VND\r\n", 0, false);

		woosim.saveSpool(UTF_8, "-----------------------------------------------\r\n", 0, false);
		if(cardData != null){
			byte[] card = {extractdata[0],extractdata[1],extractdata[2],extractdata[3],'-',extractdata[4],extractdata[5],extractdata[6],extractdata[7],'-','*','*','*','*','-','*','*','*','*',0x0a};

			woosim.saveSpool(UTF_8, " Tiền mặt:                      259.000 VND\r\n", 0, false);
			woosim.saveSpool(UTF_8, " Tiền nợ: 0 VND", 0, false);
			woosim.controlCommand(card,card.length);


		}else{
			woosim.saveSpool(UTF_8, " Tiền mặt:                      259.000 VND\r\n", 0, false);
		}

		byte[] lf = {0x0a};
		woosim.controlCommand(lf, lf.length);
		woosim.saveSpool(UTF_8, " Cảm ơn!\r\n\r\n\r\n", 0, true);

//		try {
//			woosim.printBitmap("/sdcard/images/woosim.bmp");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		woosim.controlCommand(lf, lf.length);
//		woosim.controlCommand(lf, lf.length);
//		byte[] ff ={0x0c};
//		woosim.controlCommand(ff, 1);

		woosim.printSpool(true);
		cardData = null;
	}




	protected void onDestroy() {
		super.onDestroy();
		// Make sure we're not doing discovery anymore
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}
		// Unregister broadcast listeners
		this.unregisterReceiver(mReceiver);
	}

	private OnItemClickListener mDeviceClickListener =new OnItemClickListener(){
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			mBtAdapter.cancelDiscovery();
			// Get the device MAC address, which is the last 17 chars in the View
			String info = ((TextView) v).getText().toString();
			address = info.substring(info.length() - 17);
			// Create the result Intent and include the MAC address
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
			System.out.println("address" + address);
			setContentView(R.layout.woosim);
			createButton();
			// Set result and finish this Activity
			setResult(Activity.RESULT_OK, intent);
		}
	};


	private void createButton(){
		btnOpen = (Button)findViewById(R.id.btn_open);
		btnOpen.setOnClickListener(this);
		btnClose = (Button)findViewById(R.id.btn_close);
		btnClose.setOnClickListener(this);
		btnPrint2 = (Button)findViewById(R.id.btn_print2inch);
		btnPrint2.setOnClickListener(this);
		btnPrint3 = (Button)findViewById(R.id.btn_print3inch);
		btnPrint3.setOnClickListener(this);
		btnMSR23 = (Button)findViewById(R.id.btn_msr23);
		btnMSR23.setOnClickListener(this);
		btnMSR123 = (Button)findViewById(R.id.btn_msr123);
		btnMSR123.setOnClickListener(this);
		btnCardCancel = (Button)findViewById(R.id.btn_cardcancel);
		btnCardCancel.setOnClickListener(this);
		btnFinish = (Button)findViewById(R.id.btn_finish);
		btnFinish.setOnClickListener(this);
		cheProtocol = (CheckBox)findViewById(R.id.che_protocol);
		cheProtocol.setOnClickListener(this);
		editTrack1 = (EditText)findViewById(R.id.edit1);
		editTrack1.setOnClickListener(this);
		editTrack2 = (EditText)findViewById(R.id.edit2);
		editTrack2.setOnClickListener(this);
		editTrack3 = (EditText)findViewById(R.id.edit3);
		editTrack3.setOnClickListener(this);


	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle(R.string.select_device);
				if (mNewDevicesArrayAdapter.getCount() == 0) {
					String noDevices = getResources().getText(R.string.none_found).toString();
					mNewDevicesArrayAdapter.add(noDevices);
				}
			}	
		}		
	};


	/**
	 * Start device discover with the BluetoothAdapter
	 */
	private void doDiscovery() {
		// Indicate scanning in the title
		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.scanning);
		// Turn on sub-title for new devices
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
		// If we're already discovering, stop it
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		// Request discover from BluetoothAdapter
		mBtAdapter.startDiscovery();
	}

	public boolean onKeyDown(int keyConde, KeyEvent event){
		if((keyConde == KeyEvent.KEYCODE_BACK)){
//			woosim.closeConnection();
			finish();
		}
		return false;
	}

}



