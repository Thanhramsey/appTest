package com.vnpt.staffhddtpos58.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vnpt.staffhddtpos58.R;

public class CustomRow extends LinearLayout {

    TextView txtName, txtCount;
    private String mName, mCount;

    public CustomRow(Context context) {
        super(context);
        initView();
    }

    public CustomRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomRow, 0, 0);
        try {

            mName = a.getString(R.styleable.CustomRow_name);
            mCount = a.getString(R.styleable.CustomRow_count);
        } finally {
            a.recycle();
        }
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_row, this);
        txtName = view.findViewById(R.id.txtName);
        txtCount = view.findViewById(R.id.txtCount);
    }

    public void setName(String mName) {
        this.mName = mName;
        txtName.setText(mName);
        invalidate();
        requestLayout();
    }

    public void setCount(String mCount) {
        this.mCount = mCount;
        txtCount.setText(mCount);
        invalidate();
        requestLayout();
    }

    public String getmName() {
        return mName;
    }

    public String getmCount() {
        return mCount;
    }
}
