package com.oneness.fdxmerchant.Fragments.ParentFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Adapters.OngoingOrderAdapter;
import com.oneness.fdxmerchant.Adapters.TodayOrderAdapter;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrdersModel;
import com.oneness.fdxmerchant.Models.OrderModels.OngoingOrderModel;
import com.oneness.fdxmerchant.Models.OrderModels.OngoingOrderResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.DashboardDataModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.RestaurantDashboardResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.TodayOrderModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView orderRv, ongoingOrderRv;
    List<TodayOrderModel> todayOrderList = new ArrayList<>();
    List<NewOrdersModel> newOrderList = new ArrayList<>();
    List<OngoingOrderModel> ongoingOrderList = new ArrayList<>();
    TodayOrderAdapter todayAdapter;
    OngoingOrderAdapter ongoingAdapter;

    TextView tvRestNameHeader, tvTodayOrderCount, tvTodayCommission, tvTodayOrderAmount, tvRestAdr;
    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    TextView tvOngoingOrder, tvRevenue, tvTotOrder;
    int orderListSize = 0;
    TextView noOrderNew, noOrderOngoing;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);

        prefs = new Prefs(getActivity());
        dialogView = new DialogView();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NotificationManager.class);
            createNotificationChannel(notificationManager);
        }
        // If you are writting code in fragment

        orderRv = v.findViewById(R.id.orderRv);
        ongoingOrderRv = v.findViewById(R.id.ongoingOrderRv);
        tvRestNameHeader = v.findViewById(R.id.tvRestNameHeader);
        tvRestAdr = v.findViewById(R.id.tvRestAdr);
        //tvNewOrder = v.findViewById(R.id.tvNewOrder);
        tvOngoingOrder = v.findViewById(R.id.tvOngoingOrder);
        tvRevenue = v.findViewById(R.id.tvRevenue);
        tvTotOrder = v.findViewById(R.id.tvTotOrder);
        //tvDeliveredOrder = v.findViewById(R.id.tvDeliveredOrder);
        // tvCancelledOrder = v.findViewById(R.id.tvCancelledOrder);
        tvTodayOrderCount = v.findViewById(R.id.tvTodayOrderCount);
        noOrderNew = v.findViewById(R.id.noOrderNew);
        noOrderOngoing = v.findViewById(R.id.noOrderOngoing);
        //  tvTodayCommission = v.findViewById(R.id.tvTodayCommission);
        //tvTodayOrderAmount = v.findViewById(R.id.tvTodayOrderAmount);

        tvRestNameHeader.setText(prefs.getData(Constants.REST_NAME));

        if (Constants.orderSize == 0) {
            if (prefs.getData(Constants.ORDER_SIZE) != null) {
                if (!prefs.getData(Constants.ORDER_SIZE).equals("")) {
                    String oSize = prefs.getData(Constants.ORDER_SIZE);
                    //int x= Integer.valueOf(oSize);
                    int x = Integer.parseInt(oSize);
                    Constants.orderSize = x;
                }

            }
        }

        //getOrders();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //
                try {
                    getDashboardData();
                    //Toast.makeText(getActivity(), "TESTING....", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }


                handler.postDelayed(this, 8000);
            }
        };
        runnable.run();



        /*todayAdapter = new TodayOrderAdapter(getActivity(), todayOrderList);
        orderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        orderRv.setAdapter(todayAdapter);*/

        return v;
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActivity().getPackageName() + "/" + R.raw.tone_test2); //Here is FILE_NAME is the name of file that you want to play
// Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library if

        CharSequence name = "Echannel";
        String description = "testing";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("CHANNEL_1", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(sound, audioAttributes);
            notificationManager.createNotificationChannel(channel);
        }


    }

    private void getDashboardData() {
        //dialogView.showCustomSpinProgress(getActivity());
        manager.service.getDashboardData(prefs.getData(Constants.REST_ID)).enqueue(new Callback<RestaurantDashboardResponseModel>() {
            @Override
            public void onResponse(Call<RestaurantDashboardResponseModel> call, Response<RestaurantDashboardResponseModel> response) {
                if (response.isSuccessful()) {
                    //dialogView.dismissCustomSpinProgress();
                    RestaurantDashboardResponseModel rdrm = response.body();
                    if (!rdrm.error) {
                        DashboardDataModel dashboardDataModel = rdrm.data;

                        todayOrderList = dashboardDataModel.todays_orders;
                        //tvNewOrder.setText(dashboardDataModel.new_order_count);
                        tvOngoingOrder.setText(dashboardDataModel.ongoing_order_count);
                        Constants.revenueToday = dashboardDataModel.todays_order_amount;
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                        String formatted = formatter1.format(Double.parseDouble(dashboardDataModel.todays_order_amount));
                        if (formatted.equals(".00")) {
                            formatted = "0.00";
                        }

                        tvRevenue.setText("\u20B9 " + formatted);
                        tvTotOrder.setText(dashboardDataModel.new_order_count);
                        tvRestAdr.setText(prefs.getData(Constants.REST_ADR));
                        //tvDeliveredOrder.setText(dashboardDataModel.delivered_order_count);
                        // tvCancelledOrder.setText(dashboardDataModel.cancelled_order_count);
                        //tvTodayOrderAmount.setText(dashboardDataModel.todays_order_amount);
                        //tvTodayCommission.setText(dashboardDataModel.todays_restaurant_commission);
                        tvTodayOrderCount.setText(dashboardDataModel.today_order_count);

                        getNewOrders(prefs.getData(Constants.REST_ID));

                        Log.d("REST_ID>>", prefs.getData(Constants.REST_ID));

                        //orderListSize = todayOrderList.size();
                        // if (orderListSize > Constants.orderSize){
                        //Constants.orderSize = orderListSize;
                        // showNewOrderAlert(orderListSize);
                        //  }


                       /* if (todayOrderList.size()>0){
                            todayAdapter = new TodayOrderAdapter(getActivity(), todayOrderList);
                            orderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            orderRv.setAdapter(todayAdapter);

                        }*/

                    } else {

                    }
                } else {
                    //dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RestaurantDashboardResponseModel> call, Throwable t) {
                //dialogView.dismissCustomSpinProgress();

            }
        });
    }

    private void getNewOrders(String data) {
        manager.service.getNewOrders(data).enqueue(new Callback<NewOrderResponseModel>() {
            @Override
            public void onResponse(Call<NewOrderResponseModel> call, Response<NewOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    NewOrderResponseModel norm = response.body();
                    if (!norm.error) {
                        newOrderList = norm.orders;

                        orderListSize = newOrderList.size();
                        if (orderListSize > Constants.orderSize) {
                            Constants.orderSize = orderListSize;
                            showNewOrderAlert(orderListSize);
                        }

                        getOngoingOrders(data);

                        if (newOrderList.size() > 0) {
                            orderRv.setVisibility(View.VISIBLE);
                            noOrderNew.setVisibility(View.GONE);
                            todayAdapter = new TodayOrderAdapter(getActivity(), newOrderList);
                            orderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            orderRv.setAdapter(todayAdapter);

                        } else {
                            orderRv.setVisibility(View.GONE);
                            noOrderNew.setVisibility(View.VISIBLE);
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<NewOrderResponseModel> call, Throwable t) {

            }
        });
    }

    private void getOngoingOrders(String data) {

        //dialogView.showCustomSpinProgress(getActivity());

        manager.service.getOngoingOrders(data).enqueue(new Callback<OngoingOrderResponseModel>() {
            @Override
            public void onResponse(Call<OngoingOrderResponseModel> call, Response<OngoingOrderResponseModel> response) {

                if (response.isSuccessful()) {
                    //dialogView.dismissCustomSpinProgress();

                    OngoingOrderResponseModel norm = response.body();

                    if (!norm.error) {
                        ongoingOrderList = norm.orders;

                        if (ongoingOrderList.size() > 0) {
                            ongoingOrderRv.setVisibility(View.VISIBLE);
                            noOrderOngoing.setVisibility(View.GONE);
                            ongoingAdapter = new OngoingOrderAdapter(getActivity(), ongoingOrderList);
                            ongoingOrderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            ongoingOrderRv.setAdapter(ongoingAdapter);

                        } else {
                            ongoingOrderRv.setVisibility(View.GONE);
                            noOrderOngoing.setVisibility(View.VISIBLE);
                        }

                    } else {

                    }

                } else {
                    // dialogView.dismissCustomSpinProgress();
                }

            }

            @Override
            public void onFailure(Call<OngoingOrderResponseModel> call, Throwable t) {

                //dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void showNewOrderAlert(int orderListSize) {
        if (getActivity() != null) {
            Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.inflate_custom_alert_dialog);
        /*LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);
        //Constants.isDialogOn = 1;

        final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();*/
            TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
            tvHeader.setText(getResources().getString(R.string.app_name));

            TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
            tvMsg.setText(prefs.getData(Constants.REST_NAME) + ", new order received!");
            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
            btnCancel.setVisibility(View.GONE);
            btnCancel.setText("Cancel");
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
            btnOk.setBackground(getResources().getDrawable(R.drawable.side_round_btn_orng_bg));
            btnOk.setText("Ok");

            MediaPlayer music = MediaPlayer.create(getActivity(), R.raw.tone_test);
            music.start();

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    prefs.saveData(Constants.ORDER_SIZE, String.valueOf(orderListSize));
                    music.stop();

                    Constants.orderSize = orderListSize;
                    //Constants.isDialogOn = 0;
                    dialog.dismiss();

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Constants.isDialogOn = 0;
                    music.stop();
                    dialog.dismiss();


                }
            });

       /* alertD.setView(promptView);
        try {*/
            dialog.show();
       /* }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/

        }

    }
}
