package com.vnpt.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vnpt.common.Common;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.listener.OnLoadMoreListener;
import com.vnpt.staffhddtpos58.R;
import com.vnpt.utils.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecyclerViewInvoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<InvoiceCadmin> mItemList;
    private List<InvoiceCadmin> invoiceListFiltered;

    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    Context mContext;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean isSearching;

    public RecyclerViewInvoiceAdapter(RecyclerView recyclerView, List<InvoiceCadmin> invoiceLists, Context context) {
        this.mItemList = invoiceLists;
        this.invoiceListFiltered = invoiceLists;
        this.mContext = context;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("totalItemCount","1:"+linearLayoutManager.getItemCount());
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.d("totalItemCount","2:"+totalItemCount);
                Log.d("lastVisibleItem","2:"+lastVisibleItem);
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return invoiceListFiltered.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_hoadon, parent, false);
            return new InvoiceViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int posi) {
        if (holder instanceof InvoiceViewHolder) {
            InvoiceCadmin object = invoiceListFiltered.get(posi);
            InvoiceViewHolder invoiceHolder = (InvoiceViewHolder) holder;
            if (posi % 2 == 0) {
                invoiceHolder.containerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            } else {
                invoiceHolder.containerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.blue_hightlight));
            }
            invoiceHolder.txtInvNo.setText(object.getPattern()+"-"+ object.getSerial()+"-"+object.getInvNum());
            invoiceHolder.txtNameCus.setText(object.getCusname());
            if (object.getUnitCurrency()==null)
            {

            }
            invoiceHolder.txtCusCode.setText(""+object.getCusCode());
            invoiceHolder.txtPrice.setText(Helper.formatPrice(object.getAmount()) + " " + object.getUnitCurrency());
            invoiceHolder.txtAddress.setText("" + object.getCusAddress());
            if (object.getPaymentStatus() == 1) {
                invoiceHolder.imageTag.setImageResource(R.drawable.tag_green);
            } else {
                invoiceHolder.imageTag.setImageResource(R.drawable.tag_red);
            }
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }
    public void sortBy(boolean isAscending, int typeFilter) {
        if (typeFilter == Common.FILTER_BY_NAME_CUSTOMER) {
            if (isAscending) {
                Collections.sort(invoiceListFiltered, new Comparator<InvoiceCadmin>() {
                    public int compare(InvoiceCadmin o1, InvoiceCadmin o2) {
                        return o1.getCusname().compareTo(o2.getCusname());
                    }
                });
            } else {
                Collections.sort(invoiceListFiltered, new Comparator<InvoiceCadmin>() {
                    public int compare(InvoiceCadmin o1, InvoiceCadmin o2) {
                        return o2.getCusname().compareTo(o1.getCusname());
                    }
                });
            }
        } else {
            if (isAscending) {
                //Sorting Invoice price number Ascending Order
                Collections.sort(invoiceListFiltered, new Comparator<InvoiceCadmin>() {
                    public int compare(InvoiceCadmin obj1, InvoiceCadmin obj2) {
                        return (obj1.getAmount() < obj2.getAmount()) ? -1 : (obj1.getAmount() > obj2.getAmount()) ? 1 : 0;
                    }
                });
            } else {
                //Sorting Invoice price number Descending Order
                Collections.sort(invoiceListFiltered, new Comparator<InvoiceCadmin>() {
                    public int compare(InvoiceCadmin obj1, InvoiceCadmin obj2) {
                        return (obj1.getAmount() > obj2.getAmount()) ? -1 : (obj1.getAmount() > obj2.getAmount()) ? 1 : 0;
                    }
                });
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return invoiceListFiltered == null ? 0 : invoiceListFiltered.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
    public void clearAllData() {
        mItemList.clear();
        invoiceListFiltered.clear();
        notifyDataSetChanged();
    }
    public void addAllData(ArrayList<InvoiceCadmin> listTB) {
        if (listTB != null) {
            mItemList.addAll(listTB);
            notifyDataSetChanged();
        }
    }
    public void removeLastItem(){
        mItemList.remove(mItemList.size()-1);
        invoiceListFiltered.remove(invoiceListFiltered.size()-1);
        notifyItemRemoved(mItemList.size()-1);
        notifyItemRemoved(invoiceListFiltered.size()-1);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()|| charString.equals("")) {
                    invoiceListFiltered = mItemList;
                } else {
                    List<InvoiceCadmin> filteredList = new ArrayList<>();
                    for (InvoiceCadmin row : mItemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCusCode().toLowerCase().contains(charString.toLowerCase()) || row.getCusname().contains(charString.toLowerCase())||row.getInvNum().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    invoiceListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = invoiceListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                invoiceListFiltered = (ArrayList<InvoiceCadmin>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }

    private class InvoiceViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout containerLayout;
        public TextView txtNameCus;
        public TextView txtAddress;
        public TextView txtInvNo;
        public TextView txtPrice;
        public ImageView imageTag;
        public TextView txtCusCode;

        public InvoiceViewHolder(View convertView) {
            super(convertView);
            containerLayout = (RelativeLayout) convertView.findViewById(R.id.container_item);
            txtNameCus = (TextView) convertView.findViewById(R.id.txtNameCus);
            txtPrice = (TextView) convertView
                    .findViewById(R.id.txtPrice);
            txtAddress = (TextView) convertView
                    .findViewById(R.id.txtAddress);
            txtCusCode = (TextView) convertView
                    .findViewById(R.id.txtCusCode);
            imageTag = (ImageView) convertView
                    .findViewById(R.id.imageTag);
            txtInvNo = (TextView) convertView.findViewById(R.id.txtInvNo);
        }
    }


}
