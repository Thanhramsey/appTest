package com.vnpt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vnpt.dto.HoaDon;
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

public class ListHoaDonAdapter extends BaseAdapter {

    private ArrayList<HoaDon> arrHoaDon = new ArrayList<HoaDon>();

    private LayoutInflater mInflater;
    Context mContext;
    /**
     * @param context
     */
    public ListHoaDonAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public void clearAllData()
    {
        arrHoaDon.clear();
        notifyDataSetChanged();
    }
    public void addItem(HoaDon item) {
        arrHoaDon.add(item);
    }

    @Override
    public int getCount() {

        return arrHoaDon.size();
    }

    @Override
    public HoaDon getItem(int position) {
        return arrHoaDon.get(position);
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
            convertView = mInflater.inflate(R.layout.item_list_hoadon, null);
            holder.containerLayout = (RelativeLayout) convertView.findViewById(R.id.container_item);
            holder.txtNameCus = (TextView) convertView.findViewById(R.id.txtNameCus);
            holder.txtPrice = (TextView) convertView
                    .findViewById(R.id.txtPrice);
            holder.txtAddress = (TextView) convertView
                    .findViewById(R.id.txtAddress);
            holder.imageTag = (ImageView) convertView
                    .findViewById(R.id.imageTag);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setDataViews(position, getItem(position), (ViewHolder) convertView.getTag());

        return convertView;
    }
    private void setDataViews(final int posi, final HoaDon object, final ViewHolder holder) {
        if (posi % 2 == 0) {
            holder.containerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        else
        {
            holder.containerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.blue_hightlight));
        }
        holder.txtNameCus.setText(object.getNameCus());
        double price = Double.parseDouble(object.getPrice()) + Double.parseDouble(object.getTax());
        holder.txtPrice.setText(Helper.formatPrice(price)+" VNĐ");
        holder.txtAddress.setText(""+object.getAddress());
        if(object.getStatus().equals("1"))
        {
            holder.imageTag.setImageResource(R.drawable.tag_green);
        }
        else
        {
            holder.imageTag.setImageResource(R.drawable.tag_red);
        }

    }
    /**
     * @author:truonglt2
     * @since:Feb 7, 2014 3:09:01 PM
     * @Description: lop holder temp
     */
    class ViewHolder {
        public RelativeLayout containerLayout;
        public TextView txtNameCus;
        public TextView txtAddress;
        public TextView txtPrice;
        public ImageView imageTag;
    }

}