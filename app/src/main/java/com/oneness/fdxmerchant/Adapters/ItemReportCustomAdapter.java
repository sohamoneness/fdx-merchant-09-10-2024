package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportModel;
import com.oneness.fdxmerchant.R;

import java.text.DecimalFormat;
import java.util.List;

public class ItemReportCustomAdapter extends RecyclerView.Adapter<ItemReportCustomAdapter.Hold> {

    List<ItemReportModel> irList;
    Context context;
    int count = 15;

    public ItemReportCustomAdapter(FragmentActivity activity, List<ItemReportModel> itemReportList) {
        this.context = activity;
        this.irList = itemReportList;
    }

    @NonNull
    @Override
    public ItemReportCustomAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemReportCustomAdapter.Hold holder, int position) {

        ItemReportModel irm = irList.get(position);

        holder.tvName.setText(irm.name);
        holder.tvCat.setText("Biriyani");
        holder.tvTotOrder.setText("Total Order: " + irm.total_order_count);
        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted = formatter1.format(Double.parseDouble(irm.total_order_amount));
        if (formatted.equals(".00")){
            formatted = "0.00";
        }
        holder.tvOrderAmount.setText("\u20B9 " + formatted);
        String formatted1 = formatter1.format(Double.parseDouble(irm.price));
        if (formatted1.equals(".00")){
            formatted1 = "0.00";
        }
        holder.tvUnitPrice.setText("Unit Price: \u20B9 " + formatted1);

    }

    @Override
    public int getItemCount() {
        return irList.size();
        //return count;
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvName, tvTotOrder, tvOrderAmount, tvCat,tvUnitPrice;
        public Hold(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvCat = itemView.findViewById(R.id.tvCat);
            tvTotOrder = itemView.findViewById(R.id.tvTotOrder);
            tvOrderAmount = itemView.findViewById(R.id.tvOrderAmount);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);

        }
    }
}
