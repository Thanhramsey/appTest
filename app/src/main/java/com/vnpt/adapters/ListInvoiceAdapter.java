package com.vnpt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vnpt.common.Common;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.staffhddtpos58.R;
import com.vnpt.utils.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * @Description: adapter cua list book
 * @author:truonglt2
 * @since:Feb 7, 2014 3:49:14 PM
 * @version: 1.0
 * @since: 1.0
 */

public class ListInvoiceAdapter extends BaseAdapter {

    private ArrayList<InvoiceCadmin> arrHoaDon = new ArrayList<InvoiceCadmin>();

    private LayoutInflater mInflater;
    Context mContext;


    /**
     * @param context
     */
    public ListInvoiceAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void clearAllData() {
        arrHoaDon.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
// quy định số lượng hiển thị
        return arrHoaDon == null ? 0 : arrHoaDon.size();
    }

    @Override
    public InvoiceCadmin getItem(int position) {
        if(position==-1)
        {
            position = 0;
        }
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
            holder.txtCusCode = (TextView) convertView
                    .findViewById(R.id.txtCusCode);
            holder.imageTag = (ImageView) convertView
                    .findViewById(R.id.imageTag);
            holder.txtInvNo = (TextView) convertView.findViewById(R.id.txtInvNo);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        setDataViews(position, getItem(position), holder);

        return convertView;
    }

    private void setDataViews(final int posi, final InvoiceCadmin object, final ViewHolder holder) {
        if (posi % 2 == 0) {
            holder.containerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.containerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.blue_hightlight));
        }
        holder.txtInvNo.setText(object.getPattern()+"-"+ object.getSerial()+"-"+object.getInvNum());
        holder.txtNameCus.setText(object.getCusname());
        if (object.getUnitCurrency()==null)
        {

        }
        holder.txtCusCode.setText(""+object.getCusCode());
        holder.txtPrice.setText(Helper.formatPrice(object.getAmount()) + " " + object.getUnitCurrency());
        holder.txtAddress.setText("" + object.getCusAddress());
        if (object.getPaymentStatus() == 1) {
            holder.imageTag.setImageResource(R.drawable.tag_green);
        } else {
            holder.imageTag.setImageResource(R.drawable.tag_red);
        }

    }

    public void sortBy(boolean isAscending, int typeFilter) {
        if (typeFilter == Common.FILTER_BY_NAME_CUSTOMER) {
            if (isAscending) {
                Collections.sort(arrHoaDon, new Comparator<InvoiceCadmin>() {
                    public int compare(InvoiceCadmin o1, InvoiceCadmin o2) {
                        return o1.getCusname().compareTo(o2.getCusname());
                    }
                });
            } else {
                Collections.sort(arrHoaDon, new Comparator<InvoiceCadmin>() {
                    public int compare(InvoiceCadmin o1, InvoiceCadmin o2) {
                        return o2.getCusname().compareTo(o1.getCusname());
                    }
                });
            }
        } else {
            if (isAscending) {
                //Sorting Invoice price number Ascending Order
                Collections.sort(arrHoaDon, new Comparator<InvoiceCadmin>() {
                    public int compare(InvoiceCadmin obj1, InvoiceCadmin obj2) {
                        return (obj1.getAmount() < obj2.getAmount()) ? -1 : (obj1.getAmount() > obj2.getAmount()) ? 1 : 0;
                    }
                });
            } else {
                //Sorting Invoice price number Descending Order
                Collections.sort(arrHoaDon, new Comparator<InvoiceCadmin>() {
                    public int compare(InvoiceCadmin obj1, InvoiceCadmin obj2) {
                        return (obj1.getAmount() > obj2.getAmount()) ? -1 : (obj1.getAmount() > obj2.getAmount()) ? 1 : 0;
                    }
                });
            }
        }
        notifyDataSetChanged();
    }

    public void addItem(InvoiceCadmin item) {
        arrHoaDon.add(item);
    }

    public void addAllData(ArrayList<InvoiceCadmin> listTB) {
        if (listTB != null) {
            arrHoaDon.addAll(listTB);
            notifyDataSetChanged();
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
        public TextView txtInvNo;
        public TextView txtPrice;
        public ImageView imageTag;
        public TextView txtCusCode;
    }

}