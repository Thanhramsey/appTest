
package com.vnpt.staffhddtpos58.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnpt.staffhddtpos58.R;

public class CustomizeDialog extends Dialog implements View.OnClickListener{
    ImageView iconDialog;

    Button btnYes;
    Button btnNo;

    Context mContext;

    TextView mTitle = null;

    TextView mMessage = null;

    View v = null;

    Button cancelButton;

    public CustomizeDialog(Context context) {
        super(context);
        mContext = context;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        /** Design the dialog in main.xml file */
        setContentView(R.layout.dialog_contents_method_payment);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        mTitle = (TextView) findViewById(R.id.txtTitle);
        mMessage = (TextView) findViewById(R.id.txtContent);
        btnYes = (Button) findViewById(R.id.btn_yes);
        btnNo = (Button) findViewById(R.id.btn_no);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
        mTitle.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.btn_yes:
            {

                break;
            }
            case R.id.btn_no:
            {
                this.dismiss();
                break;
            }
            default:
            {
                break;
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitle.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        mTitle.setText(mContext.getResources().getString(titleId));
    }

    /**
     * Set the message text for this dialog's window.
     *
     * @param message - The new message to display in the title.
     */
    public void setMessage(CharSequence message) {
        mMessage.setText(message);
    }

    /**
     * Set the message ID
     *
     * @param messageId
     */
    public void setMessage(int messageId) {
        mMessage.setText(mContext.getResources().getString(messageId));
    }

}
