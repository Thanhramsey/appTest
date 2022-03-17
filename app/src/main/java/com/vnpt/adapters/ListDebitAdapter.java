package com.vnpt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.dto.ItemDebit;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.R;
import com.vnpt.utils.Helper;

import java.util.ArrayList;


/**
 * @Description: adapter cua list book
 * @author:truonglt2
 * @since:Feb 7, 2014 3:49:14 PM
 * @version: 1.0
 * @since: 1.0
 */

public class ListDebitAdapter extends BaseAdapter {

    private ArrayList<ItemDebit> arrItem = new ArrayList<ItemDebit>();
    OnEventControlListener mListener;
    private LayoutInflater mInflater;
    Context mContext;
    /**
     * @param context
     */
    public ListDebitAdapter(Context context, OnEventControlListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public void clearAllData()
    {
        arrItem.clear();
        notifyDataSetChanged();
    }
    public void addItem(ItemDebit item) {
        arrItem.add(item);
    }

    @Override
    public int getCount() {

        return arrItem.size();
    }

    @Override
    public ItemDebit getItem(int position) {
        return arrItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_list_debit, null);
            holder.txtNameService = (TextView) convertView.findViewById(R.id.txtNameService);
            holder.txtPriceService = (TextView) convertView.findViewById(R.id.txtPriceService);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setDataViews(position, getItem(position), (ViewHolder) convertView.getTag());

        return convertView;
    }
    private void setDataViews(final int posi, final ItemDebit object, final ViewHolder holder) {
        holder.txtNameService.setText(object.getNameTerm());
        holder.txtPriceService.setText(""+ Helper.formatPrice(object.getTotalPrice())+ Common.CURRENCY);
        holder.cbxStatus.setChecked(object.isChecked());
        holder.cbxStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    arrItem.get(posi).setChecked(true);
                }
                else
                {
                    arrItem.get(posi).setChecked(false);
                }
                mListener.onEvent(ActionEventConstant.ACTION_CHECK_ALL_DEBIT_INVOICE,null,""+posi);
            }
        });
    }
    /**
     * @author:truonglt2
     * @since:Feb 7, 2014 3:09:01 PM
     * @Description: lop holder temp
     */
    class ViewHolder {
        public TextView txtNameService;
        public TextView txtPriceService;
        public CheckBox cbxStatus;
    }
    public double getSumMoney()
    {
        double sum = 0;
        for (int i = 0; i< arrItem.size(); i++)
        {
            sum += arrItem.get(i).getTotalPrice();
        }
        return sum;
    }
    public int getItemChecked()
    {
        int count=0;
        for ( ItemDebit item : arrItem)
        {
            if(item.isChecked())
            {
                count+=1;
            }
        }
        return count;
    }
    public double getMoneyItemChecked()
    {
        double sum = 0;
        for ( ItemDebit item : arrItem)
        {
            if(item.isChecked())
            {
                sum += item.getTotalPrice();
            }
        }
        return sum;
    }
    public void setCheckedWithPosion(int position,boolean isChecked)
    {
        for (int i = 0; i < arrItem.size(); i++)
        {
            if(i == position)
            {
                arrItem.get(i).setChecked(isChecked);
                break;
            }
        }
    }
    public void setCheckAll(boolean isChecked)
    {
        for (int i = 0; i < arrItem.size(); i++)
        {
            arrItem.get(i).setChecked(isChecked);
        }
    }
}
