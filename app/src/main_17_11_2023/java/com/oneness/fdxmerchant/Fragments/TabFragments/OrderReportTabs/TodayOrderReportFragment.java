package com.oneness.fdxmerchant.Fragments.TabFragments.OrderReportTabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.ReportManagement.DateWiseOrderReport;
import com.oneness.fdxmerchant.Adapters.OrderReportAdapter;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderItemModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.DashboardDataModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.RestaurantDashboardResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.Prefs;
import com.oneness.fdxmerchant.Utils.RecyclerItemClickListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayOrderReportFragment extends Fragment {

    RecyclerView orderReportRv;
    TextView tvToday, tvRevenue, tvTotOrder, tvCancelOrderCount, tvDeliverOrder, tvNoOrder;
    Prefs prefs;
    ApiManager manager = new ApiManager();
    List<DateWiseOrderReportModel> orderReportList = new ArrayList<>();
    List<DateWiseOrderItemModel> orderItemList = new ArrayList<>();
    OrderReportAdapter orderReportAdapter;
    Button btnCSV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.today_order_report_fragment, container, false);

        prefs = new Prefs(getActivity());

        orderReportRv = v.findViewById(R.id.orderReportRv);
        tvToday = v.findViewById(R.id.tvToday);
        tvRevenue = v.findViewById(R.id.tvRevenue);
        tvTotOrder = v.findViewById(R.id.tvTotOrder);
        tvCancelOrderCount = v.findViewById(R.id.tvCancelOrderCount);
        tvDeliverOrder = v.findViewById(R.id.tvDeliverOrder);
        tvNoOrder = v.findViewById(R.id.tvNoOrder);
        btnCSV = v.findViewById(R.id.btnCSVDownload);

        //getDashboardData();
        getCurrentDate();

        return v;
    }

    private void getDashboardData() {
        //dialogView.showCustomSpinProgress(getActivity());
        manager.service.getDashboardData(prefs.getData(Constants.REST_ID)).enqueue(new Callback<RestaurantDashboardResponseModel>() {
            @Override
            public void onResponse(Call<RestaurantDashboardResponseModel> call, Response<RestaurantDashboardResponseModel> response) {
                if (response.isSuccessful()){
                    //dialogView.dismissCustomSpinProgress();
                    RestaurantDashboardResponseModel rdrm = response.body();
                    if (!rdrm.error){
                        DashboardDataModel dashboardDataModel = rdrm.data;
                        tvDeliverOrder.setText(dashboardDataModel.delivered_order_count);
                        tvRevenue.setText("\u20B9 " + dashboardDataModel.todays_restaurant_commission);
                        tvTotOrder.setText(dashboardDataModel.today_order_count);
                        //tvDeliveredOrder.setText(dashboardDataModel.delivered_order_count);
                        // tvCancelledOrder.setText(dashboardDataModel.cancelled_order_count);
                        //tvTodayOrderAmount.setText(dashboardDataModel.todays_order_amount);
                        //tvTodayCommission.setText(dashboardDataModel.todays_restaurant_commission);
                        tvCancelOrderCount.setText(dashboardDataModel.cancelled_order_count);


                    }else{

                    }
                }else{
                    //dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RestaurantDashboardResponseModel> call, Throwable t) {
                //dialogView.dismissCustomSpinProgress();

            }
        });
    }

    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String formattedDate1 = df1.format(c);
        tvToday.setText(formattedDate1);
        /*tvTo.setText(formattedDate);*/

        DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel = new DateWiseOrderReportRequestModel(
                formattedDate,
                formattedDate,
                prefs.getData(Constants.REST_ID)
        );
        getOrderReport(dateWiseOrderReportRequestModel);
    }

    private void getOrderReport(DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel) {
        //dialogView.showCustomSpinProgress(DateWiseOrderReport.this);
        manager.service.getDateWiseOrderReport(dateWiseOrderReportRequestModel).enqueue(new Callback<DateWiseOrderReportResponseModel>() {
            @Override
            public void onResponse(Call<DateWiseOrderReportResponseModel> call, Response<DateWiseOrderReportResponseModel> response) {
                if (response.isSuccessful()){
                    //dialogView.dismissCustomSpinProgress();
                    DateWiseOrderReportResponseModel dworrm = response.body();
                    if (!dworrm.error){
                        orderReportList = dworrm.orders;

                        /*if (!dworrm.orders.isEmpty()){
                            orderReportList = dworrm.orders;
                            tvNoOrder.setVisibility(View.GONE);
                            orderReportRv.setVisibility(View.VISIBLE);
                        }else {
                            tvNoOrder.setVisibility(View.VISIBLE);
                            orderReportRv.setVisibility(View.GONE);
                        }*/



                       /* //tvOrderCount.setText(dworrm.total_order_count);
                        if (dworrm.total_order_amount != null){
                            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                            String formatted = formatter1.format(Double.parseDouble(dworrm.total_order_amount));

                           // tvTotalAmount.setText("\u20B9" + formatted);

                        }else {
                           // tvTotalAmount.setText("0.00");
                        }*/
                        if(dworrm.total_order_amount != null){
                            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                            String formatted = formatter1.format(Double.parseDouble(dworrm.total_order_amount));
                            if (formatted.equals(".00")){
                                formatted = "0.00";
                            }
                            tvRevenue.setText("\u20B9 " + formatted);
                        }else {
                            tvRevenue.setText("\u20B9 " + "0.00");
                        }



                        tvTotOrder.setText(dworrm.total_order_count);
                        //tvNoOrder.setText(dworrm.total_order_count);
                        tvDeliverOrder.setText(dworrm.delivered_order_count);
                        tvCancelOrderCount.setText(dworrm.cancelled_order_count);



                        if (orderReportList.size()>0){
                            tvNoOrder.setVisibility(View.GONE);
                            orderReportRv.setVisibility(View.VISIBLE);
                            btnCSV.setVisibility(View.VISIBLE);
                            orderReportAdapter = new OrderReportAdapter(getActivity(), orderReportList);
                            orderReportRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            orderReportRv.setAdapter(orderReportAdapter);
                        }else {
                            tvNoOrder.setVisibility(View.VISIBLE);
                            orderReportRv.setVisibility(View.GONE);
                            btnCSV.setVisibility(View.GONE);
                        }

                    }else {

                    }
                }else {
                    //dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<DateWiseOrderReportResponseModel> call, Throwable t) {
               // dialogView.dismissCustomSpinProgress();

            }
        });
    }
}
