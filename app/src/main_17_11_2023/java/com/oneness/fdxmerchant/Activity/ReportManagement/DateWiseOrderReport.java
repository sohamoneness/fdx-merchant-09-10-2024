package com.oneness.fdxmerchant.Activity.ReportManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.TablayoutAdapter.OrderReportTabLayoutAdapter;
import com.oneness.fdxmerchant.Utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateWiseOrderReport extends AppCompatActivity {

    /*TextView tvFrom, tvTo, tvOrderCount, tvTotalAmount;
    RecyclerView orderReportRv;
    Button btnSearch;

    List<DateWiseOrderReportModel> orderReportList = new ArrayList<>();
    OrderReportAdapter orderReportAdapter;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();*/

    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    ImageView iv_back;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_wise_order_report);

        prefs = new Prefs(DateWiseOrderReport.this);
        dialogView = new DialogView();

        iv_back = findViewById(R.id.iv_back);
        tabLayout = findViewById(R.id.orderTabLayout);
        viewPager = findViewById(R.id.orderViewPager);
        /*tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        tvOrderCount = findViewById(R.id.tvOrderCount);
        tvTotalAmount = findViewById(R.id.tvOrderAmount);

        orderReportRv = findViewById(R.id.orderReportRv);

        btnSearch = findViewById(R.id.btnSearch);*/

       // getCurrentDate();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Custom"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        OrderReportTabLayoutAdapter adapter=new OrderReportTabLayoutAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //viewPager.setCurrentItem(tab.getPosition());
                if(tabLayout.getSelectedTabPosition() == 0){
                    //Toast.makeText(getActivity(), "Tab 1" , Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(tab.getPosition());

                }else if(tabLayout.getSelectedTabPosition() == 1){
                    //Toast.makeText(getActivity(), "Tab 2" , Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(tab.getPosition());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                //viewPager.setCurrentItem(0);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

       /* tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(DateWiseOrderReport.this, date1, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


            }
        });

        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(DateWiseOrderReport.this, date2, myCalendar1
                        .get(android.icu.util.Calendar.YEAR), myCalendar1.get(android.icu.util.Calendar.MONTH),
                        myCalendar1.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel = new DateWiseOrderReportRequestModel(
                        tvFrom.getText().toString(),
                        tvTo.getText().toString(),
                        prefs.getData(Constants.REST_ID)
                );
                getOrderReport(dateWiseOrderReportRequestModel);

            }
        });*/


    }

  /*  final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

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
            updateLabel(tvTo);
        }

    };

    private void updateLabel(TextView editText) {
        //   String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));

    }

    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        tvFrom.setText(formattedDate);
        tvTo.setText(formattedDate);

        DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel = new DateWiseOrderReportRequestModel(
                tvFrom.getText().toString(),
                tvTo.getText().toString(),
                prefs.getData(Constants.REST_ID)
        );
        getOrderReport(dateWiseOrderReportRequestModel);
    }*/

    private void getOrderReport(DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel) {
        dialogView.showCustomSpinProgress(DateWiseOrderReport.this);
        manager.service.getDateWiseOrderReport(dateWiseOrderReportRequestModel).enqueue(new Callback<DateWiseOrderReportResponseModel>() {
            @Override
            public void onResponse(Call<DateWiseOrderReportResponseModel> call, Response<DateWiseOrderReportResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    DateWiseOrderReportResponseModel dworrm = response.body();
                    if (!dworrm.error){

                      /*  orderReportList = dworrm.orders;
                        tvOrderCount.setText(dworrm.total_order_count);
                        if (dworrm.total_order_amount != null){
                            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                            String formatted = formatter1.format(Double.parseDouble(dworrm.total_order_amount));

                            tvTotalAmount.setText("\u20B9" + formatted);

                        }else {
                            tvTotalAmount.setText("0.00");
                        }*/


                        /*if (orderReportList.size()>0){
                            orderReportAdapter = new OrderReportAdapter(DateWiseOrderReport.this, orderReportList);
                            orderReportRv.setLayoutManager(new LinearLayoutManager(DateWiseOrderReport.this, LinearLayoutManager.VERTICAL, false));
                            orderReportRv.setAdapter(orderReportAdapter);*/
                        //}

                    }else {

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<DateWiseOrderReportResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();

            }
        });
    }
}