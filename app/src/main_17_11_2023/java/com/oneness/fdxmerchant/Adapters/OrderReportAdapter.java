package com.oneness.fdxmerchant.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.Order.OrderDetails;
import com.oneness.fdxmerchant.Activity.ReportManagement.DateWiseOrderReport;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderItemModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.TodayOrderModel;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderReportAdapter extends RecyclerView.Adapter<OrderReportAdapter.Hold> {

    List<DateWiseOrderReportModel> orList;
    Context context;
    List<DateWiseOrderItemModel> orderItemList = new ArrayList<>();
    OrderReportsItemsAdapter orderReportsItemsAdapter;

   /* public OrderReportAdapter(DateWiseOrderReport dateWiseOrderReport, List<DateWiseOrderReportModel> orderReportList) {
        this.orList = orderReportList;
        this.context = dateWiseOrderReport;
    }*/

    public OrderReportAdapter(FragmentActivity activity, List<DateWiseOrderReportModel> orderReportList) {
        this.orList = orderReportList;
        this.context = activity;
    }

    @NonNull
    @Override
    public OrderReportAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_report_row, parent, false);
        return new Hold(mView);
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    public void onBindViewHolder(@NonNull OrderReportAdapter.Hold holder, int position) {

        DateWiseOrderReportModel om = orList.get(position);
        holder.tvId.setText("ID: " + om.unique_id);
        //holder.tvOrderStat.setText(om.unique_id);
        //holder.tvItems.setText(om.total_item_count + " items");
        //SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("dd MMM, yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm a");
        Log.d("DATA_DATE>>", om.created_at);
        try {
            Date date = df.parse(om.created_at);
            String time = df1.format(date);
            Log.d("Time>>", time);


            holder.tvItems.setText(time);
            Date date1 = df.parse(om.created_at);
            String orderDate = df2.format(date1);
            holder.tvTime.setText(orderDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted = formatter1.format(Double.parseDouble(om.amount));
        holder.tvPrice.setText("\u20B9" + formatted);

        DecimalFormat formatter2 = new DecimalFormat("#,##,###.00");
        String formatted2 = formatter2.format(Double.parseDouble(om.restaurant_commission));
        holder.tvCommission.setText("\u20B9" + formatted2);



        //holder.tvName.setText(om.name);
        //holder.tvStatus.setText(om.ord_status);

        if (om.status.equals("1")){
            holder.tvOrderStat.setText("New");
            holder.tvOrderStat.setTextColor(context.getResources().getColor(R.color.black));
        }else if (om.status.equals("8") || om.status.equals("9")){
            holder.tvOrderStat.setTextColor(context.getResources().getColor(R.color.green));
            holder.tvOrderStat.setText("Delivered");
        }else if (om.status.equals("10")){
            holder.tvOrderStat.setTextColor(context.getResources().getColor(R.color.red2));
            holder.tvOrderStat.setText("Cancelled");
        }else {
            holder.tvOrderStat.setTextColor(context.getResources().getColor(R.color.yellow));
            holder.tvOrderStat.setText("Ongoing");
        }

        /*holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderItemPopup(om);
            }
        });*/

        holder.tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderItemPopup(om);
            }
        });





       /* holder.tvTrack.setVisibility(View.GONE);


        holder.tvTrack.setText("Details");
        holder.tvTrack.setBackground(context.getResources().getDrawable(R.drawable.track_bg));

        holder.tvTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetails.ordId = om.id;
                if (om.status.equals("1")) {
                    Constants.isNewOrder = 1;
                }else{
                    Constants.isNewOrder = 0;
                }
                context.startActivity(new Intent(context.getApplicationContext(), OrderDetails.class));
            }
        });*/

    }

    private void showOrderItemPopup(DateWiseOrderReportModel om) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.order_report_items_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView tvTotPrice = (TextView) dialog.findViewById(R.id.tvTotPrice);
        TextView tvOrderId = (TextView) dialog.findViewById(R.id.tvOrderId);
        TextView tvBoy = (TextView) dialog.findViewById(R.id.tvBoy);

        TextView tvSubTot = (TextView) dialog.findViewById(R.id.tvSubTot);
        TextView tvTax = (TextView) dialog.findViewById(R.id.tvTax);
        TextView tvPromoDiscount = (TextView) dialog.findViewById(R.id.tvPromoDiscount);
        TextView tvCutlery = (TextView) dialog.findViewById(R.id.tvCutlery);
        TextView tvPacking = (TextView) dialog.findViewById(R.id.tvPacking);
        TextView tvFDX = (TextView) dialog.findViewById(R.id.tvFDX);

        RelativeLayout delBoyRL = (RelativeLayout) dialog.findViewById(R.id.delBoyRL);
        RecyclerView itemsRv = (RecyclerView) dialog.findViewById(R.id.itemsRv);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

        tvOrderId.setText("#" + om.unique_id);
        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        /*String formatted = formatter1.format(Double.parseDouble(om.amount));
        tvTotPrice.setText("\u20B9" + formatted);*/
        String formatted6 = formatter1.format(Double.parseDouble(om.restaurant_commission)
                + Double.parseDouble(om.tax_amount)
                - Double.parseDouble(om.restaurant_share_commission)
                + Double.parseDouble(om.cutlery_charge)
                + Double.parseDouble(om.packing_price));
        if (formatted6.equals(".00")){
            formatted6 = "0.00";
        }
        tvTotPrice.setText("\u20B9" + formatted6);

        String formatted5 = formatter1.format(Double.parseDouble(om.amount));
        if (formatted5.equals(".00")){
            formatted5 = "0.00";
        }
        tvSubTot.setText("\u20B9" + formatted5);

        String formatted4 = formatter1.format(Double.parseDouble(om.tax_amount));
        if (formatted4.equals(".00")){
            formatted4 = "0.00";
        }
        tvTax.setText("\u20B9" + formatted4);

        String formatted3 = formatter1.format(Double.parseDouble(om.restaurant_share_commission));
        if (formatted3.equals(".00")){
            formatted3 = "0.00";
        }
        tvPromoDiscount.setText("\u20B9" + formatted3);

        String formatted2 = formatter1.format(Double.parseDouble(om.cutlery_charge));
        if (formatted2.equals(".00")){
            formatted2 = "0.00";
        }
        tvCutlery.setText("\u20B9" + formatted2);

        String formatted1 = formatter1.format(Double.parseDouble(om.packing_price));
        if (formatted1.equals(".00")){
            formatted1 = "0.00";
        }
        tvPacking.setText("\u20B9" + formatted1);

        String formatted = formatter1.format(Double.parseDouble(om.amount) - Double.parseDouble(om.restaurant_commission));
        if (formatted.equals(".00")){
            formatted = "0.00";
        }
        tvFDX.setText("\u20B9" + formatted);



        if (om.boy_name.equals("")){
            delBoyRL.setVisibility(View.GONE);
        }else {
            delBoyRL.setVisibility(View.VISIBLE);
            tvBoy.setText(om.boy_name);
        }



        orderItemList = om.items;


        if (orderItemList.size()>0){
            orderReportsItemsAdapter = new OrderReportsItemsAdapter(context, om.items);
            itemsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            itemsRv.setAdapter(orderReportsItemsAdapter);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return orList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvId, tvPrice, tvOrderStat, tvItems, tvTime, tvCommission, tvShow;
        LinearLayout mainLL;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOrderStat = itemView.findViewById(R.id.tvOrderStat);
            tvItems = itemView.findViewById(R.id.tvItems);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvCommission = itemView.findViewById(R.id.tvCommission);
            mainLL = itemView.findViewById(R.id.mainLL);
            tvShow = itemView.findViewById(R.id.tvShow);
        }
    }
}
