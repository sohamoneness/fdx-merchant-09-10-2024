package com.oneness.fdxmerchant.Fragments.TabFragments.OrderReportTabs;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.ReportManagement.DateWiseOrderReport;
import com.oneness.fdxmerchant.Adapters.OrderReportAdapter;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.DashboardDataModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.RestaurantDashboardResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomOrderReportFragment extends Fragment {

    TextView tvFrom, tvTo, tvNoOrder;
    RecyclerView orderReportRv;
    RelativeLayout btnSearch;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    Prefs prefs;
    ApiManager manager = new ApiManager();

    TextView tvRevenue, tvTotOrder, tvCancelOrderCount, tvDeliverOrder;

    OrderReportAdapter orderReportAdapter;
    List<DateWiseOrderReportModel> orderReportList = new ArrayList<>();
    Button btnCSV;
    String fromDate = "", toDate = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_order_report_fragment, container, false);

        prefs = new Prefs(getActivity());

        tvFrom = v.findViewById(R.id.tvFrom);
        tvTo = v.findViewById(R.id.tvTo);
        btnSearch = v.findViewById(R.id.btnSearch);
        orderReportRv = v.findViewById(R.id.orderReportRv);
        tvRevenue = v.findViewById(R.id.tvRevenue);
        tvTotOrder = v.findViewById(R.id.tvTotOrder);
        tvCancelOrderCount = v.findViewById(R.id.tvCancelOrderCount);
        tvDeliverOrder = v.findViewById(R.id.tvDeliverOrder);
        tvNoOrder = v.findViewById(R.id.tvNoOrder);
        btnCSV = v.findViewById(R.id.btnCSVDownload);

        getCurrentDate();

        //getDashboardData();



        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();


            }
        });

        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date2, myCalendar1
                        .get(android.icu.util.Calendar.YEAR), myCalendar1.get(android.icu.util.Calendar.MONTH),
                        myCalendar1.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DATES>>", fromDate + ", " + toDate);
                DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel = new DateWiseOrderReportRequestModel(
                        fromDate,
                        toDate,
                        prefs.getData(Constants.REST_ID)
                );
                getOrderReport(dateWiseOrderReportRequestModel);

            }
        });


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

    private void getOrderReport(DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel) {
        //dialogView.showCustomSpinProgress(DateWiseOrderReport.this);
        manager.service.getDateWiseOrderReport(dateWiseOrderReportRequestModel).enqueue(new Callback<DateWiseOrderReportResponseModel>() {
            @Override
            public void onResponse(Call<DateWiseOrderReportResponseModel> call, Response<DateWiseOrderReportResponseModel> response) {
                if (response.isSuccessful()){
                    //dialogView.dismissCustomSpinProgress();
                    DateWiseOrderReportResponseModel dworrm = response.body();
                    if (!dworrm.error){
                        orderReportList.clear();

                        orderReportList = dworrm.orders;
                        //Toast.makeText(getActivity(), "Data Fetched!!", Toast.LENGTH_SHORT).show();
                        /*if (!dworrm.orders.isEmpty()){
                            orderReportList = dworrm.orders;
                            tvNoOrder.setVisibility(View.GONE);
                            orderReportRv.setVisibility(View.VISIBLE);
                        }else {
                            tvNoOrder.setVisibility(View.VISIBLE);
                            orderReportRv.setVisibility(View.GONE);
                        }*/

                      /*  orderReportList = dworrm.orders;
                        tvOrderCount.setText(dworrm.total_order_count);
                        if (dworrm.total_order_amount != null){
                            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                            String formatted = formatter1.format(Double.parseDouble(dworrm.total_order_amount));

                            tvTotalAmount.setText("\u20B9" + formatted);

                        }else {
                            tvTotalAmount.setText("0.00");
                        }*/
                        if (dworrm.total_order_amount != null){
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
                       // Toast.makeText(getActivity(), "ERROR:TRUE", Toast.LENGTH_SHORT).show();

                    }
                }else {
                   // dialogView.dismissCustomSpinProgress();
                   // Toast.makeText(getActivity(), "SERVER NO Data Fetched!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DateWiseOrderReportResponseModel> call, Throwable t) {
                //dialogView.dismissCustomSpinProgress();
                //Toast.makeText(getActivity(), "NO Data Fetched!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        Calendar day1 = Calendar.getInstance();
        day1.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String formattedDate1 = df.format(day1.getTime());
        String formattedDateForShow = df1.format(c);
        String formattedDateForShow1 = df1.format(day1.getTime());
        tvFrom.setText(formattedDateForShow1);
        tvTo.setText(formattedDateForShow);
        fromDate = formattedDate1;
        toDate = formattedDate;

        DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel = new DateWiseOrderReportRequestModel(
                fromDate,
                toDate,
                prefs.getData(Constants.REST_ID)
        );
        getOrderReport(dateWiseOrderReportRequestModel);
    }

    final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(android.icu.util.Calendar.YEAR, year);
            myCalendar.set(android.icu.util.Calendar.MONTH, monthOfYear);
            myCalendar.set(android.icu.util.Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(tvFrom);
        }

    };

    final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar1.set(android.icu.util.Calendar.YEAR, year);
            myCalendar1.set(android.icu.util.Calendar.MONTH, monthOfYear);
            myCalendar1.set(android.icu.util.Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1(tvTo);
        }

    };

    private void updateLabel(TextView editText) {
        //   String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat1 = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        fromDate = sdf.format(myCalendar.getTime());

        editText.setText(sdf1.format(myCalendar.getTime()));


    }

    private void updateLabel1(TextView editText) {
        //   String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat1 = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        toDate = sdf.format(myCalendar1.getTime());

        editText.setText(sdf1.format(myCalendar1.getTime()));

    }


}
