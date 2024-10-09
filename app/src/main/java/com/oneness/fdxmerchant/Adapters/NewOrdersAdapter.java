package com.oneness.fdxmerchant.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.Order.OrderDetails;
import com.oneness.fdxmerchant.Models.OrderModels.AcceptOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.AcceptOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrdersModel;
import com.oneness.fdxmerchant.Models.OrderModels.RejectOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.RejectOrderResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.Hold> {

    List<NewOrdersModel> ordList;
    Context context;
    NewOrderItemAdapter newOrderItemAdapter;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    String time = "";
    String stock = "", reasonTime = "";

    public NewOrdersAdapter(FragmentActivity activity, List<NewOrdersModel> newOrderList) {
        this.context = activity;
        this.ordList = newOrderList;
    }


    @NonNull
    @Override
    public NewOrdersAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_order_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrdersAdapter.Hold holder, int position) {
        dialogView = new DialogView();
        NewOrdersModel om = ordList.get(position);
        holder.btnAcceptLL.setVisibility(View.VISIBLE);
        holder.btnNextLL.setVisibility(View.GONE);
        holder.deliveryLL.setVisibility(View.GONE);

        holder.tvTime.setText(getFormattedDate(om.created_at));

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

        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        holder.tvId.setText("ID: " + om.unique_id);
        holder.tvStatus.setText("New");
        holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.new_order_stat_bg));
        holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
        //String formatted = formatter1.format(Double.parseDouble(om.total_amount));
        String formatted = formatter1.format(Double.parseDouble(om.amount));
        holder.tvOrderAmount.setText("₹ " + formatted);


        String formatted1 = formatter1.format(Double.parseDouble(om.restaurant_commission));
        if (formatted1.equals(".00")){
            formatted1 = "0.00";
        }

        holder.tvCommission.setText("₹ " + formatted1);
       // holder.tvTotBill.setText("₹ " + formatted1);
        String formatted2 = formatter1.format(Double.parseDouble(om.discounted_amount));
        if (formatted2.equals(".00")){
            formatted2 = "0.00";
        }

        holder.tvDiscount.setText("₹ " + formatted2);
        String formatted10 = formatter1.format(Double.parseDouble(om.restaurant_share_commission));
        if (formatted10.equals(".00")){
            formatted10 = "0.00";
        }
        holder.tvPromoDiscount.setText("₹ " + formatted10);

       /* if (om.coupon_code != null){
            holder.tvUseCode.setText("[ Coupon code: " + om.coupon_code + " ]");
            holder.tvUseCode.setVisibility(View.VISIBLE);
        }else {
            holder.tvUseCode.setVisibility(View.GONE);
        }*/
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

       /* String formatted12 = formatter1.format(Double.parseDouble(om.restaurant_commission));
        if (formatted12.equals(".00")){
            formatted12 = "0.00";
        }

        holder.tvTotPay.setText("₹ " + formatted12);*/

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


        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup("Accept", om);
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup("Reject", om);
            }
        });

        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetails.ordId = om.id;
                Constants.isNewOrder = 1;
                ((Activity)context).startActivity(new Intent(((Activity)context), OrderDetails.class));
                //((Activity)context).finish();
            }
        });

        holder.totBillLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Animation animRotate = AnimationUtils.loadAnimation(context,R.anim.clock_wise_rotation);
               // Animation antiAnimRotate = AnimationUtils.loadAnimation(context,R.anim.anti_clock_wise_rotation);
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


        if (om.items.size() > 0) {
            newOrderItemAdapter = new NewOrderItemAdapter(context, om.items);
            holder.itemRv.setLayoutManager(new LinearLayoutManager((Activity) context, LinearLayoutManager.VERTICAL, false));
            holder.itemRv.setAdapter(newOrderItemAdapter);
        }

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

    @Override
    public int getItemCount() {
        return ordList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvId, tvIdEnd, tvStatus, tvOrderAmount, tvTime, tvOrderType;
        TextView tvTotBill, tvSubTot, tvTotPay, tvCommission, tvPromoDiscount;
        TextView tvUseCode, tvDiscount, tvTax, tvNotes, tvPacking, tvCutlery;
        RecyclerView itemRv;
        Button btnAccept, btnReject;
        ImageView ivArrow;
        LinearLayout btnAcceptLL, btnNextLL, deliveryLL, mainLL, billLL, totBillLL;

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
            btnReject = itemView.findViewById(R.id.btnReject);
            btnAccept = itemView.findViewById(R.id.btnAccept);
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
    private void showPopup(String from, NewOrdersModel om) {
        Dialog dialog = new Dialog((Activity)context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.cancel_reason_popup_lay);
        //dialog.setCancelable(false);
        //dialog.setCanceledOnTouchOutside(false);
        /*LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.cancel_reason_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(context).create();*/
        TextView tvTime = (TextView) dialog.findViewById(R.id.tvTime);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnReject = (Button) dialog.findViewById(R.id.btnReject);
        RadioGroup rgStock = (RadioGroup) dialog.findViewById(R.id.rgStock);
        RadioGroup rgTime = (RadioGroup) dialog.findViewById(R.id.rgTime);
        RadioButton rbComplete = (RadioButton) dialog.findViewById(R.id.rbComplete);
        RadioButton rbPartial = (RadioButton) dialog.findViewById(R.id.rbPartial);
        RadioButton rbMin = (RadioButton) dialog.findViewById(R.id.rbMin);
        RadioButton rbHr = (RadioButton) dialog.findViewById(R.id.rbHr);
        LinearLayout rejectLL = (LinearLayout) dialog.findViewById(R.id.rejectLL);
        LinearLayout acceptLL = (LinearLayout) dialog.findViewById(R.id.acceptLL);
        SeekBar sbTime = (SeekBar) dialog.findViewById(R.id.sbTime);

        if (from.equals("Accept")) {
            acceptLL.setVisibility(View.VISIBLE);
            rejectLL.setVisibility(View.GONE);
        } else {
            acceptLL.setVisibility(View.GONE);
            rejectLL.setVisibility(View.VISIBLE);
        }

        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                time = String.valueOf(progress);
                // TODO Auto-generated method stub
                tvTime.setText("(" + time + " min)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        rgStock.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbComplete:
                        stock = rbComplete.getText().toString();
                        break;
                    case R.id.rbPartial:
                        stock = rbPartial.getText().toString();
                        break;
                }


                // on below line we are displaying a toast message.
                //Toast.makeText(context, stock, Toast.LENGTH_SHORT).show();
            }
        });

        rgTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbMin:
                        reasonTime = rbMin.getText().toString();
                        break;
                    case R.id.rbHr:
                        reasonTime = rbHr.getText().toString();
                        break;
                }


                // on below line we are displaying a toast message.
                // Toast.makeText(context, reasonTime, Toast.LENGTH_SHORT).show();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mainReason = "";
                if (stock.equals("")){
                    if (reasonTime.equals("")){
                        Toast.makeText(context, "Please select a reason!", Toast.LENGTH_SHORT).show();
                    }else {
                        mainReason = reasonTime;
                    }
                }else {
                    mainReason = stock;
                }
                RejectOrderRequestModel rejectOrderRequestModel = new RejectOrderRequestModel(
                        om.id,
                        mainReason
                );

                cancelOrderRequest(rejectOrderRequestModel);
                dialog.dismiss();
            }
        });






        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (time.equals("") || time.equals("0")){
                    Toast.makeText(context, "Please set approx preparation time!", Toast.LENGTH_SHORT).show();
                }else {
                    AcceptOrderRequestModel acceptOrderRequestModel = new AcceptOrderRequestModel(
                            om.id,
                            time
                    );
                    acceptOrderRequest(acceptOrderRequestModel);
                }
                dialog.dismiss();

            }
        });


        // alertD.setView(promptView);
        // try {
        dialog.show();
        /*} catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/
    }

    private void acceptOrderRequest(AcceptOrderRequestModel acceptOrderRequestModel) {

        dialogView.showCustomSpinProgress(context);

        manager.service.acceptNewOrder(acceptOrderRequestModel).enqueue(new Callback<AcceptOrderResponseModel>() {
            @Override
            public void onResponse(Call<AcceptOrderResponseModel> call, Response<AcceptOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    AcceptOrderResponseModel acceptOrderResponseModel = response.body();

                    if (!acceptOrderResponseModel.error) {
                        Constants.orderSize = Constants.orderSize - 1;
                        Toast.makeText(context, "Order Accepted!", Toast.LENGTH_SHORT).show();
                        ((Activity)context).startActivity(new Intent((Activity)context, Dashboard.class));
                        ((Activity)context).finish();
                    } else {

                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AcceptOrderResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void cancelOrderRequest(RejectOrderRequestModel rejectOrderRequestModel) {
        dialogView.showCustomSpinProgress(context);

        manager.service.rejectNewOrder(rejectOrderRequestModel).enqueue(new Callback<RejectOrderResponseModel>() {
            @Override
            public void onResponse(Call<RejectOrderResponseModel> call, Response<RejectOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    RejectOrderResponseModel rejectOrderResponseModel = response.body();
                    if (!rejectOrderResponseModel.error) {
                        Constants.orderSize = Constants.orderSize - 1;
                        Toast.makeText(context, "Order Cancelled!", Toast.LENGTH_SHORT).show();
                        ((Activity)context).startActivity(new Intent((Activity)context, Dashboard.class));
                        ((Activity)context).finish();
                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RejectOrderResponseModel> call, Throwable t) {

            }
        });

    }
}
