package com.oneness.fdxmerchant.Activity.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Adapters.OrderItemAdapter;
import com.oneness.fdxmerchant.Models.OrderModels.AcceptOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.AcceptOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.DispatchOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.DispatchOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.ExtraTimeRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.ExtraTimeResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderDetailsModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderDetailsResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModel;
import com.oneness.fdxmerchant.Models.OrderModels.ReadyOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.ReadyOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.RejectOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.RejectOrderResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetails extends AppCompatActivity {

    RecyclerView pastOrderItemRv;
    List<OrderItemsModel> orderItemList = new ArrayList<>();
    OrderItemAdapter orderItemAdapter;
    public static String ordId = "";
    String time = "";
    String stock = "", reasonTime = "";
    String callNo = "";
    String isReady = "", orderType = "";

    TextView tvTotPrice, tvOrderNumber, tvPayment, tvDate, tvPhn, tvDelTo, tvDelBy, tvNotes, tvOrderType;
    TextView tvDelFee, tvTax, tvSelectTime, tvDistance, tvTime, tvExtraTime;// tvPackingChrg, tvPayType;
    //TextView tvRestName, tvRestAdr;
    TextView tvTransId, tvStatus, tvUseCode, tvPromoDiscount, tvDiscount, tvCommission,tvPacking, tvCutlery;
    Button btnExtraTime;
    TextView tvSubTot;

    ImageView iv_back, ivPhn, ivDelCall;

    //int REQUEST_PHONE_CALL = 100;

    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();

    LinearLayout acceptanceLL, readyLL, llExtraTime, noteLL;
    Button btnCancel, btnAccept, btnReady;
    String msg = "";
    String incTime = "";
    SeekBar sbTime;
    CheckBox cbTime;
    RelativeLayout extraTimeRL, stockRL, driverRL, imgCallRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        prefs = new Prefs(OrderDetails.this);
        dialogView = new DialogView();

        pastOrderItemRv = findViewById(R.id.pastOrderItemRv);
        tvTotPrice = findViewById(R.id.tvTotPrice);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvPayment = findViewById(R.id.tvPayment);
        tvDate = findViewById(R.id.tvDate);
        tvPhn = findViewById(R.id.tvPhn);
        tvDelTo = findViewById(R.id.tvDelTo);
        tvDelBy = findViewById(R.id.tvDelBy);
        tvDelFee = findViewById(R.id.tvDelFee);
        sbTime = findViewById(R.id.sbTime);
        //tvPackingChrg = findViewById(R.id.tvPackingChrg);
        tvTax = findViewById(R.id.tvTax);
        tvCommission = findViewById(R.id.tvCommission);
        tvOrderType = findViewById(R.id.tvOrderType);
        //tvPayType = findViewById(R.id.tvPayType);
        //tvRestName = findViewById(R.id.tvRestName);
        //tvRestAdr = findViewById(R.id.tvRestAdr);
        iv_back = findViewById(R.id.iv_back);
        tvTransId = findViewById(R.id.tvTransId);
        acceptanceLL = findViewById(R.id.acceptanceLL);
        btnCancel = findViewById(R.id.btnCancel);
        btnAccept = findViewById(R.id.btnAccept);
        tvSelectTime = findViewById(R.id.tvSelectTime);
        cbTime = findViewById(R.id.cbTime);
        tvStatus = findViewById(R.id.tvStatus);
        tvDistance = findViewById(R.id.tvDistance);
        tvTime = findViewById(R.id.tvTime);
        tvUseCode = findViewById(R.id.tvUseCode);
        tvPromoDiscount = findViewById(R.id.tvPromoDiscount);
        tvDiscount = findViewById(R.id.tvDiscount);
        stockRL = findViewById(R.id.stockRL);
        extraTimeRL = findViewById(R.id.extraTimeRL);
        driverRL = findViewById(R.id.driverRL);
        readyLL = findViewById(R.id.readyLL);
        llExtraTime = findViewById(R.id.llExtraTime);
        btnReady = findViewById(R.id.btnReady);
        imgCallRL = findViewById(R.id.imgCallRL);
        ivPhn = findViewById(R.id.ivPhn);
        tvNotes = findViewById(R.id.tvNotes);
        ivDelCall = findViewById(R.id.ivDelCall);
        btnExtraTime = findViewById(R.id.btnExtraTime);
        tvExtraTime = findViewById(R.id.tvExtraTime);
        noteLL = findViewById(R.id.noteLL);
        tvPacking = findViewById(R.id.tvPacking);
        tvCutlery = findViewById(R.id.tvCutlery);
        tvSubTot = findViewById(R.id.tvSubTot);

        if (Constants.isNewOrder == 1) {
            acceptanceLL.setVisibility(View.VISIBLE);
            tvStatus.setText("New");
            readyLL.setVisibility(View.GONE);
            stockRL.setVisibility(View.VISIBLE);
            extraTimeRL.setVisibility(View.GONE);
            btnExtraTime.setVisibility(View.GONE);
            driverRL.setVisibility(View.VISIBLE);
            tvStatus.setBackground(getResources().getDrawable(R.drawable.new_order_stat_bg));
            tvStatus.setTextColor(ContextCompat.getColor(OrderDetails.this, R.color.white));
        } else if (Constants.isNewOrder == 2) {
            acceptanceLL.setVisibility(View.GONE);
            readyLL.setVisibility(View.GONE);
            tvStatus.setText("Delivered");
            stockRL.setVisibility(View.GONE);
            extraTimeRL.setVisibility(View.GONE);
            btnExtraTime.setVisibility(View.GONE);
            driverRL.setVisibility(View.VISIBLE);
            tvStatus.setBackground(getResources().getDrawable(R.drawable.delivered_stat_bg));
            tvStatus.setTextColor(ContextCompat.getColor(OrderDetails.this, R.color.white));

        } else if (Constants.isNewOrder == 3) {
            acceptanceLL.setVisibility(View.GONE);
            readyLL.setVisibility(View.GONE);
            tvStatus.setText("Cancelled");
            stockRL.setVisibility(View.GONE);
            extraTimeRL.setVisibility(View.GONE);
            btnExtraTime.setVisibility(View.GONE);
            driverRL.setVisibility(View.GONE);
            tvStatus.setBackground(getResources().getDrawable(R.drawable.cancel_stat_bg));
            tvStatus.setTextColor(ContextCompat.getColor(OrderDetails.this, R.color.red2));

        } else {
            acceptanceLL.setVisibility(View.GONE);
            readyLL.setVisibility(View.VISIBLE);
            tvStatus.setText("Preparing");
            stockRL.setVisibility(View.VISIBLE);
            extraTimeRL.setVisibility(View.VISIBLE);
            btnExtraTime.setVisibility(View.GONE);
            tvStatus.setBackground(getResources().getDrawable(R.drawable.prepare_stat_bg));
            tvStatus.setTextColor(ContextCompat.getColor(OrderDetails.this, R.color.black));
        }

        //checkCallPermission();
        // makeOrderItemList();
        getOrderDetails(ordId);

        btnExtraTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("tt>>", incTime);
                if (incTime.equals("") || incTime.equals("0")) {
                    Toast.makeText(OrderDetails.this, "Please set additional preparation time!", Toast.LENGTH_SHORT).show();
                } else {
                    ExtraTimeRequestModel extraTimeRequestModel = new ExtraTimeRequestModel(
                            ordId,
                            incTime
                    );
                    takeExtraTime(extraTimeRequestModel);
                }
                //alertD.dismiss();

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                msg = "Please enter approx preparation time (in minutes)!";
                showPopup("Accept");

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg = "Please mention cancellation reason below!";
                showPopup("Reject");
            }
        });

        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                incTime = String.valueOf(progress);
                // TODO Auto-generated method stub
                tvSelectTime.setText("(" + incTime + " min)");
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

        cbTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbTime.isChecked()) {
                    llExtraTime.setVisibility(View.VISIBLE);
                    btnExtraTime.setVisibility(View.VISIBLE);
                } else {
                    llExtraTime.setVisibility(View.GONE);
                    btnExtraTime.setVisibility(View.GONE);
                }
            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReady.equals("1")) {
                    DispatchOrderActivity.order_id = ordId;
                    startActivity(new Intent(OrderDetails.this, DispatchOrderActivity.class));
                    finish();
                } else {
                    ReadyOrderRequestModel readyOrderRequestModel = new ReadyOrderRequestModel(
                            ordId
                    );
                    readyOrderRequest(readyOrderRequestModel);
                }
            }
        });


    }

    private void readyOrderRequest(ReadyOrderRequestModel readyOrderRequestModel) {

        dialogView.showCustomSpinProgress(OrderDetails.this);

        manager.service.readyOrder(readyOrderRequestModel).enqueue(new Callback<ReadyOrderResponseModel>() {
            @Override
            public void onResponse(Call<ReadyOrderResponseModel> call, Response<ReadyOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    ReadyOrderResponseModel acceptOrderResponseModel = response.body();

                    if (!acceptOrderResponseModel.error) {
                        Toast.makeText(OrderDetails.this, "Order Ready!", Toast.LENGTH_SHORT).show();
                        if (orderType.equals("2")) {
                            //tvOrderType.setText("Takeaway");
                            //tvOrderType.setVisibility(View.VISIBLE);
                            btnReady.setText("Delivered");
                        } else {
                            //tvOrderType.setVisibility(View.GONE);
                            btnReady.setText("Dispatch");
                        }

                        startActivity(new Intent(OrderDetails.this, Dashboard.class));
                        finish();

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


    private void showPopup(String from) {
        Dialog dialog = new Dialog(OrderDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.cancel_reason_popup_lay);
        /*LayoutInflater layoutInflater = LayoutInflater.from(OrderDetails.this);
        View promptView = layoutInflater.inflate(R.layout.cancel_reason_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(OrderDetails.this).create();*/
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

        int max = 60;
        int min = 0;
        int step = 5;

        sbTime.setMax((max - min) / step);

        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                time = String.valueOf(min + (progress * step));
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

        /*sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

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
        });*/

        rgStock.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
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
                switch (checkedId) {
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
                if (stock.equals("")) {
                    if (reasonTime.equals("")) {
                        Toast.makeText(OrderDetails.this, "Please select a reason!", Toast.LENGTH_SHORT).show();
                    } else {
                        mainReason = reasonTime;
                    }
                } else {
                    mainReason = stock;
                }
                RejectOrderRequestModel rejectOrderRequestModel = new RejectOrderRequestModel(
                        ordId,
                        mainReason
                );

                cancelOrderRequest(rejectOrderRequestModel);
                dialog.dismiss();
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (time.equals("") || time.equals("0")) {
                    Toast.makeText(OrderDetails.this, "Please set approx preparation time!", Toast.LENGTH_SHORT).show();
                } else {
                    AcceptOrderRequestModel acceptOrderRequestModel = new AcceptOrderRequestModel(
                            ordId,
                            time
                    );
                    acceptOrderRequest(acceptOrderRequestModel);
                }
                dialog.dismiss();

            }
        });


       /* alertD.setView(promptView);
        try {*/
        dialog.show();
        /*} catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/
    }

    private void acceptOrderRequest(AcceptOrderRequestModel acceptOrderRequestModel) {

        dialogView.showCustomSpinProgress(OrderDetails.this);

        manager.service.acceptNewOrder(acceptOrderRequestModel).enqueue(new Callback<AcceptOrderResponseModel>() {
            @Override
            public void onResponse(Call<AcceptOrderResponseModel> call, Response<AcceptOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    AcceptOrderResponseModel acceptOrderResponseModel = response.body();

                    if (!acceptOrderResponseModel.error) {
                        Constants.orderSize = Constants.orderSize - 1;
                        Toast.makeText(OrderDetails.this, "Order Accepted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderDetails.this, Dashboard.class));
                        finish();
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
        dialogView.showCustomSpinProgress(OrderDetails.this);

        manager.service.rejectNewOrder(rejectOrderRequestModel).enqueue(new Callback<RejectOrderResponseModel>() {
            @Override
            public void onResponse(Call<RejectOrderResponseModel> call, Response<RejectOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    RejectOrderResponseModel rejectOrderResponseModel = response.body();
                    if (!rejectOrderResponseModel.error) {
                        Constants.orderSize = Constants.orderSize - 1;
                        Toast.makeText(OrderDetails.this, "Order Cancelled!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderDetails.this, Dashboard.class));
                        finish();
                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RejectOrderResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });

    }

    private void getOrderDetails(String id) {
        dialogView.showCustomSpinProgress(OrderDetails.this);

        manager.service.getOrderDetails(id).enqueue(new Callback<OrderDetailsResponseModel>() {
            @Override
            public void onResponse(Call<OrderDetailsResponseModel> call, Response<OrderDetailsResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    OrderDetailsResponseModel orderDetailsResponseModel = response.body();
                    if (orderDetailsResponseModel.error != true) {

                        orderItemList = new ArrayList<>();

                        OrderDetailsModel orderDetailsModel = orderDetailsResponseModel.orderData;

                        orderItemList = orderDetailsModel.items;

                        tvOrderNumber.setText("ID: " + orderDetailsModel.unique_id);
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(orderDetailsModel.created_at);
                            String newstring = new SimpleDateFormat("dd MMM, yyyy hh:mm aa").format(date);
                            tvTime.setText(newstring);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (orderDetailsModel.extra_preparation_time.equals("0")) {

                            btnExtraTime.setVisibility(View.VISIBLE);
                            cbTime.setVisibility(View.VISIBLE);
                            tvExtraTime.setVisibility(View.GONE);


                        } else {
                            btnExtraTime.setVisibility(View.GONE);
                            cbTime.setVisibility(View.GONE);
                            tvExtraTime.setText(orderDetailsModel.extra_preparation_time + " Mints");
                            tvExtraTime.setVisibility(View.VISIBLE);
                        }

                        /*if (orderDetailsModel.status.equals("1")){
                            extraTimeRL.setVisibility(View.GONE);
                        }*/
                        DecimalFormat fm1 = new DecimalFormat("#,##,###.00");
                        String subTot = fm1.format(Double.parseDouble(orderDetailsModel.amount));
                        if (subTot.equals(".00")){
                            subTot = "0.00";
                        }
                        tvSubTot.setText("₹ " + subTot);


                        DecimalFormat fm2 = new DecimalFormat("#,##,###.00");
                        String cutPrice = fm2.format(Double.parseDouble(orderDetailsModel.cutlery_charge));
                        if (cutPrice.equals(".00")){
                            cutPrice = "0.00";
                        }
                        tvCutlery.setText("₹ " + cutPrice);

                        if (orderDetailsModel.packing_price != null){
                            DecimalFormat fm = new DecimalFormat("#,##,###.00");
                            String packPrice = fm.format(Double.parseDouble(orderDetailsModel.packing_price));
                            if (packPrice.equals(".00")){
                                packPrice = "0.00";
                            }
                            tvPacking.setText("₹ " + packPrice);
                        }else{
                            tvPacking.setText("₹ " + "0.00");
                        }


                        if (orderDetailsModel.status.equals("2")) {
                            if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("0")) {
                                tvStatus.setText("Food Ready");
                                // stockRL.setVisibility(View.GONE);

                                /*extraTimeRL.setVisibility(View.VISIBLE);
                                cbTime.setVisibility(View.GONE);
                                tvExtraTime.setVisibility(View.VISIBLE);
                                tvExtraTime.setText(orderDetailsModel.extra_preparation_time + " Mints");
                                btnExtraTime.setVisibility(View.GONE);*/
                            } else if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("1")) {
                                tvStatus.setText("Order Dispatched");
                                stockRL.setVisibility(View.GONE);

                                /*extraTimeRL.setVisibility(View.VISIBLE);
                                cbTime.setVisibility(View.GONE);
                                tvExtraTime.setVisibility(View.VISIBLE);
                                tvExtraTime.setText(orderDetailsModel.extra_preparation_time + " Mints");
                                btnExtraTime.setVisibility(View.GONE);*/
                            } else {
                                tvStatus.setText("Restaurant Accepted");

                            }

                        } else if (orderDetailsModel.status.equals("3")) {
                            if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("0")) {
                                tvStatus.setText("Food Ready");
                                //stockRL.setVisibility(View.GONE);
                               /* extraTimeRL.setVisibility(View.VISIBLE);
                                cbTime.setVisibility(View.GONE);
                                tvExtraTime.setVisibility(View.VISIBLE);
                                tvExtraTime.setText(orderDetailsModel.extra_preparation_time + " Mints");
                                btnExtraTime.setVisibility(View.GONE);*/
                            } else if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("1")) {
                                tvStatus.setText("Order Dispatched");
                                stockRL.setVisibility(View.GONE);
                                /*extraTimeRL.setVisibility(View.VISIBLE);
                                cbTime.setVisibility(View.GONE);
                                tvExtraTime.setVisibility(View.VISIBLE);
                                tvExtraTime.setText(orderDetailsModel.extra_preparation_time + " Mints");
                                btnExtraTime.setVisibility(View.GONE);*/
                            } else {
                                tvStatus.setText("Rider Assigned");
                            }
                        } else if (orderDetailsModel.status.equals("4")) {
                            if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("0")) {
                                tvStatus.setText("Food Ready");
                                /*extraTimeRL.setVisibility(View.VISIBLE);
                                cbTime.setVisibility(View.GONE);
                                tvExtraTime.setVisibility(View.VISIBLE);
                                tvExtraTime.setText(orderDetailsModel.extra_preparation_time + " Mints");
                                btnExtraTime.setVisibility(View.GONE);*/
                            } else if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("1")) {
                                tvStatus.setText("Order Dispatched");
                                stockRL.setVisibility(View.GONE);
                               /* extraTimeRL.setVisibility(View.VISIBLE);
                                cbTime.setVisibility(View.GONE);
                                tvExtraTime.setVisibility(View.VISIBLE);
                                tvExtraTime.setText(orderDetailsModel.extra_preparation_time + " Mints");
                                btnExtraTime.setVisibility(View.GONE);*/
                            } else {
                                tvStatus.setText("Restaurant Accepted");
                            }
                        } else if (orderDetailsModel.status.equals("5")) {
                            if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("0")) {
                                tvStatus.setText("Food Ready");
                            } else if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("1")) {
                                tvStatus.setText("Order Dispatched");
                                stockRL.setVisibility(View.GONE);
                            } else {
                                tvStatus.setText("Rider started");
                            }
                        } else if (orderDetailsModel.status.equals("6")) {
                            if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("0")) {
                                tvStatus.setText("Food Ready");
                            } else if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("1")) {
                                tvStatus.setText("Order Dispatched");
                                stockRL.setVisibility(View.GONE);
                            } else {
                                tvStatus.setText("Reached Restaurant");
                            }
                        } else if (orderDetailsModel.status.equals("7")) {
                            if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("0")) {
                                tvStatus.setText("Food Ready");

                            } else if (orderDetailsModel.is_ready.equals("1") && orderDetailsModel.is_dispatched.equals("1")) {
                                tvStatus.setText("Order Dispatched");
                                stockRL.setVisibility(View.GONE);
                            } else {
                                tvStatus.setText("Order Picked");
                            }
                        }


                        isReady = orderDetailsModel.is_ready;

                        if (orderDetailsModel.is_dispatched.equals("1")) {
                            btnReady.setVisibility(View.GONE);
                            extraTimeRL.setVisibility(View.VISIBLE);
                            btnExtraTime.setVisibility(View.GONE);
                        } else {
                            if (orderDetailsModel.is_ready.equals("1")) {
                                if (orderDetailsModel.order_type.equals("2")) {
                                    btnReady.setText("Delivered");
                                } else {
                                    btnReady.setText("Dispatch");
                                }

                               /* extraTimeRL.setVisibility(View.VISIBLE);
                                cbTime.setVisibility(View.GONE);
                                tvExtraTime.setVisibility(View.VISIBLE);
                                tvExtraTime.setText(orderDetailsModel.extra_preparation_time + " Mints");
                                btnExtraTime.setVisibility(View.GONE);*/
                            } else {
                                int time = Integer.parseInt(orderDetailsModel.time_diff) * (1000);
                                Log.d("TIME>>", "" + time);
                                new CountDownTimer(time, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                        // Used for formatting digit to be in 2 digits only
                                        NumberFormat f = new DecimalFormat("00");
                                        long hour = (millisUntilFinished / 3600000) % 24;
                                        long min = (millisUntilFinished / 60000) % 60;
                                        long sec = (millisUntilFinished / 1000) % 60;
                                        btnReady.setText("Ready (" + f.format(hour) + ":" + f.format(min) + ":" + f.format(sec) + ")");
                                    }

                                    // When the task is over it will print 00:00:00 there
                                    public void onFinish() {
                                       /* ReadyOrderRequestModel readyOrderRequestModel = new ReadyOrderRequestModel(
                                                ordId
                                        );
                                        readyOrderRequest(readyOrderRequestModel);*/
                                        btnReady.setText("Ready");
                                    }
                                }.start();
                            }

                        }


                        //tvTime.setText(orderDetailsModel.created_at);
                        //btnReady.setText("Ready ( "+orderDetailsModel.preparation_time+" )");

                        if (orderDetailsModel.notes != null) {
                            if (orderDetailsModel.notes.equals("")) {
                                noteLL.setVisibility(View.GONE);
                            } else {
                                noteLL.setVisibility(View.VISIBLE);
                                tvNotes.setText(orderDetailsModel.notes);
                            }
                        } else {
                            noteLL.setVisibility(View.GONE);
                            tvNotes.setText(orderDetailsModel.notes);
                        }


                        DecimalFormat formatter5 = new DecimalFormat("#,##,###.00");
                        String formatted10 = formatter5.format(Double.parseDouble(orderDetailsModel.delivery_distance));
                        if (formatted10.equals(".00")) {
                            formatted10 = "0.00";
                        }

                        tvDistance.setText("Distance from restaurant " + formatted10 + " km.");
                        //tvUseCode.setText("[ Coupon code: " + orderDetailsModel.coupon_code + " ]");
                        /*if (orderDetailsModel.coupon_code != null) {
                            tvUseCode.setText("[ Coupon code: " + orderDetailsModel.coupon_code + " ]");
                            tvUseCode.setVisibility(View.VISIBLE);
                        } else {
                            tvUseCode.setVisibility(View.GONE);
                        }*/

                        DecimalFormat formatter4 = new DecimalFormat("#,##,###.00");
                        String formatted2 = formatter4.format(Double.parseDouble(orderDetailsModel.discounted_amount));
                        if (formatted2.equals(".00")) {
                            formatted2 = "0.00";
                        }
                        tvDiscount.setText("- ₹ " + formatted2);
                        String formatted15 = formatter4.format(Double.parseDouble(orderDetailsModel.restaurant_share_commission));
                        if (formatted15.equals(".00")) {
                            formatted15 = "0.00";
                        }


                        tvPromoDiscount.setText("- ₹ " + formatted15);

                        String formatted4 = formatter4.format(Double.parseDouble(orderDetailsModel.restaurant_commission));
                        if (formatted4.equals(".00")) {
                            formatted4 = "0.00";
                        }
                        tvCommission.setText("₹ " + formatted4);

                        orderType = orderDetailsModel.order_type;

                        if (orderDetailsModel.order_type.equals("2")) {
                            tvOrderType.setText("Takeaway");
                            tvOrderType.setVisibility(View.VISIBLE);
                        } else {
                            tvOrderType.setVisibility(View.GONE);
                        }


                        if (orderDetailsModel.is_ready.equals("1")) {
                            //extraTimeRL.setVisibility(View.GONE);
                            extraTimeRL.setVisibility(View.VISIBLE);
                            cbTime.setVisibility(View.GONE);
                            if (!orderDetailsModel.extra_preparation_time.equals("0")) {
                                tvExtraTime.setText(orderDetailsModel.extra_preparation_time + " Mints");
                                tvExtraTime.setVisibility(View.VISIBLE);
                            }else {
                                extraTimeRL.setVisibility(View.GONE);
                            }
                            btnExtraTime.setVisibility(View.GONE);
                        } else {
                            if (orderDetailsModel.status.equals("1")) {
                                extraTimeRL.setVisibility(View.GONE);
                                btnExtraTime.setVisibility(View.GONE);
                            } else if (orderDetailsModel.status.equals("8")) {
                                extraTimeRL.setVisibility(View.GONE);
                                btnExtraTime.setVisibility(View.GONE);

                            } else if (orderDetailsModel.status.equals("10")) {
                                extraTimeRL.setVisibility(View.GONE);
                                btnExtraTime.setVisibility(View.GONE);
                            } else {
                                if (!orderDetailsModel.extra_preparation_time.equals("0")) {
                                    extraTimeRL.setVisibility(View.VISIBLE);
                                    btnExtraTime.setVisibility(View.GONE);
                                } else {
                                    //extraTimeRL.setVisibility(View.GONE);
                                    btnExtraTime.setVisibility(View.GONE);
                                }
                            }

                        }

                        if (orderDetailsModel.delivery_boy_id.equals("0")) {
                            driverRL.setVisibility(View.GONE);
                            tvDelBy.setVisibility(View.GONE);
                        } else {
                            driverRL.setVisibility(View.VISIBLE);
                            tvDelBy.setVisibility(View.VISIBLE);
                            tvDelBy.setText(orderDetailsModel.boy.name);

                        }
                       /* if (orderDetailsModel.payment_status.equals("1")){
                            if (orderDetailsModel.transaction_id.equals("test-cod")){
                                tvPayment.setText("Paid : Using cash");
                            }else {
                                tvPayment.setText("paid : Using razorpay");
                            }
                        }else{
                            tvPayment.setText("Unpaid");
                        }*/

                        //tvDelBy.setText(orderDetailsModel.);
                        /*if (orderDetailsModel.transaction_id.equals("test-cod")){
                            tvTransId.setText("Cash On Delivery");
                        }else {
                            tvTransId.setText(orderDetailsModel.transaction_id);
                        }*/

                        tvDelTo.setText(orderDetailsModel.delivery_address);
                        tvPhn.setText(orderDetailsModel.mobile);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                        String formatted = formatter1.format(Double.parseDouble(orderDetailsModel.delivery_charge));
                        if (formatted.equals(".00")) {
                            formatted = "0.00";
                        }
                        tvDelFee.setText("₹ " + formatted);
                        //String formatted2 = formatter1.format(Double.parseDouble(orderDetailsModel.packing_price));
                        //tvPackingChrg.setText("₹ " + formatted2);
                        String formatted3 = formatter1.format(Double.parseDouble(orderDetailsModel.tax_amount));
                        if (formatted3.equals(".00")) {
                            formatted3 = "0.00";
                        }
                        tvTax.setText("₹ " + formatted3);

                        /*String formatted1 = formatter1.format(Double.parseDouble(orderDetailsModel.restaurant_commission));
                        tvTotPrice.setText("₹ " + formatted1);*/
                       /* String formatted6 = formatter1.format(Double.parseDouble(orderDetailsModel.restaurant_commission)
                                + Double.parseDouble(orderDetailsModel.tax_amount)
                                - Double.parseDouble(orderDetailsModel.restaurant_share_commission)
                                + Double.parseDouble(orderDetailsModel.cutlery_charge)
                                + Double.parseDouble(orderDetailsModel.packing_price));
                        if (formatted6.equals(".00")){
                            formatted6 = "0.00";
                        }
                       tvTotPrice.setText("₹ " + formatted6);*/
                        String formatted6 = formatter1.format(Double.parseDouble(orderDetailsModel.amount)
                                + Double.parseDouble(orderDetailsModel.tax_amount)
                                + Double.parseDouble(orderDetailsModel.cutlery_charge)
                                + Double.parseDouble(orderDetailsModel.packing_price)
                                - Double.parseDouble(orderDetailsModel.restaurant_share_commission));

                        if (formatted6.equals(".00")){
                            formatted6 = "0.00";
                        }
                        tvTotPrice.setText("₹ " + formatted6);
                        //holder.tvTotBill.setText("₹ " + formatted6);

                        callNo = orderDetailsModel.mobile;

                        ivPhn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //callNo = "6290130853";
                                showCallPopup(callNo);
                            }
                        });

                        imgCallRL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (orderDetailsModel.delivery_boy_id.equals("0")) {
                                    ivDelCall.setVisibility(View.GONE);
                                } else {
                                    ivDelCall.setVisibility(View.VISIBLE);
                                    showCallPopup(orderDetailsModel.boy.mobile);
                                }
                            }
                        });





                        /*if (orderDetailsModel.transaction_id.equals("test-cod")){
                            tvPayType.setText("Cash Payment");
                        }else {
                            tvPayType.setText("Online Payment");
                        }*/

                        // tvRestName.setText(orderDetailsModel.restaurant.name);
                        // tvRestAdr.setText(orderDetailsModel.restaurant.address);


                        if (orderItemList.size() > 0) {

                            orderItemAdapter = new OrderItemAdapter(OrderDetails.this, orderItemList);
                            pastOrderItemRv.setLayoutManager(new LinearLayoutManager(OrderDetails.this, LinearLayoutManager.VERTICAL, false));
                            pastOrderItemRv.setAdapter(orderItemAdapter);

                        }

                    } else {

                    }


                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });
    }

    private void showCallPopup(String callNo) {
        Dialog dialog = new Dialog(OrderDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.call_popup_lay);
       /* LayoutInflater layoutInflater = LayoutInflater.from(OrderDetails.this);
        View promptView = layoutInflater.inflate(R.layout.call_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(OrderDetails.this).create();*/
        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnReject = (Button) dialog.findViewById(R.id.btnCancel);

        tvMsg.setText("Are you sure to call " + callNo + " ?");

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + callNo));//change the number
                startActivity(callIntent);
                dialog.dismiss();

            }
        });


        /*alertD.setView(promptView);
        try {*/
        dialog.show();
       /* } catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/
    }

    private void takeExtraTime(ExtraTimeRequestModel extraTimeRequestModel) {

        dialogView.showCustomSpinProgress(OrderDetails.this);

        manager.service.extraTimeOrder(extraTimeRequestModel).enqueue(new Callback<ExtraTimeResponseModel>() {
            @Override
            public void onResponse(Call<ExtraTimeResponseModel> call, Response<ExtraTimeResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    ExtraTimeResponseModel extraTimeResponseModel = response.body();

                    if (!extraTimeResponseModel.error) {
                        btnExtraTime.setVisibility(View.GONE);
                        Toast.makeText(OrderDetails.this, "Order preparation time updated!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());

                    } else {
                        Toast.makeText(OrderDetails.this, extraTimeResponseModel.message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ExtraTimeResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }
}