package com.vnpt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vnpt.dto.ProductInvoiceDetails;
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

public class ListProdInvAdapter extends BaseAdapter {

    private ArrayList<ProductInvoiceDetails> arrItem = new ArrayList<>();

    private LayoutInflater mInflater;
    Context mContext;

    /**
     * @param context
     */
    public ListProdInvAdapter(Context context, ArrayList<ProductInvoiceDetails> arr) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrItem = arr;
    }

    public ListProdInvAdapter(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void clearAllData() {
        arrItem.clear();
        notifyDataSetChanged();
    }

    public void addItem(ProductInvoiceDetails item) {
        arrItem.add(item);
    }

    @Override
    public int getCount() {

        return arrItem.size();
    }

    @Override
    public ProductInvoiceDetails getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_list_product_invoice, null);
            holder.txtProdName = (TextView) convertView.findViewById(R.id.txtProdName);
            holder.txtProdUnit = (TextView) convertView.findViewById(R.id.txtProdUnit);
            holder.txtProdPrice = (TextView) convertView.findViewById(R.id.txtProdPrice);
            holder.txtProdQuantity = (TextView) convertView.findViewById(R.id.txtProdQuantity);
            holder.txtProdAmount = (TextView) convertView.findViewById(R.id.txtProdAmount);
            holder.containerLayout = (LinearLayout) convertView.findViewById(R.id.container_item);
            holder.containerProdVat = (LinearLayout) convertView.findViewById(R.id.ln_prod_vat);
            holder.containerProdDiscount = (LinearLayout) convertView.findViewById(R.id.ln_prod_discount);


            holder.txtProdDiscount = (TextView) convertView.findViewById(R.id.txtProdDiscount);
            holder.txtProdDiscountAmount = (TextView) convertView.findViewById(R.id.txtProdDiscountAmount);
            holder.txtProdVATRate = (TextView) convertView.findViewById(R.id.txtProdVATRate);
            holder.txtProdVATAmount = (TextView) convertView.findViewById(R.id.txtProdVATAmount);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setDataViews(position, getItem(position), (ViewHolder) convertView.getTag());

        return convertView;
    }

    private void setDataViews(final int posi, final ProductInvoiceDetails object, final ViewHolder holder) {
        if (posi % 2 == 0) {
            holder.containerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.containerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.blue_hightlight));
        }
        holder.txtProdName.setText(object.getProdName());
        holder.txtProdUnit.setText(object.getProdUnit());
//        holder.txtProdPrice.setText(Helper.formatPrice(object.getProdPrice()));
        holder.txtProdPrice.setText("" + Helper.formatPrice(object.getProdPrice()));
        holder.txtProdQuantity.setText("" + (int) object.getProdQuantity());
        holder.txtProdAmount.setText("" + Helper.formatPrice(object.getAmount()));

        holder.txtProdDiscount.setText("" + object.getDiscount() + "%");
        holder.txtProdDiscountAmount.setText("" + Helper.formatPrice(object.getDiscountAmount()));
        holder.txtProdVATRate.setText("" + object.getVATRate() + "%");
        holder.txtProdVATAmount.setText("" + Helper.formatPrice(object.getVATAmount()));

        if(object.getDiscountAmount()==0){
            holder.containerProdDiscount.setVisibility(View.GONE);
        }
        if(object.getVATAmount()==0){
            holder.containerProdVat.setVisibility(View.GONE);
        }
    }


    /**
     * @author:truonglt2
     * @since:Feb 7, 2014 3:09:01 PM
     * @Description: lop holder temp
     */
    class ViewHolder {
        public TextView txtProdName;
        public TextView txtProdUnit;
        public TextView txtProdPrice;
        public TextView txtProdQuantity;
        public TextView txtProdAmount;
        public TextView txtProdDiscount;
        public TextView txtProdDiscountAmount;
        public TextView txtProdVATRate;
        public TextView txtProdVATAmount;
        public LinearLayout containerLayout;
        public LinearLayout containerProdVat;
        public LinearLayout containerProdDiscount;
    }

}