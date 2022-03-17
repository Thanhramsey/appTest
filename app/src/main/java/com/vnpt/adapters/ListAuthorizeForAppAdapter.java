package com.vnpt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vnpt.dto.ItemAuthorizeForApp;
import com.vnpt.staffhddtpos58.R;

import java.util.ArrayList;


/**
 * @Description: adapter cua list book
 * @author:truonglt2
 * @since:Feb 7, 2014 3:49:14 PM
 * @version: 1.0
 * @since: 1.0
 */

public class ListAuthorizeForAppAdapter extends BaseAdapter {

    private ArrayList<ItemAuthorizeForApp> arrHoaDon = new ArrayList<ItemAuthorizeForApp>();

    private LayoutInflater mInflater;
    Context mContext;
    /**
     * @param context
     */
    public ListAuthorizeForAppAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public void clearAllData()
    {
        arrHoaDon.clear();
        notifyDataSetChanged();
    }
    public void addItem(ItemAuthorizeForApp item) {
        arrHoaDon.add(item);
    }

    @Override
    public int getCount() {

        return arrHoaDon.size();
    }

    @Override
    public ItemAuthorizeForApp getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_list_authorize_app, null);
            holder.tvtNameAuthorize = (TextView) convertView.findViewById(R.id.tvtNameAuthorize);
            holder.imageTag = (ImageView) convertView
                    .findViewById(R.id.imageTag);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setDataViews(position, getItem(position), (ViewHolder) convertView.getTag());

        return convertView;
    }
    private void setDataViews(final int posi, final ItemAuthorizeForApp object, final ViewHolder holder) {
        holder.tvtNameAuthorize.setText(object.getNameAuthorize());

        if(object.getIdItem() == 1)
        {
            holder.imageTag.setImageResource(R.drawable.documentation_signatured);
        }
        else if(object.getIdItem() == 2)
        {
            holder.imageTag.setImageResource(R.drawable.microphone);
        }
        else
        {
            holder.imageTag.setImageResource(R.drawable.photo_camera);
        }

    }
    /**
     * @author:truonglt2
     * @since:Feb 7, 2014 3:09:01 PM
     * @Description: lop holder temp
     */
    class ViewHolder {
        public TextView tvtNameAuthorize;
        public ImageView imageTag;
        public LinearLayout containerLayout;
    }

}