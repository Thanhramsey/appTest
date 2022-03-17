package com.vnpt.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class InvoiceUpdatePaymentedRequest implements Parcelable {
	private int idCustomer;
	private int idInvoice;
	private String reason;
	private ArrayList<CerfiticateItem> arrUserUpdateCerfiticate;
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}



	public InvoiceUpdatePaymentedRequest() {
		this.idCustomer = 0;
		this.idInvoice = 0;
		this.reason = "";
		this.arrUserUpdateCerfiticate = new ArrayList<>();
	}

	public int getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}

	public int getIdInvoice() {
		return idInvoice;
	}

	public void setIdInvoice(int idInvoice) {
		this.idInvoice = idInvoice;
	}

	public ArrayList<CerfiticateItem> getArrUserUpdateCerfiticate() {
		return arrUserUpdateCerfiticate;
	}

	public void setArrUserUpdateCerfiticate(ArrayList<CerfiticateItem> arrUserUpdateCerfiticate) {
		this.arrUserUpdateCerfiticate = arrUserUpdateCerfiticate;
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(idCustomer);
		dest.writeInt(idInvoice);
		dest.writeString(reason);
		dest.writeTypedList(arrUserUpdateCerfiticate);
	}

	private void readFromParcel(Parcel in) {
		reason = in.readString();
		idCustomer = in.readInt();
		idInvoice = in.readInt();
		arrUserUpdateCerfiticate = in.createTypedArrayList(CerfiticateItem.CREATOR);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		@Override
		public CerfiticateItem createFromParcel(Parcel in) {
			return new CerfiticateItem(in);
		}

		@Override
		public CerfiticateItem[] newArray(int size) {
			return new CerfiticateItem[size];
		}
	};
}
