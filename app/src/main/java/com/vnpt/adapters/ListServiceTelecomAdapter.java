package com.vnpt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vnpt.common.Common;
import com.vnpt.dto.ItemServiceTelecom;
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

public class ListServiceTelecomAdapter extends BaseAdapter {

    private ArrayList<ItemServiceTelecom> arrItem = new ArrayList<ItemServiceTelecom>();

    private LayoutInflater mInflater;
    Context mContext;

    /**
     * @param context
     */
    public ListServiceTelecomAdapter(Context context, ArrayList<ItemServiceTelecom> arr) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrItem = arr;
    }

    public ListServiceTelecomAdapter(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void clearAllData() {
        arrItem.clear();
        notifyDataSetChanged();
    }

    public void addItem(ItemServiceTelecom item) {
        arrItem.add(item);
    }

    @Override
    public int getCount() {

        return arrItem.size();
    }

    @Override
    public ItemServiceTelecom getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_list_services_telecom, null);
            holder.txtNameService = (TextView) convertView.findViewById(R.id.txtNameService);
            holder.txtPriceService = (TextView) convertView.findViewById(R.id.txtPriceService);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setDataViews(position, getItem(position), (ViewHolder) convertView.getTag());

        return convertView;
    }

    private void setDataViews(final int posi, final ItemServiceTelecom object, final ViewHolder holder) {
        holder.txtNameService.setText(object.getNameService());
        holder.txtPriceService.setText("" + Helper.formatPrice(object.getPriceService()) + Common.CURRENCY);

    }

    /**
     * @author:truonglt2
     * @since:Feb 7, 2014 3:09:01 PM
     * @Description: lop holder temp
     */
    class ViewHolder {
        public TextView txtNameService;
        public TextView txtPriceService;
    }

    public double getSumMoney() {
        double sum = 0;
        for (int i = 0; i < arrItem.size(); i++) {
            sum += arrItem.get(i).getPriceService();
        }
        return sum;
    }
}