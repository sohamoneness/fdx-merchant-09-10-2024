package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderItemModel;
import com.oneness.fdxmerchant.R;

import java.text.DecimalFormat;
import java.util.List;

public class OrderReportsItemsAdapter extends RecyclerView.Adapter<OrderReportsItemsAdapter.Hold> {
    List<DateWiseOrderItemModel> orderItemList;
    Context context;
    double priceWithAddOn = 0.0;

    public OrderReportsItemsAdapter(Context context, List<DateWiseOrderItemModel> items) {
        this.context = context;
        this.orderItemList = items;
    }

    @NonNull
    @Override
    public OrderReportsItemsAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_report_items_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderReportsItemsAdapter.Hold holder, int position) {

        DateWiseOrderItemModel dwoim = orderItemList.get(position);

        holder.tvItem.setText(dwoim.product_name);

        if (dwoim.add_on_name != null && dwoim.add_on_name2 != null){

            if (dwoim.add_on_name.equals("") && dwoim.add_on_name2.equals("")) {
                holder.tvAddItem.setVisibility(View.GONE);
            } else if (!dwoim.add_on_name.equals("") && dwoim.add_on_name2.equals("")) {
                holder.tvAddItem.setVisibility(View.VISIBLE);
                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                String formatted1 = formatter1.format(Double.parseDouble(dwoim.add_on_price));
                if (formatted1.equals(".00")) {
                    formatted1 = "0.00";
                }
                holder.tvAddItem.setText("Add-on: " + dwoim.add_on_name+ "(" + "\u20B9" + formatted1 + ")");
            } else if (!dwoim.add_on_name.equals("") && !dwoim.add_on_name2.equals("")) {
                holder.tvAddItem.setVisibility(View.VISIBLE);
                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                String formatted = formatter1.format(Double.parseDouble(dwoim.add_on_price2));
                if (formatted.equals(".00")) {
                    formatted = "0.00";
                }

                String formatted1 = formatter1.format(Double.parseDouble(dwoim.add_on_price));
                if (formatted1.equals(".00")) {
                    formatted1 = "0.00";
                }
                holder.tvAddItem.setText("Add-ons: " + dwoim.add_on_name + "(" + "\u20B9" + formatted1 + ")" + " & " + dwoim.add_on_name2 + "(" + "\u20B9" + formatted + ")");
            }

        }else if (dwoim.add_on_name != null) {

            if (dwoim.add_on_name.equals("")) {
                holder.tvAddItem.setVisibility(View.GONE);
            } else if (!dwoim.add_on_name.equals("")) {
                holder.tvAddItem.setVisibility(View.VISIBLE);
                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                String formatted1 = formatter1.format(Double.parseDouble(dwoim.add_on_price));
                if (formatted1.equals(".00")) {
                    formatted1 = "0.00";
                }
                holder.tvAddItem.setText("Add-on: " + dwoim.add_on_name+ "(" + "\u20B9" + formatted1 + ")");
            }

        }else {
            holder.tvAddItem.setVisibility(View.GONE);
        }



        if (dwoim.add_on_name != null && dwoim.add_on_name2 != null){

            if (dwoim.add_on_name.equals("") && dwoim.add_on_name2.equals("")) {
                priceWithAddOn = Double.parseDouble(dwoim.price);
            } else if (!dwoim.add_on_name.equals("") && dwoim.add_on_name2.equals("")) {

                priceWithAddOn = Double.parseDouble(dwoim.price) + Double.parseDouble(dwoim.add_on_price);

            } else if (!dwoim.add_on_name.equals("") && !dwoim.add_on_name2.equals("")) {
                priceWithAddOn = Double.parseDouble(dwoim.price) + Double.parseDouble(dwoim.add_on_price) + Double.parseDouble(dwoim.add_on_price2);

            }

        }else if (dwoim.add_on_name != null) {
            if (!dwoim.add_on_name.equals("")) {
               priceWithAddOn = Double.parseDouble(dwoim.price) + Double.parseDouble(dwoim.add_on_price);

            } else {
                priceWithAddOn = Double.parseDouble(dwoim.price);
           }

        }else {
            priceWithAddOn = Double.parseDouble(dwoim.price);
        }

        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted = formatter1.format(priceWithAddOn);
        if (formatted.equals(".00")) {
            formatted = "0.00";
        }

        holder.tvQty.setText(dwoim.quantity + " X " + "\u20B9" + formatted);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvItem, tvQty, tvAddItem;

        public Hold(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
            tvAddItem = itemView.findViewById(R.id.tvAddItem);
            tvQty = itemView.findViewById(R.id.tvQty);
        }
    }
}
