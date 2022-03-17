package com.vnpt.printproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vnpt.dto.HoaDon;
import com.vnpt.dto.Invoice;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.printproject.woosim.BluetoothPrinterActivity;
import com.vnpt.staffhddtpos58.R;
import com.vnpt.utils.Helper;
import com.woosim.printer.WoosimCmd;
import com.woosim.printer.WoosimImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

//import printproject.com.model.SalesModel;
//import printproject.com.utility.Utility;

/**
 * This class is responsible to generate a static sales receipt and to print that receipt
 */
public class PrintReceipt {
    public final static String EUC_KR = "EUC-KR";
    private final static String UTF_8 = "UTF-8";

    public static boolean printBillFromOrder(Context context) {
        if (BluetoothPrinterActivity.BLUETOOTH_PRINTER.IsNoConnection()) {
            return false;
        }

//		double totalBill=0.00, netBill=0.00, totalVat=0.00;
//
//		//LF = Line feed
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.Begin();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);	//30 * 0.125mm
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Company Name");
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);
//
//        //BT_Write() method will initiate the printer to start printing.
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Branch Name: " + "Stuttgart Branch" +
//				"\nOrder No: " + "1245784256454" +
//				"\nBill No: " + "554741254854" +
//				"\nTrn. Date:" + "29/12/2015" +
//				"\nSalesman:" + "Mr. Salesman");
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//LEFT
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);	//50 * 0.125mm
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font
//
//		//static sales record are generated
//		SalesModel.generatedMoneyReceipt();
//
//		for(int i=0;i<StaticValue.arrayListSalesModel.size();i++){
//			SalesModel salesModel = StaticValue.arrayListSalesModel.get(i);
//			BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(salesModel.getProductShortName());
//			BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//			BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(" " + salesModel.getSalesAmount() + "x" + salesModel.getUnitSalesCost() +
//					"=" + Utility.doubleFormatter(salesModel.getSalesAmount() * salesModel.getUnitSalesCost()) + "" + StaticValue.CURRENCY);
//			BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//
//			totalBill=totalBill + (salesModel.getUnitSalesCost() * salesModel.getSalesAmount());
//		}
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
//
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 2);//RIGHT
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);	//50 * 0.125mm
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte)0x00);//normal font
//
//		totalVat=Double.parseDouble(Utility.doubleFormatter(totalBill*(StaticValue.VAT/100)));
//		netBill=totalBill+totalVat;
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Total Bill:" + Utility.doubleFormatter(totalBill) + "" + StaticValue.CURRENCY);
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(Double.toString(StaticValue.VAT) + "% VAT:" + Utility.doubleFormatter(totalVat) + "" +
//				StaticValue.CURRENCY);
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//center
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
//
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 2);//Right
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x9);
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Net Bill:" + Utility.doubleFormatter(netBill) + "" + StaticValue.CURRENCY);
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//center
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//left
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("VAT Reg. No:" + StaticValue.VAT_REGISTRATION_NUMBER);
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//left
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(StaticValue.BRANCH_ADDRESS);
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte)1);//Center
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("\n\nThank You\nPOWERED By SIAS ERP");
//
//
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//		BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        return true;
    }

    //	private void Print_2inch(){
//
//		byte[] init = {0x1b,'@'};
//		WoosimPrinterActivity.woosim.controlCommand(init, init.length);
//
//		String regData = "";
//
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		Date dt = new Date();
//		regData = dateFormat.format(dt);
//
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, " Sales Receipt\r\n\r\n\r\n", 0x11, true);
//
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "MERCHANT NAME     woosim coffee\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "MASTER            Gil-dong Hong\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "ADDRESS   #501, Daerung Techno\r\n          town3rd 448,Gasan-dong\r\n          Gumcheon-gu, Seoul\r\n          Korea\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "HELP DESK      (+82-2)2107-3721\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, ""+regData+"\r\n", 0, false);
//
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "--------------------------------\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "Product       Sale       Price\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "--------------------------------\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "Cafe mocha      2         7.5$\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "Cafe latte      1         7.0$\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "Cappuccino      1         7.5$\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "--------------------------------\r\n", 0, false);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "Total                    29.5$\r\n", 0, false);
//
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "--------------------------------\r\n", 0, false);
//		if(WoosimPrinterActivity.cardData != null){
//			byte[] card = {WoosimPrinterActivity.extractdata[0],WoosimPrinterActivity.extractdata[1],WoosimPrinterActivity.extractdata[2],WoosimPrinterActivity.extractdata[3],'-',WoosimPrinterActivity.extractdata[4],WoosimPrinterActivity.extractdata[5],WoosimPrinterActivity.extractdata[6],WoosimPrinterActivity.extractdata[7],'-','*','*','*','*','-','*','*','*','*',0x0a};
//
//			WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "Credits                  29.5$\r\n", 0, false);
//			WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "Credit no.\r\n", 0, false);
//			WoosimPrinterActivity.woosim.controlCommand(card,card.length);
//
//
//		}else{
//			WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "Cash                     29.5$\r\n", 0, false);
//		}
//
//		byte[] lf = {0x0a};
//		WoosimPrinterActivity.woosim.controlCommand(lf, lf.length);
//		WoosimPrinterActivity.woosim.saveSpool(EUC_KR, "Thank you!\r\n\r\n\r\n", 0, true);
//
////		try {
////			woosim.printBitmap("/sdcard/images/woosim.bmp");
////
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		WoosimPrinterActivity.woosim.controlCommand(lf, lf.length);
//		WoosimPrinterActivity.woosim.controlCommand(lf, lf.length);
//		byte[] ff ={0x0c};
//		WoosimPrinterActivity.woosim.controlCommand(ff, 1);
//		WoosimPrinterActivity.woosim.printSpool(true);
//		WoosimPrinterActivity.cardData = null;
//	}
    public static void printReceiptDemo_2inch() {
        byte[] init = {0x1b, '@'};
        WoosimPrinterActivity.woosim.controlCommand(init, init.length);

        String regData = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date dt = new Date();
        regData = dateFormat.format(dt);

        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "       HÓA ĐƠN VIỄN THÔNG\r\n\r\n\r\n", 0, true);

        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "VNPT SOFTWARE ĐÀ NẴNG\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "346 ĐƯỜNG 2/9,Q.HẢI CHÂU,ĐÀ NẴNG\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "ĐIỆN THOẠI: 0121.6644955\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "KH:               LÊ TẤN TRƯƠNG\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "SỐ HĐ:            DNG-06-000123\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "T.GIAN:         " + regData + "\r\n", 0, false);
        byte[] lf = {0x0a};
        WoosimPrinterActivity.woosim.controlCommand(lf, lf.length);

        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "D.VỤ S.DỤNG    S.LƯỢNG     GIÁ\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "CƯỚC INTERNET		\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "                   1      99.000\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "CƯỚC VIỄN THÔNG CNTT T03/2016\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "                   1     165.000\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "TỔNG CƯỚC:              254.000\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "THUẾ (VAT):              25.400\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "TỔNG CỘNG:              279.400\r\n", 0, false);


        if (WoosimPrinterActivity.cardData != null) {
//			byte[] card = {WoosimPrinterActivity.extractdata[0],extractdata[1],extractdata[2],extractdata[3],'-',extractdata[4],extractdata[5],extractdata[6],extractdata[7],'-','*','*','*','*','-','*','*','*','*',0x0a};
//
//			WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Credits                  29.5$\r\n", 0, false);
//			WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Credit no.\r\n", 0, false);
//			WoosimPrinterActivity.woosim.controlCommand(card,card.length);


        } else {
            byte[] lf2 = {0x0a};
            WoosimPrinterActivity.woosim.controlCommand(lf2, lf2.length);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "TIỀN MẶT:           279.400 VNĐ\r\n", 0, false);
        }

        byte[] lf3 = {0x0a};
        WoosimPrinterActivity.woosim.controlCommand(lf3, lf3.length);
        WoosimPrinterActivity.woosim.saveSpool(UTF_8, "CẢM ƠN QUÝ KHÁCH!\r\n\r\n\r\n", 0, true);

//		try {
//			woosim.printBitmap("/sdcard/images/woosim.bmp");
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		woosim.controlCommand(lf, lf.length);
        WoosimPrinterActivity.woosim.controlCommand(lf, lf.length);
//		byte[] ff ={0x0c};
//		woosim.controlCommand(ff, 1);
        WoosimPrinterActivity.woosim.printSpool(true);
        WoosimPrinterActivity.cardData = null;
    }

    public static void printReceiptDemoVNPT_2inch(Context context, HoaDon mHoaDon) {
        byte[] init = {0x1b, '@'};
        if (WoosimPrinterActivity.woosim == null) {
            return;
        }
        WoosimPrinterActivity.woosim.controlCommand(init, init.length);
        byte[] lf = {0x0a};
        String regData = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date dt = new Date();
        regData = dateFormat.format(dt);
        if (mHoaDon.getStateDebit().equals("0")) {// nợ 1 tháng
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "       HÓA ĐƠN VIỄN THÔNG\r\n\r\n", 0, true);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "VNPT SOFTWARE ĐÀ NẴNG\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "346 ĐƯỜNG 2/9,Q.HẢI CHÂU,ĐÀ NẴNG\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Điện thoại:        0915.676.468\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
            int lenghtMCus = mHoaDon.getCodeCustomer().length();
            String strMKhBlank = "Mã KH:                          ";
            String strMKH = strMKhBlank.substring(0, strMKhBlank.length() - lenghtMCus);
            strMKH = strMKH + mHoaDon.getCodeCustomer();
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, strMKH + "\r\n", 0, false);

            int lenghtCus = mHoaDon.getNameCus().length();
            String strKhBlank = "KH:                             ";
            String strKH = strKhBlank.substring(0, strKhBlank.length() - lenghtCus);
            strKH = strKH + mHoaDon.getNameCus();
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, strKH + "\r\n", 0, false);

            int lenghtHD = mHoaDon.getNumberInvoice().length();
            String strHDBlank = "Số HĐ:                          ";
            String strHD = strHDBlank.substring(0, strHDBlank.length() - lenghtHD);
            strHD = strHD + mHoaDon.getNumberInvoice();

            WoosimPrinterActivity.woosim.saveSpool(UTF_8, strHD + "\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "T.gian:         " + regData + "\r\n", 0, false);
            byte[] lf4 = {0x0a};
            WoosimPrinterActivity.woosim.controlCommand(lf4, lf4.length);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "D.VỤ            S.LƯỢNG     GIÁ\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Cước MyTV		\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "                   1      35.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Cước Internet		\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "                   1      70.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Cước viễn thông CNTT T03/2016\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "                   1      20.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Tổng cước:              125.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Thuế (VAT):              12.500\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Tổng cộng:              137.500\r\n", 0, false);
            byte[] lf2 = {0x0a};
            WoosimPrinterActivity.woosim.controlCommand(lf2, lf2.length);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Thành tiền:          137.500 VNĐ\r\n", 0, false);
            byte[] lf3 = {0x0a};
            WoosimPrinterActivity.woosim.controlCommand(lf3, lf3.length);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Cảm ơn quý khách đã dùng dịch vụ của VPNT!\r\n\r\n\r\n", 0, true);
        } else// nợ nhiều tháng
        {
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "       HÓA ĐƠN VIỄN THÔNG\r\n\r\n", 0, true);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "VNPT SOFTWARE ĐÀ NẴNG\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "346 ĐƯỜNG 2/9,Q.HẢI CHÂU,ĐÀ NẴNG\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Điện thoại:        0915.676.468\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
            int lenghtMCus = mHoaDon.getCodeCustomer().length();
            String strMKhBlank = "Mã KH:                          ";
            String strMKH = strMKhBlank.substring(0, strMKhBlank.length() - lenghtMCus);
            strMKH = strMKH + mHoaDon.getCodeCustomer();
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, strMKH + "\r\n", 0, false);

            int lenghtCus = mHoaDon.getNameCus().length();
            String strKhBlank = "KH:                             ";
            String strKH = strKhBlank.substring(0, strKhBlank.length() - lenghtCus);
            strKH = strKH + mHoaDon.getNameCus();
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, strKH + "\r\n", 0, false);

            int lenghtHD = mHoaDon.getNumberInvoice().length();
            String strHDBlank = "Số HĐ:                          ";
            String strHD = strHDBlank.substring(0, strHDBlank.length() - lenghtHD);
            strHD = strHD + mHoaDon.getNumberInvoice();

            WoosimPrinterActivity.woosim.saveSpool(UTF_8, strHD + "\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "T.gian:         " + regData + "\r\n", 0, false);
            byte[] lf4 = {0x0a};
            WoosimPrinterActivity.woosim.controlCommand(lf4, lf4.length);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "D.VỤ            S.LƯỢNG     GIÁ\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Thanh toán cước tháng 5		\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "                   1   1.550.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Thanh toán cước tháng 6		\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "                   1   1.550.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Dịch vụ MyTV		\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "                   1      99.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Dịch vụ Internet		\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "                   1      61.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Cước trả sau điện thoại Vina\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "                   1      20.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Tổng cước:             3.280.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Thuế (VAT):               20.000\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "--------------------------------\r\n", 0, false);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Tổng cộng:             3.300.000\r\n", 0, false);
            byte[] lf2 = {0x0a};
            WoosimPrinterActivity.woosim.controlCommand(lf2, lf2.length);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Thành tiền:        3.300.000 VNĐ\r\n", 0, false);
            byte[] lf3 = {0x0a};
            WoosimPrinterActivity.woosim.controlCommand(lf3, lf3.length);
            WoosimPrinterActivity.woosim.saveSpool(UTF_8, "Cảm ơn quý khách đã dùng dịch vụ của VPNT!\r\n\r\n\r\n", 0, true);
        }
        Bitmap bitmap = Helper.getBitmapFromAsset(context, "file:///android_asset/print_icon.bmp");
        try {
            if (bitmap != null) {
                System.out.println("co bitmap");
                WoosimPrinterActivity.woosim.printBitmap("file:///android_asset/print_icon.bmp");
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        WoosimPrinterActivity.woosim.controlCommand(lf, lf.length);
        WoosimPrinterActivity.woosim.printSpool(true);
        WoosimPrinterActivity.cardData = null;
    }

    //    public static void printReceiptDemoVNPT(Context context) throws IOException {
//
////        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.initPrinter());
////        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setPageMode());
////        sendImg(0, 5, R.drawable.logo_vnpt_print, context);
////        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_printStdMode());
//        ByteBuffer buffer = ByteBuffer.allocate(4096);
//        buffer.put(WoosimCmd.initPrinter());
//        buffer.put(WoosimCmd.setPageMode());
//        buffer.put(sendImgBuff(0, 5, R.drawable.logo_vnpt_print, context));
//        String str1 = "HÓA ĐƠN VIỄN THÔNG\n";
//        String str2 = "VNPT SOFTWARE\n";
//        String str3 = "346 Đường 2/9,\n";
//        String str31 = "Q.Hải Châu,Đà Nẵng\n";
//        String str4 = "ĐT:0915.676.468\n";
//        String str5 = "Cảm ơn quý khách đã dùng dịch vụ của VPNT!";
//        buffer.put(WoosimCmd.PM_setArea(0, 0, 384, 175));
//
//        buffer.put(WoosimCmd.PM_setPosition(150, 7));
//        buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
//        buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
//        buffer.put(str2.getBytes("UTF-8"));
//
//        buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
//        buffer.put(WoosimCmd.setTextStyle(false, false, false, 1, 1));
//        buffer.put(WoosimCmd.PM_setPosition(150, 42));
//        buffer.put(str3.getBytes("UTF-8"));
//
//        buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
//        buffer.put(WoosimCmd.setTextStyle(false, false, false, 1, 1));
//        buffer.put(WoosimCmd.PM_setPosition(150, 77));
//        buffer.put(str31.getBytes("UTF-8"));
//
//
//        buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
//        buffer.put(WoosimCmd.setTextStyle(false, false, false, 1, 1));
//        buffer.put(WoosimCmd.PM_setPosition(150, 112));
//        buffer.put(str4.getBytes("UTF-8"));
//
//
//        buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
//        buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
//        buffer.put(WoosimCmd.PM_setPosition(0, 135));
//        buffer.put(str5.getBytes("UTF-8"));
//        buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_CP437, WoosimCmd.FONT_MEDIUM));
//
////        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_printStdMode());
//        String str9 = "\n";
////        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.printData());
////        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setArea(0, 195, 384, 177));
////        String str6 = "SHIP TO:\n";
////        String str7 = "        H17/02, 925 Ngô Qyền\n        448, Gasan-dong Gumcheon-gu\n        Seoul, Rep. of Korea\n";
////        String str8 = "\n";
////        String str9 = "\n";
////        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);
////        byteStream.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_CP437, WoosimCmd.FONT_MEDIUM));
////        byteStream.write(WoosimCmd.setTextStyle(true, false, false, 1, 1));
////        byteStream.write(str6.getBytes());
////        byteStream.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_CP437, WoosimCmd.FONT_MEDIUM));
////        byteStream.write(WoosimCmd.setTextStyle(true, false, false, 1, 1));
////        byteStream.write(str7.getBytes());
////        byteStream.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_CP437, WoosimCmd.FONT_MEDIUM));
////        byteStream.write(WoosimCmd.setTextStyle(true, false, false, 1, 1));
////        byteStream.write(str8.getBytes());
////        byteStream.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_CP437, WoosimCmd.FONT_MEDIUM));
////        byteStream.write(WoosimCmd.setTextStyle(true, false, false, 1, 1));
////        byteStream.write(str9.getBytes());
////        byteStream.write(WoosimCmd.feedToMark());
////        BluetoothPrinterActivity.mPrintService.write(byteStream.toByteArray());
//
//
//
////        buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
////        buffer.put(str31.getBytes("UTF-8"));
////        buffer.put(str5.getBytes("UTF-8"));
//        buffer.put(str9.getBytes());
//        buffer.put(str9.getBytes());
//        buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_CP437, WoosimCmd.FONT_LARGE));
//        buffer.put(WoosimCmd.printData());
//        buffer.put(WoosimCmd.PM_printStdMode());
//        printBuffer(buffer);
//
//    }
    public static void printReceiptDemoVNPT(Context context) throws IOException {

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.initPrinter());
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setPageMode());
        sendImg(0, 5, R.drawable.logo_vnpt_print, context);
//        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setArea(0, 0, 384, 150));

        String str1 = "HÓA ĐƠN VIỄN THÔNG\n";
        String str2 = "VNPT SOFTWARE\r\n";
        String str3 = "346 Đường 2/9,\r\n";
        String str31 = "Q.Hải Châu,Đà Nẵng\r\n";
        String str4 = "ĐT:0915.676.468\r\n";
        String str5 = "Cảm ơn quý khách đã dùng dịch vụ của VPNT!\r\n\r\n\r\n";
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setArea(0, 0, 384, 175));

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 7));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setTextStyle(true, false, false, 1, 1));
        BluetoothPrinterActivity.mPrintService.write(str2.getBytes("UTF-8"));

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 42));
        BluetoothPrinterActivity.mPrintService.write(str3.getBytes("UTF-8"));

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 77));
        BluetoothPrinterActivity.mPrintService.write(str31.getBytes("UTF-8"));


        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 112));
        BluetoothPrinterActivity.mPrintService.write(str4.getBytes("UTF-8"));

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_printStdMode());
        String str9 = "\n";

        ByteBuffer buffer = ByteBuffer.allocate(512);
        buffer.put(WoosimCmd.initPrinter());
        buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
        buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
        buffer.put(str5.getBytes("UTF-8"));
        buffer.put(WoosimCmd.printData());
        printBuffer(buffer);

    }

    public static void printNotificationReceiptDemoVNPT_NEW(Context context, InvoiceCadmin mHoaDon) throws IOException {
        // in header
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.initPrinter());
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setPageMode());
        sendImg(0, 5, R.drawable.logo_vnpt_print, context);

        String str1 = "       HÓA ĐƠN VIỄN THÔNG\n";
        String str2 = "VNPT SOFTWARE\n";
        String str3 = "346 Đường 2/9,\n";
        String str31 = "Q.Hải Châu,Đà Nẵng\n";
        String str4 = "ĐT:0915.676.468\n";
        String str5 = "Cảm ơn quý khách đã dùng dịch vụ của VPNT!";
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setArea(0, 0, 384, 200));

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 7));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setTextStyle(true, false, false, 1, 1));
        BluetoothPrinterActivity.mPrintService.write(str2.getBytes("UTF-8"));

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 42));
        BluetoothPrinterActivity.mPrintService.write(str3.getBytes("UTF-8"));

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 77));
        BluetoothPrinterActivity.mPrintService.write(str31.getBytes("UTF-8"));


        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 112));
        BluetoothPrinterActivity.mPrintService.write(str4.getBytes("UTF-8"));


//        sendImg(60, 155, R.drawable.notication_invoice, context);

//        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
//        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setTextStyle(true, false, false, 1, 1));
//        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(0, 135));
//        BluetoothPrinterActivity.mPrintService.write(str5.getBytes("UTF-8"));
//        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_CP437, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_printStdMode());

//        return;
        //Xong header

        String regData = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date dt = new Date();
        regData = dateFormat.format(dt);
        if (mHoaDon.getPaymentStatus()==0) {// nợ 1 tháng
            String strLine = "--------------------------------\r\n";
            String strEnter = "\r\n";
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(WoosimCmd.initPrinter());
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
//            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
//            buffer.put(str1.getBytes("UTF-8"));
            buffer.put(strEnter.getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            int lenghtMCus = mHoaDon.getCusCode().length();
            String strMKhBlank = "Mã KH:                          ";
            String strMKH = strMKhBlank.substring(0, strMKhBlank.length() - lenghtMCus);
            strMKH = strMKH + mHoaDon.getCusCode()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strMKH.getBytes("UTF-8"));


            int lenghtCus = mHoaDon.getCusname().length();
            String strKhBlank = "KH:                             ";
            String strKH = strKhBlank.substring(0, strKhBlank.length() - lenghtCus);
            strKH = strKH + mHoaDon.getCusname()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strKH.getBytes("UTF-8"));


            int lenghtHD = mHoaDon.getInvNum().length();
            String strHDBlank = "Số HĐ:                          ";
            String strHD = strHDBlank.substring(0, strHDBlank.length() - lenghtHD);
            strHD = strHD + mHoaDon.getInvNum()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strHD.getBytes("UTF-8"));


            String strTG = "T.gian:         " + regData + "\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strTG.getBytes("UTF-8"));

            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("D.Vụ            S.Lượng     Giá\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Cước MyTV		\r\n".getBytes("UTF-8"));
            buffer.put("                   1      35.000\r\n".getBytes("UTF-8"));
            buffer.put("Cước Internet		\r\n".getBytes("UTF-8"));
            buffer.put("                   1      70.000\r\n".getBytes("UTF-8"));
            buffer.put("Cước viễn thông CNTT T03/2016\r\n".getBytes("UTF-8"));
            buffer.put("                   1      20.000\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Tổng cước:              125.000\r\n".getBytes("UTF-8"));
            buffer.put("Thuế (VAT):              12.500\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Tổng cộng:              137.500\r\n".getBytes("UTF-8"));
            buffer.put("\r\n".getBytes("UTF-8"));
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
            buffer.put("Thành tiền:          137.500 VNĐ\r\n\r\n".getBytes("UTF-8"));
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
            buffer.put("Cảm ơn quý khách đã sử dụng dịch vụ của VPNT!\r\n\r\n\r\n".getBytes("UTF-8"));

            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_CP437, WoosimCmd.FONT_LARGE));
            buffer.put(WoosimCmd.printData());
            printBuffer(buffer);
        } else// nợ nhiều tháng
        {
            String strLine = "--------------------------------\r\n";
            String strEnter = "\r\n";
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(WoosimCmd.initPrinter());
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
//            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
//            buffer.put(str1.getBytes("UTF-8"));
            buffer.put(strEnter.getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            int lenghtMCus = mHoaDon.getCusCode().length();
            String strMKhBlank = "Mã KH:                          ";
            String strMKH = strMKhBlank.substring(0, strMKhBlank.length() - lenghtMCus);
            strMKH = strMKH + mHoaDon.getCusCode()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strMKH.getBytes("UTF-8"));


            int lenghtCus = mHoaDon.getCusname().length();
            String strKhBlank = "KH:                             ";
            String strKH = strKhBlank.substring(0, strKhBlank.length() - lenghtCus);
            strKH = strKH + mHoaDon.getCusname()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strKH.getBytes("UTF-8"));


            int lenghtHD = mHoaDon.getInvNum().length();
            String strHDBlank = "Số HĐ:                          ";
            String strHD = strHDBlank.substring(0, strHDBlank.length() - lenghtHD);
            strHD = strHD + mHoaDon.getInvNum()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strHD.getBytes("UTF-8"));


            String strTG = "T.gian:         " + regData + "\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strTG.getBytes("UTF-8"));

            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("D.Vụ            S.Lượng     Giá\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Thanh toán cước tháng 5		\r\n".getBytes("UTF-8"));
            buffer.put("                   1   1.550.000\r\n".getBytes("UTF-8"));
            buffer.put("Thanh toán cước tháng 6		\r\n".getBytes("UTF-8"));
            buffer.put("                   1   1.550.000\r\n".getBytes("UTF-8"));
            buffer.put("Dịch vụ MyTV		\r\n".getBytes("UTF-8"));
            buffer.put("                   1      99.000\r\n".getBytes("UTF-8"));
            buffer.put("Dịch vụ Internet		\r\n".getBytes("UTF-8"));
            buffer.put("                   1      61.000\r\n".getBytes("UTF-8"));
            buffer.put("Cước trả sau điện thoại Vina\r\n".getBytes("UTF-8"));
            buffer.put("                   1      20.000\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Tổng cước:            3.280.000\r\n".getBytes("UTF-8"));
            buffer.put("Thuế (VAT):              20.000\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Tổng cộng:            3.300.000\r\n".getBytes("UTF-8"));
            buffer.put("\r\n".getBytes("UTF-8"));
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
            buffer.put("Thành tiền:        3.300.000 VNĐ\r\n\r\n".getBytes("UTF-8"));
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
            buffer.put("Cảm ơn quý khách đã sử dụng dịch vụ của VPNT!\r\n\r\n\r\n".getBytes("UTF-8"));
            buffer.put(WoosimCmd.printData());
            printBuffer(buffer);
        }

    }
    public static void printReceiptDemoVNPT_NEW(Context context, Invoice mHoaDon) throws IOException {
        // in header
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.initPrinter());
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setPageMode());
        sendImg(0, 5, R.drawable.logo_vnpt_print, context);

        String str1 = "       HÓA ĐƠN VIỄN THÔNG\n";
        String str2 = "VNPT IT KV3\n";
        String str3 = "346 Đường 2/9,\n";
        String str31 = "Q.Hải Châu,Đà Nẵng\n";
        String str4 = "ĐT:0915.676.468\n";
        String str5 = "Cảm ơn quý khách đã dùng dịch vụ của VPNT!";
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setArea(0, 0, 384, 200));

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 7));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setTextStyle(true, false, false, 1, 1));
        BluetoothPrinterActivity.mPrintService.write(str2.getBytes("UTF-8"));

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 42));
        BluetoothPrinterActivity.mPrintService.write(str3.getBytes("UTF-8"));

        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 77));
        BluetoothPrinterActivity.mPrintService.write(str31.getBytes("UTF-8"));


        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(150, 112));
        BluetoothPrinterActivity.mPrintService.write(str4.getBytes("UTF-8"));


//        sendImg(15, 155, R.drawable.bien_nhan_new, context);

//        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_MEDIUM));
//        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setTextStyle(true, false, false, 1, 1));
//        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_setPosition(0, 135));
//        BluetoothPrinterActivity.mPrintService.write(str5.getBytes("UTF-8"));
//        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_CP437, WoosimCmd.FONT_MEDIUM));
        BluetoothPrinterActivity.mPrintService.write(WoosimCmd.PM_printStdMode());

//        return;
        //Xong header

        String regData = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date dt = new Date();
        regData = dateFormat.format(dt);
        if (mHoaDon.getState_debit()==0) {// nợ 1 tháng
            String strLine = "--------------------------------\r\n";
            String strEnter = "\r\n";
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(WoosimCmd.initPrinter());
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
//            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
//            buffer.put(str1.getBytes("UTF-8"));
            buffer.put(strEnter.getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            int lenghtMCus = mHoaDon.getMa_kh().length();
            String strMKhBlank = "Mã KH:                          ";
            String strMKH = strMKhBlank.substring(0, strMKhBlank.length() - lenghtMCus);
            strMKH = strMKH + mHoaDon.getMa_kh()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strMKH.getBytes("UTF-8"));


            int lenghtCus = mHoaDon.getName().length();
            String strKhBlank = "KH:                             ";
            String strKH = strKhBlank.substring(0, strKhBlank.length() - lenghtCus);
            strKH = strKH + mHoaDon.getName()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strKH.getBytes("UTF-8"));


            int lenghtHD = mHoaDon.getNumber_no().length();
            String strHDBlank = "Số HĐ:                          ";
            String strHD = strHDBlank.substring(0, strHDBlank.length() - lenghtHD);
            strHD = strHD + mHoaDon.getNumber_no()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strHD.getBytes("UTF-8"));


            String strTG = "T.gian:         " + regData + "\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strTG.getBytes("UTF-8"));

            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("D.Vụ            S.Lượng     Giá\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Cước MyTV		\r\n".getBytes("UTF-8"));
            buffer.put("                   1      35.000\r\n".getBytes("UTF-8"));
            buffer.put("Cước Internet		\r\n".getBytes("UTF-8"));
            buffer.put("                   1      70.000\r\n".getBytes("UTF-8"));
            buffer.put("Cước viễn thông CNTT T03/2016\r\n".getBytes("UTF-8"));
            buffer.put("                   1      20.000\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Tổng cước:              125.000\r\n".getBytes("UTF-8"));
            buffer.put("Thuế (VAT):              12.500\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Tổng cộng:              137.500\r\n".getBytes("UTF-8"));
            buffer.put("\r\n".getBytes("UTF-8"));
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
            buffer.put("Thành tiền:          137.500 VNĐ\r\n\r\n".getBytes("UTF-8"));
            buffer.put("Xác nhận của nhân viên thu tiền\r\n\r\n\r\n\r\n\r\n\r\n\r\n".getBytes("UTF-8"));
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
            buffer.put("Cảm ơn quý khách đã sử dụng dịch vụ của VPNT!\r\n\r\n\r\n".getBytes("UTF-8"));

            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_CP437, WoosimCmd.FONT_LARGE));
            buffer.put(WoosimCmd.printData());
            printBuffer(buffer);
        } else// nợ nhiều tháng
        {
            String strLine = "--------------------------------\r\n";
            String strEnter = "\r\n";
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(WoosimCmd.initPrinter());
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
//            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
//            buffer.put(str1.getBytes("UTF-8"));
            buffer.put(strEnter.getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            int lenghtMCus = mHoaDon.getMa_kh().length();
            String strMKhBlank = "Mã KH:                          ";
            String strMKH = strMKhBlank.substring(0, strMKhBlank.length() - lenghtMCus);
            strMKH = strMKH + mHoaDon.getMa_kh()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strMKH.getBytes("UTF-8"));


            int lenghtCus = mHoaDon.getName().length();
            String strKhBlank = "KH:                             ";
            String strKH = strKhBlank.substring(0, strKhBlank.length() - lenghtCus);
            strKH = strKH + mHoaDon.getName()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strKH.getBytes("UTF-8"));


            int lenghtHD = mHoaDon.getNumber_no().length();
            String strHDBlank = "Số HĐ:                          ";
            String strHD = strHDBlank.substring(0, strHDBlank.length() - lenghtHD);
            strHD = strHD + mHoaDon.getNumber_no()+"\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strHD.getBytes("UTF-8"));


            String strTG = "T.gian:         " + regData + "\r\n";
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(strTG.getBytes("UTF-8"));

            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("D.Vụ            S.Lượng     Giá\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Thanh toán cước tháng 5		\r\n".getBytes("UTF-8"));
            buffer.put("                   1   1.550.000\r\n".getBytes("UTF-8"));
            buffer.put("Thanh toán cước tháng 6		\r\n".getBytes("UTF-8"));
            buffer.put("                   1   1.550.000\r\n".getBytes("UTF-8"));
            buffer.put("Dịch vụ MyTV		\r\n".getBytes("UTF-8"));
            buffer.put("                   1      99.000\r\n".getBytes("UTF-8"));
            buffer.put("Dịch vụ Internet		\r\n".getBytes("UTF-8"));
            buffer.put("                   1      61.000\r\n".getBytes("UTF-8"));
            buffer.put("Cước trả sau điện thoại Vina\r\n".getBytes("UTF-8"));
            buffer.put("                   1      20.000\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Tổng cước:            3.280.000\r\n".getBytes("UTF-8"));
            buffer.put("Thuế (VAT):              20.000\r\n".getBytes("UTF-8"));
            buffer.put(strLine.getBytes("UTF-8"));
            buffer.put("Tổng cộng:            3.300.000\r\n".getBytes("UTF-8"));
            buffer.put("\r\n".getBytes("UTF-8"));
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
            buffer.put("Thành tiền:        3.300.000 VNĐ\r\n\r\n".getBytes("UTF-8"));
            buffer.put("Xác nhận của nhân viên thu tiền\r\n\r\n\r\n\r\n\r\n\r\n\r\n".getBytes("UTF-8"));
            buffer.put(WoosimCmd.setCodeTable(WoosimCmd.MCU_RX, WoosimCmd.CT_DBCS, WoosimCmd.FONT_LARGE));
            buffer.put(WoosimCmd.setTextStyle(true, false, false, 1, 1));
            buffer.put("Cảm ơn quý khách đã sử dụng dịch vụ của VPNT!\r\n\r\n\r\n".getBytes("UTF-8"));
            buffer.put(WoosimCmd.printData());
            printBuffer(buffer);
        }


    }
    private static byte[] sendImgBuff(int x, int y, int id, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id, options);
//        if (bmp == null) return;

        byte[] data = WoosimImage.drawBitmap(x, y, bmp);
        bmp.recycle();
        return data;
//        BluetoothPrinterActivity.mPrintService.write(data);
    }

    private static void sendImg(int x, int y, int id, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id, options);
        if (bmp == null) return;

        byte[] data = WoosimImage.drawBitmap(x, y, bmp);
        bmp.recycle();

        BluetoothPrinterActivity.mPrintService.write(data);
    }

    private static void printBuffer(ByteBuffer buffer) {
        byte[] byteArray = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(byteArray);
        BluetoothPrinterActivity.mPrintService.write(byteArray);
    }
}
