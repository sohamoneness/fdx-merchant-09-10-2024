package com.oneness.fdxmerchant.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.Order.OrderDetails;
import com.oneness.fdxmerchant.Models.OrderModels.DeliveredOrderModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrdersModel;
import com.oneness.fdxmerchant.Models.OrderModels.OngoingOrderModel;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DeliveredOrderAdapter extends RecyclerView.Adapter<DeliveredOrderAdapter.Hold> {

    List<DeliveredOrderModel> ordList;
    Context context;
    NewOrderItemAdapter newOrderItemAdapter;
    int count = 10;

    public DeliveredOrderAdapter(FragmentActivity activity, List<DeliveredOrderModel> delOrderList) {
        this.context = activity;
        this.ordList = delOrderList;
    }


    @NonNull
    @Override
    public DeliveredOrderAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_order_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveredOrderAdapter.Hold holder, int position) {

        DeliveredOrderModel om = ordList.get(position);
        holder.btnAcceptLL.setVisibility(View.GONE);
        holder.deliveryLL.setVisibility(View.GONE);
        holder.btnNextLL.setVisibility(View.GONE);
        holder.tvTime.setText(getFormattedDate(om.created_at));
       /* Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(om.created_at);
            String newstring = new SimpleDateFormat("dd MMM, yyyy hh:mm aa").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        holder.tvId.setText("ID: " + om.unique_id);
        holder.tvStatus.setText("Delivered");
        holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.delivered_stat_bg));
        holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
        //String formatted = formatter1.format(Double.parseDouble(om.total_amount));
        String formatted = formatter1.format(Double.parseDouble(om.amount));
        holder.tvOrderAmount.setText("₹ " + formatted);


        if (om.notes != null){
            if (om.notes.equals("")){
                holder.tvNotes.setVisibility(View.GONE);
            }else {
                holder.tvNotes.setVisibility(View.VISIBLE);
                holder.tvNotes.setText(om.notes);
            }
        }else {
            holder.tvNotes.setVisibility(View.GONE);
        }


        if (om.packing_price != null){
            String packPrice = formatter1.format(Double.parseDouble(om.packing_price));
            if (packPrice.equals(".00")){
                packPrice = "0.00";
            }
            holder.tvPacking.setText("₹ " + packPrice);
        }else {
            holder.tvPacking.setText("₹ " + "0.00");
        }


        if (om.cutlery_charge != null){
            String cutPrice = formatter1.format(Double.parseDouble(om.cutlery_charge));
            if (cutPrice.equals(".00")){
                cutPrice = "0.00";
            }
            holder.tvCutlery.setText("₹ " + cutPrice);
        }else {
            holder.tvCutlery.setText("₹ " + "0.00");
        }

        /*String formatted6 = formatter1.format(Double.parseDouble(om.restaurant_commission)
                + Double.parseDouble(om.tax_amount)
                - Double.parseDouble(om.restaurant_share_commission)
                + Double.parseDouble(om.cutlery_charge)
                + Double.parseDouble(om.packing_price));
        if (formatted6.equals(".00")){
            formatted6 = "0.00";
        }
        holder.tvTotPay.setText("₹ " + formatted6);
        holder.tvTotBill.setText("₹ " + formatted6);*/

        String formatted6 = formatter1.format(Double.parseDouble(om.amount)
                + Double.parseDouble(om.tax_amount)
                + Double.parseDouble(om.cutlery_charge)
                + Double.parseDouble(om.packing_price)
                - Double.parseDouble(om.restaurant_share_commission));

        if (formatted6.equals(".00")){
            formatted6 = "0.00";
        }
        holder.tvTotPay.setText("₹ " + formatted6);
        holder.tvTotBill.setText("₹ " + formatted6);

        /*String formatted1 = formatter1.format(Double.parseDouble(om.restaurant_commission));
        if (formatted1.equals(".00")){
            formatted1 = "0.00";
        }
        holder.tvCommission.setText("₹ " + formatted1);
        holder.tvTotBill.setText("₹ " + formatted1);
        holder.tvTotPay.setText("₹ " + formatted1);*/
        String formatted2 = formatter1.format(Double.parseDouble(om.discounted_amount));
        if (formatted2.equals(".00")){
            formatted2 = "0.00";
        }
        holder.tvDiscount.setText("₹ " + formatted2);
        String formatted5 = formatter1.format(Double.parseDouble(om.restaurant_share_commission));
        if (formatted5.equals(".00")){
            formatted5 = "0.00";
        }
        holder.tvPromoDiscount.setText("₹ " + formatted5);
        /*if (om.coupon_code != null){
            holder.tvUseCode.setText("[ Coupon code: " + om.coupon_code + " ]");
            holder.tvUseCode.setVisibility(View.VISIBLE);
        }else {
            holder.tvUseCode.setVisibility(View.GONE);
        }*/

        String formatted3 = formatter1.format(Double.parseDouble(om.tax_amount));
        if (formatted3.equals(".00")){
            formatted3 = "0.00";
        }
        holder.tvTax.setText("₹ " + formatted3);

        String formatted4 = formatter1.format(Double.parseDouble(om.amount));
        holder.tvSubTot.setText("₹ " + formatted4);

        if (om.order_type.equals("2")){
            holder.tvOrderType.setVisibility(View.VISIBLE);
            holder.tvOrderType.setText("Takeaway");
        }else {
            holder.tvOrderType.setVisibility(View.GONE);
        }

        if (om.items.size()>0){
            newOrderItemAdapter = new NewOrderItemAdapter(context, om.items);
            holder.itemRv.setLayoutManager(new LinearLayoutManager((Activity)context, LinearLayoutManager.VERTICAL, false));
            holder.itemRv.setAdapter(newOrderItemAdapter);
        }

        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetails.ordId = om.id;
                Constants.isNewOrder = 2;
                ((Activity)context).startActivity(new Intent(((Activity)context), OrderDetails.class));
                //((Activity)context).finish();
            }
        });

        holder.totBillLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.billLL.getVisibility() == View.GONE){
                    holder.ivArrow.setRotation((float) 180.0);
                    //holder.ivArrow.startAnimation(animRotate);
                    holder.billLL.setVisibility(View.VISIBLE);
                }else {
                    //holder.ivArrow.startAnimation(antiAnimRotate);
                    holder.ivArrow.setRotation((float) 0.0);
                    holder.billLL.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return ordList.size();
        //return count;
    }
    private String getFormattedDate(String created_at) {
        String newstring = "";
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(created_at);
            newstring = new SimpleDateFormat("dd MMM, yyyy hh:mm aa").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newstring;
    }


    public class Hold extends RecyclerView.ViewHolder {

        TextView tvId, tvIdEnd, tvStatus, tvOrderAmount, tvTime, tvOrderType;
        TextView tvTotBill, tvSubTot, tvTotPay, tvCommission, tvPromoDiscount;
        TextView tvUseCode, tvDiscount, tvTax, tvNotes;
        RecyclerView itemRv;
        Button btnNext;
        ImageView ivArrow;
        LinearLayout btnAcceptLL, btnNextLL, deliveryLL, mainLL, billLL, totBillLL;
        TextView tvPacking, tvCutlery;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvIdEnd = itemView.findViewById(R.id.tvIdEnd);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOrderAmount = itemView.findViewById(R.id.tvOrderAmount);
            itemRv = itemView.findViewById(R.id.itemRv);
            tvTotBill = itemView.findViewById(R.id.tvTotBill);
            tvSubTot = itemView.findViewById(R.id.tvSubTot);
            tvTax = itemView.findViewById(R.id.tvTax);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvUseCode = itemView.findViewById(R.id.tvUseCode);
            tvPromoDiscount = itemView.findViewById(R.id.tvPromoDiscount);
            tvCommission = itemView.findViewById(R.id.tvCommission);
            tvTotPay = itemView.findViewById(R.id.tvTotPay);
            //btnReject = itemView.findViewById(R.id.btnReject);
            btnNext = itemView.findViewById(R.id.btnNext);
            btnNextLL = itemView.findViewById(R.id.btnNextLL);
            btnAcceptLL = itemView.findViewById(R.id.btnAcceptLL);
            deliveryLL = itemView.findViewById(R.id.deliveryLL);
            mainLL = itemView.findViewById(R.id.mainLL);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            billLL = itemView.findViewById(R.id.billLL);
            totBillLL = itemView.findViewById(R.id.totBillLL);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            tvOrderType = itemView.findViewById(R.id.tvOrderType);
            tvCutlery = itemView.findViewById(R.id.tvCutlery);
            tvPacking = itemView.findViewById(R.id.tvPacking);

        }
    }
}
