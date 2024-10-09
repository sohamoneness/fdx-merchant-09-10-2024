package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModel;
import com.oneness.fdxmerchant.R;

import java.text.DecimalFormat;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.Hold> {

    Context mContext;
    List<OrderItemsModel> itemList;
    String from = "";
    double totPrice = 0.0, price = 0.0;
    int qty = 0;

    public OrderItemAdapter(Context context, List<OrderItemsModel> orderItemModelList) {

        this.mContext = context;
        this.itemList = orderItemModelList;

    }




    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_order_item_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        OrderItemsModel oim = itemList.get(position);
        qty = Integer.parseInt(oim.quantity);
        price = Double.parseDouble(oim.price);
        totPrice = price * qty;

        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted1 = formatter1.format(totPrice);
        if (formatted1.equals(".00")){
            formatted1 = "0.00";
        }

        holder.tvItem.setText(oim.quantity + " X " +oim.product_name);
        //holder.tvExtra.setVisibility(View.GONE);
       /* DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted = formatter1.format(Double.parseDouble(oim.price));*/
        //holder.tvPrice.setText("\u20B9" + " " + formatted1);

        if (oim.add_on_quantity != null && oim.add_on_quantity2 != null){
            if (!oim.add_on_quantity.equals("0") && !oim.add_on_quantity2.equals("0")) {
                holder.tvAddOn.setVisibility(View.VISIBLE);
                holder.tvAddOn.setText("Add-on: \n" + oim.add_on_name + " (" + oim.add_on_quantity + " X " + "\u20B9" +oim.add_on_price + ")" + " & " + oim.add_on_name2 + " (" + oim.add_on_quantity2 + " X " + "\u20B9" +oim.add_on_price2 + ")");
                holder.tvPrice.setText("\u20B9"+String.valueOf((Double.parseDouble(oim.price) + Double.parseDouble(oim.add_on_price) + Double.parseDouble(oim.add_on_price2))*Double.parseDouble(oim.quantity)));
            } else if (!oim.add_on_quantity.equals("0") && oim.add_on_quantity2.equals("0")) {
                holder.tvAddOn.setVisibility(View.VISIBLE);
                holder.tvAddOn.setText("Add-on: \n" + oim.add_on_name + " (" + oim.add_on_quantity + " X " + "\u20B9" +oim.add_on_price + ")");
                holder.tvPrice.setText("\u20B9"+String.valueOf((Double.parseDouble(oim.price) + Double.parseDouble(oim.add_on_price))*Double.parseDouble(oim.quantity)));
            } else {
                holder.tvAddOn.setVisibility(View.GONE);
                holder.tvPrice.setText("\u20B9"+String.valueOf(Double.parseDouble(oim.price)*Double.parseDouble(oim.quantity)));
            }
        }else if (oim.add_on_quantity != null){
            if (!oim.add_on_quantity.equals("0")) {
                holder.tvAddOn.setVisibility(View.VISIBLE);
                holder.tvAddOn.setText("Add-on: \n" + oim.add_on_name + " (" + oim.add_on_quantity + " X " + "\u20B9" +oim.add_on_price + ")");
                holder.tvPrice.setText("\u20B9" + String.valueOf((Double.parseDouble(oim.price) + Double.parseDouble(oim.add_on_price)) * Double.parseDouble(oim.quantity)));
            }
        }else {
            holder.tvAddOn.setVisibility(View.GONE);
            holder.tvPrice.setText("\u20B9"+String.valueOf(Double.parseDouble(oim.price)*Double.parseDouble(oim.quantity)));
        }
       /* if (oim.isVeg == true){
            holder.ivVeg.setVisibility(View.VISIBLE);
            holder.ivNonVeg.setVisibility(View.GONE);
        }else{
            holder.ivVeg.setVisibility(View.GONE);
            holder.ivNonVeg.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        /*ImageView ivVeg, ivNonVeg;
        TextView tvItemName, tvExtra, tvAmount;*/
        TextView tvItem, tvPrice, tvAddOn;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.tvItem);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAddOn = itemView.findViewById(R.id.tvAddOn);

        }
    }
}
