package com.vnpt.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.vnpt.staffhddtpos58.R;


public class DialogProgressSys extends ProgressDialog {

    private String strLoadding = null;
    
    public DialogProgressSys(Context context, int theme) {
        super(context, theme);
    }

    public DialogProgressSys(Context context, String txtLoadding){
        super(context);
        this.strLoadding = txtLoadding;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setCancelable(false);
                
        setContentView(R.layout.layout_progress_bar_loadding);
        if(strLoadding!=null){
            TextView txtLoadding = (TextView) findViewById(R.id.oaprogresstitle);
            txtLoadding.setText(strLoadding);
        }
    }
    
    public void showDialog() {
        show();
    }
}