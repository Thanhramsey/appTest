package com.vnpt.dto;

import java.io.Serializable;

/**
 * @author:truonglt2
 * @since:Jan 21, 2014 4:43:57 PM
 * @Description:
 */
public class MethodConfirm implements Serializable {

	private boolean isSendToEmail;
	private boolean isSendToAppUser;
	private boolean isPrintToPaper;
	private boolean isSendSMS;

	public boolean isSendSMS() {
		return isSendSMS;
	}

	public void setSendSMS(boolean sendSMS) {
		isSendSMS = sendSMS;
	}

	public boolean isSendToEmail() {
		return isSendToEmail;
	}

	public void setSendToEmail(boolean sendToEmail) {
		isSendToEmail = sendToEmail;
	}

	public boolean isSendToAppUser() {
		return isSendToAppUser;
	}

	public void setSendToAppUser(boolean sendToAppUser) {
		isSendToAppUser = sendToAppUser;
	}

	public boolean isPrintToPaper() {
		return isPrintToPaper;
	}

	public void setPrintToPaper(boolean printToPaper) {
		isPrintToPaper = printToPaper;
	}



}
