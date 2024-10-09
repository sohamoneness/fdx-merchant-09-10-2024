package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.Order.NewOrderModule.OrderByMerchantList;
import com.oneness.fdxmerchant.R;

import java.util.ArrayList;
import java.util.List;

public class MerchantOrderListAdapter extends RecyclerView.Adapter<MerchantOrderListAdapter.Hold> {

    Context context;
    List<String> orderList;

    public MerchantOrderListAdapter(OrderByMerchantList orderByMerchantList, List<String> orderList) {
        this.context = orderByMerchantList;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MerchantOrderListAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.placed_order_list_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchantOrderListAdapter.Hold holder, int position) {


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvId, tvTime, tvOrderAmount, tvCustName, tvCustAdr, tvContact, tvDelBoy;
        public Hold(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvOrderAmount = itemView.findViewById(R.id.tvOrderAmount);
            tvCustName = itemView.findViewById(R.id.tvCustName);
            tvCustAdr = itemView.findViewById(R.id.tvCustAdr);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvDelBoy = itemView.findViewById(R.id.tvDelBoy);

        }
    }
}
