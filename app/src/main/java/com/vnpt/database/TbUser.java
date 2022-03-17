package com.vnpt.database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TbUser {
	public final static String NGUOIDUNG_TABLE = "NGUOIDUNG";

	public final static String NGUOIDUNG_IdNguoiDung = "IdNguoiDung";
	public final static String NGUOIDUNG_CMND = "CMND";
	public final static String NGUOIDUNG_Email = "Email";
	public final static String NGUOIDUNG_DiaChi = "DiaChi";
	public final static String NGUOIDUNG_DienThoai = "DienThoai";
	public final static String NGUOIDUNG_NgaySinh = "NgaySinh";
	public final static String NGUOIDUNG_CreateDate = "CreateDate";
	public final static String NGUOIDUNG_Ten = "Ten";

	protected static TbUser instance;

	public TbUser(Context context) {
		mcontext = context;
	}

	static Context mcontext;

	public static TbUser getInstance(Context context) {

		if (instance == null) {
			instance = new TbUser(context);
		}
		return instance;
	}

	public Cursor fetchAllLaiXuatWithQuery(String query) {

		Cursor cursor = DatabaseHelp.getInstance(mcontext).selectQuery(query);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

	public long insertNguoiDung(String CMND, String Email, String DiaChi,
			String DienThoai, String NgaySinh, String Ten)

	{
		ContentValues values = new ContentValues();
		values.put(NGUOIDUNG_CMND, CMND);
		values.put(NGUOIDUNG_Email, Email);
		values.put(NGUOIDUNG_DiaChi, DiaChi);
		values.put(NGUOIDUNG_DienThoai, DienThoai);
		values.put(NGUOIDUNG_NgaySinh, NgaySinh);
		values.put(NGUOIDUNG_CreateDate, getCrurentDateTime());
		values.put(NGUOIDUNG_Ten, Ten);

		if (DatabaseHelp.getInstance(mcontext).INSERT_TABLE(NGUOIDUNG_TABLE,
				values) == -1) {
			return 0;// failed
		} else {
			return 1;// ook
		}

	}

	public String getCrurentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	/*
	 * public Cursor getAccount(String userName, String password) { String query
	 * = "SELECT * FROM dbTaiKhoan where TenDangNhap='" + userName +
	 * "' and Pass='" + password + "' limit 1;"; Cursor cursor =
	 * dbHelper.selectQuery(query); if (cursor != null) { if (cursor.getCount()
	 * > 0) {// có phần tử sẽ trả về đối tượng cursor cursor.moveToFirst();
	 * return cursor; } else { // trả về dữ liệu null return null; }
	 * 
	 * } else { // trả về dữ liệu null return null; }
	 * 
	 * 
	 * 
	 * 
	 * }
	 */
}
