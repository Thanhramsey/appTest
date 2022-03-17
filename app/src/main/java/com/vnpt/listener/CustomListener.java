package com.vnpt.listener;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.vnpt.common.Common;
import com.vnpt.staffhddtpos58.R;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.Helper;
import com.vnpt.utils.StoreSharePreferences;

public class CustomListener implements View.OnClickListener {
  private final Dialog dialog;
  Context mContext;
  EditText medtServer,medtPattern,medtSerial;
  public CustomListener(Dialog dialog) {
    this.dialog = dialog;

  }
  public CustomListener(Context context,Dialog dialog,EditText edtServer,EditText edtPattern,EditText edtSerial) {
    this.mContext= context;
    this.dialog = dialog;
    this.medtServer = edtServer;
    this.medtPattern = edtPattern;
    this.medtSerial = edtSerial;

  }

  @Override
  public void onClick(View v) {
    String addServer = medtServer.getText().toString();
    String pattern = medtPattern.getText().toString();
    String serial = medtSerial.getText().toString();
    if(Helper.getInstance().isStringEmpty(addServer))
    {
      medtServer.setError(mContext.getString(R.string.txt_please_intput_address_server));
      return;
    }
    if(Helper.getInstance().isStringEmpty(pattern))
    {
      medtPattern.setError(mContext.getString(R.string.text_input_pattern));
      return;
    }
    if(Helper.getInstance().isStringEmpty(serial))
    {
      medtSerial.setError(mContext.getString(R.string.text_input_serial));
      return;
    }
    if(!Helper.getInstance().isStringEmpty(addServer)&& !Helper.getInstance().isStringEmpty(pattern)&& !Helper.getInstance().isStringEmpty(pattern))
    {
      StoreSharePreferences.getInstance(mContext).saveStringPreferences(Common.KEY_FINAL_ADDRESS_SERVER, addServer);
      StoreSharePreferences.getInstance(mContext).saveStringPreferences(Common.KEY_DEFAULT_PATTERN_INVOICES,pattern);
      StoreSharePreferences.getInstance(mContext).saveStringPreferences(Common.KEY_DEFAULT_SERIAL_INVOICES, serial);
      StoreSharePreferences.getInstance(mContext).saveIntPreferences(Common.KEY_FIRST_CONFIG,1);
      DialogUtils.showInfoDialog(mContext, mContext.getString(R.string.message), mContext.getString(R.string.txt_infor_config_system_invoices));
      dialog.dismiss();
    }
    // Do whatever you want here

    // If you want to close the dialog, uncomment the line below
    //dialog.dismiss();
  }
}