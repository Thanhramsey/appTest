package com.vnpt.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vnpt.common.Common;
import com.vnpt.dto.InvoiceHDDTDetails;
import com.vnpt.dto.ProductInvoiceDetails;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.R;
import com.vnpt.utils.DateTimeUtil;
import com.vnpt.utils.Helper;

import java.util.ArrayList;


/**
 * @Description: adapter cua list book
 * @author:truonglt2
 * @since:Feb 7, 2014 3:49:14 PM
 * @version: 1.0
 * @since: 1.0
 */

public class ListExpandableInvoiceAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<InvoiceHDDTDetails> parents = new ArrayList<>();
    OnEventControlListener mListener;

    public ListExpandableInvoiceAdapter(Context context, OnEventControlListener listener) {
        this.mContext = context;
        this.mListener = listener;
        // Create Layout Inflator
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // This Function used to inflate parent rows view

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parentView) {
        ViewParentHolder holder = null;
        if (convertView == null) {
            holder = new ViewParentHolder();
            convertView = mInflater.inflate(R.layout.item_list_debit, null);
            holder.txtNameService = (TextView) convertView.findViewById(R.id.txtNameService);
            holder.txtPriceService = (TextView) convertView.findViewById(R.id.txtPriceService);
            convertView.setTag(holder);
        } else {
            holder = (ViewParentHolder) convertView.getTag();
        }
        setDataParentViews(groupPosition, parents.get(groupPosition), (ViewParentHolder) convertView.getTag());
        // measure ListView item (to solve 'ListView inside ScrollView' problem)
        convertView.measure(View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return convertView;
    }

    private void setDataParentViews(final int posi, final InvoiceHDDTDetails object, final ViewParentHolder holder) {
        holder.txtNameService.setText(object.getInvoiceName());
        holder.txtPriceService.setText("" + Helper.formatPrice(object.getTotal()) + Common.CURRENCY);
//        holder.cbxStatus.setChecked(object.isChecked());
//        holder.cbxStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    object.setChecked(true);
//                } else {
//                    object.setChecked(false);
//                }
//                mListener.onEvent(ActionEventConstant.ACTION_CHECK_ALL_DEBIT_INVOICE, null, "" + posi);
//            }
//        });
    }

    // This Function used to inflate child rows view
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parentView) {
        final InvoiceHDDTDetails parent = parents.get(groupPosition);
//        final ServiceDebit child = parent.getItemInvoice().get(childPosition);
        ViewChildHolder holder = null;
        if (convertView == null) {
            holder = new ViewChildHolder();
            convertView = mInflater.inflate(R.layout.child_row_telecome, null);
            holder.txtNumberInvoice = (TextView) convertView.findViewById(R.id.txtNumberInvoice);
            holder.txtUnitConsume = (TextView) convertView.findViewById(R.id.txtUnitConsume);
            holder.txtContentConsume = (TextView) convertView.findViewById(R.id.txtContentConsume);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtTax = (TextView) convertView.findViewById(R.id.txtTax);
            holder.txtSumService = (TextView) convertView.findViewById(R.id.txtSumService);
            holder.txtSumMoney = (TextView) convertView.findViewById(R.id.txtSumMoney);
            holder.listDataService = (ListView) convertView.findViewById(R.id.listDataService);
            convertView.setTag(holder);
        } else {
            holder = (ViewChildHolder) convertView.getTag();
        }
        setDataChildViews(groupPosition, childPosition, parent, (ViewChildHolder) convertView.getTag());
        // measure ListView item (to solve 'ListView inside ScrollView' problem)
        convertView.measure(View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return convertView;
    }

    void setDataChildViews(int groupPosition, int childPosition, InvoiceHDDTDetails itemParent, final ViewChildHolder holder) {
        ListServicesAdapter adapter = new ListServicesAdapter(mContext, itemParent.getArrayListProduct());
        double sumPrice = itemParent.getTotal();
        adapter.notifyDataSetChanged();
        holder.listDataService.setAdapter(adapter);
        holder.txtNumberInvoice.setText("" + itemParent.getInvoiceNo());
        double tax = itemParent.getVAT_Amount();
        holder.txtTax.setText("" + Helper.formatPrice(tax) + Common.CURRENCY);
        double sumValPrice = adapter.getSumMoney() + tax;
        holder.txtSumMoney.setText("" + Helper.formatPrice(sumValPrice) + Common.CURRENCY);
        holder.txtSumService.setText("" + Helper.formatPrice(adapter.getSumMoney()) + Common.CURRENCY);
        holder.txtPrice.setText("" + Helper.formatPrice(sumValPrice) + Common.CURRENCY);
        holder.txtContentConsume.setText("" + DateTimeUtil.getCurrentMonth(itemParent.getArisingDate(), Common.DATETIME_FORMAT_PATTERN_1));
        holder.listDataService.setScrollContainer(false);
    }

    @Override
    public ProductInvoiceDetails getChild(int groupPosition, int childPosition) {
        Log.i("Childs", groupPosition + "=  getChild ==" + childPosition);
        final ProductInvoiceDetails parent = parents.get(groupPosition).getArrayListProduct().get(childPosition);
        return parent;
//        return parents.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 0;
        if (parents.get(groupPosition).getArrayListProduct() != null)
            size = parents.get(groupPosition).getArrayListProduct().size();
        return size;
    }


    @Override
    public InvoiceHDDTDetails getGroup(int groupPosition) {
        Log.i("Parent", groupPosition + "=  getGroup ");

        return parents.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return parents.size();
    }

    //Call when parent row clicked
    @Override
    public long getGroupId(int groupPosition) {
//        Log.i("Parent", groupPosition + "=  getGroupId " + ParentClickStatus);
//
//        if (groupPosition == 2 && ParentClickStatus != groupPosition) {
//
//            //Alert to user
//            Toast.makeText(getApplicationContext(), "Parent :" + groupPosition,
//                    Toast.LENGTH_LONG).show();
//        }
//
//        ParentClickStatus = groupPosition;
//        if (ParentClickStatus == 0)
//            ParentClickStatus = -1;

        return groupPosition;
    }

    @Override
    public void notifyDataSetChanged() {
        // Refresh List rows
        super.notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return ((parents == null) || parents.isEmpty());
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    public void clearAllData() {
        parents.clear();
        notifyDataSetChanged();
    }

    public void addItem(InvoiceHDDTDetails item) {
        this.parents.add(item);
    }
    /***********************************************************************/


    /**
     * @author:truonglt2
     * @since:Feb 7, 2014 3:09:01 PM
     * @Description: lop holder temp
     */
    class ViewParentHolder {
        public TextView txtNameService;
        public TextView txtPriceService;
    }

    class ViewChildHolder {
        public TextView txtNumberInvoice;
        public TextView txtUnitConsume;
        public TextView txtContentConsume;
        public TextView txtPrice;
        public TextView txtTax;
        public TextView txtSumService;
        public TextView txtSumMoney;
        public ListView listDataService;
    }

//    public double getSumMoney() {
//        double sum = 0;
//        for (int i = 0; i < parents.size(); i++) {
//            sum += parents.get(i).getTotalPrice();
//        }
//        return sum;
//    }

//    public int getItemChecked() {
//        int count = 0;
//        for (ItemDebit item : parents) {
//            if (item.isChecked()) {
//                count += 1;
//            }
//        }
//        return count;
//    }
//    public ArrayList<Integer> getAllIdInvoiceChecked() {
//        ArrayList<Integer> arrInt = new ArrayList<>();
//        for (ItemDebit item : parents) {
//            if (item.isChecked()) {
//                arrInt.add(item.getIdInvoices());
//            }
//        }
//        return arrInt;
//    }

//    public double getMoneyItemChecked() {
//        double sum = 0;
//        for (ItemDebit item : parents) {
//            if (item.isChecked()) {
//                sum += item.getTotalPrice();
//            }
//        }
//        return sum;
//    }

//    public void setCheckedWithPosion(int position, boolean isChecked) {
//        for (int i = 0; i < parents.size(); i++) {
//            if (i == position) {
//                parents.get(i).setChecked(isChecked);
//                break;
//            }
//        }
//    }
//
//    public void setCheckAll(boolean isChecked) {
//        for (int i = 0; i < parents.size(); i++) {
//            parents.get(i).setChecked(isChecked);
//        }
//    }
}