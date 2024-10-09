package com.oneness.fdxmerchant.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.Order.DispatchOrderActivity;
import com.oneness.fdxmerchant.Activity.Order.OrderDetails;
import com.oneness.fdxmerchant.Models.OrderModels.OngoingOrderModel;
import com.oneness.fdxmerchant.Models.OrderModels.ReadyOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.ReadyOrderResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OngoingOrderAdapter extends RecyclerView.Adapter<OngoingOrderAdapter.Hold> {

    List<OngoingOrderModel> ordList;
    Context context;
    NewOrderItemAdapter newOrderItemAdapter;
    int count = 10;
    DialogView dialogView;
    ApiManager manager = new ApiManager();

    public OngoingOrderAdapter(FragmentActivity activity, List<OngoingOrderModel> ongoingOrderList) {
        this.context = activity;
        this.ordList = ongoingOrderList;
    }


    @NonNull
    @Override
    public OngoingOrderAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_order_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingOrderAdapter.Hold holder, int position) {

        dialogView = new DialogView();
        OngoingOrderModel om = ordList.get(position);
        holder.btnAcceptLL.setVisibility(View.GONE);
        holder.deliveryLL.setVisibility(View.GONE);
        holder.btnNextLL.setVisibility(View.VISIBLE);
        holder.tvTime.setText(getFormattedDate(om.created_at));



       /* Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(om.created_at);
            String newstring = new SimpleDateFormat("dd MMM, yyyy hh:mm aa").format(date);
            holder.tvTime.setText(newstring);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        holder.tvId.setText("ID: " + om.unique_id);
        holder.tvStatus.setText("Preparing");
        holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.prepare_stat_bg));
        holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
        /*String formatted = formatter1.format(Double.parseDouble(om.total_amount));
        holder.tvOrderAmount.setText("₹ " + formatted);
        holder.tvTotBill.setText("₹ " + formatted);*/
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

        if (om.status.equals("2")){
            if (om.is_ready.equals("1") && om.is_dispatched.equals("0")){
                holder.tvStatus.setText("Food Ready");
            }else if (om.is_ready.equals("1") && om.is_dispatched.equals("1")){
                holder.tvStatus.setText("Order Dispatched");
            }else{
                holder.tvStatus.setText("Restaurant Accepted");
            }

        }else if (om.status.equals("3")){
            if (om.is_ready.equals("1") && om.is_dispatched.equals("0")){
                holder.tvStatus.setText("Food Ready");
            }else if (om.is_ready.equals("1") && om.is_dispatched.equals("1")){
                holder.tvStatus.setText("Order Dispatched");
            }else{
                holder.tvStatus.setText("Rider Assigned");
            }
        }else if (om.status.equals("4")){
            if (om.is_ready.equals("1") && om.is_dispatched.equals("0")){
                holder.tvStatus.setText("Food Ready");
            }else if (om.is_ready.equals("1") && om.is_dispatched.equals("1")){
                holder.tvStatus.setText("Order Dispatched");
            }else{
                holder.tvStatus.setText("Restaurant Accepted");
            }
        }else if (om.status.equals("5")){
            if (om.is_ready.equals("1") && om.is_dispatched.equals("0")){
                holder.tvStatus.setText("Food Ready");
            }else if (om.is_ready.equals("1") && om.is_dispatched.equals("1")){
                holder.tvStatus.setText("Order Dispatched");
            }else{
                holder.tvStatus.setText("Rider started");
            }
        }else if (om.status.equals("6")){
            if (om.is_ready.equals("1") && om.is_dispatched.equals("0")){
                holder.tvStatus.setText("Food Ready");
            }else if (om.is_ready.equals("1") && om.is_dispatched.equals("1")){
                holder.tvStatus.setText("Order Dispatched");
            }else{
                holder.tvStatus.setText("Reached Restaurant");
            }
        }else if (om.status.equals("7")){
            if (om.is_ready.equals("1") && om.is_dispatched.equals("0")){
                holder.tvStatus.setText("Food Ready");
            }else if (om.is_ready.equals("1") && om.is_dispatched.equals("1")){
                holder.tvStatus.setText("Order Dispatched");
            }else{
                holder.tvStatus.setText("Order Picked");
            }
        }

        if (!om.delivery_boy_id.equals("0")){
            holder.deliveryLL.setVisibility(View.VISIBLE);
            holder.tvDelBoy.setText(om.boy.name+" assign as delivery partner for this order.");
        }else{
            holder.deliveryLL.setVisibility(View.GONE);
        }

        /*String formatted1 = formatter1.format(Double.parseDouble(om.restaurant_commission));
        if (formatted1.equals(".00")){
            formatted1 = "0.00";
        }
        holder.tvCommission.setText("₹ " + formatted1);*/

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

        String formatted2 = formatter1.format(Double.parseDouble(om.discounted_amount));
        if (formatted2.equals(".00")){
            formatted2 = "0.00";
        }
        holder.tvDiscount.setText("₹ " + formatted2);
       // holder.tvPromoDiscount.setText("₹ " + formatted2);
       /* if (om.coupon_code != null){
            holder.tvUseCode.setText("[ Coupon code: " + om.coupon_code + " ]");
            holder.tvUseCode.setVisibility(View.VISIBLE);
        }else {
            holder.tvUseCode.setVisibility(View.GONE);
        }*/
        //holder.tvTotPay.setText("₹ " + formatted);
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

        String formatted = formatter1.format(Double.parseDouble(om.amount));
        holder.tvOrderAmount.setText("₹ " + formatted);
        /*String formatted6 = formatter1.format(Double.parseDouble(om.restaurant_commission));
        if (formatted6.equals(".00")){
            formatted6 = "0.00";
        }
        holder.tvCommission.setText("₹ " + formatted6);
        holder.tvTotPay.setText("₹ " + formatted6);
        holder.tvTotBill.setText("₹ " + formatted6);*/

        String formatted5 = formatter1.format(Double.parseDouble(om.restaurant_share_commission));
        if (formatted5.equals(".00")){
            formatted5 = "0.00";
        }

        holder.tvPromoDiscount.setText("₹ " + formatted5);

        if (om.items.size()>0){
            newOrderItemAdapter = new NewOrderItemAdapter(context, om.items);
            holder.itemRv.setLayoutManager(new LinearLayoutManager((Activity)context, LinearLayoutManager.VERTICAL, false));
            holder.itemRv.setAdapter(newOrderItemAdapter);
        }

        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetails.ordId = om.id;
                //OrderDetails.readyTime = om.preparation_time;
                Constants.isNewOrder = 0;
                ((Activity)context).startActivity(new Intent(((Activity)context), OrderDetails.class));
                //((Activity)context).finish();
            }
        });

        holder.totBillLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Animation animRotate = AnimationUtils.loadAnimation(context,R.anim.clock_wise_rotation);
                //Animation antiAnimRotate = AnimationUtils.loadAnimation(context,R.anim.anti_clock_wise_rotation);
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

        if (om.is_ready.equals("0")){
            holder.btnNext.setVisibility(View.VISIBLE);
            //int time = Integer.parseInt(om.preparation_time) * (1000 * 60);
            int time = Integer.parseInt(om.time_diff) * (1000);

            Log.d("TIME>>", ""+time);


            new CountDownTimer(time, 1000) {
                public void onTick(long millisUntilFinished) {
                    // Used for formatting digit to be in 2 digits only
                    NumberFormat f = new DecimalFormat("00");
                    long hour = (millisUntilFinished / 3600000) % 24;
                    long min = (millisUntilFinished / 60000) % 60;
                    long sec = (millisUntilFinished / 1000) % 60;
                    holder.btnNext.setText("Ready (" + f.format(hour) + ":" + f.format(min) + ":" + f.format(sec) + ")");
                }
                // When the task is over it will print 00:00:00 there
                public void onFinish() {
                    holder.btnNext.setText("Ready");
                    /*ReadyOrderRequestModel readyOrderRequestModel = new ReadyOrderRequestModel(
                            om.id
                    );
                    readyOrderRequest(readyOrderRequestModel, holder.btnNext);*/
                }
            }.start();
        }else if (om.is_dispatched.equals("1")){
            holder.btnNext.setVisibility(View.GONE);
        }else{
            if (om.order_type.equals("2")){
                holder.btnNext.setVisibility(View.VISIBLE);
                holder.btnNext.setText("Delivered");
            }else {
                holder.btnNext.setVisibility(View.VISIBLE);
                holder.btnNext.setText("Dispatch");
            }


        }



        //holder.btnNext.setText("Ready ( " + om.preparation_time + " )");

        holder.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (om.is_ready.equals("0")){
                    ReadyOrderRequestModel readyOrderRequestModel = new ReadyOrderRequestModel(
                            om.id
                    );
                    readyOrderRequest(readyOrderRequestModel, holder.btnNext, om);
                }else if (om.is_ready.equals("1")){
                    DispatchOrderActivity.order_id = om.id;
                    ((Activity)context).startActivity(new Intent((Activity)context, DispatchOrderActivity.class));
                    ((Activity)context).finish();
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

        TextView tvId, tvIdEnd, tvStatus, tvOrderAmount, tvTime, tvNotes,tvDelBoy, tvOrderType;
        TextView tvTotBill, tvSubTot, tvTotPay, tvCommission, tvPromoDiscount;
        TextView tvUseCode, tvDiscount, tvTax;
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
           // btnReject = itemView.findViewById(R.id.btnReject);
            btnNext = itemView.findViewById(R.id.btnNext);
            btnNextLL = itemView.findViewById(R.id.btnNextLL);
            btnAcceptLL = itemView.findViewById(R.id.btnAcceptLL);
            deliveryLL = itemView.findViewById(R.id.deliveryLL);
            mainLL = itemView.findViewById(R.id.mainLL);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            billLL = itemView.findViewById(R.id.billLL);
            totBillLL = itemView.findViewById(R.id.totBillLL);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            tvDelBoy = itemView.findViewById(R.id.tvDelBoy);
            tvOrderType = itemView.findViewById(R.id.tvOrderType);
            tvCutlery = itemView.findViewById(R.id.tvCutlery);
            tvPacking = itemView.findViewById(R.id.tvPacking);
        }
    }

    private void readyOrderRequest(ReadyOrderRequestModel readyOrderRequestModel, Button btnNext, OngoingOrderModel om) {

        dialogView.showCustomSpinProgress(context);

        manager.service.readyOrder(readyOrderRequestModel).enqueue(new Callback<ReadyOrderResponseModel>() {
            @Override
            public void onResponse(Call<ReadyOrderResponseModel> call, Response<ReadyOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    ReadyOrderResponseModel acceptOrderResponseModel = response.body();

                    if (!acceptOrderResponseModel.error) {
                        Toast.makeText(context, "Order Ready!", Toast.LENGTH_SHORT).show();
                        if (om.order_type.equals("2")){
                            btnNext.setText("Delivered");
                        }else {
                            btnNext.setText("Dispatch");
                        }

                        ((Activity)context).startActivity(new Intent((Activity)context, Dashboard.class));
                        ((Activity)context).finish();

                    } else {

                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ReadyOrderResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }
}
