package com.vnpt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vnpt.dto.Term;
import com.vnpt.staffhddtpos58.R;

import java.util.ArrayList;


/**
 * @Description: adapter cua list Term
 * @author:truonglt2
 * @since:Feb 7, 2014 3:49:14 PM
 * @version: 1.0
 * @since: 1.0
 */

public class ListTermAdapter extends BaseAdapter {

    private ArrayList<Term> arrItem = new ArrayList<Term>();

    private LayoutInflater mInflater;
    Context mContext;

    /**
     * @param context
     */
    public ListTermAdapter(Context context, ArrayList<Term> arr) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrItem = arr;
    }

    public ListTermAdapter(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void clearAllData() {
        arrItem.clear();
        notifyDataSetChanged();
    }

    public void addItem(Term item) {
        arrItem.add(item);
    }

    @Override
    public int getCount() {

        return arrItem.size();
    }

    @Override
    public Term getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_list_term, null);
            holder.txtNameTerm = (TextView) convertView.findViewById(R.id.txtNameTerm);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setDataViews(position, getItem(position), (ViewHolder) convertView.getTag());

        return convertView;
    }

    private void setDataViews(final int posi, final Term object, final ViewHolder holder) {
        holder.txtNameTerm.setText(object.getName_term());
    }

    /**
     * @author:truonglt2
     * @since:Feb 7, 2014 3:09:01 PM
     * @Description: lop holder temp
     */
    class ViewHolder {
        public TextView txtNameTerm;
    }

}